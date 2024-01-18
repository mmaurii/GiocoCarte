package basic;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LineAmministratori{
	StringProperty nome = new SimpleStringProperty();
	StringProperty password = new SimpleStringProperty();

	public LineAmministratori(String nome, String password){
		setNome(nome);
    	setPassword(password);
	}

    public StringProperty nomeProperty() {
        return nome;
    }

	public final String getNome() {
		return nomeProperty().get();
	}

	public void setNome(String nome) {
		nomeProperty().set(nome);;
	}
	
    public StringProperty passwordProperty() {
        return password;
    }

	public final String getPassword() {
		return passwordProperty().get();
	}

	public void setPassword(String nome) {
		passwordProperty().set(nome);;
	}
	
	public String toString() {
		return getNome()+" , "+getPassword();
	}
}