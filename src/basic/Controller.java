package basic;
import java.awt.TextField;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {
	@FXML TextField username;
	@FXML TextField password;
	@FXML Label lblVerifica;

	public void contolloLogin(ActionEvent ae) {
		Amministratore a;
		boolean flag = false;
		do {
			String p = password.getText();
			//String u = username.getText();
			System.out.println(p);
			

			a = new Amministratore(p, p);
			flag = a.verificaAdmin();
		}while(!flag);
		
		if(a!=null) {
			lblVerifica.setText("verificato");
		}
	}
}

