package seung.kimchi.java;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SDate {

	public SDate() {
		// TODO Auto-generated constructor stub
	}
	
	public static int getDateInteger() {
		return Integer.parseInt(getDateString("yyyyMMdd", new Date()).replaceAll("[^0-9]", ""));
	}
	public static int getDateInteger(String pattern) {
		return Integer.parseInt(getDateString(pattern, new Date()).replaceAll("[^0-9]", ""));
	}
	public static int getDateInteger(String pattern, Date date) {
		return Integer.parseInt(getDateString(pattern, date, TimeZone.getDefault()).replaceAll("[^0-9]", ""));
	}
	public static int getDateInteger(String pattern, Date date, TimeZone timeZone) {
		return Integer.parseInt(getDateString(pattern, date, timeZone).replaceAll("[^0-9]", ""));
	}
	
	public static String getDateString() {
		return getDateString("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", new Date(), TimeZone.getDefault());
	}
	public static String getDateString(String pattern) {
		return getDateString(pattern, new Date());
	}
	public static String getDateString(String pattern, Date date) {
		return getDateString(pattern, date, TimeZone.getDefault());
	}
	public static String getDateString(String pattern, Date date, String timeZone) {
		return getDateString(pattern, date, TimeZone.getTimeZone(timeZone));
	}
	public static String getDateString(String pattern, Date date, TimeZone timeZone) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		simpleDateFormat.setTimeZone(timeZone);
		return simpleDateFormat.format(date);
	}
	
}
