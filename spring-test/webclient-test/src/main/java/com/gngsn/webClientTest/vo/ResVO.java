package com.gngsn.webClientTest.vo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
 @JsonPropertyOrder({"success", "errorCd", "msg", "data"})
// @JsonInclude(Include.NON_NULL)
public class ResVO {

	private boolean success;
	private HttpStatus status;
	private String msg;
	private Object data;

	public ResVO(boolean success, String msg) {
		super();
		this.success = success;
		this.msg = msg;
	}

	public ResVO(boolean success, HttpStatus status, String msg) {
		super();
		this.success = success;
		this.status = status;
		this.msg = msg;
	}

	public ResVO(boolean success, String msg, Object data) {
		super();
		this.success = success;
		this.msg = msg;
		this.data = data;
	}
}


