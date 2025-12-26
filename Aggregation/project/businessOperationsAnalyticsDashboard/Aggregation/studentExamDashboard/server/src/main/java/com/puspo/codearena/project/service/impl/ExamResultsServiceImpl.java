package com.puspo.codearena.project.service.impl;

import com.puspo.codearena.project.dto.CategoryWiseStats;
import com.puspo.codearena.project.dto.DashboardSummary;
import com.puspo.codearena.project.dto.ExamResultDto;
import com.puspo.codearena.project.repository.ExamResultRepository;
import com.puspo.codearena.project.service.ExamResultsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ExamResultsServiceImpl implements ExamResultsService {
    private final ExamResultRepository examResultRepository;

    @Override
    public DashboardSummary getDashboardSummary() {
        log.info("Fetching dashboard summary");
        return examResultRepository.getDashboardSummary();
    }

    @Override
    public List<CategoryWiseStats> categoryWiseStats() {
        log.info("Fetching category wise stats");
        return examResultRepository.getCategoryWiseStats();
    }

    @Override
    public List<ExamResultDto> getDetailedResutls() {
        log.info("Fetching detailed results");
        return examResultRepository.getDetailedExamResults();
    }
}
