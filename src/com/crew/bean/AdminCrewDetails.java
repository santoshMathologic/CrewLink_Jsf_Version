package com.crew.bean;


import java.io.IOException;
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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.crew.exception.CrewException;
import com.crew.model.CrewChangeDrivingsection;
import com.crew.model.CrewHeadquarterMaster;
import com.crew.model.CrewOutstation;
import com.crew.model.Customer;
import com.crew.model.Test;
import com.crew.model.Test2;
import com.crew.model.TimeTableMaster;
import com.crew.service.CrewService;
import com.crew.util.CrewUtilities;
import com.crew.util.HeadQuarterCodeComprator;
import com.crew.util.StationCodeComprator;
import com.crew.util.TimeTableStationCodeComprator;






@ManagedBean(name="crewdetailbean",eager=true)
@ViewScoped
@SuppressWarnings("unchecked")

public class AdminCrewDetails implements Serializable {

	private  static final long serialVersionUID = 1L;

	
	
	
	@ManagedProperty(name = "schoolService", value = "#{schoolService}")
	private CrewService schoolService;
	private HttpSession httpSession = (HttpSession) FacesContext
			.getCurrentInstance().getExternalContext().getSession(false);
	private static final Logger CREW_LOGGER = Logger.getLogger(AdminCrewDetails.class);
	
	private ApplicationContext context;
	private List<Test> listTest;
	private Long lRequiredHour;
	private Integer iTotalMinutes = new Integer(0);
	private String toTrain;
	private String fromTrain;
	private int passedParameter;
	private List<TimeTableMaster>mastertablelist;
	private List<SelectItem> toStationList;
	private List<SelectItem> toStationList2;
	private String selectedToStation;
	private String crewBaseQuery;
	private String selectedcrewBaseName;
	private List<CrewHeadquarterMaster> crewBaseList;
	private Map<String,String> crewBaseMap = new HashMap<String, String>();
	private String selectedCrewBaseType;

	private List<TimeTableMaster> listTimeTableMasterForward;
	private List<TimeTableMaster> listTimeTableMasterForward2;
	private Test test;
	private Test2 test2;
	  
	private String trainNo;
	private long toDistance = 0;
	private long fromDistance = 0;
	private long totalDistance = 0;
	private Date fromDate;
	private Date toDate;
	private Long differenceDate;
	private Long diffInSecond;
	private Long diffInMinute;
	private Long diffInHour;
	private Long diffInDays;
	private Date departureTime;
	private Date arrivalTime;
	private Date departTime2 = null;
	private List<Test2> listTest2;
	
	private CrewChangeDrivingsection crew;
	private List<CrewChangeDrivingsection> crewList;
	private CrewChangeDrivingsection editCrewChange;
	private List<TimeTableMaster>timeTableMasterList; 
	private List<TimeTableMaster> tableList; 
	private List<Object[]> objects;
	
	private List<TimeTableMaster> timeTableIdTrainList;
	private List<TimeTableMaster> timeTableToList;
	private List<CrewChangeDrivingsection> crewChangeDrivingsectionsList;
	private List<CrewChangeDrivingsection>selectCrewChangeList;
	private Long customerloginId;
	private StringBuilder dutyhours;
	
	/// added on 16-3-2015
	private  TimeTableMaster timeTableMaster;
	
	// santosh 6-july-2015
	
	
	
