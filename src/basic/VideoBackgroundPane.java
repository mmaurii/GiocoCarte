package basic;

import java.io.File;

import javafx.scene.Parent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class VideoBackgroundPane extends Parent {

    public VideoBackgroundPane(String videoFile) {
        init(videoFile);
    }

    private void init(String videoFile) {
        // Carica il file video
    	File f = new File(videoFile);
        Media media = new Media(new File(f.getAbsolutePath()).toURI().toString());

        // Imposta il video come sfondo
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        // Crea un MediaView per il video di sfondo
        MediaView mediaView = new MediaView(mediaPlayer);

        // Imposta il MediaView per coprire l'intera scena
        mediaView.setFitWidth(1920);
        mediaView.setFitHeight(1080);


        getChildren().add(mediaView);
    }
}