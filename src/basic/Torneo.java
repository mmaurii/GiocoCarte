package basic;

import java.util.ArrayList;

public class Torneo {

	private String cod;
	private ArrayList<Partita> elencoPartite;

	
	public Torneo(String cod) {
		this.cod=cod;
	}

	public Torneo(String cod, ArrayList<Partita> elencoPartite) {
		this.cod=cod;
		this.elencoPartite=elencoPartite;
	}
	
}
