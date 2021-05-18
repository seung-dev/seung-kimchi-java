package seung.kimchi.java.utils.excel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
@Setter
@Getter
@Slf4j
public class SExcel implements Serializable {

	private static final long serialVersionUID = 1L;
	
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
	
	private String error_code;
	private String error_message;
	
	private int numberOfSheets;
	@Builder.Default
	private List<SSheet> sheets = new ArrayList<>();
	
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
	
}
