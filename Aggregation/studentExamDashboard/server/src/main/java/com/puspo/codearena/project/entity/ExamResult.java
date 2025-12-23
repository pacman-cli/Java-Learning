package com.puspo.codearena.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "exam_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "marks_Obtained", nullable = false)
    private Integer marksObtained;

    @Column(name = "total_marks", nullable = false)
    private Integer totalMarks;

    @Column(name = "exam_date", nullable = false)
    private Date examDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    //helper method to calculate percentage
    private Double getPercentage() {
        return (double) (marksObtained / totalMarks * 100);
    }
}
