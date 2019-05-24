package com.hogan.app.book;


import com.hogan.common.base.BaseDao;


/**
 * @InterfaceName MBookDao
 * @Description 预约Dao接口(BaseDao接口+自定义接口+jpa规范扩展接口)
 * @author hogan
 * @date 2019-05-24 17:04:56
 */
public interface MBookDao extends BaseDao<MBook, String>, MBookDaoCustom {
	
	//此处定义模块jpa规范扩展接口(无需实现)
}