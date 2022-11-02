package com.studentinformation.util;

import com.studentinformation.security.PrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@RequiredArgsConstructor
public class WithCustomMemberSecurityContextFactory implements WithSecurityContextFactory<WithCustomMember> {

    private final PrincipalDetailsService service;

    @Override
    public SecurityContext createSecurityContext(WithCustomMember customMember) {
        UserDetails principal = service.loadUserByUsername(customMember.value());
        Authentication auth =
                new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        return context;
    }

}
