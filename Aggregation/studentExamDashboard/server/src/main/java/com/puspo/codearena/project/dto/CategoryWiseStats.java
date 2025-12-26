package com.puspo.codearena.project.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryWiseStats {
    private String subjectNames;
    private Long totalExams;
    private Double averageMarks;
    private Double passRate;
}
