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
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DLSequence;
import org.bouncycastle.asn1.DLSet;
import org.bouncycastle.asn1.DLTaggedObject;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.cms.CMSAttributes;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
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
import seung.kimchi.java.utils.SAlgorithm;
import seung.kimchi.java.utils.SCertificate;
import seung.kimchi.java.utils.SCharset;

@Slf4j
public class SCert {

	public final static String _INPUT_TYPE_HEX = "Hex";
	public final static String _INPUT_TYPE_BASE64 = "Base64";
	public final static String _INPUT_TYPE_PATH = "Path";
	
	public SCert() {
		// TODO Auto-generated constructor stub
	}
	
	public static int verifySign(byte[] sign) {
		
		int valid = 0;
		
//		try(
//				ASN1InputStream ans1InputStream = new ASN1InputStream(sign);
//				) {
//			
//			CMSSignedData cmsSignedData = new CMSSignedData(ContentInfo.getInstance(ans1InputStream.readObject()));
//			
//		} catch (CMSException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return valid;
	}
	
	public static byte[] sign(
			byte[] signCertDer
			, byte[] signPriKey
			, String key
			, byte[] message
			) {
		
		byte[] signed = null;
		
		try {
			
			SCertificate sCertificate = getSCertificate(signCertDer);
			PrivateKey privateKey = getPrivateKey(signPriKey, key);
			
			CMSTypedData cmsTypedData = new CMSProcessableByteArray(message);
			
			ContentSigner contentSigner = new JcaContentSignerBuilder(sCertificate.getSigAlgName())
					.setProvider(BouncyCastleProvider.PROVIDER_NAME)
					.build(privateKey);
			
			JcaSignerInfoGeneratorBuilder jcaSignerInfoGeneratorBuilder = new JcaSignerInfoGeneratorBuilder(
					new JcaDigestCalculatorProviderBuilder().setProvider(BouncyCastleProvider.PROVIDER_NAME).build()
					);
			SignerInfoGenerator infoGen = jcaSignerInfoGeneratorBuilder.build(contentSigner, sCertificate.getX509Certificate());
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
			cmsSignedDataGenerator.addCertificate(new X509CertificateHolder(sCertificate.getX509Certificate().getEncoded()));
			cmsSignedDataGenerator.addSignerInfoGenerator(infoGen);
			
			CMSSignedData cmsSignedData = cmsSignedDataGenerator.generate(cmsTypedData, true);
			signed = cmsSignedData.getEncoded("DER");
			
		} catch (OperatorCreationException e) {
			log.error("Failed to sign.");
		} catch (CertificateEncodingException e) {
			log.error("Failed to sign.");
		} catch (CMSException e) {
			log.error("Failed to sign.");
		} catch (IOException e) {
			log.error("Failed to sign.");
		}
		
		return signed;
	}
	
	public static SCertificate getSCertificate(String type, String certificate) {
		SCertificate sCertificate = null;
		try {
			switch (type) {
			case _INPUT_TYPE_HEX:
				sCertificate = getSCertificate(SConvert.decodeHex(certificate));
				break;
			case _INPUT_TYPE_BASE64:
				sCertificate = getSCertificate(SConvert.decodeBase64(certificate.getBytes(SCharset._UTF_8)));
				break;
			case _INPUT_TYPE_PATH:
				sCertificate = getSCertificate(new File(certificate));
				break;
			default:
				break;
			}
		} catch (UnsupportedEncodingException e) {
			log.error("Failed to read signCertDer.", e);
		}
		return sCertificate;
	}
	
	public static SCertificate getSCertificate(File certificate) {
		SCertificate sCertificate = null;
		try {
			sCertificate = getSCertificate(FileUtils.readFileToByteArray(certificate));
		} catch (IOException e) {
			log.error("Failed to read signCertDer.", e);
		}
		return sCertificate;
	}
	
