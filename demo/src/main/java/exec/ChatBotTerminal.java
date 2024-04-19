package exec;

import dao.SQLQuery;
import lmi.ChatResponse;
import lmi.LanguageModelInterface;

public class ChatBotTerminal {
    public static void main(String[] args) {
        String database = "teste-api-2";
        SQLQuery sqlQuery = new SQLQuery(database);
        String question = "What are the names of all the products in the database?";
        String lmodel = LanguageModelInterface.getLModel();
        String lmInput = ChatResponse.getLmInput(question);

        System.out.println(lmInput);
        System.out.println("Processing question...");

        String lmResponse = ChatResponse.getLmResponseFromQuestion(question);

        System.out.println("- Language model: \"" + lmodel + "\"\n- SQL command: \"" + lmResponse + "\"");

        System.out.println(sqlQuery.executeQuery(lmResponse));
    }
}