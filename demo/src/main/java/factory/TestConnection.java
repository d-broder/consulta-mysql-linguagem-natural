package factory;

import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) throws SQLException {
        Connection connection = new ConnectionFactory().getConnection("");
        System.out.println("Open connection");
        connection.close();
    }
}