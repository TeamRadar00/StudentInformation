package com.studentinformation.security;

public class BasicSecurityUrl {

    private static final String GRADE_URL = "/grade";
    private static final String LECTURE_URL = "/lectures";
    private static final String APPLICATION_URL = "/applications";

    protected static final String[] AUTH_WHITELIST = {
            "/members/find-**",
            "/members/login",

            "/assets/**",
            "/css/**",
            "/js/**"
    };

    protected static final String[] ADMIN_ACCESS = {
            "/admin"
    };

    protected static final String[] PROFESSOR_ACCESS = {
            GRADE_URL+"/readObjection/**",
            GRADE_URL+"/objectionList/**",
            GRADE_URL+"/giveGrade",

            LECTURE_URL,
            LECTURE_URL+"/new",
            LECTURE_URL+"/{lectureId}/edit"
    };

    protected static final String[] STUDENT_ACCESS = {
            GRADE_URL+"/myGrade",
            GRADE_URL+"/objection",

            LECTURE_URL+"/my",

            APPLICATION_URL,
            APPLICATION_URL+"/{lectureId}/new"
    };

}
