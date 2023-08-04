package basic;
import java.util.ArrayList;

public class Partita {
	Mazzo m;
	ArrayList<Giocatore> elencoGiocatori = new ArrayList<Giocatore>();
	String cod;

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
	//public void partitaVeloce(){}
	
	//public void partitaLunga(){}
	
}
