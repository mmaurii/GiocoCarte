package basic;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Mazzo {
	
	ArrayList<Carta> mazzo = new ArrayList<Carta>();
	
	/*public Mazzo(ArrayList<Carta> mazzo) {
		this.mazzo = mazzo;
	}*/
	
	public Mazzo() {
		popolaMazzo();
	}
	
	public void popolaMazzo() {
		

		int numero;
		Seme seme;
		String percorso;
		
		try {
			File f = new File("src/Carte.txt");

			Scanner scan = new Scanner(f);
			while(scan.hasNextLine()) {
				String data[] = scan.nextLine().split(", ");
				numero = Integer.parseInt(data[0]);
				seme = Seme.valueOf(data[1]);
				percorso = data[2];
				
				Carta c = new Carta(numero, seme, percorso);
				//System.out.println(numero + ", " + seme + ", " + percorso);
				mazzo.add(c);
				
			}
			scan.close();
		}catch(FileNotFoundException e) {
			System.out.println(e);
		}
	}
	
	public void mescola() {
		Collections.shuffle(mazzo);
	}
	
	public ArrayList<Carta> getMazzo(){
		return mazzo;
	}
	
	public ArrayList<Carta> pescaCarte(int numeroCarte) {
		ArrayList<Carta> cartePerGiocatore = new ArrayList<Carta>();
		
		for(int i =0;i<numeroCarte;i++) {
			cartePerGiocatore.add(mazzo.remove(0));
		}
		
		return cartePerGiocatore;
	}
}
