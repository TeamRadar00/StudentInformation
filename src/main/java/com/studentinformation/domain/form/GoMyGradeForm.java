package com.studentinformation.domain.form;

import com.studentinformation.domain.Grade;
import com.studentinformation.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GoMyGradeForm {

    private MemberForm memberForm;
    private TotalGradeForm totalGradeForm;
    private List<Grade> grades;

    public static GoMyGradeForm of(Member member){
        return new GoMyGradeForm(MemberForm.of(member),TotalGradeForm.of(member.getGrades()),member.getGrades());
    }


}
