package com.chinesedreamer.zentaomonitor.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum BugStatus {
	ACTIVE("active", "激活"),
	RESOLVED("resolved", "已解决"),
	CLOSED("closed", "已关闭");
	
	@EnumValue
	private final String status;
	private final String desc;
	private BugStatus(String status, String desc) {
		this.status = status;
		this.desc = desc;
	}
	public String getStatus() {
		return status;
	}
	public String getDesc() {
		return desc;
	}
	
}
