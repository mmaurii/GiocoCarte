package basic;

import java.util.ArrayList;

public class Torneo {

	private String cod;
	private ArrayList<Partita> elencoPartite;
	private Giocatore vincitore = null; 
	
	public Torneo(String cod) {
		this.cod=cod;
	}

	public Torneo(String cod, ArrayList<Partita> elencoPartite) {
		this.cod=cod;
		this.elencoPartite=elencoPartite;
	}
	
	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public ArrayList<Partita> getElencoPartite() {
		return elencoPartite;
	}

	public void setElencoPartite(ArrayList<Partita> elencoPartite) {
		this.elencoPartite = elencoPartite;
	}

	public Giocatore getVincitore() {
		return vincitore;
	}

	public void setVincitore(Giocatore vincitore) {
		this.vincitore = vincitore;
	}
}
