package com.studentinformation.web.form.grade;


import com.studentinformation.domain.Grade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class TotalGradeForm {

    private int totalCredit;
    private double totalGrade;
    private double averageGrade;

    public static TotalGradeForm of(List<Grade> grades){
        int totalCredit = grades.stream()
                .mapToInt(grade -> grade.getLecture().getLectureCredit())
                .sum();

        double totalGrade = grades.stream()
                .mapToDouble(grade -> (grade.getScore().getScore() * grade.getLecture().getLectureCredit()))
                .sum();

        double averageGrade = totalGrade/totalCredit;
        return new TotalGradeForm(totalCredit,totalGrade,averageGrade);
    }


}
