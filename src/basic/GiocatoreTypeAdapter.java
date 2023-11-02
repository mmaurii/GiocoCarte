package basic;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.Gson;

public class GiocatoreTypeAdapter extends TypeAdapter<Giocatore> {

	@Override
	public void write(JsonWriter out, Giocatore giocatore) throws IOException {
		Gson gson = new Gson();
	//	System.out.println("entraa1");
//		if(giocatore instanceof Bot) {
//			System.out.println("entraaa1");
//			gson.toJson(giocatore, Bot.class, out);	
//		}else {
			gson.toJson(giocatore, Giocatore.class, out);	
//		}
	}

	@Override
	public Giocatore read(JsonReader in) throws IOException {
	//	System.out.println("entraa2");
		Gson gson = new Gson();
		Giocatore g = gson.fromJson(in, Giocatore.class);
//		if(g instanceof Bot) {
//			System.out.println("entraaa2");
//			return (Bot)g;
//		}
		return g;
	}
}