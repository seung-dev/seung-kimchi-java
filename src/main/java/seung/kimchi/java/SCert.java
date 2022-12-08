package seung.kimchi.java;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DLTaggedObject;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.cms.CMSAttributes;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.CertificatePolicies;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.PolicyInformation;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSAttributeTableGenerator;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.DefaultAuthenticatedAttributeTableGenerator;
import org.bouncycastle.cms.SignerInfoGenerator;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;

import lombok.extern.slf4j.Slf4j;
import seung.kimchi.java.utils.SSignPriKey;
import seung.kimchi.java.utils.SSignCertDer;

@Slf4j
public class SCert {

	public final static String _INPUT_TYPE_HEX = "Hex";
	public final static String _INPUT_TYPE_BASE64 = "Base64";
	public final static String _INPUT_TYPE_PATH = "Path";
	
	public SCert() {
	}
	
	public static SSignCertDer s_sign_cert_der(
			String path
			) throws IOException {
		return s_sign_cert_der(new File(path));
	}
	public static SSignCertDer s_sign_cert_der(
			File file
			) throws IOException {
		return s_sign_cert_der(FileUtils.readFileToByteArray(file));
	}
	public static SSignCertDer s_sign_cert_der(
			byte[] encoded
			) {
		
		String type = "";
		int version = 0;
		String serial_number = "";
		String signiture_algorithm_oid = "";
		String signiture_algorithm_name = "";
		String issuer_dn = "";
		String subject_dn = "";
		long not_before_epoch = 0;
		String not_before_local = "";
		long not_after_epoch = 0;
		String not_after_local = "";
		List<String> key_usage = new ArrayList<>();
		String certificate_policy_oid = "";
		String crl_distribution_point = "";
		String subject_alternative_name_oid = "";
		String vid_oid = "";
		String vid_hash_algorithm_oid = "";
		String vid = "";
		
		while(true) {
			
			X509Certificate x509_certificate = x509_certificate(encoded);
			if(x509_certificate == null) {
				break;
			}
			
			type = x509_certificate.getType();
			version = x509_certificate.getVersion();
			serial_number = x509_certificate.getSerialNumber().toString();
			signiture_algorithm_oid = x509_certificate.getSigAlgOID();
			signiture_algorithm_name = x509_certificate.getSigAlgName();
			issuer_dn = x509_certificate.getIssuerDN().getName();
			subject_dn = x509_certificate.getSubjectDN().getName();
			Date not_before = x509_certificate.getNotBefore();
			not_before_epoch = not_before.getTime();
			not_before_local = to_text(not_before, "yyyy-MM-dd HH:mm:ss");
			Date not_after = x509_certificate.getNotAfter();
			not_after_epoch = not_after.getTime();
			not_after_local = to_text(not_after, "yyyy-MM-dd HH:mm:ss");
			key_usage.addAll(key_usage(x509_certificate));
			certificate_policy_oid = certificate_policy_oid(x509_certificate);
			crl_distribution_point = crl_distribution_point(x509_certificate);
			subject_alternative_name_oid = subject_alternative_name_oid(x509_certificate);
			if(!"".equals(subject_alternative_name_oid)) {
				vid_oid = vid_oid(x509_certificate);
				vid_hash_algorithm_oid = vid_hash_algorithm_oid(x509_certificate);
				vid = vid(x509_certificate);
			}
			
			break;
		}// end of while
		
		return SSignCertDer.builder()
				.type(type)
				.version(version)
				.serial_number(serial_number)
				.signiture_algorithm_oid(signiture_algorithm_oid)
				.signiture_algorithm_name(signiture_algorithm_name)
				.issuer_dn(issuer_dn)
				.subject_dn(subject_dn)
				.not_before_epoch(not_before_epoch)
				.not_before_local(not_before_local)
				.not_after_epoch(not_after_epoch)
				.not_after_local(not_after_local)
				.key_usage(key_usage)
				.certificate_policy_oid(certificate_policy_oid)
				.crl_distribution_point(crl_distribution_point)
				.subject_alternative_name_oid(subject_alternative_name_oid)
				.vid_oid(vid_oid)
				.vid_hash_algorithm_oid(vid_hash_algorithm_oid)
				.vid(vid)
				.build()
				;
	}
	
