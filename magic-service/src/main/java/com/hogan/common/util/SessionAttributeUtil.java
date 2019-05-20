package com.hogan.common.util;

import com.hogan.framework.user.User;

import javax.servlet.http.HttpServletRequest;

public class SessionAttributeUtil {
	public static final String USER_ATTRIBUTE = "user";

	public static void setUserAttribute(HttpServletRequest request, User user) {
		request.getSession().setAttribute(USER_ATTRIBUTE, user);
	}

	public static User getUserAttribute(HttpServletRequest request) {
		Object obj = request.getSession().getAttribute(USER_ATTRIBUTE);
		return obj == null ? null : (User) obj;
	}
}
