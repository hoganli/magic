package com.hogan.app.project;


import com.hogan.common.base.BaseDao;


/**
 * @InterfaceName MProjectDao
 * @Description projectDao接口(BaseDao接口+自定义接口+jpa规范扩展接口)
 * @author hogan
 * @date 2019-05-24 17:16:09
 */
public interface MProjectDao extends BaseDao<MProject, String>, MProjectDaoCustom {
	
	//此处定义模块jpa规范扩展接口(无需实现)
}