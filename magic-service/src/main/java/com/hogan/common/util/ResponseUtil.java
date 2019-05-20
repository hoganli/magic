package com.hogan.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hogan.common.base.ReturnVO;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * ClassName:ResponseUtil
 * Description: 统一的响应处理
 * User:Administrator
 * Date:2018/5/17 10:17
 */
public class ResponseUtil {

    /**
     * json响应（主要用于失败响应）
     */
    public static void responseJson(HttpServletResponse response, Integer status, Boolean success, String message) {
        ReturnVO vo = new ReturnVO();
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.setStatus(status);

            out = response.getWriter();
            vo.setSuccess(success);
            vo.setMessage(message);

            ObjectMapper mapper = new ObjectMapper();
            out.println(mapper.writeValueAsString(vo));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
    }
}
