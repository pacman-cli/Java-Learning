package com.puspo.codearena.project.dto;

import com.puspo.codearena.project.entity.Status;
import lombok.*;

import java.sql.Date;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamResultDto {
    private Long id;
    private String studentName;
    private String subjectName;
    private Integer marksObtained;
    private Integer totalMarks;
    private Double percentage;
    private LocalDate examDate;
    private String status;

    public ExamResultDto(Long id, String studentName, String subjectName, Integer marksObtained, Integer totalMarks,
            Double percentage, Date examDate, Status status) {
        this.id = id;
        this.studentName = studentName;
        this.subjectName = subjectName;
        this.marksObtained = marksObtained;
        this.totalMarks = totalMarks;
        this.percentage = percentage;
        this.examDate = examDate != null ? examDate.toLocalDate() : null;
        this.status = status != null ? status.name() : null;
    }
}
