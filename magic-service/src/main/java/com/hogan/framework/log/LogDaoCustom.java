package com.hogan.framework.log;


import org.springframework.data.domain.Page;

import java.util.Map;


/**
 * ClassName:LogDaoCustom
 * Description:LogDaoCustom
 * User:dada
 * Date:2018/07/18
 */
public interface LogDaoCustom {
	
	//此处定义模块非jpa规范接口或者复杂业务接口（需要实现）

    /**
     * 查询Log列表（Criteria）
     */
    public Page<Log> findLogByCriteria(Map<String, Object> paramMap) throws Exception;

    /**
     * 查询Log列表（Jpql）
     */
    public Page<Log> findLogByJpql(Map<String, Object> paramMap) throws Exception;

    /**
     * 查询Log列表（Sql）
     */
    public Page<Log> findLogBySql(Map<String, Object> paramMap) throws Exception;

}