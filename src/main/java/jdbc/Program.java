package jdbc;

import models.Course;
import models.CourseFabrica;
import view.View;

import java.sql.*;
import java.util.*;

public class Program {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String url = "jdbc:mysql://localhost:3306/";
    private static final String user = "root";
    private static final String password = "12345678";


    public static void main(String[] args) throws SQLException {

        Connection connection = DriverManager.getConnection(url, user, password);
        while (true) {
            useDatabase(connection);
            System.out.println("Use database successfully");
            View.menu();
            if (scanner.hasNextInt()) {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 0 -> {
                        System.out.println("Завершение работы приложения");
                        connection.close();
                        return;
                    }
                    case 1 -> {
                        createDatabase(connection);
                        System.out.println("Database created successfully");
                        useDatabase(connection);
                        System.out.println("Use database successfully");
                    }
                    case 2 -> {
                        createTable(connection);
                        System.out.println("Create table successfully");
                    }
                    case 3 -> {
                        Map<Integer, Course> collection = readData(connection);
                        collection.forEach((k, v) -> System.out.println("key: "+k+" value:"+v));
                    }
                    case 4 -> {
                        insertData(connection, CourseFabrica.create());
                        System.out.println("Insert data successfully");
                    }
                    case 5 -> {
                        HashMap<Integer, Course> collection = (HashMap<Integer, Course>) readData(connection);
                        updateData(connection, CourseFabrica.update(collection));
                    }
                    case 6 -> {
                        System.out.print("Введите Id для удаления: ");
                        try {
                            int id = Integer.parseInt(scanner.nextLine());
                            deleteData(connection, id);
                        } catch (InputMismatchException ex) {
                            ex.printStackTrace();
                        }

                    }
                    default -> System.out.println("Некорректный пункт меню.\nПовторите попытку.");
                }

            } else {
                System.out.println("Введены некорректные данные");
            }
        }

    }

    private static void createDatabase(Connection connection) throws SQLException {
        String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS coursesDB";
        try (PreparedStatement statement = connection.prepareStatement(createDatabaseSQL)) {
            statement.execute();
        }
    }
    private static void useDatabase (Connection connection) throws SQLException {
        String useDatabaseSQL = "USE coursesDB";
        try (PreparedStatement statement = connection.prepareStatement(useDatabaseSQL)) {
            statement.execute();
        }
    }
    private static void createTable (Connection connection) throws SQLException {
        String createTable = "CREATE TABLE IF NOT EXISTS courses (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), age INT);";
        try (PreparedStatement statement = connection.prepareStatement(createTable)) {
            statement.execute();
        }
    }
    private static void insertData (Connection connection, Course course) throws SQLException {
        String insertDataSQL = "INSERT INTO courses (title, duration) VALUES (?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(insertDataSQL)) {
            statement.setString(1, course.getTitle());
            statement.setInt(2, course.getDuration());
            statement.executeUpdate();
        }
    }
    private static Map<Integer, Course> readData (Connection connection) throws SQLException {
        HashMap<Integer, Course> coursesList = new HashMap<>();
        String readDataSQL = "SELECT * FROM courses;";
        try (PreparedStatement statement = connection.prepareStatement(readDataSQL)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                int duration = resultSet.getInt("duration");
                coursesList.put(id, new Course(id, title, duration));
            }
            return coursesList;
        }
    }

    /**
     * Обновление данных в таблице courses по идентификатору
     * @param connection Соединение с БД
     * @param course курс
     * @throws SQLException Исключение при выполнении запроса
     */
    private static void updateData(Connection connection, Course course) throws SQLException {
        String updateDataSQL = "UPDATE courses SET title=?, duration=? WHERE id=?;";
        try (PreparedStatement statement = connection.prepareStatement(updateDataSQL)) {
            statement.setString(1, course.getTitle());
            statement.setInt(2, course.getDuration());
            statement.setInt(3, course.getId());
            statement.executeUpdate();
        }
    }
    /**
     * Удаление записи из таблицы students по идентификатору
     * @param connection Соединение с БД
     * @param id Идентификатор записи
     * @throws SQLException Исключение при выполнении запроса
     */
    private static void deleteData(Connection connection, int id) throws SQLException {
        String deleteDataSQL = "DELETE FROM courses WHERE id=?;";
        try (PreparedStatement statement = connection.prepareStatement(deleteDataSQL)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }
}
