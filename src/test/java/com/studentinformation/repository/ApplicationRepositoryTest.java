package com.studentinformation.repository;

import com.studentinformation.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ApplicationRepositoryTest {

    @Autowired EntityManager em;
    @Autowired MemberRepository memberRepository;
    @Autowired LectureRepository lectureRepository;
    @Autowired ApplicationRepository applicationRepository;

    @Test
    void basicTest() throws Exception {
        //given
        Member member = new Member("123", "password", "choi", MemberState.inSchool, "공대");
        Lecture lecture = new Lecture("C기초", member, "2022_2",  "", 20);
        Application application = new Application(member, lecture);

        //when
        memberRepository.save(member);
        lectureRepository.save(lecture);
        applicationRepository.save(application);
        em.flush();
        em.clear();

        Application findApplication = applicationRepository.findById(application.getId()).get();

        //then
        assertThat(application).usingRecursiveComparison().isEqualTo(findApplication);

        // 참고로 순서를 바꾸면 실패한다. applicationRepository에 저장된 member와 lecture 객체가 하이버네이트 프록시에 감싸졌기에
        // 원본 객체를 상속받은 프록시 객체는 원본 객체와 같다고 할 순 없으나 원본 객체는 프록시 객체의 일부이기에 같다고 할 수 있다..? 이게 맞겠지??
//        assertThat(findApplication).usingRecursiveComparison().isEqualTo(application);
    }

}