	public static SCertificate getSCertificate(byte[] certificate) {
		
		SCertificate sCertificate = null;
		
		X509Certificate x509Certificate = getX509Certificate(certificate);
		
		if(x509Certificate != null) {
			
			PublicKey publicKey = x509Certificate.getPublicKey();
			sCertificate = SCertificate.builder()
					.x509Certificate(x509Certificate)
					.type(x509Certificate.getType())
					.version(x509Certificate.getVersion())
					.serialNumber(x509Certificate.getSerialNumber().toString())
					.serialNumberHex(SConvert.encodeHexString(x509Certificate.getSerialNumber(), true))
					.sigAlgOID(x509Certificate.getSigAlgOID())
					.sigAlgName(x509Certificate.getSigAlgName())
					.issuerDN(x509Certificate.getIssuerDN().getName())
					.subjectDN(x509Certificate.getSubjectDN().getName())
					.notBefore(x509Certificate.getNotBefore().toInstant().getEpochSecond())
					.notAfter(x509Certificate.getNotAfter().toInstant().getEpochSecond())
					.publicKey(publicKey)
					.publicKeyFormat(publicKey.getFormat())
					.publicKeyAlgorithm(publicKey.getAlgorithm())
					.crlDistPointList(getCRLDistPointList(x509Certificate))
					.build()
					;
			
		}
		
		return sCertificate;
	}
	
	public static List<String> getCRLDistPointList(X509Certificate x509Certificate) {
		
		List<String> crlDistPointList = new ArrayList<>();
		
		try(
				ASN1InputStream ans1InputStreamI = new ASN1InputStream(x509Certificate.getExtensionValue(Extension.cRLDistributionPoints.getId()));
				ASN1InputStream ans1InputStreamII = new ASN1InputStream(new ByteArrayInputStream(((DEROctetString) ans1InputStreamI.readObject()).getOctets()));
				) {
			
			CRLDistPoint distPoint = CRLDistPoint.getInstance(ans1InputStreamII.readObject());
			
			for(DistributionPoint distributionPoint : distPoint.getDistributionPoints()) {
				
				DistributionPointName distributionPointName = distributionPoint.getDistributionPoint();
				
				if(distributionPointName != null) {
					if(distributionPointName.getType() == DistributionPointName.FULL_NAME) {
						for(GeneralName generalName : GeneralNames.getInstance(distributionPointName.getName()).getNames()) {
							if(GeneralName.uniformResourceIdentifier == generalName.getTagNo()) {
								crlDistPointList.add(DERIA5String.getInstance(generalName.getName()).getString());
							}
						}
					}
				}
			}
			
		} catch (IOException e) {
			log.error("Failed to read data.", e);
		}
		
		return crlDistPointList;
	}
	
	public static X509Certificate getX509Certificate(
			byte[] certificate
			) {
		X509Certificate x509Certificate = null;
		try(
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(certificate);
				) {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			x509Certificate = (X509Certificate) certificateFactory.generateCertificate(byteArrayInputStream);
		} catch (Exception e) {
			log.error("Failed to convert to X509Certificate.", e);
		}
		return x509Certificate;
	}
	
