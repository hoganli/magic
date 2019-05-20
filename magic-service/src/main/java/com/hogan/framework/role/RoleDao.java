package com.hogan.framework.role;


import com.hogan.common.base.BaseDao;

import java.util.List;


/**
 * ClassName:RoleDao
 * Description:RoleDao
 * User:dada
 * Date:2018/07/18
 */
public interface RoleDao extends BaseDao<Role, String>, RoleDaoCustom {

    //此处定义模块jpa规范扩展接口(无需实现)
    public List<Role> findByDeleteSign(Boolean deleteSign);
}