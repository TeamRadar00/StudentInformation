package com.studentinformation.domain;

import lombok.Getter;

@Getter
public enum MemberState {
    inSchool("ROLE_INSCHOOL", "재학"),
    outSchool("ROLE_OUTSCHOOL","휴학"),
    professor("ROLE_PROFESSOR","교수"),
    admin("ROLE_ADMIN","운영자");

    private final String code;
    private final String description;

    MemberState(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
