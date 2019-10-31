package com.chinesedreamer.zentaomonitor.vo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StoryVo extends BaseZtVo{

	private Long id;
	private String title;
	private Integer priority;
	private String stage;
	
	private List<TaskVo> unCloseTasks;
}
