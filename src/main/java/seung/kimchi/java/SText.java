package seung.kimchi.java;

import java.util.Random;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

/**
 * <h1>Description</h1>
 * <pre>{@code
 * Text Data Type Handler.
 * }</pre>
 * <hr>
 * @author seung
 * @since 2020.12.21
 * @version 1.0.0
 */
@Slf4j
public class SText {

	public final static String _PAD_LEFT = "L";
	public final static String _PAD_RIGHT = "R";
	
	public SText() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * <h1>Description</h1>
	 * <pre>{@code
	 * Convert to Full Width Characters.
	 * }</pre>
	 * <h1>Usage</h1>
	 * <pre>{@code
	 * System.out.println(SText.to_half_width("()"));
	 * }</pre>
	 * <hr>
	 * @param text
	 * @author seung
	 * @since 2022.08.08
	 * @version 1.0.0
	 */
	public static String to_full_width(String text) {
		
		if(text == null) {
			return text;
		}
		
		if("".equals(text)) {
			return text;
		}
		
		char[] characters = text.toCharArray();
		char[] full_width = new char[characters.length];
		
		for(int i = 0; i < characters.length; i++) {
			if(characters[i] == 32) {
				full_width[i] = (char) 12288;
				continue;
			}
			if(characters[i] < 127) {
				full_width[i] = (char) (characters[i] + 65248);
				continue;
			}
			full_width[i] = characters[i];
		}// end of characters
		
		return new String(full_width);
	}
	
	/**
	 * <h1>Description</h1>
	 * <pre>{@code
	 * Convert to Half Width Characters.
	 * }</pre>
	 * <h1>Usage</h1>
	 * <pre>{@code
	 * System.out.println(SText.to_half_width("（）"));
	 * }</pre>
	 * <hr>
	 * @param text
	 * @author seung
	 * @since 2022.08.08
	 * @version 1.0.0
	 */
	public static String to_half_width(String text) {
		
		if(text == null) {
			return text;
		}
		
		if("".equals(text)) {
			return text;
		}
		
		char[] characters = text.toCharArray();
		char[] half_width = new char[characters.length];
		
		for(int i = 0; i < characters.length; i++) {
			if(characters[i] == 12288) {
				half_width[i] = (char) 32;
				continue;
			}
			if(characters[i] > 65280  && characters[i] < 65375) {
				half_width[i] = (char) (characters[i] - 65248);
				continue;
			}
			half_width[i] = characters[i];
		}// end of characters
		
		return new String(half_width);
	}
	
	public static String random(int min, int max, int maxLength, String padChar) {
		int no = new Random().nextInt(max - min + 1) + min;
		return pad(_PAD_RIGHT, "" + no, maxLength, padChar);
	}
	public static int random(int min, int max) {
		return new Random().nextInt(max - min + 1) + min;
	}
	
	/**
	 * <h1>Description</h1>
	 * <pre>{@code
	 * Padding.
	 * }</pre>
	 * <h1>Usage</h1>
	 * <pre>{@code
	 * System.out.println(SText.pad(SText._PAD_LEFT, "1", 6, "0");
	 * System.out.println(SText.pad(SText._PAD_RIGHT, "1", 6, "0");
	 * }</pre>
	 * <hr>
	 * @param padPos - SText._PAD_LEFT: {@value SText#_PAD_LEFT}, SText._PAD_RIGHT: {@value SText#_PAD_RIGHT}
	 * @param data
	 * @param maxLength
	 * @param padChar
	 * @author seung
	 * @since 2021.01.04
	 * @version 1.0.0
	 */
	public static String pad(String padPos, String data, int maxLength, String padChar) {
		if(data == null) {
			log.info("Data is null.");
			return data;
		}
		if(data.length() > maxLength) {
			log.info("Data length is bigger than maxLength.");
			return data;
		}
		StringBuffer format = new StringBuffer();
		if(_PAD_LEFT.equals(padPos)) {
			format.append("%-");
		} else if(_PAD_RIGHT.equals(padPos)) {
			format.append("%");
		}
		format.append(maxLength);
		format.append("s");
		return String
				.format(format.toString(), data)
				.replace(" ", padChar)
				;
	}
	
	/**
	 * <h1>Description</h1>
	 * <pre>{@code
	 * Generate java code.
	 * }</pre>
	 * <h1>Usage</h1>
	 * <pre>{@code
	 * System.out.println(SText.toJavaCode("password".getBytes());
	 * }</pre>
	 * <hr>
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static String toJavaCode(byte[] data) {
		String javaCode = "";
		if(data == null) {
			javaCode = "byte[] byteArray = null;";
		} else if(0 == data.length) {
			javaCode = "byte[] byteArray = null;";
		} else {
			StringBuffer stringBuffer = new StringBuffer();
			for(byte b : data) {
				stringBuffer.append(String.format(", (byte) 0x%02x", b));
			}
			javaCode = String.format("byte[] byteArray = {%s};", stringBuffer.toString().substring(2));
		}
		return javaCode;
	}
	
	/**
	 * <h1>Description</h1>
	 * <pre>{@code
	 * Generate UUID.
	 * }</pre>
	 * <h1>Usage</h1>
	 * <pre>{@code
	 * System.out.println(SText.uuid());
	 * }</pre>
	 * <h1>Equal</h1>
	 * <pre>{@code
	 * SText.uuid("-", "");
	 * }</pre>
	 * <hr>
	 * @see SText#uuid(String, String)
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static String uuid() {
		return uuid("-", "");
	}
	/**
	 * <h1>Description</h1>
	 * <pre>{@code
	 * Generate UUID.
	 * }</pre>
	 * <h1>Usage</h1>
	 * <pre>{@code
	 * String uuid = SText.uuid("-", "");
	 * System.out.println(uuid);
	 * }</pre>
	 * <hr>
	 * @param regex
	 * @param replacement
	 * @see UUID#randomUUID()
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static String uuid(String regex, String replacement) {
		return UUID.randomUUID().toString().replaceAll(regex, replacement);
	}
	
	public static String trim(String data) {
		if(data == null) {
			return "";
		}
		return data.replaceAll("^\\s+|\\s+$", "");
	}
	
}
