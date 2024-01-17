package basic;

import java.io.*;
import java.net.URL;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.application.Platform;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ButtonType;

public class ControllerTorneo implements Initializable{
	//variabili di controllo
	Torneo trn;
	int shiftPrtInterface;
	final int maxNumPrt=10;
	final int finaleATre=3;
	final String selettorePrt = "prt";
	final String selettoreSFnl = "semifinale";
	final String selettoreFnl = "finale";
	final String pathClassifica = "src/Classifica.txt";
	final String pathImgPrtSvolta = "/basic/IMGtorneo/verde.jpg";
	final String pathImgPrt = "/basic/IMGtorneo/nero.jpg";
	final String pathImgSemifinale = "/basic/IMGtorneo/argento.jpg";
	final String pathImgfinale = "/basic/IMGtorneo/oro.jpg";
	Partita prt;

	//variabili FXML
	@FXML ImageView imgPrt1;
	@FXML ImageView imgPrt2;
	@FXML ImageView imgPrt3;
	@FXML ImageView imgPrt4;
	@FXML ImageView imgPrt5;
	@FXML ImageView imgPrt6;
	@FXML ImageView imgPrt7;
	@FXML ImageView imgPrt8;
	@FXML ImageView imgPrt9;
	@FXML ImageView imgPrt10;
	@FXML ImageView imgSemifinale1;
	@FXML ImageView imgSemifinale2;
	@FXML ImageView imgFinale;
	@FXML GridPane gpPrt1;
	@FXML GridPane gpPrt2;
	@FXML GridPane gpPrt3;
	@FXML GridPane gpPrt4;
	@FXML GridPane gpPrt5;
	@FXML GridPane gpPrt6;
	@FXML GridPane gpPrt7;
	@FXML GridPane gpPrt8;
	@FXML GridPane gpPrt9;
	@FXML GridPane gpPrt10;
	@FXML GridPane gpSemifinale1;
	@FXML GridPane gpSemifinale2;
	@FXML GridPane gpFinale;
	@FXML Label lblVincitorePrt1;
	@FXML Label lblVincitorePrt2;
	@FXML Label lblVincitorePrt3;
	@FXML Label lblVincitorePrt4;
	@FXML Label lblVincitorePrt5;
	@FXML Label lblVincitorePrt6;
	@FXML Label lblVincitorePrt7;
	@FXML Label lblVincitorePrt8;
	@FXML Label lblVincitorePrt9;
	@FXML Label lblVincitorePrt10;
	@FXML Label lblVincitoreSemifinale1;
	@FXML Label lblVincitoreSemifinale2;
	@FXML Label lblVincitoreFinale;
	@FXML Line ln1;
	@FXML Line ln2;
	@FXML Line ln3;
	@FXML Line ln4;
	@FXML Line ln5;
	@FXML Line ln6;
	@FXML Line ln7;
	@FXML Line ln8;
	@FXML Line ln9;
	@FXML Line ln10;
	@FXML Line ln11;
	@FXML Line ln12;
	@FXML Line ln13;
	@FXML Line ln1semifinale1;
	@FXML Line ln2semifinale1;
	@FXML Line ln1semifinale2;
	@FXML Line ln2semifinale2;
	@FXML Button btnTorneoTornaAllaHome;
	@FXML Button btnClassifica;
	
	@Override
	public void initialize(URL arg0, ResourceBundle rbTorneo) {		
		trn=(Torneo)rbTorneo.getObject("Torneo");
		if(trn!=null){
			//sistemo opportunamente l'interfaccia
			setInterface();
		}else {
			System.out.println("errore");
		}
	}

	@FXML public void avviaPrt1(MouseEvent mouseEvent) {
		int pos = 0;
		avviaPartita(selettorePrt,pos-shiftPrtInterface);

	}

	@FXML public void avviaPrt2(MouseEvent mouseEvent) {
		int pos = 1;
		avviaPartita(selettorePrt,pos-shiftPrtInterface);
	}

