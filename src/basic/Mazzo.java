package basic;
import java.util.ArrayList;
import java.util.Collections;

public class Mazzo {
	ArrayList<Carta> mazzo = new ArrayList<Carta>();
	
	public Mazzo(ArrayList<Carta> mazzo) {
		this.mazzo=mazzo;
	}
	
	public void mescola() {
		Collections.shuffle(mazzo);
	}
	
}
