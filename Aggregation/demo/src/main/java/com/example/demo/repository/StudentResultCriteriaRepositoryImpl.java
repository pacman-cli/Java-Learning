package com.example.demo.repository;

import com.example.demo.dto.SubjectAverageDTO;
import com.example.demo.entity.StudentResult;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentResultCriteriaRepositoryImpl implements StudentResultCriteriaRepository {
    //When DO you need EntityManager?
    //You SHOULD use EntityManager when you need:
    //🔹 Complex queries
    //  -Dynamic filters
    //  -Conditional queries
    //  -Complex joins
    //  -Runtime-built queries
    //🔹 Aggregation & reporting (important for dashboards)
    //  -COUNT, SUM, AVG
    //  -GROUP BY
    //  -Dynamic grouping
    //🔹 Criteria API
    //CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    //CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
    //🔹 Batch operations
    //Bulk update
    //Bulk delete
    //Performance tuning

    //a temporary memory area that manages the lifecycle and state of entity objects during a transaction
    @PersistenceContext //spring injects a thread safe proxy
            EntityManager entityManager;

    //CriteriaBuilder → builds query
    //CriteriaQuery → type-safe query
    //cb.sum() → aggregation
    //groupBy() → SQL GROUP BY
    //cb.construct() → maps directly to DTO
    @Override
    public Double findAverageMarks() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder(); //This is used to construct queries programmatically

        // Create a CriteriaQuery that will return a Double value
        // AVG() function always returns a decimal number
        CriteriaQuery<Double> query = cb.createQuery(Double.class);

        Root<StudentResult> root = query.from(StudentResult.class);  // StudentResult represents the table/entity being queried

        // Select the average of the "marks" field using AVG aggregation
        // root.get("marks") refers to the marks property in StudentResult entity
        query.select(cb.avg(root.get("marks")));

        // Create and execute the query
        // getSingleResult() is used because AVG returns a single value
        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public Integer findMaxMarks() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
        Root<StudentResult> root = query.from(StudentResult.class);
        query.select(cb.max(root.get("marks")));
        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public List<SubjectAverageDTO> subjectWiseAverage() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SubjectAverageDTO> query = cb.createQuery(SubjectAverageDTO.class); //return type
        Root<StudentResult> root = query.from(StudentResult.class); //do query from this class
        query.select(
                cb.construct(
                        SubjectAverageDTO.class,
                        root.get("subject"),
                        cb.avg(root.get("marks"))
                )
        );

        query.groupBy(root.get("subject"));
        return entityManager.createQuery(query).getResultList();
    }
}
