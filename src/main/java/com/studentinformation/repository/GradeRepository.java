package com.studentinformation.repository;

import com.studentinformation.domain.Grade;
import com.studentinformation.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    List<Grade> findByStudentId(Long studentId);


}
