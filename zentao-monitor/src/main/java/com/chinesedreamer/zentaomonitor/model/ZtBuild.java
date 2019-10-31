package com.chinesedreamer.zentaomonitor.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ZtBuild extends BaseModel{

	@TableField("`name`")
	private String name;
	
	@TableField("`date`")
	private Date date;
	
	@TableField("stories")
	private String stories;
}
