package com.crew.adminbean;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.crew.exception.CrewException;
import com.crew.model.Customer;
import com.crew.service.CrewService;
import com.crew.util.CrewUtilities;


@ManagedBean(name="paginationbean",eager=true)
@ViewScoped
public class Paginationbean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "schoolService", value = "#{schoolService}")
	private CrewService schoolService;
	private HttpSession httpSession = (HttpSession) FacesContext
				.getCurrentInstance().getExternalContext().getSession(false);

	private final int DEFAULT_RECORDS_NUMBER = 10;
    private final int DEFAULT_PAGE_INDEX = 1;
    private int records;
    private int recordsTotal;
    private int pageIndex;
    private int pages;
    private List<Customer> model;
    private static final Logger CREW_LOGGER = Logger.getLogger(Paginationbean.class);
	private ApplicationContext context;
	
	@PostConstruct
	private void initilization() {

		if (!(httpSession.getAttribute("COMMONLOGINID") == null)) {
			Long Id = (Long) httpSession.getAttribute("COMMONLOGINID");
			CreateList();
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
	
	
	}

	
	public void repeatPagination()
	{   this.records = this.DEFAULT_RECORDS_NUMBER; //10
		this.pageIndex = this.DEFAULT_PAGE_INDEX;   //1
		
		model = this.fetchCurrentList(this.getFirst(), this.getFirst()+records);
		
		System.out.println("Size of Model  = ="+model.size());
		
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
    {return model.isEmpty();
    }
	


	///  all getter and Setters
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


	public static Logger getCrewLogger() {
		return CREW_LOGGER;
	}

	public List<Customer> getModel() {
		if(model==null)
            updateModel();
		return model;
	}

	public void setModel(List<Customer> model) {
		this.model = model;
	}

	public int getDEFAULT_RECORDS_NUMBER() {
		return DEFAULT_RECORDS_NUMBER;
	}

	public int getDEFAULT_PAGE_INDEX() {
		return DEFAULT_PAGE_INDEX;
	}

}
