package com.studentinformation.service;


import com.studentinformation.domain.*;
import com.studentinformation.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;;

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
//            log.info("saveApplication = {}",application);
            return application;
        }else{
            throw new IllegalArgumentException("not enough lecture's size to save");
        }
    }


    @Transactional
    public void deleteApplication(Long applicationId){
        applicationRepository.deleteById(applicationId);

    }


}
