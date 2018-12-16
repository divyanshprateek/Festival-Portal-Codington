package com.accenture.adf.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.ModelAndView;

import com.accenture.adf.businesstier.controller.VisitorController;
import com.accenture.adf.businesstier.dao.EventDAO;
import com.accenture.adf.businesstier.dao.VisitorDAO;
import com.accenture.adf.businesstier.entity.Event;
import com.accenture.adf.businesstier.entity.Visitor;
import com.accenture.adf.exceptions.FERSGenericException;
import com.accenture.adf.helper.FERSDataConnection;

/**
 * Junit test case to test the class VisitorController
 * 
 */
public class TestVisitorController {

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private HttpSession session;
	private ModelAndView modelAndView;
	private VisitorController controller;
	private VisitorDAO visitorDao;
	private Connection connection;
	private Visitor visitor ;
	private Event event;
	private java.sql.PreparedStatement statement1;
	int visitorId=0;
	/**
	 * Set up initial methods required before execution of every method
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		modelAndView = new ModelAndView();
		controller = new VisitorController();
		session = new MockHttpSession();
		response = new MockHttpServletResponse();
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
	 * Deallocate objects after execution of every method
	 * 
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
	 * Positive test case to test the method newVisitor
	 */
	@Test
	public void testNewVisitor_Positive() {
		try {
			request = new MockHttpServletRequest("GET", "/newVistor.htm");

			request.setParameter("USERNAME", visitor.getUserName());
			request.setParameter("PASSWORD", visitor.getPassword());
			request.setParameter("FIRSTNAME", visitor.getFirstName());
			request.setParameter("LASTNAME", visitor.getLastName());
			request.setParameter("EMAIL", visitor.getEmail());
			request.setParameter("PHONENO", visitor.getPhoneNumber());
			request.setParameter("ADDRESS", visitor.getAddress());
			modelAndView = controller.newVisitor(request, response);
		} catch (Exception exception) {
			fail("Exception");
		}
		assertEquals("/registration.jsp", modelAndView.getViewName());
	}

	/**
	 * Negative test case to test the method newVisitor
	 * @throws Exception 
	 */
	@Test(expected=FERSGenericException.class)
	public void testNewVisitor_Negative() throws Exception {
		/**
		 * @TODO: Call newVisitor method by passing request object as null and 
		 * asserting the model view name
		 */	
		request=null;
		modelAndView=controller.newVisitor(request, response);
		
	}

