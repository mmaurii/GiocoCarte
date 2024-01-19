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

public class ControllerCreaPartita {
	//variabili di controllo
	final int lungCodicePartita=10;
	final int nViteDefault=5;
	final int maxUtentiPrt=8;
	final int minUtentiPrt=1;//il limite inferiore è escluso e quello superiore incluso
	
	final String pathSalvataggioPrt="Documenti/SalvataggioPartite.json";
	File file1 = new File(pathSalvataggioPrt);
	
	Partita prt;
	Mazzo mazzo = new Mazzo();
	ArrayList<Giocatore> giocatoriPrt = new ArrayList<Giocatore>();

	String path = "Documenti/Classifica.txt";
	File file = new File(path);

	@FXML ListView<String> lstGiocatoriRegistrati;
	@FXML ListView<String> lstUtentiPartita;
	@FXML Button btnAggiungiUtente;
	@FXML TextField txtNomeUtente; 
	@FXML Button btnAggiungiUtenteRobot;
	@FXML TextField txtNomeUtenteRobot;
	@FXML Button btnGeneraCodice;
	@FXML TextField txtCodice;
	@FXML ComboBox<String> comboNVite;
	@FXML Button tornaIndietro;
	@FXML Label lblErrore;
	@FXML Label lblErroreNomeUtente;

