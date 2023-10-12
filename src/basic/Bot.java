package basic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Bot extends Giocatore{
	GridPane interfaccia;
	
	public Bot(String nome, String password, int nVite, ArrayList<Carta> carte, long punteggio) {
		super(nome, nVite, carte, punteggio);
	}

	public Bot(String nome) {
		super(nome);
	}
	
	public void giocaTurno(BorderPane root) {//metodo principale che permette di far giocare il bot e di conseguenza avanzare la partita
		/*try {
			TimeUnit.SECONDS.sleep(2);
			*/
			this.interfaccia = (GridPane)root.getCenter();
			

			iniziaTurno();
/*
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/

	}
	
	private void dichiaraVite() {
		
	}
	
	private void giocaCarta() {
		
	}
	
	private void iniziaTurno() {
		String idNodo ="btnInizioTurnoGiocatore";
		Iterator i = interfaccia.getChildren().iterator();

		//cerco il bottone btnInizioTurnoGiocatore e ne scateno l'evento
		while(i.hasNext()) {
			Object o = i.next();
			if(o instanceof Button) {
				Button b = (Button)o;
				if(b.getId().equals(idNodo)) {
					b.fire();
				}
			}
		}
	}
	
	private void finisciTurno() {
		String idNodo ="btnFineTurnoGiocatore";
		Iterator i = interfaccia.getChildren().iterator();

		//cerco il bottone btnInizioTurnoGiocatore e ne scateno l'evento
		while(i.hasNext()) {
			Object o = i.next();
			if(o instanceof Button) {
				Button b = (Button)o;
				if(b.getId().equals(idNodo)) {
					b.fire();
				}
			}
		}
	}
	
	private void iniziaNuovoRound() {
		
	}
	
	private void iniziaNuovaMano() {
		
	}
}
