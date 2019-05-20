package com.hogan.common.util;

import java.security.MessageDigest;

/**
 * ClassName:DigestUtil
 * Description: 摘要工具类
 * User:Administrator
 * Date:2018/6/7 16:39
 */
public class DigestUtil {

    /**
     * SHA256摘要（String to byte数组）
     */
    public static byte[] SHA256ToByte(String message) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(message.getBytes("utf-8"));
        return md.digest();
    }

    /**
     * SHA256摘要（String to Hex编码）
     */
    public static String SHA256ToHex(String message) throws Exception {
        return EncodeUtil.byteToHex(SHA256ToByte(message));
    }

    public static void main(String[] args) throws Exception {
        String s = "admin_03da7e701f69bce218a3317680e541bedf94a9069bd04b9d215e507d757f2d0c";
        System.out.println("原始字符串：" + s);

        // sha256摘要并16进制编码
        String sha256ToHex = SHA256ToHex(s);
        System.out.println("sha256摘要并16进制编码：" + sha256ToHex);
    }

}