	//aggiungo alla partita un nuovo utente  
	@FXML public void aggiungiUtente(ActionEvent actionEvent) {
		lblErroreNomeUtente.setVisible(false);
		String nome = txtNomeUtente.getText();
		if(!containsSpecialCharacters(nome)) {
			if (!nome.trim().equals("")) {
				//controllo che non vengano inseriti giocatori con lo stesso nome all'interno della listview listUtentiPartita
				if (!lstUtentiPartita.getItems().contains(nome)&& !lstGiocatoriRegistrati.getItems().contains(nome)) {
					txtNomeUtente.clear();
					lstUtentiPartita.getItems().add(nome);
					lstGiocatoriRegistrati.getItems().add(nome);
					giocatoriPrt.add(new Giocatore(nome));

					try {
						FileWriter fw = new FileWriter(file, true);
						fw.write(0 + " , " + nome + "\n");
						fw.close();
					} catch (FileNotFoundException FNFe) {
						FNFe.printStackTrace();
					} catch (IOException IOe) {
						IOe.printStackTrace();
					}
				} else {
					txtNomeUtente.clear();
					lblErroreNomeUtente.setVisible(true);
					lblErroreNomeUtente.setText("Il Nickname inserito esiste già, riprova con un altro Nickname");
				} 
			}else {
				txtNomeUtente.clear();
				lblErroreNomeUtente.setVisible(true);
				lblErroreNomeUtente.setText("inserisci un nome per il giocatore");
			}
		}else {
			txtNomeUtente.clear();
			lblErroreNomeUtente.setVisible(true);
			lblErroreNomeUtente.setText("il nome non può contenere: \"!@#$%^&*()-_=+[]{}|;:'\\\",.<>?/\"");
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
	@FXML public void aggiungiBot(ActionEvent actionEvent) {
		lblErroreNomeUtente.setVisible(false);
		String nome = txtNomeUtenteRobot.getText();
		if(!containsSpecialCharacters(nome)) {
			if (!nome.trim().equals("")) {
				//controllo che non vengano inseriti bot con lo stesso nome
				if(!lstUtentiPartita.getItems().contains(nome) && !lstGiocatoriRegistrati.getItems().contains(nome)) {
					txtNomeUtenteRobot.clear();
					lstUtentiPartita.getItems().add(nome);
					giocatoriPrt.add(new Bot(nome));
				} else {
					txtNomeUtenteRobot.clear();
					lblErroreNomeUtente.setVisible(true);
					lblErroreNomeUtente.setText("Il Nickname inserito esiste già, riprova con un altro Nickname");
				} 
			}else {
				txtNomeUtenteRobot.clear();
				lblErroreNomeUtente.setVisible(true);
				lblErroreNomeUtente.setText("inserisci un nome per il Bot");
			}
		}else {
			txtNomeUtenteRobot.clear();
			lblErroreNomeUtente.setVisible(true);
			lblErroreNomeUtente.setText("il nome non può contenere: \"!@#$%^&*()-_=+[]{}|;:'\\\",.<>?/\"");
		}
	}

	/**
	 * rimuovo un giocatore aggiunto in precedenza alla partita in corso
	 * @param mouseEvent
	 */
	@FXML public void rimuoviGiocatore(MouseEvent mouseEvent) {
		boolean flagRimozione=true;
		String nomeUtente=lstUtentiPartita.getSelectionModel().getSelectedItem();
		if(nomeUtente!=null) {
			for (Giocatore gio : giocatoriPrt) {
				if(gio instanceof Bot && gio.getNome().equals(nomeUtente)) {
					flagRimozione=false;
				}
			}
			if(flagRimozione) {//rimuovo il nome e lo aggiuno nella lista utenti registrati
				int posUtente = lstUtentiPartita.getItems().indexOf(nomeUtente);
				lstUtentiPartita.getItems().remove(posUtente); 
				giocatoriPrt.remove(posUtente);
				lstGiocatoriRegistrati.getItems().add(nomeUtente);
			}else{//rimuovo il nome
				int posUtente = lstUtentiPartita.getItems().indexOf(nomeUtente);
				lstUtentiPartita.getItems().remove(posUtente); 
				giocatoriPrt.remove(posUtente);
			}
		}
	}

	/**
	 * 	Genero il codice per una nuova partita
	 */
	@FXML public void generaCodice(ActionEvent actionEvent) {
		lblErrore.setVisible(false);
		if(lstUtentiPartita.getItems().size()<=maxUtentiPrt) {		//controllo il numerop di giocatori
			if(lstUtentiPartita.getItems().size()>minUtentiPrt) {
				UUID uniqueID = UUID.randomUUID();
				//'p' sta per partita
				String uniqueCode = "p"+uniqueID.toString().replaceAll("-", "").substring(0, 8);
				txtCodice.setText(uniqueCode);
				btnGeneraCodice.setDisable(true);

				//do le vite e le carte ai giocatori
				String nVite = comboNVite.getSelectionModel().getSelectedItem();
				if(nVite!=null) {
					for(Giocatore i : giocatoriPrt) {
						i.setVite(Integer.parseInt(nVite));
					}
				}else {//imposto un valore di default
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
			}else {
				lblErrore.setVisible(true);
				lblErrore.setText("Aggiungi almeno 2 giocatori per giocare");
			}
		}else {
			lblErrore.setVisible(true);
			lblErrore.setText("Puoi aggiungere al massimo 8 giocatori a partita");
		}
	}

	/**
	*torno alla Schermata di gestione funzionalità torneo partita e giocatori
	*/
	@FXML public void tornaIndietro(ActionEvent actionEvent) {
		//chiudo la finestra di di creazione della partita e torno al menu
		Stage stage = (Stage)tornaIndietro.getScene().getWindow();
		stage.close();

		//riapro la finestra di menu
		try {
			MediaPlayer currentMediaPlayer = VideoBackgroundPane.getCurrentMediaPlayer();
			if (currentMediaPlayer != null) {
				currentMediaPlayer.stop();
			}

			VideoBackgroundPane videoBackgroundPane = new VideoBackgroundPane("/v1.mp4");
			Thread.sleep(200);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("PartitaTorneo.fxml"));
			Parent root = loader.load();
			loader.getController();	

			StackPane stackPane = new StackPane();
			stackPane.setStyle("-fx-background-color: #38B6FF;");
			stackPane.getChildren().addAll(videoBackgroundPane, root);

			stage.setTitle("Gestione Funzionalità");
			Scene interfacciaHome = new Scene(stackPane, 600, 400);
			stage.setScene(interfacciaHome);
			stage.show();

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}	
	}   
	
	/**
	 * carico i giocatori registrati dal file della classifica all'apposita listview
	 */
	public void caricaGiocatoriRegistrati() {
		try {
			Scanner scan = new Scanner(file);			
			while(scan.hasNext()) {
				String line = scan.nextLine();
				String[] data = line.split(" , ");
				lstGiocatoriRegistrati.getItems().add(data[1].trim());	
			}
			scan.close();
		} catch (FileNotFoundException fnfe) {
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
	
	/**
	 * determina il numero di carte da dare a ogni giocatore in base al numero di giocatori della partita
	 * @param numeroGiocatori
	 * @return numero di carte da distribuire a ogni giocatore
	 */
	private int quanteCarteAGiocatore(int numeroGiocatori) {
		if(numeroGiocatori>4) {
			return 5;
		}else if(numeroGiocatori == 2){
			return 1;
		}else {
			return numeroGiocatori;
		}
	}

	/**
	 * salva la partita in un file '.json'
	 * @param partita
	 */
	private void SalvaPartita(Partita partita) {
		try {
			GsonBuilder gsonBuilder = new GsonBuilder();
			//imposto un TypeAdapter per salvare correttamente l'elenco dei giocatori che contiene sia Bot che Giocatori
			gsonBuilder.registerTypeAdapter(new TypeToken<ArrayList<Giocatore>>() {}.getType(), new ElencoGiocatoriTypeAdapter());
			Gson gson=gsonBuilder.create();
			ArrayList<Partita> elencoPartite = new ArrayList<Partita>();
			FileReader fr = new FileReader(file1);
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
			FileWriter fw = new FileWriter(file1);
			JsonWriter jsnWriter = new JsonWriter(fw);
			jsnWriter.beginArray();

			for (Partita p : elencoPartite) {
				gson.toJson(p, Partita.class, jsnWriter);
				fw.write('\n');
			}
			jsnWriter.endArray();
			jsnWriter.close();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * controlla la presenza dei seguenti caratteri speciali: <pre>!@#$%^&*()-_=+[]{}|;:'\",.<>?/</pre> 
	 * @param text
	 * @return true se i caratteri speciali sono presenti in <code>text</code>, false altrimenti
	 */
	private boolean containsSpecialCharacters(String text) {
		// Definisci qui l'insieme di caratteri speciali che vuoi controllare
		String specialCharacters = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/";

		for (char c : text.toCharArray()) {
			if (specialCharacters.contains(String.valueOf(c))) {
				return true;
			}
		}
		return false;
	}
}