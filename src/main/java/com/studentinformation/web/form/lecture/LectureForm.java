package com.studentinformation.web.form.lecture;

import com.studentinformation.domain.Lecture;
import com.studentinformation.domain.Member;
import com.studentinformation.domain.Week;
import com.studentinformation.web.form.member.MemberForm;
import lombok.*;

import java.util.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LectureForm {

    private Long id;
    private String lectureName;
    private MemberForm professor;
    private String semester;
    private List<LectureTime> lectureTimeList;
    private int limitNum;

    public static LectureForm of(Lecture lecture) {
        List<LectureTime> lectureTimeList = new Vector<>();
        String[] timeArr = lecture.getTime().split("/");
        Week[] week = Week.values();
        for (int i = 0; i < week.length; i++) {
            String[] token = timeArr[i].split("~");
            if(token.length<2) lectureTimeList.add(new LectureTime());
            else lectureTimeList.add(new LectureTime(token[0], token[1]));
        }
        return new LectureForm(lecture.getId(), lecture.getLectureName(), MemberForm.of(lecture.getProfessor()), lecture.getSemester(),
                lectureTimeList ,lecture.getLimitNum());
    }

    public Lecture convertEntity(Member member) {
        StringBuilder sb = new StringBuilder();
        lectureTimeList.stream().forEach(time -> sb.append(time).append("/"));
        String time = sb.toString();

        return new Lecture(lectureName, member, semester, time, limitNum);
    }

    @AllArgsConstructor
    @Getter @Setter
    public static class LectureTime {
        private String startTime;
        private String endTime;

        public LectureTime() {this.startTime = ""; this.endTime = "";}

        @Override
        public String toString() {
            return startTime + "~" + endTime;
        }
    }
}
