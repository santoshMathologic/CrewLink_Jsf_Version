package com.crew.bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;


@ViewScoped
@ManagedBean(name="testBean",eager=true)

public class TestBean {

	private String nameField;
	public TestBean() {
	}
	
	public void test() 
	{
		if (nameField.length() == 0) 
		{
			System.out.println("Empty");
			FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "fields is Emp",
                    "The email address you entered is not valid.");
			
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, message);
			
           
            			  
		} else 
		{
		   
		}
	
	}

	public String getNameField() {
		return nameField;
	}

	public void setNameField(String nameField) {
		this.nameField = nameField;
	}
	
	
}
