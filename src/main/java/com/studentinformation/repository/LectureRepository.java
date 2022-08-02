package com.studentinformation.repository;

import com.studentinformation.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    List<Lecture> findByLectureName(String lectureName);

    List<Lecture> findBySemester(String semester);
}
