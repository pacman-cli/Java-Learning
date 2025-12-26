package com.puspo.codearena.project.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardSummary {
    private Long totalStudents;
    private Long totalExams;
    private Double averageMarks;
    private Double passPercentage;
    private Long totalPassedExams;
    private Long totalFailedExams;
}
