package com.chinesedreamer.zentaomonitor.comparator;

import java.util.Comparator;

import com.chinesedreamer.zentaomonitor.vo.TaskVo;

public class TaskVoComparator implements Comparator<TaskVo>{

	@Override
	public int compare(TaskVo o1, TaskVo o2) {
		return o1.getAssignTo().compareTo(o2.getAssignTo());
	}

}
