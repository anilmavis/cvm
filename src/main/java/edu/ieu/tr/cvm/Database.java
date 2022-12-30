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
        final Statement statement = connection.createStatement();
        final String string = "create table if not exists cv " +
                "(id integer primary key autoincrement," +
                "fullName varchar(255)," +
                "age integer," +
                "birthYear integer," +
                "email varchar(255)," +
                "description varchar(255)," +
                "homeAddress varchar(255)," +
                "jobAddress varchar(255)," +
                "telephoneNumber varchar(255)," +
                "websiteLink varchar(255)); " +

                "create table if not exists skill " +
                "(id integer primary key autoincrement," +
                "cv_id integer," +
                "name varchat(255)," +
                "level integer," +
                "foreign key(cv_id) references cv(id)); " +

                "create table if not exists education " +
                "(id integer primary key autoincrement," +
                "cv_id integer," +
                "name varchar(255)," +
                "foreign key(cv_id) references cv(id)); " +

                "create table if not exists additional_skill " +
                "(id integer primary key autoincrement," +
                "cv_id integer," +
                "name varchat(255)," +
                "level integer," +
                "foreign key(cv_id) references cv(id));" +

                "create table if not exists tag " +
                "(id integer primary key autoincrement," +
                "cv_id integer," +
                "name varchar(255)," +
                "foreign key(cv_id) references cv(id))";
        statement.executeUpdate(string);
        statement.close();
    }

    public static Database getInstance() throws ClassNotFoundException, SQLException {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public List<Cv> getAll() throws SQLException {
        final PreparedStatement statement1 = connection.prepareStatement("select * from cv");
        final ResultSet set1 = statement1.executeQuery();
        final List<Cv> cvs = new ArrayList<>();

        while (set1.next()) {
            cvs.add(Cv.Builder.getInstance()
                    .id(set1.getInt(1))
                    .fullName(set1.getString(2))
                    .birthYear(set1.getInt(3))
                    .email(set1.getString(4))
                    .description(set1.getString(5))
                    .homeAddress(set1.getString(6))
                    .jobAdress(set1.getString(7))
                    .telephoneNumber(set1.getString(8))
                    .build());
        }
        statement1.close();

        final PreparedStatement statement2 = connection.prepareStatement("select * from skill");
        final ResultSet set2 = statement2.executeQuery();

        for (int i = 0; set2.next(); i++) {
            final Map<String, Integer> skills = new HashMap<>();
            skills.put(set2.getString(1), set2.getInt(2));
            cvs.set(i, Cv.Builder.getInstance(cvs.get(i)).skills(skills).build());
        }
        return cvs;
    }

    public void close() throws SQLException {
        connection.close();
    }

    public String educationToString(final Cv cv) {
        String s = "";

        for (final Map.Entry<String, Integer> entry : cv.getEducation().entrySet()) {
            s += "insert into education (cv_id,name) values (" + cv.getId() + ",'" + entry.getKey() + "');";

        }
        System.out.println(s);

        return s;
    }

    public String tagToString(final Cv cv) {
        final StringBuilder sb = new StringBuilder();
        cv.getTags().forEach(e -> {
            sb.append("insert into tag (cv_id,name) values (" + cv.getId() + "," + e + ");");
        });

        return sb.toString();
    }

    public String skillToString(final Cv cv) {
        String s = "";
        for (final Map.Entry<String, Integer> entry : cv.getSkills().entrySet())
            s += "insert into skill (cv_id,level) values (" + cv.getId() + "," + entry.getKey() + ");";

        return s;
    }

    public String additionalskillToString(final Cv cv) {
        String s = "";
        for (final Map.Entry<String, Integer> entry : cv.getAdditionalSkills().entrySet())
            s += "insert into additional_skill (cv_id,level) values (" + cv.getId() + "," + entry.getKey() + ");";

        return s;
    }

    public Cv insert(final Cv cv) throws SQLException {
        final PreparedStatement statement1 = connection.prepareStatement("insert into cv " +
                "(fullName, " +
                "birthYear, " +
                "email, " +
                "description, " +
                "homeAddress, " +
                "jobAddress, " +
                "telephoneNumber, " +
                "websiteLink) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?) " +
                "returning id");
        statement1.setString(1, cv.getFullName());
        statement1.setInt(2, cv.getBirthYear());
        statement1.setString(3, cv.getEmail());
        statement1.setString(4, cv.getDescription());
        statement1.setString(5, cv.getHomeAddress());
        statement1.setString(6, cv.getJobAdress());
        statement1.setString(7, cv.getTelephoneNumber());
        statement1.setString(8, cv.getWebsiteLink());
        final ResultSet set = statement1.executeQuery();
        final Cv builtCv = Cv.Builder.getInstance(cv)
                .id(set.getInt(1))
                .build();
        statement1.close();
        final Statement statement2 = connection.createStatement();
        statement2.executeUpdate(
                skillToString(cv) + educationToString(cv) + additionalskillToString(cv) + tagToString(cv));
        statement2.close();
        return builtCv;
    }

    public void delete(final Cv cv) throws SQLException {
        final PreparedStatement statement1 = connection.prepareStatement("delete from cv where id = ?");
        final PreparedStatement statement2 = connection.prepareStatement("delete from skill where cv_id = ?");
        final PreparedStatement statement3 = connection
                .prepareStatement("delete from education where cv_id = ?");
        final PreparedStatement statement4 = connection
                .prepareStatement("delete from additional_skill where cv_id = ?");
        final PreparedStatement statement5 = connection.prepareStatement("delete from tag where cv_id = ?");

        statement1.setInt(1, cv.getId());
        statement1.executeUpdate();
        statement1.close();

        statement2.setInt(1, cv.getId());
        statement2.executeUpdate();
        statement2.close();

        statement3.setInt(1, cv.getId());
        statement3.executeUpdate();
        statement3.close();

        statement4.setInt(1, cv.getId());
        statement4.executeUpdate();
        statement4.close();

        statement5.setInt(1, cv.getId());
        statement5.executeUpdate();
        statement5.close();
    }
}
