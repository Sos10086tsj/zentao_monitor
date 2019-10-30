package com.chinesedreamer.zentaomonitor.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chinesedreamer.zentaomonitor.constant.StoryPriority;
import com.chinesedreamer.zentaomonitor.constant.StoryStage;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("zt_story")
public class ZtStory extends BaseModel{
	
	@TableField
	private String plan;
	
	@TableField
	private String title;
	
	@TableField("pri")
	private StoryPriority priority;
	
	@TableField("stage")
	private StoryStage stage;
}
