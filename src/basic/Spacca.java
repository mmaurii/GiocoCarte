package basic;

import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class Spacca extends Application {
	final String pathVideoAnimazione = "/SPACCA.mp4";
	final String pathVideoSfondo = "/v1.mp4";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Carica il video dell'animazione iniziale
        Media videoMedia = new Media(getClass().getResource(pathVideoAnimazione).toExternalForm());
        MediaPlayer videoPlayer = new MediaPlayer(videoMedia);
        MediaView mediaView = new MediaView(videoPlayer);

        //quondo finisce la riproduzione del video avvio l'interfaccia
        videoPlayer.setOnEndOfMedia(() -> {
            videoPlayer.stop();
            caricaHomeScreen(stage);
        });
        
        // Mostra il video nella finestra
        StackPane videoPane = new StackPane(mediaView);
        Scene videoScene = new Scene(videoPane, 600, 400);
        stage.setScene(videoScene);
        stage.show();
        
        // Avvia la riproduzione del video
        videoPlayer.play();
    }
    
    /**
     * carica i dati della schermata di home nello stage preso come parametro
     * @param stage
     */
    private void caricaHomeScreen(Stage stage) {
        try {
            // Creare un'istanza di VideoBackgroundPane con il percorso del video
            VideoBackgroundPane videoBackgroundPane = new VideoBackgroundPane("/v1.mp4");
			Thread.sleep(200);
            // Caricare il layout principale dalla classe Home.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
            Parent root = loader.load();

            // Recuperare il controller del layout principale
            ControllerHome controller = loader.getController();
            // Caricare la classifica
            controller.caricaClassifica();

            // Aggiungere il layout principale sopra il VideoBackgroundPane
            BorderPane bp = new BorderPane();

            // Aggiungere un colore di fallback o un'immagine di fallback
            // Puoi personalizzare questa parte in base alle tue esigenze
            bp.setStyle("-fx-background-color: #38B6FF;"); // Imposta un colore di fallback bianco

            bp.getChildren().addAll(videoBackgroundPane, root);

            stage.setTitle("HOME");
            Scene homeScene = new Scene(bp, 600, 400);
            stage.setScene(homeScene);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
}