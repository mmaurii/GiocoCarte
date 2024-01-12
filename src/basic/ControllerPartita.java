package basic;

import java.io.*;
import java.net.URL;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.application.Platform;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ButtonType;


public class ControllerPartita implements Initializable{
	//variabili di controllo
	final int lungCodicePartita=10;
	final int nViteDefault=5;
	static Partita prt;
	Torneo trn;
	int posPartitaTrn;
	String selettore;
	Mazzo mazzo = new Mazzo();
	final int valCartaSpecialeAssoDenara=40;
	final String pathClassifica = "src/Classifica.txt";
	final String pathStatus = "src/Status.txt";	
	final String pathRetroCarta = "/basic/IMGcarte/retro.jpg";
	final String selettorePrt = "prt";
	final String selettoreSFnl = "semifinale";
	final String selettoreFnl = "finale";
	public Thread t;

	//variabili FXML
	@FXML BorderPane borderPanePartita;
	@FXML Label lblTurnoGiocatore;
	@FXML Label lblManoGiocatore;
	@FXML Label lblVincitoreMano;
	@FXML Button btnInizioTurnoGiocatore;
	@FXML ImageView imgCartaMano1;
	@FXML ImageView imgCartaMano2;
	@FXML ImageView imgCartaMano3;
	@FXML ImageView imgCartaMano4;
	@FXML ImageView imgCartaMano5;
	@FXML TextField txtNumeroPrese;
	@FXML GridPane gridPaneNumeroPrese=new GridPane();
	@FXML Button btnIniziaNuovaMano;
	@FXML Button btnPartitaTornaAllaHome;
	@FXML Button btnPartitaTornaAlTorneo;
	@FXML Button btnClassifica;
	@FXML Button btnFineTurnoGiocatore;
	@FXML Label lblVitaPersa;
	@FXML Button btnIniziaNuovoRound;
	@FXML Label lblPrese;
	@FXML Label lblNumPreseNonValido;
	@FXML ListView<String> lstViewPrese;
	@FXML ListView<String> lstViewVite;
	@FXML ImageView imgCartaBanco1;
	@FXML ImageView imgCartaBanco2;
	@FXML ImageView imgCartaBanco3;
	@FXML ImageView imgCartaBanco4;
	@FXML ImageView imgCartaBanco5;
	@FXML ImageView imgCartaBanco6;
	@FXML ImageView imgCartaBanco7;
	@FXML ImageView imgCartaBanco8;

	@Override
	public void initialize(URL location, ResourceBundle rb) {
		try {
			trn=(Torneo)rb.getObject("Torneo");
			prt = (Partita)rb.getObject("PartitaTrn");
			posPartitaTrn=(int)rb.getObject("posPartitaTrn");
			selettore=(String) rb.getObject("selettore");
		}catch(MissingResourceException mre) {
			prt=(Partita)rb.getObject("Partita");
		}
//		if(!rb.keySet().contains("Torneo")) {
//		}else {System.out.println("a");
//			trn=(Torneo)rb.getObject("Torneo");
//		}
		if(prt!=null){
			//sistemo opportunamente l'interfaccia
			setInterface();
			if(prt.getFlagTorneo()) {
				btnPartitaTornaAllaHome.setVisible(false);
				btnPartitaTornaAlTorneo.setVisible(true);
			}else {
				btnPartitaTornaAllaHome.setVisible(true);
				btnPartitaTornaAlTorneo.setVisible(false);
			}
		}else {
			System.out.println("errore");
		}
	}


	//inizia il turno dell'n giocatore
	@FXML public void inizioTurnoGiocatore(ActionEvent actionEvent) {
		lblTurnoGiocatore.setVisible(false);
		lblManoGiocatore.setVisible(true);
		btnInizioTurnoGiocatore.setDisable(true);
		if(!prt.isModalitaPrt()) {//!gridPaneNumeroPrese.isVisible()
			dichiaraPreseSetVisible(false);
			lblManoGiocatore.setText("Mano di "+prt.getGiocatoreCorrente().getNome());
			prt.setBtnInizioTurnoGiocatoreClicked(true);
		}else {
			dichiaraPreseSetVisible(true);
			lblManoGiocatore.setText(prt.getGiocatoreCorrente().getNome()+" dichiara le prese");
			btnFineTurnoGiocatore.setDisable(false);
		}

		//mostro le carte in output relative al giocatore del turno corrente
		ArrayList<ImageView> listaCarteMano = new ArrayList<ImageView>(Arrays.asList(imgCartaMano1, imgCartaMano2, imgCartaMano3, imgCartaMano4, imgCartaMano5));
		for(int i = 0; i<listaCarteMano.size();i++) {	
			if(i<prt.getGiocatoreCorrente().getCarteMano().size()) {
				Image newImg = new Image(getClass().getResourceAsStream(prt.getGiocatoreCorrente().getCarteMano().get(i).getPercorso()));
				listaCarteMano.get(i).setImage(newImg);
			}else {
				listaCarteMano.get(i).setImage(null);
			}
		}
	}


