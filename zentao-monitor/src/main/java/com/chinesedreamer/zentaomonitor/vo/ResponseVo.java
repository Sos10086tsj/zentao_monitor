package com.chinesedreamer.zentaomonitor.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ResponseVo {
	protected Boolean success;
	protected String errorMessage;
	protected Object data;
	
	@JsonIgnore
	public static ResponseVo success() {
		return success(null);
	}
	
	@JsonIgnore
	public static ResponseVo success(Object data) {
		ResponseVo vo = new ResponseVo();
		vo.setSuccess(true);
		vo.setData(data);
		return vo;
	}
	
	@JsonIgnore
	public static ResponseVo failure(String errorMessage) {
		ResponseVo vo = new ResponseVo();
		vo.setSuccess(false);
		vo.setErrorMessage(errorMessage);
		return vo;
	}
}
