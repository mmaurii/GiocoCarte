package basic;

import java.util.*;
import java.io.*;

public class Amministratore extends Giocatore{
	String password;
	public Amministratore(String nome, String password, int nVite, ArrayList<Carta> carte, long punteggio) {
		super(nome, nVite, carte, punteggio);
		this.password=password;
	}

	public Amministratore(String nome, String password) {
		super(nome);
		this.password=password;
	}

	/**
	 * verifica se l'amministratore è presente nel salvataggio
	 * @return true se è presente, false se non lo è
	 */
	public boolean verificaAdmin() {
		String u;
		String p;

		final String path = "Documenti/Passwords.txt";
		File file = new File(path);

		Scanner scan;
		try {
			scan = new Scanner(file);

			while(scan.hasNextLine()) {
				String data[] = scan.nextLine().split(" , ");
				u = data[0];
				p = data[1];

				if(u.equals(nome) && p.equals(password))
				{
					scan.close();
					return true;
				}
			}
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return false;
	}
}


