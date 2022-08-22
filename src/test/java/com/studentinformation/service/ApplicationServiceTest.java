package com.studentinformation.service;

import com.studentinformation.domain.*;
import com.studentinformation.repository.ApplicationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class ApplicationServiceTest {

    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private ApplicationRepository applicationRepository;

    @Test
    public void 수강신청() throws Exception {
        //given
        Application application = makeTestApplication();
        //when
        Application saveApplication = applicationService.saveApplication(application);
        //then
        assertThat(saveApplication).isEqualTo(application);
    }

    @Test
    public void 수강신청_예외처리_인원초과() throws Exception {
        //given
        Member testMember = new Member("test","test","test", MemberState.inSchool,"test");
        Member professor = new Member("test","test","test", MemberState.professor,"test");
        Lecture testLecture = new Lecture("test",professor,"test", Week.MONDAY, OffsetTime.now(),0);

        Application application = new Application(testMember, testLecture);
        //when
        //then
        assertThatThrownBy(()->applicationService.saveApplication(application))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("not enough lecture's size to save");
    }



    @Test
    public void 수강신청취소() throws Exception {
        //given
        Application application = makeTestApplication();
        applicationService.saveApplication(application);

        //when
        applicationService.deleteApplication(application.getId());
        //then
        assertThat(applicationRepository.findById(application.getId())).isEmpty();
    }


    private Application makeTestApplication(){
        Member testMember = new Member("test","test","test", MemberState.inSchool,"test");
        Member professor = new Member("test","test","test", MemberState.professor,"test");
        Lecture testLecture = new Lecture("test",professor,"test", Week.MONDAY, OffsetTime.now(),2);

        return new Application(testMember,testLecture);
    }

}
