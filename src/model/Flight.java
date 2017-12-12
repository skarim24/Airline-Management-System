package model;

import javafx.beans.property.SimpleStringProperty;

public class Flight {
	
	public static final String TABLE = "flight";
	public static final String COL_FLIGHTID = "flightID";
	public static final String COL_JETID = "jetID";
	public static final String COL_SOURCE = "source";
	public static final String COL_DESTINATION = "destination";
	public static final String COL_TIME = "flightTime";
	public static final String COL_DATE = "flightDate";
	
	public static final String TABLE_JET = "jet";
	public static final String COL_JETNAME = "jetName";
	public static final String COL_CAPACITY = "capacity";

	
	
	private int flightId;
	private int jetId;
	private SimpleStringProperty jetName;
	private SimpleStringProperty jetCapacity;
	private SimpleStringProperty source;
	private SimpleStringProperty destination;
	private SimpleStringProperty flightTime;
	private SimpleStringProperty flightDate;
	public Flight(int flightId, int jetId, String jetName, String jetCapacity,
			String source, String destination, String flightTime,
			String flightDate) {
		this.flightId = flightId;
		this.jetId = jetId;
		this.jetName = new SimpleStringProperty(jetName);
		this.jetCapacity = new SimpleStringProperty(jetCapacity);;
		this.source = new SimpleStringProperty(source);;
		this.destination = new SimpleStringProperty(destination);;
		this.flightTime = new SimpleStringProperty(flightTime);;
		this.flightDate = new SimpleStringProperty(flightDate);;
	}
	public int getFlightId() {
		return flightId;
	}
	public int getJetId() {
		return jetId;
	}
	public String getJetName() {
		return jetName.get();
	}
	public String getJetCapacity() {
		return jetCapacity.get();
	}
	public String getSource() {
		return source.get();
	}
	public String getDestination() {
		return destination.get();
	}
	public String getFlightTime() {
		return flightTime.get();
	}
	public String getFlightDate() {
		return flightDate.get();
	}
	
	
	
	
	
	
	
	
}
