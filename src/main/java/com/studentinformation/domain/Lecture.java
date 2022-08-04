package com.studentinformation.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.Temporal;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.OffsetTime;
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

    public void setProfessor(Member professor){
        this.professor = professor;
        professor.getProfessorLectures().add(this);
    }

    public Lecture(String lectureName, Member professor, String semester, Week week, OffsetTime time, int limitNum) {
        this.createDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
        this.lectureName = lectureName;
        this.semester = semester;
        this.week = week;
        this.time = time;
        this.limitNum = limitNum;
        setProfessor(professor);
        this.applications = new ArrayList<>();
    }

    public void update(Lecture newLecture){
        this.lastModifiedDate = LocalDateTime.now();
        this.lectureName = newLecture.getLectureName();
        this.semester = newLecture.getSemester();
        this.week = newLecture.getWeek();
        this.time = newLecture.getTime();
        this.limitNum = newLecture.getLimitNum();
        setProfessor(newLecture.getProfessor());
    }

    public boolean checkCurrentCountUnderLimitNum(){
        return applications.size()<limitNum;
    }
}
