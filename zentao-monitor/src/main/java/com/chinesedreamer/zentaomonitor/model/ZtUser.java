package com.chinesedreamer.zentaomonitor.model;

import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ZtUser extends BaseModel{

	@TableField
	private String account;
	
	@TableField("realname")
	private String realName;
	
	@TableField
	private String mobile;
}
