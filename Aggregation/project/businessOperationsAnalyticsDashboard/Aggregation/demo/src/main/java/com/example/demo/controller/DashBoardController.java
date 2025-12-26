package com.example.demo.controller;

import com.example.demo.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/dashboard")
public class DashBoardController {
    private final DashboardService dashboardService;

    public DashBoardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }
//    **************** Aggregation Implementation using sql ************************
//    private final StudentRepository studentRepository;
//
//    public DashBoardController(StudentRepository studentRepository) {
//        this.studentRepository = studentRepository;
//    }
//
//    @GetMapping("/summary")
//    public Map<String,Object> getSummary(){
//        //for storing the summaries, we can use hashmap
//        Map<String,Object> response = new HashMap<>();
//
//        response.put("averageMarks",studentRepository.findAverageMarks());
//        response.put("highestMarks",studentRepository.findMaxMarks());
//
//        //for subject wise average we need a arraylist
//        List<Map<String,Object>> subjectStats = new ArrayList<>();
//
//        for(Object[] subjectStat: studentRepository.subjectWiseAverage()){
//            subjectStats.add(Map.of("subject",subjectStat[0],"averageMarks",subjectStat[1]));
//        }
//        response.put("subjectWiseAverage", subjectStats);
//        return response;
//    }

    @GetMapping("/summary")
    public Map<String, Object> getSummary() {
        return dashboardService.getDashBoardSummary();
    }
}
