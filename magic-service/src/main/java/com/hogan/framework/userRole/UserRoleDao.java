package com.hogan.framework.userRole;

import com.hogan.common.base.BaseDao;

import java.util.List;

/**
 * ClassName:UserRoleDao
 * Description:UserRoleDao
 * User:dada
 * Date:2018/07/18
 */
public interface UserRoleDao extends BaseDao<UserRole, String> {

    public List<UserRole> findByRoleId(String roleId) throws Exception;

    public List<UserRole> findByUserId(String userId) throws Exception;

    public void deleteByUserId(String userId) throws Exception;

    public void deleteByRoleId(String roleId) throws Exception;
}
