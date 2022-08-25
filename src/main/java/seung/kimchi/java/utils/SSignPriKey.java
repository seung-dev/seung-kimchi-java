package seung.kimchi.java.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Slf4j
public class SSignPriKey {

	private String private_key_algorythm_oid;
	private String encryption_algorithm_oid;
	private byte[] salt;
	private int iteration_count;
	private int key_length;
	private String prf_algorithm_oid;
	private byte[] iv;
	private byte[] private_key;
	
	public String stringify() {
		return stringify(false);
	}
	public String stringify(boolean is_pretty) {
		String json = "{}";
		try {
			json = new ObjectMapper()
					.setSerializationInclusion(Include.ALWAYS)
					.configure(SerializationFeature.INDENT_OUTPUT, is_pretty)
					.writeValueAsString(this)
					;
		} catch (JsonProcessingException e) {
			log.error("Failed to convert to json format text.", e);
		}
		return json;
	}
	
}
