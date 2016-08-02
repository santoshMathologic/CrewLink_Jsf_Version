package com.crew.adminbean;


import java.awt.print.Book;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.crew.exception.CrewException;
import com.crew.model.Customer;
import com.crew.service.CrewService;
import com.crew.util.CrewUtilities;


@ManagedBean(name="adminviewusers",eager=true)
@ViewScoped
public class AdminviewUsersbean implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "schoolService", value = "#{schoolService}")
	private CrewService schoolService;
	private HttpSession httpSession = (HttpSession) FacesContext
			.getCurrentInstance().getExternalContext().getSession(false);
	private static final Logger CREW_LOGGER = Logger.getLogger(AdminviewUsersbean.class);
	
	private ApplicationContext context;
	private List<Customer> CustomerList;
	private Long deletedId;
	private Long accountdisableId;
	private boolean IsDisable;
	private Customer Customerupdate;
	private Customer update;
	
	private Customer cst;
	
	/// 2nd Approch 
	private boolean Bdisabled = Boolean.FALSE;
	private boolean bchecked;
	
	
	
	//// search
	
	private String strSearch;
	private String strDisplayMessages;
    private boolean messageBoolean;	
	
	
    //// sorting 
    
    private boolean sortAscending = true; 
	
	
	
	
	
	//   page 
	
	private final int DEFAULT_RECORDS_NUMBER = 20;
    private final int DEFAULT_PAGE_INDEX = 1;
    private int records;
    private int recordsTotal;
    private int pageIndex;
    private int pages;

	
	@PostConstruct
	private void initilization() {

		if (!(httpSession.getAttribute("COMMONLOGINID") == null)) {
			Long Id = (Long) httpSession.getAttribute("COMMONLOGINID");
			System.out.print("Loging Id = =" + Id);
			CreateList();
			repeatPagination();
			//load();

		} else {
			try {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(CrewUtilities.getRedirectContext() + "/login.xhtml");
			} catch (IOException e) {
				CREW_LOGGER.debug(e.getMessage());
			}

		}

	}


	private void load() {
	
		
		if(!(CustomerList==null))
		{
		try {
			CustomerList = (List<Customer>) this.schoolService.loadList("from Customer");
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
		}
		
	}



	private void CreateList()
	{
		context = new ClassPathXmlApplicationContext("Beans.xml");
		CustomerList = (List<Customer>) context.getBean("list");
		Customerupdate = (Customer) context.getBean("customer");
		update = (Customer) context.getBean("customer");
	    cst =(Customer) context.getBean("customer");
	    messageBoolean  = Boolean.FALSE;
		 
	}

	public void deleteusers()
	{
	System.out.print("Deleted Id"+this.deletedId);
		
		try {
			schoolService.deleteAdmin(deletedId);
			removefromList();
			CrewUtilities.displayMessage("Info","User has been deleted");
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	private void removefromList()
	{	for (int nIndex = 0; nIndex < CustomerList.size(); nIndex++) {
			if (CustomerList.get(nIndex).getId() == deletedId) {
				CustomerList.remove(nIndex);
			}
		}
	}
	
	
	
	
	


	 public void accountDisabled()
	 {  
		 System.out.println("selectedId = ="+IsDisable);
		 System.out.println("CheckedId = ="+mapString());
		 
	     try {
			Customerupdate = (Customer) schoolService.loadUniqueObject("from Customer as cst where cst.id="+mapString());
			Customerupdate.setAccount(IsDisable);
			schoolService.updateObjectInDb(Customerupdate);
			if(IsDisable)
			{FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Account has been enabled", "Account has been enabled"));		
			}
			else
			{FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Account has been disabled enabled", "Account has been enabled"));
			}
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
	


public void distinctsExcecutes()
{
	//this.schoolService.select_DISTINCT("NULL","NULL");
	
}
	
public void update(ActionEvent event)
{
	long  retrivedId = (long) event.getComponent().getAttributes().get("passId");
	
	try {
		update =(Customer) this.schoolService.loadUniqueResultObject(retrivedId, Customer.class, "get");
		
	    cst = 	(Customer) this.schoolService.loadUniqueObject("from Customer as cst where cst.id = "+retrivedId);
	} catch (CrewException e) {
		CREW_LOGGER.debug(e.getMessage());
		e.printStackTrace();
	}
}


public void saveUpdate()
{
	
	System.out.println(""+update.getPassword());
	System.out.println(""+cst.getPassword());
	
	try {
		schoolService.updateObjectInDb(cst);
	} catch (CrewException e) {
	    CREW_LOGGER.debug(e.getMessage());
		e.printStackTrace();
	}	

    load();
	RequestContext context = RequestContext.getCurrentInstance();
	context.execute("PF('editDlg').hide();");
	
	
	
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
	






public String sortByFirstName()
{	if(sortAscending)
	{
		 Collections.sort(CustomerList, new Comparator<Customer>() {
		        @Override
		        public int compare(Customer o1, Customer o2)
		        {
		        	 String firstname1 = o1.getFirstname();
		      	     String firstname2 =  o2.getFirstname();
		             return firstname1.compareTo(firstname2);
		        }
		      });
		 
		 sortAscending = false;
	}
	else
	{
		Collections.sort(CustomerList, new Comparator<Customer>() {

			@Override
			public int compare(Customer o1, Customer o2) {
				 String firstname1 = o1.getFirstname();
	      	     String firstname2 =  o2.getFirstname();
	             return firstname2.compareTo(firstname1);
			}
	       
	      });
		sortAscending = true;
	}
	
	
	return null;
	
}





////  santosh page 

public void repeatPagination()
{   this.records = this.DEFAULT_RECORDS_NUMBER; //10
	this.pageIndex = this.DEFAULT_PAGE_INDEX;   //1
	
	CustomerList = this.fetchCurrentList(this.getFirst(), this.getFirst()+records);
	this.recordsTotal  = this.getListSize();
	System.out.println("Total Rcords "+recordsTotal);
	if(records>0)
	{	
		pages = records <= 0 ? 1:recordsTotal/records;
		System.out.println("Pages "+pages);
		if(recordsTotal%records>0){	pages++;}
		if (pages == 0) {pages = 1;}
		
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
{return CustomerList.isEmpty();
}
public List<Customer> getModel() {
	if(CustomerList==null)
        updateModel();
	return CustomerList;
}

public void setModel(List<Customer> model) {
	this.CustomerList = model;
}



@SuppressWarnings("unchecked")
public void searchUsers()
{
	
	System.out.println("dasdasdas");
	System.out.println(""+this.strSearch);
	
	if(strSearch!=null|| !strSearch.isEmpty())
	{
	//System.out.println(""+strSearch.equalsIgnoreCase(strSearch));	
		
		try {
			CustomerList  = (List<Customer>) schoolService.loadList("from Customer as cst where cst.firstname like '"+strSearch.trim()+"'");
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}	
	}
	
	if(CustomerList.isEmpty())
	{
		messageBoolean =  Boolean.TRUE;
		System.out.println("dasdasd");
		String displayMessages =  "No Records Found ";
		strDisplayMessages = displayMessages;
		FacesMessage message =
                new FacesMessage(FacesMessage.SEVERITY_ERROR,"No Records Foun","No Records Found ");
                
	}
	
	
}





















	
	// all Getter and Setter are beginning from here
	
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



	public List<Customer> getCustomerList() {
		return CustomerList;
	}



	public void setCustomerList(List<Customer> customerList) {
		CustomerList = customerList;
	}

	public Long getDeletedId() {
		return deletedId;
	}

	public void setDeletedId(Long deletedId) {
		this.deletedId = deletedId;
	}

	public boolean isIsDisable() {
		return IsDisable;
	}

	public void setIsDisable(boolean isDisable) {
		IsDisable = isDisable;
	}

	public Long getAccountdisableId() {
		return accountdisableId;
	}

	public void setAccountdisableId(Long accountdisableId) {
		this.accountdisableId = accountdisableId;
	}

	public Customer getCustomerupdate() {
		return Customerupdate;
	}

	public void setCustomerupdate(Customer customerupdate) {
		Customerupdate = customerupdate;
	}


	public boolean isBdisabled() {
		return Bdisabled;
	}


	public void setBdisabled(boolean bdisabled) {
		Bdisabled = bdisabled;
	}



	public Customer getUpdate() {
		return update;
	}


	public void setUpdate(Customer update) {
		this.update = update;
	}


	public Customer getCst() {
		return cst;
	}


	public void setCst(Customer cst) {
		
		this.cst = cst;
	}


	public boolean isBchecked() {
		return bchecked;
	}


	public void setBchecked(boolean bchecked) {
		this.bchecked = bchecked;
		System.out.println("dasdas"+bchecked);
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


	public static long getSerialversionuid() {
		return serialVersionUID;
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


	public String getStrSearch() {
		return strSearch;
	}


	public void setStrSearch(String strSearch) {
		this.strSearch = strSearch;
	}


	public String getStrDisplayMessages() {
		return strDisplayMessages;
	}


	public void setStrDisplayMessages(String strDisplayMessages) {
		this.strDisplayMessages = strDisplayMessages;
	}


	public boolean isMessageBoolean() {
		return messageBoolean;
	}


	public void setMessageBoolean(boolean messageBoolean) {
		this.messageBoolean = messageBoolean;
	}


	public boolean isSortAscending() {
		return sortAscending;
	}


	public void setSortAscending(boolean sortAscending) {
		this.sortAscending = sortAscending;
	}

	



	
	
	
	
	
	
	

}
