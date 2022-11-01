package com.studentinformation.service;

import com.studentinformation.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class LectureServiceTest {

    @Autowired private MemberService memberService;
    @Autowired private LectureService lectureService;

    @Test
    public void 강의생성() throws Exception {
        //given
        Member member = new Member("test","test","test",MemberState.professor,"test");
        Lecture lecture = new Lecture("test", member, "test", "", 100);

         //when
        Lecture test = lectureService.makeLecture(lecture);

        //then
        assertThat(test).isEqualTo(lecture);
    }

    @Test
    public void 강의수정() throws Exception {
        //given
        Lecture oldLecture = makeTestLecture();
        Lecture newLecture = new Lecture("change", oldLecture.getProfessor(), "change",
                oldLecture.getTime(), 100);

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
        String testProfessorName = "test";
        String testSemester = "202202";


        Member professor = new Member("test","test",testProfessorName, MemberState.professor,"test");
        memberService.addMember(professor);

        for(int i=0;i<totalLecture;i++){
            Lecture testLecture;
            if(i%2==0){
                testLecture = new Lecture("test" + i, professor, "202202","", 100);
            }else{
                testLecture = new Lecture("test" + i, professor, "202201", "", 100);
            }
            lectureService.makeLecture(testLecture);
        }

        Pageable pageable = PageRequest.of(startPage,pageContentCount, Sort.by("id"));
        //when

        Page<Lecture> test = lectureService.findByProfessorName(testProfessorName, testSemester, pageable);

        //then
        for (Lecture lecture : test) {
            assertThat(lecture.getProfessor().getMemberName()).isEqualTo(testProfessorName);
            assertThat(lecture.getSemester()).isEqualTo(testSemester);
        }
        assertThat(test.getTotalElements()).isEqualTo(Math.round((float) totalLecture/2));
    }

    @Test
    public void 강의이름으로검색() throws Exception {
        //given
        int startPage = 0;
        int pageContentCount = 10;
        int totalLecture = 105;
        String testLectureName = "test";
        String testSemester = "202202";

        Member professor = new Member("test","test",testLectureName, MemberState.professor,"test");
        memberService.addMember(professor);

        for(int i=0;i<totalLecture;i++){
            Lecture testLecture;
            if(i%2==0){
                testLecture = new Lecture("test" + i, professor, "202202", "", 100);
            }else{
                testLecture = new Lecture("test" + i, professor, "202201", "", 100);
            }
            lectureService.makeLecture(testLecture);
        }

        Pageable pageable = PageRequest.of(startPage,pageContentCount, Sort.by("id"));
        //when

        Page<Lecture> test = lectureService.findByLectureName(testLectureName, testSemester,pageable);

        //then
        for (Lecture lecture : test) {
            assertThat(lecture.getProfessor().getMemberName()).isEqualTo(testLectureName);
            assertThat(lecture.getSemester()).isEqualTo(testSemester);
        }
        assertThat(test.getTotalElements()).isEqualTo(Math.round((float) totalLecture/2));
    }

    private Lecture makeTestLecture(){
        Member member = new Member("test","test","test",MemberState.professor,"test");
        Lecture lecture = new Lecture("test",member,"test","",100);
        return lectureService.makeLecture(lecture);

    }

}