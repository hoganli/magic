package com.hogan.framework.permission;


import com.hogan.common.base.BaseDao;
import org.springframework.data.domain.Sort;

import java.util.List;


/**
 * ClassName:PermissionDao
 * Description:PermissionDao
 * User:dada
 * Date:2018/07/18
 */
public interface PermissionDao extends BaseDao<Permission, String>, PermissionDaoCustom {

    //此处定义模块jpa规范扩展接口(无需实现)
    List<Permission> findByMenuIdAndDeleteSign(String menuId, Boolean deleteSign, Sort sort);
}