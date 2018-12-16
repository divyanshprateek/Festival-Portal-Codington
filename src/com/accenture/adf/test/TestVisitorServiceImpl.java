package com.accenture.adf.test;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.accenture.adf.businesstier.dao.EventDAO;
import com.accenture.adf.businesstier.dao.VisitorDAO;
import com.accenture.adf.businesstier.entity.Event;
import com.accenture.adf.businesstier.entity.Visitor;
import com.accenture.adf.businesstier.service.VisitorServiceImpl;
import com.accenture.adf.helper.FERSDataConnection;

/**
 * Junit test class for VisitorServiceImpl
 *
 */
public class TestVisitorServiceImpl {

	private Visitor visitor;
	private VisitorServiceImpl visitorServiceImpl;
	private VisitorDAO visitorDao;
	private Connection connection;
	private Event event;
	private java.sql.PreparedStatement statement1;
	int visitorId=0;

	/**
	 * Set up the initial methods 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {		
		visitorServiceImpl = new VisitorServiceImpl();
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
	 * Deallocates the objects after execution of every method
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		/**
		 * @TODO: Release all the objects here by assigning them null  
		 */
		statement1=connection.prepareStatement("DELETE FROM EVENTSESSIONSIGNUP WHERE EVENTSESSIONID=1;");
		//statement1.setInt(1, 1);
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
	 * Test case for method createVisitor
	 */
	@Test
	public void testCreateVisitor() {
		/**
		 * @TODO: Set the appropriate values for visitor object and
		 * call the method createVisitor by passing an argument of this visitor 
		 * object and then asserting the returned type of this method
		 */		
	}

	/**
	 * Test case for method createVisitor
	 */
	@Test
	public void testSearchVisitor() {
		/**
		 * @TODO: Call searchVisitor method by passing the appropriate arguments 
		 * and then asserting the returned type visitor username with the argument passed
		 */		
		assertEquals(visitor.getUserName(),visitorServiceImpl.searchVisitor(visitor.getUserName(), visitor.getPassword()).getUserName());
	}

	/**
	 * Test case for method RegisterVisitor
	 * @throws SQLException 
	 */
	@Test
	public void testRegisterVisitor() throws SQLException {
		/**
		 * @TODO: Call RegisterVisitor method by passing visitor object which 
		 * can be retrieved using searchVisitor method and then asserting the returned
		 * type of RegisterVisitor method 
		 */		
		visitorServiceImpl.RegisterVisitor(visitorServiceImpl.searchVisitor(visitor.getUserName(), visitor.getPassword()), event.getEventid(), 1);
		int visitorId=0;
		 
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
	 * Test case for method showRegisteredEvents
	 */
	@Test
	public void testShowRegisteredEvents() {
		/**
		 * @TODO: Call showRegisteredEvents method by passing visitor object which 
		 * can be retrieved using searchVisitor method and then asserting the returned
		 * type of showRegisteredEvents method 
		 */	
		int eventID=0;
		visitorServiceImpl.RegisterVisitor(visitorServiceImpl.searchVisitor(visitor.getUserName(), visitor.getPassword()), event.getEventid(), 1);
		List<Object[]> events= visitorServiceImpl.showRegisteredEvents(visitor);
		for(Object[] o: events)
		{
			eventID=(Integer) o[0];
		}
		assertEquals(event.getEventid(),eventID);
		
	}

	/**
	 * Test case for method updateVisitorDetails
	 */
	@Test
	public void testUpdateVisitorDetails() {
		/**
		 * @TODO: Call updateVisitorDetails method by passing the visitor object which
		 * can be retrieved using searchVisitor method and then asserting the returned
		 * type of updateVisitorDetails
		 */		
		Visitor visitor1=visitorServiceImpl.searchVisitor(visitor.getUserName(), visitor.getPassword());
		visitor1.setPhoneNumber("8971578581");
		assertEquals(1,visitorServiceImpl.updateVisitorDetails(visitor1));
		
		
	}

	/**
	 * Test case for method unregisterEvent
	 * @throws SQLException 
	 */
	@Test
	public void testUnregisterEvent() throws SQLException {
		/**
		 * @TODO: Call unregisterEvent method by passing the visitor object which can be
		 * retrieved using searchVisitor method and then asserting the returned type 
		 * of unregisterEvent
		 */		
		visitorServiceImpl.RegisterVisitor(visitorServiceImpl.searchVisitor(visitor.getUserName(), visitor.getPassword()), event.getEventid(), 1);
		int visitorId=0;
		 visitorServiceImpl.unregisterEvent(visitor, event.getEventid(), 1);
		statement1=connection.prepareStatement("SELECT VISITORID FROM EVENTSESSIONSIGNUP WHERE EVENTSESSIONID=? AND EVENTID=?;");
		statement1.setInt(1, 1);
		statement1.setInt(2, event.getEventid());
		ResultSet resultSet=statement1.executeQuery();
		while(resultSet.next())
		{
		  visitorId=resultSet.getInt(1);
		}
		  assertEquals(visitorId,0);
	}

}
