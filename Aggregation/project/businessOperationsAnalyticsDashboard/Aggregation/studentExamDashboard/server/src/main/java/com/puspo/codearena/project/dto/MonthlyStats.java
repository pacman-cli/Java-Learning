package com.puspo.codearena.project.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyStats {
    private String month;
    private Long totalExams;
    private Double averageMarks;
    private Double passRate;
}
