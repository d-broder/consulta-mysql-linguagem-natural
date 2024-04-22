package util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import factory.ConnectionFactory;

public class GetDatabasesNames {

    public List<String> getDatabasesNames() {
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
        GetDatabasesNames getDatabaseNames = new GetDatabasesNames();
        List<String> databaseNames = getDatabaseNames.getDatabasesNames();
        for (String dbName : databaseNames) {
            System.out.println(dbName);
        }
    }
}