	//l'n giocatore gioca la carta1
	@FXML public void GiocaCartaMano1(MouseEvent mouseEvent) {
		final int posCartaCliccata=0;
		giocaCartaMano(posCartaCliccata);	
	}    


	//l'n giocatore gioca la carta2
	@FXML public void GiocaCartaMano2(MouseEvent mouseEvent) {
		final int posCartaCliccata=1;
		giocaCartaMano(posCartaCliccata);
	}    


	//l'n giocatore gioca la carta3
	@FXML public void GiocaCartaMano3(MouseEvent mouseEvent) {
		final int posCartaCliccata=2;
		giocaCartaMano(posCartaCliccata);	
	}    


	//l'n giocatore gioca la carta4
	@FXML public void GiocaCartaMano4(MouseEvent mouseEvent) {
		final int posCartaCliccata=3;
		giocaCartaMano(posCartaCliccata);
	}    


	//l'n giocatore gioca la carta5
	@FXML public void GiocaCartaMano5(MouseEvent mouseEvent) {
		final int posCartaCliccata=4;
		giocaCartaMano(posCartaCliccata);
	}


	//dispongo la fine del turno per il giocatore corrente
	@FXML public void fineTurnoGiocatore(ActionEvent actionEvent) {
		lblNumPreseNonValido.setVisible(false);
		int numeroPreseGiocatore=0;
		int giocatoreChePrende;

		if(prt.isModalitaPrt()) {//gridPaneNumeroPrese.isVisible()
			try {
				numeroPreseGiocatore=Integer.parseInt(txtNumeroPrese.getText());
				prt.getGiocatoreCorrente().setPreseDichiarate(numeroPreseGiocatore);
				prt.presePerQuestaMano+=numeroPreseGiocatore;
				//System.out.println(presePerQuestaMano+" != "+(Integer)(this.prt.getElencoGiocatori().get(countTurnoGiocatore).getCarteMano().size()+1)+" || "+(Integer)(countTurnoGiocatore+1)+" != "+this.prt.getElencoGiocatori().size());
				if(prt.presePerQuestaMano!=prt.getGiocatoreCorrente().getCarteMano().size()||prt.getCountTurnoGiocatore()!=prt.getElencoGiocatori().size()-1) {
					Iterator<String> iterator = lstViewPrese.getItems().iterator();
					while(iterator.hasNext()) {
						String s = iterator.next();
						if(s.contains(prt.getGiocatoreCorrente().getNome())) {
							lstViewPrese.getItems().remove(s);
							break;
						}
					}
					//visualizzo il numero di prese per questo giocatore
					lstViewPrese.getItems().add(prt.getGiocatoreCorrente().getNome()+" "+numeroPreseGiocatore+" prese");

					//rimetto le carte coperte
					copriCarteGiocatore(false);

					if(prt.isDichiaraPrese()) {
						//						//visualizzo il numero di vite di questo giocatore se è il primo turno
						//						if(prt.isPrimoTurno()) {
						//							iterator = lstViewVite.getItems().iterator();
						//							while(iterator.hasNext()) {
						//								String s = iterator.next();
						//								if(s.contains(prt.getGiocatoreCorrente().getNome())) {
						//									lstViewVite.getItems().remove(s);
						//								}
						//							}
						//							lstViewVite.getItems().add(prt.getGiocatoreCorrente().getNome()+" "+prt.getGiocatoreCorrente().getVite()+" vite");
						//						}

						//incremento il contatore dei giocatori
						prt.setCountTurnoGiocatore(prt.getCountTurnoGiocatore()+1);;
						//rimetto le carte coperte
						//copriCarteGiocatore(false);

						if(prt.getCountTurnoGiocatore()>=ControllerPartita.prt.getElencoGiocatori().size()) {
							prt.setCountTurnoGiocatore(0);
							//sistemo l'interfaccia per iniziare a giocare le carte
							dichiaraPreseSetVisible(false);
							prt.setModalitaPrt(false);
							lblManoGiocatore.setVisible(false);
							lblTurnoGiocatore.setVisible(false);
							btnIniziaNuovoRound.setVisible(true);
							btnFineTurnoGiocatore.setDisable(true);
							btnInizioTurnoGiocatore.setDisable(true);
						}else {
							//faccio dichiarare le prese al prossimo giocatore
							lblManoGiocatore.setVisible(false);
							btnFineTurnoGiocatore.setDisable(true);
							btnInizioTurnoGiocatore.setDisable(false);
							//							System.out.println("a");
							//definisco chi giocherà il prossimo turno
							lblTurnoGiocatore.setText("è il turno di: "+prt.getGiocatoreCorrente().getNome());
							lblTurnoGiocatore.setVisible(true);
						}
					}
				}else {
					prt.presePerQuestaMano-=numeroPreseGiocatore;
					lblNumPreseNonValido.setVisible(true);
				}
				txtNumeroPrese.setText("");
			}catch(NumberFormatException nfe){
				lblPrese.setTextFill(Color.RED);
				txtNumeroPrese.setText(null);
			}    
		}else {
			prt.presePerQuestaMano=0;

			//if(lstViewPrese.getItems().size()-1==countTurnoGiocatore||!gridPaneNumeroPrese.isVisible()) {
			//rimetto le carte coperte
			copriCarteGiocatore(false);

			//controllo che non si sfori il numero di giocatori
			if(prt.getCountTurnoGiocatore()<prt.getElencoGiocatori().size()) {
				//passo il turno al prossimo giocatore incrementando il contatore
				prt.setCountTurnoGiocatore(prt.getCountTurnoGiocatore()+1);
			}else {
				btnInizioTurnoGiocatore.setDisable(true);
			}

			//controllo che non si sia conclusa una mano
			if(prt.getElencoGiocatori().get(prt.getElencoGiocatori().size()-1).getCarteMano().size() != 0){
				//controllo che non si sia chiuso un giro di carte 
				if(prt.getCountTurnoGiocatore()<prt.getElencoGiocatori().size()) {
					//sistemo la visualizzazione dell'interfaccia
					lblManoGiocatore.setVisible(false);
					btnFineTurnoGiocatore.setDisable(true);
					btnInizioTurnoGiocatore.setDisable(false);
					//					System.out.println("b");
					//definisco chi giocherà il prossimo turno
					lblTurnoGiocatore.setText("è il turno di: "+prt.getGiocatoreCorrente().getNome());
					lblTurnoGiocatore.setVisible(true);
				}else {
					//azzero il contatore dei turni
					prt.setCountTurnoGiocatore(0);

					//Calcolo chi prende in base alle carte sul banco
					giocatoreChePrende = CalcolaPunti(prt.getLstCarteBanco());

					Giocatore gio = prt.getElencoGiocatori().get(giocatoreChePrende);
					prt.getElencoGiocatori().get(giocatoreChePrende).incrementaPreseEffettuate();
					lblVitaPersa.setText(gio.getNome()+" ha PRESO questo turno");
					lblVitaPersa.setVisible(true);
					lblManoGiocatore.setVisible(false);
					lblTurnoGiocatore.setVisible(false);
					btnFineTurnoGiocatore.setDisable(true);
					btnIniziaNuovoRound.setVisible(true);

					//cambio l'ordine dei giocatori spostando il primo in fondo alla lista
					gio=prt.getElencoGiocatori().remove(0);
					prt.getElencoGiocatori().add(gio);
				}
			}else {
				//resetto lo stato per non far giocare le carte
				prt.setBtnInizioTurnoGiocatoreClicked(false);
				//azzero il contatore dei turni
				prt.setCountTurnoGiocatore(0);

				//Calcolo chi prende in base alle carte sul banco
				giocatoreChePrende = CalcolaPunti(prt.getLstCarteBanco());

				Giocatore gio = prt.getElencoGiocatori().get(giocatoreChePrende);
				prt.getElencoGiocatori().get(giocatoreChePrende).incrementaPreseEffettuate();
				lblVitaPersa.setText(gio.getNome()+" ha PRESO questo turno");
				lblVitaPersa.setVisible(true);
				//prt.getLstCarteBanco().clear();
				lblManoGiocatore.setVisible(false);
				lblTurnoGiocatore.setVisible(false);
				btnFineTurnoGiocatore.setDisable(true);

				btnIniziaNuovaMano.setVisible(true);

				//verifico chi ha sbagliato a dichiarare e gli rimuovo la vita
				lstViewVite.getItems().clear();
				for(Giocatore g : prt.getElencoGiocatori()) {	
					if(g.getPreseDichiarate() != g.getPreseEffettuate())
					{
						g.perdiVita();
					}
					if(g.getVite()==0) {
						lstViewVite.getItems().add(g.getNome()+" è eliminato");
					}else {
						lstViewVite.getItems().add(g.getNome()+" "+g.getVite()+" vite");
					}

					//azzero i contatori delle prese relativi alla mano corrente
					g.setPreseEffettuate(0);
					g.setPreseDichiarate(-1);
				}

				Giocatore vincitorePareggio = null;
				Iterator<Giocatore> g = prt.getElencoGiocatori().iterator();
				while(g.hasNext()) {
					gio = g.next();
					if(gio.getVite()==0) {
						g.remove();
						prt.addGiocatoreEliminato(gio.getNome());
						vincitorePareggio=gio;
					}
				}

				if(prt.getElencoGiocatori().size()>1) {//avvio una nuova mano
					cominciaNuovaMano();

					//cambio l'ordine dei giocatori spostando il primo in fondo alla lista
					gio=prt.getElencoGiocatori().remove(0);
					prt.getElencoGiocatori().add(gio);
				}else if (prt.getElencoGiocatori().size()==1){//concludo la partita e ne annuncio il vincitore 
					lblVitaPersa.setText(prt.getElencoGiocatori().get(0).getNome()+" ha VINTO la partita");
					lblManoGiocatore.setVisible(false);
					lblTurnoGiocatore.setVisible(false);
					btnIniziaNuovaMano.setVisible(false);
					btnPartitaTornaAlTorneo.setDisable(false);

					//conteggio punti
					aggiornaClassifica(pathClassifica);
				}else{//concludo la partita e ne annuncio il pareggio
					lblVitaPersa.setText("la partita è finita in PAREGGIO");
					lblManoGiocatore.setVisible(false);
					lblTurnoGiocatore.setVisible(false);
					btnIniziaNuovaMano.setVisible(false);
					
					//definisco l'ultimo giocatore eliminato dalla lista come il vincitore in caso di pareggio
					prt.getElencoGiocatori().add(vincitorePareggio);
				}
			}	
		}


		//controllo che la partita non sia conclusa
		if(prt.getElencoGiocatori().size()>1) {
			//se il prossimo giocatore che deve giocare è un bot lo avvio
			Giocatore gio = prt.getGiocatoreCorrente();
			if(gio instanceof Bot) {
				Bot b = (Bot)gio;
				t = new Thread(b);
				t.setDaemon(true);
				Platform.runLater(t);
				System.out.println("bot");
			}
		}
	}


