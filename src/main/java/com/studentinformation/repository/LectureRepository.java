package com.studentinformation.repository;

import com.studentinformation.domain.Lecture;
import com.studentinformation.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    Page<Lecture> findBySemester(String semester, Pageable pageable);

    @Query("select a.lecture from Application a where a.student.id = :id")
    List<Lecture> findLecturesByStudentId(@Param("id") Long id);

    List<Lecture> findLecturesByProfessor(Member member);

    Optional<Lecture> findByLectureNameAndProfessorAndSemester(String lectureName, Member professor, String semester);
}
