package deleteFlight;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import DatabaseHandler.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import model.Flight;

public class deleteFlightController implements Initializable{

	
	 @FXML
	    private JFXTextField flightIDField;

	    @FXML
	    private JFXButton deleteBtn;

	    @FXML
	    void deleteFlight(ActionEvent event) {
	    	DatabaseHandler handler = DatabaseHandler.getInstance();
	    	String input = flightIDField.getText().toString();
	    	
	    	String query = "SELECT * FROM FLIGHT WHERE flightID = " + input;
	    	
	    	ResultSet result = handler.execQuery(query);
	    	
	    	try {
				if(result.next()){
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Confirmation Dialog");
					alert.setHeaderText("Deleting Flight");
					alert.setContentText("The Flight will be deleted from the database.\nAre you sure you want to delete?");

					Optional<ButtonType> check = alert.showAndWait();
					if (check.get() == ButtonType.OK){
					    // ... user chose OK
						String delQuery = "DELETE FROM FLIGHT WHERE flightID = " + input;
						String qu = "UPDATE JET SET isFree = 1 WHERE jetID = " + result.getInt(Flight.COL_JETID);
						handler.execAction(delQuery);
						handler.execAction(qu);
						Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
						alert1.setTitle("Deleted");
						alert1.setHeaderText(null);
						alert1.setContentText("Successfully Deleted");
						alert1.showAndWait();
					} else {
					    // ... user chose CANCEL or closed the dialog
					}
				}
				else{
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
	    	
	    	
	    }

		@Override
		public void initialize(URL location, ResourceBundle resources) {
			
		}
	    
}
