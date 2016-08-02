package com.crew.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

import com.crew.exception.CrewException;
import com.crew.model.CrewChangeDrivingsection;
import com.crew.model.Customer;
import com.crew.model.DrivingDutyOfPilotDrivingSection;
import com.crew.model.PilotJourney;
import com.crew.model.TransactionIdGenerator;
import com.crew.service.CrewService;
import com.crew.util.CrewUtilities;

@ManagedBean(name = "oneDrivingbean", eager = true)
@ViewScoped
@SuppressWarnings("unchecked")
public class OneDrivingSectionOneDrivingDuty implements Serializable {

	private static final long serialVersionUID = 1L;
	private ApplicationContext context;
	private List<CrewChangeDrivingsection> crewChangeDrivingsectionList;
	@ManagedProperty(name = "schoolService", value = "#{schoolService}")
	private CrewService schoolService;
	private HttpSession httpSession = (HttpSession) FacesContext
			.getCurrentInstance().getExternalContext().getSession(false);
	private static final Logger CREW_LOGGER = Logger
			.getLogger(OneDrivingSectionOneDrivingDuty.class);

	private Map<Long, String> oneoneDrivingsectionMap = new HashMap<Long, String>();

	private String strTransId = null;
	private int count = 0;
	private List<CrewChangeDrivingsection> selectedCrewChangeDrivingSection;
	private DrivingDutyOfPilotDrivingSection DDS;
	private List<DrivingDutyOfPilotDrivingSection> DrivingDutylist;

	// all filter values declarations

	private List<CrewChangeDrivingsection> filterDrivingSection;
	private TransactionIdGenerator transactionId;
	private CrewChangeDrivingsection updatedStatus;
	private Long customerloginId;
	
	private String selectedFrom;
	private String selectedTo;
	private String selectedFrom1;
	private String selectedTo1;
	private List<SelectItem> selectlistfrom_station;
	private List<SelectItem> To_stationlist; 
	
	
	 private List<SelectItem>selectListviewFrom_station;
	 private List<SelectItem>selectListviewTo_station;
	 private String selectedviewFrom;
	 private String selectedviewTo;
	 private String selectedviewFrom1;
	 private String selectedviewTo1; 
	
	
	 

	 @PostConstruct
		private void initilization() {

			if (!(httpSession.getAttribute("COMMONLOGINID") == null)) {
				customerloginId = (Long) httpSession.getAttribute("COMMONLOGINID");
				CreateAllList();
				loadlist();
				commonselectOnedrivingSections();
				
			
			} else {
				
				System.out.print("bean else Part");
				try {
					FacesContext.getCurrentInstance().getExternalContext()
							.redirect(CrewUtilities.getRedirectContext() + "/login.xhtml");
				} catch (Exception e) {
					CREW_LOGGER.debug(e.getMessage());
				}

			}

		}

	private void CreateAllList() {

		context 						= new ClassPathXmlApplicationContext("Beans.xml");
		crewChangeDrivingsectionList 	= (List<CrewChangeDrivingsection>) context.getBean("list");
		DDS 							= (DrivingDutyOfPilotDrivingSection) context.getBean("drivingdutyofpilotdrivingsection");
		filterDrivingSection 			= (List<CrewChangeDrivingsection>) context.getBean("list");
		transactionId 					= (TransactionIdGenerator) context.getBean("transactionidgenerator");
		updatedStatus 					= (CrewChangeDrivingsection) context.getBean("crewChange");
		
		
		
		 selectlistfrom_station 		=   new  ArrayList<SelectItem>();
		 To_stationlist 				=   new ArrayList<SelectItem>();
		 selectListviewFrom_station 	= 	new ArrayList<SelectItem>();
		 selectListviewTo_station  		= 	new ArrayList<SelectItem>();
		 
		
		
	}

