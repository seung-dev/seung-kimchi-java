package seung.kimchi.java;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import lombok.extern.slf4j.Slf4j;
import seung.kimchi.java.utils.SCharset;
import seung.kimchi.java.utils.SLinkedHashMap;

@Slf4j
public class SConvert {

	public SConvert() {
		// TODO Auto-generated constructor stub
	}
	
	public static String exception(Exception exception) {
//		return exception.getMessage();
		StringWriter stringWriter = new StringWriter();
		exception.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static List<SLinkedHashMap> toListSLinkedHashMap(String data) {
		List<SLinkedHashMap> items = null;
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
			items = objectMapper
					.registerModule(
							new SimpleModule("seung", Version.unknownVersion())
							.addAbstractTypeMapping(Map.class, SLinkedHashMap.class)
							)
					.readValue(data, List.class)
					;
		} catch (JsonParseException e) {
			log.error("Failed to convert to SLinkedHashMap.", e);
		} catch (JsonMappingException e) {
			log.error("Failed to convert to SLinkedHashMap.", e);
		} catch (IOException e) {
			log.error("Failed to convert to SLinkedHashMap.", e);
		}
		return items;
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
	 * <pre>{@code
	 * Convert to json format text.
	 * }</pre>
	 * <h1>Usage</h1>
	 * <pre>{@code
	 * Map<String, String> map = new HashMap<>();
	 * map.put("key1", "value1");
	 * map.put("key2", "value2");
	 * System.out.println(SConvert.stringify(map));
	 * }</pre>
	 * <h1>Equal</h1>
	 * <pre>{@code
	 * Map<String, String> map = new HashMap<>();
	 * map.put("key1", "value1");
	 * map.put("key2", "value2");
	 * System.out.println(SConvert.stringify(map, 0, "  "));
	 * }</pre>
	 * <hr>
	 * @param data
	 * @author seung
	 * @since 2021.01.04
	 * @version 1.0.0
	 */
	public static String stringify(Object data) {
		return stringify(data, false);
	}
	/**
	 * <h1>Description</h1>
	 * <pre>{@code
	 * Convert to json format text.
	 * }</pre>
	 * <h1>Usage</h1>
	 * <pre>{@code
	 * Map<String, String> map = new HashMap<>();
	 * map.put("key1", "value1");
	 * map.put("key2", "value2");
	 * System.out.println(SConvert.stringify(map, 1, "    "));
	 * System.out.println(SConvert.stringify(map, 1, "\t"));
	 * }</pre>
	 * <hr>
	 * @param data
	 * @param isPretty - {@link Boolean#FALSE}, {@link Boolean#TRUE}
	 * @param indent - default: 2 spaces
	 * @author seung
	 * @since 2021.01.04
	 * @version 1.0.0
	 */
	public static String stringify(Object data, boolean isPretty) {
		String json = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.getSerializerProvider().setNullKeySerializer(new JsonSerializer<Object>() {
				@Override
				public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
					jsonGenerator.writeFieldName("");
				}
			});
			json = objectMapper
					.setSerializationInclusion(Include.ALWAYS)
					.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
					.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
					.configure(SerializationFeature.INDENT_OUTPUT, isPretty)
					.writeValueAsString(data)
					;
		} catch (JsonProcessingException e) {
			log.error("Failed to convert to json format text.", e);
		}
		return json;
	}
	
	public static byte[] decompress(byte[] data) {
		return decompress(data, true);
	}
	public static byte[] decompress(byte[] data, boolean nowrap) {
		
		byte[] inflated = null;
		
		Inflater inflater = new Inflater(nowrap);
		
		inflater.setInput(data);
		
		byte[] b = new byte[1024];
		int len;
		try (
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				) {
			
			while(!inflater.finished()) {
				len = inflater.inflate(b);
				byteArrayOutputStream.write(b, 0, len);
			}
			
			inflated = byteArrayOutputStream.toByteArray();
			inflater.end();
			
		} catch (IOException e) {
			log.error("Failed to decompress data.", e);
		} catch (DataFormatException e) {
			log.error("Failed to decompress data.", e);
		}
		
		return inflated;
	}
	
	public static byte[] compress(byte[] data) {
		return compress(data, Deflater.BEST_COMPRESSION, true);
	}
	public static byte[] compress(byte[] data, int level, boolean nowrap) {
		
		byte[] deflated = null;
		
		Deflater deflater = new Deflater(level, nowrap);
		
		deflater.setInput(data);
		deflater.finish();
		
		byte[] b = new byte[1024];
		int len;
		try (
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				) {
			
			while(!deflater.finished()) {
				len = deflater.deflate(b);
				byteArrayOutputStream.write(b, 0, len);
			}
			
			deflated = byteArrayOutputStream.toByteArray();
			deflater.end();
			
		} catch (IOException e) {
			log.error("Failed to compress data.", e);
		}
		
		return deflated;
	}
	
	/**
	 * <h1>Description</h1>
	 * <pre>{@code
	 * Convert to base64 decoded data.
	 * }</pre>
	 * <h1>Usage</h1>
	 * <pre>{@code
	 * String decoded = SConvert.decodeBase64String(
	 *   "ZGF0YQ=="
	 *   );
	 * System.out.println(decoded);
	 * }</pre>
	 * <h1>Equal</h1>
	 * <pre>{@code
	 * String decoded = SConvert.decodeBase64String(
	 *   "ZGF0YQ=="
	 *   , SCharset._UTF_8
	 *   );
	 * System.out.println(decoded);
	 * }</pre>
	 * <hr>
	 * @param encoded
	 * @see SConvert#decodeBase64String(byte[], String)
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
	 * <pre>{@code
	 * Convert to base64 decoded data.
	 * }</pre>
	 * <h1>Usage</h1>
	 * <pre>{@code
	 * String decoded = SConvert.decodeBase64String(
	 *   "ZGF0YQ==".getBytes()
	 *   , SCharset._UTF_8
	 *   );
	 * System.out.println(decoded);
	 * }</pre>
	 * <hr>
	 * @param encoded
	 * @param charset {@value SCharset#_UTF_8}, {@value SCharset#_EUC_KR}, ...
	 * @see SConvert#decodeBase64String(byte[], String)
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
	public static byte[] decodeBase64(String encoded, String charset) throws UnsupportedEncodingException {
		return Base64.decodeBase64(encoded.getBytes(charset));
	}
	/**
	 * <h1>Description</h1>
	 * <pre>{@code
	 * Convert to base64 decoded data.
	 * }</pre>
	 * <h1>Usage</h1>
	 * <pre>{@code
	 * byte[] decoded = SConvert.decodeBase64(
	 *   "ZGF0YQ==".getBytes()
	 *   );
	 * for(byte b : decoded) {
	 *   System.out.println(b >= 0 ? String.format("%8s", Integer.toBinaryString(b)).replaceAll("\s", "0") : Integer.toBinaryString(b).substring(24));
	 * }
	 * }</pre>
	 * <hr>
	 * @param encoded
	 * @see Base64#decodeBase64(byte[])
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static byte[] decodeBase64(byte[] encoded) {
		return Base64.decodeBase64(encoded);
	}
	
	/**
	 * <h1>Description</h1>
	 * <pre>{@code
	 * Convert to base64 encoded data.
	 * }</pre>
	 * <h1>Usage</h1>
	 * <pre>{@code
	 * String encoded = SConvert.encodeBase64String(
	 *   "data".getBytes()
	 *   , SCharset._UTF_8
	 *   );
	 * System.out.println(encoded);
	 * }</pre>
	 * <hr>
	 * @param data
	 * @param charset {@value SCharset#_UTF_8}, {@value SCharset#_EUC_KR}, ...
	 * @see SConvert#encodeBase64(byte[])
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
	 * <pre>{@code
	 * Convert to base64 decoded data.
	 * }</pre>
	 * <h1>Usage</h1>
	 * <pre>{@code
	 * byte[] encoded = SConvert.encodeBase64(
	 *   "data".getBytes()
	 *   );
	 * for(byte b : encoded) {
	 *   System.out.println(b >= 0 ? String.format("%8s", Integer.toBinaryString(b)).replaceAll("\s", "0") : Integer.toBinaryString(b).substring(24));
	 * }
	 * }</pre>
	 * <hr>
	 * @param data
	 * @see Base64#encodeBase64(byte[])
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static byte[] encodeBase64(byte[] data) {
		return Base64.encodeBase64(data);
	}
	
	/**
	 * <h1>Description</h1>
	 * <pre>{@code
	 * Convert to hex decoded data.
	 * }</pre>
	 * <h1>Usage</h1>
	 * <pre>{@code
	 * String decoded = SConvert.decodeHexString(
	 *   "64617461"
	 *   );
	 * System.out.println(decoded);
	 * }</pre>
	 * <h1>Equal</h1>
	 * <pre>{@code
	 * String decoded = SConvert.decodeHexString(
	 *   "64617461"
	 *   , SCharset._UTF_8
	 *   );
	 * System.out.println(decoded);
	 * }</pre>
	 * <hr>
	 * @param encoded
	 * @see SConvert#decodeHexString(String, String)
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static String decodeHexString(String encoded) {
		return decodeHexString(encoded, SCharset._UTF_8);
	}
	/**
	 * <h1>Description</h1>
	 * <pre>{@code
	 * Convert to hex decoded data.
	 * }</pre>
	 * <h1>Usage</h1>
	 * <pre>{@code
	 * String decoded = SConvert.decodeHexString(
	 *   "64617461"
	 *   , SCharset._UTF_8
	 *   );
	 * System.out.println(decoded);
	 * }</pre>
	 * <hr>
	 * @param encoded
	 * @param charset {@value SCharset#_UTF_8}, {@value SCharset#_EUC_KR}, ...
	 * @see SConvert#decodeHex(String)
	 * @see SCharset}
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
	 * <pre>{@code
	 * Convert to hex decoded data.
	 * }</pre>
	 * <h1>Usage</h1>
	 * <pre>{@code
	 * byte[] decoded = SConvert.decodeHexString(
	 *   "64617461"
	 *   );
	 * for(byte b : decoded) {
	 *   System.out.println(b >= 0 ? String.format("%8s", Integer.toBinaryString(b)).replaceAll("\s", "0") : Integer.toBinaryString(b).substring(24));
	 * }
	 * }</pre>
	 * <hr>
	 * @param encoded
	 * @see Hex#decodeHex(String)
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
	 * <pre>{@code
	 * Convert to hex encoded data.
	 * }</pre>
	 * <h1>Usage</h1>
	 * <pre>{@code
	 * String encoded = SConvert.encodeHexString(
	 *   new BigInteger("1234567890")
	 *   , true
	 *   );
	 * System.out.println(encoded);
	 * }</pre>
	 * <hr>
	 * @param data
	 * @param toLowerCase
	 * @see SConvert#encodeHexString(byte[], boolean)
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static String encodeHexString(BigInteger data, boolean toLowerCase) {
		return encodeHexString(data.toByteArray(), toLowerCase);
	}
	/**
	 * <h1>Description</h1>
	 * <pre>{@code
	 * Convert to hex encoded data.
	 * }</pre>
	 * <h1>Usage</h1>
	 * <pre>{@code
	 * String encoded = SConvert.encodeHexString(
	 *   "data"
	 *   );
	 * System.out.println(encoded);
	 * }</pre>
	 * <h1>Equal</h1>
	 * <pre>{@code
	 * SConvert.encodeHexString(
	 *   "data"
	 *   , true
	 *   );
	 * }</pre>
	 * <hr>
	 * @param data
	 * @see SConvert#encodeHexString(byte[], boolean)
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
	 * <pre>{@code
	 * Convert to hex encoded data.
	 * }</pre>
	 * <h1>Usage</h1>
	 * <pre>{@code
	 * String encoded = SConvert.encodeHexString(
	 *   "data".getBytes()
	 *   , true
	 *   );
	 * System.out.println(encoded);
	 * }</pre>
	 * <hr>
	 * @param data
	 * @param toLowerCase
	 * @see Base64#encodeBase64(byte[])
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static String encodeHexString(byte[] data, boolean toLowerCase) {
		return Hex.encodeHexString(data, toLowerCase);
	}
	
	public static String encodeURIComponent(String data, String charset) {
		String encoded = "";
		try {
			encoded = URLEncoder
					.encode(data, charset)
					.replaceAll("\\+", "%20")
					.replaceAll("\\%21", "!")
					.replaceAll("\\%27", "'")
					.replaceAll("\\%28", "(")
					.replaceAll("\\%29", ")")
					.replaceAll("\\%7E", "~")
					;
		} catch (UnsupportedEncodingException e) {
			log.error("Failed to convert to url encoded text.", e);
		}
		return encoded;
	}
	
	/**
	 * <h1>Description</h1>
	 * <pre>{@code
	 * Available Charset.
	 * }</pre>
	 * <h1>Usage</h1>
	 * <pre>{@code
	 * for(String charset : SConvert.availableCharset()) {
	 *   System.out.println(charset);
	 * }
	 * }</pre>
	 * <hr>
	 * @see Charset#availableCharsets()
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static Set<String> availableCharset() {
		return Charset.availableCharsets().keySet();
	}
	
}
