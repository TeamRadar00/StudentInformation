package com.studentinformation.service;


import com.studentinformation.domain.*;
import com.studentinformation.repository.LectureRepository;
import com.studentinformation.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * 교수이름이 포함되고, semester에 해당하는 lecutre 반환(쿼리 최적화 완료)
     */
    public Page<Lecture> findByProfessorName(String professorName, String semester, Pageable pageable){
        return lectureRepository.findAllByProfessorName(MemberState.professor, professorName, semester, pageable);
    }


    /**
     * 강의이름이 포함되고, semester에 해당하는 lecture 반환(쿼리 최적화 완료)
     */
    public Page<Lecture> findByLectureName(String lectureName, String semester, Pageable pageable) {
        return lectureRepository.findAllByLectureName(MemberState.professor, lectureName, semester, pageable);
    }

    //강의 id는 단 하나여서 Lecture 하나만 반환
    public Lecture findByLectureId(Long lectureId){
        return lectureRepository.findById(lectureId).orElse(null);
    }

    /**
     * member가 신청하지 않은 lecture 반환 / 2022년 2학기
     */
    public Page<Lecture> findRemainLecture(Member member, Pageable pageable) {
        return lectureRepository.findAllRemainLecture(member,"202202", pageable);
    }
    public boolean checkInaccessibleLectureWithProfessor(Member professor,Long lectureId){
        boolean access =professor.getProfessorLectures().stream()
                .anyMatch(lecture -> lecture.getId().equals(lectureId));
        return !access;
    }
}
