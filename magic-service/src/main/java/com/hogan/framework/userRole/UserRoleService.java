package com.hogan.framework.userRole;

import com.hogan.common.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ClassName:UserRoleService
 * Description:UserRoleService
 * User:hogan.li
 * Date:2018/07/18
 */
@Service
@Transactional
public class UserRoleService extends BaseServiceImpl<UserRole, String>{

    @Autowired
    private UserRoleDao userRoleDao;

    @Transactional(rollbackFor = Exception.class)
    public void saveUserRole(String userId, List<String> roleIds) throws Exception {
        for(String roleId : roleIds){
            UserRole userRole = new UserRole();
            userRole.setRoleId(roleId);
            userRole.setUserId(userId);
            save(userRole);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUserRole(String userId, List<String> roleIds) throws Exception {
        // 先删除旧的关联
        userRoleDao.deleteByUserId(userId);
        // 保存新的关联
        saveUserRole(userId, roleIds);
    }
}
