package basic;

import java.io.*;
import java.util.*;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ControllerPopUpGiocatori {
	final String pathClassifica = "Documenti/Classifica.txt";

	@FXML ListView<String> lstGiocatori;
	@FXML Button btnElimina;
	@FXML Button btnSalva;
	@FXML TableView<LineClassifica> tblGiocatori;
	@FXML TableColumn<LineClassifica, String> tblNomi;
	@FXML TableColumn<LineClassifica, Integer> tblPunti;

	/**
	 * salva le modifiche appartate in unn file
	 * @param actionEvent
	 */
	@FXML public void salva(ActionEvent actionEvent) {
		List<LineClassifica> items = tblGiocatori.getItems();
		try {
			File file = new File(pathClassifica);
			try (FileWriter writer = new FileWriter(file)) {
				for (LineClassifica item : items) {
					writer.write(item.toString() + '\n');
				}
			}
		} catch (IOException e) {
			System.err.println("Errore: " + e.getMessage());
		}

		Stage stage = (Stage)btnSalva.getScene().getWindow();
		stage.close();
	}

	/**
	 * elimina il giocatore selezionato dalla visualizzazione, le modifiche non sono definitive
	 * @param actionEvent
	 */
	@FXML public void eliminaGiocatore(ActionEvent actionEvent) {
		tblGiocatori.getItems().remove(tblGiocatori.getSelectionModel().getSelectedItem()); 
	}

	// Metodi ausiliari
	/**
	 * carica i giocatori presenti nel file della classifica
	 */
	public void caricaGiocatori() {
		ArrayList<LineClassifica> row = new ArrayList<>();
		try {
			File file = new File(pathClassifica);
			Scanner scan = new Scanner(file);	
			while(scan.hasNext()) {//carico i dati dal file di testo
				String line = scan.nextLine();
				String[] lineItems = line.split(" , ");
				row.add(new LineClassifica(Integer.parseInt(lineItems[0]), lineItems[1]));
			}
			scan.close();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}

		//ordino in base al punteggio la lista
		row.sort(null);	
        // Associazione delle ObservableList alle TableColumn
		tblNomi.setCellValueFactory(new Callback<CellDataFeatures<LineClassifica, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<LineClassifica, String> cell) {
				// cell.getValue() returns the LineClassifica instance for a particular TableView row
				return cell.getValue().nomeProperty();
			}
		});
		
		tblPunti.setCellValueFactory(new Callback<CellDataFeatures<LineClassifica, Integer>, ObservableValue<Integer>>() {
			public ObservableValue<Integer> call(CellDataFeatures<LineClassifica, Integer> cell) {
				// cell.getValue() returns the LineClassifica instance for a particular TableView row
				return cell.getValue().puntiProperty().asObject();
			}
		});

		// Aggiunta dei dati alla TableView
		int counter=1;
		for(LineClassifica i : row) {
			i.setRanking(counter);
			tblGiocatori.getItems().add(i);
			counter++;
		}

		//metto il contenuto della tableview in grassetto
		tblGiocatori.setStyle("-fx-font-weight: bold;");
	} 
}