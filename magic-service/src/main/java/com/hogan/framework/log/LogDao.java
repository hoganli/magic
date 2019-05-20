package com.hogan.framework.log;


import com.hogan.common.base.BaseDao;


/**
 * ClassName:LogDao
 * Description:LogDao
 * User:hogan.li
 * Date:2018/07/18
 */
public interface LogDao extends BaseDao<Log, String>, LogDaoCustom {
	
	//此处定义模块jpa规范扩展接口(无需实现)
}