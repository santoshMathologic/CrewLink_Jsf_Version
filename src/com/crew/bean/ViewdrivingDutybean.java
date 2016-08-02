package com.crew.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.crew.exception.CrewException;
import com.crew.model.CrewChangeDrivingsection;
import com.crew.model.DrivingDutyOfPilotDrivingSection;
import com.crew.model.PilotJourney;
import com.crew.service.CrewService;
import com.crew.util.CrewUtilities;




@ManagedBean(name="viewdrivingduty",eager=true)
@ViewScoped
@SuppressWarnings("unchecked")
public class ViewdrivingDutybean implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(name = "schoolService", value = "#{schoolService}")
	private CrewService schoolService;
	private HttpSession httpSession = (HttpSession) FacesContext
			.getCurrentInstance().getExternalContext().getSession(false);
	private static final Logger CREW_LOGGER = Logger.getLogger(ViewdrivingDutybean.class);
	
	private List<DrivingDutyOfPilotDrivingSection> drivingdutyview;
	private List<DrivingDutyOfPilotDrivingSection> filterdrivingdutyview;
	
	private ApplicationContext context;
	 private String selectedFrom;
	 private String selectedFrom1;
	 private String selectedTo;
	 private String selectedTo1;
	 
	 private List<SelectItem> selectlistfrom_station;
	 private List<SelectItem> To_stationlist;
	 private long customerloginId;
	 private List<CrewChangeDrivingsection> listCrewChange;
	 private List<PilotJourney> listPilotJourney;
	 
	 private List<DrivingDutyOfPilotDrivingSection> selectionList;
	 
	 private  CrewChangeDrivingsection ds;
	 private  List<PilotJourney> listPJ;
		
	
	 
	
	@PostConstruct
	private void initilization() {
		
		if (!(httpSession.getAttribute("COMMONLOGINID") == null)) {
			customerloginId = (Long) httpSession.getAttribute("COMMONLOGINID");
			createList();
			
			loaddrivingduty();
			commonselectdrivindDuty();
		}
		else {
				try {
					FacesContext.getCurrentInstance().getExternalContext()
					.redirect(CrewUtilities.getRedirectContext() + "/login.xhtml");
					} catch (Exception e) {
						CREW_LOGGER.debug(e.getMessage());
					}
			}
	}
	
		
	public void commonselectdrivindDuty() {
		List<DrivingDutyOfPilotDrivingSection> drivingdutylist = new ArrayList<DrivingDutyOfPilotDrivingSection>();
		
		 HashMap<String,String> to_station=new HashMap<String,String>();  
		 HashMap<String,String> from_station=new HashMap<String,String>();
		
		  
		
		
		try {
			drivingdutylist = (List<DrivingDutyOfPilotDrivingSection>) schoolService
					.loadList("from DrivingDutyOfPilotDrivingSection");
		
			  
			if (!drivingdutylist.isEmpty()) {
				for (DrivingDutyOfPilotDrivingSection dds : drivingdutylist) {
					to_station.put(dds.getToStation(), dds.getToStation());
					from_station
							.put(dds.getFromStation(), dds.getFromStation());
				}

				for (Map.Entry<String, String> m : from_station.entrySet()) {

					selectlistfrom_station.add(new SelectItem(m.getKey()));

				}

				for (Map.Entry<String, String> to_entry : to_station.entrySet()) {

					To_stationlist.add(new SelectItem(to_entry.getKey()));
				}

			}

		} catch (CrewException e) {
			e.printStackTrace();
			CREW_LOGGER.debug(e.getMessage());
		}

	}
	
	private void loaddrivingduty()
	{
		try {
			setDrivingdutyview((List<DrivingDutyOfPilotDrivingSection>) schoolService.loadList("from DrivingDutyOfPilotDrivingSection as dds where dds.customer.id = "+customerloginId));
			
			filterdrivingdutyview= (List<DrivingDutyOfPilotDrivingSection>)schoolService.loadList("from DrivingDutyOfPilotDrivingSection as dds where dds.customer.id = "+customerloginId);
		} catch (CrewException e) {
			CREW_LOGGER.debug(e.getMessage());
			e.printStackTrace();
		}
	}

	private void createList()
	{ context = new ClassPathXmlApplicationContext("Beans.xml");
	  setDrivingdutyview((List<DrivingDutyOfPilotDrivingSection>) context.getBean("list"));
	  filterdrivingdutyview  = (List<DrivingDutyOfPilotDrivingSection>) context.getBean("list");
	  selectlistfrom_station = new  ArrayList<SelectItem>();
	  To_stationlist = new ArrayList<SelectItem>();
	  
	  listCrewChange = (List<CrewChangeDrivingsection>) context.getBean("list");
      listPilotJourney=(List<PilotJourney>) context.getBean("list");
      selectionList = (List<DrivingDutyOfPilotDrivingSection>) context.getBean("list");
      ds =  (CrewChangeDrivingsection) context.getBean("crewChange");
      listPJ = (List<PilotJourney>) context.getBean("list");
	  
	}
	
	
	
	
	public void  searchDrivingDuty()
	{
		
		
		 if(selectedFrom!=null||selectedTo!=null||selectedFrom1!=null || selectedTo1!=null)
		 {
			if(selectedFrom!=null &&  !selectedFrom.isEmpty() && selectedTo!=null && !selectedTo.isEmpty() && selectedFrom1!=null && !selectedFrom1.isEmpty() && selectedTo1!=null && !selectedTo1.isEmpty())
			{
			 System.out.println("Search 1");
			 drivingdutyview = (List<DrivingDutyOfPilotDrivingSection>)  schoolService.searchdrivingduty2(selectedFrom, selectedTo, selectedFrom1, selectedTo1);
			}
	     else if(selectedFrom1!=null && !selectedFrom1.isEmpty() && selectedTo1!=null && !selectedTo1.isEmpty())
			{
			 	System.out.println("Search 2");
				drivingdutyview = (List<DrivingDutyOfPilotDrivingSection>) schoolService.searchdrivingduty1(selectedFrom1, selectedTo1);
			}
		 else if(selectedFrom!=null  &&  !selectedFrom.isEmpty() && selectedTo!=null && !selectedTo.isEmpty())
		 {
			 System.out.println("Search 3");
			drivingdutyview  =	(List<DrivingDutyOfPilotDrivingSection>) schoolService.searchdrivingduty(selectedFrom, selectedTo);
		    System.out.println(""+drivingdutyview.size());
		 }
		 else if(!selectedFrom.isEmpty())
				{
				 System.out.println("Singgle Search From");
				 drivingdutyview = (List<DrivingDutyOfPilotDrivingSection>) schoolService.singleviewSearch(selectedFrom,"FROM");
				}
				else if(!selectedTo.isEmpty())
				{drivingdutyview = (List<DrivingDutyOfPilotDrivingSection>) schoolService.singleviewSearch(selectedTo,"TO");
				}
				else if(!selectedFrom1.isEmpty())
				{drivingdutyview = (List<DrivingDutyOfPilotDrivingSection>) schoolService.singleviewSearch(selectedFrom1,"FROM1");
				}
				else if(!selectedTo1.isEmpty())
				{drivingdutyview = (List<DrivingDutyOfPilotDrivingSection>) schoolService.singleviewSearch(selectedTo1,"TO1");
				}
		 else
			{
				System.out.println("No selection Made");
			}
		 }
			
		
	
		
	
	}
		
	
	
	public void deleteFromDrivingDuty(DrivingDutyOfPilotDrivingSection d)
	{
		
		Long lTransactionId = new Long(0);
		
		lTransactionId = d.getTransactionNoDsToDd();
		DrivingDutyOfPilotDrivingSection DDPT= (DrivingDutyOfPilotDrivingSection) context.getBean("drivingdutyofpilotdrivingsection");
		CrewChangeDrivingsection ds  =  (CrewChangeDrivingsection) context.getBean("crewChange");
		List<PilotJourney> listPJ = (List<PilotJourney>) context.getBean("list");
		try {
			ds = (CrewChangeDrivingsection) schoolService
					.loadUniqueObject("from CrewChangeDrivingsection as CD where CD.transactionNoDsToDd="
							+ lTransactionId);
			listPJ = (List<PilotJourney>) schoolService
					.loadList("from PilotJourney as CD where CD.transactionNoDsToDd="
							+ lTransactionId);
			for(PilotJourney p: listPJ)
			{
				p.setTransactionNoDsToDd((long)0);
				schoolService.updateObjectInDb(p);
			}
			//ds.setStatus(false);
			schoolService.updateObjectInDb(ds);
		} catch (CrewException e1) {
			CREW_LOGGER.debug(e1.getMessage());
			e1.printStackTrace();
			CREW_LOGGER.debug(e1.getMessage());
		}

		try {
			schoolService.deleteById(d.getId(), "DrivingDutyOfPilotDrivingSection");
			remove(d.getId());
			schoolService.update("CrewChangeDrivingsection",lTransactionId);
		} catch (Exception e) {
			e.printStackTrace();
			CREW_LOGGER.debug(e.getMessage());
		}
	}
	
	
	private void remove(long deletedId)
	{	for(int nIndex = 0;nIndex<drivingdutyview.size();nIndex++)
		{	 if(drivingdutyview.get(nIndex).getId()==deletedId)
			 { drivingdutyview.remove(nIndex); 
			 }
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void viewDrivingSectionsFromDrvingDuty(DrivingDutyOfPilotDrivingSection d)
	{	Long lTransactionId = new Long(0);
		lTransactionId = d.getTransactionNoDsToDd();
		
		try {
			listCrewChange = (List<CrewChangeDrivingsection>) 
					schoolService.loadList("from CrewChangeDrivingsection as cc where cc.transactionNoDsToDd =" +lTransactionId);
			listPilotJourney = (List<PilotJourney>) schoolService.loadList("from PilotJourney as cc where cc.transactionNoDsToDd =" +lTransactionId);
			
		} catch (CrewException e) {
			e.printStackTrace();
			CREW_LOGGER.debug(e.getMessage());
		}
		
				
	}

	
	// filter value of datatable with upper case and lowercase letter 
			public boolean filterByName(Object value, Object filter, Locale locale) {
			    String filterText = (filter == null) ? null : filter.toString().trim();
			    if (filterText == null || filterText.equals("")) {
			        return true;
			    }

			    if (value == null) {
			        return false;
			    }

			    String drivingduty = value.toString().toUpperCase();
			    filterText = filterText.toUpperCase();

			    if (drivingduty.contains(filterText)) {
			        return true;
			    } else {
			        return false;
			    }
			}
	
	
	
	
	
	/////  Multiple Delete Option 
			
			public void delete()
			{
				
				System.out.println(""+selectionList.size());
				Long lTransactionId = new Long(0);
				
			
				for(DrivingDutyOfPilotDrivingSection ddps:selectionList)
				{
					lTransactionId = ddps.getTransactionNoDsToDd();
					
					try {
						ds = (CrewChangeDrivingsection) schoolService
								.loadUniqueObject("from CrewChangeDrivingsection as CD where CD.transactionNoDsToDd="
										+ lTransactionId);
						
						listPJ = (List<PilotJourney>) schoolService
								.loadList("from PilotJourney as CD where CD.transactionNoDsToDd="
										+ lTransactionId);
						for(PilotJourney p: listPJ)
						{
							p.setTransactionNoDsToDd((long)0);
							schoolService.updateObjectInDb(p);
						}
						schoolService.updateObjectInDb(ds);
						try {
							schoolService.deleteById(ddps.getId(), "DrivingDutyOfPilotDrivingSection");
							remove(ddps.getId());
							schoolService.update("CrewChangeDrivingsection",lTransactionId);
						} catch (Exception e) {
							e.printStackTrace();
							CREW_LOGGER.debug(e.getMessage());
						}
						
						
					} catch (CrewException e) {

						e.printStackTrace();
					}
				}
			}
	
	
			public void view()
			{
				Long lTransactionId = new Long(0);
				for(DrivingDutyOfPilotDrivingSection ds:selectionList)
				{	lTransactionId  = ds.getTransactionNoDsToDd();
					
					try {
						listCrewChange = (List<CrewChangeDrivingsection>) 
								schoolService.loadList("from CrewChangeDrivingsection as cc where cc.transactionNoDsToDd =" +lTransactionId);
						listPilotJourney = (List<PilotJourney>) schoolService.loadList("from PilotJourney as cc where cc.transactionNoDsToDd =" +lTransactionId);
						
					} catch (CrewException e) {
						e.printStackTrace();
						CREW_LOGGER.debug(e.getMessage());
					}
					
				}
				
			
		
			}
	
			
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	///  all getter and setters

	

	public ApplicationContext getContext() {
		return context;
	}

	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	public static Logger getCrewLogger() {
		return CREW_LOGGER;
	}

	public List<DrivingDutyOfPilotDrivingSection> getDrivingdutyview() {
		return drivingdutyview;
	}

	public void setDrivingdutyview(List<DrivingDutyOfPilotDrivingSection> drivingdutyview) {
		this.drivingdutyview = drivingdutyview;
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

	public List<DrivingDutyOfPilotDrivingSection> getFilterdrivingdutyview() {
		return filterdrivingdutyview;
	}

	public void setFilterdrivingdutyview(
			List<DrivingDutyOfPilotDrivingSection> filterdrivingdutyview) {
		this.filterdrivingdutyview = filterdrivingdutyview;
	}


	public String getSelectedFrom() {
		return selectedFrom;
	}


	public void setSelectedFrom(String selectedFrom) {
		this.selectedFrom = selectedFrom;
	}


	public String getSelectedTo() {
		return selectedTo;
	}


	public void setSelectedTo(String selectedTo) {
		this.selectedTo = selectedTo;
	}


	public String getSelectedFrom1() {
		return selectedFrom1;
	}


	public void setSelectedFrom1(String selectedFrom1) {
		this.selectedFrom1 = selectedFrom1;
	}


	public String getSelectedTo1() {
		return selectedTo1;
	}


	public void setSelectedTo1(String selectedTo1) {
		this.selectedTo1 = selectedTo1;
	}


	public List<SelectItem> getSelectlistfrom_station() {
		return selectlistfrom_station;
	}


	public void setSelectlistfrom_station(List<SelectItem> selectlistfrom_station) {
		this.selectlistfrom_station = selectlistfrom_station;
	}


	public List<SelectItem> getTo_stationlist() {
		return To_stationlist;
	}


	public void setTo_stationlist(List<SelectItem> to_stationlist) {
		To_stationlist = to_stationlist;
	}


	public long getCustomerloginId() {
		return customerloginId;
	}


	public void setCustomerloginId(long customerloginId) {
		this.customerloginId = customerloginId;
	}


	public List<CrewChangeDrivingsection> getListCrewChange() {
		return listCrewChange;
	}


	public void setListCrewChange(List<CrewChangeDrivingsection> listCrewChange) {
		this.listCrewChange = listCrewChange;
	}


	public List<PilotJourney> getListPilotJourney() {
		return listPilotJourney;
	}


	public void setListPilotJourney(List<PilotJourney> listPilotJourney) {
		this.listPilotJourney = listPilotJourney;
	}


	public List<DrivingDutyOfPilotDrivingSection> getSelectionList() {
		return selectionList;
	}


	public void setSelectionList(
			List<DrivingDutyOfPilotDrivingSection> selectionList) {
		this.selectionList = selectionList;
	}


	public CrewChangeDrivingsection getDs() {
		return ds;
	}


	public void setDs(CrewChangeDrivingsection ds) {
		this.ds = ds;
	}


	public List<PilotJourney> getListPJ() {
		return listPJ;
	}


	public void setListPJ(List<PilotJourney> listPJ) {
		this.listPJ = listPJ;
	}


	
	
	
	
	
}
