package com.hogan.app.project;


import com.hogan.framework.FrameworkConstants;
import com.hogan.framework.user.User;
import com.hogan.common.base.ReturnVO;
import com.hogan.common.util.DateUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @ClassName MProjectController
 * @Description projectController
 * @author hogan
 * @date 2019-05-24 17:16:09
 */
@RestController
@RequestMapping(value = "/magic")
public class MProjectController {

	private static Logger log = LoggerFactory.getLogger(MProjectController.class);

	@Autowired
	private MProjectService mProjectService;
	
    /**
     * 添加MProject
     */
	@RequestMapping(value = "/api/mProject", method = RequestMethod.POST)
    @RequiresPermissions("mProject:create")
	public ReturnVO addMProject(@RequestBody MProject mProject) {
		
		ReturnVO vo = new ReturnVO();
		
		try {
			User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
			mProject.setCreateDate(DateUtil.getCurrentTimeStamp().toString());
            mProject.setCreateBy(user.getId());
            mProjectService.save(mProject);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
		}
		
		return vo;
	}
	
    /**
     * 更新MProject
     */
	@RequestMapping(value = "/api/mProject", method = RequestMethod.PUT)
    @RequiresPermissions("mProject:update")
    public ReturnVO updateMProject(@RequestBody MProject mProject) {

        ReturnVO vo = new ReturnVO();

        try {
            User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
            mProject.setUpdateDate(DateUtil.getCurrentTimeStamp().toString());
            mProject.setUpdateBy(user.getId());
            mProjectService.update(mProject);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }
    
    /**
     * 删除MProject
     */
   	@RequestMapping(value = "/api/mProject/{mProjectId}", method = RequestMethod.DELETE)
    @RequiresPermissions("mProject:delete")
    public ReturnVO deleteMProject(@PathVariable("mProjectId") String mProjectId) {

        ReturnVO vo = new ReturnVO();

        try {
            mProjectService.deleteById(mProjectId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }
    
    /**
     * 批量删除MProject
     */
    @RequestMapping(value = "/api/mProjects/batch", method = RequestMethod.POST)
    @RequiresPermissions("mProject:delete")
    public ReturnVO deleteMProjectList(@RequestBody List<String> mProjectIds) {

        ReturnVO vo = new ReturnVO();

        try {
            mProjectService.deleteAllById(mProjectIds);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }
	
	/**
     * 查询MProject列表
     */
    @RequestMapping(value = "/api/mProjects", method = RequestMethod.POST)
    @RequiresPermissions("mProject:list")
    public ReturnVO findMProjectList(@RequestBody Map<String, Object> paramMap) {

        ReturnVO vo = new ReturnVO();

        try {
            Page<MProject> page = mProjectService.findMProjectByCondition(paramMap);
            vo.setDataList(page.getContent());
            vo.setTotalProperty(page.getTotalElements());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }

    @RequestMapping(value = "/api/projects", method = RequestMethod.GET)
    @RequiresPermissions("mProject:list")
    public ReturnVO findProjects() {

        ReturnVO vo = new ReturnVO();

        try {
            vo.setDataList(mProjectService.findByUsed(true));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }

    @RequestMapping(value = "/la/projectOptions", method = RequestMethod.GET)
    public ReturnVO projectOptions() {

        ReturnVO vo = new ReturnVO();

        try {
            vo.setDataList(mProjectService.findProjectOptions());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }
    
    /**
     * 查询单个MProject对象
     */
    @RequestMapping(value = "/api/mProject/{mProjectId}", method = RequestMethod.GET)
    @RequiresPermissions("mProject:list")
    public ReturnVO getMProject(@PathVariable("mProjectId") String mProjectId) {

        ReturnVO vo = new ReturnVO();

        try {
            MProject mProject = mProjectService.findById(mProjectId);
            if (mProject != null) {
                vo.setData(mProject);
            } else {
                vo.setSuccess(false);
                vo.setMessage(FrameworkConstants.ERROR_MSG_RECORD_NOT_EXIST);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }
	
}