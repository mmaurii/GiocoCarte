package basic;

import java.awt.Desktop;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;

public class ControllerHome {
	//variabili di controllo
	final int lungCodicePartita=10;
	final int nViteDefault=5;
	Partita prt;
	Torneo trn;
	Mazzo mazzo = new Mazzo();
	ArrayList<Giocatore> giocatoriPrt = new ArrayList<Giocatore>();
	final String pathClassifica = "Documenti/Classifica.txt";
	final String pathSalvataggioPartite = "Documenti/SalvataggioPartite.json";
	final String pathSalvataggioTornei = "Documenti/SalvataggioTornei.json";
	final String telegramWebSite = "https://t.me/GiocoSpaccaBot";
	
	//eventi FXML
	@FXML private TextField txtUsername;
	@FXML private PasswordField txtPassword;
	@FXML private Button btnLogin;
	@FXML TableView<LineClassifica> tblClassifica;
	@FXML TableColumn<LineClassifica, Integer> rankingTblClassifica;
	@FXML TableColumn<LineClassifica, Integer> ptTblClassifica;
	@FXML TableColumn<LineClassifica, String> nomiTblClassifica;
	@FXML Label lblTurnoGiocatore;
	@FXML Button btnGioca;
	@FXML TextField txtCod;
	@FXML Label lblCodErrato;
	@FXML ListView<String> lstViewVite;
	@FXML Label lblAccessoErrato;
	@FXML ImageView ImgTelegramBot;

