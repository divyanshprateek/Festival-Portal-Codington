package com.accenture.adf.businesstier.service;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.accenture.adf.businesstier.dao.EventDAO;
import com.accenture.adf.businesstier.dao.VisitorDAO;
import com.accenture.adf.businesstier.entity.Event;
import com.accenture.adf.businesstier.entity.Visitor;
import com.accenture.adf.exceptions.FERSGenericException;

/**
 * <br/>
 * CLASS DESCRIPTION: <br/>
 * 
 * A service class that implements the VisitorFacade. Makes visitor-related data
 * requests to the VisitorDAO class <br/>
 * 
 *  @author krishna.kishore
 * 
 */
public class VisitorServiceImpl implements VisitorFacade {

	// LOGGER for logging all exceptions of VISITOR DAO
	private static Logger log = Logger.getLogger(VisitorServiceImpl.class);

	/**
	 * <br/>
	 * METHOD DESCRIPTION: <br/>
	 * Used for inserting new visitor via the DAO <br/>
	 * 
	 * @return insertStatus
	 * 
	 * @param visitor
	 * 
	 */
	public boolean createVisitor(Visitor visitor) {

		VisitorDAO visitorDAO = new VisitorDAO();
		boolean insertStatus = false;
		try {
			insertStatus = visitorDAO.insertData(visitor);
		} catch (SQLException exception) {
			log.info("Exception is :" + exception.getMessage());
		} catch (ClassNotFoundException exception) {
			log.info("Exception is :" + exception.getMessage());
		} catch (Exception exception) {
			log.info("Exception is :" + exception.getMessage());
		}
		return insertStatus;
	}

	/**
	 * <br/>
	 * METHOD DESCRIPTION: <br/>
	 * Used for searching visitor details via the DAO <br/>
	 * 
	 * @return Visitor
	 * 
	 * @param username
	 * @param password
	 * 
	 */
	public Visitor searchVisitor(String username, String password) {

		VisitorDAO visitorDAO = new VisitorDAO();
		Visitor visitor = new Visitor();
		try {
			visitor = visitorDAO.searchUser(username, password);
		} catch (SQLException exception) {
			log.info("Exception is :" + exception.getMessage());
		} catch (ClassNotFoundException exception) {
			log.info("Exception is :" + exception.getMessage());
		}
		return visitor;
	}

	/**
	 * <br/>
	 * METHOD DESCRIPTION: <br/>
	 * Used for registering user to event via the DAO <br/>
	 * 
	 * @param visitor
	 * @param eventid
	 * @param sessionid
	 * 
	 */
	public void RegisterVisitor(Visitor visitor, int eventid, int sessionid) {

		VisitorDAO visitorDAO = new VisitorDAO();
		EventDAO eventDAO = new EventDAO();
		try {
			visitorDAO.registerVisitorToEvent(visitor, eventid, sessionid);
			eventDAO.updateEventNominations(eventid, sessionid);
		} catch (SQLException exception) {
			log.info("Exception is :" + exception.getMessage());
		} catch (ClassNotFoundException exception) {
			log.info("Exception is :" + exception.getMessage());
		} catch (Exception exception) {
			log.info("Exception is :" + exception.getMessage());
		}

	}

	/**
	 * <br/>
	 * METHOD DESCRIPTION: <br/>
	 * Used for displaying all registered events via the DAO <br/>
	 * 
	 * @return ArrayList of Event-related information objects
	 * @param visitor
	 * 
	 */
	public ArrayList<Object[]> showRegisteredEvents(Visitor visitor) {

		VisitorDAO visitorDAO = new VisitorDAO();
		ArrayList<Object[]> showEvents = new ArrayList<Object[]>();
		try {
			showEvents = visitorDAO.registeredEvents(visitor);
		} catch (ClassNotFoundException exception) {
			log.info("-----Exception is :" + exception.getMessage());
		} catch (SQLException exception) {
			log.info("------Exception is :" + exception.getMessage());
		}
		return showEvents;
	}

	/**
	 * <br/>
	 * METHOD DESCRIPTION: <br/>
	 * Used for updating visitor details via the DAO <br/>
	 * 
	 * @return status
	 * 
	 * @param visitor
	 * 
	 */
	public int updateVisitorDetails(Visitor visitor) {

		VisitorDAO visitorDAO = new VisitorDAO();
		int status = 0;
		try {
			status = visitorDAO.updateVisitor(visitor);
		} catch (ClassNotFoundException exception) {
			log.info("Exception is :" + exception.getMessage());
		} catch (SQLException exception) {
			log.info("Exception is :" + exception.getMessage());
		}
		return status;
	}
	
	/**
	 * SERVICE CLASS for updating visitor details	  
	 * @throws FERSGenericException 
	 */
	public int changePassword(Visitor visitor) throws FERSGenericException {

		VisitorDAO visitorDAO = new VisitorDAO();
				
		try{
			
			return visitorDAO.changePassword(visitor);
		
		}catch (ClassNotFoundException e) {
			log.error("ClassNotFoundException: " + e.getMessage());
			throw new FERSGenericException(e.getMessage(), e);
		}catch (SQLException e) {
			log.error("SQLException: " + e.getMessage());
			throw new FERSGenericException(e.getMessage(), e);	
		}
	}

	/**
	 * <br/>
	 * METHOD DESCRIPTION: <br/>
	 * Used for removing event registered by visitor via the DAO <br/>
	 * 
	 * @param visitor
	 * @param eventid
	 * @param sessionid
	 * 
	 */
	public void unregisterEvent(Visitor visitor, int eventid, int eventsessionid) {

		VisitorDAO visitorDAO = new VisitorDAO();
		try {
			visitorDAO.unregisterEvent(visitor, eventid, eventsessionid);
		} catch (ClassNotFoundException exception) {
			log.info("Exception is :" + exception.getMessage());
		} catch (SQLException exception) {
			log.info("Exception is :" + exception.getMessage());
		} catch (Exception exception) {
			log.info("Exception is :" + exception.getMessage());
		}
	}

}
