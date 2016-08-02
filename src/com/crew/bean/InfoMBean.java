package com.crew.bean;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.crew.exception.CrewException;
import com.crew.model.Customer;
import com.crew.service.CrewService;
import com.crew.util.CrewUtilities;

@ManagedBean(name = "infoMBean")
@ViewScoped
@SuppressWarnings("unchecked")

public class InfoMBean implements Serializable{
	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "schoolService", value = "#{schoolService}")
	private CrewService schoolService;
	private static final Logger CREW_LOGGER = Logger.getLogger(InfoMBean.class);
	private String user;
	private String userroles;
	HttpSession httpSession = (HttpSession) FacesContext
			.getCurrentInstance().getExternalContext().getSession(false);
	
	@PostConstruct
	private void init(){
		if (!(httpSession.getAttribute("COMMONLOGINID") == null)) 
		{
			
			List<Customer> infoTableList = new ArrayList<Customer>();
			
				try {
					infoTableList = (List<Customer>) schoolService
							.loadList(" from Customer as cst where cst.id = "
									+(Long) httpSession.getAttribute("COMMONLOGINID"));
				} catch (CrewException e) {
					CREW_LOGGER.debug(e.getMessage());
					e.printStackTrace();
				}
			
			
			if(!infoTableList.isEmpty())
			{
			user = infoTableList.get(0).getUsername();
			user.toUpperCase(Locale.CHINA);
			userroles = infoTableList.get(0).getUserRole();
			userroles.toUpperCase();
			
			
			}

		
		}else {
			
				try {
					FacesContext.getCurrentInstance().getExternalContext()
							.redirect(CrewUtilities.getRedirectContext());
				} catch (IOException e) {
					CREW_LOGGER.debug(e.getMessage());
					e.printStackTrace();
				}
			
		}
		
		
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public CrewService getSchoolService() {
		return schoolService;
	}
	public void setSchoolService(CrewService schoolService) {
		this.schoolService = schoolService;
		//init();
		
	}
	public String getUserroles() {
		return userroles;
	}
	public void setUserroles(String userroles) {
		this.userroles = userroles;
	}

	
}
