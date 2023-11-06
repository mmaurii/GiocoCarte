package basic;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Mazzo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
		int valore;
		int speciale;
		try {
			File f = new File("src/Carte.txt");

			Scanner scan = new Scanner(f);
			while(scan.hasNextLine()) {
				String data[] = scan.nextLine().split(", ");
				numero = Integer.parseInt(data[0]);
				seme = Seme.valueOf(data[1]);
				percorso = data[2];
				valore=Integer.parseInt(data[3]);
				speciale=Integer.parseInt(data[4]);
				if(valore == 40)
				{
					CartaSpeciale cs = new CartaSpeciale(numero, seme, percorso, valore, speciale);
					mazzo.add(cs);
				}else
				{
					Carta c = new Carta(numero, seme, percorso, valore, speciale);
					//System.out.println(numero + ", " + seme + ", " + percorso + ", " + valore);
					mazzo.add(c);
				}
				
			}
			scan.close();
		}catch(FileNotFoundException e) {
			System.out.println(e);
		}
	}
	
	public void mescola() {
		Collections.shuffle(mazzo);
	}
	
	
	public void setSpeciale() {
		Random random = new Random();
        int r = random.nextInt(39) + 1;
        System.out.println("Numero casuale: " + r);
		mazzo.get(r).speciale = 1;
        System.out.println(mazzo.get(r).percorso + "\n" +  mazzo.get(r).valore + "\n" + mazzo.get(r).speciale);

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
