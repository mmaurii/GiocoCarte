package basic;

import javafx.scene.Parent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class VideoBackgroundPane extends Parent {

    private static MediaPlayer currentMediaPlayer;

    public VideoBackgroundPane(String videoFile) {
        init(videoFile);
    }

    /**
     * imposta un video nel MediaPlayer
     * @param pathVideoFile percorso del video da metterre come sfondo
     */
    private void init(String pathVideoFile) {
        // Carica il file video
        Media media = new Media(getClass().getResource(pathVideoFile).toExternalForm());

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

        currentMediaPlayer = mediaPlayer;
    }

    public static MediaPlayer getCurrentMediaPlayer() {
        return currentMediaPlayer;
    }
}
