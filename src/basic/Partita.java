package basic;
import java.util.ArrayList;

public class Partita {
	Mazzo m;
	ArrayList<Giocatore> elencoGiocatori = new ArrayList<Giocatore>();
	String cod;
	
	public Partita(String cod) {
		this.cod=cod;
	}

	public Partita(String cod, ArrayList<Giocatore> elencoGiocatori, Mazzo m) {
		this.cod=cod;
		this.elencoGiocatori=elencoGiocatori;
		this.m=m;
	}
	
	//public void partitaVeloce(){}
	
	//public void partitaLunga(){}
}
