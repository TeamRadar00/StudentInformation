package com.studentinformation.domain.form;

import com.studentinformation.domain.Lecture;
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

    public static LectureForm createLectureFrom(Lecture lecture) {
        List<LectureTime> lectureTimeList = new Vector<>();
//        lectureTimeMap.keySet().forEach(a -> a.na);
//        Set<Map.Entry<Week, LectureTime>> entries = lectureTimeMap.entrySet();
//        entries.forEach(t -> t.set);
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

    @AllArgsConstructor
    @Getter @Setter
    public static class LectureTime {
        private String startTime;
        private String endTime;

        public LectureTime() {this.startTime = ""; this.endTime = "";}
    }
}
