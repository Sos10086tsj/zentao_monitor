package com.chinesedreamer.zentaomonitor.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum BugStatus {
	active("active", "激活"),
	resolved("resolved", "已解决"),
	closed("closed", "已关闭");
	
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
