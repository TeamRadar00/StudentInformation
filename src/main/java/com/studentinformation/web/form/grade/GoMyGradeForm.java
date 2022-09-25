package com.studentinformation.web.form.grade;

import com.studentinformation.domain.Grade;
import com.studentinformation.domain.Member;
import com.studentinformation.web.form.member.MemberForm;
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
