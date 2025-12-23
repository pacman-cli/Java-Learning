package com.example.demo.repository;

import com.example.demo.dto.SubjectAverageDTO;

import java.util.List;

public interface StudentResultCriteriaRepository{
    Double findAverageMarks();

    Integer findMaxMarks();

    List<SubjectAverageDTO> subjectWiseAverage();
}
