package com.hogan.app.member;


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
 * @ClassName MMemberController
 * @Description memberController
 * @author hoagn
 * @date 2019-05-24 17:15:32
 */
@RestController
@RequestMapping(value = "/magic")
public class MMemberController {

	private static Logger log = LoggerFactory.getLogger(MMemberController.class);

	@Autowired
	private MMemberService mMemberService;
	
    /**
     * 添加MMember
     */
	@RequestMapping(value = "/api/mMember", method = RequestMethod.POST)
    @RequiresPermissions("mMember:create")
	public ReturnVO addMMember(@RequestBody MMember mMember) {
		
		ReturnVO vo = new ReturnVO();
		
		try {
			User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
			mMember.setCreateDate(DateUtil.getCurrentTimeStamp().toString());
            mMember.setCreateBy(user.getId());
            mMemberService.save(mMember);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
		}
		
		return vo;
	}

    /**
     * 添加MMember
     */
    @RequestMapping(value = "/la/getOpenId", method = RequestMethod.POST)
    @RequiresPermissions("mMember:create")
    public ReturnVO getOpenId(@RequestParam String code) {

        ReturnVO vo = new ReturnVO();

        try {
            String openId = mMemberService.getOpenId(code);
            vo.setData(openId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }
	
    /**
     * 更新MMember
     */
	@RequestMapping(value = "/api/mMember", method = RequestMethod.PUT)
    @RequiresPermissions("mMember:update")
    public ReturnVO updateMMember(@RequestBody MMember mMember) {

        ReturnVO vo = new ReturnVO();

        try {
            User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
            mMember.setUpdateDate(DateUtil.getCurrentTimeStamp().toString());
            mMember.setUpdateBy(user.getId());
            mMemberService.update(mMember);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }
    
    /**
     * 删除MMember
     */
   	@RequestMapping(value = "/api/mMember/{mMemberId}", method = RequestMethod.DELETE)
    @RequiresPermissions("mMember:delete")
    public ReturnVO deleteMMember(@PathVariable("mMemberId") String mMemberId) {

        ReturnVO vo = new ReturnVO();

        try {
            mMemberService.deleteById(mMemberId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }
    
    /**
     * 批量删除MMember
     */
    @RequestMapping(value = "/api/mMembers/batch", method = RequestMethod.POST)
    @RequiresPermissions("mMember:delete")
    public ReturnVO deleteMMemberList(@RequestBody List<String> mMemberIds) {

        ReturnVO vo = new ReturnVO();

        try {
            mMemberService.deleteAllById(mMemberIds);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }
	
	/**
     * 查询MMember列表
     */
    @RequestMapping(value = "/api/mMembers", method = RequestMethod.POST)
    @RequiresPermissions("mMember:list")
    public ReturnVO findMMemberList(@RequestBody Map<String, Object> paramMap) {

        ReturnVO vo = new ReturnVO();

        try {
            Page<MMember> page = mMemberService.findMMemberByCondition(paramMap);
            vo.setDataList(page.getContent());
            vo.setTotalProperty(page.getTotalElements());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }
    
    /**
     * 查询单个MMember对象
     */
    @RequestMapping(value = "/api/mMember/{mMemberId}", method = RequestMethod.GET)
    @RequiresPermissions("mMember:list")
    public ReturnVO getMMember(@PathVariable("mMemberId") String mMemberId) {

        ReturnVO vo = new ReturnVO();

        try {
            MMember mMember = mMemberService.findById(mMemberId);
            if (mMember != null) {
                vo.setData(mMember);
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