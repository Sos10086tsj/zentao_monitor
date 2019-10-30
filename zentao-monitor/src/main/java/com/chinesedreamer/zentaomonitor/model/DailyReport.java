package com.chinesedreamer.zentaomonitor.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_ext_daily_report")
public class DailyReport extends BaseModel{
	
	@TableField("report_date")
	private Date reportDate;
	
	@TableField("report_title")
	private String reportTitle;
	
	@TableField("story_base_id")
	private Integer storyBaseId;
	
	@TableField
	private String stories;
	
	@TableField("task_base_id")
	private Integer taskBaseId;
	
	@TableField
	private String tasks;
	
	@TableField("bug_base_id")
	private Integer bugBaseId;
	
	@TableField
	private String bugs;
}