	/**
	 * login per il menu
	 * @param actionEvent
	 */
	@FXML public void loginAction(ActionEvent actionEvent) {
		lblAccessoErrato.setVisible(false);
		String username_text = txtUsername.getText();
		String password_text = txtPassword.getText();
		Amministratore a = new Amministratore(username_text, password_text);
		//controllo nonme utente e password
		if(a.verificaAdmin())
		{
			//se la password è corretta chiudo la finestra di home
			Stage stage = (Stage)btnLogin.getScene().getWindow();
			stage.close();
			//apro la finestra delle impostazioni e creazine partite e tornei(menu)
			try {

				MediaPlayer currentMediaPlayer = VideoBackgroundPane.getCurrentMediaPlayer();
				if (currentMediaPlayer != null) {
					currentMediaPlayer.stop();
				}
				VideoBackgroundPane videoBackgroundPane = new VideoBackgroundPane("/v1.mp4");
				Thread.sleep(300);
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
				Parent root = loader.load();
				loader.getController();
				
				StackPane stackPane = new StackPane();
				stackPane.setStyle("-fx-background-color: #38B6FF;");//imposto un colore di background
				stackPane.getChildren().addAll(videoBackgroundPane, root);
				stage.setTitle("Gestione Funzionalità");
				Scene interfacciaPartitaTorneo = new Scene(stackPane, 600, 400);
				//avvio la nuova interfaccia
				stage.setScene(interfacciaPartitaTorneo);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else {//avviso che le credenziali di accesso sono errate
			lblAccessoErrato.setVisible(true);
			lblAccessoErrato.setText("nome e/o password errati");
		}
	}

	/**
	 * controlla il codice della partita o del torneo e la/o avvia
	 * @param actionEvent
	 */
	@FXML public void gioca(ActionEvent actionEvent) {
		//ottengo il codice inserito dall'utente
		String cod = txtCod.getText();

		//controllo se il codice è un codice partita o torneo
		if(cod!="") {
			if(cod.charAt(0)=='t') {
				Torneo t = verificaDisponibilitaTorneo(cod); //assegno la partita appena creata se no la cerco sul file.json e la carico o null se non si trova 
				if(t==null) {
					//se il torneo non viene trovato mando un messaggio di errore
					lblCodErrato.setText("errore il codice torneo è sbagliato, inseriscine uno corretto");
				}else {
					this.trn=t;

					//controllo che il torneo non sia già stato concluso
					if(this.trn.getVincitore()==null) {
						avviaTorneo();
					}else {
						lblCodErrato.setText("il torneo si è già concluso");
					}
				}
			}else {
				Partita p = verificaDisponibilitaPartita(cod); //assegno la partita appena creata se no la cerco sul file.json e la carico o null se non si trova 
				if(p==null) {
					//se la partita non viene trovata mando un messaggio di errore
					lblCodErrato.setText("errore il codice partita è sbagliato, inseriscine uno corretto");
				}else {
					this.prt=p;

					//controllo che la partita non sia già stata conclusa
					if(this.prt.getElencoGiocatori().size()>1) {
						avviaPartita();
					}else {
						lblCodErrato.setText("la partita ha un solo giocatore, perchè si è già conclusa");
					}
				}
			}
		}else {
			lblCodErrato.setText("inserisci un codice");
		}
	}
	
	@FXML public void apriTelegram(MouseEvent mouseEvent) {
		//apro una tab del browser predefinito che rimanda al bot
		try {
			Desktop.getDesktop().browse(new URI(telegramWebSite));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	//METODI AUSILIARI PER IL PASSAGGIO DEI DATI IN FASE DI RUN-TIME
	/**
	 * carica i dati dal file di testo della classifica 'classifica.txt'
	 * e li visualizza in una TableView ordinati per miglior punteggio
	 */
	public void caricaClassifica() {
		ArrayList<LineClassifica> row = new ArrayList<>();
		try {
			File file = new File(pathClassifica);
			Scanner scan = new Scanner(file);	
			while(scan.hasNext()) {//carico i dati dal file di testo
				String line = scan.nextLine();
				String[] lineItems = line.split(" , ");
				//salvo i dati caricati 
				row.add(new LineClassifica(Integer.parseInt(lineItems[0]), lineItems[1]));
			}
			scan.close();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}

		//ordino in base al punteggio la lista
		row.sort(null);	
		// Associazione delle ObservableList alle TableColumn
		nomiTblClassifica.setCellValueFactory(new Callback<CellDataFeatures<LineClassifica, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<LineClassifica, String> cell) {
				// cell.getValue() returns the LineClassifica instance for a particular TableView row
				return cell.getValue().nomeProperty();
			}
		});

		rankingTblClassifica.setCellValueFactory(new Callback<CellDataFeatures<LineClassifica, Integer>, ObservableValue<Integer>>() {
			public ObservableValue<Integer> call(CellDataFeatures<LineClassifica, Integer> cell) {
				// cell.getValue() returns the LineClassifica instance for a particular TableView row
				return cell.getValue().rankingProperty().asObject();
			}
		});

		ptTblClassifica.setCellValueFactory(new Callback<CellDataFeatures<LineClassifica, Integer>, ObservableValue<Integer>>() {
			public ObservableValue<Integer> call(CellDataFeatures<LineClassifica, Integer> cell) {
				// cell.getValue() returns the LineClassifica instance for a particular TableView row
				return cell.getValue().puntiProperty().asObject();
			}
		});

		// Aggiunta dei dati alla TableView
		int counter=1;
		for(LineClassifica i : row) {
			//numero gli utenti in ordine di punteggio
			i.setRanking(counter);
			tblClassifica.getItems().add(i);
			counter++;
		}

		//metto il contenuto della tableview in grassetto
		tblClassifica.setStyle("-fx-font-weight: bold;");
	}


	/**
	 * avvio la partita
	 */
	private void avviaPartita() {
		//chiudo la finestra di home e apro quella di gioco
		Stage stage = (Stage)btnGioca.getScene().getWindow();
		stage.close();

		//apro la finestra di gioco
		StackPane root = new StackPane();
		try {
			MediaPlayer currentMediaPlayer = VideoBackgroundPane.getCurrentMediaPlayer();
			if (currentMediaPlayer != null) {
				currentMediaPlayer.stop();
			}
			
			FXMLLoader loader;
			loader = new FXMLLoader(getClass().getResource("Partita.fxml"));

			ResourceBundle rb = new ResourceBundle() {
				@Override
				protected Object handleGetObject(String key) {//risorse utilizzate per inizializzare il nuovo controller
					if (key.equals("Partita")) return prt;
					return null;
				}
				@Override
				public Enumeration<String> getKeys() {
					return Collections.enumeration(keySet());
				}
			};
			loader.setResources(rb);

			root = loader.load();
			ControllerPartita controller = loader.getController();
			//definisco chi giocherà il primo turno
			Giocatore gio = this.prt.getGiocatoreCorrente();
			lblTurnoGiocatore = new Label("è il turno di: "+gio.getNome());
			Scene interfacciaDiGioco = new Scene(root);
			stage.setScene(interfacciaDiGioco);
			stage.setTitle("Partita");
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {//controllo l'evento di chiusura dello stage
				@Override
				public void handle(WindowEvent e) {
					Platform.exit();
					if(stage.getScene().equals(interfacciaDiGioco)) {//in questo modo controllo di salvare la partita solo quando esco dall'interfaccia di gioco
						controller.SalvaPartita(controller.prt);
					}
					System.exit(0);
				}
			});

			stage.show();

			//copio le informazioni relative alla label lblTurnoGiocatore
			controller.copiaInformazioniLabel(lblTurnoGiocatore);

			//controllo sei il giocatore è un bot
			if(gio instanceof Bot) {
				Bot b = (Bot)gio;
				controller.t = new Thread(b);			
				controller.t.setDaemon(true);
				Platform.runLater(controller.t);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * dato il codice di una partita il metodo controlla se è presente nel salvataggio.
	 * Se è presente la partita ritorno la partita se no ritorno null
	 * @param codicePartita
	 * @return partita
	 * 
	 */
	private Partita verificaDisponibilitaPartita(String codicePartita) {
		if(this.prt!=null) {//controllo che la partita sia stata creata
			if(!this.prt.getCodice().equals(codicePartita)) {//se il codice della partita non è di quella appena inserita la carico da file
				return CaricaPartita(codicePartita);
			}
			return this.prt;
		}else {//provo a caricarla da file
			return CaricaPartita(codicePartita);
		}
	}


	/**
	 * se trovo una partita associata al codice nel salvataggio la carico se no ritorno null
	 * @param codicePartita
	 * @return partita
	 */
	private Partita CaricaPartita(String codicePartita) {
		try {
			GsonBuilder gsonBuilder = new GsonBuilder();
			//imposto un TypeAdapter per salvare correttamente l'elenco dei giocatori che contiene sia Bot che Giocatori
			gsonBuilder.registerTypeAdapter(new TypeToken<ArrayList<Giocatore>>() {}.getType(), new ElencoGiocatoriTypeAdapter());
			Gson gson=gsonBuilder.create();
			ArrayList<Partita> elencoPartite = new ArrayList<Partita>();
			Partita prtTrovata=null;
			
			File file = new File(pathSalvataggioPartite);
			
			FileReader fr = new FileReader(file);
			JsonReader jsnReader=new JsonReader(fr);

			if(jsnReader.peek() != JsonToken.NULL){
				jsnReader.beginArray();
				//carico il contenuto del file
				while(jsnReader.hasNext()) {
					Partita p = gson.fromJson(jsnReader, Partita.class);
					if(p.getCodice().equals(codicePartita)) {
						//mi salvo la partita richiesta
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
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}

	/**
	 * avvio il torneo
	 */
	private void avviaTorneo() {
		//chiudo la finestra di home e apro quella di gioco
		Stage stage = (Stage)btnGioca.getScene().getWindow();
		stage.close();

		//apro la finestra di gioco
		StackPane root = new StackPane();
		try {
			MediaPlayer currentMediaPlayer = VideoBackgroundPane.getCurrentMediaPlayer();
			if (currentMediaPlayer != null) {
				currentMediaPlayer.stop();
			}
			
			FXMLLoader loader;
			loader = new FXMLLoader(getClass().getResource("Torneo.fxml"));

			ResourceBundle rb = new ResourceBundle() {
				@Override
				protected Object handleGetObject(String key) {//risorse utilizzate per inizializzare il nuovo controller
					if (key.equals("Torneo")) return trn;
					return null;
				}
				@Override
				public Enumeration<String> getKeys() {
					return Collections.enumeration(keySet());
				}
			};
			loader.setResources(rb);

			root = loader.load();
			ControllerTorneo controller = loader.getController();
			Scene interfacciaDiGioco = new Scene(root);
			stage.setResizable(false);
			stage.setScene(interfacciaDiGioco);
			stage.setTitle("Torneo");
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent e) {
					Platform.exit();
					if(stage.getScene().equals(interfacciaDiGioco)) {//in questo modo controllo di salvare la partita solo quando esco dall'interfaccia di gioco
						controller.SalvaTorneo(controller.trn);
					}
					System.exit(0);
				}
			});

			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	/**
	 * dato il codice di un torneo il metodo controlla se è presente nel salvataggio.
	 * Se è presente il torneo ritorno il torneo se no ritorno null
	 * @param codiceTorneo
	 * @return torneo
	 * 
	 */
	private Torneo verificaDisponibilitaTorneo(String codiceTorneo) {
		if(this.trn!=null) {//controllo che il torneo sia stato creato
			if(!this.trn.getCodice().equals(codiceTorneo)) {//se il codice del torneo non è di quello appena creato provo a caricarlo da file
				return CaricaTorneo(codiceTorneo);
			}
			return this.trn;
		}else {//provo a caricarla da file
			return CaricaTorneo(codiceTorneo);
		}
	}

	/**
	 * se trovo un torneo associato al codice nel salvataggio lo carico se no ritorno null
	 * @param codiceTorneo
	 * @return Torneo
	 */
	private Torneo CaricaTorneo(String codiceTorneo) {
		try {
			GsonBuilder gsonBuilder = new GsonBuilder();
			//imposto un TypeAdapter per salvare correttamente l'elenco dei giocatori che contiene sia Bot che Giocatori
			gsonBuilder.registerTypeAdapter(new TypeToken<ArrayList<Giocatore>>() {}.getType(), new ElencoGiocatoriTypeAdapter());
			Gson gson=gsonBuilder.create();
			ArrayList<Torneo> elencoTornei = new ArrayList<Torneo>();
			Torneo trnTrovato=null;
			
			File file = new File(pathSalvataggioTornei);
			
			FileReader fr = new FileReader(file);
			JsonReader jsnReader=new JsonReader(fr);

			if(jsnReader.peek() != JsonToken.NULL){
				jsnReader.beginArray();
				//carico il contenuto del file
				while(jsnReader.hasNext()) {
					Torneo t = gson.fromJson(jsnReader, Torneo.class);
					if(t.getCodice().equals(codiceTorneo)) {
						//mi salvo la partita richiesta
						trnTrovato=t;
					}else {
						elencoTornei.add(t);
					}
				}
				jsnReader.endArray();
				jsnReader.close();
			}

			return trnTrovato;
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}
}