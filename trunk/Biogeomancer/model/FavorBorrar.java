package model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class FavorBorrar {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		String s = "Cantón";
		String s2 = "Kärnthen";
		String s3 = "�?strand";
//		String utfStr = new String(s.getBytes("ISO-8859-1"), "UTF-8");
//		System.out.println(utfStr);
//		utfStr = new String(s2.getBytes("ISO-8859-1"), "UTF-8");
//		System.out.println(utfStr);
		
		/*System.out.println(new CharsetToolkit(s.getBytes()).guessEncoding().displayName());
		System.out.println(new CharsetToolkit(s2.getBytes()).guessEncoding().displayName());
		System.out.println(new CharsetToolkit(s3.getBytes()).guessEncoding().displayName());
		for(char c : s3.toCharArray()) {
			System.out.println(c+" - "+((int)c));
		}*/
		//System.out.println('�'+" - "+(int)'�');
		//System.out.println('�'+" - "+(int)'�');
		System.out.println(URLDecoder.decode("Ca�o", "ISO-8859-1"));
		System.out.println(new String("Ca�o".getBytes("latin-1"), "UTF-8"));
		
		
	}

}
