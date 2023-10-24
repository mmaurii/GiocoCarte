package basic;

import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
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
    final int delay = 3;//in secondi 
    String nm;
	public Bot(String nome, String password, int nVite, ArrayList<Carta> carte, long punteggio) {
		super(nome, nVite, carte, punteggio);
	}

	public Bot(String nome) {
		super(nome);
	}
	
	@Override
	public void run() {
		UUID uniqueID = UUID.randomUUID();//cancellare
		nm = uniqueID.toString().replaceAll("-", "").substring(0, 8);//cancellare
		giocaTurno();
	}

	public void giocaTurno(BorderPane root, Partita prt) {
		this.interfaccia = (GridPane)root.getCenter();
		this.prt=prt;
	}

	private void giocaTurno() {//metodo principale che permette di far giocare il bot e di conseguenza avanzare la partita
		//controllo se devo giocare una carta o dichiarare le prese()
		String idNodoLabel ="lblPrese";
		String idNodoBtnNuovoRound ="btnIniziaNuovoRound";
		String idNodoBtnNuovaMano ="btnIniziaNuovaMano";		
		Iterator<Node> i = interfaccia.getChildren().iterator();
		System.out.println("\t"+nm);

		//cerco il bottone associato all'id 
		while(i.hasNext()) {
			Object o = i.next();
			if(o instanceof Button) {
				Button b = (Button)o;
				if(b.getId().equals(idNodoBtnNuovoRound)&&b.isVisible()) {
					Task<Void> t2 = taskDelay(delay);
					new Thread(t2).start();//avvio il task per il delay
					t2.setOnSucceeded(event2 -> {//finito il delay procedo con le operazioni
						System.out.println(nm);
						System.out.println("iniziaNuovoRound");
						iniziaNuovoRound(b);
					});
					return;
				}else if(b.getId().equals(idNodoBtnNuovaMano)&&b.isVisible()) {
					Task<Void> t2 = taskDelay(delay);
					new Thread(t2).start();//avvio il task per il delay
					t2.setOnSucceeded(event2 -> {//finito il delay procedo con le operazioni
						System.out.println(nm);
						System.out.println("iniziaNuovaMano");
						iniziaNuovaMano(b);
					});
					return;
				} 
			}
		}
		
		//reset iteratore
		i=interfaccia.getChildren().iterator();
		
		//cerco la label associato all'id e ne controllo alcuni parametri se il ciclo prima non ha fatto return e quindi btnIniziaNuovaMano e btnIniziaNuovoRound sono visibili
		while(i.hasNext()) {
			Object o = i.next();
			if(o instanceof Label) {
				Label l = (Label)o;
				if(l.getId().equals(idNodoLabel)) {
					Task<Void> t = taskDelay(delay);
					new Thread(t).start();//avvio il task per il delay
					t.setOnSucceeded(event ->{//finito il delay procedo con le operazioni
						iniziaTurno();

						Task<Void> t1 = taskDelay(delay);
						new Thread(t1).start();//avvio il task per il delay
						t1.setOnSucceeded(event1 -> {//finito il delay procedo con le operazioni
							//controllo se devo dichiarare le prese o giocare una carta
							if(l.isVisible()) {
								System.out.println(nm);
								System.out.println("dichiaro le prese");
								dichiaraPrese();
							}else {
								System.out.println(nm);
								System.out.println("gioca la carta");
								giocaCarta();
							}

							Task<Void> t2 = taskDelay(delay);
							new Thread(t2).start();//avvio il task per il delay
							t2.setOnSucceeded(event2 -> {//finito il delay procedo con le operazioni
								finisciTurno();
							});
						});
					});
					return;
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
							System.out.println(this.carte.size());
							int n = rand.nextInt(this.carte.size())+1;
							if(iv.getId().equals(idNodo+n)) {
								MouseEvent mouseEvent = new MouseEvent(MouseEvent.MOUSE_CLICKED,
										iv.getScaleX(), iv.getScaleY(),  // Le coordinate x e y dell'evento
										0, 0, 
										null, 0, false, false, false, false, true, false, false, false, false, false, false, false, null
										);	
								iv.fireEvent(mouseEvent);
								return;//evito che il ciclo vada a richiamare un nuovo random generando un errore dovuto a this.carte.size()=0
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
	
    private Task<Void> taskDelay(int secondi){
        Task<Void> slowTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Simuliamo un'operazione lenta
                for (int i = 0; i < secondi; i++) {
                    Thread.sleep(1000); // Rallenta per 3 secondi in totale
                }
                return null;
            }
        };
    	
    	return slowTask;
    }
}
