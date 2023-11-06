package basic;

import java.io.*;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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


public class ControllerPartita {
	//variabili di controllo
	int numeroCarteAGiocatore;
	final int lungCodicePartita=10;
	final int nViteDefault=5;
	static Partita prt;
	Mazzo mazzo = new Mazzo();
	ArrayList<Giocatore> giocatoriPrt = new ArrayList<Giocatore>();
	String pathRetroCarta = "/basic/IMGcarte/retro.jpg";
	int countTurnoGiocatore=0;
	int valCartaSpeciale=40;
	private ArrayList<Carta> lstCarteBanco = new ArrayList<Carta>();
	boolean dichiaraPrese=true;
	boolean primoTurno=true;
	boolean isLocked = false;
	String pathClassifica = "src/Classifica.txt";
	String pathStatus = "src/Status.txt";	
	
	@FXML BorderPane borderPanePartita;
	@FXML Label lblTurnoGiocatore;
	@FXML Label lblManoGiocatore;
	@FXML Button btnInizioTurnoGiocatore;
	@FXML ImageView imgCartaMano1;
	@FXML ImageView imgCartaMano2;
	@FXML ImageView imgCartaMano3;
	@FXML ImageView imgCartaMano4;
	@FXML ImageView imgCartaMano5;
	@FXML TextField txtNumeroPrese;
	@FXML GridPane gridPaneNumeroPrese=new GridPane();
	boolean btnInizioTurnoGiocatoreClicked=false;
	//inizia il turno dell'n giocatore
	@FXML public void inizioTurnoGiocatore(ActionEvent actionEvent) {
		lblTurnoGiocatore.setVisible(false);
		lblManoGiocatore.setVisible(true);
		btnInizioTurnoGiocatore.setDisable(true);
		if(!gridPaneNumeroPrese.isVisible()) {
			lblManoGiocatore.setText("Mano di "+ControllerPartita.prt.getElencoGiocatori().get(countTurnoGiocatore).getNome());
			btnInizioTurnoGiocatoreClicked=true;
		}else {
			lblManoGiocatore.setText(ControllerPartita.prt.getElencoGiocatori().get(countTurnoGiocatore).getNome()+" dichiara le prese");
			btnFineTurnoGiocatore.setDisable(false);
		}

		//mostro le carte in output relative al giocatore del turno corrente
		ArrayList<ImageView> listaCarteMano = new ArrayList<ImageView>(Arrays.asList(imgCartaMano1, imgCartaMano2, imgCartaMano3, imgCartaMano4, imgCartaMano5));
		for(int i = 0; i<listaCarteMano.size();i++) {	
			if(i<ControllerPartita.prt.getElencoGiocatori().get(countTurnoGiocatore).getCarteMano().size()) {
				Image newImg = new Image(getClass().getResourceAsStream(prt.getElencoGiocatori().get(countTurnoGiocatore).getCarteMano().get(i).getPercorso()));
				listaCarteMano.get(i).setImage(newImg);
			}else {
				listaCarteMano.get(i).setImage(null);
			}
		}
	}


	@FXML ImageView imgCartaBanco1;
	@FXML ImageView imgCartaBanco2;
	@FXML ImageView imgCartaBanco3;
	@FXML ImageView imgCartaBanco4;
	@FXML ImageView imgCartaBanco5;
	@FXML ImageView imgCartaBanco6;
	@FXML ImageView imgCartaBanco7;
	@FXML ImageView imgCartaBanco8;
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


