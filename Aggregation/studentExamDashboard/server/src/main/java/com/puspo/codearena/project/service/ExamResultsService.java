package com.puspo.codearena.project.service;

import com.puspo.codearena.project.dto.CategoryWiseStats;
import com.puspo.codearena.project.dto.DashboardSummary;
import com.puspo.codearena.project.dto.ExamResultDto;

import java.util.List;

public interface ExamResultsService {
    DashboardSummary getDashboardSummary();

    List<CategoryWiseStats> categoryWiseStats();

    List<ExamResultDto> getDetailedResutls();
}
