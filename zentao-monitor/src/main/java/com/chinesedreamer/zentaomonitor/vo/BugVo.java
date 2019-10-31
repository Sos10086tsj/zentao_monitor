package com.chinesedreamer.zentaomonitor.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BugVo extends BaseZtVo{

	private Long id;
	private String title;
	private Integer priority;
	private String status;
	private String assignedTo;
}
