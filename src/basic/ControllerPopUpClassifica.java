package basic;

import java.io.*;
import java.net.URL;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.*;

import javax.security.auth.login.AccountNotFoundException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ObservableValue;

public class ControllerPopUpClassifica implements Initializable{
	//variabili di controllo
	int numeroCarteAGiocatore;
    final int lungCodicePartita=10;
    final int nViteDefault=5;
    Partita prt;
    Mazzo mazzo = new Mazzo();
    ArrayList<Giocatore> giocatoriPrt = new ArrayList<Giocatore>();
    String pathRetroCarta = "/basic/IMGcarte/retro.jpg";
    int countTurnoGiocatore=0;
    boolean dichiaraPrese=true;
    boolean primoTurno=true;
    String pathClassifica = "src/Classifica.txt";
    String pathStatus = "src/Status.txt";
    
	@FXML TableView<Line> tblClassifica;
	@FXML TableColumn<Line, Integer> rankingTblClassifica;
	@FXML TableColumn<Line, Integer> ptTblClassifica;
	@FXML TableColumn<Line, String> nomiTblClassifica;


	public void caricaClassifica() {
		ArrayList<Line> row = new ArrayList<>();
		try {
			File file = new File(pathClassifica);
			Scanner scan = new Scanner(file);	
			while(scan.hasNext()) {//carico i dati dal file di testo
				String line = scan.nextLine();
				String[] lineItems = line.split(" , ");
				row.add(new Line(Integer.parseInt(lineItems[0]), lineItems[1]));
			}
			scan.close();
		} catch (FileNotFoundException fnfe) {
			// TODO Auto-generated catch block
			fnfe.printStackTrace();
		}

		//ordino in base al punteggio la lista
		row.sort(null);	
        // Associazione delle ObservableList alle TableColumn
		nomiTblClassifica.setCellValueFactory(new Callback<CellDataFeatures<Line, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Line, String> cell) {
				// p.getValue() returns the Person instance for a particular TableView row
				return cell.getValue().nomeProperty();
			}
		});

		rankingTblClassifica.setCellValueFactory(new Callback<CellDataFeatures<Line, Integer>, ObservableValue<Integer>>() {
			public ObservableValue<Integer> call(CellDataFeatures<Line, Integer> cell) {
				// p.getValue() returns the Person instance for a particular TableView row
				return cell.getValue().rankingProperty().asObject();
			}
		});
		
		ptTblClassifica.setCellValueFactory(new Callback<CellDataFeatures<Line, Integer>, ObservableValue<Integer>>() {
			public ObservableValue<Integer> call(CellDataFeatures<Line, Integer> cell) {
				// p.getValue() returns the Person instance for a particular TableView row
				return cell.getValue().puntiProperty().asObject();
			}
		});

		// Aggiunta dei dati alla TableView
		int counter=1;
		for(Line i : row) {
			i.setRanking(counter);
			tblClassifica.getItems().add(i);
			counter++;
		}

		//metto il contenuto della tableview in grassetto
		tblClassifica.setStyle("-fx-font-weight: bold;");
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		caricaClassifica();
	}
}