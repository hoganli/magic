package com.hogan.common.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * ClassName:AesUtil
 * Description:AES加解密工具类
 * User:hogan.li
 * Date:2017/8/03 12:00
 */
public class AesUtil {

    /**
     * 生成AES随机密钥
     */
    private static SecretKey generateAESKey(String secret) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        // 将密钥进行SHA-1摘要
//        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
//        random.setSeed(secret.getBytes());
//        keyGenerator.init(128, random);
        // 生成密钥
        keyGenerator.init(128, new SecureRandom(secret.getBytes()));
        return keyGenerator.generateKey();
    }

    /**
     * 生成AES固定密钥
     */
    private static SecretKeySpec generateAESKeySpec(String secret) throws Exception {
        String hex = DigestUtil.SHA256ToHex(secret);
        String key = hex.substring(0, 16);
        return new SecretKeySpec(key.getBytes(), "AES");
    }

    /**
     * AES加密（String to byte数组）
     */
    public static byte[] AESEncryptToByte(String source, String secret) throws Exception {
        SecretKeySpec key = generateAESKeySpec(secret);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(source.getBytes("UTF-8"));
    }

    /**
     * AES加密（String to Base64编码）
     */
    public static String AESEncryptToBase64(String source, String secret) throws Exception {
        return EncodeUtil.byteToBase64(AESEncryptToByte(source, secret));
    }

    /**
     * AES加密（String to Hex编码）
     */
    public static String AESEncryptToHex(String source, String secret) throws Exception {
        return EncodeUtil.byteToHex(AESEncryptToByte(source, secret));
    }

    /**
     * AES解密（byte数组 to String）
     */
    public static String AESDecryptByte(byte[] source, String secret) throws Exception {
        SecretKeySpec key = generateAESKeySpec(secret);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] bytes = cipher.doFinal(source);
        return new String(bytes, "UTF-8");
    }

    /**
     * AES解密（Base64编码 to String）
     */
    public static String AESDecryptBase64(String source, String secret) throws Exception {
        return AESDecryptByte(EncodeUtil.base64ToByte(source), secret);
    }

    /**
     * AES解密（Hex编码 to String）
     */
    public static String AESDecryptHex(String source, String secret) throws Exception {
        return AESDecryptByte(EncodeUtil.hexToByte(source), secret);
    }

    public static void main(String[] args) throws Exception {

        // AES加解密
//        String source = "hogan.li@006";
        String source = "hogan.li@000";
//        String secret = "1234567812345678";   // 标准密钥
        String secret = "min-chong.liao";       // 非标密钥
        byte[] aesEncryptToByte = AESEncryptToByte(source, secret);
        System.out.println("AES加密（byte数组）：" + new String(aesEncryptToByte));

        String aesEncryptToBase64 = AESEncryptToBase64(source, secret);
        System.out.println("AES加密（Base64编码）：" + aesEncryptToBase64);

        String aesDecryptByte = AESDecryptByte(aesEncryptToByte, secret);
        System.out.println("AES解密（byte数组）：" + aesDecryptByte);

        String aesDecryptBase64 = AESDecryptBase64(aesEncryptToBase64, secret);
        System.out.println("AES解密（Base64编码）：" + aesDecryptBase64);

        // 测试前端加密的AES
//        String source1 = "/cQv7A1b6Kdkah9AQ1yHRA==";
        String source1 = "i4K9VKnaRLuSyeZxdItVSw==";
        String secret1 = "min-chong.liao";
        String aesDecryptBase641 = AESDecryptBase64(source1, secret1);
        System.out.println("AES解密（Base64编码）：" + aesDecryptBase641);
    }

}
