package com.crew.bean;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.crew.HelperUtil.Periodic;
import com.crew.exception.CrewException;
import com.crew.model.CrewChangeDrivingsection;
import com.crew.model.CrewLink;
import com.crew.model.Customer;
import com.crew.model.DrivingDutyOfPilotDrivingSection;
import com.crew.model.RoundTrip;
import com.crew.model.SaveLinkParameter;
import com.crew.model.TransactionIdRrCl;
import com.crew.service.CrewService;
import com.crew.util.CrewUtilities;



@ManagedBean(name="rrtocrewlink",eager=true)
@ViewScoped
@SuppressWarnings("unchecked")
public class RoundTripToCrewLink {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "schoolService", value = "#{schoolService}")
	private CrewService schoolService;
	private HttpSession httpSession = (HttpSession) FacesContext
			.getCurrentInstance().getExternalContext().getSession(false);
	private static final Logger CREW_LOGGER = Logger.getLogger(adminDrivingduty.class);
	
	
	private ApplicationContext context;
	private String drivingsectionsequence;
	private String [] dsectionarray = new String[100];
	private int count = 0;
	private Map<String,Long> mapSeqNoPlusId= new LinkedHashMap<String,Long>();
	private Boolean bSelectedCheckBox;
	private List<RoundTrip> roundTripList;
	private Long lSelectedValue[] = new Long[100];
	private Map<Long,String> mapIdAndAvailable = new LinkedHashMap<Long,String>();
	Map<String,Long> mapSorted=null; 
	private List<RoundTrip> selectionList;
	
	////   
	
	 private List<SelectItem>roundtripSelectItemFrom;
	 private List<SelectItem>roundtripSelectItemTo;
	 private String roundTripFrom;
	 private String roundTripTo;
	 private String roundTripFrom1;
	 private String roundTripTo1; 
	 private long loginId;
	 String strAllTrainNames = new String();
	 //private String roundTripStatus;
	 
	 
	 
     
     private String headQuarterTotal;
     private String headQuarter14DaysPercentage;
     
     private String osRestTotal;
     private String osRestPer14DaysPercentage;
     private List<String> lstDrivingSequence = new LinkedList<>();
     
     
	 
	 
	 // santosh
	private long average_Kms_per_month = 0;
	private List<CrewChangeDrivingsection> crewList = new ArrayList<CrewChangeDrivingsection>();
	
	private CrewLink crewLink;
	Double dAvgKM = new Double(0);
	Integer nRunningTime = new Integer(0);
	Double nTotalRunningTime = new Double(0);
	
	
	
	 
	 
	    @PostConstruct
		private void initilization() {

		if (!(httpSession.getAttribute("COMMONLOGINID") == null)) {
			loginId = (Long) httpSession.getAttribute("COMMONLOGINID");
			 createList();
			 loadList();
			 addedroundTripSelectedItems();
		

		} else {
			try {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(CrewUtilities.getRedirectContext()+ "/login.xhtml");
			} catch (IOException e) {
				CREW_LOGGER.debug(e.getMessage());
			}

		}

		}
	 
	 
		 
		@SuppressWarnings("unchecked")
		private void createList()
		{
			context = new ClassPathXmlApplicationContext("Beans.xml");
			roundTripList = (List<RoundTrip>) context.getBean("list");
			
			roundtripSelectItemFrom  =  new ArrayList<SelectItem>();
			roundtripSelectItemTo    = new ArrayList<SelectItem>();
			crewLink = (CrewLink) context.getBean("crewlink");
			selectionList = (List<RoundTrip>) context.getBean("list");
		}
		@SuppressWarnings("unchecked")
		public void loadList()
		{	try {
				roundTripList = (List<RoundTrip>) schoolService.loadList("from RoundTrip as rrt where rrt.customer.id="+loginId);
				
				//average_WorkingHours_PerTwoWeeks();
				headQuarterRest();
				osRest();
				Collections.sort(roundTripList, new Comparator<RoundTrip>() {
			     @Override
						public int compare(RoundTrip o1, RoundTrip o2) {
							return o1.getFromStation().compareToIgnoreCase(o2.getFromStation());
						}
			        });
			
			} catch (CrewException e) {
				e.printStackTrace();
				CREW_LOGGER.debug(e.getMessage());
			}
		}

		public void saveInMap(Long id)
		{
			System.out.println("Sequence Id = " +drivingsectionsequence);
			lstDrivingSequence.add(drivingsectionsequence);
			dsectionarray[count] = drivingsectionsequence;
			mapSeqNoPlusId.put(drivingsectionsequence, id);
			mapSorted = new TreeMap<String, Long>(mapSeqNoPlusId);
			count++;
		}
		
		public void addedroundTripSelectedItems() {
			
			
			List<RoundTrip>roundTripList = new ArrayList<RoundTrip>();
			
			 HashMap<String,String> to=new HashMap<String,String>();  
			 HashMap<String,String> from=new HashMap<String,String>();
			 
			 try {
				roundTripList = (List<RoundTrip>) schoolService.loadList("from RoundTrip");
			} catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());
			}
			 
			 
			  for(RoundTrip r:roundTripList)
			  {   to.put(r.getToStation(), r.getToStation());
				  from.put(r.getFromStation(), r.getFromStation());			  
			  }
			 
			  for(Map.Entry<String,String> mapfrom:from.entrySet()){  
				  roundtripSelectItemFrom.add(new SelectItem(mapfrom.getKey()));
				  }  
			 
			  for(Map.Entry<String,String> mapto:to.entrySet()){  
				  roundtripSelectItemTo.add(new SelectItem(mapto.getKey()));
				  }  
			 
			  
			  
		}
		
		@SuppressWarnings("unchecked")
		private Long generateTransactionIdFromRoundTripToCrewLink()
		{
			Long lTransactionId = new Long(0);
			TransactionIdRrCl transactionIdRrToCl = (TransactionIdRrCl) context.getBean("transactionidrrcl");
			List<TransactionIdRrCl> listTransactionIdGenerator = (List<TransactionIdRrCl>) context.getBean("list");
			try {
				listTransactionIdGenerator = (List<TransactionIdRrCl>) schoolService.loadList("from TransactionIdRrCl");
				
				System.out.println("size of trans id from dd to rr!!!!!!!" +listTransactionIdGenerator.size());
				Long l1 = new Long(111);
				System.out.println("value of l1 is !!!!!!!" +l1);
				if(listTransactionIdGenerator.isEmpty())
				{
					transactionIdRrToCl.setTransactionIdRrCl(l1);
					System.out.println("trans id from dd to rr-> " +transactionIdRrToCl.getTransactionIdRrCl());
					schoolService.saveInDb(transactionIdRrToCl);
					
					System.out.println("value of l1 is !!!!!!!" +l1);
				}
				else
				{
					List<Long> listTransactioIds = new LinkedList<Long>();
					transactionIdRrToCl.setTransactionIdRrCl(l1);
					for(TransactionIdRrCl t : listTransactionIdGenerator)
					{
						listTransactioIds.add(t.getTransactionIdRrCl());
					}
					Collections.sort(listTransactioIds);
					
					for(int i = 0; i<listTransactioIds.size();i++)
					{
						if(i==listTransactioIds.size()-1)
						{
							lTransactionId = listTransactioIds.get(i);
						}
					}
					System.out.println("lTransactionId-> " +transactionIdRrToCl.getTransactionIdRrCl());
				}
				
				
			} catch (CrewException e) {
				e.printStackTrace();
				CREW_LOGGER.debug(e.getMessage());
			}
			return lTransactionId;
		}
		
		//calculate difference between arrival and departure 
		public Integer calculateDiffBetweenArrivalAndDeparture(Date dtArrival,Date dtDepartureTime)
		{
			System.out.println("-------------------------");
			Integer iDiffInMinutes = new Integer(0);
			/*String strAvailableTime[] = strAvailable.split(Pattern.quote(":"));
			String strHour = new String();
			
			String strMinute = new String();
			for(int i = 0 ; i<strAvailableTime.length-1;i++)
			{
				strHour = strAvailableTime[i];
				strMinute = strAvailableTime[i+1];
			}*/
			Calendar calAvailable = Calendar.getInstance();
			calAvailable.setTime(dtArrival);
			
			Integer iArrHour = calAvailable.get(Calendar.HOUR_OF_DAY);
			Integer iArrMinute = calAvailable.get(Calendar.MINUTE);
			
			Calendar calDeparture = Calendar.getInstance();
			
			calDeparture.setTime(dtDepartureTime);
			Integer iDepHour = calDeparture.get(Calendar.HOUR_OF_DAY);
			Integer iDepMinute = calDeparture.get(Calendar.MINUTE);
			if (iDepHour < iArrHour) {
			/*	System.out.println("Hour of Departure -> " + iDepHour
						+ "\t Minute of Departure-> " + iDepMinute);
				System.out.println("Hour of Departure -> " + iArrHour
						+ "\t Minute of Departure-> " + iArrMinute);*/

				iDiffInMinutes = 
						 (iArrHour * 60 + iArrMinute)- (iDepHour * 60 + iDepMinute);
				System.out.println("Diff In minutes dep<arr -> " + iDiffInMinutes);
			} else {
				/*System.out.println("Hour of Departure -> " + iDepHour
						+ "\t Minute of Departure-> " + iDepMinute);
				System.out.println("Hour of Departure -> " + iArrHour
						+ "\t Minute of Departure-> " + iArrMinute);*/

				iDiffInMinutes = (iArrHour * 60 + iArrMinute)+ 24 * 60
						- (iDepHour * 60 + iDepMinute) ;
				System.out.println("Diff In minutes dep>arr -> " + iDiffInMinutes);
			}
			System.out.println("-------------------------");
			return iDiffInMinutes;
		}
		public String createCrewLinks()
		{
			
			List<Long> listIds = new LinkedList<Long>();
			Integer totalHours = new Integer(0);
			Date dtSignOff = new Date();
			String strAvailabilityHQ = new String();
			String strAvailabilityCal = new String();
			Date dtSignOffCal = new Date();
			
			Long nTotalKMs = new Long(0);
			
			/*System.out.println("-----------------------------------------------");
			for(Entry<Long,String> entry : mapIdAndAvailable.entrySet() )
			{
				System.out.println("Id ---- " +entry.getKey() + "\t\t Available----  " +entry.getValue());
			}
			System.out.println("-----------------------------------------------");
			*/
			for(Entry<String,Long> entry : mapSorted.entrySet())
			{
				listIds.add(entry.getValue());
			}
			
			RoundTrip roundTripObj = (RoundTrip) context.getBean("roundtrip");
			String strAvailableTime = new String();
			Date dtDepartureTime = new Date();
			for(int i = 0; i <listIds.size(); i++)
			{
				
				System.out.println("the list id is " + listIds.get(i));
					try {
						roundTripObj = (RoundTrip) 
								schoolService.loadUniqueObject("from RoundTrip as rr where rr.id=" + listIds.get(i));
					} catch (CrewException e) {
						CREW_LOGGER.debug(e.getMessage());
						e.printStackTrace();
					}
					strAvailabilityCal = roundTripObj.getAvailabilityAtHq();
					//nTotalKMs = nTotalKMs + roundTripObj.getDistance();
					//System.out.println("The Total KM is " +nTotalKMs);
					
					
					String[] strSplitPartsTrains = roundTripObj.getTrainNo().split(
							Pattern.quote(","));
				//	System.out.println("After Splitting Train Numbers are ");
							
					for(String s : strSplitPartsTrains)
					{
						//System.out.println(s);
						
						try {
							List<DrivingDutyOfPilotDrivingSection> list=
(List<DrivingDutyOfPilotDrivingSection>) schoolService.loadList("from DrivingDutyOfPilotDrivingSection as dd where dd.trainNo like'" +s+"'");
						
						for(DrivingDutyOfPilotDrivingSection d : list)
						{
							//System.out.println("Diff Between arrival and departure " +calculateDiffBetweenArrivalAndDeparture(d.getArrival(),d.getDeparture()));
							nRunningTime = nRunningTime + calculateDiffBetweenArrivalAndDeparture(d.getArrival(),d.getDeparture());
						}
						} catch (CrewException e) {
							CREW_LOGGER.debug(e.getMessage());
							e.printStackTrace();
						}
					}
					
					String[] strSplitParts = roundTripObj.getNoOfDays().split(
							Pattern.quote(":"));
					Integer hour = new Integer(0);
					Integer minute = new Integer(0);
					for (int j = 0; j < strSplitParts.length - 1; j++) {
						hour = Integer.parseInt(strSplitParts[j]);
						minute = Integer.parseInt(strSplitParts[j + 1]);
					}
					
					totalHours = totalHours + hour * 60 + minute;
				//	System.out.println("hour" +hour +"\t\t Minutes \t\t " + minute + "totalHours-->>>>>> " + totalHours);
				//	System.out.println("-----------------------------------------------");
				if(i>=1)
				{
					try {
						roundTripObj = (RoundTrip) 
								schoolService.loadUniqueObject("from RoundTrip as rr where rr.id=" + listIds.get(i));
								
					} catch (CrewException e) {
						e.printStackTrace();
					}
					dtDepartureTime = roundTripObj.getDeparturetime();
					calculateDiffBetweenAvailableAndDeparture(strAvailableTime, dtDepartureTime);
				}
				Long lTransactionId = generateTransactionIdFromRoundTripToCrewLink();
				try {
					
					roundTripObj = (RoundTrip) schoolService.loadUniqueObject("from RoundTrip as rr where rr.id=" + listIds.get(i));
					
					System.out.println("Departure time is --   " +roundTripObj.getDeparturetime());
					System.out.println("Arrival time is --   " +roundTripObj.getArrivaltime());
					
					
					strAvailableTime = roundTripObj.getAvailabilityAtHq();
					crewLink.setFromStation(roundTripObj.getFromStation());
					crewLink.setToStation(roundTripObj.getToStation());
					crewLink.setDeparture(roundTripObj.getDeparturetime());
					crewLink.setArrivalTime(roundTripObj.getArrivaltime());
					crewLink.setHeadQuarterRest(roundTripObj.getRrHqRest());
					crewLink.setOutStationRest(roundTripObj.getRrOsRest());
					crewLink.setTrainNo(roundTripObj.getTrainNo());
					crewLink.setTransactionNoCrewLink(lTransactionId);
					crewLink.setSequenceId(lstDrivingSequence.get(i).toString());
					crewLink.setCrewBase(roundTripObj.getCrewBase());
					crewLink.setCrewType(roundTripObj.getCrewType());
					crewLink.setDistance(roundTripObj.getDistance());
					crewLink.setWorkingHour(roundTripObj.getNoOfDays());
					
					
					if(mapIdAndAvailable.containsKey(listIds.get(i)))
					{   // System.out.println("-----------------------------------------------");
						totalHours = totalHours + 14*60;
						//System.out.println("totalHours-->>>>>> " + totalHours);
						//System.out.println("Inside PR " + mapIdAndAvailable.get(listIds.get(i)));
						//strAvailable = mapIdAndAvailable.get(listIds.get(i));
						dtSignOff = roundTripObj.getSignoffTime();
						strAvailabilityHQ = roundTripObj.getAvailabilityAtHq();
						String strAvailableHQ[] = strAvailabilityHQ.split(Pattern.quote(":"));
						String strHour = new String();
						String strMinute = new String();
						for(int j = 0 ; j<strAvailableHQ.length-1;j++)
						{
							strHour = strAvailableHQ[j];
							strMinute = strAvailableHQ[j+1];
						}
						
						Calendar calSignOff = Calendar.getInstance();
						calSignOff.setTime(dtSignOff);
						calSignOff.set(Calendar.HOUR_OF_DAY,Integer.parseInt(strHour)+14);  ///  adding PR here
						calSignOff.set(Calendar.MINUTE,Integer.parseInt(strMinute));
						
						Integer iHour = calSignOff.get(Calendar.HOUR_OF_DAY);
						Integer iMinute = calSignOff.get(Calendar.MINUTE);
						
						String strHourAndMinute = iHour.toString().concat(":" + iMinute);
						//System.out.println("Inside PR dtSignOff" + dtSignOff);
						crewLink.setAvailability(strHourAndMinute);
						//System.out.println("strHourAndMinute" +strHourAndMinute);
					}
					else
					{System.out.println("-----------------------------------------------");
						
						//System.out.println("roundTripObj.getAvailabilityAtHq()" + roundTripObj.getAvailabilityAtHq());
						crewLink.setAvailability(roundTripObj.getAvailabilityAtHq());
						
					}
					
				} catch (CrewException e) {
					e.printStackTrace();
				}
				
				try {
					
					    crewLink.setCustomer((Customer)schoolService.loadUniqueObject("from Customer as cst where cst.id="+loginId));
					  //  crewLink.setSequenceId(Long.valueOf(dsectionarray[i]));
						schoolService.saveInDb(crewLink);
						roundTripObj.setStatus(true);
						schoolService.updateObjectInDb(roundTripObj);
						saveTransactionIdToRoundTripTable( mapSeqNoPlusId.get(dsectionarray[i]),lTransactionId);
						TransactionIdRrCl trddrr = (TransactionIdRrCl) context.getBean("transactionidrrcl");
						Long lTransactionId2 = this.generateTransactionIdFromRoundTripToCrewLink();
						lTransactionId2++;
						trddrr.setTransactionIdRrCl(lTransactionId2);
						schoolService.saveInDb(trddrr);
					}
					catch (CrewException e) {
					e.printStackTrace();
											}
			
		}
			/*try {
				schoolService.deleteQuery("SaveLinkParameter");
				SaveLinkParameter save = (SaveLinkParameter) context.getBean("saveLinkParameter");
				totalHours = totalHours / (24*60);
				dAvgKM = (double) ((nTotalKMs / totalHours)* 30);
				nRunningTime = nRunningTime / 60;
				nTotalRunningTime = (double) ((nRunningTime)/totalHours);
				nTotalRunningTime = nTotalRunningTime * 24;
				System.out.println("The dAvgKM KM is " + dAvgKM);
				System.out.println("The Running Time is " + nRunningTime);
				System.out.println("The totalHours  is " + totalHours);
				System.out.println("The nTotalRunningTime Time is " + nTotalRunningTime);
				save.setAvgKm(dAvgKM);
				save.setAvgRnTime(nTotalRunningTime);
				
				schoolService.saveInDb(save);
			} catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
			}*/
			
			//System.out.println("The nTotalKMs KM is " + nTotalKMs);
			//System.out.println("The totalHours  is " + totalHours/60);
			drivingsectionsequence = null;
			loadList();
			return "viewroundtrips.xhtml?faces-redirect=true";
			
		}
		
		
		
		
		
		
		
		
		
		
		
		
		public Integer calculateDiffBetweenAvailableAndDeparture(String strAvailable,Date dtDepartureTime)
		{
		
			String strAvailableTime[] = strAvailable.split(Pattern.quote(":"));
			String strHour = new String();
			Integer iDiffInMinutes = new Integer(0);
			String strMinute = new String();
			for(int i = 0 ; i<strAvailableTime.length-1;i++)
			{
				strHour = strAvailableTime[i];
				strMinute = strAvailableTime[i+1];
			}
			Calendar calAvailable = Calendar.getInstance();
			calAvailable.setTime(dtDepartureTime);
			calAvailable.set(Calendar.HOUR_OF_DAY, Integer.parseInt(strHour));
			calAvailable.set(Calendar.MINUTE, Integer.parseInt(strMinute));
			
			Integer iAvailableHour = calAvailable.get(Calendar.HOUR_OF_DAY);
			Integer iAvailableMinute = calAvailable.get(Calendar.MINUTE);
			
			Calendar calDeparture = Calendar.getInstance();
			
			calDeparture.setTime(dtDepartureTime);
			Integer iDepartureHour = calDeparture.get(Calendar.HOUR_OF_DAY);
			Integer iDepartureMinute = calDeparture.get(Calendar.MINUTE);
			
			if(iAvailableHour==0) iAvailableHour = 24;
			if(iDepartureHour==0) iDepartureHour = 24;
			
			
			if(iAvailableHour < iDepartureHour)
			{iDiffInMinutes =  (iDepartureHour*60 + iDepartureMinute) - (iAvailableHour * 60 +iAvailableMinute);
			}
			else
			{	iDiffInMinutes = (iDepartureHour*60 + iDepartureMinute) + 24* 60 - (iAvailableHour * 60 +iAvailableMinute);
			}
			return iDiffInMinutes;
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		public void saveTransactionIdToRoundTripTable(Long slno,Long lTransactionId)
		{
			try {
				@SuppressWarnings("unchecked")
				List<RoundTrip> listRoundTrip = (List<RoundTrip>) schoolService.loadList("from RoundTrip as rr where rr.id=" +slno);
				
				
				
				System.out.println("-----------------------------------------------");
				for(RoundTrip r : listRoundTrip)
				{	r.setTransactionNoCrewLink(String.valueOf(lTransactionId));
				    schoolService.updateObjectInDb(r);
				}
				System.out.println("-----------------------------------------------");
				
			} catch (CrewException e) {
				e.printStackTrace();
				CREW_LOGGER.debug(e.getMessage());
			}
		}
		
		
		@SuppressWarnings("unchecked")
		public void selectedCheckBox(RoundTrip d)
		{
			
			System.out.println("selected check box " + bSelectedCheckBox);
			lSelectedValue[count] = d.getId();
			count++;
			System.out.println("No of Count  = ="+count);
		
			List<RoundTrip> listSelectedIdRR =(List<RoundTrip>) context.getBean("list"); 
			for(int i = 0; i<count; i++)
			{
				System.out.println("selected check box id->" + lSelectedValue[i]);
				try {
					listSelectedIdRR =(List<RoundTrip>) schoolService.loadList("from RoundTrip as r where r.id=" + lSelectedValue[i]);
					for(RoundTrip r : listSelectedIdRR)
					{
						mapIdAndAvailable.put(lSelectedValue[i], r.getAvailabilityAtHq());
					}
				} catch (CrewException e) {
					e.printStackTrace();
					CREW_LOGGER.debug(e.getMessage());
				}
			}
		}
		
		
		
		
		
		public void roundTripMultipleSearch()
		{
			if(this.roundTripFrom!=null||this.roundTripTo!=null||this.roundTripFrom1!=null ||this.roundTripTo1!=null)
			{	
				 if(roundTripFrom!=null && !roundTripFrom.isEmpty() && roundTripTo!=null && !roundTripTo.isEmpty() && roundTripFrom1!=null && !roundTripFrom1.isEmpty() && roundTripTo1!=null && !roundTripTo1.isEmpty())
					{  roundTripList = (List<RoundTrip>) schoolService.loadRoundTripSearchList(roundTripFrom, roundTripTo,roundTripFrom1,roundTripTo1);
				    }
				 else if(roundTripFrom!=null && !roundTripFrom.isEmpty() && roundTripTo!=null && !roundTripTo.isEmpty())
				 { 
					 roundTripList =(List<RoundTrip>) schoolService.loadRoundTripSearchListTwoBox(roundTripFrom,roundTripTo,"UPPER");
				 }
				 else if(roundTripFrom1!=null && !roundTripFrom1.isEmpty() && roundTripTo1!=null && !roundTripTo1.isEmpty())
				 {
					 roundTripList =(List<RoundTrip>) schoolService.loadRoundTripSearchListTwoBox(roundTripFrom1,roundTripTo1,"LOWER");
				 }
				 else if(!roundTripFrom.isEmpty())
				 {
					 roundTripList = (List<RoundTrip>) schoolService.roundTripsingleSearch(roundTripFrom,"FROM");
				 }
				 else if(!roundTripTo.isEmpty())
				 {
					 roundTripList = (List<RoundTrip>) schoolService.roundTripsingleSearch(roundTripTo,"TO");
				 }
				 else if(!roundTripFrom1.isEmpty())
				 {
					 roundTripList = (List<RoundTrip>) schoolService.roundTripsingleSearch(roundTripTo,"FROM1");
				 }
				 else if(!roundTripTo1.isEmpty())
				 {
					 roundTripList = (List<RoundTrip>) schoolService.roundTripsingleSearch(roundTripTo1,"TO1");
				 }
					 
					 
				
			}
			//System.out.println("inside Search");
			
		}
		
		public void deleteMultipleSelection()
		{
			System.out.println("Selected Train Numbers are:");
			List<DrivingDutyOfPilotDrivingSection> DDPT2= (List<DrivingDutyOfPilotDrivingSection>) context.getBean("list");
			Long lTransactionId = new Long(0);
			for(RoundTrip r : selectionList)
			{
				
				lTransactionId = r.getTransactionNoDdToRt();
				
				try {
					
					DDPT2 = (List<DrivingDutyOfPilotDrivingSection>)
							schoolService.loadList("from  DrivingDutyOfPilotDrivingSection as cd where cd.transactionNoDdToRt="+lTransactionId);
					for(DrivingDutyOfPilotDrivingSection d1 : DDPT2)
					{
						d1.setStatus(false);
						d1.setTransactionNoDdToRt((long)0);
						schoolService.updateObjectInDb(d1);
						schoolService.deleteById(r.getId(), "RoundTrip");
						remove(r);
					}
					
					
					
				} catch (Exception e) {
					CREW_LOGGER.debug(e.getMessage());
				}
			}
			
		}
		public void deleteFromRoundTrip(RoundTrip d)
		{
			Long lTransactionId = new Long(0);
			lTransactionId = d.getTransactionNoDdToRt();
			List<DrivingDutyOfPilotDrivingSection> DDPT2= (List<DrivingDutyOfPilotDrivingSection>) context.getBean("list");
			try {
				
				DDPT2 = (List<DrivingDutyOfPilotDrivingSection>)
						schoolService.loadList("from  DrivingDutyOfPilotDrivingSection as cd where cd.transactionNoDdToRt="+lTransactionId);
				for(DrivingDutyOfPilotDrivingSection d1 : DDPT2)
				{
					d1.setStatus(false);
					d1.setTransactionNoDdToRt((long)0);
					schoolService.updateObjectInDb(d1);
					schoolService.deleteById(d.getId(), "RoundTrip");
					remove(d);
				}
				
				
				
			} catch (Exception e) {
				CREW_LOGGER.debug(e.getMessage());
			}
			
		}
			
	
		 private void remove(RoundTrip d) {
			 for(int nIndex=0;nIndex<roundTripList.size();nIndex++)
			 { if(roundTripList.get(nIndex).getId()==d.getId())
				 {roundTripList.remove(nIndex);
				 }
			 }
		}
		 
		
		
	
		 
		 
		 
///  santosh 		 
	
		
	
	public void osRest()
	{
		
		
		try {
			List<DrivingDutyOfPilotDrivingSection> DrivingSectionList  = (List<DrivingDutyOfPilotDrivingSection>) this.schoolService.loadList("from DrivingDutyOfPilotDrivingSection as dds where dds.customer.id="+loginId);
			
			System.out.println("Size is  = ="+DrivingSectionList.size());
			String str [] = new String[DrivingSectionList.size()];
			
			for(int nIndex = 0;nIndex<DrivingSectionList.size();nIndex++)
			{
				if(DrivingSectionList.get(nIndex).getOsRest()!=null)
				{
					str [nIndex] = DrivingSectionList.get(nIndex).getOsRest();	
				}
				  
				    
			}
			
			ArrayList<String> arraylist = new ArrayList<String>();
			for(int nIndex = 0;nIndex<str.length;nIndex++)
			{	if(str[nIndex]!=null)
				{	StringTokenizer tok = new StringTokenizer(str[nIndex], ":");
					while (tok.hasMoreElements()) {
						String str1 = (String) tok.nextElement();
						 arraylist.add(str1);
					}	
				}
				
				
			}
			
			OSRest_minutes_hour_Summation(arraylist);

			/*for(String str2:arraylist)
			{
				System.out.println(""+str2);
			}
			*/
			
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
		System.out.print("----------------------------OS---------------------");
		
		
	}
	
	public void OSRest_minutes_hour_Summation(ArrayList<String> arraylist)
	{
	
		int nCount = 0;
		int str1[]= new int[arraylist.size()]; 
		for (String str : arraylist) {
			str1[nCount++] = Integer.parseInt(str);
		}
		int nMinutes  = 0;
		int nHours  = 0;
		for(int nIndex = 0;nIndex<str1.length;nIndex++)
		{	if(nIndex%2==0)
			{  nHours+=str1[nIndex];
			}else
			{   nMinutes+=str1[nIndex];
				if(nMinutes>59)
				{	nHours++;
					nMinutes -=60;
				}
				
			}
		}
		
		
		  Calendar cl = Calendar.getInstance();
		  cl.set(Calendar.HOUR_OF_DAY,(int)nHours);
		  cl.set(Calendar.MINUTE, (int)nMinutes);
		  System.out.println("New Times "+cl.getTime());
		  
		  System.out.println("Hours"+nHours);
		  System.out.println("Minutes "+nMinutes);
		  
		  StringBuffer OSstr_hour_minutes = new StringBuffer(String.valueOf(nHours)); 
		  OSstr_hour_minutes.append(".").append(String.valueOf(nMinutes));
		  headQuarterTotal = OSstr_hour_minutes.toString();
		   
		  OShour_minutes_Percentage(OSstr_hour_minutes.toString());
		  
		  
		  System.out.println("----------------------------OS END---------------------");
	}
	
	
	private void OShour_minutes_Percentage(String OSstr_hour_minutes) {
		
		float nPer2Weeks  = 14.0f;
		float nPerDays = 24.0f;
		System.out.println("---------inside OS Percentage   ---------------");
		System.out.println(""+OSstr_hour_minutes);
		String str []  = OSstr_hour_minutes.split(Pattern.quote("."));
		int nHour  = Integer.parseInt(str[0]);
		int nMinutes  = Integer.parseInt(str[1]);
		
		
		System.out.println("Hour =="+nHour);
		System.out.println("Minutes  = "+nMinutes);
		
		
		float min = (float) Math.round(nHour * 60);
		float nCompMin = nMinutes+min;
		System.out.println("Total Minutes "+nCompMin);
		
		float per14Days  = (nPer2Weeks*nPerDays)/nCompMin*100;
		
		DecimalFormat df = new DecimalFormat("##.##");
		df.setRoundingMode(RoundingMode.DOWN);
		String Total2Weeks  = df.format(per14Days);
		
		StringBuffer br=new StringBuffer(Total2Weeks);
		br.append("%");
		
		System.out.println("OS PER  = ="+br.toString());
		osRestPer14DaysPercentage = br.toString();
		System.out.println("---------End of  Percentage ---------------");
	
		
	}



	public void headQuarterRest()
	{	try {
			List<DrivingDutyOfPilotDrivingSection> DrivingSectionList = (List<DrivingDutyOfPilotDrivingSection>) this.schoolService.loadList("from DrivingDutyOfPilotDrivingSection as dds where dds.customer.id="+this.loginId);
			System.out.println("----------------------------HQ---------------------");
			
			System.out.println("Size is  = ="+DrivingSectionList.size());
			String str [] = new String[DrivingSectionList.size()];
			
			for(int nIndex = 0;nIndex<DrivingSectionList.size();nIndex++)
			{
				if(DrivingSectionList.get(nIndex).getHqRest()!=null)
				{
					str [nIndex] = DrivingSectionList.get(nIndex).getHqRest();	
				}
				  
				    
			}
			
			ArrayList<String> arraylist = new ArrayList<String>();
			for(int nIndex = 0;nIndex<str.length;nIndex++)
			{	if(str[nIndex]!=null)
				{	StringTokenizer tok = new StringTokenizer(str[nIndex], ":");
					while (tok.hasMoreElements()) {
						String str1 = (String) tok.nextElement();
						 arraylist.add(str1);
					}	
				}
				
				
			}
			
			headQuarter_minutes_hour_Summation(arraylist);

		
			
			//System.out.println("The Size Of ArrayList  = ="+arraylist.size());
			
		} catch (CrewException e)
		{
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
		
		
	}
	
	public void headQuarter_minutes_hour_Summation(ArrayList<String> arraylist2)
	{	int nCount = 0;
		int str1[]= new int[arraylist2.size()]; 
		for (String str : arraylist2) {
			str1[nCount++] = Integer.parseInt(str);
		}
		int nMinutes  = 0;
		int nHours  = 0;
		for(int nIndex = 0;nIndex<str1.length;nIndex++)
		{	if(nIndex%2==0)
			{  nHours+=str1[nIndex];
			}else
			{   nMinutes+=str1[nIndex];
				if(nMinutes>59)
				{	nHours++;
					nMinutes -=60;
				}
				
			}
		}
		
		
		  Calendar cl = Calendar.getInstance();
		  cl.set(Calendar.HOUR_OF_DAY,(int)nHours);
		  cl.set(Calendar.MINUTE, (int)nMinutes);
		  System.out.println("New Times "+cl.getTime());
		  
		  System.out.println("Hours"+nHours);
		  System.out.println("Minutes "+nMinutes);
		  StringBuffer str_hour_minutes = new StringBuffer(String.valueOf(nHours)); 
		  str_hour_minutes.append(".").append(String.valueOf(nMinutes));
		  headQuarterTotal = str_hour_minutes.toString();
		   
		  hour_minutes_Percentage(str_hour_minutes.toString());
		   System.out.println("----------------------------End HQ---------------------");
			 
		   
		
		
	}
	
	
	public void hour_minutes_Percentage(String headQuarterTotal2)
	{
		
		float nPer2Weeks  = 14.0f;
		float nPerDays = 24.0f;
		System.out.println("---------inside Percentage ---------------");
		System.out.println(""+headQuarterTotal2);
		String str []  = headQuarterTotal2.split(Pattern.quote("."));
		int nHour  = Integer.parseInt(str[0]);
		int nMinutes  = Integer.parseInt(str[1]);
		
		
		System.out.println("Hour =="+nHour);
		System.out.println("Minutes  = "+nMinutes);
		
		
		float min = (float) Math.round(nHour * 60);
		float nCompMin = nMinutes+min;
		System.out.println("Total Minutes "+nCompMin);
		
		float per14Days  = (nPer2Weeks*nPerDays)/nCompMin*100;
		
		DecimalFormat df = new DecimalFormat("##.##");
		df.setRoundingMode(RoundingMode.DOWN);
		String Total2Weeks  = df.format(per14Days);
		
		StringBuffer br=new StringBuffer(Total2Weeks);
		br.append("%");
		
		headQuarter14DaysPercentage = br.toString();
		System.out.println("HQ HOUR PERCENTAGE= ="+headQuarter14DaysPercentage);
		
		
		
		
		
		
		
		//double hours = min / 60d; // Note the "d is stands for double of the Value "
		
		//System.out.println(""+hours);
		
		System.out.println("---------End of  Percentage ---------------");
	}
	
	

		
		  
		 	
		
		
		// random number to store the same Sequence Id in DrivingSection Table and CrewLink Table which matches Number of Driving Section
		// for Single CrewLink
		public int getSequenceRandomNumber() {
			 Random r = new Random(System.currentTimeMillis());
			 return (1 + r.nextInt(2)) * 10 + r.nextInt(10);
		}
		
		
		
		public void roundTripTrainSepreated(String crewBase,String crewType,long crewSequenceId) throws CrewException
		{
			List<CrewChangeDrivingsection> crewchangeDrivingsectionList  = new ArrayList<CrewChangeDrivingsection>(); 
			List<String> removed= new ArrayList<String>();
			for (Map.Entry<String, Long> entry : mapSorted.entrySet())
			{ RoundTrip rr = (RoundTrip) schoolService.loadUniqueObject("from RoundTrip as RR where RR.id="+entry.getValue());
				StringTokenizer token = new StringTokenizer(rr.getTrainNo(),",+,");
				while(token.hasMoreElements())
				  { String removedTrains = (String) token.nextElement(); 
					removed.add(removedTrains);
				    System.out.println(""+removedTrains);
				  }
			}
			List<CrewChangeDrivingsection> crewList = new ArrayList<CrewChangeDrivingsection>();

			for(int n = 0;n<removed.size();n++)
			{	crewchangeDrivingsectionList   = (List<CrewChangeDrivingsection>) schoolService.loadList("from CrewChangeDrivingsection as ccds where ccds.trainNo like '"+removed.get(n)+"' and ccds.crewBase like '"+crewBase+"' and ccds.crewType like '"+crewType+"' ");
				crewList.addAll(crewchangeDrivingsectionList);
			}
			 for (Map.Entry<String, Long> entry : mapSeqNoPlusId.entrySet()) 
			 {   System.out.println("Key = " + entry.getKey() + " Value = " + entry.getValue());
		     }
			List<RoundTrip> rrlist  = (List<RoundTrip>) context.getBean("list");
			for (Map.Entry<Long, String> entry : mapIdAndAvailable.entrySet()) 
			 {  rrlist = (List<RoundTrip>) schoolService.loadList("from RoundTrip rr where rr.id ="+entry.getKey());
			 
		     }
		
			
			List<Periodic> periodicList =new ArrayList<Periodic>();
			
			if(!rrlist.isEmpty())
			{	
				StringTokenizer token = new StringTokenizer(rrlist.get(0).getTrainNo(),",+,");
				while(token.hasMoreElements())
					{   String independentsTrains = (String) token.nextElement(); 
					    periodicList.add(new Periodic(independentsTrains,rrlist.get(0).getCrewBase(),rrlist.get(0).getCrewType()));
				    }
			}
			
			
			 
			CrewChangeDrivingsection crewChange= (CrewChangeDrivingsection) context.getBean("crewChange");
			for(Periodic per :periodicList)
			{	
				crewChange = (CrewChangeDrivingsection) schoolService.loadUniqueObject("from CrewChangeDrivingsection as ccds where ccds.trainNo like '"+per.getTrainNumbers()+"' AND ccds.crewBase like '"+per.getCrewBase()+"' AND ccds.crewType like '"+per.getCrewType()+"'");
				crewChange.setPeriodicRest(true);
				schoolService.updateObjectInDb(crewChange);
			}
			for(CrewChangeDrivingsection rr :crewList)
			{	CrewChangeDrivingsection d  = (CrewChangeDrivingsection)schoolService.loadUniqueObject("from CrewChangeDrivingsection as ccds where ccds.trainNo like '"+rr.getTrainNo()+"' and ccds.crewBase like '"+crewBase+"' and ccds.crewType like '"+crewType+"' ");;	 
			    d.setCrewLinkSequence(crewSequenceId);
			    schoolService.updateObjectInDb(d);
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
		

		
		
		
	/// End SAN METHODS  
		
		
		
		
		
		
		
		
		

		///  all getter and setter
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

		public ApplicationContext getContext() {
			return context;
		}

		public void setContext(ApplicationContext context) {
			this.context = context;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		public static Logger getCrewLogger() {
			return CREW_LOGGER;
		}



		public String getDrivingsectionsequence() {
			return drivingsectionsequence;
		}



		public void setDrivingsectionsequence(String drivingsectionsequence) {
			this.drivingsectionsequence = drivingsectionsequence;
		}



		public String[] getDsectionarray() {
			return dsectionarray;
		}



		public void setDsectionarray(String[] dsectionarray) {
			this.dsectionarray = dsectionarray;
		}



		public List<RoundTrip> getRoundTripList() {
			return roundTripList;
		}



		public void setRoundTripList(List<RoundTrip> roundTripList) {
			this.roundTripList = roundTripList;
		}



		public Boolean getbSelectedCheckBox() {
			return bSelectedCheckBox;
		}



		public Long[] getlSelectedValue() {
			return lSelectedValue;
		}



		public void setlSelectedValue(Long[] lSelectedValue) {
			this.lSelectedValue = lSelectedValue;
		}



		public void setbSelectedCheckBox(Boolean bSelectedCheckBox) {
			this.bSelectedCheckBox = bSelectedCheckBox;
		}



		public Map<Long,String> getMapIdAndAvailable() {
			return mapIdAndAvailable;
		}



		public void setMapIdAndAvailable(Map<Long,String> mapIdAndAvailable) {
			this.mapIdAndAvailable = mapIdAndAvailable;
		}



		public List<SelectItem> getRoundtripSelectItemFrom() {
			return roundtripSelectItemFrom;
		}



		public void setRoundtripSelectItemFrom(List<SelectItem> roundtripSelectItemFrom) {
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



		public String getRoundTripTo1() {
			return roundTripTo1;
		}



		public void setRoundTripTo1(String roundTripTo1) {
			this.roundTripTo1 = roundTripTo1;
		}



		public long getLoginId() {
			return loginId;
		}



		public void setLoginId(long loginId) {
			this.loginId = loginId;
		}



		public long getAverage_Kms_per_month() {
			return average_Kms_per_month;
		}



		public void setAverage_Kms_per_month(long average_Kms_per_month) {
			this.average_Kms_per_month = average_Kms_per_month;
		}



	




		public String getHeadQuarterTotal() {
			return headQuarterTotal;
		}



		public void setHeadQuarterTotal(String headQuarterTotal) {
			this.headQuarterTotal = headQuarterTotal;
		}



		public String getHeadQuarter14DaysPercentage() {
			return headQuarter14DaysPercentage;
		}



		public void setHeadQuarter14DaysPercentage(String headQuarter14DaysPercentage) {
			this.headQuarter14DaysPercentage = headQuarter14DaysPercentage;
		}



		public String getOsRestTotal() {
			return osRestTotal;
		}



		public void setOsRestTotal(String osRestTotal) {
			this.osRestTotal = osRestTotal;
		}



		public String getOsRestPer14DaysPercentage() {
			return osRestPer14DaysPercentage;
		}



		public void setOsRestPer14DaysPercentage(String osRestPer14DaysPercentage) {
			this.osRestPer14DaysPercentage = osRestPer14DaysPercentage;
		}



		public List<CrewChangeDrivingsection> getCrewList() {
			return crewList;
		}



		public void setCrewList(List<CrewChangeDrivingsection> crewList) {
			this.crewList = crewList;
		}



		public Double getdAvgKM() {
			return dAvgKM;
		}



		public void setdAvgKM(Double dAvgKM) {
			this.dAvgKM = dAvgKM;
		}



		public Integer getnRunningTime() {
			return nRunningTime;
		}



		public void setnRunningTime(Integer nRunningTime) {
			this.nRunningTime = nRunningTime;
		}



		public Double getnTotalRunningTime() {
			return nTotalRunningTime;
		}



		public void setnTotalRunningTime(Double nTotalRunningTime) {
			this.nTotalRunningTime = nTotalRunningTime;
		}



		public List<RoundTrip> getSelectionList() {
			return selectionList;
		}



		public void setSelectionList(List<RoundTrip> selectionList) {
			this.selectionList = selectionList;
		}



	


	
}