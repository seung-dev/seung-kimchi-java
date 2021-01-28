package seung.kimchi.java.utils;

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
public class SEnv {

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
	private boolean shutdown = false;
	
	@Builder.Default
	private String shutdown_message = "";
	
	// server
	@Builder.Default
	private int server_no = -1;
	
	@Builder.Default
	private boolean is_windows = false;
	
	@Builder.Default
	private boolean is_linux = false;
	
	private String host_name;
	
	private String ip4v;
	
	private String domain;
	
	private String os_name;
	
	private String os_ver;
	
	// app
	@Builder.Default
	private int app_no = -1;
	
	private String app_name;
	
	private String app_ver;
	
	private String build_time;
	
	private int server_port;
	
	private String app_mode;
	
	@Builder.Default
	private boolean is_ops = false;
	
	@Builder.Default
	private boolean is_dev = false;
	
	@Builder.Default
	private boolean is_loc = false;
	
	@Builder.Default
	private boolean batch_enabled = false;
	
	public void shutdown(boolean shutdown, String shutdown_message) {
		this.shutdown = shutdown;
		this.shutdown_message = shutdown_message;
	}
	
}
