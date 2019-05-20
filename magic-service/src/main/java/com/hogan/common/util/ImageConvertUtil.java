package com.hogan.common.util;

import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName:ImageConvertUtil
 * @Description:ImageConvertUtil
 * @Author:shou-quan.tang
 * @Date:2018/3/13 10:38 星期二
 */
public class ImageConvertUtil {

    private static BASE64Decoder decoder = new BASE64Decoder();

    private static ImageInfoCheckUtil imageInfoCheckUtil;

    /**
     * 校验BASE64图片加密字符串并上传.
     * @param base64String
     * @param suffix 图片格式.
     */
    public static String convertByteToImage(String base64String, String suffix) throws Exception {
        byte[] bytes = null;
        ByteArrayInputStream bais = null;
        try {
            bytes = decoder.decodeBuffer(checkBase64String(base64String));
            bais = new ByteArrayInputStream(bytes);

            if(!checkImageInputStream(bais)){
                throw new Exception("校验不通过,不是图片内容文件！");
            }

            // 文件上传
//            String result = FastDfsFactory.uploadFile(null, bytes, suffix);

            return null;

        } catch (IOException e) {
            throw new Exception("base64字符串转换成图片失败");
        } finally {
            try {
                if(bais != null) {
                    bais.close();
                    bais = null;
                }
            } catch (Exception e) {
                throw new Exception("关闭文件流发生异常: " + e);
            }
        }
    }

    /**
     * 判断是否需要split Base64String
     */
    private static String checkBase64String(String base64String) {
        int index = base64String.indexOf("base64,");
        if (index > 0) {
            String[] imgStr = base64String.split(",");
            return imgStr[1];
        } else {
            return  base64String;
        }
    }

    private static Boolean checkImageInputStream(InputStream inputStream) {
        if (imageInfoCheckUtil == null) {
            imageInfoCheckUtil = new ImageInfoCheckUtil();
        }
        imageInfoCheckUtil.setInput(inputStream);
        imageInfoCheckUtil.setDetermineImageNumber(true);
        imageInfoCheckUtil.setCollectComments(true);
        
        return imageInfoCheckUtil.check();
    }

    /**
     * 判断字符串是否为图片的base64字符串
     * @param imageString
     * @return
     */
    public static Boolean checkImageBase64String(String imageString) throws Exception{
        byte[] bytes = decoder.decodeBuffer(checkBase64String(imageString));
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        return checkImageInputStream(bais);
    }
}
