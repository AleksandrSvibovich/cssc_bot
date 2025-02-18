package bd;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class PostgreSQLConnection {


    public static void main(String[] args) {
        Properties properties = new Properties();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

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

            // Создание объекта Statement
            statement = connection.createStatement();

            // Выполнение запроса
            String sql = "SELECT * FROM USERS"; // Пример запроса
            resultSet = statement.executeQuery(sql);

            // Обработка результатов
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                String email = resultSet.getString("EMAIL");
                String phone = resultSet.getString("PHONE");
                System.out.println("ID: " + id + ", Name: " + name + ", Email: " + email + ", Phone: " + phone);
            }

        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // Закрытие ресурсов
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}