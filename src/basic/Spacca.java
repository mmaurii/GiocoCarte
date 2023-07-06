package basic;
import java.util.*;

public class Spacca {
	public static void main(String[] args) {
		Amministratore a1 = login();
		if(a1!=null) {
			System.out.println("giusto");
		}
		
	}
	
	// metodo che avvia la procedura di login
	public static Amministratore login() {
		System.out.println("inserisci dati");
		Amministratore a1;
		boolean flag = false;
		Scanner scan = new Scanner(System.in);
		do {
			System.out.println("inserisci nome e password");
			String username = scan.nextLine();
			String password = scan.nextLine();
			a1= new Amministratore(username, password);
			flag = a1.verificaAdmin();
		}while(!flag);
		scan.close();
		return a1;
	}
}
