package basic;

import javafx.fxml.FXMLLoader;
import javafx.scene.*;

import java.io.File;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;



public class Spacca extends Application{
	
	
	public static void main(String[] args) {

		launch(args);
	}
	
	@Override
		public void start(Stage stage) throws Exception {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
			Parent root = loader.load();
			
			ControllerHome controller = loader.getController();
			controller.populateListView();
			
			
			stage.setTitle("HOME");
			/*
			String audioFilePath = ("src/audio.mp3");
			File f = new File(audioFilePath);
			System.out.println(f.getAbsolutePath());
			Media media = new Media(new File(f.getAbsolutePath()).toURI().toString());
	        MediaPlayer mediaPlayer = new MediaPlayer(media);
	        mediaPlayer.setAutoPlay(true);
	        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);*/
		    
			Scene interfacciaLogin = new Scene(root);
			stage.setScene(interfacciaLogin);
		    stage.setMinHeight(400);
		    stage.setMinWidth(600);
			stage.show();
		}
}
