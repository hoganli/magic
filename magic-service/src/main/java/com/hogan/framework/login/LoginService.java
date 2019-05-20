package com.hogan.framework.login;

import com.hogan.framework.user.User;
import com.hogan.framework.user.UserDao;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ClassName:LoginService
 * Description:LoginService
 * User:dada
 * Date:2018/07/18
 */
@Service
@Transactional
public class LoginService {

    @Autowired
    private UserDao userDao;

    /**
     * 用shiro整合数据库和域登录
     */
    public User login(String account, String password) {

        Subject subject = SecurityUtils.getSubject();
        User user;

        try {
            // shiro的用户名密码凭证必须与登录完成后的校验信息中的用户名密码一致，否则会校验失败
            UsernamePasswordToken token = new UsernamePasswordToken(account, password);
            // 发起登录请求，调用UserRealm的doGetAuthenticationInfo方法，获取校验信息
            subject.login(token);

            // 登录成功后，将用户域密码（密文）保存到用户表
            //            if (!FrameworkConstants.ADMIN.equals(account)) {
            //                userDao.setPasswordFor(password, account);
            //            }

            // 登录成功后从shiro的会话中得到用户对象
            user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return user;
    }
}
