package com.hogan.framework.rolePermission;

import com.hogan.common.base.BaseServiceImpl;
import com.hogan.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:RolePermissionService
 * Description:RolePermissionService
 * User:hogan.li
 * Date:2018/07/18
 */
@Service
@Transactional
public class RolePermissionService extends BaseServiceImpl<RolePermission, String> {

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Transactional(rollbackFor = Exception.class)
    public void saveRolePermission(String roleId, List<String> permissionIds) throws Exception {
        for (String permissionId : permissionIds) {
            RolePermission rp = new RolePermission();
            rp.setRoleId(roleId);
            rp.setPermissionId(permissionId);
            rp.setCreateDate(DateUtil.getCurrentTimeStamp().toString());
            save(rp);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateRolePermission(String roleId, List<String> permissionIds) throws Exception {
        // 先删除已有的关联
        rolePermissionDao.deleteByRoleId(roleId);
        // 保存新的关联
        saveRolePermission(roleId, permissionIds);
    }

    @Transactional
    public List<String> getPermissionIdListByRoleId(String roleId) throws Exception {
        List<RolePermission> rolePermissionList = rolePermissionDao.findByRoleId(roleId);
        List<String> idList = new ArrayList<>(rolePermissionList.size());
        if (rolePermissionList != null && rolePermissionList.size() > 0) {
            for (RolePermission rp : rolePermissionList) {
                idList.add(rp.getPermissionId());
            }
        }
        return idList;
    }
}