	@PostConstruct
	private void initilization() {

		if (!(httpSession.getAttribute("COMMONLOGINID") == null)) {
			customerloginId = (Long) httpSession.getAttribute("COMMONLOGINID");
			
			
			try {
				crew  = (CrewChangeDrivingsection) schoolService.loadUniqueObject("from CrewChangeDrivingsection as CCD where CCD.id="+customerloginId);
			} catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
			}
			
			createList();
			loadStation();
			loadHeadQuarterList();
			loadallCrewChange();
			

		} else {
			try {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(CrewUtilities.getRedirectContext() + "/login.xhtml");
			} catch (IOException e) {
				CREW_LOGGER.debug(e.getMessage());
			}

		}

	}

	
	
	private void createList()
	{
	
		context = new ClassPathXmlApplicationContext("Beans.xml");

		listTimeTableMasterForward = (List<TimeTableMaster>) context.getBean("list");
		listTimeTableMasterForward2 = (List<TimeTableMaster>) context.getBean("list");
		listTest = (List<Test>) context.getBean("list");
		listTest2 = (List<Test2>) context.getBean("list");
		mastertablelist = (List<TimeTableMaster>) context.getBean("list");
		crewBaseList =  (List<CrewHeadquarterMaster>) context.getBean("list");
		crew = (CrewChangeDrivingsection) context.getBean("crewChange");
		crewList = (List<CrewChangeDrivingsection>) context.getBean("list");
		timeTableIdTrainList = (List<TimeTableMaster>) context.getBean("list");
		timeTableToList = (List<TimeTableMaster>) context.getBean("list");
		setToStationList(new ArrayList<SelectItem>());
		toStationList2 = new ArrayList<SelectItem>();
		selectCrewChangeList =  (List<CrewChangeDrivingsection>) context.getBean("list");
		setCrewChangeDrivingsectionsList((List<CrewChangeDrivingsection>) context
				.getBean("list"));
		test = (Test) context.getBean("test");
		test2 = (Test2) context.getBean("test2");
		timeTableMaster = (TimeTableMaster) context.getBean("timeTableMaster");
		
		
	}
	
	
	public void sizeSelectCrewChangeList()
	{
		//System.out.println("size of selectCrewChangeList is ->" +selectCrewChangeList.size());
	}
	
	private void loadallCrewChange()
	{
		
		try {
			crewList = (List<CrewChangeDrivingsection>) this.schoolService
					.loadList("from CrewChangeDrivingsection as cc where cc.trainNo like'"+trainNo+"' and cc.customer.id="+customerloginId);
			//customerloginId
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public String saveToDb()
	{
		


		totalDistance = toDistance-fromDistance;   /// calculate total No of Distance 
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		
		calendar1.setTime(departureTime);
		calendar2.setTime(arrivalTime);
		
		
		System.out.println("dep  = "+calendar1.getTime());
		System.out.println("Arri = "+calendar2.getTime());
		
		System.out.println("Total Distance = ="+totalDistance);
		
		
		long miliSecondForDate1 = calendar1.getTimeInMillis();
		long miliSecondForDate2 = calendar2.getTimeInMillis();
		
		System.out.println("milli1 = "+miliSecondForDate1);
		System.out.println("milli2 = "+miliSecondForDate2);
		
		System.out.println("Before Absolute Differ Milli = "+(miliSecondForDate1-miliSecondForDate2));
		 
		long diffInMilis = java.lang.Math.abs(miliSecondForDate1 - miliSecondForDate2);
		
		System.out.println("After Absolute Differ Milli = "+diffInMilis);
		
		/* ------First Approch ------------------------*/
		diffInSecond = diffInMilis / 1000;
		diffInMinute = diffInMilis / (60 * 1000);
		diffInHour = diffInMilis / (60 * 60 * 1000);
		diffInDays = diffInMilis / (24 * 60 * 60 * 1000);
		
		
		System.out.println("--------------------------First---------------");
		System.out.println("Second  = = "+diffInSecond);
		System.out.println("Minutes  = = "+diffInMinute);
		System.out.println("Hour  = = "+diffInHour);
		System.out.println("Day = = "+diffInDays);
		
		Long rem = (diffInMinute % 60);
		System.out.println("minutes remainder = =" +rem);
		
		System.out.println("--------------------------End First---------------");
		
		
	
		
		String hours = diffInHour.toString();
		String minutes = rem.toString();
		
		String hoursAndMinutes = hours.concat(":"+minutes);
		/* ------End First Approch ------------------------*/
		
		
		/*---------------Second Approches Santosh ------------*/
		
		System.out.println("-----------Inside Second Approches -----------------------");
		
		System.out.println("Arrival Time = = "+arrivalTime);
		System.out.println("Departure Time = ="+departureTime);
		
		
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2= Calendar.getInstance();
		
		cal1.setTime(arrivalTime);
		cal2.setTime(departureTime);
		
		long arrivalmilliseconds   = cal1.getTimeInMillis();
		long departuremilliseconds = cal2.getTimeInMillis();
		
		System.out.println("arrival millis = "+arrivalmilliseconds);
		System.out.println("depart millis = "+departuremilliseconds);
		long timedifferencemilliseconds;
		
		if(arrivalmilliseconds>=departuremilliseconds)
		{
			timedifferencemilliseconds = arrivalmilliseconds - departuremilliseconds;
			Long timeDifSeconds  = timedifferencemilliseconds / 1000;
	        Long timeDifMinutes  = timedifferencemilliseconds / (60 * 1000);
	        Long timeDifHours    = timedifferencemilliseconds / (60 * 60 * 1000);
	        Long timeDifDays     = timedifferencemilliseconds / (24 * 60 * 60 * 1000);
	        
	        System.out.println(timeDifSeconds + " Seconds");
	        System.out.println(timeDifMinutes + " Minutes");
	        System.out.println(timeDifHours + " Hours");
	        System.out.println(timeDifDays + " Days");
	        
	        long remainMinutes  = timeDifMinutes%60;  
			
	        String totalhours =timeDifHours.toString();
	        
	        dutyhours=new StringBuilder(totalhours);  
	        dutyhours.append(".").append(remainMinutes).toString();
	        
	        System.out.print("New Times "+dutyhours);
	        
	        
	        System.out.println("Time Difference Milliseconds inside If  = ="+timedifferencemilliseconds);
			
		}else
		{
			timedifferencemilliseconds = departuremilliseconds - arrivalmilliseconds ;
			
			Long timeDifSeconds  = timedifferencemilliseconds / 1000;
	        Long timeDifMinutes  = timedifferencemilliseconds / (60 * 1000);
	        Long timeDifHours    = timedifferencemilliseconds / (60 * 60 * 1000);
	        Long timeDifDays     = timedifferencemilliseconds / (24 * 60 * 60 * 1000);
			
	        long remainMinutes  = timeDifMinutes%60;  
			String totalhours =timeDifHours.toString();
			dutyhours=new StringBuilder(totalhours);  
			dutyhours.append(".").append(remainMinutes).toString();
	        
	        System.out.print("New Times in Else Part "+dutyhours);
	        
	        
	        System.out.println(timeDifSeconds + " Seconds");
	        System.out.println(timeDifMinutes + " Minutes");
	        System.out.println(timeDifHours + " Hours");
	        System.out.println(timeDifDays + " Days");
		
		
	        
	        System.out.println("Time Difference Milliseconds inside else part  = ="+timedifferencemilliseconds);
		}
			
		
		System.out.print("-----------Inside End  Second Approches---------------------- ");
		
		
		/*------------End of Second Approches --------------*/
		
		
		
		if(!isDrivingSectionExists())
		{
				crew.setArrival(arrivalTime);
				crew.setCrewBase(crewBaseQuery);
				crew.setCrewType(selectedCrewBaseType);
				crew.setDeparture(departureTime);
				crew.setFromStation(fromTrain);
				crew.setKilometer(totalDistance);
				crew.setToStation(selectedToStation);
				//crew.setHour(hoursAndMinutes);
				crew.setHour(dutyhours.toString());
				
				crew.setTrainNo(toTrain);
				try {
					crew.setCustomer((Customer)schoolService.loadUniqueObject("from Customer as cst where cst.id="+customerloginId));
				} catch (CrewException e1) {
			
			e1.printStackTrace();
		}
				
			 try {
			 	schoolService.saveInDb(crew);
				CrewUtilities.displayMessage("Info","Driving Section has saved");
				loadallCrewChange();
			} catch (Exception e) {
				CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
			}
			
			 fromTrain = selectedToStation;
			 updateToFrom();
			 fromDistance = toDistance;
			 departureTime = departTime2;
		}
		else
		{
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Already Exist In DB! You Cannot Create Duplicate", null));
		}
			 
			 return null;
		
	}
	
	public boolean isDrivingSectionExists()
	{
		boolean IsExists = Boolean.FALSE;
		try {
			
			List<CrewChangeDrivingsection> crewChangeList = 
					(List<CrewChangeDrivingsection>) schoolService.loadList("from CrewChangeDrivingsection as ccd where ccd.customer.id="+customerloginId);
			for(CrewChangeDrivingsection c : crewChangeList)
			{
				if(fromTrain.equalsIgnoreCase(c.getFromStation()) && selectedToStation.equalsIgnoreCase(c.getToStation()))
				{
					if(toTrain.equalsIgnoreCase(c.getTrainNo()))
					   IsExists = Boolean.TRUE;
				}
			}
		} catch (CrewException e1) {
			e1.printStackTrace();
			CREW_LOGGER.debug(e1.getMessage());
		}
	
		
		return IsExists;
	}
	
	
	public void editCrew(CrewChangeDrivingsection changeDrivingsection)
	{
		
		
		try {
			editCrewChange = (CrewChangeDrivingsection) schoolService.loadUniqueObject("from CrewChangeDrivingsection as CCD where CCD.id ="+changeDrivingsection.getId());
			
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('creweditDialog').show();");
			 RequestContext.getCurrentInstance().update("crewDlgid");
			
			
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
		  
	}
	
	public void editSave()
	{
		try {
			schoolService.updateObjectInDb(editCrewChange);
			 RequestContext.getCurrentInstance().update("crewDlgid");
			  loadallCrewChange();
			 RequestContext.getCurrentInstance().update("viewform");
		} catch (CrewException e) {
			
			e.printStackTrace();
		}
	}
	
	//crewbean
	public void delete(CrewChangeDrivingsection crewChangeDelete)
		{	schoolService.deleteById(crewChangeDelete.getId(),"CrewChangeDrivingsection");
			loadallCrewChange();
			RequestContext.getCurrentInstance().update("viewform");
		}
	
private void loadHeadQuarterList()
	{
	
	
	try {
		crewBaseList = (List<CrewHeadquarterMaster>) schoolService.loadList("from CrewHeadquarterMaster");
	} catch (CrewException e) {
		CREW_LOGGER.debug(e.getMessage());
		e.printStackTrace();
	}
		
	}
	public List<String> completeText(String query) {
        List<String> crewBase = new ArrayList<String>();
        for(CrewHeadquarterMaster p: crewBaseList)
        {	  if(p.getCrewBase().toLowerCase().startsWith(query.toLowerCase()))
        	  {  crewBase.add(p.getCrewBase());    
        	  }
        		   
        }
        return  crewBase;
       
    }
	
	public void onItemSelect(SelectEvent event) {
		selectedcrewBaseName = event.getObject().toString().trim();
		loadCrewType(selectedcrewBaseName);
	    }
	
	private void loadTrainTableMaster_CrewChange(String trainid)
	{
			try {
				tableList  = (List<TimeTableMaster>) schoolService.loadList("from TimeTableMaster as TTM where TTM.trainNo like'"+trainid+"'");
				} catch (Exception e) {
					e.printStackTrace();
					CREW_LOGGER.debug(e.getMessage());
				}                          
				
				
	}
	
	public String loadStation() {
		mastertablelist.clear();
		int nSize = 0;
		
		if(selectedToStation!=""){
		
		try {
			mastertablelist = (List<TimeTableMaster>) schoolService
					.loadList("from TimeTableMaster as TTM where TTM.trainNo="
							+ mapString("id"));
			loadTrainTableMaster_CrewChange(mapString("id"));
			loadheadQuarter_and_Os(mapString("id"));
		//	mastertablelist.forEach((TimeTableMaster) -> System.out.print(TimeTableMaster.getStationCode() + ""+TimeTableMaster.getCrewBase()));
			
			
			toTrain = mapString("id");
			
			nSize = mastertablelist.size();
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
		

			if (!mastertablelist.isEmpty()) 
			{	
				toTrain = mastertablelist.get(0).getTrainNo();
				fromTrain = mastertablelist.get(0).getStationCode();
				trainNo = mastertablelist.get(0).getTrainNo();
				departureTime = mastertablelist.get(0).getDeparture();
						
				fromDistance = mastertablelist.get(0).getDistance();
				fromDate = mastertablelist.get(0).getDeparture();
	
				List<TimeTableMaster> list = mastertablelist.subList(1, nSize);
	
				for (TimeTableMaster l : list)
					toStationList.add(new SelectItem(l.getStationCode()));
			}
		}
		else
		{
			
		}
		return null;

	}
	
	public void selectedStation()
	{
		System.out.println("From Station -  "+selectedToStation);
		System.out.println("Train No - "+trainNo);
		
		
		try {
				timeTableIdTrainList = (List<TimeTableMaster>)schoolService.loadList("from TimeTableMaster as t where t.stationCode like '"
				+ selectedToStation+ "' AND t.trainNo like'"
				+ trainNo+ "'");
			
			
			for(TimeTableMaster t : timeTableIdTrainList)
			{
				//System.out.println("id-----------" +t.getId());
				toDistance = t.getDistance();
				
				//System.out.println("Total Distance = =  "+toDistance);
				toDate = t.getArrival();
				
				//System.out.println("Arrival Time = =  "+toDate);
				
				arrivalTime = t.getArrival();
				//System.out.println("Arrival Time = =  "+arrivalTime);
				departTime2 = t.getDeparture();
				//System.out.println("Departure Time = =  "+departTime2);
			}
		  
			
			long id1 = 0;
			long id2 = 0;
			
			for(TimeTableMaster t : timeTableIdTrainList)
			{
				id1=t.getId();
			}
			int size1 = mastertablelist.size();
			id2 = mastertablelist.get(size1-1).getId();
			timeTableToList= (List<TimeTableMaster>) schoolService.betweenQuery(id1+1, id2);
			
		
		} catch (CrewException e) {
			e.printStackTrace();
			CREW_LOGGER.debug(e.getMessage());
		}
	}

	
	
	
	
	private void loadCrewType(String selectedCrewBase) {
		try {
			List<CrewHeadquarterMaster> list = (List<CrewHeadquarterMaster>) schoolService
					.loadList("from CrewHeadquarterMaster as CHM where CHM.crewBase like '"
							+ selectedCrewBase + "'");

			for (CrewHeadquarterMaster CHM : list) {
				if ((CHM.getDmeNo()    != null  && CHM.getAcmeNo() 	 != null
					&& CHM.getAcpNo()  != null  && CHM.getDpNo() 	 != null
					&& CHM.getEmuNo()  != null && CHM.getDmuNo()     != null 
					&& CHM.getMemuNo() != null && CHM.getAcndNo()    != null  
					&& CHM.getDndNo()  != null  && CHM.getOthers1No() != null && CHM.getOthers2No() != null))
				   {

							crewBaseMap.put("DME", "DME");
							crewBaseMap.put("ACME", "ACME");
							crewBaseMap.put("ACP", "ACP");
							crewBaseMap.put("DP", "DP");
							crewBaseMap.put("EMU", "EMU");
							crewBaseMap.put("DMU", "DMU");
							crewBaseMap.put("MEMU", "MENU");
							crewBaseMap.put("ACND", "ACND");
							crewBaseMap.put("DND", "DND");
							crewBaseMap.put("Others1", "Others1");
							crewBaseMap.put("Others2", "Others2");
							

				} else if((CHM.getDmeNo()  == null  && CHM.getAcmeNo() 	 != null
						&& CHM.getAcpNo()  != null  && CHM.getDpNo() 	 != null
						&& CHM.getEmuNo()  != null && CHM.getDmuNo()     != null 
						&& CHM.getMemuNo() != null && CHM.getAcndNo()    != null  
						&& CHM.getDndNo()  != null  && CHM.getOthers1No() != null && CHM.getOthers2No() != null)) {

							
							crewBaseMap.put("ACME", "ACME");
							crewBaseMap.put("ACP", "ACP");
							crewBaseMap.put("DP", "DP");
							crewBaseMap.put("EMU", "EMU");
							crewBaseMap.put("DMU", "DMU");
							crewBaseMap.put("MEMU", "MENU");
							crewBaseMap.put("ACND", "ACND");
							crewBaseMap.put("DND", "DND");
							crewBaseMap.put("Others1", "Others1");
							crewBaseMap.put("Others2", "Others2");

				}

				else if((CHM.getDmeNo()    != null  && CHM.getAcmeNo() 	 == null
						&& CHM.getAcpNo()  != null  && CHM.getDpNo() 	 != null
						&& CHM.getEmuNo()  != null && CHM.getDmuNo()     != null 
						&& CHM.getMemuNo() != null && CHM.getAcndNo()    != null  
						&& CHM.getDndNo()  != null  && CHM.getOthers1No() != null && CHM.getOthers2No() != null)){
				
							crewBaseMap.put("DME", "DME");
							crewBaseMap.put("ACP", "ACP");
							crewBaseMap.put("DP", "DP");
							crewBaseMap.put("EMU", "EMU");
							crewBaseMap.put("DMU", "DMU");
							crewBaseMap.put("MEMU", "MENU");
							crewBaseMap.put("ACND", "ACND");
							crewBaseMap.put("DND", "DND");
							crewBaseMap.put("Others1", "Others1");
							crewBaseMap.put("Others2", "Others2");
		

				}
				
				else if((CHM.getDmeNo()  != null  && CHM.getAcmeNo() 	 != null
						&& CHM.getAcpNo()  == null  && CHM.getDpNo() 	 != null
						&& CHM.getEmuNo()  != null && CHM.getDmuNo()     != null 
						&& CHM.getMemuNo() != null && CHM.getAcndNo()    != null  
						&& CHM.getDndNo()  != null  && CHM.getOthers1No() != null && CHM.getOthers2No() != null)) {

							crewBaseMap.put("DME", "DME");
							crewBaseMap.put("ACME", "ACME");
							crewBaseMap.put("DP", "DP");
							crewBaseMap.put("EMU", "EMU");
							crewBaseMap.put("DMU", "DMU");
							crewBaseMap.put("MEMU", "MENU");
							crewBaseMap.put("ACND", "ACND");
							crewBaseMap.put("DND", "DND");
							crewBaseMap.put("Others1", "Others1");
							crewBaseMap.put("Others2", "Others2");

				}
				
				else if ((CHM.getDmeNo()   != null  && CHM.getAcmeNo() 	 != null
						&& CHM.getAcpNo()  != null  && CHM.getDpNo() 	 == null
						&& CHM.getEmuNo()  != null && CHM.getDmuNo()     != null 
						&& CHM.getMemuNo() != null && CHM.getAcndNo()    != null  
						&& CHM.getDndNo()  != null  && CHM.getOthers1No() != null && CHM.getOthers2No() != null)) {

							crewBaseMap.put("DME", "DME");
							crewBaseMap.put("ACME", "ACME");
							crewBaseMap.put("ACP", "ACP");
							crewBaseMap.put("EMU", "EMU");
							crewBaseMap.put("DMU", "DMU");
							crewBaseMap.put("MEMU", "MENU");
							crewBaseMap.put("ACND", "ACND");
							crewBaseMap.put("DND", "DND");
							crewBaseMap.put("Others1", "Others1");
							crewBaseMap.put("Others2", "Others2");

				}
				
				
				else if ((CHM.getDmeNo()   != null  && CHM.getAcmeNo() 	 != null
						&& CHM.getAcpNo()  != null  && CHM.getDpNo() 	 != null
						&& CHM.getEmuNo()  == null && CHM.getDmuNo()     != null 
						&& CHM.getMemuNo() != null && CHM.getAcndNo()    != null  
						&& CHM.getDndNo()  != null  && CHM.getOthers1No() != null && CHM.getOthers2No() != null)) {

								crewBaseMap.put("DME", "DEM");
								crewBaseMap.put("ACME", "ACME");
								crewBaseMap.put("ACP", "ACP");
								crewBaseMap.put("DP", "DP");
								crewBaseMap.put("DMU", "DMU");
								crewBaseMap.put("ACND", "ACND");
								crewBaseMap.put("MEMU", "MENU");
								crewBaseMap.put("DND", "DND");
								crewBaseMap.put("Others1", "Others1");
								crewBaseMap.put("Others2", "Others2");

				}
				
				else if((CHM.getDmeNo()    != null  && CHM.getAcmeNo() 	 != null
						&& CHM.getAcpNo()  != null  && CHM.getDpNo() 	 != null
						&& CHM.getEmuNo()  != null && CHM.getDmuNo()     == null 
						&& CHM.getMemuNo() != null && CHM.getAcndNo()    != null  
						&& CHM.getDndNo()  != null  && CHM.getOthers1No() != null && CHM.getOthers2No() != null)) {

								crewBaseMap.put("DME", "DME");
								crewBaseMap.put("ACME", "ACME");
								crewBaseMap.put("ACP", "ACP");
								crewBaseMap.put("DP", "DP");
								crewBaseMap.put("EMU", "EMU");
								crewBaseMap.put("MEMU", "MENU");
								crewBaseMap.put("ACND", "ACND");
								crewBaseMap.put("DND", "DND");
								crewBaseMap.put("Others1", "Others1");
								crewBaseMap.put("Others2", "Others2");

				}

				else if((CHM.getDmeNo()    != null  && CHM.getAcmeNo() 	 != null
						&& CHM.getAcpNo()  != null  && CHM.getDpNo() 	 != null
						&& CHM.getEmuNo()  != null && CHM.getDmuNo()     != null 
						&& CHM.getMemuNo() == null && CHM.getAcndNo()    != null  
						&& CHM.getDndNo()  != null  && CHM.getOthers1No() != null && CHM.getOthers2No() != null))
				{
					crewBaseMap.put("DME", "DME");
					crewBaseMap.put("ACME", "ACME");
					crewBaseMap.put("ACP", "ACP");
					crewBaseMap.put("DP", "DP");
					crewBaseMap.put("EMU", "EMU");
					crewBaseMap.put("DMU", "DMU");
					crewBaseMap.put("ACND", "ACND");
					crewBaseMap.put("DND", "DND");
					crewBaseMap.put("Others1", "Others1");
					crewBaseMap.put("Others2", "Others2");
				}
				else if((CHM.getDmeNo()    != null  && CHM.getAcmeNo() 	 != null
						&& CHM.getAcpNo()  != null  && CHM.getDpNo() 	 != null
						&& CHM.getEmuNo()  != null && CHM.getDmuNo()     != null 
						&& CHM.getMemuNo() != null && CHM.getAcndNo()    == null  
						&& CHM.getDndNo()  != null  && CHM.getOthers1No() != null && CHM.getOthers2No() != null))
				{
					
					crewBaseMap.put("DME", "DME");
					crewBaseMap.put("ACME", "ACME");
					crewBaseMap.put("ACP", "ACP");
					crewBaseMap.put("DP", "DP");
					crewBaseMap.put("EMU", "EMU");
					crewBaseMap.put("DMU", "DMU");
					crewBaseMap.put("MEMU", "MENU");
					crewBaseMap.put("DND", "DND");
					crewBaseMap.put("Others1", "Others1");
					crewBaseMap.put("Others2", "Others2");
					
				}

				else if((CHM.getDmeNo()    != null  && CHM.getAcmeNo() 	 != null
						&& CHM.getAcpNo()  != null  && CHM.getDpNo() 	 != null
						&& CHM.getEmuNo()  != null && CHM.getDmuNo()     != null 
						&& CHM.getMemuNo() != null && CHM.getAcndNo()    != null  
						&& CHM.getDndNo()  != null  && CHM.getOthers1No() != null && CHM.getOthers2No() != null))
				{
				
					crewBaseMap.put("DME", "DME");
					crewBaseMap.put("ACME", "ACME");
					crewBaseMap.put("ACP", "ACP");
					crewBaseMap.put("DP", "DP");
					crewBaseMap.put("EMU", "EMU");
					crewBaseMap.put("DMU", "DMU");
					crewBaseMap.put("MEMU", "MENU");
					crewBaseMap.put("ACND", "ACND");
					crewBaseMap.put("Others1", "Others1");
					crewBaseMap.put("Others2", "Others2");
				}

				else if((CHM.getDmeNo()    != null  && CHM.getAcmeNo() 	 != null
						&& CHM.getAcpNo()  != null  && CHM.getDpNo() 	 != null
						&& CHM.getEmuNo()  != null && CHM.getDmuNo()     != null 
						&& CHM.getMemuNo() != null && CHM.getAcndNo()    != null  
						&& CHM.getDndNo()  != null  && CHM.getOthers1No() == null && CHM.getOthers2No() != null))
				{
				
					crewBaseMap.put("DME", "DME");
					crewBaseMap.put("ACME", "ACME");
					crewBaseMap.put("ACP", "ACP");
					crewBaseMap.put("DP", "DP");
					crewBaseMap.put("EMU", "EMU");
					crewBaseMap.put("DMU", "DMU");
					crewBaseMap.put("MEMU", "MENU");
					crewBaseMap.put("ACND", "ACND");
					crewBaseMap.put("DND", "DND");
					crewBaseMap.put("Others2", "Others2");
					
				}
				else if((CHM.getDmeNo()    != null  && CHM.getAcmeNo() 	 != null
						&& CHM.getAcpNo()  != null  && CHM.getDpNo() 	 != null
						&& CHM.getEmuNo()  != null && CHM.getDmuNo()     != null 
						&& CHM.getMemuNo() != null && CHM.getAcndNo()    != null  
						&& CHM.getDndNo()  != null  && CHM.getOthers1No() != null && CHM.getOthers2No() == null))
				{

					crewBaseMap.put("DME", "DME");
					crewBaseMap.put("ACME", "ACME");
					crewBaseMap.put("ACP", "ACP");
					crewBaseMap.put("DP", "DP");
					crewBaseMap.put("EMU", "EMU");
					crewBaseMap.put("DMU", "DMU");
					crewBaseMap.put("MEMU", "MENU");
					crewBaseMap.put("ACND", "ACND");
					crewBaseMap.put("DND", "DND");
					crewBaseMap.put("Others1", "Others1");
					
				}
				
				else if ((CHM.getDmeNo()    == null  && CHM.getAcmeNo() 	 == null
						&& CHM.getAcpNo()   != null  && CHM.getDpNo() 	     != null
						&& CHM.getEmuNo()   != null && CHM.getDmuNo()        != null 
						&& CHM.getMemuNo()  != null && CHM.getAcndNo()       != null  
						&& CHM.getDndNo()   != null  && CHM.getOthers1No()   != null && CHM.getOthers2No() != null))
					   {

								
								crewBaseMap.put("ACP", "ACP");
								crewBaseMap.put("DP", "DP");
								crewBaseMap.put("EMU", "EMU");
								crewBaseMap.put("DMU", "DMU");
								crewBaseMap.put("MEMU", "MENU");
								crewBaseMap.put("ACND", "ACND");
								crewBaseMap.put("DND", "DND");
								crewBaseMap.put("Others1", "Others1");
								crewBaseMap.put("Others2", "Others2");

					} 
				else if ((CHM.getDmeNo()    == null  && CHM.getAcmeNo() 	 == null
						&& CHM.getAcpNo()   == null  && CHM.getDpNo() 	     != null
						&& CHM.getEmuNo()   != null && CHM.getDmuNo()        != null 
						&& CHM.getMemuNo()  != null && CHM.getAcndNo()       != null  
						&& CHM.getDndNo()   != null  && CHM.getOthers1No()   != null && CHM.getOthers2No() != null))
					   {
								   
								
								crewBaseMap.put("DP", "DP");
								crewBaseMap.put("EMU", "EMU");
								crewBaseMap.put("DMU", "DMU");
								crewBaseMap.put("MEMU", "MENU");
								crewBaseMap.put("ACND", "ACND");
								crewBaseMap.put("DND", "DND");
								crewBaseMap.put("Others1", "Others1");
								crewBaseMap.put("Others2", "Others2");
					   }
				else if ((CHM.getDmeNo()    == null  && CHM.getAcmeNo() 	 == null
						&& CHM.getAcpNo()   == null  && CHM.getDpNo() 	 	 == null
						&& CHM.getEmuNo()   != null && CHM.getDmuNo()     	 != null 
						&& CHM.getMemuNo()  != null && CHM.getAcndNo()    	 != null  
						&& CHM.getDndNo()   != null  && CHM.getOthers1No() 	 != null && CHM.getOthers2No() != null))
					   {

							
								crewBaseMap.put("EMU", "EMU");
								crewBaseMap.put("DMU", "DMU");
								crewBaseMap.put("MEMU", "MENU");
								crewBaseMap.put("ACND", "ACND");
								crewBaseMap.put("DND", "DND");
								crewBaseMap.put("Others1", "Others1");
								crewBaseMap.put("Others2", "Others2");

					}
				else if ((CHM.getDmeNo()    == null  && CHM.getAcmeNo() 	 == null
						&& CHM.getAcpNo()   == null  && CHM.getDpNo() 	     == null
						&& CHM.getEmuNo()   == null && CHM.getDmuNo()        != null 
						&& CHM.getMemuNo()  != null && CHM.getAcndNo()       != null  
						&& CHM.getDndNo()   != null  && CHM.getOthers1No()   != null && CHM.getOthers2No() != null))
					   {
					
									
									crewBaseMap.put("DMU", "DMU");
									crewBaseMap.put("MEMU", "MENU");
									crewBaseMap.put("ACND", "ACND");
									crewBaseMap.put("DND", "DND");
									crewBaseMap.put("Others1", "Others1");
									crewBaseMap.put("Others2", "Others2");
					   }
				
				else if ((CHM.getDmeNo()    == null  && CHM.getAcmeNo() 	 == null
						&& CHM.getAcpNo()   == null  && CHM.getDpNo() 	     == null
						&& CHM.getEmuNo()   == null && CHM.getDmuNo()        == null 
						&& CHM.getMemuNo()  != null && CHM.getAcndNo()       != null  
						&& CHM.getDndNo()   != null  && CHM.getOthers1No()   != null && CHM.getOthers2No() != null))
					   {
					
									
									crewBaseMap.put("MEMU", "MENU");
									crewBaseMap.put("ACND", "ACND");
									crewBaseMap.put("DND", "DND");
									crewBaseMap.put("Others1", "Others1");
									crewBaseMap.put("Others2", "Others2");
					   }
				
				else if ((CHM.getDmeNo()    == null  && CHM.getAcmeNo() 	 == null
						&& CHM.getAcpNo()   == null  && CHM.getDpNo() 	     == null
						&& CHM.getEmuNo()   == null && CHM.getDmuNo()        == null 
						&& CHM.getMemuNo()  == null && CHM.getAcndNo()       != null  
						&& CHM.getDndNo()   != null  && CHM.getOthers1No()   != null && CHM.getOthers2No() != null))
					   {
					
									
									
									crewBaseMap.put("ACND", "ACND");
									crewBaseMap.put("DND", "DND");
									crewBaseMap.put("Others1", "Others1");
									crewBaseMap.put("Others2", "Others2");
									System.out.println("17 IF");
					   }
				
				else if ((CHM.getDmeNo()    == null  && CHM.getAcmeNo() 	 == null
						&& CHM.getAcpNo()   == null  && CHM.getDpNo() 	     == null
						&& CHM.getEmuNo()   == null && CHM.getDmuNo()        == null 
						&& CHM.getMemuNo()  == null && CHM.getAcndNo()       == null  
						&& CHM.getDndNo()   != null  && CHM.getOthers1No()   != null && CHM.getOthers2No() != null))
					   {
									
									crewBaseMap.put("DND", "DND");
									crewBaseMap.put("Others1", "Others1");
									crewBaseMap.put("Others2", "Others2");
									System.out.println("18 IF");
					   }
				
				else if ((CHM.getDmeNo()    == null  && CHM.getAcmeNo() 	 == null
						&& CHM.getAcpNo()   == null  && CHM.getDpNo() 	     == null
						&& CHM.getEmuNo()   == null && CHM.getDmuNo()        == null 
						&& CHM.getMemuNo()  == null && CHM.getAcndNo()       == null  
						&& CHM.getDndNo()   == null  && CHM.getOthers1No()   == null && CHM.getOthers2No() != null))
					   {
									
									
									
									crewBaseMap.put("Others2", "Others2");
									System.out.println("19 IF");
					   }

				else if ((CHM.getDmeNo()    == null  && CHM.getAcmeNo() 	 == null
						&& CHM.getAcpNo()   == null  && CHM.getDpNo() 	     == null
						&& CHM.getEmuNo()   == null && CHM.getDmuNo()        == null 
						&& CHM.getMemuNo()  == null && CHM.getAcndNo()       == null  
						&& CHM.getDndNo()   == null  && CHM.getOthers1No()   == null && CHM.getOthers2No() == null))
					   {
									
									System.out.println("20 IF");
					   }




			}

		} catch (CrewException e) {
			 CREW_LOGGER.debug(e.getMessage());
			
		}
	}
	

	
	
	
	private String mapString(String strMap)
	{
		Map<String, String> map = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		return map.get(strMap);  
	}
	
	
	public void updateToFrom()
	{
		toStationList.clear();
		try {
			List<TimeTableMaster> list = (List<TimeTableMaster>)schoolService.loadList("from TimeTableMaster as t where t.crewChange like '"
					+ selectedToStation+ "' AND t.trainNo like'"
					+ trainNo+ "'");
			
			
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
			
		}
		for (TimeTableMaster l : timeTableToList)
		{
			toStationList2.add(new SelectItem(l.getStationCode()));
		}
		toStationList = toStationList2;
		RequestContext.getCurrentInstance().update("viewform");
	}
	
	
	
	
	
	
	
	/// santosh Color Code for HQ OS HQ&OS 
	
	

	public void loadheadQuarter_and_Os(String trainNo)
	{
		
		
		try {
			List<TimeTableMaster>TimeTableMasterList = (List<TimeTableMaster>) schoolService.loadList("from TimeTableMaster as TM where TM.trainNo like '"+trainNo+"'");
			List<CrewOutstation>CrewOutstationList = (List<CrewOutstation>) schoolService.loadList("from CrewOutstation");
			List<CrewHeadquarterMaster>CrewHeadquarterMasterList= (List<CrewHeadquarterMaster>) schoolService.loadList("from CrewHeadquarterMaster");
			
			
		
				 checkforHeadQuarter(TimeTableMasterList,CrewHeadquarterMasterList,trainNo);
		         checkforOutStation(TimeTableMasterList,CrewOutstationList,trainNo);
		         RequestContext.getCurrentInstance().update("addcrew"); 
			
			
		} catch (CrewException e) {
			e.printStackTrace();
		}
	}
	
	


	
	private void checkforOutStation(List<TimeTableMaster> timeTableMasterList,
			List<CrewOutstation> crewOutstationList,String trainNo)
	{	    
		Collections.sort(crewOutstationList,new StationCodeComprator());
	    Collections.sort(timeTableMasterList,new TimeTableStationCodeComprator());
	   for(TimeTableMaster  TML:timeTableMasterList)
		{	 for(CrewOutstation CS:crewOutstationList)
			 { if(TML.getStationCode().equalsIgnoreCase(CS.getStationCode()))
						 {String strCodes = TML.getStationCode();
					       try {
	timeTableMaster = (TimeTableMaster) schoolService.loadUniqueObject("from TimeTableMaster pld where pld.stationCode like '"
			+strCodes+ "'and pld.trainNo like '"
			+trainNo+ "'");
	timeTableMaster.setOs(true);
	schoolService.updateObjectInDb(timeTableMaster);
	RequestContext.getCurrentInstance().update("addcrew");
} catch (CrewException e) {
	CREW_LOGGER.debug(e.getMessage());
	e.printStackTrace();
}
					        
					         break;
						 }
			 
				
			 }
		}
		
	
	}



	private void checkforHeadQuarter(
			List<TimeTableMaster> timeTableMasterList,
			List<CrewHeadquarterMaster> crewHeadquarterMasterList,String trainNo) {
		
		Collections.sort(timeTableMasterList,new TimeTableStationCodeComprator());  // sort the TimeTableMaster
		
		
		
		for(CrewHeadquarterMaster HQ:crewHeadquarterMasterList)
		{	//System.out.println(HQ.getCrewBase());
		}
		
		Collections.sort(crewHeadquarterMasterList,new HeadQuarterCodeComprator()); // sort the HQ
		for(CrewHeadquarterMaster HQ:crewHeadquarterMasterList)
		{	//System.out.println(""+HQ.getCrewBase());
		}
		
		//System.out.println("-------Matches Found----------");
		for(TimeTableMaster  TM:timeTableMasterList)
		{for(CrewHeadquarterMaster HQ:crewHeadquarterMasterList)
			{	if(TM.getStationCode().equalsIgnoreCase(HQ.getCrewBase())){
					String stationCode  = TM.getStationCode();
				//	System.out.println(""+stationCode);
					
					try {
						timeTableMaster = (TimeTableMaster) schoolService.loadUniqueObject("from TimeTableMaster pld where pld.stationCode like '"
								+stationCode+ "'and pld.trainNo like '"
								+trainNo+ "'");
						timeTableMaster.setHq(true);
						schoolService.updateObjectInDb(timeTableMaster);
						RequestContext.getCurrentInstance().update("addcrew");
					} catch (CrewException e) {
						CREW_LOGGER.debug(e.getMessage());
						e.printStackTrace();
					}
					break;
				}
			} 
		
		}
	}




	
	public void onRowEdit(RowEditEvent event)
	{
		
		System.out.println("OnRow Edit");
		FacesMessage msg = new FacesMessage("Edit Cancelled", ((CrewChangeDrivingsection) event.getObject()).getCrewBase());
        FacesContext.getCurrentInstance().addMessage(null, msg);
		
		/*FacesContext
		.getCurrentInstance()
		.addMessage(
				null,
				new FacesMessage(
						FacesMessage.SEVERITY_FATAL,"Please updated your c"+" is Already Exists ! Cannot create a duplicate.",
						null));*/
	}
	
	public void onRowCancel(RowEditEvent event)
	{
		System.out.println("OnRow Cancel");
		FacesContext
		.getCurrentInstance()
		.addMessage(
				null,
				new FacesMessage(
						FacesMessage.SEVERITY_FATAL,"Please updated your c"+" is Already Exists ! Cannot create a duplicate.",
						null));
		
	}
	
	
	
	//suggestion1 04-09-15
	
	public void suggestion1()
	{
		List<CrewHeadquarterMaster> listHQ = null;
		lRequiredHour =lRequiredHour * 60;
		List<CrewOutstation> listOS= null;
		Set<Long> listId = new LinkedHashSet<Long>();
		Integer nTotalDistance = new Integer(0);
		boolean bCheck = true;
		Date dtDepartureDate[] = new Date[100];
		listId.clear();
		iTotalMinutes = 0;
		int count2=0;
		try {
			List<TimeTableMaster> listTimeTable = (List<TimeTableMaster>) 
				    schoolService.loadList("from TimeTableMaster as t where t.trainNo like '"+toTrain+"'" );
			listHQ = (List<CrewHeadquarterMaster>) schoolService.loadList("from CrewHeadquarterMaster");
			listOS = (List<CrewOutstation>) schoolService.loadList("from CrewOutstation");
			
			for(TimeTableMaster t : listTimeTable)
			{
				for(CrewHeadquarterMaster c : listHQ)
				{
					if(t.getStationCode().equalsIgnoreCase(c.getCrewBase()))
					{
						listId.add(t.getId());
						//System.out.println("station code->>>" +t.getStationCode());
					}
					else
					{
						for(CrewOutstation c1 : listOS)	
						{
							if(t.getStationCode().equalsIgnoreCase(c1.getStationCode()))
							{
								listId.add(t.getId());
								//System.out.println("station code->>>" +t.getStationCode());
							}
						}
					}
				}
			}
				
			
			manipulationAlgo2(listId);
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
		
		
		for(Long l : listId)
		{
			System.out.println("The Id are : " +l);
		}
	}
	
	public void manipulationAlgo2(Set<Long> listId)
	{
		Long lFirstId = new Long(0l);
		Long lSecondId = new Long(0l);
		int count2=0;
		lRequiredHour = lRequiredHour * 60;
		Date dtDepartureDate[] = new Date[100];
		iTotalMinutes = 0;
		List<TimeTableMaster> listTimeTableMaster=null;
		for(Long l : listId)
		{
		
		try {
			listTimeTableMaster = (List<TimeTableMaster>) 
					schoolService.loadList("from TimeTableMaster as tt where tt.id=" +l);
			lFirstId = l;
			
			for(TimeTableMaster t : listTimeTableMaster)
			{
				dtDepartureDate[count2] = t.getDeparture();
				if(count2>=1)
				{
					iTotalMinutes = iTotalMinutes + calDifference(dtDepartureDate[count2-1],t.getArrival());
					System.out.println("--------------iTotalMinutes-------------------------------" +iTotalMinutes);
					
					
				}
				if(iTotalMinutes > lRequiredHour)
				{
					
					System.out.println("Station code is 111111111111111- " +t.getStationCode());
					iTotalMinutes = 0;
				}
				System.out.println("Station code is --- " +t.getStationCode());
				System.out.println("----------");
				count2++;
			}
						
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
		
		}
		
	}
	
	
	
	
	//ends
	
	
	
	
	
	public void checkInput()
	{
		
		System.out.println("dasdasda");
	}
	
	
	
	
	public void forward3()
	{
		
		
		
		if(lRequiredHour!=null)
		{
			
			List<CrewHeadquarterMaster> listHQ = null;
			List<CrewOutstation> listOS= null;
			Set<Long> listId = new LinkedHashSet<Long>();
			listId.clear();
			
			try {
				List<TimeTableMaster> listTimeTable = (List<TimeTableMaster>) 
			    schoolService.loadList("from TimeTableMaster as t where t.trainNo like '"+toTrain+"'");
				
				listHQ = (List<CrewHeadquarterMaster>) schoolService.loadList("from CrewHeadquarterMaster");
				listOS = (List<CrewOutstation>) schoolService.loadList("from CrewOutstation");
				
				for(TimeTableMaster t : listTimeTable)
				{
					for(CrewHeadquarterMaster c : listHQ)
					{
						if(t.getStationCode().equalsIgnoreCase(c.getCrewBase()))
						{
							listId.add(t.getId());
							//System.out.println("station code->>>" +t.getStationCode());
						}
						else
						{
							for(CrewOutstation c1 : listOS)	
							{
								if(t.getStationCode().equalsIgnoreCase(c1.getStationCode()))
								{
									listId.add(t.getId());
									//System.out.println("station code->>>" +t.getStationCode());
								}
							}
						}
					}
					
				}
				manipulationAlgo(listId);
		
			} catch (CrewException e) {
				e.printStackTrace();
			}
		}
		else
		{
			FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Required Fields is Empty ",
                    "The email address you entered is not valid.");
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, message);
		}
		
	
	}
	
	public void manipulationAlgo(Set<Long> listId)
	{
		lRequiredHour = lRequiredHour * 60;
		List<Long> lstFinalIds = new LinkedList<>();
		Long lId[] = new Long[100];
		Date dtDepartureDate[] = new Date[100];
		iTotalMinutes = 0;
		int count1=0;
		int count2=0;
		int count3=0;
		List<TimeTableMaster> listTimeTableMaster=null;
		List<String> listStrStationCodes = new LinkedList<String>();
		String strStationNames[] = new String[100];
		//String strSecondStationName = new String();
		int count4 = 0;
		for(Long l : listId)
		{
			count4++;
			try {
				listTimeTableMaster = (List<TimeTableMaster>) 
						schoolService.loadList("from TimeTableMaster as tt where tt.id=" +l);
				
				for(TimeTableMaster t: listTimeTableMaster){
					
					//System.out.println("station codes--->>>" +t.getStationCode());
					if(count2==0){listStrStationCodes.add(t.getStationCode());
					lstFinalIds.add(l);
					}
					
				//	if(listTimeTableMaster.size()==count4){listStrStationCodes.add(t.getStationCode());}
					dtDepartureDate[count1] = t.getDeparture();
					strStationNames[count3] = t.getStationCode();
					lId[count3] = t.getId();
					System.out.println("-----------------------------------");
					if(count2>=1)
					{
						iTotalMinutes = iTotalMinutes + calDifference(dtDepartureDate[count1-1],t.getArrival());
				//		System.out.println("---iTotalMinutes-------------------------------" +iTotalMinutes);
					}
					
					System.out.println("-----------------------------------");
					
					if(iTotalMinutes>lRequiredHour)
					{ 
				//		System.out.println("---iTotalMinutes--------crossed lrequiredhour-----------------------" +iTotalMinutes);
				//		System.out.println("station codes--->>>" +strStationNames[count3-1]);
						listStrStationCodes.add(strStationNames[count3-1]);
						lstFinalIds.add(lId[count3-1]);
						iTotalMinutes=0;
						iTotalMinutes = iTotalMinutes + calDifference(dtDepartureDate[count1-1],t.getArrival());
					}
					/*if(count4==listTimeTableMaster.size())
					{
						System.out.println("the sizze of the listTimeTableMaster is--!!!!!!!!!!! " +listTimeTableMaster.size());
					}*/
					//System.out.println("size of count4 is!!!!!!!!!!!!!!!!!!" + count4);
					//System.out.println("size of listTimeTableMaster is!!!!!!!!!!!!!!!!!!" + listTimeTableMaster.size());
					
					
					count1++;
					count2++;
					count3++;
				}
			} catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
			}
			if(count4==listId.size())
			{
				System.out.println("size of count4 is!!!!!!!!!!!!!!!!!!" + count4);
				System.out.println("size of listId is!!!!!!!!!!!!!!!!!!" + listId.size());
				
				try {
					listTimeTableMaster = (List<TimeTableMaster>) 
							schoolService.loadList("from TimeTableMaster as tt where tt.id=" +l);
					for(TimeTableMaster t :listTimeTableMaster )
					{
						lstFinalIds.add(t.getId());
						listStrStationCodes.add(t.getStationCode());
					}
					
					
				} catch (CrewException e) {
					CREW_LOGGER.debug(e.getMessage());
					e.printStackTrace();
				}
				
				
			}
			
		}
		System.out.println("-----------------------------------");

		schoolService.deleteQuery("Test");
		suggestion1FinalDisplay(lstFinalIds);
	}
	
	
	public void suggestion1FinalDisplay(List<Long> lstFinalIds)
	{
		Map<Long,Long> mapFinalSuggestion1 = new LinkedHashMap<>();
		Date dtDeparture = new Date();
		Date dtArrival = new Date();
		String strFrom = new String();
		String strTo = new String();
		Integer nFirstDistance = new Integer(0);
		Integer nSecondDistance = new Integer(0);
		Integer nTotalDistance = new Integer(0);
		int count = 0;
		Long lFirstId[] = new Long[100];
		
		for(Long l : lstFinalIds)
		{
			lFirstId[count] = l;
			if(count>=1)
			{
				mapFinalSuggestion1.put(lFirstId[count-1], l);
			}
			count++;
		}
		List<TimeTableMaster> listFinalSuggestion1 = (List<TimeTableMaster>) context.getBean("list");
		List<TimeTableMaster> listFinalSuggestion2 = (List<TimeTableMaster>) context.getBean("list");
		
		for(Entry<Long,Long> entry : mapFinalSuggestion1.entrySet())
		{
		//	System.out.println("First Id is " +entry.getKey() +"\t\t Second Key is " +entry.getValue());
		//	System.out.println("-----------------------------------------------");
			try {
				listFinalSuggestion1 = (List<TimeTableMaster>) 
						schoolService.loadList("from TimeTableMaster as tt where tt.id=" +entry.getKey());
				for(TimeTableMaster t1 : listFinalSuggestion1)
				{	strFrom = t1.getStationCode();
					dtDeparture = t1.getDeparture();
					nFirstDistance = t1.getDistance();
				}
				listFinalSuggestion2 = (List<TimeTableMaster>) 
						schoolService.loadList("from TimeTableMaster as tt where tt.id=" +entry.getValue());
				for(TimeTableMaster t2 : listFinalSuggestion2)
				{
					strTo = t2.getStationCode();
					dtArrival = t2.getArrival();
					nSecondDistance = t2.getDistance();
				}
				nTotalDistance = nSecondDistance - nFirstDistance;
				
				test.setTrainNo(trainNo);
				test.setFromStation(strFrom);
				test.setToStation(strTo);
				test.setDeparture(dtDeparture);
				test.setArrival(dtArrival);
				test.setDistance(nTotalDistance);
				
				
			try {
					schoolService.saveInDb(test);
				} catch (CrewException e) {
					CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
				}
				
			//	System.out.println("-----------------------------------------------");
			} catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
			}
			
			
		}
		
		listTest.clear();
		try {
			listTest = (List<Test>) schoolService.loadList("from Test");
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	
	
	public void save_Generated_Driving_Sections(List<TimeTableMaster> listTimeTable)
	{
		System.out.println("the size of the listTime table is ********** " +listTimeTable.size());
		String strFromStation[] = new String[100];
		String strToStation[] = new String[100];
		Date dtDepart[] = new Date[100];
		Date dtArrival[] = new Date[100];
		Integer lDistance[] = new Integer[100];
		int count1 = 0;
		
		schoolService.deleteQuery("Test");
		
		
		for(TimeTableMaster t : listTimeTable)
		{
			strFromStation[count1] = t.getStationCode();
			dtDepart[count1] = t.getDeparture();
			dtArrival[count1] = t.getArrival();
			lDistance[count1] = t.getDistance();
			Integer lTotalDistance = new Integer(0);
			
			if(count1>=1)
			{
				strToStation[count1] = t.getStationCode();
				
				System.out.println("strFromStation[count1] ********** " +strFromStation[count1-1]);
				System.out.println("dtDepart[count1] ********** " +dtDepart[count1]);
				System.out.println("dtArrival[count1] ********** " +dtArrival[count1]);
				System.out.println("lDistance[count1] ********** " +lDistance[count1-1]);
				lTotalDistance = lDistance[count1] - lDistance[count1-1];
				System.out.println("lTotalDistance ********** " +lTotalDistance);
				System.out.println("strToStation[count1] ********** " +strToStation[count1]);
				
				
				test.setTrainNo(trainNo);
				test.setFromStation(strFromStation[count1-1]);
				test.setToStation(strToStation[count1]);
				test.setDeparture(dtDepart[count1-1]);
				test.setArrival(dtArrival[count1]);
				test.setDistance(lTotalDistance);
				
				
			try {
				schoolService.saveInDb(test);
				lTotalDistance = 0;
				} catch (CrewException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
				}
				
				
			}
			
			count1++;
		}
		listTest.clear();
		try {
			listTest = (List<Test>) schoolService.loadList("from Test");
			
			/*for(Test t : listTest)
			{
				System.out.println("from station-!!!!!!!!!!!!!!" +t.getFromStation()+"\t To Station!!!!!!!!!!!!!!" +t.getToStation());
			}*/
			
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public Integer calDifference(Date dtFirst,Date dtSecond)
	{
		
		System.out.println("first date is " + dtFirst);
		System.out.println("dtSecond date is " + dtSecond);
		
		Calendar calDeparture1 = Calendar.getInstance();
		Calendar calDeparture2 = Calendar.getInstance();
		
		calDeparture1.setTime(dtFirst);
		calDeparture2.setTime(dtSecond);
		
		Integer iDiffInMinutes = new Integer(0);
		
		Integer iDepHour1 = calDeparture1.get(Calendar.HOUR_OF_DAY);
		Integer iDepMinute1 = calDeparture1.get(Calendar.MINUTE);
		
		Integer iDepHour2 = calDeparture2.get(Calendar.HOUR_OF_DAY);
		Integer iDepMinute2 = calDeparture2.get(Calendar.MINUTE);
		
		
		if(iDepHour1==0) iDepHour1 = 24;
		if(iDepHour2==0) iDepHour2 = 24;
		
		if(iDepHour1 == iDepHour2)
		{
			if(iDepMinute1 < iDepMinute2)
			{
				iDiffInMinutes =  iDepMinute2 - iDepMinute1;
				System.out.println("Diff In minutes dep<arr -> " + iDiffInMinutes);
			}
		}
		else if(iDepHour1 < iDepHour2)
		{
			System.out.println("Hour of Departure -> " + iDepHour1 +"\t Minute of Departure-> " +iDepMinute1);
			System.out.println("Hour of Departure -> " + iDepHour2 +"\t Minute of Departure-> " +iDepMinute2);
			
			iDiffInMinutes =  (iDepHour2*60 + iDepMinute2) - (iDepHour1 * 60 +iDepMinute1);
			System.out.println("Diff In minutes dep<arr -> " + iDiffInMinutes);
		}
		else					
		{
			System.out.println("Hour of Departure -> " + iDepHour1 +"\t Minute of Departure-> " +iDepMinute1);
			System.out.println("Hour of Departure -> " + iDepHour2 +"\t Minute of Departure-> " +iDepMinute2);
			
			iDiffInMinutes = (iDepHour2*60 + iDepMinute2)  + 24* 60 - (iDepHour1 * 60 +iDepMinute1);
			System.out.println("Diff In minutes dep>arr -> " + iDiffInMinutes);
		}
		
		
		return iDiffInMinutes;
	}

	//backward suggestion
	public void backwardSuggestion()
	{
		List<Long> lstFinalIds = new LinkedList<>();
		List<CrewHeadquarterMaster> listHQ = null;
		List<CrewOutstation> listOS= null;
		Set<Long> listId2 = new LinkedHashSet<Long>();
		try {
			List<TimeTableMaster> listTimeTable = (List<TimeTableMaster>) 
		    schoolService.loadList("from TimeTableMaster as t where t.trainNo like '"+toTrain+"'"+"order by t.id desc");
			
			listHQ = (List<CrewHeadquarterMaster>) schoolService.loadList("from CrewHeadquarterMaster");
			listOS = (List<CrewOutstation>) schoolService.loadList("from CrewOutstation");
			
			for(TimeTableMaster t : listTimeTable)
			{
				for(CrewHeadquarterMaster c : listHQ)
				{
					if(t.getStationCode().equalsIgnoreCase(c.getCrewBase()))
					{
						listId2.add(t.getId());
						//System.out.println("station code->>>" +t.getStationCode());
					}
					else
					{
						for(CrewOutstation c1 : listOS)	
						{
							if(t.getStationCode().equalsIgnoreCase(c1.getStationCode()))
							{
								listId2.add(t.getId());
								//System.out.println("station code->>>" +t.getStationCode());
							}
						}
					}
				}
				
			}
			System.out.println("Backward Ids are ");
			for(Long l : listId2)
			{
				System.out.println(l);
			}
			manipulationBackward(listId2);
		} catch (CrewException e) {
			e.printStackTrace();
		}
	}
	
	
	public void manipulationBackward(Set<Long> listId2)
	{
		List<Long> listFinalBack = new LinkedList<Long>();
		listTimeTableMasterForward2.clear();
		lRequiredHour = lRequiredHour * 60;
		Date dtArrivalDate[] = new Date[100];
		Long lIds[] = new Long[100];
		Date dtDep[] = new Date[100];
		int count1=0;
		iTotalMinutes = 0;
		int count2=0;
		List<String> listStrStationCodes1 = new LinkedList<String>();
		String strStationNames[] = new String[100];
		//String strSecondStationName = new String();
		int count4 = 0;
		int count3=0;
		Boolean bCrossed = false;
		List<TimeTableMaster> listTimeTableMaster1=null;
		for(Long l : listId2)
		{
			count4++;
			
				try {
					listTimeTableMaster1 = (List<TimeTableMaster>) 
							schoolService.loadList("from TimeTableMaster as tt where tt.id=" +l);
				} catch (CrewException e) {
					e.printStackTrace();
				}
				for(TimeTableMaster t: listTimeTableMaster1){
					
					
					strStationNames[count3] = t.getStationCode();
					if(count2==0){listStrStationCodes1.add(t.getStationCode());
					listFinalBack.add(t.getId());
					
					}
					System.out.println("-----------------------------------");
					dtArrivalDate[count1] = t.getArrival();
					dtDep[count1] = t.getDeparture();
					lIds[count1] = t.getId();
					if(count2>=1)
					{
						iTotalMinutes = iTotalMinutes + calDifference(dtDep[count1],dtArrivalDate[count1-1]);
						System.out.println("departure time--->>" + dtDep +"\t arrival time-->>>" +dtArrivalDate[count1]);
						System.out.println("---iTotalMinutes-------------------------------" +iTotalMinutes);
					}

					if(iTotalMinutes>lRequiredHour)
					{ 
						System.out.println("---iTotalMinutes--------crossed lrequiredhour-----------------------" +iTotalMinutes);
						System.out.println("station codes--->>>" +strStationNames[count3]);
						listStrStationCodes1.add(strStationNames[count3-1]);
						listFinalBack.add(lIds[count1-1]);
						iTotalMinutes= 0;
					}
				
					System.out.println("-----------------------------------");
					
					
					
					count2++;
					count1++;
					count3++;
					
					if(count4==listId2.size())
					{
						try {
							listTimeTableMaster1 = (List<TimeTableMaster>) 
									schoolService.loadList("from TimeTableMaster as tt where tt.id=" +l);
							for(TimeTableMaster t1 :listTimeTableMaster1 )
							{
								listStrStationCodes1.add(t1.getStationCode());
								System.out.println("station codes are !!!!!!!!!!!!!!!!!!" + t.getStationCode());
								listFinalBack.add(t1.getId());
							}
							
							
						} catch (CrewException e) {
							CREW_LOGGER.debug(e.getMessage());
							e.printStackTrace();
						}
					}
			}
			
		}
		
		
		System.out.println("-----------------------------------");
		TimeTableMaster tObj = (TimeTableMaster) context.getBean("timeTableMaster");
		for(String s : listStrStationCodes1)
		{
			System.out.println("station codes--->>>" +s);
			try {
				tObj = (TimeTableMaster) 
						schoolService.loadUniqueObject("from TimeTableMaster as tt where tt.stationCode like'"+s+"'");
				
				listTimeTableMasterForward2.add(tObj);
			} catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
			}
		}
		System.out.println("Size of listTimeTableMasterForward >>>" +listTimeTableMasterForward2.size());
		//save_Generated_Driving_Sections2(listTimeTableMasterForward2);
		System.out.println("---------------Final Ids are-------------------");
		
		
		for(Long l : listFinalBack)
		{
			System.out.println(l);
		}
		schoolService.deleteQuery("Test2");
		showFinalSuggest2(listFinalBack);
	}
	
	
	public void showFinalSuggest2(List<Long> listFinalBack)
	{
		Collections.sort(listFinalBack);
		Map<Long,Long> mapFinalSuggestion1 = new LinkedHashMap<>();
		Date dtDeparture = new Date();
		Date dtArrival = new Date();
		String strFrom = new String();
		String strTo = new String();
		Integer nFirstDistance = new Integer(0);
		Integer nSecondDistance = new Integer(0);
		Integer nTotalDistance = new Integer(0);
		int count = 0;
		Long lFirstId[] = new Long[100];
		
		List<TimeTableMaster> listFinalSuggestion1 = (List<TimeTableMaster>) context.getBean("list");
		List<TimeTableMaster> listFinalSuggestion2 = (List<TimeTableMaster>) context.getBean("list");
		
		
		for(Long l : listFinalBack)
		{
			lFirstId[count] = l;
			if(count>=1)
			{
				mapFinalSuggestion1.put(lFirstId[count-1], l);
			}
			count++;
		}
		
		for(Entry<Long,Long> entry : mapFinalSuggestion1.entrySet() )
		{
			System.out.println("First Id is " +entry.getKey() +"\t\t Second Key is " +entry.getValue());
			System.out.println("------------------------------------------");
			try {
				listFinalSuggestion1 = (List<TimeTableMaster>) 
						schoolService.loadList("from TimeTableMaster as tt where tt.id=" +entry.getKey());
				for(TimeTableMaster t1 : listFinalSuggestion1)
				{
					
					System.out.println("From station is " +t1.getStationCode());
					System.out.println("Departure is " +t1.getDeparture());
					System.out.println("Distance1 is " +t1.getDistance());
					
					strFrom = t1.getStationCode();
					dtDeparture = t1.getDeparture();
					nFirstDistance = t1.getDistance();
					
					
				}
				
				
				
				
				listFinalSuggestion2 = (List<TimeTableMaster>) 
						schoolService.loadList("from TimeTableMaster as tt where tt.id=" +entry.getValue());
				for(TimeTableMaster t2 : listFinalSuggestion2)
				{
					System.out.println("to station is " +t2.getStationCode());
					System.out.println("Arrival is " +t2.getArrival());
					System.out.println("Distance2 is " +t2.getDistance());
				
					strTo = t2.getStationCode();
					dtArrival = t2.getArrival();
					nSecondDistance = t2.getDistance();
				}
				System.out.println("------------------------------------------");
				nTotalDistance = nSecondDistance - nFirstDistance;
				test2.setTrainNo(trainNo);
				test2.setFromStation(strFrom);
				test2.setToStation(strTo);
				test2.setDeparture(dtDeparture);
				test2.setArrival(dtArrival);
				test2.setDistance(nTotalDistance);
				
				schoolService.saveInDb(test2);
				
			}catch(Exception e)
			{
				CREW_LOGGER.debug(e.getMessage());
			}
			
		}
		
		listTest2.clear();
		try {
			listTest2 = (List<Test2>) schoolService.loadList("from Test2");
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	
	
	
	public void save_Generated_Driving_Sections2(List<TimeTableMaster> listTimeTable)
	{
		System.out.println("the size of the listTime table is ********** " +listTimeTable.size());
		
		String strFromStation[] = new String[100];
		String strToStation[] = new String[100];
		Date dtDepart[] = new Date[100];
		Date dtArrival[] = new Date[100];
		Integer lDistance[] = new Integer[100];
		int count1 = 0;
		
		schoolService.deleteQuery("Test2");
		for(TimeTableMaster t : listTimeTable)
		{
			System.out.println("departure-> " +t.getDeparture() +"\t\t Arrival " +t.getArrival());
		}
		
		for(TimeTableMaster t : listTimeTable)
		{
			System.out.println("DEparture time->>>" +t.getDeparture() +"\t Arrival Time-->>>" +t.getArrival());
			strFromStation[count1] = t.getStationCode();
			dtDepart[count1] = t.getDeparture();
			dtArrival[count1] = t.getArrival();
			lDistance[count1] = t.getDistance();
			Integer lTotalDistance = new Integer(0);
			
			if(count1>=1)
			{
				strToStation[count1] = t.getStationCode();
				
				System.out.println("strFromStation[count1] ********** " +strFromStation[count1-1]);
				System.out.println("dtDepart[count1] ********** " +dtDepart[count1]);
				System.out.println("dtArrival[count1] ********** " +dtArrival[count1]);
				System.out.println("lDistance[count1] ********** " +lDistance[count1-1]);
				lTotalDistance = java.lang.Math.abs(lDistance[count1] - lDistance[count1-1]);
				System.out.println("strToStation[count1] ********** " +strToStation[count1]);
				
				
				test2.setTrainNo(trainNo);
				test2.setFromStation(strFromStation[count1-1]);
				test2.setToStation(strToStation[count1]);
				test2.setDeparture(dtDepart[count1]);
				test2.setArrival(dtArrival[count1-1]);
				test2.setDistance(lTotalDistance);
				
				
			try {
				schoolService.saveInDb(test2);
				lTotalDistance = 0;
				} catch (CrewException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
				}
			}
			count1++;
		}
		listTest2.clear();
		try {
			listTest2 = (List<Test2>) schoolService.loadList("from Test2 as t order by t.id desc");
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
	}

	public void updateToDrivingSectionTable()
	{
		/*System.out.println("Train number is->>>>>" + toTrain);*/
		CrewChangeDrivingsection crew = (CrewChangeDrivingsection) context.getBean("crewChange");
		schoolService.deleteByTrainNumber(toTrain, "CrewChangeDrivingsection");
	//	int c = 0;
		
		for(Test t : listTest)
		{
			crew.setTrainNo(t.getTrainNo());
			crew.setFromStation(t.getFromStation());
			crew.setToStation(t.getToStation());
			crew.setDeparture(t.getDeparture());
			crew.setArrival(t.getArrival());
			crew.setKilometer(t.getDistance().longValue());
			try {
				crew.setCustomer((Customer)schoolService.loadUniqueObject("from Customer as ct where ct.id="+customerloginId));
			} catch (CrewException e1) {
				CREW_LOGGER.debug(e1.getMessage());
				e1.printStackTrace();
			}
		//	crew.setCrewBase(listStrCrewBase.get(c));
			//crew.setCrewType(listStrCrewType.get(c));
			try {
				schoolService.saveInDb(crew);
				iTotalMinutes=0;
			} catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
			}
		}
		loadallCrewChange();
		//RequestContext.getCurrentInstance().update("CrewTable");;
	}
	
	
	public void updateToDrivingSectionTable1()
	{
		System.out.println("Train number is->>>>>" + toTrain);
		CrewChangeDrivingsection crew1 = (CrewChangeDrivingsection) context.getBean("crewChange");
		
		schoolService.deleteByTrainNumber(toTrain, "CrewChangeDrivingsection");
		for(Test2 t : listTest2)
		{
			
			crew1.setTrainNo(t.getTrainNo());
			crew1.setFromStation(t.getToStation());
			crew1.setToStation(t.getFromStation());
			crew1.setDeparture(t.getDeparture());
			crew1.setArrival(t.getArrival());
			crew1.setKilometer(t.getDistance().longValue());
			try {
				crew.setCustomer((Customer)schoolService.loadUniqueObject("from Customer as ct where ct.id="+customerloginId));
			} catch (CrewException e1) {
				CREW_LOGGER.debug(e1.getMessage());
				e1.printStackTrace();
			}
	
			try {
				schoolService.saveInDb(crew1);
			} catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
			}
		}
		//RequestContext.getCurrentInstance().update("viewform");
		loadallCrewChange();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/// all getter and setter




	public List<TimeTableMaster> getMastertablelist() {
		return mastertablelist;
	}


	public void setMastertablelist(List<TimeTableMaster> mastertablelist) {
		this.mastertablelist = mastertablelist;
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


	public ApplicationContext getContext() {
		return context;
	}


	public void setContext(ApplicationContext context) {
		this.context = context;
	}





	public String getFromTrain() {
		return fromTrain;
	}


	public void setFromTrain(String fromTrain) {
		this.fromTrain = fromTrain;
	}





	public List<SelectItem> getToStationList() {
		return toStationList;
	}


	public void setToStationList(List<SelectItem> toStationList) {
		this.toStationList = toStationList;
	}



	public void setPassedParameter(int passedParameter) {
		this.passedParameter = passedParameter;
	}



	public int getPassedParameter() {
		return passedParameter;
	}




	public static Logger getCrewLogger() {
		return CREW_LOGGER;
	}



	public String getCrewBaseQuery() {
		return crewBaseQuery;
	}



	public void setCrewBaseQuery(String crewBaseQuery) {
		this.crewBaseQuery = crewBaseQuery;
	}



	public String getSelectedcrewBaseName() {
		return selectedcrewBaseName;
	}



	public void setSelectedcrewBaseName(String selectedcrewBaseName) {
		this.selectedcrewBaseName = selectedcrewBaseName;
	}



	public Map<String,String> getCrewBaseMap() {
		return crewBaseMap;
	}



	public void setCrewBaseMap(Map<String,String> crewBaseMap) {
		this.crewBaseMap = crewBaseMap;
	}

	public List<CrewHeadquarterMaster> getCrewBaseList() {
		return crewBaseList;
	}

	public void setCrewBaseList(List<CrewHeadquarterMaster> crewBaseList) {
		this.crewBaseList = crewBaseList;
	}

	public long getToDistance() {
		return toDistance;
	}

	public void setToDistance(long toDistance) {
		this.toDistance = toDistance;
	}

	public long getFromDistance() {
		return fromDistance;
	}

	public void setFromDistance(long fromDistance) {
		this.fromDistance = fromDistance;
	}

	public long getTotalDistance() {
		return totalDistance;
	}



	public void setTotalDistance(long totalDistance) {
		this.totalDistance = totalDistance;
	}



	public Date getFromDate() {
		return fromDate;
	}



	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}



	public Date getToDate() {
		return toDate;
	}



	public void setToDate(Date toDate) {
		this.toDate = toDate;
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





	public String getToTrain() {
		return toTrain;
	}



	public void setToTrain(String toTrain) {
		this.toTrain = toTrain;
	}



	public String getTrainNo() {
		return trainNo;
	}



	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}



	public String getSelectedCrewBaseType() {
		return selectedCrewBaseType;
	}



	public void setSelectedCrewBaseType(String selectedCrewBaseType) {
		this.selectedCrewBaseType = selectedCrewBaseType;
	}



	public String getSelectedToStation() {
		return selectedToStation;
	}



	public void setSelectedToStation(String selectedToStation) {
		this.selectedToStation = selectedToStation;
	}



	public CrewChangeDrivingsection getCrew() {
		return crew;
	}



	public void setCrew(CrewChangeDrivingsection crew) {
		this.crew = crew;
	}



	public List<CrewChangeDrivingsection> getCrewList() {
		return crewList;
	}



	public void setCrewList(List<CrewChangeDrivingsection> crewList) {
		this.crewList = crewList;
	}



	public Date getDepartureTime() {
		return departureTime;
	}



	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}



	public Date getArrivalTime() {
		return arrivalTime;
	}



	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}



	public CrewChangeDrivingsection getEditCrewChange() {
		return editCrewChange;
	}



	public void setEditCrewChange(CrewChangeDrivingsection editCrewChange) {
		this.editCrewChange = editCrewChange;
	}




	public List<TimeTableMaster> getTimeTableMasterList() {
		return timeTableMasterList;
	}



	public void setTimeTableMasterList(List<TimeTableMaster> timeTableMasterList) {
		this.timeTableMasterList = timeTableMasterList;
	}





	public List<Object[]> getObjects() {
		return objects;
	}



	public void setObjects(List<Object[]> objects) {
		this.objects = objects;
	}



	







	public List<TimeTableMaster> getTimeTableIdTrainList() {
		return timeTableIdTrainList;
	}



	public void setTimeTableIdTrainList(List<TimeTableMaster> timeTableIdTrainList) {
		this.timeTableIdTrainList = timeTableIdTrainList;
	}



	public List<TimeTableMaster> getTimeTableToList() {
		return timeTableToList;
	}



	public void setTimeTableToList(List<TimeTableMaster> timeTableToList) {
		this.timeTableToList = timeTableToList;
	}



	public List<SelectItem> getToStationList2() {
		return toStationList2;
	}



	public void setToStationList2(List<SelectItem> toStationList2) {
		this.toStationList2 = toStationList2;
	}



	public List<TimeTableMaster> getTableList() {
		return tableList;
	}



	public void setTableList(List<TimeTableMaster> tableList) {
		this.tableList = tableList;
	}



	public List<CrewChangeDrivingsection> getCrewChangeDrivingsectionsList() {
		return crewChangeDrivingsectionsList;
	}



	public void setCrewChangeDrivingsectionsList(
			List<CrewChangeDrivingsection> crewChangeDrivingsectionsList) {
		this.crewChangeDrivingsectionsList = crewChangeDrivingsectionsList;
	}



	public List<CrewChangeDrivingsection> getSelectCrewChangeList() {
		return selectCrewChangeList;
	}



	public Date getDepartTime2() {
		return departTime2;
	}



	public void setDepartTime2(Date departTime2) {
		this.departTime2 = departTime2;
	}






	


	


	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public void setDifferenceDate(Long differenceDate) {
		this.differenceDate = differenceDate;
	}



	public void setDiffInSecond(Long diffInSecond) {
		this.diffInSecond = diffInSecond;
	}



	public void setDiffInMinute(Long diffInMinute) {
		this.diffInMinute = diffInMinute;
	}



	public void setDiffInHour(Long diffInHour) {
		this.diffInHour = diffInHour;
	}



	public void setDiffInDays(Long diffInDays) {
		this.diffInDays = diffInDays;
	}



	public void setSelectCrewChangeList(List<CrewChangeDrivingsection> selectCrewChangeList) {
		this.selectCrewChangeList = selectCrewChangeList;
	}

	public Long getCustomerloginId() {
		return customerloginId;
	}

	public void setCustomerloginId(Long customerloginId) {
		this.customerloginId = customerloginId;
	}



	public StringBuilder getDutyhours() {
		return dutyhours;
	}



	public void setDutyhours(StringBuilder dutyhours) {
		this.dutyhours = dutyhours;
	}



	public Long getlRequiredHour() {
		return lRequiredHour;
	}



	public List<Test> getListTest() {
		return listTest;
	}



	public void setListTest(List<Test> listTest) {
		this.listTest = listTest;
	}



	public List<TimeTableMaster> getListTimeTableMasterForward() {
		return listTimeTableMasterForward;
	}



	public void setListTimeTableMasterForward(
			List<TimeTableMaster> listTimeTableMasterForward) {
		this.listTimeTableMasterForward = listTimeTableMasterForward;
	}



	public Test getTest() {
		return test;
	}



	public void setTest(Test test) {
		this.test = test;
	}



	public void setlRequiredHour(Long lRequiredHour) {
		this.lRequiredHour = lRequiredHour;
	}



	public List<TimeTableMaster> getListTimeTableMasterForward2() {
		return listTimeTableMasterForward2;
	}



	public void setListTimeTableMasterForward2(
			List<TimeTableMaster> listTimeTableMasterForward2) {
		this.listTimeTableMasterForward2 = listTimeTableMasterForward2;
	}



	public List<Test2> getListTest2() {
		return listTest2;
	}



	public void setListTest2(List<Test2> listTest2) {
		this.listTest2 = listTest2;
	}



	








}
