package com.hogan.app.member;


import com.hogan.common.base.BaseDao;


/**
 * @InterfaceName MMemberDao
 * @Description memberDao接口(BaseDao接口+自定义接口+jpa规范扩展接口)
 * @author hoagn
 * @date 2019-05-24 17:15:32
 */
public interface MMemberDao extends BaseDao<MMember, String>, MMemberDaoCustom {
    MMember findByMobilePhone(String mobilePhone);

    //此处定义模块jpa规范扩展接口(无需实现)
}