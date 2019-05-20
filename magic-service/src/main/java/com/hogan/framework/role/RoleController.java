package com.hogan.framework.role;

import com.hogan.common.base.ReturnVO;
import com.hogan.common.util.DateUtil;
import com.hogan.framework.user.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ClassName:RoleController
 * Description:RoleController
 * User:hogan.li
 * Date:2018/07/18
 */
@Controller
@RequestMapping(value = "/magic/api")
public class RoleController {

	private static Logger log = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private RoleService roleService;
	
    /**
     * 添加Role
     */
	@RequestMapping(value = "/role", method = RequestMethod.POST)
    @RequiresPermissions("role:add")
	@ResponseBody
	public ReturnVO addRole(@RequestBody Role role) {
		
		ReturnVO vo = new ReturnVO();
		
		try {
            User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
			role.setCreateDate(DateUtil.getCurrentTimeStamp().toString());
            role.setCreateBy(user.getId());
            roleService.save(role);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
		}
		
		return vo;
	}


    /**
     * 更新Role
     */
	@RequestMapping(value = "/role", method = RequestMethod.PUT)
    @RequiresPermissions("role:update")
    @ResponseBody
    public ReturnVO updateRole(@RequestBody Role role) {

        ReturnVO vo = new ReturnVO();

        try {
            User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
            role.setUpdateDate(DateUtil.getCurrentTimeStamp().toString());
            role.setUpdateBy(user.getId());
            roleService.update(role);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }
    
    /**
     * 删除Role
     */
    @RequestMapping(value = "/role", method = RequestMethod.DELETE)
    @RequiresPermissions("role:delete")
    @ResponseBody
    public ReturnVO deleteRole(@RequestParam String id) {

        ReturnVO vo = new ReturnVO();

        try {
            roleService.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }

    /**
     * 删除Role列表
     */
    @RequestMapping(value = "/roles/batch", method = RequestMethod.POST)
    @RequiresPermissions("role:delete")
    @ResponseBody
    public ReturnVO deleteRoleList(@RequestBody List<String> ids) {

        ReturnVO vo = new ReturnVO();

        try {
            roleService.deleteByIds(ids);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }

    /**
     * 查询role列表（默认全部）
     */
    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    @RequiresPermissions("role:list")
    @ResponseBody
    public ReturnVO getRoleList() {

        ReturnVO vo = new ReturnVO();

        try {
            List<Role> dataList = roleService.findAll();
            vo.setDataList(dataList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }

	/**
     * 查询Role列表（自定义条件）
     */
    @RequestMapping(value = "/roles/queries", method = RequestMethod.POST)
    @RequiresPermissions("role:list")
    @ResponseBody
    public ReturnVO getRoleList(@RequestBody Map<String, Object> paramMap) {

        ReturnVO vo = new ReturnVO();

        try {
            Page<Role> page = roleService.findRoleByCondition(paramMap);
            vo.setDataList(page.getContent());
            vo.setTotalProperty(page.getTotalElements());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }
}