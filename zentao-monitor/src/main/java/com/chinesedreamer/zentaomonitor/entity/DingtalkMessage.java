package com.chinesedreamer.zentaomonitor.entity;

import java.util.List;

import lombok.Data;

@Data
public class DingtalkMessage {

	private String title;
	private String text;
	private List<String> notifyMobiles;
}
