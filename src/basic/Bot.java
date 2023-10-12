package basic;

import java.awt.TextField;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javafx.scene.layout.GridPane;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class Bot extends Giocatore{
	GridPane interfaccia;
	Partita prt;
	public Bot(String nome, String password, int nVite, ArrayList<Carta> carte, long punteggio) {
		super(nome, nVite, carte, punteggio);
	}

	public Bot(String nome) {
		super(nome);
	}
	
	public void giocaTurno(BorderPane root, Partita prt) {//metodo principale che permette di far giocare il bot e di conseguenza avanzare la partita
		/*try {
			TimeUnit.SECONDS.sleep(2);
			*/
			this.interfaccia = (GridPane)root.getCenter();
			this.prt=prt;
			
			//controllo se devo giocare una carta o dichiarare le prese()
			String idNodo ="lblPrese";
			Iterator i = interfaccia.getChildren().iterator();

			//cerco il bottone associato all'id 
	/*		while(i.hasNext()) {
				Object o = i.next();
				System.out.println(o.getClass().descriptorString());
			}*/
			//cerco il bottone associato all'id 
			while(i.hasNext()) {
				Object o = i.next();
				if(o instanceof Label) {
					Label l = (Label)o;
					if(l.getId().equals(idNodo)) {
						iniziaTurno();

						//controllo se devo dichiarare le prese o giocare una carta
						if(l.isVisible()) {
							dichiaraPrese();
							finisciTurno();
						}else {
							giocaCarta();
							finisciTurno();
						}
					}
				}
				
				if(o instanceof Button) {
					Button b = (Button)o;
					if(b.getId().equals("btnIniziaNuovoRound")) {
						iniziaNuovoRound();
					}else if(b.getId().equals("BtnIniziaNuovaMano")) {
						iniziaNuovaMano();
					}
				}
			}
/*
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/

	}
	
	private void dichiaraPrese() {
		String idNodo ="txtNumeroPrese";
		Iterator i = interfaccia.getChildren().iterator();

		//cerco il nodo associato all'id e ne scateno l'evento
		while(i.hasNext()) {
			Object o = i.next();
			if(o instanceof TextField) {
				TextField tf = (TextField)o;
				if(tf.getName().equals(idNodo)) {
					//controlla giustizia numero inserito
					Random rand = new Random();
					int n = rand.nextInt(6);
					tf.setText(""+n);
				}
			}
		}
	}
	
	private void giocaCarta() {
		String idNodo ="imgCartaMano";
		Iterator i = interfaccia.getChildren().iterator();

		//cerco il nodo associato all'id e ne scateno l'evento
		while(i.hasNext()) {
			Object o = i.next();
			if(o instanceof ImageView) {
				ImageView iv = (ImageView)o;
				Random rand = new Random();
				int n = rand.nextInt(carte.size()+1);
				if(iv.getId().equals(idNodo+n)) {
					Event e = new Event(MouseEvent.MOUSE_CLICKED);
					iv.fireEvent(e);
				}
			}
		}
	}
	
	private void iniziaTurno() {
		String idNodo ="btnInizioTurnoGiocatore";
		trovaNodoInterfaccia(idNodo);
	}
	
	private void finisciTurno() {
		String idNodo ="btnFineTurnoGiocatore";
		trovaNodoInterfaccia(idNodo);

	}
	
	private void iniziaNuovoRound() {
		String idNodo ="btnIniziaNuovoRound";
		trovaNodoInterfaccia(idNodo);

	}
	
	private void iniziaNuovaMano() {
		String idNodo ="BtnIniziaNuovaMano";
		trovaNodoInterfaccia(idNodo);
	}
	
	private void trovaNodoInterfaccia(String idNodo) {
		Iterator i = interfaccia.getChildren().iterator();

		//cerco il bottone associato all'id e ne scateno l'evento
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
}
