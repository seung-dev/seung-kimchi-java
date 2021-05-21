package seung.kimchi.java;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SFile {

	public static String contentDisposition(
			String userAgent
			, String fileName
			) throws UnsupportedEncodingException {
		
		String prefix = "attachment; filename=";
		String suffix = "";
		
		switch(browser(userAgent)) {
			case "MSIE":
				suffix = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
				break;
			case "Chrome":
				StringBuffer sb = new StringBuffer();
				for(int i = 0; i < fileName.length(); i++) {
					char c = fileName.charAt(i);
					if(c > '~') {
						sb.append(URLEncoder.encode("" + c, "UTF-8"));
					} else {
						sb.append(c);
					}
				}
				suffix = sb.toString();
				break;
			case "Opera":
			case "Firefox":
			default:
				suffix = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") +"\"";
				break;
		}
		
		return prefix.concat(suffix);
	}
	
	public static String browser(String userAgent) {
		
		String browser = "";
		
		if(userAgent.indexOf("MSIE") > -1) {
			browser = "MSIE";
		} else if(userAgent.indexOf("Trident") > -1) {
			browser = "MSIE";
		} else if(userAgent.indexOf("Chrome") > -1) {
			browser = "Chrome";
		} else if(userAgent.indexOf("Opera") > -1) {
			browser = "Opera";
		} else {
			browser = "Firefox";
		}
		
		return browser;
	}
	
}
