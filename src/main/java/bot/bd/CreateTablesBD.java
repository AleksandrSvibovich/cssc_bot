package bot.bd;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class CreateTablesBD {

    public void createTables() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Подключение к базе данных
            connection = CreateConnectionBD.getInstance().getConnection();
                        // Создание объекта Statement
            statement = connection.createStatement();

            //создание таблицы
            String create_users = "CREATE TABLE users (\n" +
                    "    id SERIAL PRIMARY KEY,                \n" +
                    "    tg_name VARCHAR(255) NOT NULL,        \n" +
                    "    real_name VARCHAR(255) NOT NULL,      \n" +
                    "    availability_status BOOLEAN DEFAULT FALSE  \n" +
                    ");";

            String create_envs = "CREATE TABLE environments (\n" +
                    "    id SERIAL PRIMARY KEY,                \n" +
                    "    env_level VARCHAR(50) NOT NULL,       \n" +
                    "    env_url VARCHAR(255) NOT NULL         \n" +
                    ");";

            String create_test_acc_table = "CREATE TABLE test_user_accs (\n" +
                    "    id SERIAL PRIMARY KEY,                \n" +
                    "    env_level VARCHAR(50) NOT NULL,       \n" +
                    "    login VARCHAR(255) NOT NULL,          \n" +
                    "    password VARCHAR(255) NOT NULL        \n" +
                    ");";


            try {
                // Пытаемся выполнить запрос к таблице
                statement.executeQuery("Select * from users");
            } catch (SQLException e) {
                // Если таблицы не существует, создаем её
                if (e.getSQLState().equals("42S02")) { // Код ошибки "таблица не существует"
                    statement.executeUpdate(create_users);
                } else {
                    // Обработка других ошибок
                    throw e;
                }
            }

            try {
                // Пытаемся выполнить запрос к таблице
                statement.executeQuery("Select * from environments");
            } catch (SQLException e) {
                // Если таблицы не существует, создаем её
                if (e.getSQLState().equals("42S02")) { // Код ошибки "таблица не существует"
                    statement.executeQuery(create_envs);
                } else {
                    // Обработка других ошибок
                    throw e;
                }
            }

            try {
                // Пытаемся выполнить запрос к таблице
                statement.executeQuery("Select * from test_user_accs");
            } catch (SQLException e) {
                // Если таблицы не существует, создаем её
                if (e.getSQLState().equals("42S02")) { // Код ошибки "таблица не существует"
                    statement.executeQuery(create_test_acc_table);
                } else {
                    // Обработка других ошибок
                    throw e;
                }
            }



        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
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
