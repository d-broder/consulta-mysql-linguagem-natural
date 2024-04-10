package exec;

import dao.SQLQuery;
import lmi.LanguageModelInterface;

public class GetSQLQueryAndResponse {
    public static void main(String[] args) {
        SQLQuery sqlQuery = new SQLQuery();
        // String question = "What are the names of all the products in the database?";
        String question = "What are the costumers?";

        String lmInput = LanguageModelInterface.getLmInput(question);
        System.out.println(lmInput);

        System.out.println("Processing question...");

        String lmResponse = LanguageModelInterface.getLMResponse(question, "openai");

        System.out.println("SQL command: \"" + lmResponse + "\"");

        sqlQuery.executeQuery(lmResponse);
    }
}
