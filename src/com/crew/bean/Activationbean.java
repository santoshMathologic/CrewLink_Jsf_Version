package com.crew.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;



@ManagedBean(name="activationbean",eager=true)
@RequestScoped
public class Activationbean implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	 @ManagedProperty(value="#{param.key}")
	 private String key;
	 private boolean valid;

	public Activationbean() {
		
	}
	
	@PostConstruct
    public void init() {
        // Get User based on activation key.
        // Delete activation key from database.
        // Login user.
		
		valid = true;
		
    }

	
	
	public void checkkey()
	{
		System.out.print("------------The Key is ---------------"+key);
	}
	
	
	
	//  all getter and Setters are Here
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}


}
