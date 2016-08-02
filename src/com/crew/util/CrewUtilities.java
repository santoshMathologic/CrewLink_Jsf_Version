package com.crew.util;

import java.util.Random;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public final class CrewUtilities {
	 
	    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz#$%&";
		public static void displayMessage(String nChoices,String messages) {
		FacesMessage message; 
		 switch(nChoices)
		    {
		       case "Info":
				 				message = new FacesMessage(FacesMessage.SEVERITY_INFO,messages, null);  
				 				FacesContext.getCurrentInstance().addMessage(null, message); 
				                break;
			       
		       case "Warning":
		    	   
		    	   				message = new FacesMessage(FacesMessage.SEVERITY_WARN,messages, null);  
		    	   				FacesContext.getCurrentInstance().addMessage(null, message); 
		    	   				break;
	       
			   	case "Error":
		       		
		       					message = new FacesMessage(FacesMessage.SEVERITY_ERROR,messages, null);  
		       					FacesContext.getCurrentInstance().addMessage(null, message); 
		       					break;

		       	case "Fatal":
		       		
		       					message = new FacesMessage(FacesMessage.SEVERITY_ERROR,messages, null);  
		       					FacesContext.getCurrentInstance().addMessage(null, message); 
		       					break;
		       		
	 
		 }
		 
	    }  
		
		public static String getRedirectContext() {
			HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			return	request.getServletContext().getContextPath();
			
			
		}

	public static String randomPassString() {
		Random rnd = new Random();
		 final int nLength=10;
		StringBuilder stringbuffer = new StringBuilder(nLength);
		for (int nLoop = 0; nLoop< nLength; nLoop++)
			stringbuffer.append(AB.charAt(rnd.nextInt(AB.length())));
		return stringbuffer.toString();
	}
	
}
