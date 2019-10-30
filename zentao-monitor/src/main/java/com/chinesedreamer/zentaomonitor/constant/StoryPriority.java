package com.chinesedreamer.zentaomonitor.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum StoryPriority {
	LEVEL_1(1),
	LEVEL_2(2),
	LEVEL_3(3),
	LEVEL_4(4);
	
	@EnumValue
	private final Integer priority;
	private StoryPriority(Integer priority) {
		this.priority = priority;
	}
	public Integer getPriority() {
		return priority;
	}
	
	
}
