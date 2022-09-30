package com.studentinformation.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private static final String GRADE_URI = "/grade";
    private static final String LECTURE_URI = "/lectures";
    private static final String APPLICATION_URI = "/applications";

    private static final String[] AUTH_WHITELIST = {
            "/assets/**",
            "/css/**",
            "/js/**"
    };

    private static final String[] ADMIN_ACCESS = {
            "/admin",

            LECTURE_URI,
            LECTURE_URI+"/new",
            LECTURE_URI+"/{lectureId}/edit",

            APPLICATION_URI,
            APPLICATION_URI+"/{lectureId}/new"
    };

    private static final String[] PROFESSOR_ACCESS = {
            GRADE_URI+"/readObjection/**",
            GRADE_URI+"/objectionList/**",
            GRADE_URI+"/giveGrade",

            LECTURE_URI,
            LECTURE_URI+"/new",
            LECTURE_URI+"/{lectureId}/edit"
    };

    private static final String[] STUDENT_ACCESS = {
            GRADE_URI+"/myGrade",
            GRADE_URI+"/objection",

            LECTURE_URI+"/my",

            APPLICATION_URI,
            APPLICATION_URI+"/{lectureId}/new"
    };


    @Bean
    public BCryptPasswordEncoder encodePassword(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
                .antMatchers("/members/login").permitAll()
                .antMatchers(ADMIN_ACCESS).hasRole("ADMIN")
                .antMatchers(PROFESSOR_ACCESS).hasRole("PROFESSOR")
                    // 교수님만 접근가능
                .antMatchers(STUDENT_ACCESS).hasAnyRole("INSCHOOL","OUTSCHOOL")
                    // 학생만 접근 가능
                .antMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()
                    // 나머지 주소는 인증해야 접근 가능
            .and()
                .formLogin() //form 기반의 로그인
                    .loginPage("/members/login") // 인증이 필요한 URL에 접근하면 /members/login으로 이동
                    .usernameParameter("studentNum") //로그인 시 form에서 가져올 값
                    .passwordParameter("password") //로그인시 form에서 가져올 값
                    .loginProcessingUrl("/member_login") //로그인시 처리할 URL 입력
                    .defaultSuccessUrl("/home") //로그인 성공하면 "/"로 이동
                    .failureUrl("/members/login") //로그인 실패하면 /member/login으로 이동
            .and()
                .logout()
                    .logoutUrl("/members/logout")
                    .logoutSuccessUrl("/members/login");
    }
}
