package com.hogan.framework.menu;


import com.hogan.common.base.BaseDao;

import java.util.List;


/**
 * ClassName:MenuDao
 * Description:MenuDao
 * User:dada
 * Date:2018/07/18
 */
public interface MenuDao extends BaseDao<Menu, String>, MenuDaoCustom {

    //此处定义模块jpa规范扩展接口(无需实现)
    List<Menu> findByIsFolderAndDeleteSign(Boolean isFolder, Boolean deleteSign);

    List<Menu> findByParentIdAndDeleteSign(String parentId, Boolean deleteSign);
}