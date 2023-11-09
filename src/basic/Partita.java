package basic;
import java.io.Serializable;
import java.util.ArrayList;

public class Partita implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Mazzo m;
	private ArrayList<Giocatore> elencoGiocatori;
	private String cod;
	private int countTurnoGiocatore=0;


	public int getCountTurnoGiocatore() {
		return countTurnoGiocatore;
	}

	public void setCountTurnoGiocatore(int countTurnoGiocatore) {
		this.countTurnoGiocatore = countTurnoGiocatore;
	}

	public Partita() {}
	
	public Partita(String cod) {
		this.cod=cod;
	}

	public Partita(String cod, ArrayList<Giocatore> elencoGiocatori, Mazzo m) {
		this.cod=cod;
		this.elencoGiocatori=elencoGiocatori;
		this.m=m;
	}
	
	public Partita(String cod, ArrayList<Giocatore> elencoGiocatori) {
		this.cod=cod;
		this.elencoGiocatori=elencoGiocatori;
	}
	
	public String getCodice() {
		return cod;
	}

	public void setCodice(String codice) {
		this.cod=codice;
	}
	
	public void setElencoGiocatori(ArrayList<Giocatore> elencoGiocatori){
		this.elencoGiocatori=elencoGiocatori;
	}
	
	public ArrayList<Giocatore> getElencoGiocatori(){
		return elencoGiocatori;
	}
	
	public Mazzo getMazzo() {
		return m;
	}

	public void setMazzo(Mazzo m) {
		this.m=m;
	}
	
	public Giocatore getGiocatoreCorrente() {
		return elencoGiocatori.get(countTurnoGiocatore);
	}
	
	public Giocatore getGiocatore(int pos) {
		return elencoGiocatori.get(pos);
	}
}
