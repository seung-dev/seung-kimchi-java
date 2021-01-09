package seung.kimchi.java.utils;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SRequestBody {

	@NotBlank(message = "요청코드(requestCode)는 필수항목 입니다.")
	private String requestCode;
	
	private SRequest sRequest;
	
	private SLinkedHashMap data;
	
}
