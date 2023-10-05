package basic;

import java.io.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import java.util.*;
import javax.security.auth.login.AccountNotFoundException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXMLLoader;
import com.google.gson.Gson;


public class ControllerPartita {
	//variabili di controllo
	int numeroCarteAGiocatore;
	final int lungCodicePartita=10;
	final int nViteDefault=5;
	Partita prt;
	Mazzo mazzo = new Mazzo();
	ArrayList<Giocatore> giocatoriPrt = new ArrayList<Giocatore>();
	String pathRetroCarta = "/basic/IMGcarte/retro.jpg";
	int countTurnoGiocatore=0;
	private ArrayList<Carta> lstCarteBanco = new ArrayList<Carta>();
	boolean dichiaraPrese=true;
	boolean primoTurno=true;
	String pathClassifica = "src/Classifica.txt";
	String pathStatus = "src/Status.txt";


	@FXML Label lblTurnoGiocatore;
	@FXML Label lblManoGiocatore;
	@FXML Button btnInizioTurnoGiocatore;
	@FXML ImageView imgCartaMano1;
	@FXML ImageView imgCartaMano2;
	@FXML ImageView imgCartaMano3;
	@FXML ImageView imgCartaMano4;
	@FXML ImageView imgCartaMano5;
	@FXML TextField txtNumeroPrese;
	boolean btnInizioTurnoGiocatoreClicked=false;
	//inizia il turno dell'n giocatore
	@FXML public void inizioTurnoGiocatore(ActionEvent actionEvent) {
		if(!paneNumeroPrese.isVisible()) {
			lblManoGiocatore.setText("Mano di "+this.prt.getElencoGiocatori().get(countTurnoGiocatore).getNome());
			lblTurnoGiocatore.setVisible(false);
			lblManoGiocatore.setVisible(true);
			btnInizioTurnoGiocatore.setDisable(true);
			btnInizioTurnoGiocatoreClicked=true;
		}else {
			lblManoGiocatore.setText(this.prt.getElencoGiocatori().get(countTurnoGiocatore).getNome()+" dichiara le prese");
			lblTurnoGiocatore.setVisible(false);
			lblManoGiocatore.setVisible(true);
			btnInizioTurnoGiocatore.setDisable(true);
			btnFineTurnoGiocatore.setDisable(false);
		}

		//mostro le carte in output relative al giocatore del turno corrente
		ArrayList<ImageView> listaCarteMano = new ArrayList<ImageView>(Arrays.asList(imgCartaMano1, imgCartaMano2, imgCartaMano3, imgCartaMano4, imgCartaMano5));
		for(int i = 0; i<listaCarteMano.size();i++) {	
			if(i<this.prt.getElencoGiocatori().get(countTurnoGiocatore).getCarteMano().size()) {
				Image newImg = new Image(getClass().getResourceAsStream(prt.getElencoGiocatori().get(countTurnoGiocatore).getCarteMano().get(i).getPercorso()));
				listaCarteMano.get(i).setImage(newImg);
			}else {
				listaCarteMano.get(i).setImage(null);
			}
		}
	}


	@FXML ImageView imgCartaBanco1;
	@FXML ImageView imgCartaBanco2;
	@FXML ImageView imgCartaBanco3;
	@FXML ImageView imgCartaBanco4;
	@FXML ImageView imgCartaBanco5;
	@FXML ImageView imgCartaBanco6;
	@FXML ImageView imgCartaBanco7;
	@FXML ImageView imgCartaBanco8;
	//l'n giocatore gioca la carta1
	@FXML public void GiocaCartaMano1(MouseEvent mouseEvent) {
		final int posCartaCliccata=0;
		giocaCartaMano(posCartaCliccata);	
	}    


	//l'n giocatore gioca la carta2
	@FXML public void GiocaCartaMano2(MouseEvent mouseEvent) {
		final int posCartaCliccata=1;
		giocaCartaMano(posCartaCliccata);
	}    


	//l'n giocatore gioca la carta3
	@FXML public void GiocaCartaMano3(MouseEvent mouseEvent) {
		final int posCartaCliccata=2;
		giocaCartaMano(posCartaCliccata);	
	}    


	//l'n giocatore gioca la carta4
	@FXML public void GiocaCartaMano4(MouseEvent mouseEvent) {
		final int posCartaCliccata=3;
		giocaCartaMano(posCartaCliccata);
	}    


