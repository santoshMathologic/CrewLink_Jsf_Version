package com.crew.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.crew.exception.CrewException;
import com.crew.model.Customer;
import com.crew.service.CrewService;
import com.crew.util.*;


@ManagedBean(name="customerbean",eager=true)
@ViewScoped
public class CustomerRegistrationbean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "schoolService", value = "#{schoolService}")
	private CrewService schoolService;
	
	@ManagedProperty(value="#{sendjavamail}")
	private SendJavaMail maildependency;
	
	
	private static final Logger CREW_LOGGER = Logger.getLogger(CustomerRegistrationbean.class);
	
	private ApplicationContext context;
	private Customer addcustomer;
	private String username;
	private String password;
	private String emailAddress;
	private String dateofBirth;
	private String address;
	private String selectedCity;
	private String selectedStates;
	private String phoneNo;
	private String selectedZones;
	private boolean isDisabled;
	
	
	
	
	private Map<String, String> citylist= new HashMap<>();
	private Map<String, String> stateslist= new HashMap<>();
	private Map<String, String> listofZones= new HashMap<>();
	
	
	@PostConstruct
	private void createList()
	{  context = new ClassPathXmlApplicationContext("Beans.xml");
	   addcustomer = (Customer) context.getBean("customer");
	   isDisabled = Boolean.FALSE;
	   
	   createAllCity();
	   createAllStates();
	   createAllZones();
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
	
	
	
	public String addCustomerInDB()
	{		
		final String randompasskey  = CrewUtilities.randomPassString();
		
		
		addcustomer.setUsername(username);
		addcustomer.setEmailAddress(emailAddress);
	
		addcustomer.setDob(dateofBirth);
		//addcustomer.setPassword(addcustomer.getPassword());
		addcustomer.setPassword(randompasskey);
		
		addcustomer.setAddress(address);
		addcustomer.setCity(selectedCity);
		addcustomer.setStates(selectedStates);
		addcustomer.setUserRole("customer");
		addcustomer.setZones(selectedZones);
		
		try {
			schoolService.saveInDb(addcustomer);
			try {
				maildependency.sendTemplateEmail(randompasskey,username,addcustomer.getFirstname(),emailAddress);
			} catch (AddressException e1) {
				CREW_LOGGER.debug(e1.getMessage());
				e1.printStackTrace();
			} catch (MessagingException e1) {
				CREW_LOGGER.debug(e1.getMessage());
				e1.printStackTrace();
			}
		} catch (Exception e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	

	public void validateSamePassword(FacesContext context, UIComponent toValidate, Object value) {
		UIInput passwordField = (UIInput) context.getViewRoot().findComponent("mainform:password");
	    if (passwordField == null)
	        throw new IllegalArgumentException(String.format("Unable to find component."));
	    String password = (String) passwordField.getValue();
	    String confirmPassword = (String) value;
	    if (!confirmPassword.equals(password)) {
	        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match!", "Passwords do not match!");
	        throw new ValidatorException(message);
	    }
	    }
	


	
	
	 public void redirect() {

			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) context
					.getExternalContext().getResponse();
			try {
				response.sendRedirect("index.jsf");
			} catch (IOException e) {

				e.printStackTrace();
			}

		}
	
	
	 
   @SuppressWarnings("unchecked")
	public void IsEmailExists()
	 {
		 boolean bExists  = Boolean.FALSE;
		 List<Customer> customerList = (List<Customer>) context.getBean("list");
		 try {
			 customerList =  (List<Customer>) schoolService.loadList("from Customer as cst where cst.emailAddress like '"+emailAddress+"'");
			 loop: for(Customer customer :customerList)
			 { if(customer.getEmailAddress().equalsIgnoreCase(emailAddress))
					{bExists=Boolean.TRUE;
					 break loop;
					}
			 }
			if(bExists)
			{CrewUtilities.displayMessage("Info","This Mail ID is already in use..!! Please type another");
			}
			else
			{	isDisabled = Boolean.TRUE;
			    System.out.print("else part");
				CrewUtilities.displayMessage("Info","This Mail ID is Available..!!");	
			}
			 
			
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
	 }
	
	 
	
	
	
	/// all getter and Setters

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





	public Customer getAddcustomer() {
		return addcustomer;
	}





	public void setAddcustomer(Customer addcustomer) {
		this.addcustomer = addcustomer;
	}


	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getDateofBirth() {
		return dateofBirth;
	}

	public void setDateofBirth(String dateofBirth) {
		this.dateofBirth = dateofBirth;
	}



	public static Logger getCrewLogger() {
		return CREW_LOGGER;
	}



	public Map<String, String> getCitylist() {
		return citylist;
	}



	public void setCitylist(Map<String, String> citylist) {
		this.citylist = citylist;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public Map<String, String> getStateslist() {
		return stateslist;
	}



	public void setStateslist(Map<String, String> stateslist) {
		this.stateslist = stateslist;
	}



	public String getSelectedCity() {
		return selectedCity;
	}



	public void setSelectedCity(String selectedCity) {
		this.selectedCity = selectedCity;
	}



	public String getSelectedStates() {
		return selectedStates;
	}



	public void setSelectedStates(String selectedStates) {
		this.selectedStates = selectedStates;
	}



	public String getPhoneNo() {
		return phoneNo;
	}



	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}



	public Map<String, String> getListofZones() {
		return listofZones;
	}



	public void setListofZones(Map<String, String> listofZones) {
		this.listofZones = listofZones;
	}



	public String getSelectedZones() {
		return selectedZones;
	}



	public void setSelectedZones(String selectedZones) {
		this.selectedZones = selectedZones;
	}



	public SendJavaMail getMaildependency() {
		return maildependency;
	}



	public void setMaildependency(SendJavaMail maildependency) {
		this.maildependency = maildependency;
	}



	public boolean isDisabled() {
		return isDisabled;
	}



	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}



	



	


	

}
