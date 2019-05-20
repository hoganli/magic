package com.hogan.framework.unit;


import com.hogan.common.base.ReturnVO;
import com.hogan.common.util.DateUtil;
import com.hogan.common.util.SessionAttributeUtil;
import com.hogan.framework.FrameworkConstants;
import com.hogan.framework.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * ClassName:UnitController
 * Description:UnitController
 * User:dada
 * Date:2018/07/18
 */
@Controller
public class UnitController {

	private static Logger log = LoggerFactory.getLogger(UnitController.class);

	@Autowired
	private UnitService unitService;
	
    /**
     * 添加Unit
     */
	@RequestMapping(value = "/addUnit.do", method = RequestMethod.POST)
	@ResponseBody
	public ReturnVO addUnit(HttpServletRequest request, @RequestBody Unit unit) {
		
		ReturnVO vo = new ReturnVO();
		
		try {
			User user = SessionAttributeUtil.getUserAttribute(request);
			unit.setCreateDate(DateUtil.getCurrentTimeStamp().toString());
            unit.setCreateBy(user.getId());
            unitService.save(unit);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
		}
		
		return vo;
	}
	
    /**
     * 更新Unit
     */
	@RequestMapping(value = "/updateUnit.do", method = RequestMethod.POST)
    @ResponseBody
    public ReturnVO updateUnit(HttpServletRequest request, @RequestBody Unit unit) {

        ReturnVO vo = new ReturnVO();

        try {
            User user = SessionAttributeUtil.getUserAttribute(request);
            unit.setUpdateDate(DateUtil.getCurrentTimeStamp().toString());
            unit.setUpdateBy(user.getId());
            unitService.update(unit);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }
    
    /**
     * 删除Unit
     */
    @RequestMapping(value = "/deleteUnit.do", method = RequestMethod.GET)
    @ResponseBody
    public ReturnVO deleteUnit(@RequestParam String id) {

        ReturnVO vo = new ReturnVO();

        try {
            unitService.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }
	
	/**
     * 查询Unit列表
     */
    @RequestMapping(value = "/findUnitList.do", method = RequestMethod.POST)
    @ResponseBody
    public ReturnVO findUnitList(@RequestBody Map<String, Object> paramMap) {

        ReturnVO vo = new ReturnVO();

        try {
            Page<Unit> page = unitService.findUnitByCondition(paramMap);
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
     * 查询单个Unit对象
     */
    @RequestMapping(value = "/getUnit.do", method = RequestMethod.GET)
    @ResponseBody
    public ReturnVO getUnit(@RequestParam String id) {

        ReturnVO vo = new ReturnVO();

        try {
            Unit unit = unitService.findById(id);
            if (unit != null) {
                vo.setData(unit);
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