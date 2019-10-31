package com.chinesedreamer.zentaomonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.chinesedreamer.zentaomonitor.service.ReportService;
import com.chinesedreamer.zentaomonitor.vo.ResponseVo;

@RestController
@RequestMapping("report")
public class ReportController {
	
	@Autowired
	private ReportService reportService;

	/**
	 * 	获取每日报表的数据
	 * @param id	报表id
	 * @return
	 */
	@RequestMapping(value = "daily/{id}", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ResponseVo getDailyReport(@PathVariable("id") Long id) {
		return ResponseVo.success(this.reportService.getDailyReport(id));
	}
	
	@RequestMapping(value = "daily/execute", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ResponseVo executeDailyReport() {
		this.reportService.generateDailyReport();
		return ResponseVo.success();
	}
}
