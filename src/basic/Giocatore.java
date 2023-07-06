package basic;
import java.util.ArrayList;

public class Giocatore {
	String nome;
	int nVite;
	ArrayList<Carta> carte = new ArrayList<Carta>();
	long punteggio;
	
	public Giocatore(String nome, int nVite, ArrayList<Carta> carte, long punteggio) {
		this.nome=nome;
		this.nVite=nVite;
		this.carte=carte;
		this.punteggio=punteggio;
	}
}
