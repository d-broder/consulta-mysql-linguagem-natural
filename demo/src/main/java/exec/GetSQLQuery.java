package exec;

import lmi.LMInterface;

public class GetSQLQuery {
    public static void main(String[] args) {
        String question = "How many products are in the database?";
        System.out.println("Processing question...");
        String answer = LMInterface.getLMResponse(question);
        System.out.println(answer);
    }
}