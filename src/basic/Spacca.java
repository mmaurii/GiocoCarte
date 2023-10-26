package basic;

import javafx.fxml.FXMLLoader;
import javafx.scene.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;



public class Spacca extends Application {
	String videoFilePath = "src/SPACCA.mp4";
    String audioFilePath = "src/audio.mp3";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Carica il video
        Media videoMedia = new Media(new File(videoFilePath).toURI().toString());
        MediaPlayer videoPlayer = new MediaPlayer(videoMedia);
        MediaView mediaView = new MediaView(videoPlayer);

        //quondo finisce la riproduzione del video avvio l'interfaccia
        videoPlayer.setOnEndOfMedia(() -> {
            videoPlayer.stop();
            showHomeScreen(stage);
        });
        
        // Mostra il video nella finestra
        StackPane videoPane = new StackPane(mediaView);
        Scene videoScene = new Scene(videoPane, 600, 400);
        stage.setScene(videoScene);
        stage.show();
        
        // Avvia la riproduzione del video
        videoPlayer.play();
    }

    private void showHomeScreen(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
            Parent root = loader.load();

            ControllerHome controller = loader.getController();
            //carico la classifica
            controller.populateListView();
            //carico il sottofondo musicale
    		File f = new File(audioFilePath);
    		Media media = new Media(new File(f.getAbsolutePath()).toURI().toString());
    		MediaPlayer mediaPlayer = new MediaPlayer(media);
    		mediaPlayer.setAutoPlay(true);
    		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

            stage.setTitle("HOME");
            Scene homeScene = new Scene(root, 600, 400);
            stage.setScene(homeScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
