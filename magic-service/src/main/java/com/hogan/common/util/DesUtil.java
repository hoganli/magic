package com.hogan.common.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * ClassName:DesUtil
 * Description:DES加解密工具类
 * User:dada
 * Date:2017/8/30 13:56
 */
public class DesUtil {
    /**
     * 生成DES随机密钥
     */
    public static String generateDESKey() throws Exception {
        // 获取keyGen
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");

        // 生成key
        keyGen.init(56);
        SecretKey key = keyGen.generateKey();

        // base64编码
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(key.getEncoded());
    }

    /**
     * 加载已有DES密钥
     */
    public static SecretKey loadDESKey(String base64Key) throws Exception {
        // base64解码
        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] bytes = base64Decoder.decodeBuffer(base64Key);

        // 生成key
        return new SecretKeySpec(bytes, "DES");
    }

    /**
     * 生成DES固定密钥
     */
    private static SecretKeySpec generateDESKeySpec(String secret) throws Exception {
        String hex = DigestUtil.SHA256ToHex(secret);
        String key = hex.substring(0, 8);
        System.out.println(key);
        return new SecretKeySpec(key.getBytes(), "DES");
    }

    /**
     * DES加密（String to byte数组）
     */
    public static byte[] DESEncryptToByte(String source, String secret, byte[] iv) throws Exception {

//        SecretKeySpec key = new SecretKeySpec(secret.getBytes(), "DES");
        SecretKeySpec key = generateDESKeySpec(secret);

        // 获取DES实例（CBC模式——需要初始向量iv）
//        IvParameterSpec zeroIv = new IvParameterSpec(iv);
//        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
//        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);

        // 获取DES实例（ECB模式——不需要初始向量iv）
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        // 执行DES加密
        return cipher.doFinal(source.getBytes("UTF-8"));
    }

    /**
     * DES加密（String to Base64编码）
     */
    public static String DESEncryptToBase64(String source, String secret, byte[] iv) throws Exception {
        return EncodeUtil.byteToBase64(DESEncryptToByte(source, secret, iv));
    }

    /**
     * DES解密（byte数组 to String）
     */
    public static String DESDecryptByte(byte[] source, String secret, byte[] iv) throws Exception {

//        SecretKeySpec key = new SecretKeySpec(secret.getBytes(), "DES");
        SecretKeySpec key = generateDESKeySpec(secret);

        // 获取DES实例（CBC模式——需要初始向量iv）
//        IvParameterSpec zeroIv = new IvParameterSpec(iv);
//        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
//        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);

        // 获取DES实例（ECB模式——不需要初始向量iv）
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);

        // 执行DES解密
        byte decryptedData[] = cipher.doFinal(source);

        return new String(decryptedData, "UTF-8");
    }

    /**
     * DES解密（Base64解码 to String）
     */
    public static String DESDecryptBase64(String source, String secret, byte[] iv) throws Exception {
        return DESDecryptByte(EncodeUtil.base64ToByte(source), secret, iv);
    }

    public static void main(String[] args) throws Exception {
        // 生成DES密钥
        String base64Key = generateDESKey();
        System.out.println("base64编码的DES key：" + base64Key);

        // 加载密钥
        SecretKey key = loadDESKey(base64Key);

        // DES加解密
        String source = "1234.abcd";
        String secret = "12345678";
        byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};
        System.out.println("加密前字符串：" + source);
        System.out.println("默认密钥：" + secret);

        byte[] desEncryptToByte = DESEncryptToByte(source, secret, null);
        System.out.println("DES加密（byte数组）：" + new String(desEncryptToByte));

        String desEncryptToBase64 = DESEncryptToBase64(source, secret, iv);
        System.out.println("DES加密（Base64编码）：" + desEncryptToBase64);

        String desDecryptByte = DESDecryptByte(desEncryptToByte, secret, iv);
        System.out.println("DES解密（byte数组）：" + desDecryptByte);

        String desDecryptBase64 = DESDecryptBase64(desEncryptToBase64, secret, iv);
        System.out.println("DES解密（Base64编码）：" + desDecryptBase64);

        // 测试前端加密的DES
        String source1 = "+FUQkDS4wO8blGkfm0cD9w==";
        String secret1 = "min-chong.liao";
        String desDecryptBase641 = DESDecryptBase64(source1, secret1,iv);
        System.out.println("DES解密（Base64编码）：" + desDecryptBase641);

    }
}
