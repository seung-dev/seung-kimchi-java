package seung.kimchi.java.utils;

import java.util.List;

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
public class SSignCertDer {

	private String type;
	private int version;
	private String serial_number;
	private String signiture_algorithm_oid;
	private String signiture_algorithm_name;
	private String issuer_dn;
	private String subject_dn;
	private long not_before_epoch;
	private String not_before_local;
	private long not_after_epoch;
	private String not_after_local;
	private List<String> key_usage;
	private String certificate_policy_oid;
	private String crl_distribution_point;
	private String subject_alternative_name_oid;
	private String vid_oid;
	private String vid_hash_algorithm_oid;
	private String vid;
	
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
