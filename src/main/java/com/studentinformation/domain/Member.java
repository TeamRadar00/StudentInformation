package com.studentinformation.domain;

import com.studentinformation.domain.form.LoginMemberForm;
import com.studentinformation.domain.form.MemberForm;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "student_num")
    private String studentNum;

    private String password;

    @Column(name = "member_name")
    private String memberName;

    @Enumerated(EnumType.STRING)
    private MemberState state;

    @Column(name = "college_name")
    private String collegeName;

    @OneToMany(mappedBy = "student")
    private List<Application> applications;

    @OneToMany(mappedBy = "student")
    private List<Grade> grades;

    @OneToMany(mappedBy = "professor")
    private List<Lecture> professorLectures;

    public Member(String studentNum, String password, String memberName, MemberState state, String collegeName) {
        this.createDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
        this.studentNum = studentNum;
        this.password = password;
        this.memberName = memberName;
        this.state = state;
        this.collegeName = collegeName;
        this.applications = new ArrayList<>();
        this.grades = new ArrayList<>();
        this.professorLectures = new ArrayList<>();
    }


    public void update(Member member){
        this.lastModifiedDate = LocalDateTime.now();
        this.studentNum = member.getStudentNum();
        this.password = member.getPassword();
        this.memberName = member.getMemberName();
        this.state = member.getState();
        this.collegeName = member.getCollegeName();
    }

    public void changePassword(String newPassword){
        this.lastModifiedDate = LocalDateTime.now();
        this.password = newPassword;
    }

    public boolean checkFirstAccess(){
        return createDate.equals(lastModifiedDate);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", studentNum='" + studentNum + '\'' +
                ", password='" + password + '\'' +
                ", memberName='" + memberName + '\'' +
                ", state=" + state +
                ", collegeName='" + collegeName + '\'' +
                ", createDate=" + createDate +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }

    public MemberForm getMemberForm() {
        return new MemberForm(studentNum, memberName, state, collegeName);
    }
}
