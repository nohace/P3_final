package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;



public class MTGCardManagerGUI extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("MTG Card Manager");
        showLoginRegisterScene();
    }

    private void showLoginRegisterScene() {
        // Layout
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(15));

        // Controls
        Label welcomeLabel = new Label("Welcome to MTG Card Manager!");
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");
        Button exitButton = new Button("Exit");

        layout.getChildren().addAll(welcomeLabel, loginButton, registerButton, exitButton);

        // Actions
        loginButton.setOnAction(e -> showRoleSelectionScene());
        registerButton.setOnAction(e -> showRegisterScene());
        exitButton.setOnAction(e -> primaryStage.close());

        // Scene
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showRoleSelectionScene() {
        // Layout
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(15));

        // Controls
        Label roleLabel = new Label("Choose your role:");
        Button adminButton = new Button("Admin");
        Button userButton = new Button("User");
        Button backButton = new Button("Back");

        layout.getChildren().addAll(roleLabel, adminButton, userButton, backButton);

        // Actions
        adminButton.setOnAction(e -> showAdminScene());
        userButton.setOnAction(e -> showUserScene());
        backButton.setOnAction(e -> showLoginRegisterScene());

        // Scene
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
    }

    private void showRegisterScene() {
        // Layout
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(15));

        // Controls
        Label registerLabel = new Label("Register a new account");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        Button registerButton = new Button("Register");
        Button backButton = new Button("Back");

        layout.getChildren().addAll(registerLabel, usernameField, passwordField, registerButton, backButton);

        // Actions
        registerButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            if (!username.isEmpty() && !password.isEmpty()) {
                // Call database logic to register user
                System.out.println("Registered user: " + username);
                showLoginRegisterScene();
            } else {
                showAlert("Error", "Please enter a valid username and password.");
            }
        });
        backButton.setOnAction(e -> showLoginRegisterScene());

        // Scene
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
    }

    private void showUserScene() {
        // Layout
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(15));

        // Controls
        Label userLabel = new Label("User Dashboard");
        Button browseButton = new Button("Browse Cards");
        Button tradeButton = new Button("Trade Cards");
        Button logoutButton = new Button("Logout");

        layout.getChildren().addAll(userLabel, browseButton, tradeButton, logoutButton);

        // Actions
        browseButton.setOnAction(e -> showAlert("Browse", "Here you can browse your card collection."));
        tradeButton.setOnAction(e -> showAlert("Trade", "Here you can trade cards with other users."));
        logoutButton.setOnAction(e -> showLoginRegisterScene());

        // Scene
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
    }

    private void showAdminScene() {
        // Layout
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(15));

        // Controls
        Label adminLabel = new Label("Admin Dashboard");
        Button manageUsersButton = new Button("Manage Users");
        Button viewCollectionsButton = new Button("View All Collections");
        Button logoutButton = new Button("Logout");

        layout.getChildren().addAll(adminLabel, manageUsersButton, viewCollectionsButton, logoutButton);

        // Actions
        manageUsersButton.setOnAction(e -> showAlert("Manage Users", "Here you can manage users."));
        viewCollectionsButton.setOnAction(e -> showAlert("View Collections", "Here you can view all card collections."));
        logoutButton.setOnAction(e -> showLoginRegisterScene());

        // Scene
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
