package com.crew.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Properties;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;




public class ReadsDifferentsFilesMbean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger SCHOOL_LOGGER = Logger.getLogger(ReadsDifferentsFilesMbean.class);

	public ReadsDifferentsFilesMbean()
	{
		
	}
	
	public void initialization()
	{
		try {
		Properties props = 	getPropertiesFromClasspath("prop.properties");
		Enumeration<?> enuKeys = props.keys();
		while (enuKeys.hasMoreElements()) {
			String key = (String) enuKeys.nextElement();
			String value = props.getProperty(key);
			//System.out.println(key + ": " + value);
		}
		
	
	} catch (IOException e) {
		
		//System.out.println(e.getMessage());
	}
		
	}

	public static Logger getSchoolLogger() {
		return SCHOOL_LOGGER;
	}
	
	private Properties getPropertiesFromClasspath(String propFileName) throws IOException {
		Properties props = new Properties();
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(propFileName);
		
		if (!(inputStream == null)) {
			props.load(inputStream); 
		}
		else
		{
			throw new FileNotFoundException("property file '" + propFileName
					+ "' not found in the classpath");
		}
		inputStream.close();
		return props;
		
     }
	
	 public Properties loadProperties()
	 {
		 String workingDir = System.getProperty("user.dir");
		 File temp = new File(workingDir, "prop.properties");
		 
		 InputStream resourceAsStream =null; 
		 Properties  properties = null;

		 try {
			 properties = new Properties();
		     resourceAsStream = new FileInputStream(temp);
		     if (!(resourceAsStream == null)) 
		     {properties.load(resourceAsStream);
		     }   
		 } catch (IOException e) {
		     //System.out.print("File not found "+e.getMessage());
			 e.printStackTrace();
		     
		 }
		 
		 return properties;
	
	 }
	
	 public void getpath()
		{
			 System.err.println(Paths.get("prop.properties").toAbsolutePath().toString());
			 System.err.println(System.getProperty("user.dir"));
		}
	 public void writetoPropertiesfiles()
	 {
		 try (FileOutputStream writeToStockFile = new FileOutputStream(
					"prop.properties")) {
				Properties writeStockProperty = new Properties();
				StringBuffer key = new StringBuffer();
				StringBuffer value = new StringBuffer();
				{
				key.append("santosh");
				value.append("1cd09mca13");

				key.append("sanjay");
				value.append("1cd09mca14");
				
				key.append("Prabhu");
				value.append("1cd09mca15");
				
				key.append("Mahes");
				value.append("1cd09mca16");
				
				key.append("raja");
				value.append("1cd09mca17");
				
				}

					writeStockProperty
							.setProperty(key.toString(), value.toString());

				
				writeStockProperty.store(writeToStockFile, null);
		 }
		 catch(IOException e)
		 {
			 e.printStackTrace();
		 }
	 }

	 @SuppressWarnings({ "unchecked", "unused" })
	 public Boolean writeIntoProperties(String keys,String values) throws IOException
	 {
		 boolean bBoolean = Boolean.FALSE;
		 Properties prop = new Properties();
		 InputStream in = this.getClass().getResourceAsStream("/prop.properties");
		 
		 if(!(in==null))
		 {
			 prop.load(in);
		 	 prop.setProperty(keys,values);
		 	
		 	 prop.store(new FileOutputStream("prop.properties"), "This is an optional header comment string");
		 	 bBoolean= Boolean.TRUE;
		 }
		 
		 
		 return bBoolean;
	 }
	 
	 public void redirect() {

			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) context
					.getExternalContext().getResponse();
			try {
				response.sendRedirect("sector.jsf");
			} catch (IOException e) {

				e.printStackTrace();
			}

		}

	
}
