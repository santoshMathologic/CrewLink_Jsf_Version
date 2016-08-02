
package com.crew.serviceimpl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.crew.dao.CrewDAO;
import com.crew.exception.CrewException;
import com.crew.model.Customer;
import com.crew.model.TimeTableMaster;
import com.crew.service.CrewService;





public class CrewServiceImpl implements CrewService,Serializable {

	
	private static final long serialVersionUID = 1L;
	private CrewDAO crewDAO;
	private Customer customerLoginDetails;

	// Methods

	@Override
	public Collection<?> loadList(String query) throws CrewException {
		return crewDAO.loadList(query);
	}
	public CrewDAO getCrewDAO() {
		return crewDAO;
	}
	public void setCrewDAO(CrewDAO crewDAO) {
		this.crewDAO = crewDAO;
	}
	@Override
	public void saveInDb(Object object) throws CrewException {
		this.crewDAO.saveInDb(object);
		
	}
	@Override
	public Object loadUniqueObject(String query) throws CrewException {
		return crewDAO.loadUniqueObject(query);
	}
	@Override
	public void updateObjectInDb(Object object) throws CrewException {
		crewDAO.updateObjectInDb(object);
	}
	@Override
	public void delete(Long masterTableId, String tableName, String tableId)
			throws CrewException {
			crewDAO.delete(masterTableId, tableName, tableId);
		
	}
	@Override
	public void update(boolean b, long id)throws CrewException {
		crewDAO.update(b, id);
	}
	
	@Override
	public Collection<?> loadBetweenTrainList(String para1, String para2)
			throws CrewException {
		
		return crewDAO.loadBetweenTrainList(para1,para2);
	}
	@Override
	public void deleteById(long id, String tableName) {
		crewDAO.deleteById(id, tableName);
	}
	@Override
	public List<Object[]> loadByNTables(String sTrainno) {
		return crewDAO.loadByNTables(sTrainno);
	}
	
	
	@Override
	public List<TimeTableMaster> betweenQuery(long id1,long id2)
	{
		return crewDAO.betweenQuery(id1, id2);
	}
	@Override
	public void saveInDbCollection(Collection<?> saveList) throws CrewException {
		crewDAO.saveInDbCollection(saveList);
	}
	




	public List<?> gettingSizeRow(int numOfRows)
	{
		return crewDAO.gettingSizeRow(numOfRows);
	}
	@Override
	public void updateCrewChangeTable(long id, boolean status) {
		
		crewDAO.updateCrewChangeTable(id, status);
	}
	
	@Override
	public void update(String tableName, long id) throws CrewException {
		crewDAO.update(tableName,id);
	}
	@Override
	public void updateInDrivingDuty(long id) throws CrewException {
		crewDAO.updateInDrivingDuty(id);
		
	}
	@Override
	public void updateInDrivingDutyDDRT(long id, long transId)
			throws CrewException {
		crewDAO.updateInDrivingDutyDDRT(id,transId);
	}
	
	
	
	
	
