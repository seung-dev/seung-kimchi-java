package seung.kimchi.java;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SPdf {

	public SPdf() {
		// TODO Auto-generated constructor stub
	}
	
	public static byte[] encrypt(
			File file
			, String key
			) {
		byte[] pdf = null;
		try {
			pdf = encrypt(FileUtils.readFileToByteArray(file), key, false, false, false, true);
		} catch (IOException e) {
			log.error("Failed to encrypt pdf file.", e);
		}
		return pdf;
	}
	public static byte[] encryptPDF(
			byte[] data
			, String key
			) {
		return encrypt(data, key, false, false, false, true);
	}
	public static byte[] encrypt(
			byte[] data
			, String key
			, boolean allowPrinting
			, boolean allowExtraction
			, boolean allowModifications
			, boolean readOnly
			) {
		
		byte[] pdf = null;
		
		try(
				PDDocument pdDocument = PDDocument.load(data);
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				) {
			
			AccessPermission accessPermission = new AccessPermission();
			accessPermission.setCanExtractContent(allowExtraction);
			accessPermission.setCanModify(allowModifications);
			accessPermission.setCanPrint(allowPrinting);
			if(readOnly) {
				accessPermission.setReadOnly();
			}
			/*
			accessPermission.setCanAssembleDocument(false);
			accessPermission.setCanExtractForAccessibility(false);
			accessPermission.setCanFillInForm(false);
			accessPermission.setCanModifyAnnotations(false);
			accessPermission.setCanPrintDegraded(false);
			*/
			
			StandardProtectionPolicy policy = new StandardProtectionPolicy(
					key
					, key
					, accessPermission
					);
			policy.setEncryptionKeyLength(256);
			
			pdDocument.protect(policy);
			pdDocument.save(byteArrayOutputStream);
			
			pdf = byteArrayOutputStream.toByteArray();
			
		} catch (IOException e) {
			log.error("Failed to set key to pdf file.", e);
		}
		
		return pdf;
	}
	
}