	@FXML Button btnFineTurnoGiocatore;
	@FXML Label lblVitaPersa;
	@FXML Button btnIniziaNuovoRound;
	@FXML Label lblPrese;
	@FXML Label lblNumPreseNonValido;
	@FXML ListView<String> lstViewPrese;
	@FXML ListView<String> lstViewVite;
	int presePerQuestaMano=0;
	//dispongo la fine del turno per il giocatore corrente
	@FXML public void fineTurnoGiocatore(ActionEvent actionEvent) {
		lblNumPreseNonValido.setVisible(false);
		int numeroPreseGiocatore=0;
		int giocatoreChePrende;

		if(gridPaneNumeroPrese.isVisible()) {
			try {
				numeroPreseGiocatore=Integer.parseInt(txtNumeroPrese.getText());
				ControllerPartita.prt.getElencoGiocatori().get(countTurnoGiocatore).setPreseDichiarate(numeroPreseGiocatore);
				presePerQuestaMano+=numeroPreseGiocatore;

				//System.out.println(presePerQuestaMano+" != "+(Integer)(this.prt.getElencoGiocatori().get(countTurnoGiocatore).getCarteMano().size()+1)+" || "+(Integer)(countTurnoGiocatore+1)+" != "+this.prt.getElencoGiocatori().size());
				if(presePerQuestaMano!=ControllerPartita.prt.getElencoGiocatori().get(countTurnoGiocatore).getCarteMano().size()||countTurnoGiocatore!=ControllerPartita.prt.getElencoGiocatori().size()-1) {
					//visualizzo il numero di prese per questo giocatore
					lstViewPrese.getItems().add(ControllerPartita.prt.getElencoGiocatori().get(countTurnoGiocatore).getNome()+" "+numeroPreseGiocatore+" prese");
					if(dichiaraPrese) {
						//visualizzo il numero di vite di questo giocatore se è il primo turno
						if(primoTurno) {
							lstViewVite.getItems().add(ControllerPartita.prt.getElencoGiocatori().get(countTurnoGiocatore).getNome()+" "+ControllerPartita.prt.getElencoGiocatori().get(countTurnoGiocatore).getVite()+" vite");
						}

						//incremento il contatore dei giocatori
						countTurnoGiocatore++;
						//rimetto le carte coperte
						copriCarteGiocatore();

						if(countTurnoGiocatore>=ControllerPartita.prt.getElencoGiocatori().size()) {
							countTurnoGiocatore=0;
							//sistemo l'interfaccia per iniziare a giocare le carte
							gridPaneNumeroPrese.setVisible(false);
							lblPrese.setVisible(false);
							lblManoGiocatore.setVisible(false);
							btnIniziaNuovoRound.setVisible(true);
							btnFineTurnoGiocatore.setDisable(true);
							btnInizioTurnoGiocatore.setDisable(true);
						}else {
							//faccio dichiarare le prese al prossimo giocatore
							lblManoGiocatore.setVisible(false);
							btnFineTurnoGiocatore.setDisable(true);
							btnInizioTurnoGiocatore.setDisable(false);
							//definisco chi giocherà il prossimo turno
							lblTurnoGiocatore.setText("è il turno di: "+ControllerPartita.prt.getElencoGiocatori().get(countTurnoGiocatore).getNome());
							lblTurnoGiocatore.setVisible(true);
						}
					}
				}else {
					presePerQuestaMano-=numeroPreseGiocatore;
					lblNumPreseNonValido.setVisible(true);
				}
				txtNumeroPrese.setText("");
			}catch(NumberFormatException nfe){
				lblPrese.setTextFill(Color.RED);
				txtNumeroPrese.setText(null);
			}    
		}else {
			presePerQuestaMano=0;

			//if(lstViewPrese.getItems().size()-1==countTurnoGiocatore||!gridPaneNumeroPrese.isVisible()) {
			//rimetto le carte coperte
			copriCarteGiocatore();

			//controllo che non si sfori il numero di giocatori
			if(countTurnoGiocatore<prt.getElencoGiocatori().size()) {
				//passo il turno al prossimo giocatore incrementando il contatore
				countTurnoGiocatore++;
			}else {
				btnInizioTurnoGiocatore.setDisable(true);
			}

			//controllo che non si sia conclusa una mano
			if(ControllerPartita.prt.getElencoGiocatori().get(ControllerPartita.prt.getElencoGiocatori().size()-1).getCarteMano().size() != 0){
				//controllo che non si sia chiuso un giro di carte 
				if(countTurnoGiocatore<ControllerPartita.prt.getElencoGiocatori().size()) {
					//sistemo la visualizzazione dell'interfaccia
					lblManoGiocatore.setVisible(false);
					btnFineTurnoGiocatore.setDisable(true);
					btnInizioTurnoGiocatore.setDisable(false);
					//definisco chi giocherà il prossimo turno
					lblTurnoGiocatore.setText("è il turno di: "+ControllerPartita.prt.getElencoGiocatori().get(countTurnoGiocatore).getNome());
					lblTurnoGiocatore.setVisible(true);
				}else {
					//azzero il contatore dei turni
					countTurnoGiocatore=0;

					//Calcolo chi prende in base alle carte sul banco
					giocatoreChePrende = CalcolaPunti(lstCarteBanco);

					Giocatore gio = ControllerPartita.prt.getElencoGiocatori().get(giocatoreChePrende);
					ControllerPartita.prt.getElencoGiocatori().get(giocatoreChePrende).incrementaPreseEffettuate();
					lblVitaPersa.setText(gio.getNome()+" ha PRESO questo turno");
					lblVitaPersa.setVisible(true);
					lstCarteBanco.clear();
					lblManoGiocatore.setVisible(false);
					btnFineTurnoGiocatore.setDisable(true);
					btnIniziaNuovoRound.setVisible(true);

					//cambio l'ordine dei giocatori spostando il primo in fondo alla lista
					gio=ControllerPartita.prt.getElencoGiocatori().remove(0);
					ControllerPartita.prt.getElencoGiocatori().add(gio);
				}
			}else {
				//azzero il contatore dei turni
				countTurnoGiocatore=0;

				//Calcolo chi prende in base alle carte sul banco
				giocatoreChePrende = CalcolaPunti(lstCarteBanco);

				Giocatore gio = ControllerPartita.prt.getElencoGiocatori().get(giocatoreChePrende);
				ControllerPartita.prt.getElencoGiocatori().get(giocatoreChePrende).incrementaPreseEffettuate();
				lblVitaPersa.setText(gio.getNome()+" ha PRESO questo turno");
				lblVitaPersa.setVisible(true);
				lstCarteBanco.clear();
				lblManoGiocatore.setVisible(false);
				btnFineTurnoGiocatore.setDisable(true);

				btnIniziaNuovaMano.setVisible(true);

				//verifico chi ha sbagliato a dichiarare e gli rimuovo la vita
				lstViewVite.getItems().clear();
				for(Giocatore g : ControllerPartita.prt.getElencoGiocatori()) {	
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

				Iterator<Giocatore> g = ControllerPartita.prt.getElencoGiocatori().iterator();
				while(g.hasNext()) {
					if(g.next().getVite()==0) {
						g.remove();
					}
				}

				if(ControllerPartita.prt.getElencoGiocatori().size()>1) {//avvio una nuova mano
					cominciaNuovaMano();

					//cambio l'ordine dei giocatori spostando il primo in fondo alla lista
					gio=ControllerPartita.prt.getElencoGiocatori().remove(0);
					ControllerPartita.prt.getElencoGiocatori().add(gio);
				}else if (ControllerPartita.prt.getElencoGiocatori().size()==1){//concludo la partita e ne annuncio il vincitore 
					lblVitaPersa.setText(ControllerPartita.prt.getElencoGiocatori().get(0).getNome()+" ha VINTO la partita");
					btnIniziaNuovaMano.setVisible(false);

					//conteggio punti
					aggiornaClassifica(pathClassifica);
				}else{//concludo la partita e ne annuncio il pareggio
					lblVitaPersa.setText("la partita è finita in PAREGGIO");
					btnIniziaNuovaMano.setVisible(false);
				}
			}	
		}


		//controllo che la partita non sia conclusa
		if(ControllerPartita.prt.getElencoGiocatori().size()>1) {
			//se il prossimo giocatore che deve giocare è un bot lo avvio
			Giocatore gio = ControllerPartita.prt.getElencoGiocatori().get(countTurnoGiocatore);
			if(gio instanceof Bot) {
				Bot b = (Bot)gio;
	//			b.giocaTurno(this.prt);
				Thread t = new Thread(b);
				t.setDaemon(true);
				Platform.runLater(t);
			}
		}
	}


	@FXML public void fineTurnoGiocatoreControlloBot() {

	}


	@FXML public void IniziaNuovoRound(ActionEvent actionEvent) {
		//sistemo l'interfaccia perchè possa essere giocato un nuovo round
		primoTurno=false;
		btnIniziaNuovoRound.setVisible(false);
		lblVitaPersa.setVisible(false);
		lblTurnoGiocatore.setText("è il turno di: "+ControllerPartita.prt.getElencoGiocatori().get(countTurnoGiocatore).getNome());
		lblTurnoGiocatore.setVisible(true);
		btnInizioTurnoGiocatore.setDisable(false);

		ArrayList<ImageView> listaCarteBanco = new ArrayList<ImageView>(Arrays.asList(imgCartaBanco1, imgCartaBanco2, imgCartaBanco3, imgCartaBanco4, imgCartaBanco5, imgCartaBanco6, imgCartaBanco7, imgCartaBanco8));
		for(ImageView i : listaCarteBanco) {
			i.setImage(null);
		}

		//mostro le carte coperte del giocatore che deve iniziare il turno
		copriCarteGiocatore();
		
		//se il prossimo giocatore che deve giocare è un bot lo avvio
		Giocatore gio = ControllerPartita.prt.getElencoGiocatori().get(countTurnoGiocatore);
		if(gio instanceof Bot) {
			Bot b = (Bot)gio;
	//		b.giocaTurno(this.prt);
			Thread t = new Thread(b);
			t.setDaemon(true);
			Platform.runLater(t);
		}
	}
	

	@FXML Button btnIniziaNuovaMano;
	@FXML public void IniziaNuovaMano(ActionEvent actionEvent) {
		//sistemo l'interfaccia per poter iniziare la nuova mano
		dichiaraPrese=true;
		lblPrese.setTextFill(Color.BLACK);
		lstViewPrese.getItems().clear();
		lblVitaPersa.setVisible(false);
		btnIniziaNuovaMano.setVisible(false);
		lblTurnoGiocatore.setText("è il turno di: "+ControllerPartita.prt.getElencoGiocatori().get(countTurnoGiocatore).getNome());
		lblTurnoGiocatore.setVisible(true);
		btnInizioTurnoGiocatore.setDisable(false);
		gridPaneNumeroPrese.setVisible(true);
		lblPrese.setVisible(true);
		//rimetto le carte coperte;
		//copriCarteGiocatore();

		//elimino le carte dal banco
		ArrayList<ImageView> listaCarteBanco = new ArrayList<ImageView>(Arrays.asList(imgCartaBanco1, imgCartaBanco2, imgCartaBanco3, imgCartaBanco4, imgCartaBanco5, imgCartaBanco6, imgCartaBanco7, imgCartaBanco8));
		for(ImageView i : listaCarteBanco) {	
			i.setImage(null);
		}
		
		//se il prossimo giocatore che deve giocare è un bot lo avvio
		Giocatore gio = ControllerPartita.prt.getElencoGiocatori().get(countTurnoGiocatore);
		if(gio instanceof Bot) {
			Bot b = (Bot)gio;
		//	b.giocaTurno(this.prt);
			Thread t = new Thread(b);
			t.setDaemon(true);
			Platform.runLater(t);
		}
	}
	

	@FXML Button btnPartitaTornaAllaHome;
	//torno all' interfaccia di login
	@FXML public void TornaAllaHome(ActionEvent actionEvent) {
		//elimino i possibili bot in esecuzione

		//chiudo la finestra di Gioco della partita e torno alla finestra di login iniziale
		Stage stage = (Stage)btnPartitaTornaAllaHome.getScene().getWindow();
		stage.close();

		//stabilire come fare il salvataggio della partita
		//implementare salvataggio
		if(ControllerPartita.prt.getElencoGiocatori().size()>1) {
			SalvaPartita(ControllerPartita.prt);
		}

		//riapro la finestra di login


		try {

			//root = FXMLLoader.load(getClass().getResource("Login.fxml"));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
			Parent root = loader.load();
			ControllerHome controller = loader.getController();
			Scene interfacciaHome = new Scene(root);
			controller.copiaInformazioniPartita(ControllerPartita.prt);

			stage.setScene(interfacciaHome);
			stage.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@FXML Button btnClassifica;

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

	//metodo che passa il numero di carte in fase di run-time da un istanza della classe controller all'altra
	public void copiaInformazioniNumCarte(int numeroCarteAGiocatore)
	{
		this.numeroCarteAGiocatore=numeroCarteAGiocatore;
	}

	//metodo che passa i dati della label lblTurnoGiocatore in fase di run-time da un istanza della classe controller all'altra
	public void copiaInformazioniLabel(Label lblTurnoGiocatore) {
		this.lblTurnoGiocatore.setText(lblTurnoGiocatore.getText());
	} 

	private void giocaCartaMano(int posCartaCliccata) {
		ArrayList<ImageView> listaCarteBanco = new ArrayList<ImageView>(Arrays.asList(imgCartaBanco1, imgCartaBanco2, imgCartaBanco3, imgCartaBanco4, imgCartaBanco5, imgCartaBanco6, imgCartaBanco7, imgCartaBanco8));
		ArrayList<ImageView> listaCarteMano = new ArrayList<ImageView>(Arrays.asList(imgCartaMano1, imgCartaMano2, imgCartaMano3, imgCartaMano4, imgCartaMano5));
		//controllo che il giocatore abbia iniziato il suo turno
		if(btnInizioTurnoGiocatoreClicked) {
			//"sposto" la carta giocata dalla mano al banco nel primo posto disponibile
			for(ImageView i : listaCarteBanco) {	
				if(i.getImage()==null) {
					if(prt.getElencoGiocatori().get(countTurnoGiocatore).getCarteMano().get(posCartaCliccata).getValore() == valCartaSpeciale && prt.getElencoGiocatori().get(countTurnoGiocatore).getCarteMano().get(posCartaCliccata).getSpeciale() == 1 && !(prt.getElencoGiocatori().get(countTurnoGiocatore) instanceof Bot))
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
							CartaSpeciale cs = (CartaSpeciale)(prt.getElencoGiocatori().get(countTurnoGiocatore).getCarteMano().get(posCartaCliccata));
							cs.setValore(0);

			                
			            }
					}else if(prt.getElencoGiocatori().get(countTurnoGiocatore).getCarteMano().get(posCartaCliccata).getValore() != valCartaSpeciale && prt.getElencoGiocatori().get(countTurnoGiocatore).getCarteMano().get(posCartaCliccata).getSpeciale() == 1 && !(prt.getElencoGiocatori().get(countTurnoGiocatore) instanceof Bot))
					{
						prt.getElencoGiocatori().get(countTurnoGiocatore).nVite = prt.getElencoGiocatori().get(countTurnoGiocatore).nVite + 1;
						Alert alert = new Alert(AlertType.ERROR);
			            alert.setTitle("Carta Speciale");
			            alert.setHeaderText(null);
			            alert.setContentText("Hai vinto una VITA!!!");
			            alert.showAndWait();
						lstViewVite.getItems().clear();
						for(Giocatore g : ControllerPartita.prt.getElencoGiocatori()) {	
							lstViewVite.getItems().add(g.getNome()+" "+g.getVite()+" vite");
						}
					}

					i.setImage(new Image(getClass().getResourceAsStream(prt.getElencoGiocatori().get(countTurnoGiocatore).getCarteMano().get(posCartaCliccata).getPercorso())));
					listaCarteMano.get(posCartaCliccata).setImage(null);
					btnInizioTurnoGiocatoreClicked=false;
					btnFineTurnoGiocatore.setDisable(false);
					break;//ho inserito l'immagine nel tabellone quindi esco dal ciclo
				}
			}

			//rimuovo la carta dalla mano del gioccatore e la metto nella lista di carte del banco
			lstCarteBanco.add(ControllerPartita.prt.getElencoGiocatori().get(countTurnoGiocatore).removeCartaMano(posCartaCliccata));
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
		if(numeroCarteAGiocatore>1) {
			numeroCarteAGiocatore--;
		}else if (numeroCarteAGiocatore==1) {//quando arrivo a una carta a giocatore rido le carte in base al numero di gioc atori
			numeroCarteAGiocatore = quanteCarteAGiocatore(ControllerPartita.prt.getElencoGiocatori().size());
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
	}

	private void copriCarteGiocatore() {
		ArrayList<ImageView> listaCarteMano = new ArrayList<ImageView>(Arrays.asList(imgCartaMano1, imgCartaMano2, imgCartaMano3, imgCartaMano4, imgCartaMano5));
		//rimetto le carte coperte
		for(int i=0; i< listaCarteMano.size();i++) {
			if(countTurnoGiocatore+1<ControllerPartita.prt.getElencoGiocatori().size()&&i<ControllerPartita.prt.getElencoGiocatori().get(countTurnoGiocatore+1).getCarteMano().size()) {
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

	private void SalvaPartita(Partita partita) {
		try {
			boolean presenzaPrt = false;
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
						p=partita;
					}
				}

				//se la partita non era presente nel file la aggiungo in coda e la salvo
				if(!presenzaPrt) {
					elencoPartite.add(partita);
				}

				//salvo la lista di partite caricate dal file
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
}