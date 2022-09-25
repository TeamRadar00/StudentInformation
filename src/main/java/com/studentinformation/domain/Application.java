package com.studentinformation.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@EqualsAndHashCode(callSuper = true)
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

    public void setStudent(Member student){
        this.student=student;
        student.getApplications().add(this);
    }

    public void setLecture(Lecture lecture){
        this.lecture = lecture;
        lecture.getApplications().add(this);
    }


    public Application(Member student, Lecture lecture) {
        this.createDate = this.lastModifiedDate =LocalDateTime.now();
        setStudent(student);
        setLecture(lecture);
    }


    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}
