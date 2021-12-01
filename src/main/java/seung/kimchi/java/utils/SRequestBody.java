package seung.kimchi.java.utils;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@Slf4j
public class SRequestBody implements Serializable {

	private static final long serialVersionUID = 1L;
	
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
	
	@Size(max = 36)
	@NotBlank
	private String request_code;
	
	private SLinkedHashMap data;
	
}
