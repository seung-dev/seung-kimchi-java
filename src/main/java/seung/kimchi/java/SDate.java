package seung.kimchi.java;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;

public class SDate {

	public static final String _TIMEUNIT_MILLISECOND = "ms";
	public static final String _TIMEUNIT_SECOND = "s";
	public static final String _TIMEUNIT_MINUTE = "m";
	public static final String _TIMEUNIT_HOUR = "h";
	public static final String _TIMEUNIT_WEEK = "w";
	public static final String _TIMEUNIT_DAY = "day";
	public static final String _TIMEUNIT_MONTH = "month";
	public static final String _TIMEUNIT_YEAR = "year";
	
	public SDate() {
		// TODO Auto-generated constructor stub
	}
	
	public static Date add(Date date, String timeUnit, int amount) {
		
		Date addedDate = null;
		
		switch (timeUnit) {
			case _TIMEUNIT_MILLISECOND:
				addedDate = DateUtils.addMilliseconds(date, amount);
				break;
			case _TIMEUNIT_SECOND:
				addedDate = DateUtils.addSeconds(date, amount);
				break;
			case _TIMEUNIT_MINUTE:
				addedDate = DateUtils.addMinutes(date, amount);
				break;
			case _TIMEUNIT_HOUR:
				addedDate = DateUtils.addHours(date, amount);
				break;
			case _TIMEUNIT_WEEK:
				addedDate = DateUtils.addWeeks(date, amount);
				break;
			case _TIMEUNIT_DAY:
				addedDate = DateUtils.addDays(date, amount);
				break;
			case _TIMEUNIT_MONTH:
				addedDate = DateUtils.addMonths(date, amount);
				break;
			case _TIMEUNIT_YEAR:
				addedDate = DateUtils.addYears(date, amount);
				break;
			default:
				break;
		}
		
		return addedDate;
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
	public static String toText(String pattern, Date date, Locale locale) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, locale);
		return simpleDateFormat.format(date);
	}
	
	public static String epoch() {
		return Long.toString(new Date().getTime());
	}
	
}