	@FXML public void avviaPrt3(MouseEvent mouseEvent) {
		int pos = 2;
		avviaPartita(selettorePrt,pos-shiftPrtInterface);
	}

	@FXML public void avviaPrt4(MouseEvent mouseEvent) {
		int pos = 3;
		avviaPartita(selettorePrt,pos-shiftPrtInterface);
	}

	@FXML public void avviaPrt5(MouseEvent mouseEvent) {
		int pos = 4;
		avviaPartita(selettorePrt,pos-shiftPrtInterface);
	}

	@FXML public void avviaPrt6(MouseEvent mouseEvent) {
		int pos = 5;
		avviaPartita(selettorePrt,pos-shiftPrtInterface);
	}

	@FXML public void avviaPrt7(MouseEvent mouseEvent) {
		int pos = 6;
		avviaPartita(selettorePrt,pos-shiftPrtInterface);
	}

	@FXML public void avviaPrt8(MouseEvent mouseEvent) {
		int pos = 7;
		avviaPartita(selettorePrt,pos-shiftPrtInterface);
	}

	@FXML public void avviaPrt9(MouseEvent mouseEvent) {
		int pos = 8;
		avviaPartita(selettorePrt,pos-shiftPrtInterface);
	}

	@FXML public void avviaPrt10(MouseEvent mouseEvent) {
		int pos = 9;
		avviaPartita(selettorePrt,pos-shiftPrtInterface);
	}

	@FXML public void avviaSemifinale1(MouseEvent mouseEvent) {
		int pos = 0;
		if(trn.getElencoSemifinali()[pos]!=null) {
			avviaPartita(selettoreSFnl,pos);
		}
	}

	@FXML public void avviaSemifinale2(MouseEvent mouseEvent) {
		int pos = 1;
		if(trn.getElencoSemifinali()[pos]!=null) {
			avviaPartita(selettoreSFnl,pos);
		}
	}

	@FXML public void avviaFinale(MouseEvent mouseEvent) {
		if(trn.getFinale()!=null) {
			int pos = -1;
			avviaPartita(selettoreFnl,pos);
		}
	}

