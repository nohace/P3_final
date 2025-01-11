package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MTGCardCollectionSwingApp {

    private static final String DB_URL = "jdbc:sqlite:C:/Users/andrei/IdeaProjects/P3_final/mtg_card_manager.db";
    private JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                MTGCardCollectionSwingApp app = new MTGCardCollectionSwingApp();
                app.initializeDatabase(); // Create tables if they don't exist
                app.initializeUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the tables if they do not already exist (similar to console initializeDatabase).
     */
    private void initializeDatabase() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Create users table
            String createUsersTableSQL = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    role TEXT NOT NULL CHECK (role IN ('admin', 'user'))
                );
            """;
            stmt.execute(createUsersTableSQL);

            // Create cards table
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
            stmt.execute(createCardsTableSQL);
        }
    }

    /**
     * Initialize the main window and show the login scene.
     */
    private void initializeUI() {
        frame = new JFrame("MTG Card Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null); // Center the frame
        showLoginScene();
        frame.setVisible(true);
    }

    /**
     * Scene 1: Login / Register scene.
     */
    private void showLoginScene() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Welcome to MTG Card Manager");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;

        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(passwordField, gbc);

        JLabel roleLabel = new JLabel("Role:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(roleLabel, gbc);

        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"User", "Admin"});
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(roleComboBox, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panel.add(loginButton, gbc);

        JButton registerButton = new JButton("Register");
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(registerButton, gbc);

        // Perform login
        loginButton.addActionListener((ActionEvent e) -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String role = roleComboBox.getSelectedItem().toString().toLowerCase();

            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                if (isValidUser(conn, username, password, role)) {
                    if (role.equals("user")) {
                        showUserScene(username);
                    } else {
                        showAdminScene(username);
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Invalid credentials!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        // Perform registration
        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String role = roleComboBox.getSelectedItem().toString().toLowerCase();

            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                addUser(conn, username, password, role);
                JOptionPane.showMessageDialog(
                        frame,
                        "Account created successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } catch (SQLException ex) {
                if (ex.getMessage().contains("UNIQUE")) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Username already exists. Try a different username.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Error creating account: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        frame.setContentPane(panel);
        frame.revalidate();
    }

    /**
     * Scene 2 (User): Main user panel with "Browse Collection", "Trade Cards", "Logout".
     */
    private void showUserScene(String username) {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        JLabel welcomeLabel = new JLabel("Welcome, " + username, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JButton browseButton = new JButton("Browse Collection");
        JButton tradeButton = new JButton("Trade Cards");
        JButton logoutButton = new JButton("Logout");

        browseButton.addActionListener(e -> showBrowseCollectionScene(username));
        tradeButton.addActionListener(e -> showTradeScene(username));
        logoutButton.addActionListener(e -> showLoginScene());

        panel.add(welcomeLabel);
        panel.add(browseButton);
        panel.add(tradeButton);
        panel.add(logoutButton);

        frame.setContentPane(panel);
        frame.revalidate();
    }

    /**
     * Scene 2 (Admin): Main admin panel with "Manage Users", "View All Collections", "Logout".
     */
    private void showAdminScene(String username) {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        JLabel welcomeLabel = new JLabel("Welcome, Admin " + username, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JButton manageUsersButton = new JButton("Manage Users");
        JButton viewCollectionsButton = new JButton("View All Collections");
        JButton logoutButton = new JButton("Logout");

        manageUsersButton.addActionListener(e -> showManageUsersScene());
        viewCollectionsButton.addActionListener(e -> showViewAllCollectionsScene());
        logoutButton.addActionListener(e -> showLoginScene());

        panel.add(welcomeLabel);
        panel.add(manageUsersButton);
        panel.add(viewCollectionsButton);
        panel.add(logoutButton);

        frame.setContentPane(panel);
        frame.revalidate();
    }

    /**
     * Scene 3 (User): Browse Collection – allow "View My Cards", "Add Card", "Remove Card", "Favorite a Card", and "Back".
     */
    private void showBrowseCollectionScene(String username) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton viewButton = new JButton("View My Cards");
        JButton addButton = new JButton("Add Card");
        JButton removeButton = new JButton("Remove Card");
        JButton favoriteButton = new JButton("Favorite Card");
        JButton backButton = new JButton("Back");

        // Text area to display messages / results
        JTextArea displayArea = new JTextArea(10, 40);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // View My Cards
        viewButton.addActionListener(e -> {
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String query = "SELECT * FROM cards WHERE username = ?;";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, username);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        StringBuilder sb = new StringBuilder();
                        boolean found = false;
                        while (rs.next()) {
                            found = true;
                            sb.append(String.format("Card: %s | Type: %s | Mana Cost: %d | Rarity: %s | Favorite: %s\n",
                                    rs.getString("name"),
                                    rs.getString("type"),
                                    rs.getInt("manaCost"),
                                    rs.getString("rarity"),
                                    rs.getBoolean("favorite") ? "Yes" : "No"));
                        }
                        displayArea.setText(found ? sb.toString() : "Your collection is empty.");
                    }
                }
            } catch (SQLException ex) {
                displayArea.setText("Error fetching cards: " + ex.getMessage());
            }
        });

        // Add Card
        addButton.addActionListener(e -> {
            JPanel inputPanel = new JPanel(new GridLayout(6, 2));
            JTextField nameField = new JTextField();
            JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"creature", "spell", "land"});
            JTextField manaCostField = new JTextField();
            JComboBox<String> rarityComboBox = new JComboBox<>(new String[]{"common", "uncommon", "rare", "legendary"});
            JTextField cardSetField = new JTextField();

            inputPanel.add(new JLabel("Card Name:"));
            inputPanel.add(nameField);
            inputPanel.add(new JLabel("Type:"));
            inputPanel.add(typeComboBox);
            inputPanel.add(new JLabel("Mana Cost:"));
            inputPanel.add(manaCostField);
            inputPanel.add(new JLabel("Rarity:"));
            inputPanel.add(rarityComboBox);
            inputPanel.add(new JLabel("Card Set:"));
            inputPanel.add(cardSetField);

            int result = JOptionPane.showConfirmDialog(frame, inputPanel,
                    "Add New Card", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText().trim();
                String type = (String) typeComboBox.getSelectedItem();
                String rarity = (String) rarityComboBox.getSelectedItem();
                String cardSet = cardSetField.getText().trim();
                int manaCost;
                try {
                    manaCost = Integer.parseInt(manaCostField.getText().trim());
                    if (manaCost < 0) {
                        throw new NumberFormatException("Negative cost");
                    }
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(frame,
                            "Mana cost must be a non-negative integer.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Insert card into DB
                try (Connection conn = DriverManager.getConnection(DB_URL)) {
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
                        displayArea.setText("Card added successfully.\n");
                    }
                } catch (SQLException ex) {
                    displayArea.setText("Error adding card: " + ex.getMessage());
                }
            }
        });

        // Remove Card
        removeButton.addActionListener(e -> {
            String cardName = JOptionPane.showInputDialog(frame,
                    "Enter the card name to remove:",
                    "Remove Card",
                    JOptionPane.QUESTION_MESSAGE);
            if (cardName != null && !cardName.trim().isEmpty()) {
                try (Connection conn = DriverManager.getConnection(DB_URL)) {
                    String deleteSQL = "DELETE FROM cards WHERE username = ? AND name = ?;";
                    try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
                        pstmt.setString(1, username);
                        pstmt.setString(2, cardName.trim());
                        int rowsAffected = pstmt.executeUpdate();
                        if (rowsAffected > 0) {
                            displayArea.setText("Card removed successfully.\n");
                        } else {
                            displayArea.setText("Card not found.\n");
                        }
                    }
                } catch (SQLException ex) {
                    displayArea.setText("Error removing card: " + ex.getMessage());
                }
            }
        });

        // Favorite a Card
        favoriteButton.addActionListener(e -> {
            String cardName = JOptionPane.showInputDialog(frame,
                    "Enter the card name to mark as favorite:",
                    "Favorite Card",
                    JOptionPane.QUESTION_MESSAGE);
            if (cardName != null && !cardName.trim().isEmpty()) {
                try (Connection conn = DriverManager.getConnection(DB_URL)) {
                    String updateSQL = "UPDATE cards SET favorite = 1 WHERE username = ? AND name = ?;";
                    try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                        pstmt.setString(1, username);
                        pstmt.setString(2, cardName.trim());
                        int rowsAffected = pstmt.executeUpdate();
                        if (rowsAffected > 0) {
                            displayArea.setText("Card marked as favorite.\n");
                        } else {
                            displayArea.setText("Card not found.\n");
                        }
                    }
                } catch (SQLException ex) {
                    displayArea.setText("Error marking card as favorite: " + ex.getMessage());
                }
            }
        });

        // Back
        backButton.addActionListener(e -> showUserScene(username));

        buttonPanel.add(viewButton);
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(favoriteButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        frame.setContentPane(mainPanel);
        frame.revalidate();
    }

    /**
     * Scene 4 (User): Trading cards.
     * The user enters the card they want to trade, the username to trade with, and the card they want in return.
     */
    private void showTradeScene(String username) {
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        JTextField cardToTradeField = new JTextField();
        JTextField tradeWithField = new JTextField();
        JTextField cardToReceiveField = new JTextField();

        inputPanel.add(new JLabel("Card you want to trade:"));
        inputPanel.add(cardToTradeField);
        inputPanel.add(new JLabel("Username to trade with:"));
        inputPanel.add(tradeWithField);
        inputPanel.add(new JLabel("Card you want from them:"));
        inputPanel.add(cardToReceiveField);

        int result = JOptionPane.showConfirmDialog(frame, inputPanel,
                "Trade Cards", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String cardToTrade = cardToTradeField.getText().trim();
            String recipient = tradeWithField.getText().trim();
            String cardToReceive = cardToReceiveField.getText().trim();

            // Perform trade logic
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
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
                        JOptionPane.showMessageDialog(frame,
                                "Trade successful!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame,
                                "Trade failed. Check card ownership or card names.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Error trading cards: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // User cancelled trading
        }

        // Go back to user scene
        showUserScene(username);
    }

    /**
     * Scene (Admin): Manage Users – allow "Add User", "Remove User", "View Users", and "Back".
     */
    private void showManageUsersScene() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton addUserButton = new JButton("Add User");
        JButton removeUserButton = new JButton("Remove User");
        JButton viewUsersButton = new JButton("View Users");
        JButton backButton = new JButton("Back");

        JTextArea displayArea = new JTextArea(10, 40);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Add User
        addUserButton.addActionListener(e -> {
            JPanel inputPanel = new JPanel(new GridLayout(3, 2));
            JTextField usernameField = new JTextField();
            JTextField passwordField = new JTextField();
            JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"admin", "user"});

            inputPanel.add(new JLabel("New Username:"));
            inputPanel.add(usernameField);
            inputPanel.add(new JLabel("New Password:"));
            inputPanel.add(passwordField);
            inputPanel.add(new JLabel("Role:"));
            inputPanel.add(roleComboBox);

            int result = JOptionPane.showConfirmDialog(frame, inputPanel,
                    "Add User", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try (Connection conn = DriverManager.getConnection(DB_URL)) {
                    addUser(conn, usernameField.getText().trim(),
                            passwordField.getText().trim(),
                            (String) roleComboBox.getSelectedItem());
                    displayArea.setText("User added successfully.\n");
                } catch (SQLException ex) {
                    if (ex.getMessage().contains("UNIQUE")) {
                        displayArea.setText("Username already exists. Try a different one.\n");
                    } else {
                        displayArea.setText("Error adding user: " + ex.getMessage() + "\n");
                    }
                }
            }
        });

        // Remove User
        removeUserButton.addActionListener(e -> {
            String userToRemove = JOptionPane.showInputDialog(frame,
                    "Enter username to remove:",
                    "Remove User",
                    JOptionPane.QUESTION_MESSAGE);
            if (userToRemove != null && !userToRemove.trim().isEmpty()) {
                try (Connection conn = DriverManager.getConnection(DB_URL)) {
                    String deleteSQL = "DELETE FROM users WHERE username = ?;";
                    try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
                        pstmt.setString(1, userToRemove.trim());
                        int rowsAffected = pstmt.executeUpdate();
                        if (rowsAffected > 0) {
                            displayArea.setText("User removed successfully.\n");
                        } else {
                            displayArea.setText("User not found.\n");
                        }
                    }
                } catch (SQLException ex) {
                    displayArea.setText("Error removing user: " + ex.getMessage() + "\n");
                }
            }
        });

        // View Users
        viewUsersButton.addActionListener(e -> {
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement()) {
                String query = "SELECT username, role FROM users;";
                try (ResultSet rs = stmt.executeQuery(query)) {
                    StringBuilder sb = new StringBuilder("Registered Users:\n");
                    while (rs.next()) {
                        sb.append(String.format("Username: %s, Role: %s\n",
                                rs.getString("username"), rs.getString("role")));
                    }
                    displayArea.setText(sb.toString());
                }
            } catch (SQLException ex) {
                displayArea.setText("Error viewing users: " + ex.getMessage() + "\n");
            }
        });

        // Back
        backButton.addActionListener(e -> {
            // After managing, return to admin login again (or the admin scene).
            // The code that navigates you back to the Admin scene is not sure who the admin user is,
            // so you might want to store the current admin's username in a field or pass it along.
            // For simplicity, we just revert to the login scene, or you can store a global adminUsername.
            showLoginScene();
        });

        buttonPanel.add(addUserButton);
        buttonPanel.add(removeUserButton);
        buttonPanel.add(viewUsersButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        frame.setContentPane(mainPanel);
        frame.revalidate();
    }

    /**
     * Scene (Admin): View All Collections – show all cards from every user.
     */
    private void showViewAllCollectionsScene() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JTextArea displayArea = new JTextArea(15, 50);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showLoginScene());

        // Retrieve all cards from DB
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String query = "SELECT username, name, type, rarity FROM cards;";
            try (ResultSet rs = stmt.executeQuery(query)) {
                StringBuilder sb = new StringBuilder("All User Collections:\n");
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    sb.append(String.format("User: %s | Card: %s | Type: %s | Rarity: %s\n",
                            rs.getString("username"),
                            rs.getString("name"),
                            rs.getString("type"),
                            rs.getString("rarity")));
                }
                if (!found) {
                    sb.append("No cards found in the database.\n");
                }
                displayArea.setText(sb.toString());
            }
        } catch (SQLException ex) {
            displayArea.setText("Error retrieving all collections: " + ex.getMessage());
        }

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.revalidate();
    }

    /**
     * Validate user credentials (similar to console-based isValidUser).
     */
    private boolean isValidUser(Connection conn, String username, String password, String role) throws SQLException {
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

    /**
     * Add a user (similar to console-based addUser).
     */
    private void addUser(Connection conn, String username, String password, String role) throws SQLException {
        if (username.isEmpty() || password.isEmpty()) {
            throw new SQLException("Username/Password cannot be empty.");
        }
        String insertSQL = "INSERT INTO users (username, password, role) VALUES (?, ?, ?);";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.executeUpdate();
        }
    }
}
