package com.hogan.app.book;


import org.springframework.data.domain.Page;

import java.util.Map;


/**
 * @InterfaceName MBookDaoCustom
 * @Description 预约DaoCustom自定义接口（非jpa规范接口或者复杂业务接口）
 * @author hogan
 * @date 2019-05-24 17:04:56
 */
public interface MBookDaoCustom {
	
	//此处定义模块非jpa规范接口或者复杂业务接口（需要实现）

    /**
     * 查询MBook列表（Criteria）
     */
    public Page<MBook> findMBookByCriteria(Map<String, Object> paramMap) throws Exception;

    /**
     * 查询MBook列表（Jpql）
     */
    public Page<MBook> findMBookByJpql(Map<String, Object> paramMap) throws Exception;

    /**
     * 查询MBook列表（Sql）
     */
    public Page<MBook> findMBookBySql(Map<String, Object> paramMap) throws Exception;

}