	//l'n giocatore gioca la carta5
	@FXML public void GiocaCartaMano5(MouseEvent mouseEvent) {
		final int posCartaCliccata=4;
		giocaCartaMano(posCartaCliccata);
	}


	@FXML Button btnFineTurnoGiocatore;
	@FXML Label lblVitaPersa;
	@FXML Button btnIniziaNuovoRound;
	@FXML Pane paneNumeroPrese;
	@FXML Label lblPrese;
	@FXML Label lblNumPreseNonValido;
	@FXML ListView<String> lstViewPrese;
	@FXML ListView<String> lstViewVite;
	int presePerQuestaMano=0;
	//dispongo la fine del turno per il giocatore corrente
	@FXML public void fineTurnoGiocatore(ActionEvent actionEvent) {
		lblNumPreseNonValido.setVisible(false);
		int numeroPreseGiocatore=0;
		int giocatoreChePrende;

		if(paneNumeroPrese.isVisible()) {
			try {
				numeroPreseGiocatore=Integer.parseInt(txtNumeroPrese.getText());
				this.prt.getElencoGiocatori().get(countTurnoGiocatore).setPreseDichiarate(numeroPreseGiocatore);
				presePerQuestaMano+=numeroPreseGiocatore;

				//System.out.println(presePerQuestaMano+" != "+(Integer)(this.prt.getElencoGiocatori().get(countTurnoGiocatore).getCarteMano().size()+1)+" || "+(Integer)(countTurnoGiocatore+1)+" != "+this.prt.getElencoGiocatori().size());
				if(presePerQuestaMano!=this.prt.getElencoGiocatori().get(countTurnoGiocatore).getCarteMano().size()||countTurnoGiocatore!=this.prt.getElencoGiocatori().size()-1) {
					//visualizzo il numero di prese per questo giocatore
					lstViewPrese.getItems().add(this.prt.getElencoGiocatori().get(countTurnoGiocatore).getNome()+" "+numeroPreseGiocatore+" prese");
					if(dichiaraPrese) {
						//visualizzo il numero di vite di questo giocatore se è il primo turno
						if(primoTurno) {
							lstViewVite.getItems().add(this.prt.getElencoGiocatori().get(countTurnoGiocatore).getNome()+" "+this.prt.getElencoGiocatori().get(countTurnoGiocatore).getVite()+" vite");
						}

						//incremento il contatore dei giocatori
						countTurnoGiocatore++;
						//rimetto le carte coperte
						copriCarteGiocatore();

						if(countTurnoGiocatore>=this.prt.getElencoGiocatori().size()) {
							countTurnoGiocatore=0;
							//sistemo l'interfaccia per iniziare a giocare le carte
							paneNumeroPrese.setVisible(false);
							lblManoGiocatore.setVisible(false);
							btnIniziaNuovoRound.setVisible(true);
							btnFineTurnoGiocatore.setDisable(true);
							btnInizioTurnoGiocatore.setDisable(true);
						}else {
							//faccio dichiarare le prese al prossimo giocatore
							lblManoGiocatore.setVisible(false);
							btnFineTurnoGiocatore.setDisable(true);
							btnInizioTurnoGiocatore.setDisable(false);
							//definisco chi giocherà il prossimo turno
							lblTurnoGiocatore.setText("è il turno di: "+this.prt.getElencoGiocatori().get(countTurnoGiocatore).getNome());
							lblTurnoGiocatore.setVisible(true);
						}
					}
				}else {
					presePerQuestaMano-=numeroPreseGiocatore;
					lblNumPreseNonValido.setVisible(true);
				}
				txtNumeroPrese.setText("");
			}catch(NumberFormatException nfe){
				lblPrese.setTextFill(Color.RED);
				txtNumeroPrese.setText(null);
			}    
		}else {
			presePerQuestaMano=0;

			//if(lstViewPrese.getItems().size()-1==countTurnoGiocatore||!paneNumeroPrese.isVisible()) {
			//rimetto le carte coperte
			copriCarteGiocatore();

			//controllo che non si sfori il numero di giocatori
			if(countTurnoGiocatore<prt.getElencoGiocatori().size()) {
				//passo il turno al prossimo giocatore incrementando il contatore
				countTurnoGiocatore++;
			}else {
				btnInizioTurnoGiocatore.setDisable(true);
			}

			//controllo che non sia conclusa una mano
			if(this.prt.getElencoGiocatori().get(this.prt.getElencoGiocatori().size()-1).getCarteMano().size() != 0)
			{
				//controllo che non si sia chiuso un giro di carte 
				if(countTurnoGiocatore<this.prt.getElencoGiocatori().size()) {
					//sistemo la visualizzazione dell'interfaccia
					lblManoGiocatore.setVisible(false);
					btnFineTurnoGiocatore.setDisable(true);
					btnInizioTurnoGiocatore.setDisable(false);
					//definisco chi giocherà il prossimo turno
					lblTurnoGiocatore.setText("è il turno di: "+this.prt.getElencoGiocatori().get(countTurnoGiocatore).getNome());
					lblTurnoGiocatore.setVisible(true);
				}else {
					//azzero il contatore dei turni
					countTurnoGiocatore=0;

					//Calcolo chi prende in base alle carte sul banco
					giocatoreChePrende = CalcolaPunti(lstCarteBanco);

					Giocatore gio = this.prt.getElencoGiocatori().get(giocatoreChePrende);
					this.prt.getElencoGiocatori().get(giocatoreChePrende).incrementaPreseEffettuate();
					lblVitaPersa.setText(gio.getNome()+" ha PRESO questo turno");
					lblVitaPersa.setVisible(true);
					lstCarteBanco.clear();
					lblManoGiocatore.setVisible(false);
					btnFineTurnoGiocatore.setDisable(true);
					btnIniziaNuovoRound.setVisible(true);

					//cambio l'ordine dei giocatori spostando il primo in fondo alla lista
					gio=this.prt.getElencoGiocatori().remove(0);
					this.prt.getElencoGiocatori().add(gio);
				}
			}else {
				//azzero il contatore dei turni
				countTurnoGiocatore=0;

				//Calcolo chi prende in base alle carte sul banco
				giocatoreChePrende = CalcolaPunti(lstCarteBanco);

				Giocatore gio = this.prt.getElencoGiocatori().get(giocatoreChePrende);
				this.prt.getElencoGiocatori().get(giocatoreChePrende).incrementaPreseEffettuate();
				lblVitaPersa.setText(gio.getNome()+" ha PRESO questo turno");
				lblVitaPersa.setVisible(true);
				lstCarteBanco.clear();
				lblManoGiocatore.setVisible(false);
				btnFineTurnoGiocatore.setDisable(true);

				btnIniziaNuovaMano.setVisible(true);

				//verifico chi ha sbagliato a dichiarare e gli rimuovo la vita
				lstViewVite.getItems().clear();
				for(Giocatore g : this.prt.getElencoGiocatori()) {	
					if(g.getPreseDichiarate() != g.getPreseEffettuate())
					{
						g.perdiVita();
					}
					if(g.getVite()==0) {
						lstViewVite.getItems().add(g.getNome()+" è eliminato");
					}else {
						lstViewVite.getItems().add(g.getNome()+" "+g.getVite()+" vite");
					}

					//azzero i contatori delle prese relativi alla mano corrente
					g.setPreseEffettuate(0);
					g.setPreseDichiarate(-1);
				}

				Iterator<Giocatore> g = this.prt.getElencoGiocatori().iterator();
				while(g.hasNext()) {
					if(g.next().getVite()==0) {
						g.remove();
					}
				}

				if(this.prt.getElencoGiocatori().size()>1) {//avvio una nuova mano
					cominciaNuovaMano();

					//cambio l'ordine dei giocatori spostando il primo in fondo alla lista
					gio=this.prt.getElencoGiocatori().remove(0);
					this.prt.getElencoGiocatori().add(gio);
				}else if (this.prt.getElencoGiocatori().size()==1){//concludo la partita e ne annuncio il vincitore 
					lblVitaPersa.setText(this.prt.getElencoGiocatori().get(0).getNome()+" ha VINTO la partita");
					btnIniziaNuovaMano.setVisible(false);

					//conteggio punti
					aggiornaClassifica(pathClassifica);
				}else{//concludo la partita e ne annuncio il pareggio
					lblVitaPersa.setText("la partita è finita in PAREGGIO");
					btnIniziaNuovaMano.setVisible(false);
				}
			}	
		}
	}


