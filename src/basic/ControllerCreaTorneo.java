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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ControllerCreaTorneo {
	//variabili di controllo
	final int lungCodicePartita=10;
	final int nViteDefault=5;
	Torneo trn;
	Mazzo mazzo = new Mazzo();
	ArrayList<Giocatore> giocatoriTrn = new ArrayList<Giocatore>();
	ArrayList<Giocatore> giocatoriPrt = new ArrayList<Giocatore>();
	ArrayList<Partita> elencoPrt = new ArrayList<Partita>();
	int minGiocatoriTorneo = 6;
	int maxGiocatoriTorneo = 40;
	String pathRetroCarta = "/basic/IMGcarte/retro.jpg";
	String pathStatus = "src/Status.txt";
	String pathClassifica = "src/Classifica.txt";
	int numeroGiocatori;


	@FXML ListView<String> lstGiocatoriRegistrati;
	@FXML ListView<String> listUtentiTorneo;
	@FXML Button btnAggiungiUtente;
	@FXML Button btnNumeroGiocatori;
	@FXML TextField txtNumeroGiocatori;
	@FXML TextField txtNomeUtente; 
	@FXML Button btnTornaAllaHome;
	@FXML Button btnGeneraCodice;
	@FXML TextField txtCodice;
	@FXML ComboBox<String> comboNVite;

	@FXML public void numeroGiocatori(ActionEvent actionEvent) {

		numeroGiocatori = Integer.parseInt(txtNumeroGiocatori.getText());
		if(numeroGiocatori>6&&numeroGiocatori<=40) 
		{
			btnAggiungiUtente.setDisable(false);
			btnNumeroGiocatori.setDisable(true);
			lstGiocatoriRegistrati.setDisable(false);
			listUtentiTorneo.setDisable(false);
		}else {
			btnAggiungiUtente.setDisable(true);
			txtNumeroGiocatori.clear();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Errore");
			alert.setHeaderText(null);
			alert.setContentText("Inserire un numero di giocatori minimo di 4 e massimo di 40"); 
			alert.showAndWait();
		}
	}

	//aggiungo alla partita un nuovo utente  
	@FXML public void AggiungiUtente(ActionEvent actionEvent) {
		String nome = txtNomeUtente.getText();
		//controllo che non vengano inseriti giocatori con lo stesso nome all'interno della listview listUtentiPartita
		if(!nome.trim().equals("") && !listUtentiTorneo.getItems().contains(nome) && !lstGiocatoriRegistrati.getItems().contains(nome)) {
			txtNomeUtente.clear();
			listUtentiTorneo.getItems().add(nome);
			lstGiocatoriRegistrati.getItems().add(nome);
			giocatoriTrn.add(new Giocatore(nome));
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

		if(!listUtentiTorneo.getItems().contains(nomeUtente) && nomeUtente != null)
		{
			listUtentiTorneo.getItems().add(nomeUtente);
			giocatoriTrn.add(new Giocatore(nomeUtente));	//inseisci numero di vite e carte
			lstGiocatoriRegistrati.getItems().remove(nomeUtente);

		}
	}

	@FXML public void rimuoviGiocatore(MouseEvent mouseEvent) {
		String nomeUtente=listUtentiTorneo.getSelectionModel().getSelectedItem();
		int posUtente = listUtentiTorneo.getItems().indexOf(nomeUtente);
		listUtentiTorneo.getItems().remove(posUtente); 
		giocatoriTrn.remove(posUtente);
		lstGiocatoriRegistrati.getItems().add(nomeUtente);
	}

	//torno alla Schermata di login
	@FXML public void TornaAllaHome(ActionEvent actionEvent) {
		//chiudo la finestra di di creazione della partita e torno alla finestra di login
		Stage stage = (Stage)btnTornaAllaHome.getScene().getWindow();
		stage.close();

		//riapro la finestra di login
		try {
			VideoBackgroundPane videoBackgroundPane = new VideoBackgroundPane("src/v1.mp4");

			FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
			Parent root = loader.load();

			ControllerHome controller = loader.getController();	

			StackPane stackPane = new StackPane();
			stackPane.setStyle("-fx-background-color: #38B6FF;");
			stackPane.getChildren().addAll(videoBackgroundPane, root);

			stage.setTitle("HOME");
			Scene interfacciaHome = new Scene(stackPane, 600, 400);
			//copio le informazioni relative alla partita in corso e carico le informazioni della classifica
			controller.copiaInformazioniTorneo(this.trn);
			controller.populateListView();

			stage.setScene(interfacciaHome);
			stage.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	} 

	//Genero il codice per un nuovo torneo
	@FXML public void GeneraCodice(ActionEvent actionEvent) {
		txtCodice.clear();
		String uniqueCode = "";

		if(listUtentiTorneo.getItems().size() == numeroGiocatori) {
			try {
				UUID uniqueID = UUID.randomUUID();
				//'t' sta per torneo
				uniqueCode = "t"+uniqueID.toString().replaceAll("-", "").substring(0, 8);
				File file = new File(pathStatus);

				txtCodice.setText(uniqueCode);
				btnGeneraCodice.setDisable(true);

				//salvo il codice corrente nel file di StausTornei
				FileWriter fw = new FileWriter(file);
				fw.write("codiceTorneo , "+uniqueCode);
				fw.close();
			}catch(FileNotFoundException e) {
				System.out.println(e);
			}catch(IOException eIO) {
				System.out.println(eIO);    		
			}
		}else {
			txtCodice.setStyle("-fx-text-fill: red;");
			txtCodice.setText("Inserisci il numero esatto di Partecipanti");
		}

		ArrayList<Partita> elencoPrt=generaPartite();
		this.trn = new Torneo(uniqueCode, elencoPrt);
	}

	//METODI AUSILIARI
	private ArrayList<Partita> generaPartite() {
		ArrayList<Partita> elencoPrt=new ArrayList<>();
		Collections.shuffle(giocatoriTrn);

		int numeroPartite = giocatoriTrn.size()/4;
		if(numeroPartite == 1)
		{
			numeroPartite = 2;
		}
		
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

	//modifica metodo fa cose sbagliate?
	Partita creaPartita(ArrayList<Giocatore> giocatoriPrt) {
		UUID uniqueID = UUID.randomUUID();
		String uniqueCode = uniqueID.toString().replaceAll("-", "").substring(0, 8);
//			File file = new File(pathStatus);


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
//			txtCodice.setText(uniqueCode);

//			btnGeneraCodice.setDisable(true);

		//salvo il codice corrente nel file di status
//			FileWriter fw = new FileWriter(file);
//			fw.write("codicePartita , "+uniqueCode);
//			fw.close();

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
		System.out.println("numero carte mazzo "+mazzo.getMazzo().size());
		//do le carte a ogni giocatore
		mazzo.setSpeciale();
		mazzo.mescola();
		int numeroCarteAGiocatore=quanteCarteAGiocatore(p.getElencoGiocatori().size());
		p.setNumeroCarteAGiocatore(numeroCarteAGiocatore);//mi salvo il numero di carte che ho dato per i turni futuri
		for(Giocatore g : p.getElencoGiocatori()) {
			g.setCarteMano(mazzo.pescaCarte(numeroCarteAGiocatore));
		}

		return p;
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

	public void populateListView() {
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


	/**

	//aggiungo alla partita un utente robot 
	@FXML Button btnAggiungiUtenteRobot;
	@FXML TextField txtNomeUtenteRobot; 
	@FXML public void AggiungiUtenteRobot(ActionEvent actionEvent) {
		String nome = txtNomeUtenteRobot.getText();
		//controllo che non vengano inseriti giocatori con lo stesso nome all'interno della listview listUtentiPartita
		if(!nome.trim().equals("") && !listUtentiPartita.getItems().contains(nome) && !lstGiocatoriRegistrati.getItems().contains(nome)) {
			txtNomeUtenteRobot.clear();
			listUtentiPartita.getItems().add(nome);
			giocatoriPrt.add(new Bot(nome));
		}else {
			txtNomeUtenteRobot.clear();
		}
	}





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
	 **/

}