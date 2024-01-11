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

	@Override
	public void initialize(URL arg0, ResourceBundle rbTorneo) {
		// TODO Auto-generated method stub
		
		prt=(Partita)rbPartita.getObject("Partita");
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

}
