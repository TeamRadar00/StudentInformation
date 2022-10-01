package com.studentinformation.security;

import com.studentinformation.domain.Member;
import com.studentinformation.web.session.SessionConst;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //세션에다가 member정보 저장
        HttpSession session = request.getSession();
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = principal.getMember();
        session.setAttribute(SessionConst.LOGIN_MEMBER,member);


        // 로그인 전의 페이지로 리다이렉트
        setDefaultTargetUrl("/home");
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if(savedRequest!=null){
            // 인증 받기 전 url로 이동하기
            String targetUrl = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request,response,targetUrl);
        }else{
            // 기본 url로 이동
            redirectStrategy.sendRedirect(request,response,getDefaultTargetUrl());
        }

    }
}
