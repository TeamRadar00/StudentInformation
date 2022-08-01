package com.studentinformation.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "applicaion_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member student;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    public Application(Member student, Lecture lecture) {
        this.createDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
        this.student = student;
        this.lecture = lecture;
    }

    public void updateGrade(Grade grade) {
        this.lastModifiedDate = LocalDateTime.now();
        this.grade = grade;
    }
}
