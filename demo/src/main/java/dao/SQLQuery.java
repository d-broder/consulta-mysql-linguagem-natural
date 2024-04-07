package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import factory.ConnectionFactory;

public class SQLQuery {
    private Connection connection;

    public SQLQuery() {
        this.connection = new ConnectionFactory().getConnection();
    }

    public void executeQuery(String sql) {
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Itera sobre cada linha do resultado e imprime os valores de cada coluna
            while (rs.next()) {
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    System.out.print(rs.getString(i) + " ");
                }
                System.out.println(); // Pula para a próxima linha para o próximo registro
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
