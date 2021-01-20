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
	private String shutdownMessage = "";
	
	// server
	@Builder.Default
	private int serverNo = -1;
	
	@Builder.Default
	private boolean isWindows = false;
	
	@Builder.Default
	private boolean isLinux = false;
	
	private String hostName;
	
	private String ip4v;
	
	private String domain;
	
	private String osName;
	
	private String osVer;
	
	// app
	@Builder.Default
	private int appNo = -1;
	
	private String projName;
	
	private String projVer;
	
	private String projTime;
	
	private int serverPort;
	
	private String profilesActive;
	
	@Builder.Default
	private boolean isOps = false;
	
	@Builder.Default
	private boolean isDev = false;
	
	@Builder.Default
	private boolean isLoc = false;
	
	@Builder.Default
	private boolean batchEnabled = false;
	
	public void shutdown(boolean shutdown, String shutdownMessage) {
		this.shutdown = shutdown;
		this.shutdownMessage = shutdownMessage;
	}
	
}
