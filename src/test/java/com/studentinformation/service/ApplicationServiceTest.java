package com.studentinformation.service;

import com.studentinformation.domain.*;
import com.studentinformation.repository.ApplicationRepository;
import org.assertj.core.api.Assertions;
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
    public void 성적입력_수정() throws Exception {
        //given
        Application application = makeTestApplication();
        applicationService.saveApplication(application);
        //when
        applicationService.editApplicationOfGrade(application.getId(),Grade.A_PLUS);
        //then
        assertThat(application.getGrade()).isEqualTo(Grade.A_PLUS);
    }

    @Test
    public void 이의신청() throws Exception {
        //given
        Application application = makeTestApplication();
        applicationService.saveApplication(application);

        String objection = "이건 말이 안된다고 생각합니다!!!";
        //when
        applicationService.editApplicationOfObjection(application.getId(),objection);
        //then
        assertThat(application.getObjection()).isEqualTo(objection);
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

    @Test
    public void 이의신청목록조회() throws Exception {
        //given
        int startPage = 0;
        int pageContentCount = 10;
        int totalApplication = 100;

        Member professor = new Member("test","test","test", MemberState.professor,"test");
        Lecture lecture = new Lecture("test",professor,"test", Week.MONDAY, OffsetTime.now(),100);

        for(int i=0;i<totalApplication;i++){
            Member member = new Member("test"+i,"test"+i,"test"+i,MemberState.inSchool,"test"+i);
            Application application = new Application(member,lecture);
            applicationService.saveApplication(application);
            if(i%4==0){
                applicationService.editApplicationOfObjection(application.getId(),"test");
            }
            if(i%4==1){
                applicationService.editApplicationOfObjection(application.getId()," ");
            }
            if(i%4==1){
                applicationService.editApplicationOfObjection(application.getId(),"        d      ");
            }

        }

        Pageable pageable = PageRequest.of(startPage,pageContentCount, Sort.by("id"));
        //when
        Page<Application> allExistObjection = applicationService.findAllExistObjection(lecture.getId(), pageable);
        //then
        assertThat(allExistObjection.getNumber()).isEqualTo(startPage);
        assertThat(allExistObjection.getNumberOfElements()).isEqualTo(pageContentCount);
        for (Application application : allExistObjection) {
            System.out.println("application = " + application);
        }
    }

    private Application makeTestApplication(){
        Member testMember = new Member("test","test","test", MemberState.inSchool,"test");
        Member professor = new Member("test","test","test", MemberState.professor,"test");
        Lecture testLecture = new Lecture("test",professor,"test", Week.MONDAY, OffsetTime.now(),2);

        return new Application(testMember,testLecture);
    }

}
