package com.hogan.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
	
	public static String md5(String message) {
		String md5str = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] input = message.getBytes();  
	        byte[] buff = md.digest(input);    
	        md5str = StringUtil.byteArr2HexStr(buff);  
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5str;
	}
}
