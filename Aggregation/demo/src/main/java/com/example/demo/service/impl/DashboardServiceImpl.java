package com.example.demo.service.impl;

import com.example.demo.repository.StudentResultCriteriaRepository;
import com.example.demo.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    public final StudentResultCriteriaRepository studentResultCriteriaRepository;

    public Map<String, Object> getDashBoardSummary() {
        Map<String, Object> response = new HashMap<>();

        response.put("averageMarks", studentResultCriteriaRepository.findAverageMarks());
        response.put("highestMarks", studentResultCriteriaRepository.findMaxMarks());
        response.put("subjectWiseAvg", studentResultCriteriaRepository.subjectWiseAverage());

        return response;
    }
}
