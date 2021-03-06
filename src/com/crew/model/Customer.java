package com.crew.model;
// default package
// Generated Jul 9, 2015 4:05:32 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Customer generated by hbm2java
 */
@Entity
@Table(name = "customer", catalog = "crewdb")
public class Customer implements java.io.Serializable {

	private Long id;
	private String firstname;
	private String middlename;
	private String lastname;
	private String username;
	private String password;
	private String emailAddress;
	private String dob;
	private String userRole;
	private String city;
	private String states;
	private String address;
	private String zones;
	private Boolean account;
	private Set<CrewLink> crewLinks = new HashSet<CrewLink>(0);
	private Set<CrewChangeDrivingsection> crewChangeDrivingsections = new HashSet<CrewChangeDrivingsection>(
			0);
	private Set<PilotJourney> pilotJourneys = new HashSet<PilotJourney>(0);
	private Set<RoundTrip> roundTrips = new HashSet<RoundTrip>(0);
	private Set<DrivingDutyOfPilotDrivingSection> drivingDutyOfPilotDrivingSections = new HashSet<DrivingDutyOfPilotDrivingSection>(
			0);
	private Set<DrivingDutyOfPilotDrivingSection> drivingDutyOfPilotDrivingSections_1 = new HashSet<DrivingDutyOfPilotDrivingSection>(
			0);
	private Set<CrewChangeDrivingsection> crewChangeDrivingsections_1 = new HashSet<CrewChangeDrivingsection>(
			0);
	private Set<PilotJourney> pilotJourneys_1 = new HashSet<PilotJourney>(0);
	private Set<RoundTrip> roundTrips_1 = new HashSet<RoundTrip>(0);

	public Customer() {
	}

	public Customer(
			String firstname,
			String middlename,
			String lastname,
			String username,
			String password,
			String emailAddress,
			String dob,
			String userRole,
			String city,
			String states,
			String address,
			String zones,
			Boolean account,
			Set<CrewLink> crewLinks,
			Set<CrewChangeDrivingsection> crewChangeDrivingsections,
			Set<PilotJourney> pilotJourneys,
			Set<RoundTrip> roundTrips,
			Set<DrivingDutyOfPilotDrivingSection> drivingDutyOfPilotDrivingSections,
			Set<DrivingDutyOfPilotDrivingSection> drivingDutyOfPilotDrivingSections_1,
			Set<CrewChangeDrivingsection> crewChangeDrivingsections_1,
			Set<PilotJourney> pilotJourneys_1, Set<RoundTrip> roundTrips_1) {
		this.firstname = firstname;
		this.middlename = middlename;
		this.lastname = lastname;
		this.username = username;
		this.password = password;
		this.emailAddress = emailAddress;
		this.dob = dob;
		this.userRole = userRole;
		this.city = city;
		this.states = states;
		this.address = address;
		this.zones = zones;
		this.account = account;
		this.crewLinks = crewLinks;
		this.crewChangeDrivingsections = crewChangeDrivingsections;
		this.pilotJourneys = pilotJourneys;
		this.roundTrips = roundTrips;
		this.drivingDutyOfPilotDrivingSections = drivingDutyOfPilotDrivingSections;
		this.drivingDutyOfPilotDrivingSections_1 = drivingDutyOfPilotDrivingSections_1;
		this.crewChangeDrivingsections_1 = crewChangeDrivingsections_1;
		this.pilotJourneys_1 = pilotJourneys_1;
		this.roundTrips_1 = roundTrips_1;
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

	@Column(name = "firstname", length = 50)
	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@Column(name = "middlename", length = 50)
	public String getMiddlename() {
		return this.middlename;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	@Column(name = "lastname", length = 50)
	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@Column(name = "username", length = 20)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", length = 20)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "EmailAddress", length = 40)
	public String getEmailAddress() {
		return this.emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@Column(name = "dob", length = 20)
	public String getDob() {
		return this.dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	@Column(name = "user_role", length = 20)
	public String getUserRole() {
		return this.userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	@Column(name = "city", length = 20)
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "states", length = 20)
	public String getStates() {
		return this.states;
	}

	public void setStates(String states) {
		this.states = states;
	}

	@Column(name = "address", length = 500)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "zones", length = 500)
	public String getZones() {
		return this.zones;
	}

	public void setZones(String zones) {
		this.zones = zones;
	}

	@Column(name = "account")
	public Boolean getAccount() {
		return this.account;
	}

	public void setAccount(Boolean account) {
		this.account = account;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
	public Set<CrewLink> getCrewLinks() {
		return this.crewLinks;
	}

	public void setCrewLinks(Set<CrewLink> crewLinks) {
		this.crewLinks = crewLinks;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
	public Set<CrewChangeDrivingsection> getCrewChangeDrivingsections() {
		return this.crewChangeDrivingsections;
	}

	public void setCrewChangeDrivingsections(
			Set<CrewChangeDrivingsection> crewChangeDrivingsections) {
		this.crewChangeDrivingsections = crewChangeDrivingsections;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
	public Set<PilotJourney> getPilotJourneys() {
		return this.pilotJourneys;
	}

	public void setPilotJourneys(Set<PilotJourney> pilotJourneys) {
		this.pilotJourneys = pilotJourneys;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
	public Set<RoundTrip> getRoundTrips() {
		return this.roundTrips;
	}

	public void setRoundTrips(Set<RoundTrip> roundTrips) {
		this.roundTrips = roundTrips;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
	public Set<DrivingDutyOfPilotDrivingSection> getDrivingDutyOfPilotDrivingSections() {
		return this.drivingDutyOfPilotDrivingSections;
	}

	public void setDrivingDutyOfPilotDrivingSections(
			Set<DrivingDutyOfPilotDrivingSection> drivingDutyOfPilotDrivingSections) {
		this.drivingDutyOfPilotDrivingSections = drivingDutyOfPilotDrivingSections;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
	public Set<DrivingDutyOfPilotDrivingSection> getDrivingDutyOfPilotDrivingSections_1() {
		return this.drivingDutyOfPilotDrivingSections_1;
	}

	public void setDrivingDutyOfPilotDrivingSections_1(
			Set<DrivingDutyOfPilotDrivingSection> drivingDutyOfPilotDrivingSections_1) {
		this.drivingDutyOfPilotDrivingSections_1 = drivingDutyOfPilotDrivingSections_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
	public Set<CrewChangeDrivingsection> getCrewChangeDrivingsections_1() {
		return this.crewChangeDrivingsections_1;
	}

	public void setCrewChangeDrivingsections_1(
			Set<CrewChangeDrivingsection> crewChangeDrivingsections_1) {
		this.crewChangeDrivingsections_1 = crewChangeDrivingsections_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
	public Set<PilotJourney> getPilotJourneys_1() {
		return this.pilotJourneys_1;
	}

	public void setPilotJourneys_1(Set<PilotJourney> pilotJourneys_1) {
		this.pilotJourneys_1 = pilotJourneys_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
	public Set<RoundTrip> getRoundTrips_1() {
		return this.roundTrips_1;
	}

	public void setRoundTrips_1(Set<RoundTrip> roundTrips_1) {
		this.roundTrips_1 = roundTrips_1;
	}

}
