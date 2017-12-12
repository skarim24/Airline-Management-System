package changePassword;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import DatabaseHandler.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import model.Util;

public class ChangePassController {

	@FXML
    private JFXTextField oldpassField;

    @FXML
    private JFXTextField newpassField;
    
    @FXML
    private JFXTextField newpassField1;


    @FXML
    private JFXButton changeBtn;

    @FXML
    void changePassword(ActionEvent event) {
    	String currentPass = Util.getLoginPerson().getPassword();
    	DatabaseHandler handler = DatabaseHandler.getInstance();
    	
    	if(oldpassField.getText().toString().equals(currentPass)){
    		String newPass =  newpassField.getText().toString();
    		String confirmPass = newpassField1.getText().toString();
    		String loginpersonId = Util.getLoginPerson().getSsn();
    		
    		if(newPass.equals(confirmPass)){
    			//Updating the login person password using query.
        		String qu = "UPDATE PERSON SET password = \"" + newPass + "\" WHERE SSN = \"" + loginpersonId + "\"";
        		handler.execAction(qu);
        		Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
    			alert1.setTitle("Success");
    			alert1.setHeaderText(null);
    			alert1.setContentText("Password changed successfully");
    			alert1.showAndWait();
    		}
    		else{
    			Alert alert1 = new Alert(Alert.AlertType.ERROR);
    			alert1.setTitle("Failed");
    			alert1.setHeaderText(null);
    			alert1.setContentText("Password don't match. Try Again!");
    			alert1.showAndWait();
    		}
    	
    	}
    	else{
    		Alert alert1 = new Alert(Alert.AlertType.ERROR);
			alert1.setTitle("Failed");
			alert1.setHeaderText(null);
			alert1.setContentText("Old Password is Incorrect");
			alert1.showAndWait();
    	}
    	
    }
	
}
