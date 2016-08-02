package com.crew.util;

import java.util.Comparator;
import com.crew.model.CrewHeadquarterMaster;
public class HeadQuarterCodeComprator implements Comparator<CrewHeadquarterMaster>{
	@Override
	public int compare(CrewHeadquarterMaster hq1, CrewHeadquarterMaster hq2) {
		return (hq1.getCrewBase().compareTo(hq2.getCrewBase()));
	}

}
