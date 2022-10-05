package com.studentinformation.security;

import com.studentinformation.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;


//@Component
@Slf4j
public class WebAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());

        if(accessDeniedException instanceof AccessDeniedException){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            request.setAttribute("msg","접근 권한 없는 사용자입니다.");
            request.setAttribute("newPage","/members/login");
        }else{
            log.info(accessDeniedException.getClass().getCanonicalName());
        }
        request.getRequestDispatcher("/home").forward(request, response);
    }
}
