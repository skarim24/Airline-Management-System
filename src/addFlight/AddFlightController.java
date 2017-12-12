package addFlight;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import DatabaseHandler.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Flight;

public class AddFlightController implements Initializable {

	// ArrayList to store the result of the query , by creating objects and
	// storing them in the list
	ObservableList<String> jetIdList = FXCollections.observableArrayList();
	// Variable to store the date of flight to be stored
	LocalDate FLIGHT_DATE;
	// Variable to store the time of flight to be stored
	LocalTime FLIGHT_TIME;

	@FXML
	private VBox rootPane;

	@FXML
	private Pane flightPane;

	@FXML
	private JFXTextField jetIdField;

	@FXML
	private JFXComboBox<String> fJetIdCombo;

	@FXML
	private JFXTextField fSourceField;

	@FXML
	private JFXTextField fDestinationField;

	@FXML
	private JFXDatePicker fTimePicker;

	@FXML
	private JFXDatePicker fDatePicker;

	@FXML
	private JFXButton fDoneBtn;

	@FXML
	private Pane jetPane;

	@FXML
	private JFXComboBox<?> jetCombo;

	@FXML
	private JFXTextField jetNameField;

	@FXML
	private JFXTextField jetCapacityField;

	@FXML
	private JFXButton jetDoneBtn;

	@FXML
	void addFlightToDatabase(ActionEvent event) {

		// Checking if the input fields are empty
		if (checkFlightValidation()) {
			Alert alert1 = new Alert(Alert.AlertType.ERROR);
			alert1.setTitle("Failed");
			alert1.setHeaderText(null);
			alert1.setContentText("Input Fields are empty.");
			alert1.showAndWait();
			return;
		}

		/*
		 * Adding Flight to the Database. First Create an insert query then
		 * execute it using prepared Statement. If there is any excetpion it
		 * will be handled and alert dialog box will be shown.
		 */
		String selectedJetId = fJetIdCombo.getSelectionModel().getSelectedItem().toString();
		
		FLIGHT_DATE = fDatePicker.getValue();
                FLIGHT_TIME =fTimePicker.getTime();
		String query = "INSERT INTO FLIGHT (jetID,source,destination,flightTime,flightDate) VALUES (?,?,?,?,?)";
		String query1 = "UPDATE JET Set isFree = 0 WHERE jetID = ? ";
		try {

			PreparedStatement p1 = DatabaseHandler.getConn().prepareStatement(query);
			p1.setString(1, selectedJetId);
			p1.setString(2, fSourceField.getText().toLowerCase().toString());
			p1.setString(3, fDestinationField.getText().toLowerCase().toString());
			p1.setString(4, FLIGHT_TIME.toString());
			p1.setString(5, FLIGHT_DATE.toString());

			p1.executeUpdate();

			// Updating the isFree Column in Jet Table because Jet is reserved
			// for certain Flight.
			PreparedStatement p2 = DatabaseHandler.getConn().prepareStatement(query1);
			p2.setString(1, selectedJetId);

			p2.executeUpdate();

			Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
			alert1.setTitle("Success");
			alert1.setHeaderText(null);
			alert1.setContentText(
					"Flight Added Successfully.\nJet with ID : " + selectedJetId + " is reserved for the flight.");
			alert1.showAndWait();

			// To Avoid Errors regarding redundancy
			// We have to click on add flight again to get back.
			rootPane.setVisible(false);

		} catch (SQLException e) {
			Alert alert1 = new Alert(Alert.AlertType.ERROR);
			alert1.setTitle("Failed");
			alert1.setHeaderText(null);
			alert1.setContentText("Database Error! Try to Input again\n\n Details: " + e.getMessage());
			alert1.showAndWait();
		}

	}

	private void populateComboBox() {
		/*
		 * This Method will add the ID's of the jets that are free to the combo
		 * box. So the admin can use it while adding flight to the database.
		 */
		DatabaseHandler handler = DatabaseHandler.getInstance();
		String query = "SELECT * FROM jet WHERE isFree = 1";

		ResultSet rs = handler.execQuery(query);

		try {
			while (rs.next()) {
				jetIdList.add(String.valueOf(rs.getInt(Flight.COL_JETID)));
			}
		} catch (SQLException e) {
			Alert alert1 = new Alert(Alert.AlertType.ERROR);
			alert1.setTitle("Failed");
			alert1.setHeaderText(null);
			alert1.setContentText("Database Error! Try to Input again\n\n Details: " + e.getMessage());
			alert1.showAndWait();
		}

		fJetIdCombo.getItems().setAll(jetIdList);

	}

	@FXML
	void addJetToDatabase(ActionEvent event) {

		// Checking if the input fields are empty
		if (checkJetValidation()) {
			Alert alert1 = new Alert(Alert.AlertType.ERROR);
			alert1.setTitle("Failed");
			alert1.setHeaderText(null);
			alert1.setContentText("Input Fields are empty.");
			alert1.showAndWait();
			return;
		}

		/*
		 * Inserting Jet into Database. First Create a PreparedStatement Query
		 * (INSERT query) then execute it using prepared statement object If
		 * there is any exception then alert dialog box will be shown
		 */
		String query = "INSERT INTO " + Flight.TABLE_JET + " ( jetID , jetName , capacity ) VALUES (? ,?,?) ";

		try {
			PreparedStatement p1 = DatabaseHandler.getConn().prepareStatement(query);
			p1.setInt(1, Integer.parseInt(jetIdField.getText().toString()));
			p1.setString(2, jetNameField.getText().toLowerCase().toString());
			p1.setString(3, jetCapacityField.getText().toLowerCase().toString());

			p1.executeUpdate();

			Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
			alert1.setTitle("Success");
			alert1.setHeaderText(null);
			alert1.setContentText("Jet Successfully added to the database!");
			alert1.showAndWait();

			// To Avoid Errors regarding redundancy
			// We have to click on add flight again to get back.
			rootPane.setVisible(false);

		} catch (SQLException e) {
			Alert alert1 = new Alert(Alert.AlertType.ERROR);
			alert1.setTitle("Failed");
			alert1.setHeaderText(null);
			alert1.setContentText("Database Error!\n Possibel Error: Key Voilation has occurred");
			alert1.showAndWait();

		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		populateComboBox();
		fDatePicker.setOnAction(event -> {
			FLIGHT_DATE = fDatePicker.getValue();
		});

		
	}

	private boolean checkJetValidation() {

		return jetNameField.getText().toString().equals("") || jetCapacityField.getText().toString().equals("")
				|| jetIdField.getText().toString().equals("");
	}

	private boolean checkFlightValidation() {

		return fSourceField.getText().toString().equals("") || fDestinationField.getText().toString().equals("")
				|| fTimePicker.getPromptText().toString().equals("")
				|| fDatePicker.getPromptText().toString().equals("");
	}
}
