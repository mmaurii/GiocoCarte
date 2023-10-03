package basic;

import java.io.*;
import java.net.URL;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import java.util.*;
import com.google.gson.Gson;
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

public class ControllerHome implements Initializable{
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
			Parent root;
			try {
				root = FXMLLoader.load(getClass().getResource("PartitaTorneo.fxml"));
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
    	Partita p = CaricaPartita(codPartita);
    	if(p==null) {
    		/*if(codPartita.equals(p.getCodice())) {
    			this.prt = p;
    			System.out.println("nice1");
    		}else if(this.prt!=null)*/
    		if(prt!=null){//controllo che venga creata una partita per poterne confrontare il codice
    			if(codPartita.equals(this.prt.getCodice())) {
    				avviaPartita();
    			}else {
    				lblCodPartitaErrato.setText("errore il codice partita è sbagliato,\ninseriscine uno corretto");
    			}
    		}else {
				lblCodPartitaErrato.setText("errore il codice partita è sbagliato,\ninseriscine uno corretto");
    		}
    	}else {
    		//inizializzo la partita e la avvio
    		this.prt=p;
    		avviaPartita();
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

    private int quanteCarteAGiocatore(int numeroGiocatori) {
    	if(numeroGiocatori>4) {
    		return 5;
    	}else if(numeroGiocatori == 2){
    		return 1;
    	}else {
    		return numeroGiocatori;
    	}
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
		
		lstClassifica.getItems().sort(Comparator.reverseOrder());
    }
    
    private void avviaPartita() {
		//chiudo la finestra di home e apro quella di gioco
		Stage stage = (Stage)btnGioca.getScene().getWindow();
		stage.close();
		
		//apro la finestra di gioco
		Group root = new Group();
		try {
			//root = FXMLLoader.load(getClass().getResource("Partita.fxml"));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Partita.fxml"));
			root = loader.load();
			ControllerPartita controller = loader.getController();
			//definisco chi giocherà il primo turno
			lblTurnoGiocatore = new Label("è il turno di: "+this.prt.getElencoGiocatori().get(countTurnoGiocatore).getNome());
			lblTurnoGiocatore.setTextFill(Color.BLACK);
			lblTurnoGiocatore.setFont(Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 24));
			lblTurnoGiocatore.setId("lblTurnoGiocatore");
			root.getChildren().add(lblTurnoGiocatore);
			Scene interfacciaDiGioco = new Scene(root);
			stage.setScene(interfacciaDiGioco);
			stage.show();
			lblTurnoGiocatore.setTranslateX(190);
			lblTurnoGiocatore.setTranslateY(220);

			//do le carte a ogni giocatore
			mazzo.mescola();
			numeroCarteAGiocatore=quanteCarteAGiocatore(prt.getElencoGiocatori().size());
			for(Giocatore g : this.prt.getElencoGiocatori()) {
				g.setCarteMano(mazzo.pescaCarte(numeroCarteAGiocatore));
			}

			//copio le informazioni relative alla partita in corso
			controller.copiaInformazioniPartita(prt);
			//copio le informazioni relative alla label lblTurnoGiocatore
			controller.copiaInformazioniLabel(lblTurnoGiocatore);
			//copio le informazioni relative al numero di carte per la mano corrente 
			controller.copiaInformazioniNumCarte(numeroCarteAGiocatore);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    //controlla
    /*
    private void SalvaPartita(Partita partita) {
    	try {
    		String path="src/SalvataggioPartite.txt";
    		FileOutputStream file = new FileOutputStream(path);
    		ObjectOutputStream objOutput = new ObjectOutputStream(file);
    		objOutput.writeObject(partita);
    		objOutput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private Partita CaricaPartita(String codicePartita) {
    	try {
    		String path="src/SalvataggioPartite.txt";
    		FileInputStream file = new FileInputStream(path);
    		ObjectInputStream objInput = new ObjectInputStream(file);
    		//objInput.setObjectInputFilter(objInput.getObjectInputFilter());
    		Partita partita=new Partita();
    		while(true) {
    		    try {
    	    		partita= (Partita) objInput.readObject();
    	    		if(partita.getCodice().equals(codicePartita)) {
    	    			System.out.println("nice");
    	    		}
    		    } catch (EOFException e) {
    		         // end of file reached
    				//e.printStackTrace();
	    			//System.out.println("male");
    	    		objInput.close();
    	    		return partita;

    		    }
    		}    		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }*/
    
	private Partita CaricaPartita(String codicePartita) {
		try {
			Gson gson = new Gson();
			ArrayList<Partita> elencoPartite = new ArrayList<Partita>();
			Boolean presenzaPrt=false;
			Partita prtTrovata=null;
			String path="src/SalvataggioPartite.json";
			File file = new File(path);
			Scanner scan;
			scan = new Scanner(file);

			scan.useDelimiter("%");

			//carico il contenuto del file
			while(scan.hasNext()) {
				String dati =scan.next();
				Partita p = gson.fromJson(dati, Partita.class);
				if(p.getCodice().equals(codicePartita)) {
					prtTrovata=p;
				}else {
					elencoPartite.add(p);
				}
			}
			scan.close();
			
			//verifico che la partita cercata sia stata trovata
			if(prtTrovata!=null) {
				FileWriter fw = new FileWriter(file);

				for(Partita p : elencoPartite) {
					String datiGson = gson.toJson(p);
					fw.write(datiGson+"%");
				}
				fw.close();
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