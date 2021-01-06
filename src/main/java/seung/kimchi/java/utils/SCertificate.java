package seung.kimchi.java.utils;

import java.security.PublicKey;
import java.security.cert.X509Certificate;
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
public class SCertificate {

	private X509Certificate x509Certificate;
	private String type;
	private int version;
	private String serialNumber;
	private String serialNumberHex;
	private String sigAlgOID;
	private String sigAlgName;
	private String issuerDN;
	private String subjectDN;
	private long notBefore;
	private long notAfter;
	private PublicKey publicKey;
	private String publicKeyFormat;
	private String publicKeyAlgorithm;
	private List<String> crlDistPointList;
	
	public String toJsonString(boolean isPretty) {
		String jsonString = "";
		try {
			jsonString = new ObjectMapper()
					.setSerializationInclusion(Include.ALWAYS)
					.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
					.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
					.configure(SerializationFeature.INDENT_OUTPUT, isPretty)
					.writeValueAsString(this)
					;
		} catch (JsonProcessingException e) {
			log.error("Failed to convert to json format text.", e);
		}
		return jsonString;
	}
	
}
