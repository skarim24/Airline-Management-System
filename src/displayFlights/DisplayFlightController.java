package displayFlights;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import DatabaseHandler.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import model.Flight;
import model.Util;

public class DisplayFlightController implements Initializable {
	ToggleGroup radioBtnGroup;
	ObservableList<Flight> flightList = FXCollections.observableArrayList();

	@FXML
	private AnchorPane rootPane;

	@FXML
	private JFXRadioButton byCityBtn;

	@FXML
	private JFXRadioButton byDateBtn;

	@FXML
	private JFXRadioButton byTimeBtn;

	@FXML
	private JFXTextField searchField;

	@FXML
	private JFXButton cSearchBtn;

	@FXML
	private JFXButton cAddBtn;

	@FXML
	private JFXButton cDeleteBtn;

	@FXML
	private BorderPane borderpane;

	@FXML
	private Label seatsLbl;

	@FXML
	private TableView<Flight> tableview;

	@FXML
	private TableColumn<Flight, String> fidCol;

	@FXML
	private TableColumn<Flight, String> jidCol;

	@FXML
	private TableColumn<Flight, String> jNameCol;

	@FXML
	private TableColumn<Flight, String> jCapacityCol;

	@FXML
	private TableColumn<Flight, String> sourceCol;

	@FXML
	private TableColumn<Flight, String> destCol;

	@FXML
	private TableColumn<Flight, String> dateCol;

	@FXML
	private TableColumn<Flight, String> timeCol;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Checking who is currently login and then tweaking GUI accordingly.
		int isAdmin = Util.getLoginPerson().getIsAdmin();
		if (isAdmin == 1) {

			borderpane.setTop(null);

		}

		radioBtnGroup = new ToggleGroup();
		byDateBtn.setToggleGroup(radioBtnGroup);
		byDateBtn.setSelected(true);
		byCityBtn.setToggleGroup(radioBtnGroup);
		byTimeBtn.setToggleGroup(radioBtnGroup);

