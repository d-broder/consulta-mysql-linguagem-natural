package tests;

import dao.QueryExecutor;
import lmi.ChatResponse;

public class ChatBotTerminal {
    public static void main(String[] args) {
        String question = "Qual é o continente e a população do país United States?";
        String database = "world";
        double temperature = 0.0;

        while (temperature < 1) {
            long startTime = System.currentTimeMillis();
            System.out.println("\n** Temperature: " + temperature);

            ChatResponse chatResponse = new ChatResponse(question, database, temperature);
            QueryExecutor sqlQuery = new QueryExecutor(database);

            String lmResponsed = null;
            String result = null;

            lmResponsed = chatResponse.getLmResponseFromQuestion();
            result = sqlQuery.executeQuery(lmResponsed);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("* Time (seconds): " + (duration / 1000)
                    + "\n* SQL command: \"" + lmResponsed + "\"\n* Result: " + result);

            temperature += 0.01;
        }
    }
}