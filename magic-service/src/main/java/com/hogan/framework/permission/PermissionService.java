package com.hogan.framework.permission;


import com.hogan.common.base.BaseServiceImpl;
import com.hogan.framework.menu.MenuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


/**
 * ClassName:PermissionService
 * Description:PermissionService
 * User:dada
 * Date:2018/07/18
 */
@Service
@Transactional
public class PermissionService extends BaseServiceImpl<Permission, String> {

	@Autowired
	private PermissionDao permissionDao;

	@Autowired
    private MenuDao menuDao;

    /**
     * 根据条件查询Permission列表（实现方式三选一）
     */
    public Page<Permission> findPermissionByCondition(Map<String, Object> paramMap) throws Exception {
        Page<Permission> page = permissionDao.findPermissionByCriteria(paramMap);
        for (Permission p : page.getContent()) {
            p.setMenu(menuDao.getOne(p.getMenuId()));
        }
        return page;
//        return permissionDao.findPermissionByJpql(paramMap);
//        return permissionDao.findPermissionBySql(paramMap);
    }

}