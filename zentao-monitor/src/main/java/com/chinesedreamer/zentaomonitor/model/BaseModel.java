package com.chinesedreamer.zentaomonitor.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

@Data
public abstract class BaseModel {
	@TableId(value = "id", type = IdType.AUTO)
	protected Long id;
}
