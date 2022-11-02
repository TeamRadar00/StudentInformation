package com.studentinformation;

import com.studentinformation.service.ApplicationService;
import com.studentinformation.service.GradeService;
import com.studentinformation.service.LectureService;
import com.studentinformation.service.MemberService;
import com.studentinformation.web.initDB.InitLocalDB;
import com.studentinformation.web.initDB.InitTestDB;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class StudentInformationApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentInformationApplication.class, args);
    }

    @Bean
    @Profile("local")
    public InitLocalDB initializeLocalDBData(MemberService memberService, LectureService lectureService,
                                             ApplicationService applicationService, GradeService gradeService) {
        return new InitLocalDB(memberService, lectureService, applicationService, gradeService);
    }

    @Bean
    @Profile("test")
    public InitTestDB initializeTestDBData(MemberService memberService, LectureService lectureService,
                                           ApplicationService applicationService, GradeService gradeService) {
        return new InitTestDB(memberService, lectureService, applicationService, gradeService);
    }


}