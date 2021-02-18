package seung.kimchi.java;

import java.io.UnsupportedEncodingException;

import kong.unirest.HttpRequest;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import seung.kimchi.java.utils.SLinkedHashMap;

@Slf4j
public class SHttp {

	public static String ip4v(String url) throws InterruptedException, UnsupportedEncodingException {
		
		String ip4v = "";
		
		HttpResponse<byte[]> httpResponse = request(
				Unirest
					.get(url)
					.connectTimeout(1000 * 3)
					.socketTimeout(1000 * 60)
				, 3
				, 1000
				);
		
		if(200 == httpResponse.getStatus() && httpResponse.getBody() != null) {
			ip4v = new String(httpResponse.getBody(), "UTF-8");
		}
		
		return ip4v;
	}
	
	public static String ip4v() throws InterruptedException {
		
		String ip4v = "";
		
		HttpResponse<byte[]> httpResponse = request(
				Unirest
				.get("https://api.ip.pe.kr/json/")
				.connectTimeout(1000 * 3)
				.socketTimeout(1000 * 60)
				, 3
				, 1000
				);
		
		if(200 == httpResponse.getStatus() && httpResponse.getBody() != null) {
			SLinkedHashMap network = new SLinkedHashMap(new String(httpResponse.getBody()));
			ip4v = network.getString("ip", "");
		}
		
		return ip4v;
	}
	
//	@SuppressWarnings("rawtypes")
//	public static HttpResponse<File> requestFile(
//			HttpRequest httpRequest
//			, int maxSize
//			, long sleep
//			, String path
//			) throws InterruptedException {
//		
//		HttpResponse<File> httpResponse = null;
//		
//		int trySize = 0;
//		while(trySize++ < maxSize) {
//			try {
//				httpResponse = httpRequest.asFile(path);
//				if(200 == httpResponse.getStatus()) {
//					break;
//				} else {
//					Thread.sleep(sleep);
//				}
//			} catch (Exception e) {
//				log.error("Failed to request.({}/{})", trySize, maxSize, e);
//				if(sleep > 0) {
//					Thread.sleep(sleep);
//				}
//			}// end of try
//		}// end of while
//		
//		return httpResponse;
//	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HttpResponse<byte[]> request(
			HttpRequest httpRequest
			, int maxSize
			, long sleep
			) throws InterruptedException {
		
		HttpResponse<byte[]> httpResponse = null;
		
		int trySize = 0;
		while(trySize++ < maxSize) {
			try {
				httpResponse = httpRequest.asBytes();
				if(200 == httpResponse.getStatus()) {
					break;
				} else {
					Thread.sleep(sleep);
				}
			} catch (Exception e) {
				log.error("Failed to request.({}/{})", trySize, maxSize, e);
				if(sleep > 0) {
					Thread.sleep(sleep);
				}
			}// end of try
		}// end of while
		
		return httpResponse;
	}
	
}
