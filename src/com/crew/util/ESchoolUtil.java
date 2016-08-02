package com.crew.util;

import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;



public class ESchoolUtil {

	private static final Logger CREW_LOGGER = Logger.getLogger(ESchoolUtil.class);

	
	public static String getContext() {
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		return	request.getServletContext().getContextPath();
		
		
	}
	

	public static void logOut() {
		HttpSession httpSession = (HttpSession) FacesContext
				.getCurrentInstance().getExternalContext().getSession(false);
		httpSession.removeAttribute("COMMONLOGINID");
		
		httpSession.invalidate();

		redirect(getContext());
	}
	public static void redirect(String redirectTo) {

		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(redirectTo);
		} catch (IOException e) {

			CREW_LOGGER.debug(e.getMessage());
		}
	}
	
	
}