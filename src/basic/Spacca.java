package basic;

import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class Spacca extends Application {
	String videoFilePath = "src/SPACCA.mp4";
	String v = "src/v1.mp4";


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Carica il video
        Media videoMedia = new Media(new File(videoFilePath).toURI().toString());
        MediaPlayer videoPlayer = new MediaPlayer(videoMedia);
        MediaView mediaView = new MediaView(videoPlayer);

        // Avvia la riproduzione del video
        videoPlayer.setOnEndOfMedia(() -> {
            videoPlayer.stop();
            showHomeScreen(stage);
        });
        videoPlayer.play();

        // Mostra il video nella finestra
        StackPane videoPane = new StackPane(mediaView);
        Scene videoScene = new Scene(videoPane, 600, 400);
        stage.setScene(videoScene);
        stage.show();
    }

    private void showHomeScreen(Stage stage) {
        try {
            // Carica il layout principale dal file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
            Parent root = loader.load();

            // Carica il video di sfondo
            File f = new File(v);
            Media media = new Media(new File(f.getAbsolutePath()).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

            // Crea un MediaView per il video di sfondo
            MediaView mediaView = new MediaView(mediaPlayer);

            // Imposta il MediaView per coprire l'intera scena
            mediaView.setFitWidth(1920);
            mediaView.setFitHeight(1080);

            // Aggiungi il MediaView alla radice del layout
            ((Pane) root).getChildren().add(mediaView);

            // Recupera il controller del layout principale
            ControllerHome controller = loader.getController();
            controller.populateListView();

            stage.setTitle("HOME");
            Scene homeScene = new Scene(root, 600, 400);

            // Imposta lo stile del MediaView in modo che sia dietro agli altri elementi
            mediaView.toBack();

            stage.setScene(homeScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}