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
import org.junit.Before;
import org.junit.Test;

import com.accenture.adf.businesstier.entity.Event;
import com.accenture.adf.businesstier.entity.EventCoordinator;
import com.accenture.adf.businesstier.entity.Visitor;
import com.accenture.adf.businesstier.service.EventServiceImpl;
import com.accenture.adf.helper.FERSDataConnection;

/**
 * Junit test case to test class EventServiceImpl
 * 
 */
public class TestEventServiceImpl {

	private EventServiceImpl eventServiceImpl;
	private PreparedStatement statement;
	private Connection connection;
	private ResultSet resultSet;
	/**
	 * Set up the objects required before execution of every method
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		eventServiceImpl = new EventServiceImpl();
		new Visitor();
		connection = FERSDataConnection.createConnection();
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
	 * Deallocates the objects after execution of every method
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
		FERSDataConnection.closeConnection();
	}

	/**
	 * Test case to test the method getAllEvents
	 * @throws SQLException 
	 */
	@Test
	public void testGetAllEvents() throws SQLException {
		/**
		 * @TODO: Call getAllEvents method and assert it for the size of returned array
		 */	
		List<Object[]> eventList = null,elist;
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
		elist = eventServiceImpl.getAllEvents();
		assertEquals(eventList.size(),elist.size());
	}

	/**
	 * Test case to test the method checkEventsofVisitor
	 */
	@Test
	public void testCheckEventsofVisitor() {
		/**
		 * @TODO: Call checkEventsofVisitor and assert the returned type of this method
		 * for appropriate return type
		 */	
		Visitor visitor = new Visitor();
		visitor.setFirstName("divyansh");
		visitor.setVisitorId(10000);
		boolean status = eventServiceImpl.checkEventsofVisitor(visitor, 10000, 10000);
		assertEquals(true,status);
	}

	/**
	 * Test case to test the method updateEventDeletions
	 * @throws SQLException 
	 */
	@Test
	public void testUpdateEventDeletions() throws SQLException {
		/**
		 * @TODO: Call updateEventDeletions and assert the return type of this method
		 */	
		int seatsavailableb = 0,seatsavailablea = 0;
		String sql = "select seatsavailable from eventsession where eventsessionid = 10000";
		statement = connection.prepareStatement(sql);
		resultSet = statement.executeQuery();
		while(resultSet.next())
		{
			seatsavailableb = resultSet.getInt(1);
		}
		eventServiceImpl.updateEventDeletions(10000, 10000);
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
	 * Junit test case for getEventCoordinator
	 * @throws SQLException 
	 */
	@Test
	public void testGetEventCoordinator() throws SQLException {
		/**
		 * @TODO: Call getAllEventCoordinators and assert the size of return type of this method
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
		coordinatorList = eventServiceImpl.getAllEventCoordinators();
		assertEquals(eventCoordinatorList.size(),coordinatorList.size());
	}

	/**
	 * Junit test case for getEvent
	 */
	@Test
	public void testGetEvent() {
		/**
		 * @TODO: Call getEvent and assert the event id of this event with 
		 * passed event id 
		 */		
		Event event = eventServiceImpl.getEvent(10000,10000);
		assertNotNull(event);
	}

	/**
	 * Junit test case for updateEvent
	 * @throws SQLException 
	 */
	@Test
	public void testInsertEvent() throws SQLException {
		/**
		 * @TODO: Call insertEvent
		 * Create event object by setting appropriate values
		 * Assert the status of insertEvent method
		 */		
		Event event = new Event();
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
		int status = eventServiceImpl.insertEvent(event);
		statement = connection.prepareStatement("delete from eventsession where eventid = 10001");
		statement.executeUpdate();
		statement = connection.prepareStatement("delete from event where eventid = 10001");
		statement.executeUpdate();
		assertEquals(1,status);
	}

	/**
	 * Junit test case for updateEvent
	 */
	@Test
	public void testUpdateEvent() {
		/**
		 * @TODO: Fetch Event object by calling getAllEvents method 
		 * Update event object by setting appropriate values
		 * Call updateEvent method
		 * Assert the status of updateEvent method
		 */	
		Event event = new Event();
		event.setEventid(10000);
		event.setDescription("kite");
		event.setDuration("2 hours");
		event.setName("Ravan");
		event.setPlace("sector 23,gurgaon");
		event.setEventtype("festival");
		event.setSeatsavailable(Integer.toString(20));
		event.setEventSession(10000);
		int status = eventServiceImpl.updateEvent(event);
		assertEquals(1,status);
	}

	/**
	 * Junit test case for deleteEvent
	 * @throws SQLException 
	 */
	@Test
	public void testDeleteEvent() throws SQLException {
		/**
		 * @TODO: Fetch Event object by calling getAllEvents method 
		 * Update event object by setting appropriate values
		 * Call deleteEvent method
		 * Assert the status of deleteEvent method
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
		int status = eventServiceImpl.deleteEvent(10001, 10001);
		statement = connection.prepareStatement("delete from eventcoordinator where eventcoordinatorid = 10001");
		statement.executeUpdate();
		assertEquals(1,status);
	}

}
