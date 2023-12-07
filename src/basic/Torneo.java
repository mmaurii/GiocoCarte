package basic;

import java.util.ArrayList;

public class Torneo {

	private String cod;
	private ArrayList<Squadra> squadre;

	
	public Torneo(String cod) {
		this.cod=cod;
	}

	public Torneo(String cod, ArrayList<Squadra> squadre) {
		this.cod=cod;
		this.squadre=squadre;
	}
	
}
