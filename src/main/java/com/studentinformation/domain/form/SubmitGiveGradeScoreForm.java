package com.studentinformation.domain.form;

import com.studentinformation.domain.Score;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SubmitGiveGradeScoreForm {
    private List<Score> scores;
}
