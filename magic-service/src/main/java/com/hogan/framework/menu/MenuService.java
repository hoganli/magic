package com.hogan.framework.menu;


import com.hogan.common.base.BaseServiceImpl;
import com.hogan.framework.permission.Permission;
import com.hogan.framework.permission.PermissionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
 * ClassName:MenuService
 * Description:MenuService
 * User:hogan.li
 * Date:2018/07/18
 */
@Service
@Transactional
public class MenuService extends BaseServiceImpl<Menu, String> {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private PermissionDao permissionDao;

    /**
     * 根据条件查询Menu列表（实现方式三选一）
     */
    public Page<Menu> findMenuByCondition(Map<String, Object> paramMap) throws Exception {
        return menuDao.findMenuByCriteria(paramMap);
//        return menuDao.findMenuByJpql(paramMap);
//        return menuDao.findMenuBySql(paramMap);
    }

    /**
     * 查询菜单已经菜单包含的权限
     */
    public List<Menu> getMenusWithPermissions() {
        List<Menu> menuList = menuDao.findByIsFolderAndDeleteSign(false, false);
        Sort sort = new Sort(Sort.Direction.DESC, "isNecessary");
        for (Menu menu : menuList) {
            List<Permission> permissions = permissionDao.findByMenuIdAndDeleteSign(menu.getId(), false, sort);
            menu.setPermissions(permissions);
        }
        return menuList;
    }
}