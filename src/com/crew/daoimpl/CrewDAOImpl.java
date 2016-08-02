
package com.crew.daoimpl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import com.crew.dao.AbstractCrewDAO;
import com.crew.dao.CrewDAO;
import com.crew.exception.CrewException;
import com.crew.model.CrewChangeDrivingsection;
import com.crew.model.Customer;
import com.crew.model.DrivingDutyOfPilotDrivingSection;
import com.crew.model.PilotJourney;
import com.crew.model.RoundTrip;
import com.crew.model.TimeTableMaster;



@Transactional
@SuppressWarnings("unchecked")

public class CrewDAOImpl extends AbstractCrewDAO implements CrewDAO,Serializable {

	
	private static final long serialVersionUID = 1L;
	Session session;
	private static final Logger CREW_LOGGER = Logger.getLogger(CrewDAOImpl.class);
	private boolean bPilotJourney = Boolean.FALSE;
	
	

	

	public List<?> find(String query) {

		List<?> objects = null;
		try {

			session = sessionFactory.openSession();
			objects = session.createQuery(query).list();

		} catch (HibernateException e) {
			CREW_LOGGER.debug(e.getMessage());

		}

		finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}
		return objects;
	}

	
	public List<TimeTableMaster> betweenQuery(long id1,long id2)
	{
		
		session = sessionFactory.openSession();
		return session
		.createQuery("FROM TimeTableMaster AS c WHERE c.id BETWEEN :stDate AND :edDate ")
		.setParameter("stDate", id1)
		.setParameter("edDate", id2)
		.list();
		
	}


  

	@Override
	public Collection<?> loadList(String query) throws CrewException {
		return find(query);
	}


	
	public boolean saveObject(Object object) {
	boolean isSaved = Boolean.FALSE;
	try {
			
		session = sessionFactory.openSession();
		Transaction tx = session.getTransaction();
			
			
			if (object != null) {
				tx.begin();
				session.save(object);
		
				//session.flush();
				isSaved = Boolean.TRUE;
				
				tx.commit();
			}

		} catch (HibernateException e) {
			CREW_LOGGER.debug(e.getMessage());
			
		}

		finally {
			if (session != null && session.isOpen()) {
				
				session.close();
				
			}
		}
		
	/*	Session session = sessionFactory.openSession();
	    Transaction tx = null;
	    try {
	        tx = session.beginTransaction();
	        session.save(object);
	        tx.commit(); // Flush happens automatically
	        
	        isSaved = true;
	    }
	    catch (RuntimeException e) {
	        tx.rollback();
	        throw e; // or display error message
	    }
	    finally {
	        session.close();
	    }*/
		return isSaved;
	}

	@Override
	public void saveInDb(Object object) throws CrewException
	{	
		boolean b = false;
		b=saveObject(object);
		if(b)
		{
			System.out.println("object saved");
		}
		else
		{
			System.out.println("object not saved!!!!!!!");
		}
	}
	
	public boolean saveOrUpdateObject(Object object) throws CrewException 
	{
		boolean isSaved = Boolean.FALSE;
		
		try
		{
		session = sessionFactory.openSession();
		
		if (object != null) {
			session.saveOrUpdate(object);
			session.flush();
			
			isSaved = Boolean.TRUE;
		}
		}catch(HibernateException e)
		{
			CREW_LOGGER.debug(e.getMessage());
			CREW_LOGGER.info("Eror in Save Or UpdateObject");
			
		}
		
		return isSaved;

	}
	
	public Object getUniqueObject(String query) throws HibernateException {
		Object uniqueObject = null;
		
		
		try
		{
			session = sessionFactory.openSession();
			final List<?> objList = session.createQuery(query).list();
			
			if (objList != null && objList.size() > 0) {
				uniqueObject = objList.get(0);
			}
		}
		
		catch(HibernateException e)
		{	CREW_LOGGER.debug(e.getMessage());
			
		}
		
		finally
	    {   if(session !=null && session.isOpen())
	        { session.close();
	          session=null;
	        }
	    }

		return uniqueObject;
	}
	public Object getUniqueObject2(String query) throws HibernateException {
		Object uniqueObject = null;
		try
		{
			session = sessionFactory.openSession();
			final List<?> objList = session.createQuery(query).list();
		}
		catch(HibernateException e)
		{	CREW_LOGGER.debug(e.getMessage());
		}
		finally
	    {   if(session !=null && session.isOpen())
	        { session.close();
	          session=null;
	        }
	    }
		return uniqueObject;
	}



	@Override
	public Object loadUniqueObject(String query) throws CrewException {
			return getUniqueObject(query);
	}
	@Override
	public Object loadObject(String query) throws CrewException {
			return getUniqueObject2(query);
	}



	@Override
	public void updateObjectInDb(Object object) throws CrewException {
		saveOrUpdateObject(object);
	}




	@Override
	public void delete(Long masterTableId, String tableName, String tableId)
			throws CrewException {
		try
		{
			Query query;
			String hql="delete from "+tableName+" as cn where cn."+tableId+"="+masterTableId;
				session = sessionFactory.openSession();
				query = session.createQuery(hql);
				query.executeUpdate();	
	
		}catch(HibernateException e)
		{	CREW_LOGGER.debug(e.getMessage());
			
		}
		
		finally
	    {   if(session !=null && session.isOpen())
	        { 	session.close();
	          	session=null;
	        }
	    }
		
	}
	
	public void update(boolean b,long id)throws CrewException
	{
		Query q;
		try
		{
			session = sessionFactory.openSession();
			q = session.createQuery("update :TimeTableMaster set crewChange =" +b+
					" where id="+id);
			q.executeUpdate();
			
		}catch(HibernateException e)
		{	CREW_LOGGER.debug(e.getMessage());
			
		}
		finally
	    {   if(session !=null && session.isOpen())
	        { session.close();
	          session=null;
	        }
	    }
	}
	public void updateInPilotJourney(long id)throws CrewException
	{
		Query q;
		try
		{
			session = sessionFactory.openSession();
			q = session.createQuery("update PilotJourney set transactionNoDsToDd =" +"NULL"+
					"where transactionNoDsToDd="+id);
			q.executeUpdate();
			System.out.println("updated successfully!!!!");
		}catch(HibernateException e)
		{	CREW_LOGGER.debug(e.getMessage());
			
		}
		finally
	    {   if(session !=null && session.isOpen())
	        { session.close();
	          session=null;
	        }
	    }
	}
	public void updateInDrivingDuty(long id)throws CrewException
	{
		Query q;
		try
		{
			session = sessionFactory.openSession();
			q = session.createQuery("update DrivingDutyOfPilotDrivingSection set transactionNoDdToRt =" +"NULL"+
					"where id="+id);
			q.executeUpdate();
			System.out.println("updated successfully!!!!");
		}catch(HibernateException e)
		{	CREW_LOGGER.debug(e.getMessage());
			
		}
		finally
	    {   if(session !=null && session.isOpen())
	        { session.close();
	          session=null;
	        }
	    }
	}
	public void updateInDrivingDutyDDRT(long id,long transId)throws CrewException
	{
		Query q;
		try
		{
			session = sessionFactory.openSession();
			q = session.createQuery("update DrivingDutyOfPilotDrivingSection set transactionNoDdToRt =" +transId+
					"where id="+id);
			q.executeUpdate();
			System.out.println("updated successfully!!!!");
		}catch(HibernateException e)
		{	CREW_LOGGER.debug(e.getMessage());
			
		}
		finally
	    {   if(session !=null && session.isOpen())
	        { session.close();
	          session=null;
	        }
	    }
	}
	public void updateInDrivingDutyDDRT2(long id,long transId)throws CrewException
	{
		Query q;
		System.out.println("idid ->>>>" +id);
		
			try
			{
				session = sessionFactory.openSession();
				q = session.createQuery("update DrivingDutyOfPilotDrivingSection set transactionNoDdToRt=" +"NULL"+ "where transactionNoDdToRt="+transId);
				q.executeUpdate();
				System.out.println("updated successfully!!!!");
			}catch(HibernateException e)
			{	CREW_LOGGER.debug(e.getMessage());
				
			}
			finally
		    {   if(session !=null && session.isOpen())
		        { session.close();
		          session=null;
		        }
		    }
		
		
		
	}
	public void update(String tableName,long id)throws CrewException
	{
		Query q;
		try
		{
			session = sessionFactory.openSession();
			q = session.createQuery("update CrewChangeDrivingsection set transactionNoDsToDd =" +"NULL"+
					",sequenceId="+"NULL"+",status="+"NULL"+" where transactionNoDsToDd="+id);
			q.executeUpdate();
			System.out.println("updated successfully!!!!");
		}catch(HibernateException e)
		{	CREW_LOGGER.debug(e.getMessage());
			
		}
		finally
	    {   if(session !=null && session.isOpen())
	        { session.close();
	          session=null;
	        }
	    }
	}
	public void updateCrewChangeTable(long id,boolean status)
	{
		Query q;
		try
		{
			session = sessionFactory.openSession();
			q = session.createQuery("update CrewChangeDrivingsection set status =" +status+
					" where id="+id);
			q.executeUpdate();
			
		}catch(HibernateException e)
		{	CREW_LOGGER.debug(e.getMessage());
			
		}
		finally
	    {   if(session !=null && session.isOpen())
	        { session.close();
	          session=null;
	        }
	    }
	}



	@Override
	public Collection<?> loadBetweenTrainList(String para1, String para2)
			throws CrewException {
		
		Query q;
		try
		{	session = sessionFactory.openSession();
			q = session.createQuery("from TimeTableMaster as TT where TT.stationCode between :para1 and :para2 " );
			q.setParameter("para1",para1);
			q.setParameter("para2",para2);
			
		}catch(HibernateException e)
		{	CREW_LOGGER.debug(e.getMessage());
		}
		finally
	    {   if(session !=null && session.isOpen())
	        { session.close();
	          session=null;
	        }
	    }
		  
		return null;
	}




	//daoImpl
		public void deleteById(long id,String tableName)
		{
			Query q;
			session = sessionFactory.openSession();
			q = session.createQuery("delete from "+tableName+" as t where t.id="+id);
			q.executeUpdate();
		}




		@Override
		public List<Object[]> loadByNTables(String sTrainno) {
		
			SQLQuery query;
			List<?> objects = null;
			session = sessionFactory.openSession();
			query = session.createSQLQuery("SELECT TTM.Train_No,TTM.Station_Code,TTM.Distance,TTM.Departure,TTM.Arrival,CCD.Crew_Base,CCD.CrewType FROM time_table_master as TTM,crew_change_drivingsection As CCD WHERE TTM.Train_No=CCD.train_no AND TTM.Train_No like :sTrainno");
			query.setParameter("sTrainno","%"+sTrainno+"%");
			
			objects =query.list(); 
			
			List<Object[]> rows = query.list();

			
			return rows;
		}


	

		public void bulkCreate(Collection<?> entities) {
			 
			  StatelessSession statelessSession = getSessionFactory().openStatelessSession();
			  statelessSession.beginTransaction();
			  Iterator<?> it = entities.iterator();
			  while (it.hasNext()) {
			    statelessSession.insert(it.next());
			  }
			  statelessSession.getTransaction().commit();
			  statelessSession.close();
			}

		

		public List<?> gettingSizeRow(int numOfRows)
		{
			session = sessionFactory.openSession();
			Transaction tx = session.getTransaction();
			List<?> objects = null;
			Query q=null;	
			try{	
				q = session.createQuery("FROM Finaltable");
				q.setFirstResult(0);
				q.setMaxResults(numOfRows);
				objects = q.list();
				
				
				
				
			} catch (HibernateException e) {
				CREW_LOGGER.debug(e.getMessage());
				
			}

			
			return objects;
			
		}
