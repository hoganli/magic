package com.hogan.framework.user;

import com.hogan.common.base.BaseServiceImpl;
import com.hogan.common.util.DateUtil;
import com.hogan.common.util.DigestUtil;
import com.hogan.framework.FrameworkConstants;
import com.hogan.framework.menu.Menu;
import com.hogan.framework.menu.MenuDao;
import com.hogan.framework.permission.Permission;
import com.hogan.framework.permission.PermissionDao;
import com.hogan.framework.rolePermission.RolePermission;
import com.hogan.framework.rolePermission.RolePermissionDao;
import com.hogan.framework.userRole.UserRole;
import com.hogan.framework.userRole.UserRoleDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * ClassName:UserService
 * Description:UserService
 * User:hogan.li
 * Date:2018/07/18
 */
@Service
@Transactional
public class UserService extends BaseServiceImpl<User, String> {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private MenuDao menuDao;

//    @Value("${password_expiration_day}")
//    private Long passwordExpirationDay;

    @Value("${init_password}")
    private String initPassword;

    /**
     * 根据条件查询User列表（实现方式三选一）
     */
    public Page<User> findUserByCondition(Map<String, Object> paramMap) throws Exception {
        Page<User> users = userDao.findUserByCriteria(paramMap);
        for (User user : users.getContent()) {
            List<String> roleIds = new ArrayList<>();
            List<UserRole> userRoles = userRoleDao.findByUserId(user.getId());
            for (UserRole userRole : userRoles) {
                roleIds.add(userRole.getRoleId());
            }
            user.setRoleIds(roleIds);
        }
        return users;
    }

    /**
     * 根据account查询用户
     */
    public User findByAccount(String account) throws Exception {
        return userDao.findByAccount(account);
    }

    /**
     * 同步域用户
     */
    @Transactional(rollbackFor = Exception.class)
    public User syncLdapUser(User user) throws Exception {
        if (StringUtils.isBlank(user.getAccount())) {
            throw new RuntimeException("用户账号不能为空！");
        }
        User localUser = userDao.findByAccount(user.getAccount());
        String dbPassword = DigestUtil.SHA256ToHex(user.getAccount() + "_" + initPassword);
        if (localUser == null) {
            user.setPassword(dbPassword);
            user.setCreateDate(DateUtil.getCurrentTimeStamp().toString());
            user.setState(true);
            user.setUnitId("");
            return userDao.save(user);
        } else {
            localUser.setEmail(user.getEmail());
            localUser.setUserName(user.getUserName());
            localUser.setGender(user.getGender());
            localUser.setHomePhone(user.getHomePhone());
            localUser.setMobilePhone(user.getMobilePhone());
            localUser.setOfficePhone(user.getOfficePhone());
            user.setDeleteSign(false);
            return userDao.save(localUser);
        }
    }

    /**
     * 删除用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) throws Exception {
        // 删除用户本身
        super.deleteById(id);

        // 删除用户与角色关联关系
        userRoleDao.deleteByUserId(id);
    }

    /**
     * 删除用户列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<String> ids) throws Exception {
        if (ids != null && ids.size() > 0) {
            for (String id : ids) {
                // 删除用户本身
                super.deleteById(id);

                // 删除用户与角色关联关系
                userRoleDao.deleteByUserId(id);
            }
        }
    }

    /**
     * 查找用户菜单
     */
    public Map<String, List<String>> findUserMenusAndPermissions(User user) throws Exception {
        // 用set去重
        Set<String> menus = new HashSet<>();
        Set<String> permissions = new HashSet<>();
        Map<String, List<String>> map = new HashMap<>();

        // 管理员赋予所有菜单和权限，普通用户根据分配的角色获取菜单权限
        if (FrameworkConstants.ADMIN.equals(user.getAccount())) {
            List<Menu> menuList = menuDao.findByIsFolderAndDeleteSign(false, false);
            for (Menu menu : menuList) {
                menus.add(menu.getMenuCode());
                List<Permission> permissionList = permissionDao.findByMenuIdAndDeleteSign(menu.getId(), false, null);
                for (Permission permission : permissionList) {
                    permissions.add(permission.getPermissionCode());
                }
            }
        } else {
            // 获取用户角色列表
            List<UserRole> userRoles = userRoleDao.findByUserId(user.getId());
            for (UserRole userRole : userRoles) {
                List<RolePermission> rolePermissions = rolePermissionDao.findByRoleId(userRole.getRoleId());
                for (RolePermission rolePermission : rolePermissions) {
                    // 查询权限代码
                    Permission permission = permissionDao.getOne(rolePermission.getPermissionId());
                    permissions.add(permission.getPermissionCode());
                    // 通过权限查关联菜单，并把菜单代码放到列表中
                    menus.add(menuDao.getOne(permission.getMenuId()).getMenuCode());
                }
            }
        }

        map.put("menus", new ArrayList<>(menus));
        map.put("permissions", new ArrayList<>(permissions));

        return map;
    }

    /**
     * 校验用户旧密码（修改密码时提前判断）
     **/
    public Boolean checkPassword(String account, String password) throws Exception {
        Boolean flag = true;
        User user = userDao.findByAccount(account);
        if (user != null) {
            // 密码摘要（sha256（account_password）），其中password已经在前端进行过sha256摘要
            String dbPassword = DigestUtil.SHA256ToHex(account + "_" + password);
            // 本地认证
            if (!dbPassword.equals(user.getPassword())) {
                flag = false;
            }
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * 修改密码
     */
    public void updatePassword(String account, String password) throws Exception {
        User user = userDao.findByAccount(account);
        if (user != null) {
            // 密码摘要（sha256（account_password）），其中password已经在前端进行过sha256摘要
            String newPassword = DigestUtil.SHA256ToHex(account + "_" + password);
            user.setPassword(newPassword);
            user.setUpdateDate(DateUtil.getCurrentTimeStamp().toString());
            user.setPasswordUpdateDate(DateUtil.getCurrentTimeStamp().toString());
            userDao.save(user);
        }
    }

    /**
     * 重置密码
     */
    public void resetPasswordById(String userId) throws Exception {
        User user = userDao.getOne(userId);
        if (user != null) {
            String passWord = DigestUtil.SHA256ToHex(user.getAccount() + "_" + initPassword);
            user.setPassword(passWord);
            user.setPasswordUpdateDate(null);
            user.setUpdateDate(DateUtil.getCurrentTimeStamp().toString());
            userDao.save(user);
        }
    }

    /**
     * 校验用户时间
     */
//    public String checkUserUpdateDate(String account) throws Exception {
//        String flag = null;
//        User user = userDao.findByAccount(account);
//        if (user != null) {
//            String sDate = user.getPasswordUpdateDate();
//            if (StringUtils.isNotBlank(sDate)) {
//                String dDate = DateUtil.getCurrentTimeStamp().toString();
//                Long day = DateUtil.getDayByTimeLag(sDate, dDate);
//                // 时间差大于90天
//                if (day > passwordExpirationDay) {
//                    flag = "0";
//                }
//            } else {
//                flag = "1";
//            }
//        }
//        return flag;
//    }

    public List<User> findByAccountNotIn(List<String> accounts) {
        return userDao.findByAccountNotIn(accounts);
    }
}