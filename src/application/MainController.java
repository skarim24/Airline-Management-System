package application;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import DatabaseHandler.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;
import model.Util;

public class MainController implements Initializable {

	private Stage prevStage;

	public void setPrevStage(Stage stage) {
		this.prevStage = stage;
	}

	@FXML
	private ImageView loginImageView;

	@FXML
	private JFXTextField usernameField;

	@FXML
	private JFXPasswordField passwordField;

	@FXML
	private JFXButton loginBtn;

	@FXML
	private JFXButton signupBtn;
	

    @FXML
    private BorderPane borderPane;


	DatabaseHandler handler;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		handler = DatabaseHandler.getInstance();
		BackgroundImage myBI= new BackgroundImage(new Image("File:images/main_background.jpg"),
		        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
		          BackgroundSize.DEFAULT);
		
		borderPane.setBackground(new Background(myBI));
		
//		Image image = new Image("File:images/loginImage.png");
//		loginImageView.setImage(image);
		
	}

	@FXML
	void loadLoginPage(ActionEvent e) {

		/*If the input fields are empty
		 *then notify using alert dialog box
		 * */
		if(checkValidation()){
			Alert alert1 = new Alert(Alert.AlertType.ERROR);
			alert1.setTitle("Failed");
			alert1.setHeaderText(null);
			alert1.setContentText("One of the Input Field is empty.");
			alert1.showAndWait();
			return;
		}

		// Checking if the username and password is valid then login
		// successfully.
		String qu = "SELECT * FROM person WHERE username = \"" + usernameField.getText().toLowerCase().toString() + "\""
				+ " AND password = \"" + passwordField.getText().toLowerCase().toString() + "\"";

		try {
			// PreparedStatement p1 =
			// DatabaseHandler.getConn().prepareStatement(qu);
			// p1.setString(1,
			// usernameField.getText().toLowerCase().toString());
			// //p1.setString(2,
			// passwordField.getText().toLowerCase().toString());

			ResultSet result = handler.execQuery(qu);

			if (result == null)
				System.out.println("RESULT is NULL");

			if (!result.next()) {
				Alert alert1 = new Alert(Alert.AlertType.ERROR);
				alert1.setTitle("Failed");
				alert1.setHeaderText(null);
				alert1.setContentText("Failed to login! Invalid username or password. Try Again!");
				alert1.showAndWait();
			} else {
				User user = new User(result.getString("SSN"), result.getString(User.COL_FIRSTNAME),
						result.getString(User.COL_LASTNAME), result.getString(User.COL_ADDRESS),
						result.getString(User.COL_ZIPCODE), result.getString(User.COL_STATE),
						result.getString(User.COL_USERNAME), result.getString(User.COL_PASSWORD),
						result.getString(User.COL_EMAIL), result.getInt(User.COL_ISADMIN));
				Util.setLoginPerson(user);
				loadWindow("/mainapp/mainapp.fxml", "AirLine Management System");
			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// }

	}

	@FXML
	void loadSignUp(ActionEvent e) {
		loadWindow("/signup/signup.fxml", "Sign Up");
	}

	void loadWindow(String loc, String title) {
		try {
			Parent parent = FXMLLoader.load(getClass().getResource(loc));
			prevStage.setTitle(title);
			prevStage.setMaximized(true);
			prevStage.setScene(new Scene(parent));
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	private boolean checkValidation(){
		return usernameField.getText().toString().equals("") || passwordField.getText().toString().equals("") ;
	}

}
