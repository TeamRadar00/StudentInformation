package com.studentinformation.domain;

import lombok.Getter;

@Getter
public enum Score {

    P("P", 0f), NP("NP", 0f),
    A_PLUS("A+", 4.5f), A_ZERO("A0", 4f),
    B_PLUS("B+", 3.5f), B_ZERO("B0", 3f),
    C_PLUS("C+", 2.5f), C_ZERO("C0", 2f),
    D_PLUS("D+", 1.5f), D_ZERO("D0", 1f),
    F("F", 0f);


    private final String name;
    private final float score;

    Score(String name, float score) {
        this.name = name;
        this.score = score;
    }
}
