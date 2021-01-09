package seung.kimchi.java.utils;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class SRequest {

	public String stringify(boolean isPretty) throws JsonProcessingException {
		return new ObjectMapper()
				.setSerializationInclusion(Include.ALWAYS)
				.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
				.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
				.configure(SerializationFeature.INDENT_OUTPUT, isPretty)
				.writeValueAsString(this)
				;
	}
	
	@Builder.Default
	private long request_time = new Date().getTime();
	
	@Builder.Default
	private SLinkedHashMap network = new SLinkedHashMap();
	
	@Builder.Default
	private SLinkedHashMap header = new SLinkedHashMap();
	
	@Builder.Default
	private SLinkedHashMap session = new SLinkedHashMap();
	
	@Builder.Default
	private SLinkedHashMap data = new SLinkedHashMap();
	
	@SuppressWarnings("unchecked")
	public void network(Object key, Object value) {
		this.network.put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public void header(Object key, Object value) {
		this.header.put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public void session(Object key, Object value) {
		this.network.put(key, value);
	}
	
}
