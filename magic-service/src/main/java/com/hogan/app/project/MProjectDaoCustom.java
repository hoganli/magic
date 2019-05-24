package com.hogan.app.project;


import org.springframework.data.domain.Page;

import java.util.Map;


/**
 * @InterfaceName MProjectDaoCustom
 * @Description projectDaoCustom自定义接口（非jpa规范接口或者复杂业务接口）
 * @author hogan
 * @date 2019-05-24 17:16:09
 */
public interface MProjectDaoCustom {
	
	//此处定义模块非jpa规范接口或者复杂业务接口（需要实现）

    /**
     * 查询MProject列表（Criteria）
     */
    public Page<MProject> findMProjectByCriteria(Map<String, Object> paramMap) throws Exception;

    /**
     * 查询MProject列表（Jpql）
     */
    public Page<MProject> findMProjectByJpql(Map<String, Object> paramMap) throws Exception;

    /**
     * 查询MProject列表（Sql）
     */
    public Page<MProject> findMProjectBySql(Map<String, Object> paramMap) throws Exception;

}