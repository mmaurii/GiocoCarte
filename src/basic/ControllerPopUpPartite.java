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

public class ControllerPopUpPartite {
	final String pathSalvataggioPartite = "Documenti/SalvataggioPartite.json";
    ArrayList<Partita> elencoPartite = new ArrayList<Partita>();
    Gson gson;
    
    @FXML ListView<String> lstPartite;
    @FXML Button btnElimina;
    @FXML Button btnSalva;

	/**
	 * salva le modifiche appartate in unn file
	 * @param actionEvent
	 */
    @FXML public void salva(ActionEvent actionEvent) {
				try {
					File file = new File(pathSalvataggioPartite);
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
					fnfe.printStackTrace();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
						
				Stage stage = (Stage)btnSalva.getScene().getWindow();
		    	stage.close();	
    }
    
	/**
	 * elimina la partita selezionata dalla visualizzazione, le modifiche non sono definitive
	 * @param actionEvent
	 */
    @FXML public void eliminaPartita(ActionEvent actionEvent) {
	 String cod = lstPartite.getSelectionModel().getSelectedItem();
	 int pos = lstPartite.getItems().indexOf(cod);
	 lstPartite.getItems().remove(pos); 
	 elencoPartite.remove(pos);
    }
    
    // Metodi ausiliari
    /**
     * carica le partite dall'apposito file di testo e le visualizza
     */
    public void caricaPartite() {
 		try {
 			 GsonBuilder gsonBuilder = new GsonBuilder();

 			gsonBuilder.registerTypeAdapter(new TypeToken<ArrayList<Giocatore>>() {}.getType(), new ElencoGiocatoriTypeAdapter());
 			gson = gsonBuilder.create();
 			File file = new File(pathSalvataggioPartite);
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
			fnfe.printStackTrace();
		} catch (IOException ioe) {
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