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

public class ControllerCreaTorneo {
	//variabili di controllo
	final int lungCodicePartita=10;
	final int nViteDefault=5;
	Torneo trn;
	Mazzo mazzo = new Mazzo();
	ArrayList<Giocatore> giocatoriTrn = new ArrayList<Giocatore>();
	ArrayList<Giocatore> giocatoriPrt = new ArrayList<Giocatore>();
	ArrayList<Partita> elencoPrt = new ArrayList<Partita>();
	final int minGiocatoriTorneo = 6;
	final int maxGiocatoriTorneo = 40;
	String pathSalvataggioTrn = "Documenti/SalvataggioTornei.json";
	String pathClassifica = "Documenti/Classifica.txt";
	int numeroGiocatori;

	@FXML ListView<String> lstGiocatoriRegistrati;
	@FXML ListView<String> listUtentiTorneo;
	@FXML Button btnAggiungiUtente;
	@FXML Button btnNumeroGiocatori;
	@FXML TextField txtNumeroGiocatori;
	@FXML TextField txtNomeUtente; 
	@FXML Button btnTornaIndietro;
	@FXML Button btnGeneraCodice;
	@FXML TextField txtCodice;
	@FXML ComboBox<String> comboNVite;
	@FXML Label lblErrore;
	@FXML Label lblErroreNumGiocatori;

	/**
	 * ottiene il numero di giocatori da cui sara composto il torneo
	 * @param actionEvent
	 */
	@FXML public void numeroGiocatori(ActionEvent actionEvent) {
		lblErroreNumGiocatori.setVisible(false);
		try {
			numeroGiocatori = Integer.parseInt(txtNumeroGiocatori.getText());
			if(numeroGiocatori>minGiocatoriTorneo&&numeroGiocatori<=maxGiocatoriTorneo)//controllo il numero di giocatori inseriti 
			{
				btnAggiungiUtente.setDisable(false);
				btnNumeroGiocatori.setDisable(true);
				lstGiocatoriRegistrati.setDisable(false);
				listUtentiTorneo.setDisable(false);
				btnGeneraCodice.setDisable(false);
			}else {
				txtNumeroGiocatori.clear();
				lblErroreNumGiocatori.setVisible(true);
				lblErroreNumGiocatori.setText("inserisci un numero di giocatori compreso tra: "+(minGiocatoriTorneo+1)+" e "+maxGiocatoriTorneo);
			}
		}catch(NumberFormatException nfe) {
			txtNumeroGiocatori.clear();
			lblErroreNumGiocatori.setVisible(true);
			lblErroreNumGiocatori.setText("inserisci un numero di giocatori compreso tra: "+(minGiocatoriTorneo+1)+" e "+maxGiocatoriTorneo);
		}
	}

	/**
	 * aggiungo alla partita un utente tramite prendendo il nome dalla textfield di input
	 * @param actionEvent
	 */
	@FXML public void AggiungiUtente(ActionEvent actionEvent) {
		lblErroreNumGiocatori.setVisible(true);
		String nome = txtNomeUtente.getText();
		if(!containsSpecialCharacters(nome)) {
			//controllo che non vengano inseriti giocatori con lo stesso nome all'interno della listview listUtentiPartita
			if(!nome.trim().equals("") && !listUtentiTorneo.getItems().contains(nome) && !lstGiocatoriRegistrati.getItems().contains(nome)) {
				txtNomeUtente.clear();
				listUtentiTorneo.getItems().add(nome);
				lstGiocatoriRegistrati.getItems().add(nome);
				giocatoriTrn.add(new Giocatore(nome));
				try{
					File file = new File(pathClassifica);
					FileWriter fw = new FileWriter(file, true);
					fw.write(0 + " , " + nome + "\n");
					fw.close();
				} catch (FileNotFoundException FNFe) {
					FNFe.printStackTrace();
				} catch (IOException IOe) {
					IOe.printStackTrace();
				}
			}else {
				txtNomeUtente.clear();
				lblErroreNumGiocatori.setVisible(true);
				lblErroreNumGiocatori.setText("Il Nickname inserito esiste già, riprova con un altro Nickname");
			}
		}else {
			txtNomeUtente.clear();
			lblErroreNumGiocatori.setVisible(true);
			lblErroreNumGiocatori.setText("il nome non può contenere: \"!@#$%^&*()-_=+[]{}|;:'\\\",.<>?/\"");
		}
	}

	/**
	 * aggiungo alla partita un utente già registrato tramite la selezione da listview
	 * @param mouseEvent
	 */
	@FXML public void selezionaGiocatore(MouseEvent mouseEvent) {
		String nomeUtente = lstGiocatoriRegistrati.getSelectionModel().getSelectedItem();   

		if(!listUtentiTorneo.getItems().contains(nomeUtente) && nomeUtente != null)
		{
			listUtentiTorneo.getItems().add(nomeUtente);
			giocatoriTrn.add(new Giocatore(nomeUtente));	
			lstGiocatoriRegistrati.getItems().remove(nomeUtente);
		}
	}

	/**
	 * rimuovo il giocatore/bot dalla lista degli utenti della partita
	 * @param mouseEvent
	 */
	@FXML public void rimuoviGiocatore(MouseEvent mouseEvent) {
		String nomeUtente=listUtentiTorneo.getSelectionModel().getSelectedItem();
		if(nomeUtente!=null) {
			int posUtente = listUtentiTorneo.getItems().indexOf(nomeUtente);
			listUtentiTorneo.getItems().remove(posUtente); 
			giocatoriTrn.remove(posUtente);
			lstGiocatoriRegistrati.getItems().add(nomeUtente);
		}
	}

