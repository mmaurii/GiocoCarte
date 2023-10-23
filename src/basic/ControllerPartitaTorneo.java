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
import javafx.scene.layout.Pane;

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
    
    //crea partita
    @FXML public void CreaPartitaAction(ActionEvent actionEvent) {
    	//chiudo la finestra di scelta per la creazione di partite o tornei
    	Stage stage = (Stage)btnCreaPartita.getScene().getWindow();
    	stage.close();
    	
    	//apro la finestra per la creazine delle partite

		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("CreaPartita.fxml"));
			Parent root = loader.load();
			
			ControllerCreaPartita controller = loader.getController();
			controller.populateListView();
			
			
			stage.setTitle("Crea una Partita");
			Scene interfacciaCreaPartita = new Scene(root);
			stage.setScene(interfacciaCreaPartita);
			stage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @FXML public void MouseEntra(MouseEvent mouseEvent) {
    	
        btnCreaPartita.setStyle("-fx-background-color:  lightblue; -fx-border-color: black; -fx-border-width: 2;");
    	
    }
    
    @FXML public void MouseEsce(MouseEvent mouseEvent) {
    	
    	btnCreaPartita.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2;");
    	
    }

    
    //crea torneo
    @FXML Button btnCreaTorneo;
    @FXML public void CreaTorneoAction(ActionEvent actionEvent) {
    	Stage stage = (Stage)btnCreaTorneo.getScene().getWindow();
    	stage.close();
    	
    	//apro la finestra per la creazione del torneo
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("CreaTorneo.fxml"));
			stage.setTitle("Crea un Torneo");
			Scene interfacciaCreaTorneo = new Scene(root);
			stage.setScene(interfacciaCreaTorneo);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
 @FXML public void MouseEntra2(MouseEvent mouseEvent) {
    	
	 btnCreaTorneo.setStyle("-fx-background-color:  lightblue; -fx-border-color: black; -fx-border-width: 2;");
    	
    }
    
    @FXML public void MouseEsce2(MouseEvent mouseEvent) {
    	
    	btnCreaTorneo.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2;");
    	
    }
    
    @FXML ListView<String> lstClassifica;

    public void populateListView() {
    	
		try {
			File file = new File(pathClassifica);
			Scanner scan = new Scanner(file);			
			while(scan.hasNext()) {
				String line = scan.nextLine();
				lstClassifica.getItems().add(line);	
			}
			scan.close();
		} catch (FileNotFoundException fnfe) {
			// TODO Auto-generated catch block
			fnfe.printStackTrace();
		}
	
		//ordino in base al punteggio la listview
		lstClassifica.getItems().sort(Comparator.reverseOrder());			
		int counter=1;
		ArrayList<String> listaNumerata = new ArrayList<String>();
		for(String i : lstClassifica.getItems()) {
			i=(counter+"\t"+i);
			listaNumerata.add(i);
			counter++;
		}
		
		//metto il contenuto della listview in grassetto
		lstClassifica.setStyle("-fx-font-weight: bold;");
		
		//centro le scritte all'interno della listview
		lstClassifica.setCellFactory(param -> new ListCell<String>() {
		    @Override
		    protected void updateItem(String item, boolean empty) {
		        super.updateItem(item, empty);
		        if (empty || item == null) {
		            setText(null);
		            setGraphic(null);
		        } else {
		            setText(item);
		            setAlignment(javafx.geometry.Pos.CENTER);
		        }
		    }
		});
		
		//mostro in output la classifica
		lstClassifica.getItems().setAll(listaNumerata);
    }
}