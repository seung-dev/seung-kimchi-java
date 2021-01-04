package seung.kimchi.java.utils;

import java.math.BigInteger;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SSignCertDer {

	private byte[] encoded;
	private String type;
	private int version;
	private BigInteger serialNumber;
	private String serialNumberHex;
	private String sigAlgOID;
	private String sigAlgName;
	private String issuerDN;
	private String subjectDN;
	private long notBefore;
	private long notAfter;
	private byte[] publicKey;
	private String publicKeyFormat;
	private String publicKeyAlgorithm;
	private byte[] signPriKey;
	
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
		}
		return jsonString;
	}
	
}
