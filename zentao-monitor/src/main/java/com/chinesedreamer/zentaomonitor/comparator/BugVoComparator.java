package com.chinesedreamer.zentaomonitor.comparator;

import java.util.Comparator;

import com.chinesedreamer.zentaomonitor.vo.BugVo;

public class BugVoComparator implements Comparator<BugVo>{

	@Override
	public int compare(BugVo o1, BugVo o2) {
		return o1.getAssignedTo().compareTo(o2.getAssignedTo());
	}

}
