package com.crew.bean;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.model.SelectableDataModel;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.crew.exception.CrewException;
import com.crew.model.CrewChangeDrivingsection;
import com.crew.model.CrewHeadquarterMaster;
import com.crew.model.CrewOutstation;
import com.crew.model.DrivingDuty;
import com.crew.model.DrivingDutyOfPilotDrivingSection;
import com.crew.model.Onedrivingsection;
import com.crew.model.PilotJourney;
import com.crew.service.CrewService;



@ManagedBean(name="dSectionbean",eager=true)
@ViewScoped
@SuppressWarnings({"unchecked", "deprecation"})
public class adminDrivingduty extends ListDataModel<CrewChangeDrivingsection> implements SelectableDataModel<CrewChangeDrivingsection>, Serializable{

	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "schoolService", value = "#{schoolService}")
	private CrewService schoolService;
	private HttpSession httpSession = (HttpSession) FacesContext
			.getCurrentInstance().getExternalContext().getSession(false);
	private static final Logger CREW_LOGGER = Logger.getLogger(adminDrivingduty.class);
	
	private ApplicationContext context;
	private List<CrewChangeDrivingsection> crewList;
	private List<CrewChangeDrivingsection> drivingSectionList;
	private String selectedDrivingSection;
	 //private CrewChangeDrivingsection[] selectCrewChange;
	private List<CrewChangeDrivingsection>selectCrewChange;

	private List<CrewChangeDrivingsection>selectCrewChangeList;
	private List<CrewChangeDrivingsection>selectOneOneCrewList;
	 //private List<Onedrivingsection> drivinglist;
	 
	 private DrivingDuty drivingduty;
	 private Onedrivingsection ODS;
	 final String operatingSystem = System.getProperty("os.name");
	 private DrivingDutyOfPilotDrivingSection drivingDutyPilotDrivingSection;
	 private List<Long> ids = new ArrayList<Long>();
	 private String strId[] = new String[10];
	 String strTotalId = new String();
	public adminDrivingduty() {
	}
	
	@PostConstruct
	private void initilization()
	{
		
			createList();
		    loadCrewList();

		
	
	}
	
	
	private void loadCrewList() {
		
		
		
		try {
			crewList =  (List<CrewChangeDrivingsection>) schoolService.loadList(" from CrewChangeDrivingsection");
		    } catch (CrewException e) 
			{
		    	e.printStackTrace();
			}
	 
	}
	


	
	private void createList()
	{
	
		context = new ClassPathXmlApplicationContext("Beans.xml");
		setCrewList((List<CrewChangeDrivingsection>) context.getBean("list"));
		drivingSectionList = (List<CrewChangeDrivingsection>) context.getBean("list");
		selectCrewChangeList =  (List<CrewChangeDrivingsection>) context.getBean("list");
		selectOneOneCrewList =  (List<CrewChangeDrivingsection>) context.getBean("list");
		ODS = (Onedrivingsection) context.getBean("onedrivingsection");
		drivingduty =  (DrivingDuty) context.getBean("drivingduty");
		drivingDutyPilotDrivingSection = (DrivingDutyOfPilotDrivingSection) context
				.getBean("drivingdutyofpilotdrivingsection");
	}
	
	
	@Override
	public CrewChangeDrivingsection getRowData(String rowKey) {
		 drivingSectionList = (List<CrewChangeDrivingsection>) getWrappedData();
		
        for(CrewChangeDrivingsection CCD : drivingSectionList) {
            if(CCD.getTrainNo().equals(rowKey))
            {                return CCD;
            }
        }

        return null;
	}

	@Override
	public Object getRowKey(CrewChangeDrivingsection crewChange) {
		
		return crewChange.getTrainNo();
	}
	
	public void generateDrivingSection()
	{
		
		
		if(selectedDrivingSection.equals("DrivingSection+DrivingSection"))
				{
					RequestContext context = RequestContext.getCurrentInstance();
					context.execute("PF('drivinDutyWdg').show();");
					RequestContext.getCurrentInstance().update("drivingsection");
		
				}
		else if (selectedDrivingSection.equals("PilotJourney+DrivingSection"))
		{
			System.out.println("Inside the pilot journey plus driving section");
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('driPilotDlg').show();");
			RequestContext.getCurrentInstance().update("drivingsection");
		}
		else if(selectedDrivingSection.equals("DrivingSection+PilotJourney"))
		{
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('pilotDriDlg').show();");
			RequestContext.getCurrentInstance().update("drivingsection");
		}
		else if(selectedDrivingSection.equals("DrivingSection=DrivingSection"))
		{
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('oneToOneDrivinDutyWdg').show();");
			RequestContext.getCurrentInstance().update("onetoonedriid");
		}
			
			
		
	}
