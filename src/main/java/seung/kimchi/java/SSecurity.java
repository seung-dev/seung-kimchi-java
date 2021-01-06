package seung.kimchi.java;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import lombok.extern.slf4j.Slf4j;
import seung.kimchi.java.utils.SAlgorithm;
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
	 * Convert to digested data.
	 * </pre>
	 * <h1>Usage</h1>
	 * <pre>
	 * byte[] digested = SConvert.digest(
	 *   "SHA-256"
	 *   , "data".getBytes()
	 *   );
	 * for(byte b : digested) {
	 *   System.out.println(b >= 0 ? String.format("%8s", Integer.toBinaryString(b)).replaceAll("\s", "0") : Integer.toBinaryString(b).substring(24));
	 * }
	 * </pre>
	 * <h1>Equal</h1>
	 * <pre>
	 * byte[] digested = SConvert.digest(
	 *   "SHA-256"
	 *   , null
	 *   , 1
	 *   , "data".getBytes()
	 *   );
	 * for(byte b : digested) {
	 *   System.out.println(b >= 0 ? String.format("%8s", Integer.toBinaryString(b)).replaceAll("\s", "0") : Integer.toBinaryString(b).substring(24));
	 * }
	 * </pre>
	 * <hr/>
	 * @param algorithm {@value SAlgorithm#_MD5}, {@value SAlgorithm#_SHA256}, ...
	 * @param data
	 * @see {@link SConvert#digest(String, String, int, byte[])}
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
	 * <pre>
	 * Convert to digested data.
	 * </pre>
	 * <h1>Usage</h1>
	 * <pre>
	 * byte[] digested = SConvert.digest(
	 *   "SHA-256"
	 *   , 1
	 *   , "data".getBytes()
	 *   );
	 * for(byte b : digested) {
	 *   System.out.println(b >= 0 ? String.format("%8s", Integer.toBinaryString(b)).replaceAll("\s", "0") : Integer.toBinaryString(b).substring(24));
	 * }
	 * </pre>
	 * <h1>Equal</h1>
	 * <pre>
	 * byte[] digested = SConvert.digest(
	 *   "SHA-256"
	 *   , null
	 *   , 1
	 *   , "data".getBytes()
	 *   );
	 * for(byte b : digested) {
	 *   System.out.println(b >= 0 ? String.format("%8s", Integer.toBinaryString(b)).replaceAll("\s", "0") : Integer.toBinaryString(b).substring(24));
	 * }
	 * </pre>
	 * <hr/>
	 * @param algorithm {@value SAlgorithm#_MD5}, {@value SAlgorithm#_SHA256}, ...
	 * @param provider {@value BouncyCastleProvider#PROVIDER_NAME}
	 * @param data
	 * @see {@link SConvert#digest(String, String, int, byte[])}
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
	 * <pre>
	 * Convert to digested data.
	 * </pre>
	 * <h1>Usage</h1>
	 * <pre>
	 * byte[] digested = null;
	 * // without provider
	 * digested = SConvert.digest(
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
	 * digested = SConvert.digest(
	 *   "SHA-256"
	 *   , BouncyCastleProvider.PROVIDER_NAME
	 *   , 1
	 *   , "data".getBytes()
	 *   );
	 * for(byte b : digested) {
	 *   System.out.println(b >= 0 ? String.format("%8s", Integer.toBinaryString(b)).replaceAll("\s", "0") : Integer.toBinaryString(b).substring(24));
	 * }
	 * </pre>
	 * <hr/>
	 * @param algorithm {@value SAlgorithm#_MD5}, {@value SAlgorithm#_SHA256}, ...
	 * @param provider {@value BouncyCastleProvider#PROVIDER_NAME}
	 * @param iteration
	 * @param data
	 * @see {@link SConvert#availableAlgorithm()}
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
		MessageDigest messageDigest;
		try {
			if(provider == null || "".equals(provider)) {
				messageDigest = MessageDigest.getInstance(algorithm);
			} else {
				messageDigest = MessageDigest.getInstance(algorithm, provider);
			}
			for(int i = 0; i < iteration; i++) {
				messageDigest.update(digest);
				digest = messageDigest.digest();
			}
		} catch (NoSuchAlgorithmException e) {
			log.error("Failed to digest data.", e);
		} catch (NoSuchProviderException e) {
			log.error("Failed to digest data.", e);
		}
		return digest;
	}
	
	@SuppressWarnings("unchecked")
	public static SLinkedHashMap availableAlgorithms() {
		SLinkedHashMap algorithms = new SLinkedHashMap();
		for(String serviceName : availableServiceName()) {
			algorithms.put(serviceName, getAlgorithms(serviceName));
		}
		return algorithms;
	}
	
	public static List<String> getAlgorithms(String serviceName) {
		return new ArrayList<>(Security.getAlgorithms(serviceName));
	}
	
	public static List<String> availableServiceName() {
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
	
	public static List<SLinkedHashMap> availableProviders() {
		List<SLinkedHashMap> providers = new ArrayList<>();
		for(Provider provider : Security.getProviders()) {
			List<SLinkedHashMap> service = new ArrayList<>();
			provider.getServices().stream().forEach(s -> {
				service.add(new SLinkedHashMap()
						.add("type", s.getType())
						.add("className", s.getClassName())
						.add("algorithm", s.getAlgorithm())
						);
			});
			providers.add(new SLinkedHashMap()
					.add("name", provider.getName())
					.add("version", provider.getVersion())
					.add("info", provider.getInfo())
					.add("service", service)
					);
		}
		return providers;
	}
	
	public static int addBouncyCastleProvider() {
		if(Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}
		return 1;
	}
	
}
