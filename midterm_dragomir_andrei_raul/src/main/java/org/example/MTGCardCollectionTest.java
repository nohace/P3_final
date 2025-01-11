package org.example;

import java.sql.*;
import java.util.Scanner;

public class MTGCardCollectionTest {

    private static final String DB_URL = "jdbc:sqlite:C:/Users/andrei/IdeaProjects/P3_final/mtg_card_manager.db";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                initializeDatabase(conn);
            }

            Scanner scanner = new Scanner(System.in);

            System.out.println("Welcome to the MTG Card Manager!");
            while (true) {
                System.out.println("Are you an admin or a user? (admin/user/create_account/exit):");
                String role = scanner.nextLine().trim().toLowerCase();

                switch (role) {
                    case "admin":
                        adminLogin(scanner, conn);
                        break;
                    case "user":
                        userLogin(scanner, conn);
                        break;
                    case "create_account":
                        createAccount(scanner, conn);
                        break;
                    case "exit":
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter 'admin', 'user', 'create_account', or 'exit'.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private static void initializeDatabase(Connection conn) throws SQLException {
        String createUsersTableSQL = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                role TEXT NOT NULL CHECK (role IN ('admin', 'user'))
            );
        """;

        String createCardsTableSQL = """
            CREATE TABLE IF NOT EXISTS cards (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL,
                name TEXT NOT NULL,
                type TEXT NOT NULL,
                manaCost INTEGER CHECK (manaCost >= 0),
                rarity TEXT CHECK (rarity IN ('common', 'uncommon', 'rare', 'legendary')),
                cardSet TEXT,
                power INTEGER CHECK (power >= 0),
                toughness INTEGER CHECK (toughness >= 0),
                effect TEXT,
                entersTapped BOOLEAN,
                manaProduced INTEGER CHECK (manaProduced >= 0),
                favorite BOOLEAN DEFAULT 0,
                FOREIGN KEY (username) REFERENCES users(username)
            );
        """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTableSQL);
            stmt.execute(createCardsTableSQL);
        }
    }

    private static void createAccount(Scanner scanner, Connection conn) {
        try {
            System.out.println("Choose account type to create (admin/user):");
            String role = validateRole(scanner);

            System.out.println("Enter a username:");
            String username = validateNonEmptyInput(scanner, "Username cannot be empty. Enter a valid username:");

            System.out.println("Enter a password:");
            String password = validateNonEmptyInput(scanner, "Password cannot be empty. Enter a valid password:");

            addUser(conn, username, password, role);
        } catch (SQLException e) {
            System.out.println("Error creating account: " + e.getMessage());
        }
    }

    private static void addUser(Connection conn, String username, String password, String role) throws SQLException {
        String insertSQL = "INSERT INTO users (username, password, role) VALUES (?, ?, ?);";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.executeUpdate();
            System.out.println(role + " account created successfully for username: " + username);
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                System.out.println("Username already exists. Try a different username.");
            } else {
                throw e;
            }
        }
    }

    private static void adminLogin(Scanner scanner, Connection conn) throws SQLException {
        System.out.println("Enter admin username:");
        String username = validateNonEmptyInput(scanner, "Username cannot be empty. Enter a valid username:");

        System.out.println("Enter admin password:");
        String password = validateNonEmptyInput(scanner, "Password cannot be empty. Enter a valid password:");

        if (isValidUser(conn, username, password, "admin")) {
            System.out.println("Admin logged in successfully!");
            while (true) {
                System.out.println("Admin actions: manage_users/view_collections/exit:");
                String action = scanner.nextLine().trim().toLowerCase();

                switch (action) {
                    case "manage_users":
                        manageUsers(scanner, conn);
                        break;
                    case "view_collections":
                        viewAllCollections(conn);
                        break;
                    case "exit":
                        System.out.println("Logging out as admin...");
                        return;
                    default:
                        System.out.println("Invalid action. Enter 'manage_users', 'view_collections', or 'exit'.");
                }
            }
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    private static void manageUsers(Scanner scanner, Connection conn) throws SQLException {
        while (true) {
            System.out.println("Manage Users: add/remove/view/exit:");
            String action = scanner.nextLine().trim().toLowerCase();

            switch (action) {
                case "add":
                    System.out.println("Enter new username:");
                    String username = validateNonEmptyInput(scanner, "Username cannot be empty. Enter a valid username:");

                    System.out.println("Enter new password:");
                    String password = validateNonEmptyInput(scanner, "Password cannot be empty. Enter a valid password:");

                    System.out.println("Enter role (admin/user):");
                    String role = validateRole(scanner);

                    addUser(conn, username, password, role);
                    break;

                case "remove":
                    System.out.println("Enter username to remove:");
                    String userToRemove = validateNonEmptyInput(scanner, "Username cannot be empty. Enter a valid username:");

                    removeUser(conn, userToRemove);
                    break;

                case "view":
                    viewUsers(conn);
                    break;

                case "exit":
                    return;

                default:
                    System.out.println("Invalid choice. Enter 'add', 'remove', 'view', or 'exit'.");
            }
        }
    }

    private static void removeUser(Connection conn, String username) throws SQLException {
        String deleteSQL = "DELETE FROM users WHERE username = ?;";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setString(1, username);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User removed successfully.");
            } else {
                System.out.println("User not found.");
            }
        }
    }

    private static void viewUsers(Connection conn) throws SQLException {
        String query = "SELECT username, role FROM users;";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("Registered Users:");
            while (rs.next()) {
                System.out.printf("Username: %s, Role: %s\n", rs.getString("username"), rs.getString("role"));
            }
        }
    }

    private static void viewAllCollections(Connection conn) throws SQLException {
        String query = "SELECT username, name, type, rarity FROM cards;";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("All User Collections:");
            while (rs.next()) {
                System.out.printf("User: %s | Card: %s | Type: %s | Rarity: %s\n",
                        rs.getString("username"), rs.getString("name"),
                        rs.getString("type"), rs.getString("rarity"));
            }
        }
    }

    private static void userLogin(Scanner scanner, Connection conn) throws SQLException {
        System.out.println("Enter username:");
        String username = validateNonEmptyInput(scanner, "Username cannot be empty. Enter a valid username:");

        System.out.println("Enter password:");
        String password = validateNonEmptyInput(scanner, "Password cannot be empty. Enter a valid password:");

        if (isValidUser(conn, username, password, "user")) {
            System.out.println("Login successful! Welcome, " + username);
            while (true) {
                System.out.println("Actions: browse/trade/exit:");
                String action = scanner.nextLine().trim().toLowerCase();

                switch (action) {
                    case "browse":
                        browseCollection(conn, username, scanner);
                        break;

                    case "trade":
                        tradeCards(conn, username, scanner);
                        break;

                    case "exit":
                        System.out.println("Logging out...");
                        return;

                    default:
                        System.out.println("Invalid action. Enter 'browse', 'trade', or 'exit'.");
                }
            }
        } else {
            System.out.println("Invalid user credentials.");
        }
    }

    private static boolean isValidUser(Connection conn, String username, String password, String role) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?;";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static void browseCollection(Connection conn, String username, Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("Browse actions: view/add/remove/favorite/exit:");
            String action = scanner.nextLine().trim().toLowerCase();

            switch (action) {
                case "view":
                    viewUserCollection(conn, username);
                    break;

                case "add":
                    addCardToCollection(conn, username, scanner);
                    break;

                case "remove":
                    removeCardFromCollection(conn, username, scanner);
                    break;

                case "favorite":
                    favoriteCard(conn, username, scanner);
                    break;

                case "exit":
                    return;

                default:
                    System.out.println("Invalid choice. Enter 'view', 'add', 'remove', 'favorite', or 'exit'.");
            }
        }
    }

    private static void viewUserCollection(Connection conn, String username) throws SQLException {
        String query = "SELECT * FROM cards WHERE username = ?;";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    System.out.println("Your collection is empty.");
                    return;
                }

                System.out.println(username + "'s Card Collection:");
                while (rs.next()) {
                    System.out.printf("Card: %s | Type: %s | Mana Cost: %d | Rarity: %s\n",
                            rs.getString("name"), rs.getString("type"),
                            rs.getInt("manaCost"), rs.getString("rarity"));
                }
            }
        }
    }

    private static void addCardToCollection(Connection conn, String username, Scanner scanner) throws SQLException {
        System.out.println("Enter card name:");
        String name = validateNonEmptyInput(scanner, "Card name cannot be empty. Enter a valid card name:");

        System.out.println("Enter card type (creature/spell/land):");
        String type = validateCardType(scanner);

        System.out.println("Enter mana cost (non-negative integer):");
        int manaCost = validateNonNegativeInteger(scanner, "Enter a valid non-negative integer for mana cost:");

        System.out.println("Enter rarity (common/uncommon/rare/legendary):");
        String rarity = validateRarity(scanner);

        System.out.println("Enter card set:");
        String cardSet = scanner.nextLine().trim();

        String insertSQL = """
            INSERT INTO cards (username, name, type, manaCost, rarity, cardSet)
            VALUES (?, ?, ?, ?, ?, ?);
        """;

        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, username);
            pstmt.setString(2, name);
            pstmt.setString(3, type);
            pstmt.setInt(4, manaCost);
            pstmt.setString(5, rarity);
            pstmt.setString(6, cardSet);
            pstmt.executeUpdate();
            System.out.println("Card added successfully.");
        }
    }

    private static void removeCardFromCollection(Connection conn, String username, Scanner scanner) throws SQLException {
        System.out.println("Enter card name to remove:");
        String name = validateNonEmptyInput(scanner, "Card name cannot be empty. Enter a valid card name:");

        String deleteSQL = "DELETE FROM cards WHERE username = ? AND name = ?;";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setString(1, username);
            pstmt.setString(2, name);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Card removed successfully.");
            } else {
                System.out.println("Card not found.");
            }
        }
    }

    private static void favoriteCard(Connection conn, String username, Scanner scanner) throws SQLException {
        System.out.println("Enter card name to mark as favorite:");
        String name = validateNonEmptyInput(scanner, "Card name cannot be empty. Enter a valid card name:");

        String updateSQL = "UPDATE cards SET favorite = 1 WHERE username = ? AND name = ?;";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, username);
            pstmt.setString(2, name);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Card marked as favorite.");
            } else {
                System.out.println("Card not found.");
            }
        }
    }

    private static void tradeCards(Connection conn, String username, Scanner scanner) throws SQLException {
        System.out.println("Enter the card name you want to trade:");
        String cardToTrade = validateNonEmptyInput(scanner, "Card name cannot be empty. Enter a valid card name:");

        System.out.println("Enter the username of the user you want to trade with:");
        String recipient = validateNonEmptyInput(scanner, "Recipient username cannot be empty. Enter a valid username:");

        System.out.println("Enter the card name you want from " + recipient + ":");
        String cardToReceive = validateNonEmptyInput(scanner, "Card name cannot be empty. Enter a valid card name:");

        String tradeSQL = """
            UPDATE cards
            SET username = CASE
                WHEN name = ? AND username = ? THEN ?
                WHEN name = ? AND username = ? THEN ?
                ELSE username
            END
            WHERE name IN (?, ?) AND username IN (?, ?);
        """;

        try (PreparedStatement pstmt = conn.prepareStatement(tradeSQL)) {
            pstmt.setString(1, cardToTrade);
            pstmt.setString(2, username);
            pstmt.setString(3, recipient);
            pstmt.setString(4, cardToReceive);
            pstmt.setString(5, recipient);
            pstmt.setString(6, username);
            pstmt.setString(7, cardToTrade);
            pstmt.setString(8, cardToReceive);
            pstmt.setString(9, username);
            pstmt.setString(10, recipient);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 2) {
                System.out.println("Trade successful!");
            } else {
                System.out.println("Trade failed. Check card ownership.");
            }
        }
    }

    private static String validateNonEmptyInput(Scanner scanner, String errorMessage) {
        while (true) {
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println(errorMessage);
        }
    }

    private static String validateRarity(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("common") || input.equals("uncommon") || input.equals("rare") || input.equals("legendary")) {
                return input;
            }
            System.out.println("Invalid rarity. Enter 'common', 'uncommon', 'rare', or 'legendary':");
        }
    }

    private static String validateCardType(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("creature") || input.equals("spell") || input.equals("land")) {
                return input;
            }
            System.out.println("Invalid card type. Enter 'creature', 'spell', or 'land':");
        }
    }

    private static int validateNonNegativeInteger(Scanner scanner, String errorMessage) {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= 0) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // Do nothing, just show error message
            }
            System.out.println(errorMessage);
        }
    }

    private static String validateRole(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("admin") || input.equals("user")) {
                return input;
            }
            System.out.println("Invalid role. Enter 'admin' or 'user':");
        }
    }
}
