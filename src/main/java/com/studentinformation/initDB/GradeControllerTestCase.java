package com.studentinformation.initDB;


import com.studentinformation.domain.Grade;
import com.studentinformation.domain.Lecture;
import com.studentinformation.domain.Member;
import com.studentinformation.domain.MemberState;
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


    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void createGradeTestCase(){
        Member member = createMember();
        for(int i=0;i<6;i++){
            Member professor = new Member("test" + i, "test" + i, "professor" + i, MemberState.professor, "test");
            Lecture lecture = new Lecture("test" + i, professor, "202202", "~/12:00~13:50/~/13:00~14:50/~/~/~/", 20);
            Grade emptyGrade = Grade.createEmptyGrade(member, lecture);
            gradeService.saveGrade(emptyGrade);
        }
    }


    private Member createMember() {
        Member member = new Member("test","test","test", MemberState.inSchool,"test");
        member.changeCreateDate();
        memberService.addMember(member);
        return member;
    }
}
