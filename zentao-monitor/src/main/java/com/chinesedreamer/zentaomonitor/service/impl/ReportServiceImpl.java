package com.chinesedreamer.zentaomonitor.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chinesedreamer.zentaomonitor.comparator.BugVoComparator;
import com.chinesedreamer.zentaomonitor.comparator.StoryVoComparator;
import com.chinesedreamer.zentaomonitor.comparator.TaskVoComparator;
import com.chinesedreamer.zentaomonitor.config.AppProperties;
import com.chinesedreamer.zentaomonitor.constant.BugStatus;
import com.chinesedreamer.zentaomonitor.constant.MonitorConfigType;
import com.chinesedreamer.zentaomonitor.constant.StoryStage;
import com.chinesedreamer.zentaomonitor.constant.TaskStatus;
import com.chinesedreamer.zentaomonitor.dao.DailyReportMapper;
import com.chinesedreamer.zentaomonitor.dao.MonitorConfigMapper;
import com.chinesedreamer.zentaomonitor.dao.ZtBugMapper;
import com.chinesedreamer.zentaomonitor.dao.ZtBuildMapper;
import com.chinesedreamer.zentaomonitor.dao.ZtStoryMapper;
import com.chinesedreamer.zentaomonitor.dao.ZtTaskMapper;
import com.chinesedreamer.zentaomonitor.dao.ZtUserMapper;
import com.chinesedreamer.zentaomonitor.entity.DingtalkMessage;
import com.chinesedreamer.zentaomonitor.model.DailyReport;
import com.chinesedreamer.zentaomonitor.model.MonitorConfig;
import com.chinesedreamer.zentaomonitor.model.ZtBug;
import com.chinesedreamer.zentaomonitor.model.ZtBuild;
import com.chinesedreamer.zentaomonitor.model.ZtStory;
import com.chinesedreamer.zentaomonitor.model.ZtTask;
import com.chinesedreamer.zentaomonitor.model.ZtUser;
import com.chinesedreamer.zentaomonitor.service.DingtalkSenderService;
import com.chinesedreamer.zentaomonitor.service.ReportService;
import com.chinesedreamer.zentaomonitor.vo.BugVo;
import com.chinesedreamer.zentaomonitor.vo.DailyReportVo;
import com.chinesedreamer.zentaomonitor.vo.StoryVo;
import com.chinesedreamer.zentaomonitor.vo.TaskVo;

@Service
public class ReportServiceImpl implements ReportService{
	
