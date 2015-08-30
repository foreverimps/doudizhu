package com.zj.platform.gamecenter.utils;

import java.security.MessageDigest;

/**
 * @title: MD5Util
 * @description: 加密码工具类
 * @author: ygw
 */
public final class MD5Utils {
	/**
	 * 用于注册与登录
	 * 
	 * @param s 需要加密的字符串
	 * @return 返回加密后的字符串
	 */
	public static String toMD5(String s,String charset) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes(charset);
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		}
		catch (Exception e) {
			return null;
		}
	}
}
