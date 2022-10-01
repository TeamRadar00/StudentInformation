package com.studentinformation.web.argumentResolver;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @AuthenticationPrincipal spring security가 세션에 저장한 멤버 정보를 가져옴
 * Authentication.getPrincipal()
 * principal.getMember()
 * 와 같음
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal(expression = "member")
public @interface Login {

}
