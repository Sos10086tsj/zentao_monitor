package com.chinesedreamer.zentaomonitor.model;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chinesedreamer.zentaomonitor.constant.TaskPriority;
import com.chinesedreamer.zentaomonitor.constant.TaskStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("zt_task")
public class ZtTask extends BaseModel{

	@TableField
	private Long parent;
	
	@TableField
	private Long story;
	
	@TableField
	private String name;
	
	@TableField("pri")
	private TaskPriority priority;
	
	@TableField("`left`")
	private BigDecimal left;
	
	@TableField
	private Date deadline;
	
	@TableField
	private TaskStatus status;
	
	@TableField("assignedTo")
	private String assignedTo;
	
	@TableField
	private String deleted;
}
