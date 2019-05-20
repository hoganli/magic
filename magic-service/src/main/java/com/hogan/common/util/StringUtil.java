package com.hogan.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class StringUtil {
    private static final String[] SRC = new String[]{"&", "\"", "<", ">", "'"};
    private static final String[] DEST = new String[]{"&amp;", "&quot;", "&lt;", "&gt;", "&apos;"};

    public static String escapeHTML(String src) {
        return StringUtils.replaceEach(src, SRC, DEST);
    }

    /**
     * return string array by split char
     *
     * @param lineString
     * @param regex
     * @return
     */
    public static String[] splitLine(String lineString, String regex) {
        if (StringUtils.isBlank(lineString)) {
            return null;
        } else {
            if (StringUtils.isBlank(regex)) {
                return new String[]{lineString};
            } else {
                return lineString.split(regex);
            }
        }
    }

    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
     * hexStr2ByteArr(String strIn) 互为可逆的转换过程
     *
     * @param arrB 需要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     */
    public static String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 判断一个对象是否为空或者长度为0时返回true，否则返回false
     * String:null或length=0返回true
     * List,Set,Map:null或size=0返回true
     * 其他null返回true
     *
     * @param o
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static final boolean isEmpty(Object o) {
        if (null == o) {
            return true;
        }
        if (o instanceof String) {
            return (0 == ((String) o).trim().length());
        }
        if (o instanceof List) {
            return (0 == ((List) o).size());
        }
        if (o instanceof Set) {
            return (0 == ((Set) o).size());
        }
        if (o instanceof Map) {
            return (0 == ((Map) o).size());
        }
        if (o instanceof Object[]) {
            return (0 == ((Object[]) o).length);
        }
        return false;
    }

    /**
     * 判断一个对象不为null，并且长度不为0时返回true，否则返回false
     * 规则参考Util.isEmpty()
     *
     * @param o
     * @return
     * @see #isEmpty(Object o)
     */
    public static final boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
     * hexStr2ByteArr(String strIn) 互为可逆的转换过程
     *
     * @param arrB 需要转换的byte数组 off byte数组数据的起始位置 len byte数组数据的长度
     * @return 转换后的字符串
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     */
    public static String byteArr2HexStr(byte[] arrB, int off, int len) throws Exception {
        int iLen = len;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i + off];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 将字符串转换为表示16进制值的字符串
     *
     * @param str 需要转换的字符串
     * @return 转换后的字符串
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     */
    public static String Str2HexStr(String str) throws Exception {
        int iLen = str.length();
        // 每个字符用两个字符表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = str.charAt(i);
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    public static byte[] replace2Byte(byte[] src, byte[] des, int start) {
        for (int i = 0; i < src.length; i++) {
            des[start + i] = src[i];
        }
        return des;
    }

    public static String padding(String padType, char padChar, int dataLen, String oriStr) {
        StringBuilder builder = new StringBuilder(oriStr);

        if (oriStr != null) {
            int strLen = oriStr.length();

            if (strLen < dataLen) {
                int padLen = dataLen - strLen;
                for (int i = 0; i < padLen; i++) {
                    if ("l".equalsIgnoreCase(padType)) {
                        builder.insert(0, Character.toString(padChar));
                    } else {
                        builder.append(Character.toString(padChar));
                    }
                }
            }
        }

        return builder.toString();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(escapeHTML("<input type='text' onclick='alert()'/>"));
    }

    public static String getSerialNum() {
        String timestamp = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
        String ramdomNum = "000" + ThreadLocalRandom.current().nextInt(10000);
        return timestamp + ramdomNum.substring(ramdomNum.length() - 4, ramdomNum.length());
    }

}
