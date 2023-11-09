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
            // Creare un'istanza di VideoBackgroundPane con il percorso del video
            VideoBackgroundPane videoBackgroundPane = new VideoBackgroundPane("src/v1.mp4");

            // Caricare il layout principale dalla classe Home.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
            Parent root = loader.load();

            // Recuperare il controller del layout principale
            ControllerHome controller = loader.getController();
            // Caricare la classifica
            controller.populateListView();

            // Aggiungere il layout principale sopra il VideoBackgroundPane
            StackPane stackPane = new StackPane();

            // Aggiungere un colore di fallback o un'immagine di fallback
            // Puoi personalizzare questa parte in base alle tue esigenze
            stackPane.setStyle("-fx-background-color: #38B6FF;"); // Imposta un colore di fallback bianco

            stackPane.getChildren().addAll(videoBackgroundPane, root);

            stage.setTitle("HOME");
            Scene homeScene = new Scene(stackPane, 600, 400);

            stage.setScene(homeScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}