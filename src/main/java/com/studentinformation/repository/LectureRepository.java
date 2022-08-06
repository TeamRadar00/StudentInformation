package com.studentinformation.repository;

import com.studentinformation.domain.Lecture;
import com.studentinformation.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    Page<Lecture> findBySemester(String semester, Pageable pageable);
}
