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



public class ControllerPopUpAmministratori {

    String path = "src/Passwords.txt";
    
    
    @FXML ListView<String> lstAmministratori;
    @FXML Button btnAggiungi;
    @FXML Button btnElimina;
    @FXML Button btnSalva;
    @FXML TextField txtUsername; 
    @FXML TextField txtPassword; 

    @FXML public void salva(ActionEvent actionEvent) {
    	
        List<String> items = lstAmministratori.getItems();
        try (FileWriter writer = new FileWriter(path)) {
            for (String item : items) {
                writer.write(item + '\n');
            }
        } catch (IOException e) {
            System.err.println("Errore: " + e.getMessage());
        }

    	Stage stage = (Stage)btnSalva.getScene().getWindow();
    	stage.close();
    	
	
    }
    
    @FXML public void eliminaAmministratore(ActionEvent actionEvent) {
    	
	 lstAmministratori.getItems().remove(lstAmministratori.getSelectionModel().getSelectedItem()); 
    	
    }
 
    @FXML public void aggiungiAmministratore(ActionEvent actionEvent) {
 	
	 String username = txtUsername.getText();
	 String password = txtPassword.getText();
	 String line = username + " , " + password;
	 lstAmministratori.getItems().add(line);
    }
    
    // Metodi ausiliari
    public void caricaAmministratori() {
    	
 		try {
 			File file = new File(path);
 			Scanner scan = new Scanner(file);			
 			while(scan.hasNext()) {
 				String line = scan.nextLine();
 				lstAmministratori.getItems().add(line);	
 			}
 			scan.close();
 		} catch (FileNotFoundException fnfe) {
 			// TODO Auto-generated catch block
 			fnfe.printStackTrace();
 		}

 		//metto il contenuto della listview in grassetto
 		lstAmministratori.setStyle("-fx-font-weight: bold;");
 		
		//centro le scritte all'interno della listview
 		lstAmministratori.setCellFactory(param -> new ListCell<String>() {
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