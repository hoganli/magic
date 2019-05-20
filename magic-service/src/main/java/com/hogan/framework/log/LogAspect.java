package com.hogan.framework.log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hogan.common.base.ReturnVO;
import com.hogan.common.util.DateUtil;
import com.hogan.framework.FrameworkConstants;
import com.hogan.framework.user.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Map;

/**
 * ClassName:LogAspect
 * Description:LogAspect
 * User:dada
 * Date:2018/07/18
 */
@Aspect
@Component
public class LogAspect {

    @Autowired
    private LogService logService;


    /**
     * 登录校验切入点
     */
    @Pointcut("execution(* com.hogan.framework.login.LoginController.loginCheck(..))")
    public void loginCheckCell() {
    }

    /**
     * 登录切入点
     */
    @Pointcut("execution(* com.hogan.framework.login.LoginController.login(..))")
    public void loginCell() {
    }

    /**
     * 退出切入点
     */
    @Pointcut("execution(* com.hogan.framework.login.LoginController.logout(..))")
    public void logoutCell() {
    }

    /**
     * 添加切入点
     */
    @Pointcut("execution(* com.hogan.gcm.*..*Controller.add*(..))")
    public void saveCell() {
    }

    /**
     * 修改切入点
     */
    @Pointcut("execution(* com.hogan.gcm.*..*Controller.update*(..))")
    public void updateCell() {
    }

    /**
     * 删除切入点
     */
    @Pointcut("execution(* com.hogan.gcm.*..*Controller.delete*(..))")
    public void deleteCell() {
    }

    /**
     * 登录校验日志
     */
    @AfterReturning(value = "loginCheckCell()",returning = "vo")
    public void loginCheckLog(JoinPoint joinPoint, ReturnVO vo) throws Throwable {
        //请求对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //日志对象
        Log log = new Log();

        //操作ip+host
        log.setOpIp(request.getRemoteAddr());
        log.setOpHost(request.getRemoteHost());

        //操作类型
        log.setOpType(FrameworkConstants.LOG_OP_TYPE_LOGINCHECK);

        //操作方法
        String method = joinPoint.getSignature().getName();
        log.setOpMethod(method);

        //操作参数
        Object[] objects = joinPoint.getArgs();
        log.setOpArgs(getOpArgs(objects));

        //操作结果
        Boolean success = (Boolean) vo.getSuccess();
        if (success) {
            Object data = vo.getData();
            if (data != null) {
                log.setOpResult(false);
            } else {
                log.setOpResult(success);
            }
        } else {
            log.setOpResult(success);
        }

        //操作用户
        Map userLinkHashMap = (Map)objects[0];
        String account = userLinkHashMap.get("account").toString();
        log.setUserAccount(account);

        //创建时间
        log.setCreateDate(DateUtil.getCurrentTimeStamp().toString());

        logService.save(log);
    }

    /**
     * 登录日志
     */
    @AfterReturning(value = "loginCell()",returning = "vo")
    public void loginLog(JoinPoint joinPoint, ReturnVO vo) throws Throwable {
        //请求对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //日志对象
        Log log = new Log();

        //操作ip+host
        log.setOpIp(request.getRemoteAddr());
        log.setOpHost(request.getRemoteHost());

        //操作类型
        log.setOpType(FrameworkConstants.LOG_OP_TYPE_LOGIN);

        //操作方法
        String method = joinPoint.getSignature().getName();
        log.setOpMethod(method);

        //操作参数
        Object[] objects = joinPoint.getArgs();
//        log.setOpArgs(getOpArgs(objects));

        //操作结果
        Boolean success = (Boolean) vo.getSuccess();
        log.setOpResult(success);

        //操作用户
        User user = null;
        if(success){
            //登录成功（从session获取）
//            user = SessionAttributeUtil.getUserAttribute(request);
            user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");

            if(user == null){
                //登录成功,但需要修改密码（从resultMap获取）
                user = (User) vo.getData();
            }

            if(user != null){
                log.setUserId(user.getId());
                log.setUserAccount(user.getAccount());
                log.setUserName(user.getUserName());
            }
        }else{
            //登录失败（从args获取）
//            user = (User) objects[1];
//            if(user != null){
//                log.setUserAccount(user.getAccount());
//            }
            Map userLinkHashMap = (Map)objects[1];
            String account = userLinkHashMap.get("account").toString();
            log.setUserAccount(account);
        }

        //创建时间
        log.setCreateDate(DateUtil.getCurrentTimeStamp().toString());

        logService.save(log);
    }

    /**
     * 退出日志
     */
    @Around(value = "logoutCell()")
    public Object logoutLog(ProceedingJoinPoint joinPoint) throws Throwable {
        //请求对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //日志对象
        Log log = new Log();

        //操作ip+host
        log.setOpIp(request.getRemoteAddr());
        log.setOpHost(request.getRemoteHost());

        //操作类型
        log.setOpType(FrameworkConstants.LOG_OP_TYPE_LOGOUT);

        //操作方法
        String method = joinPoint.getSignature().getName();
        log.setOpMethod(method);

        //操作参数
        Object[] objects = joinPoint.getArgs();
        log.setOpArgs(getOpArgs(objects));

        //操作用户
//        User user = SessionAttributeUtil.getUserAttribute(request);
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        if(user != null){
            log.setUserId(user.getId());
            log.setUserAccount(user.getAccount());
            log.setUserName(user.getUserName());
        }

        //操作结果
        ReturnVO vo = (ReturnVO) joinPoint.proceed();
        log.setOpResult(vo.getSuccess());

        //创建时间
        log.setCreateDate(DateUtil.getCurrentTimeStamp().toString());

        logService.save(log);

        return vo;
    }

