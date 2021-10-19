package seung.kimchi.java.utils;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Builder
@Setter
@Getter
@Slf4j
public class SRequestHeader {

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
	
	@Builder.Default
	private long request_time = new Date().getTime();
	
	@Builder.Default
	private SLinkedHashMap header = new SLinkedHashMap();
	
	@Builder.Default
	private SLinkedHashMap cookie = new SLinkedHashMap();
	
	@Builder.Default
	private SLinkedHashMap session = new SLinkedHashMap();
	
	public void header(String key, Object value) {
		this.header.add(key, value);
	}
	
	public String headers(String key) {
		return this.header.getString(key, "");
	}
	
	public void cookie(String key, Object value) {
		this.cookie.add(key, value);
	}
	
	public String cookie(String key) {
		return this.cookie.getString(key, "");
	}
	
	@SuppressWarnings("unchecked")
	public void session(Object key, Object value) {
		this.session.put(key, value);
	}
	
	public Object session(Object key) {
		return this.session.get(key);
	}
	
	public String sessionText(Object key) {
		return this.session.getString(key, "");
	}
	
}
