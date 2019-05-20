package com.hogan.common.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * ClassName:BaseWebMvcConfiguer
 * Description: 自定义拦截器注册
 * User:hogan.li
 * Date:2018/5/14 17:46
 */
@Configuration
public class BaseWebMvcConfiguer extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截器：统一的请求预处理拦截器
//        registry.addInterceptor(new BaseRequestInterceptor());
        super.addInterceptors(registry);
    }
}
