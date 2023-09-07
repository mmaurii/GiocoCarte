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

import java.io.IOException;
import java.util.*;
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
import javafx.scene.control.TextField;

public class Controller {
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
    //boolean primoGiro=true;
    
    //eventi FXML
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnLogin;
    //login
    @FXML public void loginAction(ActionEvent actionEvent) {

        String username_text = txtUsername.getText();
        String password_text = txtPassword.getText();
        Amministratore a = new Amministratore(username_text, password_text);
        //controllo nonme utente e password
        if(a.verificaAdmin())
        {
        	//se la password è corretta chiudo la finestra di login
        	Stage stage = (Stage)btnLogin.getScene().getWindow();
        	stage.close();
        	//apro la finestra delle impostazioni e creazine partite e tornei
			Parent root;
			try {
				root = FXMLLoader.load(getClass().getResource("PartitaTorneo.fxml"));
				stage.setTitle("Menu");
				Scene interfacciaPartitaTorneo = new Scene(root);
				stage.setScene(interfacciaPartitaTorneo);
				stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }else {
        	//avvisa dell' errore
        }
    }
    
    
    @FXML Button btnCreaPartita;
    @FXML ComboBox<String> comboNVite;
    //crea partita
    @FXML public void CreaPartitaAction(ActionEvent actionEvent) {
    	//chiudo la finestra di scelta per la creazione di partite o tornei
    	Stage stage = (Stage)btnCreaPartita.getScene().getWindow();
    	stage.close();
    	
    	//apro la finestra per la creazine delle partite
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("CreaPartita.fxml"));
			stage.setTitle("Crea una Partita");
			Scene interfacciaCreaPartita = new Scene(root);
			stage.setScene(interfacciaCreaPartita);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    
    //crea torneo
    @FXML Button btnCreaTorneo;
    @FXML public void CreaTorneoAction(ActionEvent actionEvent) {
    	Stage stage = (Stage)btnCreaTorneo.getScene().getWindow();
    	stage.close();
    	
    	//apro la finestra per la creazione del torneo
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("CreaTorneo.fxml"));
			stage.setTitle("Crea un Torneo");
			Scene interfacciaCreaTorneo = new Scene(root);
			stage.setScene(interfacciaCreaTorneo);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    //aggiungo alla partita un utente  
    @FXML ListView<String> listUtentiPartita;
    @FXML Button btnAggiungiUtente;
    @FXML TextField txtNomeUtente; 
    @FXML public void AggiungiUtente(ActionEvent actionEvent) {
    	String nome = txtNomeUtente.getText();
    	//controllo che non vengano inseriti giocatori con lo stesso nome
    	if(!listUtentiPartita.getItems().contains(nome)) {
    		txtNomeUtente.clear();
    		listUtentiPartita.getItems().add(nome);
    		giocatoriPrt.add(new Giocatore(nome));
    	}else {
    		txtNomeUtente.clear();
    	}
    }

    
    //Genero il codice per una nuova partita
    @FXML Button btnGeneraCodice;
    @FXML Label lblCodice;
    @FXML public void GeneraCodice(ActionEvent actionEvent) {
    	try {
    		File file = new File("src/Status.txt");
    		Scanner scan = new Scanner(file);//controlla errori legati alla lettura e scrittura del file
    		String codPartita = scan.nextLine().split(" , ")[1];
    		scan.close();
    		
    		//controllare univocita
    		codPartita = Integer.toString(Integer.parseInt(codPartita)+1);

    		//aggiungo al codice gli 0 non rilevanti
    		int nCifre = codPartita.length();
    		for(int i=0; i<lungCodicePartita-nCifre; i++) {
    			codPartita="0"+codPartita;
    		}

    		lblCodice.setText(codPartita);
    		btnGeneraCodice.setDisable(true);

    		//salvo il codice corrente nel file di status
    		FileWriter fw = new FileWriter(file);
    		fw.write("codicePartita , "+codPartita);
    		fw.close();
    		
    		//do le vite e le carte ai giocatori
    		String nVite = comboNVite.getSelectionModel().getSelectedItem();
    		if(nVite!=null) {
    			for(Giocatore i : giocatoriPrt) {
    				i.setVite(Integer.parseInt(nVite));
    			}
    		}else {
    			for(Giocatore i : giocatoriPrt) {
    				i.setVite(nViteDefault);
    			}
    		}

    		//imposto i dati di una nuova partita
    		prt=new Partita(codPartita, giocatoriPrt);
    	}catch(FileNotFoundException e) {
    		System.out.println(e);
    	}catch(IOException eIO) {
    		System.out.println(eIO);    		
    	}
    }

    
    @FXML Button btnTornaAlLogin;
    //torno alla Schermata di login
    @FXML public void TornaAlLogin(ActionEvent actionEvent) {
    	//chiudo la finestra di di creazione della partita e torno alla finestra di login
    	Stage stage = (Stage)btnTornaAlLogin.getScene().getWindow();
    	stage.close();
    	
    	//riapro la finestra di login
		try {
			//root = FXMLLoader.load(getClass().getResource("Login.fxml"));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
			Parent root = loader.load();
			Controller controller = loader.getController();
			Scene interfacciaLogin = new Scene(root);
			controller.copiaInformazioniPartita(prt);

			stage.setScene(interfacciaLogin);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    }


	//avvio l'interfaccia di gioco
    @FXML Button btnGioca;
    @FXML TextField txtCodPartita;
    @FXML Label lblCodPartitaErrato;
    @FXML ListView<String> lstViewVite;
    @FXML public void avviaPartita(ActionEvent actionEvent) {
    	//ottengo il codice partita inserito dall'utente
    	String cod = txtCodPartita.getText();
    	if(prt!=null)//controllo che venga creata una partita per poterne confrontare il codice
    		if(cod.equals(this.prt.getCodice())) {
    			//chiudo la finestra di login e apro quella di gioco
    			Stage stage = (Stage)btnGioca.getScene().getWindow();
    			stage.close();

    			//apro la finestra di gioco
    			Group root = new Group();
    			try {
    				//root = FXMLLoader.load(getClass().getResource("Partita.fxml"));
    				FXMLLoader loader = new FXMLLoader(getClass().getResource("Partita.fxml"));
    				root = loader.load();
    				Controller controller = loader.getController();
    				//definisco chi giocherà il primo turno
        			lblTurnoGiocatore = new Label("è il turno di: "+this.prt.getElencoGiocatori().get(countTurnoGiocatore).getNome());
        			lblTurnoGiocatore.setTextFill(Color.BLACK);
        			lblTurnoGiocatore.setFont(Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 24));
        			lblTurnoGiocatore.setId("lblTurnoGiocatore");
        			root.getChildren().add(lblTurnoGiocatore);
        			Scene interfacciaDiGioco = new Scene(root);
        			stage.setScene(interfacciaDiGioco);
        			stage.show();
        			lblTurnoGiocatore.setTranslateX(190);
        			lblTurnoGiocatore.setTranslateY(220);
        			
    				//copio le informazioni relative alla partita in corso
    				controller.copiaInformazioniPartita(prt);
    				//copio le informazioni relative alla label lblTurnoGiocatore
        			controller.copiaInformazioniLabel(lblTurnoGiocatore);
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}

    			//do le carte a ogni giocatore
    	    	mazzo.mescola();
    	    	numeroCarteAGiocatore=quanteCarteAGiocatore(prt.getElencoGiocatori().size());
    	    	for(Giocatore g : prt.getElencoGiocatori()) {
    	    		g.setCarteMano(mazzo.pescaCarte(quanteCarteAGiocatore(prt.getElencoGiocatori().size())));
    	    	}
    		}else {
    			lblCodPartitaErrato.setText("errore il codice partita è sbagliato,\ninseriscine uno corretto");
    		}
    }


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
    	lblManoGiocatore.setText("Mano di "+this.prt.getElencoGiocatori().get(countTurnoGiocatore).getNome());
    	lblTurnoGiocatore.setVisible(false);
    	lblManoGiocatore.setVisible(true);
    	btnInizioTurnoGiocatore.setDisable(true);
    	ArrayList<ImageView> listaCarteMano = new ArrayList<ImageView>(Arrays.asList(imgCartaMano1, imgCartaMano2, imgCartaMano3, imgCartaMano4, imgCartaMano5));

    	//mostro le carte in output relative al giocatore del turno corrente
    	for(int i = 0; i<listaCarteMano.size();i++) {	
			if(i<this.prt.getElencoGiocatori().get(countTurnoGiocatore).getCarteMano().size()) {
    		Image newImg = new Image(getClass().getResourceAsStream(prt.getElencoGiocatori().get(countTurnoGiocatore).getCarteMano().get(i).getPercorso()));
    		listaCarteMano.get(i).setImage(newImg);
			}else {
				listaCarteMano.get(i).setImage(null);
			}
    	}

    	btnInizioTurnoGiocatoreClicked=true;
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
    int presePerQuestaMano=0;
    //dispongo la fine del turno per il giocatore corrente
    @FXML public void fineTurnoGiocatore(ActionEvent actionEvent) {
    	lblNumPreseNonValido.setVisible(false);
    	int numeroPreseGiocatore=0;
    	try {
    		if(paneNumeroPrese.isVisible()) {
    			numeroPreseGiocatore=Integer.parseInt(txtNumeroPrese.getText());
    			this.prt.getElencoGiocatori().get(countTurnoGiocatore).setPreseDichiarate(numeroPreseGiocatore);
    			presePerQuestaMano+=numeroPreseGiocatore;
    		}else {
    			presePerQuestaMano=0;
    		}
    		
    		if(presePerQuestaMano!=this.prt.getElencoGiocatori().get(countTurnoGiocatore).getCarteMano().size()+1||countTurnoGiocatore!=this.prt.getElencoGiocatori().size()) {
    			if(paneNumeroPrese.isVisible()) {
    				//visualizzo il numero di prese per questo giocatore
    				lstViewPrese.getItems().add(this.prt.getElencoGiocatori().get(countTurnoGiocatore).getNome()+" "+numeroPreseGiocatore+" prese");
    				//visualizzo il numero di vite di questo giocatore
    				//imposto le vite dei giocatori nella relativa list view
    				lstViewVite.getItems().add(this.prt.getElencoGiocatori().get(countTurnoGiocatore).getNome()+" "+this.prt.getElencoGiocatori().get(countTurnoGiocatore).getVite()+" vite");
    			}

    			ArrayList<ImageView> listaCarteMano = new ArrayList<ImageView>(Arrays.asList(imgCartaMano1, imgCartaMano2, imgCartaMano3, imgCartaMano4, imgCartaMano5));
    			//rimetto le carte coperte
    			for(int i=0; i< listaCarteMano.size();i++) {
    				if(countTurnoGiocatore<this.prt.getElencoGiocatori().size()&&i<this.prt.getElencoGiocatori().get(countTurnoGiocatore).getCarteMano().size()) {
    					listaCarteMano.get(i).setImage(new Image(getClass().getResourceAsStream(pathRetroCarta)));
    				}else {
    					listaCarteMano.get(i).setImage(null);
    				}
    			}

    			//controllo che non si sfori il numero di giocatori
    			if(countTurnoGiocatore<prt.getElencoGiocatori().size()) {
    				//passo il turno al prossimo giocatore incrementando il contatore
    				countTurnoGiocatore++;
    			}else {
    				btnInizioTurnoGiocatore.setDisable(true);
    			}

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

    					//disabilito la possibilità di dire quante prese si fanno
    					paneNumeroPrese.setVisible(false);

    					//Calcolo chi prende in base alle carte sul banco
    					int giocatoreChePrende = CalcolaPunti(lstCarteBanco);

    					Giocatore gio = this.prt.getElencoGiocatori().get(giocatoreChePrende);
    					this.prt.getElencoGiocatori().get(giocatoreChePrende).setPreseEffettuate();
    					lblVitaPersa.setText(gio.getNome()+" ha PRESO questa mano");
    					lblVitaPersa.setVisible(true);
    					lstCarteBanco.clear();
    					lblManoGiocatore.setVisible(false);
    					btnFineTurnoGiocatore.setDisable(true);
    					btnIniziaNuovoRound.setVisible(true);
    				}
    			}else {
					//azzero il contatore dei turni
					countTurnoGiocatore=0;
					
					//Calcolo chi prende in base alle carte sul banco
					int giocatoreChePrende = CalcolaPunti(lstCarteBanco);

					Giocatore gio = this.prt.getElencoGiocatori().get(giocatoreChePrende);
					this.prt.getElencoGiocatori().get(giocatoreChePrende).setPreseEffettuate();
					lblVitaPersa.setText(gio.getNome()+" ha PRESO questa mano");
					lblVitaPersa.setVisible(true);
					lstCarteBanco.clear();
					lblManoGiocatore.setVisible(false);
					btnFineTurnoGiocatore.setDisable(true);
					
					btnIniziaNuovaMano.setVisible(true);
					
    				//verifico chi ha sbagliato a dichiarare e gli rimuovo la vita
							
    				for(Giocatore g : this.prt.getElencoGiocatori()) {
    					
    					System.out.println(g.getPreseDichiarate());
    					System.out.println(g.getPreseEffettuate());
    					
    					if(g.getPreseDichiarate() != g.getPreseEffettuate())
    					{
    	    		    	System.out.println("nuova mano");
    						g.perdiVita();
    						for(String s : lstViewVite.getItems()) {
    							if(s.contains(g.getNome())) {
    								s=g.getNome()+" "+g.getVite()+" vite";
    							}
    						}
    					}
    				}

    				
    				for(Giocatore g : this.prt.getElencoGiocatori()) {
    					if(g.getVite()==0)
    					{	
    						giocatoriPrt.remove(g);
    						for(String s : lstViewVite.getItems()) {
    							if(s.contains(g.getNome())) {
    	    						lstViewVite.getItems().remove(s);
    							}
    						}
    					}
    				}
    				
    				//si è conclusa una mano quindi ne inizio un'altra se non c'è un vincitore
    				numeroCarteAGiocatore--;
    				cominciaNuovaMano(numeroCarteAGiocatore);//il metodo conclude la partita quando rimane un solo giocatore
    				
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
    	

    }
    

    @FXML public void IniziaNuovoRound(ActionEvent actionEvent) {
    	//sistemo l'interfaccia perchè possa essere giocato un nuovo round
    	btnIniziaNuovoRound.setVisible(false);
    	lblVitaPersa.setVisible(false);
    	lblManoGiocatore.setVisible(true);
    	btnInizioTurnoGiocatore.setDisable(false);
    	
    	ArrayList<ImageView> listaCarteBanco = new ArrayList<ImageView>(Arrays.asList(imgCartaBanco1, imgCartaBanco2, imgCartaBanco3, imgCartaBanco4, imgCartaBanco5, imgCartaBanco6, imgCartaBanco7, imgCartaBanco8));
    	for(ImageView i : listaCarteBanco) {
    		i.setImage(null);
    	}
    }
    
    @FXML Button btnIniziaNuovaMano;
    @FXML public void IniziaNuovaMano(ActionEvent actionEvent) {
    	//sistemo l'interfaccia per poter iniziare la nuova mano
		lblPrese.setTextFill(Color.BLACK);
    	lstViewPrese.getItems().clear();
    	btnIniziaNuovaMano.setVisible(false);
		lblTurnoGiocatore.setText("è il turno di: "+this.prt.getElencoGiocatori().get(countTurnoGiocatore).getNome());
		btnInizioTurnoGiocatore.setDisable(false);
		paneNumeroPrese.setVisible(true);
		
		//rimetto le carte coperte
		ArrayList<ImageView> listaCarteMano = new ArrayList<ImageView>(Arrays.asList(imgCartaMano1, imgCartaMano2, imgCartaMano3, imgCartaMano4, imgCartaMano5));
		for(int i=0; i< listaCarteMano.size();i++) {
			if(countTurnoGiocatore<this.prt.getElencoGiocatori().size()&&i<this.prt.getElencoGiocatori().get(countTurnoGiocatore).getCarteMano().size()) {
				listaCarteMano.get(i).setImage(new Image(getClass().getResourceAsStream(pathRetroCarta)));
			}else {
				listaCarteMano.get(i).setImage(null);
			}
		}
		
		//elimino le carte dal banco
    	ArrayList<ImageView> listaCarteBanco = new ArrayList<ImageView>(Arrays.asList(imgCartaBanco1, imgCartaBanco2, imgCartaBanco3, imgCartaBanco4, imgCartaBanco5, imgCartaBanco6, imgCartaBanco7, imgCartaBanco8));
    	for(ImageView i : listaCarteBanco) {	
    		i.setImage(null);
		}
    	
    	System.out.println(this.prt.getElencoGiocatori().get(0).getCarteMano().size());
    }

    
    //METODI AUSILIARI PER IL PASSAGGIO DEI DATI IN FASE DI RUN-TIME
    //metodo che passa i dati della partita in fase di run-time da un istanza della classe controller all'altra
    private void copiaInformazioniPartita(Partita tempPrt) {
    	this.prt=tempPrt;
	}

    //metodo che passa i dati della label lblTurnoGiocatore in fase di run-time da un istanza della classe controller all'altra
    private void copiaInformazioniLabel(Label lblTurnoGiocatore) {
    	this.lblTurnoGiocatore=lblTurnoGiocatore;
	}

    //metodo che passa i dati della label lblTurnoGiocatore in fase di run-time da un istanza della classe controller all'altra
    private void copiaInformazioniCartaMano5(ImageView img) {
    	this.imgCartaMano5=img;
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
    
    private void cominciaNuovaMano(int numCarte) {
    	if(this.prt.getElencoGiocatori().size()>1) {
    		//inizio una nuova mano
			//do le carte a ogni giocatore
    		mazzo.popolaMazzo();
	    	mazzo.mescola();
	    	for(Giocatore g : prt.getElencoGiocatori()) {
	    		g.setCarteMano(mazzo.pescaCarte(numCarte));
	    	}
	    	
	    	btnIniziaNuovaMano.setVisible(true);
	    	System.out.println("richiamo il metodo per la nuova mano");

	    	
    	}else {
    		//concludo la partita e ne annuncio il vincitore
    		
    	}
    }
}