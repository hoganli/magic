package com.hogan.framework.menu;


import org.springframework.data.domain.Page;

import java.util.Map;


/**
 * ClassName:MenuDaoCustom
 * Description:MenuDaoCustom
 * User:dada
 * Date:2018/07/18
 */
public interface MenuDaoCustom {
	
	//此处定义模块非jpa规范接口或者复杂业务接口（需要实现）

    /**
     * 查询Menu列表（Criteria）
     */
    public Page<Menu> findMenuByCriteria(Map<String, Object> paramMap) throws Exception;

    /**
     * 查询Menu列表（Jpql）
     */
    public Page<Menu> findMenuByJpql(Map<String, Object> paramMap) throws Exception;

    /**
     * 查询Menu列表（Sql）
     */
    public Page<Menu> findMenuBySql(Map<String, Object> paramMap) throws Exception;

}