package basic;

import java.io.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class ControllerPopUpTornei {
    String path = "src/SalvataggioTornei.json";
    ArrayList<Torneo> elencoTornei = new ArrayList<Torneo>();
    Gson gson;
    
    @FXML ListView<String> lstTornei;
    @FXML Button btnElimina;
    @FXML Button btnSalva;

    @FXML public void salvaTorneo(ActionEvent actionEvent) {
				try {
					FileWriter fw = new FileWriter(path);
					JsonWriter jsnWriter = new JsonWriter(fw);
					
					jsnWriter.beginArray();
					for (Torneo t : elencoTornei) {
						gson.toJson(t, Torneo.class, jsnWriter);
						fw.write('\n');
					}
					jsnWriter.endArray();
					jsnWriter.close();
					
				} catch (FileNotFoundException fnfe) {
					fnfe.printStackTrace();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				
				Stage stage = (Stage)btnSalva.getScene().getWindow();
		    	stage.close();
    }
    
    @FXML public void eliminaTorneo(ActionEvent actionEvent) {
    	
	 String cod = lstTornei.getSelectionModel().getSelectedItem();
	 int pos = lstTornei.getItems().indexOf(cod);
	 lstTornei.getItems().remove(pos); 
	 elencoTornei.remove(pos);
    }
    
    // Metodi ausiliari
    public void caricaTornei() {
 		try {
 			GsonBuilder gsonBuilder = new GsonBuilder();
 			gsonBuilder.registerTypeAdapter(new TypeToken<ArrayList<Giocatore>>() {}.getType(), new ElencoGiocatoriTypeAdapter());
 			gson = gsonBuilder.create();
 		
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
			}
 			
 			for(Torneo t : elencoTornei) 
 	 		{
 				lstTornei.getItems().add(t.getCodice());
 	 		}
 		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

 		
 		//metto il contenuto della listview in grassetto
 		lstTornei.setStyle("-fx-font-weight: bold;");
 		
		//centro le scritte all'interno della listview
 		lstTornei.setCellFactory(param -> new ListCell<String>() {
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