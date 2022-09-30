package com.studentinformation.web.form.lecture;

import com.studentinformation.domain.Lecture;
import com.studentinformation.domain.Member;
import com.studentinformation.domain.Week;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Vector;

@Setter
@Getter
@AllArgsConstructor
public class CRUDLectureForm {

    @NotBlank
    private String lectureName;

    @NotBlank
    private String year;

    // 1 or 2
    private String semester;

    private List<LectureTime> lectureTimeList;

    @NotNull
    @Range(min = 1,max = 100)
    private Integer limitNum;

    public Lecture convertEntity(Member professor) {
        StringBuilder timeBuilder = new StringBuilder();
        lectureTimeList.stream().forEach(time -> timeBuilder.append(time).append("/"));
        StringBuilder yearAndSemester = new StringBuilder();
        yearAndSemester.append(year).append("0").append(semester);

        return new Lecture(lectureName, professor, yearAndSemester.toString(), timeBuilder.toString(), limitNum);
    }

    public static CRUDLectureForm of(Lecture lecture) {
        List<LectureTime> lectureTimeList = new Vector<>();
        String[] timeArr = lecture.getTime().split("/");
        Week[] week = Week.values();
        for (int i = 0; i < week.length; i++) {
            String[] token = timeArr[i].split("~");
            if(token.length<2) lectureTimeList.add(new LectureTime());
            else lectureTimeList.add(new LectureTime(token[0], token[1]));
        }
        String year = lecture.getSemester().substring(0, 4);
        String semester = lecture.getSemester().substring(5);
        return new CRUDLectureForm(lecture.getLectureName(), year,semester, lectureTimeList ,lecture.getLimitNum());
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
