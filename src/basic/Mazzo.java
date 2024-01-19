package basic;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Mazzo {
	ArrayList<Carta> mazzo = new ArrayList<Carta>();
	
	public Mazzo() {
		popolaMazzo();
	}
	
	/**
	 * carica le carte dal file di testo e inizializza le carte del mazzo
	 */
	public void popolaMazzo() {
		int numero;
		Seme seme;
		String percorso;
		int valore;
		int speciale;
		try {
			
			String path = "Documenti/Carte.txt";
			File file = new File(path);

			Scanner scan = new Scanner(file);
			while(scan.hasNextLine()) {
				String data[] = scan.nextLine().split(", ");
				numero = Integer.parseInt(data[0]);
				seme = Seme.valueOf(data[1]);
				percorso = data[2];
				valore=Integer.parseInt(data[3]);
				speciale=Integer.parseInt(data[4]);
				if(valore == 40){
					CartaSpeciale cs = new CartaSpeciale(numero, seme, percorso, valore, speciale);
					mazzo.add(cs);
				}else{
					Carta c = new Carta(numero, seme, percorso, valore, speciale);
					mazzo.add(c);
				}
			}
			scan.close();
		}catch(FileNotFoundException e) {
			System.out.println(e);
		}
	}
	
	/**
	 * mescola il mazzo tramite il metodo <code>shuffle</code> della classe <code>collections</code>
	 */
	public void mescola() {
		Collections.shuffle(mazzo);
	}
	
	/**
	 * setta una carta speciale in maniera randomica (se pescata al primo turno farà acquisire una vita)
	 */
	public void setSpeciale() {
		Random random = new Random();
        int r = random.nextInt(39) + 1;
		mazzo.get(r).speciale = 1;
	}
	
	public ArrayList<Carta> getMazzo(){
		return mazzo;
	}
	
	/**
	 * pesca un numero pari a 'numeroCarte' dal mazzo e lo restituisce
	 * @param numeroCarte
	 * @return ArrayList<<a>Carta> cartePescate
	 */
	public ArrayList<Carta> pescaCarte(int numeroCarte) {
		ArrayList<Carta> cartePerGiocatore = new ArrayList<Carta>();
		for(int i =0;i<numeroCarte;i++) {
			cartePerGiocatore.add(mazzo.remove(0));
		}
		return cartePerGiocatore;
	}
}
