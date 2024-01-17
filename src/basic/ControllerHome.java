package basic;

import java.io.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import java.util.*;

import org.w3c.dom.ls.LSInput;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;


public class ControllerHome {
	//variabili di controllo
	final int lungCodicePartita=10;
	final int nViteDefault=5;
	Partita prt;
	Torneo trn;
	Mazzo mazzo = new Mazzo();
	ArrayList<Giocatore> giocatoriPrt = new ArrayList<Giocatore>();
	String pathRetroCarta = "/basic/IMGcarte/retro.jpg";
	String pathClassifica = "src/Classifica.txt";
	String pathStatus = "src/Status.txt";

	//eventi FXML
	@FXML private TextField txtUsername;
	@FXML private PasswordField txtPassword;
	@FXML private Button btnLogin;
	@FXML TableView<Line> tblClassifica;
	@FXML TableColumn<Line, Integer> rankingTblClassifica;
	@FXML TableColumn<Line, Integer> ptTblClassifica;
	@FXML TableColumn<Line, String> nomiTblClassifica;
	@FXML Label lblTurnoGiocatore;
	@FXML Button btnGioca;
	@FXML TextField txtCod;
	@FXML Label lblCodErrato;
	@FXML ListView<String> lstViewVite;

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
				VideoBackgroundPane videoBackgroundPane = new VideoBackgroundPane("src/v1.mp4");

				FXMLLoader loader = new FXMLLoader(getClass().getResource("PartitaTorneo.fxml"));
				Parent root = loader.load();

				StackPane stackPane = new StackPane();
				stackPane.setStyle("-fx-background-color: #38B6FF;");
				stackPane.getChildren().addAll(videoBackgroundPane, root);

				stage.setTitle("Menu");
				Scene interfacciaPartitaTorneo = new Scene(stackPane, 600, 400);


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

	@FXML public void gioca(ActionEvent actionEvent) {
		//ottengo il codice inserito dall'utente
		String cod = txtCod.getText();

		//controllo se il codice è un codice partita o torneo
		if(cod!=null) {
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
		}
	}

	@FXML public void aggiungiAmministratore(MouseEvent mouseEvent) 
	{
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("popUpAmministratori.fxml"));
            Parent root = loader.load();

			ControllerPopUpAmministratori controller = loader.getController();
			controller.populateLst();

			Stage stage = new Stage();
			stage.setTitle("Amministratori");
			Scene scene = new Scene(root);
			stage.setHeight(450);
		    stage.setWidth(500);
			stage.setScene(scene);
			stage.show();			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//METODI AUSILIARI PER IL PASSAGGIO DEI DATI IN FASE DI RUN-TIME
	//metodo che passa i dati della partita in fase di run-time da un istanza della classe controller all'altra
	public void copiaInformazioniPartita(Partita tempPrt) {
		this.prt=tempPrt;
	}

	public void copiaInformazioniTorneo(Torneo tempT) {
		this.trn=tempT;
	} 

