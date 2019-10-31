package com.chinesedreamer.zentaomonitor.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chinesedreamer.zentaomonitor.comparator.BugVoComparator;
import com.chinesedreamer.zentaomonitor.comparator.TaskVoComparator;
import com.chinesedreamer.zentaomonitor.constant.BugStatus;
import com.chinesedreamer.zentaomonitor.constant.StoryStage;
import com.chinesedreamer.zentaomonitor.constant.TaskStatus;
import com.chinesedreamer.zentaomonitor.dao.DailyReportMapper;
import com.chinesedreamer.zentaomonitor.dao.ZtBugMapper;
import com.chinesedreamer.zentaomonitor.dao.ZtStoryMapper;
import com.chinesedreamer.zentaomonitor.dao.ZtTaskMapper;
import com.chinesedreamer.zentaomonitor.dao.ZtUserMapper;
import com.chinesedreamer.zentaomonitor.model.DailyReport;
import com.chinesedreamer.zentaomonitor.model.ZtBug;
import com.chinesedreamer.zentaomonitor.model.ZtStory;
import com.chinesedreamer.zentaomonitor.model.ZtTask;
import com.chinesedreamer.zentaomonitor.model.ZtUser;
import com.chinesedreamer.zentaomonitor.service.ReportService;
import com.chinesedreamer.zentaomonitor.vo.BugVo;
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
	@Autowired
	private ZtUserMapper ztUserMapper;
	@Autowired
	private ZtBugMapper ztBugMapper;
	
	private Map<String, ZtUser> cacheMap;
	private Map<String, String> notifyUsers = new HashMap<String, String>();
	
	@Override
	public DailyReportVo getReport(Long id) {
		notifyUsers.clear();
		
		DailyReport dailyReport = this.dailyReportMapper.selectById(id);
		
		DailyReportVo vo = new DailyReportVo();
		vo.setReportDate(dailyReport.getReportDate());
		vo.setReportTitle(dailyReport.getReportTitle());
		
		//story
		vo.setTotalStoryNum(0);
		vo.setProcessingStoryNum(0);
		Set<Long> storyIds = new HashSet<Long>();
		if (StringUtils.isNotEmpty(dailyReport.getStories())) {
			//处理story相关信息
			String[] storyIdStrs = dailyReport.getStories().split(",");
			List<StoryVo> storyVos = new ArrayList<StoryVo>();
			for (String storyIdStr : storyIdStrs) {
				// 获取story信息
				Long storyId = Long.parseLong(storyIdStr) + dailyReport.getStoryBaseId().longValue();
				ZtStory ztStory = this.ztStoryMapper.selectById(storyId);
				StoryVo storyVo = this.convertStory2Vo(ztStory);
				// 获取story关联的任务信息
				List<ZtTask> ztTasks = this.getTasksByStoryId(storyId);
				List<TaskVo> taskVos = ztTasks.stream().map(t -> { return convertTask2Vo(t); }).collect(Collectors.toList());
				taskVos.sort(new TaskVoComparator());
				storyVo.setUnCloseTasks(taskVos);
				storyVos.add(storyVo);
				if (!ztStory.getStage().equals(StoryStage.CLOSED)) {
					vo.setProcessingStoryNum(vo.getProcessingStoryNum() + 1);
				}
				storyIds.add(Long.parseLong(storyIdStr));
			}
			vo.setStories(storyVos);
			vo.setTotalStoryNum(storyIdStrs.length);
		}
		
		//非story tasks
		List<ZtTask> uncloseTasks = this.getUnclosedTasks(storyIds);
		List<TaskVo> uncloseTaskVos = uncloseTasks.stream().map(t -> { return convertTask2Vo(t); }).collect(Collectors.toList());
		uncloseTaskVos.sort(new TaskVoComparator());
		vo.setOtherUncloseTasks(uncloseTaskVos);
		
		//bug
		vo.setTotalVersionBugNum(0);
		vo.setTotalVersionUncloseBugNum(0);
		List<ZtBug> ztBugs = this.getBugsByBuildId(dailyReport.getBuildId());
		if (!CollectionUtils.isEmpty(ztBugs)) {
			vo.setTotalVersionBugNum(ztBugs.size());
			if (null == vo.getUncloseBugs()) {
				vo.setUncloseBugs(new ArrayList<BugVo>());
			}
			for (ZtBug ztBug : ztBugs) {
				if (!ztBug.getStatus().equals(BugStatus.closed)) {
					vo.setTotalVersionUncloseBugNum(vo.getTotalVersionUncloseBugNum() + 1);
					BugVo bugVo = this.convertBug2Vo(ztBug);
					vo.getUncloseBugs().add(bugVo);
				}
			}
			vo.getUncloseBugs().sort(new BugVoComparator());
		}
		
		if (!this.notifyUsers.isEmpty()) {
			vo.setNotifyMobiles(new ArrayList<String>());
			for (String key : this.notifyUsers.keySet()) {
				vo.getNotifyMobiles().add(this.notifyUsers.get(key));
			}
		}
		return vo;
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
		vo.setAssignTo(this.getUserRealname(ztTask.getAssignedTo()));
		return vo;
	}
	
	private List<ZtTask> getTasksByStoryId(Long storyId) {
		QueryWrapper<ZtTask> queryWrapper = new QueryWrapper<ZtTask>();
		queryWrapper.eq("story", storyId);
		queryWrapper.eq("deleted", "0");
		queryWrapper.notIn("status", Arrays.asList(TaskStatus.CANCEL, TaskStatus.CLOSED));
		return this.ztTaskMapper.selectList(queryWrapper);
	}
	
	private String getUserRealname(String account) {
		if (account.equals("closed")) {
			return "-";
		}
		if (null == this.cacheMap) {
			this.cacheMap = new HashMap<String, ZtUser>();
		}
		if (this.cacheMap.containsKey(account)) {
			return this.cacheMap.get(account).getRealName();
		}
		QueryWrapper<ZtUser> queryWrapper = new QueryWrapper<ZtUser>();
		queryWrapper.eq("account", account);
		ZtUser ztUser = this.ztUserMapper.selectOne(queryWrapper);
		if (!this.notifyUsers.containsKey(account)) {
			this.notifyUsers.put(account, ztUser.getMobile());
		}
		return ztUser.getRealName();
	}
	
	private List<ZtTask> getUnclosedTasks(Set<Long> storyIds) {
		QueryWrapper<ZtTask> queryWrapper = new QueryWrapper<ZtTask>();
		queryWrapper.notIn("story", storyIds);
		queryWrapper.eq("deleted", "0");
		queryWrapper.ne("status", TaskStatus.CLOSED);
		return this.ztTaskMapper.selectList(queryWrapper);
	}
	
	private List<ZtBug> getBugsByBuildId(Long buildId) {
		QueryWrapper<ZtBug> queryWrapper = new QueryWrapper<ZtBug>();
		queryWrapper.eq("deleted", "0");
		queryWrapper.and(w -> w.eq("openedBuild", buildId).or().likeLeft("openedBuild", "," + buildId).or().likeRight("openedBuild", buildId + ",").or().like("openedBuild", ","+buildId+","));
		return this.ztBugMapper.selectList(queryWrapper);
	}
	
	private BugVo convertBug2Vo(ZtBug ztBug) {
		BugVo bugVo = new BugVo();
		bugVo.setId(ztBug.getId());
		bugVo.setPriority(ztBug.getPriority().getPriority());
		bugVo.setStatus(ztBug.getStatus().getDesc());
		bugVo.setTitle(ztBug.getTitle());
		bugVo.setAssignedTo(this.getUserRealname(ztBug.getAssignedTo()));
		return bugVo;
	}
}
