package exec;

import dao.SQLQuery;

public class GetSQLResponse {
    public static void main(String[] args) {
        SQLQuery sqlQuery = new SQLQuery();
        executeQuery(sqlQuery, "SELECT COUNT(*) AS total FROM customers");
        System.out.println();
        executeQuery(sqlQuery, "SELECT * FROM customers");
    }

    private static void executeQuery(SQLQuery sqlQuery, String sql) {
        System.out.println("Query: \"" + sql + "\"");
        System.out.println("Results:");
        sqlQuery.executeQuery(sql);
    }
}
