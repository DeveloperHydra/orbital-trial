package wiki.hydra.orbital.database;

import wiki.hydra.orbital.Main;

import java.sql.*;

public class DatabaseManager {
    private Connection connection;
    private Main plugin;

    public DatabaseManager(Main plugin) {
        this.plugin = plugin;
    }

    public void connectDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + "/balances.db");

            try (Statement stmt = connection.createStatement()) {
                String createTableSQL = "CREATE TABLE IF NOT EXISTS balances (username TEXT PRIMARY KEY, balance DOUBLE)";
                stmt.executeUpdate(createTableSQL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeDatabase() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getBalance(String username) {
        double balance = 0;
        try (PreparedStatement stmt = connection.prepareStatement("SELECT balance FROM balances WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    public void setBalance(String username, double amount) {
        try (PreparedStatement stmt = connection.prepareStatement("INSERT OR REPLACE INTO balances (username, balance) VALUES (?, ?)")) {
            stmt.setString(1, username);
            stmt.setDouble(2, amount);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBalance(String username, double amount) {
        try {
            double currentBalance = getBalance(username);
            double newBalance = currentBalance + amount;

            try (PreparedStatement stmt = connection.prepareStatement("INSERT OR REPLACE INTO balances (username, balance) VALUES (?, ?)")) {
                stmt.setString(1, username);
                stmt.setDouble(2, newBalance);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
