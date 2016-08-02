package com.crew.adminbean;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.crew.exception.CrewException;
import com.crew.model.Customer;
import com.crew.service.CrewService;
import com.crew.util.CrewUtilities;


@ManagedBean(name="disableAccountbean",eager=true)
@ViewScoped
public class DisableAccountbean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "schoolService", value = "#{schoolService}")
	private CrewService schoolService;
	private HttpSession httpSession = (HttpSession) FacesContext
			.getCurrentInstance().getExternalContext().getSession(false);
	private static final Logger CREW_LOGGER = Logger.getLogger(AdminviewUsersbean.class);
	
	private ApplicationContext context;
	private List<Customer> customerListDisableEnablelist;
	private Customer customerObject;
	private boolean IsDisable;
	private Customer Customerupdate;
	
	//page
	private final int DEFAULT_RECORDS_NUMBER = 20;
    private final int DEFAULT_PAGE_INDEX = 1;
    private int records;
    private int recordsTotal;
    private int pageIndex;
    private int pages;
	
	
	
	public DisableAccountbean() {
	}
	
	@PostConstruct
	private void initilization() {

		if (!(httpSession.getAttribute("COMMONLOGINID") == null)) {
			Long Id = (Long) httpSession.getAttribute("COMMONLOGINID");
			System.out.print("Loging Id = =" + Id);
			CreateList();
			loadUserAccount();
			repeatPagination();

		} else {
			try {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(CrewUtilities.getRedirectContext() + "/login.xhtml");
			} catch (IOException e) {
				CREW_LOGGER.debug(e.getMessage());
			}

		}

	}

	private void CreateList()
	{
		context = new ClassPathXmlApplicationContext("Beans.xml");
		customerListDisableEnablelist = (List<Customer>) context.getBean("list");
		customerObject = (Customer) context.getBean("customer");
		Customerupdate = (Customer) context.getBean("customer");
		 
	}
	
	
	public void loadUserAccount()
	{
		try {
			customerListDisableEnablelist = (List<Customer>) this.schoolService.loadList("from Customer");
		} catch (CrewException e) {
		    CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
	}

	
	public void repeatPagination()
	{   this.records = this.DEFAULT_RECORDS_NUMBER; //10
		this.pageIndex = this.DEFAULT_PAGE_INDEX;   //1
		
		customerListDisableEnablelist = this.fetchCurrentList(this.getFirst(), this.getFirst()+records);
		
		
		
		this.recordsTotal  = this.getListSize();
		System.out.println("Total Rcords "+recordsTotal);
		if(records>0)
		{	
			pages = records <= 0 ? 1:recordsTotal/records;
			System.out.println("Pages "+pages);
			if(recordsTotal%records>0){	pages++;System.out.println("Inc Pages "+pages);}
			if (pages == 0) {pages = 1;	System.out.println("Pages "+pages);}
			
		}
		else
		{
			records = 1;
            pages = 1;
		}
		updateModel();
		
	}
	
	
	public List<Customer> loadlist()
	{	try {
			return (List<Customer>) schoolService.loadList("from Customer");
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Customer> fetchCurrentList(int from, int to)
	{	return (List<Customer>) loadlist().subList(from, to);
	}
	

	public int getListSize()
	{	return loadlist().size();
	}
	
	
	 public void updateModel() {
	        int fromIndex = getFirst();
	        int toIndex = getFirst() + records;
	        if(toIndex > this.recordsTotal)
	        {
	            toIndex = this.recordsTotal;
	        }
	       setModel(fetchCurrentList(fromIndex, toIndex));
	    }
	
	public void next() {
        if(this.pageIndex < pages) {
            this.pageIndex++;
        }

        updateModel();
    }

    public void prev() {
        if(this.pageIndex > 1) {
            this.pageIndex--;
        }

        updateModel();
    }   

    public int getFirst() {
        return (pageIndex * records) - records;
    }


	
    
    public boolean listEmpty()
    {return customerListDisableEnablelist.isEmpty();
    }
	
    public List<Customer> getModel() {
		if(customerListDisableEnablelist==null)
            updateModel();
		return customerListDisableEnablelist;
	}

	public void setModel(List<Customer> customerListDisableEnablelist) {
		this.customerListDisableEnablelist = customerListDisableEnablelist;
	}
	
	
	
	public void isEnable()
	{
	if(mapString()!=0)
		{
			try {
				 customerObject =  (Customer) schoolService.loadUniqueObject("from Customer as cst where cst.id = "+mapString());
				
				System.out.println(""+customerObject.getUsername());
			} catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
			}	
		}
		
		
	}
	

public void account()
{  
	 System.out.println("selectedId = ="+IsDisable);
	 
	 System.out.println("CheckedId = ="+mapString());
    try {
		Customerupdate = (Customer) schoolService.loadUniqueObject("from Customer as cst where cst.id="+mapString());
		Customerupdate.setAccount(IsDisable);
		schoolService.updateObjectInDb(Customerupdate);
		//load();
		
	/*	if(IsDisable)
		{FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Account has been enabled", "Account has been enabled"));		
		}
		else
		{FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Account has been disabled enabled", "Account has been enabled"));
		}*/
	} catch (CrewException e) {
		CREW_LOGGER.debug(e.getMessage());
		e.printStackTrace();
	}
    
   
}
	
	
	
	
	 
	private long mapString() {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String, String> params = fc.getExternalContext()
				.getRequestParameterMap();
		return (Long.parseLong(params.get("QUERYPARAM")));
	}
	
	
	public void updateRecords()
	{try {
				schoolService.updateObjectInDb(customerObject);
				loadUserAccount();
				//listener();
		 } catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
			}
			
		
	}
	
	
	public void listener() {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
			ec.redirect(ec.getRequestContextPath() + "/masteradmin/disableAccount.xhtml");
		} catch (IOException e) {
		
			e.printStackTrace();
		}
	}	

	
	
	
	
	
	
	///  gettter and setter methods 
	

	public CrewService getSchoolService() {
		return schoolService;
	}

	public void setSchoolService(CrewService schoolService) {
		this.schoolService = schoolService;
	}

	public HttpSession getHttpSession() {
		return httpSession;
	}

	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}

	public ApplicationContext getContext() {
		return context;
	}

	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	public List<Customer> getCustomerListDisableEnablelist() {
		return customerListDisableEnablelist;
	}

	public void setCustomerListDisableEnablelist(
			List<Customer> customerListDisableEnablelist) {
		this.customerListDisableEnablelist = customerListDisableEnablelist;
	}

	public Customer getCustomerObject() {
		return customerObject;
	}

	public void setCustomerObject(Customer customerObject) {
		this.customerObject = customerObject;
	}

	public boolean isIsDisable() {
		return IsDisable;
	}

	public void setIsDisable(boolean isDisable) {
		IsDisable = isDisable;
	}

	public Customer getCustomerupdate() {
		return Customerupdate;
	}

	public void setCustomerupdate(Customer customerupdate) {
		Customerupdate = customerupdate;
	}

	public int getRecords() {
		return records;
	}

	public void setRecords(int records) {
		this.records = records;
	}

	public int getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public static Logger getCrewLogger() {
		return CREW_LOGGER;
	}

	public int getDEFAULT_RECORDS_NUMBER() {
		return DEFAULT_RECORDS_NUMBER;
	}

	public int getDEFAULT_PAGE_INDEX() {
		return DEFAULT_PAGE_INDEX;
	}
	
	

}