		initCol();
		loadData();
	}

	private void loadData() {
		DatabaseHandler handler = DatabaseHandler.getInstance();
		// Query to get the JOIN of Flight table and Jet Table and get all the
		// result;
		String qu = "SELECT * FROM flight NATURAL JOIN jet";

		ResultSet rs = handler.execQuery(qu);

		try {

			while (rs.next()) {
				int flightID = rs.getInt(Flight.COL_FLIGHTID);
				int jetID = rs.getInt(Flight.COL_JETID);
				String jetName = rs.getString(Flight.COL_JETNAME);
				String jetCapacity = rs.getString(Flight.COL_CAPACITY);
				String source = rs.getString(Flight.COL_SOURCE);
				String destination = rs.getString(Flight.COL_DESTINATION);
				String date = rs.getString(Flight.COL_DATE);
				String time = rs.getString(Flight.COL_TIME);

				System.out.println(flightID + " " + jetID);

				Flight newFlight = new Flight(flightID, jetID, jetName, jetCapacity, source, destination, time, date);

				flightList.add(newFlight);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tableview.getItems().setAll(flightList);

	}

	@FXML
	void addFlightCustomer(ActionEvent event) {
		Flight selectedFlight = tableview.getSelectionModel().getSelectedItem();
		int remainingSeats = Integer.parseInt(seatsLbl.getText().toString());
		if (selectedFlight != null && remainingSeats != 0) {
			// Adding selected flight to the customer account.
			try {

				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Dialog");
				alert.setHeaderText("Flight Reservation");
				alert.setContentText("Are you sure you want to add this flight to your account?");

				Optional<ButtonType> check = alert.showAndWait();
				if (check.get() == ButtonType.OK) {
					// ... user chose OK
					String loginPersonSSN = Util.getLoginPerson().getSsn();
					int selectedFlightID = selectedFlight.getFlightId();
					String query = "INSERT INTO PERSON_FLIGHT (SSN , flightID) VALUES (?,?)";
					PreparedStatement p1 = DatabaseHandler.getConn().prepareStatement(query);
					p1.setString(1, loginPersonSSN);
					p1.setInt(2, selectedFlightID);

					p1.execute();
					// HERE FLIGHT has been Added to the account.
					Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
					alert1.setTitle("Success");
					alert1.setHeaderText(null);
					alert1.setContentText("Flight Added Successfully.");
					alert1.showAndWait();

					// Calculating the No of Seats.

				} else {
					// ... user chose CANCEL or closed the dialog
				}

			} catch (SQLException e) {
				Alert alert1 = new Alert(Alert.AlertType.ERROR);
				alert1.setTitle("Failed");
				alert1.setHeaderText(null);
				alert1.setContentText("Database Error: " + e.getMessage());
				alert1.showAndWait();
			}
		} else {
			Alert alert1 = new Alert(Alert.AlertType.ERROR);
			alert1.setTitle("Failed");
			alert1.setHeaderText(null);
			alert1.setContentText(
					"1.No Flight Selected. Select a flight to add it to your account\n2.Sorry, all seats are reserved");
			alert1.showAndWait();
		}

	}

	@FXML
	void searchFlightCustomer(ActionEvent event) {
		/*
		 * Searching for a particular result. Based on the choice of radioBtn.
		 */
		String input = searchField.getText().toLowerCase().toString();
		System.out.println(input);
		if (byCityBtn.isSelected()) {
			loadData(input, "city");
		} else if (byDateBtn.isSelected()) {
			loadData(input, "date");
		} else if (byTimeBtn.isSelected()) {
			loadData(input, "time");
		}
	}

	private void loadData(String inputText, String choice) {
		DatabaseHandler handler = DatabaseHandler.getInstance();
		// Query to get the JOIN of Flight table and Jet Table and get all the
		// result;
		String qu;
		ResultSet res = null;
		try {
			if (choice.equals("city")) {

				qu = "SELECT * FROM flight NATURAL JOIN jet WHERE flight.source like(\"%" + inputText
						+ "%\") OR flight.destination like(\"%" + inputText + "%\")";
				// qu = "SELECT * FROM flight NATURAL JOIN jet ";

				res = handler.execQuery(qu);

			} else if (choice.equals("date")) {
				qu = "SELECT * FROM flight NATURAL JOIN jet WHERE flight.flightDate like(\"%" + inputText + "%\")";
				res = handler.execQuery(qu);
			} else if (choice.equals("time")) {
				qu = "SELECT * FROM flight NATURAL JOIN jet WHERE flight.flightTime like(\"%" + inputText + "%\")";
				res = handler.execQuery(qu);
			}

			while (res.next()) {
				System.out.println("WhileLoop In");
				int flightID = res.getInt(Flight.COL_FLIGHTID);
				int jetID = res.getInt(Flight.COL_JETID);
				String jetName = res.getString(Flight.COL_JETNAME);
				String jetCapacity = res.getString(Flight.COL_CAPACITY);
				String source = res.getString(Flight.COL_SOURCE);
				String destination = res.getString(Flight.COL_DESTINATION);
				String date = res.getString(Flight.COL_DATE);
				String time = res.getString(Flight.COL_TIME);

				Flight newFlight = new Flight(flightID, jetID, jetName, jetCapacity, source, destination, time, date);

				flightList.add(newFlight);
			}

			if (flightList.isEmpty()) {
				Alert alert1 = new Alert(Alert.AlertType.ERROR);
				alert1.setTitle("Failed");
				alert1.setHeaderText(null);
				alert1.setContentText("Record Not Found in the Database ");
				alert1.showAndWait();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tableview.getItems().setAll(flightList);
		res = null;
		flightList.clear();
	}

	private void initCol() {
		fidCol.setCellValueFactory(new PropertyValueFactory<>("flightId"));
		jidCol.setCellValueFactory(new PropertyValueFactory<>("jetId"));
		jNameCol.setCellValueFactory(new PropertyValueFactory<>("jetName"));
		jCapacityCol.setCellValueFactory(new PropertyValueFactory<>("jetCapacity"));
		sourceCol.setCellValueFactory(new PropertyValueFactory<>("Source"));
		destCol.setCellValueFactory(new PropertyValueFactory<>("Destination"));
		timeCol.setCellValueFactory(new PropertyValueFactory<>("flightTime"));
		dateCol.setCellValueFactory(new PropertyValueFactory<>("flightDate"));
	}

	@FXML
	void setAvailableSeatsLabel(MouseEvent event) {
		int totalSeats = 0;
		int reservedSeats = 0;
		DatabaseHandler handler = DatabaseHandler.getInstance();
		// Getting the Total Capacity of the Selected Flight Jet
		Flight selectedFlight = tableview.getSelectionModel().getSelectedItem();

		if (selectedFlight != null) {

			int selectedJetID = selectedFlight.getJetId();
			String query = "SELECT * FROM JET WHERE jetID = " + selectedJetID;
			/*
			 * SELECT COUNT(flightID) FROM person_flight WHERE flightID = 2
			 * GROUP BY flightID
			 */
			String query1 = "SELECT COUNT(flightID) FROM PERSON_FLIGHT WHERE flightID = " + selectedFlight.getFlightId()
					+ " GROUP BY flightID";

			ResultSet rs1 = handler.execQuery(query1);
			ResultSet rs = handler.execQuery(query);
			try {
				// Analyzing Result of Query..
				if (rs.next()) {
					totalSeats = Integer.parseInt(rs.getString(Flight.COL_CAPACITY));
				} else {
					Alert alert1 = new Alert(Alert.AlertType.ERROR);
					alert1.setTitle("Failed");
					alert1.setHeaderText(null);
					alert1.setContentText("No Jet in the database having a flight you selected.");
					alert1.showAndWait();
				}

				// Analyzing Result of Query1
				if (rs1.next()) {
					reservedSeats = Integer.parseInt(rs1.getString(1));
					seatsLbl.setText(String.valueOf(totalSeats - reservedSeats));
				} else {
					Alert alert1 = new Alert(Alert.AlertType.ERROR);
					alert1.setTitle("Failed");
					alert1.setHeaderText(null);
					alert1.setContentText("No Flights reserved yet");
					alert1.showAndWait();
					seatsLbl.setText(String.valueOf(totalSeats));

				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else{
			Alert alert1 = new Alert(Alert.AlertType.ERROR);
			alert1.setTitle("Failed");
			alert1.setHeaderText(null);
			alert1.setContentText("No Flights Selected");
			alert1.showAndWait();
		}

	}
}
