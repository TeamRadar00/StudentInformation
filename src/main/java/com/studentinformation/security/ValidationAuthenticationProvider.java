package com.studentinformation.security;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;


public class ValidationAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String memberNum = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (!StringUtils.hasText(memberNum)) {
            throw new UsernameNotFoundException("학번을 입력해주세요.");
        } else if (!StringUtils.hasText(password)) {
            throw new AuthenticationCredentialsNotFoundException("비밀번호를 입력해주세요.");
        }

        return super.authenticate(authentication);
    }

}
