package com.chinesedreamer.zentaomonitor.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.chinesedreamer.zentaomonitor.service.ReportService;

@Component
public class DailyReportTask {
	private Logger logger = LoggerFactory.getLogger(DailyReportTask.class);
	
	@Autowired
	private ReportService reportService;

	@Scheduled(cron = "0 30 12,18 ? * MON-FRI")
	public void generateReport() {
		this.logger.info("--------- start ----------");
		this.reportService.generateDailyReport();
		this.logger.info("--------- end ----------");
	}
}
