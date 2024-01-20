package basic;

import java.io.*;
import java.net.URL;
import java.util.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import javafx.beans.value.ObservableValue;

public class ControllerPopUpClassifica implements Initializable{
	//variabili di controllo
	int numeroCarteAGiocatore;
    final int lungCodicePartita=10;
    final int nViteDefault=5;
    Partita prt;
    Mazzo mazzo = new Mazzo();
    ArrayList<Giocatore> giocatoriPrt = new ArrayList<Giocatore>();
    int countTurnoGiocatore=0;
    boolean dichiaraPrese=true;
    boolean primoTurno=true;
    final String pathClassifica = "Documenti/Classifica.txt";
    
	@FXML TableView<LineClassifica> tblClassifica;
	@FXML TableColumn<LineClassifica, Integer> rankingTblClassifica;
	@FXML TableColumn<LineClassifica, Integer> ptTblClassifica;
	@FXML TableColumn<LineClassifica, String> nomiTblClassifica;


	/**
	 * carica la classifica e la visualizzo nell'apposita tableview
	 */
	public void caricaClassifica() {
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
		nomiTblClassifica.setCellValueFactory(new Callback<CellDataFeatures<LineClassifica, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<LineClassifica, String> cell) {
				// cell.getValue() returns the LineClassifica instance for a particular TableView row
				return cell.getValue().nomeProperty();
			}
		});

		rankingTblClassifica.setCellValueFactory(new Callback<CellDataFeatures<LineClassifica, Integer>, ObservableValue<Integer>>() {
			public ObservableValue<Integer> call(CellDataFeatures<LineClassifica, Integer> cell) {
				// cell.getValue() returns the LineClassificav instance for a particular TableView row
				return cell.getValue().rankingProperty().asObject();
			}
		});
		
		ptTblClassifica.setCellValueFactory(new Callback<CellDataFeatures<LineClassifica, Integer>, ObservableValue<Integer>>() {
			public ObservableValue<Integer> call(CellDataFeatures<LineClassifica, Integer> cell) {
				// cell.getValue() returns the LineClassifica instance for a particular TableView row
				return cell.getValue().puntiProperty().asObject();
			}
		});

		// Aggiunta dei dati alla TableView
		int counter=1;
		for(LineClassifica i : row) {
			i.setRanking(counter);
			tblClassifica.getItems().add(i);
			counter++;
		}

		//metto il contenuto della tableview in grassetto
		tblClassifica.setStyle("-fx-font-weight: bold;");
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		caricaClassifica();
	}
}