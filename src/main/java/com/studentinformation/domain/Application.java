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

    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member student;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column(length = 1000)
    private String objection;


    public void setStudent(Member student){
        this.student=student;
        student.getApplications().add(this);
    }

    public void setLecture(Lecture lecture){
        this.lecture = lecture;
        lecture.getApplications().add(this);
    }
    public Application(Member student, Lecture lecture) {
        this.createDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
        setStudent(student);
        setLecture(lecture);
    }

    public void updateGrade(Grade grade) {
        this.lastModifiedDate = LocalDateTime.now();
        this.grade = grade;
    }

    public void updateObjection(String objection){
        this.lastModifiedDate = LocalDateTime.now();
        this.objection = objection;
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", grade=" + grade +
                ", objection='" + objection + '\'' +
                ", createDate=" + createDate +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}
