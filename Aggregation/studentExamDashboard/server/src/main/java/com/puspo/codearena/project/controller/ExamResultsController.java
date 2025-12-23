package com.puspo.codearena.project.controller;

import com.puspo.codearena.project.dto.CategoryWiseStats;
import com.puspo.codearena.project.dto.DashboardSummary;
import com.puspo.codearena.project.dto.ExamResultDto;
import com.puspo.codearena.project.service.ExamResultsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
public class ExamResultsController {
    private final ExamResultsService examResultsService;

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummary> getDashboardSummary() {
        DashboardSummary dashboardSummary = examResultsService.getDashboardSummary();
        return ResponseEntity.ok(dashboardSummary);
    }

    @GetMapping("/category-wise-stats")
    public ResponseEntity<List<CategoryWiseStats>> getCategoryWiseStats() {
        List<CategoryWiseStats> categoryWiseStatsList = examResultsService.categoryWiseStats();
        return ResponseEntity.ok(categoryWiseStatsList);
    }

    @GetMapping("/detailedExamResults")
    public ResponseEntity<List<ExamResultDto>> getDetailedExamResults() {
        List<ExamResultDto> examResultDtoList = examResultsService.getDetailedResutls();
        return ResponseEntity.ok(examResultDtoList);
    }
}
