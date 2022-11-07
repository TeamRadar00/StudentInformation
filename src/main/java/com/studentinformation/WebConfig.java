package com.studentinformation;

import com.studentinformation.web.converter.ScoreRequestConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new ScoreRequestConverter());
    }


    /**
     * ArgumentResolver 가 아닌 어노테이션으로 처리해서 주석처리함
     */
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        resolvers.add(new LoginMemberArgumentResolver());
//    }

    /**
     * spring security 가 filter 기반으로 작동하기 때문에 로그인 확인 interceptor 주석처리함
     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
////        registry.addInterceptor(new LoginInterceptor())
////                .order(1)
////                .addPathPatterns("/**")
////                .excludePathPatterns("/css/**", "/*.ico", "/error");
//
//        registry.addInterceptor(new LoginCheckInterceptor())
//                .order(1)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/members/add", "/members/login", "/members/logout", "/members/find-*",
//                        "/css/**", "/*.ico", "/error");
//    }
}
