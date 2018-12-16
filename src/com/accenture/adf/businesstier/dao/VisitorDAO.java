package com.accenture.adf.businesstier.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.accenture.adf.businesstier.entity.Event;
import com.accenture.adf.businesstier.entity.Visitor;
import com.accenture.adf.exceptions.FERSGenericException;
import com.accenture.adf.helper.FERSDataConnection;
import com.accenture.adf.helper.FERSDbQuery;

/**
 *  <br/>
 *  CLASS DESCRIPTION: <br/>
 *  A Data Access Object (DAO) class for handling and managing visitor related 
 *  data requested, used, and processed in the application and maintained in 
 *  the database.  The interface between the application and visitor data 
 *  persisting in the database. <br/>
 *  
 *  @author krishna.kishore
 *  
 */
public class VisitorDAO {

	// LOGGER for handling all transaction messages in VISITORDAO
	private static Logger log = Logger.getLogger(VisitorDAO.class);

	//JDBC API classes for data persistence
	private Connection connection = null;
	private PreparedStatement statement = null;
	private ResultSet resultSet = null;
	private FERSDbQuery query;

	//Default constructor for injecting Spring dependencies for SQL queries
	public VisitorDAO() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		query = (FERSDbQuery) context.getBean("SqlBean");
	}

	/**
	 *  <br/>
	 *  METHOD DESCRIPTION: <br/>
	 *  DAO method to insert visitor details into the VISITOR table 
	 *  after validating that the desired username of the visitor 
	 *  does not currently exist. <br/>
	 *  
	 *  @return boolean
	 *  
	 *  @param visitor
	 *  
	 *  @throws ClassNotFoundException
	 *  @throws SQLException
	 *  @throws Exception
	 *  
	 */
	public boolean insertData(Visitor visitor) throws ClassNotFoundException,
			SQLException, Exception {
		connection = FERSDataConnection.createConnection();
		Statement selStatement = connection.createStatement();
		statement = connection.prepareStatement(query.getInsertQuery());
		resultSet = selStatement.executeQuery(query.getValidateVisitor());
		boolean userFound = false;
		while (resultSet.next()) {
			if (resultSet.getString("username").equals(visitor.getUserName())) {
				userFound = true;
				log.info("Vistor with USERNAME already exists in Database");
				break;
			}
		}
		if (userFound == false) {
			statement.setString(1, visitor.getUserName());
			statement.setString(2, visitor.getPassword());
			statement.setString(3, visitor.getFirstName());
			statement.setString(4, visitor.getLastName());
			statement.setString(5, visitor.getEmail());
			statement.setString(6, visitor.getPhoneNumber());
			statement.setString(7, visitor.getAddress());
			int status = statement.executeUpdate();
			if (status <= 0)
				throw new FERSGenericException("Records not updated properly",
						new Exception());
			log.info("Visitor details inserted into Database");
			FERSDataConnection.closeConnection();
			return true;
		}
		resultSet.close();
		FERSDataConnection.closeConnection();
		return false;
	}

	/**
	 *  <br/>
	 *  METHOD DESCRIPTION: <br/>
	 *  DAO method for searching for a existing visitor account 
	 *  using the entered username and password. <br/>
	 *  
	 *  @return Visitor
	 *  
	 *  @param  username
	 *  @param  password
	 *  
	 *  @throws ClassNotFoundException
	 *  @throws SQLException
	 *  
	 */
	public Visitor searchUser(String username, String password)
			throws ClassNotFoundException, SQLException {
		connection = FERSDataConnection.createConnection();
		Visitor visitor = new Visitor();
		statement = connection.prepareStatement(query.getSearchQuery());
		statement.setString(1, username);
		statement.setString(2, password);
		resultSet = statement.executeQuery();
		log.info("Retreived Visitor details from DATABASE for username :"
				+ username);
		
		while (resultSet.next()) {
			visitor.setUserName(resultSet.getString("username"));
			//visitor.setPassword(resultSet.getString("password"));
			visitor.setVisitorId(resultSet.getInt("visitorid"));
			visitor.setFirstName(resultSet.getString("firstname"));
			visitor.setLastName(resultSet.getString("lastname"));
			visitor.setEmail(resultSet.getString("email"));
			visitor.setPhoneNumber(resultSet.getString("phonenumber"));
			visitor.setAddress(resultSet.getString("address"));
			visitor.setAdmin(resultSet.getBoolean("isadmin"));
		}
		resultSet.close();
		FERSDataConnection.closeConnection();
		return visitor;
	}

	/**
	 *  <br/>
	 *  METHOD DESCRIPTION: <br/>
	 *  DAO method to register visitor to specific event and checking about status
	 *  of visitor to particular event. <br/>
	 *  
	 *  PSEUDO-CODE: <br/>
	 *     Create a connection to the database <br/>
	 *     Prepare a statement object using the connection: that inserts the   
	 *       visitor, event, and session IDs into the EVENTSESSIONSIGNUP table <br/>
	 *     Execute the query to perform the update <br/>
	 *  
	 *  @return void
	 *  
	 *  @param visitor
	 *  @param eventid
	 *  @param sessionid
	 *  
	 *  @throws ClassNotFoundException
	 *  @throws SQLException
	 *  @throws Exception
	 *  
	 */
	public void registerVisitorToEvent(Visitor visitor, int eventid, int sessionid)
			throws ClassNotFoundException, SQLException, Exception {

		log.info("register visitor to event:entry");
		try
		{
			connection=FERSDataConnection.createConnection();
			statement=connection.prepareStatement(query.getRegisterQuery());
			statement.setInt(1,visitor.getVisitorId());
			statement.setInt(2, sessionid);
			statement.setInt(3,eventid);
			statement.executeUpdate();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(statement!=null)
			{
				statement.close();
			}
			if(connection!=null)
			{
				connection.close();
			}
		}
		log.info("register visitor to event:exit");

	}

	/**
	 *  <br/>
	 *  METHOD DESCRIPTION:<br/>
	 *  DAO method to display all the events registered by particular visitor<br/>
	 *  
	 *  PSEUDO-CODE: <br/>
	 *     Create a connection to the database <br/>
	 *     Prepare a statement object using the connection: that returns the event   
	 *       information for all the events that are registered to a visitor<br/>
	 *     Execute the query to retrieve the results into a result set<br/>
	 *     Using a WHILE LOOP, place each event record‘s information 
	 *       in an event list. <br/>
	 *  
	 *  @return Collection of Object Arrays
	 *  
	 *  @param  visitor
	 *  
	 *  @throws ClassNotFoundException
	 *  @throws SQLException
	 *  
	 */
	public ArrayList<Object[]> registeredEvents(Visitor visitor)throws ClassNotFoundException, SQLException {
		int a=0;
		
		ArrayList<Object[]> eventList = new ArrayList<Object[]>();
		connection=FERSDataConnection.createConnection();
		statement=connection.prepareStatement(query.getStatusQuery());
		statement.setInt(1,visitor.getVisitorId());
		resultSet=statement.executeQuery();
		
		Object[] object;
	
		while(resultSet.next())
		{
			a=0;
			object=new Object[8];
			int coordinatorId=0;
			//E1.EVENTID, E1.NAME, E1.DESCRIPTION, E1.PLACES, E1.DURATION, E1.EVENTTYPE, E4.FIRSTNAME,E4.LASTNAME, E3.EVENTSESSIONID, E2.SIGNUPID FROM EVENT E1, EVENTSESSIONSIGNUP E2, EVENTSESSION E3, EVENTCOORDINATOR E4
			object[a++]=resultSet.getInt(1);
			object[a++]=resultSet.getString(2);
			object[a++]=resultSet.getString(3);
			object[a++]=resultSet.getString(4);
			object[a++]=resultSet.getString(5);
			object[a++]=resultSet.getString(6);
			statement=connection.prepareStatement("SELECT EVENTCOORDINATORID FROM EVENTCOORDINATOR WHERE FIRSTNAME=? AND LASTNAME=?;");
			statement.setString(1, resultSet.getString(7));
			statement.setString(2, resultSet.getString(8));
			ResultSet resultSet1=statement.executeQuery();
			while(resultSet1.next())
			{
				coordinatorId=resultSet1.getInt(1);
			}
			object[a++]=coordinatorId;
			object[a++]=resultSet.getInt(9);
			eventList.add(object);
		}
		return eventList;
	}

	/**
	 *  <br/>
	 *  METHOD DESCRIPTION: <br/>
	 *  DAO method to update visitor with additional information <br/>
	 * 
	 *  @return int
	 * 
	 *  @param visitor
	 * 
	 *  @throws ClassNotFoundException
	 *  @throws SQLException
	 *  
	 */
	public int updateVisitor(Visitor visitor) throws ClassNotFoundException,
			SQLException {
		connection = FERSDataConnection.createConnection();
		statement = connection.prepareStatement(query.getUpdateQuery());
		statement.setString(1, visitor.getFirstName());
		statement.setString(2, visitor.getLastName());
		statement.setString(3, visitor.getUserName());
		//statement.setString(4, visitor.getPassword());
		statement.setString(4, visitor.getEmail());
		statement.setString(5, visitor.getPhoneNumber());
		statement.setString(6, visitor.getAddress());
		statement.setInt(7, visitor.getVisitorId());

		int status = statement.executeUpdate();
		log.info("Updating visitor details in Database for Visitor ID :"
				+ visitor.getVisitorId());
		FERSDataConnection.closeConnection();
		return status;
	}
	
	/**
	 * 
	 * @param visitor
	 * @return
	 * @throws FERSGenericException 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public int changePassword(Visitor visitor) throws ClassNotFoundException, SQLException {
		int status = -1;
		
		try{
			connection = FERSDataConnection.createConnection();
			
		 if(connection!=null){			
			if(visitor!=null){
				
				if(matchWithOldPwd(visitor, connection)){
					status = -10;
				}else{
					statement = connection.prepareStatement(query.getChangePWDQuery());
					statement.setString(1, visitor.getPassword());
					statement.setInt(2, visitor.getVisitorId());
			
					status = statement.executeUpdate();
					
					log.info("Updating visitor details in Database for Visitor ID :" + visitor.getVisitorId());
				}	
			}else{
				log.error("Visitor details are invalid");
			}
		 }else{
			 throw new SQLException("Connection Error, could not establish connection with database");
		 }
		}finally{
			if(statement!=null)
				statement.close();
			if(connection!=null)
				FERSDataConnection.closeConnection();
		}			
		return status;
	}

	private boolean matchWithOldPwd(Visitor visitor, Connection connection2) throws SQLException{
		String currentPWD = "";				
			
		try{
			statement = connection.prepareStatement(query.getVerifyPWDQuery());								
			statement.setInt(1, visitor.getVisitorId());
	
			resultSet = statement.executeQuery();
			if(resultSet.next())
				currentPWD = resultSet.getString("password");
			
			if(currentPWD.equalsIgnoreCase(visitor.getPassword())){
				log.info("New password must be different from previous password, please choose a different password");
				return true;
			}
			
		}finally{
			if(statement!=null)
				statement.close();			
		}			
		return false;	
	}

	/**
	 *  <br/>
	 *  METHOD DESCRIPTION: <br/>
	 *  DAO method to unregister a visitor from an event that the
	 *  visitor registered for previously <br/>
	 *  
	 *  PSEUDO-CODE: <br/>
	 *     Create a connection to the database <br/>
	 *     Prepare a statement object using the connection: that deletes the   
	 *       visitor, event, and session IDs into the EVENTSESSIONSIGNUP table <br/>
	 *     Execute the query to perform the update <br/>
     *
	 *  @return void
	 *  
	 *  @param  visitor
	 *  @param  eventid
	 *  @param  eventsessionid
	 *  
	 *  @throws ClassNotFoundException
	 *  @throws SQLException
	 *  @throws Exception
	 *  
	 */
	public void unregisterEvent(Visitor visitor, int eventid, int eventsessionid)
			throws ClassNotFoundException, SQLException, Exception {
		log.info("unregister event:entry");
		
		connection = FERSDataConnection.createConnection();
		statement = connection.prepareStatement(query.getDeleteEventQuery());
		statement.setInt(1, eventsessionid);
		statement.setInt(2, visitor.getVisitorId());
		statement.setInt(3, eventid);
		
		if (statement.executeUpdate() <= 0)
			throw new FERSGenericException("Records not updated properly",
					new Exception());
		log.info("unregistering event in Database for the visitor :"
				+ visitor.getFirstName());
		log.info("unregister event:exit");
		FERSDataConnection.closeConnection();

	}

}