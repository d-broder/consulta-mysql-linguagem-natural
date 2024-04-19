package util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import factory.ConnectionFactory;

public class GetDatabaseNames {

    public List<String> getAllDatabaseNames() {
        List<String> databaseNames = new ArrayList<>();
        Connection connection = null;

        try {
            ConnectionFactory factory = new ConnectionFactory();
            connection = factory.getConnection("");

            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getCatalogs();

            while (resultSet.next()) {
                String dbName = resultSet.getString("TABLE_CAT");
                // Excluindo os bancos de dados extras
                if (!dbName.equals("information_schema") && !dbName.equals("mysql")
                        && !dbName.equals("performance_schema")) {
                    databaseNames.add(dbName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return databaseNames;
    }

    public static void main(String[] args) {
        GetDatabaseNames getDatabaseNames = new GetDatabaseNames();
        List<String> databaseNames = getDatabaseNames.getAllDatabaseNames();
        for (String dbName : databaseNames) {
            System.out.println(dbName);
        }
    }
}
