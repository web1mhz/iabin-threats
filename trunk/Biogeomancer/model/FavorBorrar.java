package model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class FavorBorrar {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		String s = "CantÃ³n";
		String s2 = "KÃ¤rnthen";
		String s3 = "Ã?strand";
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
		//System.out.println('¤'+" - "+(int)'¤');
		//System.out.println('ñ'+" - "+(int)'ñ');
		System.out.println(URLDecoder.decode("Caï¿½o", "ISO-8859-1"));
		System.out.println(new String("Caï¿½o".getBytes("latin-1"), "UTF-8"));
		
		
	}

}
