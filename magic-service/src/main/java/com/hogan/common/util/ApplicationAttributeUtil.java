package com.hogan.common.util;


import javax.servlet.http.HttpServletRequest;


/**
 * 将用户登录信息存入Application
 */
public class ApplicationAttributeUtil {

    // 用户尝试登录失败次数
    public static final String USER_SIGN_IN_FAIL_TIMES_ATTRIBUTE = "user.sign.in.fail.times.attribute.";

    // 用户最后登录失败时间
    public static final String USER_SIGN_IN_FAIL_LAST_TIME_ATTRIBUTE = "user.sign.in.fail.last.time.attribute.";

    public static void setUserSignInFailTimesAttribute(HttpServletRequest request, String userAcc, Integer signInTimes) {
        request.getServletContext().setAttribute(USER_SIGN_IN_FAIL_TIMES_ATTRIBUTE + userAcc, signInTimes);
    }

    public static Integer getUserSignInFailTimesAttribute(HttpServletRequest request, String userAcc) {
        Object obj = request.getServletContext().getAttribute(USER_SIGN_IN_FAIL_TIMES_ATTRIBUTE + userAcc);
        return obj == null ? 0 : (Integer) obj;
    }

    public static void removeUserSignInFailTimesAttribute(HttpServletRequest request, String userAcc) {
        request.getServletContext().removeAttribute(USER_SIGN_IN_FAIL_TIMES_ATTRIBUTE + userAcc);
    }

    public static void setUserSignInFailLastTimeAttribute(HttpServletRequest request, String userAcc, Long signInLastTime) {
        request.getServletContext().setAttribute(USER_SIGN_IN_FAIL_LAST_TIME_ATTRIBUTE + userAcc, signInLastTime);
    }

    public static Long getUserSignInFailLastTimeAttribute(HttpServletRequest request, String userAcc) {
        Object obj = request.getServletContext().getAttribute(USER_SIGN_IN_FAIL_LAST_TIME_ATTRIBUTE + userAcc);
        return obj == null ? 0l : (Long) obj;
    }

    public static void removeUserSignInFailLastTimeAttribute(HttpServletRequest request, String userAcc) {
        request.getServletContext().removeAttribute(USER_SIGN_IN_FAIL_LAST_TIME_ATTRIBUTE + userAcc);
    }
}
