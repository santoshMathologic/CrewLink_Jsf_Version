package com.crew.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.crew.exception.CrewException;
import com.crew.model.Customer;
import com.crew.service.CrewService;
import com.crew.util.CrewUtilities;
import com.crew.util.SendJavaMail;

@ManagedBean(name="forgetpasskeybean",eager=true)
@ViewScoped
@SuppressWarnings("unchecked")
public class Forgetpasskeybean implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "schoolService", value = "#{schoolService}")
	private CrewService schoolService;
	private HttpSession httpSession = (HttpSession) FacesContext
			.getCurrentInstance().getExternalContext().getSession(false);
	private static final Logger CREW_LOGGER = Logger.getLogger(Forgetpasskeybean.class);
	private ApplicationContext context;
	private String emailAddress;
	private long loginId;
	private Customer aCustomer;
	@ManagedProperty(value="#{sendjavamail}")
	private SendJavaMail maildependency;
	
	
	@PostConstruct 
	private void initilization()
	{	if (!(httpSession.getAttribute("COMMONLOGINID") == null))
		{this.loginId = (long) httpSession.getAttribute("COMMONLOGINID");
		}
		else
		{CreateAllList();	
		}
	}

	private void CreateAllList() {
		context = new ClassPathXmlApplicationContext("Beans.xml");
		aCustomer  = (Customer) context.getBean("customer");
		
	}

	
	
	public String resetToDB()
	{	
		Map<Customer,Boolean> isExistsmap  = isValueExists();
		Iterator<?> it = isExistsmap.entrySet().iterator();
				while (it.hasNext()) {
						Map.Entry mapPair = (Map.Entry)it.next();
							System.out.println(mapPair.getKey() + " = " + mapPair.getValue());
							if(!(boolean) mapPair.getValue())
							{	Customer customer = (Customer) mapPair.getKey();
							    final String randompasskey  = CrewUtilities.randomPassString();
								customer.setPassword(randompasskey);
								try {
									schoolService.updateObjectInDb(customer);
									try {
										maildependency.sendTemplateEmail(randompasskey,customer.getUsername(),null,customer.getEmailAddress());
									} catch (AddressException e) {
										CREW_LOGGER.debug(e.getMessage());
										e.printStackTrace();
									} catch (MessagingException e) {
										CREW_LOGGER.debug(e.getMessage());
										e.printStackTrace();
									}
								} catch (CrewException e) {
									CREW_LOGGER.debug(e.getMessage());
									e.printStackTrace();
								}
							}
							it.remove(); 
						}
		return "login.xhtml?faces-redirect=true";
	}
	
	
	private  Map<Customer,Boolean> isValueExists()
	{
	 boolean bExists = 	Boolean.TRUE;
	 Map<Customer,Boolean> isExistmap=new HashMap<Customer,Boolean>();  
		try {
			aCustomer = (Customer) schoolService.loadUniqueObject("from Customer as cst where cst.emailAddress like '"+emailAddress+"'");
			if(!(aCustomer==null))
			{	bExists = Boolean.FALSE;
			    isExistmap.put(aCustomer, bExists);
			}
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			
		}
		isExistmap.put(aCustomer, bExists);
		return isExistmap;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/// all getter and setter are defined here
	public CrewService getSchoolService() {
		return schoolService;
	}
	
	public long getLoginId() {
		return loginId;
	}

	public void setLoginId(long loginId) {
		this.loginId = loginId;
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

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Customer getaCustomer() {
		return aCustomer;
	}

	public void setaCustomer(Customer aCustomer) {
		this.aCustomer = aCustomer;
	}

	public SendJavaMail getMaildependency() {
		return maildependency;
	}

	public void setMaildependency(SendJavaMail maildependency) {
		this.maildependency = maildependency;
	}



	
	
}
