package seung.kimchi.java;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;

import lombok.extern.slf4j.Slf4j;
import seung.kimchi.java.utils.SAlgorithm;
import seung.kimchi.java.utils.SCharset;
import seung.kimchi.java.utils.SLinkedHashMap;

@Slf4j
public class SSecurity {

	public final static String _ALG_MODE_NONE = "NONE";
	public final static String _ALG_MODE_CBC = "CBC";
	public final static String _ALG_MODE_CCM = "CCM";
	public final static String _ALG_MODE_CFB = "CFB";
	public final static String _ALG_MODE_CTR = "CTR";
	public final static String _ALG_MODE_CTS = "CTS";
	public final static String _ALG_MODE_ECB = "ECB";
	public final static String _ALG_MODE_GCM = "GCM";
	public final static String _ALG_MODE_OFB = "OFB";
	public final static String _ALG_MODE_PCBC = "PCBC";
	
	public final static String _ALG_PADDING_NO = "NoPadding";
	public final static String _ALG_PADDING_ISO10126 = "ISO10126Padding";
	public final static String _ALG_PADDING_OAEP = "OAEPPadding";
	public final static String _ALG_PADDING_PKCS1 = "PKCS1Padding";
	public final static String _ALG_PADDING_PKCS5 = "PKCS5Padding";
	public final static String _ALG_PADDING_SSL3 = "SSL3Padding";
	
