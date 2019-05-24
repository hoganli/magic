package com.hogan.app.book;


import com.hogan.common.base.BaseServiceImpl;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
 * @ClassName MBookService
 * @Description 预约Service实现类
 * @author hogan
 * @date 2019-05-24 17:04:56
 */
@Service
@Transactional
public class MBookService extends BaseServiceImpl<MBook, String> {

	@Autowired
	private MBookDao mBookDao;
	
	/**
     * 根据条件查询MBook列表（实现方式三选一）
     */
    public Page<MBook> findMBookByCondition(Map<String, Object> paramMap) throws Exception {
        return mBookDao.findMBookByCriteria(paramMap);
//        return mBookDao.findMBookByJpql(paramMap);
//        return mBookDao.findMBookBySql(paramMap);
    } 
     
}