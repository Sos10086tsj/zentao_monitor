package com.chinesedreamer.zentaomonitor.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class DailyReportVo {

	private Date reportDate;	//报表时间
	private String reportTitle;
	
	private Integer totalStoryNum;		//总需求数
	private Integer processingStoryNum;	//未完成需求数
	
	private List<StoryVo> stories;	//需求列表
	private List<TaskVo> otherTasks;	//未关联需求的任务列表
	
	private Integer totalVersionBugNum;	//版本bug总数
	private Integer totalVersionUncloseBugNum;	//版本内未关闭bug总数
	private List<BugVo> uncloseBugs; //未关闭bug清单
}
