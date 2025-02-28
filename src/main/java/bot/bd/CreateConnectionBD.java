package bot.bd;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class CreateConnectionBD {

    private static CreateConnectionBD instance;
    private Connection connection;

    public static CreateConnectionBD getInstance() {
        if (instance == null) {
            instance = new CreateConnectionBD();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }


    private CreateConnectionBD() {
        Properties properties = new Properties();
        try {
            FileInputStream input = new FileInputStream("config.properties");
            properties.load(input);

            // Получение параметров подключения
            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");

            // Подключение к базе данных
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Connected to the PostgreSQL server successfully.");
    }
}
