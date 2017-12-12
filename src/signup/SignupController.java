package signup;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import DatabaseHandler.DatabaseHandler;
import application.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

public class SignupController implements Initializable{

	@FXML
	private VBox vbox;
	
	@FXML
    private JFXTextField firstnameField;

    @FXML
    private JFXTextField lastnameField;

    @FXML
    private JFXTextField addressField;

    @FXML
    private JFXTextField userField;

    @FXML
    private JFXTextField passField;

    @FXML
    private JFXTextField zipcodeField;

    @FXML
    private JFXTextField stateField;

    @FXML
    private JFXTextField emailField;

    @FXML
    private JFXTextField ssnField;

    @FXML
    private JFXButton doneBtn;

    @FXML
    private JFXButton cancelBtn;

    @FXML
    void signupMethod(ActionEvent event) {
    	/*
		 * Here we will first check the validation of input data
		 * Then we will store it in the database if its correct.
		 *
		 */
    	
    	if(checkValidatoin()){
    		Alert alert1 = new Alert(Alert.AlertType.ERROR);
			alert1.setTitle("Failed");
			alert1.setHeaderText(null);
			alert1.setContentText("Input Fields are empty.");
			alert1.showAndWait();
			return;
    	}
    	
		
		
		//Creating Query for prepared statement.
		String qu = "INSERT INTO " + User.TABLE + "(SSN ,firstname,lastname,address,state,zipcode,username,password,email) VALUES (?,?,?,?,?,?,?,?,?) ";
		
		//Creating a Prepared Statement object to excute query
		try {
			PreparedStatement p1 = DatabaseHandler.getConn().prepareStatement(qu);
			p1.setString(1, ssnField.getText().toLowerCase().toString());
			p1.setString(2, firstnameField.getText().toLowerCase().toString());
			p1.setString(3, lastnameField.getText().toLowerCase().toString());
			p1.setString(4, addressField.getText().toLowerCase().toString());
			p1.setString(5, stateField.getText().toLowerCase().toString());
			p1.setString(6, zipcodeField.getText().toLowerCase().toString());
			p1.setString(7, userField.getText().toLowerCase().toString());
			p1.setString(8, passField.getText().toLowerCase().toString());
			p1.setString(9, emailField.getText().toLowerCase().toString());
			
			
			p1.executeUpdate();
			
			//Showing Information on Alert Dialog Box on success
			Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
			alert1.setTitle("Success");
			alert1.setHeaderText(null);
			alert1.setContentText("User Successfully added to the database!");
			alert1.showAndWait();
			
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			Alert alert1 = new Alert(Alert.AlertType.ERROR);
			alert1.setTitle("Failed");
			alert1.setHeaderText(null);
			alert1.setContentText("Key Voilation!\nTry to input again!\nUser with same username or Same SSN exists");
			alert1.showAndWait();
		}
		
	}
    
    @FXML
    void cancelMethod(ActionEvent event){
    	Parent parent;
		try {
			FXMLLoader loader = new  FXMLLoader(getClass().getResource("/application/main.fxml"));
			parent = (Parent)loader.load();
			Stage currStage = (Stage) vbox.getScene().getWindow();
			MainController controller = (MainController)loader.getController();
			controller.setPrevStage(currStage);
			currStage.setTitle("Main Screen");
			currStage.setScene(new Scene(parent));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		BackgroundImage myBI= new BackgroundImage(new Image("File:images/main_background.jpg"),
		        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
		          BackgroundSize.DEFAULT);
		
		vbox.setBackground(new Background(myBI));
	}
     
	
	private boolean checkValidatoin(){
		return ssnField.getText().toString().equals("") || firstnameField.getText().toString().equals("") ||
				lastnameField.getText().toString().equals("") || zipcodeField.getText().toString().equals("") ||
				stateField.getText().toString().equals("") || addressField.getText().toString().equals("") ||
				userField.getText().toString().equals("") || passField.getText().toString().equals("") ||
				emailField.getText().toString().equals("") ;
	}
}
