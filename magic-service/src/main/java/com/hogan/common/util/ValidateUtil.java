package com.hogan.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * ClassName:ValidateUtil
 * Description:
 * User:Administrator
 * Date:2018/2/11 10:04
 */
public class ValidateUtil {

    /**
     * API请求参数摘要认证
     */
    public static Boolean paramValidate(Map<String, Object> paramMap, String digest) throws Exception {
        if (StringUtils.isNotBlank(digest)) {

            // digest格式：salt!sha256(paramString + salt)
            String[] digestArray = digest.split("!");

            // 对接收参数进行摘要
            ObjectMapper mapper = new ObjectMapper();
            String paramString = mapper.writeValueAsString(paramMap);
            String paramDigest = DigestUtil.SHA256ToHex(paramString + digestArray[0]);

            // 验证参数
            if (paramDigest.equalsIgnoreCase(digestArray[1])) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * API请求参数摘要认证
     */
    public static Boolean paramValidate(String paramString, String digest) throws Exception {
        if (StringUtils.isNotBlank(digest)) {

            // digest格式：salt!sha256(paramString + salt)
            String[] digestArray = digest.split("!");

            // 对接收参数进行摘要
            String paramDigest = DigestUtil.SHA256ToHex(paramString + digestArray[0]);

            // 验证参数
            if (paramDigest.equalsIgnoreCase(digestArray[1])) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * API请求参数XSS校验（检查危险字符，可完善）
     */
    public static Boolean xssValidate(String paramString) {
        char[] xssCharArray = new char[]{'<', '>', '&', '#'};
        if (StringUtils.isNotBlank(paramString)) {
            for (char xssChar : xssCharArray) {
                if (paramString.indexOf(xssChar) > -1) {
                    return false;
                }
            }
        }
        return true;
    }
}
