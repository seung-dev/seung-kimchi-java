package seung.kimchi.java.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Builder
@Getter
@Setter
@Slf4j
public class SEnv {

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
	private boolean shutdown = false;
	
	@Builder.Default
	private String shutdown_message = "";
	
	// os
	private String os_name;
	private String os_ver;
	@Builder.Default
	private boolean is_windows = false;
	@Builder.Default
	private boolean is_linux = false;
	@Builder.Default
	private boolean is_mac = false;
	
	// server
	@Builder.Default
	private int server_no = -1;
	private String host_name;
	private String ip4v;
	private String host;
	private int server_desc;
	
	// app
	@Builder.Default
	private int app_no = -1;
	private int server_port;
	private String app_name;
	private String app_ver;
	private String build_time;
	private String app_path;
	
	// mode
	private String app_mode;
	@Builder.Default
	private boolean is_ops = false;
	@Builder.Default
	private boolean is_dev = false;
	@Builder.Default
	private boolean is_loc = false;
	
	public void shutdown(boolean shutdown, String shutdown_message) {
		this.shutdown = shutdown;
		this.shutdown_message = shutdown_message;
	}
	
}
