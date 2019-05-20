package com.hogan.framework.ldap;

import com.hogan.framework.user.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import java.util.List;

/**
 * ClassName:LdapService
 * Description:LdapService
 * User:dada
 * Date:2018/04/28
 */
@Service
@Transactional
public class LdapService {

    @Autowired
    private Environment env;

    @Autowired
    private ContextSource contextSource;

    @Autowired
    private LdapTemplate ldapTemplate;

    /**
     * 查询域用户列表
     */
    public List<User> getLdapUserList(String key) throws Exception {
        // 过滤条件
        String filter;
        if (StringUtils.isBlank(key)) {
            filter = "(&(objectClass=User))";
        } else {
            filter = "(&(objectClass=User)(|(sAMAccountName=" + key + ")(name=" + key + ")(mail=" + key + ")))";
        }

        // 递归查询
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        // 结果映射
        ldapTemplate.setIgnorePartialResultException(true);

        return ldapTemplate.search("", filter, controls, new AttributesMapper<User>() {
            @Override
            public User mapFromAttributes(Attributes attributes) throws NamingException {
                User user = new User();

                // 用户账户
                user.setAccount(attributes.get("sAMAccountName") != null ? attributes.get("sAMAccountName").get().toString() : null);

                // 用户名称（中文）
                user.setUserName(attributes.get("displayName") != null ? attributes.get("displayName").get().toString() : null);

                // 电子邮箱
                user.setEmail(attributes.get("mail") != null ? attributes.get("mail").get().toString() : null);

                // 部门信息
                String distinguishedName = attributes.get("distinguishedName").toString();
                int begin = distinguishedName.indexOf("OU=");
                int end = distinguishedName.indexOf(",", begin);
                String department = distinguishedName.substring(begin + 3, end);
                user.setRemark(department);

                return user;
            }
        });
    }

    /**
     * 域登录认证
     */
    public DirContext getContext(String account, String password) throws NamingException {
        String base = env.getProperty("ldap_base");
        String principal = new StringBuilder().append(account).append("@").append(base).toString();
        return contextSource.getContext(principal, password);
    }
}
