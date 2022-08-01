package com.studentinformation.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.OffsetTime;

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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member professor;

    private String semester;

    @Enumerated(EnumType.STRING)
    private Week week;

    private OffsetTime time;

    @Column(name = "limit_num")
    private int limitNum;

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
}
