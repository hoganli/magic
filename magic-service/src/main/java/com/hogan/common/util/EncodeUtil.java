package com.hogan.common.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * ClassName:EncodeUtil
 * Description:编解码工具类
 * User:dada
 * Date:2017/8/30 17:08
 */
public class EncodeUtil {

    /**
     * base64编码
     */
    public static String byteToBase64(byte[] bytes) throws Exception {
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(bytes);
    }

    /**
     * base64解码
     */
    public static byte[] base64ToByte(String base64) throws Exception {
        BASE64Decoder base64Decoder = new BASE64Decoder();
        return base64Decoder.decodeBuffer(base64);
    }

    /**
     * urlSafe编码
     */
    public static String urlSafeEncode(String base64Encode) {
        String urlSafeBase64Encode = base64Encode.replace('+', '-');
        urlSafeBase64Encode = urlSafeBase64Encode.replace('/', '_');
        urlSafeBase64Encode = urlSafeBase64Encode.replaceAll("=", "");
        return urlSafeBase64Encode;
    }

    /**
     * urlSafe解码
     */
    public static String urlSafeDecode(String urlSafeBase64Encode){
        String base64Encode = urlSafeBase64Encode.replace('-', '+');
        base64Encode = base64Encode.replace('_', '/');
        int mod4 = base64Encode.length() % 4;
        // 不足4倍长度，结尾补“=”
        if (mod4 > 0) {
            base64Encode = base64Encode + "====".substring(mod4);
        }
        return base64Encode;
    }

    /**
     * urlSafeBase64编码
     */
    public static String urlSafeBase64Encode(byte[] data) {
        // base64编码
        String base64Encode = new BASE64Encoder().encode(data);

        // urlSafe编码
        String urlSafeBase64Encode = base64Encode.replace('+', '-');
        urlSafeBase64Encode = urlSafeBase64Encode.replace('/', '_');
        urlSafeBase64Encode = urlSafeBase64Encode.replaceAll("=", "");
        return urlSafeBase64Encode;
    }

    /**
     * urlSafeBase64解码
     */
    public static byte[] urlSafeBase64Decode(final String urlSafeBase64Encode) throws IOException {
        // urlSafe解码
        String base64Encode = urlSafeBase64Encode.replace('-', '+');
        base64Encode = base64Encode.replace('_', '/');
        int mod4 = base64Encode.length() % 4;
        if (mod4 > 0) {
            base64Encode = base64Encode + "====".substring(mod4);   // 不足4倍长度，结尾补“=”
        }

        // base64解码
        return new BASE64Decoder().decodeBuffer(base64Encode);
    }

    /**
     * 16进制编码
     */
    public static String byteToHex(byte[] bytes) throws Exception{
        StringBuilder hex = new StringBuilder();

        for(int i=0;i<bytes.length;i++){
            byte b = bytes[i];
//            boolean negative = false;   //是否为负数
//            if(b<0){
//                negative = true;
//            }
//            int inte = Math.abs(b);
//
//            // 负数会转换成正数（最高位的负号变成数值计算），再转16进制
//            if(negative){
//                inte = inte | 0x80;
//            }

            String temp = Integer.toHexString(b & 0xFF);
            if(temp.length() == 1){
                hex.append("0");
            }
            hex.append(temp.toLowerCase());
        }

        return hex.toString();
    }

    /**
     * 16进制解码
     */
    public static byte[] hexToByte(String hex) throws Exception{
        byte[] bytes = new byte[hex.length() / 2];

        for(int i=0;i<hex.length();i = i+2){
            String sub = hex.substring(i, i + 2);
            boolean negative = false;   // 是否为负数
            int inte = Integer.parseInt(sub, 16);
            if(inte > 127){
                negative = true;
            }
            if(inte == 128){
                inte = -128;
            }else if(negative){
                inte = 0-(inte & 0x7F);
            }

            byte b = (byte) inte;
            bytes[i/2] = b;
        }

        return bytes;
    }

    /**
     * main测试方法
     */
    public static void main(String[] args) throws Exception{
        String s = "hello,world";
        System.out.println("原始字符串："+s);

        // base64编码
        String base64Encode = byteToBase64(s.getBytes());
        System.out.println("base64编码："+base64Encode);

        // urlSafe编码
        String urlSafeEncode = urlSafeEncode(base64Encode);
        System.out.println("urlSafe编码："+urlSafeEncode);

        // urlSafeBase64编码
        String urlSafeBase64Encode = urlSafeBase64Encode(s.getBytes());
        System.out.println("urlSafeBase64编码："+urlSafeBase64Encode);

        // urlSafeBase64解码
        byte[] bytes = urlSafeBase64Decode(urlSafeBase64Encode);
        System.out.println("urlSafeBase64解码："+new String(bytes));

        // urlSafe解码
        String urlSafeDecode = urlSafeDecode(urlSafeEncode);
        System.out.println("urlSafe解码："+urlSafeDecode);

        // base64解码
        byte[] base64ToByte = base64ToByte(base64Encode);
        System.out.println("base64解码："+new String(base64ToByte));

        // 16进制编码
        String hex = byteToHex(s.getBytes());
        System.out.println("16进制编码："+hex);

        // 16进制解码
        byte[] hexToByte = hexToByte(hex);
        System.out.println("16进制解码："+new String(hexToByte));
    }

}