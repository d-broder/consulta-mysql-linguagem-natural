package tests;

import dao.QueryExecutor;

public class GetSQLResults {
    public static void main(String[] args) {
        String database = "teste-api-2";
        QueryExecutor queryExecutor = new QueryExecutor(database);
        executeQuery(queryExecutor, "SELECT COUNT(*) AS total FROM customers");
        System.out.println();
        executeQuery(queryExecutor, "SELECT * FROM customers");
    }

    private static void executeQuery(QueryExecutor queryExecutor, String query) {
        System.out.println("Query: \"" + query + "\"");
        System.out.println("Results:");
        System.out.println(queryExecutor.executeQuery(query));
    }
}