	/**
	 * torno al menu precedente
	 * @param actionEvent
	 */
	@FXML public void tornaIndietro(ActionEvent actionEvent) {
		//chiudo la finestra di di creazione della partita e torno al menu
		Stage stage = (Stage)btnTornaIndietro.getScene().getWindow();
		stage.close();

		//riapro la finestra del menu
		try {
			MediaPlayer currentMediaPlayer = VideoBackgroundPane.getCurrentMediaPlayer();
			if (currentMediaPlayer != null) {
				currentMediaPlayer.stop();
			}

			VideoBackgroundPane videoBackgroundPane = new VideoBackgroundPane("/v1.mp4");
			Thread.sleep(300);
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
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	} 

	/**
	 * Genero il codice per un nuovo torneo
	 * @param actionEvent
	 */
	@FXML public void generaCodice(ActionEvent actionEvent) {
		txtCodice.clear();
		String uniqueCode = "";
		lblErrore.setVisible(false);

		if(listUtentiTorneo.getItems().size() == numeroGiocatori) {//controllo che il numero di giocatori inserito coincida con quello dichiarato
			UUID uniqueID = UUID.randomUUID();
			//'t' sta per torneo
			uniqueCode = "t"+uniqueID.toString().replaceAll("-", "").substring(0, 8);

			txtCodice.setText(uniqueCode);
			btnGeneraCodice.setDisable(true);

			ArrayList<Partita> elencoPrt=generaPartite();
			this.trn = new Torneo(uniqueCode, elencoPrt);

			//salvo il torneo su file.json
			SalvaTorneo(this.trn);
		}else {
			lblErrore.setVisible(true);
			lblErrore.setText("Inserisci il numero esatto di Partecipanti");
		}
	}

	//METODI AUSILIARI
	/**
	 * genera le partite per il torneo
	 * @return ArrayList<<a>Partita>
	 * <pre>(sono le partite della fase a 'gironi')</pre>
	 */
	private ArrayList<Partita> generaPartite() {
		ArrayList<Partita> elencoPrt=new ArrayList<>();
		Collections.shuffle(giocatoriTrn);//mischio i giocatori
		//definisco il  numero di squadre
		int numeroPartite = giocatoriTrn.size()/4;
		if(numeroPartite == 1)
		{
			numeroPartite = 2;
		}

		//definisco il numero di giocatori
		int numeroGiocatoriPartita = giocatoriTrn.size()/numeroPartite;	

		for(int i = 1; i <= numeroPartite; i++) 
		{
			giocatoriPrt = new ArrayList<Giocatore>();	
			if(i == numeroPartite)
			{
				int z = giocatoriTrn.size();
				for(int c = 0; c < z; c++) 
				{
					giocatoriPrt.add(giocatoriTrn.remove(0));
				}	
			}else {
				for(int j = 0; j < numeroGiocatoriPartita; j++) 
				{
					giocatoriPrt.add(giocatoriTrn.remove(0));
				}
			}

			//creo la partita
			Partita p = creaPartita(giocatoriPrt);
			p.setFlagTorneo(true);
			//aggiungo la partita all'elenco delle partite del torneo
			elencoPrt.add(p);
		}
		return elencoPrt;
	}

	/**
	 * creo una partita per il torneo dato un elenco di giocatori
	 * @param giocatoriPrt
	 * @return partita
	 */
	private Partita creaPartita(ArrayList<Giocatore> giocatoriPrt) {
		UUID uniqueID = UUID.randomUUID();
		//la 'p' iniziale distingue i codici partita da quelli dei tornei
		String uniqueCode = "p"+uniqueID.toString().replaceAll("-", "").substring(0, 8);

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
		Partita p=new Partita(uniqueCode, giocatoriPrt);
		mazzo=new Mazzo();
		//do le carte a ogni giocatore
		mazzo.setSpeciale();
		mazzo.mescola();
		int numeroCarteAGiocatore=quanteCarteAGiocatore(p.getElencoGiocatori().size());
		p.setNumeroCarteAGiocatore(numeroCarteAGiocatore);//mi salvo il numero di carte che ho dato per i turni futuri
		for(Giocatore g : p.getElencoGiocatori()) {//do le carte
			g.setCarteMano(mazzo.pescaCarte(numeroCarteAGiocatore));
		}
		return p;
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
	 * carico i giocatori registrati dal file della classifica all'apposita listview
	 */
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
	 * salva il torneo in un file '.json'
	 * @param torneo
	 */
	private void SalvaTorneo(Torneo torneo) {
		try {
			GsonBuilder gsonBuilder = new GsonBuilder();
			//imposto un TypeAdapter per salvare correttamente l'elenco dei giocatori che contiene sia Bot che Giocatori
			gsonBuilder.registerTypeAdapter(new TypeToken<ArrayList<Giocatore>>() {}.getType(), new ElencoGiocatoriTypeAdapter());
			Gson gson=gsonBuilder.create();
			ArrayList<Torneo> elencoTornei = new ArrayList<Torneo>();
			File file1 = new File(pathSalvataggioTrn);
			FileReader fr = new FileReader(file1);
			JsonReader jsnReader=new JsonReader(fr);

			jsnReader.beginArray();
			//carico il contenuto del file
			while(jsnReader.hasNext()) {
				Torneo t = gson.fromJson(jsnReader, Torneo.class);
				elencoTornei.add(t);
			}
			jsnReader.endArray();
			jsnReader.close();

			elencoTornei.add(torneo);
			//salvo la lista di tornei caricati dal file + quella creata
			FileWriter fw = new FileWriter(file1);
			JsonWriter jsnWriter = new JsonWriter(fw);
			jsnWriter.beginArray();

			for (Torneo t : elencoTornei) {
				gson.toJson(t, Torneo.class, jsnWriter);
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