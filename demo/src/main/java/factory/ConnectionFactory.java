package factory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    public Connection getConnection(String database) {
        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream("demo/res/config.properties")) {
            prop.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading config.properties file", e);
        }

        String url = prop.getProperty("url") + database;
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");

        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }
}
