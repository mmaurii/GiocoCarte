package basic;

import java.io.*;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.net.URL;
import java.sql.Time;

import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import java.util.*;

//import javax.management.timer.Timer;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;


public class ControllerHome implements Initializable{
	//variabili di controllo
	int numeroCarteAGiocatore;
    final int lungCodicePartita=10;
    final int nViteDefault=5;
    boolean isLocked=false;
    Partita prt;
    Mazzo mazzo = new Mazzo();
    ArrayList<Giocatore> giocatoriPrt = new ArrayList<Giocatore>();
    String pathRetroCarta = "/basic/IMGcarte/retro.jpg";
    int countTurnoGiocatore=0;
    boolean dichiaraPrese=true;
    boolean primoTurno=true;
    String pathClassifica = "src/Classifica.txt";
    String pathStatus = "src/Status.txt";
    
    //eventi FXML
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnLogin;
    //login
    @FXML public void loginAction(ActionEvent actionEvent) {

        String username_text = txtUsername.getText();
        String password_text = txtPassword.getText();
        Amministratore a = new Amministratore(username_text, password_text);
        //controllo nonme utente e password
        if(a.verificaAdmin())
        {
        	//se la password è corretta chiudo la finestra di home
        	Stage stage = (Stage)btnLogin.getScene().getWindow();
        	stage.close();
        	//apro la finestra delle impostazioni e creazine partite e tornei
			
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("PartitaTorneo.fxml"));
				Parent root = loader.load();
				
				stage.setTitle("Menu");
				Scene interfacciaPartitaTorneo = new Scene(root);
				stage.setScene(interfacciaPartitaTorneo);
				stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }else {
        	//avvisa dell' errore
        }
    }
    
	//avvio l'interfaccia di gioco
    @FXML Label lblTurnoGiocatore;
    @FXML Button btnGioca;
    @FXML TextField txtCodPartita;
    @FXML Label lblCodPartitaErrato;
    @FXML ListView<String> lstViewVite;
    @FXML public void avviaPartita(ActionEvent actionEvent) {
    	//ottengo il codice partita inserito dall'utente
    	String codPartita = txtCodPartita.getText();
    	Partita p = verificaDisponibilitaPartita(codPartita); //se la partita è presente sul file.json la carico
    	boolean flagPartitaNuova=true;
    	if(p==null) {
    		/*if(codPartita.equals(p.getCodice())) {
    			this.prt = p;
    			System.out.println("nice1");
    		}else if(this.prt!=null)*/
    		if(this.prt!=null){//controllo che venga creata una partita per poterne confrontare il codice
    			if(codPartita.equals(this.prt.getCodice())) {
    				if(this.prt.getElencoGiocatori().size()>1) {
    					avviaPartita(flagPartitaNuova);
    				}else {
        				lblCodPartitaErrato.setText("la partita ha un solo giocatore, perchè si è già conclusa");
    				}
    			}else {
    				lblCodPartitaErrato.setText("errore il codice partita è sbagliato, inseriscine uno corretto");
    			}
    		}else {
				lblCodPartitaErrato.setText("errore il codice partita è sbagliato, inseriscine uno corretto");
    		}
    	}else {
    		//inizializzo la partita e la avvio
    		this.prt=p;
    		flagPartitaNuova=false;
    		avviaPartita(flagPartitaNuova);
    	}

    }
    
    
    //METODI AUSILIARI PER IL PASSAGGIO DEI DATI IN FASE DI RUN-TIME
    //metodo che passa i dati della partita in fase di run-time da un istanza della classe controller all'altra
    public void copiaInformazioniPartita(Partita tempPrt) {
    	this.prt=tempPrt;
	}    
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
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
		            TextFlow textFlow = new TextFlow();
		            Text text = new Text(item);
		            text.setStyle("-fx-fill: black; -fx-font-size: 14px;"); // Imposta lo stile del testo
		            textFlow.getChildren().add(text);

		            // Imposta l'allineamento del testo a giustificato
		            textFlow.setTextAlignment(TextAlignment.JUSTIFY);
		            textFlow.setLineSpacing(5); // Regola lo spaziamento tra le righe se necessario

		            setGraphic(textFlow);
		        }
		    }
		});
		//mostro in output la classifica
		lstClassifica.getItems().setAll(listaNumerata);
    }
    
    private void avviaPartita(Boolean flagPartitaNuova) {
		//chiudo la finestra di home e apro quella di gioco
		Stage stage = (Stage)btnGioca.getScene().getWindow();
		stage.close();
		
		//apro la finestra di gioco
		BorderPane root = new BorderPane();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Partita.fxml"));
			//Locale locale = new Locale(); "Partita",this.prt
			//loader.setResources(ResourceBundle.getBundle("Partita",));
			root = loader.load();
			ControllerPartita controller = loader.getController();
			//definisco chi giocherà il primo turno
			Giocatore gio = this.prt.getElencoGiocatori().get(countTurnoGiocatore);
			lblTurnoGiocatore = new Label("è il turno di: "+gio.getNome());
			Scene interfacciaDiGioco = new Scene(root);
			stage.setScene(interfacciaDiGioco);
			stage.show();
			
//			if(flagPartitaNuova) {
//				//do le carte a ogni giocatore
//				mazzo.mescola();
//				numeroCarteAGiocatore=quanteCarteAGiocatore(prt.getElencoGiocatori().size());
//				for(Giocatore g : this.prt.getElencoGiocatori()) {
//					g.setCarteMano(mazzo.pescaCarte(numeroCarteAGiocatore));
//				}
//			}
			
			//copio le informazioni relative alla partita in corso
			controller.copiaInformazioniPartita(prt);
			//copio le informazioni relative alla label lblTurnoGiocatore
			controller.copiaInformazioniLabel(lblTurnoGiocatore);
			//copio le informazioni relative al numero di carte per la mano corrente 
			controller.copiaInformazioniNumCarte(numeroCarteAGiocatore);

			//se il prossimo giocatore che deve giocare è un bot lo avvio
			if(gio instanceof Bot) {
				isLocked = true;
				Bot b = (Bot)gio;
				b.giocaTurno(root, this.prt);
				Thread t = new Thread(b);
				Platform.runLater(t);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    //    private int quanteCarteAGiocatore(int numeroGiocatori) {
    //    	if(numeroGiocatori>4) {
    //    		return 5;
    //    	}else if(numeroGiocatori == 2){
    //    		return 1;
    //    	}else {
    //    		return numeroGiocatori;
    //    	}
    //    }

    private Partita verificaDisponibilitaPartita(String codicePartita) {
    	if(this.prt!=null) {
    		if(!this.prt.getCodice().equals(codicePartita)) {
    			return CaricaPartita(codicePartita);
    		}
    	}else {
    		return CaricaPartita(codicePartita);
    	}
    		
    	return null;
    }
    
    private Partita CaricaPartita(String codicePartita) {
		try {
	    	Gson gson = new Gson();
			ArrayList<Partita> elencoPartite = new ArrayList<Partita>();
			Partita prtTrovata=null;
			String path="src/SalvataggioPartite.json";
			FileReader fr = new FileReader(path);
			JsonReader jsnReader=new JsonReader(fr);

			if(jsnReader.peek() != JsonToken.NULL){
				jsnReader.beginArray();
				//carico il contenuto del file
				while(jsnReader.hasNext()) {
					Partita p = gson.fromJson(jsnReader, Partita.class);
					if(p.getCodice().equals(codicePartita)) {
						prtTrovata=p;
					}else {
						elencoPartite.add(p);
					}
				}
				jsnReader.endArray();
				jsnReader.close();
			}
			return prtTrovata;
		} catch (FileNotFoundException fnfe) {
			// TODO Auto-generated catch block
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			// TODO Auto-generated catch block
			ioe.printStackTrace();
		}
		return null;
    }
}