package util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import factory.ConnectionFactory;

public class GetDatabaseSchema {
    public String getDatabaseSchema(String database) {
        Connection connection = null;
        StringBuilder schema = new StringBuilder();

        try {
            connection = new ConnectionFactory().getConnection(database);

            // Obtém o nome do banco de dados
            String databaseName = connection.getCatalog();
            schema.append("Database: ").append(databaseName).append("\n\n");

            // Obtém os metadados do banco de dados
            DatabaseMetaData metaData = connection.getMetaData();

            // Obtém as tabelas do banco de dados
            ResultSet tables = metaData.getTables(databaseName, null, "%", new String[] { "TABLE" });
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                schema.append("Table: ").append(tableName).append("\n");

                // Obtém as colunas de cada tabela
                ResultSet columns = metaData.getColumns(databaseName, null, tableName, "%");
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String columnType = columns.getString("TYPE_NAME");
                    schema.append("  Column: ").append(columnName).append(", Type: ").append(columnType);

                    // Verifica se a coluna é uma chave primária
                    if (isPrimaryKey(metaData, databaseName, tableName, columnName)) {
                        schema.append(" (Primary Key)");
                    }

                    schema.append("\n");
                }
                columns.close();

                // Adiciona uma quebra de linha entre as tabelas
                schema.append("\n");
            }
            tables.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fecha a conexão com o banco de dados
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return schema.toString();
    }

    private boolean isPrimaryKey(DatabaseMetaData metaData, String databaseName, String tableName, String columnName)
            throws SQLException {
        ResultSet primaryKeys = metaData.getPrimaryKeys(databaseName, null, tableName);
        while (primaryKeys.next()) {
            String primaryKeyColumnName = primaryKeys.getString("COLUMN_NAME");
            if (primaryKeyColumnName.equals(columnName)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        GetDatabaseSchema extractor = new GetDatabaseSchema();
        String schema = extractor.getDatabaseSchema("teste-api-2");
        System.out.println(schema);
    }
}
