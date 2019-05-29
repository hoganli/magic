package com.hogan.app.project;


import com.hogan.common.base.BaseServiceImpl;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
 * @ClassName MProjectService
 * @Description projectService实现类
 * @author hogan
 * @date 2019-05-24 17:16:09
 */
@Service
@Transactional
public class MProjectService extends BaseServiceImpl<MProject, String> {

	@Autowired
	private MProjectDao mProjectDao;
	
	/**
     * 根据条件查询MProject列表（实现方式三选一）
     */
    public Page<MProject> findMProjectByCondition(Map<String, Object> paramMap) throws Exception {
        return mProjectDao.findMProjectByCriteria(paramMap);
//        return mProjectDao.findMProjectByJpql(paramMap);
//        return mProjectDao.findMProjectBySql(paramMap);
    }

    public List<MProject> findByUsed(boolean used) {
        return mProjectDao.findByUsed(used);
    }
}