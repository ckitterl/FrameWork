package com.nd.utilities.language;

import java.net.URLEncoder;

import android.net.Uri;
import android.text.TextUtils;

public class StringHelper {



	/**
	 * md5編碼
	 * 
	 * @param source
	 * @return
	 */
	public static String getMD5(byte[] source) {
		String s = null;
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest();
			char str[] = new char[16 * 2];
			int k = 0;
			for (int i = 0; i < 16; i++) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			s = new String(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	public static String getFileNameFromUrl(String p_url) {
		String path = p_url.substring(p_url.lastIndexOf("/") + 1,
				p_url.length());
		int start, end;
		do {
			start = path.indexOf("{");
			end = path.indexOf("}");
			if (start != -1 && end != -1) {
				String tmp = path.substring(start, end + 1);
				path = path.replace(tmp, "");
			}
		} while (start != -1 && end != -1);
		if (path.contains("&"))
			path = getMD5(path.getBytes());
		return URLEncoder.encode(path);
	}


	public static String convertTimeToHms(int time) {
		time /= 1000;
		int minute = time / 60;
		int hour = minute / 60;
		int second = time % 60;
		minute %= 60;
		return String.format("%02d:%02d:%02d", hour, minute, second);
	}

	public static String convertTimeToMs(int time) {
		time /= 1000;
		int minute = time / 60;
		int second = time % 60;
		return String.format("%02d:%02d", minute, second);
	}


	public static int parseStringToInt(String data, int def) {
		if (TextUtils.isEmpty(data))
			return def;
		return Integer.valueOf(data);
	}

	/**
	 * 將字符串首字母變成大寫
	 * 
	 * @param value
	 * @return
	 */
	public static String upperCaseFirstLetter(String value) {
		if (TextUtils.isEmpty(value))
			return value;
		if (Character.isUpperCase(value.charAt(0)))
			return value;
		if (value.length() > 1) {
			return (new StringBuilder())
					.append(Character.toUpperCase(value.charAt(0)))
					.append(value.substring(1)).toString();
		} else {
			return value.toUpperCase();
		}
	}

	/**
	 * 將字符串首字母變成小寫
	 * 
	 * @param value
	 * @return
	 */
	public static String lowerCaseFirstLetter(String value) {
		if (TextUtils.isEmpty(value))
			return value;
		if (Character.isLowerCase(value.charAt(0)))
			return value;
		if (value.length() > 1) {
			return (new StringBuilder())
					.append(Character.toLowerCase(value.charAt(0)))
					.append(value.substring(1)).toString();
		} else {
			return value.toLowerCase();
		}
	}

	/**
	 * 过滤sql语句中用的字符串中的特殊字符
	 * 
	 * @param ret
	 *            需要过滤的字符串
	 * @return 过滤特殊字符的字符串
	 */
	public static String sqliteEscape(String str) {
		String ret;
		if (str == null)
			ret = "";
		else {
			ret = str;
			ret = ret.replace("/", "//");
			ret = ret.replace("'", "''");
			ret = ret.replace("[", "/[");
			ret = ret.replace("]", "/]");
			ret = ret.replace("%", "/%");
			ret = ret.replace("&", "/&");
			ret = ret.replace("_", "/_");
			ret = ret.replace("(", "/(");
			ret = ret.replace(")", "/)");
		}
		return ret;
	}
	/**
	 * 猜测字符编码
	 * @param contents
	 * @return
	 */
	private static String guessAppropriateEncoding(CharSequence contents) {
		// Very crude at the moment
		for (int i = 0; i < contents.length(); i++) {
			if (contents.charAt(i) > 0xFF) {
				return "UTF-8";
			}
		}
		return null;
	}
}
