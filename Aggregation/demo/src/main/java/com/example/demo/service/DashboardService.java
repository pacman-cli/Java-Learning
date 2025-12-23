package com.example.demo.service;

import com.example.demo.repository.StudentResultCriteriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

public interface DashboardService {
    Map<String, Object> getDashBoardSummary();
}