	@FXML public void IniziaNuovoRound(ActionEvent actionEvent) {
		//sistemo l'interfaccia perchè possa essere giocato un nuovo round
		primoTurno=false;
		btnIniziaNuovoRound.setVisible(false);
		lblVitaPersa.setVisible(false);
		lblTurnoGiocatore.setText("è il turno di: "+this.prt.getElencoGiocatori().get(countTurnoGiocatore).getNome());
		lblTurnoGiocatore.setVisible(true);
		//lblManoGiocatore.setText(pathClassifica);
		//lblManoGiocatore.setVisible(true);
		btnInizioTurnoGiocatore.setDisable(false);

		ArrayList<ImageView> listaCarteBanco = new ArrayList<ImageView>(Arrays.asList(imgCartaBanco1, imgCartaBanco2, imgCartaBanco3, imgCartaBanco4, imgCartaBanco5, imgCartaBanco6, imgCartaBanco7, imgCartaBanco8));
		for(ImageView i : listaCarteBanco) {
			i.setImage(null);
		}

		//mostro le carte coperte del giocatore che deve iniziare il turno
		copriCarteGiocatore();
	}


	@FXML Button btnIniziaNuovaMano;
	@FXML public void IniziaNuovaMano(ActionEvent actionEvent) {
		//sistemo l'interfaccia per poter iniziare la nuova mano
		dichiaraPrese=true;
		lblPrese.setTextFill(Color.BLACK);
		lstViewPrese.getItems().clear();
		lblVitaPersa.setVisible(false);
		btnIniziaNuovaMano.setVisible(false);
		lblTurnoGiocatore.setText("è il turno di: "+this.prt.getElencoGiocatori().get(countTurnoGiocatore).getNome());
		btnInizioTurnoGiocatore.setDisable(false);
		paneNumeroPrese.setVisible(true);

		//rimetto le carte coperte;
		//copriCarteGiocatore();

		//elimino le carte dal banco
		ArrayList<ImageView> listaCarteBanco = new ArrayList<ImageView>(Arrays.asList(imgCartaBanco1, imgCartaBanco2, imgCartaBanco3, imgCartaBanco4, imgCartaBanco5, imgCartaBanco6, imgCartaBanco7, imgCartaBanco8));
		for(ImageView i : listaCarteBanco) {	
			i.setImage(null);
		}

		//System.out.println(this.prt.getElencoGiocatori().get(0).getCarteMano().size());
	}


