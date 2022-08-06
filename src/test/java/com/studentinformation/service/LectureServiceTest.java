package com.studentinformation.service;

import com.studentinformation.domain.Lecture;
import com.studentinformation.domain.Member;
import com.studentinformation.domain.MemberState;
import com.studentinformation.domain.Week;
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
public class LectureServiceTest {

    @Autowired
    private LectureService lectureService;

    @Test
    public void 강의생성() throws Exception {
        //given
        Member member = new Member("test","test","test",MemberState.professor,"test");
        Lecture lecture = new Lecture("test",member,"test",Week.MONDAY,OffsetTime.now(),100);
        Lecture test = lectureService.makeLecture(lecture);
        //when
        //then

        assertThat(test).isEqualTo(lecture);
    }

    @Test
    public void 강의수정() throws Exception {
        //given
        Lecture oldLecture = makeTestLecture();
        Lecture newLecture = new Lecture("change",oldLecture.getProfessor(),"change",Week.MONDAY,OffsetTime.now(),100);
        //when
        lectureService.editLecture(oldLecture.getId(),newLecture);
        //then

        assertThat(newLecture.getLectureName()).isEqualTo("change");
        assertThat(newLecture.getSemester()).isEqualTo("change");
    }

    @Test
    public void 강의삭제() throws Exception {
        //given
        Lecture lecture = makeTestLecture();
        //when
        lectureService.deleteLecture(lecture.getId());
        //then
        assertThat(lectureService.findByLectureId(lecture.getId())).isNull();
    }

    @Test
    public void 교수님이름으로검색() throws Exception {
        //given
        int startPage = 0;
        int pageContentCount = 10;
        int totalLecture = 105;


        Member professor = new Member("test","test","test", MemberState.professor,"test");

        for(int i=0;i<totalLecture;i++){
            Lecture testLecture = new Lecture("test"+i,professor,"202202", Week.SATURDAY, OffsetTime.now(),100);
            lectureService.makeLecture(testLecture);
        }

        Pageable pageable = PageRequest.of(startPage,pageContentCount, Sort.by("id"));
        //when

        Page<Lecture> test = lectureService.findByProfessorName("te", pageable);

        //then
        assertThat(test.getTotalElements()).isEqualTo(totalLecture);
        assertThat(test.getTotalPages()).isEqualTo(Math.round((float) totalLecture/pageContentCount));
        assertThat(test.getNumber()).isEqualTo(startPage);
        assertThat(test.getNumberOfElements()).isEqualTo(pageContentCount);
    }

    @Test
    public void 강의이름으로검색() throws Exception {
        //given
        int startPage = 0;
        int pageContentCount = 10;
        int totalLecture = 105;


        Member professor = new Member("test","test","test", MemberState.professor,"test");

        for(int i=0;i<totalLecture;i++){
            Lecture testLecture = new Lecture("test"+i,professor,"202202", Week.SATURDAY, OffsetTime.now(),100);
            lectureService.makeLecture(testLecture);
        }

        Pageable pageable = PageRequest.of(startPage,pageContentCount, Sort.by("id"));
        //when

        Page<Lecture> test = lectureService.findByLectureName("test1", pageable);

        //then
//        assertThat(test.getTotalElements()).isEqualTo(totalLecture);
//        assertThat(test.getTotalPages()).isEqualTo(Math.round((float) totalLecture/pageContentCount));
        assertThat(test.getNumber()).isEqualTo(startPage);
        assertThat(test.getNumberOfElements()).isEqualTo(pageContentCount);
    }

    @Test
    public void 학기로강의검색() throws Exception {
        //given
        int startPage = 0;
        int pageContentCount = 10;
        int totalLecture = 105;


        Member professor = new Member("test","test","test", MemberState.professor,"test");

        for(int i=0;i<totalLecture;i++){
            Lecture testLecture = new Lecture("test"+i,professor,"202201", Week.SATURDAY, OffsetTime.now(),100);
            lectureService.makeLecture(testLecture);
        }

        Pageable pageable = PageRequest.of(startPage,pageContentCount, Sort.by("id"));
        //when

        Page<Lecture> test = lectureService.findBySemester("202201", pageable);

        //then
        assertThat(test.getTotalElements()).isEqualTo(totalLecture);
        assertThat(test.getTotalPages()).isEqualTo(Math.round((float) totalLecture/pageContentCount));
        assertThat(test.getNumber()).isEqualTo(startPage);
        assertThat(test.getNumberOfElements()).isEqualTo(pageContentCount);
    }

    private Lecture makeTestLecture(){
        Member member = new Member("test","test","test",MemberState.professor,"test");
        Lecture lecture = new Lecture("test",member,"test",Week.MONDAY,OffsetTime.now(),100);
        return lectureService.makeLecture(lecture);

    }


}
