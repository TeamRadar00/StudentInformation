package com.studentinformation.web.form.grade;

import com.studentinformation.domain.Grade;
import com.studentinformation.domain.Lecture;
import com.studentinformation.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class GradeGetObjectionListForm {

    private Lecture selectLecture;
    private Page<Grade> gradeList;
    private List<Lecture> lectureList;

    public static GradeGetObjectionListForm of(Lecture lecture,Page<Grade> gradeList){
        return new GradeGetObjectionListForm(lecture,gradeList,lecture.getProfessor().getProfessorLectures());
    }

    public static GradeGetObjectionListForm of(Member professor){
        return new GradeGetObjectionListForm(null,null,professor.getProfessorLectures());
    }
}
