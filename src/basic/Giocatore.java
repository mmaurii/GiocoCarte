package basic;
import java.util.ArrayList;

public class Giocatore {
	String nome;
	int nVite;
	ArrayList<Carta> carte = new ArrayList<Carta>();
	long punteggio;
	int preseDichiarate;
	int preseEffettuate;
	
	public Giocatore(String nome, int nVite, ArrayList<Carta> carte, long punteggio) {
		this.nome=nome;
		this.nVite=nVite;
		this.carte=carte;
		this.punteggio=punteggio;
		this.preseDichiarate=-1;
		this.preseEffettuate=0;
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
	
	public int getPreseDichiarate() {
		return preseDichiarate;
	}
	
	public int getPreseEffettuate() {
		return preseEffettuate;
	}
	
	public void setPreseDichiarate(int nPrese) {
		this.preseDichiarate=nPrese;
	}
	
	public void setPreseEffettuate(int nPrese) {
		this.preseEffettuate=nPrese;
	}
	
	public void incrementaPreseEffettuate() {
		this.preseEffettuate++;
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
