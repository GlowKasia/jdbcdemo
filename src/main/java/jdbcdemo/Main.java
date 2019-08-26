package jdbcdemo;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// 1. Stworzenie schema: jdbc_students
// 2.
//create table `students`{
//`id` int not null auto_increment,
//`name` varchar (255) not null,
//`age` int not null,
//`average` double not null,
//`alive` tinyint not null,
//primary key (id)
//};
// 3.
public class Main {

    private static final String CREATE_TABLE_QUERY = "create table if not exists `students`(\n" +
            "`id` int not null auto_increment,\n" +
            "`name` varchar (255) not null,\n" +
            "`age` int not null,\n" +
            "`average` double not null,\n" +
            "`alive` tinyint not null,\n" +
            "primary key (id)\n" +
            ");";

    private static final String INSERT_QUERY =
            "insert into `students` (`name`, `age`, `average`, `alive`) values ( ? , ? , ? , ? );\n";

    private static final String DB_HOST = "localhost"; //127.0.0.1
    private static final String DB_PORT = "3306";
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
            System.out.println("HURRA!");

            Student student = new Student(null, "Kasia Glow", 25,5.0, true );

            try(PreparedStatement statement = connection.prepareStatement(CREATE_TABLE_QUERY)){
               statement.execute();
            }


            try(PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)){
                statement.setString(1, student.getName());
                statement.setInt(2, student.getAge());
                statement.setDouble(3, student.getAverage());
                statement.setBoolean(4, student.isAlive());

                boolean success = statement.execute();

                if(success){
                    System.out.println("SUKCES!");
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
