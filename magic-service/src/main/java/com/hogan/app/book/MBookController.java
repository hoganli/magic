package com.hogan.app.book;


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
 * @ClassName MBookController
 * @Description 预约Controller
 * @author hogan
 * @date 2019-05-24 17:04:56
 */
@RestController
@RequestMapping(value = "/magic/api")
public class MBookController {

	private static Logger log = LoggerFactory.getLogger(MBookController.class);

	@Autowired
	private MBookService mBookService;
	
    /**
     * 添加MBook
     */
	@RequestMapping(value = "/mBook", method = RequestMethod.POST)
    @RequiresPermissions("mBook:create")
	public ReturnVO addMBook(@RequestBody MBook mBook) {
		
		ReturnVO vo = new ReturnVO();
		
		try {
			User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
			mBook.setCreateDate(DateUtil.getCurrentTimeStamp().toString());
            mBook.setCreateBy(user.getId());
            mBookService.save(mBook);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
		}
		
		return vo;
	}
	
    /**
     * 更新MBook
     */
	@RequestMapping(value = "/mBook", method = RequestMethod.PUT)
    @RequiresPermissions("mBook:update")
    public ReturnVO updateMBook(@RequestBody MBook mBook) {

        ReturnVO vo = new ReturnVO();

        try {
            User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
            mBook.setUpdateDate(DateUtil.getCurrentTimeStamp().toString());
            mBook.setUpdateBy(user.getId());
            mBookService.update(mBook);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }
    
    /**
     * 删除MBook
     */
   	@RequestMapping(value = "/mBook/{mBookId}", method = RequestMethod.DELETE)
    @RequiresPermissions("mBook:delete")
    public ReturnVO deleteMBook(@PathVariable("mBookId") String mBookId) {

        ReturnVO vo = new ReturnVO();

        try {
            mBookService.deleteById(mBookId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }
    
    /**
     * 批量删除MBook
     */
    @RequestMapping(value = "/mBooks/batch", method = RequestMethod.POST)
    @RequiresPermissions("mBook:delete")
    public ReturnVO deleteMBookList(@RequestBody List<String> mBookIds) {

        ReturnVO vo = new ReturnVO();

        try {
            mBookService.deleteAllById(mBookIds);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        }

        return vo;
    }
	
	/**
     * 查询MBook列表
     */
    @RequestMapping(value = "/mBooks", method = RequestMethod.POST)
    @RequiresPermissions("mBook:list")
    public ReturnVO findMBookList(@RequestBody Map<String, Object> paramMap) {

        ReturnVO vo = new ReturnVO();

        try {
            Page<MBook> page = mBookService.findMBookByCondition(paramMap);
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
     * 查询单个MBook对象
     */
    @RequestMapping(value = "/mBook/{mBookId}", method = RequestMethod.GET)
    @RequiresPermissions("mBook:list")
    public ReturnVO getMBook(@PathVariable("mBookId") String mBookId) {

        ReturnVO vo = new ReturnVO();

        try {
            MBook mBook = mBookService.findById(mBookId);
            if (mBook != null) {
                vo.setData(mBook);
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