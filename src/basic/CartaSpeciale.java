package basic;

public class CartaSpeciale extends Carta{
	public CartaSpeciale(int numero, Seme seme, String percorso, int valore) {
		super(numero, seme, percorso, valore);
	}
	
	public void setValore(int v) {
		this.valore = v;
	}
}
