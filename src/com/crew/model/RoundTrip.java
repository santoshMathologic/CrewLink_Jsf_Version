package com.crew.model;
// default package
// Generated Jul 23, 2015 11:19:14 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RoundTrip generated by hbm2java
 */
@Entity
@Table(name = "round_trip", catalog = "crewdb")
public class RoundTrip implements java.io.Serializable {

	private Long id;
	private Customer customer;
	private String trainNo;
	private Long serialNo;
	private String fromStation;
	private String toStation;
	private Long distance;
	private String crewType;
	private String noOfDays;
	private Date arrivaltime;
	private Date departuretime;
	private String crewBase;
	private String sumOfDdnames;
	private String availabilityAtHq;
	private Long transactionNoDdToRt;
	private Integer periodicRest;
	private Integer periodicRestAvailability;
	private Integer noOfDayWithPeriodicRest;
	private String transactionNoCrewLink;
	private String availability;
	private Date signonTime;
	private Date signoffTime;
	private Boolean status;
	private String rrHqRest;
	private String rrOsRest;

	public RoundTrip() {
	}

	public RoundTrip(Customer customer, String trainNo, Long serialNo,
			String fromStation, String toStation, Long distance,
			String crewType, String noOfDays, Date arrivaltime,
			Date departuretime, String crewBase, String sumOfDdnames,
			String availabilityAtHq, Long transactionNoDdToRt,
			Integer periodicRest, Integer periodicRestAvailability,
			Integer noOfDayWithPeriodicRest, String transactionNoCrewLink,
			String availability, Date signonTime, Date signoffTime,
			Boolean status, String rrHqRest, String rrOsRest) {
		this.customer = customer;
		this.trainNo = trainNo;
		this.serialNo = serialNo;
		this.fromStation = fromStation;
		this.toStation = toStation;
		this.distance = distance;
		this.crewType = crewType;
		this.noOfDays = noOfDays;
		this.arrivaltime = arrivaltime;
		this.departuretime = departuretime;
		this.crewBase = crewBase;
		this.sumOfDdnames = sumOfDdnames;
		this.availabilityAtHq = availabilityAtHq;
		this.transactionNoDdToRt = transactionNoDdToRt;
		this.periodicRest = periodicRest;
		this.periodicRestAvailability = periodicRestAvailability;
		this.noOfDayWithPeriodicRest = noOfDayWithPeriodicRest;
		this.transactionNoCrewLink = transactionNoCrewLink;
		this.availability = availability;
		this.signonTime = signonTime;
		this.signoffTime = signoffTime;
		this.status = status;
		this.rrHqRest = rrHqRest;
		this.rrOsRest = rrOsRest;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_table_fk")
	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Column(name = "train_no", length = 200)
	public String getTrainNo() {
		return this.trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	@Column(name = "SerialNo")
	public Long getSerialNo() {
		return this.serialNo;
	}

	public void setSerialNo(Long serialNo) {
		this.serialNo = serialNo;
	}

	@Column(name = "from_Station", length = 30)
	public String getFromStation() {
		return this.fromStation;
	}

	public void setFromStation(String fromStation) {
		this.fromStation = fromStation;
	}

	@Column(name = "to_Station", length = 30)
	public String getToStation() {
		return this.toStation;
	}

	public void setToStation(String toStation) {
		this.toStation = toStation;
	}

	@Column(name = "distance")
	public Long getDistance() {
		return this.distance;
	}

	public void setDistance(Long distance) {
		this.distance = distance;
	}

	@Column(name = "Crew_Type", length = 9)
	public String getCrewType() {
		return this.crewType;
	}

	public void setCrewType(String crewType) {
		this.crewType = crewType;
	}

	@Column(name = "No_Of_Days", length = 12)
	public String getNoOfDays() {
		return this.noOfDays;
	}

	public void setNoOfDays(String noOfDays) {
		this.noOfDays = noOfDays;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "arrivaltime", length = 0)
	public Date getArrivaltime() {
		return this.arrivaltime;
	}

	public void setArrivaltime(Date arrivaltime) {
		this.arrivaltime = arrivaltime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "departuretime", length = 0)
	public Date getDeparturetime() {
		return this.departuretime;
	}

	public void setDeparturetime(Date departuretime) {
		this.departuretime = departuretime;
	}

	@Column(name = "Crew_Base", length = 5)
	public String getCrewBase() {
		return this.crewBase;
	}

	public void setCrewBase(String crewBase) {
		this.crewBase = crewBase;
	}

	@Column(name = "sum_of_DDNames", length = 100)
	public String getSumOfDdnames() {
		return this.sumOfDdnames;
	}

	public void setSumOfDdnames(String sumOfDdnames) {
		this.sumOfDdnames = sumOfDdnames;
	}

	@Column(name = "availability_at_hq", length = 20)
	public String getAvailabilityAtHq() {
		return this.availabilityAtHq;
	}

	public void setAvailabilityAtHq(String availabilityAtHq) {
		this.availabilityAtHq = availabilityAtHq;
	}

	@Column(name = "Transaction_No_DD_TO_RT")
	public Long getTransactionNoDdToRt() {
		return this.transactionNoDdToRt;
	}

	public void setTransactionNoDdToRt(Long transactionNoDdToRt) {
		this.transactionNoDdToRt = transactionNoDdToRt;
	}

	@Column(name = "periodic_Rest")
	public Integer getPeriodicRest() {
		return this.periodicRest;
	}

	public void setPeriodicRest(Integer periodicRest) {
		this.periodicRest = periodicRest;
	}

	@Column(name = "periodic_Rest_Availability")
	public Integer getPeriodicRestAvailability() {
		return this.periodicRestAvailability;
	}

	public void setPeriodicRestAvailability(Integer periodicRestAvailability) {
		this.periodicRestAvailability = periodicRestAvailability;
	}

	@Column(name = "No_of_day_with_Periodic_Rest")
	public Integer getNoOfDayWithPeriodicRest() {
		return this.noOfDayWithPeriodicRest;
	}

	public void setNoOfDayWithPeriodicRest(Integer noOfDayWithPeriodicRest) {
		this.noOfDayWithPeriodicRest = noOfDayWithPeriodicRest;
	}

	@Column(name = "Transaction_No_CrewLink", length = 20)
	public String getTransactionNoCrewLink() {
		return this.transactionNoCrewLink;
	}

	public void setTransactionNoCrewLink(String transactionNoCrewLink) {
		this.transactionNoCrewLink = transactionNoCrewLink;
	}

	@Column(name = "availability", length = 20)
	public String getAvailability() {
		return this.availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "signon_time", length = 0)
	public Date getSignonTime() {
		return this.signonTime;
	}

	public void setSignonTime(Date signonTime) {
		this.signonTime = signonTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "signoff_time", length = 0)
	public Date getSignoffTime() {
		return this.signoffTime;
	}

	public void setSignoffTime(Date signoffTime) {
		this.signoffTime = signoffTime;
	}

	@Column(name = "status")
	public Boolean getStatus() {
		return this.status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	@Column(name = "rr_hqRest", length = 20)
	public String getRrHqRest() {
		return this.rrHqRest;
	}

	public void setRrHqRest(String rrHqRest) {
		this.rrHqRest = rrHqRest;
	}

	@Column(name = "rr_osRest", length = 20)
	public String getRrOsRest() {
		return this.rrOsRest;
	}

	public void setRrOsRest(String rrOsRest) {
		this.rrOsRest = rrOsRest;
	}

	
}
