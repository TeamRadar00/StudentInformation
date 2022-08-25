package com.studentinformation.domain.form;

import com.studentinformation.domain.Lecture;
import com.studentinformation.domain.Member;
import com.studentinformation.domain.Week;
import lombok.*;

import java.util.*;

@Setter
@Getter
@ToString
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
        return new LectureForm(lecture.getId(), lecture.getLectureName(), MemberForm.createMemberForm(lecture.getProfessor()), lecture.getSemester(),
                lectureTimeList ,lecture.getLimitNum());
    }

    public Lecture convertEntity(Member professor) {
        StringBuilder sb = new StringBuilder();
        lectureTimeList.stream().forEach(time -> sb.append(time).append("/"));
        String time = sb.toString();
        return new Lecture(lectureName, professor, semester, time, limitNum);
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
