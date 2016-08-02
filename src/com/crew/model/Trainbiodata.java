package com.crew.model;
// default package
// Generated May 21, 2015 7:33:53 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Trainbiodata generated by hbm2java
 */
@Entity
@Table(name = "trainbiodata", catalog = "crewdb")
public class Trainbiodata implements java.io.Serializable {

	private Long id;
	private Integer trainNo;
	private String trainName;
	private String originatingStation;
	private String destinationStation;
	private Boolean daily;

	public Trainbiodata() {
	}

	public Trainbiodata(Integer trainNo, String trainName,
			String originatingStation, String destinationStation, Boolean daily) {
		this.trainNo = trainNo;
		this.trainName = trainName;
		this.originatingStation = originatingStation;
		this.destinationStation = destinationStation;
		this.daily = daily;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "train_no")
	public Integer getTrainNo() {
		return this.trainNo;
	}

	public void setTrainNo(Integer trainNo) {
		this.trainNo = trainNo;
	}

	@Column(name = "train_name")
	public String getTrainName() {
		return this.trainName;
	}

	public void setTrainName(String trainName) {
		this.trainName = trainName;
	}

	@Column(name = "originating_station")
	public String getOriginatingStation() {
		return this.originatingStation;
	}

	public void setOriginatingStation(String originatingStation) {
		this.originatingStation = originatingStation;
	}

	@Column(name = "destination_station")
	public String getDestinationStation() {
		return this.destinationStation;
	}

	public void setDestinationStation(String destinationStation) {
		this.destinationStation = destinationStation;
	}

	@Column(name = "daily")
	public Boolean getDaily() {
		return this.daily;
	}

	public void setDaily(Boolean daily) {
		this.daily = daily;
	}

}