	@FXML public void fineTurnoGiocatoreControlloBot() {

	}


	@FXML public void IniziaNuovoRound(ActionEvent actionEvent) {
		//sistemo l'interfaccia perchè possa essere giocato un nuovo round
		prt.setPrimoTurno(false);
		btnIniziaNuovoRound.setVisible(false);
		lblVitaPersa.setVisible(false);
		lblTurnoGiocatore.setText("è il turno di: "+prt.getGiocatoreCorrente().getNome());
		lblTurnoGiocatore.setVisible(true);
		btnInizioTurnoGiocatore.setDisable(false);

		ArrayList<ImageView> listaCarteBanco = new ArrayList<ImageView>(Arrays.asList(imgCartaBanco1, imgCartaBanco2, imgCartaBanco3, imgCartaBanco4, imgCartaBanco5, imgCartaBanco6, imgCartaBanco7, imgCartaBanco8));
		for(ImageView i : listaCarteBanco) {
			i.setImage(null);
		}
		prt.getLstCarteBanco().clear();

		//mostro le carte coperte del giocatore che deve iniziare il turno
		copriCarteGiocatore(false);

		//se il prossimo giocatore che deve giocare è un bot lo avvio
		Giocatore gio = prt.getGiocatoreCorrente();
		if(gio instanceof Bot) {
			Bot b = (Bot)gio;
			t = new Thread(b);
			t.setDaemon(true);
			Platform.runLater(t);
			System.out.println("bot");
		}
	}


