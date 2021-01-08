package seung.kimchi.java;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

/**
 * <h1>Description</h1>
 * <pre>
 * Text Data Type Handler.
 * </pre>
 * <hr></hr>
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
	 * <pre>
	 * Padding.
	 * </pre>
	 * <h1>Usage</h1>
	 * <pre>
	 * System.out.println(SText.pad(SText._PAD_LEFT, "1", 6, "0");
	 * System.out.println(SText.pad(SText._PAD_RIGHT, "1", 6, "0");
	 * </pre>
	 * <hr></hr>
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
		if(data.length() >= maxLength) {
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
	 * <pre>
	 * Generate java code.
	 * </pre>
	 * <h1>Usage</h1>
	 * <pre>
	 * System.out.println(SText.toJavaCode("password".getBytes());
	 * </pre>
	 * <hr></hr>
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
	 * <pre>
	 * Generate UUID.
	 * </pre>
	 * <h1>Usage</h1>
	 * <pre>
	 * System.out.println(SText.uuid());
	 * </pre>
	 * <h1>Equal</h1>
	 * <pre>
	 * SText.uuid("-", "");
	 * </pre>
	 * <hr></hr>
	 * @see {@link SText#uuid(String, String)}
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static String uuid() {
		return uuid("-", "");
	}
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * Generate UUID.
	 * </pre>
	 * <h1>Usage</h1>
	 * <pre>
	 * String uuid = SText.uuid("-", "");
	 * System.out.println(uuid);
	 * </pre>
	 * <hr></hr>
	 * @param regex
	 * @param replacement
	 * @see {@link UUID#randomUUID()}
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static String uuid(String regex, String replacement) {
		return UUID.randomUUID().toString().replaceAll(regex, replacement);
	}
	
}
