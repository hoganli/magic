package com.hogan.common.shiro;

import com.hogan.common.util.ResponseUtil;
import com.hogan.framework.FrameworkConstants;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * ClassName:ShiroAuthenticationFilter
 * Description: 自定义登录认证过滤器
 * User:hogan.li
 * Date:2018/5/11 11:32
 */

public class ShiroAuthenticationFilter extends FormAuthenticationFilter {

    private static Logger log = LoggerFactory.getLogger(ShiroAuthenticationFilter.class);

    /**
     * 登录拦截处理
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletResponse res = (HttpServletResponse) response;
        ResponseUtil.responseJson(res, 401, false, FrameworkConstants.ERROR_MSG_AUTHC_FAILED);

        return false;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return super.isAccessAllowed(request, response, mappedValue);
    }

    /**
     * 通知springboot不要自动注册filter，而是通过Shiro来管理我们的自定义Filter
     */
//    @Bean
//    public FilterRegistrationBean registration(ShiroAuthenticationFilter filter) {
//        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
//        registration.setEnabled(false);
//        return registration;
//    }
}
