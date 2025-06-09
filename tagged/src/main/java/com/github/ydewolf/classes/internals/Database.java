package com.github.ydewolf.classes.internals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    protected Connection conn;

    public Database() {
        try {
            this.conn = DriverManager.getConnection("jdbc:sqlite:base.taggeddb");
            this.create_tables();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

    }

    private void create_tables() throws SQLException {
        Statement statement = this.conn.createStatement();
        statement.setQueryTimeout(30);

        statement.executeUpdate("CREATE TABLE CachedFiles (path VARCHAR(255) NOT NULL PRIMARY KEY)");
        statement.executeUpdate("CREATE TABLE FileTags (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(63) NOT NULL, file_path VARCHAR(255) NOT NULL, FOREIGN KEY (file_path) REFERENCES CachedFiles(path))");
    }
}
