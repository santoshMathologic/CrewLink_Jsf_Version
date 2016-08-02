package com.crew.bean;

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
import com.crew.model.Finaltable;
import com.crew.service.CrewService;


@ManagedBean(name="optimizedbean",eager=true)
@ViewScoped
public class OptimizedAdmin {
	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "schoolService", value = "#{schoolService}")
	private CrewService schoolService;
	private HttpSession httpSession = (HttpSession) FacesContext
			.getCurrentInstance().getExternalContext().getSession(false);
	private static final Logger CREW_LOGGER = Logger.getLogger(adminDrivingduty.class);
	
	private ApplicationContext context;
	
	private List<Finaltable> listFinalTable;
	
	private String source;
	private String destination;
	private int numberOfTrains;
	
	@PostConstruct
		private void initilization() {
				 createList();
				 
		}
	
	@SuppressWarnings("unchecked")
	private void createList()
	{
		context = new ClassPathXmlApplicationContext("Beans.xml");
		listFinalTable = (List<Finaltable>) context.getBean("list");
	}
	
	
	@SuppressWarnings("unchecked")
	public void selectTrains() throws CrewException
	{
		List<String> listOfTrainNo = null;
		int i = 0;
		listFinalTable = (List<Finaltable>) schoolService.gettingSizeRow(numberOfTrains);
		
		System.out.println("size of the final table" +listFinalTable.size());
		
		
		for(Finaltable f : listFinalTable)
		{
			System.out.println("train 1" +f.getTrainno1());
			System.out.println("train 2" +f.getTrainno2());
			System.out.println("diff" +f.getDifferecnce());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//all getters and setters
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
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public int getNumberOfTrains() {
		return numberOfTrains;
	}
	public void setNumberOfTrains(int numberOfTrains) {
		this.numberOfTrains = numberOfTrains;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public static Logger getCrewLogger() {
		return CREW_LOGGER;
	}

	public List<Finaltable> getListFinalTable() {
		return listFinalTable;
	}

	public void setListFinalTable(List<Finaltable> listFinalTable) {
		this.listFinalTable = listFinalTable;
	}
}
