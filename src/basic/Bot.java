package basic;

import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javafx.scene.layout.GridPane;
import javafx.event.Event;
import javafx.scene.Node;
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
		Thread t=new Thread();
			this.interfaccia = (GridPane)root.getCenter();
			this.prt=prt;
			
			//controllo se devo giocare una carta o dichiarare le prese()
			String idNodo ="lblPrese";
			Iterator<Node> i = interfaccia.getChildren().iterator();

			//cerco il bottone associato all'id 
	/*		while(i.hasNext()) {
				Object o = i.next();
				System.out.println(o.getClass().descriptorString());
			}*/
			//cerco la label associato all'id 
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
					if(b.getId().equals("btnIniziaNuovoRound")&&b.isVisible()) {
						iniziaNuovoRound(b);
					}else if(b.getId().equals("BtnIniziaNuovaMano")&&b.isVisible()) {
						iniziaNuovaMano(b);
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
		String idNodoFinale ="txtNumeroPrese";
		String idNodoIntermedio ="gridPaneNumeroPrese";

		Iterator<Node> i = interfaccia.getChildren().iterator();

		//cerco il nodo associato all'id e ne scateno l'evento
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
									nPrese+=gio.getPreseDichiarate();
								}

								int n = rand.nextInt(carte.size()+1);
								nPrese+=n;
								//System.out.println(n);					
								//System.out.println(nPrese);
								if(nPrese==carte.size()) {
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
		
		/*
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
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
				System.out.println("a");
				if(gp.getId().equals(idNodoIntermedio)) {
					System.out.println("b");
					Iterator<Node> y = gp.getChildren().iterator();
					while(y.hasNext()) {
						o = y.next();
						if(o instanceof ImageView) {
							ImageView iv = (ImageView)o;
							Random rand = new Random();
							int n = rand.nextInt(carte.size())+1;
							System.out.println(idNodo+n);
							if(iv.getId().equals(idNodo+n)) {
								System.out.println("c");
								Event e = new Event(MouseEvent.MOUSE_CLICKED);
								iv.fireEvent(e);
							}
						}
					}
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
	
	private void iniziaNuovoRound(Button b) {
		//String idNodo ="btnIniziaNuovoRound";
		//trovaNodoInterfaccia(idNodo);
		b.fire();
	}
	
	private void iniziaNuovaMano(Button b) {
		//String idNodo ="BtnIniziaNuovaMano";
		//trovaNodoInterfaccia(idNodo);
		b.fire();
	}
	
	private void trovaNodoInterfaccia(String idNodo) {
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