public void createPilotTable()
{
	RequestContext context = RequestContext.getCurrentInstance();
	context.execute("PF('pilotTableWdg').show();");
	RequestContext.getCurrentInstance().update("pilotableid");
}

	private Date setSignOn(Date departureTime)
	{	int nHour= departureTime.getHours();
		int nMinutes= departureTime.getMinutes();
		int nSecond= departureTime.getSeconds();
		
		int Todate = departureTime.getDate();
		int ToMonth = departureTime.getMonth();
		int ToYear = departureTime.getYear();
		nMinutes=nMinutes-30;
		Date signonTime = new Date(ToYear, ToMonth, Todate, nHour, nMinutes, nSecond);
	
		return signonTime;
	}
	private Date setSignOff(Date arrival)
	{
		
		int nHour= arrival.getHours();
		int nMinutes= arrival.getMinutes();
		int nSecond= arrival.getSeconds();
		
		int Todate = arrival.getDate();
		int ToMonth = arrival.getMonth();
		int ToYear = arrival.getYear();
		nMinutes=nMinutes+30;
		Date signOffTime = new Date(ToYear, ToMonth, Todate, nHour, nMinutes, nSecond);
		return signOffTime;
		}
	
	private Date calculateDutyTime(Date SignOnTime,Date SignOff)
	{
		int nsignOnHour= SignOnTime.getHours();
		int nsignOnMinutes= SignOnTime.getMinutes();
		int nsignOnSecond= SignOnTime.getSeconds();
		
		int nsignOndate = SignOnTime.getDate();
		int nsignOnMonth = SignOnTime.getMonth();
		int nsignOnYear = SignOnTime.getYear();
		
		
		
		int nsignoffHour= SignOff.getHours();
		int nsignoffMinutes= SignOff.getMinutes();
		int nsignoffSecond= SignOff.getSeconds();
		
		int nsignoffdate = SignOff.getDate();
		int nsignoffMonth = SignOff.getMonth();
		int nsignoffYear = SignOff.getYear();
		
		Date dutydate= new Date(nsignOnYear, nsignOnMonth, nsignOndate,(nsignoffHour-nsignOnHour),(nsignoffMinutes-nsignOnMinutes),(nsignoffSecond-nsignOnSecond));
	
		return dutydate;
		
		
		
	}
	
	

	private void consoleClear()
	{
		System.out.print(""+operatingSystem.toString());
		 if (operatingSystem .contains("Windows"))
		    {
		        try {
					//Runtime.getRuntime().exec("cls");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    else
		    {
		        try {
					//Runtime.getRuntime().exec("clear");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
	}
   


	private int calculateAvailabilityTime(Date dutyTime, Date SignOff)
	{
		//consoleClear();
		int availabilityTime = 0;
		
		  if(!(dutyTime==null && SignOff==null))
		  {
			     int nDHour= dutyTime.getHours();
			     int nSHour = SignOff.getHours();
			     
			      if(nDHour<8)
				  {    availabilityTime = nSHour+6;
				  	   return availabilityTime;
				  }
				  else
				  {	availabilityTime = nSHour+10;
				    return availabilityTime;
				  }
		  }
	    
		  return availabilityTime;
	}
	
	public void checkOutStation()
	{
		
	}
	public void checkBaseStation(String to_Station)
	{
		
		List<CrewHeadquarterMaster>headquarterMasterlist = null;
		try {
		      headquarterMasterlist  = (List<CrewHeadquarterMaster>) schoolService.loadList("from CrewHeadquarterMaster");
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
		
		String strStation = null;
	loop:for(CrewHeadquarterMaster CHM:headquarterMasterlist)
		{
			if(to_Station.equals(CHM.getCrewBase()))
					{
						strStation = to_Station;
						break loop;
					}
		}
		
	}
	public void sizeSelectCrewChangeList()
	{
		System.out.println("size of selectCrewChangeList is ->" +selectCrewChangeList.size());
	}
	
	/*public void oneOneDrivingDutyGeneration()
	{
		ids.clear();
		System.out.println("size of the selectOneOneCrewList--->>>>"+selectOneOneCrewList.size());
		Date dtArrival = null;
		Date dtDeparture = null;
		Date dtSignOn = null;
		Date dtSignOff = null;
		Date dtAvailability = null;
		boolean bOutStation = false;
		boolean bHeadQuarter = false;
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		
		String crewBase = null;
		String crewType = null;
		System.out.println("--------------------------------------------------------------");
		DrivingDutyOfPilotDrivingSection driDutyObject = new  DrivingDutyOfPilotDrivingSection();
		for(CrewChangeDrivingsection c:selectOneOneCrewList)
		{
			System.out.println("train no->>" +c.getTrainNo());
			System.out.println("getFromStation->>" +c.getFromStation());
			System.out.println("getToStation->>" +c.getToStation());
			System.out.println("getArrival->>" +c.getArrival());
			System.out.println("getDeparture->>" +c.getDeparture());
			System.out.println("getKilometer->>" +c.getKilometer());
			
			crewBase = c.getCrewBase();
			crewType = c.getCrewType();
			dtArrival = c.getArrival();
			dtDeparture = c.getDeparture();
			System.out.println("dtArrival->>" +dtArrival);
			System.out.println("dtDeparture->>" +dtDeparture);
			
			calendar1.setTime(dtDeparture);
			calendar2.setTime(dtArrival);

			calendar1.add(Calendar.MINUTE, -30);
			calendar2.add(Calendar.MINUTE, 30);
			
			dtSignOn = calendar1.getTime();
			dtSignOff = calendar2.getTime(); 
			
			System.out.println("dtSignOn->>" +dtSignOn);
			System.out.println("dtSignOff->>" +dtSignOff);
			
			
			calendar1.setTime(dtSignOn);
			calendar2.setTime(dtSignOff);
			
			int hAvailability= calendar1.get(Calendar.HOUR_OF_DAY);
			int mAvailability = calendar1.get(Calendar.MINUTE);

			int hDeparture = calendar2.get(Calendar.HOUR_OF_DAY);
			int mDeparture = calendar2.get(Calendar.MINUTE);
			
			int nTotalMinutes = 0;
			long diffFirstSignOnHour = 0;
			if(hAvailability < hDeparture)
			{
				if(mDeparture < mAvailability)
				{
					diffFirstSignOnHour = (hDeparture - hAvailability)-1;
					nTotalMinutes = java.lang.Math.abs(mDeparture - mAvailability);
					System.out.println("nTotalMinutes-->>>>" +nTotalMinutes);
				}
				else{
					diffFirstSignOnHour = hDeparture - hAvailability;
					nTotalMinutes =  java.lang.Math.abs(mAvailability - mDeparture);
					System.out.println("nTotalMinutes-->>>>" +nTotalMinutes);
				}
			}
			else
			{
				//diffFirstSignOnHour = 24-hAvailability;
				hAvailability = hAvailability * 60 + mAvailability;
				hAvailability = 24*60-hAvailability;
				System.out.println("hAvailability by diff from 24--->>> "+hAvailability);
				
				hDeparture = hDeparture * 60 + mDeparture;
				System.out.println("hDeparture --->>> "+hDeparture);
				diffFirstSignOnHour = hAvailability + hDeparture;
				diffFirstSignOnHour = diffFirstSignOnHour/60;
				hAvailability = 24 - hAvailability;
				hDeparture = hDeparture * 60 + mDeparture;
				System.out.println("hAvailability --->>> "+hAvailability);
				hAvailability = hAvailability * 60;
				System.out.println("hAvailability in minutes --->>> "+hAvailability);
				hAvailability = hAvailability - mAvailability;
				System.out.println("hAvailability after adding minutes--->>> "+hAvailability);
				diffFirstSignOnHour = hAvailability + hDeparture;
				System.out.println("diffFirstSignOnHour-->>>>" +diffFirstSignOnHour);
				nTotalMinutes  =(int)  java.lang.Math.abs(diffFirstSignOnHour % 60);
				System.out.println("nTotalMinutes-->>>>" +nTotalMinutes);
				diffFirstSignOnHour = diffFirstSignOnHour /60;
				
			}
			long finalHour = 0;
			System.out.println("diffFirstSignOnHour-->>>>" +diffFirstSignOnHour);
		//	System.out.println("nTotalMinutes-->>>>" +nTotalMinutes);
			System.out.println("diffFirstSignOnHour-->>>>" +diffFirstSignOnHour);
			diffFirstSignOnHour = diffFirstSignOnHour * 60;
			System.out.println("diffFirstSignOnHour in minutes-->>>>" +diffFirstSignOnHour);
			diffFirstSignOnHour = diffFirstSignOnHour + (mAvailability - mDeparture);
			System.out.println("diffFirstSignOnHour in second -->>>>" +diffFirstSignOnHour);
			finalHour = diffFirstSignOnHour / 60;
			System.out.println("diffFirstSignOnHour in hour -->>>>" +diffFirstSignOnHour);
			
			
			int rem  = (int) (diffFirstSignOnHour % 60);
			System.out.println("diffFirstSignOnHour in remainder -->>>>" +rem);
			
			
			String strHour = String.valueOf(diffFirstSignOnHour);
			String strMinutes = String.valueOf(nTotalMinutes);
			
			String strHourAndMinutesConcatenated = strHour.concat("."+strMinutes);
			
		
			
			System.out.println("finalHour-->>>> " +finalHour);
			System.out.println("strMinutes-->>>> " +strMinutes);
			
			System.out.println("strHourAndMinutesConcatenated-->>>> " +strHourAndMinutesConcatenated);
			System.out.println("strMinutes-->>>> " +strMinutes);
			
			//System.out.println("--------------------------------------------------------------");
			
			
			
			try {
				List<CrewHeadquarterMaster> listHQ = (List<CrewHeadquarterMaster>)schoolService.loadList("from CrewHeadquarterMaster");
				List<CrewOutstation> listOS = (List<CrewOutstation>)schoolService.loadList("from CrewOutstation");
				
				for(CrewOutstation c1: listOS)
				{
					if(c.getToStation().equalsIgnoreCase(c1.getStationCode()))
					{
						bOutStation = true;
					}
				}
				for(CrewHeadquarterMaster c2: listHQ)
				{
					if(c.getToStation().equalsIgnoreCase(c2.getCrewBase()))
					{
						bHeadQuarter = true;
					}
				}
			} catch (CrewException e2) {
				e2.printStackTrace();
			}
			
			System.out.println("bOutStation" +bOutStation+"\t" +"bHeadQuarter" +bHeadQuarter);
			
			
			if(bHeadQuarter && bOutStation)
			{
				if(finalHour>=8)
				{
					calendar1.setTime(dtSignOff);
					calendar1.add(Calendar.MINUTE, 960);
					dtAvailability=calendar1.getTime();
					System.out.println("dtSignOff" +dtSignOff);
					System.out.println("dtAvailability" +dtAvailability);
					
					calendar2.setTime(availabilityDate);
					miliSecondForDate1 = calendar1.getTimeInMillis();
					miliSecondForDate2 = calendar2.getTimeInMillis();
					diffInMilis = miliSecondForDate2 - miliSecondForDate1;
					availabilityHours = diffInMilis / (60 * 1000);
					System.out.println("availabilityHours"+availabilityHours);
					
				}
				else if(finalHour<8)
				{
					calendar1.setTime(dtSignOff);
					calendar1.add(Calendar.MINUTE, 360);
					dtAvailability=calendar1.getTime();
					System.out.println("signOffTime" +dtSignOff);
					System.out.println("availabilityDate" +dtAvailability);
					
					calendar1.setTime(dtSignOff);

					calendar1.add(Calendar.MINUTE, 720);
					dtAvailability=calendar1.getTime();
					System.out.println("dtSignOff" +dtSignOff);
					System.out.println("dtAvailability" +dtAvailability);
					
				}
			}
			else if(bHeadQuarter)
			{
				if(finalHour>=8)
				{
					calendar1.setTime(signOffTime);

					calendar1.add(Calendar.MINUTE, 960);
					availabilityDate=calendar1.getTime();
					System.out.println("signOffTime" +signOffTime);
					System.out.println("availabilityDate" +availabilityDate);
					
					calendar2.setTime(availabilityDate);
					
					miliSecondForDate1 = calendar1.getTimeInMillis();
					miliSecondForDate2 = calendar2.getTimeInMillis();
					
					diffInMilis = miliSecondForDate2 - miliSecondForDate1;
					availabilityHours = diffInMilis / (60 * 1000);
					
					System.out.println("availabilityHours"+availabilityHours);
					calendar1.setTime(dtSignOff);

					calendar1.add(Calendar.MINUTE, 960);
					dtAvailability=calendar1.getTime();
					System.out.println("dtSignOff" +dtSignOff);
					System.out.println("dtAvailability" +dtAvailability);
				}
				else if(finalHour<8)
				{
					calendar1.setTime(signOffTime);

					calendar1.add(Calendar.MINUTE, 720);
					availabilityDate=calendar1.getTime();
					System.out.println("signOffTime" +signOffTime);
					System.out.println("availabilityDate" +availabilityDate);
					
					calendar1.setTime(dtSignOff);

					calendar1.add(Calendar.MINUTE, 320);
					dtAvailability=calendar1.getTime();
					System.out.println("dtSignOff" +dtSignOff);
					System.out.println("dtAvailability" +dtAvailability);
					
				}
				else if(finalHour<5)
				{
					calendar1.setTime(signOffTime);

					calendar1.add(Calendar.MINUTE, 720);
					availabilityDate=calendar1.getTime();
					System.out.println("signOffTime" +signOffTime);
					System.out.println("availabilityDate" +availabilityDate);
					
					calendar1.setTime(dtSignOff);

					calendar1.add(Calendar.MINUTE, 120);
					dtAvailability=calendar1.getTime();
					System.out.println("dtSignOff" +dtSignOff);
					System.out.println("dtAvailability" +dtAvailability);
					
				}
			}
			else if(bOutStation)
			{
				if(finalHour>8)
				{
					
					calendar1.setTime(signOffTime);

					calendar1.add(Calendar.MINUTE, 480);
					availabilityDate=calendar1.getTime();
					System.out.println("signOffTime" +signOffTime);
					System.out.println("availabilityDate" +availabilityDate);
					
					
					calendar1.setTime(dtSignOff);

					calendar1.add(Calendar.MINUTE, 480);
					dtAvailability=calendar1.getTime();
					System.out.println("dtSignOff" +dtSignOff);
					System.out.println("dtAvailability" +dtAvailability);
					
					
				}
				else if(finalHour<8 && finalHour>5)
				{
					calendar1.setTime(signOffTime);

					calendar1.add(Calendar.MINUTE, 360);
					availabilityDate=calendar1.getTime();
					System.out.println("signOffTime" +signOffTime);
					System.out.println("availabilityDate" +availabilityDate);
					
					
					calendar1.setTime(dtSignOff);

					calendar1.add(Calendar.MINUTE, 480);
					dtAvailability=calendar1.getTime();
					System.out.println("dtSignOff" +dtSignOff);
					System.out.println("dtAvailability" +dtAvailability);
				}
				else if(finalHour==5)
				{
					calendar1.setTime(signOffTime);

					calendar1.add(Calendar.MINUTE,240);
					availabilityDate=calendar1.getTime();
					System.out.println("signOffTime" +signOffTime);
					System.out.println("availabilityDate" +availabilityDate);
					calendar1.setTime(dtSignOff);

					calendar1.add(Calendar.MINUTE, 300);
					dtAvailability=calendar1.getTime();
					System.out.println("dtSignOff" +dtSignOff);
					System.out.println("dtAvailability" +dtAvailability);
				}
				else if(finalHour<5)
				{
					calendar1.setTime(signOffTime);

					calendar1.add(Calendar.MINUTE, 720);
					availabilityDate=calendar1.getTime();
					System.out.println("signOffTime" +signOffTime);
					System.out.println("availabilityDate" +availabilityDate);
					
					calendar1.setTime(dtSignOff);

					calendar1.add(Calendar.MINUTE, 240);
					dtAvailability=calendar1.getTime();
					System.out.println("dtSignOff" +dtSignOff);
					System.out.println("dtAvailability" +dtAvailability);
					
				}
			}
			else if(!bOutStation && !bHeadQuarter)
			{
				
				
				
				if(finalHour>8)
				{
					
					calendar1.setTime(signOffTime);

					calendar1.add(Calendar.MINUTE, 480);
					availabilityDate=calendar1.getTime();
					System.out.println("signOffTime" +signOffTime);
					System.out.println("availabilityDate" +availabilityDate);
					
					
					calendar1.setTime(dtSignOff);

					calendar1.add(Calendar.MINUTE, 480);
					dtAvailability=calendar1.getTime();
					System.out.println("dtSignOff" +dtSignOff);
					System.out.println("dtAvailability" +dtAvailability);
					
					
				}
				else if(finalHour<8 && finalHour>5)
				{
					calendar1.setTime(signOffTime);

					calendar1.add(Calendar.MINUTE, 360);
					availabilityDate=calendar1.getTime();
					System.out.println("signOffTime" +signOffTime);
					System.out.println("availabilityDate" +availabilityDate);
					
					
					calendar1.setTime(dtSignOff);

					calendar1.add(Calendar.MINUTE, 480);
					dtAvailability=calendar1.getTime();
					System.out.println("dtSignOff" +dtSignOff);
					System.out.println("dtAvailability" +dtAvailability);
				}
				else if(finalHour<=5)
				{
					calendar1.setTime(signOffTime);

					calendar1.add(Calendar.MINUTE,240);
					availabilityDate=calendar1.getTime();
					System.out.println("signOffTime" +signOffTime);
					System.out.println("availabilityDate" +availabilityDate);
					
					
					
					calendar1.setTime(dtSignOff);

					calendar1.add(Calendar.MINUTE, 240);
					dtAvailability=calendar1.getTime();
					System.out.println("dtSignOff" +dtSignOff);
					System.out.println("dtAvailability" +dtAvailability);
				}
				else if(finalHour<5)
				{
					calendar1.setTime(signOffTime);

					calendar1.add(Calendar.MINUTE, 720);
					availabilityDate=calendar1.getTime();
					System.out.println("signOffTime" +signOffTime);
					System.out.println("availabilityDate" +availabilityDate);
					
					calendar1.setTime(dtSignOff);

					calendar1.add(Calendar.MINUTE, 360);
					dtAvailability=calendar1.getTime();
					System.out.println("dtSignOff" +dtSignOff);
					System.out.println("dtAvailability" +dtAvailability);
					
				}
				
				
			}
			for(CrewChangeDrivingsection c2 : selectCrewChangeList)
			{
				ids.add(c2.getId());
			}
			getIds1();
			ids.clear();
			
			driDutyObject.setTrainNo(c.getTrainNo());
			driDutyObject.setFromStation(c.getFromStation());
			driDutyObject.setToStation(c.getToStation());
			driDutyObject.setArrival(c.getArrival());
			driDutyObject.setDeparture(c.getDeparture());
			driDutyObject.setKm(c.getKilometer());
			driDutyObject.setSignonTime(dtSignOn);
			driDutyObject.setSignoffTime(dtSignOff);
			driDutyObject.setAvailability(dtAvailability);
			driDutyObject.setTotalDutyHours(strHourAndMinutesConcatenated);
			driDutyObject.setDeletedId1(c.getId().toString());
			driDutyObject.setCrewBase(crewBase);
			driDutyObject.setCrewType(crewType);
			
			try {
				schoolService.saveInDb(driDutyObject);
				System.out.println("saved successfully!!!!!!!");
				
				schoolService.updateCrewChangeTable(c.getId(), true);
				//
			} catch (CrewException e) {
				e.printStackTrace();
			} 
			}
			
			
			
			
			
		
			System.out.println("--------------------------------------------------------------");
		
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("drivingduty.faces");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		}*/
		
	
		
	
	
/*	public String selectedDrivingSection() 
	{
		
		String trainNo1 = null;	
		String trainNo[] = new String[10];
		String trainNo3 = null;
		
		String fromStationFinal = null;
		String toStationFinal = null;
		Date departFinal = null;
		Date arrivalFinal = null;
		
		
		String fromStations[] = new String[10];
		int count = 0;
		String toStationCheck[] = new String[10];
		Date depart[] = new Date[10];
		Date arrival[] = new Date[10];
		long distanceFirst[] = new long[10];
		long totalDistances = 0;
		Date signOnTime = null;
		Date signOffTime = null;
		long dutyHours = 0;
		Date availabilityDate = null;
		long availabilityHours = 0;
		boolean bOutStation = false;
		boolean bHeadQuarter = false;

		
		
		long diffInSecond;
		long diffInMinute;
		long diffInHour;
		long diffInDays;
		
		String fromStationChecking = null;
		String toStationChecking = null;
		 
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		Boolean bStatusFlag = true;
		Iterator i = selectCrewChangeList.iterator();
		try {
			List<CrewChangeDrivingsection> statusCrewChangeList = (List<CrewChangeDrivingsection>)
					schoolService.loadList("from CrewChangeDrivingsection as cc where cc.status="+bStatusFlag);
			
			System.out.println("statusCrewChangeList size is " + statusCrewChangeList.size());
		} catch (CrewException e2) {
			e2.printStackTrace();
		}
		
		
	for(CrewChangeDrivingsection c : selectCrewChangeList)
	{
		ids.add(c.getId());
	}
		
		
		
		while (i.hasNext()) {
			CrewChangeDrivingsection p = (CrewChangeDrivingsection) i.next();
			trainNo1 = p.getTrainNo();
			fromStations[count] = p.getFromStation();
			toStationCheck[count] = p.getToStation();
			trainNo[count]= p.getTrainNo();
			depart[count] = p.getDeparture();
			arrival[count] = p.getArrival();
			distanceFirst[count] = p.getKilometer();
			count++;
		}
		
		System.out.println("trainNo1" +trainNo1);
		
		
		for(int i1 =0; i1<count;i1++)
		{
			System.out.println("fromStations[count]" +fromStations[i1]);
			System.out.println("toStationCheck[count]" +toStationCheck[i1]);
			
			System.out.println("depart[count]" +depart[i1]);
			System.out.println("arrival[count]" +arrival[i1]);
			
			System.out.println("distanceFirst[count]" +distanceFirst[i1]);
			
			
			
			totalDistances = totalDistances + distanceFirst[i1];
		}
		trainNo3 = trainNo[count-2].concat("+" +trainNo[count-1]);
		System.out.println("trainNo3-->" +trainNo3);
		System.out.println("----------------------------------------");
		if(count>1){
			if(fromStations[1].equalsIgnoreCase(toStationCheck[0]))
			{
				
				fromStationFinal = fromStations[0];
				toStationFinal = toStationCheck[1];
				departFinal = depart[0];
				arrivalFinal = arrival[1];
				
				System.out.println("fromStationFinal" +fromStationFinal);
				System.out.println("toStationFinal" +toStationFinal);
				System.out.println("departFinal" +departFinal);
				System.out.println("arrivalFinal" +arrivalFinal);
				System.out.println("totalDistances"+totalDistances);
				
				
				

				calendar1.setTime(departFinal);
				calendar2.setTime(arrivalFinal);

				calendar1.add(Calendar.MINUTE, -30);
				calendar2.add(Calendar.MINUTE, 30);
				
				signOnTime = calendar1.getTime();
				signOffTime = calendar2.getTime(); 
				
				calendar1.setTime(signOnTime);
				calendar2.setTime(signOffTime);
				
				int hAvailability= calendar1.get(Calendar.HOUR_OF_DAY);
				int mAvailability = calendar1.get(Calendar.MINUTE);

				int hDeparture = calendar2.get(Calendar.HOUR_OF_DAY);
				int mDeparture = calendar2.get(Calendar.MINUTE);
				
				System.out.println("mAvailability-->>>>" +mAvailability);
				System.out.println("mDeparture-->>>>" +mDeparture);
				System.out.println("hAvailability->" +hAvailability);
				System.out.println("hDeparture->" +hDeparture);
				int nTotalMinutes = 0;
				long diffFirstSignOnHour = 0;
				
				
				if(hAvailability < hDeparture)
				{
					if(mDeparture < mAvailability)
					{
						diffFirstSignOnHour = (hDeparture - hAvailability)-1;
						nTotalMinutes = java.lang.Math.abs(mDeparture - mAvailability);
						System.out.println("nTotalMinutes in less then mdeparture ->>>"+nTotalMinutes);
					}
					else
					{
						diffFirstSignOnHour = hDeparture - hAvailability;
						nTotalMinutes = mDeparture - mAvailability;
						System.out.println("nTotalMinutes in greater then mdeparture ->>>"+nTotalMinutes);
					}
				}
				else
				{
					//System.out.println("nTotalMinutes in greater then mdeparture ->>>"+nTotalMinutes);
					diffFirstSignOnHour = 24-hAvailability;
					diffFirstSignOnHour = diffFirstSignOnHour  + hDeparture;
				}
				
				
				
				long finalHour = 0;
				
				
				//System.out.println("diffFirstSignOnHour-->>>>" +diffFirstSignOnHour);
				diffFirstSignOnHour = diffFirstSignOnHour * 60;
				//System.out.println("diffFirstSignOnHour in minutes-->>>>" +diffFirstSignOnHour);
				diffFirstSignOnHour = diffFirstSignOnHour + mAvailability + mDeparture;
				//System.out.println("diffFirstSignOnHour in second -->>>>" +diffFirstSignOnHour);
				finalHour = diffFirstSignOnHour / 60;
				System.out.println("diffFirstSignOnHour in hour -->>>>" +diffFirstSignOnHour);
				
				
				int rem  = (int) (diffFirstSignOnHour % 60);
				//System.out.println("diffFirstSignOnHour in remainder -->>>>" +rem);
				
				
				String strHour = String.valueOf(diffFirstSignOnHour);
				String strMinutes = String.valueOf(nTotalMinutes);
				
				String strHourAndMinutesConcatenated = strHour.concat("."+strMinutes);
				
				System.out.println("strHourAndMinutesConcatenated-->>>> " +strHourAndMinutesConcatenated);
				System.out.println("strMinutes-->>>> " +strMinutes);
				
				System.out.println("----------------------------------------------------------------");

				long miliSecondForDate1 = calendar1.getTimeInMillis();
				long miliSecondForDate2 = calendar2.getTimeInMillis();

				long diffInMilis = miliSecondForDate2 - miliSecondForDate1;

				diffInSecond = diffInMilis / 1000;
				dutyHours = diffInMilis / (60 * 1000);
				diffInHour = diffInMilis / (60 * 60 * 1000);
				diffInDays = diffInMilis / (24 * 60 * 60 * 1000);
				
				
				List<CrewHeadquarterMaster> listHQ=null;
				try {
					listHQ = (List<CrewHeadquarterMaster>)schoolService.loadList("from CrewHeadquarterMaster");
				} catch (CrewException e) {
					e.printStackTrace();
				}
				List<CrewOutstation> listOS=null;
				try {
					listOS = (List<CrewOutstation>)schoolService.loadList("from CrewOutstation");
				} catch (CrewException e) {
					e.printStackTrace();
				}
				
				for(CrewOutstation c: listOS)
				{
					if(toStationFinal.equalsIgnoreCase(c.getStationCode()))
					{
						bOutStation = true;
					}
				}
				for(CrewHeadquarterMaster c: listHQ)
				{
					if(toStationFinal.equalsIgnoreCase(c.getCrewBase()))
					{
						bHeadQuarter = true;
					}
				}
			if(bHeadQuarter && bOutStation)
			{
				if(finalHour>8)
				{
					calendar1.setTime(signOffTime);

					calendar1.add(Calendar.MINUTE, 960);
					availabilityDate=calendar1.getTime();
					System.out.println("signOffTime" +signOffTime);
					System.out.println("availabilityDate" +availabilityDate);
					
					calendar2.setTime(availabilityDate);
					
					miliSecondForDate1 = calendar1.getTimeInMillis();
					miliSecondForDate2 = calendar2.getTimeInMillis();
					
					diffInMilis = miliSecondForDate2 - miliSecondForDate1;
					availabilityHours = diffInMilis / (60 * 1000);
					
					System.out.println("availabilityHours"+availabilityHours);
					
				}
				else if(finalHour<8)
				{
					calendar1.setTime(signOffTime);

					calendar1.add(Calendar.MINUTE, 720);
					availabilityDate=calendar1.getTime();
					System.out.println("signOffTime" +signOffTime);
					System.out.println("availabilityDate" +availabilityDate);
					
				}
			}
			else if(bHeadQuarter)
			{
				if(finalHour>8)
				{
					calendar1.setTime(signOffTime);

					calendar1.add(Calendar.MINUTE, 960);
					availabilityDate=calendar1.getTime();
					System.out.println("signOffTime" +signOffTime);
					System.out.println("availabilityDate" +availabilityDate);
					
					calendar2.setTime(availabilityDate);
					
					miliSecondForDate1 = calendar1.getTimeInMillis();
					miliSecondForDate2 = calendar2.getTimeInMillis();
					
					diffInMilis = miliSecondForDate2 - miliSecondForDate1;
					availabilityHours = diffInMilis / (60 * 1000);
					
					System.out.println("availabilityHours"+availabilityHours);
					
				}
				else if(finalHour<8)
				{
					calendar1.setTime(signOffTime);

					calendar1.add(Calendar.MINUTE, 720);
					availabilityDate=calendar1.getTime();
					System.out.println("signOffTime" +signOffTime);
					System.out.println("availabilityDate" +availabilityDate);
					
				}
			}
			else if(bOutStation)
			{
				if(finalHour>8)
				{
					
					calendar1.setTime(signOffTime);

					calendar1.add(Calendar.MINUTE, 480);
					availabilityDate=calendar1.getTime();
					System.out.println("signOffTime" +signOffTime);
					System.out.println("availabilityDate" +availabilityDate);
					
					calendar2.setTime(availabilityDate);
					
					miliSecondForDate1 = calendar1.getTimeInMillis();
					miliSecondForDate2 = calendar2.getTimeInMillis();
					
					diffInMilis = miliSecondForDate2 - miliSecondForDate1;
					availabilityHours = diffInMilis / (60 * 1000);
					
					System.out.println("availabilityHours"+availabilityHours);
					
				}
				else if(finalHour<8 && finalHour>5)
				{
					calendar1.setTime(signOffTime);

					calendar1.add(Calendar.MINUTE, 360);
					availabilityDate=calendar1.getTime();
					System.out.println("signOffTime" +signOffTime);
					System.out.println("availabilityDate" +availabilityDate);
					
				}
				else if(finalHour<=5)
				{
					calendar1.setTime(signOffTime);

					calendar1.add(Calendar.MINUTE,240);
					availabilityDate=calendar1.getTime();
					System.out.println("signOffTime" +signOffTime);
					System.out.println("availabilityDate" +availabilityDate);
					
				}
			}
			else if(!bOutStation && !bHeadQuarter)
			{
				
				
				
				calendar1.setTime(signOffTime);

				calendar1.add(Calendar.MINUTE, 960);
				availabilityDate=calendar1.getTime();
				System.out.println("signOffTime" +signOffTime);
				System.out.println("availabilityDate" +availabilityDate);

				
				
			}
			
			List<DrivingDutyOfPilotDrivingSection> listCheck = null;
			try {
				listCheck = (List<DrivingDutyOfPilotDrivingSection>) schoolService
						.loadList("from DrivingDutyOfPilotDrivingSection");
			} catch (CrewException e1) {
				e1.printStackTrace();
			}
			
			
			boolean bb = false;
			//FacesContext.getCurrentInstance().getExternalContext().redirect("drivingduty.faces");
			for (DrivingDutyOfPilotDrivingSection d : listCheck) {
				fromStationChecking = d.getFromStation();
				toStationChecking = d.getToStation();
				
				
				if(fromStationChecking.equalsIgnoreCase(fromStationFinal) && toStationChecking.equalsIgnoreCase(toStationFinal))
				{
					bb=true;
				}
			}
			if(bb)
			{
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"Already Exist In DB! You Cannot Create Duplicate", null));
			}
			else
			{
				
				getIds1();
				ids.clear();
				drivingDutyPilotDrivingSection.setTrainNo(trainNo1);
				drivingDutyPilotDrivingSection.setFromStation(fromStationFinal);
				drivingDutyPilotDrivingSection.setToStation(toStationFinal);
				drivingDutyPilotDrivingSection.setDeparture(departFinal);
				drivingDutyPilotDrivingSection.setArrival(arrivalFinal);
				drivingDutyPilotDrivingSection.setKm(totalDistances);
				drivingDutyPilotDrivingSection.setSignoffTime(signOffTime);
				drivingDutyPilotDrivingSection.setSignonTime(signOnTime);
				//drivingDutyPilotDrivingSection.setDutyHours(diffInHour);
				drivingDutyPilotDrivingSection.setAvailability(availabilityDate);
				drivingDutyPilotDrivingSection.setTotalDutyHours(strHourAndMinutesConcatenated);
				drivingDutyPilotDrivingSection.setDeletedId1(strId[2-2]);
				drivingDutyPilotDrivingSection.setDeletedId2(strId[2-1]);
				try {
					schoolService.saveInDb(drivingDutyPilotDrivingSection);
					
				//	System.out.println("combined ids is " +strTotalId);
					System.out.println("saved successfully!!!!!!!!!!!");
					FacesContext.getCurrentInstance().getExternalContext().redirect("drivingduty.faces");
				
				} catch (CrewException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
			} 
			else
			{
				System.out.println("Both are not equal");
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"From and To stations should be equal", null));
			}
				
			}
		
		
		String arrivatTrain = null;
		String departureTrain = null;
		String To_Station = null;
		String From_Station = null;
		Date arrivalTime;
		Date departureTime;
		
		
		
		
		 if(!checkForDuplicateTrains((selectCrewChangeList.get(0).getToStation().trim()),(selectCrewChangeList.get(1).getFromStation().trim()),selectCrewChangeList))// check for duplicate 
		 {if(selectCrewChangeList.size()>1) // check for selection 
		 	{if(selectCrewChangeList.get(0).getFromStation().equals(selectCrewChangeList.get(1).getToStation())) // check for Two different stations
				{
					 RequestContext context = RequestContext.getCurrentInstance();
				     context.execute("PF('DrivingDutyDlg').show();");
					 RequestContext.getCurrentInstance().update("crewDlgid");
					 
							 arrivatTrain = selectCrewChangeList.get(0).getTrainNo();
							 departureTrain= selectCrewChangeList.get(1).getTrainNo();
							 To_Station = selectCrewChangeList.get(0).getToStation();
							 From_Station = selectCrewChangeList.get(1).getFromStation();
							 arrivalTime= selectCrewChangeList.get(0).getArrival();
							 departureTime= selectCrewChangeList.get(1).getDeparture();
					 
							
							 StringBuffer strbuffer = new StringBuffer();
							 strbuffer.append(arrivatTrain+"+"+departureTrain);
							 
							 drivingduty.setTrainNo(strbuffer.toString());
							 drivingduty.setArrivalTime(arrivalTime);
							 drivingduty.setDistance(selectCrewChangeList.get(0).getKilometer()+selectCrewChangeList.get(1).getKilometer());
							 drivingduty.setDepartureTime(departureTime);
							 drivingduty.setToStation(To_Station);
							 drivingduty.setFromStation(From_Station);
							 
							 checkBaseStation(To_Station);
							 
							 ODS.setTrainNo(strbuffer.toString());
							 ODS.setArrivalTime(arrivalTime);
							 ODS.setDepartureTime(departureTime);
							 ODS.setToStation(To_Station);
							 ODS.setFromStation(From_Station);
							 ODS.setSignOn(setSignOn(departureTime)); // signOnTime Calculation
							 ODS.setSignOff(setSignOff(arrivalTime)); // signOnTime Calculation
							 Date dutyHour =  calculateDutyTime(setSignOn(departureTime),setSignOff(arrivalTime));
							 ODS.setAvailabilityTime(calculateAvailabilityTime(dutyHour,setSignOff(arrivalTime)));
							 ODS.setDutyHour(dutyHour);// calculate dutytime
							 ODS.setDistance(selectCrewChangeList.get(0).getKilometer()+selectCrewChangeList.get(1).getKilometer());
							
					
					 try {
						 schoolService.saveInDb(ODS);
						 RequestContext.getCurrentInstance().update("drivingDuty");
						 FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"Driving Duty Create Successfully!", "Driving Duty Create Successfully!"));
						 
					} catch (Exception e) {
						e.printStackTrace();
					}
					 
					 
					 
				}
				else
				{
					 RequestContext.getCurrentInstance().update("viewGrowl");
				     FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Wrong Selected Station!", "Wrong Selection"));	
				}
				
			}
		 }
		
		
		
		//return "drivingduty.faces?faces-redirect=true";
		 return null;
		}*/
	public void getIds1()
	{
		int count = 0;
		
		
				
		for(Long l : ids)
		{
			System.out.println("ids-->>>" +l);
			strId[count] = l.toString();
			schoolService.updateCrewChangeTable(l, true);
			count++;
		}
		
		strTotalId= strId[count-2].concat("+"+strId[count-1]);
		
		
		
	}
		
		  		
	@SuppressWarnings("unused")
	private boolean checkForDuplicateTrains(String toStation, String fromStation,List<CrewChangeDrivingsection>selectCrewChangeList) {

		boolean classExist = false;
		
		List<Onedrivingsection> onedrivingList = null;
		try {
		onedrivingList = (List<Onedrivingsection>) this.schoolService.loadList("from Onedrivingsection");
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}

		loop: for (Onedrivingsection ods : onedrivingList) {

			if (toStation.equalsIgnoreCase(ods.getToStation().trim()) && (fromStation.equalsIgnoreCase(ods.getFromStation())))
					{
				classExist = true;
				
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(
										FacesMessage.SEVERITY_FATAL,
										selectCrewChangeList.get(0).getTrainNo()+" and "+selectCrewChangeList.get(1).getTrainNo()
												+ " is Already Exists ! Cannot create a duplicate.",
										null));
				RequestContext.getCurrentInstance().update("viewGrowl");

				break loop;
			}
		}

		return classExist;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//// all the getter and setter 
	

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

	public static Logger getCrewLogger() {
		return CREW_LOGGER;
	}



	public List<CrewChangeDrivingsection> getCrewList() {
		return crewList;
	}



	public void setCrewList(List<CrewChangeDrivingsection> crewList) {
		this.crewList = crewList;
	}

	public List<CrewChangeDrivingsection> getDrivingSectionList() {
		return drivingSectionList;
	}

	public void setDrivingSectionList(List<CrewChangeDrivingsection> drivingSectionList) {
		this.drivingSectionList = drivingSectionList;
	}

	public String getSelectedDrivingSection() {
		return selectedDrivingSection;
	}

	public void setSelectedDrivingSection(String selectedDrivingSection) {
		this.selectedDrivingSection = selectedDrivingSection;
	}

	public List<CrewChangeDrivingsection> getSelectCrewChange() {
		return selectCrewChange;
	}

	public void setSelectCrewChange(List<CrewChangeDrivingsection> selectCrewChange) {
		this.selectCrewChange = selectCrewChange;
	}

	public List<CrewChangeDrivingsection> getSelectCrewChangeList() {
		return selectCrewChangeList;
	}

	public void setSelectCrewChangeList(List<CrewChangeDrivingsection> selectCrewChangeList) {
		this.selectCrewChangeList = selectCrewChangeList;
	}
	public DrivingDuty getDrivingduty() {
		return drivingduty;
	}

	public void setDrivingduty(DrivingDuty drivingduty) {
		this.drivingduty = drivingduty;
	}
	public Onedrivingsection getODS() {
		return ODS;
	}

	public void setODS(Onedrivingsection oDS) {
		ODS = oDS;
	}

	public DrivingDutyOfPilotDrivingSection getDrivingDutyPilotDrivingSection() {
		return drivingDutyPilotDrivingSection;
	}

	public void setDrivingDutyPilotDrivingSection(
			DrivingDutyOfPilotDrivingSection drivingDutyPilotDrivingSection) {
		this.drivingDutyPilotDrivingSection = drivingDutyPilotDrivingSection;
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public List<CrewChangeDrivingsection> getSelectOneOneCrewList() {
		return selectOneOneCrewList;
	}

	public void setSelectOneOneCrewList(List<CrewChangeDrivingsection> selectOneOneCrewList) {
		this.selectOneOneCrewList = selectOneOneCrewList;
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



}
