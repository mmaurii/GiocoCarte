package basic;

import java.io.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.media.MediaPlayer;

public class ControllerPartitaTorneo {
	//variabili di controllo
	int numeroCarteAGiocatore;
	final int lungCodicePartita=10;
	final int nViteDefault=5;
	Partita prt;
	Mazzo mazzo = new Mazzo();
	ArrayList<Giocatore> giocatoriPrt = new ArrayList<Giocatore>();
	String pathRetroCarta = "/basic/IMGcarte/retro.jpg";
	int countTurnoGiocatore=0;
	boolean dichiaraPrese=true;
	boolean primoTurno=true;
	String pathClassifica = "src/Classifica.txt";
	String pathStatus = "src/Status.txt";

	@FXML Button btnCreaPartita;
	@FXML ComboBox<String> comboNVite;
	@FXML Button btnModificaGiocatori;
	@FXML Button btnCreaTorneo;
	@FXML Button btnTornaAllaHome;
	@FXML Button btnModificaTornei;
	@FXML BorderPane bpInterfaccia;
	@FXML Button btnCreaAccount;

	/**
	 * apre l'interfaccia per creare una nuova partita
	 * @param actionEvent
	 */
	@FXML public void CreaPartitaAction(ActionEvent actionEvent) {
		//chiudo la finestra di scelta per la creazione di partite o tornei
		Stage stage = (Stage)btnCreaPartita.getScene().getWindow();
		stage.close();

		//apro la finestra per la creazine delle partite
		try {
			MediaPlayer currentMediaPlayer = VideoBackgroundPane.getCurrentMediaPlayer();
			if (currentMediaPlayer != null) {
				currentMediaPlayer.stop();
			}

			VideoBackgroundPane videoBackgroundPane = new VideoBackgroundPane("/v1.mp4");

			FXMLLoader loader = new FXMLLoader(getClass().getResource("CreaPartita.fxml"));
			Parent root = loader.load();

			StackPane stackPane = new StackPane();
			stackPane.setStyle("-fx-background-color: #38B6FF;");
			stackPane.getChildren().addAll(videoBackgroundPane, root);

			ControllerCreaPartita controller = loader.getController();
			controller.caricaGiocatoriRegistrati();
			stage.setTitle("Crea una Partita");
			Scene interfacciaCreaPartita = new Scene(stackPane, 600, 400);
			stage.setScene(interfacciaCreaPartita);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * apre l'interfaccia per creare una nuovo torneo
	 * @param actionEvent
	 */
	@FXML public void CreaTorneoAction(ActionEvent actionEvent) {
		Stage stage = (Stage)btnCreaTorneo.getScene().getWindow();
		stage.close();

		//apro la finestra per la creazione del torneo
		try {
			MediaPlayer currentMediaPlayer = VideoBackgroundPane.getCurrentMediaPlayer();
			if (currentMediaPlayer != null) {
				currentMediaPlayer.stop();
			}

			VideoBackgroundPane videoBackgroundPane = new VideoBackgroundPane("/v1.mp4");

			FXMLLoader loader = new FXMLLoader(getClass().getResource("CreaTorneo.fxml"));
			Parent root = loader.load();

			StackPane stackPane = new StackPane();
			stackPane.setStyle("-fx-background-color: #38B6FF;");
			stackPane.getChildren().addAll(videoBackgroundPane, root);

			ControllerCreaTorneo controller = loader.getController();
			controller.caricaGiocatoriRegistrati();
			stage.setTitle("Crea un Torneo");
			Scene interfacciaCreaPartita = new Scene(stackPane, 600, 400);
			stage.setScene(interfacciaCreaPartita);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *apre l'interfaccia per modificare i giocatori
	 * @param actionEvent
	 */
	@FXML public void modificaGiocatori(ActionEvent actionEvent) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("popUpGiocatori.fxml"));
			Parent root = loader.load();
			Stage parentStage = (Stage) bpInterfaccia.getScene().getWindow();

			ControllerPopUpGiocatori controller = loader.getController();
			controller.caricaGiocatori();

			Stage stage = new Stage();
			stage.initOwner(parentStage);
			stage.initModality(Modality.APPLICATION_MODAL); 
			stage.setTitle("Giocatori");
			Scene scene = new Scene(root);
			stage.setMinHeight(400);
			stage.setMinWidth(300);
			stage.setScene(scene);
			stage.showAndWait();		
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	/**
	 * apre l'interfaccia per modificare le partite
	 * @param actionEvent
	 */
	@FXML public void modificaPartite(ActionEvent actionEvent) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("popUpPartite.fxml"));
			Parent root = loader.load();
			Stage parentStage = (Stage) bpInterfaccia.getScene().getWindow();

			ControllerPopUpPartite controller = loader.getController();
			controller.caricaPartite();

			Stage stage = new Stage();
			stage.initOwner(parentStage);
			stage.initModality(Modality.APPLICATION_MODAL); 
			stage.setTitle("Partite");
			Scene scene = new Scene(root);
			stage.setHeight(450);
			stage.setWidth(350);
			stage.setScene(scene);
			stage.showAndWait();		
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 

	/**
	 * apre l'interfaccia per modificare i tornei
	 * @param actionEvent
	 */
	@FXML public void modificaTornei(ActionEvent actionEvent) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("popUpTornei.fxml"));
			Parent root = loader.load();
			Stage parentStage = (Stage) bpInterfaccia.getScene().getWindow();

			ControllerPopUpTornei controller = loader.getController();
			controller.caricaTornei();

			Stage stage = new Stage();
			stage.initOwner(parentStage);
			stage.initModality(Modality.APPLICATION_MODAL); 
			stage.setTitle("Tornei");
			Scene scene = new Scene(root);
			stage.setHeight(450);
			stage.setWidth(350);
			stage.setScene(scene);
			stage.showAndWait();		
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 

	/**
	 * chiude l'interfaccia e riapre la home iniziale con il login
	 * @param actionEvent
	 */
	@FXML public void TornaAllaHome(ActionEvent actionEvent) {
		//chiudo la finestra del menu e di gestione delle funzionalita
		Stage stage = (Stage)btnTornaAllaHome.getScene().getWindow();
		stage.close();

		//riapro la finestra di login
		try {
			MediaPlayer currentMediaPlayer = VideoBackgroundPane.getCurrentMediaPlayer();
			if (currentMediaPlayer != null) {
				currentMediaPlayer.stop();
			}
			VideoBackgroundPane videoBackgroundPane = new VideoBackgroundPane("/v1.mp4");

			FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
			Parent root = loader.load();

			ControllerHome controller = loader.getController();	

			StackPane stackPane = new StackPane();
			stackPane.setStyle("-fx-background-color: #38B6FF;");
			stackPane.getChildren().addAll(videoBackgroundPane, root);
			stage.setTitle("HOME");
			Scene interfacciaHome = new Scene(stackPane, 600, 400);
			//carico le informazioni della classifica
			controller.caricaClassifica();
			stage.setScene(interfacciaHome);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}   

	/**
	 * apre un interfaccia per poter gestire gli account amministratore
	 * @param actionEvent
	 */
	@FXML public void creaAccount(ActionEvent actionEvent) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("popUpAmministratori.fxml"));
			Parent root = loader.load();
			Stage parentStage = (Stage) bpInterfaccia.getScene().getWindow();

			loader.getController();

			Stage stage = new Stage();
			stage.initOwner(parentStage);
			stage.initModality(Modality.APPLICATION_MODAL); 
			stage.setTitle("Amministratori");
			Scene scene = new Scene(root);
			stage.setHeight(450);
			stage.setWidth(335);
			stage.setScene(scene);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}