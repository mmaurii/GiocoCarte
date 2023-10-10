package basic;

import java.io.*;
import java.net.URL;

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
import javafx.fxml.Initializable;
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
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.VBox;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;

public class ControllerCreaPartita {
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

    //aggiungo alla partita un utente  
    @FXML ListView<String> listUtentiPartita;
    @FXML Button btnAggiungiUtente;
    @FXML TextField txtNomeUtente; 
    @FXML public void AggiungiUtente(ActionEvent actionEvent) {
    	String nome = txtNomeUtente.getText();
    	//controllo che non vengano inseriti giocatori con lo stesso nome all'interno della listview listUtentiPartita
    	if(!listUtentiPartita.getItems().contains(nome)) {
    		txtNomeUtente.clear();
    		listUtentiPartita.getItems().add(nome);
    		giocatoriPrt.add(new Giocatore(nome));
    	}else {
    		txtNomeUtente.clear();
    	}
    }

    //aggiungo alla partita un utente robot 
    @FXML Button btnAggiungiUtenteRobot;
    @FXML TextField txtNomeUtenteRobot; 
    @FXML public void AggiungiUtenteRobot(ActionEvent actionEvent) {
    	String nome = txtNomeUtenteRobot.getText();
    	//controllo che non vengano inseriti giocatori con lo stesso nome all'interno della listview listUtentiPartita
    	if(!listUtentiPartita.getItems().contains(nome)) {
    		txtNomeUtenteRobot.clear();
    		listUtentiPartita.getItems().add(nome);
    		giocatoriPrt.add(new Bot(nome));
    	}else {
    		txtNomeUtenteRobot.clear();
    	}
    }
    
    //Genero il codice per una nuova partita
    @FXML Button btnGeneraCodice;
    @FXML Label lblCodice;
    @FXML ComboBox<String> comboNVite;
    @FXML public void GeneraCodice(ActionEvent actionEvent) {
    	if(listUtentiPartita.getItems().size()>1) {
    		try {
    			File file = new File(pathStatus);
    			Scanner scan = new Scanner(file);//controlla errori legati alla lettura e scrittura del file
    			String codPartita = scan.nextLine().split(" , ")[1];
    			scan.close();

    			//controllare univocita
    			codPartita = Integer.toString(Integer.parseInt(codPartita)+1);

    			//aggiungo al codice gli 0 non rilevanti
    			int nCifre = codPartita.length();
    			for(int i=0; i<lungCodicePartita-nCifre; i++) {
    				codPartita="0"+codPartita;
    			}

        		lblCodice.setTextFill(Color.BLACK);
    			lblCodice.setText(codPartita);
    			btnGeneraCodice.setDisable(true);

    			//salvo il codice corrente nel file di status
    			FileWriter fw = new FileWriter(file);
    			fw.write("codicePartita , "+codPartita);
    			fw.close();

    			//do le vite e le carte ai giocatori
    			String nVite = comboNVite.getSelectionModel().getSelectedItem();
    			if(nVite!=null) {
    				for(Giocatore i : giocatoriPrt) {
    					i.setVite(Integer.parseInt(nVite));
    				}
    			}else {
    				for(Giocatore i : giocatoriPrt) {
    					i.setVite(nViteDefault);
    				}
    			}

    			//imposto i dati di una nuova partita
    			prt=new Partita(codPartita, giocatoriPrt);
    	    	System.out.println(codPartita);

    		}catch(FileNotFoundException e) {
    			System.out.println(e);
    		}catch(IOException eIO) {
    			System.out.println(eIO);    		
    		}
    	}else {
    		lblCodice.setTextFill(Color.RED);
    		lblCodice.setText("Aggiungi almeno due giocatori");
			
    	}
    }

    
    @FXML Button btnTornaAllaHome;
    //torno alla Schermata di login
    @FXML public void TornaAllaHome(ActionEvent actionEvent) {
    	//chiudo la finestra di di creazione della partita e torno alla finestra di login
    	Stage stage = (Stage)btnTornaAllaHome.getScene().getWindow();
    	stage.close();
    	
    	//riapro la finestra di login
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
			Parent root = loader.load();
			
			ControllerHome controller = loader.getController();			
			stage.setTitle("HOME");
			Scene interfacciaHome = new Scene(root);
			//copio le informazioni relative alla partita in corso e carico le informazioni della classifica
			controller.copiaInformazioniPartita(this.prt);
			controller.populateListView();

			stage.setScene(interfacciaHome);
		    stage.setMinHeight(400);
		    stage.setMinWidth(600);
		    stage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    }   
}