//http://www.codejava.net/java-se/jdbc/connect-to-a-database-with-jdbc
//https://www.youtube.com/watch?v=e3gnhsGqNmI

package DatabaseHandler;
import java.sql.*;

import javax.swing.JOptionPane;

public class DatabaseHandler {

	private static DatabaseHandler handler = null;

	private static final String DB_URL = "jdbc:mysql://localhost:3306/";
	private static String user = "root";// Username of database
	private static String pass = "";// Password of database
	private static Connection conn = null;
	private static Statement stmt = null;

	private DatabaseHandler() {

		createConnection();
		setupDatabase();
		setupPersonTable();
		setupQuestionTable();
		setupJetTable();
		setupFlightTable();
		setupTicketTable();
		setupPersonFlightRelation();
		setupReferentialIntegrities();
	}

	

	public static DatabaseHandler getInstance() {
		if (handler == null) {
			handler = new DatabaseHandler();
		}
		return handler;
	}

	void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(DB_URL, user, pass);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void setupDatabase(){
		
		try{
			stmt = conn.createStatement();
			
			stmt.execute("CREATE DATABASE IF NOT EXISTS AirlineDB");
			stmt.execute("USE AirlineDB");
		}catch(SQLException e){
			System.out.println(e.getMessage());
		}
	}
	
	void setupPersonTable() {
		String TABLE_NAME = "PERSON";
		try {
			stmt = conn.createStatement();

			DatabaseMetaData dbm = conn.getMetaData();
			ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);

			if (tables.next()) {
				System.out.println("Table " + TABLE_NAME + "already exists. Ready for go!");
			} else {
				stmt.execute("CREATE TABLE " + TABLE_NAME + "(" + "	SSN varchar(200) PRIMARY KEY,\n"
						+ "	firstname varchar(200),\n" + "	lastname varchar(200),\n" + "	address varchar(100),\n"
						+ "	isAdmin boolean default false,\n"
						+ " zipcode varchar(100),\n"
						+ " state varchar(150),\n"
						+ " username varchar(200) UNIQUE,\n"
						+ " email varchar(200),\n"
						+ " password varchar(200),\n "
						+ "	questionID int(200),\n "
						+ " answer varchar(200)"
						+" )");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " --- setupDatabase");
		} finally {
		}
	}
	
	void setupPersonFlightRelation() {
		String TABLE_NAME = "PERSON_FLIGHT";
		try {
			stmt = conn.createStatement();

			DatabaseMetaData dbm = conn.getMetaData();
			ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);

			if (tables.next()) {
				System.out.println("Table " + TABLE_NAME + "already exists. Ready for go!");
			} else {
				stmt.execute("CREATE TABLE " + TABLE_NAME + "(" + "	SSN varchar(200),\n"
						+ "	flightID int(200),"
						+ " PRIMARY KEY (SSN , flightID)"
						+" )");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " --- setupDatabase");
		} finally {
		}
	}
	
	
	void setupQuestionTable() {
		String TABLE_NAME = "QUESTION";
		try {
			stmt = conn.createStatement();

			DatabaseMetaData dbm = conn.getMetaData();
			ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);

			if (tables.next()) {
				System.out.println("Table " + TABLE_NAME + "already exists. Ready for go!");
			} else {
				stmt.execute("CREATE TABLE " + TABLE_NAME + "(" + "	questionID int(200) primary key,\n"
						+ "	question varchar(200)"+" )");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " --- setupDatabase");
		} finally {
		}
	}
	
	
	
	
	void setupJetTable() {
		String TABLE_NAME = "JET";
		try {
			stmt = conn.createStatement();

			DatabaseMetaData dbm = conn.getMetaData();
			ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);

			if (tables.next()) {
				System.out.println("Table " + TABLE_NAME + "already exists. Ready for go!");
			} else {
				stmt.execute("CREATE TABLE " + TABLE_NAME + "(" + "	jetID int(200) primary key ,\n"
						+ "	jetName varchar(200),\n" 
						+ "	capacity varchar(200),\n"
						+ " isFree boolean default true"
						+ " )");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " --- setupDatabase");
		} finally {
		}
	}
	
	void setupFlightTable() {
		String TABLE_NAME = "FLIGHT";
		try {
			stmt = conn.createStatement();

			DatabaseMetaData dbm = conn.getMetaData();
			ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);

			if (tables.next()) {
				System.out.println("Table " + TABLE_NAME + "already exists. Ready for go!");
			} else {
				stmt.execute("CREATE TABLE " + TABLE_NAME + "(" + "	flightID int(200) primary key AUTO_INCREMENT,\n"
						+ "	jetID int(200),\n" 
						+ "	source varchar(200),\n"
						+ "	destination varchar(200),\n"
						+ "	flightTime varchar(200),\n"
						+ "	flightDate varchar(200)"
						+ " )");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " --- setupDatabase");
		} finally {
		}
	}
	
	
	void setupTicketTable() {
		String TABLE_NAME = "TICKET";
		try {
			stmt = conn.createStatement();

			DatabaseMetaData dbm = conn.getMetaData();
			ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);

			if (tables.next()) {
				System.out.println("Table " + TABLE_NAME + "already exists. Ready for go!");
			} else {
				stmt.execute("CREATE TABLE " + TABLE_NAME + "(" + "	ticketID int(200) primary key,\n"
						+ "	SSN varchar(200),\n" 
						+ "	reservationDate varchar(200),\n"
						+ "	reservationTime varchar(200),\n"
						+ "	flightID int(200)"
						+ " )");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " --- setupDatabase");
		} finally {
		}
	}
	
	
	void setupReferentialIntegrities(){
		try{
			stmt = conn.createStatement();
			
			stmt.execute("ALTER TABLE PERSON ADD FOREIGN KEY (questionID) REFERENCES QUESTION(questionID)");
			stmt.execute("INSERT INTO PERSON (SSN,username,password,isAdmin)"
					+ "	VALUES (000000,\"admin\",\"admin\" ,1)");
			stmt.execute("ALTER TABLE FLIGHT ADD FOREIGN KEY (jetID) REFERENCES JET(jetID)");
			stmt.execute("ALTER TABLE TICKET ADD FOREIGN KEY (SSN) REFERENCES PERSON(SSN)");
			stmt.execute("ALTER TABLE TICKET ADD FOREIGN KEY (flightID) REFERENCES PERSON(flightID)");
			

			
			
			
		}catch(SQLException e){
			System.out.println(e.getMessage());
		}
	}
	
	

	public static Connection getConn() {
		return conn;
	}



	public static void setConn(Connection conn) {
		DatabaseHandler.conn = conn;
	}



	public ResultSet execQuery(String query) {
		ResultSet result;
		try {
			stmt = conn.createStatement();
			result = stmt.executeQuery(query);
		} catch (SQLException ex) {
			System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
			return null;
		} finally {
		}
		return result;
	}

	public boolean execAction(String qu) {
		try {
			stmt = conn.createStatement();
			stmt.execute(qu);
			return true;
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
			System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
			return false;
		} finally {
		}
	}
}
