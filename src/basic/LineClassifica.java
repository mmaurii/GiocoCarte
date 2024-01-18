package basic;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LineClassifica implements Comparable<LineClassifica>{
	IntegerProperty ranking = new SimpleIntegerProperty();
	IntegerProperty punti = new SimpleIntegerProperty();
	StringProperty nome = new SimpleStringProperty();
	
	public LineClassifica(int ranking, int punti, String nome){
		setRanking(ranking);
		setPunti(punti);
		setNome(nome);
	}
	
    public LineClassifica(int punti, String nome) {
		setPunti(punti);
		setNome(nome);
	}

	public IntegerProperty rankingProperty() {
        return ranking;
    }

	public final int getRanking() {
		return rankingProperty().get();
	}

	public void setRanking(int ranking) {
		rankingProperty().set(ranking);;
	}

    public IntegerProperty puntiProperty() {
        return punti;
    }

	public final int getPunti() {
		return puntiProperty().get();
	}

	public void setPunti(int punti) {
		puntiProperty().set(punti);;
	}

    public StringProperty nomeProperty() {
        return nome;
    }

	public final String getNome() {
		return nomeProperty().get();
	}

	public void setNome(String nome) {
		nomeProperty().set(nome);;
	}

	@Override
	public int compareTo(LineClassifica ln) {//ordina in ordine decrescente in base ai punti
        return Integer.compare(ln.getPunti(), getPunti());
	}
}