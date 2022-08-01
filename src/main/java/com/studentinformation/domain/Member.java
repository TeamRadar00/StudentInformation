package com.studentinformation.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
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


    public Member(String studentNum, String password, String memberName, MemberState state, String collegeName) {
        this.createDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
        this.studentNum = studentNum;
        this.password = password;
        this.memberName = memberName;
        this.state = state;
        this.collegeName = collegeName;
    }
}
