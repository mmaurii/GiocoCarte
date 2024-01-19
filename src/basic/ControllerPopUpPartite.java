package basic;

import java.io.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.*;

import javax.security.auth.login.AccountNotFoundException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;



public class ControllerPopUpPartite {

	String path = "Documenti/SalvataggioPartite.json";
	File file = new File(path);

    ArrayList<Partita> elencoPartite = new ArrayList<Partita>();
    Gson gson;
    
    @FXML ListView<String> lstPartite;
    @FXML Button btnElimina;
    @FXML Button btnSalva;

    @FXML public void salva(ActionEvent actionEvent) {
    	
    	
				try {
					FileWriter fw = new FileWriter(file);
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
				
						
				Stage stage = (Stage)btnSalva.getScene().getWindow();
		    	stage.close();	
    	
	
    }
    
    @FXML public void eliminaPartita(ActionEvent actionEvent) {
    	
	 String cod = lstPartite.getSelectionModel().getSelectedItem();
	 int pos = lstPartite.getItems().indexOf(cod);
	 lstPartite.getItems().remove(pos); 
	 elencoPartite.remove(pos);
    }
    
    // Metodi ausiliari
    public void caricaPartite() {
    	
 		try {
 			 GsonBuilder gsonBuilder = new GsonBuilder();

 			gsonBuilder.registerTypeAdapter(new TypeToken<ArrayList<Giocatore>>() {}.getType(), new ElencoGiocatoriTypeAdapter());
 			gson = gsonBuilder.create();
 		
 			FileReader fr = new FileReader(file);
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
			}
 			
 			for(Partita p : elencoPartite) 
 	 		{
 				lstPartite.getItems().add(p.getCodice());
 	 		}
 			
 		} catch (FileNotFoundException fnfe) {
			// TODO Auto-generated catch block
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			// TODO Auto-generated catch block
			ioe.printStackTrace();
		}

 		
 		
 		//metto il contenuto della listview in grassetto
 		lstPartite.setStyle("-fx-font-weight: bold;");
 		
		//centro le scritte all'interno della listview
 		lstPartite.setCellFactory(param -> new ListCell<String>() {
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
}