	private Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);
	
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
	@Autowired
	private ZtBuildMapper ztBuildMapper;
	@Autowired
	private MonitorConfigMapper monitorConfigMapper;
	@Autowired
	private AppProperties appProperties;
	@Autowired
	private DingtalkSenderService dingtalkSenderService;
	
	private Map<String, ZtUser> cacheMap;
	private Map<String, String> notifyUsers = new HashMap<String, String>();
	private String SEPARATOR = ",";
	
	@Override
	public DailyReportVo getDailyReport(Long id) {
		notifyUsers.clear();
		
		DailyReport dailyReport = this.dailyReportMapper.selectById(id);
		
		DailyReportVo vo = new DailyReportVo();
		vo.setReportDate(dailyReport.getReportDate());
		vo.setReportTitle(dailyReport.getReportTitle());
		ZtBuild ztBuild = this.ztBuildMapper.selectById(dailyReport.getBuildId());
		vo.setBuildDate(ztBuild.getDate());
		
		//story
		vo.setTotalStoryNum(0);
		vo.setProcessingStoryNum(0);
		Set<Long> storyIds = new HashSet<Long>();
		if (StringUtils.isNotEmpty(dailyReport.getStories())) {
			//处理story相关信息
			String[] storyIdStrs = dailyReport.getStories().split(SEPARATOR);
			List<StoryVo> storyVos = new ArrayList<StoryVo>();
			for (String storyIdStr : storyIdStrs) {
				// 获取story信息
				Long storyId = Long.parseLong(storyIdStr) + dailyReport.getStoryBaseId().longValue();
				ZtStory ztStory = this.ztStoryMapper.selectById(storyId);
				StoryVo storyVo = this.convertStory2Vo(ztStory);
				// 获取story关联的任务信息
//				List<ZtTask> ztTasks = this.getTasksByStoryId(storyId);
//				List<TaskVo> taskVos = ztTasks.stream().map(t -> { return convertTask2Vo(t); }).collect(Collectors.toList());
//				taskVos.sort(new TaskVoComparator());
//				storyVo.setUnCloseTasks(taskVos);
				storyVos.add(storyVo);
				if (!ztStory.getStage().equals(StoryStage.CLOSED)) {
					vo.setProcessingStoryNum(vo.getProcessingStoryNum() + 1);
				}
				storyIds.add(Long.parseLong(storyIdStr));
			}
			storyVos.sort(new StoryVoComparator());
			vo.setStories(storyVos);
			vo.setTotalStoryNum(storyIdStrs.length);
		}
		
		//tasks
		if (StringUtils.isNotEmpty(dailyReport.getTasks())) {
			if (null == vo.getOtherUncloseTasks()) {
				vo.setOtherUncloseTasks(new ArrayList<TaskVo>());
			}
			String[] taskIdStrs = dailyReport.getTasks().split(SEPARATOR);
			for (String taskIdStr : taskIdStrs) {
				Long taskId = Long.parseLong(taskIdStr) + dailyReport.getTaskBaseId().longValue();
				ZtTask ztTask = this.ztTaskMapper.selectById(taskId);
				if (!ztTask.getStatus().equals(TaskStatus.CANCEL) && !ztTask.getStatus().equals(TaskStatus.CLOSED)) {
					vo.getOtherUncloseTasks().add(this.convertTask2Vo(ztTask));
				}
				
			}
			vo.getOtherUncloseTasks().sort(new TaskVoComparator());
		}
		
		//bug
		vo.setTotalVersionBugNum(0);
		vo.setTotalVersionUncloseBugNum(0);
		if (StringUtils.isNotEmpty(dailyReport.getBugs())) {
			if (null == vo.getUncloseBugs()) {
				vo.setUncloseBugs(new ArrayList<BugVo>());
			}
			String[] bugIdStrs = dailyReport.getBugs().split(SEPARATOR);
			for (String bugIdStr : bugIdStrs) {
				Long bugId = Long.parseLong(bugIdStr) + dailyReport.getBugBaseId().longValue();
				ZtBug ztBug = this.ztBugMapper.selectById(bugId);
				if (!ztBug.getStatus().equals(BugStatus.CLOSED)) {
					vo.getUncloseBugs().add(this.convertBug2Vo(ztBug));
				}
			}
			vo.getUncloseBugs().sort(new BugVoComparator());
		}
		return vo;
	}

	private StoryVo convertStory2Vo(ZtStory ztStory) {
		StoryVo vo = new StoryVo();
		vo.setId(ztStory.getId());
		vo.setTitle(ztStory.getTitle());
		vo.setPriority(ztStory.getPriority().getPriority());
		vo.setStage(ztStory.getStage().getDesc());
		vo.setUrl(this.convertUrl(this.appProperties.getZentaoBaseUrl() + this.appProperties.getZentaoSotryUrl(), ztStory.getId()));
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
		vo.setUrl(this.convertUrl(this.appProperties.getZentaoBaseUrl() + this.appProperties.getZentaoTaskUrl(), ztTask.getId()));
		return vo;
	}
	
