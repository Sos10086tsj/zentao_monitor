package com.chinesedreamer.zentaomonitor.vo;

import java.util.List;

import lombok.Data;

@Data
public class StoryVo {

	private Long id;
	private String title;
	private Integer priority;
	private String stage;
	
	private List<TaskVo> tasks;
}
