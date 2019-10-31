package com.chinesedreamer.zentaomonitor.service;

import com.chinesedreamer.zentaomonitor.vo.DailyReportVo;

public interface ReportService {
	public DailyReportVo getDailyReport(Long id);
	
	public void generateDailyReport();
}
