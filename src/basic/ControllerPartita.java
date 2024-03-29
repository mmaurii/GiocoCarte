package basic;

import java.io.*;
import java.net.URL;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.application.Platform;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
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
	final String pathClassifica = "Documenti/Classifica.txt";
	final String pathSalvataggioPartite = "Documenti/SalvataggioPartite.json";
	final String pathRetroCarta = "/basic/IMGcarte/retro.jpg";
	final String selettorePrt = "prt";
	final String selettoreSFnl = "semifinale";
	final String selettoreFnl = "finale";
	public Thread t;
	final String pareggio = "la partita è finita in PAREGGIO";

	//variabili FXML
	@FXML BorderPane borderPanePartita;
	@FXML Label lblTurnoGiocatore;
	@FXML Label lblManoGiocatore;
	@FXML Label lblVincitoreMano;
	@FXML Button btnInizioTurnoGiocatore;
	@FXML Pane imgCartaMano1;
	@FXML Pane imgCartaMano2;
	@FXML Pane imgCartaMano3;
	@FXML Pane imgCartaMano4;
	@FXML Pane imgCartaMano5;
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
	@FXML Pane imgCartaBanco1;
	@FXML Pane imgCartaBanco2;
	@FXML Pane imgCartaBanco3;
	@FXML Pane imgCartaBanco4; 
	@FXML Pane imgCartaBanco5;
	@FXML Pane imgCartaBanco6;
	@FXML Pane imgCartaBanco7;
	@FXML Pane imgCartaBanco8;

	@Override
	public void initialize(URL location, ResourceBundle rb) {
		try {//ottengo i dati per la partita
			trn=(Torneo)rb.getObject("Torneo");
			prt = (Partita)rb.getObject("PartitaTrn");
			posPartitaTrn=(int)rb.getObject("posPartitaTrn");
			selettore=(String) rb.getObject("selettore");
		}catch(MissingResourceException mre) {
			prt=(Partita)rb.getObject("Partita");
		}

		//sistemo opportunamente l'interfaccia
		setInterface();
		if(prt.getFlagTorneo()) {
			btnPartitaTornaAllaHome.setVisible(false);
			btnPartitaTornaAlTorneo.setVisible(true);
		}else {
			btnPartitaTornaAllaHome.setVisible(true);
			btnPartitaTornaAlTorneo.setVisible(false);
		}
	}


	/**
	 * inizia il turno dell'n giocatore
	 * @param actionEvent
	 */
	@FXML public void inizioTurnoGiocatore(ActionEvent actionEvent) {
		lblTurnoGiocatore.setVisible(false);
		lblManoGiocatore.setVisible(true);
		btnInizioTurnoGiocatore.setDisable(true);
		if(!prt.isModalitaPrt()) {//controllo se mi trovo in una fase di dichiazione delle pre o di gioco delle carte
			dichiaraPreseSetVisible(false);//gioca carte
			lblManoGiocatore.setText("Mano di "+prt.getGiocatoreCorrente().getNome());
			prt.setBtnInizioTurnoGiocatoreClicked(true);
		}else {
			dichiaraPreseSetVisible(true);//dichiara prese
			lblManoGiocatore.setText(prt.getGiocatoreCorrente().getNome()+" dichiara le prese");
			btnFineTurnoGiocatore.setDisable(false);
		}

		//mostro le carte in output relative al giocatore del turno corrente
		ArrayList<Pane> listaCarteMano = new ArrayList<Pane>(Arrays.asList(imgCartaMano1, imgCartaMano2, imgCartaMano3, imgCartaMano4, imgCartaMano5));
		for(int i = 0; i<listaCarteMano.size();i++) {//in base al numero di carte setto gli sfondi dei pane e quelli avanzati li setto a null
			if(i<prt.getGiocatoreCorrente().getCarteMano().size()) {
				Image backgroundImage = new Image(getClass().getResourceAsStream(prt.getGiocatoreCorrente().getCarteMano().get(i).getPercorso()));

				BackgroundImage background = new BackgroundImage(
						backgroundImage,
						BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT,
						BackgroundPosition.CENTER,
						new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false)
						);
				listaCarteMano.get(i).setBackground(new Background(background));
			}else {
				listaCarteMano.get(i).setBackground(null);
			}
		}
	}


	/**
	 * l'n giocatore gioca la carta1
	 * @param mouseEvent
	 */
	@FXML public void GiocaCartaMano1(MouseEvent mouseEvent) {
		final int posCartaCliccata=0;
		giocaCartaMano(posCartaCliccata);	
	}    


	/**
	 * l'n giocatore gioca la carta2
	 * @param mouseEvent
	 */
	@FXML public void GiocaCartaMano2(MouseEvent mouseEvent) {
		final int posCartaCliccata=1;
		giocaCartaMano(posCartaCliccata);
	}    


	/**
	 * l'n giocatore gioca la carta3
	 * @param mouseEvent
	 */
	@FXML public void GiocaCartaMano3(MouseEvent mouseEvent) {
		final int posCartaCliccata=2;
		giocaCartaMano(posCartaCliccata);	
	}    


	/**
	 * l'n giocatore gioca la carta4
	 * @param mouseEvent
	 */
	@FXML public void GiocaCartaMano4(MouseEvent mouseEvent) {
		final int posCartaCliccata=3;
		giocaCartaMano(posCartaCliccata);
	}    


	/**
	 * l'n giocatore gioca la carta5
	 * @param mouseEvent
	 */
	@FXML public void GiocaCartaMano5(MouseEvent mouseEvent) {
		final int posCartaCliccata=4;
		giocaCartaMano(posCartaCliccata);
	}


	/**
	 * dispongo la fine del turno per il giocatore corrente
	 * @param actionEvent
	 */
	@FXML public void fineTurnoGiocatore(ActionEvent actionEvent) {
		lblNumPreseNonValido.setVisible(false);
		lblPrese.setTextFill(Color.BLACK);
		int numeroPreseGiocatore=0;
		int giocatoreChePrende;

		if(prt.isModalitaPrt()) {//controllo se devo giocare le carte o dichiarare le prese
			try {
				numeroPreseGiocatore=Integer.parseInt(txtNumeroPrese.getText());
				prt.getGiocatoreCorrente().setPreseDichiarate(numeroPreseGiocatore);
				prt.presePerQuestaMano+=numeroPreseGiocatore;
				//controllo le prese per questa mano
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
						//incremento il contatore dei giocatori
						prt.setCountTurnoGiocatore(prt.getCountTurnoGiocatore()+1);

						//controllo se qualcuno deve ancora giocare
						if(prt.getCountTurnoGiocatore()>=prt.getElencoGiocatori().size()) {
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
					//definisco chi giocherà il prossimo turno
					lblTurnoGiocatore.setText("è il turno di: "+prt.getGiocatoreCorrente().getNome());
					lblTurnoGiocatore.setVisible(true);
				}else {
					//azzero il contatore dei turni
					prt.setCountTurnoGiocatore(0);

					//Calcolo chi prende in base alle carte sul banco
					giocatoreChePrende = CalcolaPunti(prt.getLstCarteBanco());

					//dichiaro chi ha preso questo turno
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

				//dichiaro chi ha preso questo turno
				Giocatore gio = prt.getElencoGiocatori().get(giocatoreChePrende);
				prt.getElencoGiocatori().get(giocatoreChePrende).incrementaPreseEffettuate();
				lblVitaPersa.setText(gio.getNome()+" ha PRESO questo turno");
				lblVitaPersa.setVisible(true);
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

				Giocatore vincitorePareggio = null;//mantengo l'ultimmo giocatore in elenco (passerà il turno nel caso di un torneo)
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

					//conteggio punti se il giocatore non è un bot
					if(!(prt.getElencoGiocatori().get(0) instanceof Bot)) {
						aggiornaClassifica();
					}
				}else{//concludo la partita e ne annuncio il pareggio
					lblVitaPersa.setText(pareggio);
					lblManoGiocatore.setVisible(false);
					lblTurnoGiocatore.setVisible(false);
					btnIniziaNuovaMano.setVisible(false);
					btnPartitaTornaAlTorneo.setDisable(false);

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
			}
		}
	}

	/**
	 * inizio un nuovo round dando la possibilità a un nuovo giocatore di dare le carte
	 * @param actionEvent
	 */
	@FXML public void IniziaNuovoRound(ActionEvent actionEvent) {
		//sistemo l'interfaccia perchè possa essere giocato un nuovo round
		//	prt.setPrimoTurno(false);
		btnIniziaNuovoRound.setVisible(false);
		lblVitaPersa.setVisible(false);
		lblTurnoGiocatore.setText("è il turno di: "+prt.getGiocatoreCorrente().getNome());
		lblTurnoGiocatore.setVisible(true);
		btnInizioTurnoGiocatore.setDisable(false);

		ArrayList<Pane> listaCarteBanco = new ArrayList<Pane>(Arrays.asList(imgCartaBanco1, imgCartaBanco2, imgCartaBanco3, imgCartaBanco4, imgCartaBanco5, imgCartaBanco6, imgCartaBanco7, imgCartaBanco8));

		for(Pane i : listaCarteBanco) {
			i.setBackground(null);
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
		}
	}

	/**
	 * inizio una nuova mano facendo dichiarare le prese ai giocatori
	 * @param actionEvent
	 */
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
		ArrayList<Pane> listaCarteBanco = new ArrayList<Pane>(Arrays.asList(imgCartaBanco1, imgCartaBanco2, imgCartaBanco3, imgCartaBanco4, imgCartaBanco5, imgCartaBanco6, imgCartaBanco7, imgCartaBanco8));
		for(Pane i : listaCarteBanco) {	
			i.setBackground(null);
		}
		prt.getLstCarteBanco().clear();

		//se il prossimo giocatore che deve giocare è un bot lo avvio
		Giocatore gio = prt.getGiocatoreCorrente();
		if(gio instanceof Bot) {
			Bot b = (Bot)gio;
			t = new Thread(b);
			t.setDaemon(true);
			Platform.runLater(t);
		}
	}


	/**
	 * chiude la partita e torna all' interfaccia di login
	 * @param actionEvent
	 */
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
			VideoBackgroundPane videoBackgroundPane = new VideoBackgroundPane("/v1.mp4");
			Thread.sleep(300);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
			Parent root = loader.load();

			ControllerHome controller = loader.getController();
			controller.caricaClassifica();

			StackPane stackPane = new StackPane();
			stackPane.setStyle("-fx-background-color: #38B6FF;"); // Imposta un colore di fallback
			stackPane.getChildren().addAll(videoBackgroundPane, root);
			stage.setTitle("HOME");
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
	 * chiudo la partita e torno all'interfaccia del torneo
	 * @param actionEvent
	 */
	@FXML public void TornaAlTorneo(ActionEvent actionEvent) {
		//chiudo la finestra di Gioco della partita e torno alla finestra del torneo
		Stage stage = (Stage)btnPartitaTornaAlTorneo.getScene().getWindow();
		stage.close();

		//modifico la partita giocata nel torneo
		if (selettore.equals(selettorePrt)) {
			trn.getElencoPartite().set(posPartitaTrn, prt);
		}else if(selettore.equals(selettoreSFnl)) {
			trn.getElencoSemifinali()[posPartitaTrn]=prt;
		}else if(selettore.equals(selettoreFnl)) { 
			if(lblVitaPersa.getText().equals(pareggio)) {//controllo se il torneo è finito in pareggio
				trn.setFinale(prt);
			}else {
				trn.setFinale(prt);
				trn.setVincitore(prt.getElencoGiocatori().get(0));
			}
		}

		//riapro la finestra del torneo
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Torneo.fxml"));

			ResourceBundle rb = new ResourceBundle() {//dati per inizializzare l'interfaccia del torneo
				@Override
				protected Object handleGetObject(String key) {
					if (key.equals("Torneo")) { 
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
			stackPane.setStyle("-fx-background-color: #38B6FF;"); // Imposta un colore di fallback
			stackPane.getChildren().addAll(root);
			stage.setTitle("Torneo");
			Scene interfacciaTorneo = new Scene(stackPane, 720, 480);
			stage.setScene(interfacciaTorneo);
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent e) {
					Platform.exit();
					if(stage.getScene().equals(interfacciaTorneo)) {//in questo modo controllo di salvare il torneo solo quando esco dall'interfaccia di gioco
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
	 * creo un pop-up che visualizzi la classifica
	 * @param actionEvent
	 */
	@FXML public void VisualizzaClassifica(ActionEvent actionEvent) {
		BorderPane root = new BorderPane();
		try {
			Stage parentStage = (Stage) btnClassifica.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("popUpClassifica.fxml"));
			root = loader.load();
			loader.getController();

			Stage stage = new Stage();
			stage.initOwner(parentStage);
			stage.initModality(Modality.APPLICATION_MODAL); 
			stage.setTitle("Classifica");
			Scene scene = new Scene(root);
			stage.setMinHeight(400);
			stage.setMinWidth(270);
			stage.setScene(scene);
			stage.showAndWait();			
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	//METODI AUSILIARI
	//metodo che passa i dati della label lblTurnoGiocatore in fase di run-time da una classe controller all'altra
	public void copiaInformazioniLabel(Label lblTurnoGiocatore) {
		this.lblTurnoGiocatore.setText(lblTurnoGiocatore.getText());
	} 

	/**
	 * gioca la carta cliccata dall'utente posizionandola sul banco nel primo posto disponibile
	 * @param posCartaCliccata
	 */
	private void giocaCartaMano(int posCartaCliccata) {
		ArrayList<Pane> listaCarteBanco = new ArrayList<Pane>(Arrays.asList(imgCartaBanco1, imgCartaBanco2, imgCartaBanco3, imgCartaBanco4, imgCartaBanco5, imgCartaBanco6, imgCartaBanco7, imgCartaBanco8));
		ArrayList<Pane> listaCarteMano = new ArrayList<Pane>(Arrays.asList(imgCartaMano1, imgCartaMano2, imgCartaMano3, imgCartaMano4, imgCartaMano5));
		//controllo che il giocatore abbia iniziato il suo turno
		if(prt.isBtnInizioTurnoGiocatoreClicked()) {
			//"sposto" la carta giocata dalla mano al banco nel primo posto disponibile
			for(Pane i : listaCarteBanco) {	
				if(i.getBackground()==null) {
					if(listaCarteMano.get(posCartaCliccata).getBackground()!=null) {
						if(prt.getGiocatoreCorrente().getCarteMano().get(posCartaCliccata).getValore() == valCartaSpecialeAssoDenara && prt.getGiocatoreCorrente().getCarteMano().get(posCartaCliccata).getSpeciale() == 1 && !(prt.getGiocatoreCorrente() instanceof Bot))
						{
							//assegno un valore alla carta speciale (asso di denara)
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
							}//la carta ha di default il valore massimo
						}else if(prt.getGiocatoreCorrente().getCarteMano().get(posCartaCliccata).getValore() != valCartaSpecialeAssoDenara && prt.getGiocatoreCorrente().getCarteMano().get(posCartaCliccata).getSpeciale() == 1 && !(prt.getGiocatoreCorrente() instanceof Bot)){
							prt.getGiocatoreCorrente().nVite = prt.getGiocatoreCorrente().nVite + 1;	//do una vita in più a chi ha pescato la carta speciale estratta a sorte
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Carta Speciale");
							alert.setHeaderText(null);
							alert.setContentText("Hai vinto una VITA!!!");
							alert.showAndWait();
							lstViewVite.getItems().clear();
							mostraVite();
						}

						//inizializzo la carta da visualizzare sul banco
						Image backgroundImage = new Image(getClass().getResourceAsStream(prt.getGiocatoreCorrente().getCarteMano().get(posCartaCliccata).getPercorso()));
						BackgroundImage background = new BackgroundImage(
								backgroundImage,
								BackgroundRepeat.NO_REPEAT,
								BackgroundRepeat.NO_REPEAT,
								BackgroundPosition.CENTER,
								new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false)
								);

						i.setBackground(new Background(background));
						listaCarteMano.get(posCartaCliccata).setBackground(null);
						prt.setBtnFineTurnoGiocatoreDisable(false);
						btnFineTurnoGiocatore.setDisable(false);

						//rimuovo la carta dalla mano del gioccatore e la metto nella lista di carte del banco
						Carta c = prt.getGiocatoreCorrente().removeCartaMano(posCartaCliccata);
						prt.lstCarteBancoAdd(c);
						//}	
						prt.setBtnInizioTurnoGiocatoreClicked(false);
						break;//ho inserito l'immagine nel tabellone quindi esco dal ciclo	
					}
				}
			}
		}
	}

	/**
	 * metodo che calcola quale giocatore ha perso nel round appena concluso
	 * @param listaCarteBanco
	 * @return pos giocatore
	 */
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
	 * inizia una nuova mano se non c'è un vincitore, distribuisce le carte ai giocatori
	 */
	private void cominciaNuovaMano() {
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

	/**
	 * mette le carte coperte al giocatore corrente dopo aver controllato se la partita era stata già avviata
	 * @param resume
	 */
	private void copriCarteGiocatore(boolean resume) {
		ArrayList<Pane> listaCarteMano = new ArrayList<Pane>(Arrays.asList(imgCartaMano1, imgCartaMano2, imgCartaMano3, imgCartaMano4, imgCartaMano5));
		//int posProssimoGiocatore=prt.getCountTurnoGiocatore()+1;
		int posProssimoGiocatore=resume?prt.getCountTurnoGiocatore():prt.getCountTurnoGiocatore()+1;
		//rimetto le carte coperte
		for(int i=0; i< listaCarteMano.size();i++) {
			if(posProssimoGiocatore<ControllerPartita.prt.getElencoGiocatori().size()&&i<prt.getGiocatore(posProssimoGiocatore).getCarteMano().size()) {

				Image backgroundImage = new Image(getClass().getResourceAsStream(pathRetroCarta));

				BackgroundImage background = new BackgroundImage(
						backgroundImage,
						BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT,
						BackgroundPosition.CENTER,
						new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false)
						);


				listaCarteMano.get(i).setBackground(new Background(background));
			}else {
				listaCarteMano.get(i).setBackground(null);
			}
		}
	}

	/**
	 * aggiorna i punteggi della classifica e li salva nel file di testo dedicato
	 */
	private void aggiornaClassifica() {
		try{
			final int puntiVincitore=10;
			ArrayList<String> data = new ArrayList<String>();
			File file = new File(pathClassifica);
			Scanner scan = new Scanner(file);			
			boolean presenzaGiocatore=false;
			while(scan.hasNext()) {
				String line = scan.nextLine();
				String[] lineData = line.split(" , ");
				//controllo il nome salvato su file e lo confronto col vincitore
				if(lineData[1].equals(prt.getElencoGiocatori().get(0).getNome())) {
					//incremento il punteggio
					lineData[0]=""+(puntiVincitore+Integer.parseInt(lineData[0]));
					line = lineData[0]+" , "+lineData[1];
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
			FNFe.printStackTrace();
		} catch (IOException IOe) {
			IOe.printStackTrace();
		}
	}

	/**
	 * salva la partita in un file '.json'
	 * @param partita
	 */
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

			File file1 = new File(pathSalvataggioPartite);

			FileReader fr = new FileReader(file1);
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
				FileWriter fw = new FileWriter(file1);
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
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * mostra o nasconde l'interfaccia di dichiarazione delle prese in base al parametr booleano val
	 * @param val
	 */
	private void dichiaraPreseSetVisible(boolean val) {
		gridPaneNumeroPrese.setVisible(val);
		lblPrese.setVisible(val);
		txtNumeroPrese.setVisible(val);
	}

	/**
	 * mostra le prese dichiarate da chi lo ha già fatto se no visualizza '*'
	 */
	private void mostraPrese() {
		//mostro le prese dichiarate da chi lo ha fatto
		for(Giocatore g : prt.getElencoGiocatori()) {
			String prese = g.getPreseDichiarate()>=0?Integer.toString(g.getPreseDichiarate()):"*";
			lstViewPrese.getItems().add(g.getNome()+" "+prese+" prese");
		}		
	}

	/**
	 * mostra le vite di ogni giocatore o se qualche d'uno è stato eliminato
	 */
	private void mostraVite() {
		//mostro le vite di ogni giocatore
		for(Giocatore g : prt.getElencoGiocatori()) {
			lstViewVite.getItems().add(g.getNome()+" "+g.getVite()+" vite");
		}
		for(String nome : prt.getElencoGiocatoriEliminati()) {
			lstViewVite.getItems().add(nome+" è eliminato");
		}
	}

	/**
	 * mostra le carte della mano del giocatore corrente
	 */
	private void mostraCarteGiocatore() {
		ArrayList<Pane> listaCarteMano = new ArrayList<Pane>(Arrays.asList(imgCartaMano1, imgCartaMano2, imgCartaMano3, imgCartaMano4, imgCartaMano5));
		//mostro le carte del giocatore corrente
		for(int i=0; i< listaCarteMano.size();i++) {
			int posGiocatore=prt.getCountTurnoGiocatore();
			if(posGiocatore<ControllerPartita.prt.getElencoGiocatori().size()&&i<prt.getGiocatore(posGiocatore).getCarteMano().size()) {

				Image backgroundImage = new Image(getClass().getResourceAsStream(prt.getGiocatoreCorrente().getCarteMano().get(i).getPercorso()));

				BackgroundImage background = new BackgroundImage(
						backgroundImage,
						BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT,
						BackgroundPosition.CENTER,
						new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false)
						);

				listaCarteMano.get(i).setBackground(new Background(background));
			}else {
				listaCarteMano.get(i).setBackground(null);
			}
		}
	}

	/**
	 * mostra le carte presenti sul banco
	 */
	private void mostraCarteBanco() {
		//mostro le carte sul banco
		ArrayList<Pane> listaCarteBanco = new ArrayList<Pane>(Arrays.asList(imgCartaBanco1, imgCartaBanco2, imgCartaBanco3, imgCartaBanco4, imgCartaBanco5, imgCartaBanco6, imgCartaBanco7, imgCartaBanco8));
		for(int i=0; i< prt.getLstCarteBanco().size();i++) {

			Image backgroundImage = new Image(getClass().getResourceAsStream(prt.getLstCarteBanco().get(i).getPercorso()));

			BackgroundImage background = new BackgroundImage(
					backgroundImage,
					BackgroundRepeat.NO_REPEAT,
					BackgroundRepeat.NO_REPEAT,
					BackgroundPosition.CENTER,
					new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false)
					);

			listaCarteBanco.get(i).setBackground(new Background(background));
		}
	}

	/**
	 * imposta l'interfaccia della partita corrente adattandola in base allo stato e al momento in cui 
	 * si trova la partita stessa
	 */
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