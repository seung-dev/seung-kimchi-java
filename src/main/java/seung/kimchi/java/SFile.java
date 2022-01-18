package seung.kimchi.java;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;

import lombok.extern.slf4j.Slf4j;
import seung.kimchi.java.utils.SLinkedHashMap;

@Slf4j
public class SFile {

	public static final String _ZIP_DATA_TYPE_PATH = "path";
	public static final String _ZIP_DATA_TYPE_BYTES = "bytes";
	public static final String _ZIP_DATA_TYPE_HEX = "hex";
	public static final String _ZIP_DATA_TYPE_BASE64 = "base64";
	
	public static int to_file(ZipInputStream zipInputStream, String path, int size) {
		
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
	
	public static int is_zip(String filePath) {
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
	
	public static void add_zip_entry(
			ZipOutputStream zipOutputStream
			, String path_file
			) throws IOException {
		
		File file = new File(path_file);
		String file_name = file.getName();
		
		add_zip_entry(zipOutputStream, file_name, FileUtils.readFileToByteArray(file));
	}
	
	public static void add_zip_entry(
			ZipOutputStream zipOutputStream
			, String file_name
			, byte[] file_data
			) throws IOException {
		
		ByteArrayInputStream byteArrayInputStream = null;
		ZipEntry zipEntry = null;
		try {
			
			byteArrayInputStream = new ByteArrayInputStream(file_data);
			zipEntry = new ZipEntry(file_name);
			zipOutputStream.putNextEntry(zipEntry);
			
			byte[] b = new byte[1024 * 4];
			int off = 0;
			int len = 0;
			while((len = byteArrayInputStream.read(b)) >= 0) {
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
				if(byteArrayInputStream != null) {
					byteArrayInputStream.close();
				}
			} catch (IOException e) {
				log.error("exception=", e);
			}
		}// end of try
		
	}
	
	public static int to_zip_file(
			String path_zip
			, List<String> path_entry_list
			) {
		
		File file = new File(path_zip);
		if(file.exists()) {
			file.delete();
		}
		
		FileOutputStream fileOutputStream = null;
		ZipOutputStream zipOutputStream = null;
		try {
			
			fileOutputStream = new FileOutputStream(path_zip);
			zipOutputStream = new ZipOutputStream(fileOutputStream);
			
			for(String path_entry : path_entry_list) {
				add_zip_entry(zipOutputStream, path_entry);
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
		
		return is_zip(path_zip);
	}
	
	public static byte[] zip(
			String data_type
			, List<SLinkedHashMap> file_list
			) {
		
		byte[] zip = null;
		
		ByteArrayOutputStream byteArrayOutputStream = null;
		ZipOutputStream zipOutputStream = null;
		try {
			
			byteArrayOutputStream = new ByteArrayOutputStream();
			zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
			
			String file_name = "";
			byte[] file_data = null;
			for(SLinkedHashMap entry_file : file_list) {
				file_name = entry_file.getString("file_name");
				switch(data_type) {
				case _ZIP_DATA_TYPE_PATH:
					file_data = FileUtils.readFileToByteArray(new File(entry_file.getString("file_path")));
					break;
				case _ZIP_DATA_TYPE_HEX:
					file_data = SConvert.decodeHex(entry_file.getString("file_hex"));
					break;
				case _ZIP_DATA_TYPE_BASE64:
					file_data = SConvert.decodeBase64(entry_file.getString("file_base64"), "UTF-8");
					break;
				case _ZIP_DATA_TYPE_BYTES:
				default:
					file_data = entry_file.getByteArray("file_data");
					break;
				}
				add_zip_entry(zipOutputStream, file_name, file_data);
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
