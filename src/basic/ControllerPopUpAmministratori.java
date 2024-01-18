package basic;

import java.io.*;
import java.net.URL;
import java.util.*;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ControllerPopUpAmministratori implements Initializable {
	String path = "src/Passwords.txt";

	@FXML TableView<LineAmministratori> tblAmministratori;
	@FXML TableColumn<LineAmministratori, String> tblNomeAmministratori;
	@FXML TableColumn<LineAmministratori, String> tblPasswordAmministratori;
	@FXML Button btnAggiungi;
	@FXML Button btnElimina;
	@FXML Button btnSalva;
	@FXML TextField txtUsername; 
	@FXML TextField txtPassword; 
	@FXML Label lblErroreAmministratore;
	
	@FXML public void salva(ActionEvent actionEvent) {
		List<LineAmministratori> items = tblAmministratori.getItems();
		try (FileWriter writer = new FileWriter(path)) {
			for (LineAmministratori item : items) {
				writer.write(item.toString() + '\n');
			}
		} catch (IOException e) {
			System.err.println("Errore: " + e.getMessage());
		}

		Stage stage = (Stage)btnSalva.getScene().getWindow();
		stage.close();
	}

	@FXML public void eliminaAmministratore(ActionEvent actionEvent) {
		tblAmministratori.getItems().remove(tblAmministratori.getSelectionModel().getSelectedItem()); 
	}

	@FXML public void aggiungiAmministratore(ActionEvent actionEvent) {
		lblErroreAmministratore.setVisible(false);
		String username = txtUsername.getText();
		String password = txtPassword.getText();
		if(!containsSpecialCharacters(username)) {
			if(!password.contains(" , ")) {
				//controllo che non vengano inseriti giocatori con lo stesso nome all'interno della listview listUtentiPartita
				LineAmministratori line = new LineAmministratori(username, password);
				if(!username.trim().equals("") && !password.trim().equals("")) {
					if(!tblAmministratori.getItems().contains(line)) {
						txtUsername.clear();
						txtPassword.clear();
						tblAmministratori.getItems().add(line);
					}else {
						txtUsername.clear();
						txtPassword.clear();
						lblErroreAmministratore.setVisible(true);
						lblErroreAmministratore.setText("l'account esiste già, riprova cambiando i dati");
					}
				}else {
					txtUsername.clear();
					txtPassword.clear();
					lblErroreAmministratore.setVisible(true);
					lblErroreAmministratore.setText("completa l'inserimento dei dati");
				}
			}else {
				txtUsername.clear();
				txtPassword.clear();
				lblErroreAmministratore.setVisible(true);
				lblErroreAmministratore.setText("la password non può contenere: ' , '");
			}
		}else {
			txtUsername.clear();
			txtPassword.clear();
			lblErroreAmministratore.setVisible(true);
			lblErroreAmministratore.setText("il nome non può contenere: \"!@#$%^&*()-_=+[]{}|;:'\\\",.<>?/\"");
		}
	}

	// Metodi ausiliari
	public void caricaAmministratori() {
		// Associazione delle ObservableList alle TableColumn
		tblNomeAmministratori.setCellValueFactory(new Callback<CellDataFeatures<LineAmministratori, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<LineAmministratori, String> cell) {
				// p.getValue() returns the Person instance for a particular TableView row
				return cell.getValue().nomeProperty();
			}
		});
		
		tblPasswordAmministratori.setCellValueFactory(new Callback<CellDataFeatures<LineAmministratori, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<LineAmministratori, String> cell) {
				// p.getValue() returns the Person instance for a particular TableView row
				return cell.getValue().passwordProperty();
			}
		});

		// Aggiunta dei dati alla TableView
		try {
			File file = new File(path);
			Scanner scan = new Scanner(file);			
			while(scan.hasNext()) {//carico i dati dal file di testo
				String line = scan.nextLine();
				String[] lineItems = line.split(" , ");
				//salvo i dati caricati 
				tblAmministratori.getItems().add(new LineAmministratori((lineItems[0]), lineItems[1]));
			}
			scan.close();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}

		//metto il contenuto della tableview in grassetto
		tblAmministratori.setStyle("-fx-font-weight: bold;");
	} 

	private boolean containsSpecialCharacters(String text) {
		// Definisci qui l'insieme di caratteri speciali che vuoi controllare
		String specialCharacters = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/";

		for (char c : text.toCharArray()) {
			if (specialCharacters.contains(String.valueOf(c))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		caricaAmministratori();
	}
}