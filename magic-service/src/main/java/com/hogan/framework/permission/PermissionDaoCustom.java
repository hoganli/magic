package com.hogan.framework.permission;


import org.springframework.data.domain.Page;

import java.util.Map;


/**
 * ClassName:PermissionDaoCustom
 * Description:PermissionDaoCustom
 * User:dada
 * Date:2018/07/18
 */
public interface PermissionDaoCustom {
	
	//此处定义模块非jpa规范接口或者复杂业务接口（需要实现）

    /**
     * 查询Permission列表（Criteria）
     */
    public Page<Permission> findPermissionByCriteria(Map<String, Object> paramMap) throws Exception;

    /**
     * 查询Permission列表（Jpql）
     */
    public Page<Permission> findPermissionByJpql(Map<String, Object> paramMap) throws Exception;

    /**
     * 查询Permission列表（Sql）
     */
    public Page<Permission> findPermissionBySql(Map<String, Object> paramMap) throws Exception;

}