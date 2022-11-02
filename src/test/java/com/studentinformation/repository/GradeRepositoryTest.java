package com.studentinformation.repository;

import com.studentinformation.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class GradeRepositoryTest {

    @Autowired EntityManager em;
    @Autowired MemberRepository memberRepository;
    @Autowired LectureRepository lectureRepository;
    @Autowired GradeRepository gradeRepository;

    @Test
    void basicTest() throws Exception {
        //given
        Member member = new Member("123", "password", "choi", MemberState.inSchool, "공대");
        Lecture lecture = new Lecture("C기초", member, "2022_2",  "", 20);
        Grade grade = Grade.createEmptyGrade(member, lecture);

        //when
        memberRepository.save(member);
        lectureRepository.save(lecture);
        gradeRepository.save(grade);
        em.flush();
        em.clear();

        Grade findGrade = gradeRepository.findById(grade.getId()).get();

        //then
        assertThat(grade).usingRecursiveComparison().isEqualTo(findGrade);
    }

}