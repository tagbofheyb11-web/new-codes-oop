import java.sql.*;
import java.util.*;

public class StudentDbRepository {

    private final String url = "jdbc:sqlite:data/students.db";

    public StudentDbRepository() {
        createTable();
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS students ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT,"
                + "course TEXT,"
                + "email TEXT,"
                + "dateCreated TEXT,"
                + "dateUpdated TEXT"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(Student s) {
    String sql = "INSERT INTO students(name, course, email, dateCreated, dateUpdated) VALUES(?,?,?,?,?)";

    try (Connection conn = DriverManager.getConnection(url);
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, s.getName());
        ps.setString(2, s.getCourse());
        ps.setString(3, s.getEmail());
        ps.setString(4, s.getDateCreated());
        ps.setString(5, s.getDateUpdated());

        ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Student> load() {
        List<Student> list = new ArrayList<>();

        String sql = "SELECT * FROM students";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Student s = new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("course"),
                        rs.getString("email")
                );
                s.setDateCreated(rs.getString("dateCreated"));
                s.setDateUpdated(rs.getString("dateUpdated"));
                list.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}