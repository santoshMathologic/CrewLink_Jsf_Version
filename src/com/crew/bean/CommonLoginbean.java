package com.crew.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.crew.exception.CrewException;
import com.crew.model.Customer;
import com.crew.service.CrewService;
import com.crew.util.CrewUtilities;





@SessionScoped
@ManagedBean(name="commonloginbean",eager=true)
public class CommonLoginbean implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "schoolService", value = "#{schoolService}")
	private CrewService schoolService;

	private static final Logger CREW_LOGGER = Logger.getLogger(CommonLoginbean.class);
	private String username;
	private String password;
	public boolean IsLogging = false;
	private ApplicationContext context;
	private String userRoles;
	private boolean isActiveChecked;
	
	
	@PostConstruct
	private void initilization()
	{		createList();
			
	}
	
	private void createList()
	{context = new ClassPathXmlApplicationContext("Beans.xml");
	 isActiveChecked  = Boolean.FALSE;
	}
	public void verifyCommonLogin()
	{
		IsLogging = true;
		
		if ((getUsername() != null && !"".equals(getUsername())) && (getPassword()!=null && !"".equals(getPassword())))
				{
				List<Customer> customerInfoTableList = new ArrayList<Customer>(1);
							try {
						customerInfoTableList = schoolService.verifyCommonLogin(getUsername(),getPassword());
						if(customerInfoTableList.size()>0)
						{	switch(customerInfoTableList.get(0).getUserRole().toLowerCase())
							{
							case "admin" :  	
								
												userRoles = "admin";
												navigate(customerInfoTableList.get(0).getId(), "/masteradmin/viewusers.xhtml",
												"COMMONLOGINID");
												break;
							
							case "customer":
												userRoles = "customer";
												navigate(customerInfoTableList.get(0).getId(), "/admin/alltrain.xhtml",
												"COMMONLOGINID");
												break;
												
							default : 		 	
												navigate(null, "login.xhtml",
												"NULL");
											 	break;
							}
							
						} 
						else {
							FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "userName and Password Incorrect",null); 
							FacesContext.getCurrentInstance().addMessage(null, msg);  
						}

							 
						
				}catch(Exception e)
				{	CREW_LOGGER.debug(e.getMessage());		
				}
		       }
			
					
			

		
	}

	
	private boolean IsAccountActive(String username,String password)
	{
		
		boolean bIsActive = Boolean.FALSE;
		boolean bChecked = Boolean.TRUE;
		List<Customer> customerList  = new ArrayList<Customer>();
		if(!isActiveChecked)
		{
		
				try {
					customerList = (List<Customer>) schoolService.loadList("from Customer as cust where cust.username='"
						+ username + "' And cust.password='"
						+ password + "' OR cust.account='"+bChecked+"'");
					System.out.print("Size  = "+customerList.size());
					 if(customerList.size()>0)
					 { bIsActive = Boolean.TRUE;
					   return bIsActive;
					 }
				} catch (CrewException e) {
					CREW_LOGGER.debug(e.getMessage());
					e.printStackTrace();
				}
		
		}
		return bIsActive;
	}
	
	private <T> void navigate(Long dbLoginId, String successPath,String loginString) {
		HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance()
	   .getExternalContext().getSession(true);				
		httpSession.setAttribute(loginString, dbLoginId);
	    redirect(CrewUtilities.getRedirectContext()+successPath);
}
	
	private void redirect(String path)
	{
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect(path);
			} catch (IOException e) {
				CREW_LOGGER.debug(e.getStackTrace());
			}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//// all getter and setter
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

	public static Logger getCrewLogger() {
		return CREW_LOGGER;
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

	public boolean isIsLogging() {
		return IsLogging;
	}

	public void setIsLogging(boolean isLogging) {
		IsLogging = isLogging;
	}

	public String getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(String userRoles) {
		this.userRoles = userRoles;
	}

	public boolean isActiveChecked() {
		return isActiveChecked;
	}

	public void setActiveChecked(boolean isActiveChecked) {
		this.isActiveChecked = isActiveChecked;
	}
	
	

	
	

}
