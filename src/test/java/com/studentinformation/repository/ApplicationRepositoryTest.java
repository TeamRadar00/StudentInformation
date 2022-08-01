package com.studentinformation.repository;

import com.studentinformation.domain.*;
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
class ApplicationRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired LectureRepository lectureRepository;
    @Autowired ApplicationRepository applicationRepository;

    @Test
    void basicTest() throws Exception {
        //given
        Member member = new Member("123", "password", "choi", MemberState.inSchool, "공대");
        memberRepository.save(member);

        OffsetTime lectureTime = OffsetTime.of(LocalTime.now(), ZoneOffset.ofHours(2));
        Lecture lecture = new Lecture("C기초", member, "2022_2", Week.MONDAY, lectureTime, 20);
        lectureRepository.save(lecture);

        Application application = new Application(member, lecture);
        applicationRepository.save(application);

        //when
        Application findApplication = applicationRepository.findById(application.getId()).get();

        //then
        assertThat(findApplication).isEqualTo(application);
    }

}