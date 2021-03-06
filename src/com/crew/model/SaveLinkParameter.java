package com.crew.model;
// default package
// Generated Aug 17, 2015 11:25:44 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SaveLinkParameter generated by hbm2java
 */
@Entity
@Table(name = "save_link_parameter", catalog = "crewdb")
public class SaveLinkParameter implements java.io.Serializable {

	private Long id;
	private Double avgKm;
	private Double avgRnTime;

	public SaveLinkParameter() {
	}

	public SaveLinkParameter(Double avgKm, Double avgRnTime) {
		this.avgKm = avgKm;
		this.avgRnTime = avgRnTime;
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

	@Column(name = "avg_km", precision = 22, scale = 0)
	public Double getAvgKm() {
		return this.avgKm;
	}

	public void setAvgKm(Double avgKm) {
		this.avgKm = avgKm;
	}

	@Column(name = "avg_rn_time", precision = 22, scale = 0)
	public Double getAvgRnTime() {
		return this.avgRnTime;
	}

	public void setAvgRnTime(Double avgRnTime) {
		this.avgRnTime = avgRnTime;
	}

}
