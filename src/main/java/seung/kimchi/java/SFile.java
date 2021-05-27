package seung.kimchi.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class SFile {

	public static int zip(
			String filePath
			, List<String> entryPaths
			) {
		
		FileOutputStream fileOutputStream = null;
		ZipOutputStream zipOutputStream = null;
		FileInputStream fileInputStream = null;
		ZipEntry zipEntry = null;
		try {
			
			fileOutputStream = new FileOutputStream(filePath);
			zipOutputStream = new ZipOutputStream(fileOutputStream);
			
			byte[] b = null;
			int len = 0;
			int off = 0;
			for(String entryPath : entryPaths) {
				File file = new File(entryPath);
				fileInputStream = new FileInputStream(file);
				zipEntry = new ZipEntry(file.getName());
				zipOutputStream.putNextEntry(zipEntry);
				b = new byte[1024 * 4];
				len = 0;
				while((len = fileInputStream.read(b)) >= 0) {
					zipOutputStream.write(b, off, len);
				}
				zipOutputStream.flush();
				fileInputStream.close();
			}
			
		} catch (Exception e) {
			try {
				if(fileInputStream != null) {
					fileInputStream.close();
				}
				if(zipOutputStream != null) {
					zipOutputStream.close();
				}
				if(fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (IOException ioe) {
			}
		}
		
		return isZip(filePath);
	}
	
	public static int isZip(String filePath) {
		int isZip = 0;
		try (
				ZipFile zipFile = new ZipFile(filePath);
		) {
			isZip = zipFile.size();
		} catch (Exception e) {
			isZip = -1;
		}
		return isZip;
	}
	
}
