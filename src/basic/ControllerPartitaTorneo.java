package basic;

import java.io.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.*;

import javax.security.auth.login.AccountNotFoundException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;

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
    private ArrayList<Carta> lstCarteBanco = new ArrayList<Carta>();
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
	
    //crea partita
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
			
            VideoBackgroundPane videoBackgroundPane = new VideoBackgroundPane("src/v1.mp4");

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    //crea torneo
    @FXML public void CreaTorneoAction(ActionEvent actionEvent) {
    	Stage stage = (Stage)btnCreaTorneo.getScene().getWindow();
    	stage.close();
    	
    	//apro la finestra per la creazione del torneo
		try {
			MediaPlayer currentMediaPlayer = VideoBackgroundPane.getCurrentMediaPlayer();
			if (currentMediaPlayer != null) {
	            currentMediaPlayer.stop();
	        }
			
			VideoBackgroundPane videoBackgroundPane = new VideoBackgroundPane("src/v1.mp4");

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @FXML public void modificaGiocatori(ActionEvent actionEvent) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("popUpGiocatori.fxml"));
            Parent root = loader.load();

			ControllerPopUpGiocatori controller = loader.getController();
			controller.populateLst();

			Stage stage = new Stage();
			stage.setTitle("Giocatori");
			Scene scene = new Scene(root);
			stage.setMinHeight(400);
		    stage.setMinWidth(300);
			stage.setScene(scene);
			stage.show();			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
    
    @FXML public void modificaPartite(ActionEvent actionEvent) {
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("popUpPartite.fxml"));
            Parent root = loader.load();

			ControllerPopUpPartite controller = loader.getController();
			controller.populateLst();

			Stage stage = new Stage();
			stage.setTitle("Partite");
			Scene scene = new Scene(root);
			stage.setHeight(450);
		    stage.setWidth(350);
			stage.setScene(scene);
			stage.show();			
		} catch (IOException e) {
			e.printStackTrace();
		}
    } 
    
    @FXML public void modificaTornei(ActionEvent actionEvent) {
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("popUpTornei.fxml"));
            Parent root = loader.load();

			ControllerPopUpTornei controller = loader.getController();
			controller.caricaTornei();

			Stage stage = new Stage();
			stage.setTitle("Tornei");
			Scene scene = new Scene(root);
			stage.setHeight(450);
		    stage.setWidth(350);
			stage.setScene(scene);
			stage.show();			
		} catch (IOException e) {
			e.printStackTrace();
		}
    } 
    
    //torno alla Schermata di login
	@FXML public void TornaAllaHome(ActionEvent actionEvent) {
		//chiudo la finestra di di creazione della partita e torno alla finestra di login
		Stage stage = (Stage)btnTornaAllaHome.getScene().getWindow();
		stage.close();

		//riapro la finestra di login
		try {
            VideoBackgroundPane videoBackgroundPane = new VideoBackgroundPane("src/v1.mp4");
			
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}   
}