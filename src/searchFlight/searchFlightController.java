package searchFlight;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.Flight;

public class searchFlightController implements Initializable {

	ToggleGroup radioBtnGroup;
	ObservableList<Flight> flightList = FXCollections.observableArrayList();

	@FXML
	private JFXRadioButton byCityBtn;

	@FXML
	private JFXRadioButton byDateBtn;

	@FXML
	private JFXRadioButton byTimeBtn;

	@FXML
	private JFXTextField searchField;

	@FXML
	private JFXButton searchBtn;

	@FXML
	private AnchorPane rootPane;

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

	@FXML
	void searchFlight(ActionEvent event) {
		/*
		 * Searching for a particular result. Based on the choice of radioBtn.
		 */
		String input = searchField.getText().toLowerCase().toString();
		System.out.println(input);
		if(byCityBtn.isSelected()){
			loadData(input, "city");
		}
		else if(byDateBtn.isSelected()){
			loadData(input, "date");
		}
		else if(byTimeBtn.isSelected()){
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

				qu = "SELECT * FROM flight NATURAL JOIN jet WHERE flight.source like(\"%"
								+ inputText + "%\") OR flight.destination like(\"%"
								+ inputText + "%\")";
				//qu = "SELECT * FROM flight NATURAL JOIN jet ";

				res = handler.execQuery(qu);
				
			} else if (choice.equals("date")){
				qu = "SELECT * FROM flight NATURAL JOIN jet WHERE flight.flightDate like(\"%"
								+ inputText + "%\")";
				res = handler.execQuery(qu);
			}
			else if (choice.equals("time")){
				qu = "SELECT * FROM flight NATURAL JOIN jet WHERE flight.flightTime like(\"%"
								+ inputText + "%\")";
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
				
				if(flightList.isEmpty()){
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		radioBtnGroup = new ToggleGroup();
		byDateBtn.setToggleGroup(radioBtnGroup);
		byDateBtn.setSelected(true);
		byCityBtn.setToggleGroup(radioBtnGroup);
		byTimeBtn.setToggleGroup(radioBtnGroup);

		initCol();
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