	@FXML public void IniziaNuovaMano(ActionEvent actionEvent) {
		//sistemo l'interfaccia per poter iniziare la nuova mano
		prt.setDichiaraPrese(true);
		lblPrese.setTextFill(Color.BLACK);
		lstViewPrese.getItems().clear();
		mostraPrese();
		lblVitaPersa.setVisible(false);
		btnIniziaNuovaMano.setVisible(false);
		lblTurnoGiocatore.setText("è il turno di: "+prt.getGiocatoreCorrente().getNome());
		lblTurnoGiocatore.setVisible(true);
		btnInizioTurnoGiocatore.setDisable(false);
		prt.setModalitaPrt(true);
		dichiaraPreseSetVisible(true);
		//rimetto le carte coperte;
		copriCarteGiocatore(false);

		//elimino le carte dal banco
		ArrayList<ImageView> listaCarteBanco = new ArrayList<ImageView>(Arrays.asList(imgCartaBanco1, imgCartaBanco2, imgCartaBanco3, imgCartaBanco4, imgCartaBanco5, imgCartaBanco6, imgCartaBanco7, imgCartaBanco8));
		for(ImageView i : listaCarteBanco) {	
			i.setImage(null);
		}
		prt.getLstCarteBanco().clear();

		//se il prossimo giocatore che deve giocare è un bot lo avvio
		Giocatore gio = prt.getGiocatoreCorrente();
		if(gio instanceof Bot) {
			Bot b = (Bot)gio;
			t = new Thread(b);
			t.setDaemon(true);
			Platform.runLater(t);
			System.out.println("bot");
		}
	}


