package com.hogan.app.member;


import org.springframework.data.domain.Page;

import java.util.Map;


/**
 * @InterfaceName MMemberDaoCustom
 * @Description memberDaoCustom自定义接口（非jpa规范接口或者复杂业务接口）
 * @author hoagn
 * @date 2019-05-24 17:15:32
 */
public interface MMemberDaoCustom {
	
	//此处定义模块非jpa规范接口或者复杂业务接口（需要实现）

    /**
     * 查询MMember列表（Criteria）
     */
    public Page<MMember> findMMemberByCriteria(Map<String, Object> paramMap) throws Exception;

    /**
     * 查询MMember列表（Jpql）
     */
    public Page<MMember> findMMemberByJpql(Map<String, Object> paramMap) throws Exception;

    /**
     * 查询MMember列表（Sql）
     */
    public Page<MMember> findMMemberBySql(Map<String, Object> paramMap) throws Exception;

}