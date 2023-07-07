package basic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    String u = "admin";
    String p = "admin";

    @FXML
    public void loginAction(ActionEvent actionEvent) {

        String username_text = txtUsername.getText();
        String password_text = txtPassword.getText();

        if(username_text.compareTo(u) == 0 && password_text.compareTo(p) == 0)
        {
            txtUsername.setText("Loggato");
        }
    }
}