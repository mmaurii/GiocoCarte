package basic;
import java.util.*;

public class Spacca {
	public static void main(String[] args) {
		
		
	}
	
	// metodo che avvia la procedura di login
	public static Amministratore Login() {
		Amministratore a1;
		boolean flag = false;
		Scanner scan = new Scanner(System.in);
		do {
			String username = scan.nextLine();
			String password = scan.nextLine();
			a1= new Amministratore(username, password);
			flag = a1.
		}while(!flag);
		return a1;
	}
}
