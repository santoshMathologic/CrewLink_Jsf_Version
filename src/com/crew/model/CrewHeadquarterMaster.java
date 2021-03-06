package com.crew.model;
// default package
// Generated Apr 28, 2015 3:26:24 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CrewHeadquarterMaster generated by hbm2java
 */
@Entity
@Table(name = "crew_headquarter_master", catalog = "crewlinkssystem")
public class CrewHeadquarterMaster implements java.io.Serializable {

	private Long id;
	private String crewBase;
	private Integer dmeNo;
	private Integer dmeKm;
	private Integer acmeNo;
	private Integer acmeKm;
	private Integer acpNo;
	private Integer acpKm;
	private Integer dpNo;
	private Integer dpKm;
	private Integer emuNo;
	private Integer emuKm;
	private Integer dmuNo;
	private Integer dmuKm;
	private Integer memuNo;
	private Integer memuKm;
	private Integer acndNo;
	private Integer acndKm;
	private Integer dndNo;
	private Integer dndKm;
	private Integer others1No;
	private Integer others1Km;
	private Integer others2No;
	private Integer others2Km;
	private Integer timeGivenforSignOn;
	private Integer timeGivenforSignOff;

	public CrewHeadquarterMaster() {
	}

	public CrewHeadquarterMaster(String crewBase, Integer dmeNo, Integer dmeKm,
			Integer acmeNo, Integer acmeKm, Integer acpNo, Integer acpKm,
			Integer dpNo, Integer dpKm, Integer emuNo, Integer emuKm,
			Integer dmuNo, Integer dmuKm, Integer memuNo, Integer memuKm,
			Integer acndNo, Integer acndKm, Integer dndNo, Integer dndKm,
			Integer others1No, Integer others1Km, Integer others2No,
			Integer others2Km, Integer timeGivenforSignOn,
			Integer timeGivenforSignOff) {
		this.crewBase = crewBase;
		this.dmeNo = dmeNo;
		this.dmeKm = dmeKm;
		this.acmeNo = acmeNo;
		this.acmeKm = acmeKm;
		this.acpNo = acpNo;
		this.acpKm = acpKm;
		this.dpNo = dpNo;
		this.dpKm = dpKm;
		this.emuNo = emuNo;
		this.emuKm = emuKm;
		this.dmuNo = dmuNo;
		this.dmuKm = dmuKm;
		this.memuNo = memuNo;
		this.memuKm = memuKm;
		this.acndNo = acndNo;
		this.acndKm = acndKm;
		this.dndNo = dndNo;
		this.dndKm = dndKm;
		this.others1No = others1No;
		this.others1Km = others1Km;
		this.others2No = others2No;
		this.others2Km = others2Km;
		this.timeGivenforSignOn = timeGivenforSignOn;
		this.timeGivenforSignOff = timeGivenforSignOff;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "Crew Base", length = 7)
	public String getCrewBase() {
		return this.crewBase;
	}

	public void setCrewBase(String crewBase) {
		this.crewBase = crewBase;
	}

	@Column(name = "DME_No")
	public Integer getDmeNo() {
		return this.dmeNo;
	}

	public void setDmeNo(Integer dmeNo) {
		this.dmeNo = dmeNo;
	}

	@Column(name = "DME_KM")
	public Integer getDmeKm() {
		return this.dmeKm;
	}

	public void setDmeKm(Integer dmeKm) {
		this.dmeKm = dmeKm;
	}

	@Column(name = "ACME_No")
	public Integer getAcmeNo() {
		return this.acmeNo;
	}

	public void setAcmeNo(Integer acmeNo) {
		this.acmeNo = acmeNo;
	}

	@Column(name = "ACME_KM")
	public Integer getAcmeKm() {
		return this.acmeKm;
	}

	public void setAcmeKm(Integer acmeKm) {
		this.acmeKm = acmeKm;
	}

	@Column(name = "ACP_No")
	public Integer getAcpNo() {
		return this.acpNo;
	}

	public void setAcpNo(Integer acpNo) {
		this.acpNo = acpNo;
	}

