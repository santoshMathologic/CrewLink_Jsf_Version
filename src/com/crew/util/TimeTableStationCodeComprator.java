package com.crew.util;

import java.util.Comparator;

import com.crew.model.TimeTableMaster;;

public class TimeTableStationCodeComprator implements Comparator<TimeTableMaster>{
	@Override
	public int compare(TimeTableMaster o1, TimeTableMaster o2) {
		return (o1.getStationCode().compareTo(o2.getStationCode()));
	}

}
