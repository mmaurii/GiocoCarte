package basic;

import java.io.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.application.Platform;
import javafx.scene.control.ListCell;


public class ControllerHome {
	//variabili di controllo
    final int lungCodicePartita=10;
    final int nViteDefault=5;
    Partita prt;
    Mazzo mazzo = new Mazzo();
    ArrayList<Giocatore> giocatoriPrt = new ArrayList<Giocatore>();
    String pathRetroCarta = "/basic/IMGcarte/retro.jpg";
    String pathClassifica = "src/Classifica.txt";
    String pathStatus = "src/Status.txt";
    
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
        	//se la password è corretta chiudo la finestra di home
        	Stage stage = (Stage)btnLogin.getScene().getWindow();
        	stage.close();
        	//apro la finestra delle impostazioni e creazine partite e tornei
			
			try {
	            VideoBackgroundPane videoBackgroundPane = new VideoBackgroundPane("src/v1.mp4");

				FXMLLoader loader = new FXMLLoader(getClass().getResource("PartitaTorneo.fxml"));
				Parent root = loader.load();
				
	            StackPane stackPane = new StackPane();
	            stackPane.setStyle("-fx-background-color: #38B6FF;");
	            stackPane.getChildren().addAll(videoBackgroundPane, root);
				
				stage.setTitle("Menu");
				Scene interfacciaPartitaTorneo = new Scene(stackPane, 600, 400);
				
				
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
    
	//avvio l'interfaccia di gioco
    @FXML Label lblTurnoGiocatore;
    @FXML Button btnGioca;
    @FXML TextField txtCodPartita;
    @FXML Label lblCodPartitaErrato;
    @FXML ListView<String> lstViewVite;
    @FXML public void avviaPartita(ActionEvent actionEvent) {
    	//ottengo il codice partita inserito dall'utente
    	String codPartita = txtCodPartita.getText();
    	Partita p = verificaDisponibilitaPartita(codPartita); //se la partita è presente sul file.json la carico se no assegno quella appena creata o null se non si trova 
    	if(p==null) {
    		//se la partita non viene trovata mando un messaggio di errore
    		lblCodPartitaErrato.setText("errore il codice partita è sbagliato, inseriscine uno corretto");
    	}else {
    		boolean resume = false;
    		//controllo se la partita è stata caricata da file(resume) o è stat appena aggiunta
    		if(prt!=null) {
    			if(this.prt.getCodice().equals(p.getCodice())) {
    				//no resume partita
    				resume=false;
    			}else {//resume partita
    				resume=true;
    			}
    		}
    		
			this.prt=p;
    		
    		//controllo che la partita non sia già stata conclusa
    		if(this.prt.getElencoGiocatori().size()>1) {
//    			prt.resume=resume;
    			avviaPartita();
    		}else {
    			lblCodPartitaErrato.setText("la partita ha un solo giocatore, perchè si è già conclusa");
    		}
    	}

    }

    
    //METODI AUSILIARI PER IL PASSAGGIO DEI DATI IN FASE DI RUN-TIME
    //metodo che passa i dati della partita in fase di run-time da un istanza della classe controller all'altra
    public void copiaInformazioniPartita(Partita tempPrt) {
    	this.prt=tempPrt;
	}    
    
    @FXML ListView<String> lstClassifica;
    public void populateListView() {
    	
		try {
			File file = new File(pathClassifica);
			Scanner scan = new Scanner(file);			
			while(scan.hasNext()) {
				String line = scan.nextLine();
				lstClassifica.getItems().add(line);	
			}
			scan.close();
		} catch (FileNotFoundException fnfe) {
			// TODO Auto-generated catch block
			fnfe.printStackTrace();
		}
	
		//ordino in base al punteggio la listview
		lstClassifica.getItems().sort(Comparator.reverseOrder());			
		int counter=1;
		ArrayList<String> listaNumerata = new ArrayList<String>();
		for(String i : lstClassifica.getItems()) {
			i=(counter+"\t"+i);
			listaNumerata.add(i);
			counter++;
		}
		
		//metto il contenuto della listview in grassetto
		lstClassifica.setStyle("-fx-font-weight: bold;");
		
		//centro le scritte all'interno della listview
		lstClassifica.setCellFactory(param -> new ListCell<String>() {
		    @Override
		    protected void updateItem(String item, boolean empty) {
		        super.updateItem(item, empty);
		        if (empty || item == null) {
		            setText(null);
		            setGraphic(null);
		        } else {
		            TextFlow textFlow = new TextFlow();
		            Text text = new Text(item);
		            text.setStyle("-fx-fill: black; -fx-font-size: 14px;"); // Imposta lo stile del testo
		            textFlow.getChildren().add(text);

		            // Imposta l'allineamento del testo a giustificato
		            textFlow.setTextAlignment(TextAlignment.JUSTIFY);
		            textFlow.setLineSpacing(5); // Regola lo spaziamento tra le righe se necessario

		            setGraphic(textFlow);
		        }
		    }
		});
		//mostro in output la classifica
		lstClassifica.getItems().setAll(listaNumerata);
    }
    
    private void avviaPartita() {
		//chiudo la finestra di home e apro quella di gioco
		Stage stage = (Stage)btnGioca.getScene().getWindow();
		stage.close();
		
		//apro la finestra di gioco
		BorderPane root = new BorderPane();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Partita.fxml"));
			//Locale locale = new Locale(); "Partita",this.prt
			//loader.setResources(ResourceBundle.getBundle("Partita",));
			root = loader.load();
			ControllerPartita controller = loader.getController();
			//definisco chi giocherà il primo turno
			Giocatore gio = this.prt.getGiocatoreCorrente();
			lblTurnoGiocatore = new Label("è il turno di: "+gio.getNome());
			Scene interfacciaDiGioco = new Scene(root);
			stage.setScene(interfacciaDiGioco);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                 Platform.exit();
                 controller.SalvaPartita(prt);
                 System.exit(0);
                }
            });


