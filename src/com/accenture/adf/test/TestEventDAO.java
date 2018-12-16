package com.accenture.adf.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.accenture.adf.businesstier.dao.EventDAO;
import com.accenture.adf.businesstier.entity.Event;
import com.accenture.adf.businesstier.entity.EventCoordinator;
import com.accenture.adf.businesstier.entity.Visitor;
import com.accenture.adf.exceptions.FERSGenericException;
import com.accenture.adf.helper.FERSDataConnection;

/**
 * Junit test class for EventDAO class
 * 
 */
public class TestEventDAO {

	private static Connection connection = null;
	private static PreparedStatement statement = null;
	private static ResultSet resultSet = null;
	private ArrayList<Object[]> showAllEvents;
	Event event;
	private EventDAO dao;
	/**
	 * Sets up database connection before other methods are executed in this
	 * class
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpDatabaseConnection() throws Exception {
		connection = FERSDataConnection.createConnection();
	}

	/**
	 * Closes the database connection after all the methods are executed
	 * 
	 * @throws Exception
	 */
	@AfterClass
	public static void tearDownDatabaseConnection() throws Exception {
		/**
		 * @TODO: Close connection object here  
		 */
		FERSDataConnection.closeConnection();
	}

	/**
	 * Sets up the objects required in other methods
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		showAllEvents = new ArrayList<Object[]>();
		dao = new EventDAO();
		String sql = "insert into event values (10000,'diwali','fire crackers','society','2 hours','show')";
		statement = connection.prepareStatement(sql);
		statement.executeUpdate();
		sql = "insert into visitor values(10000,'divyansh','divyansh','divyansh','prateek','divyansh@gmail.com',7897948298,'sector 23,gurgaon',1)";
		statement = connection.prepareStatement(sql);
		statement.executeUpdate();
		sql = "insert into eventcoordinator values(10000,'divyansh','divyansh','divyansh','prateek','divyansh@gmail.com','67237891678','gurgaon')";
		statement = connection.prepareStatement(sql);
		statement.executeUpdate();
		sql = "insert into eventsession values(10000,10000,10000,10)";
		statement = connection.prepareStatement(sql);
		statement.executeUpdate();
		sql = "insert into eventsessionsignup values(10000,10000,10000,10000)";
		statement = connection.prepareStatement(sql);
		statement.executeUpdate();
	}

	/**
	 * Deallocate the resources after execution of method
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		/**
		 * @TODO: Release all the objects here by assigning them null  
		 */
		String sql = "delete from eventsessionsignup where signupid = 10000";
		statement = connection.prepareStatement(sql);
		statement.executeUpdate();
		sql = "delete from eventsession where eventsessionid = 10000";
		statement = connection.prepareStatement(sql);
		statement.executeUpdate();
		sql = "delete from eventcoordinator where eventcoordinatorid = 10000";
		statement = connection.prepareStatement(sql);
		statement.executeUpdate();
		sql = "delete from visitor where visitorid = 10000";
		statement = connection.prepareStatement(sql);
		statement.executeUpdate();
	    sql = "delete from event where eventid = 10000";
		statement = connection.prepareStatement(sql);
		statement.executeUpdate();
	}

	/**
	 * Positive test case to test the method showAllEvents
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void testShowAllEvents_Positive() throws ClassNotFoundException, SQLException {
		/**
		 * @TODO: Call showAllEvents method and assert it for
		 * size of returned type list
		 */
		List<Object[]> eventList = null;
		statement = connection.prepareStatement("SELECT E1.EVENTID, E1.NAME, E1.DESCRIPTION, E1.PLACES, E1.DURATION, E1.EVENTTYPE,  E2.EVENTSESSIONID, E2.SEATSAVAILABLE FROM EVENT E1, EVENTSESSION E2 WHERE E1.EVENTID = E2.EVENTID");
		resultSet = statement.executeQuery();
		while(resultSet.next())
		{
			Object[] object = new Object[8];
			eventList = new ArrayList<Object[]>();
			object[0] = resultSet.getInt("eventid");
			object[1] = resultSet.getString("name");
			object[2] = resultSet.getString("description");
			object[3] = resultSet.getString("places");
			object[4] = resultSet.getString("duration");
			object[5] = resultSet.getString("eventtype");
			object[6] = resultSet.getInt("seatsavailable");
			object[7] = resultSet.getInt("eventsessionid");
			eventList.add(object);
		}
		showAllEvents = dao.showAllEvents();
		assertEquals(eventList.size(),showAllEvents.size());
	}
	
	/**
	 * Junit test case to test positive case for updateEventDeletions
	 * @throws Exception 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void testUpdateEventDeletions_Positive() throws ClassNotFoundException, SQLException, Exception {
		/**
		 * @TODO: Find out seats available for an event by opening a connection
		 * and calling the query SELECT SEATSAVAILABLE FROM EVENT WHERE EVENTID = ?
		 * Call the updateEventDeletions for eventId
		 * Again find out the seats available for this event
		 * testSeatsAvailableBefore should be 1 more then testSeatsAvailableAfter
		 */		
		int seatsavailableb = 0,seatsavailablea = 0;
		String sql = "select seatsavailable from eventsession where eventsessionid = 10000";
		statement = connection.prepareStatement(sql);
		resultSet = statement.executeQuery();
		while(resultSet.next())
		{
			seatsavailableb = resultSet.getInt(1);
		}
		dao.updateEventDeletions(10000, 10000);
		statement = connection.prepareStatement(sql);
		resultSet = statement.executeQuery();
		while(resultSet.next())
		{
			seatsavailablea = resultSet.getInt(1);
		}
		int diff = seatsavailablea-seatsavailableb;
		assertEquals(1,diff);
	}

	/**
	 * Negative test case for method updateEventDeletions
	 * @throws Exception 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	@Test(expected = FERSGenericException.class)
	public void testUpdateEventDeletions_Negative() throws ClassNotFoundException, SQLException, Exception {
		/**
		 * @TODO: Call updateEventDeletions for incorrect eventid and it should
		 * throw an exception
		 */
		dao.updateEventDeletions(000000,000000);
	}

	/**
	 * Positive test case for method updateEventNominations
	 * @throws Exception 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void testUpdateEventNominations_Positive() throws ClassNotFoundException, SQLException, Exception {
		/**
		 * @TODO: Find out seats available for an event by opening a connection
		 * and calling the query SELECT SEATSAVAILABLE FROM EVENT WHERE EVENTID = ?
		 * Call the updateEventNominations for eventId
		 * Again find out the seats available for this event
		 * testSeatsAvailableBefore should be 1 less then testSeatsAvailableAfter
		 */	
		int seatsavailableb = 0,seatsavailablea = 0;
		String sql = "select seatsavailable from eventsession where eventsessionid = 10000";
		statement = connection.prepareStatement(sql);
		resultSet = statement.executeQuery();
		while(resultSet.next())
		{
			seatsavailableb = resultSet.getInt(1);
		}
		dao.updateEventNominations(10000,10000);
		statement = connection.prepareStatement(sql);
		resultSet = statement.executeQuery();
		while(resultSet.next())
		{
			seatsavailablea = resultSet.getInt(1);
		}
		int diff = seatsavailableb-seatsavailablea;
		assertEquals(1,diff);
		
	}

	/**
	 * Negative test case for method updateEventNominations
	 * @throws Exception 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	@Test(expected = FERSGenericException.class)
	public void testUpdateEventNominations_Negative() throws ClassNotFoundException, SQLException, Exception {
		/**
		 * @TODO: Call updateEventNominations for incorrect eventid and it should
		 * throw an exception
		 */	
		dao.updateEventNominations(000000, 000000);
	}

	/**
	 * Positive test case for method checkEventsofVisitor
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void testCheckEventsOfVisitor_Positive() throws ClassNotFoundException, SQLException {
		/**
		 * @TODO: Create visitor object by setting appropriate values
		 * Call checkEventsofVisitor method by passing this visitor object and
		 * valid eventId
		 * Assert the value of return type 
		 */	
		Visitor visitor = new Visitor();
		visitor.setVisitorId(10000);
		visitor.setFirstName("divyansh");
 		boolean status = dao.checkEventsofVisitor(visitor, 10000, 10000);
 		assertEquals(true,status);
	}
	
	/**
	 * Junit test case for getEventCoordinator
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void testGetEventCoordinator() throws ClassNotFoundException, SQLException{
		/**
		 * @TODO: Call getEventCoordinator method
		 * Assert the size of return type arraylist
		 */		
		List<EventCoordinator> coordinatorList,eventCoordinatorList = new ArrayList<EventCoordinator>();
		EventCoordinator eventCoordinator;
		statement = connection.prepareStatement("SELECT EVENTCOORDINATORID, USERNAME FROM EVENTCOORDINATOR");
		resultSet = statement.executeQuery();
		while(resultSet.next())
		{
			eventCoordinator = new EventCoordinator();
			int id = resultSet.getInt(1);
			String userName = resultSet.getString(2);
			eventCoordinator.setEventcoordinatorid(id);
			eventCoordinator.setUserName(userName);
			eventCoordinatorList.add(eventCoordinator);
		}
		coordinatorList = dao.getEventCoordinator();
		assertEquals(eventCoordinatorList.size(),coordinatorList.size());
		
	}
	
	/**
	 * Junit test case for getEvent
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void testGetEvent() throws SQLException, ClassNotFoundException{
		/**
		 * @TODO: Call getEvent method 
		 * Assert the returned Event type with the passed value of event id
		 */		
		event = dao.getEvent(10000,10000);
		assertNotNull(event);
	}	
	
	/**
	 * Junit test case for updateEvent
	 * @throws SQLException 
	 * @throws  
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void testInsertEvent() throws ClassNotFoundException, SQLException{
		/**
		 * @TODO: Create Event object by setting appropriate values
		 * Call insertEvent method by passing this event object
		 * Assert the status of return type of this insertEvent method
		 */		
		event = new Event();
		event.setEventid(10001);
		event.setDescription("kite");
		event.setDuration("2 hours");
		event.setName("Ravan");
		event.setPlace("sector 23,gurgaon");
		event.setEventtype("festival");
		event.setEventCoordinatorId(10000);
		event.setSeatsavailable("10");
		event.setEventSession(2);
		int status = dao.insertEvent(event);
		statement = connection.prepareStatement("delete from eventsession where eventid = 10001");
		statement.executeUpdate();
		statement = connection.prepareStatement("delete from event where eventid = 10001");
		statement.executeUpdate();
		assertEquals(1,status);
	}
	
	/**
	 * Junit test case for updateEvent
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void testUpdateEvent() throws ClassNotFoundException, SQLException{
		/**
		 * @TODO: Fetch Event object by calling showAllEvents method
		 * Update the values of event object
		 * Call updateEvent method by passing this modified event as object
		 * Assert the status of return type of updateEvent method
		 */			
		event = new Event();
		event.setEventid(10000);
		event.setDescription("kite");
		event.setDuration("2 hours");
		event.setName("Ravan");
		event.setPlace("sector 23,gurgaon");
		event.setEventtype("festival");
		event.setSeatsavailable(Integer.toString(20));
		event.setEventSession(10000);
		int status = dao.updateEvent(event);
		assertEquals(1,status);
	}
	
	/**
	 * Junit test case for deleteEvent
	 * @throws FERSGenericException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void testDeleteEvent() throws ClassNotFoundException, SQLException, FERSGenericException{
		/**
		 * @TODO: Fetch Event object by calling showAllEvents method		 * 
		 * Call deleteEvent method by passing this event id and event session id as object
		 * Assert the status of return type of updateEvent method
		 */		
		String sql = "insert into event values (10001,'diwali','fire crackers','society','2 hours','show')";
		statement = connection.prepareStatement(sql);
		statement.executeUpdate();
		sql = "insert into eventcoordinator values(10001,'divyansh','divyansh','divyansh','prateek','divyansh@gmail.com','67237891678','gurgaon')";
		statement = connection.prepareStatement(sql);
		statement.executeUpdate();
		sql = "insert into eventsession values(10001,10001,10001,10)";
		statement = connection.prepareStatement(sql);
		statement.executeUpdate();
		int status = dao.deleteEvent(10001, 10001);
		statement = connection.prepareStatement("delete from eventcoordinator where eventcoordinatorid = 10001");
		statement.executeUpdate();
		assertEquals(1,status);
		
	}

}
