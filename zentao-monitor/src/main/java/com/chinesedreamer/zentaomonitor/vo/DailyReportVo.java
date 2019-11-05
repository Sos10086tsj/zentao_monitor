package com.chinesedreamer.zentaomonitor.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class DailyReportVo {

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date reportDate;	//报表时间
	private String reportTitle;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date buildDate;
	
	private Integer totalStoryNum;		//总需求数
	private Integer processingStoryNum;	//未完成需求数
	
	private List<StoryVo> stories;	//需求列表
	private List<TaskVo> otherUncloseTasks;	//未关联需求的任务列表
	
	private Integer totalVersionBugNum;	//版本bug总数
	private Integer totalVersionUncloseBugNum;	//版本内未关闭bug总数
	private List<BugVo> uncloseBugs; //未关闭bug清单
	
	private List<String> notifyMobiles;
}
