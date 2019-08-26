package jdbcdemo;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    //    1. Stworzenie database(schema): jdbc_students
//    2. Przygotowanie zapytań

    private static final String CREATE_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS `students`(\n" +
                    " `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
                    " `name` VARCHAR(255) NOT NULL,\n" +
                    " `age` INT NOT NULL,\n" +
                    " `average` DOUBLE NOT NULL,\n" +
                    " `alive` TINYINT NOT NULL\n" +
                    ")";

    private static final String INSERT_QUERY =
            "INSERT INTO `students` \n" +
                    "(`name`, `age`, `average`, `alive`) \n" +
                    "VALUES\n" +
                    "( ? , ? , ? , ? );";

    private static final String DELETE_QUERY = "DELETE FROM `students` WHERE `id` = ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM `students`;";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM `students` WHERE `id` = ?;";

    //    private static final String DB_HOST = "localhost"; // 127.0.0.1
    private static final String DB_HOST = "194.181.116.187"; // 127.0.0.1
    private static final String DB_PORT = "13306";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "root";
    private static final String DB_NAME = "jdbc_students";


    public static void main(String[] args) {
        MysqlDataSource dataSource = new MysqlDataSource();

        dataSource.setPort(Integer.parseInt(DB_PORT));
        dataSource.setUser(DB_USERNAME);
        dataSource.setServerName(DB_HOST);
        dataSource.setPassword(DB_PASSWORD);
        dataSource.setDatabaseName(DB_NAME);

        try {
            dataSource.setServerTimezone("Europe/Warsaw");
            dataSource.setUseSSL(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Connection connection = dataSource.getConnection();
            System.out.println("HURRRA!");

            try (PreparedStatement statement = connection.prepareStatement(CREATE_TABLE_QUERY)) {
                statement.execute();
            }

            Scanner scanner = new Scanner(System.in);

            String komenda;
            do {
                komenda = scanner.nextLine();

                if (komenda.equalsIgnoreCase("wstaw")) {
                    System.out.println("Imie:");
                    String imie = scanner.nextLine();
                    System.out.println("Wiek:");
                    int wiek = Integer.parseInt(scanner.nextLine());
                    System.out.println("Srednia:");
                    double srednia = Double.parseDouble(scanner.nextLine());
                    System.out.println("Czy zywy:");
                    boolean zywy = Boolean.parseBoolean(scanner.nextLine());

                    Student student = new Student(imie, wiek, srednia, zywy);
                    insertStudent(connection, student);
                } else if (komenda.equalsIgnoreCase("usun")) {
                    System.out.println("Podaj id:");
                    Long studentId = Long.parseLong(scanner.nextLine());

                    deleteStudent(connection, studentId);
                } else if (komenda.equalsIgnoreCase("list")) {
                    listAllStudents(connection);
                }
//                Komenda SELECT * // pobierz wszystkie
            } while (!komenda.equalsIgnoreCase("quit"));


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void getByIdStudent(Connection connection, Long searchedId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_QUERY)) {
            statement.setLong(1, searchedId);
//            statement.setLong(1, "%" + searchedId + "%");

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) { // jeśli jest rekord
                Student student = new Student();

                student.setId(resultSet.getLong(1));
                student.setName(resultSet.getString(2));
                student.setAge(resultSet.getInt(3));
                student.setAverage(resultSet.getDouble(4));
                student.setAlive(resultSet.getBoolean(5));

                System.out.println(student);
            } else {
                System.out.println("Nie udało się odnaleźć studenta");
            }
        }
    }

    private static void listAllStudents(Connection connection) throws SQLException {
        List<Student> studentList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Student student = new Student();

                student.setId(resultSet.getLong(1));
                student.setName(resultSet.getString(2));
                student.setAge(resultSet.getInt(3));
                student.setAverage(resultSet.getDouble(4));
                student.setAlive(resultSet.getBoolean(5));

                studentList.add(student);
            }
        }

        for (Student student : studentList) {
            System.out.println(student);
        }
    }

    private static void deleteStudent(Connection connection, Long studentId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setLong(1, studentId);

            boolean success = statement.execute();

            if (success) {
                System.out.println("SUKCES!");
            }
        }
    }

    private static void insertStudent(Connection connection, Student student) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            statement.setString(1, student.getName());
            statement.setInt(2, student.getAge());
            statement.setDouble(3, student.getAverage());
            statement.setBoolean(4, student.isAlive());

            boolean success = statement.execute();

            if (success) {
                System.out.println("SUKCES!");
            }
        }
    }
}
