package seung.kimchi.java;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import lombok.extern.slf4j.Slf4j;
import seung.kimchi.java.utils.SLinkedHashMap;

@Slf4j
public class SFile {

	public static int toFile(ZipInputStream zipInputStream, String path, int size) {
		
		int result = -1;
		try(
				FileOutputStream fileOutputStream = new FileOutputStream(path);
				BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
				) {
			byte[] b = new byte[size];
			int read;
			while((read = zipInputStream.read(b)) > 0) {
				bufferedOutputStream.write(b, 0, read);
			}
		} catch (FileNotFoundException e) {
			result = 0;
		} catch (IOException e) {
			result = 0;
		} finally {
			if(new File(path).exists()) {
				result = 1;
			}
		}
		
		return result;
	}
	
	public static void add_zip_entry(
			ZipOutputStream zipOutputStream
			, String entry_path
			) throws IOException {
		
		File file = new File(entry_path);
		String fileName = file.getName();
		
		FileInputStream fileInputStream = null;
		ZipEntry zipEntry = null;
		try {
			
			fileInputStream = new FileInputStream(file);
			zipEntry = new ZipEntry(fileName);
			zipOutputStream.putNextEntry(zipEntry);
			
			byte[] b = new byte[1024 * 4];
			int off = 0;
			int len = 0;
			while((len = fileInputStream.read(b)) >= 0) {
				zipOutputStream.write(b, off, len);
			}
			
			zipOutputStream.closeEntry();
			
		} catch (FileNotFoundException e) {
			log.error("exception=", e);
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if(fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (IOException e) {
				log.error("exception=", e);
			}
		}
		
	}
	
	public static int isZip(String filePath) {
		int isZip = 0;
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(filePath);
			isZip = zipFile.size();
		} catch (Exception e) {
			log.error("exception=", e);
			isZip = -1;
		} finally {
			try {
				if(zipFile != null) {
					zipFile.close();
				}
			} catch (IOException e) {
				log.error("exception=", e);
			}
		}
		return isZip;
	}
	
	public static int zip(
			String file_path
			, List<String> file_path_list
			) {
		
		File file = new File(file_path);
		if(file.exists()) {
			file.delete();
		}
		
		FileOutputStream fileOutputStream = null;
		ZipOutputStream zipOutputStream = null;
		try {
			
			fileOutputStream = new FileOutputStream(file_path);
			zipOutputStream = new ZipOutputStream(fileOutputStream);
			
			for(String entry_path : file_path_list) {
				add_zip_entry(zipOutputStream, entry_path);
			}
			
			zipOutputStream.flush();
			
		} catch (Exception e) {
			log.error("exception=", e);
		} finally {
			try {
				if(zipOutputStream != null) {
					zipOutputStream.close();
				}
				if(fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (IOException e) {
				log.error("exception=", e);
			}
		}
		
		return isZip(file_path);
	}
	
	public static void add_zip_entry(
			ZipOutputStream zipOutputStream
			, String entry_path
			, String entry_name
			) throws IOException {
		
		File file = new File(entry_path);
		
		FileInputStream fileInputStream = null;
		ZipEntry zipEntry = null;
		try {
			
			fileInputStream = new FileInputStream(file);
			zipEntry = new ZipEntry(entry_name);
			zipOutputStream.putNextEntry(zipEntry);
			
			byte[] b = new byte[1024 * 4];
			int off = 0;
			int len = 0;
			while((len = fileInputStream.read(b)) >= 0) {
				zipOutputStream.write(b, off, len);
			}
			
			zipOutputStream.closeEntry();
			
		} catch (FileNotFoundException e) {
			log.error("exception=", e);
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if(fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (IOException e) {
				log.error("exception=", e);
			}
		}
		
	}
	
	public static byte[] zip(
			List<SLinkedHashMap> file_list
			) {
		
		byte[] zip = null;
		
		ByteArrayOutputStream byteArrayOutputStream = null;
		ZipOutputStream zipOutputStream = null;
		try {
			
			byteArrayOutputStream = new ByteArrayOutputStream();
			zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
			
			for(SLinkedHashMap entry_file : file_list) {
				add_zip_entry(zipOutputStream, entry_file.getString("file_path"), entry_file.getString("file_name"));
			}
			
			zipOutputStream.flush();
			
		} catch (Exception e) {
			log.error("exception=", e);
		} finally {
			try {
				if(zipOutputStream != null) {
					zipOutputStream.close();
				}
				if(byteArrayOutputStream != null) {
					zip = byteArrayOutputStream.toByteArray();
					byteArrayOutputStream.close();
				}
			} catch (IOException e) {
				log.error("exception=", e);
			}
		}
		
		return zip;
	}
	
}
