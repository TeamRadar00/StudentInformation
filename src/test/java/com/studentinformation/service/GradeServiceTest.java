package com.studentinformation.service;

import com.studentinformation.domain.*;
import com.studentinformation.repository.LectureRepository;
import com.studentinformation.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class GradeServiceTest {

    @Autowired
    private GradeService gradeService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private ApplicationService applicationService;

    @Test
    public void grade생성() throws Exception {
        Grade grade = makeTestGrade();
        Grade saveGrade = gradeService.saveGrade(grade);

        assertThat(grade).isEqualTo(saveGrade);
    }


    @Test
    public void 성적입력_수정() throws Exception {
        //given
        Grade grade = makeTestGrade();
        gradeService.saveGrade(grade);
        //when
        gradeService.editGradeOfScore(grade.getId(),Score.F);
        //then
        assertThat(grade.getScore()).isEqualTo(Score.F);
    }

    @Test
    public void 이의신청() throws Exception {
        //given
        Grade grade = makeTestGrade();
        gradeService.saveGrade(grade);
        String objection = "이건 말이 안된다고 생각합니다!!!";
        //when
        gradeService.editGradeOfObjection(grade.getId(), objection);
        //then
        assertThat(grade.getObjection()).isEqualTo(objection);
    }


    @Test
    public void 이의신청목록조회() throws Exception {
        //given
        int startPage = 0;
        int pageContentCount = 10;
        int totalApplication = 100;

        Member professor = new Member("test","test","test", MemberState.professor,"test");
        memberRepository.save(professor);
        Lecture lecture = new Lecture("test",professor,"test", "",100);
        lectureRepository.save(lecture);
        for(int i=0;i<totalApplication;i++){
            Member member = new Member("test"+i,"test"+i,"test"+i,MemberState.inSchool,"test"+i);
            memberRepository.save(member);
            Grade grade = new Grade(member, lecture);
            gradeService.saveGrade(grade);
            if(i%4==0){
                gradeService.editGradeOfObjection(grade.getId(),"test");
            }
            if(i%4==1){
                gradeService.editGradeOfObjection(grade.getId(),"   ");
            }
            if(i%4==1){
                gradeService.editGradeOfObjection(grade.getId(),"    d    ");
            }

        }

        Pageable pageable = PageRequest.of(startPage,pageContentCount, Sort.by("id"));
        //when
        Page<Grade> allExistObjection = gradeService.findAllExistObjection(lecture.getId(), pageable);
        //then
        assertThat(allExistObjection.getNumber()).isEqualTo(startPage);
        assertThat(allExistObjection.getNumberOfElements()).isEqualTo(pageContentCount);
        for (Grade grade : allExistObjection) {
            System.out.println("grade = " + grade);
        }
    }

    @Test
    void EmptyGrade생성() throws Exception {
        //given
        Member member = new Member("test","test","test", MemberState.inSchool,"test");
        Lecture lecture = new Lecture("test",member,"test", "",100);

        //when
        Grade emptyGrade = gradeService.createEmptyGrade(member,lecture);

        //then
        assertThat(emptyGrade.getScore()).isEqualTo(Score.IN);
    }

    private Grade makeTestGrade(){
        Member testMember = new Member("test","test","test", MemberState.inSchool,"test");
        Member professor = new Member("test","test","test", MemberState.professor,"test");
        Lecture testLecture = new Lecture("test",professor,"test",  "",2);

        return new Grade(testMember,testLecture);
    }
}