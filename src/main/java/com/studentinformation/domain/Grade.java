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
public class Grade extends BaseEntity{

    @Id@GeneratedValue
    @Column(name = "grade_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member student;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @Enumerated(EnumType.STRING)
    private Score score;

    @Column(length = 1000)
    private String objection;

    private boolean objectionCheck;

    public void setStudent(Member student){
        this.student = student;
        student.getGrades().add(this);
    }

    public void setLecture(Lecture lecture){
        this.lecture = lecture;
        lecture.getGrades().add(this);
    }

    public Grade(Member student, Lecture lecture) {
        this.createDate = this.lastModifiedDate =LocalDateTime.now();
        setStudent(student);
        setLecture(lecture);
        objectionCheck=false;
    }

    public void updateGrade(Score score) {
        this.lastModifiedDate = LocalDateTime.now();
        this.score = score;
    }

    public void updateObjection(String objection){
        this.lastModifiedDate = LocalDateTime.now();
        this.objection = objection;
    }

    public static Grade createEmptyGrade(Member student, Lecture lecture) {
        Grade emptyGrade = new Grade(student, lecture);
        emptyGrade.score = Score.IN;
        return emptyGrade;
    }


    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", score=" + score +
                ", objection='" + objection + '\'' +
                ", createDate=" + createDate +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}
