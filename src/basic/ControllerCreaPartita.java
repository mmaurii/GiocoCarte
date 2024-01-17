package basic;

import java.io.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ControllerCreaPartita {
	//variabili di controllo
	final int lungCodicePartita=10;
	final int nViteDefault=5;
	Partita prt;
	Mazzo mazzo = new Mazzo();
	ArrayList<Giocatore> giocatoriPrt = new ArrayList<Giocatore>();
	String pathRetroCarta = "/basic/IMGcarte/retro.jpg";
	String pathStatus = "src/Status.txt";
	String pathClassifica = "src/Classifica.txt";

	@FXML ListView<String> lstGiocatoriRegistrati;
	@FXML ListView<String> lstUtentiPartita;
	@FXML Button btnAggiungiUtente;
	@FXML TextField txtNomeUtente; 
	@FXML Button btnAggiungiUtenteRobot;
	@FXML TextField txtNomeUtenteRobot;
	@FXML Button btnGeneraCodice;
	@FXML TextField txtCodice;
	@FXML ComboBox<String> comboNVite;
	@FXML Button btnTornaAllaHome;
	@FXML Label lblErrore;

	//aggiungo alla partita un nuovo utente  
	@FXML public void AggiungiUtente(ActionEvent actionEvent) {
		String nome = txtNomeUtente.getText();
		//controllo che non vengano inseriti giocatori con lo stesso nome all'interno della listview listUtentiPartita
		if(!nome.trim().equals("") && !lstUtentiPartita.getItems().contains(nome) && !lstGiocatoriRegistrati.getItems().contains(nome)) {
			txtNomeUtente.clear();
			lstUtentiPartita.getItems().add(nome);
			lstGiocatoriRegistrati.getItems().add(nome);
			giocatoriPrt.add(new Giocatore(nome));

			try{

				FileWriter fw = new FileWriter(pathClassifica, true);
				fw.write(0 + " , " + nome + "\n");
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

		String nomeUtente = lstGiocatoriRegistrati.getSelectionModel().getSelectedItem();   

		if(!lstUtentiPartita.getItems().contains(nomeUtente) && nomeUtente != null)
		{
			lstUtentiPartita.getItems().add(nomeUtente);
			giocatoriPrt.add(new Giocatore(nomeUtente));
			lstGiocatoriRegistrati.getItems().remove(nomeUtente);
		}
	}

	//aggiungo alla partita un utente robot  
	@FXML public void AggiungiUtenteRobot(ActionEvent actionEvent) {
		String nome = txtNomeUtenteRobot.getText();
		//controllo che non vengano inseriti giocatori con lo stesso nome all'interno della listview listUtentiPartita
		if(!nome.trim().equals("") && !lstUtentiPartita.getItems().contains(nome) && !lstGiocatoriRegistrati.getItems().contains(nome)) {
			txtNomeUtenteRobot.clear();
			lstUtentiPartita.getItems().add(nome);
			giocatoriPrt.add(new Bot(nome));
		}else {
			txtNomeUtenteRobot.clear();
		}
	}
	
	@FXML public void rimuoviGiocatore(MouseEvent mouseEvent) {
		String nomeUtente=lstUtentiPartita.getSelectionModel().getSelectedItem();
		int posUtente = lstUtentiPartita.getItems().indexOf(nomeUtente);
		lstUtentiPartita.getItems().remove(posUtente); 
		giocatoriPrt.remove(posUtente);
		lstGiocatoriRegistrati.getItems().add(nomeUtente);
	}

	//Genero il codice per una nuova partita
	@FXML public void GeneraCodice(ActionEvent actionEvent) {
		lblErrore.setVisible(false);
		if(lstUtentiPartita.getItems().size()<=8) {
			if(lstUtentiPartita.getItems().size()>1) {
				try {

					UUID uniqueID = UUID.randomUUID();
					//'p' sta per partita
					String uniqueCode = "p"+uniqueID.toString().replaceAll("-", "").substring(0, 8);
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
					txtCodice.setText(uniqueCode);

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
					this.prt=new Partita(uniqueCode, giocatoriPrt);

					//do le carte a ogni giocatore
					mazzo.setSpeciale();
					mazzo.mescola();
					int numeroCarteAGiocatore=quanteCarteAGiocatore(prt.getElencoGiocatori().size());
					prt.setNumeroCarteAGiocatore(numeroCarteAGiocatore);//mi salvo il numero di carte che ho dato per i turni futuri
					for(Giocatore g : this.prt.getElencoGiocatori()) {
						g.setCarteMano(mazzo.pescaCarte(numeroCarteAGiocatore));
					}

					//salvo la partita su file.json
					SalvaPartita(this.prt);
				}catch(FileNotFoundException e) {
					System.out.println(e);
				}catch(IOException eIO) {
					System.out.println(eIO);    		
				}
			}else {
				lblErrore.setVisible(true);
				//txtCodice.setStyle("-fx-text-fill: red;");
				lblErrore.setText("Aggiungi almeno 2 giocatori");
			}
		}else {
			lblErrore.setVisible(true);
			//txtCodice.setStyle("-fx-text-fill: red;");
			lblErrore.setText("Puoi aggiungere al massimo 8 giocatori");

		}
	}

	//torno alla Schermata di gestione funzionalità torneo partita e giocatori
	@FXML public void TornaAllaHome(ActionEvent actionEvent) {
		//chiudo la finestra di di creazione della partita e torno alla finestra di login
		Stage stage = (Stage)btnTornaAllaHome.getScene().getWindow();
		stage.close();

		//riapro la finestra di login
		try {
			MediaPlayer currentMediaPlayer = VideoBackgroundPane.getCurrentMediaPlayer();
			if (currentMediaPlayer != null) {
	            currentMediaPlayer.stop();
	        }
			
            VideoBackgroundPane videoBackgroundPane = new VideoBackgroundPane("src/v1.mp4");
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("PartitaTorneo.fxml"));
			Parent root = loader.load();

			ControllerPartitaTorneo controller = loader.getController();	
			
			StackPane stackPane = new StackPane();
            stackPane.setStyle("-fx-background-color: #38B6FF;");
            stackPane.getChildren().addAll(videoBackgroundPane, root);
            
			stage.setTitle("Gestione Funzionalità");
			Scene interfacciaHome = new Scene(stackPane, 600, 400);
			//copio le informazioni relative alla partita in corso e carico le informazioni della classifica
			//controller.copiaInformazioniPartita(this.prt);
			//controller.caricaClassifica();

			stage.setScene(interfacciaHome);
			stage.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}   

	public void caricaGiocatoriRegistrati() {

		try {
			File file = new File(pathClassifica);
			Scanner scan = new Scanner(file);			
			while(scan.hasNext()) {
				String line = scan.nextLine();
				String[] data = line.split(" , ");
				lstGiocatoriRegistrati.getItems().add(data[1].trim());	
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

	private int quanteCarteAGiocatore(int numeroGiocatori) {
		if(numeroGiocatori>4) {
			return 5;
		}else if(numeroGiocatori == 2){
			return 1;
		}else {
			return numeroGiocatori;
		}
	}

	private void SalvaPartita(Partita partita) {
		try {
			GsonBuilder gsonBuilder = new GsonBuilder();
			//imposto un TypeAdapter per salvare correttamente l'elenco dei giocatori che contiene sia Bot che Giocatori
			gsonBuilder.registerTypeAdapter(new TypeToken<ArrayList<Giocatore>>() {}.getType(), new ElencoGiocatoriTypeAdapter());
			Gson gson=gsonBuilder.create();
			ArrayList<Partita> elencoPartite = new ArrayList<Partita>();
			String path="src/SalvataggioPartite.json";
			FileReader fr = new FileReader(path);
			JsonReader jsnReader=new JsonReader(fr);

			jsnReader.beginArray();
			//carico il contenuto del file
			while(jsnReader.hasNext()) {
				Partita p = gson.fromJson(jsnReader, Partita.class);
				elencoPartite.add(p);
			}
			jsnReader.endArray();
			jsnReader.close();

			elencoPartite.add(partita);
			//salvo la lista di partite caricate dal file + quella creata
			FileWriter fw = new FileWriter(path);
			JsonWriter jsnWriter = new JsonWriter(fw);
			jsnWriter.beginArray();

			for (Partita p : elencoPartite) {
				gson.toJson(p, Partita.class, jsnWriter);
				fw.write('\n');
			}
			jsnWriter.endArray();
			jsnWriter.close();
		} catch (FileNotFoundException fnfe) {
			// TODO Auto-generated catch block
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			// TODO Auto-generated catch block
			ioe.printStackTrace();
		}
	}
}