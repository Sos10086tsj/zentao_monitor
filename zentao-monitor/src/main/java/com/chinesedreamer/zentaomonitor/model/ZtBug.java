package com.chinesedreamer.zentaomonitor.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.chinesedreamer.zentaomonitor.constant.BugPriority;
import com.chinesedreamer.zentaomonitor.constant.BugStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ZtBug extends BaseModel{

	@TableField
	private String title;
	
	@TableField("pri")
	private BugPriority priority;
	
	@TableField("status")
	private BugStatus status;
	
	@TableField("openedBuild")
	private String buildIds;
	
	@TableField("assignedTo")
	private String assignedTo;
}
