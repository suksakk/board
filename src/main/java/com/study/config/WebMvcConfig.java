package com.study.config;

import com.study.interceptor.LoggerInterceptor;
import com.study.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggerInterceptor())
                .excludePathPatterns("/css/**", "/images/**", "/js/**");

        // addInterceptor() 이전에 로그용 인터셉터를 처리하는 과정에서 구현한 오버라이드 메서드
        // 애플리케이션 내의 모든 페이지(URI)에 접근할 때 LoginCheckInterceptor의 preHandle()이 작동
        // excludePathPatterns()를 이용해서 로그인 페이지와 로그인/로그아웃은 URI는 인터셉터 실행에서 제외
        // 만약 로그인/로그아웃 관련 URI를 호출했을 때 인터셉터가 작동하게 되면, LoginCheckInterceptor의 preHandle()에 의해 계속해서 로그인 페이지를 호출하는 무한 루프에 빠진다.
        registry.addInterceptor(new LoginCheckInterceptor())
                .addPathPatterns("/**/*.do")
                .excludePathPatterns("/log*");
    }

}