	public static byte[] getPrivateEncryptedVID(byte[] signPriKey, String key) {
		
		byte[] encryptedVID = null;
		try(
				ASN1InputStream asn1InputStream = new ASN1InputStream(decryptPrivateKey(signPriKey, key));
				) {
			
			// seq_0
			ASN1Sequence asn1Sequence = (ASN1Sequence) asn1InputStream.readObject();
			
			Iterator<ASN1Encodable> iterator = asn1Sequence.iterator();
			ASN1Encodable asn1Encodable = null;
			DLTaggedObject dlTaggedObject = null;
			while(iterator.hasNext()) {
				
				asn1Encodable = iterator.next();
				if(asn1Encodable instanceof DLTaggedObject) {
					
					dlTaggedObject = (DLTaggedObject) asn1Encodable;
					
					if(0 == dlTaggedObject.getTagNo()) {
						
						DLSequence dlSequence = (DLSequence) dlTaggedObject.getObject();
						
						ASN1ObjectIdentifier asn1ObjectIdentifier = (ASN1ObjectIdentifier) dlSequence.getObjectAt(0);
						switch(asn1ObjectIdentifier.getId()) {
						case "1.2.410.200004.10.1.1.3":
							DLSet dlSet = (DLSet) dlSequence.getObjectAt(1);
							DERBitString derBitString = (DERBitString) dlSet.getObjectAt(0);
							encryptedVID = derBitString.getOctets();
							break;
						default:
							break;
						}
						
//						for(int index = 0; index < dlSequence.size(); index++) {
//							
//							if(dlSequence.getObjectAt(index) instanceof ASN1ObjectIdentifier) {
//								
//								ASN1ObjectIdentifier asn1ObjectIdentifier = (ASN1ObjectIdentifier) dlSequence.getObjectAt(index);
//								
//								switch(asn1ObjectIdentifier.getId()) {
//									case "1.2.410.200004.10.1.1.3":
//										DLSet dlSet = (DLSet) dlSequence.getObjectAt(index + 1);
//										DERBitString derBitString = (DERBitString) dlSet.getObjectAt(0);
//										encryptedVID = derBitString.getOctets();
//										break;
//									default:
//										break;
//								}
//								
//								break;
//							}
//							
//						}// end of dlSequence
						
					}// end of tag 0
					
					break;
				}// end of dlTaggedObject
				
			}// end of iterator seq_0
			
		} catch (IOException e) {
			log.error("Failed to read private encrypted VID.", e);
		}
		
		return encryptedVID;
	}
	
	public static SCertificate getPrivateKey(String type, String signPriKey) {
		SCertificate sCertificate = null;
		try {
			switch (type) {
			case _INPUT_TYPE_HEX:
				sCertificate = getSCertificate(SConvert.decodeHex(signPriKey));
				break;
			case _INPUT_TYPE_BASE64:
				sCertificate = getSCertificate(SConvert.decodeBase64(signPriKey.getBytes(SCharset._UTF_8)));
				break;
			case _INPUT_TYPE_PATH:
				sCertificate = getSCertificate(new File(signPriKey));
				break;
			default:
				break;
			}
		} catch (UnsupportedEncodingException e) {
			log.error("Failed to read signCertDer.", e);
		}
		return sCertificate;
	}
	
	public static PrivateKey getPrivateKey(File signPriKey, String key) {
		PrivateKey privateKey = null;
		try {
			privateKey = getPrivateKey(FileUtils.readFileToByteArray(signPriKey), key);
		} catch (IOException e) {
			log.error("Failed to read private key.", e);
		}
		return privateKey;
	}
	
	public static PrivateKey getPrivateKey(byte[] signPriKey, String key) {
		
		PrivateKey privateKey = null;
		
		try {
			byte[] decrypted = decryptPrivateKey(signPriKey, key);
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(decrypted);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		} catch (NoSuchAlgorithmException e) {
			log.error("Failed to read private key.", e);
		} catch (InvalidKeySpecException e) {
			log.error("Failed to read private key.", e);
		}
		
		return privateKey;
	}
	
