package org.ciat.ita.client.correctormanager;

import java.io.UnsupportedEncodingException;

import org.ciat.ita.model.CharsetToolkit;


public class DecodeManager {
	
	public static String decodeString(String str) {
		if (str != null && str.length() > 0) {
			String charset = "";
			charset = new CharsetToolkit(str.getBytes()).guessEncoding().name();
			try {
				if (!charset.contains("ASCII")) {
					if (charset.equals("UTF-8")) {
						// System.out.println(charset+" [UTF-8]: " + str +
						// " --> "
						// + new String(str.getBytes("ISO-8859-1"), "UTF-8"));
						return new String(str.getBytes("ISO-8859-1"), "UTF-8");
					} else if (str.contains("?")) {
						String newStr = new String(str.getBytes("ISO-8859-1"), "UTF-8");
						if (countSimbols(newStr, '?') < countSimbols(str, '?')) {
							// System.out.println("CHANGED! [Cont. (?)]: " + str
							// + " --> " + newStr);
							return newStr;
						} /*
						 * else {
						 * System.out.println("NOT CHANGED! [Cont. (?)]: " + str
						 * + " --> " + newStr); }
						 */
					} else if (str.contains("¤")) {
						String newStr = new String(str.getBytes("ISO-8859-1"), "UTF-8");
						if (countSimbols(newStr, '¤') < countSimbols(str, '¤')) {
							// System.out.println(charset+" [Cont. (¤)]: " + str
							// + " --> " + newStr);
							return newStr;
						}
					}
				} // else System.out.println(charset+" [is Ok!]: " + str);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return str;
	}
	
	/**
	 * Count how many simbols exist in a specific string.
	 * 
	 * @param str
	 *            - the specified string
	 * @return the number of simbols.
	 */
	private static int countSimbols(String str, char simbol) {
		int n = 0;
		for (char c : str.toCharArray()) {
			if (c == simbol)
				n++;
		}
		return n;
	}

}
