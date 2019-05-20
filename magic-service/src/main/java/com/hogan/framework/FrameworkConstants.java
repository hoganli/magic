package com.hogan.framework;

/**
 * 公共属性类
 */
public class FrameworkConstants {

    /*-----------------------------------------系统标识--------------------------------------------------------*/
    public static final String ADMIN = "admin";
    public static final String AUDITOR = "auditor";

    /*-----------------------------------------异常提示--------------------------------------------------------*/
    public static final String ERROR_MSG_USER_NON_EXISTENT = "该用户不存在！";
    public static final String ERROR_MSG_LOGIN_ACCOUNT_INVALID = "账号或密码不正确，请重试";
    public static final String ERROR_MSG_CHECK_PASSWORD_FAILURE = "旧密码检验不通过，请重试";
    public static final String ERROR_MSG_FORCE_PASSWORD_CHANGE = "密码已经过期，强制修改密码";
    public static final String ERROR_MSG_FIRST_LOGIN_FORCE_CHANGE = "用户首次登录，必须修改密码";
    public static final String ERROR_MSG_LOGIN_ACCOUNT_DISABLED = "该账号已被停用，请与系统管理员联系";
    public static final String ERROR_MSG_RECORD_NOT_EXIST = "记录不存在，请刷新重试";
    public static final String ERROR_MSG_PARAM_INVALID = "请求参数无效，请刷新重试！";
    public static final String ERROR_MSG_PARAM_ILLEGALITY = "请求参数非法，请检查重试！";
    public static final String ERROR_MSG_AUTHC_FAILED = "登录认证失败，请重新登录！";
    public static final String ERROR_MSG_AUTHZ_FAILED = "权限认证失败，请联系管理员！";

    public static final String ERROR_MSG_PROJECT_NAME_EXISTS = "项目名称已存在,请重新输入!";
    public static final String ERROR_MSG_PROJECT_CODE_EXISTS = "项目代码已存在,请重新输入!";

    /*-----------------------------------------操作日志--------------------------------------------------------*/
    public static final String LOG_OP_TYPE_LOGIN = "登录";
    public static final String LOG_OP_TYPE_LOGINCHECK = "登录校验";
    public static final String LOG_OP_TYPE_LOGOUT = "退出";
    public static final String LOG_OP_TYPE_SAVE = "新增";
    public static final String LOG_OP_TYPE_UPDATE = "编辑";
    public static final String LOG_OP_TYPE_DELETE = "删除";
    public static final String LOG_OP_TYPE_WARNING = "文件预警";
    public static final String LOG_OP_TYPE_DELETE_FILE = "清理备份";
    public static final String LOG_OP_TYPE_SEND_MAIL = "发送邮件";
    //
    public static final String LOG_JOB_IP = "127.0.0.1";
    public static final String LOG_JOB_METHOD = "job";
    public static final String LOG_JOB_USER_ACCOUNT = "system";
    public static final String LOG_JOB_USERNAME = "system";
}
