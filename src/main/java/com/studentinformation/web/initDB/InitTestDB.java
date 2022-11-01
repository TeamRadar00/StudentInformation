package com.studentinformation.web.initDB;

import com.studentinformation.domain.*;
import com.studentinformation.service.ApplicationService;
import com.studentinformation.service.GradeService;
import com.studentinformation.service.LectureService;
import com.studentinformation.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class InitTestDB {

    private final MemberService memberService;
    private final LectureService lectureService;
    private final ApplicationService applicationService;
    private final GradeService gradeService;

    @EventListener(ApplicationReadyEvent.class)
    public void createEntity(){
        Member student = createMemberEntity("student", MemberState.inSchool);
        Member professor = createMemberEntity("professor", MemberState.professor);
        Member admin = createMemberEntity("admin", MemberState.admin);

        Lecture lecture1 = createLectureEntity("lecture1", professor);
        Lecture lecture2 = createLectureEntity("lecture2", professor);

        applicationService.saveApplication(new Application(student, lecture1));
        applicationService.saveApplication(new Application(student, lecture2));

        gradeService.saveGrade(Grade.createEmptyGrade(student, lecture1));
        gradeService.saveGrade(Grade.createEmptyGrade(student, lecture2));
    }


    private Lecture createLectureEntity(String lectureName, Member professor) {
        Lecture lecture = new Lecture(lectureName, professor, "202202", "~/12:00~13:50/~/13:00~14:50/~/~/~/", 20);
        lectureService.makeLecture(lecture);
        return lecture;
    }

    private Member createMemberEntity(String memberName, MemberState state) {
        Member member = new Member(memberName, memberName, memberName, state, memberName);
        member.changeCreateDate();
        memberService.addMember(member);
        return member;
    }

}
