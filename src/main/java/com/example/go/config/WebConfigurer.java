package com.example.go.config;

import com.example.go.interceptor.ManagerLoginInterceptor;
import com.example.go.interceptor.UserLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {
    /**
     * 拦截器注册
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor( new ManagerLoginInterceptor()).addPathPatterns("/manager/**");
        registry.addInterceptor( new UserLoginInterceptor()).addPathPatterns("/user/**");
    }
}
