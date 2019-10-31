package com.chinesedreamer.zentaomonitor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;


@Configuration
@Data
public class AppProperties {

	@Value("${app.zentao.baseUrl}")
	private String zentaoBaseUrl;
	
	@Value("${app.zentao.sotryUrl}")
	private String zentaoSotryUrl;
	
	@Value("${app.zentao.taskUrl}")
	private String zentaoTaskUrl;
	
	@Value("${app.zentao.bugUrl}")
	private String zentaoBugUrl;
}
