package com.studentinformation.security;

public class BasicSecurityUrl {

    private static final String GRADE_URL = "/grade";
    private static final String LECTURE_URL = "/lectures";
    private static final String APPLICATION_URL = "/applications";

    public static final String[] AUTH_WHITELIST = {
            "/assets/**",
            "/css/**",
            "/js/**"
    };

    public static final String[] ADMIN_ACCESS = {
            "/admin",

            LECTURE_URL,
            LECTURE_URL+"/new",
            LECTURE_URL+"/{lectureId}/edit",

            APPLICATION_URL,
            APPLICATION_URL+"/{lectureId}/new"
    };

    public static final String[] PROFESSOR_ACCESS = {
            GRADE_URL+"/readObjection/**",
            GRADE_URL+"/objectionList/**",
            GRADE_URL+"/giveGrade",

            LECTURE_URL,
            LECTURE_URL+"/new",
            LECTURE_URL+"/{lectureId}/edit"
    };

    public static final String[] STUDENT_ACCESS = {
            GRADE_URL+"/myGrade",
            GRADE_URL+"/objection",

            LECTURE_URL+"/my",

            APPLICATION_URL,
            APPLICATION_URL+"/{lectureId}/new"
    };

}
