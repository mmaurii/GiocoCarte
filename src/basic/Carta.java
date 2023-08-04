package basic;

public class Carta {
	int n;
	Seme s;
	String p;

	public Carta(int n, Seme s, String p) {
		this.n=n;
		this.s=s;
		this.p=p;
	}
	
	public String getPercorso() {
		return p;
	}
}
