package com.chinesedreamer.zentaomonitor.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum TaskStatus {
	WAIT("wait", "未开始"),
	DOING("doing", "进行中"),
	DONE("done", "已完成"),
	PAUSE("pause", "已暂停"),
	CANCEL("cancel", "已取消"),
	CLOSED("closed", "已关闭");
	
	@EnumValue
	private final String status;
	private final String desc;
	
	private TaskStatus( String status, String desc) {
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
