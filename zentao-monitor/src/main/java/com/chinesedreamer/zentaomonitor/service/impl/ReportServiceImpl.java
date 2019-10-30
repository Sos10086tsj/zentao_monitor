package com.chinesedreamer.zentaomonitor.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chinesedreamer.zentaomonitor.comparator.TaskVoComparator;
import com.chinesedreamer.zentaomonitor.dao.DailyReportMapper;
import com.chinesedreamer.zentaomonitor.dao.ZtStoryMapper;
import com.chinesedreamer.zentaomonitor.dao.ZtTaskMapper;
import com.chinesedreamer.zentaomonitor.model.DailyReport;
import com.chinesedreamer.zentaomonitor.model.ZtStory;
import com.chinesedreamer.zentaomonitor.model.ZtTask;
import com.chinesedreamer.zentaomonitor.service.ReportService;
import com.chinesedreamer.zentaomonitor.vo.DailyReportVo;
import com.chinesedreamer.zentaomonitor.vo.StoryVo;
import com.chinesedreamer.zentaomonitor.vo.TaskVo;

@Service
public class ReportServiceImpl implements ReportService{
	
	@Autowired
	private DailyReportMapper dailyReportMapper;
	@Autowired
	private ZtStoryMapper ztStoryMapper;
	@Autowired
	private ZtTaskMapper ztTaskMapper;
	
	@Override
	public DailyReportVo getReport(Long id) {
		DailyReport dailyReport = this.dailyReportMapper.selectById(id);
		
		DailyReportVo vo = new DailyReportVo();
		vo.setReportDate(dailyReport.getReportDate());
		vo.setReportTitle(dailyReport.getReportTitle());
		
		if (StringUtils.isEmpty(dailyReport.getStories())) {
			vo.setTotalStoryNum(0);
			vo.setProcessingStoryNum(0);
		}else {
			
			//处理story相关信息
			String[] storyIdStrs = dailyReport.getStories().split(",");
			List<StoryVo> storyVos = new ArrayList<StoryVo>();
			for (String storyIdStr : storyIdStrs) {
				// 获取story信息
				Long storyId = Long.parseLong(storyIdStr) + dailyReport.getStoryBaseId().longValue();
				ZtStory ztStory = this.ztStoryMapper.selectById(storyId);
				StoryVo storyVo = this.convertStory2Vo(ztStory);
				// 获取story关联的任务信息
				List<ZtTask> ztTasks = this.getByStoryId(storyId);
				List<TaskVo> taskVos = ztTasks.stream().map(t -> { return convertTask2Vo(t); }).collect(Collectors.toList());
				taskVos.sort(new TaskVoComparator());
				storyVo.setTasks(taskVos);
				storyVos.add(storyVo);
			}
			vo.setStories(storyVos);
		}
		
		System.out.println(JSON.toJSONString(vo));
		return null;
	}

	private StoryVo convertStory2Vo(ZtStory ztStory) {
		StoryVo vo = new StoryVo();
		vo.setId(ztStory.getId());
		vo.setTitle(ztStory.getTitle());
		vo.setPriority(ztStory.getPriority().getPriority());
		vo.setStage(ztStory.getStage().getDesc());
		return vo;
	}
	
	private TaskVo convertTask2Vo(ZtTask ztTask) {
		TaskVo vo = new TaskVo();
		vo.setId(ztTask.getId());
		vo.setIsSub(ztTask.getParent().intValue() > 0 ? true : false);
		vo.setName(ztTask.getName());
		vo.setPriorityName(ztTask.getPriority().getPriority() + "");
		vo.setLeft(ztTask.getLeft());
		vo.setDeadline(ztTask.getDeadline());
		vo.setStatusName(ztTask.getStatus().getDesc());
		//TODO
		vo.setAssignTo(ztTask.getAssignedTo());
		return vo;
	}
	
	private List<ZtTask> getByStoryId(Long storyId) {
		QueryWrapper<ZtTask> queryWrapper = new QueryWrapper<ZtTask>();
		queryWrapper.eq("story", storyId);
		queryWrapper.eq("deleted", "0");
		return this.ztTaskMapper.selectList(queryWrapper);
	}
}
