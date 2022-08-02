package com.studentinformation.service;


import com.studentinformation.domain.Lecture;
import com.studentinformation.domain.Member;
import com.studentinformation.repository.LectureRepository;
import com.studentinformation.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        return lectureRepository.save(lecture);
    }

    //강의 수정
    @Transactional
    public Lecture editLecture(Long oldLectureId,Lecture newLecture){
        Lecture oldLecture = lectureRepository.findById(oldLectureId).get();
        return oldLecture.update(newLecture);
    }

    //강의 삭제
    @Transactional
    public void deleteLecture(Lecture lecture){
        lectureRepository.delete(lecture);
    }

    // 페이징 기능 추가 해야 됨
    public List<Lecture> findLectureByProfessorName(String professorName){
        Optional<Member> professor = memberRepository.findByMemberName(professorName);
        if(professor.isEmpty()) return null;
        return professor.get().getProfessorLectures();
    }

    // 페이징 기능 추가 해야 됨
    public List<Lecture> findLectureByLectureName(String lectureName){
        return lectureRepository.findByLectureName(lectureName);
    }

    //강의 id는 단 하나여서 Lecture 하나만 반환
    public Lecture findLectureByLectureId(Long lectureId){
        return lectureRepository.findById(lectureId).orElse(null);
    }

    // 페이징 기능 추가 해야 됨
    public List<Lecture> findLectureBySemester(String semester){
        return lectureRepository.findBySemester(semester);
    }


}
