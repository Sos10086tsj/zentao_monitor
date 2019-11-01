package com.chinesedreamer.zentaomonitor.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.chinesedreamer.zentaomonitor.config.AppProperties;
import com.chinesedreamer.zentaomonitor.entity.DingtalkMessage;
import com.chinesedreamer.zentaomonitor.service.DingtalkSenderService;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;

@Service
public class DingtalkSenderServiceImpl implements DingtalkSenderService{
	
	private Logger logger = LoggerFactory.getLogger(DingtalkSenderServiceImpl.class);

	@Autowired
	private AppProperties appProperties;
	@Override
	public void sendMessage(DingtalkMessage message) {
		DingTalkClient client = new DefaultDingTalkClient(this.appProperties.getDingTalkAccessToken());
		OapiRobotSendRequest request = new OapiRobotSendRequest();
		request.setMsgtype("markdown");
		OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
		markdown.setTitle(message.getTitle());
		markdown.setText(message.getText());
		
		request.setMarkdown(markdown);
		
		OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
		at.setIsAtAll("true");
		request.setAt(at);
		
		try {
			OapiRobotSendResponse response = client.execute(request);
			System.out.println(JSON.toJSONString(response));
		} catch (ApiException e) {
			this.logger.error("{}", e);
		}
	}

}
