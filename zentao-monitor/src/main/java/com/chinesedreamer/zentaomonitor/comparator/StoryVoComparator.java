package com.chinesedreamer.zentaomonitor.comparator;

import java.util.Comparator;

import com.chinesedreamer.zentaomonitor.vo.StoryVo;

public class StoryVoComparator implements Comparator<StoryVo>{

	@Override
	public int compare(StoryVo o1, StoryVo o2) {
		return o1.getPriority().compareTo(o2.getPriority());
	}

}
