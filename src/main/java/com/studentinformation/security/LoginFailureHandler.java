package com.studentinformation.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final String defaultFailureUrl;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (exception.getClass().equals(UsernameNotFoundException.class)) {
            response.sendRedirect(defaultFailureUrl + "?memberNumBlank");
        } else if (exception.getClass().equals(AuthenticationCredentialsNotFoundException.class)) {
            response.sendRedirect(defaultFailureUrl + "?passwordBlack");
        } else if (exception.getClass().equals(BadCredentialsException.class)) {
            response.sendRedirect(defaultFailureUrl + "?notMatched");
        } else {
            response.sendRedirect(defaultFailureUrl);
        }
    }
}
