package com.crew.bean;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ColumnResizeEvent;
import org.primefaces.event.SelectEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.crew.exception.CrewException;
import com.crew.model.CrewChangeDrivingsection;
import com.crew.model.CrewHeadquarterMaster;
import com.crew.model.CrewLink;
import com.crew.model.DrivingDutyOfPilotDrivingSection;
import com.crew.model.HqOsRest;
import com.crew.model.RoundTrip;
import com.crew.model.SaveLinkParameter;
import com.crew.service.CrewService;
import com.crew.util.CrewUtilities;



@ManagedBean(name="crewlink",eager=true)
@ViewScoped
@SuppressWarnings("unchecked")
public class CreatedCrewLink {
	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "schoolService", value = "#{schoolService}")
	private CrewService schoolService;
	private HttpSession httpSession = (HttpSession) FacesContext
			.getCurrentInstance().getExternalContext().getSession(false);
	private static final Logger CREW_LOGGER = Logger.getLogger(adminDrivingduty.class);
	private ApplicationContext context;
	private List<CrewLink> listCrewLink;
	private List<RoundTrip> listRoundTrip;
	private long loginId;
	private List<DrivingDutyOfPilotDrivingSection> drivingSectionList;
	private List<RoundTrip> roundTripCalcuationList;
	private StringBuffer HqTotalHour;
	private List<CrewChangeDrivingsection> sequenceDrivingSectionList;
	private Double average_Kms_per_month;
	private List<CrewChangeDrivingsection> crewChangeDrivingsectionlist;
	private List<CrewChangeDrivingsection> ccdsChanges;
	private List<CrewLink> selectionList;
	private float Average_WorkingHours_14Days;
	private int number_of_Loco;
	private float Average_workingHour;
	private float averageWorking_Per14DayHourPercentage;
	private float averageWorkingPer;
	
	//  
	private long totalOsRestHours;
	private float totalOsRestHours_Percentage;
	
	
	private long totalhqRestHours;
	private float totalhqRestHours_Percentage;
	
	private Integer nCountNumberOfLocoPilots = new Integer(0);
	
	
	private Double running_Time;
	
	private Long nTotalKMs = new Long(0);
	private Integer totalHours = new Integer(0);
	private Integer nRunningTime = new Integer(0);
	private Double dAvgKM = new Double(0);
	private Integer nTotalRunningTime = new Integer(0);
	
	private Integer nTotalKM = new Integer(0);
	private Integer nAvgWorkingPerWeek = new Integer(0);
	private Integer nTotalHQRest = new Integer(0);
	private Integer nTotalOSRest = new Integer(0);
	
	
	private Integer nTotalKMSub = new Integer(0);
	private Double nAvgWorkingPerWeekSub = new Double(0);
	private Double nTotalHQRestSub = new Double(0);
	private Double nTotalOSRestSub = new Double(0);
	private List<CrewHeadquarterMaster> crewBaseList;
	private String crewBaseQuery;
	private String selectedcrewBaseName;
	private Map<String,String> crewBaseMap = new HashMap<String, String>();
	private String selectedCrewBaseType;
	@PostConstruct
	private void initilization() {

		if (!(httpSession.getAttribute("COMMONLOGINID") == null)) {
			loginId = (Long) httpSession.getAttribute("COMMONLOGINID");
			
			 createList();
			 loadList();

		}  else {
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
		listCrewLink = (List<CrewLink>) context.getBean("list");
		listRoundTrip = (List<RoundTrip>) context.getBean("list");
		sequenceDrivingSectionList = (List<CrewChangeDrivingsection>) context.getBean("list");
		ccdsChanges  = (List<CrewChangeDrivingsection>) context.getBean("list");
		selectionList = (List<CrewLink>) context.getBean("list");
		crewBaseList =  (List<CrewHeadquarterMaster>) context.getBean("list");
	}
	
	public void loadList()
	{
		try {
			
			listRoundTrip = (List<RoundTrip>) schoolService.loadList("from RoundTrip as rt where rt.customer.id="+loginId);
			 loadHeadQuarterList();
		} catch (CrewException e) {
			e.printStackTrace();
			CREW_LOGGER.debug(e.getMessage());
		}
		
	}
	public void updateCrewLinkTable()
	{
		try {
			listCrewLink = (List<CrewLink>) 
	schoolService.loadList("from CrewLink as cc where cc.customer.id="+loginId +"and cc.crewBase like '"+crewBaseQuery+"' and cc.crewType like '"+selectedCrewBaseType+"'");
	
			calculateNumberOfLocoPilots();
		 	average_km_hour_PerMonth() ;
		 	calculateAvgWorkingHourPerWeek();
			calculateTotalHQRest();
			calculateTotalOSRest();
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
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
	public void calculateNumberOfLocoPilots()
	{
		System.out.println("---------Inside the Calculate Number of Loco PIlots ------------------ ");
		Integer nTotalWorkingHour = new Integer(0);
		System.out.println("--------------------------- ");
		for(CrewLink c1 : listCrewLink)
		{
			String strHour = new String();
			String strMinutes = new String();
			String[] strSplitParts = c1.getWorkingHour().split(":");
			for(int i = 0; i<strSplitParts.length-1;i++)
			{
				strHour = strSplitParts[i];
				strMinutes = strSplitParts[i+1];
				nTotalWorkingHour = nTotalWorkingHour + Integer.parseInt(strHour)* 60 + Integer.parseInt(strMinutes);
			//	System.out.println("StrHour is " + strHour);
			//	System.out.println("strMinutes is " + strMinutes);
			}
		}
		System.out.println("--------------------------- ");
		//System.out.println("nTotalWorkingHour ---  >>>>>> " +nTotalWorkingHour);
		nTotalWorkingHour = nTotalWorkingHour / 60;
		//System.out.println("nTotalWorkingHour in hour ---  >>>>>> " +nTotalWorkingHour);
		nTotalWorkingHour = nTotalWorkingHour / 24;
		nCountNumberOfLocoPilots = nTotalWorkingHour;
		System.out.println("nTotalWorkingHour in days ---  >>>>>> " +nTotalWorkingHour);
	}
	public void calculateAvgWorkingHourPerWeek()
	{
		
		System.out.println("Departure and arrival ");
		for(CrewLink c1 : listCrewLink)
		{
			nAvgWorkingPerWeek = nAvgWorkingPerWeek + calculateDiffBetweenDepartureAndArrival(c1.getDeparture(),c1.getArrivalTime());
			nTotalRunningTime = nTotalRunningTime + calculateDiffBetweenDepartureAndArrival(c1.getDeparture(),c1.getArrivalTime());
			nAvgWorkingPerWeek = nAvgWorkingPerWeek + 60;
		}
	//	System.out.println("nAvgWorkingPerWeek ------ " + nAvgWorkingPerWeek);
		nAvgWorkingPerWeek = nAvgWorkingPerWeek / 60;
	//	System.out.println("nAvgWorkingPerWeek in hour ------ " + nAvgWorkingPerWeek);
		nAvgWorkingPerWeek = nAvgWorkingPerWeek * 7;
	//	System.out.println("nAvgWorkingPerWeek in week ------ " + nAvgWorkingPerWeek);
		nTotalRunningTime = nTotalRunningTime / 60;
		
	}
	public void calculateTotalHQRest()
	{
		
		for(CrewLink c1 : listCrewLink)
		{
			nTotalHQRest = nTotalHQRest + Integer.parseInt(c1.getHeadQuarterRest());
			
		}
		//System.out.println("nTotalHQRest   -----------" +nTotalHQRest);
		nTotalHQRestSub = (double) (nTotalHQRest / 60);
		nTotalHQRest = nTotalHQRest / 60;
		//System.out.println("nTotalHQRest in hour -----------" +nTotalHQRest);
		if(nCountNumberOfLocoPilots!=0)
			nTotalHQRest = nTotalHQRest / nCountNumberOfLocoPilots;
		nTotalHQRest = nTotalHQRest * 14;
		System.out.println("nTotalHQRest in per 14 days -----------" +nTotalHQRest);
	}
	
	public void calculateTotalOSRest()
	{
		
		for(CrewLink c1 : listCrewLink)
		{
			nTotalOSRest = nTotalOSRest + Integer.parseInt(c1.getOutStationRest());
			
		}
		//System.out.println("nTotalHQRest   -----------" +nTotalHQRest);
		nTotalOSRestSub = (double) (nTotalOSRest / 60);
		nTotalOSRest = nTotalOSRest / 60;
		//System.out.println("nTotalHQRest in hour -----------" +nTotalHQRest);
		if(nCountNumberOfLocoPilots!=0)
		nTotalOSRest = nTotalOSRest / nCountNumberOfLocoPilots;
		nTotalOSRest = nTotalOSRest * 14;
		System.out.println("nTotalOSRest in per 14 days -----------" +nTotalOSRest);
	}
	
	public Integer calculateDiffBetweenDepartureAndArrival(Date dtDeparture,Date dtArrival)
	{
		Integer iDiffInMinutes = new Integer(0);
		Integer iTotalMinutes = new Integer(0);

		//Date dtAvai = dtArrival;
		Calendar calAvail = Calendar.getInstance();
		calAvail.setTime(dtArrival);

		Calendar calDeparture = Calendar.getInstance();
		calDeparture.setTime(dtDeparture);

		Integer iDepHour = calDeparture.get(Calendar.HOUR_OF_DAY);
		Integer iDepMinute = calDeparture.get(Calendar.MINUTE);

		Integer iArrHour = calAvail.get(Calendar.HOUR_OF_DAY);
		Integer iArrMinute = calAvail.get(Calendar.MINUTE);

		if (iDepHour == 0)iDepHour = 24;
		if (iArrHour == 0)
			iArrHour = 24;
		
		if ( iDepHour < iArrHour) {
		iDiffInMinutes =  (iArrHour * 60 + iArrMinute) -(iDepHour * 60 + iDepMinute);
		//iTotalMinutes = iTotalMinutes + iDiffInMinutes;
			
		} else {
			
			iDiffInMinutes = (iArrHour * 60 + iArrMinute) + 24 * 60- (iDepHour * 60 + iDepMinute) ;
		//	iTotalMinutes = iTotalMinutes + iDiffInMinutes;
			
		}
		//System.out.println("iDiffInMinutes -- " +iDiffInMinutes);
		return iDiffInMinutes;
	}

	public void viewRoundTrip(CrewLink d)
	{
		Long lTransactionId = new Long(0);
		lTransactionId = d.getTransactionNoCrewLink();
		try {
			listRoundTrip = (List<RoundTrip>) 
					schoolService.loadList("from RoundTrip as cc where cc.transactionNoCrewLink =" +lTransactionId);
				
		} catch (CrewException e) {
			e.printStackTrace();
			CREW_LOGGER.debug(e.getMessage());
		}
		
				
	}
	
	public void deleteCrewLiniMultipleSelection()
	{

		Long lTransactionId = new Long(0);
		
		System.out.println("The Selected Train Numbers are : ");
		for(CrewLink c : selectionList)
		{
			lTransactionId = c.getTransactionNoCrewLink();
			schoolService.deleteById(c.getId(), "CrewLink");
			removelist(c);
			
			try {
				List<RoundTrip> listRound = (List<RoundTrip>) 
						schoolService.loadList("from RoundTrip as c where c.transactionNoCrewLink=" + lTransactionId);
				for(RoundTrip r : listRound)
				{	r.setStatus(false);
					r.setTransactionNoCrewLink(String.valueOf((long)0));
					schoolService.updateObjectInDb(r);
					
				}
				nCountNumberOfLocoPilots = 0;
				nTotalRunningTime = 0;
				nTotalKM = 0;
				nAvgWorkingPerWeek = 0;
				nTotalHQRest = 0;
				nTotalOSRest = 0;
				calculateNumberOfLocoPilots();
				average_km_hour_PerMonth();
				calculateTotalHQRest();
				calculateTotalOSRest();
				calculateAvgWorkingHourPerWeek();
				
				
			} catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
			}
		
		}
	}
	
	
	public void DeleteCrewLink(CrewLink d)
	{
		
		Long lTransactionId = new Long(0);
		
		lTransactionId = d.getTransactionNoCrewLink();
		schoolService.deleteById(d.getId(), "CrewLink");
		removelist(d);
		
		try {
			List<RoundTrip> listRound = (List<RoundTrip>) 
					schoolService.loadList("from RoundTrip as c where c.transactionNoCrewLink=" + lTransactionId);
			for(RoundTrip r : listRound)
			{	r.setStatus(false);
				r.setTransactionNoCrewLink(String.valueOf((long)0));
				schoolService.updateObjectInDb(r);
				
			}
			nCountNumberOfLocoPilots = 0;
			nTotalRunningTime = 0;
			nTotalKM = 0;
			nAvgWorkingPerWeek = 0;
			nTotalHQRest = 0;
			nTotalOSRest = 0;
			calculateNumberOfLocoPilots();
			average_km_hour_PerMonth();
			calculateTotalHQRest();
			calculateTotalOSRest();
			calculateAvgWorkingHourPerWeek();
			
			
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
	
		
		
		/*average_km_hour_PerMonth();
		runningTime();*/
	}
	
	
	
	
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
	
	private void removelist(CrewLink crewlink)
	{for(int nIndex = 0;nIndex<listCrewLink.size();nIndex++)
		{	if(listCrewLink.get(nIndex).getId()==crewlink.getId())
			{	listCrewLink.remove(nIndex);	
			}
		}
		
	}
	public void onItemSelect(SelectEvent event) {
		selectedcrewBaseName = event.getObject().toString().trim();
		loadCrewType(selectedcrewBaseName);
	    }
	public void average_km_hour_PerMonth() {
	    
	    System.out.println("-----------------------------");
	    for(CrewLink c1 : listCrewLink)
		{
			nTotalKM = (int) (nTotalKM + c1.getDistance());
		}
	    
	    nTotalKMSub = nTotalKM; 
	 //   System.out.println("nTotalKM   ----------- " +nTotalKM);
	    if(nCountNumberOfLocoPilots!=0)
	    nTotalKM = nTotalKM / nCountNumberOfLocoPilots;
	   // System.out.println("nTotalKM   ----------- " +nTotalKM);
	 
	    nTotalKM = nTotalKM * 30;
	}
	
	 public void onResize(ColumnResizeEvent event) {
	        FacesMessage msg = new FacesMessage("Column ","ddddddddddddd");
	        FacesContext.getCurrentInstance().addMessage(null, msg);
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
		
	public void addedSummary()
	{
		
		try {
			drivingSectionList  = (List<DrivingDutyOfPilotDrivingSection>) this.schoolService.loadList("from DrivingDutyOfPilotDrivingSection") ;
			roundTripCalcuationList = (List<RoundTrip>) this.schoolService.loadList("from RoundTrip") ;
			
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
		
		if(!drivingSectionList.isEmpty())
		{
			int[] hQHourarray 		= 		new int[drivingSectionList.size()];
			int[] hQMinutesarray 	= 		new int[drivingSectionList.size()];
			int[] OsHourarray 		= 		new int[drivingSectionList.size()];
			int[] OsMinutesarray 	= 		new int[drivingSectionList.size()];
			int sumofHour = 0;
			int sumofMinutes = 0;
			
			
			for(int nIndex=0;nIndex<drivingSectionList.size();nIndex++)
			{ 
				if(!(drivingSectionList.get(nIndex).getOsRest()==null))
				{
				       String osRest =  drivingSectionList.get(nIndex).getOsRest();
				        String[] osRestSplit = osRest.split (Pattern.quote(":"));
						int Oshour = Integer.parseInt (osRestSplit[0].trim());
						int Osmin = Integer.parseInt  (osRestSplit[1].trim());
						OsHourarray[nIndex] = Oshour;
						OsMinutesarray [nIndex] =  Osmin;	
				      
				}
				if(!(drivingSectionList.get(nIndex).getHqRest()==null))
				{	    String HqRest =  drivingSectionList.get(nIndex).getHqRest();
					    String[] hqRestSplit = HqRest.split (Pattern.quote(":"));
						int HqResthour = Integer.parseInt (hqRestSplit[0].trim());
						int HqRestminutes = Integer.parseInt  (hqRestSplit[1].trim());
					
						hQHourarray[nIndex] = HqResthour;
						hQMinutesarray[nIndex] = HqRestminutes;
					
				}
				
			}
			
			 
			     for(int nIndex = 0;nIndex<hQHourarray.length;nIndex++)
			     {
			    	 sumofHour +=hQHourarray[nIndex];
			    	 sumofMinutes +=hQMinutesarray[nIndex];
			    	       if(sumofMinutes>60)
			    	       {   sumofHour =sumofHour+1;
			    	           sumofMinutes  = Math.abs((sumofMinutes - 60)); 
			    	       }
			    	 
			    	 
			    	 System.out.println("HQHour "+hQHourarray[nIndex]);
			    	 System.out.println("HQMinutes "+hQMinutesarray[nIndex]);
			     }
			     
			HqTotalHour = new StringBuffer(String.valueOf(sumofHour)).append(":").append(String.valueOf(sumofMinutes));
			System.out.println("Hours  = ="+sumofHour);
			System.out.println("Minutes  = ="+sumofMinutes);
			
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
	
	
	
	///  new Dialog Open
	
	public void showDialog(CrewLink crewlinkobject)
	{	
		ccdsChanges.clear();
		crewChangeDrivingsectionlist =  (List<CrewChangeDrivingsection>) context.getBean("list");
		//String arr [] = crewlinkobject.getTrainNo().split(",//+");
		String stringTrains= crewlinkobject.getTrainNo();
		
		StringTokenizer st = new StringTokenizer(stringTrains,",+,"); 
		String trains [] = new String[20];
		int nCount = 0;
		 while (st.hasMoreTokens())
		 {  
	          String allTrains = st.nextToken(); 
	          System.out.println(""+allTrains);
	          trains [nCount++] = allTrains; 
	          
		 }
		 for(int nIndex=0;nIndex<trains.length;nIndex++)
		 { if(trains[nIndex]!=null)
			 {
			 try {
					crewChangeDrivingsectionlist = (List<CrewChangeDrivingsection>) schoolService.loadList("from CrewChangeDrivingsection as ccd where ccd.trainNo like '"+trains[nIndex]+"' and ccd.crewBase like '"+crewlinkobject.getCrewBase()+"' and ccd.crewType like '"+crewlinkobject.getCrewType()+"' ");
					ccdsChanges.addAll(crewChangeDrivingsectionlist);
					System.out.println(""+crewChangeDrivingsectionlist.size());
				} catch (CrewException e) {
					CREW_LOGGER.debug(e.getMessage());
					e.printStackTrace();
				}
			 }
			 
		 }
		
	}
	
	
	
	
	
	
	public static float round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}
	
	
	
	

	
	// all getter and setters
	
	
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



	public List<CrewLink> getListCrewLink() {
		return listCrewLink;
	}



	public void setListCrewLink(List<CrewLink> listCrewLink) {
		this.listCrewLink = listCrewLink;
	}



	public List<RoundTrip> getListRoundTrip() {
		return listRoundTrip;
	}



	public void setListRoundTrip(List<RoundTrip> listRoundTrip) {
		this.listRoundTrip = listRoundTrip;
	}

	public long getLoginId() {
		return loginId;
	}

	public void setLoginId(long loginId) {
		this.loginId = loginId;
	}


	public List<DrivingDutyOfPilotDrivingSection> getDrivingSectionList() {
		return drivingSectionList;
	}


	public void setDrivingSectionList(
			List<DrivingDutyOfPilotDrivingSection> drivingSectionList) {
		this.drivingSectionList = drivingSectionList;
	}


	public List<RoundTrip> getRoundTripCalcuationList() {
		return roundTripCalcuationList;
	}


	public void setRoundTripCalcuationList(List<RoundTrip> roundTripCalcuationList) {
		this.roundTripCalcuationList = roundTripCalcuationList;
	}


	public StringBuffer getHqTotalHour() {
		return HqTotalHour;
	}


	public void setHqTotalHour(StringBuffer hqTotalHour) {
		HqTotalHour = hqTotalHour;
	}


	public List<CrewChangeDrivingsection> getSequenceDrivingSectionList() {
		return sequenceDrivingSectionList;
	}


	public void setSequenceDrivingSectionList(
			List<CrewChangeDrivingsection> sequenceDrivingSectionList) {
		this.sequenceDrivingSectionList = sequenceDrivingSectionList;
	}





	public List<CrewChangeDrivingsection> getCrewChangeDrivingsectionlist() {
		return crewChangeDrivingsectionlist;
	}


	public void setCrewChangeDrivingsectionlist(
			List<CrewChangeDrivingsection> crewChangeDrivingsectionlist) {
		this.crewChangeDrivingsectionlist = crewChangeDrivingsectionlist;
	}


	public List<CrewChangeDrivingsection> getCcdsChanges() {
		return ccdsChanges;
	}


	public void setCcdsChanges(List<CrewChangeDrivingsection> ccdsChanges) {
		this.ccdsChanges = ccdsChanges;
	}


	public Double getAverage_Kms_per_month() {
		return average_Kms_per_month;
	}


	public void setAverage_Kms_per_month(Double average_Kms_per_month) {
		this.average_Kms_per_month = average_Kms_per_month;
	}


	public float getAverage_WorkingHours_14Days() {
		return Average_WorkingHours_14Days;
	}


	public void setAverage_WorkingHours_14Days(float average_WorkingHours_14Days) {
		Average_WorkingHours_14Days = average_WorkingHours_14Days;
	}


	public int getNumber_of_Loco() {
		return number_of_Loco;
	}


	public void setNumber_of_Loco(int number_of_Loco) {
		this.number_of_Loco = number_of_Loco;
	}


	public float getAverage_workingHour() {
		return Average_workingHour;
	}


	public void setAverage_workingHour(float average_workingHour) {
		Average_workingHour = average_workingHour;
	}


	public float getAverageWorking_Per14DayHourPercentage() {
		return averageWorking_Per14DayHourPercentage;
	}


	public void setAverageWorking_Per14DayHourPercentage(
			float averageWorking_Per14DayHourPercentage) {
		this.averageWorking_Per14DayHourPercentage = averageWorking_Per14DayHourPercentage;
	}


	public float getAverageWorkingPer() {
		return averageWorkingPer;
	}


	public void setAverageWorkingPer(float averageWorkingPer) {
		this.averageWorkingPer = averageWorkingPer;
	}


	public long getTotalOsRestHours() {
		return totalOsRestHours;
	}


	public void setTotalOsRestHours(long totalOsRestHours) {
		this.totalOsRestHours = totalOsRestHours;
	}


	public float getTotalOsRestHours_Percentage() {
		return totalOsRestHours_Percentage;
	}


	public void setTotalOsRestHours_Percentage(float totalOsRestHours_Percentage) {
		this.totalOsRestHours_Percentage = totalOsRestHours_Percentage;
	}


	public long getTotalhqRestHours() {
		return totalhqRestHours;
	}


	public void setTotalhqRestHours(long totalhqRestHours) {
		this.totalhqRestHours = totalhqRestHours;
	}


	public float getTotalhqRestHours_Percentage() {
		return totalhqRestHours_Percentage;
	}


	public Double getRunning_Time() {
		return running_Time;
	}


	public void setRunning_Time(Double running_Time) {
		this.running_Time = running_Time;
	}


	public void setTotalhqRestHours_Percentage(float totalhqRestHours_Percentage) {
		this.totalhqRestHours_Percentage = totalhqRestHours_Percentage;
	}

	public Long getnTotalKMs() {
		return nTotalKMs;
	}

	public void setnTotalKMs(Long nTotalKMs) {
		this.nTotalKMs = nTotalKMs;
	}

	public Integer getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(Integer totalHours) {
		this.totalHours = totalHours;
	}

	public Integer getnRunningTime() {
		return nRunningTime;
	}

	public void setnRunningTime(Integer nRunningTime) {
		this.nRunningTime = nRunningTime;
	}

	public Double getdAvgKM() {
		return dAvgKM;
	}

	public void setdAvgKM(Double dAvgKM) {
		this.dAvgKM = dAvgKM;
	}

	public Integer getnTotalRunningTime() {
		return nTotalRunningTime;
	}

	public void setnTotalRunningTime(Integer nTotalRunningTime) {
		this.nTotalRunningTime = nTotalRunningTime;
	}

	public Integer getnCountNumberOfLocoPilots() {
		return nCountNumberOfLocoPilots;
	}

	public void setnCountNumberOfLocoPilots(Integer nCountNumberOfLocoPilots) {
		this.nCountNumberOfLocoPilots = nCountNumberOfLocoPilots;
	}

	public Integer getnTotalKM() {
		return nTotalKM;
	}

	public void setnTotalKM(Integer nTotalKM) {
		this.nTotalKM = nTotalKM;
	}

	public Integer getnAvgWorkingPerWeek() {
		return nAvgWorkingPerWeek;
	}

	public void setnAvgWorkingPerWeek(Integer nAvgWorkingPerWeek) {
		this.nAvgWorkingPerWeek = nAvgWorkingPerWeek;
	}

	public Integer getnTotalHQRest() {
		return nTotalHQRest;
	}

	public void setnTotalHQRest(Integer nTotalHQRest) {
		this.nTotalHQRest = nTotalHQRest;
	}

	public Integer getnTotalOSRest() {
		return nTotalOSRest;
	}

	public void setnTotalOSRest(Integer nTotalOSRest) {
		this.nTotalOSRest = nTotalOSRest;
	}
	public Integer getnTotalKMSub() {
		return nTotalKMSub;
	}
	public void setnTotalKMSub(Integer nTotalKMSub) {
		this.nTotalKMSub = nTotalKMSub;
	}

	public Double getnAvgWorkingPerWeekSub() {
		return nAvgWorkingPerWeekSub;
	}

	public void setnAvgWorkingPerWeekSub(Double nAvgWorkingPerWeekSub) {
		this.nAvgWorkingPerWeekSub = nAvgWorkingPerWeekSub;
	}

	public Double getnTotalHQRestSub() {
		return nTotalHQRestSub;
	}

	public void setnTotalHQRestSub(Double nTotalHQRestSub) {
		this.nTotalHQRestSub = nTotalHQRestSub;
	}

	public Double getnTotalOSRestSub() {
		return nTotalOSRestSub;
	}

	public void setnTotalOSRestSub(Double nTotalOSRestSub) {
		this.nTotalOSRestSub = nTotalOSRestSub;
	}

	public List<CrewLink> getSelectionList() {
		return selectionList;
	}

	public void setSelectionList(List<CrewLink> selectionList) {
		this.selectionList = selectionList;
	}

	public List<CrewHeadquarterMaster> getCrewBaseList() {
		return crewBaseList;
	}

	public void setCrewBaseList(List<CrewHeadquarterMaster> crewBaseList) {
		this.crewBaseList = crewBaseList;
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

	public Map<String, String> getCrewBaseMap() {
		return crewBaseMap;
	}

	public void setCrewBaseMap(Map<String, String> crewBaseMap) {
		this.crewBaseMap = crewBaseMap;
	}

	public String getSelectedCrewBaseType() {
		return selectedCrewBaseType;
	}

	public void setSelectedCrewBaseType(String selectedCrewBaseType) {
		this.selectedCrewBaseType = selectedCrewBaseType;
	}



	
}
