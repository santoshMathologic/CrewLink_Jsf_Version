package com.crew.bean;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
import com.crew.model.Customer;
import com.crew.model.TimeTableMaster;
import com.crew.model.Trainbiodata;
import com.crew.service.CrewService;






@ManagedBean(name="adminTrainBioData",eager=true)
@ViewScoped
@SuppressWarnings("unchecked")

public class AdminTrainBioData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "schoolService", value = "#{schoolService}")
	private CrewService schoolService;
	private HttpSession httpSession = (HttpSession) FacesContext
			.getCurrentInstance().getExternalContext().getSession(false);
	
	private ApplicationContext context;
	private List<Trainbiodata> TrainbiodataList;
	private List<TimeTableMaster>mastertablelist;
	private TimeTableMaster timetableMaster;
	private static final Logger CREW_LOGGER = Logger.getLogger(AdminTrainBioData.class);
	
	
	private String trainNo;
	private String fromStation;
	private List<SelectItem> toStationList;
	private String selectedFromStaion;
	private List<Trainbiodata> filterbiodataList;
	private Customer customer;
	
	private String searchTrains;
	private boolean bMessages;
	
	

	@PostConstruct
	private void initilization()
	{		
		
		
		HttpSession httpSession = (HttpSession) FacesContext
				 .getCurrentInstance().getExternalContext().getSession(false);
		 
		 if(!(httpSession.getAttribute("COMMONLOGINID")==null))
		 {
			 Long Id  = (Long) httpSession.getAttribute("COMMONLOGINID");
			
			 try
			 {
			  setCustomer((Customer) schoolService.loadUniqueObject("from Customer as cust where cust.id="+Id));
			  CreateList();
			 }catch(Exception e)
			 { e.printStackTrace();
			 }
			
			
		 }
		 else {
				try {
					FacesContext.getCurrentInstance().getExternalContext()
					.redirect("login.xhtml");
				} catch (IOException e) {
					CREW_LOGGER.debug(e.getMessage());
				}
			}
				
		
		
		
		
	}

	
	
private void CreateList()
{
	context = new ClassPathXmlApplicationContext("Beans.xml");
	TrainbiodataList 		=  	(List<Trainbiodata>) context.getBean("list");
	mastertablelist			= 	(List<TimeTableMaster>) context.getBean("list");
	timetableMaster 		=   (TimeTableMaster) context.getBean("timeTableMaster");
	
	bMessages = Boolean.FALSE;
	
	toStationList = new ArrayList<SelectItem>();
	loadTrainDataList();
}



private void loadTrainDataList()
{
	try {
		TrainbiodataList =  (List<Trainbiodata>) this.schoolService.loadList("from Trainbiodata");
		  
	} catch (CrewException e) {
		CREW_LOGGER.debug(e.getMessage());
		e.printStackTrace();
	}
}

public String loadMap()
{
	Map<String, String> map = FacesContext.getCurrentInstance()
			.getExternalContext().getRequestParameterMap();
	return map.get("SELECTTRAIN");
}

public String loadStation()
{
	
	String selectTrainNo= loadMap();
	mastertablelist.clear();
	try {
	    mastertablelist = (List<TimeTableMaster>) schoolService.loadList("from TimeTableMaster as TTM where TTM.trainNo="+selectTrainNo);
	} catch(CrewException e) {
		
		e.printStackTrace();
	}
	
	System.out.println("The value of selectTrainNo is "+selectTrainNo);
	
	 if(!mastertablelist.isEmpty())
	 {
		trainNo = mastertablelist.get(0).getTrainNo();
		fromStation=mastertablelist.get(0).getStationCode();
	 }

	 
	 int size = mastertablelist.size();
		 List<TimeTableMaster> list  = mastertablelist.subList(1,size);
	 
	 for(TimeTableMaster l :list)
		 toStationList.add(new SelectItem(l.getStationCode()));
	
	 
	 return null;
	
}




public void loadlist(String trainNo)
{
	try {
		mastertablelist = (List<TimeTableMaster>) schoolService.loadList("from TimeTableMaster as TTM where TTM.trainNo like '"+trainNo+"'");
	} catch (CrewException e) {
		e.printStackTrace();
	}
}


