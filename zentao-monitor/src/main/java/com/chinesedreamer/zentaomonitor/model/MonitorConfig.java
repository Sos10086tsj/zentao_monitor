package com.chinesedreamer.zentaomonitor.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chinesedreamer.zentaomonitor.constant.MonitorConfigType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_ext_monitor_config")
public class MonitorConfig extends BaseModel{

	@TableField("config_type")
	private MonitorConfigType configType;
	
	@TableField("config_value")
	private String configValue;
}
