package model;

public class User extends Person {
	
	public static final String TABLE = "person";
	public static final String COL_SSN = "SSN";
	public static final String COL_FIRSTNAME = "firstName";
	public static final String COL_LASTNAME = "lastName";
	public static final String COL_EMAIL = "email";
	public static final String COL_USERNAME = "username";
	public static final String COL_PASSWORD = "password";
	public static final String COL_ADDRESS = "address";
	public static final String COL_STATE = "state";
	public static final String COL_ZIPCODE = "zipcode";
	public static final String COL_ISADMIN = "isAdmin";
	public static final String COL_QUESTIONID = "questionID";
	public static final String COL_ANSWER = "answer";
	
	
	
	
	private String username;
	private String password;
	private String email;
	private int isAdmin ;
	
	public User(){}
	
	


	public User(String ssn, String firstName, String lastName, String address, String zipcode, String state,
			String username, String password, String email , int isAdmin) {
		super(ssn,firstName,lastName,address,zipcode,state);
		this.username = username;
		this.password = password;
		this.email = email;
		this.isAdmin = isAdmin;
	}




	public int getIsAdmin() {
		return isAdmin;
	}




	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}






	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	};
	
	
	
}
