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

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class LectureRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired LectureRepository lectureRepository;

    @Test
    void basicTest() {
        Member member = new Member("123", "password", "choi", MemberState.inSchool, "공대");
        memberRepository.save(member);

        OffsetTime lectureTime = OffsetTime.of(LocalTime.now(), ZoneOffset.ofHours(2));
        Lecture lecture = new Lecture("C기초", member, "2022_2", Week.MONDAY, lectureTime, 20);

        lectureRepository.save(lecture);
        Lecture findLecture = lectureRepository.findById(lecture.getId()).get();
        assertThat(findLecture).isEqualTo(lecture);
    }

}