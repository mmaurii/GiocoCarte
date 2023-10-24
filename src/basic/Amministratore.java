package basic;

import java.util.*;
import java.io.*;

public class Amministratore extends Giocatore{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String password;
	public Amministratore(String nome, String password, int nVite, ArrayList<Carta> carte, long punteggio) {
		super(nome, nVite, carte, punteggio);
		this.password=password;
	}

	public Amministratore(String nome, String password) {
		super(nome);
		this.password=password;
	}

	public boolean verificaAdmin() {

		String u;
		String p;
		try {
			File f = new File("src/Passwords.txt");

			Scanner scan = new Scanner(f);
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
		}catch(FileNotFoundException e) {
			System.out.println(e);
		}
		return false;
	}
}


