package com.chinesedreamer.zentaomonitor.entity;

import java.util.List;

import lombok.Data;

@Data
public class DingtalkMessage {

	private String url;
	private String description;
	private List<String> notifyMobiles;
}
