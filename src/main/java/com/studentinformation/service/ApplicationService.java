package com.studentinformation.service;


import com.studentinformation.domain.Application;
import com.studentinformation.domain.Grade;
import com.studentinformation.domain.Lecture;
import com.studentinformation.repository.ApplicationRepository;
import com.studentinformation.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    private final LectureRepository lectureRepository;

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
//            log.info("saveApplication = {}",application);
            return application;
        }else{
            throw new IllegalArgumentException("not enough lecture's size to save");
        }
    }

    //성적 입력/수정 둘다 기능
    @Transactional
    public Application editApplicationOfGrade(Long oldApplicationId, Grade newGrade){
        Application application = applicationRepository.findById(oldApplicationId).get();// 확실히 존재하는 엔티티
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
    public void deleteApplication(Long applicationId){
        applicationRepository.deleteById(applicationId);
    }


    /**
     * !!!!페이징 기능 넣어야함
     * 이의제기하는 글만 반환
     */
    public Page<Application> findAllExistObjection(Long lectureId, Pageable pageable){
        Lecture lecture = lectureRepository.findById(lectureId).get();
        List<Application> applications = lecture.getApplications();
        List<Application> objectionsList = applications.stream()
                .filter(Predicate.not(app -> app.getObjection()==null))
                .filter(Predicate.not(app -> app.getObjection().isBlank()))
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min(start+pageable.getPageSize(),objectionsList.size());
        return new PageImpl<>(objectionsList.subList(start,end),pageable,objectionsList.size());
    }

}