public String saveCrewChanges(TimeTableMaster timeTableMasterObject)
{	TimeTableMaster timeTableMaster;

try {
	 timeTableMaster = (TimeTableMaster) schoolService.loadUniqueObject("from TimeTableMaster as TTM where TTM.stationCode like '"+timeTableMasterObject.getStationCode()+"'");
	 schoolService.updateObjectInDb(timeTableMaster);
	 loadlist(timeTableMasterObject.getTrainNo());
	 
} catch (CrewException e1) {
	e1.printStackTrace();
}

	
	try {
	//	schoolService.saveInDb(timetableMaster);
	} catch (Exception e) {
		CREW_LOGGER.debug(e.getMessage());
		e.printStackTrace();
	}
	
	
	return null;
//	return "alltrain.faces?faces-redirect=true";
	
}



public void searchallTrains()
{
	
	
	if(searchTrains.length()>=5  && searchTrains!=null && !"".equals(searchTrains))
	{	try {
			TrainbiodataList = (List<Trainbiodata>) schoolService.loadList("from Trainbiodata as td where td.trainNo like '"+searchTrains+"'");
			if(TrainbiodataList.size()==0)
			{	setbMessages(true);
			}

		} catch (CrewException e) {
			e.printStackTrace();
			CREW_LOGGER.debug(e.getMessage());
		}
	}
	else
	{
		try {
			TrainbiodataList = (List<Trainbiodata>) schoolService.loadList("from Trainbiodata as td");
			System.out.println("in Else Size "+TrainbiodataList.size());
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
		System.out.println("in Else "+TrainbiodataList.isEmpty());
	}
	
}






















//  getter and Setters

	public CrewService getSchoolService() {
		return schoolService;
	}



	public void setSchoolService(CrewService schoolService) {
		this.schoolService = schoolService;
	}





	public ApplicationContext getContext() {
		return context;
	}





	public void setContext(ApplicationContext context) {
		this.context = context;
	}





	public HttpSession getHttpSession() {
		return httpSession;
	}





	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}





	public List<Trainbiodata> getTrainbiodataList() {
		return TrainbiodataList;
	}





	public void setTrainbiodataList(List<Trainbiodata> trainbiodataList) {
		TrainbiodataList = trainbiodataList;
	}



	public List<TimeTableMaster> getMastertablelist() {
		return mastertablelist;
	}


	public void setMastertablelist(List<TimeTableMaster> mastertablelist) {
		this.mastertablelist = mastertablelist;
	}


	public static Logger getCrewLogger() {
		return CREW_LOGGER;
	}



	public TimeTableMaster getTimetableMaster() {
		return timetableMaster;
	}



	public void setTimetableMaster(TimeTableMaster timetableMaster) {
		this.timetableMaster = timetableMaster;
	}



	



	public String getFromStation() {
		return fromStation;
	}



	public void setFromStation(String fromStation) {
		this.fromStation = fromStation;
	}



	public List<SelectItem> getToStationList() {
		return toStationList;
	}



	public void setToStationList(List<SelectItem> toStationList) {
		this.toStationList = toStationList;
	}



	public String getSelectedFromStaion() {
		return selectedFromStaion;
	}



	public void setSelectedFromStaion(String selectedFromStaion) {
		this.selectedFromStaion = selectedFromStaion;
	}



	public String getTrainNo() {
		return trainNo;
	}



	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}



	public List<Trainbiodata> getFilterbiodataList() {
		return filterbiodataList;
	}



	public void setFilterbiodataList(List<Trainbiodata> filterbiodataList) {
		this.filterbiodataList = filterbiodataList;
	}



	public Customer getCustomer() {
		return customer;
	}



	public void setCustomer(Customer customer) {
		this.customer = customer;
	}



	public String getSearchTrains() {
		return searchTrains;
	}



	public void setSearchTrains(String searchTrains) {
		this.searchTrains = searchTrains;
	}



	public boolean isbMessages() {
		return bMessages;
	}



	public void setbMessages(boolean bMessages) {
		this.bMessages = bMessages;
	}







	





}