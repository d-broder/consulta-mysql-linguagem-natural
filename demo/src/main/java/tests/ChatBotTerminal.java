package tests;

import dao.QueryExecutor;
import lmi.ChatResponse;
import lmi.LanguageModelInterface;

public class ChatBotTerminal {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        String question = "How many products?";
        String database = "teste-api-2";
        int lModelIndex = 3;

        String lModel = LanguageModelInterface.getGUIavailableModels().get(lModelIndex);

        ChatResponse chatResponse = new ChatResponse(question, database, lModelIndex);
        QueryExecutor sqlQuery = new QueryExecutor(database);

        System.out.println(chatResponse.getLmInput());
        System.out.println("Processing question...");

        String lmResponsed = null;
        boolean querySuccess = false;
        String result = null;
        int attempts = 0;

        while (!querySuccess) {
            try {
                lmResponsed = chatResponse.getLmResponseFromQuestion();
                result = sqlQuery.executeQuery(lmResponsed);
                querySuccess = true;
            } catch (Exception e) {
                attempts++;
                System.out.println("Error executing SQL query. Retrying... (" + attempts + ")");
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("* Language model: " + lModel + "\n* Total time elapsed (seconds): " + (duration / 1000)
                + "\n* SQL command: \"" + lmResponsed + "\"\n* Result:");
        System.out.println(result);
    }
}
