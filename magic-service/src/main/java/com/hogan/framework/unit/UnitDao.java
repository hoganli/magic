package com.hogan.framework.unit;


import com.hogan.common.base.BaseDao;


/**
 * ClassName:UnitDao
 * Description:UnitDao
 * User:hogan.li
 * Date:2018/07/18
 */
public interface UnitDao extends BaseDao<Unit, String>, UnitDaoCustom {
	
	//此处定义模块jpa规范扩展接口(无需实现)
}