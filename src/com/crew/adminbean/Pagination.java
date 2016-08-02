package com.crew.adminbean;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.crew.exception.CrewException;
import com.crew.service.CrewService;


@ManagedBean(name="pagenationbean",eager=true)
@ViewScoped
public class Pagination implements Serializable {

	@ManagedProperty(name = "schoolService", value = "#{schoolService}")
	private CrewService schoolService;
	private static final int DEFAULT_RECORDS_NUMBER = 10;
    private static final int DEFAULT_PAGE_INDEX = 1;
    private int records;
    private int recordsTotal;
    private int pageIndex;
    private int pages;
    private List<?> model;
    private static final Logger CREW_LOGGER = Logger.getLogger(Pagination.class);
	private ApplicationContext context;
	private String commontableName;
	private static final long serialVersionUID = 1L;
	
	@PostConstruct
	private void initilization() 
	{CreateList();
	}
	private void CreateList()
	{context = new ClassPathXmlApplicationContext("Beans.xml");
	}
	public List<?> repeatPagination(int DEFAULT_RECORDS_NUMBER,int DEFAULT_PAGE_INDEX,String commonClass)
	{   this.records = DEFAULT_RECORDS_NUMBER; //2
		this.pageIndex = DEFAULT_PAGE_INDEX;   //1
		commontableName = commonClass;
		
		model = (List<?>)fetchCurrentList(getFirst(),getFirst()+records);
		recordsTotal  = getListSize();
		if(records>0)
		{	pages = records <= 0 ? 1:recordsTotal/records;
			if(recordsTotal%records>0){	pages++;}
			if (pages == 0) {pages = 1;}
		}
		else
		{records = 1;
         pages = 1;
		}
		updateModel();
		return model;
	}
	
	public List<?> loadlist()
	{	StringBuffer stringQuery = new StringBuffer();
		stringQuery.append("from ").append(commontableName);
		try {
			return (List<?>) schoolService.loadList(stringQuery.toString());
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}	
	public List<?> fetchCurrentList(int from, int to)
	{	return (List<?>) loadlist().subList(from, to);
	}
	

	public int getListSize()
	{	return loadlist().size();
	}
	
	
	 public void updateModel() {
	        int fromIndex = getFirst();
	        int toIndex = getFirst() + records;
	        if(toIndex > recordsTotal)
	        {toIndex = recordsTotal;
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
    	System.out.println("in Prev");
        if(this.pageIndex > 1) {
            this.pageIndex--;
        }

        updateModel();
    }   

    
    
    
    ///getter and Setters 
    
    
    public int getFirst() {
        return (pageIndex * records) - records;
    }

	
    
    public boolean listEmpty()
    {return model.isEmpty();
    }

	public CrewService getSchoolService() {
		return schoolService;
	}

	public void setSchoolService(CrewService schoolService) {
		this.schoolService = schoolService;
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

	

	public ApplicationContext getContext() {
		return context;
	}

	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	public static int getDefaultRecordsNumber() {
		return DEFAULT_RECORDS_NUMBER;
	}

	public static int getDefaultPageIndex() {
		return DEFAULT_PAGE_INDEX;
	}

	public static Logger getCrewLogger() {
		return CREW_LOGGER;
	}

	public String getTableName() {
		return commontableName;
	}

	public void setTableName(String commontableName) {
		this.commontableName = commontableName;
	}
	public List<?> getModel() {
		if(model==null)
            updateModel();
		return model;
	}
	public void setModel(List<?> model) {
		this.model = model;
	}
	public String getCommontableName() {
		return commontableName;
	}
	public void setCommontableName(String commontableName) {
		this.commontableName = commontableName;
	}
	



}