	public void caricaClassifica() {
		ArrayList<Line> row = new ArrayList<>();
		try {
			File file = new File(pathClassifica);
			Scanner scan = new Scanner(file);	
			while(scan.hasNext()) {//carico i dati dal file di testo
				String line = scan.nextLine();
				String[] lineItems = line.split(" , ");
				row.add(new Line(Integer.parseInt(lineItems[0]), lineItems[1]));
			}
			scan.close();
		} catch (FileNotFoundException fnfe) {
			// TODO Auto-generated catch block
			fnfe.printStackTrace();
		}

		//ordino in base al punteggio la lista
		row.sort(null);	
        // Associazione delle ObservableList alle TableColumn
		nomiTblClassifica.setCellValueFactory(new Callback<CellDataFeatures<Line, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Line, String> cell) {
				// p.getValue() returns the Person instance for a particular TableView row
				return cell.getValue().nomeProperty();
			}
		});

		rankingTblClassifica.setCellValueFactory(new Callback<CellDataFeatures<Line, Integer>, ObservableValue<Integer>>() {
			public ObservableValue<Integer> call(CellDataFeatures<Line, Integer> cell) {
				// p.getValue() returns the Person instance for a particular TableView row
				return cell.getValue().rankingProperty().asObject();
			}
		});
		
		ptTblClassifica.setCellValueFactory(new Callback<CellDataFeatures<Line, Integer>, ObservableValue<Integer>>() {
			public ObservableValue<Integer> call(CellDataFeatures<Line, Integer> cell) {
				// p.getValue() returns the Person instance for a particular TableView row
				return cell.getValue().puntiProperty().asObject();
			}
		});

		// Aggiunta dei dati alla TableView
		int counter=1;
		for(Line i : row) {
			i.setRanking(counter);
			tblClassifica.getItems().add(i);
			counter++;
		}

		//metto il contenuto della tableview in grassetto
		tblClassifica.setStyle("-fx-font-weight: bold;");
	}

	private void avviaPartita() {
		//chiudo la finestra di home e apro quella di gioco
		Stage stage = (Stage)btnGioca.getScene().getWindow();
		stage.close();

		//apro la finestra di gioco
		BorderPane root = new BorderPane();
		try {
			FXMLLoader loader;
			loader = new FXMLLoader(getClass().getResource("Partita.fxml"));

			ResourceBundle rb = new ResourceBundle() {
				@Override
				protected Object handleGetObject(String key) {
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
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
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

			//copio le informazioni relative alla partita in corso
			//controller.copiaInformazioniPartita(prt);
			//copio le informazioni relative alla label lblTurnoGiocatore
			controller.copiaInformazioniLabel(lblTurnoGiocatore);

			if(gio instanceof Bot) {
				Bot b = (Bot)gio;
				controller.t = new Thread(b);			
				controller.t.setDaemon(true);
				Platform.runLater(controller.t);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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

	private Partita CaricaPartita(String codicePartita) {
		try {
			GsonBuilder gsonBuilder = new GsonBuilder();
			//imposto un TypeAdapter per salvare correttamente l'elenco dei giocatori che contiene sia Bot che Giocatori
			gsonBuilder.registerTypeAdapter(new TypeToken<ArrayList<Giocatore>>() {}.getType(), new ElencoGiocatoriTypeAdapter());
			Gson gson=gsonBuilder.create();
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
						//mi salvo la partita richiesta
						prtTrovata=p;
					}else {
						elencoPartite.add(p);
					}
				}
				jsnReader.endArray();
				jsnReader.close();
			}

			//salvo la lista di partite senza quella richiesta se trovata
			//			if(prtTrovata!=null) {
			//				FileWriter fw = new FileWriter(path);
			//				JsonWriter jsnWriter = new JsonWriter(fw);
			//				
			//				//rimuovo la partita richiesta dall'elenco e lo salvo
			//				elencoPartite.remove(prtTrovata);
			//				
			//				jsnWriter.beginArray();
			//				for (Partita p : elencoPartite) {
			//					gson.toJson(p, Partita.class, jsnWriter);
			//					fw.write('\n');
			//				}
			//				jsnWriter.endArray();
			//				jsnWriter.close();
			//			}
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

	private void avviaTorneo() {
		//chiudo la finestra di home e apro quella di gioco
		Stage stage = (Stage)btnGioca.getScene().getWindow();
		stage.close();

		//apro la finestra di gioco
		BorderPane root = new BorderPane();
		try {
			FXMLLoader loader;
			loader = new FXMLLoader(getClass().getResource("Torneo.fxml"));

			ResourceBundle rb = new ResourceBundle() {
				@Override
				protected Object handleGetObject(String key) {
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
			stage.setScene(interfacciaDiGioco);
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

			//copio le informazioni relative alla partita in corso
			//controller.copiaInformazioniPartita(prt);
			//copio le informazioni relative alla label lblTurnoGiocatore
			//			controller.copiaInformazioniLabel(lblTurnoGiocatore);

			//			if(gio instanceof Bot) {
			//				Bot b = (Bot)gio;
			//				controller.t = new Thread(b);			
			//				controller.t.setDaemon(true);
			//				Platform.runLater(controller.t);
			//			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

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
	
	private Torneo CaricaTorneo(String codiceTorneo) {
		try {
			GsonBuilder gsonBuilder = new GsonBuilder();
			//imposto un TypeAdapter per salvare correttamente l'elenco dei giocatori che contiene sia Bot che Giocatori
			gsonBuilder.registerTypeAdapter(new TypeToken<ArrayList<Giocatore>>() {}.getType(), new ElencoGiocatoriTypeAdapter());
			Gson gson=gsonBuilder.create();
			ArrayList<Torneo> elencoTornei = new ArrayList<Torneo>();
			Torneo trnTrovato=null;
			String path="src/SalvataggioTornei.json";
			FileReader fr = new FileReader(path);
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

			//salvo la lista di partite senza quella richiesta se trovata
			//			if(prtTrovata!=null) {
			//				FileWriter fw = new FileWriter(path);
			//				JsonWriter jsnWriter = new JsonWriter(fw);
			//				
			//				//rimuovo la partita richiesta dall'elenco e lo salvo
			//				elencoPartite.remove(prtTrovata);
			//				
			//				jsnWriter.beginArray();
			//				for (Partita p : elencoPartite) {
			//					gson.toJson(p, Partita.class, jsnWriter);
			//					fw.write('\n');
			//				}
			//				jsnWriter.endArray();
			//				jsnWriter.close();
			//			}
			return trnTrovato;
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