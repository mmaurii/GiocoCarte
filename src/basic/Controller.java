package basic;

import java.io.*;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.util.Scanner;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class Controller {
	int numeroCarteAGiocatore=5;
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
        	//se la password è corretta chiudo la finestra di login
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
    
    
    @FXML Button btnCreaPartita;
    @FXML ComboBox<String> comboNVite;
    //crea partita
    @FXML public void CreaPartitaAction(ActionEvent actionEvent) {
    	//chiudo la finestra di scelta per la creazione di partite o tornei
    	Stage stage = (Stage)btnCreaPartita.getScene().getWindow();
    	stage.close();
    	
    	//apro la finestra per la creazine delle partite
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("CreaPartita.fxml"));
			stage.setTitle("Crea una Partita");
			Scene interfacciaCreaPartita = new Scene(root);
			stage.setScene(interfacciaCreaPartita);
			stage.show();
			/*
			//imposto le opzioni della ComboBox
			ObservableList<String> comboItems = FXCollections.observableArrayList("1","2","3","4","5");
			comboNVite.setTooltip(new Tooltip());
			comboNVite.getItems().addAll(comboItems);*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
    
    
    //aggiungo alla partita un utente  
    @FXML ListView<String> listUtentiPartita;
    @FXML Button btnAggiungiUtente;
    @FXML TextField txtNomeUtente; 
    ArrayList<Giocatore> giocatoriPrt = new ArrayList<Giocatore>();
    @FXML public void AggiungiUtente(ActionEvent actionEvent) {
    	String nome = txtNomeUtente.getText();
    	//controllo che non vengano inseriti giocatori con lo stesso nome
    	if(!listUtentiPartita.getItems().contains(nome)) {
    		txtNomeUtente.clear();
    		listUtentiPartita.getItems().add(nome);
    		giocatoriPrt.add(new Giocatore(nome));
    	}else {
    		txtNomeUtente.clear();
    	}
    }

    
    //Genero il codice per una nuova partita
    @FXML Button btnGeneraCodice;
    @FXML Label lblCodice;
    final int lungCodicePartita=10;
    final int nViteDefault=5;
    Partita prt;
    Mazzo mazzo = new Mazzo();
    @FXML public void GeneraCodice(ActionEvent actionEvent) {
    	try {
    		File file = new File("src/Status.txt");
    		Scanner scan = new Scanner(file);//controlla errori legati alla lettura e scrittura del file
    		String codPartita = scan.nextLine().split(" , ")[1];
    		scan.close();
    		
    		//String unicoID = UUID.randomUUID().toString();
    		//controllare univocita
    		codPartita = Integer.toString(Integer.parseInt(codPartita)+1);

    		//aggiungo al codice gli 0 non rilevanti
    		int nCifre = codPartita.length();
    		for(int i=0; i<lungCodicePartita-nCifre; i++) {
    			codPartita="0"+codPartita;
    		}

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
    		//errato inserimento dei dati    	
    		prt=new Partita(codPartita, giocatoriPrt);
    	}catch(FileNotFoundException e) {
    		System.out.println(e);
    	}catch(IOException eIO) {
    		System.out.println(eIO);    		
    	}
    }

    
    @FXML Button btnTornaAlLogin;
    //torno alla Schermata di login
    @FXML public void TornaAlLogin(ActionEvent actionEvent) {
    	//chiudo la finestra di di creazione della partita e torno alla finestra di login
    	Stage stage = (Stage)btnTornaAlLogin.getScene().getWindow();
    	stage.close();
    	
    	//riapro la finestra di login
		//Parent root;
		try {
			//root = FXMLLoader.load(getClass().getResource("Login.fxml"));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
			Parent root = loader.load();
			Controller controller = loader.getController();
			Scene interfacciaLogin = new Scene(root);
			controller.copiaInformazioni(prt);

			stage.setScene(interfacciaLogin);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    }

    //metodo che passa i dati in fase di run-time da un istanza della classe all'altra
    private void copiaInformazioni(Partita tempPrt) {
    	this.prt=tempPrt;
	}


	//avvio l'interfaccia di gioco
    @FXML Button btnGioca;
    @FXML TextField txtCodPartita = new TextField();
    @FXML Label lblCodPartitaErrato;
    @FXML Label lblTurnoGiocatore;
    @FXML public void avviaPartita(ActionEvent actionEvent) {
    	//System.out.print(this.prt.getElencoGiocatori().remove(0).getNome());
    	//ottengo il codice partita inserito dall'utente
    	String cod = txtCodPartita.getText();
    	if(prt!=null)//controllo che venga creata una partita per poterne confrontare il codice
    		if(cod.equals(this.prt.getCodice())) {
    			//chiudo la finestra di login e apro quella di gioco
    			Stage stage = (Stage)btnGioca.getScene().getWindow();
    			stage.close();

    			//apro la finestra di gioco
    			Group root = new Group();
    			try {
    				//root = FXMLLoader.load(getClass().getResource("Partita.fxml"));
    				FXMLLoader loader = new FXMLLoader(getClass().getResource("Partita.fxml"));
    				root = loader.load();
    				Controller controller = loader.getController();
    				//definisco chi giocherà il primo turno
        			lblTurnoGiocatore = new Label("è il turno di: "+this.prt.getElencoGiocatori().get(0).getNome());
        			lblTurnoGiocatore.setTextFill(Color.BLACK);
        			lblTurnoGiocatore.setFont(Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 24));
        			root.getChildren().add(lblTurnoGiocatore);
        			//lblTurnoGiocatore.setAlignment(Pos.CENTER);
    				Scene interfacciaDiGioco = new Scene(root);
    				controller.copiaInformazioni(prt);
    				stage.setScene(interfacciaDiGioco);
    				stage.show();
        			lblTurnoGiocatore.setTranslateX(190);
        			lblTurnoGiocatore.setTranslateY(220);
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    			
    			//do le carte a ogni giocatore
    	    	mazzo.mescola();
    	    	for(Giocatore g : prt.getElencoGiocatori()) {
    	    		g.setCarteMano(mazzo.pescaCarte(numeroCarteAGiocatore));
    	    	}
    		}else {
    			lblCodPartitaErrato.setText("errore il codice partita è sbagliato");
    		}
    }
    
    
    @FXML Label lblManoGiocatore;
    @FXML Button btnInizioTurnoGiocatore;
    @FXML ImageView imgCarta1;
    @FXML ImageView imgCarta2;
    @FXML ImageView imgCarta3;
    @FXML ImageView imgCarta4;
    @FXML ImageView imgCarta5;
    
    int countGiocatore=0;
   // private void copiaInformazioniLabel()
    @FXML public void inizioTurnoGiocatore(ActionEvent actionEvent) {

    	//lblTurnoGiocatore.setVisible(false);
    	lblManoGiocatore.setVisible(true);
    	btnInizioTurnoGiocatore.setDisable(true);
    	ArrayList<ImageView> outputCarte = new ArrayList<ImageView>(Arrays.asList(imgCarta1, imgCarta2, imgCarta3, imgCarta4, imgCarta5));
    	//mostro le carte in output relative al giocatore del turno corrente
    	for(Carta c : prt.getElencoGiocatori().get(countGiocatore).getCarteMano()) {
    		for(ImageView img : outputCarte) {
    			//System.out.println(c.getPercorso());
    			//per windows è necessaria la stringa file:/// all'inizio del percorso in quanto è un URI
    			img.setImage(new Image("file:///src/basic/IMGcarte/33.jpg"));
    		}
    	}//getClass().getResource("src/basic/IMGcarte/33.jpg").toURI().toString()

    }
    
    
    @FXML public void GiocaCarta1(ActionEvent actionEvent) {
    	
    }    
    
    
    @FXML public void GiocaCarta2(ActionEvent actionEvent) {
    	
    }    
    
    
    @FXML public void GiocaCarta3(ActionEvent actionEvent) {
    	
    }    
    
    
    @FXML public void GiocaCarta4(ActionEvent actionEvent) {
    	
    }    
    
    
    @FXML public void GiocaCarta5(ActionEvent actionEvent) {
    	
    }

    

}