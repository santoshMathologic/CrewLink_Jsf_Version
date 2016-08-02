package com.crew.bean;


import java.io.IOException;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;


@ManagedBean(name="logoutbean",eager=true)
@ViewScoped
public  class LogOutMBean implements Serializable {
	private static final long serialVersionUID = 1L;
	public static void logOut() {
	HttpSession httpSession = (HttpSession) FacesContext
				.getCurrentInstance().getExternalContext().getSession(false);
		
		httpSession.removeAttribute("COMMONLOGINID");
		httpSession.invalidate();

	
	}

}
