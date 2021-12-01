package seung.kimchi.java.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;
import seung.kimchi.java.SConvert;

@SuppressWarnings("rawtypes")
@Slf4j
public class SLinkedHashMap extends LinkedHashMap {

	private static final long serialVersionUID = 1L;
	
	public SLinkedHashMap() {}
	
	public String stringify() {
		return stringify(false);
	}
	public String stringify(boolean isPretty) {
		String json = "";
		try {
			json = new ObjectMapper()
					.setSerializationInclusion(Include.ALWAYS)
					.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
					.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
					.configure(SerializationFeature.INDENT_OUTPUT, isPretty)
					.writeValueAsString(this)
					;
		} catch (JsonProcessingException e) {
			log.error("Failed to convert to json format text.", e);
		}
		return json;
	}
	
	public SLinkedHashMap(Map data) {
		add(data);
	}
	public SLinkedHashMap(Object data) {
		add(data);
	}
	public SLinkedHashMap(String data) {
		add(data);
	}
	public SLinkedHashMap(List data) {
		add(data);
	}
	
	public boolean isEqual(Object key, Object object) {
		return get(key) == object;
	}
	
	public boolean isNull(Object key) {
		return get(key) == null;
	}
	
	public boolean isBlank(Object key) {
		return "".equals(get(key));
	}
	
	public boolean isEmpty(Object key) {
		return isNull(key) || isBlank(key);
	}
	
	public List<String> keyList() {
		List<String> keyList = new ArrayList<>();
		for(Object key : this.keySet()) {
			keyList.add(key == null ? "" : "" + key);
		}
		return keyList;
	}
	
	public Object get(Object key, Object defaultValue) {
		return get(key) == null ? defaultValue : get(key);
	}
	
	public String getString(Object key) {
		return getString(key, null);
	}
	public String getString(Object key, String defaultValue) {
		if(isNull(key)) {
			return defaultValue;
		}
		Object value = get(key);
		if(value instanceof String) {
			return "" + value;
		}
		if(value instanceof String[]) {
			String[] items = (String[]) value;
			return items[0];
		}
		if(value instanceof List) {
			List items = (List) value;
			if(items.size() == 0) {
				return defaultValue;
			}
			return "" + items.get(0);
		}
		return "" + get(key);
	}
	
	public boolean getBoolean(Object key, boolean defaultValue) {
		if(isEmpty(key)) {
			return defaultValue;
		}
		Object value = get(key);
		if(value instanceof Boolean) {
			return (boolean) value;
		}
		switch (getString(key, "")) {
			case "1":
			case "true":
				return true;
			case "0":
			case "false":
				return false;
			default:
				break;
		}
		throw new ClassCastException("The value cannot be cast to boolean.");
	}
	
	public int getInt(Object key) {
		if(isEmpty(key)) {
			throw new NumberFormatException("The value can not be null or emtpy string.");
		}
		return getIntValid(key);
	}
	public int getInt(Object key, int defaultValue) {
		if(isEmpty(key)) {
			return defaultValue;
		}
		return getIntValid(key);
	}
	public int getIntValid(Object key) {
		if(!Pattern.matches("[0-9+-]+", getString(key))) {
			throw new NumberFormatException(String.format("Failed to cast to int. value=%s", getString(key)));
		}
		Object value = get(key);
		if(value instanceof Integer) {
			return (int) value;
		}
		return Integer.parseInt(getString(key));
	}
	
	public long getLong(Object key) {
		if(isEmpty(key)) {
			throw new NumberFormatException("The value can not be null or emtpy string.");
		}
		return getLongValid(key);
	}
	public long getLong(Object key, long defaultValue) {
		if(isEmpty(key)) {
			return defaultValue;
		}
		return getLongValid(key);
	}
	public long getLongValid(Object key) {
		if(!Pattern.matches("[0-9+-]+", getString(key))) {
			throw new NumberFormatException(String.format("Failed to cast to long. value=%s", getString(key)));
		}
		Object value = get(key);
		if(value instanceof Long) {
			return (long) value;
		}
		return Long.parseLong(getString(key));
	}
	
	public BigInteger getBigInteger(Object key) {
		if(isEmpty(key)) {
			throw new NumberFormatException("The value can not be null or emtpy string.");
		}
		return getBigIntegerValid(key);
	}
	public BigInteger getBigInteger(Object key, BigInteger defaultValue) {
		if(isEmpty(key)) {
			return defaultValue;
		}
		return getBigIntegerValid(key);
	}
	public BigInteger getBigIntegerValid(Object key) {
		if(!Pattern.matches("[0-9+-]+", getString(key))) {
			throw new NumberFormatException(String.format("Failed to cast to BigInteger. value=%s", getString(key)));
		}
		Object value = get(key);
		if(value instanceof BigInteger) {
			return (BigInteger) value;
		}
		return BigInteger.valueOf(getLong(key));
	}
	
	public double getDouble(Object key) {
		if(isEmpty(key)) {
			throw new NumberFormatException("The value can not be null or emtpy string.");
		}
		return getDoubleValid(key);
	}
	public double getDouble(Object key, double defaultValue) {
		if(isEmpty(key)) {
			return defaultValue;
		}
		return getDoubleValid(key);
	}
	public double getDoubleValid(Object key) {
		if(!Pattern.matches("[0-9+-.]+", getString(key))) {
			throw new NumberFormatException(String.format("Failed to cast to double. value=%s", getString(key)));
		}
		Object value = get(key);
		if(value instanceof Double) {
			return (double) value;
		}
		return Double.parseDouble(getString(key));
	}
	
