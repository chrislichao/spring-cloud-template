package org.ys.soft.framework.base.utils.encrypt;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * [字符串MD5加密工具类]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public class MD5 {
	private static Log logger = LogFactory.getLog(MD5.class);

	/**
	 * [加密]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String getEncrypt(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(input.getBytes());
			byte[] bb = md.digest();
			String hs = "", tmp = "";
			for (byte e : bb) {
				tmp = (Integer.toHexString(e & 0xFF));
				hs = tmp.length() == 1 ? hs + "0" + tmp : hs + tmp;
			}
			return hs;
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
			return "";
		}
	}

	public static void main(String[] args) {
		System.out.println(MD5.getEncrypt("88888888"));
	}

}