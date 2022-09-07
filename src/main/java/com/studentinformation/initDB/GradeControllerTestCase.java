package com.studentinformation.initDB;


import com.studentinformation.domain.*;
import com.studentinformation.service.ApplicationService;
import com.studentinformation.service.GradeService;
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
    private final MemberService memberService;
    private final ApplicationService applicationService;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void createTestCaseForStudent(){
        Member member = createMemberTest1();
        for(int i=0;i<6;i++){
            Member professor = new Member("test" + i, "test" + i, "professor" + i, MemberState.professor, "test");
            Lecture lecture = new Lecture("test" + i, professor, "202202", "~/12:00~13:50/~/13:00~14:50/~/~/~/", 20);

            applicationService.saveApplication(new Application(member, lecture));

            Grade emptyGrade = Grade.createEmptyGrade(member, lecture);
            gradeService.saveGrade(emptyGrade);
        }
    }


    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void createTestCaseForProfessor(){
        Member professor = createMemberTest2();
        Lecture lecture = new Lecture("test", professor, "202202", "~/12:00~13:50/~/13:00~14:50/~/~/~/", 20);
        for(int i=10;i<15;i++){
            Member student = new Member("test" + i, "test" + i, "student" + i, MemberState.inSchool, "test");

            applicationService.saveApplication(new Application(student, lecture));

            Grade emptyGrade = Grade.createEmptyGrade(student, lecture);
            gradeService.saveGrade(emptyGrade);
        }
    }

    private Member createMemberTest1() {
        Member member = new Member("test","test","test", MemberState.inSchool,"test");
        member.changeCreateDate();
        memberService.addMember(member);
        return member;
    }
    private Member createMemberTest2() {
        Member member = new Member("professor","test","test", MemberState.professor,"test");
        member.changeCreateDate();
        memberService.addMember(member);
        return member;
    }
}
