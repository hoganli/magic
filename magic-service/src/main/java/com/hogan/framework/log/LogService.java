package com.hogan.framework.log;


import com.hogan.common.base.BaseServiceImpl;
import com.hogan.common.util.DateUtil;
import com.hogan.framework.FrameworkConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * ClassName:LogService
 * Description:LogService
 * User:dada
 * Date:2018/07/18
 */
@Service
@Transactional
public class LogService extends BaseServiceImpl<Log, String> {

    @Autowired
    private LogDao logDao;

    /**
     * 根据条件查询Log列表（实现方式三选一）
     */
    public Page<Log> findLogByCondition(Map<String, Object> paramMap) throws Exception {
        return logDao.findLogByCriteria(paramMap);
//        return logDao.findLogByJpql(paramMap);
//        return logDao.findLogBySql(paramMap);
    }

    public void saveSystemJobLog(Log log) throws Exception {
        log.setCreateDate(DateUtil.getCurrentTimeStamp().toString());
        log.setOpMethod(FrameworkConstants.LOG_JOB_METHOD);
        log.setUserName(FrameworkConstants.LOG_JOB_USERNAME);
        log.setUserAccount(FrameworkConstants.LOG_JOB_USER_ACCOUNT);
        log.setOpIp(FrameworkConstants.LOG_JOB_IP);
        save(log);
    }
}