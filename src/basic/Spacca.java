package basic;

import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class Spacca extends Application {
	final String pathVideoAnimazione = "/SPACCA.mp4";
	final String pathVideoSfondo = "/v1.mp4";

	public static void main(String[] args) {
		final String telegramWebSite = "https://t.me/GiocoSpaccaBot";
		try {
			if(netIsAvailable(telegramWebSite)) {
				try {
					TelegramBotsApi botsApi = new TelegramBotsApi( DefaultBotSession.class );
					botsApi.registerBot(new JavaSpaccaBot());
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
			StackPane sp = new StackPane();

			// Aggiungere un colore di fallback o un'immagine di fallback
			// Puoi personalizzare questa parte in base alle tue esigenze
			sp.setStyle("-fx-background-color: #38B6FF;"); // Imposta un colore di fallback bianco

			sp.getChildren().addAll(videoBackgroundPane, root);

			stage.setTitle("HOME");
			Scene homeScene = new Scene(sp, 600, 400);
			stage.setScene(homeScene);

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {//controllo l'evento di chiusura dello stage
				@Override
				public void handle(WindowEvent e) {//killo tutti i processi
					Platform.exit();
					System.exit(0);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * controlla se la connessione internet all'indirizzo specificato è possibile
	 * @param path
	 * @return true se possibile false altrimenti
	 */
	private static boolean netIsAvailable(String path) {
		try {
			final URL url = new URL(path);
			final URLConnection conn = url.openConnection();
			conn.connect();
			conn.getInputStream().close();
			return true;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			return false;
		}
	}

}