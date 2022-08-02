package com.studentinformation.service;


import com.studentinformation.domain.Application;
import com.studentinformation.domain.Grade;
import com.studentinformation.domain.Lecture;
import com.studentinformation.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    /**
     * controller에서 application 생성 후 변수로 넘기는걸로 만듦
     * application에서 lecture 현재 인원, 강의 정원 비교해서 저장할지 결정
     * @param application
     */
    @Transactional
    public Application saveApplication(Application application){
        Lecture lecture = application.getLecture();
        if(lecture.checkCurrentCountUnderLimitNum()){ // (현재 인원 < 강의 정원)
            applicationRepository.save(application);
            log.info("saveApplication = {}",application);
            return application;
        }else{
            throw new IllegalArgumentException("not enough to save lecture's size");
        }
    }

    //성적 입력/수정 둘다 기능
    @Transactional
    public Application editApplication(Long oldApplication, Grade newGrade){
        Application application = applicationRepository.findById(oldApplication).get();// 확실히 존재하는 엔티티
        application.updateGrade(newGrade);
        return application;
    }

    //성적 이의 신청 내용 입력/수정
    @Transactional
    public Application editApplicationOfObjection(Long applicationId,String objection){
        Application application = applicationRepository.findById(applicationId).get();
        application.updateObjection(objection);
        return application;
    }

    @Transactional
    public void deleteApplication(Application application){
        applicationRepository.delete(application);
    }


    /**
     * !!!!페이징 기능 넣어야함
     * 이의제기하는 글만 반환
     * @param lecture
     */
    public List<Application> findAllExistObjection(Lecture lecture){
        List<Application> applications = lecture.getApplications();
        List<Application> objectionsList = applications.stream()
                .filter(Predicate.not(app -> app.getObjection().isEmpty()))
                .filter(Predicate.not(app ->app.getObjection().equals("")))
                .collect(Collectors.toList());
        return objectionsList;
    }

}