	//torno all' interfaccia di login
	@FXML public void TornaAllaHome(ActionEvent actionEvent) {
		//chiudo la finestra di Gioco del torneo e torno alla finestra di login iniziale
		//		Alert alert = new Alert(AlertType.ERROR);
		//		alert.setTitle("ATTENZIONE!");
		//		alert.setHeaderText(null);
		//		alert.setContentText("Sei sicuro di voler chiudere il torneo, NON potrai più riaprirlo!");
		//		ButtonType buttonTypeSi = new ButtonType("Sì");
		//		ButtonType buttonTypeNo = new ButtonType("No");
		//
		//		alert.getButtonTypes().setAll(buttonTypeSi, buttonTypeNo);
		//		Optional<ButtonType> result = alert.showAndWait();
		//
		//
		//
		//		if (result.isPresent() && result.get() == buttonTypeSi) {
		Stage stage = (Stage)btnTorneoTornaAllaHome.getScene().getWindow();
		stage.close();


		//il metodo controlla se la partita si è conclusa e nel caso la elimina dal file
		SalvaTorneo(trn);


		try {
			VideoBackgroundPane videoBackgroundPane = new VideoBackgroundPane("src/v1.mp4");

			FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
			Parent root = loader.load();

			ControllerHome controller = loader.getController();

			controller.caricaClassifica();

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
	
	//creo un pop-up che visualizzi la classifica
	@FXML public void VisualizzaClassifica(ActionEvent actionEvent) {
		BorderPane root = new BorderPane();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("popUpClassifica.fxml"));
			root = loader.load();

			loader.getController();

			Stage stage = new Stage();
			stage.setTitle("Classifica");
			Scene scene = new Scene(root);
			stage.setMinHeight(400);
			stage.setMinWidth(270);
			stage.setScene(scene);
			stage.show();			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}



	private void setInterface() {
		ImageView[] imgPrt = new ImageView[] {imgPrt1, imgPrt2, imgPrt3, imgPrt4, imgPrt5, imgPrt6, imgPrt7, imgPrt8, imgPrt9, imgPrt10};
		GridPane[] gpPrt = new GridPane[] {gpPrt1, gpPrt2, gpPrt3, gpPrt4, gpPrt5, gpPrt6, gpPrt7, gpPrt8, gpPrt9, gpPrt10};
		Label[] lblVincitoriPrt = new Label[] {lblVincitorePrt1, lblVincitorePrt2, lblVincitorePrt3, lblVincitorePrt4, lblVincitorePrt5, lblVincitorePrt6, lblVincitorePrt7, lblVincitorePrt8, lblVincitorePrt9, lblVincitorePrt10};
		Line[] lnPrt = new Line[] {ln1, ln2, ln3, ln4, ln5, ln6, ln7, ln8, ln9, ln10};
		Line[] lnPrtFinali=new Line[] {ln11, ln12, ln13};
		Line[] lnSemifinaliFinali= new Line[] {ln1semifinale1, ln2semifinale1, ln1semifinale2, ln2semifinale2};

		//controllo le partite per decidere se fare al primo turno direttamente le semifinali
		if(trn.getElencoPartite().size()>finaleATre) {//partite al turno 1
			Iterator<Partita> iterator = trn.getElencoPartite().iterator();
			ArrayList<Giocatore> giocatoriSFnl = new ArrayList<>();

			//calcolo lo shift delle partite in modo da centrarle nell'interfaccia
			shiftPrtInterface=(maxNumPrt-trn.getElencoPartite().size())/2;

			//visualizzo le partite del torneo
			for(int i =0; i<gpPrt.length;i++) {
				//se l'item della partita non è necessario lo nascondo
				if(i<shiftPrtInterface||i>=this.trn.getElencoPartite().size()+shiftPrtInterface) {
					gpPrt[i].setVisible(false);
					lnPrt[i].setVisible(false);
				}else{
					Partita prt = iterator.next();
					if(prt.getElencoGiocatori().size()==1) {
						//inserisco il nome del vincitore della partita se è già stata giocata
						lblVincitoriPrt[i].setText(prt.getElencoGiocatori().get(0).getNome());
						//cambio l'immagine rappresentativa della partita e ne disattivo l'evento
						imgPrt[i].setImage(new Image(pathImgPrtSvolta));
						imgPrt[i].setDisable(true);

						//salvo i giocatori per la semifinale
						giocatoriSFnl.add(prt.getElencoGiocatori().get(0));
					}
				}
			}

			System.out.println("numero giocatori sfnl: "+giocatoriSFnl.size());
			System.out.println("elenco semifinali: "+trn.getElencoSemifinali());
			if(trn.getElencoSemifinali()!=null) {
				System.out.println("semifinale1: "+trn.getElencoSemifinali()[0]);
				System.out.println("semifinale2: "+trn.getElencoSemifinali()[1]);
			}
			//inizializzo le semifinali
			if(trn.getElencoSemifinali()==null&&giocatoriSFnl.size()==trn.getElencoPartite().size()) {
				inizializzaSemiFinale(giocatoriSFnl);
			}

			//controllo le semifinali
			controlloInterfaceSemifinale();

			//controllo la finale
			controlloInterfaceFinale();
		}else if(trn.getElencoPartite().size()==finaleATre){//controllo se devo fare la finale a tre al secondo turno
			//non visualizzo le semifinali
			GridPane[] gpSemifinali = new GridPane[] {gpSemifinale1, gpSemifinale2};
			ArrayList<Giocatore> giocatoriFnl = new ArrayList<>();

			for(int i =0; i<gpSemifinali.length; i++) {
				//nascondo tutte le semifinali
				gpSemifinali[i].setVisible(false);
			}

			//nascondo le line semifinali to finali
			for(Line ln : lnSemifinaliFinali) {
				ln.setVisible(false);
			}

			//visualizzo le partite
			Iterator<Partita> iterator = trn.getElencoPartite().iterator();
			//calcolo lo shift delle partite in modo da centrarle nell'interfaccia
			shiftPrtInterface=(maxNumPrt-trn.getElencoPartite().size())/2;

			//visualizzo le partite del torneo
			for(int i =0; i<gpPrt.length;i++) {
				//se l'item della partita non è necessario lo nascondo
				if(i<shiftPrtInterface||i>=this.trn.getElencoPartite().size()+shiftPrtInterface) {
					gpPrt[i].setVisible(false);
				}else{
					Partita prt = iterator.next();
					if(prt.getElencoGiocatori().size()==1) {
						//inserisco il nome del vincitore della partita se è già stata giocata
						lblVincitoriPrt[i].setText(prt.getElencoGiocatori().get(0).getNome());
						//cambio l'immagine rappresentativa della partita e ne disattivo l'evento
						imgPrt[i].setImage(new Image(pathImgPrtSvolta));
						imgPrt[i].setDisable(true);

						//salvo il giocatore vincitore della partita per la finale
						giocatoriFnl.add(prt.getElencoGiocatori().get(0));
					}
					// visualizzo le line per le finali (prt to finali)
					lnPrtFinali[i-shiftPrtInterface].setVisible(true);
				}
				//nascondo le line prt to something
				lnPrt[i].setVisible(false);
			}

			if(giocatoriFnl.size()==finaleATre&&trn.getFinale()==null) {
				inizializzaFinale(giocatoriFnl);
			}

			//controllo la finale
			controlloInterfaceFinale();
		}else{//semifinali al turno 1
			//se le partite non sono iniziate le assegno alle semifinali
			//			if(trn.getElencoPartite().get(0).getElencoGiocatori().size()!=1&&trn.getElencoPartite().get(1).getElencoGiocatori().size()!=1) {
			if(trn.getElencoSemifinali()==null) {
				//inizializzo le semifinali
				Partita[] partite= new Partita[2];
				partite = trn.getElencoPartite().toArray(partite);
				trn.setElencoSemifinali(partite);
			}

			for(int i =0; i<gpPrt.length;i++) {
				//nascondo tutte le partite
				gpPrt[i].setVisible(false);
				lnPrt[i].setVisible(false);
			}

			//controllo le semifinali
			controlloInterfaceSemifinale();

			//controllo la finale
			controlloInterfaceFinale();
		}
	}



	private void controlloInterfaceSemifinale() {
		//controllo le semifinali
		Boolean flagFinale=trn.getFinale()!=null?false:true;
		ImageView[] imgSemifinale = new ImageView[] {imgSemifinale1, imgSemifinale2};
		Label[] lblVincitoriSemifinale = new Label[] {lblVincitoreSemifinale1, lblVincitoreSemifinale2};

		if(trn.getElencoSemifinali()!=null) {
			ArrayList<Giocatore> lstGiocatori=new ArrayList<Giocatore>();
			//visualizzo le semifinali del torneo
			for(int i = 0;i<imgSemifinale.length;i++) {
				if(trn.getElencoSemifinali()[i].getElencoGiocatori().size()==1) {
					//inserisco il nome del vincitore della semifinale se è già stata giocata
					lblVincitoriSemifinale[i].setText(trn.getElencoSemifinali()[i].getElencoGiocatori().get(0).getNome());
					//cambio l'immagine rappresentativa della semifinale e ne disattivo l'evento
					imgSemifinale[i].setImage(new Image(pathImgPrtSvolta));
					imgSemifinale[i].setDisable(true);

					//giocatori finale
					lstGiocatori.add(trn.getElencoSemifinali()[i].getElencoGiocatori().get(0));
				}else {
					flagFinale=false;
				}
			}

			if(flagFinale) {
				inizializzaFinale(lstGiocatori);
			}
		}
	}

	private void controlloInterfaceFinale() {
		//controllo la finale
		if(trn.getFinale()!=null) {
			if(trn.getFinale().getElencoGiocatori().size()==1) {
				//inserisco il nome del vincitore della finale se è già stata giocata
				lblVincitoreFinale.setText(trn.getFinale().getElencoGiocatori().get(0).getNome());
				//cambio l'immagine rappresentativa della finale e ne disattivo l'evento
				imgFinale.setImage(new Image(pathImgPrtSvolta));
				imgFinale.setDisable(true);
			}
		}
	}

	@FXML Label lblTurnoGiocatore;
	private void avviaPartita(String selettore,int p) {
		switch(selettore) {
		case selettorePrt:
			prt = trn.getElencoPartite().get(p);
			break;
		case selettoreSFnl:
			prt = trn.getElencoSemifinali()[p];
			break;
		case selettoreFnl:
			prt = trn.getFinale();
			break;
		}


		//chiudo la finestra di home e apro quella di gioco
		Stage stage = (Stage)imgPrt1.getScene().getWindow();
		stage.close();
		//apro la finestra di gioco
		BorderPane root = new BorderPane();
		try {
			FXMLLoader loader;
			loader = new FXMLLoader(getClass().getResource("Partita.fxml"));

			ResourceBundle rb = new ResourceBundle() {
				@Override
				protected Object handleGetObject(String key) {
					if (key.equals("Torneo")) {
						return trn;
					}else if(key.equals("PartitaTrn")) {
						return prt;
					}else if(key.equals("selettore")) {
						return selettore;
					}else if(key.equals("posPartitaTrn")) {
						return p;
					}

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
			Giocatore gio = prt.getGiocatoreCorrente();
			lblTurnoGiocatore = new Label("è il turno di: "+gio.getNome());
			Scene interfacciaDiGioco = new Scene(root);
			stage.setScene(interfacciaDiGioco);


			stage.show();

			//copio le informazioni relative alla partita in corso
			controller.copiaInformazioniPartita(prt);
			//copio le informazioni relative alla label lblTurnoGiocatore
			controller.copiaInformazioniLabel(lblTurnoGiocatore);


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	private void inizializzaFinale(ArrayList<Giocatore> giocatoriFnl) {
		//codice finale
		UUID uniqueID = UUID.randomUUID();
		//'p' sta per partita
		String uniqueCode = "p"+uniqueID.toString().replaceAll("-", "").substring(0, 8);

		//do le vite e le carte ai giocatori
		int nViteFinale = 3;
		for(Giocatore i : giocatoriFnl) {
			i.setVite(nViteFinale);
		}

		//imposto i dati della finale
		Partita prt = new Partita(uniqueCode, giocatoriFnl);

		//mazzo finale
		Mazzo m = new Mazzo();
		//do le carte a ogni giocatore
		m.setSpeciale();
		m.mescola();
		int numeroCarteAGiocatore=quanteCarteAGiocatore(giocatoriFnl.size());
		prt.setNumeroCarteAGiocatore(numeroCarteAGiocatore);//mi salvo il numero di carte che ho dato per i turni futuri
		for(Giocatore g : prt.getElencoGiocatori()) {
			g.setCarteMano(m.pescaCarte(numeroCarteAGiocatore));
		}

		//definisco la partita come appartenente a un torneo
		prt.setFlagTorneo(true);

		//inizializzo la finale
		trn.setFinale(prt);
	}

	private void inizializzaSemiFinale(ArrayList<Giocatore> giocatoriSFnl) {
		Partita[] SFnl= new Partita[2];

		//codice semifinale
		UUID uniqueID = UUID.randomUUID();
		//'p' sta per partita
		String uniqueCode1 = "p"+uniqueID.toString().replaceAll("-", "").substring(0, 8);
		uniqueID = UUID.randomUUID();
		String uniqueCode2 = "p"+uniqueID.toString().replaceAll("-", "").substring(0, 8);

		//do le vite e le carte ai giocatori
		int nViteFinale = 3;
		for(Giocatore i : giocatoriSFnl) {
			i.setVite(nViteFinale);
		}

		//divido i giocatori in due partite
		int bound = giocatoriSFnl.size()/2;
		ArrayList<Giocatore> giocatoriSFnl1=new ArrayList<Giocatore>(giocatoriSFnl.subList(0, bound));
		ArrayList<Giocatore> giocatoriSFnl2=new ArrayList<Giocatore>(giocatoriSFnl.subList(bound, giocatoriSFnl.size()));


		//imposto i dati della semifinale
		SFnl[0] = new Partita(uniqueCode1, giocatoriSFnl1);
		SFnl[1] = new Partita(uniqueCode2, giocatoriSFnl2);

		//mazzo semifinale1
		Mazzo m = new Mazzo();
		//do le carte a ogni giocatore
		m.setSpeciale();
		m.mescola();
		int numeroCarteAGiocatore=quanteCarteAGiocatore(giocatoriSFnl1.size());
		SFnl[0].setNumeroCarteAGiocatore(numeroCarteAGiocatore);//mi salvo il numero di carte che ho dato per i turni futuri
		for(Giocatore g : SFnl[0].getElencoGiocatori()) {
			g.setCarteMano(m.pescaCarte(numeroCarteAGiocatore));
		}

		//mazzo semifinale2
		m = new Mazzo();
		//do le carte a ogni giocatore
		m.setSpeciale();
		m.mescola();
		numeroCarteAGiocatore=quanteCarteAGiocatore(giocatoriSFnl1.size());
		SFnl[1].setNumeroCarteAGiocatore(numeroCarteAGiocatore);//mi salvo il numero di carte che ho dato per i turni futuri
		for(Giocatore g : SFnl[1].getElencoGiocatori()) {
			g.setCarteMano(m.pescaCarte(numeroCarteAGiocatore));
		}


		//definisco la partita come appartenente a un torneo
		SFnl[0].setFlagTorneo(true);
		SFnl[1].setFlagTorneo(true);

		//inizializzo le semifinali
		trn.setElencoSemifinali(SFnl);
	}

	public void SalvaTorneo(Torneo torneo) {
		try {
			Torneo tempTrn=null;
			boolean presenzaTrn = false;

			GsonBuilder gsonBuilder = new GsonBuilder();
			//imposto un TypeAdapter per salvare correttamente l'elenco dei giocatori che contiene sia Bot che Giocatori
			gsonBuilder.registerTypeAdapter(new TypeToken<ArrayList<Giocatore>>() {}.getType(), new ElencoGiocatoriTypeAdapter());
			Gson gson=gsonBuilder.create();
			ArrayList<Torneo> elencoTornei = new ArrayList<Torneo>();
			String path="src/SalvataggioTornei.json";
			FileReader fr = new FileReader(path);
			JsonReader jsnReader=new JsonReader(fr);

			if(jsnReader.peek() != JsonToken.NULL){
				jsnReader.beginArray();
				//carico il contenuto del file
				while(jsnReader.hasNext()) {
					Torneo t = gson.fromJson(jsnReader, Torneo.class);
					elencoTornei.add(t);
				}
				jsnReader.endArray();
				jsnReader.close();

				//controllo se il torneo da salvare era già presente nel file
				for(Torneo t : elencoTornei) {
					if(t.getCodice().equals(torneo.getCodice())) {
						presenzaTrn=true;
						tempTrn=t;
						break;
					}
				}

				//se il torneo non era presente nel file la aggiungo in coda e la salvo
				if(!presenzaTrn) {
					elencoTornei.add(torneo);
				}else if(tempTrn!=null){
					elencoTornei.remove(tempTrn);
					if(torneo.getFinale()!=null) {
						if(torneo.getFinale().getElencoGiocatori().size()>1) {
							elencoTornei.add(torneo);
						}
					}else {
						elencoTornei.add(torneo);
					}
				}

				//salvo la lista di tornei caricati dal file e aggiornati
				FileWriter fw = new FileWriter(path);
				JsonWriter jsnWriter = new JsonWriter(fw);
				jsnWriter.beginArray();
				for (Torneo t : elencoTornei) {
					gson.toJson(t, Torneo.class, jsnWriter);
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