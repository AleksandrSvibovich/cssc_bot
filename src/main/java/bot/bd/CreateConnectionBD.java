package bot.bd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class CreateConnectionBD {
    private static final Logger logger = LoggerFactory.getLogger(CreateConnectionBD.class);
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
            password = null;
            logger.info("Connected to the PostgreSQL server successfully.");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.info("Connected to the PostgreSQL server successfully.");

    }
}
