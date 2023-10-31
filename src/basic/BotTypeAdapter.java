package basic;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.Gson;

public class BotTypeAdapter extends TypeAdapter<Bot> {

    @Override
    public void write(JsonWriter out, Bot bot) throws IOException {
        Gson gson = new Gson();
		System.out.println("entra1");
        gson.toJson(bot, Bot.class, out);
    }

    @Override
    public Bot read(JsonReader in) throws IOException {
		System.out.println("entra2");
        Gson gson = new Gson();
		Bot b = gson.fromJson(in, Bot.class);
        return b;
    }
}
