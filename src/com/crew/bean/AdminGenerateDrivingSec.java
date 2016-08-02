package com.crew.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

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
import com.crew.model.TimeTableMaster;
import com.crew.model.Trainbiodata;
import com.crew.service.CrewService;
import com.crew.util.ESchoolUtil;


@ManagedBean(name="adminGenerateDriv",eager=true)
@ViewScoped
@SuppressWarnings("unchecked")
public class AdminGenerateDrivingSec implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "schoolService", value = "#{schoolService}")
	private CrewService schoolService;
	private HttpSession httpSession = (HttpSession) FacesContext
			.getCurrentInstance().getExternalContext().getSession(false);
	
	private ApplicationContext context;
	private List<Trainbiodata> TrainbiodataList;
	private List<TimeTableMaster>mastertablelist;
	private TimeTableMaster timetableMaster;
	private static final Logger CREW_LOGGER = Logger.getLogger(AdminGenerateDrivingSec.class);
	
	
	
	public AdminGenerateDrivingSec() 
	{
		
	}

	@PostConstruct
	private void initilization()
	{
		
			CreateList();
			loadMasterTable();
			

	
		
	
	}

	private void CreateList() {
		context = new ClassPathXmlApplicationContext("Beans.xml");
		mastertablelist =  (List<TimeTableMaster>) context.getBean("list");
			
	}
	
	
	private void loadMasterTable()
	{
		try {
			mastertablelist =  (List<TimeTableMaster>) schoolService.loadList("from TimeTableMaster");
		} catch (CrewException e) {
			this.CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


	////  Getter and Setters
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



	public TimeTableMaster getTimetableMaster() {
		return timetableMaster;
	}



	public void setTimetableMaster(TimeTableMaster timetableMaster) {
		this.timetableMaster = timetableMaster;
	}



	public static Logger getCrewLogger() {
		return CREW_LOGGER;
	}

}
