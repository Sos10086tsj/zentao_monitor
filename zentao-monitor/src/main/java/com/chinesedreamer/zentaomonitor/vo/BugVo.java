package com.chinesedreamer.zentaomonitor.vo;

import lombok.Data;

@Data
public class BugVo {

	private Long id;
	private String title;
	private Integer priority;
	private String status;
	private String assignedTo;
}
