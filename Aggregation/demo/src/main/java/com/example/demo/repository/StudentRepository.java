package com.example.demo.repository;

import com.example.demo.entity.StudentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<StudentResult, Long> {
//    @Query("SELECT avg(s.marks) FROM StudentResult s")
//    Double findAverageMarks();
//
//    @Query("SELECT max(s.marks) FROM StudentResult s")
//    Integer findMaxMarks();
//
//    @Query("""
//    SELECT s.subject,avg(s.marks)
//    FROM StudentResult s
//    GROUP BY s.subject
//    """)
//    List<Object[]>subjectWiseAverage();
}
