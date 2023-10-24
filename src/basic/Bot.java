package basic;

import javafx.scene.control.TextField;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.TimerTask;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import javafx.scene.layout.GridPane;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.concurrent.Task;

public class Bot extends Giocatore implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GridPane interfaccia;
	Partita prt;
    
    Task<Void> slowTask = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            // Simuliamo un'operazione lenta
            for (int i = 0; i < 3; i++) {
                Thread.sleep(1000); // Rallenta per 10 secondi in totale
            }
            return null;
        }
    };
    
	public Bot(String nome, String password, int nVite, ArrayList<Carta> carte, long punteggio) {
		super(nome, nVite, carte, punteggio);
	}

	public Bot(String nome) {
		super(nome);
	}
	
	@Override
	public void run() {
		giocaTurno();
	}
	
	public void giocaTurno(BorderPane root, Partita prt) {
		this.interfaccia = (GridPane)root.getCenter();
		this.prt=prt;
	}

	private void giocaTurno() {//metodo principale che permette di far giocare il bot e di conseguenza avanzare la partita
		//controllo se devo giocare una carta o dichiarare le prese()
		String idNodo ="lblPrese";
		Iterator<Node> i = interfaccia.getChildren().iterator();
		//cerco la label associato all'id 
		while(i.hasNext()) {
			Object o = i.next();
			if(o instanceof Label) {
				Label l = (Label)o;
				if(l.getId().equals(idNodo)) {
					iniziaTurno();

					//new Thread(slowTask).start();

					//controllo se devo dichiarare le prese o giocare una carta
					if(l.isVisible()) {
						System.out.println(this+" "+this.getNome());
						dichiaraPrese();
						finisciTurno();
					}else {
						System.out.println(this+" "+this.getNome());
						giocaCarta();
						finisciTurno();
					}
				}
			}

			if(o instanceof Button) {
				Button b = (Button)o;
				if(b.getId().equals("btnIniziaNuovoRound")&&b.isVisible()) {
					System.out.println(this+" "+this.getNome());
					iniziaNuovoRound(b);
				}else if(b.getId().equals("btnIniziaNuovaMano")&&b.isVisible()) {
					System.out.println(this+" "+this.getNome());
					iniziaNuovaMano(b);
				}
			}
		}
	}

	private void dichiaraPrese() {
		String idNodoFinale ="txtNumeroPrese";
		String idNodoIntermedio ="gridPaneNumeroPrese";

		Iterator<Node> i = interfaccia.getChildren().iterator();

		//cerco il nodo associato all'id e inserisco il numero di prese
		while(i.hasNext()) {
			Object o = i.next();
			if(o instanceof GridPane) {
				GridPane gp = (GridPane)o;
				if(gp.getId().equals(idNodoIntermedio)) {
					Iterator<Node> y = gp.getChildren().iterator();
					while(y.hasNext()) {
						o=y.next();
						if(o instanceof TextField) {
							TextField tf = (TextField)o;
							if(tf.getId().equals(idNodoFinale)) {
								//controlla giustizia numero inserito
								Random rand = new Random();

								//controllo che il numero di prese dichiarate sia appropriato
								int nPrese=0;
								for(Giocatore gio : this.prt.getElencoGiocatori()) {
									if(gio.getPreseDichiarate()>=0) {//controllo che prese dichiarate non abbia il valores di default
										nPrese+=gio.getPreseDichiarate();
									}
								}

								int n = rand.nextInt(this.carte.size()+1);
								nPrese+=n;
								
								//controllo che il numero generato non vada in conflitto 
								if(nPrese==this.carte.size()) {
									tf.setText(""+(n+1));
								}else {
									tf.setText(""+n);
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void giocaCarta() {
		String idNodo ="imgCartaMano";
		String idNodoIntermedio = "gridPaneCarteMano";
		Iterator<Node> i = interfaccia.getChildren().iterator();

		//cerco il nodo associato all'id e ne scateno l'evento
		while(i.hasNext()) {
			Object o = i.next();
			if(o instanceof GridPane) {
				GridPane gp=(GridPane)o;
				if(gp.getId().equals(idNodoIntermedio)) {
					Iterator<Node> y = gp.getChildren().iterator();
					while(y.hasNext()) {
						o = y.next();
						if(o instanceof ImageView) {
							ImageView iv = (ImageView)o;
							Random rand = new Random();
							//System.out.println(this.carte.size());
							int n = rand.nextInt(this.carte.size())+1;//due thread si scambiano e ho un errore d'esecuzione
							if(iv.getId().equals(idNodo+n)) {
								MouseEvent mouseEvent = new MouseEvent(MouseEvent.MOUSE_CLICKED,
										iv.getScaleX(), iv.getScaleY(),  // Le coordinate x e y dell'evento
										0, 0, 
										null, 0, false, false, false, false, true, false, false, false, false, false, false, false, null
										);	
								iv.fireEvent(mouseEvent);
								break;//evito che il ciclo vada a richiamare un nuovo random generando un errore dovuto a this.carte.size()=0
							}
						}
					}
				}
			}
		}

	}

	private void iniziaTurno() {
		String idNodo ="btnInizioTurnoGiocatore";
		scatenaNodoInterfaccia (idNodo);
	}
	
	private void finisciTurno() {
		String idNodo ="btnFineTurnoGiocatore";
		scatenaNodoInterfaccia(idNodo);

	}
	
	private void iniziaNuovoRound(Button b) {
		b.fire();
	}
	
	private void iniziaNuovaMano(Button b) {
		b.fire();
	}
	
	private void scatenaNodoInterfaccia(String idNodo) {
		Iterator<Node> i = interfaccia.getChildren().iterator();

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
