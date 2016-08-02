
package com.crew.dao;

import java.util.Collection;
import java.util.List;

import com.crew.exception.CrewException;
import com.crew.model.Customer;
import com.crew.model.TimeTableMaster;
@SuppressWarnings("unchecked")
public interface CrewDAO<T>  {

	
	Collection<?> loadList(String query)throws CrewException;
	void saveInDb(Object object)throws CrewException;
	public Object loadUniqueObject(String query)throws CrewException;
	void updateObjectInDb(Object object)throws CrewException;
	public void delete(Long masterTableId, String tableName, String tableId)throws CrewException; 
	public void update(boolean b,long id)throws CrewException;
	public void saveInDbCollection(Collection<?> saveList)throws CrewException;
	

	
	public Collection<?> loadBetweenTrainList(String para1,String para2)throws CrewException;
	public void deleteById(long id,String tableName);

	public List<Object[]> loadByNTables(String sTrainno);

	public List<TimeTableMaster> betweenQuery(long id1,long id2);

	public List<?> gettingSizeRow(int numOfRows);
	
	public void update(String tableName,long id)throws CrewException;
	public void updateCrewChangeTable(long id,boolean status);
	public void updateInDrivingDuty(long id)throws CrewException;
	public void updateInDrivingDutyDDRT(long id,long transId)throws CrewException;
	
	// only search
	List<?> loadReverseList2(String from, String To, String from1, String To1);
	List<?> loadReverseList1(String from1, String To1);
	List<?> loadReverseList(String from, String To);
	public List<?> singleSearch(String commonStations,int nChoice);
	
	
	// only search
	
	// drivingduty
		public List<?> searchdrivingduty(String from,String To);
		public List<?> searchdrivingduty1(String from1,String To1);
		public List<?> searchdrivingduty2(String from,String To,String from1,String To1);
		
		public List<?> StatusSearch(String selectedSearch);
		
		public List<?> roundTripStatusSearch(String selectedSearch);
		
		
	// viewDrivingDuty Search
	 public List<?> loadViewSearchList(String from, String To, String from1, String To1);
	 public List<?> loadViewSearchListTwoBox(String from, String To,String nChoice);
	 public List<?> singleviewSearch(String commonStationCode,String selection);
	 
	 
	 ///  roundTrip Search
	 public List<?> loadRoundTripSearchList(String from, String To, String from1, String To1);
	 public List<?> loadRoundTripSearchListTwoBox(String from, String To,String nSelections);
	 public List<?> roundTripsingleSearch(String commonStationCode,String selection);
	 
	 
	 
	 ////  roundTrip to CrewLink Status Search
	 public List<?> roundTriptoCrewLinkStatusSearch(String selectedSearch);
	 
	
	
	public void updateInPilotJourney(long id)throws CrewException;
	public void updateInDrivingDutyDDRT2(long id,long transId)throws CrewException;
	Object loadObject(String query) throws CrewException;
	
	
	// for login
	public List<Customer>  verifyCommonLogin(Customer customerDetails)throws CrewException;
	
	
	
	//  admin Methods 
	public void deleteAdmin(Long adminLoginId)throws CrewException;
	
	public void deletePilotJourney(Long adminLoginId)throws CrewException;

	// UniqueObject fetches Methods
	public Object loadUniqueResultObject(Long TableId, Class<?> classes,String methodname)throws CrewException; 
	public Object loadUniqueObject_by_Criteria(Long TableId, Class<?> classes)throws CrewException;
	
	
	// load Station by Critieria 
	@SuppressWarnings("unchecked")
	public List<?> loadStation_by_Criteria(Class classes,String crewbase,String crewType)throws CrewException;
	public List<?> loadStation_betweenRanges(Class classes,int lowerbound,int upperbound)throws CrewException; //1
	public List<?> loadStation_by_gt(Class classes,String entity,long entity1)throws CrewException;            //2
	public List<?> loadStation_by_ASC_DESC(Class classes, String entity,String nChoices)throws CrewException;  //3
	public List<?> loadStation_by_Projection(Class classes, String columnname)throws CrewException;   		   //4
	
	
	/// namedQuer
	public List<?> loadStation_Named()throws CrewException;   		  
	
	///  more Critieria
	
	public List<?> loadStation_by_lt(Class classes, String columnname,long entity)throws CrewException;   		   //4
	
	public List<?> loadStation_by_like(Class classes, String columnname,String filtervalue) throws CrewException;
	
	public List<?> loadStation_by_ilike(Class classes, String columnname,String filtervalue) throws CrewException; //case Sensitive form
	
	public List<?> loadStation_by_between(Class classes,String entityName, long startingRange,long endingRange) throws CrewException;
	 
	// Projections Property
	
	public List<?> ProjectionsProperties(Class classes,String userChoice,String coloumnName) throws CrewException;
	
	
	// batch Processing 
	public void batchUpdate(Collection<?> entities)throws CrewException;
	
	public void batchDelete(Collection<?> entities)throws CrewException;
	void deleteQuery(String strDelete);
	void deleteByTrainNumber(String trainNumber, String tableName);
	void updateInRoundTripAvailability(long id, String availabilityAtHq)
			throws CrewException;
	public void deleteUsingTrainNumber(String tableName,String trainNo);
	
	
}
