package com.chinesedreamer.zentaomonitor.service;

import com.chinesedreamer.zentaomonitor.entity.DingtalkMessage;

public interface DingtalkSenderService {

	public void sendMessage(DingtalkMessage message);
}
