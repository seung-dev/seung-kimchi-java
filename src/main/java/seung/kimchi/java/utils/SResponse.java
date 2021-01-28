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
import seung.kimchi.java.SConvert;

@Builder
@Getter
@Setter
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
			stringify = "Failed to convert to json format text.";
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
	
	public void success() {
		success("S000");
	}
	
	public void success(String error_code) {
		this.error_code = error_code;
	}
	
	public void error(String format, Object... args) {
		error(String.format(format, args));
	}
	
	public void error(Exception exception) {
		error(SConvert.toString(exception));
	}
	
	public void error(String error_message) {
		this.error_message = error_message;
	}
	
	public void error(String error_code, String error_message) {
		this.error_code = error_code;
		this.error_message = error_message;
	}
	
	public boolean hasError() {
		return !"S000".equals(error_code);
	}
	
	public void done() {
		this.response_time = new Date().getTime();
		this.elapsed_time = response_time - request_time;
	}
	
}