	public void loadlist() {
		try {
			crewChangeDrivingsectionList = (List<CrewChangeDrivingsection>) this.schoolService
					.loadList("from CrewChangeDrivingsection as ccds where ccds.customer.id="+customerloginId);
			filterDrivingSection = (List<CrewChangeDrivingsection>) this.schoolService
					.loadList("from CrewChangeDrivingsection as ccds where ccds.customer.id="+customerloginId);

		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
	}

	public void addUniqueIdToPilotTable(Map<String, Long> mapPilotId2) {

		for (Entry<String, Long> entry : mapPilotId2.entrySet()) {

			PilotJourney ps = (PilotJourney) context.getBean("pilotJourney");

			try {
				ps = (PilotJourney) schoolService
						.loadUniqueObject("from PilotJourney as PJ where PJ.id="
								+ entry.getValue());
				ps.setSequenceId(entry.getKey());
				schoolService.updateObjectInDb(ps);
			} catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());

			}

		}

	}
	
	
	@SuppressWarnings("unused")
	private boolean IsExistDrivingDuty() {
		Boolean bTrue = Boolean.FALSE;
		if (selectedCrewChangeDrivingSection.size() > 0) {

			List<DrivingDutyOfPilotDrivingSection> drivingSectionlist = null;
			try {
				drivingSectionlist = (List<DrivingDutyOfPilotDrivingSection>) schoolService
						.loadList("from DrivingDutyOfPilotDrivingSection");
			} catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
			}

			for (int nIndex = 0; nIndex < drivingSectionlist.size(); nIndex++) {
				if (selectedCrewChangeDrivingSection
						.get(nIndex)
						.getToStation()
						.equalsIgnoreCase(
								drivingSectionlist.get(nIndex).getToStation())) {
					bTrue = Boolean.TRUE;
				}
			}
		}

		return bTrue;

	}
	
	
	
	
	
	
	
	

	

	public String generateOneDrivingSection_and_OneDrivingDuty() {
		int idiffInMin = 0;
		int iDiffInHour = 0;
		Integer nTotalHQRest = 0;
		Integer nTotalOSRest = 0;
		if (selectedCrewChangeDrivingSection.size() > 0) {
			Date signOncalendar = new Date();
			Date signOffcalendar = new Date();

			Calendar calendar1 = Calendar.getInstance();
			Calendar calendar2 = Calendar.getInstance();
			for (CrewChangeDrivingsection CCD : selectedCrewChangeDrivingSection) {
				
				DDS.setTrainNo(CCD.getTrainNo());
				DDS.setArrival(CCD.getArrival());
				DDS.setFromStation(CCD.getFromStation());
				DDS.setToStation(CCD.getToStation());
				DDS.setDeparture(CCD.getDeparture());
				DDS.setKm(CCD.getKilometer());
				DDS.setCrewBase(CCD.getCrewBase());
				DDS.setCrewType(CCD.getCrewType());
				String strCrewBase = CCD.getCrewBase();
				
				/*-----signON--------- */

				signOncalendar = CCD.getDeparture();
				signOffcalendar = CCD.getArrival();
				System.out.println("signOncalenda->>> " +signOncalendar);
				System.out.println("signOffcalendar->>> " +signOffcalendar);
				
				calendar1.setTime(signOncalendar);
				calendar1.add(Calendar.MINUTE, -30);  // signOn = dep-30m
				DDS.setSignonTime(calendar1.getTime());
				// --------signOn Time END----//

				/*-----signOff--------- */
				calendar2.setTime(signOffcalendar);
				calendar2.add(Calendar.MINUTE, 30); // sigonoff= arrival+30m
				DDS.setSignoffTime(calendar2.getTime());
			
				
				signOncalendar = calendar1.getTime();
				signOffcalendar = calendar2.getTime();
				
				
				
				System.out.print(""+signOncalendar.getHours()+":"+signOncalendar.getMinutes());
				
				System.out.print(""+signOffcalendar.getHours()+":"+signOffcalendar.getMinutes());
				
				
				idiffInMin = calculateDiffBetweenAvailableAndDeparture(signOncalendar,signOffcalendar);
				iDiffInHour = idiffInMin / 60;
				System.out.println("idiffInMin->>> " +idiffInMin);
				System.out.println("iDiffInHour->>> " +iDiffInHour);
								Date dtAvailability = new Date();
				String strTotalHour = new String();
				System.out.println("------------------------------------");
				
				if(strCrewBase.equalsIgnoreCase(CCD.getToStation()))   // Checking for HeadQuarter(Base Station) in the List 
				{
					
					System.out.println("Inside HeadQuarter");
					if(iDiffInHour>=8)
					{
						int addedHours_greaterthen_8Hours = 960;  // 16 hours 16*60 = 960
						nTotalHQRest = addedHours_greaterthen_8Hours;
						
						calendar1.setTime(signOffcalendar);
						calendar1.add(Calendar.MINUTE,addedHours_greaterthen_8Hours);  /// 16 hrs HeadQuarter Rest
						
						
						dtAvailability=calendar1.getTime();
						System.out.println("signOffTime" +signOffcalendar);
						System.out.println(">8 availabilityDate " +dtAvailability);
						int iAvailableHour = calendar1.get(Calendar.HOUR_OF_DAY);
						int iAvailableMinute = calendar1.get(Calendar.MINUTE);
						String strAvailableHour = String.valueOf(iAvailableHour);
						String strAvailableMinute = String.valueOf(iAvailableMinute);
						strTotalHour = strAvailableHour.concat(":" + strAvailableMinute);
						DDS.setAvailability(strTotalHour);
						DDS.setHqRest(String.valueOf(addedHours_greaterthen_8Hours));
						
						
						
						System.out.println(">8 strTotalHour in hq and os" +strTotalHour);
					}
					else if(iDiffInHour<8)
					{
						
						int addedHour_lessthan_8 = 720;   ////  12 hours HQREST  therefore 12*60 = 720
						calendar1.setTime(signOffcalendar);
						nTotalHQRest = addedHour_lessthan_8;
						calendar1.add(Calendar.MINUTE, addedHour_lessthan_8); /// 12 hrs HeadQuarter Rest
						dtAvailability=calendar1.getTime();
						System.out.println("signOffTime" +dtAvailability);
						System.out.println("<8 availabilityDate " +dtAvailability);
						int iAvailableHour = calendar1.get(Calendar.HOUR_OF_DAY);
						int iAvailableMinute = calendar1.get(Calendar.MINUTE);
						
						String strAvailableHour = String.valueOf(iAvailableHour);
						String strAvailableMinute = String.valueOf(iAvailableMinute);
						
						strTotalHour = strAvailableHour.concat(":" + strAvailableMinute);
						DDS.setAvailability(strTotalHour);
						DDS.setHqRest(String.valueOf(addedHour_lessthan_8));
						System.out.println("<8 strTotalHour in hq and os  " +strTotalHour);
						
					}
				}
			
				else 
				{
					if(iDiffInHour>=8) // Checking for OS(Out Station) in the List
					{
						int addeddHour_OS_greaterthan8 = 480; // 8 Hours outstation Rest 8*60 = 480
						nTotalOSRest =  addeddHour_OS_greaterthan8;
						calendar1.setTime(signOffcalendar);
						calendar1.add(Calendar.MINUTE, addeddHour_OS_greaterthan8); /// 8 hrs HeadQuarter Rest
						dtAvailability=calendar1.getTime();
						System.out.println("signOffTime" +signOffcalendar);
						System.out.println("availabilityDate" +dtAvailability);
						
						int iAvailableHour = calendar1.get(Calendar.HOUR_OF_DAY);
						int iAvailableMinute = calendar1.get(Calendar.MINUTE);
						
						String strAvailableHour = String.valueOf(iAvailableHour);
						String strAvailableMinute = String.valueOf(iAvailableMinute);
						
						strTotalHour = strAvailableHour.concat(":" + strAvailableMinute);
						DDS.setAvailability(strTotalHour);
						DDS.setOsRest(String.valueOf(addeddHour_OS_greaterthan8));
						System.out.println(">8 strTotalHour only in os" +strTotalHour);
					}
					else if(iDiffInHour<8 && iDiffInHour>5)
					{
						int addeddHour_OS = 360; // 6 hrs  Os rest 6*60 = 360
						calendar1.setTime(signOffcalendar);
						nTotalOSRest =  addeddHour_OS;
						calendar1.add(Calendar.MINUTE, addeddHour_OS); /// 6 hrs OS Rest
						dtAvailability=calendar1.getTime();
						System.out.println("signOffTime" +signOffcalendar);
						System.out.println("availabilityDate" +dtAvailability);
						
						int iAvailableHour = calendar1.get(Calendar.HOUR_OF_DAY);
						int iAvailableMinute = calendar1.get(Calendar.MINUTE);
						
						String strAvailableHour = String.valueOf(iAvailableHour);
						String strAvailableMinute = String.valueOf(iAvailableMinute);
						
						strTotalHour = strAvailableHour.concat(":" + strAvailableMinute);
						DDS.setAvailability(strTotalHour);
						DDS.setOsRest(String.valueOf(addeddHour_OS));
						System.out.println(">8 strTotalHour only in os" +strTotalHour);
						
					}
					else if(iDiffInHour<=5)
					{
						
						int addeddHour_OS_lessthan5 = iDiffInHour*60+60;
						nTotalOSRest =  addeddHour_OS_lessthan5; // signOff+duty+1hrs
						calendar1.setTime(signOffcalendar);
						calendar1.add(Calendar.MINUTE,iDiffInHour*60+60); // duty hour + 1hrs
						dtAvailability=calendar1.getTime();
						System.out.println("signOffTime" +signOffcalendar);
						System.out.println("availabilityDate" +dtAvailability);
						
						int iAvailableHour = calendar1.get(Calendar.HOUR_OF_DAY);
						int iAvailableMinute = calendar1.get(Calendar.MINUTE);
						
						String strAvailableHour = String.valueOf(iAvailableHour);
						String strAvailableMinute = String.valueOf(iAvailableMinute);
						
						strTotalHour = strAvailableHour.concat(":" + strAvailableMinute);
						DDS.setAvailability(strTotalHour);
						DDS.setOsRest(String.valueOf(addeddHour_OS_lessthan5));
						
						System.out.println("<5 strTotalHour only in os" +strTotalHour);
						
					}
				}
				
				
			
				
				/*----------- End of Avail Time ------------- */
				/*---------------end of headQuarter Rest ---------------------   */
				DDS.setHqRest(nTotalHQRest.toString());
				DDS.setOsRest(nTotalOSRest.toString());
				Long l = generateTransactionID();
				DDS.setTransactionNoDsToDd(l);
				try {
					DDS.setCustomer((Customer)schoolService.loadUniqueObject("from Customer as cst where cst.id="+customerloginId));
				} catch (CrewException e2) {
					CREW_LOGGER.debug(e2.getMessage());
					e2.printStackTrace();
				}

				
				CrewChangeDrivingsection ds = (CrewChangeDrivingsection) context
						.getBean("crewChange");

				try {
					schoolService.saveInDb(DDS);
					nTotalHQRest = 0;
					nTotalOSRest = 0;
					System.out.println("nTotalHQRest ->     " + nTotalHQRest);
					System.out.println("nTotalOSRest ->     " + nTotalOSRest);
					ds = (CrewChangeDrivingsection) schoolService
							.loadUniqueObject("from CrewChangeDrivingsection as CD where CD.id="
									+ CCD.getId());
					ds.setSequenceId(CCD.getId().toString());
					ds.setTransactionNoDsToDd(l);
					ds.setStatus(true);
					schoolService.updateObjectInDb(ds);
					
				} catch (CrewException e1) {
				    CREW_LOGGER.debug(e1.getMessage());
					e1.printStackTrace();
				}

				
				try {
					schoolService.updateObjectInDb(updatedStatus);
					l++;
					transactionId.setTransactionId(l);
					schoolService.saveInDb(transactionId);
				} catch (Exception e) {

					CREW_LOGGER.debug(e.getMessage());
					e.printStackTrace();
				}

			}

		} else {
			System.out.print("select atleast One Rows");
		}
		
		//return "drivingduty.xhtml?faces-redirect=true";
		return "";
	}
	
	

	
	public Integer calculateDiffBetweenAvailableAndDeparture(Date dtSignOn,Date dtSignOff)
	{
		System.out.println("Inside Differnce between Avail and Dep-----------------------------------------------");
		System.out.println("dtSignOn ->" + dtSignOn);
		System.out.println("dtSignOff ->" + dtSignOff);
		System.out.println("-----------------------------------------------");
		
		Calendar calSignOn = Calendar.getInstance();
		Calendar calSignOff = Calendar.getInstance();
		calSignOn.setTime(dtSignOn);
		calSignOff.setTime(dtSignOff);
		
		Integer iDiffInMinutes = new Integer(0);
		Integer iSignOnHour = calSignOn.get(Calendar.HOUR_OF_DAY);
		Integer iSignOnMinute = calSignOn.get(Calendar.MINUTE);
		
		
		Integer iSignOffHour = calSignOff.get(Calendar.HOUR_OF_DAY);
		Integer iSignOffMinute = calSignOff.get(Calendar.MINUTE);
		if(iSignOnHour==0) iSignOnHour = 24;
		if(iSignOffHour==0) iSignOffHour = 24;
		
		if(iSignOnHour<iSignOffHour)
		{
			System.out.println("Hour of Departure -> " + iSignOnHour +"\t Minute of Departure-> " +iSignOnMinute);
			System.out.println("Hour of Departure -> " + iSignOffHour +"\t Minute of Departure-> " +iSignOffMinute);
			
			iDiffInMinutes =  (iSignOffHour*60 + iSignOffMinute) - (iSignOnHour * 60 +iSignOnMinute);
			System.out.println("Diff In minutes dep<arr -> " + iDiffInMinutes);
		}
		else
		{
			System.out.println("Hour of Departure -> " + iSignOnHour +"\t Minute of Departure-> " +iSignOnMinute);
			System.out.println("Hour of Departure -> " + iSignOffHour +"\t Minute of Departure-> " +iSignOffMinute);
			
			iDiffInMinutes = (iSignOffHour*60 + iSignOffMinute) + 24* 60 - (iSignOnHour * 60 +iSignOnMinute);
			System.out.println("Diff In minutes dep>arr -> " + iDiffInMinutes);
		}
		
		return iDiffInMinutes;
	}
	@SuppressWarnings("unused")
	private Map<Long, String> CheckforHeadQuarter() {

		Map<Long, String> headQuarterMap = new HashMap<Long, String>();
		List<CrewChangeDrivingsection> CrewChangeDrivingsectionList = null;
		try {
			CrewChangeDrivingsectionList = (List<CrewChangeDrivingsection>) schoolService
					.loadList("from CrewChangeDrivingsection");

			for (CrewChangeDrivingsection COS : CrewChangeDrivingsectionList) {
				headQuarterMap.put(COS.getId(), COS.getCrewBase());
			}

		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
		return headQuarterMap;

	}

	private Long generateTransactionID() {

		Long lTransactionId = new Long(0);

		List<TransactionIdGenerator> listTransactionIdGenerator = (List<TransactionIdGenerator>) context
				.getBean("list");
		try {
			listTransactionIdGenerator = (List<TransactionIdGenerator>) schoolService
					.loadList("from TransactionIdGenerator");

			System.out.println("size of listTransactionIdGenerator"
					+ listTransactionIdGenerator.size());
			Long l = new Long(10000);
			if (listTransactionIdGenerator.isEmpty()) {
				transactionId.setTransactionId(l);
				schoolService.saveInDb(transactionId);
			} else {
				List<Long> listTransactioIds = new LinkedList<Long>();
				for (TransactionIdGenerator t : listTransactionIdGenerator) {
					listTransactioIds.add(t.getTransactionId());
				}
				Collections.sort(listTransactioIds);

				for (int i = 0; i < listTransactioIds.size(); i++) {
					if (i == listTransactioIds.size() - 1) {
						lTransactionId = listTransactioIds.get(i);
					}
				}
			}

		} catch (CrewException e) {
			e.printStackTrace();
			CREW_LOGGER.debug(e.getMessage());
		}
		return lTransactionId;

	}
	
	
	
	//// search Option
	
	
	
	public void commonselectOnedrivingSections() {
		List<CrewChangeDrivingsection> from_stationlist = new ArrayList<CrewChangeDrivingsection>();;
		
		 HashMap<String,String> to_station=new HashMap<String,String>();  
		 HashMap<String,String> from_station=new HashMap<String,String>();
		
		  
		
		
		try {
			from_stationlist = (List<CrewChangeDrivingsection>) schoolService
					.loadList("from CrewChangeDrivingsection");
			
			if(!from_stationlist.isEmpty())
			{
			for(CrewChangeDrivingsection CCD:from_stationlist)
			{
				to_station.put(CCD.getToStation(), CCD.getToStation());
				from_station.put(CCD.getFromStation(), CCD.getToStation());
			}
			
			 for(Map.Entry<String,String> m:from_station.entrySet()){  
				   selectlistfrom_station.add(new SelectItem(m.getKey()));
				  }  
			
			 
			 for(Map.Entry<String,String> to_entry:to_station.entrySet()){  
				   To_stationlist.add(new SelectItem(to_entry.getKey()));
				  }  
			}
			else
			{
				
			}
			

		} catch (CrewException e) {
			e.printStackTrace();
		}

	}

	
	public void  multipleSearch()
	{
		
		System.out.println(""+selectedFrom);
		System.out.println(""+selectedTo);
		System.out.println(""+selectedFrom1);
		System.out.println(""+selectedTo1);
		
		
		 int nChoice;
		 if(selectedFrom!=null||selectedTo!=null||selectedFrom1!=null || selectedTo1!=null)
			{	
			
			 if(selectedFrom!=null && !selectedFrom.isEmpty() && selectedTo!=null && !selectedTo.isEmpty() && selectedFrom1!=null && !selectedFrom1.isEmpty() && selectedTo1!=null && !selectedTo1.isEmpty())
				{  crewChangeDrivingsectionList = (List<CrewChangeDrivingsection>) schoolService.loadReverseList2(selectedFrom, selectedTo,selectedFrom1,selectedTo1);
			    }
			 else if(selectedFrom!=null && !selectedFrom.isEmpty() && selectedTo!=null && !selectedTo.isEmpty())
			{	System.out.print("Above Two");
			    crewChangeDrivingsectionList = (List<CrewChangeDrivingsection>) schoolService.loadReverseList(selectedFrom, selectedTo);
			    
			    System.out.println("Size of One Of One "+crewChangeDrivingsectionList.size());
			    
			} 
			else if(!selectedFrom.isEmpty())
			{ 
				 	nChoice = 1;
				    crewChangeDrivingsectionList = (List<CrewChangeDrivingsection>) schoolService.singleSearch(selectedFrom,nChoice);
			}
			else if(!selectedTo.isEmpty())
			{
					 nChoice = 2;
					 crewChangeDrivingsectionList = (List<CrewChangeDrivingsection>) schoolService.singleSearch(selectedTo,nChoice);
			}
			else if(!selectedFrom1.isEmpty())
			{
					nChoice = 3;
					crewChangeDrivingsectionList = (List<CrewChangeDrivingsection>) schoolService.singleSearch(selectedFrom1,nChoice);
			}
			else if(!selectedTo1.isEmpty())
			{
					nChoice = 4;
					crewChangeDrivingsectionList = (List<CrewChangeDrivingsection>) schoolService.singleSearch(selectedTo1,nChoice);
			}else if(selectedFrom1!=null && !selectedFrom1.isEmpty() && selectedTo1!=null && !selectedTo1.isEmpty())
			{	
			    crewChangeDrivingsectionList = (List<CrewChangeDrivingsection>) schoolService.loadReverseList(selectedFrom1,selectedTo1);
			} 
			else
			{
				
				nChoice = 5;
				crewChangeDrivingsectionList = (List<CrewChangeDrivingsection>) schoolService.singleSearch(null,nChoice);
			}
		   				
			}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	// // all getter and Setter are mentioned here

	public ApplicationContext getContext() {
		return context;
	}

	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	public List<CrewChangeDrivingsection> getCrewChangeDrivingsectionList() {
		return crewChangeDrivingsectionList;
	}

	public void setCrewChangeDrivingsectionList(
			List<CrewChangeDrivingsection> crewChangeDrivingsectionList) {
		this.crewChangeDrivingsectionList = crewChangeDrivingsectionList;
	}

	public String getStrTransId() {
		return strTransId;
	}

	public void setStrTransId(String strTransId) {
		this.strTransId = strTransId;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public static Logger getCrewLogger() {
		return CREW_LOGGER;
	}

	public List<CrewChangeDrivingsection> getSelectedCrewChangeDrivingSection() {
		return selectedCrewChangeDrivingSection;
	}

	public void setSelectedCrewChangeDrivingSection(
			List<CrewChangeDrivingsection> selectedCrewChangeDrivingSection) {
		this.selectedCrewChangeDrivingSection = selectedCrewChangeDrivingSection;
	}

	public Map<Long, String> getOneoneDrivingsectionMap() {
		return oneoneDrivingsectionMap;
	}

	public void setOneoneDrivingsectionMap(
			Map<Long, String> oneoneDrivingsectionMap) {
		this.oneoneDrivingsectionMap = oneoneDrivingsectionMap;
	}

	public DrivingDutyOfPilotDrivingSection getDDS() {
		return DDS;
	}

	public void setDDS(DrivingDutyOfPilotDrivingSection dDS) {
		DDS = dDS;
	}

	public List<DrivingDutyOfPilotDrivingSection> getDrivingDutylist() {
		return DrivingDutylist;
	}

	public void setDrivingDutylist(
			List<DrivingDutyOfPilotDrivingSection> drivingDutylist) {
		DrivingDutylist = drivingDutylist;
	}

	public List<CrewChangeDrivingsection> getFilterDrivingSection() {
		return filterDrivingSection;
	}

	public void setFilterDrivingSection(
			List<CrewChangeDrivingsection> filterDrivingSection) {
		this.filterDrivingSection = filterDrivingSection;
	}

	public CrewChangeDrivingsection getUpdatedStatus() {
		return updatedStatus;
	}

	public void setUpdatedStatus(CrewChangeDrivingsection updatedStatus) {
		this.updatedStatus = updatedStatus;
	}

	public TransactionIdGenerator getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(TransactionIdGenerator transactionId) {
		this.transactionId = transactionId;
	}

	public Long getCustomerloginId() {
		return customerloginId;
	}

	public void setCustomerloginId(Long customerloginId) {
		this.customerloginId = customerloginId;
	}

	public String getSelectedFrom() {
		return selectedFrom;
	}

	public void setSelectedFrom(String selectedFrom) {
		this.selectedFrom = selectedFrom;
	}

	public String getSelectedTo() {
		return selectedTo;
	}

	public void setSelectedTo(String selectedTo) {
		this.selectedTo = selectedTo;
	}

	public String getSelectedFrom1() {
		return selectedFrom1;
	}

	public void setSelectedFrom1(String selectedFrom1) {
		this.selectedFrom1 = selectedFrom1;
	}

	public String getSelectedTo1() {
		return selectedTo1;
	}

	public void setSelectedTo1(String selectedTo1) {
		this.selectedTo1 = selectedTo1;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<SelectItem> getSelectlistfrom_station() {
		return selectlistfrom_station;
	}

	public void setSelectlistfrom_station(List<SelectItem> selectlistfrom_station) {
		this.selectlistfrom_station = selectlistfrom_station;
	}

	public List<SelectItem> getTo_stationlist() {
		return To_stationlist;
	}

	public void setTo_stationlist(List<SelectItem> to_stationlist) {
		To_stationlist = to_stationlist;
	}

	public List<SelectItem> getSelectListviewFrom_station() {
		return selectListviewFrom_station;
	}

	public void setSelectListviewFrom_station(
			List<SelectItem> selectListviewFrom_station) {
		this.selectListviewFrom_station = selectListviewFrom_station;
	}

	public List<SelectItem> getSelectListviewTo_station() {
		return selectListviewTo_station;
	}

	public void setSelectListviewTo_station(
			List<SelectItem> selectListviewTo_station) {
		this.selectListviewTo_station = selectListviewTo_station;
	}

	public String getSelectedviewFrom() {
		return selectedviewFrom;
	}

	public void setSelectedviewFrom(String selectedviewFrom) {
		this.selectedviewFrom = selectedviewFrom;
	}

	public String getSelectedviewTo() {
		return selectedviewTo;
	}

	public void setSelectedviewTo(String selectedviewTo) {
		this.selectedviewTo = selectedviewTo;
	}

	public String getSelectedviewFrom1() {
		return selectedviewFrom1;
	}

	public void setSelectedviewFrom1(String selectedviewFrom1) {
		this.selectedviewFrom1 = selectedviewFrom1;
	}

	public String getSelectedviewTo1() {
		return selectedviewTo1;
	}

	public void setSelectedviewTo1(String selectedviewTo1) {
		this.selectedviewTo1 = selectedviewTo1;
	}

}
