package com.hogan.framework.rolePermission;

import com.hogan.common.base.ReturnVO;
import com.hogan.framework.role.Role;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName:RolePermissionController
 * Description:RolePermissionController
 * User:hogan.li
 * Date:2018/5/10 8:39
 */
@Controller
@RequestMapping(value = "/magic/api")
public class RolePermissionController {

    private static Logger log = LoggerFactory.getLogger(RolePermissionController.class);

    @Autowired
    private RolePermissionService rolePermissionService;

    /**
     * 更新角色与权限关联关系
     */
    @RequestMapping(value = "/rolePermission", method = RequestMethod.PUT)
    @RequiresPermissions("rolePermission:update")
    @ResponseBody
    public ReturnVO updateRolePermission(@RequestBody Role role) {

        ReturnVO vo = new ReturnVO();

        try {
            rolePermissionService.updateRolePermission(role.getId(), role.getPermissionIds());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }

    /**
     * 根据roleId查询权限id列表
     */
    @RequestMapping(value = "/role/{id}/permissions", method = RequestMethod.GET)
    @ResponseBody
    public ReturnVO getPermissionIdListByRoleId(@PathVariable("id") String roleId) {

        ReturnVO vo = new ReturnVO();

        try {
            List<String> dataList = rolePermissionService.getPermissionIdListByRoleId(roleId);
            vo.setDataList(dataList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }

}