	public SSecurity() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * 복호화
	 * </pre>
	 * <h1>Request</h1>
	 * <pre>
	 *   data - 자료
	 *   transformation - AES/CBC/PKCS5Padding, RSA/ECB/OAEPWithSHA-256AndMGF1Padding, ...
	 *   provider - BouncyCastleProvider.PROVIDER_NAME(BC), ...
	 *   key - {@link SecretKeySpec}
	 *   iv_size - IV 길이
	 * </pre>
	 * <h1>Response</h1>
	 * <pre>
	 *   iv[iv_size] + encrypted[]
	 * </pre>
	 */
	public static byte[] decrypt(
			byte[] data
			, String transformation
			, String provider
			, Key key
			, int iv_size
			) {
		ByteBuffer byteBuffer = ByteBuffer.wrap(data);
		byte[] iv = new byte[iv_size];
		byteBuffer.get(iv);
		byte[] encrypted = new byte[byteBuffer.remaining()];
		byteBuffer.get(encrypted);
		return decrypt(
				encrypted//data
				, transformation
				, provider
				, key
				, iv
				);
	}
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * 복호화
	 * </pre>
	 * <h1>Request</h1>
	 * <pre>
	 *   data - 자료
	 *   transformation - AES/CBC/PKCS5Padding, RSA/ECB/OAEPWithSHA-256AndMGF1Padding, ...
	 *   provider - BouncyCastleProvider.PROVIDER_NAME(BC), ...
	 *   key - {@link SecretKeySpec}
	 *   algorithm_parameter_spec - {@link IvParameterSpec}, {@link OAEPParameterSpec}, ...
	 * </pre>
	 * <h1>Response</h1>
	 * <pre>
	 * </pre>
	 */
	public static byte[] decrypt(
			byte[] encrypted
			, String transformation
			, String provider
			, byte[] key
			, String algorithm
			, byte[] iv
			) {
		return decrypt(encrypted, transformation, provider, secret_key_spec(key, algorithm), iv_parameter_spec(iv));
	}
	public static byte[] decrypt(
			byte[] data
			, String transformation
			, String provider
			, Key key
			, byte[] iv
			) {
		return decrypt(data, transformation, provider, key, iv_parameter_spec(iv));
	}
	public static byte[] decrypt(
			byte[] encrypted
			, String transformation
			, String provider
			, Key key
			, AlgorithmParameterSpec algorithm_parameter_spec
			) {
		
		byte[] decrypted = null;
		
		Cipher cipher = null;
		try {
			
			if(provider != null && !"".equals(provider)) {
				cipher = Cipher.getInstance(transformation, provider);
			} else {
				cipher = Cipher.getInstance(transformation);
			}
			
			cipher.init(Cipher.DECRYPT_MODE, key, algorithm_parameter_spec);
			
			decrypted = cipher.doFinal(encrypted);
			
		} catch (NoSuchAlgorithmException e) {
			log.error("Failed to decrypt data.", e);
		} catch (NoSuchProviderException e) {
			log.error("Failed to decrypt data.", e);
		} catch (NoSuchPaddingException e) {
			log.error("Failed to decrypt data.", e);
		} catch (InvalidKeyException e) {
			log.error("Failed to decrypt data.", e);
		} catch (InvalidAlgorithmParameterException e) {
			log.error("Failed to decrypt data.", e);
		} catch (IllegalBlockSizeException e) {
			log.error("Failed to decrypt data.", e);
		} catch (BadPaddingException e) {
			log.error("Failed to decrypt data.", e);
		}
		
		return decrypted;
	}
	
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * 암호화
	 * </pre>
	 * <h1>Request</h1>
	 * <pre>
	 *   data - 자료
	 *   transformation - AES/CBC/PKCS5Padding, RSA/ECB/OAEPWithSHA-256AndMGF1Padding, ...
	 *   provider - BouncyCastleProvider.PROVIDER_NAME(BC), ...
	 *   key - {@link SecretKeySpec}
	 *   iv_size - IV 길이 {@link RSA_OAEP_T.secure_random_iv}
	 * </pre>
	 * <h1>Response</h1>
	 * <pre>
	 *   iv[iv_size] + encrypted[]
	 * </pre>
	 */
	public static byte[] encrypt(
			byte[] data
			, String transformation
			, String provider
			, Key key
			, int iv_size
			) {
		byte[] iv = secure_random_iv(iv_size);
		byte[] encrypted = encrypt(data, transformation, provider, key, iv);
		return ByteBuffer.allocate(iv.length + encrypted.length)
				.put(iv)
				.put(encrypted)
				.array()
				;
	}
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * 암호화
	 * </pre>
	 * <h1>Request</h1>
	 * <pre>
	 *   data - 자료
	 *   transformation - AES/CBC/PKCS5Padding, RSA/ECB/OAEPWithSHA-256AndMGF1Padding, ...
	 *   provider - BouncyCastleProvider.PROVIDER_NAME(BC), ...
	 *   key - {@link SecretKeySpec}
	 *   algorithm_parameter_spec - {@link IvParameterSpec}, {@link OAEPParameterSpec}, ...
	 * </pre>
	 * <h1>Response</h1>
	 * <pre>
	 * </pre>
	 */
	public static byte[] encrypt(
			byte[] data
			, String transformation
			, String provider
			, byte[] key
			, String algorithm
			, byte[] iv
			) {
		return encrypt(data, transformation, provider, secret_key_spec(key, algorithm), iv_parameter_spec(iv));
	}
	public static byte[] encrypt(
			byte[] data
			, String transformation
			, String provider
			, Key key
			, byte[] iv
			) {
		return encrypt(data, transformation, provider, key, iv_parameter_spec(iv));
	}
	public static byte[] encrypt(
			byte[] data
			, String transformation
			, String provider
			, Key key
			, AlgorithmParameterSpec algorithm_parameter_spec
			) {
		
		byte[] encrypted = null;
		
		Cipher cipher = null;
		try {
			
			if(provider != null && !"".equals(provider)) {
				cipher = Cipher.getInstance(transformation, provider);
			} else {
				cipher = Cipher.getInstance(transformation);
			}
			
			cipher.init(Cipher.ENCRYPT_MODE, key, algorithm_parameter_spec);
			
			encrypted = cipher.doFinal(data);
			
		} catch (NoSuchAlgorithmException e) {
			log.error("Failed to encrypt data.", e);
		} catch (NoSuchProviderException e) {
			log.error("Failed to encrypt data.", e);
		} catch (NoSuchPaddingException e) {
			log.error("Failed to encrypt data.", e);
		} catch (InvalidKeyException e) {
			log.error("Failed to encrypt data.", e);
		} catch (InvalidAlgorithmParameterException e) {
			log.error("Failed to encrypt data.", e);
		} catch (IllegalBlockSizeException e) {
			log.error("Failed to encrypt data.", e);
		} catch (BadPaddingException e) {
			log.error("Failed to encrypt data.", e);
		}
		
		return encrypted;
	}
	
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * IV 생성
	 * </pre>
	 * <h1>Request</h1>
	 * <pre>
	 *   iv - 
	 * </pre>
	 * <h1>Response</h1>
	 * <pre>
	 * </pre>
	 */
	public static AlgorithmParameterSpec iv_parameter_spec(
			byte[] iv
			) {
		return new IvParameterSpec(iv);
	}
	
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * 랜덤 IV 생성
	 * </pre>
	 * <h1>Request</h1>
	 * <pre>
	 *   iv_size - 랜덤 백터 크기
	 * </pre>
	 * <h1>Response</h1>
	 * <pre>
	 * </pre>
	 */
	public static byte[] secure_random_iv(
			int iv_size
			) {
		SecureRandom secureRandom = new SecureRandom();
		byte[] iv = new byte[iv_size];
		secureRandom.nextBytes(iv);
		return iv;
	}
	