	/**
	 * Positive test case to test the method searchVisitor
	 */
	@Test
	public void testSearchVisitor_Positive() {
		/**
		 * @TODO: Create MockHttpServletRequest object 
		 * Set request parameters for USERNAME and PASSWORD for valid values
		 * Call searchVisitor method and assert model view name 
		 */	
		request= new MockHttpServletRequest("GET","searchVisitor.htm");
		request.setParameter("USERNAME", visitor.getUserName());
		request.setParameter("PASSWORD", visitor.getPassword());
		try {
			modelAndView=controller.searchVisitor(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals("/visitormain.jsp",modelAndView.getViewName());
		
	}

	/**
	 * Negative test case of invalid user for method searchVisitor
	 */
	@Test
	public void testSearchVisitor_Negative_InvalidUser() {
		/**
		 * @TODO: Create MockHttpServletRequest object 
		 * Set request parameters for USERNAME and PASSWORD for invalid values
		 * Call searchVisitor method and assert model view name 
		 */	
		request= new MockHttpServletRequest("GET","searchVisitor.htm");
		request.setParameter("USERNAME", "divyansh");
		request.setParameter("PASSWORD", "sjfhdskjhf");
		try {
			modelAndView=controller.searchVisitor(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals("/index.jsp",modelAndView.getViewName());
		
	}

	/**
	 * Negative test case for method searchVisitor
	 * @throws Exception 
	 */
	@Test(expected=FERSGenericException.class)
	public void testSearchVisitor_Negative() throws Exception {
		/**
		 * @TODO: Call searchVisitor method by passing request object as null and 
		 * asserting the model view name
		 * 
		 */
		request=null;
		modelAndView=controller.searchVisitor(request, response);
		
	}

	/**
	 * Positive test case for method registerVisitor
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void testRegisterVisitor_Positive() throws ClassNotFoundException, SQLException {
		/**
		 * @TODO: Create MockHttpServletRequest object 
		 * Set visitor object in VISITOR session by calling searchUser method from visitorDAO		 
		 * Set request parameters for USERNAME and PASSWORD for valid values
		 * Call registerVisitor method and assert model view name 
		 */		
		request=new MockHttpServletRequest("GET","/eventreg.htm");
		session=request.getSession();
		Visitor visitor1=visitorDao.searchUser(visitor.getUserName(), visitor.getPassword());
		session.setAttribute("VISITOR", visitor1);
		request.setParameter("eventId", Integer.toString(event.getEventid()));
		request.setParameter("sessionId", Integer.toString(1));
		try {
			modelAndView=controller.registerVisitor(request,response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals("/visitormain.jsp",modelAndView.getViewName());
	}	

	/**
	 * Negative test case for method registerVisitor
	 * @throws Exception 
	 */
	@Test(expected=FERSGenericException.class )
	public void testRegisterVisitor_Negative() throws Exception {
		/**
		 * @TODO: Call registerVisitor method by passing request object as null and 
		 * asserting the model view name
		 */	
		request=null;
		modelAndView=controller.registerVisitor(request, response);
	}

	/**
	 * Positive test case for method updateVisitor
	 * @throws Exception 
	 */
	@Test
	public void testUpdateVisitor_Positive() throws Exception {
		/**
		 * @TODO: Create MockHttpServletRequest object 
		 * Set visitor object in VISITOR session by calling searchUser method from visitorDAO		 
		 * Set request parameters for all valid user values
		 * Call updateVisitor method and assert model view name 
		 */	request=new MockHttpServletRequest("GET","/updatevisitor.htm");
		session=request.getSession();
		request.setParameter("username", visitor.getUserName());
		request.setParameter("firstname", visitor.getFirstName());
		request.setParameter("lastname", visitor.getLastName());
		request.setParameter("email", visitor.getEmail());
		request.setParameter("phoneno", "8971578581");
		request.setParameter("address", visitor.getAddress());
		Visitor visitor1=visitorDao.searchUser(visitor.getUserName(), visitor.getPassword());
		session.setAttribute("VISITOR", visitor1);
		modelAndView=controller.updateVisitor(request, response);
		assertEquals("/updatevisitor.jsp",modelAndView.getViewName());
		
	}

	/**
	 * Negative test case for method updateVisitor
	 * @throws Exception 
	 */
	@Test(expected=FERSGenericException.class)
	public void testUpdateVisitor_Negative() throws Exception {
		/**
		 * @TODO: Call updateVisitor method by passing request object as null and 
		 * asserting the model view name
		 */	
		request=null;
		modelAndView=controller.updateVisitor(request, response);
		
	}

	/**
	 * Positive test case for method unregisterEvent
	 * @throws Exception 
	 */
	@Test
	public void testUnregisterEvent_Positive() throws Exception {
		/**
		 * @TODO: Create MockHttpServletRequest object 
		 * Set visitor object in VISITOR session by calling searchUser method from visitorDAO		 
		 * Set request parameters for all USERNAME, PASSWORD and eventId values
		 * Call unregisterEvent method and assert model view name 
		 */	
		request=new MockHttpServletRequest("GET","/eventunreg.htm");
		session=request.getSession();
		session.setAttribute("VISITOR", visitorDao.searchUser(visitor.getUserName(), visitor.getPassword()));
		request.setParameter("eventId", Integer.toString(event.getEventid()));
		request.setParameter("eventsessionid", Integer.toString(1));
		modelAndView=controller.unregisterEvent(request, response);
		assertEquals("/visitormain.jsp",modelAndView.getViewName());
	}

	/**
	 * Negative test case for method unregisterEvent
	 * @throws Exception 
	 */
	@Test(expected=FERSGenericException.class)
	public void testUnregisterEvent_Negative() throws Exception {
		/**
		 * @TODO: Call unregisterEvent method by passing request object as null and 
		 * asserting the model view name
		 */	
		request=null;
		modelAndView=controller.updateVisitor(request, response);
	}

	/**
	 * Positive test case for search events by name
	 * @throws Exception 
	 */
	@Test
	public void testSearchEventsByName_Positive() throws Exception {
		/**
		 * @TODO: Create MockHttpServletRequest object 
		 * Set visitor object in VISITOR session by calling searchUser method from visitorDAO		 
		 * Set request parameters for eventname
		 * Call searchEventsByName method and assert model view name 
		 */		
		request=new MockHttpServletRequest("GET","/searchEventByName.htm");
		session=request.getSession();
		session.setAttribute("VISITOR", visitorDao.searchUser(visitor.getUserName(), visitor.getPassword()));
		request.setParameter("eventname", event.getName());
		modelAndView=controller.searchEventsByName(request, response);
		assertEquals("/visitormain.jsp",modelAndView.getViewName());
	}

	/**
	 * Positive test case for search events by name catalog
	 * @throws Exception 
	 */
	@Test
	public void testSearchEventsByNameCatalog_Positive() throws Exception {
		/**
		 * @TODO: Create MockHttpServletRequest object 
		 * Set visitor object in VISITOR session by calling searchUser method from visitorDAO		 
		 * Set request parameters for eventname
		 * Call searchEventsByNameCatalog method and assert model view name 
		 */		
		request=new MockHttpServletRequest("GET","/searchEventByNameCatalog.htm");
		session=request.getSession();
		session.setAttribute("VISITOR", visitorDao.searchUser(visitor.getUserName(), visitor.getPassword()));
		request.setParameter("eventname", event.getName());
		modelAndView=controller.searchEventsByNameCatalog(request, response);
		assertEquals("/eventCatalog.jsp",modelAndView.getViewName());
	}

	/**
	 * Test case for show events in asc order
	 * @throws Exception 
	 */
	@Test
	public void testShowEventsAsc() throws Exception {
		/**
		 * @TODO: Create MockHttpServletRequest object 
		 * Set visitor object in VISITOR session by calling searchUser method from visitorDAO		
		 * Call showEventsAsc method and assert model view name 
		 */		
		request=new MockHttpServletRequest("GET","/displayasc.htm");
		session=request.getSession();
		session.setAttribute("VISITOR", visitorDao.searchUser(visitor.getUserName(), visitor.getPassword()));
		modelAndView=controller.showEventsAsc(request, response);
		assertEquals("/visitormain.jsp",modelAndView.getViewName());
	}

	/**
	 * Test case for show events in desc order
	 * @throws Exception 
	 */
	@Test
	public void testShowEventsDesc() throws Exception {
		/**
		 * @TODO: Create MockHttpServletRequest object 
		 * Set visitor object in VISITOR session by calling searchUser method from visitorDAO		
		 * Call showEventsDesc method and assert model view name 
		 */		
		request=new MockHttpServletRequest("GET","/displaydesc.htm");
		session=request.getSession();
		session.setAttribute("VISITOR", visitorDao.searchUser(visitor.getUserName(), visitor.getPassword()));
		modelAndView=controller.showEventsDesc(request, response);
		assertEquals("/visitormain.jsp",modelAndView.getViewName());
	}

	/**
	 * Test case for show events catalog asc order
	 * @throws Exception 
	 */
	@Test
	public void testShowEventsCatalogAsc() throws Exception {
		/**
		 * @TODO: Create MockHttpServletRequest object 
		 * Set visitor object in VISITOR session by calling searchUser method from visitorDAO		
		 * Call showEventsCatalogAsc method and assert model view name 
		 */		
		request=new MockHttpServletRequest("GET","/displaycatalogasc.htm");
		session=request.getSession();
		session.setAttribute("VISITOR", visitorDao.searchUser(visitor.getUserName(), visitor.getPassword()));
		modelAndView=controller.showEventsCatalogAsc(request, response);
		assertEquals("/eventCatalog.jsp",modelAndView.getViewName());
	}

	/**
	 * Test case for show events catalog desc
	 * @throws Exception 
	 */
	@Test
	public void testShowEventsCatalogDesc() throws Exception {
		/**
		 * @TODO: Create MockHttpServletRequest object 
		 * Set visitor object in VISITOR session by calling searchUser method from visitorDAO		
		 * Call showEventsCatalogDesc method and assert model view name 
		 */	
		request=new MockHttpServletRequest("GET","/displaycatalogdesc.htm");
		session=request.getSession();
		session.setAttribute("VISITOR", visitorDao.searchUser(visitor.getUserName(), visitor.getPassword()));
		modelAndView=controller.showEventsCatalogDesc(request, response);
		assertEquals("/eventCatalog.jsp",modelAndView.getViewName());
	}

	/**
	 * Negative test case for search events by name
	 * @throws Exception 
	 */
	@Test(expected=FERSGenericException.class)
	public void testSearchEventsByName_Negative() throws Exception {
		/**
		 * @TODO: Call searchEventsByName method by passing request object as null and 
		 * asserting the model view name
		 */		
		request=null;
		modelAndView=controller.searchEventsByName(request, response);
		
	}

	/**
	 * Negative test case for search events by name catalog
	 * @throws Exception 
	 */
	@Test(expected=FERSGenericException.class)
	public void testSearchEventsByNameCatalog_Negative() throws Exception {
		/**
		 * @TODO: Call searchEventsByNameCatalog method by passing request object as null and 
		 * asserting the model view name
		 */		
		request=null;
		modelAndView=controller.searchEventsByNameCatalog(request, response);
	}

	/**
	 * Negative test case for show events in asc order
	 * @throws Exception 
	 */
	@Test(expected=FERSGenericException.class)
	public void testShowEventsAsc_Negative() throws Exception {
		/**
		 * @TODO: Call showEventsAsc method by passing request object as null and 
		 * asserting the model view name
		 */		
		request=null;
		modelAndView=controller.showEventsAsc(request, response);
	}

	/**
	 * Negative test case for show events in desc order
	 * @throws Exception 
	 * 
	 */
	@Test(expected=FERSGenericException.class)
	public void testShowEventsDesc_Negative() throws Exception {
		/**
		 * @TODO: Call showEventsDesc method by passing request object as null and 
		 * asserting the model view name
		 */		
		request=null;
		modelAndView=controller.showEventsDesc(request, response);
	}

	/**
	 * Negative test case for show events catalog in asc order
	 * @throws Exception 
	 */
	@Test(expected=FERSGenericException.class)
	public void testShowEventsCatalogAsc_Negative() throws Exception {
		/**
		 * @TODO: Call showEventsCatalogAsc method by passing request object as null and 
		 * asserting the model view name
		 */		
		request=null;
		modelAndView=controller.showEventsCatalogAsc(request, response);
	}

	/**
	 * Negative test case for show events catalog in desc order
	 * @throws Exception 
	 */
	@Test(expected=FERSGenericException.class)
	public void testShowEventsCatalogDesc_Negative() throws Exception {
		/**
		 * @TODO: Call showEventsCatalogDesc method by passing request object as null and 
		 * asserting the model view name
		 */		

		request=null;
		modelAndView=controller.showEventsCatalogDesc(request, response);
	}
	
	
	/**
	 * Positive test case for change password
	 */
	/*@Test
	public void testChangePassword_Positive(){
		*//**
		 * @TODO: Create MockHttpServletRequest object 
		 * Set visitor object in VISITOR session by calling searchUser method from visitorDAO		 
		 * Set request parameters for password
		 * Call changePassword method and assert status as success
		 *//*		
	}
	
	*//**
	 * Negative test case for change password with password as null
	 *//*
	@Test
	public void testChangePassword_PasswordNull(){
		*//**
		 * @TODO: Create MockHttpServletRequest object 
		 * Set visitor object in VISITOR session by calling searchUser method from visitorDAO		 
		 * Do not set request parameters for password
		 * Call changePassword method and assert status as success
		 *//*	
	}
	
	*//**
	 * Negative test case for change password with visitor as null
	 *//*
	@Test
	public void testChangePassword_VisitorNull(){
		*//**
		 * @TODO: Create MockHttpServletRequest object 
		 * Do not set visitor object in VISITOR session by calling searchUser method from visitorDAO		 
		 * Set request parameters for password
		 * Call changePassword method and assert status as success
		 *//*		
	}*/
	
	/**
	 * Positive test case for change password
	 */
	@Test
	public void testChangePassword_Positive(){
		try{
			request = new MockHttpServletRequest("GET", "/changePWD.htm");
			Visitor visitor = visitorDao.searchUser("ylee", "password");	
			session.setAttribute("VISITOR", visitor);
			request.setSession(session);
			request.setParameter("password", "password3");
			modelAndView = controller.changePassword(request, response);		
		}catch(Exception exception){
			fail("Exception");
		}
		assertEquals("success", modelAndView.getModelMap().get("status"));
		request.setParameter("password", "password");
		modelAndView = controller.changePassword(request, response);
	}
	
	/**
	 * Negative test case for change password with password as null
	 */
	@Test
	public void testChangePassword_PasswordNull(){
		try{
			request = new MockHttpServletRequest("GET", "/changePWD.htm");
			Visitor visitor = visitorDao.searchUser("ylee", "password");			
			session.setAttribute("VISITOR", visitor);
			request.setSession(session);			
			modelAndView = controller.changePassword(request, response);		
		}catch(Exception exception){
			fail("Exception");
		}
		assertEquals("error", modelAndView.getModelMap().get("status"));
	}
	
	/**
	 * Negative test case for change password with visitor as null
	 */
	@Test
	public void testChangePassword_VisitorNull(){
		try{
			request = new MockHttpServletRequest("GET", "/changePWD.htm");
			Visitor visitor = new Visitor();			
			session.setAttribute("VISITOR", visitor);
			request.setSession(session);
			request.setParameter("password", "password");
			modelAndView = controller.changePassword(request, response);		
		}catch(Exception exception){
			fail("Exception");
		}
		assertEquals("error", modelAndView.getModelMap().get("status"));
	}
}
