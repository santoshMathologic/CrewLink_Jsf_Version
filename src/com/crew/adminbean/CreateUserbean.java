package com.crew.adminbean;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.crew.exception.CrewException;
import com.crew.model.Customer;
import com.crew.service.CrewService;


@ManagedBean(name="createuserbean",eager=true)
@ViewScoped
public class CreateUserbean implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "schoolService", value = "#{schoolService}")
	private CrewService schoolService;
	private HttpSession httpSession = (HttpSession) FacesContext
			.getCurrentInstance().getExternalContext().getSession(false);
	private static final Logger CREW_LOGGER = Logger.getLogger(CreateUserbean.class);
	private ApplicationContext context;
	private Customer customer;
	private Date selectedDate;
	
	private Map<String, String> citylist= new HashMap<>();
	private Map<String, String> stateslist= new HashMap<>();
	private Map<String, String> listofZones= new HashMap<>();
	
	
	
	@PostConstruct
	private void initilization() {

		if (!(httpSession.getAttribute("COMMONLOGINID") == null)) {
			Long Id = (Long) httpSession.getAttribute("COMMONLOGINID");
			System.out.print("Create Loging Id = =" + Id);
			CreateList();
			

		} else {
			try {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(getRedirectContext() + "/login.xhtml");
			} catch (IOException e) {
				CREW_LOGGER.debug(e.getMessage());
			}

		}

	}

	
	public static String getRedirectContext() {
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		return	request.getServletContext().getContextPath();
		
		
	}
	
	
	private void createAllCity()
	{
		citylist.put("Abhayapuri", "Abhayapuri");
		citylist.put("Achabbal", "Achabbal");
		citylist.put("Achalpur", "Achalpur");
		citylist.put("Achhnera", "Achhnera");
		citylist.put("Achabbal", "Achabbal");
		citylist.put("Adari", "Adari");
		citylist.put("Adalaj", "Adalaj");
		citylist.put("Adilabad", "Adilabad");
		citylist.put("Adityana", "Adityana");
		citylist.put("pereyaapatna", "pereyaapatna");
		citylist.put("Adoni", "Adoni");
		citylist.put("Adoor", "Adoor");
		citylist.put("Adyar", "Adyar");
		citylist.put("Adra", "Adra");
		citylist.put("Afzalpura", "Afzalpura");
		citylist.put("Agartala", "Agartala");
		citylist.put("Agra", "Agra");
		citylist.put("Ahmedabad", "Ahmedabad");
		
		
	}
	
	
	private void createAllZones() {
		
		listofZones.put("Central Railway", "Central Railway");
		listofZones.put("East Central Railway", "East Central Railway");
		listofZones.put("East Coast Railway", "East Coast Railway");
		listofZones.put("Eastern Railway", "Eastern Railway");
		listofZones.put("North Central Railway", "North Central Railway");
		listofZones.put("North Eastern Railway", "North Eastern Railway");
		listofZones.put("North Western Railway", "North Western Railway");
		listofZones.put("Northeast Frontier Railway", "Northeast Frontier Railway");
		listofZones.put("Northern Railway","Northern Railway");
		listofZones.put("South Central Railway", "South Central Railway");
		listofZones.put("South East Central Railway", "South East Central Railway");
		listofZones.put("South Eastern Railway", "South Eastern Railway");
		listofZones.put("South Western Railway", "South Western Railway");
		listofZones.put("Southern Railway", "Southern Railway");
		listofZones.put("West Central Railway", "West Central Railway");
		listofZones.put("Western Railway", "Western Railway");
		
		
		
		
		
	}



	private void createAllStates() {
		
		stateslist.put("Assam", "Assam");
		stateslist.put("Jammu and Kashmir", "Jammu and Kashmir");
		stateslist.put("Maharashtra", "Maharashtra");
		stateslist.put("UttarPardesh", "UttarPardesh");
		stateslist.put("Gujrat", "Gujrat");
		stateslist.put("AndhraPradesh", "AndhraPradesh");
		stateslist.put("Karnataka", "Karnataka");
		stateslist.put("Kerla", "Kerla");
		stateslist.put("West Bengal", "West Bengal");
		stateslist.put("Tripura", "Tripura");
		stateslist.put("Chhattisgarh", "Chhattisgarh");
		stateslist.put("Punjab", "Punjab");
		stateslist.put("Mizoram", "Mizoram");
		stateslist.put("Rajasthan", "Rajasthan");
		stateslist.put("Goa", "Goa");
		stateslist.put("Bihar", "Bihar");
		stateslist.put("Lakshadweep", "Lakshadweep");
		stateslist.put("Jharkhand", "Jharkhand");
		stateslist.put("Orissa", "Orissa");
	}


	
	private void CreateList()
	{
		context = new ClassPathXmlApplicationContext("Beans.xml");
		customer = (Customer) context.getBean("customer");
		createAllCity();
		createAllStates();
		createAllZones();
	}

	
	public String convertedDob()
	{
		String convertedDate=null;
		if(!(selectedDate==null))
		{
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	    	convertedDate = df.format(selectedDate);
			return convertedDate;
		}
		
		return convertedDate;
	}
	
	
	
	public void addUsers()
	{	
			customer.setDob(convertedDob());
		   if(!(customer==null))
		   {try {
			   		
			   		schoolService.saveInDb(customer);
			   		
			try {
				FacesContext.getCurrentInstance().getExternalContext()
				.redirect(getRedirectContext() + "/masteradmin/viewusers.xhtml");
			} catch (IOException e) {
				CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
			}
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
}
		   else
		   {
			   System.out.println("break states");
		   }
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// all the getter and Setters are started from here

	public ApplicationContext getContext() {
		return context;
	}


	public void setContext(ApplicationContext context) {
		this.context = context;
	}


	public Customer getCustomer() {
		return customer;
	}


	public void setCustomer(Customer customer) {
		this.customer = customer;
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


	public Map<String, String> getCitylist() {
		return citylist;
	}


	public void setCitylist(Map<String, String> citylist) {
		this.citylist = citylist;
	}


	public Map<String, String> getStateslist() {
		return stateslist;
	}


	public void setStateslist(Map<String, String> stateslist) {
		this.stateslist = stateslist;
	}


	public Map<String, String> getListofZones() {
		return listofZones;
	}


	public void setListofZones(Map<String, String> listofZones) {
		this.listofZones = listofZones;
	}


	public Date getSelectedDate() {
		return selectedDate;
	}


	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}
	
	
	

}