	@Override
	public List<?> loadReverseList2(String from, String To, String from1,
			String To1) {
		
		return crewDAO.loadReverseList2(from, To, from1, To1);
		
	}
	@Override
	public List<?> loadReverseList1(String from1, String To1) {
		
		return crewDAO.loadReverseList1(from1, To1);
	}
	@Override
	public List<?> loadReverseList(String from, String To) {
		
		return crewDAO.loadReverseList(from, To);
	}
	@Override
	public void updateInPilotJourney(long id) throws CrewException {
		crewDAO.updateInPilotJourney(id);
		
	}
	@Override
	public void updateInDrivingDutyDDRT2(long id,long transId)throws CrewException {
		crewDAO.updateInDrivingDutyDDRT2(id,transId);
		
	}
	@Override
	public List<?> searchdrivingduty(String from, String To) {
		
		return crewDAO.searchdrivingduty(from, To);
	}
	@Override
	public List<?> searchdrivingduty1(String from1, String To1) {
		
		return crewDAO.searchdrivingduty1(from1, To1);
	}
	@Override
	public List<?> searchdrivingduty2(String from, String To, String from1,
			String To1) {
		
		return crewDAO.searchdrivingduty2(from, To, from1, To1);
	}
	@Override
	public Object loadObject(String query) throws CrewException {
		return crewDAO.loadObject(query);
	}
	@Override
	public List<?> singleSearch(String commonStations, int nChoice) {
		return crewDAO.singleSearch(commonStations, nChoice);
	}
	@Override
	public List<?> StatusSearch(String selectedSearch) {
	
		return crewDAO.StatusSearch(selectedSearch);
	}
	@Override
	public List<?> loadViewSearchList(String from, String To, String from1,
			String To1) {
		return crewDAO.loadViewSearchList(from, To, from1, To1);
	}
	@Override
	public List<?> loadViewSearchListTwoBox(String from, String To,
			String nChoice) {
		return crewDAO.loadViewSearchListTwoBox(from, To, nChoice);
	}
	@Override
	public List<?> singleviewSearch(String commonStationCode, String selection) {
		return crewDAO.singleviewSearch(commonStationCode, selection);
	}
	@Override
	public List<?> roundTripStatusSearch(String selectedSearch) {
		return crewDAO.roundTripStatusSearch(selectedSearch);
	}
	@Override
	public List<?> loadRoundTripSearchList(String from, String To,
			String from1, String To1) {
		return crewDAO.loadRoundTripSearchList(from, To, from1, To1);
	}
	@Override
	public List<?> loadRoundTripSearchListTwoBox(String from, String To,
			String nSelections) {
		return crewDAO.loadRoundTripSearchListTwoBox(from, To, nSelections);
	}
	@Override
	public List<?> roundTripsingleSearch(String commonStationCode,
			String selection) {
		return crewDAO.roundTripsingleSearch(commonStationCode, selection);
	}
	@Override
	public List<?> roundTriptoCrewLinkStatusSearch(String selectedSearch) {
		return crewDAO.roundTriptoCrewLinkStatusSearch(selectedSearch);
	}
	public Customer getCustomerLoginDetails() {
		return customerLoginDetails;
	}
	public void setCustomerLoginDetails(Customer customerLoginDetails) {
		this.customerLoginDetails = customerLoginDetails;
	}
	@Override
	public List<Customer> verifyCommonLogin(String userName, String password)throws CrewException {
		
		customerLoginDetails.setUsername(userName);
		customerLoginDetails.setPassword(password);
		return crewDAO.verifyCommonLogin(customerLoginDetails);
	}
	@Override
	public void deleteAdmin(Long adminLoginId) throws CrewException {
		 crewDAO.deleteAdmin(adminLoginId);
	}
	@Override
	public void deletePilotJourney(Long adminLoginId) throws CrewException {
		 crewDAO.deletePilotJourney(adminLoginId);
	}
	
	
	@Override
	public Object loadUniqueObject_by_Criteria(Long TableId, Class<?> classes) throws CrewException{
		  return crewDAO.loadUniqueObject_by_Criteria(TableId, classes);
			
	}
	@Override
	public Object loadUniqueResultObject(Long TableId, Class<?> classes,
			String methodname) throws CrewException{
		return crewDAO.loadUniqueResultObject(TableId, classes, methodname);
	}
	@Override
	public List<?> loadStation_by_Criteria(Class classes,
			String crewbase, String crewType) throws CrewException{
		return crewDAO.loadStation_by_Criteria(classes, crewbase, crewType);
		
	}
	@Override
	public List<?> loadStation_betweenRanges(Class classes, int lowerbound,
			int upperbound) throws CrewException {
		return crewDAO.loadStation_betweenRanges(classes, lowerbound, upperbound);
	}
	@Override
	public List<?> loadStation_by_gt(Class classes, String entity,long entity1)
			throws CrewException {
		return crewDAO.loadStation_by_gt(classes,entity,entity1);
	}
	
	@Override
	public List<?> loadStation_by_ASC_DESC(Class classes, String entity,
			String nChoices) throws CrewException {
		return  crewDAO.loadStation_by_ASC_DESC(classes, entity,nChoices);
	}
	@Override
	public List<?> loadStation_by_Projection(Class classes, String columnname)
			throws CrewException {
		return crewDAO.loadStation_by_Projection(classes, columnname);
	}
	@Override
	public List<?> loadStation_Named() throws CrewException {
		return crewDAO.loadStation_Named();
	}
	@Override
	public List<?> loadStation_by_lt(Class classes, String columnname,
			long entity) throws CrewException {
			return crewDAO.loadStation_by_lt(classes, columnname, entity);
	}
	@Override
	public List<?> loadStation_by_like(Class classes, String columnname,
			String filtervalue) throws CrewException {
		return crewDAO.loadStation_by_like(classes, columnname, filtervalue);
	}
	@Override
	public List<?> loadStation_by_ilike(Class classes, String columnname,
			String filtervalue) throws CrewException {
		return crewDAO.loadStation_by_ilike(classes, columnname, filtervalue);
	}
	@Override
	public List<?> loadStation_by_between(Class classes, String entityName,
			long startingRange, long endingRange) throws CrewException {
		return crewDAO.loadStation_by_between(classes, entityName, startingRange, endingRange);
	}
	@Override
	public List<?> ProjectionsProperties(Class classes, String userChoice,
			String coloumnName) throws CrewException {
		return crewDAO.ProjectionsProperties(classes, userChoice, coloumnName);
	}
	@Override
	public void batchUpdate(Collection entities) throws CrewException {
			crewDAO.batchUpdate(entities);
	}
	@Override
	public void batchDelete(Collection<?> entities) throws CrewException {
		crewDAO.batchUpdate(entities);
		
	}
	@Override
	public void deleteQuery(String strDelete) {
		
		crewDAO.deleteQuery(strDelete);
	}
	@Override
	public void deleteByTrainNumber(String trainNumber, String tableName) {
		// TODO Auto-generated method stub
		crewDAO.deleteByTrainNumber(trainNumber, tableName);
	}
	@Override
	public void updateInRoundTripAvailability(long id, String availabilityAtHq)
			throws CrewException {
		crewDAO.updateInRoundTripAvailability(id, availabilityAtHq);
		
	}
	@Override
	public void deleteUsingTrainNumber(String tableName, String trainNo) {
		crewDAO.deleteUsingTrainNumber(tableName, trainNo);
		
	}
	
	
	
	
	
	// all getter and Setters 
	


	
	
	
	

	


	
}