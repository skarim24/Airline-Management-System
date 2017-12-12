package myflight;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import DatabaseHandler.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import model.Flight;
import model.Util;

public class MyFlightController implements Initializable {

	ObservableList<Flight> flightList = FXCollections.observableArrayList();

	@FXML
	private AnchorPane rootPane;

	@FXML
	private BorderPane borderpane;

	@FXML
	private JFXButton deleteBtn;

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
		initCol();
		loadData();
	}

	private void loadData() {
		DatabaseHandler handler = DatabaseHandler.getInstance();
		// Query to get the JOIN of Flight table and Jet Table and get all the
		// result;
		String loginPersonSSN = Util.getLoginPerson().getSsn();
		String qu = "SELECT * FROM flight NATURAL JOIN person_flight NATURAL JOIN jet WHERE SSN = " + loginPersonSSN;

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
	void deleteCustomerFlight(ActionEvent event) {
		DatabaseHandler handler = DatabaseHandler.getInstance();
		Flight selectedFlight = tableview.getSelectionModel().getSelectedItem();
		String loginPersonSSN = Util.getLoginPerson().getSsn();
		int selectedFlightID = selectedFlight.getFlightId();

		if (selectedFlight != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Dialog");
			alert.setHeaderText("Deleting Flight");
			alert.setContentText("Are you sure you want to delete the selected flight?");

			Optional<ButtonType> check = alert.showAndWait();
			if (check.get() == ButtonType.OK) {
				// ... user chose OK
				String query = "DELETE FROM person_flight WHERE SSN = " + loginPersonSSN + " AND flightID = "
						+ selectedFlightID;

				if (handler.execAction(query)) {
					Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
					alert1.setTitle("Success");
					alert1.setHeaderText(null);
					alert1.setContentText("Flight Deleted Successfully.");
					alert1.showAndWait();
				} else {
					Alert alert1 = new Alert(Alert.AlertType.ERROR);
					alert1.setTitle("Failed");
					alert1.setHeaderText(null);
					alert1.setContentText("There is an error deleting the flight. Try after a while.");
					alert1.showAndWait();
				}

				// Refreshing the TableView
				// Removing the selected flight
				for (int i = 0; i < flightList.size(); i++) {
					if (flightList.get(i).getFlightId() == selectedFlightID) {
						flightList.remove(i);
					}
				}

				// setting the table view after deleting the flight.
				tableview.getItems().setAll(flightList);

			} else {
				// ... user chose CANCEL or closed the dialog
			}
		} else {
			Alert alert1 = new Alert(Alert.AlertType.ERROR);
			alert1.setTitle("Failed");
			alert1.setHeaderText(null);
			alert1.setContentText("No Flights Selected. Select a flight to delete.");
			alert1.showAndWait();
		}

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

}
