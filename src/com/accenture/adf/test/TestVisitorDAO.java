package com.accenture.adf.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.accenture.adf.businesstier.dao.EventDAO;
import com.accenture.adf.businesstier.dao.VisitorDAO;
import com.accenture.adf.businesstier.entity.Event;
import com.accenture.adf.businesstier.entity.Visitor;
import com.accenture.adf.helper.FERSDataConnection;

/**
 * JUnit test case for VisitorDAO class for testing all repository methods to
 * call database sub-routines
 * 
 */
public class TestVisitorDAO {

	private Visitor visitor;
	private VisitorDAO visitorDao;
	private Connection connection;
	private Event event;
	private java.sql.PreparedStatement statement1;
	int visitorId=0;

	/**
	 * Setting up initial objects
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		visitorDao = new VisitorDAO();
		new EventDAO();
		visitor = new Visitor();
		visitor.setUserName("ylee");
		visitor.setPassword("password");
		visitor.setFirstName("Kumar");
		visitor.setLastName("Apurv");
		visitor.setEmail("kumarapurv@accenture.com");
		visitor.setAddress("Bhagalpur");
		visitor.setPhoneNumber("9015085757");
		connection=FERSDataConnection.createConnection();
		visitorDao.insertData(visitor);
		statement1 = connection.prepareStatement("SELECT VISITORID FROM VISITOR WHERE USERNAME=?;");
		statement1.setString(1,visitor.getUserName());
		ResultSet resultSet=statement1.executeQuery();
		while(resultSet.next())
		{
			visitorId=resultSet.getInt(1);
			visitor.setVisitorId(resultSet.getInt(1));
		}
		statement1=connection.prepareStatement("UPDATE VISITOR SET ISADMIN=	1;");
		statement1.executeUpdate();
		event=new Event();
		event.setEventid(1111);
		event.setDescription("Music Class");
		event.setDuration("10:100");
		event.setName("BhavyA Music");
		event.setPlace("delhi");
		event.setEventtype("Music");
		event.setEventCoordinatorId(visitorId);
		event.setSeatsavailable("1111");
		statement1=connection.prepareStatement("INSERT INTO EVENT(EVENTID, NAME, DESCRIPTION, PLACES, DURATION, EVENTTYPE) VALUES(?,?,?,?,?,?);");
		//EVENTID, NAME, DESCRIPTION, PLACES, DURATION, EVENTTYPE
		statement1.setInt(1	, event.getEventid());
		statement1.setString(2, event.getName());
		statement1.setString(3, event.getDescription());
		statement1.setString(4, event.getPlace());
		statement1.setString(5,event.getDuration());
		statement1.setString(6,event.getEventtype());
		statement1.executeUpdate();
		statement1=connection.prepareStatement("INSERT INTO EVENTCOORDINATOR VALUES(?,?,?,?,?,?,?,?);");
		statement1.setInt(1, visitorId);
		statement1.setString(2,"ylee");
		statement1.setString(3, "password");
		statement1.setString(4, "Kumar");
		statement1.setString(5, "Apurv");
		statement1.setString(6,"kumarapurv@accenture.com");
		statement1.setString(7, "9015085757");
		statement1.setString(8,"Bhagalpur");
		statement1.executeUpdate();
		statement1=connection.prepareStatement("INSERT INTO EVENTSESSION(EVENTSESSIONiD, EVENTCOORDINATORID, EVENTID, SEATSAVAILABLE) VALUES (?,?,?,?);");
		//EVENTSESSIONiD, EVENTCOORDINATORID, EVENTID, SEATSAVAILABLE) VALUES (?,?,?,?)
		statement1.setInt(1, 1);
		statement1.setInt(2, event.getEventCoordinatorId());
		statement1.setInt(3, event.getEventid());
		statement1.setInt(4, Integer.parseInt(event.getSeatsavailable()));
		statement1.executeUpdate();
	}

	/**
	 * Deallocating objects after execution of every method
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		/**
		 * @TODO: Release all the objects here by assigning them null  
		 */
		statement1=connection.prepareStatement("DELETE FROM EVENTSESSIONSIGNUP WHERE EVENTSESSIONID=1;");
//		statement1.setInt(1, 1);
		statement1.executeUpdate();
		statement1=connection.prepareStatement("DELETE FROM EVENTSESSION WHERE EVENTSESSIONID=1;");
		statement1.executeUpdate();
		statement1=connection.prepareStatement("DELETE FROM EVENTCOORDINATOR WHERE EVENTCOORDINATORID =?");
		statement1.setInt(1, visitorId);
		statement1.executeUpdate();
		statement1=connection.prepareStatement("DELETE FROM VISITOR WHERE USERNAME='ylee';");
		statement1.executeUpdate();
		statement1=connection.prepareStatement("DELETE FROM EVENT WHERE EVENTID=1111;");
		statement1.executeUpdate();
	}

	/**
	 * Test case for method insertData
	 * @throws Exception 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void testInsertData() throws ClassNotFoundException, SQLException, Exception {
		/**
		 * @TODO: Create visitor object by setting appropriate values
		 * Call insertData method by passing this visitor object
		 * Search this new visitor object by calling searchUser method
		 * Assert the values of username
		 */		
		Visitor visitor1 = new Visitor();
		visitor1.setUserName("ylee");
		visitor1.setPassword("password");
		visitor1.setFirstName("Kumar");
		visitor1.setLastName("Apurv");
		visitor1.setEmail("kumarapurv@accenture.com");
		visitor1.setAddress("Bhagalpur");
		visitor1.setPhoneNumber("9015085757");
		visitorDao.insertData(visitor1);
		assertEquals(visitor1.getUserName(),(visitorDao.searchUser(visitor1.getUserName(), visitor1.getPassword())).getUserName());
		
	}	

	/**
	 * Test case for method searchUser
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void testSearchUser() throws ClassNotFoundException, SQLException {
		/**
		 * @TODO: Call searchUser method for valid values of username
		 * and password and assert the value of username for the returned type of method
		 */		
		assertEquals(visitor.getUserName(),(visitorDao.searchUser(visitor.getUserName(), visitor.getPassword())).getUserName());
	}

	/**
	 * Test case for method registerVisitorToEvent
	 * @throws Exception 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void testRegisterVisitorToEvent() throws ClassNotFoundException, SQLException, Exception {
		/**
		 * @TODO: Fetch visitor object by calling searchUser for valid values of username and password
		 * Pass this visitor object and valid eventid to registerVisitorToEvent method
		 * and assert the value
		 */		
		  int visitorId=0;
		  visitorDao.registerVisitorToEvent(visitorDao.searchUser(visitor.getUserName(), visitor.getPassword()), event.getEventid(), 1);
		  statement1=connection.prepareStatement("SELECT VISITORID FROM EVENTSESSIONSIGNUP WHERE EVENTSESSIONID=? AND EVENTID=?;");
		  statement1.setInt(1, 1);
		  statement1.setInt(2, event.getEventid());
		  ResultSet resultSet=statement1.executeQuery();
		  while(resultSet.next())
		  {
			  visitorId=resultSet.getInt(1);
		  }
		  assertEquals(visitorId,visitor.getVisitorId());
	}	

	/**
	 * Test case for method registeredEvents
	 * @throws Exception 
	 */
	@Test
	public void testRegisteredEvents() throws Exception {
		/**
		 * @TODO: Fetch visitor object by calling searchUser for valid values of username and password
		 * Pass this visitor object and valid eventid to registeredEvents method
		 * and assert the value
		 */		
		int eventID=0;
		visitorDao.registerVisitorToEvent(visitorDao.searchUser(visitor.getUserName(), visitor.getPassword()), event.getEventid(), 1);
		Visitor visitor2=visitorDao.searchUser(visitor.getUserName(), visitor.getPassword());
		
		ArrayList<Object[]> object = visitorDao.registeredEvents(visitor2);
		
		for(Object[] o:object)
		{
			eventID=(Integer) o[0];
		}
		assertEquals(eventID,event.getEventid());
	}

	/**
	 * Test case for method updateVisitor
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void testUpdateVisitor() throws ClassNotFoundException, SQLException {
		/**
		 * @TODO: Fetch visitor object by calling searchUser for valid values of username and password
		 * Update the value in this visitor object
		 * Pass this visitor object to updateVisitor method
		 * and assert the value of changed value
		 */		
		Visitor visitor2=visitorDao.searchUser(visitor.getUserName(), visitor.getPassword());
		visitor2.setLastName("Babuta");
		
		assertEquals(visitorDao.updateVisitor(visitor2),1);
	}

	/**
	 * Test case for method registeredEvents
	 * @throws Exception 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void testUnregisterEvent() throws ClassNotFoundException, SQLException, Exception {
		/**
		 * @TODO: Fetch visitor object by calling searchUser for valid values of username and password
		 * Pass this visitor object and valid eventid to unregisterEvent method
		 * and assert the value
		 */		
		  int visitorId=0;
		  visitorDao.registerVisitorToEvent(visitorDao.searchUser(visitor.getUserName(), visitor.getPassword()), event.getEventid(), 1);
		  
		  visitorDao.unregisterEvent(visitor, event.getEventid(), 1);
		 
		  statement1=connection.prepareStatement("SELECT VISITORID FROM EVENTSESSIONSIGNUP WHERE EVENTSESSIONID=? AND EVENTID=?;");
		  statement1.setInt(1, 1);
		  statement1.setInt(2, event.getEventid());
		  
		  ResultSet resultSet=statement1.executeQuery();
		  while(resultSet.next())
		  {
			  visitorId=resultSet.getInt(0);
		  }
		  assertEquals(visitorId,0);
	}
	
	/**
	 * Test case for method change password
	 */
	/*@Test
	public void testChangePassword_VisitorNull() {
		*//**
		 * @TODO: Call changePassword method by passing visitor object as null
		 *//*		
	}*/
	
	/**
	 * Test case for method change password
	 */
	@Test
	public void testChangePassword_VisitorNull() {
		try {
			visitor = null;
			visitorDao.changePassword(visitor);
		} catch (SQLException exception) {
			fail("SQL Exception");
		} catch (ClassNotFoundException exception) {
			fail("Class Not Found Exception");
		} catch (Exception exception) {
			fail("NULL Exception");
		}
	}

}
