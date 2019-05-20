package com.hogan.common.shiro;

import com.hogan.common.util.DigestUtil;
import com.hogan.framework.FrameworkConstants;
import com.hogan.framework.ldap.LdapService;
import com.hogan.framework.user.User;
import com.hogan.framework.user.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;
import java.util.Map;

/**
 * ClassName:UserRealm
 * Description: UserRealm
 * User:dada
 * Date:2018/5/11 11:32
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private LdapService ldapService;

    @Autowired
    @Lazy
    private UserService userService;

    /**
     * 为用户设置权限，使controller的权限注解生效
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        Session session = SecurityUtils.getSubject().getSession();
        User user = (User) session.getAttribute("user");

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addStringPermissions(user.getPermissions());

        return authorizationInfo;
    }

    /**
     * 验证当前登录的Subject:login()方法中执行Subject.login()时执行此方法
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {

        // 获取用户账号密码
        String account = (String) authcToken.getPrincipal();
        String password = new String((char[]) authcToken.getCredentials());

        // 先查数据库是否有这个用户登录名
        User user;

        try {
            user = userService.findByAccount(account);
            if (user == null) {
                throw new AuthenticationException(FrameworkConstants.ERROR_MSG_USER_NON_EXISTENT);
            }
        } catch (Exception e) {
            throw new AuthenticationException(FrameworkConstants.ERROR_MSG_USER_NON_EXISTENT);
        }

        // 该用户是否被停用
        if (!user.getState()) {
            throw new AuthenticationException(FrameworkConstants.ERROR_MSG_LOGIN_ACCOUNT_DISABLED);
        }

        try {
            // 密码摘要（sha256（account_password）），其中password已经在前端进行过sha256摘要
            String dbPassword = DigestUtil.SHA256ToHex(account + "_" + password);
            // 本地认证
            if (!dbPassword.equals(user.getPassword())) {
                throw new AuthenticationException(FrameworkConstants.ERROR_MSG_LOGIN_ACCOUNT_INVALID);
            }
        } catch (Exception e) {
            throw new AuthenticationException(FrameworkConstants.ERROR_MSG_LOGIN_ACCOUNT_INVALID);
        }

        /*try {
            // 管理员使用数据库认证，普通用户使用域认证
            if (FrameworkConstants.ADMIN.equals(account) || FrameworkConstants.AUDITOR.equals(account)) {
                // 密码摘要（sha256（account_password）），其中password已经在前端进行过sha256摘要
                String dbPassword = DigestUtil.SHA256ToHex(account + "_" + password);
                // 本地认证
                if (!dbPassword.equals(user.getPassword())) {
                    throw new AuthenticationException(FrameworkConstants.ERROR_MSG_LOGIN_ACCOUNT_INVALID);
                }
            } else {
                // 密码解密（AES.AESDecryptBase64(password,secret)），其中secret即用户账户经过SHA56ToHex摘要后取前16字节
                String plainPassword = AesUtil.AESDecryptBase64(password, account);

                // 域认证
                DirContext context = null;
                try {
                    context = ldapService.getContext(account, plainPassword);
                } catch (Exception e) {
                    throw new AuthenticationException(FrameworkConstants.ERROR_MSG_LOGIN_ACCOUNT_INVALID);
                } finally {
                    if (context != null) {
                        LdapUtils.closeContext(context);
                    }
                }
            }
        } catch (Exception e) {
            throw new AuthenticationException(FrameworkConstants.ERROR_MSG_LOGIN_ACCOUNT_INVALID);
        }*/

        // 过了校验以后查询用户的菜单和权限
        Map<String, List<String>> map = null;
        try {
            map = userService.findUserMenusAndPermissions(user);
            user.setMenus(map.get("menus"));
            user.setPermissions(map.get("permissions"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        // 清掉敏感数据
//        user.setPassword("");

        // 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(account, password, getName());

        // 把用户对象放到shiro的会话中
        SecurityUtils.getSubject().getSession().setAttribute("user", user);

        return authenticationInfo;
    }
}
