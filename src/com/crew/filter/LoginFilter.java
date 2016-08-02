package com.crew.filter;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.application.ResourceHandler;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.crew.bean.CommonLoginbean;




public class LoginFilter implements Filter,Serializable {
	private static final long serialVersionUID = 1L;
	@Override
	public void destroy() {
		
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		String[] pages = { "admin/alltrain.xhtml",
				"admin/drivingsections.xhtml", "logout.xhtml",
				"masteradmin/masterhome.xhtml", "admin/drivingsections.xhtml",
				"admin/drivingduty.xhtml", "admin/drivingdutyview.xhtml",
				"admin/viewdrivingduty.xhtml", "admin/viewrr.xhtml",
				"admin/viewroundtrips.xhtml", "admin/crewlink.xhtml",
				"masteradmin/createusers.xhtml", "viewusers.xhtml" };
		
		CommonLoginbean session = (CommonLoginbean) req.getSession().getAttribute("commonloginbean");
		String url = req.getRequestURI();
		
		///System.out.println("Url = ="+url);
		
		 if(session==null || !session.IsLogging)
		 {
			 if (!url.startsWith(req.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER)) { // Skip JSF resources (CSS/JS/Images/etc)
		            resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		            resp.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		            resp.setDateHeader("Expires", 0); // Proxies.
		        }	
			 
			if (url.indexOf(pages[0]) >= 0 || url.indexOf(pages[1]) >= 0
					|| url.indexOf(pages[2]) >= 0 || url.indexOf(pages[3]) >= 0
					|| url.indexOf(pages[4]) >= 0 || url.indexOf(pages[5]) >= 0
					|| url.indexOf(pages[6]) >= 0 || url.indexOf(pages[7]) >= 0
					|| url.indexOf(pages[8]) >= 0 || url.indexOf(pages[9]) >= 0
					|| url.indexOf(pages[10]) >= 0
					|| url.indexOf(pages[11]) >= 0
					|| url.indexOf(pages[12]) >= 0) {
				/*System.out
						.print("Inside admin/adminHome.xhtml and logout.xhtml");*/
				resp.sendRedirect(req.getServletContext().getContextPath()
						+ "/login.xhtml");
			}
			 else
			 {
				/* System.out.print("++++++else Part++++++++++++");*/
				 try
				 { chain.doFilter(request, response);
				 }catch(ServletException e)
				 { Throwable rootCause = e.getRootCause(); 
					 if (rootCause instanceof RuntimeException) { // This is true for any FacesException.
				            throw (RuntimeException) rootCause; // Throw wrapped RuntimeException instead of ServletException.
				        } else {
				            throw e;
				        }
				 }
				 
			 }
		 }else
		 {
			 
			 
			 if(url.indexOf("register.xhtml")>=0||url.indexOf("login.xhtml")>=0)
			 { 
				 
				
				 System.out.print("Inside register.xhtml and login.xhtml");
			  switch(session.getUserRoles())
				 {
				 case "admin":
					 			
					 			resp.sendRedirect(req.getServletContext().getContextPath()+"/masteradmin/masterhome.xhtml");
					 			break;
				
				 case "customer":
					 			
					 				resp.sendRedirect(req.getServletContext().getContextPath()+"/admin/alltrain.xhtml");
					   				break;
					 						
				 
				 }
				 
			 }
             else if(url.indexOf("logout.xhtml")>=0)
			 {  
			 	    req.getSession().removeAttribute("commonloginbean");
			 	    req.getSession().removeAttribute("COMMONLOGINID");
			 	  
			        
			 	/* --------  
			 	 LogOutMBean.logOut();  /// already My Session has been False so No need to call this Methods
			 	 */
			 	 
			 	/* System.out.print("Inside Logout.xhtml");*/
				 //resp.sendRedirect(req.getServletContext().getContextPath()+"/login.xhtml?msg=You have been logged out sucessfully"); /* first approch for Sending Message through url  */
				 resp.sendRedirect(req.getServletContext().getContextPath()+"/login.xhtml?logoutMsg=true"); // first approch
			 }
			 else
			 {   /*System.out.print("-------Else Part of Logout.xhtml-----");*/
			 try
			 {chain.doFilter(request, response);
			 }catch(ServletException e)
			 {
				 Throwable rootCause = e.getRootCause();  
				 if (rootCause instanceof RuntimeException) { // This is true for any FacesException.
			            throw (RuntimeException) rootCause; // Throw wrapped RuntimeException instead of ServletException.
			        } else {
			            throw e;
			        }
			 
			 }
		   }// end of try
		 }
		
		
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		 String testParam = config.getInitParameter("test-param");
         System.out.println("Test Param: " + testParam);
	}

}
