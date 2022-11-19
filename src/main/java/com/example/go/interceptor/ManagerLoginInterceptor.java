package com.example.go.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 后台管理员拦截器
 */
public class ManagerLoginInterceptor implements HandlerInterceptor {
    /**
     * 发送请求处理前调用
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("管理员登录拦截器");
        return true;
    }
}
