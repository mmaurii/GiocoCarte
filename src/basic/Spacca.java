package basic;
import java.util.*;


import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.*;
import javafx.application.Application;


public class Spacca extends Application{
	
	public static void main(String[] args) {

		launch(args);
	}
	
	@Override
		public void start(Stage stage) throws Exception {
			Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
			stage.setTitle("Login Amministratore");
			Scene interfacciaLogin = new Scene(root);
			stage.setScene(interfacciaLogin);
			stage.show();
		}
}
