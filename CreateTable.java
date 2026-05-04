import java.sql.*;

public class CreateTable {
    public static void main(String[] args) {

        String url = "jdbc:sqlite:C:/Users/Fheyb Shylbhe Tagbo/.vscode/new codes/data/students.db";

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
            System.out.println("Table created successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}