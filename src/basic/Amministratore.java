package basic;

import java.util.ArrayList;

public class Amministratore extends Giocatore{
	String password;
	public Amministratore(String nome, String password, int nVite, ArrayList<Carta> carte, long punteggio) {
		super(nome, nVite, carte, punteggio);
		this.password=password;
	}
}
