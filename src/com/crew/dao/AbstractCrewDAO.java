/* 
 * AbstractESchoolDAO.java
 * 
 * Version $1.0 
 * 
 * Jan 16, 2013
 * 
 Copyright notice */

/*
 ****************************************************************************
 *                                                                      	*
 *  This listing/file contains confidential and proprietary information of 	*
 *  Multiinnovate Technologies, eSchool Management Web Application. It is not to be *
 *	used or copied without the prior written approval of Multiinnovate*
 *  Technologies, eSchool Management Web Application.							*
 ****************************************************************************
 *   Multiinnovate Web Application*
 *
 *  Title		:  AbstractESchoolDAO.java
 *  Author		:  Khader Basha Shaik
 *  Date		:  Jan 16, 2013
 *  Language	:  Java
 *
 ****************************************************************************
 *                                                                          *
 * All Rights Reserved by  Multiinnovate Technologies, eSchool Management Web		*
 * Application  															*
 *                                                                          *
 ****************************************************************************
 */

package com.crew.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

public abstract class AbstractCrewDAO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	protected SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