    /**
     * 添加日志
     */
    @AfterReturning(value = "saveCell()", returning = "vo")
    public void saveLog(JoinPoint joinPoint, ReturnVO vo) throws Throwable {
        //请求对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //日志对象
        Log log = new Log();

        //操作ip+host
        log.setOpIp(request.getRemoteAddr());
        log.setOpHost(request.getRemoteHost());

        //操作类型
        log.setOpType(FrameworkConstants.LOG_OP_TYPE_SAVE);

        //操作方法
        String method = joinPoint.getSignature().getName();
        log.setOpMethod(method);

        //操作参数
        Object[] objects = joinPoint.getArgs();

        /**
         *  不记录保存稿样清单及模板方法的参数
         *  否则导致插入日志表时参数字段值过长异常
         */
//        String OpArgs = ("addOrder".equals(method) || "addOrgTemplate".equals(method) || "addCustomerTemplate".equals(method)) ? "" : getOpArgs(objects);
        log.setOpArgs(getOpArgs(objects));
        
        //操作用户
//        User user = SessionAttributeUtil.getUserAttribute(request);
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        if(user != null){
            log.setUserId(user.getId());
            log.setUserAccount(user.getAccount());
            log.setUserName(user.getUserName());
        }

        //操作结果
        log.setOpResult(vo.getSuccess());

        //创建时间
        log.setCreateDate(DateUtil.getCurrentTimeStamp().toString());

        logService.save(log);
    }

    /**
     * 修改日志
     */
    @AfterReturning(value = "updateCell()", returning = "vo")
    public void updateLog(JoinPoint joinPoint, ReturnVO vo) throws Throwable {
        //请求对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //日志对象
        Log log = new Log();

        //操作ip+host
        log.setOpIp(request.getRemoteAddr());
        log.setOpHost(request.getRemoteHost());

        //操作类型
        log.setOpType(FrameworkConstants.LOG_OP_TYPE_UPDATE);

        //操作方法
        String method = joinPoint.getSignature().getName();
        log.setOpMethod(method);

        //操作参数
        Object[] objects = joinPoint.getArgs();
        
        /**
         *  不记录修改稿样清单及模板方法的参数
         *  否则导致插入日志表时参数字段值过长异常
         */
//        String OpArgs = ("updateCustomerTemplate".equals(method) || "updateOrgTemplate".equals(method)) ? "" : getOpArgs(objects);
        log.setOpArgs(getOpArgs(objects));

        //操作用户
//        User user = SessionAttributeUtil.getUserAttribute(request);
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        if(user != null){
            log.setUserId(user.getId());
            log.setUserAccount(user.getAccount());
            log.setUserName(user.getUserName());
        } else {
            // 针对未登录的强制修改密码
            Map userLinkHashMap = (Map)objects[0];
            String account = userLinkHashMap.get("account").toString();
            log.setUserAccount(account);
        }

        //操作结果
        log.setOpResult(vo.getSuccess());

        //创建时间
        log.setCreateDate(DateUtil.getCurrentTimeStamp().toString());

        logService.save(log);
    }

    /**
     * 删除操作
     */
    @AfterReturning(value = "deleteCell()", returning = "vo")
    public void deleteLog(JoinPoint joinPoint, ReturnVO vo) throws Throwable {
        //请求对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //日志对象
        Log log = new Log();

        //操作ip+host
        log.setOpIp(request.getRemoteAddr());
        log.setOpHost(request.getRemoteHost());

        //操作类型
        log.setOpType(FrameworkConstants.LOG_OP_TYPE_DELETE);

        //操作方法
        String method = joinPoint.getSignature().getName();
        log.setOpMethod(method);

        //操作参数
        Object[] objects = joinPoint.getArgs();
        log.setOpArgs(getOpArgs(objects));

        //操作用户
//        User user = SessionAttributeUtil.getUserAttribute(request);
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        if(user != null){
            log.setUserId(user.getId());
            log.setUserAccount(user.getAccount());
            log.setUserName(user.getUserName());
        }

        //操作结果
        log.setOpResult(vo.getSuccess());

        //创建时间
        log.setCreateDate(DateUtil.getCurrentTimeStamp().toString());

        logService.save(log);
    }

    /**
     * 获取用户操作参数
     */
    public static String getOpArgs(Object[] args) throws JsonProcessingException {
        StringBuilder sb = new StringBuilder();
        String argStr = "";
        if (args != null && args.length > 0) {
            for (Object arg : args) {
                if(arg instanceof Serializable){
                    // 获取参数类型
                    String className = arg.getClass().getSimpleName();

                    //获取参数值
                    ObjectMapper mapper = new ObjectMapper();
                    String json = "";
                    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                    mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
                    try {
                        json = mapper.writeValueAsString(arg);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("获取请求参数异常：" + e);
                    }

                    sb.append("{参数类型：" + className + "，参数值："+json+"}，");    //此处使用中文逗号，避免被csv处理成分隔符
                }
            }

            //拼接参数
            argStr = sb.toString();
            if(StringUtils.isNotBlank(argStr)){
                argStr = "["+argStr.substring(0,argStr.length()-1)+"]";
            }
        }
        return argStr;
    }
}
