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

	public Giocatore(String nome) {
		this.nome=nome;
	}
	
	public void setVite(int nVite) {
		this.nVite=nVite;
	}

	public int getVite() {
		return nVite;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void addCartaMano(Carta carta) {
		carte.add(carta);
	}
	
	public Carta removeCartaMano(int posCarta) {
		return carte.remove(posCarta);
	}
	
	public void setCarteMano(ArrayList<Carta> carte) {
		this.carte=carte;
	}	
	
	public ArrayList<Carta> getCarteMano() {
		return carte;
	}
	
	public void perdiVita() {
		this.nVite--;
	}
}
