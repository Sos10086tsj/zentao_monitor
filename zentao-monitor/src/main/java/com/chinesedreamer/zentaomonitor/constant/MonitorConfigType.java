package com.chinesedreamer.zentaomonitor.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum MonitorConfigType {

	DAILY_REPORT("DAILY_REPORT", "每日版本监控报告");
	
	@EnumValue
	private final String type;
	private final String desc;
	
	private MonitorConfigType(String type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public String getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
	
}
