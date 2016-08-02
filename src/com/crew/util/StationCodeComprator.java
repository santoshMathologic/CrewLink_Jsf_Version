package com.crew.util;

import java.util.Comparator;

import com.crew.model.CrewOutstation;

public class StationCodeComprator implements Comparator<CrewOutstation>{

	@Override
	public int compare(CrewOutstation arg0, CrewOutstation arg1) {
		return arg0.getStationCode().compareTo(arg1.getStationCode());
	}

}
