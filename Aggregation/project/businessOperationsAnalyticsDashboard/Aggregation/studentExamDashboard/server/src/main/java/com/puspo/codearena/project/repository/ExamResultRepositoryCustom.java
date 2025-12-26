package com.puspo.codearena.project.repository;

import com.puspo.codearena.project.dto.CategoryWiseStats;
import com.puspo.codearena.project.dto.DashboardSummary;
import com.puspo.codearena.project.dto.ExamResultDto;
import com.puspo.codearena.project.dto.MonthlyStats;

import java.util.List;

public interface ExamResultRepositoryCustom {
    DashboardSummary getDashboardSummary();

    List<CategoryWiseStats> getCategoryWiseStats();

    List<MonthlyStats> getMonthlyStats();

    List<ExamResultDto> getDetailedExamResults();

}
