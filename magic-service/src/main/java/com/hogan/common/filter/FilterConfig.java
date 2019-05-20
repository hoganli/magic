package com.hogan.common.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName:FilterConfig
 * Description:注册自定义过滤器
 * User:dada
 * Date:2018/5/15 11:43
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean registration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new RequestValidateFilter());        //添加过滤器
        registration.addUrlPatterns("/*");                          //设置过滤规则
        return registration;
    }
}
