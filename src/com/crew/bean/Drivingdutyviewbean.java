package com.crew.bean;


import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.event.data.PageEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.crew.exception.CrewException;
import com.crew.model.CrewChangeDrivingsection;
import com.crew.model.Customer;
import com.crew.model.DrivingDutyOfPilotDrivingSection;
import com.crew.model.FinalRoundtripSuggestion;
import com.crew.model.Finaltable;
import com.crew.model.HqOsRest;
import com.crew.model.Onedrivingsection;
import com.crew.model.PilotJourney;
import com.crew.model.RoundtripSuggestion2;
import com.crew.model.RoundtripSuggestions1;
import com.crew.model.RoundtripTemp;
import com.crew.model.TransactionIdGenerator;
import com.crew.service.CrewService;
import com.crew.util.CrewUtilities;



@ManagedBean(name="drivingbean",eager=true)
@ViewScoped
public class Drivingdutyviewbean implements Serializable {

	
	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "schoolService", value = "#{schoolService}")
	private CrewService schoolService;
	private HttpSession httpSession = (HttpSession) FacesContext
			.getCurrentInstance().getExternalContext().getSession(false);
	private static final Logger CREW_LOGGER = Logger.getLogger(Drivingdutyviewbean.class);
	
	private List<DrivingDutyOfPilotDrivingSection> selectionList;
	private ApplicationContext context;
	private List<Onedrivingsection> dlist;
	private List<CrewChangeDrivingsection> filteredCrewChangeList;
	private List<CrewChangeDrivingsection> filteredCrewChangeList2;
	private List<CrewChangeDrivingsection> filteredCrewChangeList3;
	private List<CrewChangeDrivingsection> filteredCrewChangeList4;
	private List<CrewChangeDrivingsection> filteredCrewChangeList5;
	private List<CrewChangeDrivingsection> filteredCrewChangeList6;
	private List<CrewChangeDrivingsection> filteredCrewChangeList7;
	private List<CrewChangeDrivingsection> filteredPilotTableList;
	private List<CrewChangeDrivingsection> filteredDrivingSectionTableList;
	private List<PilotJourney> filteredpilotTableList;
	
	private List<CrewChangeDrivingsection> crewChangeSelectedList;
	private List<PilotJourney> pilotTableObjectList;
	private List<CrewChangeDrivingsection> selectCrewChangeList;
	private Integer totalFilteredValues;
	private Long smallest = Long.MAX_VALUE;
	private Double avgDrivingSection;
	private Long totalfilteredHours = (long) 0;
	private Long totalfilteredDistance=(long) 0;
	
	 private Onedrivingsection[] oneSelection;
	 private Onedrivingsection updateOneDS;
	 private Onedrivingsection ODS;
	 
	 private boolean deletebuttonTrue;
	 private boolean updatebuttonTrue;
	 //jagdish
	 private List<CrewChangeDrivingsection> crewChangeDrivingsectionsList;
	 private List<Long> selectedId;
	 private List<PilotJourney> pilotjourneylist;
	 private List<Long> pilotIdList;
	 private List<Long> pilotCheckBoxList;
	 private List<Long> drivingSectionCheckBoxList;
	 private CrewChangeDrivingsection editCrewChange;
	 private long differenceDate;
	 private long diffInSecond;
	 private long diffInMinute;
	 private long diffInHour;
	 private long diffInDays;
	 private Onedrivingsection drivingDutyStore;
	 private DrivingDutyOfPilotDrivingSection drivingDutyOfPilotDrivingSectionsList;
	 private DrivingDutyOfPilotDrivingSection drivingDutyPilotDrivingSection;
	 private List<DrivingDutyOfPilotDrivingSection> loadDriDutyList;
	 private List<CrewChangeDrivingsection> loadCrewChangeList;
	 private String trainNo;
	 private List<DrivingDutyOfPilotDrivingSection> drivingPlusPilotList;
	 private String searchStationCode;
	 private List<DrivingDutyOfPilotDrivingSection> drivingDutyAfterSearchList;
	 private List<DrivingDutyOfPilotDrivingSection> drivingDutyFromStationsList;
	 private Map<String,String> arrivalMap = new HashMap<String,String>();
	 private List<Long> departureLong;
	 private Map<String, Long> departMap = new HashMap<String,Long>();
	 private boolean bToggleDisplay;
	 private Map<String,String> displayAllocationMap = new HashMap<String,String>();
	 private List<String> keyMapList;
	 private Integer numberOfCrews;
	 private String toStation;
	 private List<DrivingDutyOfPilotDrivingSection> fromAndToStationList;
	 private List<DrivingDutyOfPilotDrivingSection> toAndFromStationList;
	 private Finaltable optimizedResultObject;
	 private List<Finaltable> finalTableList;
	 private DrivingDutyOfPilotDrivingSection updateDriDutyObject;
	 private DrivingDutyOfPilotDrivingSection editDriDutyObject;
	 private List<Long> ids = new ArrayList<Long>();
	 private String strTotalId = new String();
	 private String strId[] = new String[10];
	 private String strCombineHourAndMinute;
	 
	 
 // new Decleration san
	 
	 private List<CrewChangeDrivingsection>crewChangeDrivingsectionList;
	 private List<PilotJourney>PilotJourneylist;
	 private String drivingsectionsequence;
	 private String pilotjourneysequence;
	 private String [] dsectionarray = new String[100];
	 
	 
	 private Map<String,Long> mapSeqNoPlusId= new LinkedHashMap<String,Long>();
	 private Long relativeId;
	 private Map<String,Long> mapPilotId = new LinkedHashMap<String,Long>();
	 
	 private String pTravelingSource;
	 private String pFrom_Station;
	 private String pTo_Station;
	 private String pDeparture;
	 private String pArrival;
	 private Long lTransId = new Long(0);
	 private int count = 0;
	 private Long KM;
	 private String totalFilteredHourAndMinutes;
	 private TransactionIdGenerator transactionId;
	 private DrivingDutyOfPilotDrivingSection selectedDrivingSection;
	 private List<DrivingDutyOfPilotDrivingSection> listDrivingDutyView;
	 
	 private DrivingDutyOfPilotDrivingSection selectedDuty;
	 //santosh
	 private String selectedFrom;
	 private String selectedTo;
	 private String selectedFrom1;
	 private String selectedTo1;
	 
	 private List<SelectItem> selectlistfrom_station;
	 private List<SelectItem> To_stationlist;
	 private String selectedStatus;
	 
 ///  driving duty view Search Boxes
	 
	 private List<SelectItem>selectListviewFrom_station;
	 private List<SelectItem>selectListviewTo_station;
	 private String selectedviewFrom;
	 private String selectedviewTo;
	 private String selectedviewFrom1;
	 private String selectedviewTo1; 
	 private String roundTripStatus;
	 
	 private String updateCrewbase;
	 private String updateCrewtype;
	 private CrewChangeDrivingsection crewchangedrivingsection;
	 private Long customerloginId;
	 
	 private List<CrewChangeDrivingsection> listCrewChange;
	 private List<PilotJourney> listPilotJourney;
	 private PilotJourney pilotjourneyupdate;  
	 
	 private List<SelectItem> editFromSelectone;
	 private List<SelectItem> editToSelectone;
	 
	 
	 private DrivingDutyOfPilotDrivingSection ddpsRest;
	 
	 private boolean bPilottable = Boolean.FALSE;
	 
	 // searchBase
	 private String searchBase;
	 private String searchType;
	 
	 private int iNumberOfSourceCrew;
	 private int iNumberOfDestinationCrew; 
	 
	
	 private int iSizeOfSourceCrew;
	 private int iSizeOfDestinationCrew;
	 private Map<String,String> map_Available_Train_Departure_Train = new LinkedHashMap<String,String>();
	 private List<String> listTrainNumbersFinalFirst = new LinkedList<String>();
	 private List<String> listTrainNumbersFinalTo = new LinkedList<String>();
	 
	 private List<String> listTrainNumbersFinalFirst1 = new LinkedList<String>();
	 private List<String> listTrainNumbersFinalTo1 = new LinkedList<String>();
	 
	 
	 //jagdish
	 private Integer nTotalHQRest = new Integer(0);
	 private Integer nTotalOSRest = new Integer(0);
	 private List<RoundtripSuggestions1> lstRRSuggestion1;
	 private List<RoundtripSuggestion2> lstRRSuggestion2;
	 private Long lRoundTripSuggestionId = new Long(0l);
	 private Map<String,String> mapDeletedRRSuggestion1 = new LinkedHashMap<String,String>();
	 private Map<String,String> mapDeletedRRSuggestion2 = new LinkedHashMap<String,String>();
	   @PostConstruct
		private void initilization() {

			if (!(httpSession.getAttribute("COMMONLOGINID") == null)) {
				customerloginId = (Long) httpSession.getAttribute("COMMONLOGINID");
				
				 createList();
				 
				 deletebuttonTrue = false;
				 updatebuttonTrue= false;
				 loadalldrivingsections();
				 
				 addDrivingdutySelectItems();
				 loadlist();
				 generateTrasactionId();
				 commonselectOnedrivingSections();
				 showFinalTable();

			} else {
				try {
					FacesContext.getCurrentInstance().getExternalContext()
							.redirect(CrewUtilities.getRedirectContext() + "/login.xhtml");
				} catch (Exception e) {
					CREW_LOGGER.debug(e.getMessage());
				}

			}

		}
	 
	 
	
		
	 
	@SuppressWarnings("unchecked")
	private void createList()
	{
	
		context = new ClassPathXmlApplicationContext("Beans.xml");
		dlist =  (List<Onedrivingsection>) context.getBean("list");
		updateOneDS = (Onedrivingsection) context.getBean("onedrivingsection");
		crewChangeDrivingsectionsList =  (List<CrewChangeDrivingsection>) context.getBean("list");
		setPilotjourneylist((List<PilotJourney>) context.getBean("list"));
		drivingDutyStore = (Onedrivingsection) context.getBean("onedrivingsection");
		drivingDutyOfPilotDrivingSectionsList = (DrivingDutyOfPilotDrivingSection) context.getBean("drivingdutyofpilotdrivingsection");
		drivingDutyPilotDrivingSection = (DrivingDutyOfPilotDrivingSection) context
				.getBean("drivingdutyofpilotdrivingsection");
		drivingDutyAfterSearchList = (List<DrivingDutyOfPilotDrivingSection>) context.getBean("list");
		drivingPlusPilotList = (List<DrivingDutyOfPilotDrivingSection>) context
				.getBean("list");
		drivingDutyFromStationsList = (List<DrivingDutyOfPilotDrivingSection>) context.getBean("list");
		fromAndToStationList = (List<DrivingDutyOfPilotDrivingSection>) context.getBean("list");
		toAndFromStationList = (List<DrivingDutyOfPilotDrivingSection>) context.getBean("list");
		optimizedResultObject = (Finaltable) context.getBean("finaltable");
		finalTableList = (List<Finaltable>) context.getBean("list");
		loadDriDutyList = (List<DrivingDutyOfPilotDrivingSection>) context.getBean("list");
		updateDriDutyObject = (DrivingDutyOfPilotDrivingSection) context
				.getBean("drivingdutyofpilotdrivingsection");
		
		editDriDutyObject = (DrivingDutyOfPilotDrivingSection) context
				.getBean("drivingdutyofpilotdrivingsection");
		loadCrewChangeList = (List<CrewChangeDrivingsection>) context.getBean("list");
		listDrivingDutyView = (List<DrivingDutyOfPilotDrivingSection>) context.getBean("list");
		pilotTableObjectList = (List<PilotJourney>) context.getBean("list");
		transactionId= (TransactionIdGenerator) context.getBean("transactionidgenerator");
		listCrewChange = (List<CrewChangeDrivingsection>) context.getBean("list");
		listPilotJourney = (List<PilotJourney>) context.getBean("list");
		
		
		setOneSelection(new Onedrivingsection[100]);
		setODS(new Onedrivingsection());
		
		
		
		

		///  new beans
		
		crewChangeDrivingsectionList = (List<CrewChangeDrivingsection>) context.getBean("list");
		
		PilotJourneylist = (List<PilotJourney>) context.getBean("list");
		 
		 selectlistfrom_station 		=   new  ArrayList<SelectItem>();
		 To_stationlist 				=   new ArrayList<SelectItem>();
		 selectListviewFrom_station 	= 	new ArrayList<SelectItem>();
		 selectListviewTo_station  		= 	new ArrayList<SelectItem>();
		 editFromSelectone 				=   new ArrayList<SelectItem>();
		 editToSelectone 				=   new ArrayList<SelectItem>();
		 
		 crewchangedrivingsection  = (CrewChangeDrivingsection) context.getBean("crewChange");
		 
		 ddpsRest = (DrivingDutyOfPilotDrivingSection) context.getBean("drivingdutyofpilotdrivingsection");
	}
	@SuppressWarnings("unchecked")
	private void loadlist()
	{bToggleDisplay = false;
		try {
			dlist = (List<Onedrivingsection>) schoolService.loadList("from Onedrivingsection");
			crewChangeDrivingsectionsList = (List<CrewChangeDrivingsection>) schoolService
					.loadList("from CrewChangeDrivingsection");
			setPilotjourneylist((List<PilotJourney>) schoolService
					.loadList("from PilotJourney"));
			setDrivingPlusPilotList((List<DrivingDutyOfPilotDrivingSection>) schoolService
					.loadList("from DrivingDutyOfPilotDrivingSection as dds where dds.customer.id="+customerloginId));
			loadDriDutyList = (List<DrivingDutyOfPilotDrivingSection>) schoolService
					.loadList("from DrivingDutyOfPilotDrivingSection");

			loadCrewChangeList =(List<CrewChangeDrivingsection>) schoolService
					.loadList("from CrewChangeDrivingsection as ccds where ccds.customer.id="+customerloginId);
			listDrivingDutyView = (List<DrivingDutyOfPilotDrivingSection>) schoolService
					.loadList("from DrivingDutyOfPilotDrivingSection as ddps where ddps.customer.id="+customerloginId);
			pilotTableObjectList = (List<PilotJourney>) schoolService
					.loadList("from PilotJourney");
			
			
			
			
			
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public void loadAfterDelete()
	{
		System.out.println("from station is " +selectedviewFrom);
		System.out.println("selectedviewTo station is " +selectedviewTo);
		System.out.println("selectedviewFrom1 station is " +selectedviewFrom1);
		System.out.println("selectedviewTo1 station is " +selectedviewTo1);
		if(selectedviewFrom==null && selectedviewTo==null && selectedviewFrom1==null && selectedviewTo1==null)
		 {
			try {
				listDrivingDutyView = (List<DrivingDutyOfPilotDrivingSection>) schoolService
						.loadList("from DrivingDutyOfPilotDrivingSection as ddps where ddps.customer.id="+customerloginId);
			
			} catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
			}
		 }
		 if(selectedviewFrom!=null && selectedviewTo==null && selectedviewFrom1==null && selectedviewTo1==null)
		 {
			 try {
					listDrivingDutyView = (List<DrivingDutyOfPilotDrivingSection>) schoolService
							.loadList("from DrivingDutyOfPilotDrivingSection as ddps where ddps.customer.id="+customerloginId+
									"and ddps.fromStation like '"+selectedviewFrom+"'");
					
				} catch (CrewException e) {
					CREW_LOGGER.debug(e.getMessage());
					e.printStackTrace();
				}
		 }
		 else if(selectedviewFrom!=null && selectedviewTo!=null && selectedviewFrom1==null && selectedviewTo1==null)
		 {
			 try {
					listDrivingDutyView = (List<DrivingDutyOfPilotDrivingSection>) schoolService
							.loadList("from DrivingDutyOfPilotDrivingSection as ddps where ddps.customer.id="+customerloginId+
									"and ddps.fromStation like'"+selectedviewFrom+"'"+"and ddps.toStation like'"+selectedviewTo+"'");
					/*(from DrivingDutyOfPilotDrivingSection as ddps1 where ddps1.fromStation like'"+selectedviewFrom+"'"+"and"
							+ "toStation like'"+selectedviewTo+"'"+")
					*/
				} catch (CrewException e) {
					CREW_LOGGER.debug(e.getMessage());
					e.printStackTrace();
				}
		 }
		 else if(selectedviewFrom!=null && selectedviewTo!=null && selectedviewFrom1!=null && selectedviewTo1==null)
		 {
			 try {
					listDrivingDutyView = (List<DrivingDutyOfPilotDrivingSection>) schoolService
							.loadList("from DrivingDutyOfPilotDrivingSection as ddps where ddps.customer.id="+customerloginId+
									"and ddps.fromStation like'"+selectedviewFrom+"'"+"and ddps.toStation like'"+selectedviewTo+"'"
											+ "and ddps.fromStation like'"+selectedviewFrom1+"'");
					
				} catch (CrewException e) {
					CREW_LOGGER.debug(e.getMessage());
					e.printStackTrace();
				}
		 }
		
		
		
	}
	///  new Methods
	 	@SuppressWarnings("unchecked")
		public void loadalldrivingsections()
		{
			try {
				crewChangeDrivingsectionList = (List<CrewChangeDrivingsection>) this.schoolService.loadList("from CrewChangeDrivingsection as CCD");
				PilotJourneylist  = (List<PilotJourney>) schoolService.loadList("from PilotJourney as ppt where ppt.customer.id like '"+customerloginId+"'");
				
			} catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());
				
				e.printStackTrace();
			}
			
		}
		public void addPilot()
		{
			if(!SelectOneFromload())
			{
			     RequestContext context = RequestContext.getCurrentInstance();
			     context.execute("PF('addPilotWdgt').show();");
			     RequestContext.getCurrentInstance().update("pilotdlgid");
			}
		}
		
		public void savePilotDB()
		{
			PilotJourney PP = (PilotJourney) context.getBean("pilotJourney");
			PP.setSourceOfTravel(getpTravelingSource());
			PP.setFromStation(getpFrom_Station());
			PP.setToStation(getpTo_Station());
			PP.setKm(KM);
			
			String arrivaltime  = getpArrival();
			String departureTime  = getpDeparture();
			
			
			String arrivalhour = arrivaltime.substring(0,2);
			String arrivalminute = arrivaltime.substring(3,5);
			
			
			String departurehour = departureTime.substring(0,2);
			String departureminut = departureTime.substring(3,5);
			
		
			
			
			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();
		
			c1.set(Calendar.HOUR, Integer.parseInt(arrivalhour));
			c1.set(Calendar.MINUTE, Integer.parseInt(arrivalminute));
			c2.set(Calendar.HOUR, Integer.parseInt(departurehour));
			c2.set(Calendar.MINUTE, Integer.parseInt(departureminut));
			
			
			PP.setArrivalTime(getpArrival());
			PP.setDepartureTime(getpDeparture());
			try {
				PP.setCustomer((Customer) schoolService.loadUniqueObject("from Customer as cst where cst.id="+customerloginId));
			} catch (CrewException e1) {
				CREW_LOGGER.debug(e1.getMessage());
				e1.printStackTrace();
			}
			try {
				schoolService.saveInDb(PP);
				PilotJourneylist = (List<PilotJourney>) this.schoolService.loadList("from PilotJourney as pj where pj.customer.id="+customerloginId);
				PP.clearfields();
				
			} catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
			}
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('addPilotWdgt').hide();");
		}
		
		
		public String generate()
		{
			if(count==0)
			{
				RequestContext.getCurrentInstance().execute("PF('errorRRWdgt').show();");
			}
			else
			{
				
			
			String strFromStation = new String();
			String strCrewBase = new String();
			String strCrewType = new String();
			String strToStation = new String();
			Date dtDepartureTime = new Date();
			Date dtArrivalTime = new Date();
			Long totalDistance = new Long(0);
			Date dtSignOnTime = new Date();
			Date dtSignOffTime = new Date();
			
			String strTotalHour = new String();
			
			List<Integer> listHour = new LinkedList<Integer>();
			List<Integer> listMinute = new LinkedList<Integer>();
			
			
			Calendar calendar1 = Calendar.getInstance();
			Calendar calendar2 = Calendar.getInstance();
			
			lTransId = generateTrasactionId();
		
			//schoolService.deleteQuery("delete from DrivingDutyOfPilotDrivingSection");
			
			DrivingDutyOfPilotDrivingSection DDPT= (DrivingDutyOfPilotDrivingSection) context.getBean("drivingdutyofpilotdrivingsection");
			
			String strArrivalTime = new String();
			
			List<Integer> listSequence = new ArrayList<Integer>();
			String strOnlyNumber = null;
			Map<String,String> mapIdentifyPilotId = new HashMap<String,String>();
			System.out.println("---------------------Before Sorting----------------------------------------");
			for(int k = 0; k<count; k++)
			{
				System.out.println(dsectionarray[k]);
			}
			System.out.println("---------------------------------------------------------------------------");
			for(int k = 0; k<count; k++)
			{
				if(dsectionarray[k].length()>1)
				{
					
					strOnlyNumber = dsectionarray[k].substring(dsectionarray[k].indexOf('P')+1,dsectionarray[k].length());
					mapIdentifyPilotId.put(strOnlyNumber, dsectionarray[k]);
					listSequence.add(Integer.parseInt(strOnlyNumber));
					System.out.println(strOnlyNumber);
				}
				else
				{
					listSequence.add(Integer.parseInt(dsectionarray[k]));	
					System.out.println(dsectionarray[k]);
				}
				
				
			}
			for(Integer i : listSequence)
			{
				System.out.println("After Parsing ->" +i);
			}
			Collections.sort(listSequence);
			System.out.println("------------------------After Sorting-------------------------------------");
			for(Integer i : listSequence)
			{
				System.out.println("After Parsing ->" +i);
			}
			System.out.println("---------------------------------------------------------------------------");
			String strTotalHours[] = new String[100];
			int count1 = 0;
			List<String> lisTrainNumbers = new LinkedList<String>();
			for(int i = 0; i<count ; i++)
			{
				System.out.println("-------------------------------------------------------------");
				if(mapSeqNoPlusId.containsKey(listSequence.get(i).toString()))
				{
					
					System.out.println("First IF");
					System.out.println("contains in driving sections" +listSequence.get(i).toString());
					System.out.println("value in driving section ->" + mapSeqNoPlusId.get(listSequence.get(i).toString()));
					CrewChangeDrivingsection ds  =  (CrewChangeDrivingsection) context.getBean("crewChange");
					
					try {
	ds	= (CrewChangeDrivingsection) schoolService.loadUniqueObject("from CrewChangeDrivingsection as CCD where CCD.id="+mapSeqNoPlusId.get(listSequence.get(i).toString()));
						if(i==0)
						{	
							System.out.print("i==0 Condition");
							strFromStation 		= ds.getFromStation();
							dtDepartureTime 	= ds.getDeparture();
							strTotalHours[i] 	= ds.getHour();
						}
						lisTrainNumbers.add(ds.getTrainNo());
						strCrewBase = ds.getCrewBase();
						strCrewType = ds.getCrewType();
						strTotalHours[i] = ds.getHour();
						count1++;
						totalDistance = totalDistance + ds.getKilometer();
						Calendar cal2 = Calendar.getInstance();
						cal2.setTime(ds.getDeparture());
						listHour.add(cal2.get(Calendar.HOUR_OF_DAY));
						listMinute.add(cal2.get(Calendar.MINUTE));
									
					} catch (CrewException e1) {
						CREW_LOGGER.debug(e1.getMessage());
						e1.printStackTrace();
					}
				} // end of If 
				else
				{
					//mapIdentifyPilotId.get(key)
					System.out.println("First Else");
					System.out.println("contains in pilot journey" +listSequence.get(i).toString());
					System.out.println("value in pilot journey ->" + mapIdentifyPilotId.get(listSequence.get(i).toString()));
					
					String strPilotId = new String();
					strPilotId = mapIdentifyPilotId.get(listSequence.get(i).toString());
					PilotJourney ps  =  (PilotJourney) context.getBean("pilotJourney");
					//	DrivingDutyOfPilotDrivingSection DDPT= (DrivingDutyOfPilotDrivingSection) context.getBean("drivingdutyofpilotdrivingsection");
						try {
							ps = (PilotJourney) schoolService.loadUniqueObject("from PilotJourney as CCD where CCD.id="+ mapPilotId.get(strPilotId));
							
							if(i==0)
							{
								System.out.print("i==0 Condition in else");
								strFromStation = ps.getFromStation();
								String strDepTime = ps.getDepartureTime();
								Date dt = new Date();
								Calendar calDep = Calendar.getInstance();
								calDep.setTime(dt);
								
								String strHour = strDepTime.substring(0,2);
								String strMinute = strDepTime.substring(3,5);
								
								calDep.set(Calendar.HOUR_OF_DAY, Integer.parseInt(strHour));
								calDep.set(Calendar.MINUTE, Integer.parseInt(strMinute));
								dtDepartureTime  = calDep.getTime();
									
							}
							totalDistance = totalDistance + ps.getKm();
							lisTrainNumbers.add(ps.getSourceOfTravel());
							String strDeparture = ps.getDepartureTime();
							
							Date dtArrival = dtDepartureTime;
							Calendar cal = Calendar.getInstance();
							String strHour = strDeparture.substring(0,2);
							String strMinute = strDeparture.substring(3,5);
							//strTotalHours[i] = s.getHour();
							cal.setTime(dtArrival);

							listHour.add(Integer.parseInt(strHour));
							listMinute.add(Integer.parseInt(strMinute));
							dtArrivalTime = cal.getTime();
						} catch (CrewException e1) {
							e1.printStackTrace();
						}
				}
				if(i==listSequence.size()-1)
				{
					System.out.print("i==ListSequence.Size-1");
					if(mapSeqNoPlusId.containsKey(listSequence.get(i).toString()))
					{	
						CrewChangeDrivingsection ds  =  (CrewChangeDrivingsection) context.getBean("crewChange");
						
						try {
							ds	= (CrewChangeDrivingsection) schoolService.loadUniqueObject("from CrewChangeDrivingsection as CCD where CCD.id="+mapSeqNoPlusId.get(listSequence.get(i).toString()));
							dtArrivalTime = ds.getArrival();
							strToStation = ds.getToStation();
							Calendar cal2 = Calendar.getInstance();
							cal2.setTime(ds.getDeparture());
							strTotalHours[i] = ds.getHour();
							listHour.add(cal2.get(Calendar.HOUR_OF_DAY));
							listMinute.add(cal2.get(Calendar.MINUTE));
						} catch (CrewException e1) {
							e1.printStackTrace();
						}
						
					}
					else if(mapIdentifyPilotId.containsKey(listSequence.get(i).toString()))
					{
						
						
						String strPilotId = new String();
						System.out.print("i==ListSequence.Size-1 else ");
						strPilotId = mapIdentifyPilotId.get(listSequence.get(i).toString());
						PilotJourney ps  =  (PilotJourney) context.getBean("pilotJourney");

							try {
								ps = (PilotJourney) schoolService.loadUniqueObject("from PilotJourney as CCD where CCD.id="+ mapPilotId.get(strPilotId));
							
								strToStation = ps.getToStation();
								strArrivalTime = ps.getArrivalTime();
								Date dtArrival = dtDepartureTime;
								Calendar cal = Calendar.getInstance();
								String strHour = strArrivalTime.substring(0,2);
								String strMinute = strArrivalTime.substring(3,5);
								cal.setTime(dtArrival);
								cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(strHour));
								cal.set(Calendar.MINUTE, Integer.parseInt(strMinute));
								dtArrivalTime = cal.getTime();
							
							} catch (CrewException e1) {
								e1.printStackTrace();
							}
					}
				}
		
			}
			String strTrainNumbers = new String();
			strTrainNumbers = "";
			for(String s : lisTrainNumbers)
			{	strTrainNumbers = strTrainNumbers.concat(s+","); 
			    System.out.println("Trains "+strTrainNumbers);
			}
			

			
			calendar1.setTime(dtDepartureTime);
			calendar2.setTime(dtArrivalTime);

			calendar1.add(Calendar.MINUTE, -30);  /// Signon Time before departure  (departure - 30Min) 
			calendar2.add(Calendar.MINUTE, 30);   // signoff time after arrival  (arrival + 30Min)
			
			dtSignOnTime = calendar1.getTime();
			dtSignOffTime= calendar2.getTime(); 
			
	
			///Calculation of availability timing
			calendar1.setTime(dtSignOnTime);
			calendar2.setTime(dtSignOffTime);

			Integer iSignOnHourInHour = calendar1.get(Calendar.HOUR_OF_DAY); 
			Integer iSignOnHourMinutes = calendar1.get(Calendar.MINUTE);
		
			System.out.println("-------------------------------------------------");
			Integer iSignOffHourInHour = calendar2.get(Calendar.HOUR_OF_DAY);
			Integer iSignOffHourMinutes = calendar2.get(Calendar.MINUTE);
			
			System.out.println("-------------------------------------------------");
			if(iSignOnHourInHour.equals(0))  	 iSignOnHourInHour = 24;
			if(iSignOffHourInHour.equals(0))  iSignOffHourInHour = 24;
			Integer iTotalSignOnInMinutes = new Integer(0);
			Integer iTotalSignOffInMinutes = new Integer(0);
			Integer iDutyHoursInMinutes = new Integer(0);
			Integer iDutyHoursInHours = new Integer(0);
			String strTotalDutyHours = new String();
			
			System.out.println("---------------");
			System.out.println("SignOnHour "+iSignOnHourInHour);
			System.out.println("Minutes "+iSignOnHourMinutes);
			
			System.out.println("SignoffHour "+iSignOffHourInHour);
			
			System.out.println("Minutes "+iSignOffHourMinutes);
			
			
			System.out.println("---------------");
			
			if(iSignOnHourInHour < iSignOffHourInHour)
			{
				iTotalSignOnInMinutes = iSignOnHourInHour*60+ iSignOnHourMinutes;	
				iTotalSignOffInMinutes = iSignOffHourInHour*60+ iSignOffHourMinutes;
				iDutyHoursInMinutes = iTotalSignOffInMinutes - iTotalSignOnInMinutes;
				iDutyHoursInHours = iDutyHoursInMinutes / 60;
				iDutyHoursInMinutes = iDutyHoursInMinutes % 60;
				strTotalDutyHours = iDutyHoursInHours.toString().concat("." + iDutyHoursInMinutes);
				System.out.print("Duty Hour is If "+strTotalDutyHours);
			}
			else
			{
				iSignOnHourInHour = 24*60 - (iSignOnHourInHour * 60 + iSignOnHourMinutes);
				iSignOffHourInHour = iSignOffHourInHour * 60;
				iTotalSignOnInMinutes = (iSignOnHourInHour) + (iSignOffHourInHour) ;
				iDutyHoursInHours = iTotalSignOnInMinutes / 60;
				iDutyHoursInMinutes = iTotalSignOnInMinutes % 60;
				strTotalDutyHours = iDutyHoursInHours.toString().concat("." + iDutyHoursInMinutes);
				System.out.print("Duty Hour in else  "+strTotalDutyHours);
			}
			
			System.out.println("---------------------------------------------------");

			
			Date dtAvailability = new Date();
			
			System.out.println("strCrewBase->>" +strCrewBase);
			System.out.println("strToStation->>" +strToStation);
			
			   
			System.out.println("------------------------------------");

			if(strCrewBase.equalsIgnoreCase(strToStation))   /// Check for HQ else go to OS 
			{
				int nNoofHours  = 960; /// hq rest 16 hours   (16*60=960)
				System.out.println("Inside Check for Station either HQ OR OS");
				nTotalHQRest =nTotalHQRest + nNoofHours;
				if(iDutyHoursInHours>=8)  // dutyHour >= 8hrs  then SignOff+8hrs
				{
					calendar1.setTime(dtSignOffTime);
					calendar1.add(Calendar.MINUTE, nNoofHours);// 960 minutes  = 16hrs
					DDPT.setHqRest(String.valueOf(nNoofHours)); //Set the HQ Rest 
					dtAvailability=calendar1.getTime();
					System.out.println("signOffTime ===" +dtSignOffTime);
					System.out.println(">8 availabilityDate " +dtAvailability);
					int iAvailableHour = calendar1.get(Calendar.HOUR_OF_DAY);
					int iAvailableMinute = calendar1.get(Calendar.MINUTE);
					
					String strAvailableHour = String.valueOf(iAvailableHour);
					String strAvailableMinute = String.valueOf(iAvailableMinute);
					
					strTotalHour = strAvailableHour.concat(":" + strAvailableMinute);
					
				
					System.out.println(">=8  in HQ " +strTotalHour);
					
					
					
					
				}
				else if(iDutyHoursInHours<8)
				{
					int no_of_hours = 720;  // 12 Hours HeadQuarter Rest (12*60=720)
					nTotalHQRest =nTotalHQRest + nNoofHours;
					calendar1.setTime(dtSignOffTime);

					calendar1.add(Calendar.MINUTE, no_of_hours); // 720 minutes  = 12hrs
					
					DDPT.setHqRest(String.valueOf(no_of_hours));  // saving hqRest 
				
					dtAvailability=calendar1.getTime();
					System.out.println("signOffTime ==== " +dtSignOffTime);
					System.out.println("< 8 availabilityDate " +dtAvailability);
					int iAvailableHour = calendar1.get(Calendar.HOUR_OF_DAY);
					int iAvailableMinute = calendar1.get(Calendar.MINUTE);
					
					String strAvailableHour = String.valueOf(iAvailableHour);
					String strAvailableMinute = String.valueOf(iAvailableMinute);
					
					strTotalHour = strAvailableHour.concat(":" + strAvailableMinute);
					System.out.println(" < 8  in HQ " +strTotalHour);
					
				}
			}
		
			else 
			{
				
				int Os_rest_hour  = 480 ;  //   8 Hours Rest (8*60=480)
				nTotalOSRest =nTotalOSRest + Os_rest_hour;
				//  OS REST 
				if(iDutyHoursInHours>=8)  // dutyHour >= 8hrs then SignOff+8hrs   
				{
					
					calendar1.setTime(dtSignOffTime);

					calendar1.add(Calendar.MINUTE,Os_rest_hour);  // 480 minutes  = 8hrs
					dtAvailability=calendar1.getTime();
					System.out.println("signOffTime" +dtSignOffTime);
					System.out.println("availabilityDate" +dtAvailability);
					
					int iAvailableHour = calendar1.get(Calendar.HOUR_OF_DAY);
					int iAvailableMinute = calendar1.get(Calendar.MINUTE);
					
					String strAvailableHour = String.valueOf(iAvailableHour);
					String strAvailableMinute = String.valueOf(iAvailableMinute);
					
					strTotalHour = strAvailableHour.concat(":" + strAvailableMinute);
					DDPT.setOsRest(String.valueOf(Os_rest_hour));
					System.out.println(">= 8hrs in OS" +strTotalHour);
				}
				else if(iDutyHoursInHours<8 && iDutyHoursInHours>5) // dutyHour < 8hrs and  >5hrs then SignOff+6hrs
				{
					int out_Station_rest = 360; 
					nTotalOSRest =nTotalOSRest + out_Station_rest;// 6 hours (6*60=360)
					calendar1.setTime(dtSignOffTime);
					calendar1.add(Calendar.MINUTE, out_Station_rest); // 360 minutes  = 6hrs
					dtAvailability=calendar1.getTime();
					System.out.println("signOffTime" +dtSignOffTime);
					System.out.println("availabilityDate" +dtAvailability);
					
					int iAvailableHour = calendar1.get(Calendar.HOUR_OF_DAY);
					int iAvailableMinute = calendar1.get(Calendar.MINUTE);
					
					String strAvailableHour = String.valueOf(iAvailableHour);
					String strAvailableMinute = String.valueOf(iAvailableMinute);
					
					strTotalHour = strAvailableHour.concat(":" + strAvailableMinute);
					DDPT.setOsRest(String.valueOf(out_Station_rest));
					System.out.println("<8hrs and >5 in OS" +strTotalHour);
					
				}
				else if(iDutyHoursInHours<=5)
				{
					int out_Station_rest_hours = iDutyHoursInHours*60+60;
					nTotalOSRest =nTotalOSRest + out_Station_rest_hours;
					calendar1.setTime(dtSignOffTime);
					System.out.println("Duty Hours  = ="+iDutyHoursInHours);
					calendar1.add(Calendar.MINUTE,out_Station_rest_hours);  // OS less than 5 hrs 
					dtAvailability=calendar1.getTime();
					System.out.println("signOffTime  = =" +dtSignOffTime);
					System.out.println("availabilityDate = =" +dtAvailability);
					
					int iAvailableHour = calendar1.get(Calendar.HOUR_OF_DAY);
					int iAvailableMinute = calendar1.get(Calendar.MINUTE);
					
					String strAvailableHour = String.valueOf(iAvailableHour);
					String strAvailableMinute = String.valueOf(iAvailableMinute);
					
					strTotalHour = strAvailableHour.concat(":" + strAvailableMinute);
					DDPT.setOsRest(String.valueOf(out_Station_rest_hours));
					System.out.println("<5 in OS" +strTotalHour);
					
				}
			}
			
			
			
			
			/*if(nTotalHQRest != null)
			{
				System.out.println("nTotalHQRest->>>>>>>" +nTotalHQRest);
			}
			else if(nTotalOSRest != null)
			{
				System.out.println("nTotalOSRest->>>>>>>" +nTotalOSRest);
			}*/
			HqOsRest ho = (HqOsRest) context.getBean("hqOsRest");
			Long lUniqueTransactionId = generateTrasactionId();
			DDPT.setTrainNo(strTrainNumbers);
			DDPT.setFromStation(strFromStation);
			DDPT.setToStation(strToStation);
			DDPT.setDeparture(dtDepartureTime);
			DDPT.setArrival(dtArrivalTime);
			DDPT.setSignonTime(dtSignOnTime);
			DDPT.setSignoffTime(dtSignOffTime);
			DDPT.setAvailability(strTotalHour);
			DDPT.setKm(totalDistance);
			DDPT.setCrewBase(strCrewBase);
			DDPT.setCrewType(strCrewType);

			DDPT.setTransactionNoDsToDd(lUniqueTransactionId);
			
			
			ho.setHqRest(nTotalHQRest);
			ho.setOsRest(nTotalOSRest);
			
			
			
			try {
				DDPT.setCustomer((Customer)schoolService.loadUniqueObject(" from Customer as cst where cst.id="+customerloginId));
				
				schoolService.saveInDb(ho);
				
				
				nTotalHQRest = 0;
				nTotalOSRest = 0;
			} catch (CrewException e2) {
				CREW_LOGGER.debug(e2.getMessage());
				e2.printStackTrace();
			}
			try {
				schoolService.saveInDb(DDPT);
			} catch (CrewException e) {
				
				e.printStackTrace();
				CREW_LOGGER.debug(e.getMessage());
			}
			for(Entry<String,Long> entry : mapSeqNoPlusId.entrySet())
			{CrewChangeDrivingsection ds  =  (CrewChangeDrivingsection) context.getBean("crewChange");
				try {
					ds	= (CrewChangeDrivingsection) schoolService.loadUniqueObject("from CrewChangeDrivingsection as CCD where CCD.id="+entry.getValue());
					ds.setSequenceId(entry.getValue().toString());
					ds.setTransactionNoDsToDd(lUniqueTransactionId);
					ds.setStatus(true);
					try {
						schoolService.updateObjectInDb(ds);
					    } catch (CrewException e) {
						e.printStackTrace();
						CREW_LOGGER.debug(e.getMessage());
					}
					
				} catch (CrewException e1) {
					e1.printStackTrace();
					CREW_LOGGER.debug(e1.getMessage());
				}
			
			}
			for(Entry<String,Long> entry : mapPilotId.entrySet())
			{	PilotJourney ps  =  (PilotJourney) context.getBean("pilotJourney");
					try {
						ps	= (PilotJourney) schoolService.loadUniqueObject("from PilotJourney as CCD where CCD.id="+entry.getValue());
						ps.setSequenceId(entry.getValue().toString());
						ps.setTransactionNoDsToDd(lUniqueTransactionId);
					} catch (CrewException e1) {
						e1.printStackTrace();
					}
					try {
						
						schoolService.updateObjectInDb(ps);
						
					} catch (CrewException e) {
						
						e.printStackTrace();
					}
			}
			Long lTransactionId = generateTrasactionId();
			lTransactionId++;
			transactionId.setTransactionId(lTransactionId);
			
		
			try {
				schoolService.saveInDb(transactionId);
			} catch (CrewException e) {
				e.printStackTrace();
			}
			
			RequestContext.getCurrentInstance().execute("PF('generateWdg').show();");
			}
			return null;
		}
	
				@SuppressWarnings("unchecked")
		public void viewDrivingSectionsFromDrvingDuty2(DrivingDutyOfPilotDrivingSection d)
		{
			System.out.println("---------------------------------------------------");
			System.out.println("id is " +d.getId());
			System.out.println("---------------------------------------------------");
			Long lTransactionId = new Long(0);
			
			lTransactionId = d.getTransactionNoDsToDd();
			
			try {
				listCrewChange = (List<CrewChangeDrivingsection>) 
						schoolService.loadList("from CrewChangeDrivingsection as cc where cc.transactionNoDsToDd =" +lTransactionId);
				listPilotJourney = (List<PilotJourney>) schoolService.loadList("from PilotJourney as cc where cc.transactionNoDsToDd =" +lTransactionId);
				
			} catch (CrewException e) {
				e.printStackTrace();
			}
		}
		@SuppressWarnings("unchecked")
		public void deleteFromDrivingDuty2(DrivingDutyOfPilotDrivingSection d)
		{
			System.out.println("---------------------------------------------------");
			System.out.println("id is " +d.getId());
			System.out.println("---------------------------------------------------");
			Long lTransactionId = new Long(0);
			
			lTransactionId = d.getTransactionNoDsToDd();
			System.out.println("---------------------------------------------------");
			System.out.println("lTransactionId " +lTransactionId);
			System.out.println("---------------------------------------------------");
			
			DrivingDutyOfPilotDrivingSection DDPT= (DrivingDutyOfPilotDrivingSection) context.getBean("drivingdutyofpilotdrivingsection");
			CrewChangeDrivingsection ds  =  (CrewChangeDrivingsection) context.getBean("crewChange");
			PilotJourney pj = (PilotJourney) context.getBean("pilotJourney");
			List<PilotJourney> listPJ = (List<PilotJourney>) context.getBean("list");
			try {
				ds = (CrewChangeDrivingsection) schoolService
						.loadUniqueObject("from CrewChangeDrivingsection as CD where CD.transactionNoDsToDd="
								+ lTransactionId);
				listPJ = (List<PilotJourney>) schoolService
						.loadList("from PilotJourney as CD where CD.transactionNoDsToDd="
								+ lTransactionId);
				for(PilotJourney p: listPJ)
				{	p.setTransactionNoDsToDd((long)0);
					schoolService.updateObjectInDb(pj);
				}
				ds.setStatus(false);
				pj.setTransactionNoDsToDd((long)0);
				schoolService.updateObjectInDb(ds);
				schoolService.update("CrewChangeDrivingsection",lTransactionId);
				schoolService.deleteById(d.getId(), "DrivingDutyOfPilotDrivingSection");
				
				
				loadAfterDelete();
			} catch (CrewException e1) {
				CREW_LOGGER.debug(e1.getMessage());
				e1.printStackTrace();
			}
			
		}
	
		public void saveInMap(Long id)
		{
			System.out.println("drivingsectionsequence->" +drivingsectionsequence);
			dsectionarray[count] =drivingsectionsequence;
			mapSeqNoPlusId.put(drivingsectionsequence, id);
			count++;
		}
		public void getUniqueIdFromPilotTable(Long id)
		{
			System.out.println("pilotjourneysequence->" +pilotjourneysequence);
			dsectionarray[count] = "P"+pilotjourneysequence;
			mapPilotId.put("P"+pilotjourneysequence, id);
			count++;
		}
		
		
		public void updatedComponents(PageEvent event)
		{
			int varPage = event.getPage();
			
			
			System.out.println("pages"+varPage);
			if(varPage==1)
			{
				drivingsectionsequence  = null;
				//RequestContext.getCurrentInstance().update("viewform:sequenceinput");
			}else if(varPage==2)
			{
				drivingsectionsequence  = null;
			}else if(varPage==3)
			{
				drivingsectionsequence  = null;
			}else if(varPage==4)
			{
				drivingsectionsequence  = null;
			}
			else if(varPage==5)
			{
				drivingsectionsequence  = null;
			}
			else if(varPage==6)
			{
				drivingsectionsequence  = null;
			}
			else if(varPage==7)
			{
				drivingsectionsequence  = null;
			}
			else if(varPage==8)
			{
				drivingsectionsequence  = null;
			}
			else if(varPage==9)
			{
				drivingsectionsequence  = null;
			}
			else if(varPage==10)
			{
				drivingsectionsequence  = null;
			}
			else if(varPage==11)
			{
				drivingsectionsequence  = null;
			}
			else if(varPage==12)
			{
				drivingsectionsequence  = null;
			}
			else if(varPage==13)
			{
				drivingsectionsequence  = null;
			}
			else if(varPage==14)
			{
				drivingsectionsequence  = null;
			}
			else if(varPage==15)
			{
				drivingsectionsequence  = null;
			}
			else if(varPage==16)
			{
				drivingsectionsequence  = null;
			}
			else if(varPage==17)
			{
				drivingsectionsequence  = null;
			}
			else if(varPage==18)
			{
				drivingsectionsequence  = null;
			}
			else if(varPage==19)
			{
				drivingsectionsequence  = null;
			}
			else if(varPage==20)
			{
				drivingsectionsequence  = null;
			}
			
			
			
		}
		
		
		
		
		//santosh methods
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

		
		public void addDrivingdutySelectItems() {
			List<DrivingDutyOfPilotDrivingSection> drivingList = new ArrayList<DrivingDutyOfPilotDrivingSection>();
			
			 HashMap<String,String> to_station=new HashMap<String,String>();  
			 HashMap<String,String> from_station=new HashMap<String,String>();
			
			
			try {
				drivingList = (List<DrivingDutyOfPilotDrivingSection>) schoolService
						.loadList("from DrivingDutyOfPilotDrivingSection");
				if(!drivingList.isEmpty())
				{
				for(DrivingDutyOfPilotDrivingSection CCD : drivingList)
				{
					to_station.put(CCD.getToStation(), CCD.getToStation());
					from_station.put(CCD.getFromStation(), CCD.getFromStation());
				}
				
				 //System.out.println("Key-------------value");
				 for(Map.Entry<String,String> fromEntry:from_station.entrySet()){  
					  // System.out.println(fromEntry.getKey()+" "+fromEntry.getValue());
					   selectListviewFrom_station.add(new SelectItem(fromEntry.getKey()));
					   
					  }  
				
				 
				 for(Map.Entry<String,String> to_entry:to_station.entrySet()){  
					selectListviewTo_station.add(new SelectItem(to_entry.getKey()));
					
					  }  
				
				}

			} catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
				
			}

		}
		

		public void dutyStatusSearch()
		{	
			switch(selectedStatus.trim())
			{
			
			case "DutyCreated":
				
												System.out.print("Duty Created ");
												loadCrewChangeList = (List<CrewChangeDrivingsection>) schoolService.StatusSearch(selectedStatus);
												break;
			case "DutyNotCreated":
				
												System.out.print("Not Duty Created ");
												loadCrewChangeList = (List<CrewChangeDrivingsection>) schoolService.StatusSearch(selectedStatus);
												break;
												
			case "All":
												
												System.out.print("All");
												loadCrewChangeList = (List<CrewChangeDrivingsection>) schoolService.StatusSearch(selectedStatus);
												break;
			default:
													
											   System.out.print("default ");
											   FacesContext.getCurrentInstance().addMessage(
														null,
														new FacesMessage(FacesMessage.SEVERITY_FATAL,
																"Select Status", "Select Status"));
											   break;
											   
			}
			
		}
		
		public void roundTripStatusSearch()
		{	
			switch(roundTripStatus.trim())
			{
			
			case "RoundTripcreated":
				
												System.out.print("Round created ");
												listDrivingDutyView = (List<DrivingDutyOfPilotDrivingSection>) schoolService.roundTripStatusSearch(roundTripStatus);
												break;
			case "RoundTripNotcreated":
				
												System.out.print("RoundTripNotcreated");
												listDrivingDutyView = (List<DrivingDutyOfPilotDrivingSection>) schoolService.roundTripStatusSearch(roundTripStatus);
												break;
												
												
			case "All":
												System.out.print("All");
												listDrivingDutyView = (List<DrivingDutyOfPilotDrivingSection>) schoolService.roundTripStatusSearch(roundTripStatus);
												break;
				                                
			default:
													
											   System.out.print("default ");
											   FacesContext.getCurrentInstance().addMessage(
														null,
														new FacesMessage(FacesMessage.SEVERITY_FATAL,
																"Select Status", "Select Status"));
											   break;
											   
			}
			
		}
		
		public void  multipleSearch()
		{
			/*System.out.println("selectedFrom->" +selectedFrom);
			System.out.println("selectedTo->" +selectedTo);
			System.out.println("selectedFrom1->" +selectedFrom1);
			System.out.println("selectedTo1->" +selectedTo1);*/
			 int nChoice;
			 if(selectedFrom!=null||selectedTo!=null||selectedFrom1!=null || selectedTo1!=null)
				{	
				
				 if(selectedFrom!=null && !selectedFrom.isEmpty() && selectedTo!=null && !selectedTo.isEmpty() && selectedFrom1!=null && !selectedFrom1.isEmpty() && selectedTo1!=null && !selectedTo1.isEmpty())
					{  loadCrewChangeList = (List<CrewChangeDrivingsection>) schoolService.loadReverseList2(selectedFrom, selectedTo,selectedFrom1,selectedTo1);
				    }
				 else if(selectedFrom!=null && !selectedFrom.isEmpty() && selectedTo!=null && !selectedTo.isEmpty())
				{	System.out.print("Above Two");
					loadCrewChangeList = (List<CrewChangeDrivingsection>) schoolService.loadReverseList(selectedFrom, selectedTo);
				} 
				else if(!selectedFrom.isEmpty())
				{ 
					 	nChoice = 1;
					 	loadCrewChangeList = (List<CrewChangeDrivingsection>) schoolService.singleSearch(selectedFrom,nChoice);
				}
				else if(!selectedTo.isEmpty())
				{
						 nChoice = 2;
						 loadCrewChangeList = (List<CrewChangeDrivingsection>) schoolService.singleSearch(selectedTo,nChoice);
				}
				else if(!selectedFrom1.isEmpty())
				{
						nChoice = 3;
						loadCrewChangeList = (List<CrewChangeDrivingsection>) schoolService.singleSearch(selectedFrom1,nChoice);
				}
				else if(!selectedTo1.isEmpty())
				{
						nChoice = 4;
						loadCrewChangeList = (List<CrewChangeDrivingsection>) schoolService.singleSearch(selectedTo1,nChoice);
				}else if(selectedFrom1!=null && !selectedFrom1.isEmpty() && selectedTo1!=null && !selectedTo1.isEmpty())
				{	
				    loadCrewChangeList = (List<CrewChangeDrivingsection>) schoolService.loadReverseList(selectedFrom1,selectedTo1);
				} 
				else
				{
					
					nChoice = 5;
					loadCrewChangeList = (List<CrewChangeDrivingsection>) schoolService.singleSearch(null,nChoice);
				}
			   				
				}
		}


		public void  viewDrivingDutymultipleSearch()
		{	 
			
			 if(selectedviewFrom!=null||selectedviewTo!=null||selectedviewFrom1!=null || selectedviewTo1!=null)
			 {
				 if(selectedviewFrom!=null && !selectedviewFrom.isEmpty() && selectedviewTo!=null && !selectedviewTo.isEmpty()&& selectedviewFrom1!=null && !selectedviewFrom1.isEmpty() && selectedviewTo1!=null && !selectedviewTo1.isEmpty())
					{   listDrivingDutyView = (List<DrivingDutyOfPilotDrivingSection>) schoolService.loadViewSearchList(selectedviewFrom, selectedviewTo, selectedviewFrom1, selectedviewTo1);
					}
					else if(selectedviewFrom!=null && !selectedviewFrom.isEmpty() && selectedviewTo!=null && !selectedviewTo.isEmpty())
					{
						listDrivingDutyView = (List<DrivingDutyOfPilotDrivingSection>) schoolService.loadViewSearchListTwoBox(selectedviewFrom, selectedviewTo, "UPPER");
						
					}
					else if(selectedviewFrom1!=null && !selectedviewFrom1.isEmpty() && selectedviewTo1!=null && !selectedviewTo1.isEmpty())
					{
						System.out.println("else");
						listDrivingDutyView = (List<DrivingDutyOfPilotDrivingSection>) schoolService.loadViewSearchListTwoBox(selectedviewFrom1,selectedviewTo1, "LOWER");
					}
					else if(!selectedviewFrom.isEmpty())
					{listDrivingDutyView = (List<DrivingDutyOfPilotDrivingSection>) schoolService.singleviewSearch(selectedviewFrom,"FROM");
					}
					else if(!selectedviewTo.isEmpty())
					{listDrivingDutyView = (List<DrivingDutyOfPilotDrivingSection>) schoolService.singleviewSearch(selectedviewTo,"TO");
					}
					else if(!selectedviewFrom1.isEmpty())
					{listDrivingDutyView = (List<DrivingDutyOfPilotDrivingSection>) schoolService.singleviewSearch(selectedviewFrom1,"FROM1");
					}
					else if(!selectedviewTo1.isEmpty())
					{listDrivingDutyView = (List<DrivingDutyOfPilotDrivingSection>) schoolService.singleviewSearch(selectedviewTo1,"TO1");
					}
					else
					{
						System.out.println("No selection Made");
					}
				
				 
			 }
			
			
			 
			 
		}
		
		private Long generateTrasactionId()
		{	

			Long lTransactionId = new Long(0);
			
			List<TransactionIdGenerator> listTransactionIdGenerator = (List<TransactionIdGenerator>) context.getBean("list");
			try {
				listTransactionIdGenerator = (List<TransactionIdGenerator>) schoolService.loadList("from TransactionIdGenerator");
				Long l = new Long(10000);
				if(listTransactionIdGenerator.isEmpty())
				{
					transactionId.setTransactionId(l);
					schoolService.saveInDb(transactionId);
				}
				else
				{
					List<Long> listTransactioIds = new LinkedList<Long>();
					for(TransactionIdGenerator t : listTransactionIdGenerator)
					{
						listTransactioIds.add(t.getTransactionId());
					}
					Collections.sort(listTransactioIds);
					
					for(int i = 0; i<listTransactioIds.size();i++)
					{
						if(i==listTransactioIds.size()-1)
						{
							lTransactionId = listTransactioIds.get(i);
						}
					}
				}
				
			} catch (CrewException e) {
				e.printStackTrace();
			}
			return lTransactionId;
			
		}
		
		/// end of new Methods
		public void delete() {

			if (oneSelection.length == 0) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								"please select atleast one row!",
								"please select atleast one row"));
			} else {
				if (!(dlist.isEmpty())) {
					for (Onedrivingsection str : oneSelection) {
						try {
							schoolService.delete(str.getId(), "Onedrivingsection",
									"id");
							removefromList(str.getId());
						} catch (CrewException e) {
							CREW_LOGGER.debug(e.getMessage());
						}
					}
				}
			}

		}
	
	private void removefromList(long deletedId) {
		loop: for (int index = 0; index < dlist.size(); index++) {
			if (deletedId == dlist.get(index).getId()) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, dlist.get(
								index).getTrainNo()

								+ " Succesfully deleted!", null));
				dlist.remove(index);

				break loop;
			}
		}
	}
	
	public void update() {

		if (oneSelection.length == 0) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"please select atleast one row!",
							"please select atleast one row"));
		} else {

			if (oneSelection.length == 1) {
				for (Onedrivingsection ODS : oneSelection) {
					System.out.print("" + ODS.getId());
					try {
						updateOneDS = (Onedrivingsection) schoolService
								.loadUniqueObject("from Onedrivingsection as ODS where ODS.id="
										+ ODS.getId());

					} catch (CrewException e) {
						e.printStackTrace();
						CREW_LOGGER.debug(e.getMessage());
					}
				}
				RequestContext context = RequestContext.getCurrentInstance();
				context.execute("PF('editDlg').show();");
				RequestContext.getCurrentInstance().update("editform");
			} else {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								"select Only one row!", "select Only one row"));
			}
		}

	}
	
	public String saveUpdate() {
		String redirect = "drivingView.faces?faces-redirect=true";
		try {
			schoolService.updateObjectInDb(updateDriDutyObject);

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Updated Successfully!", "Updated Successfully"));
			RequestContext.getCurrentInstance().update("updateGrowl");
			RequestContext.getCurrentInstance().update("drivingDuty");

		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}

		return redirect;
	}
	


	
	 public void onRowSelect(SelectEvent event) {
		 
		 deletebuttonTrue = false;
		 updatebuttonTrue= false;
		  ODS= new Onedrivingsection();
		 
		 try {
			List<Onedrivingsection> onedrivingsection = (List<com.crew.model.Onedrivingsection>) schoolService.loadList("from Onedrivingsection as ODS where ODS="+((Onedrivingsection) event.getObject()).getId());
		List<DrivingDutyOfPilotDrivingSection> list = (List<DrivingDutyOfPilotDrivingSection>) schoolService.loadList("from DrivingDutyOfPilotDrivingSection");
			
			
			 
					for(DrivingDutyOfPilotDrivingSection d : list)
					{
						updateDriDutyObject.setTrainNo(d.getTrainNo());
						updateDriDutyObject.setFromStation(d.getFromStation());
						updateDriDutyObject.setToStation(d.getToStation());
						updateDriDutyObject.setArrival(d.getArrival());
						updateDriDutyObject.setDeparture(d.getDeparture());
						updateDriDutyObject.setSignonTime(d.getSignonTime());
						updateDriDutyObject.setSignoffTime(d.getSignoffTime());
						updateDriDutyObject.setDutyHours(d.getDutyHours());
						updateDriDutyObject.setAvailability(d.getAvailability());
					}
		 
			
		} catch (CrewException e) {
			
			e.printStackTrace();
		}
		 
		    RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('singleRow').show();");
			RequestContext.getCurrentInstance().update("singleRowId");
	    }
	 
	    public void onRowUnselect(UnselectEvent event) {
	        FacesMessage msg = new FacesMessage("Train Selected", ((Onedrivingsection) event.getObject()).getToStation());
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	    }

	public boolean filterByCrewBaseAndType(Object value, Object filter,
			Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim();
		if (filterText == null || filterText.equals("")) {
			return true;
		}

		if (value == null) {
			return false;
		}

		String drivingduty = value.toString().toUpperCase();
		filterText = filterText.toUpperCase();

		if (drivingduty.contains(filterText)) {
			return true;
		} else {
			return false;
		}
	}
	    
	    
	    
	    
	    
	    
	//jagdish methods

	
