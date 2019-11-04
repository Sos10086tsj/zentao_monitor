package com.chinesedreamer.zentaomonitor.comparator;

import java.util.Comparator;

import com.chinesedreamer.zentaomonitor.vo.TaskVo;

public class TaskVoComparator implements Comparator<TaskVo>{

	@Override
	public int compare(TaskVo o1, TaskVo o2) {
		if (null == o1.getDeadline() && null == o2.getDeadline()) {
			if (o1.getAssignTo().equals(o2.getAssignTo())) {
				return o1.getId().compareTo(o2.getId());
			}else {
				return o1.getAssignTo().compareTo(o2.getAssignTo());
			}
		}else if (null != o1.getDeadline() && null == o2.getDeadline()) {
			return -1;
		}else if (null == o1.getDeadline() && null != o2.getDeadline()) {
			return 1;
		}else {
			if (o1.getDeadline().getTime() == o2.getDeadline().getTime()) {
				if (o1.getAssignTo().equals(o2.getAssignTo())) {
					return o1.getId().compareTo(o2.getId());
				}else {
					return o1.getAssignTo().compareTo(o2.getAssignTo());
				}
			}else {
				return o1.getDeadline().compareTo(o2.getDeadline());
			}
		}
	}

}
