package com.studentinformation.web.initDB;


import com.studentinformation.domain.*;
import com.studentinformation.repository.MemberRepository;
import com.studentinformation.service.ApplicationService;
import com.studentinformation.service.GradeService;
import com.studentinformation.service.LectureService;
import com.studentinformation.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Slf4j
@RequiredArgsConstructor
public class GradeControllerTestCase {

    private final GradeService gradeService;
    private final LectureService lectureService;
    private final MemberService memberService;
    private final ApplicationService applicationService;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void createTestCaseForStudent(){
        Member member = createMemberTest1();
        for(int i=0;i<6;i++){
            Member professor = new Member("test" + i, "test" + i, "professor" + i, MemberState.professor, "test");
            memberService.addMember(professor);
            Lecture lecture = new Lecture("test" + i, professor, "202202", "~/12:00~13:50/~/13:00~14:50/~/~/~/", 20);
            lectureService.makeLecture(lecture);

            applicationService.saveApplication(new Application(member, lecture));

            Grade emptyGrade = Grade.createEmptyGrade(member, lecture);
            gradeService.saveGrade(emptyGrade);
        }
    }


    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void createTestCaseForProfessor(){
        Member professor = createMemberTest2();
        Lecture lecture1 = new Lecture("test1", professor, "202202", "~/12:00~13:50/~/13:00~14:50/~/~/~/", 200);
        lectureService.makeLecture(lecture1);
        Lecture lecture2 = new Lecture("test2", professor, "202202", "~/12:00~13:50/~/13:00~14:50/~/~/~/", 200);
        lectureService.makeLecture(lecture2);
        Lecture lecture3 = new Lecture("test3", professor, "202202", "~/12:00~13:50/~/13:00~14:50/~/~/~/", 200);
        lectureService.makeLecture(lecture3);
        for(int i=20;i<50;i++){
            Member student = new Member("test" + i, "test" + i, "student" + i, MemberState.inSchool, "test");
            memberService.addMember(student);

            applicationService.saveApplication(new Application(student, lecture1));

            Grade emptyGrade = Grade.createEmptyGrade(student, lecture1);
            gradeService.saveGrade(emptyGrade);

            emptyGrade.updateObjection("test");
        }
        for(int i=50;i<70;i++){
            Member student = new Member("test" + i, "test" + i, "student" + i, MemberState.inSchool, "test");
            memberService.addMember(student);

            applicationService.saveApplication(new Application(student, lecture2));

            Grade emptyGrade = Grade.createEmptyGrade(student, lecture2);
            gradeService.saveGrade(emptyGrade);

            emptyGrade.updateObjection("test");
        }
        for(int i=70;i<80;i++){
            Member student = new Member("test" + i, "test" + i, "student" + i, MemberState.inSchool, "test");
            memberService.addMember(student);

            applicationService.saveApplication(new Application(student, lecture3));

            Grade emptyGrade = Grade.createEmptyGrade(student, lecture3);
            gradeService.saveGrade(emptyGrade);

            emptyGrade.updateObjection("test");
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void createTestCaseForAdmin(){
        memberService.addMember(new Member("admin", "admin", "admin", MemberState.admin, "admin"));
    }

    private Member createMemberTest1() {
        Member member = new Member("test","test","test", MemberState.inSchool,"test");
        member.changeCreateDate();
        memberService.addMember(member);
        return member;
    }
    private Member createMemberTest2() {
        Member member = new Member("professor","professor","professor", MemberState.professor,"test");
        member.changeCreateDate();
        memberService.addMember(member);
        return member;
    }
}
