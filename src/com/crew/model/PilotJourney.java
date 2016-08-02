package com.crew.model;
// default package
// Generated Jul 7, 2015 1:03:32 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.crew.model.Customer;

/**
 * PilotJourney generated by hbm2java
 */
@Entity
@Table(name = "pilot_journey", catalog = "crewdb")
public class PilotJourney implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	private Long id;
	private Customer customer;
	private String sourceOfTravel;
	private String fromStation;
	private String toStation;
	private String departureTime;
	private String arrivalTime;
	private String sequenceId;
	private Long km;
	private String prefix;
	private Long transactionNoDsToDd;
	private String trainNo;

	public PilotJourney() {
	}

	public PilotJourney(Customer customer, String sourceOfTravel,
			String fromStation, String toStation, String departureTime,
			String arrivalTime, String sequenceId, Long km, String prefix,
			Long transactionNoDsToDd, String trainNo) {
		this.customer = customer;
		this.sourceOfTravel = sourceOfTravel;
		this.fromStation = fromStation;
		this.toStation = toStation;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.sequenceId = sequenceId;
		this.km = km;
		this.prefix = prefix;
		this.transactionNoDsToDd = transactionNoDsToDd;
		this.trainNo = trainNo;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_table_fk")
	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Column(name = "source_of_travel", length = 7)
	public String getSourceOfTravel() {
		return this.sourceOfTravel;
	}

	public void setSourceOfTravel(String sourceOfTravel) {
		this.sourceOfTravel = sourceOfTravel;
	}

	@Column(name = "From_Station", length = 10)
	public String getFromStation() {
		return this.fromStation;
	}

	public void setFromStation(String fromStation) {
		this.fromStation = fromStation;
	}

	@Column(name = "To_Station", length = 10)
	public String getToStation() {
		return this.toStation;
	}

	public void setToStation(String toStation) {
		this.toStation = toStation;
	}

	@Column(name = "Departure_Time", length = 10)
	public String getDepartureTime() {
		return this.departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	@Column(name = "Arrival_Time", length = 10)
	public String getArrivalTime() {
		return this.arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	@Column(name = "sequence_id", length = 20)
	public String getSequenceId() {
		return this.sequenceId;
	}

	public void setSequenceId(String sequenceId) {
		this.sequenceId = sequenceId;
	}

	@Column(name = "km")
	public Long getKm() {
		return this.km;
	}

	public void setKm(Long km) {
		this.km = km;
	}

	@Column(name = "prefix", length = 5)
	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Column(name = "transaction_No_DS_TO_DD")
	public Long getTransactionNoDsToDd() {
		return this.transactionNoDsToDd;
	}

	public void setTransactionNoDsToDd(Long transactionNoDsToDd) {
		this.transactionNoDsToDd = transactionNoDsToDd;
	}

	@Column(name = "Train No", length = 7)
	public String getTrainNo() {
		return this.trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}
	
	public void clearfields()
	{	sourceOfTravel = null;
		arrivalTime = null;
		departureTime = null;
		fromStation = null;
		toStation = null;
		km = null;
	}

}
