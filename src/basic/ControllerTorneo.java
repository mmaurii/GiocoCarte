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
	final String pathClassifica = "src/Classifica.txt";
	final String pathImgPrtSvolta = "src/IMGtorneo/verde.jpg";
	final String pathImgPrt = "src/IMGtorneo/nero.jpg";
	final String pathImgSemifinale = "src/IMGtorneo/argento.jpg";
	final String pathImgfinale = "src/IMGtorneo/oro.jpg";

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
		avviaPartita(pos-shiftPrtInterface);

	}

	@FXML public void avviaPrt2(MouseEvent mouseEvent) {
		int pos = 1;
		avviaPartita(pos-shiftPrtInterface);
	}

	@FXML public void avviaPrt3(MouseEvent mouseEvent) {
		int pos = 2;
		avviaPartita(pos-shiftPrtInterface);
	}

	@FXML public void avviaPrt4(MouseEvent mouseEvent) {
		int pos = 3;
		avviaPartita(pos-shiftPrtInterface);
	}

	@FXML public void avviaPrt5(MouseEvent mouseEvent) {
		int pos = 4;
		avviaPartita(pos-shiftPrtInterface);
	}

	@FXML public void avviaPrt6(MouseEvent mouseEvent) {
		int pos = 5;
		avviaPartita(pos-shiftPrtInterface);
	}

	@FXML public void avviaPrt7(MouseEvent mouseEvent) {
		int pos = 6;
		avviaPartita(pos-shiftPrtInterface);
	}

	@FXML public void avviaPrt8(MouseEvent mouseEvent) {
		int pos = 7;
		avviaPartita(pos-shiftPrtInterface);
	}

	@FXML public void avviaPrt9(MouseEvent mouseEvent) {
		int pos = 8;
		avviaPartita(pos-shiftPrtInterface);
	}

	@FXML public void avviaPrt10(MouseEvent mouseEvent) {
		int pos = 9;
		avviaPartita(pos-shiftPrtInterface);
	}

	@FXML public void avviaSemifinale1(MouseEvent mouseEvent) {
		int pos = 0;
		avviaPartita(pos);
	}

	@FXML public void avviaSemifinale2(MouseEvent mouseEvent) {
		int pos = 1;
		avviaPartita(pos);
	}

	@FXML public void avviaFinale(MouseEvent mouseEvent) {
		int pos = 0;
		avviaPartita(pos);
	}

	//torno all' interfaccia di login
	@FXML public void TornaAllaHome(ActionEvent actionEvent) {
		//chiudo la finestra di Gioco della partita e torno alla finestra di login iniziale
		Stage stage = (Stage)btnTorneoTornaAllaHome.getScene().getWindow();
		stage.close();

		//riapro la finestra di Home
		try {
			VideoBackgroundPane videoBackgroundPane = new VideoBackgroundPane("src/v1.mp4");

			FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
			Parent root = loader.load();

			ControllerHome controller = loader.getController();

			controller.populateListView();

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
					}
				}
			}

			//inizializzo le semifinali
			Partita[] partite= new Partita[2];
			partite = trn.getElencoPartite().toArray(partite);
			trn.setElencoSemifinali(partite);

			//controllo le semifinali
			controlloInterfaceSemifinale();

			//controllo la finale
			controlloInterfaceFinale();
		}else if(trn.getElencoPartite().size()==finaleATre){//controllo se devo fare la finale a tre al secondo turno
			//non visualizzo le semifinali
			GridPane[] gpSemifinali = new GridPane[] {gpSemifinale1, gpSemifinale2};

			for(int i =0; i<gpSemifinali.length;i++) {
				//nascondo tutte le partite
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

					}
					// visualizzo le line per le finali (prt to finali)
					lnPrtFinali[i-shiftPrtInterface].setVisible(true);
				}
				//nascondo le line prt to something
				lnPrt[i].setVisible(false);
			}

		}else{//semifinali al turno 1

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
		ImageView[] imgSemifinale = new ImageView[] {imgSemifinale1, imgSemifinale2};
		Label[] lblVincitoriSemifinale = new Label[] {lblVincitoreSemifinale1, lblVincitoreSemifinale2};

		if(trn.getElencoSemifinali()!=null) {
			//visualizzo le semifinali del torneo
			for(int i = 0;i<imgSemifinale.length;i++) {
				if(trn.getElencoSemifinali()[i].getElencoGiocatori().size()==1) {
					//inserisco il nome del vincitore della semifinale se è già stata giocata
					lblVincitoriSemifinale[i].setText(trn.getElencoSemifinali()[i].getElencoGiocatori().get(0).getNome());
					//cambio l'immagine rappresentativa della semifinale e ne disattivo l'evento
					imgSemifinale[i].setImage(new Image(pathImgPrtSvolta));
					imgSemifinale[i].setDisable(true);
				}
			}
		}
	}

	private void controlloInterfaceFinale() {
		//controllo la finale
		if(trn.getFinale()!=null) {
			//inserisco il nome del vincitore della finale se è già stata giocata
			lblVincitoreFinale.setText(trn.getFinale().getElencoGiocatori().get(0).getNome());
			//cambio l'immagine rappresentativa della finale e ne disattivo l'evento
			imgFinale.setImage(new Image(pathImgPrtSvolta));
			imgFinale.setDisable(true);
		}
	}

	@FXML Label lblTurnoGiocatore;
	private void avviaPartita(int p) {

		Partita prt = trn.getElencoPartite().get(p);

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

}

