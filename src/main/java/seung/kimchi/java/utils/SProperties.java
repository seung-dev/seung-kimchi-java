package seung.kimchi.java.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SProperties {

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
	private Properties environment = new Properties();
	
	@Builder.Default
	private Properties build = new Properties();
	
	@Builder.Default
	private List<Properties> datasource = new ArrayList<>();
	
	@Builder.Default
	private Properties security = new Properties();
	
	@Builder.Default
	private Properties seung = new Properties();

}
