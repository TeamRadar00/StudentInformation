package com.studentinformation.domain.form;


import com.studentinformation.domain.Grade;
import com.studentinformation.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class TotalGradeForm {

    private int totalCredit;
    private double totalGrade;
    private double averageGrade;

    public TotalGradeForm(Member member){
        totalCredit = member.getGrades().stream()
                .mapToInt(grade -> grade.getLecture().getLectureCredit())
                .sum();

        totalGrade = member.getGrades().stream()
                .mapToDouble(grade -> (grade.getScore().getScore() * grade.getLecture().getLectureCredit()))
                .sum();

        averageGrade = totalGrade/member.getGrades().size();
    }


}
