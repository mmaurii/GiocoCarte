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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.VBox;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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
    String pathGiocatoriRegistrati = "src/GiocatoriRegistrati.txt";
    
    @FXML ListView<String> lstGiocatoriRegistrati;

    //aggiungo alla partita un nuovo utente  
    @FXML ListView<String> listUtentiPartita;
    @FXML Button btnAggiungiUtente;
    @FXML TextField txtNomeUtente; 
    @FXML public void AggiungiUtente(ActionEvent actionEvent) {
    	
    	
    	
    	
    	String nome = txtNomeUtente.getText();
    	//controllo che non vengano inseriti giocatori con lo stesso nome all'interno della listview listUtentiPartita
    	if(!listUtentiPartita.getItems().contains(nome) && !lstGiocatoriRegistrati.getItems().contains(nome)) {
    		txtNomeUtente.clear();
    		listUtentiPartita.getItems().add(nome);
    		lstGiocatoriRegistrati.getItems().add(nome);
    		giocatoriPrt.add(new Giocatore(nome));
    		
    		try{

    			FileWriter fw = new FileWriter(pathGiocatoriRegistrati, true);
    			fw.write(nome + '\n');
    			fw.close();
    			
	    		} catch (FileNotFoundException FNFe) {
	    			// TODO Auto-generated catch block
	    			FNFe.printStackTrace();
	    		} catch (IOException IOe) {
	    			// TODO Auto-generated catch block
	    			IOe.printStackTrace();
	    		}
    		
    		
    	}else {
    		txtNomeUtente.clear();
    		
    		Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText(null);
            alert.setContentText("Il Nickname inserito è già stato selezionato da un altro giocatore, riprova con un altro Nickname");

            alert.showAndWait();
    	}
    	
    }
    
    //aggiungo alla partita un utente già registrato
    
    
    @FXML public void selezionaGiocatore(MouseEvent mouseEvent) {
    	
        String nome = lstGiocatoriRegistrati.getSelectionModel().getSelectedItem();   
        
        if(!listUtentiPartita.getItems().contains(nome) && nome != null)
        {
    		listUtentiPartita.getItems().add(nome);
    		giocatoriPrt.add(new Giocatore(nome));	
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
    @FXML TextField lblCodice;
    @FXML ComboBox<String> comboNVite;
    @FXML public void GeneraCodice(ActionEvent actionEvent) {
    	if(listUtentiPartita.getItems().size()>1) {
    		try {
    			
    			UUID uniqueID = UUID.randomUUID();
    	        String uniqueCode = uniqueID.toString().replaceAll("-", "").substring(0, 8);
    			File file = new File(pathStatus);

    			
    			/**
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
    			}**/

    	        //lblCodice.setStyle("-fx-control-inner-background: grey;");
    			lblCodice.setText(uniqueCode);
    			btnGeneraCodice.setDisable(true);

    			//salvo il codice corrente nel file di status
    			FileWriter fw = new FileWriter(file);
    			fw.write("codicePartita , "+uniqueCode);
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
    			prt=new Partita(uniqueCode, giocatoriPrt);
    		}catch(FileNotFoundException e) {
    			System.out.println(e);
    		}catch(IOException eIO) {
    			System.out.println(eIO);    		
    		}
    	}else {
    		lblCodice.setStyle("-fx-text-fill: red;");

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

    public void populateListView() {
    	
		try {
			File file = new File(pathGiocatoriRegistrati);
			Scanner scan = new Scanner(file);			
			while(scan.hasNext()) {
				String line = scan.nextLine();
				lstGiocatoriRegistrati.getItems().add(line.trim());	
			}
			scan.close();
		} catch (FileNotFoundException fnfe) {
			// TODO Auto-generated catch block
			fnfe.printStackTrace();
		}
		
		//metto il contenuto della listview in grassetto
		lstGiocatoriRegistrati.setStyle("-fx-font-weight: bold;");
		
		//centro le scritte all'interno della listview
		lstGiocatoriRegistrati.setCellFactory(param -> new ListCell<String>() {
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
    }
}