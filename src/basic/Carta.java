package basic;

public class Carta {
	int numero;
	Seme seme;
	String percorso;
	int valore;
	int speciale;
	
	public Carta(int numero, Seme seme, String percorso, int valore, int speciale) {
		this.numero=numero;
		this.seme=seme;
		this.percorso=percorso;
		this.valore=valore;
		this.speciale=speciale;
	}
	
	public String getPercorso() {
		return percorso;
	}

	public int getValore() {
		return valore;
	}
	
	public int getSpeciale() {
		return speciale;
	}
}