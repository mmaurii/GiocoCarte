package basic;

import java.util.ArrayList;
import java.util.*;
import java.io.*;

public class Amministratore extends Giocatore{
	
	String password;
	
	public Amministratore(String nome, String password, int nVite, ArrayList<Carta> carte, long punteggio) {
		super(nome, nVite, carte, punteggio);
		this.password=password;
	}
	
	public boolean verificaAdmin(String username, String password) {
		
		String u;
		String p;
		
		File f = new File("Passwords.txt");
		
		Scanner scan = new Scanner(f);
		
		while(scan.hasNextLine()) {
			u = scan.nextString();
			p = scan.nextString();
			
			if(u == username && p == password)
				{
					return true;
				}else
				{
					return false;
				}
			}
		}
	}
}