	/**
	 * <h1>Description</h1>
	 * <pre>
	 * 암복호화 키 생성
	 * </pre>
	 * <h1>Request</h1>
	 * <pre>
	 *   key - 키
	 *   algorithm - AES, SEED, ...
	 * </pre>
	 * <h1>Response</h1>
	 * <pre>
	 * </pre>
	 */
	public static SecretKeySpec secret_key_spec(
			byte[] key
			, String algorithm
			) {
		return new SecretKeySpec(key, algorithm);
	}
	
	public static Key read_pem_private(
			String algorithm
			, String provider
			, String file_path
			) {
		Key key = null;
		try(
				FileReader fileReader = new FileReader(new File(file_path));
				PemReader pemReader = new PemReader(fileReader);
				) {
			PemObject pemObject= pemReader.readPemObject();
			byte[] content = pemObject.getContent();
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(content);
			KeyFactory keyFactory = KeyFactory.getInstance(algorithm, provider);
			key = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		} catch (Exception e) {
			log.error("Failed to read pem formatted file.", e);
		}
		return key;
	}
	
	public static Key read_pem_public(
			String algorithm
			, String provider
			, String file_path
			) {
		Key key = null;
		try(
				FileReader fileReader = new FileReader(new File(file_path));
				PemReader pemReader = new PemReader(fileReader);
				) {
			PemObject pemObject= pemReader.readPemObject();
			byte[] content = pemObject.getContent();
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(content);
			KeyFactory keyFactory = KeyFactory.getInstance(algorithm, provider);
			key = keyFactory.generatePublic(x509EncodedKeySpec);
		} catch (Exception e) {
			log.error("Failed to read pem formatted file.", e);
		}
		return key;
	}
	
	public static Long write_pem(
			String file_path
			, String file_name
			, String type
			, byte[] content
			) {
		long file_size = -1;
		try(
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				PemWriter pemWriter = new PemWriter(new OutputStreamWriter(byteArrayOutputStream));
				) {
			pemWriter.writeObject(new PemObject(type, content));
			pemWriter.flush();
			File key_file = new File(String.format("%s/%s", file_path, file_name));
			FileUtils.writeByteArrayToFile(key_file, byteArrayOutputStream.toByteArray());
			if(key_file.exists() && key_file.isFile()) {
				file_size = key_file.length();
			}
			pemWriter.close();
		} catch (Exception e) {
			log.error("Failed to convert to pem formatted file.", e);
		}
		return file_size;
	}
	
	public static KeyPair keypair(
			String algorithm
			, String provider
			, int asymmetric_key_size
			) throws NoSuchAlgorithmException, NoSuchProviderException {
		
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
				algorithm
				, provider
				);
		
		keyPairGenerator.initialize(
				asymmetric_key_size
				, new SecureRandom()
				);
		
