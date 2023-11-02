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
    	System.out.println("eii");
		Gson gson = new Gson();
        //out.name("elencoGiocatori");
    //    out.beginArray();
        for (Giocatore giocatore : elencoGiocatori) {
            out.beginObject();
            if(giocatore instanceof Bot) {
            	System.out.println("bot");
            }else {
            	System.out.println(giocatore.getClass().getName());
            }
            out.name("tipo").value(giocatore.getClass().getName()); // Salva il nome della classe
            out.name("datiGiocatore");
            out.jsonValue(new Gson().toJson(giocatore)); // Serializza il giocatore
            out.endObject();
        }
  //      out.endArray();
		gson.toJson(elencoGiocatori, Giocatore.class, out);	

	}
	
	@Override
	public ArrayList<Giocatore> read(JsonReader in) {
	    ArrayList<Giocatore> elencoGiocatori = new ArrayList<>();
	    try {
//		    in.beginArray();

	    	while (in.hasNext()) {
			    in.beginObject();
			    String tipo = null;
			    Giocatore giocatore = null;
			    while (in.hasNext()) {
			        String name = in.nextName();
			        if ("tipo".equals(name)) {
			            tipo = in.nextString();
			        } else if ("datiGiocatore".equals(name)) {
			            if (tipo.equals(Giocatore.class.getName())) {
			                giocatore = new Gson().fromJson(in.nextString(), Giocatore.class);
			            } else if (tipo.equals(Bot.class.getName())) {
			                giocatore = new Gson().fromJson(in.nextString(), Bot.class);
			            }
			        } else {
			            in.skipValue();
			        }
			    }
			    in.endObject();
			    elencoGiocatori.add(giocatore);
			}
			
		//	in.endArray();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return elencoGiocatori;
	}
}