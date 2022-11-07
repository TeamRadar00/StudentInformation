package com.studentinformation.security;


import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;



/**
 *  AccessDeniedHandler
 *  스프링 시큐리티에서는 기본적으로 로그인하지 않은 사용자를 anonymous 인증 객체를 생성해서 사용한다.
 *  사용자가 anonymous 즉 익명 객체인 경우 AuthenticationEntryPoint 를 통해 처리하지만
 *  인증 객체는 accessDeniedHandler 를 사용한다.
 *  여기서 accessDeniedHandler 가 설정되지 않는다면 AccessDeniedHandlerImpl 구현체가 실행된다.
 *  이 구현체에서 403 forbidden 던짐
 *
 *
 *  ConcurrencyLoginController
 *  maximumSessions 설정은 -1일때는 무제한으로 0이 아닌 n일때는 한 아이디에 최대 n개의 세션이 만들어질 수 있음
 *  maxSessionsPreventsLogin이 false면 다른 브라우저에서 로그인하면 처음 로그인한 브라우저의 세션이 만료되서 로그아웃됨
 *
 */



@Configuration
@EnableWebSecurity
public class SecurityConfig extends BasicSecurityUrl {

    private PrincipalDetailsService service;
    private ValidationAuthenticationProvider validationAuthenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.csrf().disable()
                .authorizeHttpRequests((authorize) -> authorize
                                .antMatchers(ADMIN_ACCESS).hasRole("ADMIN")
                                .antMatchers(PROFESSOR_ACCESS).hasAnyRole("PROFESSOR", "ADMIN")
                                .antMatchers(STUDENT_ACCESS).hasAnyRole("INSCHOOL","OUTSCHOOL", "ADMIN")
                                .antMatchers(AUTH_WHITELIST).permitAll()
                                .anyRequest().authenticated()
                                // 나머지 주소는 인증해야 접근 가능
                )
                .formLogin((form) -> form
                        .loginPage("/members/login") // 인증이 필요한 URL에 접근하면 /members/login으로 이동
                        .usernameParameter("studentNum") //로그인 시 form에서 가져올 값
                        .passwordParameter("password") //로그인시 form에서 가져올 값
                        .loginProcessingUrl("/member_login") //로그인시 처리할 URL 입력
                        .defaultSuccessUrl("/home") //로그인 성공하면 "/home"로 이동
                        .successHandler(new LoginSuccessHandler()) // 로그인 전의 페이지로 리다이렉트
                        .failureHandler(new LoginFailureHandler())
                        .failureUrl("/members/login") //로그인 실패하면 /member/login으로 이동
                )
                .logout((logout) -> logout
                        .logoutUrl("/members/logout")
                        .logoutSuccessUrl("/members/login")
                )
                .exceptionHandling((handler) -> handler
                        .accessDeniedHandler(new WebAccessDeniedHandler())
                )
                .sessionManagement((session) -> session
                        .maximumSessions(1)//동시에 여러 로그인하는것을 막음.
                        .maxSessionsPreventsLogin(false)
                        .sessionRegistry(sessionRegistry())
                );

        return http.build();
    }




    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception{
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        ValidationAuthenticationProvider validationProvider = new ValidationAuthenticationProvider();
        validationProvider.setPasswordEncoder(encodePassword());
        validationProvider.setUserDetailsService(service);
        authenticationManagerBuilder.authenticationProvider(validationProvider);

        return authenticationManagerBuilder.build();
    }


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

}