@SuppressWarnings("unchecked")
public void roundTripSuggestions()
{
List<DrivingDutyOfPilotDrivingSection> listDrivingDuty1 = (List<DrivingDutyOfPilotDrivingSection>) 
	context.getBean("list");     

List<DrivingDutyOfPilotDrivingSection> listDrivingDuty2 = (List<DrivingDutyOfPilotDrivingSection>) 
		context.getBean("list");

 if(selectedviewFrom.isEmpty() && selectedviewTo.isEmpty() && selectedviewFrom1.isEmpty() && selectedviewTo1.isEmpty())
 {
	 RequestContext.getCurrentInstance().execute("PF('rrSuggest').show();");
 }
 else if(!selectedviewFrom.isEmpty() && selectedviewTo.isEmpty() && selectedviewFrom1.isEmpty() && selectedviewTo1.isEmpty())
 {
	 RequestContext.getCurrentInstance().execute("PF('rrSuggest').show();");
 }
 else if(!selectedviewFrom.isEmpty() && !selectedviewTo.isEmpty() && selectedviewFrom1.isEmpty() && selectedviewTo1.isEmpty())
 {
	 RequestContext.getCurrentInstance().execute("PF('rrSuggest').show();");
 }
 else if(!selectedviewFrom.isEmpty() && !selectedviewTo.isEmpty() && !selectedviewFrom1.isEmpty() && selectedviewTo1.isEmpty())
 {
	 RequestContext.getCurrentInstance().execute("PF('rrSuggest').show();");
 }
 else if(!selectedviewFrom.isEmpty()|| !selectedviewTo.isEmpty() || !selectedviewFrom1.isEmpty() || !selectedviewTo1.isEmpty())
 {
	 try {
			listDrivingDuty1 = (List<DrivingDutyOfPilotDrivingSection>) 
	schoolService.loadList("from DrivingDutyOfPilotDrivingSection as d where d.fromStation like '"+selectedviewFrom+"'"+"and d.toStation like'"+selectedviewTo+"'");
			
			
			listDrivingDuty2 = (List<DrivingDutyOfPilotDrivingSection>) 
					schoolService.loadList("from DrivingDutyOfPilotDrivingSection as d where d.fromStation like '"+selectedviewFrom1+"'"+"and d.toStation like'"+selectedviewTo1+"'");
			if(listDrivingDuty1.size()!=listDrivingDuty2.size())
			{
				RequestContext.getCurrentInstance().execute("PF('equal').show();");
			}
			else
			{
				iSizeOfSourceCrew = listDrivingDuty1.size();
				setiSizeOfDestinationCrew(listDrivingDuty2.size());
				System.out.println("--------------diff----------------------------");
				roundTripSuggestion2();
				RequestContext.getCurrentInstance().execute("PF('acceptWdgt').show();");
			}
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
 }
}

public void calToInputBox()
{
if(iNumberOfSourceCrew>iSizeOfSourceCrew)
{
	RequestContext.getCurrentInstance().execute("PF('calc').show();");
}
else
{
	iNumberOfDestinationCrew = iSizeOfSourceCrew - iNumberOfSourceCrew;
	System.out.println("update value of iNumberOfDestinationCrew is->>>" + iNumberOfDestinationCrew);
//	RequestContext.getCurrentInstance().update(":viewform4 :one");
}
}

public void fromDisplay()
{
	System.out.println("" +selectedviewFrom);
	//RequestContext.getCurrentInstance().update(":crew :one :from");
}

public void toDisplay()
{
	System.out.println("" +selectedviewTo);
//	RequestContext.getCurrentInstance().update(":one :to");
}


	    public void pilotJourneyDrivingSectionList() {
			System.out.println("" + selectedId.size());

		}
		public void pilotCheckBoxList() {
			System.out.println("" + pilotCheckBoxList);
		}
		public void drivingSectionCheckBoxList() {
			System.out.println("drivingSectionCheckBoxList" + drivingSectionCheckBoxList);
			
			//generatePilotPlusDrivingSection();
		}
	
		public void editCrew(CrewChangeDrivingsection changeDrivingsection) {

			try {
				editCrewChange = (CrewChangeDrivingsection) schoolService
						.loadUniqueObject("from CrewChangeDrivingsection as CCD where CCD.id ="
								+ changeDrivingsection.getId());

				RequestContext context = RequestContext.getCurrentInstance();
				context.execute("PF('creweditDialog').show();");
				RequestContext.getCurrentInstance().update("crewDlgid");

			} catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
			}

		}
	
		public void editSave() {
			try {
				schoolService.updateObjectInDb(editCrewChange);
				RequestContext.getCurrentInstance().update("crewDlgid");
				loadlist();
				RequestContext.getCurrentInstance().update("viewform");
			} catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
			}
		}
		public void pilotJourneyList() {
			System.out.println("" + this.pilotIdList.size());
			
		}

	
		
	public void showFinalTable()
	{
		try {
			finalTableList = (List<Finaltable>) schoolService.loadList("from Finaltable");
			if(!finalTableList.isEmpty())
			{
			for(Finaltable f : finalTableList)
			{	System.out.println("Difference is " +f.getDifferecnce());
			}
			
			Collections.sort(finalTableList,new MySalaryComp());
			}
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
	}
		
		

	class MySalaryComp implements Comparator<Finaltable>{

		@Override
		public int compare(Finaltable e1, Finaltable e2) {
		    if(e1.getDifferecnce() > e2.getDifferecnce()){
		        return 1;
		    } else {
		        return -1;
		    }
		}
		}
				
	public void editOneOneDrivingDuty(DrivingDutyOfPilotDrivingSection driEditObjet)
	{try {
			editDriDutyObject = (DrivingDutyOfPilotDrivingSection) schoolService.loadUniqueObject("from DrivingDutyOfPilotDrivingSection as CCD where CCD.id ="+driEditObjet.getId());
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('driOneOneEditDialog').show();");
			RequestContext.getCurrentInstance().update("dreDlgid");
		
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
		
	}
		

	
	public void updateCrewBase(long trainNo)
	{	try {
			 crewchangedrivingsection = (CrewChangeDrivingsection) schoolService.loadUniqueObject("from CrewChangeDrivingsection as ccd where ccd.id="+trainNo);
		    } catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public void saveUpdatedCrewBase()
	{
		try {
			this.schoolService.updateObjectInDb(crewchangedrivingsection);
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('editDlg').hide();");
			loadCrewChangeList =(List<CrewChangeDrivingsection>) schoolService
					.loadList("from CrewChangeDrivingsection as ccds where ccds.customer.id="+customerloginId);
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
	
	}
	
	public void updateCrewType(long trainNo)
	{	try {
			 crewchangedrivingsection = (CrewChangeDrivingsection) schoolService.loadUniqueObject("from CrewChangeDrivingsection as ccd where ccd.id="+trainNo);
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
	}
	public void  saveUpdatedCrewType()
	{
		try {
			schoolService.updateObjectInDb(crewchangedrivingsection);
			loadCrewChangeList =(List<CrewChangeDrivingsection>) schoolService
					.loadList("from CrewChangeDrivingsection as ccds where ccds.customer.id="+customerloginId);
				RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('editDlgCrewType').hide();");
			
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private String MAP(String strMap)
	{	Map<String, String> map = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		return map.get(strMap);  
	}
		
	public void editDriOneOneSave()
	{
		try {
			schoolService.updateObjectInDb(editDriDutyObject);
			 RequestContext.getCurrentInstance().update("dreDlgid");
			 loadlist();
			 RequestContext.getCurrentInstance().update("viewform");
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
	}
		
public void deleteDriDuty(DrivingDutyOfPilotDrivingSection driEditObjet)
{
	
	String id1 = null;
	String id2 = null;
	@SuppressWarnings("unchecked")
	List<DrivingDutyOfPilotDrivingSection> listDrivingduty=null;
	List<CrewChangeDrivingsection> listCrewChange = null;
	try {
		listDrivingduty = (List<DrivingDutyOfPilotDrivingSection>) schoolService.loadList("from DrivingDutyOfPilotDrivingSection");
		listCrewChange = (List<CrewChangeDrivingsection>) schoolService.loadList("from CrewChangeDrivingsection");
	} catch (CrewException e) {
		CREW_LOGGER.debug(e.getMessage());
		e.printStackTrace();
	}
	Long id = driEditObjet.getId();
	for(DrivingDutyOfPilotDrivingSection d : listDrivingduty)
	{
		if(d.getId().equals(id))
		{
			id1 = d.getDeletedId1();
			id2 = d.getDeletedId2();
		}
	}

	if(id1 != null) schoolService.updateCrewChangeTable(Long.parseLong(id1), false);
	if(id2 != null) schoolService.updateCrewChangeTable(Long.parseLong(id2), false);
	
	String str = "DrivingDutyOfPilotDrivingSection";
	schoolService.deleteById(driEditObjet.getId(),str);
	loadlist();
	RequestContext.getCurrentInstance().update("viewform");
}




/// for show purpose 
public void filteredManipulation()
{
	totalfilteredDistance = (long) 0;
	totalfilteredHours = (long) 0;
	String strHourAndMinute[] = new String[100];
	String strHour[] = new String[100];
	String strMinute[] = new String[100];
	Long lHours[] = new Long[100];
	
	int count = 0;
	for(CrewChangeDrivingsection c : filteredCrewChangeList5)
	{
		strHourAndMinute[count] = c.getHour(); 
		totalfilteredDistance = totalfilteredDistance + c.getKilometer();
		count++;
	}
	Integer iHourMinute[] = new Integer[100];
	int count1 = 0;
	for(int i = 0 ; i<count; i++)
	{
		
		String[] strSplitParts = strHourAndMinute[i].split(Pattern.quote("."));
		for (int j = 0; j< strSplitParts.length-1;j++)
		{
			iHourMinute[count1] = Integer.parseInt(strSplitParts[j])*60 + Integer.parseInt(strSplitParts[j+1]);
		}
	     
		strHour[i] = strHourAndMinute[i].substring(0, 1);
		lHours[i] = Long.parseLong(strHour[i]);
		totalfilteredHours = totalfilteredHours + iHourMinute[count1];
		System.out.println("totalfilteredHours hours is ->>>" +totalfilteredHours);
		if(totalfilteredHours > smallest)
		{
			smallest = totalfilteredHours;
		}
		count1++;
		
		strMinute[i] = strHourAndMinute[i].substring(2,3);
		
	}
	for(int k = 0; k<count1; k++)
	{
		if(iHourMinute[k] < smallest)
		{
			smallest = iHourMinute[k].longValue();
		}
	}
	Long lHour = smallest/60;
	Long lMinute = smallest%60;
	
	String strHour1 = String.valueOf(lHour);
	String strMinute1 = String.valueOf(lMinute);
	
	strCombineHourAndMinute = strHour1.concat("." +strMinute1);
	long ltotalfilteredHours = totalfilteredHours /60;
	long ltotalFilteredMinutes = totalfilteredHours % 60;
	
	String strTotalFilteredHours =  String.valueOf(ltotalfilteredHours);
	String strTotalFilteredMinutes = String.valueOf(ltotalFilteredMinutes);
	
	setTotalFilteredHourAndMinutes(strTotalFilteredHours.concat("." +strTotalFilteredMinutes));
	
	
	avgDrivingSection = (Double) totalfilteredHours.doubleValue() /  filteredCrewChangeList5.size();
	avgDrivingSection = avgDrivingSection /60;
	avgDrivingSection =Double.parseDouble(new DecimalFormat("##.##").format(avgDrivingSection)); // only for Format
	totalFilteredValues = filteredCrewChangeList5.size();
	RequestContext.getCurrentInstance().update("showForm");
	
}
	

public void removePilotJourney()
{
	try {
	schoolService.deletePilotJourney(MapString());
			  remove(MapString());
	    	  CrewUtilities.displayMessage("Info","Pilot has been deleted Successfully");
		
	} 
	catch (Exception e){
		CREW_LOGGER.debug(e.getMessage());
		e.printStackTrace();
	}
}

	
	private void remove(long deleteId) {

		for (int nIndex = 0; nIndex < PilotJourneylist.size(); nIndex++) {
			if (PilotJourneylist.get(nIndex).getId() == deleteId) {
				System.out.println("inside If Condition");
				PilotJourneylist.remove(nIndex);

			}

		}
	}





private Long MapString()
{ FacesContext fc = FacesContext.getCurrentInstance();
  Map<String, String> params = fc.getExternalContext()
				.getRequestParameterMap();
 return (Long.parseLong(params.get("QUERYPARAM")));
}
		


public void updatePilot()
{	if(!SelectOneFromload())
		{
	try {
		  pilotjourneyupdate = (PilotJourney) schoolService.loadUniqueObject("from PilotJourney as PJ where PJ.id="+MapString());
	} catch (CrewException e) {
		CREW_LOGGER.debug(e.getMessage());
		e.printStackTrace();
		  }
		}		
}
public void updateToDB()
{	if(!(pilotjourneyupdate==null))
		{
	     try {
			schoolService.updateObjectInDb(pilotjourneyupdate);
			PilotJourneylist  = (List<PilotJourney>) schoolService.loadList("from PilotJourney as ppt where ppt.customer.id like '"+customerloginId+"'");
		} catch (CrewException e) {
               CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
}

@SuppressWarnings("unchecked")
public void viewDrivingSectionsFromDrvingDuty(DrivingDutyOfPilotDrivingSection d)
{	Long lTransactionId = new Long(0);

System.out.println("inside View Driving Sections ");
	lTransactionId = d.getTransactionNoDsToDd();
	
	try {
		listCrewChange = (List<CrewChangeDrivingsection>) 
				schoolService.loadList("from CrewChangeDrivingsection as cc where cc.transactionNoDsToDd =" +lTransactionId);
		listPilotJourney = (List<PilotJourney>) schoolService.loadList("from PilotJourney as cc where cc.transactionNoDsToDd =" +lTransactionId);
		
		if(!listPilotJourney.isEmpty())
		{
			bPilottable= Boolean.TRUE;
		}
		
	} catch (CrewException e) {
		e.printStackTrace();
		CREW_LOGGER.debug(e.getMessage());
	}
	
			
}


private boolean SelectOneFromload()
{
	boolean bSave = Boolean.FALSE;
	List<CrewChangeDrivingsection> drivingsectionlist  = new ArrayList<CrewChangeDrivingsection>();
	  try {
		 drivingsectionlist  =  (List<CrewChangeDrivingsection>) this.schoolService.loadList("from CrewChangeDrivingsection");
		 if(!drivingsectionlist.isEmpty())
		 {
			 for(CrewChangeDrivingsection ccds:drivingsectionlist)
			  {  editFromSelectone.add(new SelectItem(ccds.getFromStation()));
			  	 editToSelectone.add(new SelectItem(ccds.getToStation()));
			  }	 
		 }
		 
	} catch (CrewException e) {
         CREW_LOGGER.debug(e.getMessage());
		e.printStackTrace();
	}
	  return bSave;
}


public void searchCrewBase()
{
	
	
	if((searchBase !=null)&& (searchType!=null))
	{
		System.out.print(""+searchBase);
		System.out.print(""+searchType);
		try {
			listDrivingDutyView = (List<DrivingDutyOfPilotDrivingSection>) schoolService.loadList("from DrivingDutyOfPilotDrivingSection as ddpds where ddpds.crewBase like '"+searchBase+"' and ddpds.crewType like '"+searchType+"'");
		} catch (CrewException e) {
		     CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
	}else
	{
		try {
			listDrivingDutyView = (List<DrivingDutyOfPilotDrivingSection>) schoolService.loadList("from DrivingDutyOfPilotDrivingSection as ddpds");
		} catch (CrewException e) {
			 CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
	}
		
		
	
}





@SuppressWarnings("unchecked")
public void roundTripSuggestion2()
{
	Map<String,String> mapAvailAndId = new LinkedHashMap<String,String>();
	Map<Date,String> mapDepAndId = new LinkedHashMap<Date,String>();
	
	Map<String,String> mapAvailAndId2 = new LinkedHashMap<String,String>();
	Map<Date,String> mapDepAndId2 = new LinkedHashMap<Date,String>();
	
	
	List<String> listAvail = new LinkedList<String>();
	List<Date> listDepa  = new LinkedList<Date>();
	
	List<String> listAvailSecond = new LinkedList<String>();
	List<Date> listDepaFirst  = new LinkedList<Date>();
	
	List<DrivingDutyOfPilotDrivingSection> listDrivingDuty1 = (List<DrivingDutyOfPilotDrivingSection>) 
			context.getBean("list");     
		
		List<DrivingDutyOfPilotDrivingSection> listDrivingDuty2 = (List<DrivingDutyOfPilotDrivingSection>) 
				context.getBean("list");
		
	/*System.out.println("-----------------------------------------");
	System.out.println("Number of Source Crew->>>" +iNumberOfSourceCrew);
	System.out.println("Number of Destination Crew->>>" +iNumberOfDestinationCrew);
	
	System.out.println("Number of Source Crew size->>>" +iSizeOfSourceCrew);
	System.out.println("Number of Destination Crew size->>>" +iSizeOfDestinationCrew);
	System.out.println("-----------------------------------------");*/
		
	try {
		listDrivingDuty1 = (List<DrivingDutyOfPilotDrivingSection>) 
				schoolService.loadList("from DrivingDutyOfPilotDrivingSection as d where d.fromStation like '"+selectedviewFrom+"'"+"and d.toStation like'"+selectedviewTo+"'");
		
		listDrivingDuty2 = (List<DrivingDutyOfPilotDrivingSection>) 
				schoolService.loadList("from DrivingDutyOfPilotDrivingSection as d where d.fromStation like '"+selectedviewFrom1+"'"+"and d.toStation like'"+selectedviewTo1+"'");

		
		schoolService.deleteQuery("RoundtripTemp");
		RoundtripTemp rTemp = (RoundtripTemp) context.getBean("roundtripTemp");
		for(DrivingDutyOfPilotDrivingSection d: listDrivingDuty1)
		{
			rTemp.setTrainNo(d.getTrainNo());
			rTemp.setFromStation(d.getFromStation());
			rTemp.setToStation(d.getToStation());
			rTemp.setDeparture(d.getDeparture());
			rTemp.setArrival(d.getArrival());
			rTemp.setSignon(d.getSignonTime());
			rTemp.setSignonff(d.getSignoffTime());
			rTemp.setAvailability(d.getAvailability());
			
			/*System.out.println(rTemp.getTrainNo());
			System.out.println(rTemp.getFromStation());
			System.out.println(rTemp.getToStation());
			System.out.println(rTemp.getDeparture());
			System.out.println(rTemp.getArrival());
			System.out.println(rTemp.getSignon());
			System.out.println(rTemp.getSignonff());
			System.out.println(rTemp.getAvailability());*/
			
			//System.out.println("-----------");
			
			schoolService.saveInDb(rTemp);
		}
	//	RoundtripTemp rTemp1 = (RoundtripTemp) context.getBean("roundtripTemp");
		for(DrivingDutyOfPilotDrivingSection d: listDrivingDuty2)
		{
			rTemp.setTrainNo(d.getTrainNo());
			rTemp.setFromStation(d.getFromStation());
			rTemp.setToStation(d.getToStation());
			rTemp.setDeparture(d.getDeparture());
			rTemp.setArrival(d.getArrival());
			rTemp.setSignon(d.getSignonTime());
			rTemp.setSignonff(d.getSignoffTime());
			rTemp.setAvailability(d.getAvailability());
			schoolService.saveInDb(rTemp);
		}
		
		for(DrivingDutyOfPilotDrivingSection d : listDrivingDuty1)
		{
		//	System.out.println("from station-->" + d.getFromStation() +"\t To Station--->>" + d.getToStation());
			mapAvailAndId.put(d.getAvailability(),d.getTrainNo());
			listAvail.add(d.getAvailability());
			listDepaFirst.add(d.getDeparture());
			mapDepAndId2.put(d.getDeparture(), d.getTrainNo());
			
		}
	//System.out.println("--------------List 2----------------------------");
		
		for(DrivingDutyOfPilotDrivingSection d : listDrivingDuty2)
		{
			//System.out.println("from station-->" + d.getFromStation() +"\t To Station--->>" + d.getToStation());
			mapDepAndId.put(d.getDeparture(),d.getTrainNo());
			listDepa.add(d.getDeparture());
			listAvailSecond.add(d.getAvailability());
			mapAvailAndId2.put(d.getAvailability(), d.getTrainNo());
		}
		schoolService.deleteQuery("FinalRoundtripSuggestion");
		//generateRoundTrip(listAvail, listDepa);
		generateRR27810pm(listAvail, listDepa,listAvailSecond,listDepaFirst,mapAvailAndId,mapDepAndId,mapAvailAndId2,mapDepAndId2);
		
	} catch (CrewException e) {
		CREW_LOGGER.debug(e.getMessage());
		e.printStackTrace();
	}
	
}

	
@SuppressWarnings("unchecked")
public void generateRR27810pm(List<String> listAvailable,List<Date> listDeparture,List<String> listAvailSecond,
		List<Date> listDepaFirst,Map<String,String> mapAvailableAndId,Map<Date,String> mapDepartureAndId,
		Map<String,String> mapAvailableAndTrainNo2, Map<Date,String> mapDepAndTrainNo2)
{
	
	lstRRSuggestion1 = (List<RoundtripSuggestions1>) context.getBean("list");
	lstRRSuggestion2 = (List<RoundtripSuggestion2>) context.getBean("list");
	schoolService.deleteQuery("RoundtripSuggestions1");
	schoolService.deleteQuery("RoundtripSuggestion2");
	
/*	for(Entry<String,String> entry : mapAvailableAndId.entrySet())
	{
		System.out.println("Key -> " +entry.getKey() +"\t\t Value->>> " +entry.getValue());
	}
	
	for(Entry<Date,String> entry : mapDepartureAndId.entrySet())
	{
		System.out.println("Key -> " +entry.getKey() +"\t\t Value->>> " +entry.getValue());
	}*/
	List<Integer> listHours = new LinkedList<>();
	List<Integer> listMinutes = new LinkedList<>();
	
	List<Integer> listHoursSecond = new LinkedList<>();
	List<Integer> listMinutesSecond = new LinkedList<>();
	for(String s : listAvailable)
	{
		//System.out.println(s);
		String[] strSplitParts = s.split(Pattern.quote(":"));
		for (int j = 0; j< strSplitParts.length-1;j++)
		{
			listHours.add(Integer.parseInt(strSplitParts[j])) ;
			listMinutes.add(Integer.parseInt(strSplitParts[j+1])) ;
		}
	}
	
	for(String s : listAvailSecond)
	{
		//System.out.println(s);
		String[] strSplitParts = s.split(Pattern.quote(":"));
		for (int j = 0; j< strSplitParts.length-1;j++)
		{
			listHoursSecond.add(Integer.parseInt(strSplitParts[j])) ;
			listMinutesSecond.add(Integer.parseInt(strSplitParts[j+1])) ;
		}
	}
	
	
	Date [] dtAvail = new Date[100];
	Date [] dtAvailSecond = new Date[100];
	int count11 = 0;
	int count22 = 0;
	Calendar calAvail = Calendar.getInstance();
	Calendar calAvailSecond = Calendar.getInstance();
	Date dtTest = new Date();
	calAvail.setTime(dtTest);
	calAvailSecond.setTime(dtTest);
	List<Date> listDates = new LinkedList<>();
	List<Date> listDatesSecond = new LinkedList<>();
	
	for(int i = 0; i<listHours.size(); i++)
	{
		
		calAvail.set(Calendar.HOUR_OF_DAY,listHours.get(i));
		calAvail.set(Calendar.MINUTE,listMinutes.get(i));
		dtAvail[count11] = calAvail.getTime();
		listDates.add(calAvail.getTime());
		count11++;
	}
	
	
	for(int i = 0; i<listAvailSecond.size(); i++)
	{
		
		calAvailSecond.set(Calendar.HOUR_OF_DAY,listHoursSecond.get(i));
		calAvailSecond.set(Calendar.MINUTE,listMinutesSecond.get(i));
		dtAvailSecond[count22] = calAvailSecond.getTime();
		listDatesSecond.add(calAvailSecond.getTime());
		count22++;
	}
	
	
	
	//System.out.println("---converted to dates are ------");
	Collections.sort(listDates, new Comparator<Date>(){

        @Override
        public int compare(Date o1, Date o2) {
            return o1.compareTo(o2);
        }
    });
	
	Collections.sort(listDatesSecond, new Comparator<Date>(){

        @Override
        public int compare(Date o1, Date o2) {
            return o1.compareTo(o2);
        }
    });
	System.out.println("---------------------------------");
	listHours.clear();
	listMinutes.clear();
    for(int i = 0; i < listDates.size(); i++){
     //  System.out.println(listDates.get(i).toString());
        calAvail.setTime(listDates.get(i));
        listHours.add(calAvail.get(Calendar.HOUR_OF_DAY));
        listMinutes.add(calAvail.get(Calendar.MINUTE));
    }
  //  System.out.println("------Hours and Minutes--------------------");
    listAvailable.clear();
    for(int i = 0; i<listHours.size();i++)
    {
    	//System.out.println(listHours.get(i)+":" +listMinutes.get(i));
    	String s = listHours.get(i).toString();
    	String s1 = listMinutes.get(i).toString();
    	
    	String s3 = s.concat(":" +s1);
    	listAvailable.add(s3);
    }
    
    System.out.println("---------------------------------");
    listHoursSecond.clear();
    listMinutesSecond.clear();
    for(int i = 0; i < listDatesSecond.size(); i++){
      //System.out.println(listDates.get(i).toString());
    	calAvailSecond.setTime(listDatesSecond.get(i));
        listHoursSecond.add(calAvailSecond.get(Calendar.HOUR_OF_DAY));
        listMinutesSecond.add(calAvailSecond.get(Calendar.MINUTE));
    }
  //  System.out.println("------Hours and Minutes--------------------");
    listAvailSecond.clear();
    for(int i = 0; i<listHoursSecond.size();i++)
    {
    	//System.out.println(listHours.get(i)+":" +listMinutes.get(i));
    	String s = listHoursSecond.get(i).toString();
    	String s1 = listMinutesSecond.get(i).toString();
    	
    	String s3 = s.concat(":" +s1);
    	listAvailSecond.add(s3);
    }
    
    
    
   /* System.out.println("Availability At " +selectedviewTo);
    
    for(int i = 0; i<listAvailable.size(); i++)
    {
    	
    	System.out.println(listAvailable.get(i));
    }
    System.out.println("Departure from " +selectedviewTo);
    for(int i = 0; i<listDeparture.size(); i++)
    {
    	System.out.println(listDeparture.get(i));
    }
    
    
    System.out.println("\n");

    System.out.println("Availability At " +selectedviewFrom);*/
    
    /*for(int i = 0; i<listAvailSecond.size(); i++)
    {
    	
    	System.out.println(listAvailSecond.get(i));
    }
    System.out.println("Departure from " +selectedviewFrom);
    for(int i = 0; i<listDepaFirst.size(); i++)
    {
    	System.out.println(listDepaFirst.get(i));
    }*/
		Map<Integer,Date> mapDiffDepAndDate = new LinkedHashMap<>();
		Map<Integer,String> mapDiffAndAvailable = new LinkedHashMap<>();
		int count1 = 0;
		int[] iSmallestArray = new int[100];
		int count= 0;
		Integer smallest = new Integer(0);
		
	//	System.out.println("Difference 1");
		for(int i = 0; i<listAvailable.size();i++)
		{
			
			Integer iDiff[] = new Integer[100];
			count=0;
			//getting train number of availability
		
			for(int j = 0; j<listDeparture.size(); j++)
			{
				
				//System.out.print(compareDepAndAvail(listDeparture.get(j),listAvailable.get(i)) +"\t");
				iDiff[count] = compareDepAndAvail(listDeparture.get(j),listAvailable.get(i));
				
				mapDiffDepAndDate.put(iDiff[count], listDeparture.get(j));
				mapDiffAndAvailable.put(iDiff[count],listAvailable.get(i));
				count++;
			}
			
			System.out.println();
			
			smallest = 9999;
			/*for(int j = 0 ;j<count;j++)
			{
				if(iDiff[j]<30)
				{
					iDiff[j] = iDiff[j+1];
					//count--;
				}
				//System.out.println(iDiff[j]);
			}*/
			for(int k=0;k<count;k++)
			{
				//System.out.println(iDiff[k]);
				if(smallest>iDiff[k])
				{
					smallest = iDiff[k];
				}
			}
			String strTrainNo1 = new String();
			String strTrainNo2 = new String();
		//	System.out.println("smallest->>> " +smallest);
			for(Entry<Integer,String> entry : mapDiffAndAvailable.entrySet())
			{
				if(smallest == entry.getKey())
				{
					System.out.println("\n\n-----------Available Train---------------");
			//		System.out.println(mapAvailableAndId.get(entry.getValue()));
					strTrainNo1 = mapAvailableAndId.get(entry.getValue());
					System.out.println("--------------------------");
				}
			}
			//getting train number from available map
			for(Entry<Integer,Date> entry : mapDiffDepAndDate.entrySet())
			{
				if(smallest == entry.getKey())
				{
					System.out.println("\n\n------Departure Train-------------------");
				//	System.out.println(mapDepartureAndId.get(entry.getValue()));
					strTrainNo2 = mapDepartureAndId.get(entry.getValue());
					listDeparture.remove(entry.getValue());
					System.out.println("--------------------------\n\n");
				}
			}
			String strAccessRunningRoomTest = new String();
			Integer nHour = new Integer(0);
			Integer nMinutes = new Integer(0);
			nHour = smallest / 60;
			nMinutes = smallest % 60;
			
			strAccessRunningRoomTest = nHour.toString().concat(":" +nMinutes.toString());
			//System.out.println("Accessing Running Room Rest is " + strAccessRunningRoomTest);
			
			RoundtripSuggestions1 r1 = (RoundtripSuggestions1) context.getBean("roundtripSuggestions1");
			r1.setTrainno1(strTrainNo1);
			r1.setTrainno2(strTrainNo2);
			r1.setAccessRunningroomRest(strAccessRunningRoomTest);
			try {
				schoolService.saveInDb(r1);
			} catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
			}
		}
		System.out.println("\n\n");
		System.out.println("Difference 2");
		count1 = 0;
		mapDiffDepAndDate.clear();
		mapDiffAndAvailable.clear();
		String strTrainNo3 = new String();
		String strTrainNo4 = new String();
		for(int i = 0; i<listAvailable.size();i++)
		{
			
			Integer iDiff[] = new Integer[100];
			count = 0;
			for(int j = 0; j<listDepaFirst.size(); j++)
			{
				
				//System.out.print(compareDepAndAvail(listDeparture.get(j),listAvailable.get(i)));
				
				//System.out.print(compareDepAndAvail(listDepaFirst.get(j),listAvailSecond.get(i)) +"\t");
				iDiff[count] = compareDepAndAvail(listDepaFirst.get(j),listAvailSecond.get(i));
				
				mapDiffDepAndDate.put(iDiff[count], listDepaFirst.get(j));
				mapDiffAndAvailable.put(iDiff[count], listAvailSecond.get(i));
				count++;
			}
			System.out.println();
			smallest = 9999;
			
			for(int k=0;k<count;k++)
			{
				//System.out.println(iDiff[k]);
				if(smallest>iDiff[k])
				{
					smallest = iDiff[k];
				}
			}
			System.out.println("smallest->>> " +smallest);
		//	listDepaFirst.remove(mapDiffDepAndDate.get(smallest));
			
			for(Entry<Integer,String> entry : mapDiffAndAvailable.entrySet())
			{
				if(smallest == entry.getKey())
				{
					System.out.println("\n\n-----------Available Train---------------");
				//	System.out.println(mapAvailableAndTrainNo2.get(entry.getValue()));
					strTrainNo3 = mapAvailableAndTrainNo2.get(entry.getValue());
					System.out.println("--------------------------");
				}
			}
			String strAccessRunningRoomTest2 = new String();
			Integer nHour = new Integer(0);
			Integer nMinutes = new Integer(0);
			nHour = smallest / 60;
			nMinutes = smallest % 60;
			
			strAccessRunningRoomTest2 = nHour.toString().concat(":" +nMinutes.toString());
			System.out.println("Accessing Running Room Rest is " + strAccessRunningRoomTest2);
			//getting train number from available map
			for(Entry<Integer,Date> entry : mapDiffDepAndDate.entrySet())
			{
				if(smallest == entry.getKey())
				{
					System.out.println("\n\n------Departure Train-------------------");
				//	System.out.println(mapDepAndTrainNo2.get(entry.getValue()));
					strTrainNo4 = mapDepAndTrainNo2.get(entry.getValue());
					listDepaFirst.remove(entry.getValue());
					System.out.println("--------------------------\n\n");
				}
			}
			RoundtripSuggestion2 r2 = (RoundtripSuggestion2) context.getBean("roundtripSuggestion2");
			r2.setTrainno1(strTrainNo3);
			r2.setTrainno2(strTrainNo4);
			r2.setAccessRunningroomRest(strAccessRunningRoomTest2);
			try {
				schoolService.saveInDb(r2);
			} catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
			}
		}
		try {
			lstRRSuggestion1 = (List<RoundtripSuggestions1>) schoolService.loadList("from RoundtripSuggestions1");
			
			lstRRSuggestion2 =  (List<RoundtripSuggestion2>) schoolService.loadList("from RoundtripSuggestion2");
			System.out.println("---------------RR 1-----------");
		/*	for(RoundtripSuggestions1 r : lstRRSuggestion1)
			{
				System.out.println(r.getTrainno1() +"\t\t " +r.getTrainno2());
			}*/
			RequestContext.getCurrentInstance().update(":viewform4 :crew:rrlist1");
			
			
			System.out.println("Selected Id is " +lRoundTripSuggestionId);
			
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
}

@SuppressWarnings("unchecked")
public void choosedRowFromRoundTripSuggestion1(Long id)
{
	
	String strTrain1 = new String();
	String strTrain2 = new String();
	Map<String,String> mapAvailAndId = new LinkedHashMap<String,String>();
	Map<Date,String> mapDepAndId = new LinkedHashMap<Date,String>();
	
	Map<String,String> mapAvailAndId2 = new LinkedHashMap<String,String>();
	Map<Date,String> mapDepAndId2 = new LinkedHashMap<Date,String>();
	
	
	List<String> listAvail = new LinkedList<String>();
	List<Date> listDepa  = new LinkedList<Date>();
	
	List<String> listAvailSecond = new LinkedList<String>();
	List<Date> listDepaFirst  = new LinkedList<Date>();
	//System.out.println("Selected Id is " +id);
	List<RoundtripTemp> listDrivingDuty1 = (List<RoundtripTemp>) 
			context.getBean("list");     
		
		List<RoundtripTemp> listDrivingDuty2 = (List<RoundtripTemp>) 
				context.getBean("list");
		
	try {
		List<RoundtripTemp> listRR2 = (List<RoundtripTemp>)
				schoolService.loadList("from RoundtripTemp as rr2");
		
		List<RoundtripSuggestions1> listRR1 = (List<RoundtripSuggestions1>)
		schoolService.loadList("from RoundtripSuggestions1 as rr where rr.id=" +id);
		if(!listRR2.isEmpty())
			for(RoundtripTemp r1 : listRR2)
			{
				for(RoundtripSuggestions1 r : listRR1)
				{
					if(r1.getTrainNo().equalsIgnoreCase(r.getTrainno1()))
					{
					//	System.out.println("Exist in 1 ");
						schoolService.deleteUsingTrainNumber("RoundtripTemp", r.getTrainno1());
						strTrain1 = r.getTrainno1();
					}
					if(r1.getTrainNo().equalsIgnoreCase(r.getTrainno2()))
					{
						//System.out.println("Exist in 2 ");
						schoolService.deleteUsingTrainNumber("RoundtripTemp", r.getTrainno2());
						strTrain2 = r.getTrainno2(); 
					}
					
					
				}
			
			}
		
		mapDeletedRRSuggestion1.put(strTrain1, strTrain2);
		listDrivingDuty1 = (List<RoundtripTemp>) 
				schoolService.loadList("from RoundtripTemp as d where d.fromStation like '"+selectedviewFrom+"'"+"and d.toStation like'"+selectedviewTo+"'");
		
		listDrivingDuty2 = (List<RoundtripTemp>) 
				schoolService.loadList("from RoundtripTemp as d where d.fromStation like '"+selectedviewFrom1+"'"+"and d.toStation like'"+selectedviewTo1+"'");

		
		for(RoundtripTemp d : listDrivingDuty1)
		{
			//System.out.println("" +d.getFromStation() +"\t\t " +d.getToStation());
			mapAvailAndId.put(d.getAvailability(),d.getTrainNo());
			listAvail.add(d.getAvailability());
			listDepaFirst.add(d.getDeparture());
			mapDepAndId2.put(d.getDeparture(), d.getTrainNo());
		}
		for(RoundtripTemp d : listDrivingDuty2)
		{
			//System.out.println("" +d.getFromStation() +"\t\t " +d.getToStation());
			
			mapDepAndId.put(d.getDeparture(),d.getTrainNo());
			listDepa.add(d.getDeparture());
			listAvailSecond.add(d.getAvailability());
			mapAvailAndId2.put(d.getAvailability(), d.getTrainNo());
		}
			generateRR27810pm(listAvail, listDepa,listAvailSecond,listDepaFirst,mapAvailAndId,mapDepAndId,mapAvailAndId2,mapDepAndId2);
			
		//RequestContext.getCurrentInstance().update(":viewform4 :crew:rrlist1 :crew:rrlist2");
		
			//System.out.println("size of 1 " +lstRRSuggestion1.size());
			//System.out.println("size of 2 " +lstRRSuggestion2.size());
			
			if(lstRRSuggestion1.size()==0 || lstRRSuggestion2.size()==0 )
			{
				showRR();
			}
	} catch (CrewException e) {
		CREW_LOGGER.debug(e.getMessage());
		e.printStackTrace();
	}
	
}

@SuppressWarnings("unchecked")
public void choosedRowFromRoundTripSuggestion2(Long id)
{
	String strTrain1 = new String();
	String strTrain2 = new String();
	Map<String,String> mapAvailAndId = new LinkedHashMap<String,String>();
	Map<Date,String> mapDepAndId = new LinkedHashMap<Date,String>();
	
	Map<String,String> mapAvailAndId2 = new LinkedHashMap<String,String>();
	Map<Date,String> mapDepAndId2 = new LinkedHashMap<Date,String>();
	
	
	List<String> listAvail = new LinkedList<String>();
	List<Date> listDepa  = new LinkedList<Date>();
	
	List<String> listAvailSecond = new LinkedList<String>();
	List<Date> listDepaFirst  = new LinkedList<Date>();
	//System.out.println("Selected Id is " +id);
	List<RoundtripTemp> listDrivingDuty1 = (List<RoundtripTemp>) 
			context.getBean("list");     
		
		List<RoundtripTemp> listDrivingDuty2 = (List<RoundtripTemp>) 
				context.getBean("list");
		
	try {
		List<RoundtripTemp> listRR2 = (List<RoundtripTemp>)
				schoolService.loadList("from RoundtripTemp as rr2");
		
		List<RoundtripSuggestion2> listRR1 = (List<RoundtripSuggestion2>)
		schoolService.loadList("from RoundtripSuggestion2 as rr where rr.id=" +id);
		if(!listRR2.isEmpty())
			for(RoundtripTemp r1 : listRR2)
			{
				for(RoundtripSuggestion2 r : listRR1)
				{
					if(r1.getTrainNo().equalsIgnoreCase(r.getTrainno1()))
					{
					//	System.out.println("Exist in 1 ");
						schoolService.deleteUsingTrainNumber("RoundtripTemp", r.getTrainno1());
						strTrain1 = r.getTrainno1();
						//.put
					}
					if(r1.getTrainNo().equalsIgnoreCase(r.getTrainno2()))
					{
					//	System.out.println("Exist in 2 ");
						schoolService.deleteUsingTrainNumber("RoundtripTemp", r.getTrainno2());
						strTrain2 = r.getTrainno2(); 
					}
					
					
				}
				
			}
		
		mapDeletedRRSuggestion2.put(strTrain1, strTrain2);
		listDrivingDuty1 = (List<RoundtripTemp>) 
				schoolService.loadList("from RoundtripTemp as d where d.fromStation like '"+selectedviewFrom+"'"+"and d.toStation like'"+selectedviewTo+"'");
		
		listDrivingDuty2 = (List<RoundtripTemp>) 
				schoolService.loadList("from RoundtripTemp as d where d.fromStation like '"+selectedviewFrom1+"'"+"and d.toStation like'"+selectedviewTo1+"'");

		
		for(RoundtripTemp d : listDrivingDuty1)
		{
			//System.out.println("" +d.getFromStation() +"\t\t " +d.getToStation());
			mapAvailAndId.put(d.getAvailability(),d.getTrainNo());
			listAvail.add(d.getAvailability());
			listDepaFirst.add(d.getDeparture());
			mapDepAndId2.put(d.getDeparture(), d.getTrainNo());
		}
		for(RoundtripTemp d : listDrivingDuty2)
		{
			//System.out.println("" +d.getFromStation() +"\t\t " +d.getToStation());
			
			mapDepAndId.put(d.getDeparture(),d.getTrainNo());
			listDepa.add(d.getDeparture());
			listAvailSecond.add(d.getAvailability());
			mapAvailAndId2.put(d.getAvailability(), d.getTrainNo());
		}
		
	
			generateRR27810pm(listAvail, listDepa,listAvailSecond,listDepaFirst,mapAvailAndId,mapDepAndId,mapAvailAndId2,mapDepAndId2);
			
			

			
			if(lstRRSuggestion1.size()==0 || lstRRSuggestion2.size()==0 )
			{
				//System.out.println("Became Empty!!!!!!!!!!!!!");
				showRR();
			}
			
		//RequestContext.getCurrentInstance().update(":viewform4 :crew:rrlist1 :crew:rrlist2");
	} catch (CrewException e) {
		CREW_LOGGER.debug(e.getMessage());
		e.printStackTrace();
	}
}

@SuppressWarnings("unchecked")
public void showRR()
{FinalRoundtripSuggestion finalRR = (FinalRoundtripSuggestion) context.getBean("finalRoundtripSuggestion");
	for(Entry<String,String> entry : mapDeletedRRSuggestion1.entrySet())
	{
		//System.out.println("1 " +entry.getKey() +"\t\t 2 " +entry.getValue());
		finalRR.setTrainno1(entry.getKey());
		finalRR.setTrainno2(entry.getValue());
		
		try {
			schoolService.saveInDb(finalRR);
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
	}
	System.out.println("------------------------");
	for(Entry<String,String> entry : mapDeletedRRSuggestion2.entrySet())
	{
	//	System.out.println("1 " +entry.getKey() +"\t\t 2 " +entry.getValue());
		finalRR.setTrainno1(entry.getKey());
		finalRR.setTrainno2(entry.getValue());
		
		try {
			schoolService.saveInDb(finalRR);
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
	}

		
	
	RequestContext.getCurrentInstance().execute("PF('RRFinalResultwdgt').show();");
	RequestContext.getCurrentInstance().update(":viewform4 :finalId:del1 :finalId:del2");
	
}
@SuppressWarnings({ "unchecked", "unused" })
public void generateRoundTrip(List<String> listAvailable,List<Date> listDeparture)
{
	//System.out.println("------------Available Dates are----------------------");
	List<Integer> listHours = new LinkedList<>();
	List<Integer> listMinutes = new LinkedList<>();
	for(String s : listAvailable)
	{
		//System.out.println(s);
		String[] strSplitParts = s.split(Pattern.quote(":"));
		for (int j = 0; j< strSplitParts.length-1;j++)
		{
			listHours.add(Integer.parseInt(strSplitParts[j])) ;
			listMinutes.add(Integer.parseInt(strSplitParts[j+1])) ;
		}
	}
	
	
	Date [] dtAvail = new Date[100];
	int count11 = 0;
	Calendar calAvail = Calendar.getInstance();
	Date dtTest = new Date();
	calAvail.setTime(dtTest);
	List<Date> listDates = new LinkedList<>();
	for(int i = 0; i<listHours.size(); i++)
	{
		
		calAvail.set(Calendar.HOUR_OF_DAY,listHours.get(i));
		calAvail.set(Calendar.MINUTE,listMinutes.get(i));
		dtAvail[count11] = calAvail.getTime();
		listDates.add(calAvail.getTime());
		count11++;
	}
	//System.out.println("---converted to dates are ------");
	Collections.sort(listDates, new Comparator<Date>(){

        @Override
        public int compare(Date o1, Date o2) {
            return o1.compareTo(o2);
        }
    });
	System.out.println("---------------------------------");
	listHours.clear();
	listMinutes.clear();
    for(int i = 0; i < listDates.size(); i++){
       // System.out.println(listDates.get(i).toString());
        calAvail.setTime(listDates.get(i));
        listHours.add(calAvail.get(Calendar.HOUR_OF_DAY));
        listMinutes.add(calAvail.get(Calendar.MINUTE));
    }
  //  System.out.println("------Hours and Minutes--------------------");
    listAvailable.clear();
    for(int i = 0; i<listHours.size();i++)
    {
    	//System.out.println(listHours.get(i)+"\t\t" +listMinutes.get(i));
    	String s = listHours.get(i).toString();
    	String s1 = listMinutes.get(i).toString();
    	
    	String s3 = s.concat(":" +s1);
    	listAvailable.add(s3);
    }
	List<DrivingDutyOfPilotDrivingSection> listDrivingDuty1 = (List<DrivingDutyOfPilotDrivingSection>) 
		context.getBean("list");     
	
	List<DrivingDutyOfPilotDrivingSection> listDrivingDuty2 = (List<DrivingDutyOfPilotDrivingSection>) 
			context.getBean("list");
	Map<Integer,Date> mapDiffPlusDep = new LinkedHashMap<Integer,Date>();
	Map<Integer,String> mapAvailPlusDep = new LinkedHashMap<Integer,String>();
	int count1 = 0;
	int[] iSmallestArray = new int[100];
	int count= 0;
	Integer smallest = new Integer(0);
	List<Date> listSmallestDep = new LinkedList<Date>();
	List<String> listSmallestAvail = new LinkedList<String>();
	
	
	for(int i = 0; i<listAvailable.size();i++)
	{
		
		Integer iDiff[] = new Integer[100];
		for(int j = 0; j<listAvailable.size(); j++)
		{
			
			//System.out.print(compareDepAndAvail(listDeparture.get(j),listAvailable.get(i)));
			System.out.println("ava-->> " +listAvailable.get(i) +" dep->" +listDeparture.get(j)+" diff->>" +compareDepAndAvail(listDeparture.get(j),listAvailable.get(i)));
			iDiff[count] = compareDepAndAvail(listDeparture.get(j),listAvailable.get(i));
			mapDiffPlusDep.put(iDiff[count], listDeparture.get(j));
			mapAvailPlusDep.put(iDiff[count], listAvailable.get(i));
			count++;
		}
		smallest = 9999;
		for(int j = 0 ;j<count;j++)
		{
			if(iDiff[j]<30)
			{
				iDiff[j] = iDiff[j+1];
				//count--;
			}
		}
		for(int k=0;k<count;k++)
		{
			System.out.println(iDiff[k]);
			if(smallest>iDiff[k])
			{
				smallest = iDiff[k];
			}
		}
		System.out.println("smallest->>> " +smallest);
		iSmallestArray[count1] = smallest; 
		count1++;
		System.out.println("smallest departure is ->>>"+mapDiffPlusDep.get(smallest) +"\t smallest available is --->>" +mapAvailPlusDep.get(smallest));
		listSmallestDep.add(mapDiffPlusDep.get(smallest));
		listSmallestAvail.add(mapAvailPlusDep.get(smallest));
		
		System.out.println("------------Before Removing-----------------");
		for(Date d : listDeparture)
		{
			System.out.println(d);
		}
		for(String s : listAvailable)
		{
			System.out.println(s);
		}
		System.out.println("size of listDeparture->>> " +listDeparture.size());
		System.out.println("size of listAvailable->>> " +listAvailable.size());
		System.out.println("-----------------------------------------");
		
		listDeparture.remove(mapDiffPlusDep.get(smallest));
		listAvailable.remove(mapAvailPlusDep.get(smallest));
		
		System.out.println("------------After Removing-----------------");
		for(Date d : listDeparture)
		{
			System.out.println(d);
		}
		for(String s : listAvailable)
		{
			System.out.println(s);
		}
		System.out.println("size of listDeparture->>> " +listDeparture.size());
		System.out.println("size of listAvailable->>> " +listAvailable.size());
		System.out.println("-----------------------------------------");
		
		if(listAvailable.size()==1)
		{
			System.out.println("smallest departure is ->>>"+listDeparture.get(0) +"\t smallest available is --->>" +listAvailable.get(0));
			iSmallestArray[count1] = compareDepAndAvail(listDeparture.get(0),listAvailable.get(0));
			
			
			System.out.println("&&&&&&&&&&&&&&&&&&>>> " +compareDepAndAvail(listDeparture.get(0),listAvailable.get(0)));
			mapDiffPlusDep.put(iSmallestArray[count1], listDeparture.get(0));
			mapAvailPlusDep.put(iSmallestArray[count1], listAvailable.get(0));
			count1++;
		}
		count=0;
		i=-1;
		
	}
	System.out.println("size of count1->>> " +count1);
	for(int i = 0; i<iNumberOfSourceCrew; i++)
	{
		System.out.print("" +iSmallestArray[i]+"\t");
		System.out.print(mapDiffPlusDep.get(iSmallestArray[i])+"\t");
		System.out.println(mapAvailPlusDep.get(iSmallestArray[i]));
		
		String strTrainNoAvailable = new String();
		String strTrainNoDeparture = new String();
		try {
			listDrivingDuty1 = (List<DrivingDutyOfPilotDrivingSection>) 
					schoolService.loadList("from DrivingDutyOfPilotDrivingSection as d where d.fromStation like '"+selectedviewFrom+"'"+"and d.toStation like'"+selectedviewTo+"'");
			listDrivingDuty2 = (List<DrivingDutyOfPilotDrivingSection>) 
					schoolService.loadList("from DrivingDutyOfPilotDrivingSection as d where d.fromStation like '"+selectedviewFrom1+"'"+"and d.toStation like'"+selectedviewTo1+"'");
			
			System.out.println("----------------small and available-----------------------------");	
			System.out.println("smallest difference->>>>>>>>>>>>>>"+iSmallestArray[i]);
			for(DrivingDutyOfPilotDrivingSection d : listDrivingDuty1)
			{
				if(mapAvailPlusDep.get(iSmallestArray[i]).equalsIgnoreCase(d.getAvailability()))
				{
					System.out.println("exist availabl->>>>>" + d.getAvailability());
					strTrainNoAvailable = d.getTrainNo();
				}
			}
			System.out.println("----------------small and departure-----------------------------");
			for(DrivingDutyOfPilotDrivingSection d : listDrivingDuty2)
			{
				if(mapDiffPlusDep.get(iSmallestArray[i]).compareTo(d.getDeparture())==0)
				{
					System.out.println("exist departure ->>>>>" + d.getDeparture());
					strTrainNoDeparture = d.getTrainNo();
				}
			}
			//Final Trains has been assigned
			map_Available_Train_Departure_Train.put(strTrainNoAvailable, strTrainNoDeparture);
			
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
	}
	System.out.println("---------------------------------------------");
	
	List<String> listTraiNumbers1 = new LinkedList<String>();
	List<String> listTraiNumbers2 = new LinkedList<String>();
	String strAvail = new String();
	try {
		listDrivingDuty1 = (List<DrivingDutyOfPilotDrivingSection>) 
		schoolService.loadList("from DrivingDutyOfPilotDrivingSection as d where d.fromStation like '"+selectedviewFrom+"'"+"and d.toStation like'"+selectedviewTo+"'");
		listDrivingDuty2 = (List<DrivingDutyOfPilotDrivingSection>) 
		schoolService.loadList("from DrivingDutyOfPilotDrivingSection as d where d.fromStation like '"+selectedviewFrom1+"'"+"and d.toStation like'"+selectedviewTo1+"'");
		
	} catch (CrewException e) {
		CREW_LOGGER.debug(e.getMessage());
		e.printStackTrace();
	}
	System.out.println("-----------------------------------------------");
	List<String> listTrainNumbers1 = new LinkedList<String>();
	List<String> listTrainNumbers2 = new LinkedList<String>();
	for(Entry<String,String> entry : map_Available_Train_Departure_Train.entrySet())
	{
		System.out.println("train ->>" + entry.getKey() +"\t is assigned to->>" +entry.getValue());
		listTrainNumbers1.add(entry.getKey());
		listTrainNumbers2.add(entry.getValue());
	}
	System.out.println("-----------------------------------------------");
		//System.out.println("-----------Train Numbers1------------------------------------");
		List<String> listTrainNumbers3 = new LinkedList<String>();
		List<String> listTrainNumbers4 = new LinkedList<String>();
		for(DrivingDutyOfPilotDrivingSection d : listDrivingDuty1)
		{
			listTrainNumbers3.add(d.getTrainNo());
		}
		
		for(DrivingDutyOfPilotDrivingSection d : listDrivingDuty2)
		{
			listTrainNumbers4.add(d.getTrainNo());
		}
		
		Map<Long,String> mapTrain1 = new LinkedHashMap<Long,String>();
		Map<Long,String> mapTrain2 = new LinkedHashMap<Long,String>();
		
		Map<Long,String> mapTrain3 = new LinkedHashMap<Long,String>();
		Map<Long,String> mapTrain4 = new LinkedHashMap<Long,String>();
		Long count3 = (long)0;
		Long count4 = (long)0;
		for(String s : listTrainNumbers3)
		{
			for(String s1 : listTrainNumbers1)
			{
				if(s1.equalsIgnoreCase(s))
				{
					mapTrain1.put(count3, s1);
					count3++;
					
				}
				else
				{
					mapTrain2.put(count4, s);
					count4++;
				}
			}
			System.out.println();
		}
		count3 = (long)0;
		count4 = (long)0;
		for(String s : listTrainNumbers4)
		{
			for(String s1 : listTrainNumbers2)
			{
				if(s1.equalsIgnoreCase(s))
				{
					mapTrain3.put(count3, s1);
					count3++;
					
				}
				else
				{
					mapTrain4.put(count4, s);
					count4++;
				}
			}
			System.out.println();
		}
		System.out.println("---------------------------------------------");
		Set<String> setTrainNumbers = new LinkedHashSet<String>();
		Set<String> setTrainNumbers2 = new LinkedHashSet<String>();
		for(Entry<Long,String> entry : mapTrain2.entrySet())
		{
			if(mapTrain1.containsValue(entry.getValue()))
			{
			}
			else
			{
				setTrainNumbers.add(entry.getValue());
			}
		}
		for(Entry<Long,String> entry : mapTrain4.entrySet())
		{
			if(mapTrain3.containsValue(entry.getValue()))
			{
			}
			else
			{
				setTrainNumbers2.add(entry.getValue());
			}
		}
	roundTripDestinationSuggestion(setTrainNumbers,setTrainNumbers2,map_Available_Train_Departure_Train);
}

/*@SuppressWarnings("unchecked")
public void roundTripDestinationSuggestion(Set<String> setTrainNumbers,Set<String> setTrainNumbers2,Map<String,String> map_Available_Train_Departure_Train)
{
	
	List<DrivingDutyOfPilotDrivingSection> list1 = (List<DrivingDutyOfPilotDrivingSection>) context.getBean("list");
	List<DrivingDutyOfPilotDrivingSection> list2 = (List<DrivingDutyOfPilotDrivingSection>) context.getBean("list");

	
	
	String strAvailable = new String();
	Date dtDeparture = new Date();
	String strTrain1 = new String();
	String strTrain2 = new String();
	List<String> listTrainNumbers = new LinkedList<String>();
	int iSmallestArray[] = new int[100];
	int count6=0;
	int small = 0;
	int iDiff[] = new int[100];
	int count5 = 0;


	List<String> listSet1 = new ArrayList<String>(setTrainNumbers);
	List<String> listSet2 = new ArrayList<String>(setTrainNumbers2);

	Map<String,Integer> mapTrainNumber2 = new LinkedHashMap<>();
	
	
	
		for(String s : setTrainNumbers2)
		{
			Map<String,Integer> mapTrain1_Train2 = new LinkedHashMap<>();
			count5 = 0;
			count6=0;
			try {
				list1 = (List<DrivingDutyOfPilotDrivingSection>) schoolService.loadList("from DrivingDutyOfPilotDrivingSection as d where d.trainNo like'"+s+"'");
				strAvailable = list1.get(0).getAvailability();
				strTrain1 = list1.get(0).getTrainNo();
			} catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
			}
			for(String s1 : setTrainNumbers)
			{
				System.out.println(s+"\t\t -> " +s1);
				try {
					list2 = (List<DrivingDutyOfPilotDrivingSection>) schoolService.loadList("from DrivingDutyOfPilotDrivingSection as d where d.trainNo like'"+s1+"'");
					dtDeparture = list2.get(0).getDeparture();
					strTrain2 = list2.get(0).getTrainNo();
				} catch (CrewException e) {
					CREW_LOGGER.debug(e.getMessage());
					e.printStackTrace();
				}
				System.out.println(strAvailable+"\t\t -> " +dtDeparture);
				System.out.println("diff bet dep and available");
				System.out.println(compareDepAndAvail(dtDeparture, strAvailable));
				iDiff[count5] = compareDepAndAvail(dtDeparture, strAvailable);
				String strTrain3 = strTrain1.concat("+"+strTrain2);
				mapTrain1_Train2.put(strTrain3, iDiff[count5]);
				mapTrainNumber2.put(strTrain2, iDiff[count5]);
				count5++;
			}
			small = 99999;
			for(int i = 0 ; i<count5;i++)
			{
				System.out.println(iDiff[i]);
				if(small>iDiff[i])
				{
					small = (int) iDiff[i];
				}
			}
			System.out.println("small->>>>"+small);
			iSmallestArray[count6] = small; 
			count6++;
			
			for(Entry<String,Integer> entry : mapTrain1_Train2.entrySet())
			{
				if(entry.getValue()==small)
				{
					System.out.println("Train Numbers->" +entry.getKey() +"\t\t diff->" +entry.getValue());	
					listTrainNumbers.add(entry.getKey());
				}
			}
			String strTrainNumberToRemove = new String();
			for(Entry<String,Integer> entry : mapTrainNumber2.entrySet())
			{
				if(entry.getValue()==small)
				{
					//System.out.println("Train Numbers->" +entry.getKey() +"\t\t diff->" +entry.getValue());	
					//listTrainNumbers.add(entry.getKey());
					
					strTrainNumberToRemove = entry.getKey();
				}
			}
			
			setTrainNumbers.remove(strTrainNumberToRemove);
			System.out.println("-----------------------------------------");
		}
		roundTripAssignmentsOfTrains(map_Available_Train_Departure_Train,listTrainNumbers);
}*/
@SuppressWarnings("unchecked")
public void roundTripDestinationSuggestion(Set<String> setTrainNumbers,Set<String> setTrainNumbers2,Map<String,String> map_Available_Train_Departure_Train)
{
	
	List<DrivingDutyOfPilotDrivingSection> list1 = (List<DrivingDutyOfPilotDrivingSection>) context.getBean("list");
	List<DrivingDutyOfPilotDrivingSection> list2 = (List<DrivingDutyOfPilotDrivingSection>) context.getBean("list");

	
	
	String strAvailable = new String();
	Date dtDeparture = new Date();
	String strTrain1 = new String();
	String strTrain2 = new String();
	List<String> listTrainNumbers = new LinkedList<String>();
	int iSmallestArray[] = new int[100];
	int count6=0;
	int small = 0;
	int iDiff[] = new int[100];
	int count5 = 0;


	List<String> listSet1 = new ArrayList<String>(setTrainNumbers);
	List<String> listSet2 = new ArrayList<String>(setTrainNumbers2);

	Map<String,Integer> mapTrainNumber2 = new LinkedHashMap<>();
	
	
	
		for(String s : setTrainNumbers2)
		{
			Map<String,Integer> mapTrain1_Train2 = new LinkedHashMap<>();
			count5 = 0;
			count6=0;
			try {
				list1 = (List<DrivingDutyOfPilotDrivingSection>) schoolService.loadList("from DrivingDutyOfPilotDrivingSection as d where d.trainNo like'"+s+"'");
				strAvailable = list1.get(0).getAvailability();
				strTrain1 = list1.get(0).getTrainNo();
			} catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
			}
			for(String s1 : setTrainNumbers)
			{
				System.out.println(s+"\t\t -> " +s1);
				try {
					list2 = (List<DrivingDutyOfPilotDrivingSection>) schoolService.loadList("from DrivingDutyOfPilotDrivingSection as d where d.trainNo like'"+s1+"'");
					dtDeparture = list2.get(0).getDeparture();
					strTrain2 = list2.get(0).getTrainNo();
				} catch (CrewException e) {
					CREW_LOGGER.debug(e.getMessage());
					e.printStackTrace();
				}
				System.out.println(strAvailable+"\t\t -> " +dtDeparture);
				System.out.println("diff bet dep and available");
				System.out.println(compareDepAndAvail(dtDeparture, strAvailable));
				iDiff[count5] = compareDepAndAvail(dtDeparture, strAvailable);
				String strTrain3 = strTrain1.concat("+"+strTrain2);
				mapTrain1_Train2.put(strTrain3, iDiff[count5]);
				mapTrainNumber2.put(strTrain2, iDiff[count5]);
				count5++;
			}
			small = 99999;
			for(int i = 0 ; i<count5;i++)
			{
				System.out.println(iDiff[i]);
				if(small>iDiff[i])
				{
					small = (int) iDiff[i];
				}
			}
			System.out.println("small->>>>"+small);
			iSmallestArray[count6] = small; 
			count6++;
			
			for(Entry<String,Integer> entry : mapTrain1_Train2.entrySet())
			{
				if(entry.getValue()==small)
				{
					System.out.println("Train Numbers->" +entry.getKey() +"\t\t diff->" +entry.getValue());	
					listTrainNumbers.add(entry.getKey());
				}
			}
			String strTrainNumberToRemove = new String();
			for(Entry<String,Integer> entry : mapTrainNumber2.entrySet())
			{
				if(entry.getValue()==small)
				{
					//System.out.println("Train Numbers->" +entry.getKey() +"\t\t diff->" +entry.getValue());	
					//listTrainNumbers.add(entry.getKey());
					
					strTrainNumberToRemove = entry.getKey();
				}
			}
			
			setTrainNumbers.remove(strTrainNumberToRemove);
			System.out.println("-----------------------------------------");
		}
		roundTripAssignmentsOfTrains(map_Available_Train_Departure_Train,listTrainNumbers);
}

//assignments of trains with round trip suggestions

public void roundTripAssignmentsOfTrains(Map<String,String> map_Available_Train_Departure_Train,List<String> listTrainNumbers)
{
	System.out.println("-----------------------------------------");
	System.out.println("--------Train Numbers 1----------------");
	
	for(Entry<String,String> entry : map_Available_Train_Departure_Train.entrySet())
	{
			System.out.println(entry.getKey() +"\t\t ->" +entry.getValue());
			listTrainNumbersFinalFirst.add(entry.getKey());
			listTrainNumbersFinalTo.add(entry.getValue());
	}
	
	System.out.println("--------Train Numbers 2----------------");

	for(String s : listTrainNumbers)
	{
			System.out.println(s);	
			String[] strSplitParts = s.split(Pattern.quote("+"));
			
			for (int j = 0; j< strSplitParts.length-1;j++)
			{
				listTrainNumbersFinalFirst1.add(strSplitParts[j]);
				listTrainNumbersFinalTo1.add(strSplitParts[j+1]);
			}
	}
	RequestContext.getCurrentInstance().execute("PF('roundTripInfoWdgt').show();");
	RequestContext.getCurrentInstance().update("test12");
}







public Integer compareDepAndAvail(Date dtDepa,String strAvail)
{
	Integer iDiffInMinutes = new Integer(0);
	String[] strSplitParts = strAvail.split(Pattern.quote(":"));
	Calendar calDep = Calendar.getInstance();
	
	Integer iAvailHour = new Integer(0);
	Integer iAvailMin = new Integer(0);
	
	Integer iDepHour = new Integer(0);
	Integer iDepMin = new Integer(0);
	
	for (int j = 0; j< strSplitParts.length-1;j++)
	{
		iAvailHour = Integer.parseInt(strSplitParts[j]);
		iAvailMin = Integer.parseInt(strSplitParts[j+1]);
	}
	calDep.setTime(dtDepa);
	iDepHour = calDep.get(Calendar.HOUR_OF_DAY);
	iDepMin = calDep.get(Calendar.MINUTE);
	
	/*System.out.println("depar Hour->" +iDepHour + "\t Dep Minute ->>" + iDepMin);
	System.out.println("iAvailHour Hour->" +iAvailHour + "\t iAvailHour Minute ->>" + iAvailMin);*/
	
	if(iAvailHour==0) iAvailHour = 24;
	if(iDepHour==0) iDepHour = 24;
//	System.out.println("-----------------------------------------");
	if(iAvailHour == iDepHour)
	{
		if(iAvailMin < iDepMin)
		{
			iDiffInMinutes =  iDepMin - iAvailMin;
			///System.out.println("Diff In minutes dep<arr -> " + iDiffInMinutes);
			//iTotalMinutes = iTotalMinutes + iDiffInMinutes;
		}
	}
	else if(iAvailHour < iDepHour)
	{
		/*System.out.println("Hour of Departure -> " + iAvailHour +"\t Minute of Departure-> " +iAvailMin);
		System.out.println("Hour of Departure -> " + iDepHour +"\t Minute of Departure-> " +iDepMin);
	*/
		
		iDiffInMinutes =  (iDepHour*60 + iDepMin) - (iAvailHour * 60 +iAvailMin);
		//iTotalMinutes = iTotalMinutes + iDiffInMinutes;
		//System.out.println("Diff In minutes dep<arr -> " + iDiffInMinutes);
	}
	else					
	{
		/*System.out.println("Hour of Departure -> " + iAvailHour +"\t Minute of Departure-> " +iAvailMin);
		System.out.println("Hour of Departure -> " + iDepHour +"\t Minute of Departure-> " +iDepMin);
		*/
		iDiffInMinutes = (iDepHour*60 + iDepMin)  + 24* 60 - (iAvailHour * 60 +iAvailMin);
	//	iTotalMinutes = iTotalMinutes + iDiffInMinutes;
		//System.out.println("Diff In minutes dep>arr -> " + iDiffInMinutes);
	}
	
	//System.out.println("-----------------------------------------");
	
	return iDiffInMinutes;
}


	
	
	
	
	
	














	
	
	/// all getter and setter are mentioned below  
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


	public List<Onedrivingsection> getDlist() {
		return dlist;
	}

	public void setDlist(List<Onedrivingsection> dlist) {
		this.dlist = dlist;
	}

	public Onedrivingsection[] getOneSelection() {
		return oneSelection;
	}

	public void setOneSelection(Onedrivingsection[] oneSelection) {
		this.oneSelection = oneSelection;
	}


	public Onedrivingsection getUpdateOneDS() {
		return updateOneDS;
	}


	public void setUpdateOneDS(Onedrivingsection updateOneDS) {
		this.updateOneDS = updateOneDS;
	}


	public Onedrivingsection getODS() {
		return ODS;
	}


	public void setODS(Onedrivingsection oDS) {
		ODS = oDS;
	}

	public boolean isDeletebuttonTrue() {
		return deletebuttonTrue;
	}

	public void setDeletebuttonTrue(boolean deletebuttonTrue) {
		this.deletebuttonTrue = deletebuttonTrue;
	}

	public boolean isUpdatebuttonTrue() {
		return updatebuttonTrue;
	}

	public void setUpdatebuttonTrue(boolean updatebuttonTrue) {
		this.updatebuttonTrue = updatebuttonTrue;
	}

	public List<CrewChangeDrivingsection> getCrewChangeDrivingsectionsList() {
		return crewChangeDrivingsectionsList;
	}

	public void setCrewChangeDrivingsectionsList(
			List<CrewChangeDrivingsection> crewChangeDrivingsectionsList) {
		this.crewChangeDrivingsectionsList = crewChangeDrivingsectionsList;
	}
	public List<Long> getSelectedId() {
		return selectedId;
	}
	public void setSelectedId(List<Long> selectedId) {
		this.selectedId = selectedId;
	}

	public List<Long> getPilotIdList() {
		return pilotIdList;
	}
	public void setPilotIdList(List<Long> pilotIdList) {
		this.pilotIdList = pilotIdList;
	}
	public List<Long> getPilotCheckBoxList() {
		return pilotCheckBoxList;
	}
	public void setPilotCheckBoxList(List<Long> pilotCheckBoxList) {
		this.pilotCheckBoxList = pilotCheckBoxList;
	}
	public List<Long> getDrivingSectionCheckBoxList() {
		return drivingSectionCheckBoxList;
	}
	public void setDrivingSectionCheckBoxList(
			List<Long> drivingSectionCheckBoxList) {
		this.drivingSectionCheckBoxList = drivingSectionCheckBoxList;
	}
	public CrewChangeDrivingsection getEditCrewChange() {
		return editCrewChange;
	}
	public void setEditCrewChange(CrewChangeDrivingsection editCrewChange) {
		this.editCrewChange = editCrewChange;
	}
	public long getDifferenceDate() {
		return differenceDate;
	}
	public void setDifferenceDate(long differenceDate) {
		this.differenceDate = differenceDate;
	}
	public long getDiffInSecond() {
		return diffInSecond;
	}
	public void setDiffInSecond(long diffInSecond) {
		this.diffInSecond = diffInSecond;
	}
	public long getDiffInMinute() {
		return diffInMinute;
	}
	public void setDiffInMinute(long diffInMinute) {
		this.diffInMinute = diffInMinute;
	}
	public long getDiffInHour() {
		return diffInHour;
	}
	public void setDiffInHour(long diffInHour) {
		this.diffInHour = diffInHour;
	}
	public long getDiffInDays() {
		return diffInDays;
	}
	public void setDiffInDays(long diffInDays) {
		this.diffInDays = diffInDays;
	}
	public Onedrivingsection getDrivingDutyStore() {
		return drivingDutyStore;
	}
	public void setDrivingDutyStore(Onedrivingsection drivingDutyStore) {
		this.drivingDutyStore = drivingDutyStore;
	}
	public DrivingDutyOfPilotDrivingSection getDrivingDutyOfPilotDrivingSectionsList() {
		return drivingDutyOfPilotDrivingSectionsList;
	}
	public void setDrivingDutyOfPilotDrivingSectionsList(
			DrivingDutyOfPilotDrivingSection drivingDutyOfPilotDrivingSectionsList) {
		this.drivingDutyOfPilotDrivingSectionsList = drivingDutyOfPilotDrivingSectionsList;
	}
	public DrivingDutyOfPilotDrivingSection getDrivingDutyPilotDrivingSection() {
		return drivingDutyPilotDrivingSection;
	}
	public void setDrivingDutyPilotDrivingSection(
			DrivingDutyOfPilotDrivingSection drivingDutyPilotDrivingSection) {
		this.drivingDutyPilotDrivingSection = drivingDutyPilotDrivingSection;
	}
	public String getTrainNo() {
		return trainNo;
	}
	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}
	public List<DrivingDutyOfPilotDrivingSection> getDrivingPlusPilotList() {
		return drivingPlusPilotList;
	}
	public void setDrivingPlusPilotList(List<DrivingDutyOfPilotDrivingSection> drivingPlusPilotList) {
		this.drivingPlusPilotList = drivingPlusPilotList;
	}
	public String getSearchStationCode() {
		return searchStationCode;
	}
	public void setSearchStationCode(String searchStationCode) {
		this.searchStationCode = searchStationCode;
	}
	public List<DrivingDutyOfPilotDrivingSection> getDrivingDutyAfterSearchList() {
		return drivingDutyAfterSearchList;
	}
	public void setDrivingDutyAfterSearchList(
			List<DrivingDutyOfPilotDrivingSection> drivingDutyAfterSearchList) {
		this.drivingDutyAfterSearchList = drivingDutyAfterSearchList;
	}
	public List<DrivingDutyOfPilotDrivingSection> getDrivingDutyFromStationsList() {
		return drivingDutyFromStationsList;
	}
	public void setDrivingDutyFromStationsList(
			List<DrivingDutyOfPilotDrivingSection> drivingDutyFromStationsList) {
		this.drivingDutyFromStationsList = drivingDutyFromStationsList;
	}
	public Map<String, String> getArrivalMap() {
		return arrivalMap;
	}
	public void setArrivalMap(Map<String, String> arrivalMap) {
		this.arrivalMap = arrivalMap;
	}
	public List<Long> getDepartureLong() {
		return departureLong;
	}
	public void setDepartureLong(List<Long> departureLong) {
		this.departureLong = departureLong;
	}
	public Map<String, Long> getDepartMap() {
		return departMap;
	}
	public void setDepartMap(Map<String, Long> departMap) {
		this.departMap = departMap;
	}
	public Map<String, String> getDisplayAllocationMap() {
		return displayAllocationMap;
	}
	public void setDisplayAllocationMap(Map<String, String> displayAllocationMap) {
		this.displayAllocationMap = displayAllocationMap;
	}
	public List<String> getKeyMapList() {
		return keyMapList;
	}
	public void setKeyMapList(List<String> keyMapList) {
		this.keyMapList = keyMapList;
	}
	public Integer getNumberOfCrews() {
		return numberOfCrews;
	}
	public void setNumberOfCrews(Integer numberOfCrews) {
		this.numberOfCrews = numberOfCrews;
	}
	public String getToStation() {
		return toStation;
	}
	public void setToStation(String toStation) {
		this.toStation = toStation;
	}
	public List<DrivingDutyOfPilotDrivingSection> getFromAndToStationList() {
		return fromAndToStationList;
	}
	public void setFromAndToStationList(List<DrivingDutyOfPilotDrivingSection> fromAndToStationList) {
		this.fromAndToStationList = fromAndToStationList;
	}
	public List<DrivingDutyOfPilotDrivingSection> getToAndFromStationList() {
		return toAndFromStationList;
	}
	public void setToAndFromStationList(List<DrivingDutyOfPilotDrivingSection> toAndFromStationList) {
		this.toAndFromStationList = toAndFromStationList;
	}
	public List<CrewChangeDrivingsection> getFilteredCrewChangeList() {
		return filteredCrewChangeList;
	}
	public void setFilteredCrewChangeList(List<CrewChangeDrivingsection> filteredCrewChangeList) {
		this.filteredCrewChangeList = filteredCrewChangeList;
	}
	public Finaltable getOptimizedResultObject() {
		return optimizedResultObject;
	}
	public void setOptimizedResultObject(Finaltable optimizedResultObject) {
		this.optimizedResultObject = optimizedResultObject;
	}
	public List<Finaltable> getFinalTableList() {
		return finalTableList;
	}
	public void setFinalTableList(List<Finaltable> finalTableList) {
		this.finalTableList = finalTableList;
	}
	public List<DrivingDutyOfPilotDrivingSection> getLoadDriDutyList() {
		return loadDriDutyList;
	}
	public void setLoadDriDutyList(List<DrivingDutyOfPilotDrivingSection> loadDriDutyList) {
		this.loadDriDutyList = loadDriDutyList;
	}
	public DrivingDutyOfPilotDrivingSection getUpdateDriDutyObject() {
		return updateDriDutyObject;
	}
	public void setUpdateDriDutyObject(DrivingDutyOfPilotDrivingSection updateDriDutyObject) {
		this.updateDriDutyObject = updateDriDutyObject;
	}
	public List<CrewChangeDrivingsection> getFilteredCrewChangeList2() {
		return filteredCrewChangeList2;
	}
	public void setFilteredCrewChangeList2(List<CrewChangeDrivingsection> filteredCrewChangeList2) {
		this.filteredCrewChangeList2 = filteredCrewChangeList2;
	}
	public List<CrewChangeDrivingsection> getFilteredCrewChangeList3() {
		return filteredCrewChangeList3;
	}
	public void setFilteredCrewChangeList3(List<CrewChangeDrivingsection> filteredCrewChangeList3) {
		this.filteredCrewChangeList3 = filteredCrewChangeList3;
	}
	public List<CrewChangeDrivingsection> getFilteredCrewChangeList4() {
		return filteredCrewChangeList4;
	}
	public void setFilteredCrewChangeList4(List<CrewChangeDrivingsection> filteredCrewChangeList4) {
		this.filteredCrewChangeList4 = filteredCrewChangeList4;
	}
	public DrivingDutyOfPilotDrivingSection getEditDriDutyObject() {
		return editDriDutyObject;
	}
	public void setEditDriDutyObject(DrivingDutyOfPilotDrivingSection editDriDutyObject) {
		this.editDriDutyObject = editDriDutyObject;
	}
	public List<PilotJourney> getPilotjourneylist() {
		return pilotjourneylist;
	}
	public void setPilotjourneylist(List<PilotJourney> pilotjourneylist) {
		this.pilotjourneylist = pilotjourneylist;
	}
	public List<Long> getIds() {
		return ids;
	}
	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
	public List<CrewChangeDrivingsection> getLoadCrewChangeList() {
		return loadCrewChangeList;
	}
	public void setLoadCrewChangeList(List<CrewChangeDrivingsection> loadCrewChangeList) {
		this.loadCrewChangeList = loadCrewChangeList;
	}
	public List<CrewChangeDrivingsection> getFilteredCrewChangeList5() {
		return filteredCrewChangeList5;
	}
	public void setFilteredCrewChangeList5(List<CrewChangeDrivingsection> filteredCrewChangeList5) {
		this.filteredCrewChangeList5 = filteredCrewChangeList5;
	}
	public Long getTotalfilteredHours() {
		return totalfilteredHours;
	}
	public void setTotalfilteredHours(Long totalfilteredHours) {
		this.totalfilteredHours = totalfilteredHours;
	}
	public Long getTotalfilteredDistance() {
		return totalfilteredDistance;
	}
	public void setTotalfilteredDistance(Long totalfilteredDistance) {
		this.totalfilteredDistance = totalfilteredDistance;
	}
	public List<CrewChangeDrivingsection> getSelectCrewChangeList() {
		return selectCrewChangeList;
	}
	public void setSelectCrewChangeList(List<CrewChangeDrivingsection> selectCrewChangeList) {
		this.selectCrewChangeList = selectCrewChangeList;
	}
	public List<CrewChangeDrivingsection> getFilteredCrewChangeList6() {
		return filteredCrewChangeList6;
	}
	public void setFilteredCrewChangeList6(List<CrewChangeDrivingsection> filteredCrewChangeList6) {
		this.filteredCrewChangeList6 = filteredCrewChangeList6;
	}
	public String getStrTotalId() {
		return strTotalId;
	}
	public void setStrTotalId(String strTotalId) {
		this.strTotalId = strTotalId;
	}
	public String[] getStrId() {
		return strId;
	}
	public void setStrId(String[] strId) {
		this.strId = strId;
	}
	public List<CrewChangeDrivingsection> getFilteredCrewChangeList7() {
		return filteredCrewChangeList7;
	}
	public void setFilteredCrewChangeList7(List<CrewChangeDrivingsection> filteredCrewChangeList7) {
		this.filteredCrewChangeList7 = filteredCrewChangeList7;
	}
	public List<DrivingDutyOfPilotDrivingSection> getListDrivingDutyView() {
		return listDrivingDutyView;
	}
	public void setListDrivingDutyView(List<DrivingDutyOfPilotDrivingSection> listDrivingDutyView) {
		this.listDrivingDutyView = listDrivingDutyView;
	}
	public List<CrewChangeDrivingsection> getFilteredPilotTableList() {
		return filteredPilotTableList;
	}
	public void setFilteredPilotTableList(List<CrewChangeDrivingsection> filteredPilotTableList) {
		this.filteredPilotTableList = filteredPilotTableList;
	}
	public List<PilotJourney> getPilotTableObjectList() {
		return pilotTableObjectList;
	}
	public void setPilotTableObjectList(List<PilotJourney> pilotTableObjectList) {
		this.pilotTableObjectList = pilotTableObjectList;
	}
	public List<CrewChangeDrivingsection> getCrewChangeSelectedList() {
		return crewChangeSelectedList;
	}
	public void setCrewChangeSelectedList(List<CrewChangeDrivingsection> crewChangeSelectedList) {
		this.crewChangeSelectedList = crewChangeSelectedList;
	}
	public Integer getTotalFilteredValues() {
		return totalFilteredValues;
	}
	public void setTotalFilteredValues(Integer totalFilteredValues) {
		this.totalFilteredValues = totalFilteredValues;
	}
	public Long getSmallest() {
		return smallest;
	}
	public void setSmallest(Long smallest) {
		this.smallest = smallest;
	}
	public Double getAvgDrivingSection() {
		return avgDrivingSection;
	}
	public void setAvgDrivingSection(Double avgDrivingSection) {
		this.avgDrivingSection = avgDrivingSection;
	}
	public boolean isbToggleDisplay() {
		return bToggleDisplay;
	}
	public void setbToggleDisplay(boolean bToggleDisplay) {
		this.bToggleDisplay = bToggleDisplay;
	}
	public List<CrewChangeDrivingsection> getCrewChangeDrivingsectionList() {
		return crewChangeDrivingsectionList;
	}
	public void setCrewChangeDrivingsectionList(
			List<CrewChangeDrivingsection> crewChangeDrivingsectionList) {
		this.crewChangeDrivingsectionList = crewChangeDrivingsectionList;
	}
	public List<PilotJourney> getPilotJourneylist() {
		return PilotJourneylist;
	}
	public void setPilotJourneylist(List<PilotJourney> pilotJourneylist) {
		PilotJourneylist = pilotJourneylist;
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
	public String[] getDsectionarray() {
		return dsectionarray;
	}
	public void setDsectionarray(String[] dsectionarray) {
		this.dsectionarray = dsectionarray;
	}
	public Map<String, Long> getMapSeqNoPlusId() {
		return mapSeqNoPlusId;
	}
	public void setMapSeqNoPlusId(Map<String, Long> mapSeqNoPlusId) {
		this.mapSeqNoPlusId = mapSeqNoPlusId;
	}
	public Long getRelativeId() {
		return relativeId;
	}
	public void setRelativeId(Long relativeId) {
		this.relativeId = relativeId;
	}
	public Map<String, Long> getMapPilotId() {
		return mapPilotId;
	}
	public void setMapPilotId(Map<String, Long> mapPilotId) {
		this.mapPilotId = mapPilotId;
	}
	public String getpTravelingSource() {
		return pTravelingSource;
	}
	public void setpTravelingSource(String pTravelingSource) {
		this.pTravelingSource = pTravelingSource;
	}
	public String getpFrom_Station() {
		return pFrom_Station;
	}
	public void setpFrom_Station(String pFrom_Station) {
		this.pFrom_Station = pFrom_Station;
	}
	public String getpTo_Station() {
		return pTo_Station;
	}
	public void setpTo_Station(String pTo_Station) {
		this.pTo_Station = pTo_Station;
	}
	public String getpDeparture() {
		return pDeparture;
	}
	public void setpDeparture(String pDeparture) {
		this.pDeparture = pDeparture;
	}
	public String getpArrival() {
		return pArrival;
	}
	public void setpArrival(String pArrival) {
		this.pArrival = pArrival;
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<CrewChangeDrivingsection> getFilteredDrivingSectionTableList() {
		return filteredDrivingSectionTableList;
	}
	public void setFilteredDrivingSectionTableList(
			List<CrewChangeDrivingsection> filteredDrivingSectionTableList) {
		this.filteredDrivingSectionTableList = filteredDrivingSectionTableList;
	}
	public List<PilotJourney> getFilteredpilotTableList() {
		return filteredpilotTableList;
	}
	public void setFilteredpilotTableList(List<PilotJourney> filteredpilotTableList) {
		this.filteredpilotTableList = filteredpilotTableList;
	}
	public Long getKM() {
		return KM;
	}
	public void setKM(Long kM) {
		KM = kM;
	}
	public String getStrCombineHourAndMinute() {
		return strCombineHourAndMinute;
	}
	public void setStrCombineHourAndMinute(String strCombineHourAndMinute) {
		this.strCombineHourAndMinute = strCombineHourAndMinute;
	}
	public String getTotalFilteredHourAndMinutes() {
		return totalFilteredHourAndMinutes;
	}
	public void setTotalFilteredHourAndMinutes(
			String totalFilteredHourAndMinutes) {
		this.totalFilteredHourAndMinutes = totalFilteredHourAndMinutes;
	}
	public DrivingDutyOfPilotDrivingSection getSelectedDrivingSection() {
		return selectedDrivingSection;
	}
	public void setSelectedDrivingSection(DrivingDutyOfPilotDrivingSection selectedDrivingSection) {
		this.selectedDrivingSection = selectedDrivingSection;
	}
	

	public DrivingDutyOfPilotDrivingSection getSelectedDuty() {
		return selectedDuty;
	}

	public void setSelectedDuty(DrivingDutyOfPilotDrivingSection selectedDuty) {
		this.selectedDuty = selectedDuty;
	}

	public String getSelectedFrom() {
		return selectedFrom;
	}

	public void setSelectedFrom(String selectedFrom) {
		this.selectedFrom = selectedFrom;
	}

	public Long getlTransId() {
		return lTransId;
	}

	public void setlTransId(Long lTransId) {
		this.lTransId = lTransId;
	}

	public TransactionIdGenerator getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(TransactionIdGenerator transactionId) {
		this.transactionId = transactionId;
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

	public String getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
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

	public String getRoundTripStatus() {
		return roundTripStatus;
	}

	public void setRoundTripStatus(String roundTripStatus) {
		this.roundTripStatus = roundTripStatus;
	}

	public String getUpdateCrewbase() {
		return updateCrewbase;
	}

	public void setUpdateCrewbase(String updateCrewbase) {
		this.updateCrewbase = updateCrewbase;
	}

	public String getUpdateCrewtype() {
		return updateCrewtype;
	}

	public void setUpdateCrewtype(String updateCrewtype) {
		this.updateCrewtype = updateCrewtype;
	}

	public CrewChangeDrivingsection getCrewchangedrivingsection() {
		return crewchangedrivingsection;
	}

	public void setCrewchangedrivingsection(
			CrewChangeDrivingsection crewchangedrivingsection) {
		this.crewchangedrivingsection = crewchangedrivingsection;
	}





	public List<CrewChangeDrivingsection> getListCrewChange() {
		return listCrewChange;
	}





	public void setListCrewChange(List<CrewChangeDrivingsection> listCrewChange) {
		this.listCrewChange = listCrewChange;
	}





	public List<PilotJourney> getListPilotJourney() {
		return listPilotJourney;
	}





	public void setListPilotJourney(List<PilotJourney> listPilotJourney) {
		this.listPilotJourney = listPilotJourney;
	}





	public PilotJourney getPilotjourneyupdate() {
		return pilotjourneyupdate;
	}





	public void setPilotjourneyupdate(PilotJourney pilotjourneyupdate) {
		this.pilotjourneyupdate = pilotjourneyupdate;
	}





	public List<SelectItem> getEditFromSelectone() {
		return editFromSelectone;
	}





	public void setEditFromSelectone(List<SelectItem> editFromSelectone) {
		this.editFromSelectone = editFromSelectone;
	}





	public List<SelectItem> getEditToSelectone() {
		return editToSelectone;
	}





	public void setEditToSelectone(List<SelectItem> editToSelectone) {
		this.editToSelectone = editToSelectone;
	}





	public DrivingDutyOfPilotDrivingSection getDdpsRest() {
		return ddpsRest;
	}





	public void setDdpsRest(DrivingDutyOfPilotDrivingSection ddpsRest) {
		this.ddpsRest = ddpsRest;
	}





	public boolean isbPilottable() {
		return bPilottable;
	}





	public String getSearchBase() {
		return searchBase;
	}





	public void setSearchBase(String searchBase) {
		this.searchBase = searchBase;
	}





	public String getSearchType() {
		return searchType;
	}





	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}





	public void setbPilottable(boolean bPilottable) {
		this.bPilottable = bPilottable;
	}





	public int getiNumberOfSourceCrew() {
		return iNumberOfSourceCrew;
	}





	public void setiNumberOfSourceCrew(int iNumberOfSourceCrew) {
		this.iNumberOfSourceCrew = iNumberOfSourceCrew;
	}





	public int getiNumberOfDestinationCrew() {
		return iNumberOfDestinationCrew;
	}





	public void setiNumberOfDestinationCrew(int iNumberOfDestinationCrew) {
		this.iNumberOfDestinationCrew = iNumberOfDestinationCrew;
	}





	public int getiSizeOfDestinationCrew() {
		return iSizeOfDestinationCrew;
	}





	public void setiSizeOfDestinationCrew(int iSizeOfDestinationCrew) {
		this.iSizeOfDestinationCrew = iSizeOfDestinationCrew;
	}





	public Map<String, String> getMap_Available_Train_Departure_Train() {
		return map_Available_Train_Departure_Train;
	}





	public void setMap_Available_Train_Departure_Train(
			Map<String, String> map_Available_Train_Departure_Train) {
		this.map_Available_Train_Departure_Train = map_Available_Train_Departure_Train;
	}





	public int getiSizeOfSourceCrew() {
		return iSizeOfSourceCrew;
	}





	public void setiSizeOfSourceCrew(int iSizeOfSourceCrew) {
		this.iSizeOfSourceCrew = iSizeOfSourceCrew;
	}





	public List<String> getListTrainNumbersFinalFirst() {
		return listTrainNumbersFinalFirst;
	}





	public void setListTrainNumbersFinalFirst(
			List<String> listTrainNumbersFinalFirst) {
		this.listTrainNumbersFinalFirst = listTrainNumbersFinalFirst;
	}





	public List<String> getListTrainNumbersFinalTo() {
		return listTrainNumbersFinalTo;
	}





	public void setListTrainNumbersFinalTo(List<String> listTrainNumbersFinalTo) {
		this.listTrainNumbersFinalTo = listTrainNumbersFinalTo;
	}





	public List<String> getListTrainNumbersFinalFirst1() {
		return listTrainNumbersFinalFirst1;
	}





	public void setListTrainNumbersFinalFirst1(
			List<String> listTrainNumbersFinalFirst1) {
		this.listTrainNumbersFinalFirst1 = listTrainNumbersFinalFirst1;
	}





	public List<String> getListTrainNumbersFinalTo1() {
		return listTrainNumbersFinalTo1;
	}





	public void setListTrainNumbersFinalTo1(List<String> listTrainNumbersFinalTo1) {
		this.listTrainNumbersFinalTo1 = listTrainNumbersFinalTo1;
	}





	public List<RoundtripSuggestions1> getLstRRSuggestion1() {
		return lstRRSuggestion1;
	}





	public void setLstRRSuggestion1(List<RoundtripSuggestions1> lstRRSuggestion1) {
		this.lstRRSuggestion1 = lstRRSuggestion1;
	}





	public List<RoundtripSuggestion2> getLstRRSuggestion2() {
		return lstRRSuggestion2;
	}





	public Long getlRoundTripSuggestionId() {
		return lRoundTripSuggestionId;
	}





	public void setlRoundTripSuggestionId(Long lRoundTripSuggestionId) {
		this.lRoundTripSuggestionId = lRoundTripSuggestionId;
	}





	public void setLstRRSuggestion2(List<RoundtripSuggestion2> lstRRSuggestion2) {
		this.lstRRSuggestion2 = lstRRSuggestion2;
	}





	public Map<String, String> getMapDeletedRRSuggestion1() {
		return mapDeletedRRSuggestion1;
	}





	public void setMapDeletedRRSuggestion1(
			Map<String, String> mapDeletedRRSuggestion1) {
		this.mapDeletedRRSuggestion1 = mapDeletedRRSuggestion1;
	}





	public Map<String, String> getMapDeletedRRSuggestion2() {
		return mapDeletedRRSuggestion2;
	}





	public void setMapDeletedRRSuggestion2(
			Map<String, String> mapDeletedRRSuggestion2) {
		this.mapDeletedRRSuggestion2 = mapDeletedRRSuggestion2;
	}





	public List<DrivingDutyOfPilotDrivingSection> getSelectionList() {
		return selectionList;
	}





	public void setSelectionList(List<DrivingDutyOfPilotDrivingSection> selectionList) {
		this.selectionList = selectionList;
	}

	


	


}
