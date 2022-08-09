package com.studentinformation.domain;

import lombok.Getter;

@Getter
public enum MemberState {
    inSchool("재학"), outSchool("휴학"), professor("교수"), admin("운영자");

    private final String description;

    MemberState(String description) {
        this.description = description;
    }
}
