package mainapp;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Util;

public class MainAppController implements Initializable {

	@FXML
    private VBox btnVbox;
	
	@FXML
    private JFXButton addBtn;

    @FXML
    private JFXButton deleteBtn;

    @FXML
    private JFXButton searchBtn;


    @FXML
    private JFXButton displayFlightBtn;

    @FXML
    private AnchorPane rootPane;

    
    @FXML
    private AnchorPane rightPane;

    @FXML
    private BorderPane borderpane;

    @FXML
    private JFXButton changepassBtn;

    @FXML
    private JFXButton logoutBtn;
    
    @FXML
    private JFXButton cFlightBtn;
    
    

    
    
    @FXML
    void addFlight(ActionEvent event) {
    	System.out.println("AddFlight CLICKED");
    	try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/addFlight/addflight.fxml"));
			VBox root = (VBox)loader.load();
			borderpane.setCenter(root);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    @FXML
    void deleteFlight(ActionEvent event) {
    	System.out.println("Delete Flight CLICKED");
    	try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/deleteFlight/deleteflight.fxml"));
			AnchorPane root = (AnchorPane)loader.load();
			borderpane.setCenter(root);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    	
    }

    @FXML
    void searchFlight(ActionEvent event) {
    	System.out.println("Search Flight CLICKED");
    	try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/searchFlight/searchflight.fxml"));
			AnchorPane root = (AnchorPane)loader.load();
			borderpane.setCenter(root);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    }
    
    
    @FXML
    void displayFlight(ActionEvent event) {
    	System.out.println("Display Flight CLICKED");
    	try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/displayFlights/displayflights.fxml"));
			AnchorPane root = (AnchorPane)loader.load();
			borderpane.setCenter(root);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @FXML
    void changePassword(ActionEvent event) {

    	try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/changePassword/changepassword.fxml"));
			AnchorPane root = (AnchorPane)loader.load();
			borderpane.setCenter(root);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

    
    @FXML
    void logOut(ActionEvent event) {
    	
    	Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("Log Out");
		alert.setContentText("Are you sure you want to logout?");

		Optional<ButtonType> check = alert.showAndWait();
		if (check.get() == ButtonType.OK){
		    // ... user chose OK
			Parent parent;
			try {
				FXMLLoader loader = new  FXMLLoader(getClass().getResource("/application/main.fxml"));
				parent = (Parent)loader.load();
				Stage currStage = (Stage) rootPane.getScene().getWindow();
				MainController controller = (MainController)loader.getController();
				controller.setPrevStage(currStage);
				currStage.setTitle("Main Screen");
				currStage.setScene(new Scene(parent));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
		    // ... user chose CANCEL or closed the dialog
		}
    	
    	
    	
    }
    
    @FXML
    void myFlightsCustomer(ActionEvent event) {
    	try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/myflight/myflight.fxml"));
			BorderPane root = (BorderPane)loader.load();
			borderpane.setCenter(root);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

    

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// 0 means Customer 1 means Admin
		int isAdmin = Util.getLoginPerson().getIsAdmin();
		if(isAdmin == 0){
			//addBtn.setVisible(false);
			btnVbox.getChildren().remove(addBtn);
			btnVbox.getChildren().remove(searchBtn);
			btnVbox.getChildren().remove(deleteBtn);
			btnVbox.getChildren().add(1, cFlightBtn);
			cFlightBtn.setVisible(true);
		}

	}

	  
	  
}
