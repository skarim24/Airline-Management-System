package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			//Loading the fxml file 
			FXMLLoader loader = new FXMLLoader((getClass().getResource("/application/main.fxml")));
			//getting the fxml UI into parent object.
			Parent root = (Parent)loader.load();
			//Creating a MainController object to send the current primaryStage Object to the controller.
			MainController controller = (MainController)loader.getController();
			controller.setPrevStage(primaryStage);
			//Setting up the Scene in the stage.
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			//Showing the Stage on the screen.
			primaryStage.setMaximized(true);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
