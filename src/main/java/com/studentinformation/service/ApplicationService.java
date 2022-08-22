package com.studentinformation.service;


import com.studentinformation.domain.Application;
import com.studentinformation.domain.Score;
import com.studentinformation.domain.Lecture;
import com.studentinformation.repository.ApplicationRepository;
import com.studentinformation.repository.GradeRepository;
import com.studentinformation.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
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
     * 수강신청하면 grade 객체 생성 삭제 편하게 할려고 application이랑 grade랑 단방향 연결함
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
