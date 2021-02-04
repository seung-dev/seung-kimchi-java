package seung.kimchi.java.utils;

import java.util.Date;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import seung.kimchi.java.SConvert;

@Builder
@Getter
@Setter
@Slf4j
public class SResponse {

	public String stringify(boolean isPretty) {
		String stringify = "";
		try {
			stringify = new ObjectMapper()
					.setSerializationInclusion(Include.ALWAYS)
					.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
					.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
					.configure(SerializationFeature.INDENT_OUTPUT, isPretty)
					.writeValueAsString(this)
					;
		} catch (JsonProcessingException e) {
			log.error("Failed to convert to json format text.", e);
		}
		return stringify;
	}
	
	@NotBlank
	private String request_code;
	
	@Builder.Default
	private long request_time = -1l;
	
	@Builder.Default
	private long response_time = -1l;
	
	@Builder.Default
	private long elapsed_time = -1l;
	
	@Builder.Default
	private String error_code = "E999";
	
	@Builder.Default
	private String error_message = "";
	
	@Builder.Default
	private Object request = new SLinkedHashMap();
	
	@Builder.Default
	private SLinkedHashMap response = new SLinkedHashMap();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void add(Map map) {
		if(map != null) {
			this.response.putAll(map);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void put(Object key, Object value) {
		this.response.put(key, value);
	}
	
	public String getString(Object key, String defaultValue) {
		return this.response.getString(key, defaultValue);
	}
	
	public int getInt(Object key, int defaultValue) {
		return this.response.getInt(key, defaultValue);
	}
	
	public void success() {
		success("S000");
	}
	
	public void success(String error_code) {
		this.error_code = error_code;
	}
	
	public void error_code(String error_code) {
		this.error_code = error_code;
	}
	
	public void error_message(String error_message) {
		this.error_message = error_message;
	}
	
	public void error_message(String format, Object... args) {
		error_message(String.format(format, args));
	}
	
	public void exception(Exception exception) {
		error_message(SConvert.exception(exception));
	}
	
	public void error(String error_code, String error_message) {
		error_code(error_code);
		error_message(error_message);
	}
	
	public void error(String error_code, String format, Object... args) {
		error_code(error_code);
		error_message(format, args);
	}
	
	public boolean hasError() {
		return !"S000".equals(error_code);
	}
	
	public void done() {
		this.response_time = new Date().getTime();
		this.elapsed_time = response_time - request_time;
	}
	
}
