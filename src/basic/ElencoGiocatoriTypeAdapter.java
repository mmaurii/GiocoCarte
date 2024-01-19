package basic;

import java.io.IOException;
import java.util.ArrayList;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ElencoGiocatoriTypeAdapter extends TypeAdapter<ArrayList<Giocatore>> {
	@Override
	public void write(JsonWriter out, ArrayList<Giocatore> elencoGiocatori) throws IOException {
		out.beginArray();
		for (Giocatore giocatore : elencoGiocatori) {
			out.beginObject();
			out.name("tipo").value(giocatore.getClass().getName()); // Salva il nome della classe (Bot o Giocatore)
			out.name("datiGiocatore");
			out.jsonValue(new Gson().toJson(giocatore)); // Serializzo il giocatore
			out.endObject();
		}
		out.endArray();
	}

	@Override
	public ArrayList<Giocatore> read(JsonReader in) {
		ArrayList<Giocatore> elencoGiocatori = new ArrayList<>();
		try {
			in.beginArray();

			while (in.hasNext()) {
				in.beginObject();
				String tipo = null;
				Giocatore giocatore = null;
				while (in.hasNext()) {
					String name = in.nextName();
					if ("tipo".equals(name)) {//controllo il tipo di classe (Bot o Giocatore)
						tipo = in.nextString();
					} else if ("datiGiocatore".equals(name)) {//controllo se sto per leggere la lista di giocatori
						if (tipo.equals(Giocatore.class.getName())) {//controllo se sto per leggere un Giocatore o un Bot
							giocatore = new Gson().fromJson(in, Giocatore.class);
						} else if (tipo.equals(Bot.class.getName())) {
							giocatore = new Gson().fromJson(in, Bot.class);
						}
					} else {
						in.skipValue();
					}
				}
				in.endObject();
				elencoGiocatori.add(giocatore);
			}

			in.endArray();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return elencoGiocatori;
	}
}