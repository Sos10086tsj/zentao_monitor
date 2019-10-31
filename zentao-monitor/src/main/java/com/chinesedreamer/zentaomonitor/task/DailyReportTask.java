package com.chinesedreamer.zentaomonitor.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyReportTask {

	@Scheduled(cron = "0 45 17 ? * *")
	public void generateReport() {
		System.err.println("----------" + System.currentTimeMillis());
	}
}
