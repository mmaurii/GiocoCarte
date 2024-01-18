package basic;

import java.util.ArrayList;

public class Torneo {

	private String cod;//codice identificativo
	private ArrayList<Partita> elencoPartite; //elenco partite dei 'gironi'
	private Partita[] elencoSemifinali;	
	private Partita finale;	
	private Giocatore vincitore = null; 	//giocatore vincitore, il pareggio è contemplato ma cmq verrà definito un vincitore forchè per la finale
	
	public Torneo(String cod) {
		this.cod=cod;
	}

	public Torneo(String cod, ArrayList<Partita> elencoPartite) {
		this.cod=cod;
		this.elencoPartite=elencoPartite;
	}
	
	public String getCodice() {
		return cod;
	}

	public void setCodice(String cod) {
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

	public Partita getFinale() {
		return finale;
	}

	public void setFinale(Partita finale) {
		this.finale = finale;
	}

	public Partita[] getElencoSemifinali() {
		return elencoSemifinali;
	}

	public void setElencoSemifinali(Partita[] elencoSemifinali) {
		this.elencoSemifinali = elencoSemifinali;
	}
}
