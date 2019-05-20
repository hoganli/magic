package com.hogan.common.exception;

import com.hogan.common.util.ResponseUtil;
import com.hogan.framework.FrameworkConstants;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ClassName:BaseExceptionHandler
 * Description:全局异常处理
 * User:dada
 * Date:2018/5/14 15:24
 */
@ControllerAdvice
public class BaseExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(BaseExceptionHandler.class);

    /**
     * shiro权限认证失败
     */
    @ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
    public void authorizationException(HttpServletRequest request, HttpServletResponse response) {
        ResponseUtil.responseJson(response, 403, false, FrameworkConstants.ERROR_MSG_AUTHZ_FAILED);
    }
}
