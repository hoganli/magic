package com.hogan.framework.user;


import org.springframework.data.domain.Page;

import java.util.Map;


/**
 * ClassName:UserDaoCustom
 * Description:UserDaoCustom
 * User:hogan.li
 * Date:2018/07/18
 */
public interface UserDaoCustom {
	
	//此处定义模块非jpa规范接口或者复杂业务接口（需要实现）

    /**
     * 查询User列表（Criteria）
     */
    public Page<User> findUserByCriteria(Map<String, Object> paramMap) throws Exception;

    /**
     * 查询User列表（Jpql）
     */
    public Page<User> findUserByJpql(Map<String, Object> paramMap) throws Exception;

    /**
     * 查询User列表（Sql）
     */
    public Page<User> findUserBySql(Map<String, Object> paramMap) throws Exception;

}