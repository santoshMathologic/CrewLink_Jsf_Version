package com.crew.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.junit.runner.Request;
import org.primefaces.context.RequestContext;
import org.primefaces.event.data.PageEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.crew.exception.CrewException;
import com.crew.model.CrewlinkSuggestion;
import com.crew.model.Customer;
import com.crew.model.DrivingDutyOfPilotDrivingSection;
import com.crew.model.HqOsRest;
import com.crew.model.RoundTrip;
import com.crew.model.TransactionIdDdRr;
import com.crew.service.CrewService;
import com.crew.util.CrewUtilities;

@ManagedBean(name = "drivingdutytoroundtrip", eager = true)
@ViewScoped
public class DrivingDutyToRoundTrip implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "schoolService", value = "#{schoolService}")
	private CrewService schoolService;
	private HttpSession httpSession = (HttpSession) FacesContext
			.getCurrentInstance().getExternalContext().getSession(false);
	private static final Logger CREW_LOGGER = Logger
			.getLogger(adminDrivingduty.class);

	private ApplicationContext context;

	private String drivingsectionsequence;
	private String pilotjourneysequence;

	private String[] dsectionarray = new String[100];
	private Map<String, Long> mapSeqNoPlusId = new LinkedHashMap<String, Long>();
	private int count = 0;

	private RoundTrip roundTrip;
	private TransactionIdDdRr transactionFromDdtoRr;
	private DrivingDutyOfPilotDrivingSection selectedDrivingDuty;
	private List<RoundTrip> listRoundTrip;
	private RoundTrip selectedRoundTrip;
	private List<DrivingDutyOfPilotDrivingSection> dutyDrivingList;
	// //
	private List<DrivingDutyOfPilotDrivingSection> selectionList;
	private String statusBoolean;
	private List<SelectItem> roundtripSelectItemFrom;
	private List<SelectItem> roundtripSelectItemTo;
	private String roundTripFrom;
	private String roundTripTo;
	private String roundTripFrom1;
	private String roundTripTo1;
	private long loginId;
	private Map<String,String> mapSuggestion = new LinkedHashMap<>();

	@PostConstruct
	private void initilization() {
		if (!(httpSession.getAttribute("COMMONLOGINID") == null)) {
			loginId = (Long) httpSession.getAttribute("COMMONLOGINID");
			createList();
			loadList();
			addedroundTripSelectedItems();
		} else {
			try {
				FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.redirect(
								CrewUtilities.getRedirectContext()
										+ "/login.xhtml");
			} catch (Exception e) {
				CREW_LOGGER.debug(e.getMessage());
			}
		}

	}

	@SuppressWarnings("unchecked")
	private void createList() {

		context = new ClassPathXmlApplicationContext("Beans.xml");
		roundTrip = (RoundTrip) context.getBean("roundtrip");
		transactionFromDdtoRr = (TransactionIdDdRr) context
				.getBean("transactionIdDdRr");
		listRoundTrip = (List<RoundTrip>) context.getBean("list");
		dutyDrivingList = (List<DrivingDutyOfPilotDrivingSection>) context
				.getBean("list");
		roundTrip = (RoundTrip) context.getBean("roundtrip");

		roundtripSelectItemFrom = new ArrayList<SelectItem>();
		roundtripSelectItemTo = new ArrayList<SelectItem>();
	}

	@SuppressWarnings("unchecked")
	public void loadList() {
		try {
			listRoundTrip = (List<RoundTrip>) schoolService
					.loadList("from RoundTrip as rr where rr.customer.id="
							+ loginId);
		} catch (CrewException e) {
			e.printStackTrace();
		}
	}

	public void addedroundTripSelectedItems() {
		List<RoundTrip> roundTripList = new ArrayList<RoundTrip>();
		HashMap<String, String> to = new HashMap<String, String>();
		HashMap<String, String> from = new HashMap<String, String>();

		try {
			roundTripList = (List<RoundTrip>) schoolService
					.loadList("from RoundTrip");
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
		}

		for (RoundTrip r : roundTripList) {
			to.put(r.getToStation(), r.getToStation());
			from.put(r.getFromStation(), r.getFromStation());
		}

		for (Map.Entry<String, String> mapfrom : from.entrySet()) {
			roundtripSelectItemFrom.add(new SelectItem(mapfrom.getKey()));
		}

		for (Map.Entry<String, String> mapto : to.entrySet()) {
			roundtripSelectItemTo.add(new SelectItem(mapto.getKey()));
		}

	}

	public void roundTripMultipleSearch() {
		if (this.roundTripFrom != null || this.roundTripTo != null
				|| this.roundTripFrom1 != null || this.roundTripTo1 != null) {
			if (roundTripFrom != null && !roundTripFrom.isEmpty()
					&& roundTripTo != null && !roundTripTo.isEmpty()
					&& roundTripFrom1 != null && !roundTripFrom1.isEmpty()
					&& roundTripTo1 != null && !roundTripTo1.isEmpty()) {
				listRoundTrip = (List<RoundTrip>) schoolService
						.loadRoundTripSearchList(roundTripFrom, roundTripTo,
								roundTripFrom1, roundTripTo1);
			} else if (roundTripFrom != null && !roundTripFrom.isEmpty()
					&& roundTripTo != null && !roundTripTo.isEmpty()) {
				listRoundTrip = (List<RoundTrip>) schoolService
						.loadRoundTripSearchListTwoBox(roundTripFrom,
								roundTripTo, "UPPER");
			} else if (roundTripFrom1 != null && !roundTripFrom1.isEmpty()
					&& roundTripTo1 != null && !roundTripTo1.isEmpty()) {
				listRoundTrip = (List<RoundTrip>) schoolService
						.loadRoundTripSearchListTwoBox(roundTripFrom1,
								roundTripTo1, "LOWER");
			} else if (!roundTripFrom.isEmpty()) {
				listRoundTrip = (List<RoundTrip>) schoolService
						.roundTripsingleSearch(roundTripFrom, "FROM");
			} else if (!roundTripTo.isEmpty()) {
				listRoundTrip = (List<RoundTrip>) schoolService
						.roundTripsingleSearch(roundTripTo, "TO");
			} else if (!roundTripFrom1.isEmpty()) {
				listRoundTrip = (List<RoundTrip>) schoolService
						.roundTripsingleSearch(roundTripTo, "FROM1");
			} else if (!roundTripTo1.isEmpty()) {
				listRoundTrip = (List<RoundTrip>) schoolService
						.roundTripsingleSearch(roundTripTo1, "TO1");
			}

		}

	}

	@SuppressWarnings("unchecked")
	private Long generateTransactionIdFromDrivingDutyToRoundTrip() {
		Long lTransactionId = new Long(0);

		List<TransactionIdDdRr> listTransactionIdGenerator = (List<TransactionIdDdRr>) context
				.getBean("list");
		try {
			listTransactionIdGenerator = (List<TransactionIdDdRr>) schoolService
					.loadList("from TransactionIdDdRr");

			Long l1 = new Long(999);
			//System.out.println("value of l1 is !!!!!!!" + l1);
			if (listTransactionIdGenerator.isEmpty()) {
				transactionFromDdtoRr.setTransactionIdDdRr(l1);
				schoolService.saveInDb(transactionFromDdtoRr);

			} else {
				List<Long> listTransactioIds = new LinkedList<Long>();
				transactionFromDdtoRr.setTransactionIdDdRr(l1);
				for (TransactionIdDdRr t : listTransactionIdGenerator) {
					listTransactioIds.add(t.getTransactionIdDdRr());
				}
				Collections.sort(listTransactioIds);

				for (int i = 0; i < listTransactioIds.size(); i++) {
					if (i == listTransactioIds.size() - 1) {
						lTransactionId = listTransactioIds.get(i);
					}
				}
				//System.out.println("lTransactionId-> "+ transactionFromDdtoRr.getTransactionIdDdRr());
			}

		} catch (CrewException e) {
			e.printStackTrace();
		}
		return lTransactionId;
	}

	public void saveInMap(Long id) {
		System.out.println("Sequence Ids = =" + drivingsectionsequence);
		dsectionarray[count] = drivingsectionsequence;
		mapSeqNoPlusId.put(drivingsectionsequence, id);
		count++;

	}

	// calculation of resting at hq ((RRTIME AT HEADQUARTER)

	public Integer calculateAvailableAtHQ(Date dtDepart, Date dtArr,
			String strAvailability) {
		
		System.out.println("---------Calculate Availablity at HeadQuarter---------");
		Integer iDiffInMinutes = new Integer(0);
		Integer iTotalMinutes = new Integer(0);
		Calendar calDeparture = Calendar.getInstance();
		Calendar calAvailable = Calendar.getInstance();
		Calendar calArrival = Calendar.getInstance();
		calDeparture.setTime(dtDepart);
		calArrival.setTime(dtArr);

		Integer iDepHour = calDeparture.get(Calendar.HOUR_OF_DAY);
		Integer iDepMinute = calDeparture.get(Calendar.MINUTE);

		Integer iArrHour = calArrival.get(Calendar.HOUR_OF_DAY);
		Integer iArrMinute = calArrival.get(Calendar.MINUTE);

		if (iDepHour == 0)iDepHour = 24;
		if (iArrHour == 0)iArrHour = 24;

	
		
		
		
		if (iDepHour < iArrHour) 
		{   
			System.out.println("-------if Dep<arr--------");
			iDiffInMinutes = (iArrHour * 60 + iArrMinute)- (iDepHour * 60 + iDepMinute);
			iTotalMinutes = iTotalMinutes + iDiffInMinutes;
			System.out.println("ITotal Minutes = ="+iTotalMinutes);
			System.out.println("-------End--------");
			
		} else 
		{  
			System.out.println("-------else Dep>arr--------");
			iDiffInMinutes = (iArrHour * 60 + iArrMinute) + 24 * 60- (iDepHour * 60 + iDepMinute);
		    iTotalMinutes = iTotalMinutes + iDiffInMinutes;
		    
		    System.out.println("ITotal Minutes = ="+iTotalMinutes);
			System.out.println("-------End--------");
	    }

		String strHourAndMM[] = strAvailability.split(Pattern.quote(":"));

		String strHours = new String();
		String strMinu = new String();
		for (int l = 0; l < strHourAndMM.length - 1; l++) {
			strHours = strHourAndMM[l];
			strMinu = strHourAndMM[l + 1];
		}

		
		System.out.println("Departure Hour  = ="+iDepHour);
		System.out.println("Departure Minutes = ="+iDepMinute);
		
		System.out.println("Arrival Hour  = ="+iArrHour);
		System.out.println("Arrival Minutes = ="+iArrMinute);
		
		System.out.println("Avail Hour  = ="+strHours);
		System.out.println("Avail Minutes = ="+strMinu);
		
		

		Date dtAvailable = dtArr;
		calAvailable.setTime(dtAvailable);
		calAvailable.set(Calendar.HOUR_OF_DAY, Integer.parseInt(strHours));
		calAvailable.set(Calendar.MINUTE, Integer.parseInt(strMinu));
		Integer iAvailHour = calAvailable.get(Calendar.HOUR_OF_DAY);
		Integer iAvailMinute = calAvailable.get(Calendar.MINUTE);

		if (iArrHour < iAvailHour) 
		{	iDiffInMinutes = (iAvailHour * 60 + iAvailMinute)- (iArrHour * 60 + iArrMinute);
			iTotalMinutes = iTotalMinutes + iDiffInMinutes;
		}else{
			iDiffInMinutes = (iAvailHour * 60 + iAvailMinute) + 24 * 60- (iArrHour * 60 + iArrMinute);
			iTotalMinutes = iTotalMinutes + iDiffInMinutes;
		}
		
		System.out.println("-----------------------End -----------------------------");
       return iTotalMinutes;
	}

	public Integer calculateDifferenceBetweenAvailAndDeparture(String strAvail,Date dtDeparture) {
		System.out.println("---------Calculate Difference b/w Availablity & Departure---------");
		
		String strHourAndMM[] = strAvail.split(Pattern.quote(":"));
		Integer iDiffInMinutes = new Integer(0);
		Integer iTotalMinutes = new Integer(0);
		String strHours = new String();
		String strMinu = new String();
		for (int l = 0; l < strHourAndMM.length - 1; l++) {
			strHours = strHourAndMM[l];
			strMinu = strHourAndMM[l + 1];
		}

		Date dtAvai = dtDeparture;
		Calendar calAvail = Calendar.getInstance();
		calAvail.setTime(dtAvai);
		calAvail.set(Calendar.HOUR_OF_DAY, Integer.parseInt(strHours));
		calAvail.set(Calendar.MINUTE, Integer.parseInt(strMinu));

		Calendar calDeparture = Calendar.getInstance();
		calDeparture.setTime(dtDeparture);

		Integer iDepHour = calDeparture.get(Calendar.HOUR_OF_DAY);
		Integer iDepMinute = calDeparture.get(Calendar.MINUTE);

		Integer iArrHour = calAvail.get(Calendar.HOUR_OF_DAY);
		Integer iArrMinute = calAvail.get(Calendar.MINUTE);

		if (iDepHour == 0)iDepHour = 24;
		if (iArrHour == 0)
			iArrHour = 24;
		
		if (iArrHour < iDepHour) {
		iDiffInMinutes = (iDepHour * 60 + iDepMinute)- (iArrHour * 60 + iArrMinute);
		iTotalMinutes = iTotalMinutes + iDiffInMinutes;
			
		} else {
			
			iDiffInMinutes = (iDepHour * 60 + iDepMinute) + 24 * 60- (iArrHour * 60 + iArrMinute);
			iTotalMinutes = iTotalMinutes + iDiffInMinutes;
			
		}
		
		System.out.println("---------------End-------------------------------");
		return iDiffInMinutes;
	}

	
	
	public Integer calculateDifferenceBetweenSignOffAndAvailability(
			Date dtDeparture,String strAvail) {
		
		System.out.println("---------Calculate Difference b/w SignOff & Availablity---------");
		
		String strHourAndMM[] = strAvail.split(Pattern.quote(":"));
		Integer iDiffInMinutes = new Integer(0);
		Integer iTotalMinutes = new Integer(0);
		String strHours = new String();
		String strMinu = new String();
		for (int l = 0; l < strHourAndMM.length - 1; l++) {
			strHours = strHourAndMM[l];
			strMinu = strHourAndMM[l + 1];
		}

		Date dtAvai = dtDeparture;
		Calendar calAvail = Calendar.getInstance();
		calAvail.setTime(dtAvai);
		calAvail.set(Calendar.HOUR_OF_DAY, Integer.parseInt(strHours));
		calAvail.set(Calendar.MINUTE, Integer.parseInt(strMinu));

		Calendar calDeparture = Calendar.getInstance();
		calDeparture.setTime(dtDeparture);

		Integer iDepHour = calDeparture.get(Calendar.HOUR_OF_DAY);
		Integer iDepMinute = calDeparture.get(Calendar.MINUTE);

		Integer iArrHour = calAvail.get(Calendar.HOUR_OF_DAY);
		Integer iArrMinute = calAvail.get(Calendar.MINUTE);
		
		
	
		System.out.println("Dep Hour  = "+iDepHour);
		System.out.println("Dep Minutes  = "+iDepMinute);
		System.out.println("Arrival Hour  = "+iArrHour);
		System.out.println("Arrival Minutes  = "+iArrMinute);
		


		if (iDepHour == 0)
			iDepHour = 24;
		if (iArrHour == 0)
			iArrHour = 24;
		
		if (iDepHour < iArrHour) {
			
			iDiffInMinutes = (iArrHour * 60 + iArrMinute) - (iDepHour * 60 + iDepMinute);
			iTotalMinutes = iTotalMinutes + iDiffInMinutes;
		
		} else {
			

			iDiffInMinutes = (iArrHour * 60 + iArrMinute) + 24 * 60
					- (iDepHour * 60 + iDepMinute) ;
			iTotalMinutes = iTotalMinutes + iDiffInMinutes;
			
		}
		System.out.println("----------------End-------------------");
		return iDiffInMinutes;
	}

	
	
	
	
	
	public void updatedComponentRoundTrips(PageEvent event)
	{
		int varPage = event.getPage();
		System.out.println("pages"+varPage);
		if(varPage==0)
		{
			drivingsectionsequence  = null;
			//RequestContext.getCurrentInstance().update("viewform:sequenceinput");
		}else if(varPage==1)
		{
			drivingsectionsequence  = null;
		}else if(varPage==2)
		{
			drivingsectionsequence  = null;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	public String generateRoundTrips() {

		
		System.out.println("The value of the count is " +count);		
		if(count==0)
		{
			RequestContext.getCurrentInstance().execute("PF('errorRRWdgt').show();");
		}
		else
		{
			List<Integer> lsHQRest = new LinkedList<Integer>();
			List<Integer> lsOSRest = new LinkedList<Integer>();
			
			Integer nTotalHQRest = new Integer(0);
			Integer nTotalOSRest = new Integer(0);
			
			Integer nTotalHQ[] = new Integer[100];
			Integer nTotalOS[] = new Integer[100];
			int count111 = 0;
			
			String strCrewBase = new String();
			String strCrewType = new String();
			String strAvailable = new String();
			String strFromStation = new String();
			Date dtDepartureTime = new Date();
			Long lDistance[] = new Long[100];
			Date dtArrivalTime = new Date();
			String strLastStation = new String();
			String strAllTrainNames = new String();
			Integer iTotalHours[] = new Integer[100];
	
			DrivingDutyOfPilotDrivingSection ddpt = (DrivingDutyOfPilotDrivingSection) context
					.getBean("drivingdutyofpilotdrivingsection");
			List<Integer> listSequence = new ArrayList<Integer>();
			

		System.out.println("---------------------Before Sorting----------------------------------------");
		for (int k = 0; k < count; k++) {
			System.out.println(dsectionarray[k]);
		}
		System.out.println("---------------------End------------------------------------------------------");
		for (int k = 0; k < count; k++) {
			listSequence.add(Integer.parseInt(dsectionarray[k]));
		}


		Collections.sort(listSequence);
		System.out.println("------------------------After Sorting-------------------------------------");
		for (Integer i : listSequence) {
			System.out.println(" "+i);
		}
		System.out.println("-----------------------------End----------------------------------------------");

		List<Integer> listHours = new LinkedList<Integer>();
		List<Integer> listMinutes = new LinkedList<Integer>();
		int count2 = 0;
		int count3 = 0;
		int count4 = 0;
		Date dtDepart22[] = new Date[100];
		Date dtDeparture2 = new Date();
		String strAvailabl2 = new String();
		String strAvailable22[] = new String[100];
		
		Integer iTotalHours2 = new Integer(0);
	
		Date dtSignOff = new Date();
		Date dtSignOn = new Date();
		
		for (int i = 0; i < count; i++) {
			
			System.out.println("Inside for Loop and Count ="+count);
			System.out.println("Elements ==" + listSequence.get(i)+"Map=="+mapSeqNoPlusId.get(String.valueOf(listSequence.get(i))));
			
			try {
				ddpt = (DrivingDutyOfPilotDrivingSection) schoolService
						.loadUniqueObject("from DrivingDutyOfPilotDrivingSection as dd where dd.id="
								+ mapSeqNoPlusId.get(String
										.valueOf(listSequence.get(i))));
				
				nTotalHQRest = nTotalHQRest + Integer.parseInt(ddpt.getHqRest());
				nTotalOSRest = nTotalOSRest + Integer.parseInt(ddpt.getOsRest());
				nTotalHQ[count111] = Integer.parseInt(ddpt.getHqRest());
				nTotalOS[count111] = Integer.parseInt(ddpt.getOsRest());
				count111++;
			
			} catch (CrewException e) {
				e.printStackTrace();
			}
			if (count == 1) {
				System.out.println("Count ==1 "+count);
				try {
					ddpt = (DrivingDutyOfPilotDrivingSection) schoolService
							.loadUniqueObject("from DrivingDutyOfPilotDrivingSection as dd where dd.id="
									+ mapSeqNoPlusId.get(String
											.valueOf(listSequence.get(i))));
					strFromStation = ddpt.getFromStation();
					strLastStation = ddpt.getToStation();
					lDistance[i] = ddpt.getKm();
					strAvailable = ddpt.getAvailability();
					strAllTrainNames = ddpt.getTrainNo();
					dtArrivalTime = ddpt.getArrival();
					dtDepartureTime = ddpt.getDeparture();
					dtSignOn = ddpt.getSignonTime();
					dtSignOff = ddpt.getSignoffTime();
					strCrewBase = ddpt.getCrewBase();
					strCrewType = ddpt.getCrewType();
					iTotalHours[count4] = calculateAvailableAtHQ(ddpt.getSignonTime(), ddpt.getSignoffTime(),ddpt.getAvailability());
					
					System.out.println("Total Hours"+iTotalHours[count4]);
					
					count4++;
					break;
				
				} catch (CrewException e) {
					e.printStackTrace();
				}

			}
			if (i >= 1) {
				System.out.println("i>=1");
				try {
					ddpt = (DrivingDutyOfPilotDrivingSection) schoolService
							.loadUniqueObject("from DrivingDutyOfPilotDrivingSection as dd where dd.id="
									+ mapSeqNoPlusId.get(String
											.valueOf(listSequence.get(i))));
					
					
					
				
					
					
					
					strAllTrainNames = strAllTrainNames.concat(","
							+ ddpt.getTrainNo());
					dtDeparture2 = ddpt.getSignonTime();
					dtDepart22[count2] = dtDeparture2;
					count2++;
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (i == 0) {
				System.out.println("i==0  "+i);
				Long startId = mapSeqNoPlusId.get(String.valueOf(listSequence
						.get(i)));
				
				System.out.println("StartId"+startId);
				try {
				
					ddpt = (DrivingDutyOfPilotDrivingSection) schoolService
							.loadUniqueObject("from DrivingDutyOfPilotDrivingSection as dd where dd.id="
									+ startId);
					strFromStation = ddpt.getFromStation();
					strAllTrainNames = ddpt.getTrainNo();
					
					dtDepartureTime = ddpt.getDeparture();
					Calendar cal2 = Calendar.getInstance();
					cal2.setTime(dtDepartureTime);
					listHours.add(cal2.get(Calendar.HOUR_OF_DAY));
					listMinutes.add(cal2.get(Calendar.MINUTE));
					lDistance[i] = ddpt.getKm();
					dtSignOn = ddpt.getSignonTime();

					strCrewBase = ddpt.getCrewBase();
					strCrewType = ddpt.getCrewType();

					
			
				} catch (CrewException e) {
					e.printStackTrace();
				}
			}
			if (i == count - 1) {
				System.out.println("i==count-1");
				
				Long lastId = mapSeqNoPlusId.get(String.valueOf(listSequence.get(i)));
				try {
					ddpt = (DrivingDutyOfPilotDrivingSection) schoolService
							.loadUniqueObject("from DrivingDutyOfPilotDrivingSection as dd where dd.id="
									+ lastId);
					strLastStation = ddpt.getToStation();
					String toStation = ddpt.getToStation();
					dtArrivalTime = ddpt.getArrival();
					dtSignOff = ddpt.getSignoffTime();
					Calendar cal2 = Calendar.getInstance();
					cal2.setTime(ddpt.getDeparture());
					listHours.add(cal2.get(Calendar.HOUR_OF_DAY));
					listMinutes.add(cal2.get(Calendar.MINUTE));
					lDistance[i] = ddpt.getKm();
					

					strAvailable = ddpt.getAvailability();
					
					

				
				} catch (CrewException e) {
					e.printStackTrace();
				}
			} else {
				
				try {
					ddpt = (DrivingDutyOfPilotDrivingSection) schoolService
							.loadUniqueObject("from DrivingDutyOfPilotDrivingSection as dd where dd.id="
									+ mapSeqNoPlusId.get(String
											.valueOf(listSequence.get(i))));
					lDistance[i] = ddpt.getKm();
										
					Calendar cal2 = Calendar.getInstance();
					cal2.setTime(ddpt.getDeparture());
					listHours.add(cal2.get(Calendar.HOUR_OF_DAY));
					listMinutes.add(cal2.get(Calendar.MINUTE));
					
						
					
				} catch (CrewException e) {
					e.printStackTrace();
				}

			}

			if (i < count - 1) {
				try {
					ddpt = (DrivingDutyOfPilotDrivingSection) schoolService
							.loadUniqueObject("from DrivingDutyOfPilotDrivingSection as dd where dd.id="
									+ mapSeqNoPlusId.get(String
											.valueOf(listSequence.get(i))));

					strAvailabl2 = ddpt.getAvailability();

					strAvailable22[count3] = strAvailabl2;
					count3++;
				
					dtSignOff = ddpt.getSignoffTime();
					
				} catch (CrewException e) {
					e.printStackTrace();
				}
			}
			try {
				ddpt = (DrivingDutyOfPilotDrivingSection) schoolService
						.loadUniqueObject("from DrivingDutyOfPilotDrivingSection as dd where dd.id="
								+ mapSeqNoPlusId.get(String
										.valueOf(listSequence.get(i))));

				
				dtSignOff = ddpt.getSignoffTime();
				
			
				if(ddpt.getCrewBase().equalsIgnoreCase(ddpt.getToStation()))
				{
					nTotalHQRest = calculateDifferenceBetweenSignOffAndAvailability(ddpt.getSignoffTime(),ddpt.getAvailability());
					lsHQRest.add(nTotalHQRest);
					nTotalHQRest = 0;
				
					
				}
				else
				{
					
					nTotalOSRest = calculateDifferenceBetweenSignOffAndAvailability(ddpt.getSignoffTime(),ddpt.getAvailability());
					lsOSRest.add(nTotalOSRest);	
					nTotalOSRest = 0;
					
					
					
				}
						
				
				
			

				iTotalHours[count4] = calculateAvailableAtHQ(
						ddpt.getSignonTime(), ddpt.getSignoffTime(),
						ddpt.getAvailability());
				

				count4++;
				
				// calDiffBetweenSignOnAndSignOff(dtSignOn, dtSignOff);
			} catch (CrewException e) {
				e.printStackTrace();
			}

		}
		
		if (count > 1) 
		{	for (int j = 0; j < count3; j++) {
				iTotalHours[count4] = calculateDifferenceBetweenAvailAndDeparture(strAvailable22[j], dtDepart22[j]);
				count4++;
			}
		}

		
		for (int j = 0; j < count4; j++) {
			iTotalHours2 = iTotalHours2 + iTotalHours[j];
			
		}

		for(Integer i: lsHQRest)
		{
			//System.out.println(i);
			HqOsRest ho1 = (HqOsRest) context.getBean("hqOsRest");
			ho1.setHqRest(i);
			ho1.setOsRest(0);
			try {
				schoolService.saveInDb(ho1);
			} catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
			}
		}
	
		
		for(Integer i: lsOSRest)
		{
			
			HqOsRest ho1 = (HqOsRest) context.getBean("hqOsRest");
			ho1.setHqRest(0);
			ho1.setOsRest(i);
			try {
				schoolService.saveInDb(ho1);
			} catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
			}
		}
		
		
		Long iTotalDistance = new Long(0);
		for (int j = 0; j < count; j++) {
			iTotalDistance = iTotalDistance + lDistance[j];
			
		}
		System.out.println("Total Km =="+iTotalDistance);
		System.out.println("-------------------------------------------------------------");

		// 1. Converting Total Numbers of Minutes to Hours / by 60
		// 2. Converting Total Numbers of Minutes to Hours % by 60
		Integer iNmberOfHours = iTotalHours2 / 60;
		Integer iNumberOfMinutes = iTotalHours2 % 60;
		String strNumberOfDaysInHour = iNmberOfHours.toString().concat(":" + iNumberOfMinutes.toString());
		
		
		
		for(int i = 0 ; i<count111; i++)
		{
			
			nTotalHQRest = nTotalHQRest + nTotalHQ[i];
			nTotalOSRest = nTotalOSRest + nTotalOS[i];
			
		}
		System.out.println("The HQ rest ----------- ******************** " + nTotalHQRest);
		System.out.println("The OS rest ---------------- ***************  " + nTotalOSRest);
		Long lTransID = generateTransactionIdFromDrivingDutyToRoundTrip();
		roundTrip.setTrainNo(strAllTrainNames);
		roundTrip.setFromStation(strFromStation);
		roundTrip.setToStation(strLastStation);
		roundTrip.setDeparturetime(dtDepartureTime);
		roundTrip.setArrivaltime(dtArrivalTime);
		roundTrip.setCrewBase(strCrewBase);
		roundTrip.setCrewType(strCrewType);
		roundTrip.setRrHqRest(nTotalHQRest.toString());
		roundTrip.setRrOsRest(nTotalOSRest.toString());
		roundTrip.setDistance(iTotalDistance);
		roundTrip.setTransactionNoDdToRt(lTransID);
		roundTrip.setSignonTime(dtSignOn);
		roundTrip.setSignoffTime(dtSignOff);
		roundTrip.setAvailabilityAtHq(strAvailable);
		roundTrip.setNoOfDays(strNumberOfDaysInHour);

		// roundTrip.setAvailability(strAvailable);

		try {

			roundTrip.setCustomer((Customer) schoolService
					.loadUniqueObject("from Customer as cst where cst.id="
							+ loginId));
		schoolService.saveInDb(roundTrip); 		// --------------------------------------------------/// Open Save Methods Here

			for (Entry<String, Long> entry : mapSeqNoPlusId.entrySet()) {
				schoolService.updateInDrivingDutyDDRT(entry.getValue(),
						lTransID);
				DrivingDutyOfPilotDrivingSection ddpt1 = (DrivingDutyOfPilotDrivingSection) schoolService
						.loadUniqueObject("from DrivingDutyOfPilotDrivingSection as d where d.id="
								+ entry.getValue());
				ddpt1.setStatus(true);
			schoolService.updateObjectInDb(ddpt1);  		// --------------------------------------------------/// Open Save Methods Here

			
			}
			
		} catch (CrewException e) {
			e.printStackTrace();
		}
		TransactionIdDdRr trddrr = (TransactionIdDdRr) context
				.getBean("transactionIdDdRr");
		Long lTransactionId = generateTransactionIdFromDrivingDutyToRoundTrip();
		lTransactionId++;
		trddrr.setTransactionIdDdRr(lTransactionId);

		
		// --------------------------------------------------/// Open Save Methods Here
		
		try {
			schoolService.saveInDb(trddrr);
		} catch (Exception e) {
			this.CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
		RequestContext.getCurrentInstance().execute("PF('rrStatusDLg').show();");
		}
		return null;
	}

	
	//calculate the difference between availability and signoff
	public void calDiffBetweenAvailabilityAndSignOff(String strAvailable,Date SignOff)
	{
		
		
		
	}
	
	
	public void calculate_days()
	{
		
	}
	
	
	

	@SuppressWarnings("unchecked")
	public void viewRoundTrip(RoundTrip d) {
		System.out
				.println("---------------------------------------------------");
		System.out.println("id is " + d.getId());
		System.out
				.println("---------------------------------------------------");
		Long lTransactionId = new Long(0);

		lTransactionId = d.getTransactionNoDdToRt();
		System.out.println("trans id is " + lTransactionId);
		try {
			dutyDrivingList = (List<DrivingDutyOfPilotDrivingSection>) schoolService
					.loadList("from DrivingDutyOfPilotDrivingSection as cc where cc.transactionNoDdToRt ="
							+ lTransactionId);
			System.out.println("size of  dutyDrivingList is "
					+ dutyDrivingList.size());
		} catch (CrewException e) {
			e.printStackTrace();
			CREW_LOGGER.debug(e.getMessage());
		}

	}

	public void statusSearch() {
		switch (statusBoolean) {

		case "CrewLinkcreated":

			System.out.print("Duty Created ");
			listRoundTrip = (List<RoundTrip>) schoolService
					.roundTriptoCrewLinkStatusSearch(statusBoolean);
			break;
		case "CrewLinkNotcreated":

			System.out.print("Not Duty Created ");
			listRoundTrip = (List<RoundTrip>) schoolService
					.roundTriptoCrewLinkStatusSearch(statusBoolean);
			break;

		case "All":
			System.out.print("All");
			listRoundTrip = (List<RoundTrip>) schoolService
					.roundTriptoCrewLinkStatusSearch(statusBoolean);
			break;

		}

	}
	public void delete()
	{
		//System.out.println(""+selectionList.size());
		Long lTransactionId = new Long(0);
		System.out.println("Selection List  Train NUmbers are :");
	
		for(DrivingDutyOfPilotDrivingSection ddps:selectionList)
		{
			lTransactionId = ddps.getTransactionNoDsToDd();
			
			System.out.println(ddps.getTrainNo());
		}
	}

	public String deleteFromRoundTrip(RoundTrip d) {
		Long lTransactionId = new Long(0);
		lTransactionId = d.getTransactionNoDdToRt();
		List<DrivingDutyOfPilotDrivingSection> DDPT2 = (List<DrivingDutyOfPilotDrivingSection>) context
				.getBean("list");
		try {

			DDPT2 = (List<DrivingDutyOfPilotDrivingSection>) schoolService
					.loadList("from  DrivingDutyOfPilotDrivingSection as cd where cd.transactionNoDdToRt="
							+ lTransactionId);
			for (DrivingDutyOfPilotDrivingSection d1 : DDPT2) {
				d1.setStatus(false);
				d1.setTransactionNoDdToRt((long) 0);
				schoolService.updateObjectInDb(d1);
				schoolService.deleteById(d.getId(), "RoundTrip");
				remove(d.getId());
			}

		} catch (Exception e) {
			e.printStackTrace();
			CREW_LOGGER.debug(e.getMessage());
		}
		return null;
	}

	private void remove(long removedId) {
		for (int nloop = 0; nloop < listRoundTrip.size(); nloop++) {
			if (listRoundTrip.get(nloop).getId() == removedId) {
				listRoundTrip.remove(nloop);
			}
		}
	}

	public boolean filterByName(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim();
		if (filterText == null || filterText.equals("")) {
			return true;
		}

		if (value == null) {
			return false;
		}

		String listroundtrips = value.toString().toUpperCase();
		filterText = filterText.toUpperCase();

		if (listroundtrips.contains(filterText)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public void crewLinkSuggestion5() {
		List<Long> listRoundTripTrainNumbers = new LinkedList<>();
		List<Long> listRoundTripTrainNumbers2 = new LinkedList<>();
		List<Long> listRoundTripTrainNumbers3 = new LinkedList<>();
		List<Long> listRoundTripTrainNumbers4 = new LinkedList<>();
		List<Integer> listSmallestIds = new LinkedList<>();
		List<Integer> listSmallestDiff = new LinkedList<>();
		Integer nTotal2 = new Integer(0);
		String strHQRest = new String();
		CrewlinkSuggestion crew = (CrewlinkSuggestion) context.getBean("crewlinkSuggestion");
		
		int smallest2 = 0;
		schoolService.deleteQuery("CrewlinkSuggestion");
		Map<Long, Long> mapFinalIds = new LinkedHashMap<>();

		List<Long> listFinalId1 = new LinkedList<>();
		List<Long> listFinalId2 = new LinkedList<>();
		
		if(listRoundTrip.isEmpty())
		{
			RequestContext.getCurrentInstance().execute("PF('rrEmptyDlg').show();");
		}
		else
		{
		
		for (RoundTrip t : listRoundTrip) {
			// counts++;
			//System.out.println("The size of listRoundTrip is " + listRoundTrip.size());
			listRoundTripTrainNumbers.add(t.getId());
			listRoundTripTrainNumbers2.add(t.getId());
			listRoundTripTrainNumbers3.add(t.getId());
			listRoundTripTrainNumbers4.add(t.getId());
			
		}
		if(listRoundTrip.size()>2)
		{
			for (int k = 0; k < listRoundTrip.size(); k++) {
				Integer nTotalHour = new Integer(0);
				listFinalId1.clear();
				listFinalId2.clear();
				mapFinalIds.clear();
				
				Long id2 = new Long(0l);
				Date dtDep = new Date();
				String strAvail = new String();
				Long id1 = new Long(0l);
				Integer counts = new Integer(0);

				List<Long> deletingIds = new LinkedList<>();
				Integer totalHours = new Integer(0);
				int count2 = 0;
				String strFirstAvailable = new String();
				Date dtLastDeparture = new Date();
				int smallest = 0;
				Map<Integer, Long> mapId = new LinkedHashMap<>();
				Long lId2 = new Long(0l);

				System.out.println("\n\n");
				if (k >= 1) {
					listRoundTripTrainNumbers3.clear();
					for (int i = k; i < listRoundTripTrainNumbers4.size(); i++) {
						listRoundTripTrainNumbers3.add(listRoundTripTrainNumbers4
								.get(i));
					}
					for (int i = 0; i < k; i++) {
						listRoundTripTrainNumbers3.add(listRoundTripTrainNumbers4
								.get(i));
					}

					/*for (int i = 0; i < listRoundTripTrainNumbers3.size(); i++) {
					//	System.out.println(listRoundTripTrainNumbers3.get(i));
					}*/
					listRoundTripTrainNumbers2.clear();
					listRoundTripTrainNumbers.clear();
					listRoundTripTrainNumbers2.addAll(listRoundTripTrainNumbers3);
					listRoundTripTrainNumbers.addAll(listRoundTripTrainNumbers3);
				/*	System.out.println("Next Iteration");
					for(Long l :listRoundTripTrainNumbers)
					{
						System.out.println(l);
					}
					System.out.println("\n\n");

					for(Long l :listRoundTripTrainNumbers2)
					{
						System.out.println(l);
					}*/
				}
				Boolean bTest = true;
				//System.out.println("\n\n");
				System.out.println("------------------------------------------");
				System.out.println("size of 1 " +listRoundTripTrainNumbers.size());
				System.out.println("size of 1 " +listRoundTripTrainNumbers2.size());
				first: for (int i = 0; i < listRoundTripTrainNumbers.size()-1; i++) {
					
					
					if(bTest)
					{
						List<RoundTrip> listRound2=null;
						try {
							listRound2 = (List<RoundTrip>) schoolService
									.loadList("from RoundTrip as rr where rr.id="
											+ listRoundTripTrainNumbers
													.get(i));
						} catch (CrewException e) {
							
							e.printStackTrace();
						}
						for(RoundTrip r : listRound2)
						{
							String[] strSplitParts = r.getNoOfDays().split(
									Pattern.quote(":"));
							Integer hour = new Integer(0);
							Integer minute = new Integer(0);
							for (int j = 0; j < strSplitParts.length - 1; j++) {
								hour = Integer.parseInt(strSplitParts[j]);
								minute = Integer.parseInt(strSplitParts[j + 1]);
							}
							
							totalHours = totalHours + hour * 60 + minute;
							//nTotal2 = totalHours;
						//	nTotalHour = nTotalHour + totalHours;
							System.out.println("*****************************");
							System.out.println("hours ->" + hour
									+ "\t\t minutes-> " + minute
									+ "\t\t totallHours " + totalHours);

						}
						bTest = false;
					}
					

					
					/*System.out.println("\n\n starting Elements");
					for(Long l :listRoundTripTrainNumbers)
					{
						System.out.println(l);
					}
					System.out.println("\n\n");

					for(Long l :listRoundTripTrainNumbers2)
					{
						System.out.println(l);
					}*/
					
					
					Integer iDiff[] = new Integer[100];
					count2 = 0;
					//to pick last departure
					if (counts == 5) {
						for (int p = 0; p < listRoundTripTrainNumbers.size(); p++) {

							if (p == 0) {
								try {
									List<RoundTrip> listRound2 = (List<RoundTrip>) schoolService
											.loadList("from RoundTrip as rr where rr.id="
													+ listRoundTripTrainNumbers
															.get(p));
									for (RoundTrip r : listRound2) {
										dtLastDeparture = r.getSignonTime();
										

									}
								} catch (CrewException e) {
									CREW_LOGGER.debug(e.getMessage());
									e.printStackTrace();
								}
							}
							break;
						}

					}
					try {
						List<RoundTrip> listRound2 = (List<RoundTrip>) schoolService
								.loadList("from RoundTrip as rr where rr.id="
										+ listRoundTripTrainNumbers.get(i));
	//adding id for last identification,and taking availability from starting
						for (RoundTrip r : listRound2) {
							id1 = r.getId();
							listFinalId1.add(r.getId());
							deletingIds.add(id1);
							// System.out.println("strAvail->" +
							// r.getAvailabilityAtHq());
							strAvail = r.getAvailabilityAtHq();
							// System.out.println("Number of days is "
							// +r.getNoOfDays());
							System.out.println("strAvail->" + strAvail);
										}
					} catch (CrewException e) {
						CREW_LOGGER.debug(e.getMessage());
						e.printStackTrace();
					}

					for (int j = 0; j < listRoundTripTrainNumbers2.size(); j++) {
						try {
							List<RoundTrip> listRound2 = (List<RoundTrip>) schoolService
									.loadList("from RoundTrip as rr where rr.id="
											+ listRoundTripTrainNumbers2.get(j));
							for (RoundTrip r : listRound2) {
								// strAvail = r.getAvailabilityAtHq();

								id2 = r.getId();
							}

						} catch (CrewException e) {
							CREW_LOGGER.debug(e.getMessage());
							e.printStackTrace();
						}
	//same id should not present
						if (!id1.equals(id2)) {
							try {
								List<RoundTrip> listRound2 = (List<RoundTrip>) schoolService
										.loadList("from RoundTrip as rr where rr.id="
												+ listRoundTripTrainNumbers2.get(j));

								for (RoundTrip r : listRound2) {
									// strAvail = r.getAvailabilityAtHq();
									dtDep = r.getSignonTime();
									lId2 = r.getId();
									// id2 = r.getId();
									System.out.println("dtDep->" + dtDep);
								}
								System.out.println("id1-> " + id1 + "id2-> " + id2);
								System.out.println(compareDepAndAvail(dtDep,
										strAvail));
								iDiff[count2] = compareDepAndAvail(dtDep, strAvail);
								mapId.put(iDiff[count2], lId2);
								count2++;
							} catch (CrewException e) {
								CREW_LOGGER.debug(e.getMessage());
								e.printStackTrace();
							}
						}
					}
					smallest = 9999;
					// System.out.println("------------------Diff---------------------");
					for (int k1 = 0; k1 < count2; k1++) {
						System.out.print(iDiff[k1] + "\t\t->");
						if (smallest > iDiff[k1]) {
							smallest = iDiff[k1];
						}
					}
					System.out.println();
					System.out.println("smallest->>> " + smallest);
					counts--;
					// totalHours = totalHours + smallest;
					for (Entry<Integer, Long> entry : mapId.entrySet()) {
						if (entry.getKey() == smallest) {
							listFinalId2.add(entry.getValue());
							System.out.println("Difference is " + entry.getKey()
									+ "\t\t Id is " + entry.getValue());
							// removing matching element from the second list
							try {
								List<RoundTrip> listRound2 = (List<RoundTrip>) schoolService
										.loadList("from RoundTrip as rr where rr.id="
												+ entry.getValue());
								String strAvailAfterPr = new String();
								for (RoundTrip r : listRound2) {
									// strAvail = r.getAvailabilityAtHq();
									// System.out.println("Number of Days->" +
									// r.getNoOfDays());
									String[] strSplitParts = r.getNoOfDays().split(
											Pattern.quote(":"));
									System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
									System.out.println("SignOFf Time " +r.getSignoffTime() + 
											" Availability \t\t " +r.getAvailabilityAtHq() +"\t\t ->"+ compareSignOffAndAvail(r.getSignoffTime(), r.getAvailabilityAtHq()));
									
									strHQRest = compareSignOffAndAvail(r.getSignoffTime(), r.getAvailabilityAtHq()).toString();
									// strAvailAfterPr = r.getAvailabilityAtHq();
									System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
									Integer hour = new Integer(0);
									Integer minute = new Integer(0);
									for (int j = 0; j < strSplitParts.length - 1; j++) {
										hour = Integer.parseInt(strSplitParts[j]);
										minute = Integer
												.parseInt(strSplitParts[j + 1]);
									}
									System.out.println("totalHours->>" +totalHours);
									totalHours = totalHours + hour * 60 + minute
											+ smallest;
									
									nTotalHour = nTotalHour + totalHours;
									System.out.println("hours ->" + hour
											+ "\t\t minutes-> " + minute
											+ "\t\t totallHours " + totalHours);
								}

								//
								if (totalHours > 8640) {
									List<RoundTrip> listRound3 = (List<RoundTrip>) schoolService
											.loadList("from RoundTrip as rr where rr.id="
													+ entry.getValue());

									String strTAva = new String();
									for (RoundTrip r : listRound3) {
										// System.out.println(r.getId());
										strTAva = r.getAvailabilityAtHq();
									}
									totalHours = 0;
									String[] strSplitParts = strTAva.split(Pattern
											.quote(":"));

									Integer hour = new Integer(0);
									Integer minute = new Integer(0);
									for (int j = 0; j < strSplitParts.length - 1; j++) {
										hour = Integer.parseInt(strSplitParts[j]);
										minute = Integer
												.parseInt(strSplitParts[j + 1]);
									}
									System.out.println("hours ->" + hour
											+ "\t\t minutes-> " + minute);
									Date dtAvail = new Date();
									Calendar cal2 = Calendar.getInstance();
									cal2.setTime(dtAvail);
									cal2.set(Calendar.HOUR_OF_DAY, hour);
									cal2.set(Calendar.MINUTE, minute);
									System.out.println("Before Adding PR");
									System.out.println(cal2.getTime());

									cal2.add(Calendar.HOUR_OF_DAY, 14);
									dtAvail = cal2.getTime();
									hour = cal2.get(Calendar.HOUR_OF_DAY);
									minute = cal2.get(Calendar.MINUTE);
									System.out.println("After Adding PR");
									System.out.println("hours ->" + hour
											+ "\t\t minutes-> " + minute);
									strTAva = hour.toString().concat(":" + minute);
									// strAvailAfterPr = hour.toString().concat(":"
									// +minute);
									System.out.println("Before Updating");
									System.out.println(strTAva + "\t\t "
											+ entry.getValue());
									schoolService.updateInRoundTripAvailability(
											entry.getValue(), strTAva);

									List<RoundTrip> list = (List<RoundTrip>) schoolService
											.loadList("from RoundTrip");
									listRoundTripTrainNumbers2.clear();
									listRoundTripTrainNumbers.clear();
									for (RoundTrip r : list) {
										listRoundTripTrainNumbers2.add(r.getId());
										listRoundTripTrainNumbers.add(r.getId());
									}
									int indexOf = listRoundTripTrainNumbers2
											.indexOf(entry.getValue());
									System.out.println("index of " + indexOf);

									List<Long> list1 = new LinkedList<>();
									List<Long> list2 = new LinkedList<>();

									// System.out.println("After changing list");
									for (int p = indexOf; p < listRoundTripTrainNumbers
											.size(); p++) {
										// if(!listRoundTripTrainNumbers.get(p).equals(id1))
										list1.add(listRoundTripTrainNumbers.get(p));
										// System.out.println(listRoundTripTrainNumbers2.get(p));
									}
									for (int p = 0; p < indexOf; p++) {
										// if(!listRoundTripTrainNumbers2.get(p).equals(id1))
										list2.add(listRoundTripTrainNumbers2.get(p));
										// System.out.println(listRoundTripTrainNumbers2.get(p));
									}
									list1.addAll(list2);

									for (Long l : list1) {
										System.out.println(l);
									}

									listRoundTripTrainNumbers2.clear();
									listRoundTripTrainNumbers.clear();

									listRoundTripTrainNumbers.addAll(list1);
									listRoundTripTrainNumbers2.addAll(list1);

									System.out.println("Deleting id is ");
									for (Long l : deletingIds) {
										listRoundTripTrainNumbers.remove(l);
										listRoundTripTrainNumbers2.remove(l);
										System.out.println(l);
									}
									i = -1;

									if (listRoundTripTrainNumbers.size() == 1
											&& listRoundTripTrainNumbers2.size() == 1) {
										System.out.println("Size is 1111111111");

										for (Long l : listRoundTripTrainNumbers) {

											List<RoundTrip> listRound4 = (List<RoundTrip>) schoolService
													.loadList("from RoundTrip as rr where rr.id="
															+ l);
											for (RoundTrip r : listRound4) {
												strFirstAvailable = r
														.getAvailabilityAtHq();
												// System.out.println("Last Available "
												// + strFirstAvailable);
											}

										}

										for (Long l : listRoundTripTrainNumbers2) {
											System.out.println(l);
											List<RoundTrip> listRound4 = (List<RoundTrip>) schoolService
													.loadList("from RoundTrip as rr where rr.id="
															+ l);
											for (RoundTrip r : listRound4) {
												strFirstAvailable = r
														.getAvailabilityAtHq();
												System.out
														.println("Last Available "
																+ strFirstAvailable);
												System.out
														.println("Last Departure time is "
																+ dtLastDeparture);
											}
										}
										break first;
									}

								} else {
									List<RoundTrip> list = (List<RoundTrip>) schoolService
											.loadList("from RoundTrip");
									listRoundTripTrainNumbers2.clear();
									listRoundTripTrainNumbers.clear();
									for (RoundTrip r : list) {
										listRoundTripTrainNumbers2.add(r.getId());
										listRoundTripTrainNumbers.add(r.getId());
									}
									int indexOf = listRoundTripTrainNumbers2
											.indexOf(entry.getValue());
									// System.out.println("index of " + indexOf);

									List<Long> list1 = new LinkedList<>();
									List<Long> list2 = new LinkedList<>();

									// System.out.println("After changing list");
									for (int p = indexOf; p < listRoundTripTrainNumbers
											.size(); p++) {
										// if(!listRoundTripTrainNumbers.get(p).equals(id1))
										list1.add(listRoundTripTrainNumbers.get(p));
										// System.out.println(listRoundTripTrainNumbers2.get(p));
									}
									for (int p = 0; p < indexOf; p++) {
										// if(!listRoundTripTrainNumbers2.get(p).equals(id1))
										list2.add(listRoundTripTrainNumbers2.get(p));
										// System.out.println(listRoundTripTrainNumbers2.get(p));
									}
									list1.addAll(list2);

									listRoundTripTrainNumbers2.clear();
									listRoundTripTrainNumbers.clear();

									listRoundTripTrainNumbers.addAll(list1);
									listRoundTripTrainNumbers2.addAll(list1);

									for (Long l : deletingIds) {
										listRoundTripTrainNumbers.remove(l);
										listRoundTripTrainNumbers2.remove(l);
									}

									if (listRoundTripTrainNumbers.size() == 1
											&& listRoundTripTrainNumbers2.size() == 1) {
										System.out.println("Size is 1111111111");

										for (Long l : listRoundTripTrainNumbers) {
											// System.out.println(l);
											List<RoundTrip> listRound4 = (List<RoundTrip>) schoolService
													.loadList("from RoundTrip as rr where rr.id="
															+ l);
											for (RoundTrip r : listRound4) {
												strFirstAvailable = r
														.getAvailabilityAtHq();
												System.out
														.println("Last Available "
																+ strFirstAvailable);
												System.out
														.println("Last Departure time is "
																+ dtLastDeparture);
											}
										}

										for (Long l : listRoundTripTrainNumbers2) {
											// System.out.println(l);
											List<RoundTrip> listRound4 = (List<RoundTrip>) schoolService
													.loadList("from RoundTrip as rr where rr.id="
															+ l);
											for (RoundTrip r : listRound4) {
												strFirstAvailable = r
														.getAvailabilityAtHq();
												// System.out.println("Last Available "
												// + strFirstAvailable);
											}
										}
										break first;
									}
									// System.out.println("deleting id is " +id1);
									i = -1;
								}

								System.out.println("id1-> " + id1 + "id2-> " + id2);
							} catch (CrewException e) {
								CREW_LOGGER.debug(e.getMessage());
								e.printStackTrace();
							}
							// listRoundTripTrainNumbers2.remove(entry.getValue());
						}

					}
					System.out
							.println("------------------------------------------------");

				}
				/*System.out.println("Final ids are");
				for (Long l : listFinalId1) {
					System.out.println(l);
				}
				System.out.println("\n\n");
				for (Long l : listFinalId2) {
					System.out.println(l);
				}*/
				
				
				//System.out.println("\n\n");
				//System.out.println(nTotalHour);
				Set<Long> setFinal = new LinkedHashSet<>(listFinalId2);
				List<Long> list2 = new LinkedList<>(setFinal);
	/*
				for (Long l : list2) {
					System.out.println(l);
				}*/
				for (int i = 0; i < listFinalId1.size(); i++) {
					mapFinalIds.put(listFinalId1.get(i), list2.get(i));
					if(i==listFinalId1.size()-1)
					{
						mapFinalIds.put(list2.get(i),listFinalId1.get(0));
						Date dtDeparture = new Date();
						String strAvailabilitys = new String();
						
						try {
							List<RoundTrip> listRound4 = (List<RoundTrip>) schoolService
									.loadList("from RoundTrip as rr where rr.id="
											+ list2.get(i));
							
							///System.out.println("Departure");
							for(RoundTrip r :listRound4 )
							{
							//	System.out.println(r.getAvailabilityAtHq());
								strAvailabilitys = r.getAvailabilityAtHq();
							}
							
							List<RoundTrip> listRound5 = (List<RoundTrip>) schoolService
									.loadList("from RoundTrip as rr where rr.id="
											+ listFinalId1.get(0));
							
							
						//	System.out.println("Availability");
							for(RoundTrip r :listRound5 )
							{
						//		System.out.println(r.getDeparturetime());
								dtDeparture = r.getDeparturetime();
							}
						///	System.out.println("Departure\t\t" +dtDeparture +"\t Availability \t\t " +strAvailabilitys +"\t\t"+
						//			compareDepAndAvail(dtDeparture, strAvailabilitys)
						//			);
						//	nTotalHour = nTotalHour + compareDepAndAvail(dtDeparture, strAvailabilitys);
						} catch (CrewException e) {
							CREW_LOGGER.debug(e.getMessage());
							e.printStackTrace();
						}
					}
				}
			//	System.out.println("\n\n");
			//	System.out.println(nTotalHour);
				
			//	System.out.println("\n\n Total Hours \n");
			//	System.out.println(nTotalHour/60);
			//	nTotalHour = nTotal2 + nTotalHour;
				System.out.println(nTotalHour/60);
				
				//all id1 and id2 are stored below
				for (Entry<Long, Long> entry : mapFinalIds.entrySet()) {
					System.out.println("Key->" + entry.getKey() + "\t\t value->"
							+ entry.getValue());
				
					try {
						crew.setFirstId(entry.getKey());
						crew.setSecondId(entry.getValue());
						crew.setDifference(nTotalHour/60);
						crew.setHqRest(strHQRest);
						schoolService.saveInDb(crew);
					} catch (CrewException e) {
						CREW_LOGGER.debug(e.getMessage());
						e.printStackTrace();
					}
				}	
				System.out.println("\n\n Smallest2 \n");
				Integer total = nTotalHour/60;
				
				//All Combinations smallest difference
				listSmallestIds.add(total);
				
				smallest2 = 9999;
				
				System.out.println("Smallest Ids are");
				for (int k1 = 0; k1 < listSmallestIds.size(); k1++) {
					System.out.print(listSmallestIds.get(k1) + "\t\t->");
					if (smallest2 > listSmallestIds.get(k1)) {
						smallest2 = listSmallestIds.get(k1);
					}
				}
				System.out.println();
				
				
				//List<CrewlinkSuggestion> listCrew = schoolService.loadList("from CrewlinkSuggestion");
				//nTotalHour = 0;
			}
		}
		else
		{
			{
				Integer nTotalHour = new Integer(0);
				Date dtSignOnTime = new Date();
				Date dtFirstSignOnTime = new Date();
				String strAvailabale = new String();
				String strLastAvailabale = new String();
				for(int i = 0; i<listRoundTripTrainNumbers.size()-1;i++)
				{
					try {
						List<RoundTrip> listRound2 = (List<RoundTrip>) schoolService
								.loadList("from RoundTrip as rr where rr.id="
										+ listRoundTripTrainNumbers
												.get(i));
						
						for(RoundTrip r : listRound2)
						{
							strAvailabale = r.getAvailabilityAtHq();
							dtFirstSignOnTime = r.getSignonTime();
							String[] strSplitParts = r.getNoOfDays().split(
									Pattern.quote(":"));
							Integer hour = new Integer(0);
							Integer minute = new Integer(0);
							for (int j = 0; j < strSplitParts.length - 1; j++) {
								hour = Integer.parseInt(strSplitParts[j]);
								minute = Integer.parseInt(strSplitParts[j + 1]);
							}
							
							nTotalHour = nTotalHour + hour * 60 + minute;
							System.out.println("*****************************");
							System.out.println("hours ->" + hour
									+ "\t\t minutes-> " + minute
									+ "\t\t totallHours " + nTotalHour);
							
							
						}
						
						
						List<RoundTrip> listRound3 = (List<RoundTrip>) schoolService
								.loadList("from RoundTrip as rr where rr.id="
										+ listRoundTripTrainNumbers
												.get(i+1));
						
						for(RoundTrip r : listRound3)
						{
							dtSignOnTime = r.getSignonTime();
							strLastAvailabale = r.getAvailabilityAtHq();
							String[] strSplitParts = r.getNoOfDays().split(
									Pattern.quote(":"));
							Integer hour = new Integer(0);
							Integer minute = new Integer(0);
							for (int j = 0; j < strSplitParts.length - 1; j++) {
								hour = Integer.parseInt(strSplitParts[j]);
								minute = Integer.parseInt(strSplitParts[j + 1]);
							}
							
							nTotalHour = nTotalHour + hour * 60 + minute;
							System.out.println("*****************************");
							System.out.println("hours ->" + hour
									+ "\t\t minutes-> " + minute
									+ "\t\t totallHours " + nTotalHour);

						}
						
						System.out.println(compareDepAndAvail(dtSignOnTime, strAvailabale));
						nTotalHour = nTotalHour + compareDepAndAvail(dtSignOnTime, strAvailabale);
						
						
						//nTotalHours = nTotalHours + 3000;
						System.out.println("nTotalHours is " +nTotalHour);
						if(nTotalHour>8640)
						{
							Date dtPRAddingDate = new Date();
							String[] strSplitParts = strLastAvailabale.split(
									Pattern.quote(":"));
							Integer hour = new Integer(0);
							Integer minute = new Integer(0);
							for (int j = 0; j < strSplitParts.length - 1; j++) {
								hour = Integer.parseInt(strSplitParts[j]);
								minute = Integer.parseInt(strSplitParts[j + 1]);
							}
							
							Calendar calAdding = Calendar.getInstance();
							calAdding.setTime(dtPRAddingDate);
							calAdding.set(Calendar.HOUR_OF_DAY, hour);
							calAdding.set(Calendar.MINUTE, minute);
							
						//	System.out.println("calAdding.getTime() is " +calAdding.getTime());
							
							calAdding.add(Calendar.HOUR_OF_DAY, 14);
							
							//System.out.println("calAdding.getTime() After Adding 14 is " +calAdding.getTime());
							String strPRChangedAvailability = new String();
							
							hour = calAdding.get(Calendar.HOUR_OF_DAY);
							minute = calAdding.get(Calendar.MINUTE);
							strPRChangedAvailability = hour.toString().concat(":" +minute);
						//	System.out.println("strPRChangedAvailability After Adding 14 is " +strPRChangedAvailability);
							
							schoolService.updateInRoundTripAvailability(
									listRoundTripTrainNumbers
									.get(i+1), strPRChangedAvailability);
						
							/*nTotalHours = nTotalHours + */
							Integer sub = compareDepAndAvail(dtFirstSignOnTime, strPRChangedAvailability);
							nTotalHour = nTotalHour+sub;
							System.out.println("nTotalHours->" +nTotalHour);
						}
						else
						{
							System.out.println("difff  " +compareDepAndAvail(dtFirstSignOnTime, strLastAvailabale));
							
							Integer sub = compareDepAndAvail(dtFirstSignOnTime, strLastAvailabale);
							nTotalHour = nTotalHour+sub;
							System.out.println("nTotalHours->" +nTotalHour);
							
						}
						
						
					} catch (CrewException e) {
						CREW_LOGGER.debug(e.getMessage());
						e.printStackTrace();
					}
				}
			}
			
		}
		}
		
		
		System.out.println("smallest difference is " +smallest2);
		
		String lTrain1 = new String();
		String lTrain2 = new String();
		//based upon smallest element access the id1 and id2 from crew link suggestion table
		try {
			List<CrewlinkSuggestion> listSuggestion = (List<CrewlinkSuggestion>) 
					schoolService.loadList("from CrewlinkSuggestion as cc where cc.difference= " +smallest2);
			
			for(CrewlinkSuggestion c : listSuggestion)
			{
				System.out.println(c.getFirstId() +"\t\t " +c.getSecondId());
			
				List<RoundTrip> list1 = (List<RoundTrip>) 
						schoolService.loadList("from RoundTrip as rr where rr.id=" +c.getFirstId());
				for(RoundTrip r : list1)
				{
					lTrain1 = r.getTrainNo();
				}
				
				List<RoundTrip> list3 = (List<RoundTrip>) 
						schoolService.loadList("from RoundTrip as rr where rr.id=" +c.getSecondId());
				
				for(RoundTrip r : list3)
				{
					lTrain2 = r.getTrainNo();
				}
				mapSuggestion.put(lTrain1
						, lTrain2);
				
				//RequestContext.getCurrentInstance().update(":viewform4:suggest");
			}
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
		
	}

	
	public Integer compareDepAndAvail(Date dtDepa, String strAvail) {
		Integer iDiffInMinutes = new Integer(0);
		String[] strSplitParts = strAvail.split(Pattern.quote(":"));
		Calendar calDep = Calendar.getInstance();

		Integer iAvailHour = new Integer(0);
		Integer iAvailMin = new Integer(0);

		Integer iDepHour = new Integer(0);
		Integer iDepMin = new Integer(0);

		for (int j = 0; j < strSplitParts.length - 1; j++) {
			iAvailHour = Integer.parseInt(strSplitParts[j]);
			iAvailMin = Integer.parseInt(strSplitParts[j + 1]);
		}
		calDep.setTime(dtDepa);
		iDepHour = calDep.get(Calendar.HOUR_OF_DAY);
		iDepMin = calDep.get(Calendar.MINUTE);

		/*
		 * System.out.println("depar Hour->" +iDepHour + "\t Dep Minute ->>" +
		 * iDepMin); System.out.println("iAvailHour Hour->" +iAvailHour +
		 * "\t iAvailHour Minute ->>" + iAvailMin);
		 */

		if (iAvailHour == 0)
			iAvailHour = 24;
		if (iDepHour == 0)
			iDepHour = 24;
		// System.out.println("-----------------------------------------");
		if (iAvailHour == iDepHour) {
			if (iAvailMin < iDepMin) {
				iDiffInMinutes = iDepMin - iAvailMin;
				/*
				 * System.out.println("Diff In minutes dep<arr -> " +
				 * iDiffInMinutes);
				 */
				// iTotalMinutes = iTotalMinutes + iDiffInMinutes;
			}
			if (iAvailMin > iDepMin) {
				iDiffInMinutes = (24 * 60 - iAvailMin) + iDepMin;
				/*
				 * System.out.println("Diff In minutes dep<arr -> " +
				 * iDiffInMinutes);
				 */
				// iTotalMinutes = iTotalMinutes + iDiffInMinutes;
			}
		} else if (iAvailHour < iDepHour) {

			/*
			 * System.out.println("Hour of Departure -> " + iAvailHour
			 * +"\t Minute of Departure-> " +iAvailMin);
			 * System.out.println("Hour of Departure -> " + iDepHour
			 * +"\t Minute of Departure-> " +iDepMin);
			 */

			iDiffInMinutes = (iDepHour * 60 + iDepMin)
					- (iAvailHour * 60 + iAvailMin);
			// iTotalMinutes = iTotalMinutes + iDiffInMinutes;
			/*
			 * System.out.println("Diff In minutes dep<arr -> " +
			 * iDiffInMinutes);
			 */
		} else {

			/*
			 * System.out.println("Hour of Departure -> " + iAvailHour
			 * +"\t Minute of Departure-> " +iAvailMin);
			 * System.out.println("Hour of Departure -> " + iDepHour
			 * +"\t Minute of Departure-> " +iDepMin);
			 */

			iDiffInMinutes = (iDepHour * 60 + iDepMin) + 24 * 60
					- (iAvailHour * 60 + iAvailMin);
			// iTotalMinutes = iTotalMinutes + iDiffInMinutes;
			/*
			 * System.out.println("Diff In minutes dep>arr -> " +
			 * iDiffInMinutes);
			 */
		}

		// System.out.println("-----------------------------------------");

		return iDiffInMinutes;
	}

//difference between signoff to available
	
	public Integer compareSignOffAndAvail(Date dtDepa, String strAvail) {
		Integer iDiffInMinutes = new Integer(0);
		String[] strSplitParts = strAvail.split(Pattern.quote(":"));
		Calendar calDep = Calendar.getInstance();

		Integer iAvailHour = new Integer(0);
		Integer iAvailMin = new Integer(0);

		Integer iDepHour = new Integer(0);
		Integer iDepMin = new Integer(0);

		for (int j = 0; j < strSplitParts.length - 1; j++) {
			iAvailHour = Integer.parseInt(strSplitParts[j]);
			iAvailMin = Integer.parseInt(strSplitParts[j + 1]);
		}
		calDep.setTime(dtDepa);
		iDepHour = calDep.get(Calendar.HOUR_OF_DAY);
		iDepMin = calDep.get(Calendar.MINUTE);

		/*
		 * System.out.println("depar Hour->" +iDepHour + "\t Dep Minute ->>" +
		 * iDepMin); System.out.println("iAvailHour Hour->" +iAvailHour +
		 * "\t iAvailHour Minute ->>" + iAvailMin);
		 */

		if (iAvailHour == 0)
			iAvailHour = 24;
		if (iDepHour == 0)
			iDepHour = 24;
		// System.out.println("-----------------------------------------");
		if (iAvailHour == iDepHour) {
			if (iDepHour < iAvailHour) {
				iDiffInMinutes = iAvailMin - iDepMin ;
				/*
				 * System.out.println("Diff In minutes dep<arr -> " +
				 * iDiffInMinutes);
				 */
				// iTotalMinutes = iTotalMinutes + iDiffInMinutes;
			}
			if (iDepMin > iAvailMin) {
				iDiffInMinutes = (24 * 60 - iDepMin) + iAvailMin;
				/*
				 * System.out.println("Diff In minutes dep<arr -> " +
				 * iDiffInMinutes);
				 */
				// iTotalMinutes = iTotalMinutes + iDiffInMinutes;
			}
		} else if (iDepHour < iAvailHour) {

			/*
			 * System.out.println("Hour of Departure -> " + iAvailHour
			 * +"\t Minute of Departure-> " +iAvailMin);
			 * System.out.println("Hour of Departure -> " + iDepHour
			 * +"\t Minute of Departure-> " +iDepMin);
			 */

			iDiffInMinutes =  (iAvailHour * 60 + iAvailMin) - (iDepHour * 60 + iDepMin);
			// iTotalMinutes = iTotalMinutes + iDiffInMinutes;
			/*
			 * System.out.println("Diff In minutes dep<arr -> " +
			 * iDiffInMinutes);
			 */
		} else {

			/*
			 * System.out.println("Hour of Departure -> " + iAvailHour
			 * +"\t Minute of Departure-> " +iAvailMin);
			 * System.out.println("Hour of Departure -> " + iDepHour
			 * +"\t Minute of Departure-> " +iDepMin);
			 */

			iDiffInMinutes = (iAvailHour * 60 + iAvailMin) + 24 * 60
					-  (iDepHour * 60 + iDepMin);
			// iTotalMinutes = iTotalMinutes + iDiffInMinutes;
			/*
			 * System.out.println("Diff In minutes dep>arr -> " +
			 * iDiffInMinutes);
			 */
		}

		// System.out.println("-----------------------------------------");

		return iDiffInMinutes;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	// / all getter and setter
	public ApplicationContext getContext() {
		return context;
	}

	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	public String getDrivingsectionsequence() {
		return drivingsectionsequence;
	}

	public void setDrivingsectionsequence(String drivingsectionsequence) {
		this.drivingsectionsequence = drivingsectionsequence;
	}

	public String getPilotjourneysequence() {
		return pilotjourneysequence;
	}

	public void setPilotjourneysequence(String pilotjourneysequence) {
		this.pilotjourneysequence = pilotjourneysequence;
	}

	public CrewService getSchoolService() {
		return schoolService;
	}

	public void setSchoolService(CrewService schoolService) {
		this.schoolService = schoolService;
	}

	public HttpSession getHttpSession() {
		return httpSession;
	}

	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}

	public String[] getDsectionarray() {
		return dsectionarray;
	}

	public void setDsectionarray(String[] dsectionarray) {
		this.dsectionarray = dsectionarray;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Logger getCrewLogger() {
		return CREW_LOGGER;
	}

	public RoundTrip getRoundTrip() {
		return roundTrip;
	}

	public void setRoundTrip(RoundTrip roundTrip) {
		this.roundTrip = roundTrip;
	}

	public TransactionIdDdRr getTransactionFromDdtoRr() {
		return transactionFromDdtoRr;
	}

	public void setTransactionFromDdtoRr(TransactionIdDdRr transactionFromDdtoRr) {
		this.transactionFromDdtoRr = transactionFromDdtoRr;
	}

	public DrivingDutyOfPilotDrivingSection getSelectedDrivingDuty() {
		return selectedDrivingDuty;
	}

	public void setSelectedDrivingDuty(
			DrivingDutyOfPilotDrivingSection selectedDrivingDuty) {
		this.selectedDrivingDuty = selectedDrivingDuty;
	}

	public List<RoundTrip> getListRoundTrip() {
		return listRoundTrip;
	}

	public void setListRoundTrip(List<RoundTrip> listRoundTrip) {
		this.listRoundTrip = listRoundTrip;
	}

	public RoundTrip getSelectedRoundTrip() {
		return selectedRoundTrip;
	}

	public void setSelectedRoundTrip(RoundTrip selectedRoundTrip) {
		this.selectedRoundTrip = selectedRoundTrip;
	}

	public List<DrivingDutyOfPilotDrivingSection> getDutyDrivingList() {
		return dutyDrivingList;
	}

	public void setDutyDrivingList(
			List<DrivingDutyOfPilotDrivingSection> dutyDrivingList) {
		this.dutyDrivingList = dutyDrivingList;
	}

	public String getStatusBoolean() {
		return statusBoolean;
	}

	public void setStatusBoolean(String statusBoolean) {
		this.statusBoolean = statusBoolean;
	}

	public List<SelectItem> getRoundtripSelectItemFrom() {
		return roundtripSelectItemFrom;
	}

	public void setRoundtripSelectItemFrom(
			List<SelectItem> roundtripSelectItemFrom) {
		this.roundtripSelectItemFrom = roundtripSelectItemFrom;
	}

	public List<SelectItem> getRoundtripSelectItemTo() {
		return roundtripSelectItemTo;
	}

	public void setRoundtripSelectItemTo(List<SelectItem> roundtripSelectItemTo) {
		this.roundtripSelectItemTo = roundtripSelectItemTo;
	}

	public String getRoundTripFrom() {
		return roundTripFrom;
	}

	public void setRoundTripFrom(String roundTripFrom) {
		this.roundTripFrom = roundTripFrom;
	}

	public String getRoundTripTo() {
		return roundTripTo;
	}

	public void setRoundTripTo(String roundTripTo) {
		this.roundTripTo = roundTripTo;
	}

	public String getRoundTripFrom1() {
		return roundTripFrom1;
	}

	public void setRoundTripFrom1(String roundTripFrom1) {
		this.roundTripFrom1 = roundTripFrom1;
	}

	public Map<String, String> getMapSuggestion() {
		return mapSuggestion;
	}

	public void setMapSuggestion(Map<String, String> mapSuggestion) {
		this.mapSuggestion = mapSuggestion;
	}

	public String getRoundTripTo1() {
		return roundTripTo1;
	}

	public void setRoundTripTo1(String roundTripTo1) {
		this.roundTripTo1 = roundTripTo1;
	}

	public List<DrivingDutyOfPilotDrivingSection> getSelectionList() {
		return selectionList;
	}

	public void setSelectionList(
			List<DrivingDutyOfPilotDrivingSection> selectionList) {
		this.selectionList = selectionList;
	}
}
