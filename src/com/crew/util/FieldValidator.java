package com.crew.util;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;


@FacesValidator("fieldValidator")
public class FieldValidator implements Validator{

	@Override
	public void validate(FacesContext context, UIComponent component, Object value)throws ValidatorException {
		
	        if (Pattern.matches("[a-zA-Z]+", (String) value)) {
	           String message = "Invalid value Please Enter 1-10";
	           throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_FATAL, message, null));
	            
	        }else
	        {
	        	//System.out.println("else");
	        	/* String message = "Sorry, validation has failed because [...]. Please try again.";
		            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));*/
	        }
	      
	    }
		
	}
    
 