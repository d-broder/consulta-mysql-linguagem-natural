package exec;

import dao.SQLQuery;
import lmi.LMInterface;

public class GetSQLQueryAndResponse {
    public static void main(String[] args) {
        SQLQuery consultaSQL = new SQLQuery();
        String question = "What are the names of all the products in the database?";

        System.out.println("Processing question...");

        String lmResponse = LMInterface.getLMResponse(question);

        System.out.println("lmResponse: \"" + lmResponse + "\"");

        consultaSQL.executeQuery(lmResponse);
    }
}
