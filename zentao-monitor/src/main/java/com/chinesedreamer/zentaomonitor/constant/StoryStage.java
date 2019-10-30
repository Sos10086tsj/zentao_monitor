package com.chinesedreamer.zentaomonitor.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum StoryStage {
	NOT_START("wait", "未开始"),
	PLANNED("planned", "已计划"),
	APPROVAL("projected","已立项"),
	DEVELOPING("developing", "研发中"),
	DEV_COMPLETE("developed", "研发完毕"),
	QA_TESTING("testing", "测试中"),
	TESTING_COMPLETE("tested", "测试完毕"),
	ACCEPTED("verified", "已验收"),
	RELEASED("released", "已发布"),
	CLOSED("closed", "已关闭");
	
	@EnumValue
	private final String stage;
	private final String desc;
	
	private StoryStage(String stage, String desc) {
		this.stage = stage;
		this.desc = desc;
	}

	public String getStage() {
		return stage;
	}

	public String getDesc() {
		return desc;
	}
	
}
