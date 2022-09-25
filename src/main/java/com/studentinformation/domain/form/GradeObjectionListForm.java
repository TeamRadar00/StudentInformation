package com.studentinformation.domain.form;

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
public class GradeObjectionListForm {

    private Lecture selectLecture;
    private Page<Grade> gradeList;
    private List<Lecture> lectureList;

    public static GradeObjectionListForm of(Lecture lecture, Page<Grade> gradeList){
        return new GradeObjectionListForm(lecture,gradeList,lecture.getProfessor().getProfessorLectures());
    }

    public static GradeObjectionListForm of(Member professor){
        return new GradeObjectionListForm(null,null,professor.getProfessorLectures());
    }
}
