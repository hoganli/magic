package com.hogan.framework.role;

import com.hogan.common.base.BaseServiceImpl;
import com.hogan.framework.rolePermission.RolePermission;
import com.hogan.framework.rolePermission.RolePermissionDao;
import com.hogan.framework.userRole.UserRoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ClassName:RoleService
 * Description:RoleService
 * User:hogan.li
 * Date:2018/07/18
 */
@Service
@Transactional
public class RoleService extends BaseServiceImpl<Role, String> {

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private UserRoleDao userRoleDao;

	@Autowired
    private RolePermissionDao rolePermissionDao;
	
	/**
     * 根据条件查询Role列表（实现方式三选一）
     */
    public Page<Role> findRoleByCondition(Map<String, Object> paramMap) throws Exception {
        Page<Role> page = roleDao.findRoleByCriteria(paramMap);
        for(Role r : page.getContent()){
            List<RolePermission> rps = rolePermissionDao.findByRoleId(r.getId());
            List<String> permissionIds = new ArrayList<>();
            if(!CollectionUtils.isEmpty(rps)){
                for(RolePermission rp : rps){
                    permissionIds.add(rp.getPermissionId());
                }
            }
            r.setPermissionIds(permissionIds);
        }
        return page;
//        return roleDao.findRoleByJpql(paramMap);
//        return roleDao.findRoleBySql(paramMap);
    }

    /**
     * 删除Role
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) throws Exception {
        // 删除角色本身
        super.deleteById(id);

        // 删除角色与权限关联关系
        rolePermissionDao.deleteByRoleId(id);

        // 删除用户与角色关联关系
        userRoleDao.deleteByRoleId(id);
    }

    /**
     * 删除Role列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<String> ids) throws Exception {
        if (ids != null && ids.size() > 0) {
            for (String id : ids) {
                // 删除角色本身
                super.deleteById(id);

                // 删除角色与权限关联关系
                rolePermissionDao.deleteByRoleId(id);

                // 删除用户与角色关联关系
                userRoleDao.deleteByRoleId(id);
            }
        }
    }
}