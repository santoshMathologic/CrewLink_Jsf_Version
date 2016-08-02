package com.crew.adminbean;
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
import com.crew.model.CrewChangeDrivingsection;
import com.crew.service.CrewService;


@ManagedBean(name="crewchange",eager=true)
@ViewScoped
public class AdminCrewChangebean implements Serializable {
	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "schoolService", value = "#{schoolService}")
	private CrewService schoolService;
	private HttpSession httpSession = (HttpSession) FacesContext
				.getCurrentInstance().getExternalContext().getSession(false);
	private static final Logger CREW_LOGGER = Logger.getLogger(Paginationbean.class);
	
	private List<CrewChangeDrivingsection> drivingsectionlist;
	private ApplicationContext context;
	
	
	
	private  final int DEFAULT_RECORDS_NUMBER = 25;
    private final int DEFAULT_PAGE_INDEX = 1;
    private int records;
    private int recordsTotal;
    private int pageIndex;
    private int pages;
    
    private String searchStr;
  
	
	public AdminCrewChangebean() {
		
	}
	
	

	@PostConstruct
	private void initilization() {
			CreateList();
			repeatPagination();
		
	}
	public void repeatPagination()
	{   this.records = this.DEFAULT_RECORDS_NUMBER; //10
		this.pageIndex = this.DEFAULT_PAGE_INDEX;   //1
		
		drivingsectionlist = this.fetchCurrentList(this.getFirst(), this.getFirst()+records);
		
		
		
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
	
	
	public List<CrewChangeDrivingsection> loadlist()
	{	try {
			return (List<CrewChangeDrivingsection>) schoolService.loadList("from CrewChangeDrivingsection");
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CrewChangeDrivingsection> fetchCurrentList(int from, int to)
	{	return (List<CrewChangeDrivingsection>) loadlist().subList(from, to);
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
    {return drivingsectionlist.isEmpty();
    }
	
	
    public List<CrewChangeDrivingsection> getModel() {
		if(drivingsectionlist==null)
            updateModel();
		return drivingsectionlist;
	}

	public void setModel(List<CrewChangeDrivingsection> drivingsectionlist) {
		this.drivingsectionlist = drivingsectionlist;
	}

	
	public void searchResult()
	{	
		
		System.out.println(""+searchStr);
		if(searchStr!=null)
		{
			try {
				drivingsectionlist = 	(List<CrewChangeDrivingsection>) schoolService.loadList("from CrewChangeDrivingsection as cd where cd.trainNo like '"+searchStr.trim()+"'");
				System.out.println("if Size"+drivingsectionlist.size());
			} catch (CrewException e) {
				CREW_LOGGER.debug(e.getMessage());
				e.printStackTrace();
			}
				
		}
		else
		{
		try {
			drivingsectionlist = 	(List<CrewChangeDrivingsection>) schoolService.loadList("from CrewChangeDrivingsection");
			System.out.println("else Size"+drivingsectionlist.size());
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}	
		}
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

	private void CreateList() {
		context = new ClassPathXmlApplicationContext("Beans.xml");
		drivingsectionlist  = (List<CrewChangeDrivingsection>) context.getBean("list");
		
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







	public static Logger getCrewLogger() {
		return CREW_LOGGER;
	}



	public List<CrewChangeDrivingsection> getDrivingsectionlist() {
		return drivingsectionlist;
	}



	public void setDrivingsectionlist(
			List<CrewChangeDrivingsection> drivingsectionlist) {
		this.drivingsectionlist = drivingsectionlist;
	}



	public ApplicationContext getContext() {
		return context;
	}



	public void setContext(ApplicationContext context) {
		this.context = context;
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



	public int getDEFAULT_RECORDS_NUMBER() {
		return DEFAULT_RECORDS_NUMBER;
	}



	public int getDEFAULT_PAGE_INDEX() {
		return DEFAULT_PAGE_INDEX;
	}



	public String getSearchStr() {
		return searchStr;
	}



	public void setSearchStr(String searchStr) {
		this.searchStr = searchStr;
	}



	




	

}
