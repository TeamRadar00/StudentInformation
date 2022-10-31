package com.studentinformation.repository;

import com.studentinformation.domain.Lecture;
import com.studentinformation.domain.Member;
import com.studentinformation.domain.MemberState;
import com.studentinformation.domain.Week;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class LectureRepositoryTest {

    @Autowired EntityManager em;
    @Autowired MemberRepository memberRepository;
    @Autowired LectureRepository lectureRepository;

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    void basicTest() {
        //given
        Member member = new Member("123", "password", "choi", MemberState.inSchool, "공대");
        Lecture lecture = new Lecture("C기초", member, "2022_2", "",  20);

        //when
        memberRepository.save(member);
        lectureRepository.save(lecture);
        em.flush();
        em.clear();

        //then
        Lecture findLecture = lectureRepository.findById(lecture.getId()).get();
        assertThat(lecture).usingRecursiveComparison().isEqualTo(findLecture);
    }

}