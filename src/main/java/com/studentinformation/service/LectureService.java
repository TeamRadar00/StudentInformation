package com.studentinformation.service;


import com.studentinformation.domain.*;
import com.studentinformation.repository.LectureRepository;
import com.studentinformation.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureService {

    private final LectureRepository lectureRepository;
    private final MemberRepository memberRepository;

    //강의 생성
    @Transactional
    public Lecture makeLecture(Lecture lecture){
        log.info("makeLecture = {}",lecture);
        return lectureRepository.save(lecture);
    }

    /**
     * 강의 수정
     * @param oldLectureId
     * @param newLecture
     */
    @Transactional
    public Lecture editLecture(Long oldLectureId,Lecture newLecture){
        Lecture oldLecture = lectureRepository.findById(oldLectureId).get();
        log.info("oldLecture = {}",oldLecture);
        oldLecture.update(newLecture);
        log.info("newLecture = {}",oldLecture);
        return oldLecture;
    }

    //강의 삭제
    @Transactional
    public void deleteLecture(Long lectureId){
        lectureRepository.deleteById(lectureId);
    }


    /**
     * 모든 교수 중 이름에 professorName이 포함되어있으면 다 저장
     * 저장된 교수의 모든 Lecture를 Page로 모아둠
     * @param professorName
     * @param pageable
     * 나중에 쿼리로 최적화 해야할듯
     */
    public Page<Lecture> findByProfessorName(String professorName, String semester, Pageable pageable){
        return lectureRepository.findAllByProfessorName(MemberState.professor, professorName, semester, pageable);
    }


    /**
     * 모든 강의 조회한다음, arguement로 들어온 string을 포함하는 강의 검색
     * @param lectureName
     * @param pageable
     * 나중에 쿼리로 최적화 해야할듯
     */
    public Page<Lecture> findByLectureName(String lectureName,String semester,Pageable pageable){
        return lectureRepository.findAllByLectureName(MemberState.professor, lectureName, semester, pageable);
    }

    //강의 id는 단 하나여서 Lecture 하나만 반환
    public Lecture findByLectureId(Long lectureId){
        return lectureRepository.findById(lectureId).orElse(null);
    }

}
