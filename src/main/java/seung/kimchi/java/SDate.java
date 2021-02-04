package seung.kimchi.java;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SDate {

	public SDate() {
		// TODO Auto-generated constructor stub
	}
	
	public static int toInteger() {
		return Integer.parseInt(toText("yyyyMMdd", new Date()).replaceAll("[^0-9]", ""));
	}
	public static int toInteger(String pattern) {
		return Integer.parseInt(toText(pattern, new Date()).replaceAll("[^0-9]", ""));
	}
	public static int toInteger(String pattern, Date date) {
		return Integer.parseInt(toText(pattern, date, TimeZone.getDefault()).replaceAll("[^0-9]", ""));
	}
	public static int toInteger(String pattern, Date date, TimeZone timeZone) {
		return Integer.parseInt(toText(pattern, date, timeZone).replaceAll("[^0-9]", ""));
	}
	
	public static String toText() {
		return toText("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", new Date(), TimeZone.getDefault());
	}
	public static String toText(Date date) {
		return toText("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", date, TimeZone.getDefault());
	}
	public static String toText(String pattern) {
		return toText(pattern, new Date());
	}
	public static String toText(String pattern, Date date) {
		return toText(pattern, date, TimeZone.getDefault());
	}
	public static String toText(String pattern, Date date, String timeZone) {
		return toText(pattern, date, TimeZone.getTimeZone(timeZone));
	}
	public static String toText(String pattern, Date date, TimeZone timeZone) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		simpleDateFormat.setTimeZone(timeZone);
		return simpleDateFormat.format(date);
	}
	
}
