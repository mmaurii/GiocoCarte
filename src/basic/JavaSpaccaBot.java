package basic;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class JavaSpaccaBot extends TelegramLongPollingBot {
	final String pathClassifica = "Documenti/Classifica.txt";
	private final static String USERNAME = "Spacca";
	private final static String TOKEN = "6312718703:AAEB92VkQqQbEMamxCX92e73IaR5OeoQfI8";
	final String idRegolamento="regolamento";
	final String idClassifica="classifica";

	public String getBotUsername() {
		return USERNAME;
	}

	@Override
	public String getBotToken() {
		return TOKEN;
	}

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
			Message message = update.getMessage();
			long chatId = message.getChatId();
			String text = message.getText();

			if (text.equals("/start")) {
				// Rispondi al comando /start con pulsanti inline
				inviaTastieraInline(chatId);
			} else {
				// Gestisci altri messaggi o comandi
				SendMessage defaultMessage = new SendMessage();
				defaultMessage.setChatId(chatId);
				defaultMessage.setText("Non ho capito il comando. Prova /start");
				try {
					execute(defaultMessage);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
		} else if (update.hasCallbackQuery()) {
			// Gestisci la callback query
			CallbackQuery callbackQuery = update.getCallbackQuery();
			String data = callbackQuery.getData();
			long chatId = callbackQuery.getMessage().getChatId();

			// Esegui l'azione corrispondente alla callback data
			if (data.equals(idClassifica)) {
				replyClassifica(chatId);
			} else if (data.equals(idRegolamento)) {
				replyRegolamento(chatId);
			}
		}
	}

	/**
	 * genera un menu a bottoni per il bot
	 * @param chatId
	 */
	private void inviaTastieraInline(long chatId) {
		SendMessage message = new SendMessage();
		message.setChatId(chatId);
		message.setText("Benvenuto! Scegli un'opzione:");

		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

		// Crea un pulsante con il callbackData "comando1"
		List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
		InlineKeyboardButton button1 = new InlineKeyboardButton();
		button1.setText("Classifica");
		button1.setCallbackData(idClassifica);
		rowInline1.add(button1);

		// Crea un pulsante con il callbackData "comando2"
		List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
		InlineKeyboardButton button2 = new InlineKeyboardButton();
		button2.setText("Regole");
		button2.setCallbackData(idRegolamento);
		rowInline2.add(button2);

		rowsInline.add(rowInline1);
		rowsInline.add(rowInline2);

		markupInline.setKeyboard(rowsInline);
		message.setReplyMarkup(markupInline);
		try {
			execute(message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	/**
	 * invia un file preso come parametro (fileName) e (filePath) all'utente associato al chatId
	 * @param chatId
	 * @param filePath
	 * @param fileName
	 */
	private void inviaFilePDF(long chatId, String filePath, String fileName) {
		SendDocument sendDocument = new SendDocument();
		sendDocument.setChatId(chatId);
		InputFile document = new InputFile(new File(filePath), fileName);
		sendDocument.setDocument(document);

		try {
			execute(sendDocument);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	/**
	 * risponde all'utente associato al chatId preso come parametro inviandogli la classifica
	 * @param chatId
	 */
	private void replyClassifica(long chatId) {
		final int num = 10;//numero di classificati visualizzati
		SendMessage responseMessage = new SendMessage();
		responseMessage.setChatId(chatId);
		String output = caricaClassifica(num);
		responseMessage.setText(output);
		try {
			execute(responseMessage);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	/**
	 * genera una stringa contente le prime num righe della classifica e la restituisce
	 * @param num
	 * @return String classifica
	 */
	private String caricaClassifica(int num) {
		String output = "Classifica:\n";

		//carico la classifica e mostro i primi 10 classificati
		ArrayList<LineClassifica> row = new ArrayList<>();
		try {
			File file = new File(pathClassifica);
			Scanner scan = new Scanner(file);	
			while(scan.hasNext()) {//carico i dati dal file di testo
				String line = scan.nextLine();
				String[] lineItems = line.split(" , ");
				//salvo i dati caricati 
				row.add(new LineClassifica(Integer.parseInt(lineItems[0]), lineItems[1]));
			}
			scan.close();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}

		//ordino in base al punteggio la lista
		row.sort(null);	

		//composizione stringa di output
		int counter=1;
		for(LineClassifica i : row) {
			if(counter<=num) {//ottengo il numero di righe richiesto
				//numero gli utenti in ordine di punteggio
				i.setRanking(counter);
				output+="\n"+counter+") "+i.getNome()+" , "+i.getPunti();
				counter++;
			}
		}
		return output;
	}

	/**
	 * risponde all'utente associato al chatId inviandogli il regolamento
	 * @param chatId
	 */
	private void replyRegolamento(long chatId) {
    	final String filePath = "Documentazione/Regolamento.pdf";
        final String fileName = "Regolamento.pdf";
        inviaFilePDF(chatId, filePath, fileName);
    }

}