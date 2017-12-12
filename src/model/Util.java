package model;

public class Util {

	private static User loginPerson;

	public static User getLoginPerson() {
		return loginPerson;
	}

	public static void setLoginPerson(User loginPerson) {
		Util.loginPerson = loginPerson;
	}
	
	
	
}
