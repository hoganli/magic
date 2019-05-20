package com.hogan.framework.login;

import com.hogan.common.base.ReturnVO;
import com.hogan.framework.FrameworkConstants;
import com.hogan.framework.user.User;
import com.hogan.framework.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * ClassName:LoginController
 * Description:LoginController
 * User:hogan.li
 * Date:2017/8/11 14:38
 */
@RestController
@RequestMapping(value = "/ep/api")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ReturnVO login(@RequestHeader("Digest") String digest, @RequestBody Map<String, Object> paramMap) {

        ReturnVO vo = new ReturnVO();

        try {
            String account = (String) paramMap.get("account");
            String password = (String) paramMap.get("password");
            User user = loginService.login(account, password);
            vo.setData(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }

    /**
     * 用户退出
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public ReturnVO logout() {

        ReturnVO vo = new ReturnVO();

        try {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }

    /**
     * 登录校验
     */
    @RequestMapping(value = "/loginCheck", method = RequestMethod.POST)
    @ResponseBody
    public ReturnVO loginCheck(@RequestBody Map<String, Object> paramMap) {

        ReturnVO vo = new ReturnVO();

        try {
            String account = (String) paramMap.get("account");
            String password = (String) paramMap.get("password");
            Boolean flag = userService.checkPassword(account, password);
            if (!flag) {
                vo.setSuccess(false);
                vo.setMessage(FrameworkConstants.ERROR_MSG_LOGIN_ACCOUNT_INVALID);
            } else {
//                String dateFlag = userService.checkUserUpdateDate(account);
//                if (StringUtils.isNotBlank(dateFlag)) {
//                    vo.setData(true);
//                    vo.setMessage("0".equals(dateFlag) ? FrameworkConstants.ERROR_MSG_FORCE_PASSWORD_CHANGE : FrameworkConstants.ERROR_MSG_FIRST_LOGIN_FORCE_CHANGE);
//                }
            }                                            
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;

    }
}