//	private List<ZtTask> getTasksByStoryId(Long storyId) {
//		QueryWrapper<ZtTask> queryWrapper = new QueryWrapper<ZtTask>();
//		queryWrapper.eq("story", storyId);
//		queryWrapper.eq("deleted", "0");
//		queryWrapper.notIn("status", Arrays.asList(TaskStatus.CANCEL, TaskStatus.CLOSED, TaskStatus.DONE));
//		return this.ztTaskMapper.selectList(queryWrapper);
//	}
	
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
//		queryWrapper.notIn("story", storyIds);
		queryWrapper.eq("deleted", "0");
		queryWrapper.notIn("status", Arrays.asList(TaskStatus.CANCEL, TaskStatus.CLOSED, TaskStatus.DONE));
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
		bugVo.setUrl(this.convertUrl(this.appProperties.getZentaoBaseUrl() + this.appProperties.getZentaoBugUrl(), ztBug.getId()));
		return bugVo;
	}

	@Override
	public void generateDailyReport() {
		QueryWrapper<MonitorConfig> queryWrapper = new QueryWrapper<MonitorConfig>();
		queryWrapper.eq("config_type", MonitorConfigType.DAILY_REPORT);
		MonitorConfig config = this.monitorConfigMapper.selectOne(queryWrapper);
		if (null == config) {
			this.logger.info("missing config");
			return;
		}
		String[] buildIdStrs = config.getConfigValue().split(SEPARATOR);
		for (String buildIdStr : buildIdStrs) {
			DailyReport dailyReport = new DailyReport();
			
			ZtBuild ztBuild = this.ztBuildMapper.selectById(Long.parseLong(buildIdStr));
			dailyReport.setBuildId(Long.parseLong(buildIdStr));
			dailyReport.setReportDate(new Date());
			dailyReport.setReportTitle("[DailyReport]" + ztBuild.getName());
			
			//处理story信息
			if (StringUtils.isNotEmpty(ztBuild.getStories())) {
				String[] sotryIdStrs = ztBuild.getStories().split(SEPARATOR);
				List<Long> sotryIds = new ArrayList<Long>();
				for (String sotryIdStr : sotryIdStrs) {
					if (StringUtils.isNotEmpty(sotryIdStr)) {
						sotryIds.add(Long.parseLong(sotryIdStr));
					}
				}
				dailyReport.setStoryBaseId(this.getBaseId(sotryIds));
				dailyReport.setStories(this.convert2CommaStr(sotryIds, dailyReport.getStoryBaseId()));
			}
			//处理task信息
			List<ZtTask> ztTasks = this.getUnclosedTasks(null);
			if (!CollectionUtils.isEmpty(ztTasks)) {
				List<Long> taskIds = ztTasks.stream().map(t -> { return t.getId(); } ).collect(Collectors.toList());
				dailyReport.setTaskBaseId(this.getBaseId(taskIds));
				dailyReport.setTasks(this.convert2CommaStr(taskIds, dailyReport.getTaskBaseId()));
			}
			//处理bug信息
			List<ZtBug> ztBugs = this.getBugsByBuildId(ztBuild.getId());
			if (!CollectionUtils.isEmpty(ztBugs)) {
				List<Long> bugIds = ztBugs.stream().map(t -> { return t.getId(); } ).collect(Collectors.toList());
				dailyReport.setBugBaseId(this.getBaseId(bugIds));
				dailyReport.setBugs(this.convert2CommaStr(bugIds, dailyReport.getBugBaseId()));
			}
			this.dailyReportMapper.insert(dailyReport);
			
			DingtalkMessage message = new DingtalkMessage();
//			message.setUrl(this.appProperties.getZentaoDailyReportUrl() + dailyReport.getId());
//			message.setPicUrl(this.appProperties.getZentaoDailyReportPicUrl());
			message.setTitle("[" + ztBuild.getName() + "]版本进度报告");
			message.setText(
					"### [" + ztBuild.getName() + "版本进度报告](" + this.appProperties.getZentaoDailyReportUrl() + dailyReport.getId() + ")\n"
					+ "请大家及时完成自己的任务和bug。");
			List<String> notifyMobiles = new ArrayList<String>();
			for (String key : this.notifyUsers.keySet()) {
				notifyMobiles.add(this.notifyUsers.get(key));
			}
			message.setNotifyMobiles(notifyMobiles);
			this.dingtalkSenderService.sendMessage(message);
		}
	}
	
	private int getBaseId(List<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return 0;
		}
		Long tmp = -1l;
		for (Long id : ids) {
			if (tmp == -1 || id < tmp) {
				tmp = id;
			}
		}
		return tmp.intValue() - 1;
	}
	
	private String convert2CommaStr(List<Long> ids, Integer baseId) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < ids.size(); i++) {
			builder.append(ids.get(i).intValue() - baseId);
			if (i != ids.size() - 1) {
				builder.append(SEPARATOR);
			}
		}
		return builder.toString();
	}
	
	private String convertUrl(String template, Object... objects) {
		MessageFormat messageFormat = new MessageFormat(template);
		return messageFormat.format(objects);
	}
}
