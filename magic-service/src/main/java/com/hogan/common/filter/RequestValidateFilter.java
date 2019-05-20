package com.hogan.common.filter;

import com.hogan.common.util.ResponseUtil;
import com.hogan.common.util.ValidateUtil;
import com.hogan.framework.FrameworkConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ClassName:RequestValidateFilter
 * Description:请求参数校验过滤器
 * User:dada
 * Date:2018/5/15 11:39
 */
public class RequestValidateFilter implements Filter {

    private static Logger log = LoggerFactory.getLogger(RequestValidateFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 请求的内容类型
        String contentType = request.getContentType();

        // 多媒体请求的过滤
        if(contentType == null || contentType.contains("multipart")){
            filterChain.doFilter(request, response);
            return;
        }

        // 非多媒体请求过滤
        RequestValidateWrapper requestWrapper = null;
        try {
            requestWrapper = new RequestValidateWrapper(request);

            // 获取摘要和参数
            String digest = requestWrapper.getHeader("Digest");
            String requestParamString = requestWrapper.getRequestParamString();

            // XSS非法字符过滤
            boolean xssResult = ValidateUtil.xssValidate(requestParamString);
            if (!xssResult) {
                ResponseUtil.responseJson(response, 400, false, FrameworkConstants.ERROR_MSG_PARAM_ILLEGALITY);
                return;
            }

            // 请求参数摘要认证
            Boolean digestResult = ValidateUtil.paramValidate(requestParamString, digest);
            if (digestResult) {
                filterChain.doFilter(requestWrapper, response);
            } else {
                ResponseUtil.responseJson(response, 400, false, FrameworkConstants.ERROR_MSG_PARAM_INVALID);
                return;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void destroy() {

    }
}
