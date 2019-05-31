package com.hogan.app.member;


import com.hogan.common.base.BaseServiceImpl;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
 * @ClassName MMemberService
 * @Description memberService实现类
 * @author hoagn
 * @date 2019-05-24 17:15:32
 */
@Service
@Transactional
public class MMemberService extends BaseServiceImpl<MMember, String> {

	@Autowired
	private MMemberDao mMemberDao;
	
	/**
     * 根据条件查询MMember列表（实现方式三选一）
     */
    public Page<MMember> findMMemberByCondition(Map<String, Object> paramMap) throws Exception {
        return mMemberDao.findMMemberByCriteria(paramMap);
//        return mMemberDao.findMMemberByJpql(paramMap);
//        return mMemberDao.findMMemberBySql(paramMap);
    }

    public String getOpenId(String code) {
    }
}