package com.chinesedreamer.zentaomonitor.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class TaskVo {
	private Long id;
	private Boolean isSub;
	private String name;
	private String priorityName;
	private BigDecimal left;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date deadline;
	private String statusName;
	private String assignTo;
}