//            if(resume){//se sono in modalita di gioco carte apporto delle modifiche all'interfaccia
//            	GridPane gp = (GridPane) root.getCenter();
//            	Iterator<Node> i =gp.getChildren().iterator();
//            	while(i.hasNext()) {
//            		Object o = i.next();
//            		if(o instanceof GridPane) {
//            			GridPane gridpane= (GridPane) o;
//            			if(gridpane.getId().equals("gridPaneVite")) {
//            				Iterator<Node> y = gridpane.getChildren().iterator();
//            				while(y.hasNext()) {
//            					Object obj = y.next();
//            					if(obj instanceof ListView) {
//            						ListView<String> lst = (ListView<String>) obj;
//            						if(lst.getId().equals("lstViewVite")) {
//            							//mostro le vite di ogni giocatore
//            							for(Giocatore g : prt.getElencoGiocatori()) {
//            								lst.getItems().add(g.getNome()+" "+g.getVite()+" vite");
//            							}
//            							for(String nome : prt.getElencoGiocatoriEliminati()) {
//            								lst.getItems().add(nome+" è eliminato");
//            							}
//            						}
//            					}
//            				}
//            			}else if(gridpane.getId().equals("gridPanePreseDichiarate")) {
//            				Iterator<Node> y = gridpane.getChildren().iterator();
//            				while(y.hasNext()) {
//            					Object obj = y.next();
//            					if(obj instanceof ListView) {
//            						ListView<String> lst = (ListView<String>) obj;
//            						if(lst.getId().equals("lstViewPrese")) {
//            							//mostro le prese dichiarate da chi lo ha fatto
//            							for(Giocatore g : prt.getElencoGiocatori()) {
//            								lst.getItems().add(g.getNome()+" "+g.getPreseDichiarate()+" prese");
//            							}
//            						}
//            					}
//            				}
//            			}
//            		}
//            	}
//            }

            //			if(!prt.isModalitaPrt()){//se sono in modalita di gioco carte apporto delle modifiche all'interfaccia
            //				GridPane gp = (GridPane) root.getCenter();
            //				Iterator<Node> i =gp.getChildren().iterator();
            //				while(i.hasNext()) {
            //					Object o = i.next();
            //					if(o instanceof Label) {
            //						Label lbl = (Label) o;
            //						if(lbl.getId().equals("lblPrese")) {
            //							lbl.setVisible(false);
            //						}
            //					}
            //					
            //					if(o instanceof GridPane) {
            //						GridPane gridpane= (GridPane) o;
            //						if(gridpane.getId().equals("gridPaneNumeroPrese")) {
            //							gridpane.setVisible(false);
            //						}
            //					}
            //				}
            //			}

            stage.show();

            //copio le informazioni relative alla partita in corso
            controller.copiaInformazioniPartita(prt);
            //copio le informazioni relative alla label lblTurnoGiocatore
            controller.copiaInformazioniLabel(lblTurnoGiocatore);

			if(gio instanceof Bot) {
				Bot b = (Bot)gio;
				controller.t = new Thread(b);			
				controller.t.setDaemon(true);
				Platform.runLater(controller.t);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private Partita verificaDisponibilitaPartita(String codicePartita) {
    	if(this.prt!=null) {//controllo che la partita sia stata creata
    		if(!this.prt.getCodice().equals(codicePartita)) {//se il codice della partita non è di quella appena inserita la carico da file
    			return CaricaPartita(codicePartita);
    		}
        	return this.prt;
    	}else {//provo a caricarla da file
    		return CaricaPartita(codicePartita);
    	}
    }
    
    private Partita CaricaPartita(String codicePartita) {
		try {
			GsonBuilder gsonBuilder = new GsonBuilder();
			//imposto un TypeAdapter per salvare correttamente l'elenco dei giocatori che contiene sia Bot che Giocatori
			gsonBuilder.registerTypeAdapter(new TypeToken<ArrayList<Giocatore>>() {}.getType(), new ElencoGiocatoriTypeAdapter());
			Gson gson=gsonBuilder.create();
			ArrayList<Partita> elencoPartite = new ArrayList<Partita>();
			Partita prtTrovata=null;
			String path="src/SalvataggioPartite.json";
			FileReader fr = new FileReader(path);
			JsonReader jsnReader=new JsonReader(fr);
			
			if(jsnReader.peek() != JsonToken.NULL){
				jsnReader.beginArray();
				//carico il contenuto del file
				while(jsnReader.hasNext()) {
					Partita p = gson.fromJson(jsnReader, Partita.class);
					if(p.getCodice().equals(codicePartita)) {
						//mi salvo la partita richiesta
						prtTrovata=p;
					}else {
						elencoPartite.add(p);
					}
				}
				jsnReader.endArray();
				jsnReader.close();
			}

			//salvo la lista di partite senza quella richiesta se trovata
			if(prtTrovata!=null) {
				FileWriter fw = new FileWriter(path);
				JsonWriter jsnWriter = new JsonWriter(fw);
				
				//rimuovo la partita richiesta dall'elenco e lo salvo
				elencoPartite.remove(prtTrovata);
				
				jsnWriter.beginArray();
				for (Partita p : elencoPartite) {
					gson.toJson(p, Partita.class, jsnWriter);
					fw.write('\n');
				}
				jsnWriter.endArray();
				jsnWriter.close();
			}
			return prtTrovata;
		} catch (FileNotFoundException fnfe) {
			// TODO Auto-generated catch block
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			// TODO Auto-generated catch block
			ioe.printStackTrace();
		}
		return null;
    }
}