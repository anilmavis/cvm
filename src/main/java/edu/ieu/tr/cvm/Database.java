package edu.ieu.tr.cvm;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private static Database instance;
    private Connection connection;

    private Database() {
    }

    public void open() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:cvm.db");
        Statement statement = connection.createStatement();
        String string =
                //CV Table
                "create table if not exists cv " +
                        "(id integer primary key autoincrement," +
                        "fullName varchar(255) not null," +
                        "age integer not null," +
                        "birthYear integer," +
                        "email varchar(255)," +
                        "description varchar(255)," +
                        "homeAddress varchar(255)," +
                        "jobAddress varchar(255)," +
                        "telephoneNumber varchar(255)," +
                        "websiteLink varchar(255)" +
                        ") ; "
                        +
                        //Skill Table
                        "create table if not exists skill " +
                        "(id integer primary key autoincrement," +
                        "cv_id integer," +
                        "level integer," +
                        "foreign key(cv_id) references cv(id)" +
                        ") ; " +
                        // Education Table
                        "create table if not exists education " +
                        "(id integer primary key autoincrement," +
                        "cv_id integer," +
                        "name varchar(255)," +
                        "foreign key(cv_id) references cv(id)" +
                        ") ; " +
                        //Additional Skill
                        "create table if not exists additional_skill " +
                        "(id integer primary key autoincrement," +
                        "cv_id integer," +
                        "level integer," +
                        "foreign key(cv_id) references cv(id)" +
                        ") ; " +
                        //Tag Table
                        "create table if not exists tag " +
                        "(id integer primary key autoincrement," +
                        "cv_id integer," +
                        "name varchar(255)," +
                        "foreign key(cv_id) references cv(id)" +
                        ") ; ";
        statement.executeUpdate(string);
        statement.close();
    }

    public static Database getInstance() throws ClassNotFoundException, SQLException {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() throws SQLException {
        connection.close();
    }

    public String educationToString(Cv cv) {
        String s = "";
        System.out.println("asddas");
        for (Map.Entry<String, Integer> entry : cv.getEducation().entrySet()) {
            s += "insert into education (cv_id,name) values (" + cv.getId() + ",'" + entry.getKey() + "');";
            System.out.println("asddas");
        }
        System.out.println(s);


        return s;
    }

    public String tagToString(Cv cv) {
        StringBuilder sb = new StringBuilder();
        cv.getTags().forEach(e -> {
            sb.append("insert into tag (cv_id,name) values (" + cv.getId() + "," + e + ");");
        });

        return sb.toString();
    }

    public String skillToString(Cv cv) {
        String s = "";
        for (Map.Entry<String, Integer> entry : cv.getSkills().entrySet())
            s += "insert into skill (cv_id,level) values (" + cv.getId() + "," + entry.getKey() + ");";

        return s;
    }

    public String additionalskillToString(Cv cv) {
        String s = "";
        for (Map.Entry<String, Integer> entry : cv.getAdditionalSkills().entrySet())
            s += "insert into additional_skill (cv_id,level) values (" + cv.getId() + "," + entry.getKey() + ");";

        return s;
    }


    public Cv insert(Cv cv) throws SQLException {

        String query = "insert into cv (" +
                "fullName," +
                "age," +
                "birthYear," +
                "email," +
                "description," +
                "homeAddress," +
                "jobAddress," +
                "telephoneNumber," +
                "websiteLink)" +
                " values (?, ?,?,?,?,?,?,?,?) returning id; ";

        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, cv.getFullName());
        preparedStatement.setInt(2, cv.getAge());
        preparedStatement.setInt(3, cv.getBirthYear());
        preparedStatement.setString(4, cv.getEmail());
        preparedStatement.setString(5, cv.getDescription());
        preparedStatement.setString(6, cv.getHomeAddress());
        preparedStatement.setString(7, cv.getJobAdress());
        preparedStatement.setString(8, cv.getTelephoneNumber());
        preparedStatement.setString(9, cv.getWebsiteLink());

        ResultSet resultSet = preparedStatement.executeQuery();


        Statement statement2 = connection.createStatement();

        statement2.executeUpdate(

                skillToString(cv)
                        + educationToString(cv) +
                        additionalskillToString(cv) +
                        tagToString(cv)

        );

        return cv;
    }

    public void delete(Cv cv) throws SQLException {


    }

}
