
package com.java.contactmanagementsystemgui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseManager {
    
    private static final String URL = "jdbc:sqlite:contact_management.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            // SQLite driver 
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(URL);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC driver not found: " + e.getMessage());
        }
        return conn;
    }

    public static void createNewTable() {
        // SQL statement for creating a new table
        String sql = """
                     CREATE TABLE IF NOT EXISTS contacts (
                         id INTEGER PRIMARY KEY AUTOINCREMENT,
                         name TEXT NOT NULL,
                         phone_number TEXT,
                         email TEXT,
                         address TEXT
                     );""";

        String userSql = """
                         CREATE TABLE IF NOT EXISTS users (
                             id INTEGER PRIMARY KEY AUTOINCREMENT,
                             username TEXT NOT NULL UNIQUE,
                             password TEXT NOT NULL
                         );""" 
        ;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            System.out.println("Contacts table created or already exists.");

            stmt.execute(userSql);
            System.out.println("Users table created or already exists.");

            // Add a default user if not exists
            if (!userExists(conn, "admin")) {
                String insertUserSql = "INSERT INTO users(username, password) VALUES('admin', 'admin123')";
                stmt.execute(insertUserSql);
                System.out.println("Default user 'admin' added.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean userExists(Connection conn, String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE username = '" + username + "'";
        try (Statement stmt = conn.createStatement()) {
            return stmt.executeQuery(query).getInt(1) > 0;
        }
    }

    public static void main(String[] args) {
        // Test the connection and table creation
        createNewTable();
    }
}
 