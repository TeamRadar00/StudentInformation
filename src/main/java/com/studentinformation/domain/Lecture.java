package com.studentinformation.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.OffsetTime;
import java.util.List;
import java.util.Set;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecture extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "lecture_id")
    private Long id;

    @Column(name = "lecture_name")
    private String lectureName;

    @ManyToOne(fetch = LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member professor;

    private String semester; //년도 + 학기 : 2022/1(22년 1학기), 2022/2(22년 2학기)

    @Enumerated(EnumType.STRING)
    private Week week;

    private OffsetTime time;

    @Column(name = "limit_num")
    private int limitNum;

    @OneToMany(mappedBy = "lecture")
    private List<Application> applications;

    public Lecture(String lectureName, Member professor, String semester, Week week, OffsetTime time, int limitNum) {
        this.createDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
        this.lectureName = lectureName;
        this.professor = professor;
        this.semester = semester;
        this.week = week;
        this.time = time;
        this.limitNum = limitNum;
    }

    public Lecture update(Lecture newLecture){
        this.lastModifiedDate = LocalDateTime.now();
        this.lectureName = newLecture.getLectureName();
        this.professor = newLecture.getProfessor();
        this.semester = newLecture.getSemester();
        this.week = newLecture.getWeek();
        this.time = newLecture.getTime();
        this.limitNum = newLecture.getLimitNum();
        return this;
    }

    public boolean checkCurrentCountUnderLimitNum(){
        return applications.size()<limitNum;
    }
}
