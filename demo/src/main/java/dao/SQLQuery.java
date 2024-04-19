package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringJoiner;

import factory.ConnectionFactory;

public class SQLQuery {
    private Connection connection;

    public SQLQuery(String database) {
        this.connection = new ConnectionFactory().getConnection(database);
    }

    public String executeQuery(String sql) {
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Cria um StringJoiner para construir a string de resultado
            StringJoiner result = new StringJoiner("\n");

            // Itera sobre cada linha do resultado e adiciona os valores de cada coluna à
            // string de resultado
            while (rs.next()) {
                StringJoiner row = new StringJoiner(" ");
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                result.add(row.toString());
            }

            rs.close();
            stmt.close();

            return result.toString(); // Retorna a string de resultado
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