		return keyPairGenerator.generateKeyPair();
	}// end of keypair
	
	/**
	 * <h1>Description</h1>
	 * <pre>{@code
	 * Convert to digested data.
	 * }</pre>
	 * <h1>Usage</h1>
	 * <pre>{@code
	 * SLinkedHashMap data = new SLinkedHashMap()
	 *   .add("key1", "value1")
	 *   .add("key2", "value2")
	 *   ;
	 * String digested = SSecurity.digest(SAlgorithm._MD5, data);
	 * System.out.println(digested);
	 * }</pre>
	 * <h1>Equal</h1>
	 * <pre>{@code
	 * SLinkedHashMap data = new SLinkedHashMap()
	 *   .add("key1", "value1")
	 *   .add("key2", "value2")
	 *   ;
	 * String digested = SConvert.encodeHexString(
	 *   digest(
	 *     SAlgorithm._MD5
	 *     , ""
	 *     , 1
	 *     , SConvert.stringify(data).getBytes(SCharset._UTF_8)
	 *     )
	 *   , true
	 *   );
	 * System.out.println(digested);
	 * }</pre>
	 * <hr>
	 * @param algorithm {@value SAlgorithm#_MD5}, {@value SAlgorithm#_SHA256}, ...
	 * @param data
	 * @see SConvert#encodeHexString(byte[], boolean)
	 * @see SSecurity#digest(String, String, int, byte[])
	 * @author seung
	 * @since 2021.01.06
	 * @version 1.0.0
	 */
	public static String digest(
			String algorithm
			, Object data
			) {
		String digested = "";
		try {
			digested = SConvert.encodeHexString(digest(algorithm, "", 1, SConvert.stringify(data).getBytes(SCharset._UTF_8)), true);
		} catch (UnsupportedEncodingException e) {
			log.error("Failed to digest data.", e);
		}
		return digested;
	}
	/**
	 * <h1>Description</h1>
	 * <pre>{@code
	 * Convert to digested data.
	 * }</pre>
	 * <h1>Usage</h1>
	 * <pre>{@code
	 * byte[] digested = SSecurity.digest(
	 *   "SHA-256"
	 *   , "data".getBytes()
	 *   );
	 * for(byte b : digested) {
	 *   System.out.println(b >= 0 ? String.format("%8s", Integer.toBinaryString(b)).replaceAll("\s", "0") : Integer.toBinaryString(b).substring(24));
	 * }
	 * }</pre>
	 * <h1>Equal</h1>
	 * <pre>{@code
	 * byte[] digested = SSecurity.digest(
	 *   "SHA-256"
	 *   , null
	 *   , 1
	 *   , "data".getBytes()
	 *   );
	 * for(byte b : digested) {
	 *   System.out.println(b >= 0 ? String.format("%8s", Integer.toBinaryString(b)).replaceAll("\s", "0") : Integer.toBinaryString(b).substring(24));
	 * }
	 * }</pre>
	 * <hr>
	 * @param algorithm {@value SAlgorithm#_MD5}, {@value SAlgorithm#_SHA256}, ...
	 * @param data
	 * @see SSecurity#digest(String, String, int, byte[])
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static byte[] digest(
			String algorithm
			, byte[] data
			) {
		return digest(algorithm, "", 1, data);
	}
	/**
	 * <h1>Description</h1>
	 * <pre>{@code
	 * Convert to digested data.
	 * }</pre>
	 * <h1>Usage</h1>
	 * <pre>{@code
	 * byte[] digested = SSecurity.digest(
	 *   "SHA-256"
	 *   , 1
	 *   , "data".getBytes()
	 *   );
	 * for(byte b : digested) {
	 *   System.out.println(b >= 0 ? String.format("%8s", Integer.toBinaryString(b)).replaceAll("\s", "0") : Integer.toBinaryString(b).substring(24));
	 * }
	 * }</pre>
	 * <h1>Equal</h1>
	 * <pre>{@code
	 * byte[] digested = SSecurity.digest(
	 *   "SHA-256"
	 *   , null
	 *   , 1
	 *   , "data".getBytes()
	 *   );
	 * for(byte b : digested) {
	 *   System.out.println(b >= 0 ? String.format("%8s", Integer.toBinaryString(b)).replaceAll("\s", "0") : Integer.toBinaryString(b).substring(24));
	 * }
	 * }</pre>
	 * <hr>
	 * @param algorithm {@value SAlgorithm#_MD5}, {@value SAlgorithm#_SHA256}, ...
	 * @param iteration
	 * @param data
	 * @see SSecurity#digest(String, String, int, byte[])
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static byte[] digest(
			String algorithm
			, int iteration
			, byte[] data
			) {
		return digest(algorithm, null, iteration, data);
	}
	/**
	 * <h1>Description</h1>
	 * <pre>{@code
	 * Convert to digested data.
	 * }</pre>
	 * <h1>Usage</h1>
	 * <pre>{@code
	 * byte[] digested = null;
	 * // without provider
	 * digested = SSecurity.digest(
	 *   "SHA-256"
	 *   , null
	 *   , 1
	 *   , "data".getBytes()
	 *   );
	 * for(byte b : digested) {
	 *   System.out.println(b >= 0 ? String.format("%8s", Integer.toBinaryString(b)).replaceAll("\s", "0") : Integer.toBinaryString(b).substring(24));
	 * }
	 * // with provider
	 * Security.addProvider(new BouncyCastleProvider());
	 * digested = SSecurity.digest(
	 *   "SHA-256"
	 *   , BouncyCastleProvider.PROVIDER_NAME
	 *   , 1
	 *   , "data".getBytes()
	 *   );
	 * for(byte b : digested) {
	 *   System.out.println(b >= 0 ? String.format("%8s", Integer.toBinaryString(b)).replaceAll("\s", "0") : Integer.toBinaryString(b).substring(24));
	 * }
	 * }</pre>
	 * <hr>
	 * @param algorithm {@value SAlgorithm#_MD5}, {@value SAlgorithm#_SHA256}, ...
	 * @param provider {@value BouncyCastleProvider#PROVIDER_NAME}
	 * @param iteration
	 * @param data
	 * @author seung
	 * @since 2020.12.21
	 * @version 1.0.0
	 */
	public static byte[] digest(
			String algorithm
			, String provider
			, int iteration
			, byte[] data
			) {
		byte[] digest = data.clone();
		MessageDigest message_digest;
		try {
			if(provider != null && !"".equals(provider)) {
				message_digest = MessageDigest.getInstance(algorithm, provider);
			} else {
				message_digest = MessageDigest.getInstance(algorithm);
			}
			for(int i = 0; i < iteration; i++) {
				message_digest.update(digest);
				digest = message_digest.digest();
			}
		} catch (NoSuchAlgorithmException e) {
			log.error("Failed to digest data.", e);
		} catch (NoSuchProviderException e) {
			log.error("Failed to digest data.", e);
		}
		return digest;
	}
	
	@SuppressWarnings("unchecked")
	public static SLinkedHashMap available_algorithms() {
		SLinkedHashMap algorithms = new SLinkedHashMap();
		for(String service_name : available_service_name()) {
			algorithms.put(service_name, algorithms(service_name));
		}
		return algorithms;
	}
	
	public static List<String> algorithms(String service_name) {
		return new ArrayList<>(Security.getAlgorithms(service_name));
	}
	
	public static List<String> available_service_name() {
		List<String> types = new ArrayList<>();
		for(Provider provider : Security.getProviders()) {
			provider.getServices().stream().forEach(s -> {
				if(!types.contains(s.getType())) {
					types.add(s.getType());
				}
			});
		}
		return types;
	}
	
	public static List<SLinkedHashMap> available_providers() {
		List<SLinkedHashMap> providers = new ArrayList<>();
		for(Provider provider : Security.getProviders()) {
			List<SLinkedHashMap> service = new ArrayList<>();
			provider.getServices().stream().forEach(s -> {
				service.add(new SLinkedHashMap()
						.add("type", s.getType())
						.add("class_name", s.getClassName())
						.add("algorithm", s.getAlgorithm())
						);
			});
			providers.add(new SLinkedHashMap()
					.add("name", provider.getName())
//					.add("version", provider.getVersionStr())
					.add("version", provider.getVersion())
					.add("info", provider.getInfo())
					.add("service", service)
					);
		}
		return providers;
	}
	
	public static int add_bouncy_castle_provider() {
		if(Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}
		return 1;
	}
	
}
