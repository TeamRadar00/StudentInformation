package com.studentinformation.domain.form;

import com.studentinformation.domain.Lecture;
import com.studentinformation.domain.Member;
import com.studentinformation.domain.Week;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.OffsetTime;

@Getter
@ToString
@AllArgsConstructor
public class LectureForm {

    private Long id;
    private String lectureName;
    private MemberForm professor;
    private String semester;
    private Week week;
    private OffsetTime time;
    private int limitNum;

    public static LectureForm createLectureForm(Lecture lecture) {
        return new LectureForm(lecture.getId(), lecture.getLectureName(), MemberForm.createMemberForm(lecture.getProfessor()), lecture.getSemester(),
                lecture.getWeek(), lecture.getTime(), lecture.getLimitNum());
    }
}
