package basic;

import java.io.*;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class ControllerPopUpGiocatori {
	final String pathClassifica = "Documenti/Classifica.txt";

	@FXML ListView<String> lstGiocatori;
	@FXML Button btnElimina;
	@FXML Button btnSalva;

	@FXML public void salva(ActionEvent actionEvent) {

		List<String> items = lstGiocatori.getItems();
		try {
			File file = new File(pathClassifica);
			try (FileWriter writer = new FileWriter(file)) {
				for (String item : items) {
					writer.write(item + '\n');
				}
			}
		} catch (IOException e) {
			System.err.println("Errore: " + e.getMessage());
		}

		Stage stage = (Stage)btnSalva.getScene().getWindow();
		stage.close();


	}

	@FXML public void eliminaGiocatore(ActionEvent actionEvent) {

		lstGiocatori.getItems().remove(lstGiocatori.getSelectionModel().getSelectedItem()); 

	}

	// Metodi ausiliari
	public void caricaGiocatori() {

		try {
			File file = new File(pathClassifica);
			Scanner scan = new Scanner(file);			
			while(scan.hasNext()) {
				String line = scan.nextLine();
				lstGiocatori.getItems().add(line);	
			}
			scan.close();
		} catch (FileNotFoundException fnfe) {
			// TODO Auto-generated catch block
			fnfe.printStackTrace();
		}


		//metto il contenuto della listview in grassetto
		lstGiocatori.setStyle("-fx-font-weight: bold;");

		//centro le scritte all'interno della listview
		lstGiocatori.setCellFactory(param -> new ListCell<String>() {
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