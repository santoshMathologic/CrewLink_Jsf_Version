package com.crew.HelperUtil;

public class Periodic {

	
	private String TrainNumbers;
	private String crewBase;
	private String crewType;
	

	public Periodic() {

	}
	public Periodic(String TrainNumbers, String crewBase, String crewType) {
		super();
		this.TrainNumbers = TrainNumbers;
		this.crewBase = crewBase;
		this.crewType = crewType;
		
	}

	public String getTrainNumbers() {
		return TrainNumbers;
	}

	public void setTrainNumbers(String trainNumbers) {
		TrainNumbers = trainNumbers;
	}

	public String getCrewBase() {
		return crewBase;
	}

	public void setCrewBase(String crewBase) {
		this.crewBase = crewBase;
	}

	public String getCrewType() {
		return crewType;
	}

	public void setCrewType(String crewType) {
		this.crewType = crewType;
	}
	
}
