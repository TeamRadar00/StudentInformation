package com.studentinformation.security;


import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;


@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalDetailsService service;

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

    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher(){
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
                .antMatchers("/members/login**").permitAll()
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
                    .defaultSuccessUrl("/home") //로그인 성공하면 "/home"로 이동
//                    .successHandler(new LoginSuccessHandler()) // 로그인 전의 페이지로 리다이렉트
//                .failureHandler(new LoginFailureHandler())
                    .failureUrl("/members/login") //로그인 실패하면 /member/login으로 이동
            .and()
                .logout()
                    .logoutUrl("/members/logout")
                    .logoutSuccessUrl("/members/login")
                /**
                 *  스프링 시큐리티에서는 기본적으로 로그인하지 않은 사용자를 anonymous 인증 객체를 생성해서 사용한다.
                 *  사용자가 anonymous 즉 익명 객체인 경우 AuthenticationEntryPoint 를 통해 처리하지만
                 *  인증 객체는 accessDeniedHandler 를 사용한다.
                 *  여기서 accessDeniedHandler 가 설정되지 않는다면 AccessDeniedHandlerImpl 구현체가 실행된다.
                 *  이 구현체에서 403 forbidden 던짐
                 */
            .and()
                .exceptionHandling().accessDeniedHandler(new WebAccessDeniedHandler());


        http.sessionManagement()
                .maximumSessions(1)//동시에 여러 로그인하는것을 막음.
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry());

    }

    @Bean
    public ValidationAuthenticationProvider validationProvider() throws Exception{
        ValidationAuthenticationProvider provider = new ValidationAuthenticationProvider();
        provider.setPasswordEncoder(encodePassword());
        provider.setUserDetailsService(service);
        return provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(validationProvider());
    }


}