package com.hogan.common.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FontImageUtil {

	private static Integer width = 85;
	private static Integer height= 120;
	private static Float fontSize = 75.0f;
	
	/**
	 * 读取本地文件
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static Font getLocalFont(String path) throws Exception {
		File file = new File(path);
		FileInputStream inputStream = new FileInputStream(file);
		Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(fontSize);
		inputStream.close();
    	return font;
	}

	/**
	 * 根据字体类型生成字体
	 * @param fontFamily
	 * @return
	 * @throws Exception
	 */
	public static Font getFamilyFont(String fontFamily) throws Exception {
		Font font = new Font(fontFamily, Font.PLAIN, fontSize.intValue());
		return font;
	}
	
	/**
	 * 远程在线读取字体文件
	 * @param urlStr
	 * @return
	 * @throws Exception
	 */
	public static Font getRemoteFont(String urlStr) throws Exception {
		URL url = new URL(urlStr);
		HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();  
        urlCon.setConnectTimeout(10000);  
        urlCon.setReadTimeout(30000); 
        
		Font font = Font.createFont(Font.TRUETYPE_FONT, urlCon.getInputStream()).deriveFont(fontSize);
		urlCon.disconnect();
		
    	return font;
	}
	
	public static String genImageBase64CodeFromTextByPath(String text, String path) throws Exception {
		return getImageBase64Code(text, getLocalFont(path));
	}
	
	public static String genImageBase64CodeFromTextByURL(String text, String url) throws Exception {
		return getImageBase64Code(text, getRemoteFont(url));
	}

	public static String genImageBase64CodeFromTextByFontFamily(String text, String fontFamliy, Boolean isCardNum) throws Exception {
		if (isCardNum) {
			return getCardNumImageBase64Code(text, getFamilyFont(fontFamliy));
		} else {
			return getImageBase64Code(text, getFamilyFont(fontFamliy));
		}
	}

	/**
	 * 针对文字卡号做特殊处理
	 * @param cardNum
	 * @param font
	 * @return
	 * @throws Exception
	 */
	public static String getCardNumImageBase64Code(String cardNum, Font font) throws Exception {

		int startSpan = cardNum.indexOf(" ");  // 返回卡号第一次出现空格的位置
		int endSpan = cardNum.lastIndexOf(" ");  // 返回卡号最后一次出现空格的位置

		cardNum = processCardNum(cardNum);

		Integer width = 50;
		Integer height = 120;                 

		int span = 15;
		int difference = startSpan == 4 ? 0 : (startSpan == endSpan ? 130 : 12);

		// 创建图片
		BufferedImage image = new BufferedImage(width * (cardNum.length() - span) + difference, height, BufferedImage.TYPE_INT_BGR);
		Graphics2D graphics2d = image.createGraphics();   
		image = graphics2d.getDeviceConfiguration().createCompatibleImage(width * (cardNum.length() - span) + difference, height, Transparency.TRANSLUCENT);
		graphics2d.dispose();  

		graphics2d = image.createGraphics();
		graphics2d.setClip(0, 0, width * (cardNum.length() - span) + difference, height);
		graphics2d.setColor(new Color(0, 0, 0));
		graphics2d.setStroke(new BasicStroke(1));
		graphics2d.setFont(font);// 设置画笔字体
		
		Rectangle clip = graphics2d.getClipBounds();
		FontMetrics fm = graphics2d.getFontMetrics(font);
		AffineTransform trans = new AffineTransform();
		int ascent = fm.getAscent(); 
		int descent = fm.getDescent();
		int y = (clip.height - (ascent + descent)) / 2 + ascent;

		graphics2d.drawString(cardNum, 0, y);// 画出字符串
		graphics2d.dispose();
		// 将图片写入字节数组输出流，用于转成base64字符串
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", baos);
		byte[] bytes = baos.toByteArray();
		String imgCode = EncodeUtil.byteToBase64(bytes);
		baos.close();
		return imgCode;
	}

	/**
	 * 处理卡号文本样式。填充span
	 * @param cardNum
	 * @return
	 */
	private static String processCardNum (String cardNum) {
		String span = "     "; // 5个
		StringBuilder returnCardNum = new StringBuilder();
			String[] cardNumList  = cardNum.split(" ");
			for (int i = 0, length = cardNumList.length; i < length; i++) {
				cardNumList[i] = cardNumList[i].replaceAll ("(.{1})", "$1 ");
				returnCardNum.append(i+1 == length ? cardNumList[i].substring(0, cardNumList[i].length() -1) : cardNumList[i] + span);
			}

		return returnCardNum.toString();
	}

	public static String getImageBase64Code(String text, Font font) throws Exception {
		// 创建图片    
        BufferedImage image = new BufferedImage(width * text.length(), height, BufferedImage.TYPE_INT_BGR);    
        Graphics2D graphics2d = image.createGraphics();
        image = graphics2d.getDeviceConfiguration().createCompatibleImage(width * text.length(), height, Transparency.TRANSLUCENT);    
        graphics2d.dispose(); 
        
        graphics2d = image.createGraphics();
        graphics2d.setClip(0, 0, width * text.length(), height);
        graphics2d.setColor(new Color(0, 0, 0));    
        graphics2d.setStroke(new BasicStroke(1)); 
        graphics2d.setFont(font);// 设置画笔字体  
        
        Rectangle clip = graphics2d.getClipBounds();    
        FontMetrics fm = graphics2d.getFontMetrics(font);    
        int ascent = fm.getAscent();    
        int descent = fm.getDescent();    
        int y = (clip.height - (ascent + descent)) / 2 + ascent;
        
        graphics2d.drawString(text, 0, y);// 画出字符串    
        graphics2d.dispose();
        // 将图片写入字节数组输出流，用于转成base64字符串
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] bytes = baos.toByteArray();
        String imgCode = EncodeUtil.byteToBase64(bytes);
        baos.close();
        return imgCode;
	}
	
}
