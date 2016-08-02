package com.crew.bean;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.crew.exception.CrewException;
import com.crew.model.CrewLink;
import com.crew.model.RoundTrip;
import com.crew.service.CrewService;
import com.crew.util.CrewUtilities;




@ManagedBean(name="rtripCrewlink",eager=true)
@ViewScoped
public class RtripToCrewLink implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "schoolService", value = "#{schoolService}")
	private CrewService schoolService;
	private HttpSession httpSession = (HttpSession) FacesContext
			.getCurrentInstance().getExternalContext().getSession(false);
	private static final Logger CREW_LOGGER = Logger.getLogger(adminDrivingduty.class);
	private long loginId;
	private ApplicationContext context;
	private List<RoundTrip> roundTripList;
	private CrewLink crewLink;
	private List<RoundTrip>listRoundTrip;
	
	private String sequenceNumbers;
	private String[] sequence = new String[100];
	private String[] roundTripId = new String[100];
	
	
	private int nCount;
	private Map<Long,String> MapSaveId = new HashMap<Long,String>(); 
	private Map<Long,String> SortedMap;
	private boolean prCheckbox;

	
	
    @PostConstruct
	private void initilization() {

	if (!(httpSession.getAttribute("COMMONLOGINID") == null)) {
		loginId = (Long) httpSession.getAttribute("COMMONLOGINID");
		 createList();
		 loadList();
		 
	

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
		crewLink = (CrewLink) context.getBean("crewlink");
		nCount = 0;
	}
	@SuppressWarnings("unchecked")
	public void loadList()
	{	try {
			listRoundTrip = (List<RoundTrip>) schoolService.loadList("from RoundTrip as rrt where rrt.customer.id="+loginId);
			
			roundTripList = (List<RoundTrip>) schoolService.loadList("from RoundTrip as rrt where rrt.customer.id="+loginId);
			
		Collections.sort(roundTripList, new Comparator<RoundTrip>() {
	     @Override
					public int compare(RoundTrip o1, RoundTrip o2) {
						return o1.getFromStation().compareToIgnoreCase(o2.getFromStation());
					}
		        });
		
		
		System.out.println("Id \tTrains\t\tDep\tArr\tDistance SignOn SignOff Avail No_of_hours\n--------------------------------------------------------------------------------------"); 
		for(RoundTrip rt:listRoundTrip)
		{

			System.out.println(rt.getId()+"\t"+rt.getTrainNo()+"\t"+rt.getDeparturetime().toString().substring(10, 16)+"\t"+rt.getArrivaltime().toString().substring(10, 16)+"\t"+rt.getDistance()+"\t"+rt.getSignonTime().toString().substring(10, 16)+"\t"+rt.getSignoffTime().toString().substring(10, 16)+"\t"+rt.getAvailabilityAtHq()+"\t"+rt.getNoOfDays());
			
			
		}
		
		
		} catch (CrewException e) {
			e.printStackTrace();
			CREW_LOGGER.debug(e.getMessage());
		}
	}



	public void saveId(long Id)
	{
	
		
			if(Integer.parseInt(sequenceNumbers) >listRoundTrip.size())
			{
			    System.out.println("Please Enter the Number Less than Size ");		
			}
			else
			{
				//System.out.println(""+Id+"\t"+sequenceNumbers);
				sequence[nCount++] = sequenceNumbers;	
				roundTripId[nCount++] = String.valueOf(Id);
				MapSaveId.put(Id,sequenceNumbers);
				SortedMap=new TreeMap<Long, String>(MapSaveId); 
			}
			
		
			
	
	
	}
	
	
	public void selectedCheckBox(RoundTrip d)
	{
		//System.out.println(""+);
		System.out.println(""+prCheckbox);
		System.out.println(""+d.getId());
	}
	
	
	
	public void createLink() throws CrewException
	{
		
		String keys [] = new String[100];
		String values [] = new String[100];
		
		int n = 0;
		 for(Entry<Long,String> entry : MapSaveId.entrySet())
			{
			 keys	[n] = String.valueOf(entry.getKey()); 
			 values [n] = entry.getValue();
			 n++;
			}
		 
		 
		
		 for(int nIndex = 0;nIndex<MapSaveId.size();nIndex++)
		 {
			
			 RoundTrip roundTripObj = (RoundTrip) 
						schoolService.loadUniqueObject("from RoundTrip as rr where rr.id=" +keys[nIndex]);
			 RoundTrip roundTripObj1 = (RoundTrip) 
						schoolService.loadUniqueObject("from RoundTrip as rr where rr.id=" +values[nIndex]);
	
			// calculate_difference_between_Avail_and_SignOn(roundTripObj1.getSignonTime(),roundTripObj.getAvailabilityAtHq());
			 
			 calculate_difference_between_Avail_and_departure(roundTripObj1.getDeparturetime(),roundTripObj.getAvailabilityAtHq());
			 
		 }
			 
	
		
	}
	
	
	public void calculate_difference_between_Avail_and_SignOn(Date strSignOn, String strAvail)
	{
		
		String strAvailableTime[] = strAvail.split(Pattern.quote(":"));
	
		
		String strHour = null;
		String strMinute = null;
		
		//System.out.println("Hours \t Minutes");
		for(int i = 0 ; i<strAvailableTime.length-1;i++)
		{
			strHour = strAvailableTime[i];
			strMinute = strAvailableTime[i+1];
			//System.out.print(""+strHour+"\t"+strMinute);
		}
		
		Date convertedDate = null;
		StringBuffer br = new StringBuffer(strHour).append(":").append(strMinute);
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		try {
			convertedDate = df.parse(br.toString());
			} catch (ParseException e) {
			CREW_LOGGER.debug(e.getMessage());
			}

		Calendar calAvail = Calendar.getInstance();
		calAvail.setTime(convertedDate);
		calAvail.set(Calendar.HOUR_OF_DAY, Integer.parseInt(strHour));
		calAvail.set(Calendar.MINUTE, Integer.parseInt(strMinute));
		
		Integer iAvailableHour = calAvail.get(Calendar.HOUR_OF_DAY);
		Integer iAvailableMinute = calAvail.get(Calendar.MINUTE);
		
		System.out.println(""+iAvailableHour+":"+iAvailableMinute);
		
	
		Calendar calDeparture = Calendar.getInstance();
		calDeparture.setTime(strSignOn);
		Integer signOnHour = calDeparture.get(Calendar.HOUR_OF_DAY);
		Integer signOnMinutes = calDeparture.get(Calendar.MINUTE);
		
		
		
		System.out.print(""+signOnHour+":"+signOnMinutes);
		
		
		
		
		/*
		System.out.println("SignOn"+strSignOn);
		System.out.println("Avail"+strAvail);*/
	               	
	}
	
	public void calculate_difference_between_Avail_and_departure(Date dep, String strAvail)
	{
		
		String strAvailableTime[] = strAvail.split(Pattern.quote(":"));
		
		String strAvailHour= null;
		String strAvailMinute = null;
		for(int i = 0 ; i<strAvailableTime.length-1;i++)
		{
			strAvailHour= strAvailableTime[i];
			strAvailMinute = strAvailableTime[i+1];
			
		}
		
		Calendar calDep = Calendar.getInstance(Locale.ENGLISH);
		calDep.setTime(dep);
		Integer nDepartureHour  =  calDep.get(Calendar.HOUR_OF_DAY);
		Integer nDepartureMintes  =  calDep.get(Calendar.HOUR_OF_DAY);
		System.out.println("Departure"+nDepartureHour+":"+nDepartureMintes);
		
		Calendar availCalendar = Calendar.getInstance(Locale.ENGLISH);
		availCalendar.setTime(dep);
		availCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(strAvailHour));
		availCalendar.set(Calendar.MINUTE, Integer.parseInt(strAvailMinute));
		
		Integer availHour  = availCalendar.get(Calendar.HOUR_OF_DAY);
		Integer availMinutes  = availCalendar.get(Calendar.MINUTE);
		
		System.out.println("Avail Hour "+availHour+":"+availMinutes);
				
		
		
		if(availHour==0){availHour = 24;}
		if(nDepartureHour==0){nDepartureHour = 24;}
		
		
		
	
		
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	///
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



	public long getLoginId() {
		return loginId;
	}



	public void setLoginId(long loginId) {
		this.loginId = loginId;
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



	public CrewLink getCrewLink() {
		return crewLink;
	}



	public void setCrewLink(CrewLink crewLink) {
		this.crewLink = crewLink;
	}



	public List<RoundTrip> getRoundTripList() {
		return roundTripList;
	}



	public void setRoundTripList(List<RoundTrip> roundTripList) {
		this.roundTripList = roundTripList;
	}



	public List<RoundTrip> getListRoundTrip() {
		return listRoundTrip;
	}



	public void setListRoundTrip(List<RoundTrip> listRoundTrip) {
		this.listRoundTrip = listRoundTrip;
	}



	public String getSequenceNumbers() {
		return sequenceNumbers;
	}



	public void setSequenceNumbers(String sequenceNumbers) {
		this.sequenceNumbers = sequenceNumbers;
	}



	public String[] getSequence() {
		return sequence;
	}



	public void setSequence(String[] sequence) {
		this.sequence = sequence;
	}



	public Map<Long, String> getMapSaveId() {
		return MapSaveId;
	}



	public void setMapSaveId(Map<Long, String> mapSaveId) {
		MapSaveId = mapSaveId;
	}






	public boolean isPrCheckbox() {
		return prCheckbox;
	}



	public void setPrCheckbox(boolean prCheckbox) {
		this.prCheckbox = prCheckbox;
	}



    
    

}
