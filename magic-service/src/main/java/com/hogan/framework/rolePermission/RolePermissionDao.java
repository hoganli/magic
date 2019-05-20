package com.hogan.framework.rolePermission;

import com.hogan.common.base.BaseDao;

import java.util.List;

/**
 * ClassName:RolePermissionDao
 * Description:RolePermissionDao
 * User:dada
 * Date:2018/07/18
 */
public interface RolePermissionDao extends BaseDao<RolePermission, String> {

    public List<RolePermission> findByRoleId(String roleId);

    public void deleteByRoleId(String roleId);

}
