package com.hogan.common.shiro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hogan.common.base.ReturnVO;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * ClassName:ShiroAuthorizationFilter
 * Description: 自定义权限认证过滤器（该类无效，改由通过全局异常进行处理）
 * User:hogan.li
 * Date:2018/5/11 11:32
 */
public class ShiroAuthorizationFilter extends PermissionsAuthorizationFilter {

    private static Logger log = LoggerFactory.getLogger(ShiroAuthorizationFilter.class);

    /**
     * 权限验证不通过处理
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {

        ReturnVO vo = new ReturnVO();
        PrintWriter out = null;
        HttpServletResponse res = (HttpServletResponse) response;

        try {
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json");
            // 设置响应代码403：权限认证失败
            res.setStatus(403);

            out = response.getWriter();
            vo.setSuccess(false);
            vo.setMessage("权限认证失败，请联系管理员！");

            ObjectMapper mapper = new ObjectMapper();
            out.println(mapper.writeValueAsString(vo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
        // true:继续往下执行，false:该filter过滤器已经处理，不继续执行其他过滤器
        return false;
    }

    /**
     * 权限验证
     */
    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        return super.isAccessAllowed(request, response, mappedValue);
    }

    /**
     * 通知springboot不要自动注册filter，而是通过Shiro来管理我们的自定义Filter
     */
    @Bean
    public FilterRegistrationBean registration(ShiroAuthorizationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }
}