	//torno all' interfaccia di login
	@FXML public void TornaAllaHome(ActionEvent actionEvent) {
		//chiudo la finestra di Gioco della partita e torno alla finestra di login iniziale
		Stage stage = (Stage)btnPartitaTornaAllaHome.getScene().getWindow();
		stage.close();

		//elimino i possibili bot in esecuzione
		if(prt.getGiocatoreCorrente() instanceof Bot) {
			t.interrupt();
		}

		//il metodo controlla se la partita si è conclusa e nel caso la elimina dal file
		SalvaPartita(prt);


		//riapro la finestra di Home
		try {
			VideoBackgroundPane videoBackgroundPane = new VideoBackgroundPane("src/v1.mp4");

			FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
			Parent root = loader.load();

			ControllerHome controller = loader.getController();

			controller.populateListView();
			controller.copiaInformazioniPartita(prt);

			StackPane stackPane = new StackPane();
			stackPane.setStyle("-fx-background-color: #38B6FF;"); // Imposta un colore di fallback bianco
			stackPane.getChildren().addAll(videoBackgroundPane, root);

			stage.setTitle("HOME");
			Scene interfacciaHome = new Scene(stackPane, 600, 400);

			stage.setScene(interfacciaHome);
			stage.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	//torno all'interfaccia del torneo
	@FXML public void TornaAlTorneo(ActionEvent actionEvent) {
		//chiudo la finestra di Gioco della partita e torno alla finestra del torneo
		Stage stage = (Stage)btnPartitaTornaAlTorneo.getScene().getWindow();
		stage.close();

//		//elimino i possibili bot in esecuzione
//		if(prt.getGiocatoreCorrente() instanceof Bot) {
//			t.interrupt();
//		}

		//riapro la finestra del torneo
		//controlla perchè non va riaperta ma solo aggiornata e riattivata
		try {
			//VideoBackgroundPane videoBackgroundPane = new VideoBackgroundPane("src/v1.mp4");

			FXMLLoader loader = new FXMLLoader(getClass().getResource("Torneo.fxml"));

			ResourceBundle rb = new ResourceBundle() {
				@Override
				protected Object handleGetObject(String key) {
					if (key.equals("Torneo")) { 
						switch (selettore) {
						case selettorePrt: 
							trn.getElencoPartite().set(posPartitaTrn, prt);
							break;
						case selettoreSFnl: 
							trn.getElencoSemifinali()[posPartitaTrn]=prt;
							break;
						case selettoreFnl: 
							trn.setFinale(prt);
							break;
						}
						return trn;
					}
					return null;
				}
				@Override
				public Enumeration<String> getKeys() {
					return Collections.enumeration(keySet());
				}
			};
			loader.setResources(rb);

			Parent root = loader.load();

			ControllerTorneo controller = loader.getController();

			StackPane stackPane = new StackPane();
			stackPane.setStyle("-fx-background-color: #38B6FF;"); // Imposta un colore di fallback bianco
			stackPane.getChildren().addAll(root);

			stage.setTitle("Torneo");
			Scene interfacciaTorneo = new Scene(stackPane, 720, 480);

			stage.setScene(interfacciaTorneo);
			stage.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	//creo un pop-up che visualizzi la classifica
	@FXML public void VisualizzaClassifica(ActionEvent actionEvent) {
		BorderPane root = new BorderPane();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("popUpClassifica.fxml"));
			root = loader.load();

			ControllerPopUpClassifica controller = loader.getController();
			controller.populateListView();

			Stage stage = new Stage();
			stage.setTitle("Classifica");
			Scene scene = new Scene(root);
			stage.setMinHeight(400);
			stage.setMinWidth(200);
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
		ControllerPartita.prt=tempPrt;
	}    

	//	//metodo che passa il numero di carte in fase di run-time da un istanza della classe controller all'altra
	//	public void copiaInformazioniNumCarte(int numeroCarteAGiocatore)
	//	{
	//		this.numeroCarteAGiocatore=numeroCarteAGiocatore;
	//	}

	//metodo che passa i dati della label lblTurnoGiocatore in fase di run-time da un istanza della classe controller all'altra
	public void copiaInformazioniLabel(Label lblTurnoGiocatore) {
		this.lblTurnoGiocatore.setText(lblTurnoGiocatore.getText());
	} 

	private void giocaCartaMano(int posCartaCliccata) {
		ArrayList<ImageView> listaCarteBanco = new ArrayList<ImageView>(Arrays.asList(imgCartaBanco1, imgCartaBanco2, imgCartaBanco3, imgCartaBanco4, imgCartaBanco5, imgCartaBanco6, imgCartaBanco7, imgCartaBanco8));
		ArrayList<ImageView> listaCarteMano = new ArrayList<ImageView>(Arrays.asList(imgCartaMano1, imgCartaMano2, imgCartaMano3, imgCartaMano4, imgCartaMano5));
		//controllo che il giocatore abbia iniziato il suo turno
		if(prt.isBtnInizioTurnoGiocatoreClicked()) {
			//"sposto" la carta giocata dalla mano al banco nel primo posto disponibile
			for(ImageView i : listaCarteBanco) {	
				if(i.getImage()==null) {
					//if(listaCarteMano.get(posCartaCliccata)!=null) {
					if(prt.getGiocatoreCorrente().getCarteMano().get(posCartaCliccata).getValore() == valCartaSpecialeAssoDenara && prt.getGiocatoreCorrente().getCarteMano().get(posCartaCliccata).getSpeciale() == 1 && !(prt.getGiocatoreCorrente() instanceof Bot))
					{
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Carta Speciale");
						alert.setHeaderText(null);
						alert.setContentText("Selezionare il valore della carta speciale");
						ButtonType buttonTypeMassimo = new ButtonType("Massimo");
						ButtonType buttonTypeMinimo = new ButtonType("Minimo");

						alert.getButtonTypes().setAll(buttonTypeMassimo, buttonTypeMinimo);
						Optional<ButtonType> result = alert.showAndWait();

						if (result.isPresent() && result.get() == buttonTypeMinimo) {
							CartaSpeciale cs = (CartaSpeciale)(prt.getGiocatoreCorrente().getCarteMano().get(posCartaCliccata));
							cs.setValore(0);
						}
					}else if(prt.getGiocatoreCorrente().getCarteMano().get(posCartaCliccata).getValore() != valCartaSpecialeAssoDenara && prt.getGiocatoreCorrente().getCarteMano().get(posCartaCliccata).getSpeciale() == 1 && !(prt.getGiocatoreCorrente() instanceof Bot)){
						prt.getGiocatoreCorrente().nVite = prt.getGiocatoreCorrente().nVite + 1;
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Carta Speciale");
						alert.setHeaderText(null);
						alert.setContentText("Hai vinto una VITA!!!");
						alert.showAndWait();
						lstViewVite.getItems().clear();
						mostraVite();
					}

					i.setImage(new Image(getClass().getResourceAsStream(prt.getGiocatoreCorrente().getCarteMano().get(posCartaCliccata).getPercorso())));
					listaCarteMano.get(posCartaCliccata).setImage(null);
					prt.setBtnFineTurnoGiocatoreDisable(false);
					btnFineTurnoGiocatore.setDisable(false);
					break;//ho inserito l'immagine nel tabellone quindi esco dal ciclo
				}
			}


			//rimuovo la carta dalla mano del gioccatore e la metto nella lista di carte del banco
			prt.lstCarteBancoAdd(prt.getGiocatoreCorrente().removeCartaMano(posCartaCliccata));
			//}	
			prt.setBtnInizioTurnoGiocatoreClicked(false);
		}
	}

	//metodo che calcola quale giocatore ha perso il round appena concluso
	private int CalcolaPunti(ArrayList<Carta> listaCarteBanco) {
		int posGiocatore=-1;
		Carta cartaTemp = null;
		//confronto le carte sul banco e trovo quella che ha il punteggio minore la sua posizione mi dirà la posizione del giocatore che ha giocato la carta e perderà la vita
		for(Carta i : listaCarteBanco) {
			if(posGiocatore==-1) {
				cartaTemp=i;
				posGiocatore=0;
			}else if(i.getValore()>cartaTemp.getValore()){
				cartaTemp=i;
				posGiocatore=listaCarteBanco.indexOf(i);
			}
		}

		return posGiocatore;
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

	private void cominciaNuovaMano() {
		//si è conclusa una mano quindi ne inizio un'altra se non c'è un vincitore
		//decremento se necessario il numero di carte
		int numeroCarteAGiocatore = prt.getNumeroCarteAGiocatore();

		if(numeroCarteAGiocatore>1) {
			numeroCarteAGiocatore--;
		}else if (numeroCarteAGiocatore==1) {//quando arrivo a una carta a giocatore rido le carte in base al numero di gioc atori
			numeroCarteAGiocatore = quanteCarteAGiocatore(prt.getElencoGiocatori().size());
		}else if (ControllerPartita.prt.getElencoGiocatori().size()==2) {
			numeroCarteAGiocatore=1;
		}

		if(ControllerPartita.prt.getElencoGiocatori().size()>1) {
			//inizio una nuova mano e do le carte a ogni giocatore
			mazzo=new Mazzo();
			mazzo.mescola();

			for(Giocatore g : ControllerPartita.prt.getElencoGiocatori()) {
				g.setCarteMano(mazzo.pescaCarte(numeroCarteAGiocatore));
			}

			btnIniziaNuovaMano.setVisible(true);
		}

		//salvo il numero di carte che si hanno in questo turno
		prt.setNumeroCarteAGiocatore(numeroCarteAGiocatore);
	}

	private void copriCarteGiocatore(boolean resume) {

		ArrayList<ImageView> listaCarteMano = new ArrayList<ImageView>(Arrays.asList(imgCartaMano1, imgCartaMano2, imgCartaMano3, imgCartaMano4, imgCartaMano5));
		//int posProssimoGiocatore=prt.getCountTurnoGiocatore()+1;
		int posProssimoGiocatore=resume?prt.getCountTurnoGiocatore():prt.getCountTurnoGiocatore()+1;
		//rimetto le carte coperte
		for(int i=0; i< listaCarteMano.size();i++) {
			if(posProssimoGiocatore<ControllerPartita.prt.getElencoGiocatori().size()&&i<prt.getGiocatore(posProssimoGiocatore).getCarteMano().size()) {
				listaCarteMano.get(i).setImage(new Image(getClass().getResourceAsStream(pathRetroCarta)));
			}else {
				listaCarteMano.get(i).setImage(null);
			}
		}
	}

	private void aggiornaClassifica(String path) {
		try{
			final int puntiVincitore=10;
			ArrayList<String> data = new ArrayList<String>();
			File file = new File(path);
			Scanner scan = new Scanner(file);			
			boolean presenzaGiocatore=false;
			while(scan.hasNext()) {
				String line = scan.nextLine();
				String[] lineData = line.split(" , ");
				//controllo il nome salvato su file e lo confronto col vincitore
				if(lineData[1].equals(ControllerPartita.prt.getElencoGiocatori().get(0).getNome())) {

					//incremento il punteggio
					lineData[0]=""+(puntiVincitore+Integer.parseInt(lineData[0]));
					line = lineData[0]+" , "+lineData[1]+"\n";
					presenzaGiocatore=true;
				}	
				//mi salvo la riga appena letta
				data.add(line+"\n");
			}
			scan.close();

			FileWriter fw = new FileWriter(file);
			//Riscrivo il file con le opportune modifiche
			for(String l : data) {
				fw.write(l);
			}
			if(!presenzaGiocatore) {
				fw.write(puntiVincitore+" , "+ControllerPartita.prt.getElencoGiocatori().get(0).getNome());
			}
			fw.close();
		} catch (FileNotFoundException FNFe) {
			// TODO Auto-generated catch block
			FNFe.printStackTrace();
		} catch (IOException IOe) {
			// TODO Auto-generated catch block
			IOe.printStackTrace();
		}
	}

	public void SalvaPartita(Partita partita) {
		try {
			Partita tempPrt=null;
			boolean presenzaPrt = false;
			partita.setResume(true);
			partita.setBtnInizioTurnoGiocatoreDisable(btnInizioTurnoGiocatore.isDisable());
			partita.setBtnFineTurnoGiocatoreDisable(btnFineTurnoGiocatore.isDisable());
			partita.setBtnIniziaNuovaManoVisible(btnIniziaNuovaMano.isVisible());
			partita.setBtnIniziaNuovoRoundVisible(btnIniziaNuovoRound.isVisible());
			partita.setLblVitaPersaText(lblVitaPersa.getText());

			GsonBuilder gsonBuilder = new GsonBuilder();
			//imposto un TypeAdapter per salvare correttamente l'elenco dei giocatori che contiene sia Bot che Giocatori
			gsonBuilder.registerTypeAdapter(new TypeToken<ArrayList<Giocatore>>() {}.getType(), new ElencoGiocatoriTypeAdapter());
			Gson gson=gsonBuilder.create();
			ArrayList<Partita> elencoPartite = new ArrayList<Partita>();
			String path="src/SalvataggioPartite.json";
			FileReader fr = new FileReader(path);
			JsonReader jsnReader=new JsonReader(fr);

			if(jsnReader.peek() != JsonToken.NULL){
				jsnReader.beginArray();
				//carico il contenuto del file
				while(jsnReader.hasNext()) {
					Partita p = gson.fromJson(jsnReader, Partita.class);
					elencoPartite.add(p);
				}
				jsnReader.endArray();
				jsnReader.close();

				//controllo se la partita da salvare era già presente nel file
				for(Partita p : elencoPartite) {
					if(p.getCodice().equals(partita.getCodice())) {
						presenzaPrt=true;
						tempPrt=p;
						break;
					}
				}

				//se la partita non era presente nel file la aggiungo in coda e la salvo
				if(!presenzaPrt) {
					elencoPartite.add(partita);
				}else if(tempPrt!=null){
					elencoPartite.remove(tempPrt);
					if(partita.getElencoGiocatori().size()>1) {
						elencoPartite.add(partita);
					}
				}

				//salvo la lista di partite caricate dal file e aggiornate
				FileWriter fw = new FileWriter(path);
				JsonWriter jsnWriter = new JsonWriter(fw);
				jsnWriter.beginArray();
				for (Partita p : elencoPartite) {
					gson.toJson(p, Partita.class, jsnWriter);
					fw.write('\n');
				}
				jsnWriter.endArray();
				jsnWriter.close();
			}
		} catch (FileNotFoundException fnfe) {
			// TODO Auto-generated catch block
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			// TODO Auto-generated catch block
			ioe.printStackTrace();
		}
	}

	private void dichiaraPreseSetVisible(boolean val) {
		gridPaneNumeroPrese.setVisible(val);
		lblPrese.setVisible(val);
		txtNumeroPrese.setVisible(val);
	}

	private void mostraPrese() {
		//mostro le prese dichiarate da chi lo ha fatto
		for(Giocatore g : prt.getElencoGiocatori()) {
			String prese = g.getPreseDichiarate()>=0?Integer.toString(g.getPreseDichiarate()):"*";
			lstViewPrese.getItems().add(g.getNome()+" "+prese+" prese");
		}		
	}

	private void mostraVite() {
		//mostro le vite di ogni giocatore
		for(Giocatore g : prt.getElencoGiocatori()) {
			lstViewVite.getItems().add(g.getNome()+" "+g.getVite()+" vite");
		}
		for(String nome : prt.getElencoGiocatoriEliminati()) {
			lstViewVite.getItems().add(nome+" è eliminato");
		}
	}

	private void mostraCarteGiocatore() {
		ArrayList<ImageView> listaCarteMano = new ArrayList<ImageView>(Arrays.asList(imgCartaMano1, imgCartaMano2, imgCartaMano3, imgCartaMano4, imgCartaMano5));
		//mostro le carte del giocatore corrente
		for(int i=0; i< listaCarteMano.size();i++) {
			int posGiocatore=prt.getCountTurnoGiocatore();
			if(posGiocatore<ControllerPartita.prt.getElencoGiocatori().size()&&i<prt.getGiocatore(posGiocatore).getCarteMano().size()) {
				Image newImg = new Image(getClass().getResourceAsStream(prt.getGiocatoreCorrente().getCarteMano().get(i).getPercorso()));
				listaCarteMano.get(i).setImage(newImg);
			}else {
				listaCarteMano.get(i).setImage(null);
			}
		}
	}

	private void mostraCarteBanco() {
		//mostro le carte sul banco
		ArrayList<ImageView> listaCarteBanco = new ArrayList<ImageView>(Arrays.asList(imgCartaBanco1, imgCartaBanco2, imgCartaBanco3, imgCartaBanco4, imgCartaBanco5, imgCartaBanco6, imgCartaBanco7, imgCartaBanco8));
		for(int i=0; i< prt.getLstCarteBanco().size();i++) {
			Image newImg = new Image(getClass().getResourceAsStream(prt.getLstCarteBanco().get(i).getPercorso()));
			listaCarteBanco.get(i).setImage(newImg);
		}
	}

	private void setInterface() {
		if(prt.isResume())	{//controllo se la partita era già stata iniziata
			//ripristino stato dei controlli
			btnInizioTurnoGiocatore.setDisable(prt.isBtnInizioTurnoGiocatoreDisable());
			btnFineTurnoGiocatore.setDisable(prt.isBtnFineTurnoGiocatoreDisable());
			btnIniziaNuovaMano.setVisible(prt.isBtnIniziaNuovaManoVisible());
			btnIniziaNuovoRound.setVisible(prt.isBtnIniziaNuovoRoundVisible());
			if(!btnIniziaNuovaMano.isVisible()&&!btnIniziaNuovoRound.isVisible()) {
				//ripristino stato delle carte
				if(btnInizioTurnoGiocatore.isDisable()) {
					mostraCarteGiocatore();
				}else {
					copriCarteGiocatore(true);
				}
			}else if(btnIniziaNuovaMano.isVisible()){
				lblVitaPersa.setVisible(true);
				lblVitaPersa.setText(prt.getLblVitaPersaText());
				lblTurnoGiocatore.setVisible(false);
			}else if(btnIniziaNuovoRound.isVisible()){
				lblTurnoGiocatore.setVisible(false);
			}

			mostraCarteBanco();		
		}else {
			copriCarteGiocatore(true);
		}

		//imposto lo stato per i controlli di dichiarazione delle prese
		lblPrese.setVisible(prt.isModalitaPrt());
		gridPaneNumeroPrese.setVisible(prt.isModalitaPrt());

		//mostro le vite di ogni giocatore
		mostraVite();
		//mostro le prese dichiarate da chi lo ha fatto
		mostraPrese();
	}
}