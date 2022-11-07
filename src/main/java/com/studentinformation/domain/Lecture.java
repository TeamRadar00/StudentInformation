package com.studentinformation.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecture extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "lecture_id")
    private Long id;

    @Column(name = "lecture_name")
    private String lectureName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member professor;

    private String semester; //년도 + 학기 : 202201(22년 1학기), 202202(22년 2학기)

    // ex) ///12:00~12:50/12:00~12:50//
    private String time; // 각 요일마다 슬래시로 구분(총 6개 필요). 슬래시 사이에 값이 있다면 해당 시간이 수업시간(시작시간~종료시간).

    private int lectureCredit;

    @Column(name = "limit_num")
    private int limitNum;

    @OneToMany(mappedBy = "lecture")
    private List<Application> applications;

    @OneToMany(mappedBy = "lecture")
    private List<Grade> grades;

    public void setProfessor(Member professor){
        this.professor = professor;
        professor.getProfessorLectures().add(this);
    }

    public Lecture(String lectureName, Member professor, String semester, String time, int limitNum) {
        this.createDate = this.lastModifiedDate =LocalDateTime.now();
        this.lectureName = lectureName;
        this.semester = semester;
        this.time = time;
        this.limitNum = limitNum;
        calculateLectureCredit();
        setProfessor(professor);
        this.applications = new ArrayList<>();
        this.grades = new ArrayList<>();
    }

    public void update(Lecture newLecture){
        this.lastModifiedDate = LocalDateTime.now();
        this.lectureName = newLecture.getLectureName();
        this.semester = newLecture.getSemester();
        this.time = newLecture.getTime();
        this.limitNum = newLecture.getLimitNum();
        calculateLectureCredit();
        setProfessor(newLecture.getProfessor());
    }

    public boolean checkCurrentCountUnderLimitNum(){
        return applications.size()-1<limitNum;
    }

    private void calculateLectureCredit(){
        String[] split = time.split("/");
        int credit = 0;
        for (String s : split) {
            if(s.length()>=2){
                String[] token = s.split("~");
                String[] startTime = token[0].split(":");
                String[] endTime = token[1].split(":");
                credit =  credit + Integer.parseInt(endTime[0]) - Integer.parseInt(startTime[0])+1;
            }
        }
        this.lectureCredit = credit;
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "id=" + id +
                ", lectureName='" + lectureName + '\'' +
                ", professor=" + professor.getMemberName() +
                ", semester='" + semester + '\'' +
                ", time=" + time +
                ", limitNum=" + limitNum +
                ", createDate=" + createDate +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}