	@FXML Button btnPartitaTornaAllaHome;
	//torno all' interfaccia di login
	@FXML public void TornaAllaHome(ActionEvent actionEvent) {


		//chiudo la finestra di Gioco della partita e torno alla finestra di login iniziale
		Stage stage = (Stage)btnPartitaTornaAllaHome.getScene().getWindow();
		stage.close();

		//stabilire come fare il salvataggio della partita
		//implementare salvataggio
		if(this.prt.getElencoGiocatori().size()>1) {
			SalvaPartita(this.prt);
		}
		//System.out.println(prt.getElencoGiocatori().size());

		//riapro la finestra di login


		try {

			//root = FXMLLoader.load(getClass().getResource("Login.fxml"));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
			Parent root = loader.load();
			ControllerHome controller = loader.getController();
			Scene interfacciaHome = new Scene(root);
			controller.copiaInformazioniPartita(this.prt);

			stage.setScene(interfacciaHome);
			stage.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@FXML Button btnClassifica;

	//creo un pop-up che visualizzi la classifica
	@FXML public void VisualizzaClassifica(ActionEvent actionEvent) {
		VBox root = new VBox();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("popUpClassifica.fxml"));
			root = loader.load();

			ControllerPopUpClassifica controller = loader.getController();
			controller.populateListView();

			Stage stage = new Stage();
			stage.setTitle("Classifica");
			Scene scene = new Scene(root, 480, 310);
			stage.setScene(scene);
			stage.show();			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	//METODI AUSILIARI PER IL PASSAGGIO DEI DATI IN FASE DI RUN-TIME
	//metodo che passa i dati della partita in fase di run-time da un istanza della classe controller all'altra
	public void copiaInformazioniPartita(Partita tempPrt) {
		this.prt=tempPrt;
	}    

	//metodo che passa il numero di carte in fase di run-time da un istanza della classe controller all'altra
	public void copiaInformazioniNumCarte(int numeroCarteAGiocatore)
	{
		this.numeroCarteAGiocatore=numeroCarteAGiocatore;
	}

	//metodo che passa i dati della label lblTurnoGiocatore in fase di run-time da un istanza della classe controller all'altra
	public void copiaInformazioniLabel(Label lblTurnoGiocatore) {
		this.lblTurnoGiocatore=lblTurnoGiocatore;
	} 

	private void giocaCartaMano(int posCartaCliccata) {
		ArrayList<ImageView> listaCarteBanco = new ArrayList<ImageView>(Arrays.asList(imgCartaBanco1, imgCartaBanco2, imgCartaBanco3, imgCartaBanco4, imgCartaBanco5, imgCartaBanco6, imgCartaBanco7, imgCartaBanco8));
		ArrayList<ImageView> listaCarteMano = new ArrayList<ImageView>(Arrays.asList(imgCartaMano1, imgCartaMano2, imgCartaMano3, imgCartaMano4, imgCartaMano5));
		//controllo che il giocatore abbia iniziato il suo turno
		if(btnInizioTurnoGiocatoreClicked) {
			//"sposto" la carta giocata dalla mano al banco nel primo posto disponibile
			for(ImageView i : listaCarteBanco) {	
				if(i.getImage()==null) {
					i.setImage(new Image(getClass().getResourceAsStream(prt.getElencoGiocatori().get(countTurnoGiocatore).getCarteMano().get(posCartaCliccata).getPercorso())));
					listaCarteMano.get(posCartaCliccata).setImage(null);
					btnInizioTurnoGiocatoreClicked=false;
					btnFineTurnoGiocatore.setDisable(false);
					break;//ho inserito l'immagine nel tabellone quindi esco dal ciclo
				}
			}

			//rimuovo la carta dalla mano del gioccatore e la metto nella lista di carte del banco
			lstCarteBanco.add(this.prt.getElencoGiocatori().get(countTurnoGiocatore).removeCartaMano(posCartaCliccata));
		}	
	}

	//metodo che calcola quale giocatore ha perso il round appena concluso
	private int CalcolaPunti(ArrayList<Carta> listaCarteBanco) {
		int posGiocatore=-1;
		Carta cartaTemp = null;
		//confronto le carte sul banco e trovo quella che ha il punteggio minore la sua posizione mi dirà la posizione del giocatore che ha giocato la carta e perderà la vita
		for(Carta i : listaCarteBanco) {
			if(posGiocatore==-1) {
				cartaTemp=i;
				posGiocatore=0;
			}else if(i.getValore()>cartaTemp.getValore()){
				cartaTemp=i;
				posGiocatore=listaCarteBanco.indexOf(i);
			}
		}

		return posGiocatore;
	}

	private int quanteCarteAGiocatore(int numeroGiocatori) {
		if(numeroGiocatori>4) {
			return 5;
		}else if(numeroGiocatori == 2){
			return 1;
		}else {
			return numeroGiocatori;
		}
	}

	private void cominciaNuovaMano() {
		//si è conclusa una mano quindi ne inizio un'altra se non c'è un vincitore
		//decremento se necessario il numero di carte
		if(numeroCarteAGiocatore>1) {
			numeroCarteAGiocatore--;
		}else if (numeroCarteAGiocatore==1) {//quando arrivo a una carta a giocatore rido le carte in base al numero di gioc atori
			numeroCarteAGiocatore = quanteCarteAGiocatore(this.prt.getElencoGiocatori().size());
		}else if (this.prt.getElencoGiocatori().size()==2) {
			numeroCarteAGiocatore=1;
		}

		if(this.prt.getElencoGiocatori().size()>1) {
			//inizio una nuova mano e do le carte a ogni giocatore
			mazzo=new Mazzo();
			mazzo.mescola();
			for(Giocatore g : this.prt.getElencoGiocatori()) {
				g.setCarteMano(mazzo.pescaCarte(numeroCarteAGiocatore));
			}

			btnIniziaNuovaMano.setVisible(true);
		}
	}

	private void copriCarteGiocatore() {
		ArrayList<ImageView> listaCarteMano = new ArrayList<ImageView>(Arrays.asList(imgCartaMano1, imgCartaMano2, imgCartaMano3, imgCartaMano4, imgCartaMano5));
		//rimetto le carte coperte
		for(int i=0; i< listaCarteMano.size();i++) {
			if(countTurnoGiocatore+1<this.prt.getElencoGiocatori().size()&&i<this.prt.getElencoGiocatori().get(countTurnoGiocatore+1).getCarteMano().size()) {
				listaCarteMano.get(i).setImage(new Image(getClass().getResourceAsStream(pathRetroCarta)));
			}else {
				listaCarteMano.get(i).setImage(null);
			}
		}
	}

	private void aggiornaClassifica(String path) {
		try{
			final int puntiVincitore=10;
			ArrayList<String> data = new ArrayList<String>();
			File file = new File(path);
			Scanner scan = new Scanner(file);			
			boolean presenzaGiocatore=false;
			while(scan.hasNext()) {
				String line = scan.nextLine();
				String[] lineData = line.split(" , ");
				//controllo il nome salvato su file e lo confronto col vincitore
				if(lineData[1].equals(this.prt.getElencoGiocatori().get(0).getNome())) {

					//incremento il punteggio
					lineData[0]=""+(puntiVincitore+Integer.parseInt(lineData[0]));
					line = lineData[0]+" , "+lineData[1]+"\n";
					presenzaGiocatore=true;
				}	

				//mi salvo la riga appena letta
				data.add(line+"\n");
			}
			scan.close();

			FileWriter fw = new FileWriter(file);
			//Riscrivo il file con le opportune modifiche
			for(String l : data) {
				fw.write(l);
			}
			if(!presenzaGiocatore) {
				fw.write(puntiVincitore+" , "+this.prt.getElencoGiocatori().get(0).getNome());
			}
			fw.close();
		} catch (FileNotFoundException FNFe) {
			// TODO Auto-generated catch block
			FNFe.printStackTrace();
		} catch (IOException IOe) {
			// TODO Auto-generated catch block
			IOe.printStackTrace();
		}
	}


	//controlla
	/*
    private void SalvaPartita(Partita partita) {
    	try {
    		String path="src/SalvataggioPartite.txt";
    		FileOutputStream file = new FileOutputStream(path);
    		ObjectOutputStream objOutput = new ObjectOutputStream(file);
    		objOutput.writeObject(partita);
    		objOutput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private Partita CaricaPartita(String codicePartita) {
    	try {
    		String path="src/SalvataggioPartite.txt";
    		FileInputStream file = new FileInputStream(path);
    		ObjectInputStream objInput = new ObjectInputStream(file);
    		//objInput.setObjectInputFilter(objInput.getObjectInputFilter());
    		Partita partita=new Partita();
    		while(true) {
    		    try {
    	    		partita= (Partita) objInput.readObject();
    	    		if(partita.getCodice().equals(codicePartita)) {
    	    			System.out.println("nice");
    	    		}
    		    } catch (EOFException e) {
    		         // end of file reached
    				//e.printStackTrace();
	    			//System.out.println("male");
    	    		objInput.close();
    	    		return partita;

    		    }
    		}    		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }*/

	private void SalvaPartita(Partita partita) {
		try {
			Gson gson = new Gson();
			ArrayList<Partita> elencoPartite = new ArrayList<Partita>();
			Boolean presenzaPrt=false;
			String path="src/SalvataggioPartite.json";
			File file = new File(path);
			Scanner scan = new Scanner(file);
			scan.useDelimiter("%");

			//carico il contenuto del file
			while(scan.hasNext()) {
				String dati =scan.next();
				Partita p = gson.fromJson(dati, Partita.class);
				elencoPartite.add(p);
			}
			scan.close();

			//controllo se la partita da salvare era già presente nel file
			for(Partita p : elencoPartite) {
				if(p.getCodice().equals(partita.getCodice())) {
					presenzaPrt=true;
					p=partita;
				}
			}

			//se la partita non era presente nel file la aggiungo in coda e la salvo
			if(!presenzaPrt) {
				FileWriter fw = new FileWriter(file, true);

				String datiGson = gson.toJson(partita);
				fw.write(datiGson+"%");
				fw.close();
			}else { //salvo la lista di partite caricate dal file
				FileWriter fw = new FileWriter(file);

				for(Partita p : elencoPartite) {
					String datiGson = gson.toJson(p);
					fw.write(datiGson+"%");
				}
				fw.close();
			}
		} catch (FileNotFoundException fnfe) {
			// TODO Auto-generated catch block
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			// TODO Auto-generated catch block
			ioe.printStackTrace();
		}
	}
}