package model;

public abstract class Person {

	
	private String ssn;
	private String firstname;
	private String lastname;
	private String zipcode;
	private String state;
	private String address;
	
	
	public Person(){
	}


	public Person(String ssn, String firstname, String lastname,String address, String zipcode, String state) {
		this.ssn = ssn;
		this.firstname = firstname;
		this.lastname = lastname;
		this.zipcode = zipcode;
		this.state = state;
		this.address = address;
	}
	


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getSsn() {
		return ssn;
	}


	public void setSsn(String ssn) {
		this.ssn = ssn;
	}


	public String getFirstname() {
		return firstname;
	}


	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}


	public String getLastname() {
		return lastname;
	}


	public void setLastname(String lastname) {
		this.lastname = lastname;
	}


	public String getZipcode() {
		return zipcode;
	}


	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}
	
	
}
