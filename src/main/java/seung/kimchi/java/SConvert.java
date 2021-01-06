package seung.kimchi.java;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.Indenter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import lombok.extern.slf4j.Slf4j;
import seung.kimchi.java.utils.SAlgorithm;
import seung.kimchi.java.utils.SCharset;
import seung.kimchi.java.utils.SLinkedHashMap;

@Slf4j
public class SConvert {

	public SConvert() {
		// TODO Auto-generated constructor stub
	}
	
	public static SLinkedHashMap toSLinkedHashMap(String data) {
		SLinkedHashMap sLinkedHashMap = null;
		try {
			if(data == null || "".equals(data)) {
				log.error("Field data is empty.");
			}
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.getSerializerProvider().setNullKeySerializer(new JsonSerializer<Object>() {
				@Override
				public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
					jsonGenerator.writeFieldName("");
				}
			});
			sLinkedHashMap = objectMapper
					.registerModule(
							new SimpleModule("seung", Version.unknownVersion())
							.addAbstractTypeMapping(Map.class, SLinkedHashMap.class)
							)
					.readValue(data, SLinkedHashMap.class)
					;
		} catch (JsonParseException e) {
			log.error("Failed to convert to SLinkedHashMap.", e);
		} catch (JsonMappingException e) {
			log.error("Failed to convert to SLinkedHashMap.", e);
		} catch (IOException e) {
			log.error("Failed to convert to SLinkedHashMap.", e);
		}
		return sLinkedHashMap;
	}
	
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * Convert to json format text.
	 * </pre>
	 * <h1>Usage</h1>
	 * <pre>
	 * Map<String, String> map = new HashMap<>();
	 * map.put("key1", "value1");
	 * map.put("key2", "value2");
	 * System.out.println(SConvert.stringify(map));
	 * </pre>
	 * <h1>Equal</h1>
	 * <pre>
	 * Map<String, String> map = new HashMap<>();
	 * map.put("key1", "value1");
	 * map.put("key2", "value2");
	 * System.out.println(SConvert.stringify(map, 0, "  "));
	 * </pre>
	 * <hr/>
	 * @param data
	 * @see {@link #stringify(Object, int, String)}
	 * @author seung
	 * @since 2021.01.04
	 * @version 1.0.0
	 */
	public static String stringify(Object data) {
		return stringify(data, 0, "  ");
	}
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * Convert to json format text.
	 * </pre>
	 * <h1>Usage</h1>
	 * <pre>
	 * Map<String, String> map = new HashMap<>();
	 * map.put("key1", "value1");
	 * map.put("key2", "value2");
	 * System.out.println(SConvert.stringify(map, 0));
	 * System.out.println(SConvert.stringify(map, 1));
	 * </pre>
	 * <h1>Equal</h1>
	 * <pre>
	 * Map<String, String> map = new HashMap<>();
	 * map.put("key1", "value1");
	 * map.put("key2", "value2");
	 * System.out.println(SConvert.stringify(map, 0, "  "));
	 * System.out.println(SConvert.stringify(map, 1, "  "));
	 * </pre>
	 * <hr/>
	 * @param data
	 * @param isPretty - 0: {@link Boolean#FALSE}, 1: {@link Boolean#TRUE}
	 * @see {@link #stringify(Object, int, String)}
	 * @author seung
	 * @since 2021.01.04
	 * @version 1.0.0
	 */
	public static String stringify(Object data, int isPretty) {
		return stringify(data, isPretty, "  ");
	}
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * Convert to json format text.
	 * </pre>
	 * <h1>Usage</h1>
	 * <pre>
	 * Map<String, String> map = new HashMap<>();
	 * map.put("key1", "value1");
	 * map.put("key2", "value2");
	 * System.out.println(SConvert.stringify(map, 1, "    "));
	 * System.out.println(SConvert.stringify(map, 1, "\t"));
	 * </pre>
	 * <hr/>
	 * @param data
	 * @param isPretty - 0: {@link Boolean#FALSE}, 1: {@link Boolean#TRUE}
	 * @param indent - default: 2 spaces
	 * @see {@link ObjectMapper#writeValueAsString(Object)}
	 * @see {@link ObjectMapper#writerWithDefaultPrettyPrinter()}
	 * @see {@link ObjectMapper#setDefaultPrettyPrinter(com.fasterxml.jackson.core.PrettyPrinter)}
	 * @see {@link ObjectMapper#getSerializerProvider()}
	 * @see {@link DefaultPrettyPrinter#indentObjectsWith(Indenter)}
	 * @see {@link DefaultPrettyPrinter#indentArraysWith(Indenter)}
	 * @see {@link DefaultIndenter#withIndent(String)}
	 * @see {@link DefaultIndenter#withLinefeed(String)}
	 * @author seung
	 * @since 2021.01.04
	 * @version 1.0.0
	 */
	public static String stringify(Object data, int isPretty, String indent) {
		String json = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.getSerializerProvider().setNullKeySerializer(new JsonSerializer<Object>() {
				@Override
				public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
					jsonGenerator.writeFieldName("");
				}
			});
			if(1 == isPretty) {
				if(indent == null) {
					indent = "  ";
				}
				DefaultIndenter defaultIndenter = new DefaultIndenter();
				defaultIndenter.withIndent(indent);
				defaultIndenter.withLinefeed(DefaultIndenter.SYS_LF);
				DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
				defaultPrettyPrinter.indentObjectsWith(defaultIndenter);
				defaultPrettyPrinter.indentArraysWith(defaultIndenter);
				json = objectMapper
						.setDefaultPrettyPrinter(defaultPrettyPrinter)
						.writerWithDefaultPrettyPrinter()
						.writeValueAsString(data)
						;
			} else {
				json = objectMapper.writeValueAsString(data);
			}
		} catch (JsonProcessingException e) {
			log.error("Failed to convert to json format text.", e);
		}
		return json;
	}
	
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * Convert to base64 decoded data.
	 * </pre>
	 * <h1>Usage</h1>
	 * <pre>
	 * String decoded = SConvert.decodeBase64String(
	 *   "ZGF0YQ=="
	 *   );
	 * System.out.println(decoded);
	 * </pre>
	 * <h1>Equal</h1>
	 * <pre>
	 * String decoded = SConvert.decodeBase64String(
	 *   "ZGF0YQ=="
	 *   , SCharset._UTF_8
	 *   );
	 * System.out.println(decoded);
	 * </pre>
	 * <hr/>
	 * @param encoded
	 * @see {@link SConvert#decodeBase64String(byte[], String)}
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static String decodeBase64String(String encoded) {
		String data = "";
		try {
			data = decodeBase64String(encoded.getBytes(SCharset._UTF_8), SCharset._UTF_8);
		} catch (UnsupportedEncodingException e) {
			log.error("Failed to decode encoded data.", e);
		}
		return data;
	}
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * Convert to base64 decoded data.
	 * </pre>
	 * <h1>Usage</h1>
	 * <pre>
	 * String decoded = SConvert.decodeBase64String(
	 *   "ZGF0YQ==".getBytes()
	 *   , SCharset._UTF_8
	 *   );
	 * System.out.println(decoded);
	 * </pre>
	 * <hr/>
	 * @param encoded
	 * @param charset {@value SCharset#_UTF_8}, {@value SCharset#_EUC_KR}, ...
	 * @see {@link SConvert#decodeBase64String(byte[], String)}
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static String decodeBase64String(byte[] encoded, String charset) {
		String data = "";
		try {
			data = new String(decodeBase64(encoded), charset);
		} catch (UnsupportedEncodingException e) {
			log.error("Failed to convert to base64 decoded data.", e);
		}
		return data;
	}
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * Convert to base64 decoded data.
	 * </pre>
	 * <h1>Usage</h1>
	 * <pre>
	 * byte[] decoded = SConvert.decodeBase64(
	 *   "ZGF0YQ==".getBytes()
	 *   );
	 * for(byte b : decoded) {
	 *   System.out.println(b >= 0 ? String.format("%8s", Integer.toBinaryString(b)).replaceAll("\s", "0") : Integer.toBinaryString(b).substring(24));
	 * }
	 * </pre>
	 * <hr/>
	 * @param encoded
	 * @see {@link Base64#decodeBase64(byte[])}
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static byte[] decodeBase64(byte[] encoded) {
		return Base64.decodeBase64(encoded);
	}
	
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * Convert to base64 encoded data.
	 * </pre>
	 * <h1>Usage</h1>
	 * <pre>
	 * String encoded = SConvert.encodeBase64String(
	 *   "data".getBytes()
	 *   , SCharset._UTF_8
	 *   );
	 * System.out.println(encoded);
	 * </pre>
	 * <hr/>
	 * @param data
	 * @param charset {@value SCharset#_UTF_8}, {@value SCharset#_EUC_KR}, ...
	 * @see {@link SConvert#encodeBase64(byte[])}
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static String encodeBase64String(byte[] data, String charset) {
		String encoded = "";
		try {
			encoded = new String(encodeBase64(data), charset);
		} catch (UnsupportedEncodingException e) {
			log.error("Failed to convert to base64 encoded data.", e);
		}
		return encoded;
	}
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * Convert to base64 decoded data.
	 * </pre>
	 * <h1>Usage</h1>
	 * <pre>
	 * byte[] encoded = SConvert.encodeBase64(
	 *   "data".getBytes()
	 *   );
	 * for(byte b : encoded) {
	 *   System.out.println(b >= 0 ? String.format("%8s", Integer.toBinaryString(b)).replaceAll("\s", "0") : Integer.toBinaryString(b).substring(24));
	 * }
	 * </pre>
	 * <hr/>
	 * @param data
	 * @see {@link Base64#encodeBase64(byte[])}
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static byte[] encodeBase64(byte[] data) {
		return Base64.encodeBase64(data);
	}
	
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * Convert to hex decoded data.
	 * </pre>
	 * <h1>Usage</h1>
	 * <pre>
	 * String decoded = SConvert.decodeHexString(
	 *   "64617461"
	 *   );
	 * System.out.println(decoded);
	 * </pre>
	 * <h1>Equal</h1>
	 * <pre>
	 * String decoded = SConvert.decodeHexString(
	 *   "64617461"
	 *   , SCharset._UTF_8
	 *   );
	 * System.out.println(decoded);
	 * </pre>
	 * <hr/>
	 * @param encoded
	 * @see {@link SConvert#decodeHexString(String, String)}
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static String decodeHexString(String encoded) {
		return decodeHexString(encoded, SCharset._UTF_8);
	}
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * Convert to hex decoded data.
	 * </pre>
	 * <h1>Usage</h1>
	 * <pre>
	 * String decoded = SConvert.decodeHexString(
	 *   "64617461"
	 *   , SCharset._UTF_8
	 *   );
	 * System.out.println(decoded);
	 * </pre>
	 * <hr/>
	 * @param data
	 * @param charset {@value SCharset#_UTF_8}, {@value SCharset#_EUC_KR}, ...
	 * @see {@link SConvert#decodeHex(String)}
	 * @see {@link SCharset}
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static String decodeHexString(String encoded, String charset) {
		String decoded = "";
		try {
			decoded = new String(decodeHex(encoded), charset);
		} catch (UnsupportedEncodingException e) {
			log.error("Failed to convert to hex decoded data.", e);
		}
		return decoded;
	}
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * Convert to hex decoded data.
	 * </pre>
	 * <h1>Usage</h1>
	 * <pre>
	 * byte[] decoded = SConvert.decodeHexString(
	 *   "64617461"
	 *   );
	 * for(byte b : decoded) {
	 *   System.out.println(b >= 0 ? String.format("%8s", Integer.toBinaryString(b)).replaceAll("\s", "0") : Integer.toBinaryString(b).substring(24));
	 * }
	 * </pre>
	 * <hr/>
	 * @param encoded
	 * @see {@link Hex#decodeHex(String)}
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static byte[] decodeHex(String encoded) {
		byte[] decoded = null;
		try {
			decoded = Hex.decodeHex(encoded);
		} catch (DecoderException e) {
			log.error("Failed to convert to hex decoded data.", e);
		}
		return decoded;
	}
	
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * Convert to hex encoded data.
	 * </pre>
	 * <h1>Usage</h1>
	 * <pre>
	 * String encoded = SConvert.encodeHexString(
	 *   new BigInteger("1234567890")
	 *   , true
	 *   );
	 * System.out.println(encoded);
	 * </pre>
	 * <hr/>
	 * @param data
	 * @param toLowerCase
	 * @see {@link SConvert#encodeHexString(byte[], boolean)}
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static String encodeHexString(BigInteger data, boolean toLowerCase) {
		return encodeHexString(data.toByteArray(), toLowerCase);
	}
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * Convert to hex encoded data.
	 * </pre>
	 * <h1>Usage</h1>
	 * <pre>
	 * String encoded = SConvert.encodeHexString(
	 *   "data"
	 *   );
	 * System.out.println(encoded);
	 * </pre>
	 * <h1>Equal</h1>
	 * <pre>
	 * SConvert.encodeHexString(
	 *   "data"
	 *   , true
	 *   );
	 * </pre>
	 * <hr/>
	 * @param data
	 * @see {@link SConvert#encodeHexString(byte[], boolean)}
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static String encodeHexString(String data) {
		String encoded = "";
		try {
			return encodeHexString(data.getBytes(SCharset._UTF_8), true);
		} catch (UnsupportedEncodingException e) {
			log.error("Failed to convert to hex encoded data.", e);
		}
		return encoded;
	}
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * Convert to hex encoded data.
	 * </pre>
	 * <h1>Usage</h1>
	 * <pre>
	 * String encoded = SConvert.encodeHexString(
	 *   "data".getBytes()
	 *   , true
	 *   );
	 * System.out.println(encoded);
	 * </pre>
	 * <hr/>
	 * @param data
	 * @param toLowerCase
	 * @see {@link Base64#encodeBase64(byte[])}
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static String encodeHexString(byte[] data, boolean toLowerCase) {
		return Hex.encodeHexString(data, toLowerCase);
	}
	
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * Available Charset.
	 * </pre>
	 * <h1>Usage</h1>
	 * <pre>
	 * for(String charset : SConvert.availableCharset()) {
	 *   System.out.println(charset);
	 * }
	 * </pre>
	 * <hr/>
	 * @see {@link Charset#availableCharsets()}
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static Set<String> availableCharset() {
		return Charset.availableCharsets().keySet();
	}
	
}