	public static String to_text(Date date, String pattern) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
	}
	
	public static X509Certificate x509_certificate(
			byte[] encoded
			) {
		X509Certificate x509_certificate = null;
		try(
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(encoded);
				) {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			x509_certificate = (X509Certificate) certificateFactory.generateCertificate(byteArrayInputStream);
		} catch (Exception e) {
			log.error("Failed to convert to X509Certificate.", e);
		}
		return x509_certificate;
	}
	
	public static List<String> key_usage(X509Certificate x509_certificate) {
		
		List<String> key_usage = new ArrayList<>();
		
		ASN1OctetString asn1OctetString = ASN1OctetString.getInstance(x509_certificate.getExtensionValue(Extension.keyUsage.getId()));
		
		if(asn1OctetString == null) {
			return key_usage;
		}
		
		KeyUsage keyUsage = KeyUsage.getInstance(asn1OctetString.getOctets());
		
		if(keyUsage.hasUsages(KeyUsage.digitalSignature)) {
			key_usage.add("digitalSignature");
		} else if(keyUsage.hasUsages(KeyUsage.nonRepudiation)) {
			key_usage.add("nonRepudiation");
		} else if(keyUsage.hasUsages(KeyUsage.keyEncipherment)) {
			key_usage.add("keyEncipherment");
		} else if(keyUsage.hasUsages(KeyUsage.dataEncipherment)) {
			key_usage.add("dataEncipherment");
		} else if(keyUsage.hasUsages(KeyUsage.keyAgreement)) {
			key_usage.add("keyAgreement");
		} else if(keyUsage.hasUsages(KeyUsage.keyCertSign)) {
			key_usage.add("keyCertSign");
		} else if(keyUsage.hasUsages(KeyUsage.cRLSign)) {
			key_usage.add("cRLSign");
		} else if(keyUsage.hasUsages(KeyUsage.encipherOnly)) {
			key_usage.add("encipherOnly");
		} else if(keyUsage.hasUsages(KeyUsage.decipherOnly)) {
			key_usage.add("decipherOnly");
		}
		
		return key_usage;
	}
	
	public static String certificate_policy_oid(X509Certificate x509_certificate) {
		
		String certificate_policy = "";
		
		ASN1OctetString asn1OctetString = ASN1OctetString.getInstance(x509_certificate.getExtensionValue(Extension.certificatePolicies.getId()));
		
		if(asn1OctetString == null) {
			return certificate_policy;
		}
		
		CertificatePolicies certificatePolicies = CertificatePolicies.getInstance(asn1OctetString.getOctets());
		for(PolicyInformation policyInformation : certificatePolicies.getPolicyInformation()) {
			if(policyInformation == null) {
				continue;
			}
			certificate_policy = policyInformation.getPolicyIdentifier().getId();
			break;
		}
		
		return certificate_policy;
	}
	
	public static String crl_distribution_point(X509Certificate x509_certificate) {
		
		String crl_distribution_point = "";
		
		ASN1OctetString asn1OctetString = ASN1OctetString.getInstance(x509_certificate.getExtensionValue(Extension.cRLDistributionPoints.getId()));
		
		if(asn1OctetString == null) {
			return crl_distribution_point;
		}
		
		CRLDistPoint crlDistPoint = CRLDistPoint.getInstance(asn1OctetString.getOctets());
		
		for(DistributionPoint distributionPoint : crlDistPoint.getDistributionPoints()) {
			
			DistributionPointName distributionPointName = distributionPoint.getDistributionPoint();
			
			if(distributionPointName == null) {
				continue;
			}
			if(DistributionPointName.FULL_NAME != distributionPointName.getType()) {
				continue;
			}
			
			for(GeneralName general_name : GeneralNames.getInstance(distributionPointName.getName()).getNames()) {
				if(GeneralName.uniformResourceIdentifier != general_name.getTagNo()) {
					continue;
				}
				crl_distribution_point = DERIA5String.getInstance(general_name.getName()).getString();
				break;
			}
			
			if(!"".equals(crl_distribution_point)) {
				break;
			}
		}
		
		return crl_distribution_point;
	}
	
	public static String subject_alternative_name_oid(X509Certificate x509_certificate) {
		
		String subject_alternative_name_oid = "";
		
		ASN1OctetString asn1OctetString = ASN1OctetString.getInstance(x509_certificate.getExtensionValue(Extension.subjectAlternativeName.getId()));
		
		if(asn1OctetString == null) {
			return subject_alternative_name_oid;
		}
		
		GeneralNames generalNames = GeneralNames.getInstance(asn1OctetString.getOctets());
		
		String general_name_text = "";
		String[] general_name_split = null;
		for(GeneralName generalName : generalNames.getNames()) {
			
			if(generalName == null) {
				continue;
			}
			if(GeneralName.otherName != generalName.getTagNo()) {
				continue;
			}
			
			// 0: [1.2.410.200004.10.1.1, [0][박종승, [[1.2.410.200004.10.1.1.1, [[2.16.840.1.101.3.4.2.1], [0]#ab0525126c906e01bcdebd2ac2ae5196f4635575fa7c5eebfd395e073a7cd0fc]]]]]
			general_name_text = generalName.toString();
			if(general_name_text == null || !general_name_text.contains("[") || !general_name_text.contains("#")) {
				continue;
			}
			
			general_name_split = general_name_text.split("\\[");
			if(general_name_split.length != 9) {
				continue;
			}
			
			subject_alternative_name_oid = general_name_split[1].split(",")[0];
			break;
		}
		
		return subject_alternative_name_oid;
	}
	
	public static String vid_oid(X509Certificate x509_certificate) {
		
		String vid_oid = "";
		
		ASN1OctetString asn1OctetString = ASN1OctetString.getInstance(x509_certificate.getExtensionValue(Extension.subjectAlternativeName.getId()));
		
		GeneralNames generalNames = GeneralNames.getInstance(asn1OctetString.getOctets());
		
		String general_name_text = "";
		String[] general_name_split = null;
		for(GeneralName generalName : generalNames.getNames()) {
			
			if(generalName == null) {
				continue;
			}
			if(GeneralName.otherName != generalName.getTagNo()) {
				continue;
			}
			
			general_name_text = generalName.toString();
			if(general_name_text == null || !general_name_text.contains("[") || !general_name_text.contains("#")) {
				continue;
			}
			
			general_name_split = general_name_text.split("\\[");
			if(general_name_split.length != 9) {
				continue;
			}
			
			vid_oid = general_name_split[5].split(",")[0];
			break;
		}
		
		return vid_oid;
	}
	
	public static String vid_hash_algorithm_oid(X509Certificate x509_certificate) {
		
		String vid_hash_algorithm_oid = "";
		
		ASN1OctetString asn1OctetString = ASN1OctetString.getInstance(x509_certificate.getExtensionValue(Extension.subjectAlternativeName.getId()));
		
		GeneralNames generalNames = GeneralNames.getInstance(asn1OctetString.getOctets());
		
		String general_name_text = "";
		String[] general_name_split = null;
		for(GeneralName generalName : generalNames.getNames()) {
			
			if(generalName == null) {
				continue;
			}
			if(GeneralName.otherName != generalName.getTagNo()) {
				continue;
			}
			
			general_name_text = generalName.toString();
			if(general_name_text == null || !general_name_text.contains("[") || !general_name_text.contains("#")) {
				continue;
			}
			
			general_name_split = general_name_text.split("\\[");
			if(general_name_split.length != 9) {
				continue;
			}
			
			vid_hash_algorithm_oid = general_name_split[7].split("\\]")[0];
			break;
		}
		
		return vid_hash_algorithm_oid;
	}
	
	public static String vid(X509Certificate x509_certificate) {
		
		String vid = "";
		
		ASN1OctetString asn1OctetString = ASN1OctetString.getInstance(x509_certificate.getExtensionValue(Extension.subjectAlternativeName.getId()));
		
		GeneralNames generalNames = GeneralNames.getInstance(asn1OctetString.getOctets());
		
		String general_name_text = "";
		String[] general_name_split = null;
		for(GeneralName generalName : generalNames.getNames()) {
			
			if(generalName == null) {
				continue;
			}
			if(GeneralName.otherName != generalName.getTagNo()) {
				continue;
			}
			
			general_name_text = generalName.toString();
			if(general_name_text == null || !general_name_text.contains("[") || !general_name_text.contains("#")) {
				continue;
			}
			
			general_name_split = general_name_text.split("\\[");
			if(general_name_split.length != 9) {
				continue;
			}
			
			vid = general_name_text.split("#")[1].split("\\]")[0];
			break;
		}
		
		return vid;
	}
	
	public static SSignPriKey s_sign_pri_key(String path) throws IOException {
		return s_sign_pri_key(new File(path));
	}
	public static SSignPriKey s_sign_pri_key(File file) throws IOException {
		return s_sign_pri_key(FileUtils.readFileToByteArray(file));
	}
	public static SSignPriKey s_sign_pri_key(byte[] encoded) {
		
		String private_key_algorythm_oid = "";
		String encryption_algorithm_oid = "";
		byte[] salt = null;
		int iteration_count = 0;
		int key_length = 0;
		String prf_algorithm_oid = "";
		byte[] iv = null;
		byte[] private_key = null;
		
		while(true) {
			
			ASN1Sequence seq = ASN1Sequence.getInstance(encoded);
			
			ASN1Sequence seq_0 = ASN1Sequence.getInstance(seq.getObjectAt(0));
			ASN1OctetString seq_1 = ASN1OctetString.getInstance(seq.getObjectAt(1));
			private_key = seq_1.getOctets();
			
			ASN1ObjectIdentifier seq_0_0 = ASN1ObjectIdentifier.getInstance(seq_0.getObjectAt(0));
			
			private_key_algorythm_oid = seq_0_0.getId();
			
			// pkcs5PBES2
			// https://www.rfc-editor.org/rfc/rfc8018
			if("1.2.840.113549.1.5.13".equals(private_key_algorythm_oid)) {
				
				ASN1Sequence seq_0_1 = ASN1Sequence.getInstance(seq_0.getObjectAt(1));
				
				ASN1Sequence seq_0_1_0 = ASN1Sequence.getInstance(seq_0_1.getObjectAt(0));
				ASN1Sequence seq_0_1_1 = ASN1Sequence.getInstance(seq_0_1.getObjectAt(1));
				
				ASN1ObjectIdentifier seq_0_1_0_0 = ASN1ObjectIdentifier.getInstance(seq_0_1_0.getObjectAt(0));
				encryption_algorithm_oid = seq_0_1_0_0.getId();
				ASN1Sequence seq_0_1_0_1 = ASN1Sequence.getInstance(seq_0_1_0.getObjectAt(1));
				
				ASN1OctetString seq_0_1_0_1_0 = ASN1OctetString.getInstance(seq_0_1_0_1.getObjectAt(0));
				salt = seq_0_1_0_1_0.getOctets();
				ASN1Integer seq_0_1_0_1_1 = ASN1Integer.getInstance(seq_0_1_0_1.getObjectAt(1));
				iteration_count = seq_0_1_0_1_1.intValueExact();
				if(seq_0_1_0_1.size() > 2) {
					ASN1Integer seq_0_1_0_0_1_2 = ASN1Integer.getInstance(seq_0_1_0_1.getObjectAt(2));
					key_length = seq_0_1_0_0_1_2.intValueExact();
				}
				
				ASN1ObjectIdentifier seq_0_1_1_0 = ASN1ObjectIdentifier.getInstance(seq_0_1_1.getObjectAt(0));
				prf_algorithm_oid = seq_0_1_1_0.getId();
				ASN1OctetString seq_0_1_1_1 = ASN1OctetString.getInstance(seq_0_1_1.getObjectAt(1));
				iv = seq_0_1_1_1.getOctets();
				
				break;
			}
			
			// seedCBCWithSHA1
			// https://www.rfc-editor.org/rfc/rfc4269
			if("1.2.410.200004.1.15".equals(private_key_algorythm_oid)) {
				
				ASN1Sequence seq_0_1 = ASN1Sequence.getInstance(seq_0.getObjectAt(1));
				
				ASN1OctetString seq_0_1_0 = ASN1OctetString.getInstance(seq_0_1.getObjectAt(0));
				salt = seq_0_1_0.getOctets();
				ASN1Integer seq_0_1_1 = ASN1Integer.getInstance(seq_0_1.getObjectAt(1));
				iteration_count = seq_0_1_1.intValueExact();
				
				break;
			}
			
			break;
		}// end of while
		
		return SSignPriKey.builder()
				.private_key_algorythm_oid(private_key_algorythm_oid)
				.encryption_algorithm_oid(encryption_algorithm_oid)
				.salt(salt)
				.iteration_count(iteration_count)
				.key_length(key_length)
				.prf_algorithm_oid(prf_algorithm_oid)
				.iv(iv)
				.private_key(private_key)
				.build()
				;
	}
	
	public static byte[] decrypt_private_key(SSignPriKey s_sign_pri_key, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		byte[] decrypted = null;
		
		SecretKeySpec secretKeySpec = null;
		IvParameterSpec ivParameterSpec = null;
		while(true) {
			
			// pkcs5PBES2
			// https://www.rfc-editor.org/rfc/rfc8018
			if("1.2.840.113549.1.5.13".equals(s_sign_pri_key.getPrivate_key_algorythm_oid())) {
				
				PBEParametersGenerator pbeParametersGenerator = new PKCS5S2ParametersGenerator();
				pbeParametersGenerator.init(
						PBEParametersGenerator.PKCS5PasswordToBytes(password.toCharArray())//password
						, s_sign_pri_key.getSalt()//salt
						, s_sign_pri_key.getIteration_count()//iterationCount
						);
				
				int key_size = s_sign_pri_key.getKey_length();
				if(key_size == 0) {
					key_size = 256;
				}
				KeyParameter keyParameter = (KeyParameter) pbeParametersGenerator.generateDerivedParameters(
						key_size//keySize
						);
				
				secretKeySpec = new SecretKeySpec(keyParameter.getKey(), "SEED");
				ivParameterSpec = new IvParameterSpec(s_sign_pri_key.getIv());
				
				break;
			}// end of 1.2.840.113549.1.5.13
			
			// seedCBCWithSHA1
			// https://www.rfc-editor.org/rfc/rfc4269
			if("1.2.410.200004.1.15".equals(s_sign_pri_key.getPrivate_key_algorythm_oid())) {
				
				MessageDigest message_digest_0 = MessageDigest.getInstance("SHA-1");
				message_digest_0.update(password.getBytes("UTF-8"));
				message_digest_0.update(s_sign_pri_key.getSalt());
				
				byte[] digested_0 = message_digest_0.digest();
				for(int i = 1; i < s_sign_pri_key.getIteration_count(); i++) {
					digested_0 = message_digest_0.digest(digested_0);
				}
				
				byte[] key = new byte[16];
				System.arraycopy(digested_0, 0, key, 0, 16);
				
				secretKeySpec = new SecretKeySpec(key, "SEED");
				
				byte[] iv = null;
				if("1.2.410.200004.1.4".equals(s_sign_pri_key.getPrf_algorithm_oid())) {
					iv = "0123456789012345".getBytes("UTF-8");
					ivParameterSpec = new IvParameterSpec(iv);
					break;
				}
				
				byte[] digested_1 = new byte[4];
				System.arraycopy(digested_0, 16, digested_1, 0, 4);
				
				MessageDigest message_digest_1 = MessageDigest.getInstance("SHA-1");
				message_digest_1.reset();
				message_digest_1.update(digested_1);
				
				byte[] digested_2 = message_digest_1.digest();
				
				iv = new byte[16];
				System.arraycopy(digested_2, 0, iv, 0, 16);
				
				ivParameterSpec = new IvParameterSpec(iv);
				
				break;
			}// end of 1.2.410.200004.1.15
			
			break;
		}// end of while
		
		Cipher cipher = Cipher.getInstance("SEED/CBC/PKCS5Padding", BouncyCastleProvider.PROVIDER_NAME);
		cipher.init(
				Cipher.DECRYPT_MODE//opmode
				, secretKeySpec//key
				, ivParameterSpec//params
				);
		decrypted = cipher.doFinal(s_sign_pri_key.getPrivate_key());
		
		return decrypted;
	}
	
	public static byte[] random_number(SSignPriKey s_sign_pri_key, String password) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
		return random_number(decrypt_private_key(s_sign_pri_key, password));
	}
	public static byte[] random_number(byte[] encoded) throws IOException {
		
		byte[] random_number = null;
		
		ASN1TaggedObject asn1TaggedObject = null;
		ASN1Sequence asn1Sequence = null;
		for(ASN1Encodable asn1Encodable : ASN1Sequence.getInstance(encoded)) {
			
			if(!(asn1Encodable instanceof DLTaggedObject)) {
				continue;
			}
			
			asn1TaggedObject = ASN1TaggedObject.getInstance(asn1Encodable);
			if(asn1TaggedObject.getTagNo() != 0) {
				continue;
			}
			
			asn1Sequence = ASN1Sequence.getInstance(asn1TaggedObject.getObject());
			for(int i = 0; i < asn1Sequence.size(); i++) {
				
				ASN1Encodable asn1Encodable1 = asn1Sequence.getObjectAt(i);
				if(!(asn1Encodable1 instanceof ASN1ObjectIdentifier)) {
					continue;
				}
				
				if(!"1.2.410.200004.10.1.1.3".equals(ASN1ObjectIdentifier.getInstance(asn1Encodable1).getId())) {
					continue;
				}
				
				ASN1Set asn1Set = ASN1Set.getInstance(asn1Sequence.getObjectAt(i + 1));
				DERBitString derBitString = DERBitString.getInstance(asn1Set.getObjectAt(0));
				random_number = derBitString.getOctets();
				
				break;
			}
			
			if(random_number != null) {
				break;
			}
			
		}
		
		return random_number;
	}
	
	public static byte[] generate_vid(String rrn, byte[] random_number, String algorithm) throws IOException {
		
		DERSequence derSequence = new DERSequence(new ASN1Encodable[] {
				new DERPrintableString(rrn)
				, new DERBitString(random_number)
		});
		
		byte[] digested = SSecurity.digest(algorithm, derSequence.getEncoded());
		return SSecurity.digest(algorithm, digested);
	}
	
	public static int verify_vid(
			String sign_cert_der
			, String sign_pri_key
			, String password
			, String rrn
			) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return verify_vid(
				new File(sign_cert_der)//sign_cert_der
				, new File(sign_pri_key)//sign_pri_key
				, password
				, rrn
				);
	}
	public static int verify_vid(
			File sign_cert_der
			, File sign_pri_key
			, String password
			, String rrn
			) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return verify_vid(
				FileUtils.readFileToByteArray(sign_cert_der)//sign_cert_der
				, FileUtils.readFileToByteArray(sign_pri_key)//sign_pri_key
				, password
				, rrn
				);
	}
	public static int verify_vid(
			byte[] sign_cert_der
			, byte[] sign_pri_key
			, String password
			, String rrn
			) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		int verify_vid = 0;
		
		SSignCertDer s_sign_cert_der = s_sign_cert_der(sign_cert_der);
		SSignPriKey s_sign_pri_key = s_sign_pri_key(sign_pri_key);
		byte[] random_number = random_number(s_sign_pri_key, password);
		
		String sign_cert_der_vid = s_sign_cert_der.getVid();
		String generate_vid = Hex.encodeHexString(
				generate_vid(
						rrn
						, random_number
						, s_sign_cert_der.getVid_hash_algorithm_oid()//algorithm
						)
				, true
				);
		
		if(sign_cert_der_vid.equals(generate_vid)) {
			verify_vid = 1;
		}
		
		return verify_vid;
	}
	
	public static byte[] sign(
			String sign_cert_der
			, String sign_pri_key
			, String password
			, byte[] message
			) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, OperatorCreationException, CertificateEncodingException, CMSException, IOException {
		return sign(
				new File(sign_cert_der)//sign_cert_der
				, new File(sign_pri_key)//sign_pri_key
				, password
				, message
				);
	}
	public static byte[] sign(
			File sign_cert_der
			, File sign_pri_key
			, String password
			, byte[] message
			) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, OperatorCreationException, CertificateEncodingException, CMSException, IOException {
		return sign(
				FileUtils.readFileToByteArray(sign_cert_der)//sign_cert_der
				, FileUtils.readFileToByteArray(sign_pri_key)//sign_pri_key
				, password
				, message
				);
	}
	public static byte[] sign(
			byte[] sign_cert_der
			, byte[] sign_pri_key
			, String password
			, byte[] message
			) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, OperatorCreationException, CertificateEncodingException, CMSException, IOException {
		
		X509Certificate x509_certificate = x509_certificate(sign_cert_der);
		SSignCertDer s_sign_cert_der = s_sign_cert_der(sign_cert_der);
		
		SSignPriKey s_sign_pri_key = s_sign_pri_key(sign_pri_key);
		byte[] decrypted = decrypt_private_key(s_sign_pri_key, password);
		
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decrypted));
		
		ContentSigner contentSigner = new JcaContentSignerBuilder(s_sign_cert_der.getSigniture_algorithm_name())
				.setProvider(BouncyCastleProvider.PROVIDER_NAME)
				.build(privateKey);
		
		JcaSignerInfoGeneratorBuilder jcaSignerInfoGeneratorBuilder = new JcaSignerInfoGeneratorBuilder(
				new JcaDigestCalculatorProviderBuilder().setProvider(BouncyCastleProvider.PROVIDER_NAME).build()
				);
		SignerInfoGenerator infoGen = jcaSignerInfoGeneratorBuilder.build(contentSigner, x509_certificate);
		final CMSAttributeTableGenerator cmsAttributeTableGenerator = infoGen.getSignedAttributeTableGenerator();
		infoGen = new SignerInfoGenerator(
				infoGen
				, new DefaultAuthenticatedAttributeTableGenerator() {
					@SuppressWarnings("rawtypes")
					@Override
					public AttributeTable getAttributes(Map parameters) {
//							return super.getAttributes(parameters);
						AttributeTable attributeTable = cmsAttributeTableGenerator.getAttributes(parameters);
						return attributeTable.remove(CMSAttributes.cmsAlgorithmProtect);
					}
				}
				, infoGen.getUnsignedAttributeTableGenerator()
				);
		
		CMSSignedDataGenerator cmsSignedDataGenerator = new CMSSignedDataGenerator();
		cmsSignedDataGenerator.addCertificate(new X509CertificateHolder(x509_certificate.getEncoded()));
		cmsSignedDataGenerator.addSignerInfoGenerator(infoGen);
		
		CMSTypedData cmsTypedData = new CMSProcessableByteArray(message);
		
		CMSSignedData cmsSignedData = cmsSignedDataGenerator.generate(cmsTypedData, true);
		
		return cmsSignedData.getEncoded("DER");
	}
	
}