	@Column(name = "ACP_KM")
	public Integer getAcpKm() {
		return this.acpKm;
	}

	public void setAcpKm(Integer acpKm) {
		this.acpKm = acpKm;
	}

	@Column(name = "DP_No")
	public Integer getDpNo() {
		return this.dpNo;
	}

	public void setDpNo(Integer dpNo) {
		this.dpNo = dpNo;
	}

	@Column(name = "DP_KM")
	public Integer getDpKm() {
		return this.dpKm;
	}

	public void setDpKm(Integer dpKm) {
		this.dpKm = dpKm;
	}

	@Column(name = "EMU_No")
	public Integer getEmuNo() {
		return this.emuNo;
	}

	public void setEmuNo(Integer emuNo) {
		this.emuNo = emuNo;
	}

	@Column(name = "EMU_KM")
	public Integer getEmuKm() {
		return this.emuKm;
	}

	public void setEmuKm(Integer emuKm) {
		this.emuKm = emuKm;
	}

	@Column(name = "DMU_No")
	public Integer getDmuNo() {
		return this.dmuNo;
	}

	public void setDmuNo(Integer dmuNo) {
		this.dmuNo = dmuNo;
	}

	@Column(name = "DMU_KM")
	public Integer getDmuKm() {
		return this.dmuKm;
	}

	public void setDmuKm(Integer dmuKm) {
		this.dmuKm = dmuKm;
	}

	@Column(name = "MEMU_No")
	public Integer getMemuNo() {
		return this.memuNo;
	}

	public void setMemuNo(Integer memuNo) {
		this.memuNo = memuNo;
	}

	@Column(name = "MEMU_KM")
	public Integer getMemuKm() {
		return this.memuKm;
	}

	public void setMemuKm(Integer memuKm) {
		this.memuKm = memuKm;
	}

	@Column(name = "ACND_No")
	public Integer getAcndNo() {
		return this.acndNo;
	}

	public void setAcndNo(Integer acndNo) {
		this.acndNo = acndNo;
	}

	@Column(name = "ACND_KM")
	public Integer getAcndKm() {
		return this.acndKm;
	}

	public void setAcndKm(Integer acndKm) {
		this.acndKm = acndKm;
	}

	@Column(name = "DND_No")
	public Integer getDndNo() {
		return this.dndNo;
	}

	public void setDndNo(Integer dndNo) {
		this.dndNo = dndNo;
	}

	@Column(name = "DND_KM")
	public Integer getDndKm() {
		return this.dndKm;
	}

	public void setDndKm(Integer dndKm) {
		this.dndKm = dndKm;
	}

	@Column(name = "Others1_No")
	public Integer getOthers1No() {
		return this.others1No;
	}

	public void setOthers1No(Integer others1No) {
		this.others1No = others1No;
	}

	@Column(name = "Others1_KM")
	public Integer getOthers1Km() {
		return this.others1Km;
	}

	public void setOthers1Km(Integer others1Km) {
		this.others1Km = others1Km;
	}

	@Column(name = "Others2_No")
	public Integer getOthers2No() {
		return this.others2No;
	}

	public void setOthers2No(Integer others2No) {
		this.others2No = others2No;
	}

	@Column(name = "Others2_KM")
	public Integer getOthers2Km() {
		return this.others2Km;
	}

	public void setOthers2Km(Integer others2Km) {
		this.others2Km = others2Km;
	}

	@Column(name = "TimeGivenforSignOn")
	public Integer getTimeGivenforSignOn() {
		return this.timeGivenforSignOn;
	}

	public void setTimeGivenforSignOn(Integer timeGivenforSignOn) {
		this.timeGivenforSignOn = timeGivenforSignOn;
	}

	@Column(name = "TimeGivenforSignOff")
	public Integer getTimeGivenforSignOff() {
		return this.timeGivenforSignOff;
	}

	public void setTimeGivenforSignOff(Integer timeGivenforSignOff) {
		this.timeGivenforSignOff = timeGivenforSignOff;
	}

}
