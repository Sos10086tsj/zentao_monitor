package com.chinesedreamer.zentaomonitor.vo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class TaskVo {
	private Long id;
	private Boolean isSub;
	private String name;
	private String priorityName;
	private BigDecimal left;
	private Date deadline;
	private String statusName;
	private String assignTo;
}