//santosh
		@Override
		public List<?> loadReverseList2(String from, String To, String from1,
				String To1) {
			session = sessionFactory.openSession();
			Transaction tx = session.getTransaction();
			List<?> objects = null;
			Query q=null;	
			List<CrewChangeDrivingsection>finalList = new ArrayList<CrewChangeDrivingsection>();
			if(from==null && To==null && from1==null && To==null)
			{
				try{	
					q = session.createQuery("FROM CrewChangeDrivingsection");
					//q.setFirstResult(0);
					//q.setMaxResults(numOfRows);
					finalList = q.list();
					
					
					
					
				} catch (HibernateException e) {
					CREW_LOGGER.debug(e.getMessage());
					
				}
			}
			else{

				List<CrewChangeDrivingsection>from_tolist1 = new ArrayList<CrewChangeDrivingsection>();
				List<CrewChangeDrivingsection>from_tolist2 = new ArrayList<CrewChangeDrivingsection>();
				if(!(from==null&&To==null) && (!(from1==null && To1==null)))
				{	
					from_tolist1 =  (List<CrewChangeDrivingsection>) find("from CrewChangeDrivingsection pld where pld.fromStation like '"
							+from+ "' and pld.toStation like '"+To+"'");
					
					from_tolist2 =  (List<CrewChangeDrivingsection>) find("from CrewChangeDrivingsection pld where pld.fromStation like '"
							+from1+ "' and pld.toStation like '"+To1+"'");
					
					finalList.addAll(from_tolist1);
					finalList.addAll(from_tolist2);
				
			
			}
			}
			
			return finalList;
		}
		@Override
		public List<?> loadReverseList1(String from1, String To1) {
			
			List<CrewChangeDrivingsection>from_tolist2 = new ArrayList<CrewChangeDrivingsection>();
			if(!(from1==null&&To1==null))
			{
				from_tolist2 =  (List<CrewChangeDrivingsection>) find("from CrewChangeDrivingsection pld where pld.fromStation like '"
					+from1+ "' and pld.toStation like '"+To1+"'");
			}
			
			return from_tolist2;
		}

		@Override
		public List<?> loadReverseList(String from, String To) {
			if(!(from==null&&To==null))
			{
				return  find("from CrewChangeDrivingsection pld where pld.fromStation like '"
					+from+ "' and pld.toStation like '"
					+To+"'");
			}
			return null;
		}


		@Override
		public List<?> searchdrivingduty(String from, String To) {
			if(!(from==null&&To==null))
			{
				return  find("from DrivingDutyOfPilotDrivingSection as ddps where ddps.fromStation like '"
					+from+ "' and ddps.toStation like '"
					+To+"'");
			}
			return null;
		}


		@Override
		public List<?> searchdrivingduty1(String from1, String To1) {
			if(!(from1==null&&To1==null))
			{
				return  find("from DrivingDutyOfPilotDrivingSection as ddps where ddps.fromStation like '"
					+from1+ "' and ddps.toStation like '"
					+To1+"'");
			}
			return null;
		}


		@Override
		public List<?> searchdrivingduty2(String from, String To, String from1,
				String To1) {

			List<DrivingDutyOfPilotDrivingSection>from_tolist1 = new ArrayList<DrivingDutyOfPilotDrivingSection>();
			List<DrivingDutyOfPilotDrivingSection>from_tolist2 = new ArrayList<DrivingDutyOfPilotDrivingSection>();
			List<DrivingDutyOfPilotDrivingSection>finalList = new ArrayList<DrivingDutyOfPilotDrivingSection>();
			if(!(from==null&&To==null) && (!(from1==null && To1==null)))
			{	
				from_tolist1 =  (List<DrivingDutyOfPilotDrivingSection>) find("from DrivingDutyOfPilotDrivingSection pld where pld.fromStation like '"
						+from+ "' and pld.toStation like '"+To+"'");
				
				from_tolist2 =  (List<DrivingDutyOfPilotDrivingSection>) find("from DrivingDutyOfPilotDrivingSection pld where pld.fromStation like '"
						+from1+ "' and pld.toStation like '"+To1+"'");
				
				finalList.addAll(from_tolist1);
				finalList.addAll(from_tolist2);
			
			}
			
			return finalList;
		}


		@Override
		public List<?> singleSearch(String commonStations, int nChoice) {
			
			List<CrewChangeDrivingsection> ccdrivingsectionlist = new ArrayList<CrewChangeDrivingsection>();
			switch(nChoice)
			{
			case 1:
				                             	
				                             	ccdrivingsectionlist = (List<CrewChangeDrivingsection>) find("from CrewChangeDrivingsection as cd where cd.fromStation like '"+commonStations+"'");
				                             	break;
				                             
			case 2:
												
												ccdrivingsectionlist = (List<CrewChangeDrivingsection>) find("from CrewChangeDrivingsection as cd where cd.toStation like '"+commonStations+"'");
												break;
				
				
			case 3:
												
												ccdrivingsectionlist = (List<CrewChangeDrivingsection>) find("from CrewChangeDrivingsection as cd where cd.fromStation like '"+commonStations+"'");
												break;
				
			case 4:
												
												
												ccdrivingsectionlist = (List<CrewChangeDrivingsection>) find("from CrewChangeDrivingsection as cd where cd.toStation like '"+commonStations+"'");
												break;
				
				
				                             
				                             
				                             
		    default :
		    									   System.out.print("inside Default");
		    									   FacesContext.getCurrentInstance().addMessage(
															null,
															new FacesMessage(FacesMessage.SEVERITY_FATAL,
																	"Please select atleast one", "Please select atleast one"));
		    									break;
		    									
		    									
			}
			return ccdrivingsectionlist;
		}


		@Override
		public List<?> StatusSearch(String selectedSearch) {
			if(selectedSearch.equalsIgnoreCase("DutyCreated"))
			{
				String selectedQuery = "1";
				List<CrewChangeDrivingsection>searchDrivingSectionList= new ArrayList<CrewChangeDrivingsection>();
				searchDrivingSectionList =  (List<CrewChangeDrivingsection>) find("from CrewChangeDrivingsection CCD where CCD.status like '"+selectedQuery+"'");
				return searchDrivingSectionList;	
			}
			else if(selectedSearch.equalsIgnoreCase("DutyNotCreated"))
			{
				String selectedQuery = "NULL";
				List<CrewChangeDrivingsection>searchDrivingSectionList= new ArrayList<CrewChangeDrivingsection>();
				searchDrivingSectionList =  (List<CrewChangeDrivingsection>) find("from CrewChangeDrivingsection CCD where CCD.status ="+selectedQuery);
				return searchDrivingSectionList;
			}
			else if(selectedSearch.equalsIgnoreCase("All"))
			{
				
				List<CrewChangeDrivingsection>searchDrivingSectionList= new ArrayList<CrewChangeDrivingsection>();
				searchDrivingSectionList =  (List<CrewChangeDrivingsection>) find("from CrewChangeDrivingsection as CCD");
				return searchDrivingSectionList;
			}
				
			return null;
		}


		@Override
		public List<?> loadViewSearchList(String from, String To, String from1,
				String To1) {
			
			List<DrivingDutyOfPilotDrivingSection>from_tolist1 = new ArrayList<DrivingDutyOfPilotDrivingSection>();
			List<DrivingDutyOfPilotDrivingSection>from_tolist2 = new ArrayList<DrivingDutyOfPilotDrivingSection>();
			List<DrivingDutyOfPilotDrivingSection>mergedList = new ArrayList<DrivingDutyOfPilotDrivingSection>();
			if(!(from==null&&To==null) && (!(from1==null && To1==null)))
			{	
				from_tolist1 =  (List<DrivingDutyOfPilotDrivingSection>) find("from DrivingDutyOfPilotDrivingSection dds where dds.fromStation like '"
						+from+ "' and dds.toStation like '"+To+"'");
				
				from_tolist2 =  (List<DrivingDutyOfPilotDrivingSection>) find("from DrivingDutyOfPilotDrivingSection dds where dds.fromStation like '"
						+from1+ "' and dds.toStation like '"+To1+"'");
				
				mergedList.addAll(from_tolist1);
				mergedList.addAll(from_tolist2);
			
			}
			
			return mergedList;
			
		}


		@Override
		public List<?> loadViewSearchListTwoBox(String from, String To,
				String nChoice) {
			String from1 = from;
			String To1 = To;
			if(nChoice.equalsIgnoreCase("UPPER"))
			{
				if(!(from==null && To==null))
				{
				return  find("from DrivingDutyOfPilotDrivingSection as ddps where ddps.fromStation like '"
					+from+ "' and ddps.toStation like '"
					+To+"'");
				}
			}else if(nChoice.equalsIgnoreCase("LOWER"))
			{
				if(!(from1==null && To1==null))
				{
				return  find("from DrivingDutyOfPilotDrivingSection as ddps where ddps.fromStation like '"
					+from1+ "' and ddps.toStation like '"
					+To1+"'");
				}
					
			}
			
			return null;
		}


		@Override
		public List<?> singleviewSearch(String commonStationCode,
				String selection) {
			List<DrivingDutyOfPilotDrivingSection> commonlist = new ArrayList<DrivingDutyOfPilotDrivingSection>();
			switch(selection)
			{
			case "FROM" :
									
									commonlist =   (List<DrivingDutyOfPilotDrivingSection>) find("from DrivingDutyOfPilotDrivingSection as ddps where ddps.fromStation like '"
											+commonStationCode+ "'");
				 					break;
				 					
			case "TO" :
									
									commonlist =   (List<DrivingDutyOfPilotDrivingSection>) find("from DrivingDutyOfPilotDrivingSection as ddps where ddps.toStation like '"
									+commonStationCode+ "'");
									break;
									
									
			case "FROM1" :
									
									commonlist =   (List<DrivingDutyOfPilotDrivingSection>) find("from DrivingDutyOfPilotDrivingSection as ddps where ddps.fromStation like '"
									+commonStationCode+ "'");
									break;
									

				 					
			case "TO1" :
											
									commonlist =   (List<DrivingDutyOfPilotDrivingSection>) find("from DrivingDutyOfPilotDrivingSection as ddps where ddps.toStation like '"
									+commonStationCode+ "'");
									break;

			
			}
			return commonlist;
		}


		@Override
		public List<?> roundTripStatusSearch(String selectedSearch) {
			if(selectedSearch.equalsIgnoreCase("RoundTripcreated"))
			{
				String selectedQuery = "1";
				List<DrivingDutyOfPilotDrivingSection>searchDrivingSectionList= new ArrayList<DrivingDutyOfPilotDrivingSection>();
				searchDrivingSectionList =  (List<DrivingDutyOfPilotDrivingSection>) find("from DrivingDutyOfPilotDrivingSection RT where RT.status like '"+selectedQuery+"'");
				return searchDrivingSectionList;	
			}
			else if(selectedSearch.equalsIgnoreCase("RoundTripNotcreated"))
			{
				String selectedQuery = "NULL";
				String OrselectedQuery = "0";
				
				List<DrivingDutyOfPilotDrivingSection>searchDrivingSectionList= new ArrayList<DrivingDutyOfPilotDrivingSection>();
				searchDrivingSectionList =  (List<DrivingDutyOfPilotDrivingSection>) find("from DrivingDutyOfPilotDrivingSection RT where RT.status like '"+selectedQuery+"' OR RT.status like '"+OrselectedQuery+"'");
				return searchDrivingSectionList;
			}
			else if(selectedSearch.equalsIgnoreCase("All"))
			{
				
				List<DrivingDutyOfPilotDrivingSection>searchDrivingSectionList= new ArrayList<DrivingDutyOfPilotDrivingSection>();
				searchDrivingSectionList =  (List<DrivingDutyOfPilotDrivingSection>) find("from DrivingDutyOfPilotDrivingSection as RT");
				return searchDrivingSectionList;
			}
			
			return null;
		}


		@Override
		public List<?> loadRoundTripSearchList(String from, String To,
				String from1, String To1) {
			
			
			List<RoundTrip>from_tolist1 = new ArrayList<RoundTrip>();
			List<RoundTrip>from_tolist2 = new ArrayList<RoundTrip>();
			List<RoundTrip>mergedList = new ArrayList<RoundTrip>();
			if(!(from==null&&To==null) && (!(from1==null && To1==null)))
			{	
				from_tolist1 =  (List<RoundTrip>) find("from RoundTrip dds where dds.fromStation like '"
						+from+ "' and dds.toStation like '"+To+"'");
				
				from_tolist2 =  (List<RoundTrip>) find("from RoundTrip dds where dds.fromStation like '"
						+from1+ "' and dds.toStation like '"+To1+"'");
				
				mergedList.addAll(from_tolist1);
				mergedList.addAll(from_tolist2);
			
			}
			
			return mergedList;
			
		}


		@Override
		public List<?> loadRoundTripSearchListTwoBox(String from, String To,
				String nSelections) {
			String from1  =from;
			String To1 = To;
			if(nSelections.equalsIgnoreCase("UPPER"))
			{
				if(!(from==null && To==null))
				{
				return  find("from RoundTrip as ddps where ddps.fromStation like '"
					+from+ "' and ddps.toStation like '"
					+To+"'");
				}
			}else if(nSelections.equalsIgnoreCase("LOWER"))
			{
				if(!(from1==null && To1==null))
				{
				return  find("from RoundTrip as ddps where ddps.fromStation like '"
					+from1+ "' and ddps.toStation like '"
					+To1+"'");
				}
					
			}
			
			
			return null;
		}


		@Override
		public List<?> roundTripsingleSearch(String commonStationCode,
				String selection) {
			List<RoundTrip> rr = new ArrayList<RoundTrip>();
			switch(selection.trim())
			{
			case "TO":
									   rr =   (List<RoundTrip>) find("from RoundTrip as ddps where ddps.toStation like '"+commonStationCode+"'");
				                       break;
				                       
			case "FROM":
				   						rr =   (List<RoundTrip>) find("from RoundTrip as ddps where ddps.fromStation like '"+commonStationCode+"'");
				   						break;
                
                
			case "TO1":
										rr =   (List<RoundTrip>) find("from RoundTrip as ddps where ddps.toStation like '"+commonStationCode+"'");
										break;
                
                
			case "FROM1":
				   						rr =   (List<RoundTrip>) find("from RoundTrip as ddps where ddps.fromStation like '"+commonStationCode+"'");
				   						break;
				                       
			
			}
			return rr;
		}


		@Override
		public List<?> roundTriptoCrewLinkStatusSearch(String selectedSearch) {
			if(selectedSearch.equalsIgnoreCase("CrewLinkcreated"))
			{
				String selectedQuery = "1";
				List<RoundTrip>searchDrivingSectionList= new ArrayList<RoundTrip>();
				searchDrivingSectionList =  (List<RoundTrip>) find("from RoundTrip rr where rr.status like '"+selectedQuery+"'");
				return searchDrivingSectionList;	
			}
			else if(selectedSearch.equalsIgnoreCase("CrewLinkNotcreated"))
			{
				String selectedQuery = "0";
				List<RoundTrip>searchDrivingSectionList= new ArrayList<RoundTrip>();
				searchDrivingSectionList =  (List<RoundTrip>) find("from RoundTrip rr where rr.status like '"+selectedQuery+"'");
				return searchDrivingSectionList;
			}
			else if(selectedSearch.equalsIgnoreCase("All"))
			{	List<RoundTrip>searchDrivingSectionList= new ArrayList<RoundTrip>();
				searchDrivingSectionList =  (List<RoundTrip>) find("from RoundTrip as rr");
				return searchDrivingSectionList;
			}
				
			return null;
		}


		@Override
		public List<Customer> verifyCommonLogin(Customer customerDetails)
				throws CrewException {
			
			return (List<Customer>) find("from Customer as cust where cust.username='"
					+ customerDetails.getUsername() + "' And cust.password='"
					+ customerDetails.getPassword() + "'");
			
			
			
		}


		@Override
		public void deleteAdmin(Long adminLoginId) throws CrewException {
			
			
			try
			{		session = sessionFactory.openSession();
					Transaction tx = session.beginTransaction();
					Customer customerobj = (Customer ) session.createCriteria(Customer.class)
                    .add(Restrictions.eq("id", adminLoginId)).uniqueResult();
					session.delete(customerobj);
					session.flush();
					tx.commit();
			}
			catch(HibernateException e)
			{	CREW_LOGGER.debug(e.getMessage());
				
			}
			finally
		    {   if(session !=null && session.isOpen())
		        { session.close();
		          session=null;
		        }
		    }	
		}


		@Override
		public void deletePilotJourney(Long adminLoginId) throws CrewException {
			
			
			try
			{		session = sessionFactory.openSession();
					Transaction tx = session.beginTransaction();
					PilotJourney pilot = (PilotJourney) session.createCriteria(PilotJourney.class)
                    .add(Restrictions.eq("id", adminLoginId)).uniqueResult();
					session.delete(pilot);
					session.flush();
					tx.commit();
					
			}
			catch(HibernateException e)
			{	CREW_LOGGER.debug(e.getMessage());
			}
			finally
		    {   if(session !=null && session.isOpen())
		        { session.close();
		          session=null;
		        }
		    }
		}


		


		@Override
		public void saveInDbCollection(Collection saveList)
				throws CrewException {
			bulkCreate(saveList);
		}


		
		/// san methods starts 
		@Override
		public Object loadUniqueObject_by_Criteria(Long TableId, Class classes) {
			Object uniqueObject = null;
			try {
				session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				uniqueObject =  session.createCriteria(classes).add(Restrictions.idEq(TableId)).uniqueResult();
						if (!(uniqueObject == null))return uniqueObject;
						tx.commit();
				
			} catch (HibernateException e) {
				CREW_LOGGER.debug(e.getMessage());
			} finally {
				if (session != null && session.isOpen()) {
					session.close();
					session = null;
				}
			}
			return uniqueObject;
			
		}


		@Override
		public Object loadUniqueResultObject(Long TableId, Class classes,
				String methodname)throws CrewException {
				Object obj = null;
					switch (methodname) {
					case "load":
						session = sessionFactory.openSession();
						obj = session.load(classes, TableId);
						if (obj != null) {
							return obj;
						}
			
						break;
			
					case "get":
						session = sessionFactory.openSession();
						obj = session.get(classes, TableId);
						if (obj != null) {
							return obj;
						}
						break;
					}
			
					return obj;
		}


		public void CriteriaLogical()
		{
			 
		}


		@Override
		public List<?> loadStation_by_Criteria(Class classes,
				String crewbase, String crewType) throws CrewException{
			
				session = sessionFactory.openSession();
			    Criteria crit = session.createCriteria(classes);
			    Criterion base = Restrictions.like("crewBase", crewbase);
			    Criterion type = Restrictions.like("crewType",crewType);
			    LogicalExpression orExp = Restrictions.and(base, type);
			    crit.add(orExp);
			    List<?> results = crit.list();
			    System.out.println("List of DrivingSection "+results.size());    
			    Iterator<CrewChangeDrivingsection> listIterator = (Iterator<CrewChangeDrivingsection>) results.iterator();
			    List<CrewChangeDrivingsection> CrewChangeDrivingsectionList = new ArrayList<CrewChangeDrivingsection>();
			   while (listIterator.hasNext()) {
					CrewChangeDrivingsection ccds  = listIterator.next();
					CrewChangeDrivingsectionList.add(ccds);
				}
			return CrewChangeDrivingsectionList;
			
		}


		@Override
		public List<?> loadStation_betweenRanges(Class classes, int lowerbound,
				int upperbound) throws CrewException {
			List list = null;
			try
			{
		        session = sessionFactory.openSession();
			    Transaction tx = session.beginTransaction();
			    Criteria  crietria=session.createCriteria(classes);  
			    crietria.setFirstResult(lowerbound);  
			    crietria.setMaxResults(upperbound);  
			    list=crietria.list(); 
			    tx.commit();
			    return list;
				
			}catch (HibernateException e) {CREW_LOGGER.debug(e.getMessage());} 
			finally {
					if (session != null && session.isOpen()) {
						session.close();
						session = null;
					}
				}
			return list;
		}


		@Override
		public List<?> loadStation_by_gt(Class classes, String entity,long entity1)
				throws CrewException {
			List list = null;
			try
			{
		        session = sessionFactory.openSession();
			    Transaction tx = session.beginTransaction();
			    Criteria  crietria=session.createCriteria(classes);  
			    crietria.add(Restrictions.gt(entity,entity1));// station   
			    list=crietria.list(); 
			    tx.commit();
			    return list;
			
		}catch (HibernateException e) {CREW_LOGGER.debug(e.getMessage());} 
		finally {
				if (session != null && session.isOpen()) {
					session.close();
					session = null;
				}
			}
			
			
			return list;
		}


		@Override
	public List<?> loadStation_by_ASC_DESC(Class classes, String entity,
			String nChoices) throws CrewException {

		List list = null;
		try {
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Criteria crietria = session.createCriteria(classes);

			switch (nChoices) {
			case "asc":
				crietria.addOrder(Order.asc(entity));
				list = crietria.list();
				break;
			case "desc":
				crietria.addOrder(Order.desc(entity));
				list = crietria.list();
				break;

			}

			tx.commit();
			return list;

		} catch (HibernateException e) {
			CREW_LOGGER.debug(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}

		return list;
	}


		@Override
		public List loadStation_by_Projection(Class classes, String columnname)
				throws CrewException {
			
			List list = null;
			try {
				session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				Criteria crietria = session.createCriteria(classes);
				crietria.setProjection(Projections.property(columnname));  
				list=crietria.list(); 
				
				Iterator<?> it=list.iterator();
				while(it.hasNext())
				{
				    String s = (String)it.next();
				    System.out.println(""+s);
				    // ---- print -----
				}
				
				
				tx.commit();
				return list;

				}catch (HibernateException e) {
				CREW_LOGGER.debug(e.getMessage());
				} finally {
				if (session != null && session.isOpen()) {
					session.close();
					session = null;
				}
			}

			return list;
			
			
		}


		@Override
		public List loadStation_Named() throws CrewException {
			List list = null;
			try {
				session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				Query query = session.getNamedQuery("findTrainsByName");  
			    query.setString("trainNo", "11014");  
				List<CrewChangeDrivingsection> trainLists=query.list();  
		            Iterator<CrewChangeDrivingsection> itr=trainLists.iterator();  
				     while(itr.hasNext())
				     {  
				    	CrewChangeDrivingsection e=itr.next();  
				    	System.out.println(e);
				     }
				
				
				tx.commit();
				return list;


			}catch (HibernateException e) {CREW_LOGGER.debug(e.getMessage());
			} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}
			
			return null;
		}


		@Override
		public List<?> loadStation_by_lt(Class classes, String columnname,
				long entity) throws CrewException {
			List list = null;
			try {
				session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				
				Criteria criteria = session.createCriteria(classes);
				criteria.add(Restrictions.lt(columnname,entity));
				list=criteria.list(); 
		        if(!list.isEmpty())
		        {
		        	Iterator<?> iter = list.iterator(); 
		        	
		        	System.out.println("\t Train No \t CrewBase \t CrewType\t Distance");
				     while(iter.hasNext())
				     {  
				    	CrewChangeDrivingsection e=(CrewChangeDrivingsection) iter.next();  
				    	System.out.println("\t"+e.getTrainNo()+"\t"+e.getCrewBase()+"\t"+e.getCrewType()+"\t"+e.getKilometer());
				     }
				
				    tx.commit();
					return list;
		        }
				
				

			}catch (HibernateException e) {CREW_LOGGER.debug(e.getMessage());
			} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}
			
			return null;
		}

		
		@Override
		public List<?> loadStation_by_like(Class classes, String columnname,
				String filtervalue) throws CrewException {
			List list = null;
			try {
				session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				
				Criteria criteria = session.createCriteria(classes);
				criteria.add(Restrictions.like(columnname,filtervalue));
				list=criteria.list(); 
		        if(!list.isEmpty())
		        {
		        	Iterator<?> iter = list.iterator(); 
		        	
		        	System.out.println("Train No \t CrewBase \t CrewType\t Distance \tFrom");
				     while(iter.hasNext())
				     {  
				    	CrewChangeDrivingsection e=(CrewChangeDrivingsection) iter.next();  
				    	System.out.println("\t"+e.getTrainNo()+"\t"+e.getCrewBase()+"\t"+e.getCrewType()+"\t"+e.getKilometer()+"\t"+e.getFromStation());
				     }
				
				    tx.commit();
					return list;
		        }
				
				

			}catch (HibernateException e) {CREW_LOGGER.debug(e.getMessage());
			} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}
			
			return null;
		}


		@Override
		public List<?> loadStation_by_ilike(Class classes, String columnname,
				String filtervalue) throws CrewException {
				List list = null;
			try {
				session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				
				Criteria criteria = session.createCriteria(classes);
				criteria.add(Restrictions.ilike(columnname,filtervalue));
				list=criteria.list(); 
		        if(!list.isEmpty())
		        {
		        	Iterator<?> iter = list.iterator(); 
		        	
		        	System.out.println("Train No \t CrewBase \t CrewType\t Distance \tFrom");
				     while(iter.hasNext())
				     {  
				    	CrewChangeDrivingsection e=(CrewChangeDrivingsection) iter.next();  
				    	System.out.println("\t"+e.getTrainNo()+"\t"+e.getCrewBase()+"\t"+e.getCrewType()+"\t"+e.getKilometer()+"\t"+e.getFromStation());
				     }
				
				    tx.commit();
					return list;
		        }
				
				

			}catch (HibernateException e) {CREW_LOGGER.debug(e.getMessage());
			} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}
			
			
			
			return null;
		}


		@Override
		public List<?> loadStation_by_between(Class classes,String entityName, long startingRange,
				long endingRange) throws CrewException {
				List<?> list = null;
			try {
				session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				
				Criteria criteria = session.createCriteria(classes);
				criteria.add(Restrictions.between(entityName,startingRange,endingRange));
				list=criteria.list(); 
		        if(!list.isEmpty())
		        {
		        	Iterator<?> iter = list.iterator(); 
		        	
		        	System.out.println("Train No\tCrewBase\tCrewType\tDistance");
				     while(iter.hasNext())
				     {  
				    	CrewChangeDrivingsection e=(CrewChangeDrivingsection) iter.next();  
				    	System.out.println("\t"+e.getTrainNo()+"\t"+e.getCrewBase()+"\t"+e.getCrewType()+"\t"+e.getKilometer());
				     }
				
				    tx.commit();
					return list;
		        }
				
				

			}catch (HibernateException e) {CREW_LOGGER.debug(e.getMessage());
			} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}
			
			
			
			return null;
		}


		@Override
		public List ProjectionsProperties(Class classes, String userChoice,
				String coloumnName) throws CrewException {
			
			List<?> list = null;
			try {
					session = sessionFactory.openSession();
					Transaction tx = session.beginTransaction();
					Criteria criteria = session.createCriteria(classes);
				
				switch(userChoice)
				{
				case "rowCount":
											criteria.setProjection(Projections.rowCount());
											int resultCount = (int) criteria.uniqueResult();
											break;
											
				case "avg":
											criteria.setProjection(Projections.avg(coloumnName));
											@SuppressWarnings("unchecked")
											double average =(double) criteria.uniqueResult();
											break;
											
											
											
				case "countDistinct":
											criteria.setProjection(Projections.countDistinct(coloumnName));
											list=criteria.list();
											if(list!=null) return list;
											break;
											
											
				case "max":
											criteria.setProjection(Projections.max(coloumnName));
											Long maxResults =(Long) criteria.uniqueResult();
											break;
											
				case "min":
											criteria.setProjection(Projections.min(coloumnName));
											Long minResults =(Long) criteria.uniqueResult();
											break;
											
											
				case "sum":
											criteria.setProjection(Projections.sum(coloumnName));
											Long nSummations=(Long)criteria.uniqueResult();
											
											break;
											
											
											
											
				case "MatchMode.ANYWHERE":
										
													
													Criterion ranges = Restrictions.lt(coloumnName,400L);
													Criterion base = Restrictions.ilike("crewBase", "sbc",
															MatchMode.ANYWHERE);
													Criterion type = Restrictions.ilike("crewType", "mas",
															MatchMode.ANYWHERE);
													Disjunction disjunction = Restrictions.disjunction();
													disjunction.add(ranges);
													disjunction.add(base);
													disjunction.add(type);
													criteria.add(disjunction);
													List<?> results = criteria.list();
													if (results != null)
														return results;
													    break;
													    
													    
				
				case "AS":						     
													 criteria.add(Restrictions.ilike("fromStation", "sbc"));
			    									 criteria.setProjection(Projections.projectionList()
			    									.add(Projections.property("id"))
			    									.add(Projections.property("fromStation"))
			    									.add(Projections.property("toStation")));
			    									List<Object[]> rows = criteria.list();
			    									
			    									   System.out.println("\nId\tFrom\tTo\n");
			    									    for (Object[] row : rows) {
			    									        System.out.println(row[0] + "\t" + row[1]+"\t"+row[2]);
			    									    }
			    									    break;
													    
													    
													
			
									
				}
				
				
				tx.commit();
				}catch (HibernateException e) {CREW_LOGGER.debug(e.getMessage());} 
			finally {
					if (session != null && session.isOpen()) 
					{	session.close();
						session = null;
					}
			}
			return list;
			
		
			
		}

	
		
		public void extensiveRestore(Collection<?> entities) {
			  StatelessSession statelessSession = getSessionFactory().openStatelessSession();
			  statelessSession.beginTransaction();
			  Iterator<?> it = entities.iterator();
			  while (it.hasNext()) {
			    statelessSession.update(it.next());
			  }
			  statelessSession.getTransaction().commit();
			  statelessSession.close();
			}
		
		public void extensiveRemove(Collection<?> entities) {
			  // Open a Stateless Session (this doesn't have a persistence context cache and
			  // doesn't interact with any other second-level or query cache).  Everything
			  // executed with a Stateless session results in an immediate SQL operation.
			  StatelessSession statelessSession = getSessionFactory().openStatelessSession();
			  statelessSession.beginTransaction();
			  Iterator<?> it = entities.iterator();
			  while (it.hasNext()) {
			    statelessSession.delete(it.next());
			  }
			  statelessSession.getTransaction().commit();
			  statelessSession.close();
			  
			}


		@Override
		public void batchUpdate(Collection entities) throws CrewException {
			extensiveRestore(entities);
		}


		@Override
		public void batchDelete(Collection entities) throws CrewException {
			extensiveRemove(entities);
		}
		
		
		public List<CrewChangeDrivingsection> loadStationNames(String Startstation, String endStation)
				throws CrewException  {
			return (List<CrewChangeDrivingsection>) find(" from CrewChangeDrivingsection ccds where  ccds.fromStation  LIKE '"

					+ Startstation + "' AND ccds.fromStation  LIKE '" + endStation
							
					+ "' OR ccds.fromStation  LIKE '"
					+ Startstation
					+ "' AND ccds.fromStation LIKE '"
					+ endStation
					+ "' OR ccds.fromStation LIKE '"
					+ Startstation
					+ "' AND ccds.fromStation LIKE '"
					+ endStation
					+ "' OR ccds.fromStation  LIKE '"
					+ Startstation
					+ "' AND ccds.fromStation LIKE '"
					+ endStation
					+ "' OR ccds.fromStation  LIKE '"
					+ Startstation
					+ "' AND ccds.fromStation LIKE '"
					+ endStation
					+ "' OR ccds.fromStation LIKE '"
					+ Startstation
					+ "' AND ccds.fromStation LIKE '"
					+ endStation
					+ "' OR ccds.fromStation  LIKE '"
					+ Startstation
					+ "' AND ccds.fromStation LIKE '"
					+ endStation
					+ "' OR ccds.fromStation  LIKE '"
					+ Startstation
					+ "' AND ccds.fromStation LIKE '"
					+ endStation
					+ "' OR ccds.toStation in('11004','11301','11302')");

		}

		
		
		
		///  san methods ends 
		
		@Override
		public void deleteQuery(String strDelete) {
			
			
			session = sessionFactory.openSession();
			Transaction tx = session.getTransaction();
			Query q = null;
				try{	
					tx.begin();
					q = session.createQuery("delete from " +strDelete);
					q.executeUpdate();
					System.out.println("deleted successfully!!!!!!!!!!!1");
					tx.commit();
				} catch (HibernateException e) {
					tx.rollback();
					CREW_LOGGER.debug(e.getMessage());
					
				}
				
				session.close();
			}
		@Override
		public void deleteByTrainNumber(String trainNumber,String tableName)
		{
			Query q;
			session = sessionFactory.openSession();
			q = session.createQuery("delete from "+tableName+" as t where t.trainNo like'"+trainNumber+"'");
			q.executeUpdate();
		}

		@Override
		public void updateInRoundTripAvailability(long id,String availabilityAtHq)throws CrewException
		{
			System.out.println("in the method of update" +availabilityAtHq +"\t\t -> " +id);
			Query query;
			try
			{
				session = sessionFactory.openSession();
				String hql = "UPDATE RoundTrip set availabilityAtHq = :availabilityAtHq "  + 
			             "WHERE id = :id";	
			query = session.createQuery(hql);
			query.setParameter("availabilityAtHq", availabilityAtHq);
			query.setParameter("id", id);
			int result = query.executeUpdate();
			System.out.println("Rows affected: " + result);
			}catch(HibernateException e)
			{	CREW_LOGGER.debug(e.getMessage());
				
			}
			finally
		    {   if(session !=null && session.isOpen())
		        { session.close();
		        }
		    }
		}


		@Override
		public void deleteUsingTrainNumber(String tableName, String trainNo) {
			Query query;
			try
			{
				session = sessionFactory.openSession();
				String hql = "DELETE FROM RoundtripTemp as r WHERE r.trainNo = :trainNo ";	
				query = session.createQuery(hql);
				query.setParameter("trainNo", trainNo);
				int result = query.executeUpdate();
				System.out.println("Rows affected: " + result);
			}
			catch(HibernateException e)
			{	
				CREW_LOGGER.debug(e.getMessage());
			}
			finally
		    {   if(session !=null && session.isOpen())
		        { 
		    		session.close();
		        }
		    }
		}
		
		
		
		
		

	
}
	
	
	
	
	
