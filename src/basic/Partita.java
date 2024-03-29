package basic;

import java.util.ArrayList;

public class Partita {
	private Mazzo m;
	private ArrayList<Giocatore> elencoGiocatori;
	private ArrayList<String> elencoGiocatoriEliminati = new ArrayList<String>();
	private String cod;
	private int countTurnoGiocatore=0;
	private ArrayList<Carta> lstCarteBanco = new ArrayList<Carta>();
	boolean dichiaraPrese=true;
	int numeroCarteAGiocatore;
	boolean modalitaPrt = true; //il valore true indica che sono in fase di dichiarazione prese, il valore false indica che sono in fase di gioco delle carte
	public int presePerQuestaMano;
	boolean resume=false;
	boolean	btnInizioTurnoGiocatoreDisable;
	boolean btnFineTurnoGiocatoreDisable;
	boolean btnIniziaNuovaManoVisible;
	boolean btnIniziaNuovoRoundVisible;
	boolean btnInizioTurnoGiocatoreClicked; //se false non fa giocare le carte al giocatore
	String lblVitaPersaText;
	boolean flagTorneo = false;
	
	public Partita() {}
	
	public Partita(String cod) {
		this.cod=cod;
	}

	public Partita(String cod, ArrayList<Giocatore> elencoGiocatori, Mazzo m) {
		this.cod=cod;
		this.elencoGiocatori=elencoGiocatori;
		this.m=m;
	}
	
	public Partita(String cod, ArrayList<Giocatore> elencoGiocatori) {
		this.cod=cod;
		this.elencoGiocatori=elencoGiocatori;
	}
	
	/**
	 * controlla se la partita è in modalità di dichiarazione delle prese
	 * @return true se è in modalità dichiara prese, false altrimenti
	 */
	public boolean isDichiaraPrese() {
		return dichiaraPrese;
	}

	public void setDichiaraPrese(boolean dichiaraPrese) {
		this.dichiaraPrese = dichiaraPrese;
	}
	
	/**
	 * controlla se la partita era già stata lanciata
	 * @return true se era già stata lancianta, false altrimenti
	 */
	public boolean isResume() {
		return resume;
	}

	public void setResume(boolean resume) {
		this.resume = resume;
	}
	
	/**
	 * restituisce la posizione del giocatore che deve giocare il turno corrente
	 * @return posizione del giocatore che deve giocare
	 */
	public int getCountTurnoGiocatore() {
		return countTurnoGiocatore;
	}

	public void setCountTurnoGiocatore(int countTurnoGiocatore) {
		this.countTurnoGiocatore = countTurnoGiocatore;
	}
	
	public String getCodice() {
		return cod;
	}

	public void setCodice(String codice) {
		this.cod=codice;
	}
	
	public void setElencoGiocatori(ArrayList<Giocatore> elencoGiocatori){
		this.elencoGiocatori=elencoGiocatori;
	}
	
	public ArrayList<Giocatore> getElencoGiocatori(){
		return elencoGiocatori;
	}
	
	public Mazzo getMazzo() {
		return m;
	}

	public void setMazzo(Mazzo m) {
		this.m=m;
	}
	
	/**
	 * restituisce il giocatore che deve giocare il turno corrente
	 * @return Giocatore 
	 */
	public Giocatore getGiocatoreCorrente() {
		return elencoGiocatori.get(countTurnoGiocatore);
	}
	
	public Giocatore getGiocatore(int pos) {
		return elencoGiocatori.get(pos);
	}

	public ArrayList<Carta> getLstCarteBanco() {
		return lstCarteBanco;
	}

	public void setLstCarteBanco(ArrayList<Carta> lstCarteBanco) {
		this.lstCarteBanco = lstCarteBanco;
	}
	
	public void lstCarteBancoAdd(Carta carta) {
		this.lstCarteBanco.add(carta);
	}	
	
	public int getNumeroCarteAGiocatore() {
		return numeroCarteAGiocatore;
	}

	public void setNumeroCarteAGiocatore(int numeroCarteAGiocatore) {
		this.numeroCarteAGiocatore = numeroCarteAGiocatore;
	}
	
	/**
	 * Il valore true indica che sono in fase di dichiarazione prese.
	 * Il valore false indica che sono in fase di gioco delle carte.
	 */
	public boolean isModalitaPrt() {
		return modalitaPrt;
	}

	public void setModalitaPrt(boolean modalitaPrt) {
		this.modalitaPrt = modalitaPrt;
	}

	public ArrayList<String> getElencoGiocatoriEliminati() {
		return elencoGiocatoriEliminati;
	}

	public void addGiocatoreEliminato(String nome) {
		this.elencoGiocatoriEliminati.add(nome);
	}

	public boolean isBtnInizioTurnoGiocatoreDisable() {
		return btnInizioTurnoGiocatoreDisable;
	}

	public void setBtnInizioTurnoGiocatoreDisable(boolean btnInizioTurnoGiocatoreDisable) {
		this.btnInizioTurnoGiocatoreDisable=btnInizioTurnoGiocatoreDisable;
	}

	public boolean isBtnFineTurnoGiocatoreDisable() {
		return btnFineTurnoGiocatoreDisable;
	}

	public void setBtnFineTurnoGiocatoreDisable(boolean btnFineTurnoGiocatoreDisable) {
		this.btnFineTurnoGiocatoreDisable = btnFineTurnoGiocatoreDisable;
	}

	public boolean isBtnIniziaNuovaManoVisible() {
		return btnIniziaNuovaManoVisible;
	}

	public void setBtnIniziaNuovaManoVisible(boolean btnIniziaNuovaManoVisible) {
		this.btnIniziaNuovaManoVisible = btnIniziaNuovaManoVisible;
	}

	public boolean isBtnIniziaNuovoRoundVisible() {
		return btnIniziaNuovoRoundVisible;
	}

	public void setBtnIniziaNuovoRoundVisible(boolean btnIniziaNuovoRoundVisible) {
		this.btnIniziaNuovoRoundVisible = btnIniziaNuovoRoundVisible;
	}

	public boolean isBtnInizioTurnoGiocatoreClicked() {
		return btnInizioTurnoGiocatoreClicked;
	}

	public void setBtnInizioTurnoGiocatoreClicked(boolean btnInizioTurnoGiocatoreClicked) {
		this.btnInizioTurnoGiocatoreClicked = btnInizioTurnoGiocatoreClicked;
	}
	
	public String getLblVitaPersaText() {
		return lblVitaPersaText;
	}

	public void setLblVitaPersaText(String lblVitaPersaText) {
		this.lblVitaPersaText = lblVitaPersaText;
	}
	
	/**
	 * imposta un flag del torneo, se a true la partita appartiene a un torneo se è a false no
	 * @param boolean flag
	 */
	void setFlagTorneo(boolean flag) {
		flagTorneo=flag;
	}
	
	Boolean getFlagTorneo() {
		return flagTorneo;
	}
}