	public BigDecimal getBigDecimal(Object key) {
		if(isEmpty(key)) {
			throw new NumberFormatException("The value can not be null or emtpy string.");
		}
		return getBigDecimalValid(key);
	}
	public BigDecimal getBigDecimal(Object key, BigDecimal defaultValue) {
		if(isEmpty(key)) {
			return defaultValue;
		}
		return getBigDecimalValid(key);
	}
	public BigDecimal getBigDecimalValid(Object key) {
		if(!Pattern.matches("[0-9+-.]+", getString(key))) {
			throw new NumberFormatException(String.format("Failed to cast to BigDecimal. value=%s", getString(key)));
		}
		Object value = get(key);
		if(value instanceof BigDecimal) {
			return (BigDecimal) value;
		}
		return BigDecimal.valueOf(getDouble(key));
	}
	
	public Map getMap(Object key) {
		if(!isNull(key)) {
			Object value = get(key);
			if(value instanceof Map) {
				return (Map) value;
			}
			throw new ClassCastException("The value cannot be cast to Map.");
		}
		return null;
	}
	
	public SLinkedHashMap getSLinkedHashMap(Object key) {
		if(!isNull(key)) {
			Object value = get(key);
			if(value instanceof SLinkedHashMap) {
				return (SLinkedHashMap) value;
			}
			throw new ClassCastException("The value cannot be cast to SLinkedHashMap.");
		}
		return null;
	}
	
	public List getList(Object key) {
		if(!isNull(key)) {
			Object value = get(key);
			if(value instanceof List) {
				return (List) value;
			}
			throw new ClassCastException("The value cannot be cast to List.");
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<SLinkedHashMap> getListSLinkedHashMap(Object key) {
		if(!isNull(key)) {
			return (List<SLinkedHashMap>) get(key);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public String[] getArrayString(Object key) {
		if(isEmpty(key)) {
			return null;
		}
		Object value = get(key);
		if(value instanceof String) {
			String[] array = {
					"" + value
			};
			return array;
		}
		if(value instanceof String[]) {
			return (String[]) value;
		}
		if(value instanceof List) {
			List<String> items = (List) value;
//			return Arrays.copyOf(items.toArray(), items.size(), String[].class);
			return items.stream().toArray(String[]::new);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public int[] getArrayInt(Object key) {
		if(isEmpty(key)) {
			return null;
		}
		Object value = get(key);
		if(value instanceof Integer) {
			int[] array = {
					(int) value
			};
			return array;
		}
		if(value instanceof int[]) {
			return (int[]) value;
		}
		if(value instanceof List) {
			List<Integer> values = (List) value;
			return values.stream().mapToInt(Integer::intValue).toArray();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public long[] getArrayLong(Object key) {
		if(isEmpty(key)) {
			return null;
		}
		Object value = get(key);
		if(value instanceof Integer) {
			long[] array = {
					(long) value
			};
			return array;
		}
		if(value instanceof long[]) {
			return (long[]) value;
		}
		if(value instanceof List) {
			List<Long> values = (List) value;
			return values.stream().mapToLong(Long::longValue).toArray();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public double[] getArrayDouble(Object key) {
		if(isEmpty(key)) {
			return null;
		}
		Object value = get(key);
		if(value instanceof double[]) {
			return (double[]) value;
		}
		if(value instanceof Double) {
			double[] array = {
					(double) value
			};
			return array;
		}
		if(value instanceof List) {
			List<Double> values = (List) value;
			return values.stream().mapToDouble(Double::doubleValue).toArray();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getListString(Object key) {
		if(isEmpty(key)) {
			return null;
		}
		Object value = get(key);
		if(value instanceof String) {
			return Arrays.asList("" + value);
		}
		if(value instanceof String[]) {
			return Arrays.asList((String[]) value);
		}
		if(value instanceof List) {
			return (List<String>) value;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public SLinkedHashMap add(Map data) {
		if(data != null) {
			super.putAll(data);
		}
		return this;
	}
	@SuppressWarnings("unchecked")
	public SLinkedHashMap add(Object data) {
		try {
			String field_name = "";
			for(Field field : data.getClass().getSuperclass().getDeclaredFields()) {
				field.setAccessible(true);
				field_name = field.getName();
				if("log".equals(field_name)) {
					continue;
				}
				this.put(field_name, field.get(data));
			}
			for(Field field : data.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				field_name = field.getName();
				if("log".equals(field_name)) {
					continue;
				}
				this.put(field_name, field.get(data));
			}
		} catch (IllegalArgumentException e) {
			log.error("Failed to convert to SLinkedHashMap.", e);
		} catch (IllegalAccessException e) {
			log.error("Failed to convert to SLinkedHashMap.", e);
		}
		return this;
	}
	@SuppressWarnings("unchecked")
	public SLinkedHashMap add(String data) {
		this.putAll(SConvert.toSLinkedHashMap(data));
		return this;
	}
	@SuppressWarnings("unchecked")
	public SLinkedHashMap add(List data) {
		if(data != null) {
			for(int index = 0; index < data.size(); index++) {
				this.put(index, data.get(index));
			}
		}
		return this;
	}
	@SuppressWarnings("unchecked")
	public SLinkedHashMap add(Object key, Object value) {
		if(get(key) instanceof List) {
			getList(key).add(value);
		} else {
			super.put(key, value);
		}
		return this;
	}
	@SuppressWarnings("unchecked")
	public void append(Object key, String value) {
		super.put(key, getString(key, "") + value);
	}
	@SuppressWarnings("unchecked")
	public void append(Object key, int value) {
		if(this.isEmpty(key)) {
			super.put(key, value);
		} else {
			super.put(key, getInt(key) + value);
		}
	}
	
}
