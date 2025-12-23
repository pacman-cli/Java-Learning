package com.puspo.codearena.project.repository;

import com.puspo.codearena.project.dto.CategoryWiseStats;
import com.puspo.codearena.project.dto.DashboardSummary;
import com.puspo.codearena.project.dto.ExamResultDto;
import com.puspo.codearena.project.dto.MonthlyStats;
import com.puspo.codearena.project.entity.ExamResult;
import com.puspo.codearena.project.entity.Status;
import com.puspo.codearena.project.entity.Student;
import com.puspo.codearena.project.entity.Subject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

import java.math.RoundingMode;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ExamResultRepositoryImpl implements ExamResultRepositoryCustom {
        private final EntityManager entityManager;

        @Override
        public DashboardSummary getDashboardSummary() {
                CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                CriteriaQuery<DashboardSummary> query = cb.createQuery(DashboardSummary.class);
                // Root<ExamResult> root = query.from(ExamResult.class);

                // 👉Subqueries from different matrics
                // 👉 countDistinct-> How many unique values exists in a column
                Subquery<Long> uniqueSubquery = query.subquery(Long.class);
                Root<ExamResult> uniqueSubqueryRoot = uniqueSubquery.from(ExamResult.class);
                uniqueSubquery.select(cb.countDistinct(uniqueSubqueryRoot.get("student").get("id")));

                // 👉Counts how many exam records exist --> Total exams
                Subquery<Long> examRecQuery = query.subquery(Long.class);
                Root<ExamResult> examRecQueryRoot = examRecQuery.from(ExamResult.class);
                examRecQuery.select(cb.count(examRecQueryRoot.get("id")));

                // The average marks of all exams 👉 Calculates the average of all marksObtained
                Subquery<Double> avgMarksObQuery = query.subquery(Double.class);
                Root<ExamResult> avgMarksObQueryRoot = avgMarksObQuery.from(ExamResult.class);
                avgMarksObQuery.select(cb.avg(avgMarksObQueryRoot.get("marksObtained")));

                // 👉 Counts only exams that are PASSED
                Subquery<Long> examPassedQuery = query.subquery(Long.class);
                Root<ExamResult> examPassedQueryRoot = examPassedQuery.from(ExamResult.class);
                examPassedQuery.select(cb.count(examPassedQueryRoot.get("id")))
                                .where(
                                                cb.equal(
                                                                examPassedQueryRoot.get("status"),
                                                                Status.PASSED));
                // 👉 Counts only exams that are FAILED
                Subquery<Long> examFailedQuery = query.subquery(Long.class);
                Root<ExamResult> examFailedQueryRoot = examFailedQuery.from(ExamResult.class);
                examFailedQuery.select(cb.count(examFailedQueryRoot.get("id")))
                                .where(
                                                cb.equal(
                                                                examFailedQueryRoot.get("status"),
                                                                Status.FAILED));

                // Pass percentage = (passed / total) * 100; guarded for total = 0
                Expression<Long> totalExamsNonZero = cb.nullif(examRecQuery, 0L);
                Expression<Number> passPercentageExpr = cb.prod(
                                cb.quot(cb.toDouble(examPassedQuery), cb.toDouble(totalExamsNonZero)),
                                100d);

                // Build the main query now
                query.select(
                                cb.construct(
                                                DashboardSummary.class,
                                                uniqueSubquery,
                                                examRecQuery,
                                                avgMarksObQuery,
                                                passPercentageExpr,
                                                examPassedQuery,
                                                examFailedQuery));

                // Execute and process results
                // TypedQuery -> execute a query and get a type-safe result.
                TypedQuery<DashboardSummary> typedQuery = entityManager.createQuery(query);
                DashboardSummary result = typedQuery.getSingleResult();

                // Calculate pass percentage
                Double roundedPassPct = null;
                if (result.getPassPercentage() != null && !result.getPassPercentage().isNaN()
                                && !result.getPassPercentage().isInfinite()) {
                        roundedPassPct = BigDecimal.valueOf(result.getPassPercentage())
                                        .setScale(2, RoundingMode.HALF_UP) // Rounding BigDecimal → always use
                                                                           // setScale(2, RoundingMode.HALF_UP) for
                                                                           // percentages
                                        .doubleValue();
                }

                return DashboardSummary.builder()
                                .totalStudents(result.getTotalStudents())
                                .totalExams(result.getTotalExams())
                                .totalFailedExams(result.getTotalFailedExams())
                                .totalPassedExams(result.getTotalPassedExams())
                                .averageMarks(result.getAverageMarks() != null ? result.getAverageMarks() : 0.0)
                                .passPercentage(roundedPassPct != null ? roundedPassPct : 0.0)
                                .build();
        }

        @Override
        public List<CategoryWiseStats> getCategoryWiseStats() {
                CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                CriteriaQuery<CategoryWiseStats> query = cb.createQuery(CategoryWiseStats.class);

                Root<ExamResult> root = query.from(ExamResult.class);
                Join<ExamResult, Subject> subjectJoin = root.join("subject", JoinType.LEFT);

                // Expressions (clean & reusable)
                Expression<Long> totalExams = cb.count(root.get("id"));

                Expression<Long> passedExams = cb.sum(
                                cb.<Long>selectCase()
                                                .when(cb.equal(root.get("status"), Status.PASSED), 1L)
                                                .otherwise(0L));

                Expression<Double> passRate = cb.function(
                                "ROUND",
                                Double.class,
                                cb.prod(
                                                cb.quot(
                                                                cb.toDouble(passedExams),
                                                                cb.nullif(totalExams, 0L) // prevents divide-by-zero
                                                ),
                                                100.0));

                // DTO constructor mapping (IMPORTANT)
                query.select(
                                cb.construct(
                                                CategoryWiseStats.class,
                                                subjectJoin.get("name"),
                                                totalExams,
                                                cb.avg(root.get("marksObtained")),
                                                passRate));

                // Proper GROUP BY
                query.groupBy(
                                subjectJoin.get("id"),
                                subjectJoin.get("name"));

                // ORDER BY
                query.orderBy(cb.asc(subjectJoin.get("name")));

                return entityManager.createQuery(query).getResultList();
        }
        // Breakdown:
        // cb.quot(a, b) → a / b
        // cb.prod(a, b) → a * b
        // cb.nullif(totalExams, 0L) → prevents crash
        // cb.function("ROUND") → SQL ROUND()

        @Override
        public List<MonthlyStats> getMonthlyStats() {
                CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                CriteriaQuery<MonthlyStats> query = cb.createQuery((MonthlyStats.class));
                Root<ExamResult> root = query.from(ExamResult.class);
                // ---- Extract YEAR-MONTH (MySQL specific) ----
                Expression<String> yearMonth = cb.function(
                                "DATE_FORMAT",
                                String.class,
                                root.get("examDate"),
                                cb.literal("%Y-%m"));

                // -----Aggregation Expressions--------
                // total exams
                Expression<Long> totalExams = cb.count(root.get("id"));

                // passed exams
                Expression<Long> passedExams = cb.sum(
                                cb.<Long>selectCase()
                                                .when(
                                                                cb.equal(
                                                                                root.get("status"), Status.PASSED),
                                                                1L)
                                                .otherwise(0L));
                // average marks
                Expression<Double> avgMarks = cb.avg(root.get("marksObtained"));
                // pass rate
                Expression<Double> passRate = cb.function(
                                "ROUND",
                                Double.class,
                                cb.prod(
                                                cb.quot(
                                                                cb.toDouble(passedExams),
                                                                cb.nullif(totalExams, 0L)// prevents divide-by-zero
                                                ), 100.0 // *100
                                ));

                // ---- SELECT (DTO constructor mapping) ----
                query.select(
                                cb.construct(
                                                MonthlyStats.class,
                                                yearMonth,
                                                totalExams,
                                                avgMarks,
                                                passRate));
                // ---- GROUP & ORDER ----
                query.groupBy(yearMonth);
                query.orderBy(cb.asc(yearMonth));

                return entityManager.createQuery(query).getResultList();
        }

        @Override
        public List<ExamResultDto> getDetailedExamResults() {
                CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                CriteriaQuery<ExamResultDto> query = cb.createQuery(ExamResultDto.class);
                Root<ExamResult> root = query.from(ExamResult.class);
                // ExamResult Left Join with both Student and Subject
                Join<ExamResult, Subject> subjectJoin = root.join("subject", JoinType.LEFT);
                Join<ExamResult, Student> studentJoin = root.join("student", JoinType.LEFT);

                query.select(
                                cb.construct(
                                                ExamResultDto.class,
                                                root.get("id"),
                                                cb.concat(
                                                                studentJoin.get("firstName"),
                                                                cb.concat(
                                                                                "",
                                                                                studentJoin.get("lastName"))),
                                                subjectJoin.get("name"),
                                                root.get("marksObtained"),
                                                root.get("totalMarks"),
                                                cb.function("ROUND",
                                                                Double.class,
                                                                cb.prod(
                                                                                cb.quot(
                                                                                                cb.toDouble(root.get(
                                                                                                                "marksObtained")),
                                                                                                cb.toDouble(cb.nullif(
                                                                                                                root.get("totalMarks"),
                                                                                                                0))),
                                                                                100.0 // ROUND( (CAST(marks_obtained AS
                                                                                      // DOUBLE) /
                                                                                      // CAST(NULLIF(total_marks, 0) AS
                                                                                      // DOUBLE))* 100)
                                                                )),
                                                root.get("examDate"),
                                                root.get("status")));

                // Order by
                query.orderBy(
                                cb.desc(
                                                root.get("examDate")));
                return entityManager.createQuery(query).getResultList();
        }
}
