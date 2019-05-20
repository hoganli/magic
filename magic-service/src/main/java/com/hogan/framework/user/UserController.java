package com.hogan.framework.user;


import com.hogan.common.base.ReturnVO;
import com.hogan.common.util.DateUtil;
import com.hogan.framework.FrameworkConstants;
import com.hogan.framework.ldap.LdapService;
import com.hogan.framework.userRole.UserRoleService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * ClassName:UserController
 * Description:UserController
 * User:hogan.li
 * Date:2018/07/18
 */
@RestController
@RequestMapping(value = "/magic/api")
public class UserController {

	private static Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

    @Autowired
    private LdapService ldapService;

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 更新User
     */
    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    @RequiresPermissions("user:update")
    @ResponseBody
    public ReturnVO updateUser(@RequestBody User user) {

        ReturnVO vo = new ReturnVO();

        try {
            User currentUser = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
            user.setUpdateDate(DateUtil.getCurrentTimeStamp().toString());
            user.setUpdateBy(currentUser.getId());
            // 更新用户
            userService.update(user);
            // 更新用户与角色关联关系
            userRoleService.updateUserRole(user.getId(), user.getRoleIds());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }

    /**
     * 删除User
     */
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.DELETE)
    @RequiresPermissions("user:delete")
    @ResponseBody
    public ReturnVO deleteUser(@PathVariable("userId") String userId) {

        ReturnVO vo = new ReturnVO();

        try {
            userService.deleteById(userId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }

    /**
     * 删除User列表
     */
    @RequestMapping(value = "/users/batch", method = RequestMethod.POST)
    @RequiresPermissions("user:delete")
    @ResponseBody
    public ReturnVO deleteUserList(@RequestBody List<String> ids) {

        ReturnVO vo = new ReturnVO();

        try {
            userService.deleteByIds(ids);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }

    /**
     * 查询本地User列表分页
     */
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @RequiresPermissions(value = {"user:list", "customer:list"}, logical = Logical.OR)
    @ResponseBody
    public ReturnVO findUserList(@RequestBody Map<String, Object> paramMap) {

        ReturnVO vo = new ReturnVO();

        try {
            Page<User> page = userService.findUserByCondition(paramMap);
            vo.setDataList(page.getContent());
            vo.setTotalProperty(page.getTotalElements());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }
    
    /**
     * 根据id查询User对象
     */
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public ReturnVO getUser(@PathVariable("userId") String userId) {

        ReturnVO vo = new ReturnVO();

        try {
            User user = userService.findById(userId);
            if (user != null) {
                vo.setData(user);
            } else {
                vo.setSuccess(false);
                vo.setMessage(FrameworkConstants.ERROR_MSG_RECORD_NOT_EXIST);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }

    /**
     * 查询所有User对象(除了管理员、审计员)
     */
    @RequestMapping(value = "/allUsers", method = RequestMethod.GET)
    @ResponseBody
    public ReturnVO allUsers() {

        ReturnVO vo = new ReturnVO();

        try {
            List<User> user = userService.findByAccountNotIn(Arrays.asList(FrameworkConstants.ADMIN, FrameworkConstants.AUDITOR));
            if (user != null) {
                vo.setDataList(user);
            } else {
                vo.setSuccess(false);
                vo.setMessage(FrameworkConstants.ERROR_MSG_RECORD_NOT_EXIST);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }

    /**
     * 查询域用户列表
     */
    @RequestMapping(value = "/ldapUsers", method = RequestMethod.GET)
    @RequiresPermissions("user:sync")
    @ResponseBody
    public ReturnVO findLdap(@RequestParam Map<String, Object> paramMap) {

        ReturnVO vo = new ReturnVO();

        try {
            String name = (String) paramMap.get("name");
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("name不能为空!");
            }
            List<User> dataList = ldapService.getLdapUserList(name);
            vo.setDataList(dataList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }

    /**
     * 同步域用户
     */
    @RequestMapping(value = "/syncLdapUser", method = RequestMethod.POST)
    @RequiresPermissions("user:sync")
    @ResponseBody
    public ReturnVO addUser(@RequestBody User user) {

        ReturnVO vo = new ReturnVO();

        try {
            userService.syncLdapUser(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }

    /**
     * 根据account判断域用户是否已经同步到本地
     */
    @RequestMapping(value = "/isUserExist", method = RequestMethod.GET)
    @RequiresPermissions("user:sync")
    @ResponseBody
    public ReturnVO getUserByAccount(@RequestParam String account) {

        ReturnVO vo = new ReturnVO();

        try {
            User user = userService.findByAccount(account);
            vo.setData(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }

    /**
     * 校验用户密码
     */
    @RequestMapping(value = "/checkPassword", method = RequestMethod.POST)
    @ResponseBody
    public ReturnVO checkPassword(@RequestBody Map<String, Object> paramMap) {

        ReturnVO vo = new ReturnVO();

        try {
            String account = (String) paramMap.get("account");
            String password = (String) paramMap.get("password");
            Boolean flag = userService.checkPassword(account, password);
            if (!flag) {
                vo.setMessage(FrameworkConstants.ERROR_MSG_CHECK_PASSWORD_FAILURE);
                vo.setSuccess(false);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;

    }

    /**
     * 修改用户密码
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public ReturnVO updatePassword(@RequestBody Map<String, Object> paramMap) {

        ReturnVO vo = new ReturnVO();

        try {
            String account = (String) paramMap.get("account");
            String oldPassword = (String) paramMap.get("oldPassword");
            Boolean flag = userService.checkPassword(account, oldPassword);
            if (!flag) {
                vo.setMessage(FrameworkConstants.ERROR_MSG_CHECK_PASSWORD_FAILURE);
                vo.setSuccess(false);
            } else {
                String password = (String) paramMap.get("password");
                userService.updatePassword(account, password);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;

    }

    /**
     * 重置用户密码
     */
    @RequestMapping(value = "/resetPassword/{userId}", method = RequestMethod.PUT)
    @RequiresPermissions("user:reset")
    @ResponseBody
    public ReturnVO updatePassword(@PathVariable("userId") String userId) {

        ReturnVO vo = new ReturnVO();

        try {
            userService.resetPasswordById(userId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;

    }
}