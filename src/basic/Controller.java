package basic;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Controller {
    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;
    
    @FXML
    private Button btnLogin;

    @FXML//login
    public void loginAction(ActionEvent actionEvent) {

        String username_text = txtUsername.getText();
        String password_text = txtPassword.getText();
        Amministratore a = new Amministratore(username_text, password_text);
        if(a.verificaAdmin())
        {
        	//se la password è corretta chiudo la finestra di login
        	Stage stage = (Stage)btnLogin.getScene().getWindow();
        	stage.close();
        	
        	//apro la finestra delle impostazioni e creazine partite
			Parent root;
			try {
				root = FXMLLoader.load(getClass().getResource("impostazioni.fxml"));
				stage.setTitle("Impostazioni");
				Scene interfacciaImpostazioni = new Scene(root);
				stage.setScene(interfacciaImpostazioni);
				stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }
    }
}