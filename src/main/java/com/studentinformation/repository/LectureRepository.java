package com.studentinformation.repository;

import com.studentinformation.domain.Lecture;
import com.studentinformation.domain.Member;
import com.studentinformation.domain.MemberState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    Page<Lecture> findBySemester(String semester, Pageable pageable);

    @Query("select a.lecture from Application a left join fetch a.lecture.professor where a.student = :student")
    List<Lecture> findLecturesByStudent(@Param("student") Member student);

    List<Lecture> findLecturesByProfessor(Member member);

    Optional<Lecture> findByLectureNameAndProfessorAndSemester(String lectureName, Member professor, String semester);

    @Query(value = "select l from Lecture l left join fetch l.professor where l.professor.state = :state" +
            " and l.professor.memberName like %:name%" +
            " and l.semester = :semester",
            countQuery = "select count(l) from Lecture l where l.professor.state = :state" +
                    " and l.professor.memberName like %:name%" +
                    " and l.semester = :semester")
    Page<Lecture> findAllByProfessorName(@Param("state") MemberState state, @Param("name") String name,
                                         @Param("semester") String semester, Pageable pageable);

    @Query(value = "select l from Lecture l left join fetch l.professor where l.professor.state = :state" +
            " and l.lectureName like %:name%" +
            " and l.semester = :semester",
            countQuery = "select count(l) from Lecture l where l.professor.state = :state" +
                    " and l.lectureName like %:name%" +
                    " and l.semester = :semester")
    Page<Lecture> findAllByLectureName(@Param("state") MemberState state, @Param("name") String name,
                                       @Param("semester") String semester, Pageable pageable);

    // 2022/02 학기에 해당하는 모든 강좌에서 내가 신청한 강좌를 제외한 나머지 반환
    @Query("select l from Lecture l where l.semester = :semester and not l " +
            "in (select a.lecture from Application a where a.student = :student)")
    Page<Lecture> findAllRemainLecture(@Param("student") Member member, @Param("semester") String semester, Pageable pageable);
}
