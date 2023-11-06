package basic;

public class CartaSpeciale extends Carta{
	public CartaSpeciale(int numero, Seme seme, String percorso, int valore, int speciale) {
		super(numero, seme, percorso, valore, speciale);
	}
	
	public void setValore(int v) {
		this.valore = v;
	}
}
