package com.hogan.framework.unit;


import org.springframework.data.domain.Page;

import java.util.Map;


/**
 * ClassName:UnitDaoCustom
 * Description:UnitDaoCustom
 * User:dada
 * Date:2018/07/18
 */
public interface UnitDaoCustom {
	
	//此处定义模块非jpa规范接口或者复杂业务接口（需要实现）

    /**
     * 查询Unit列表（Criteria）
     */
    public Page<Unit> findUnitByCriteria(Map<String, Object> paramMap) throws Exception;

    /**
     * 查询Unit列表（Jpql）
     */
    public Page<Unit> findUnitByJpql(Map<String, Object> paramMap) throws Exception;

    /**
     * 查询Unit列表（Sql）
     */
    public Page<Unit> findUnitBySql(Map<String, Object> paramMap) throws Exception;

}