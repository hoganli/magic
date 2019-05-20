package com.hogan.framework.unit;


import com.hogan.common.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * ClassName:unitDao
 * Description:unitDao
 * User:hogan.li
 * Date:2018/07/18
 */
@Service
public class UnitService extends BaseServiceImpl<Unit, String> {

	@Autowired
	private UnitDao unitDao;
	
	/**
     * 根据条件查询Unit列表（实现方式三选一）
     */
    public Page<Unit> findUnitByCondition(Map<String, Object> paramMap) throws Exception {
        return unitDao.findUnitByCriteria(paramMap);
//        return unitDao.findUnitByJpql(paramMap);
//        return unitDao.findUnitBySql(paramMap);
    } 
     
}