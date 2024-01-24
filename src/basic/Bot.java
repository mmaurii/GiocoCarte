package basic;

import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Window;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.concurrent.Task;

public class Bot extends Giocatore implements Runnable{
	final int delay = 3;//in secondi 

	public Bot(String nome, String password, int nVite, ArrayList<Carta> carte, long punteggio) {
		super(nome, nVite, carte, punteggio);
	}

	public Bot(String nome) {
		super(nome);
	}

	public Bot(Giocatore g) {
		super(g.getNome(), g.getVite(), g.getCarteMano(), g.getPunteggio());
	}

	@Override
	public void run() {
		giocaTurno();
	}

	/**
	 *permette di far giocare il bot e di conseguenza avanzare nella partita
	 */
	private void giocaTurno() {
		//ottengo il gridpane relativo all'interfaccia della Partita
		GridPane interfaccia = getNodeInterfaccia();

		if(interfaccia != null) {
			String idNodoLabel ="lblPrese";
			String idNodoBtnNuovoRound ="btnIniziaNuovoRound";
			String idNodoBtnNuovaMano ="btnIniziaNuovaMano";		
			Iterator<Node> i = interfaccia.getChildren().iterator();

			//cerco il bottone associato all'id 
			while(i.hasNext()) {
				Object o = i.next();
				if(o instanceof Button) {
					Button b = (Button)o;
					if(b.getId().equals(idNodoBtnNuovoRound)&&b.isVisible()) {
						Task<Void> t2 = taskDelay(delay);
						Thread t = new Thread(t2);//avvio il task per il delay
						t.setDaemon(true);
						t.start();
						t2.setOnSucceeded(event2 -> {//finito il delay procedo con le operazioni
							iniziaNuovoRound(b);
						});
						return;
					}else if(b.getId().equals(idNodoBtnNuovaMano)&&b.isVisible()) {
						Task<Void> t2 = taskDelay(delay);
						Thread t = new Thread(t2);//avvio il task per il delay
						t.setDaemon(true);
						t.start();
						t2.setOnSucceeded(event2 -> {//finito il delay procedo con le operazioni
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
						Task<Void> taskDelay = taskDelay(delay);
						Thread t = new Thread(taskDelay);//avvio il task per il delay
						t.setDaemon(true);
						t.start();	
						taskDelay.setOnSucceeded(event ->{//finito il delay procedo con le operazioni
							iniziaTurno();

							Task<Void> taskDelay1 = taskDelay(delay);
							Thread t1 = new Thread(taskDelay1);//avvio il task per il delay
							t1.setDaemon(true);
							t1.start();
							taskDelay1.setOnSucceeded(event1 -> {//finito il delay procedo con le operazioni
								//controllo se devo dichiarare le prese o giocare una carta
								if(l.isVisible()) {
									dichiaraPrese();
								}else {
									giocaCarta();
								}

								Task<Void> taskDelay2 = taskDelay(delay);
								Thread t2 = new Thread(taskDelay2);//avvio il task per il delay
								t2.setDaemon(true);
								t2.start();								
								taskDelay2.setOnSucceeded(event2 -> {//finito il delay procedo con le operazioni
									finisciTurno();
								});
							});
						});
						return;
					}
				}
			}
		}
	}

	/**
	 * faccio dichiarare le prese al bot
	 */
	private void dichiaraPrese() {
		//ottengo il gridpane relativo all'interfaccia della Partita
		GridPane interfaccia = getNodeInterfaccia();


		if(interfaccia != null) {
			String idNodoFinale ="txtNumeroPrese";
			String idNodoIntermedio ="gridPaneNumeroPrese";

			Iterator<Node> i = interfaccia.getChildren().iterator();

			//cerco il nodo associato all'id e inserisco il numero di prese
			while(i.hasNext()) {
				Object o = i.next();
				if(o instanceof GridPane) {
					GridPane gp = (GridPane)o;
					if(gp.getId()!=null) {
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
										for(Giocatore gio : ControllerPartita.prt.getElencoGiocatori()) {
											if(gio.getPreseDichiarate()>=0) {//controllo che prese dichiarate non abbia il valore di default
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
		}
	}

	/**
	 * fa giocare una carta al bot
	 */
	private void giocaCarta() {
		//ottengo il gridpane relativo all'interfaccia della Partita
		GridPane interfaccia = getNodeInterfaccia();


		if(interfaccia != null) {
			String idNodo ="imgCartaMano";
			String idNodoIntermedio = "gridPaneCarteMano";
			Iterator<Node> i = interfaccia.getChildren().iterator();

			//cerco il nodo associato all'id e ne scateno l'evento
			while(i.hasNext()) {
				Object o = i.next();
				if(o instanceof GridPane) {
					GridPane gp=(GridPane)o;
					if(gp.getId()!=null) {
						if(gp.getId().equals(idNodoIntermedio)) {
							Iterator<Node> y = gp.getChildren().iterator();
							Random rand = new Random();
							if(this.carte.size()>0) {
								int n = rand.nextInt(this.carte.size())+1;
								while(y.hasNext()) {
									o = y.next();
									if(o instanceof Pane) {
										Pane pn = (Pane)o;
										if(pn.getId().equals(idNodo+n)) {
											MouseEvent mouseEvent = new MouseEvent(MouseEvent.MOUSE_CLICKED,
													pn.getScaleX(), pn.getScaleY(),  // Le coordinate x e y dell'evento
													0, 0, 
													null, 0, false, false, false, false, true, false, false, false, false, false, false, false, null
													);	
											pn.fireEvent(mouseEvent);
											return;//evito che il ciclo vada a richiamare un nuovo random generando un errore dovuto a this.carte.size()=0
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * fa  iniziare il turno al bot
	 */
	private void iniziaTurno() {
		String idNodo ="btnInizioTurnoGiocatore";
		scatenaNodoInterfaccia (idNodo);
	}

	/**
	 * fa finire il turno al bot
	 */
	private void finisciTurno() {
		String idNodo ="btnFineTurnoGiocatore";
		scatenaNodoInterfaccia(idNodo);

	}

	/**
	 * dato il button b ne scatena l'evento di click
	 * @param b
	 */
	private void iniziaNuovoRound(Button b) {
		b.fire();
	}

	/**
	 * dato il button b ne scatena l'evento di click
	 * @param b
	 */
	private void iniziaNuovaMano(Button b) {
		b.fire();
	}

	/**
	 * dato l'id del nodo dell'interfaccia ne scatena l'evento
	 * @param idNodo
	 */
	private void scatenaNodoInterfaccia(String idNodo) {
		//ottengo il gridpane relativo all'interfaccia della Partita
		GridPane interfaccia = getNodeInterfaccia();


		if(interfaccia != null) {
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

	/**
	 * genera un delay in base al valore preso come parametro per permettere di vedere le operazioni che il bot svolge
	 * @param int secondi di delay
	 * @return Task<<a>Void>taskDelay
	 */
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

	public Giocatore getGiocatore(){
		return new Giocatore(this.nome, this.nVite, this.carte, this.punteggio);
	}

	/**
	 * restituisce il gridPane contenuto nel center del borderpane(root) dell'interfaccia partita.fxml
	 * @return GridPane
	 */
	private GridPane getNodeInterfaccia() {
		Iterator<Window> k = Window.getWindows().iterator();
		GridPane interfaccia = null;
		while(k.hasNext()){
			StackPane sp = (StackPane)k.next().getScene().getRoot();
			if(sp.getChildren().size()==1) {
				Iterator<Node> h = sp.getChildren().iterator();
				while(h.hasNext()) {
					Object o = h.next();
					if (o instanceof BorderPane) {
						BorderPane bp = (BorderPane)o;
						interfaccia = (GridPane) bp.getCenter();
					}
				}
			}else {
				return null;
			}
		}
		return interfaccia;
	}
}
