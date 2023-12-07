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
	Torneo T;
	Mazzo mazzo = new Mazzo();
	ArrayList<Giocatore> giocatoriTrn = new ArrayList<Giocatore>();
	ArrayList<Giocatore> giocatoriSqd = new ArrayList<Giocatore>();
	ArrayList<Squadra> squadre = new ArrayList<Squadra>();

	
	String pathRetroCarta = "/basic/IMGcarte/retro.jpg";
	String pathStatus = "src/StatusTornei.txt";
	String pathClassifica = "src/Classifica.txt";

	int numeroGiocatori;
	
	
	@FXML ListView<String> lstGiocatoriRegistrati;
	@FXML ListView<String> listUtentiTorneo;


	
	
	@FXML Button btnAggiungiUtente;

	@FXML Button btnNumeroGiocatori;
	@FXML TextField txtNumeroGiocatori;
	public void numeroGiocatori(ActionEvent actionEvent) {
		
		numeroGiocatori = Integer.parseInt(txtNumeroGiocatori.getText());
		if(numeroGiocatori>=4) 
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
            alert.setContentText("Inserire un numero di giocatori minimo di 4");
            alert.showAndWait();
		}
	}
	
	
	
	
	//aggiungo alla partita un nuovo utente  
	@FXML TextField txtNomeUtente; 
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

		String nome = lstGiocatoriRegistrati.getSelectionModel().getSelectedItem();   

		if(!listUtentiTorneo.getItems().contains(nome) && nome != null)
		{
			listUtentiTorneo.getItems().add(nome);
			giocatoriTrn.add(new Giocatore(nome));	
		}
	}
		
	@FXML public void rimuoviGiocatore(MouseEvent mouseEvent) {

		listUtentiTorneo.getItems().remove(listUtentiTorneo.getSelectionModel().getSelectedItem()); 

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
	
	@FXML Button btnTornaAllaHome;
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
			controller.copiaInformazioniTorneo(this.T);
			controller.populateListView();

			stage.setScene(interfacciaHome);
			stage.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	} 
	
	
	//Genero il codice per una nuova partita
	@FXML Button btnGeneraCodice;
	@FXML TextField txtCodice;
	@FXML ComboBox<String> comboNVite;
	@FXML public void GeneraCodice(ActionEvent actionEvent) {
		
		txtCodice.clear();
		String uniqueCode = "";
		
		if(listUtentiTorneo.getItems().size() == numeroGiocatori) {
				try {

					UUID uniqueID = UUID.randomUUID();
					uniqueCode = uniqueID.toString().replaceAll("-", "").substring(0, 8);
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
		
		T = new Torneo(uniqueCode, generaSquadre());
	}
	
	
	public ArrayList<Squadra> generaSquadre() {
		
		Collections.shuffle(giocatoriTrn);
		
		int numeroSquadre = giocatoriTrn.size()/4;
		int numeroGiocatoriPerSquadra = giocatoriTrn.size()/numeroSquadre;
		
		
		
		for(int i = 1; i <= numeroSquadre; i++) 
		{
			
			if(i == numeroSquadre)
			{
				
                int z = giocatoriTrn.size();
				
				for(int c = 0; c < z; c++) 
				{
					giocatoriSqd.add(giocatoriTrn.remove(0));
				}
				
				Squadra s = new Squadra(giocatoriSqd);
				squadre.add(s);

				
				
			}else {
				for(int j = 0; j < numeroGiocatoriPerSquadra; j++) 
				{
					giocatoriSqd.add(giocatoriTrn.remove(0));
				}
				
				Squadra s = new Squadra(giocatoriSqd);
				squadre.add(s);
				
			}
		}
		
		return squadre;
	}
	
	public void creaPartite() {
	
		
		for(int i = 0; i <= squadre.size(); i++) 
		{	
			UUID uniqueID = UUID.randomUUID();
			String uniqueCode = uniqueID.toString().replaceAll("-", "").substring(0, 8);
			
			Partita p = new Partita(uniqueCode, squadre.get(i).getGiocatori());
			
			
		}
		
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