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

public class ControllerTorneo implements Initializable{
	//variabili di controllo
	Torneo trn;
	int shiftPrtInterface;
	final int maxNumPrt=10;
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
		
		//calcolo lo shift delle partite in modo da centrarle nell'interfaccia
		shiftPrtInterface=(maxNumPrt-trn.getElencoPartite().size())/2;
		//visualizzo le partite del torneo
		for(int i =0; i<gpPrt.length;i++) {
			if(i<shiftPrtInterface||i>=this.trn.getElencoPartite().size()+shiftPrtInterface) {
				gpPrt[i].setVisible(false);
			}
			
			if(trn.getElencoPartite().get(i).getElencoGiocatori().size()==1) {
				lblVincitoriPrt[i].setText(trn.getElencoPartite().get(i).getElencoGiocatori().get(0).getNome());
			}
		}
	}
}
