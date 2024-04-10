package exec;

import lmi.LanguageModelInterface;

public class GetSQLQuery {
    public static void main(String[] args) {
        String question = "How many products are in the database?";

        String lminput = LanguageModelInterface.getLmInput(question);
        System.out.println(lminput);

        System.out.println("Processing question...");
        String answer = LanguageModelInterface.getLMResponse(question, "sqlcoder");
        System.out.println(answer);
    }
}