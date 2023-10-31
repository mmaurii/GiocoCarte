package basic;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.Gson;

public class ElencoGiocatoriTypeAdapter extends TypeAdapter<ArrayList<Giocatore>> {
	@Override
	public void write(JsonWriter out, ArrayList<Giocatore> elencoGiocatori) throws IOException {
		Gson gson = new Gson();

		gson.toJson(elencoGiocatori, Giocatore.class, out);	

	}
	
	@Override
	public ArrayList<Giocatore> read(JsonReader in) throws IOException {
		System.out.println("entraa2");
		Gson gson = new Gson();
		ArrayList<Giocatore> elencoGiocatori = gson.fromJson(in, ArrayList.class);
		return elencoGiocatori;
	}
}