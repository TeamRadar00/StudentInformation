package com.studentinformation.repository;

import com.studentinformation.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

}
