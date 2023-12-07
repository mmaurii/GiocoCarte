package basic;

import java.util.ArrayList;

public class Squadra {

	private ArrayList<Giocatore> giocatori;

	public Squadra(ArrayList<Giocatore> giocatori) {
		this.giocatori=giocatori;
	}
	
	public ArrayList<Giocatore> getGiocatori() {
		return this.giocatori;
	}
}
