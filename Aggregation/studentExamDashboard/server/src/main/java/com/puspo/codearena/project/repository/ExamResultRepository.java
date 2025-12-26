package com.puspo.codearena.project.repository;

import com.puspo.codearena.project.entity.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamResultRepository extends ExamResultRepositoryCustom, JpaRepository<ExamResult, Long>,
        JpaSpecificationExecutor<ExamResult> {
}
