package com.hogan.framework.role;


import org.springframework.data.domain.Page;

import java.util.Map;


/**
 * ClassName:RoleDaoCustom
 * Description:RoleDaoCustom
 * User:dada
 * Date:2018/07/18
 */
public interface RoleDaoCustom {
	
	//此处定义模块非jpa规范接口或者复杂业务接口（需要实现）

    /**
     * 查询Role列表（Criteria）
     */
    public Page<Role> findRoleByCriteria(Map<String, Object> paramMap) throws Exception;

    /**
     * 查询Role列表（Jpql）
     */
    public Page<Role> findRoleByJpql(Map<String, Object> paramMap) throws Exception;

    /**
     * 查询Role列表（Sql）
     */
    public Page<Role> findRoleBySql(Map<String, Object> paramMap) throws Exception;

}