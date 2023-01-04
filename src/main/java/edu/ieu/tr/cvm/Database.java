package edu.ieu.tr.cvm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Database {
    private static Database instance;

    public static Database getInstance() throws ClassNotFoundException, SQLException {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private Connection connection;

    private Database() {
    }

    public void open() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:cvm.db");
        final Statement statement = connection.createStatement();
        final String string = "create table if not exists cv " +
                "(id integer primary key autoincrement," +
                "fullName text," +
                "birthYear integer," +
                "gpa real," +
                "email text," +
                "description text," +
                "homeAddress text," +
                "jobAddress text," +
                "phone text," +
                "website text); " +

                "create table if not exists skill " +
                "(id integer primary key autoincrement," +
                "cv_id integer," +
                "name text ," +
                "level integer," +
                "foreign key(cv_id) references cv(id) on update cascade on delete cascade);" +
            // "create unique index if not exists  skill_unique_name on skill (name, cv_id);" +

                "create table if not exists education " +
                "(id integer primary key autoincrement," +
                "cv_id integer," +
                "name text," +
                "year integer," +
                "foreign key(cv_id) references cv(id) on update cascade on delete cascade); " +

            //"create unique index if not exists  education_unique_name on education (name, cv_id);" +

                "create table if not exists publications " +
                "(id integer primary key autoincrement," +
                "cv_id integer," +
                "name text," +
                "year integer," +
                "foreign key(cv_id) references cv(id) on update cascade on delete cascade);" +

            // "create unique index if not exists publications_unique_name on publications (name, cv_id);" +

            

                "create table if not exists tag " +
                "(id integer primary key autoincrement," +
                "cv_id integer," +
                "name text," +
                "foreign key(cv_id) references cv(id) on update cascade on delete cascade);"
            // +"create unique index if not exists  tag_unique_name on tag (name, cv_id);"
            ;
        statement.executeUpdate(string);
        statement.close();
    }

    private List<Cv> toObject(final String query) throws SQLException {
        final Map<Integer, Cv> cvs = new HashMap<>();
        System.out.println(query);
        final PreparedStatement statement1 = connection.prepareStatement(query);
        final ResultSet set1 = statement1.executeQuery();
        while (set1.next()) {
            System.out.println(set1.getString(8));
            if (set1.getString(8) != null) {

                cvs.put(set1.getInt(1), new AcademicCv(set1.getInt(1),
                        set1.getString(2),
                        set1.getInt(3),
                        set1.getFloat(4),
                        set1.getString(5),
                        set1.getString(6),
                        set1.getString(7),
                        set1.getString(8),
                        set1.getString(9),
                        set1.getString(10),
                        new HashMap<>(),
                        new HashMap<>(),
                        new HashMap<>(),
                        new ArrayList<>()));
            } else {
                cvs.put(set1.getInt(1), new Cv(set1.getInt(1),
                        set1.getString(2),
                        set1.getInt(3),
                        set1.getFloat(4),
                        set1.getString(5),
                        set1.getString(6),
                        set1.getString(7),
                        set1.getString(9),
                        set1.getString(10),
                        new HashMap<>(),
                        new HashMap<>(),
                        new ArrayList<>()));
            }
        }
        statement1.close();

        final PreparedStatement statement2 = connection.prepareStatement("select * from skill");
        final ResultSet set2 = statement2.executeQuery();
        while (set2.next()) {

            if (cvs.get(set2.getInt(2)) != null) {
                cvs.get(set2.getInt(2)).getSkills().put(set2.getString(3), set2.getInt(4));
            }

        }
        statement2.close();

        final PreparedStatement statement3 = connection.prepareStatement("select * from publications");
        final ResultSet set3 = statement3.executeQuery();

        while (set3.next()) {
            if (cvs.get(set3.getInt(2)) != null) {
                if (cvs.get(set3.getInt(2)) instanceof final AcademicCv academicCv) {
                    academicCv.getPublications().put(set3.getString(3), set3.getInt(4));
                }
            }
        }
        statement3.close();

        final PreparedStatement statement5 = connection.prepareStatement("select * from education");
        final ResultSet set5 = statement5.executeQuery();

        while (set5.next()) {
            if (cvs.get(set5.getInt(2)) != null) {
                cvs.get(set5.getInt(2)).getEducation().put(set5.getString(3), set5.getInt(4));

            }
        }
        statement5.close();

        final PreparedStatement statement4 = connection.prepareStatement("select * from tag");
        final ResultSet set4 = statement4.executeQuery();

        while (set4.next()) {
            if (cvs.get(set4.getInt(2)) != null) {

                cvs.get(set4.getInt(2)).getTags().add(set4.getString(3));
            }
        }
        statement4.close();
        new ArrayList<>(cvs.values()).forEach(cv -> cv.print());
        return new ArrayList<>(cvs.values());
    }

    public List<Cv> getAll() throws SQLException {
        return toObject("select * from cv");
    }

    public List<Cv> filter(final String fieldName, final String fieldValue) throws SQLException {
        return toObject(String.format("select * from cv where %s like '%%%s%%'", fieldName, fieldValue));
    }

    public List<Cv> filter(final String fieldName, final double lowest, final double highest) throws SQLException {
        return toObject(
                String.format("select * from cv where %s <= %f and %s >= %f", fieldName, highest, fieldName, lowest));
    }

    /*
     * public List<Cv> filterAll(ArrayList<String> queries) throws SQLException {
     * StringBuilder query = new StringBuilder();
     * for (String s :queries) {
     * query.append(s);
     * }
     * return toObject("select * from cv where "+ query + " 1+1");
     * }
     */

    public List<Cv> filterAll(final ArrayList<String> queries) throws SQLException {
        final StringBuilder query = new StringBuilder();

        for (final String s : queries) {
            query.append(s);
        }

        return toObject("select * from cv where " + query + " 1=1");
    }

    public List<Cv> filterAllForCheckbox(final String tableName, final ArrayList<String> queries) throws SQLException {
        final StringBuilder query = new StringBuilder();
        for (final String s : queries) {
            query.append(s);
        }

        return toObject("select * from cv where id in (select cv_id from " + tableName + " where " + query + "1=1)");
    }

    public String educationToString(final Cv cv) {
        String s = "";

        for (final Map.Entry<String, Integer> entry : cv.getEducation().entrySet()) {
            s += "insert into education (cv_id,name,year) values (" + cv.getId() + ",'" + entry.getKey() + "',"
                    + entry.getValue() + ");";

        }
        return s;
    }

    public String tagToString(final Cv cv) {
        final StringBuilder sb = new StringBuilder();
        System.out.println(cv.getTags());
        cv.getTags().forEach(e -> {
            sb.append("insert into tag (cv_id,name) values (" + cv.getId() + ",'" + e + "');");
        });
        return sb.toString();
    }

    public String skillToString(final Cv cv) {
        String s = "";
        for (final Map.Entry<String, Integer> entry : cv.getSkills().entrySet())
            s += "insert into skill (cv_id,name, level) values (" + cv.getId() + ",'" + entry.getKey() + "',"
                    + entry.getValue() + ");";
        return s;
    }

    public String publicationsToString(final AcademicCv cv) {
        String s = "";
        for (final Map.Entry<String, Integer> entry : cv.getPublications().entrySet())
            s += "insert into publications (cv_id, name, year) values (" + cv.getId() + ",'" + entry.getKey() + "',"
                    + entry.getValue() + ");";
        

        return s;
    }

    public String educationToStringUpdate(final Cv cv) {
        String s = "delete from education where cv_id = " + cv.getId() + ";";
        for (final Map.Entry<String, Integer> entry : cv.getEducation().entrySet()) {
            s += "insert into education (cv_id, name, year) values ("+ cv.getId() + ",'"+ entry.getKey() + "', " + entry.getValue() + ");";

        }
        return s;
    }

    public String tagToStringUpdate(final Cv cv) {
        final StringBuilder sb = new StringBuilder();
        sb.append("delete from tag where cv_id = " + cv.getId() + ";");
        System.out.println(cv.getTags());
        cv.getTags().forEach(e -> {
            
            sb.append("insert into tag (cv_id, name) values ("+ cv.getId()+ ",'" + e +"');");
        });
        return sb.toString();
    }

    public String skillToStringUpdate(final Cv cv) {
        String s = "delete from skill where cv_id = " + cv.getId() + ";";
        for (final Map.Entry<String, Integer> entry : cv.getSkills().entrySet())
            s += "insert into skill (cv_id, name, level) values ("+ cv.getId()+ ",'" + entry.getKey() + "', " + entry.getValue() + ");";
        return s;
    }

    public String publicationsToStringUpdate(final AcademicCv cv) {
        String s = "delete from publications where cv_id = " + cv.getId() + ";";
        for (final Map.Entry<String, Integer> entry : cv.getPublications().entrySet())

            
        s += "insert into publications (cv_id, name, year) values ("+ cv.getId()+ ",'" + entry.getKey() + "', " + entry.getValue() + ");";

        return s;
    }

    public void update(final Cv cv) throws SQLException {
        final PreparedStatement statement1 = connection.prepareStatement("update cv set " +
                "fullName = ?," +
                "birthYear = ?," +
                "gpa=?," +
                "email=?, " +
                "description=?, " +
                "homeAddress=?, " +
                "jobAddress=?, " +
                "phone=?, " +
                "website=? where id = ?");
        statement1.setString(1, cv.getFullName());
        statement1.setInt(2, cv.getBirthYear());
        statement1.setDouble(3, cv.getGpa());
        statement1.setString(4, cv.getEmail());
        statement1.setString(5, cv.getDescription());
        statement1.setString(6, cv.getHomeAddress());
        if (cv instanceof final AcademicCv academicCv) {

            statement1.setString(7, academicCv.getJobAddress());
        } else {

        }
        statement1.setString(8, cv.getPhone());
        statement1.setString(9, cv.getWebsite());
        statement1.setInt(10, cv.getId());
        statement1.executeUpdate();

        statement1.close();
        final Statement statement2 = connection.createStatement();
        String publications = "";
        if (cv instanceof final AcademicCv academicCv) {
            publications = publicationsToStringUpdate(academicCv);
        } else {

        }
        String publications2 = "";
        if (cv instanceof final AcademicCv academicCv) {
            publications2 = publicationsToString(academicCv);
        } else {

        }
        System.out
                .println(skillToStringUpdate(cv) + educationToStringUpdate(cv) + publications + tagToStringUpdate(cv));
        statement2.executeUpdate(
                                 skillToStringUpdate(cv) + educationToStringUpdate(cv) + publications + tagToStringUpdate(cv));
        statement2.close();
    }

    public Cv insert(final Cv cv) throws SQLException {
        final PreparedStatement statement1 = connection.prepareStatement("insert into cv " +
                "(fullName, " +
                "birthYear, " +
                "gpa, " +
                "email, " +
                "description, " +
                "homeAddress, " +
                "jobAddress, " +
                "phone, " +
                "website) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "returning id");
        statement1.setString(1, cv.getFullName());
        statement1.setInt(2, cv.getBirthYear());
        statement1.setDouble(3, cv.getGpa());
        statement1.setString(4, cv.getEmail());
        statement1.setString(5, cv.getDescription());
        statement1.setString(6, cv.getHomeAddress());
        if (cv instanceof final AcademicCv academicCv) {

            statement1.setString(7, academicCv.getJobAddress());
        } else {

        }
        statement1.setString(8, cv.getPhone());
        statement1.setString(9, cv.getWebsite());
        final ResultSet set = statement1.executeQuery();
        cv.setId(set.getInt(1));
        statement1.close();
        final Statement statement2 = connection.createStatement();
        String publications = "";
        if (cv instanceof final AcademicCv academicCv) {
            publications = publicationsToString(academicCv);
        } else {

        }
        System.out.println(skillToString(cv) + educationToString(cv) + publications + tagToString(cv));
        statement2.executeUpdate(
                skillToString(cv) + educationToString(cv) + publications + tagToString(cv));
        statement2.close();
        return cv;
    }

    public ArrayList<String> getLocations() throws SQLException {
        final PreparedStatement statement = connection.prepareStatement("select homeAddress from cv");
        final ResultSet set = statement.executeQuery();
        final ArrayList<String> locations = new ArrayList<>();
        while (set.next()) {
            locations.add(set.getString(1));
        }
        statement.close();
        return locations;

    }

    public ArrayList<String> getSkills() throws SQLException {
        final PreparedStatement statement = connection.prepareStatement("select distinct name from skill");
        final ResultSet set = statement.executeQuery();
        final ArrayList<String> locations = new ArrayList<>();
        while (set.next()) {
            locations.add(set.getString(1));
        }
        statement.close();
        return locations;

    }

    public ArrayList<String> getSchool() throws SQLException {
        final PreparedStatement statement = connection.prepareStatement("select distinct name from education");
        final ResultSet set = statement.executeQuery();
        final ArrayList<String> locations = new ArrayList<>();
        while (set.next()) {
            locations.add(set.getString(1));
        }
        statement.close();
        return locations;

    }

    public ArrayList<String> getPublications() throws SQLException {
        final PreparedStatement statement = connection.prepareStatement("select distinct name from publications");
        final ResultSet set = statement.executeQuery();
        final ArrayList<String> locations = new ArrayList<>();
        while (set.next()) {
            locations.add(set.getString(1));
        }
        statement.close();
        return locations;

    }

    public ArrayList<String> getTags() throws SQLException {
        final PreparedStatement statement = connection.prepareStatement("select distinct name from tag");
        final ResultSet set = statement.executeQuery();
        final ArrayList<String> locations = new ArrayList<>();
        while (set.next()) {
            locations.add(set.getString(1));
        }
        statement.close();
        return locations;

    }

    public void delete(final Cv cv) throws SQLException {
        final PreparedStatement statement = connection.prepareStatement("delete from cv where id = ?");
        statement.setInt(1, cv.getId());
        statement.executeUpdate();
        statement.close();
    }

    public void close() throws SQLException {
        connection.close();
    }
}