	public static byte[] decryptPrivateKey(byte[] privateKey, String key) {
		
		byte[] decrypted = null;
		
		try(
				ASN1InputStream asn1InputStream = new ASN1InputStream(privateKey);
				) {
			
			// seq_0
			ASN1Sequence seq_0 = (ASN1Sequence) asn1InputStream.readObject();
			
			// seq_0_0: key derivation
			ASN1Sequence seq_0_0 = (ASN1Sequence) seq_0.getObjectAt(0);
			
			// seq_0_1: encrypted
			ASN1OctetString seq_0_1 = ASN1OctetString.getInstance(seq_0.getObjectAt(1));
			
			// seq_0_0_0: algorithm
			ASN1ObjectIdentifier seq_0_0_0 = (ASN1ObjectIdentifier) seq_0_0.getObjectAt(0);
			
			String oid = seq_0_0_0.getId();
			byte[] k = null;
			byte[] iv = null;
			switch(oid) {
			
				// RFC 8018 https://tools.ietf.org/html/rfc8018
				case "1.2.840.113549.1.5.13":
					
					// rfc8018_seq_0_0_1
					ASN1Sequence rfc8018_seq_0_0_1 = (ASN1Sequence) seq_0_0.getObjectAt(1);
					
					// rfc8018_seq_0_0_1_0
					ASN1Sequence rfc8018_seq_0_0_1_0 = (ASN1Sequence) rfc8018_seq_0_0_1.getObjectAt(0);
					
					// rfc8018_seq_0_0_1_0_0
//					ASN1ObjectIdentifier rfc8018_seq_0_0_1_0_0 = (ASN1ObjectIdentifier) rfc8018_seq_0_0_1_0.getObjectAt(0);
					
					// rfc8018_seq_0_0_1_0_1
					ASN1Sequence rfc8018_seq_0_0_1_0_1 = (ASN1Sequence) rfc8018_seq_0_0_1_0.getObjectAt(1);
					
					// rfc8018_seq_0_0_1_0_1_0: salt
					DEROctetString rfc8018_seq_0_0_1_0_1_0 = (DEROctetString) rfc8018_seq_0_0_1_0_1.getObjectAt(0);
					
					// rfc8018_seq_0_0_1_0_1_1: iterationCount
					ASN1Integer rfc8018_seq_0_0_1_0_1_1 = (ASN1Integer) rfc8018_seq_0_0_1_0_1.getObjectAt(1);
					
					// rfc8018_seq_0_0_1_1
					ASN1Sequence rfc8018_seq_0_0_1_1 = (ASN1Sequence) rfc8018_seq_0_0_1.getObjectAt(1);
					
					// rfc8018_seq_0_0_1_1_0
//					ASN1ObjectIdentifier rfc8018_seq_0_0_1_1_0 = (ASN1ObjectIdentifier) rfc8018_seq_0_0_1_1.getObjectAt(0);
					
					// rfc8018_seq_0_0_1_1_1: iv
					DEROctetString rfc8018_seq_0_0_1_1_1 = (DEROctetString) rfc8018_seq_0_0_1_1.getObjectAt(1);
					
					PBEParametersGenerator pbeParametersGenerator = new PKCS5S2ParametersGenerator();
					pbeParametersGenerator.init(
							// password
							PBEParametersGenerator.PKCS5PasswordToBytes(key.toCharArray())
							// salt
							, rfc8018_seq_0_0_1_0_1_0.getOctets()
							// iterationCount
							, rfc8018_seq_0_0_1_0_1_1.getValue().intValue()
							);
					
					int keySize = 256;
					KeyParameter keyParameter = (KeyParameter) pbeParametersGenerator.generateDerivedParameters(keySize);
					
					k = keyParameter.getKey();
					iv = rfc8018_seq_0_0_1_1_1.getOctets();
					
					break;
					
				// RFC 4010 https://tools.ietf.org/html/rfc4010
				case "1.2.410.200004.1.4":
					
					// rfc4010_seq_0_0_1
					ASN1Sequence rfc4010_seq_0_0_1 = (ASN1Sequence) seq_0_0.getObjectAt(1);
					
					// rfc4010_seq_0_0_1_0: salt
					DEROctetString rfc4010_seq_0_0_1_0 = (DEROctetString) rfc4010_seq_0_0_1.getObjectAt(0);
					
					// rfc4010_seq_0_0_1_1: iterationCount
					ASN1Integer rfc4010_seq_0_0_1_1 = (ASN1Integer) rfc4010_seq_0_0_1.getObjectAt(1);
					
					byte[] rfc4010_input = new byte[20];
					MessageDigest rfc4010MessageDigest = MessageDigest.getInstance(SAlgorithm._SHA1);
					rfc4010MessageDigest.update(key.getBytes(SCharset._UTF_8));
					rfc4010MessageDigest.update(rfc4010_seq_0_0_1_0.getOctets());
					rfc4010_input = rfc4010MessageDigest.digest();
					for(int index = 0; index < rfc4010_seq_0_0_1_1.getValue().intValue(); index++) {
						rfc4010_input = rfc4010MessageDigest.digest(rfc4010_input);
					}
					
					k = new byte[16];
					System.arraycopy(rfc4010_input, 0, k, 0, 16);
					
					iv = "012345678912345".getBytes();
					
					break;
					
				// RFC 4269 https://tools.ietf.org/html/rfc4269
				case "1.2.410.200004.1.15":
					
					// rfc4269_seq_0_0_1
					ASN1Sequence rfc4269_seq_0_0_1 = (ASN1Sequence) seq_0_0.getObjectAt(1);
					
					// rfc4269_seq_0_0_1_0: salt
					DEROctetString rfc4269_seq_0_0_1_0 = (DEROctetString) rfc4269_seq_0_0_1.getObjectAt(0);
					
					// rfc4269_seq_0_0_1_1: iterationCount
					ASN1Integer rfc4269_seq_0_0_1_1 = (ASN1Integer) rfc4269_seq_0_0_1.getObjectAt(1);
					
					byte[] rfc4269_input = new byte[20];
					MessageDigest rfc4269MessageDigestI = MessageDigest.getInstance(SAlgorithm._SHA1);
					rfc4269MessageDigestI.update(key.getBytes(SCharset._UTF_8));
					rfc4269MessageDigestI.update(rfc4269_seq_0_0_1_0.getOctets());
					rfc4269_input = rfc4269MessageDigestI.digest();
					for(int index = 1; index < rfc4269_seq_0_0_1_1.getValue().intValue(); index++) {
						rfc4269_input = rfc4269MessageDigestI.digest(rfc4269_input);
					}
					
					k = new byte[16];
					System.arraycopy(rfc4269_input, 0, k, 0, 16);
					
					byte[] rfc4269_input_0 = new byte[4];
					System.arraycopy(rfc4269_input, 16, rfc4269_input_0, 0, 4);
					
					MessageDigest rfc4269MessageDigestII = MessageDigest.getInstance(SAlgorithm._SHA1);
					rfc4269MessageDigestII.reset();
					rfc4269MessageDigestII.update(rfc4269_input_0);
					
					byte[] rfc4269_input_1 = rfc4269MessageDigestII.digest();
					
					iv = new byte[16];
					System.arraycopy(rfc4269_input_1, 0, iv, 0, 16);
					
					break;
					
				default:
					break;
			}
			
			SecretKeySpec secretKeySpec = new SecretKeySpec(k, "SEED");
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
			
			Cipher cipher = Cipher.getInstance("SEED/CBC/PKCS5Padding", BouncyCastleProvider.PROVIDER_NAME);
			cipher.init(
					// opmode
					Cipher.DECRYPT_MODE
					// key
					, secretKeySpec
					// params
					, ivParameterSpec
					);
			decrypted = cipher.doFinal(seq_0_1.getOctets());
			
		} catch (IOException e) {
			log.error("Failed to decrypt private key.", e);
		} catch (NoSuchAlgorithmException e) {
			log.error("Failed to decrypt private key.", e);
		} catch (NoSuchProviderException e) {
			log.error("Failed to decrypt private key.", e);
		} catch (NoSuchPaddingException e) {
			log.error("Failed to decrypt private key.", e);
		} catch (InvalidKeyException e) {
			log.error("Failed to decrypt private key.", e);
		} catch (InvalidAlgorithmParameterException e) {
			log.error("Failed to decrypt private key.", e);
		} catch (IllegalBlockSizeException e) {
			log.error("Failed to decrypt private key.", e);
		} catch (BadPaddingException e) {
			log.error("Failed to decrypt private key.", e);
		}
		
		return decrypted;
	}
	
}
