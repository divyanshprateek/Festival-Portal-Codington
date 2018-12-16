package com.accenture.adf.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import com.accenture.adf.businesstier.controller.EventController;
import com.accenture.adf.exceptions.FERSGenericException;
import com.accenture.adf.helper.FERSDataConnection;

/**
 * Junit test class for EventController
 * 
 */
public class TestEventController {

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private ModelAndView modelAndView;
	private EventController controller;
	Connection connection;
	PreparedStatement statement;
	/**
	 * Sets up initial objects required in other methods
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		modelAndView = new ModelAndView();
		controller = new EventController();
		response = new MockHttpServletResponse();	
	    connection = FERSDataConnection.createConnection();
		String sql = "insert into event values (10000,'diwali','fire crackers','society','2 hours','show')";
		statement = connection.prepareStatement(sql);
		statement.executeUpdate();
		sql = "insert into visitor values(10000,'divyansh','divyansh','divyansh','prateek','divyansh@gmail.com',7897948298,'sector 23,gurgaon',false)";
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
	 * Deallocate the objects after execution of every method
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
	 * Test case to test the positive scenario for getAvailableEvents method
	 */
	@Test
	public void testGetAvailableEvents_Positive() {

		try {
			request = new MockHttpServletRequest("GET", "/catalog.htm");
			modelAndView = controller.getAvailableEvents(request, response);
		} catch (Exception exception) {
			fail("Exception");
		}
		assertEquals("/eventCatalog.jsp", modelAndView.getViewName());
	}

	/**
	 * Executes the negative scenario for getAvailableEvents method
	 * @throws Exception 
	 */
	@Test(expected = FERSGenericException.class)
	public void testGetAvailableEvents_Negative() throws Exception {
		/**
		 * @TODO: Call getAvailableEvents methods  by passing request as null
		 * and assert it for appropriate model view name
		 */	
		request = null;
		modelAndView = controller.getAvailableEvents(request, response);
	}
	
	/**
	 * Test case to test the positive scenario for displayEvent method
	 */
	@Test
	public void testDisplayEvent_Positive() {
		/**
		 * @TODO: Call displayEvent methods and assert
		 * it for appropriate model view name
		 */	
		try
		{
			request = new MockHttpServletRequest("GET", "/displayEvent.htm");
			request.setParameter("eventId", "10000");
			request.setParameter("sessionId", "10000");
			modelAndView = controller.displayEvent(request,response);
			
		}catch(Exception e)
		{
			fail("Exception");
		}
		assertEquals("/addEvent.jsp",modelAndView.getViewName());
		
	}

	/**
	 * Executes the negative scenario for displayEvent method
	 * @throws Exception 
	 */
	@Test(expected =  FERSGenericException.class)
	public void testDisplayEvent_Negative() throws Exception {
		/**
		 * @TODO: Call displayEvent methods  by passing request as null
		 * and assert it for appropriate model view name
		 */	
		request = null;
		modelAndView = controller.displayEvent(request, response);
	}	
	
	/**
	 * Test case to test the positive scenario for updateEvent method
	 */
	@Test
	public void testUpdateEvent_Positive() {
		/**
		 * @TODO: Call updateEvent methods and assert
		 * it for appropriate model view name
		 */	
		try {
			request = new MockHttpServletRequest("GET", "/updateEvent.htm");
		   request.setParameter("eventId","10000");
			request.setParameter("sessionId","10000");
			request.setParameter("coordinator","10000");
			request.setParameter("eventName","diwali");
			request.setParameter("desc","fire crackers");
			request.setParameter("place","society");
			request.setParameter("duration","2 hours");
			request.setParameter("eventType","show");
			request.setParameter("ticket","10");
		    request.setParameter("isAdd","false");
			modelAndView = controller.updateEvent(request, response);
		} catch (Exception exception) {
			fail("Exception");
		}
		assertEquals("/addEvent.jsp", modelAndView.getViewName());
		//inserting eventsession in event dao is left
	}

	/**
	 * Executes the negative scenario for updateEvent method
	 * @throws Exception 
	 */
	@Test(expected = FERSGenericException.class)
	public void testUpdateEvent_Negative() throws Exception {
		/**
		 * @TODO: Call updateEvent methods  by passing request as null
		 * and assert it for appropriate model view name
		 */	
		request = null;
		modelAndView = controller.updateEvent(request, response);
	}
	
	/**
	 * Test case to test the positive scenario for displayEvent method
	 * @throws Exception 
	 */
	@Test
	public void testDeleteEvent_Positive() throws Exception {
		/**
		 * @TODO: Call deleteEvent methods and assert
		 * it for appropriate model view name
		 */
		request = new MockHttpServletRequest("GET", "/deleteEvent.htm");
		request.setParameter("eventId","10000");
		request.setParameter("sessionId","10000");
		modelAndView = controller.deleteEvent(request, response);
		assertEquals("/eventCatalog.jsp",modelAndView.getViewName());
	}

	/**
	 * Executes the negative scenario for displayEvent method
	 * @throws Exception 
	 */
	@Test(expected = FERSGenericException.class)
	public void testDeleteEvent_Negative() throws Exception {
		/**
		 * @TODO: Call deleteEvent methods  by passing request as null
		 * and assert it for appropriate model view name
		 */	
		request = null;
		modelAndView = controller.deleteEvent(request, response);
	}		

}
