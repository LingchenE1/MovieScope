import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * MovieManagementApp class serves as the main application class for the Movie Recommendation & Tracker System
 * Extends JavaFX Application class to provide the primary GUI interface
 * Manages all core components and user interface navigation
 */
public class MovieManagementApp extends Application {
    private MovieManager movieManager;
    private UserManager userManager;
    private RecommendationEngine recommendationEngine;

    private Stage primaryStage;
    private BorderPane rootLayout;

    // UI Components
    private Label statusLabel;
    private MovieBrowserPane movieBrowserPane;
    private WatchlistPane watchlistPane;
    private HistoryPane historyPane;
    private RecommendationPane recommendationPane;

    /**
     * Initialize method called before application start
     * Sets up core managers and loads initial data
     */
    @Override
    public void init() {
        // Initialize managers
        this.movieManager = new MovieManager();
        this.userManager = new UserManager("users.csv");
        this.recommendationEngine = new RecommendationEngine(movieManager);

        // Load data
        movieManager.loadMoviesFromCSV("movies.csv");
    }

    /**
     * Main application entry point - called when application starts
     * Sets up primary stage and initializes user interface
     * @param primaryStage The primary window for this application
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Movie Recommendation & Tracker System");

        initRootLayout();
        showLoginScreen();
    }

    /**
     * Initializes the root layout structure of the application
     * Creates main BorderPane container with status bar
     */
    private void initRootLayout() {
        rootLayout = new BorderPane();
        rootLayout.setPadding(new Insets(10));

        // Create status bar
        statusLabel = new Label("Welcome to Movie Recommendation System");
        statusLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");

        HBox statusBar = new HBox(statusLabel);
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setPadding(new Insets(5));
        statusBar.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc;");

        rootLayout.setBottom(statusBar);

        Scene scene = new Scene(rootLayout, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Displays the login screen with authentication form
     * Provides user interface for login and registration
     * Centered layout with styled form elements
     */
    private void showLoginScreen() {
        VBox loginPane = new VBox();
        loginPane.setAlignment(Pos.CENTER);

        VBox loginBox = new VBox(15);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(30));
        loginBox.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5px;");

        Label titleLabel = new Label("Movie Recommendation & Tracker");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label subtitleLabel = new Label("Please login or register to continue");
        subtitleLabel.setStyle("-fx-text-fill: #7f8c8d;");

        final TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setPrefWidth(250);

        final PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefWidth(250);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        loginButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent e) {
                handleLogin(usernameField.getText(), passwordField.getText());
            }
        });

        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        registerButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent e) {
                handleRegister(usernameField.getText(), passwordField.getText());
            }
        });

        buttonBox.getChildren().addAll(loginButton, registerButton);

        loginBox.getChildren().addAll(titleLabel, subtitleLabel, usernameField, passwordField, buttonBox);
        loginPane.getChildren().add(loginBox);

        rootLayout.setCenter(loginPane);
        updateStatus("Please login to continue");
    }

    /**
     * Displays the main application dashboard after successful login
     * Sets up navigation menu and shows default content panel
     * Provides access to all application features
     */
    private void showMainDashboard() {
        // Create main menu
        VBox menu = createMainMenu();
        rootLayout.setLeft(menu);

        // Show movie browser by default
        showMovieBrowser();
    }

    /**
     * Creates the main navigation menu for the application
     * Provides access to all major features and user actions
     * @return VBox container with styled menu buttons and user welcome
     */
    private VBox createMainMenu() {
        VBox menu = new VBox(5);
        menu.setPadding(new Insets(10));
        menu.setPrefWidth(200);
        menu.setStyle("-fx-background-color: #34495e;");

        Label welcomeLabel = new Label("Welcome, " + userManager.getCurrentUser().getUsername());
        welcomeLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        // Menu buttons
        Button[] menuButtons = {
                createMenuButton("Browse Movies", new Runnable() {
                    @Override
                    public void run() {
                        showMovieBrowser();
                    }
                }),
                createMenuButton("Add Movie to Watchlist", new Runnable() {
                    @Override
                    public void run() {
                        showAddToWatchlist();
                    }
                }),
                createMenuButton("My Watchlist", new Runnable() {
                    @Override
                    public void run() {
                        showWatchlist();
                    }
                }),
                createMenuButton("Mark as Watched", new Runnable() {
                    @Override
                    public void run() {
                        showMarkAsWatched();
                    }
                }),
                createMenuButton("View History", new Runnable() {
                    @Override
                    public void run() {
                        showHistory();
                    }
                }),
                createMenuButton("Get Recommendations", new Runnable() {
                    @Override
                    public void run() {
                        showRecommendations();
                    }
                }),
                createMenuButton("Account Settings", new Runnable() {
                    @Override
                    public void run() {
                        showAccountSettings();
                    }
                }),
                createMenuButton("Logout", new Runnable() {
                    @Override
                    public void run() {
                        handleLogout();
                    }
                })
        };

        menu.getChildren().add(welcomeLabel);
        menu.getChildren().add(new Separator());
        for (Button button : menuButtons) {
            menu.getChildren().add(button);
        }

        return menu;
    }

    /**
     * Creates a styled menu button with hover effects
     * @param text The display text for the button
     * @param action The action to execute when button is clicked
     * @return Button configured with styling and event handlers
     */
    private Button createMenuButton(String text, final Runnable action) {
        final Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");

        button.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent e) {
                action.run();
            }
        });

        //Change the button style when the mouse hovers over it.
        button.setOnMouseEntered(new EventHandler<>() {
            @Override
            public void handle(MouseEvent e) {
                button.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white;");
            }
        });

        //Restore the button style when the mouse is moved out.
        button.setOnMouseExited(new EventHandler<>() {
            @Override
            public void handle(MouseEvent e) {
                button.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
            }
        });

        return button;
    }

    /**
     * Handles user login authentication process
     * Validates input credentials and manages login flow
     * @param username The username entered by user
     * @param password The password entered by user
     */
    private void handleLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both username and password");
            return;
        }

        if (userManager.login(username, password)) {
            updateStatus("Login successful! Welcome " + username);
            showMainDashboard();
        } else {
            showAlert("Login Failed", "Invalid username or password");
        }
    }

    /**
     * Handles user registration process
     * Validates input and creates new user account
     * @param username The desired username for new account
     * @param password The password for new account
     */
    private void handleRegister(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both username and password");
            return;
        }

        if (userManager.register(username, password)) {
            updateStatus("Registration successful! Please login");
            showAlert("Success", "Account created successfully! Please login with your new credentials.");
        } else {
            showAlert("Registration Failed", "Username already exists");
        }
    }

    /**
     * Handles user logout process
     * Clears current user session and returns to login screen
     */
    private void handleLogout() {
        userManager.logout();
        updateStatus("Logged out successfully");
        showLoginScreen();
        rootLayout.setLeft(null);
    }

    /**
     * Displays the movie browser panel
     * Shows all available movies in the system
     */
    private void showMovieBrowser() {
        if (movieBrowserPane == null) {
            movieBrowserPane = new MovieBrowserPane(movieManager);
        }
        rootLayout.setCenter(movieBrowserPane);
        updateStatus("Browsing all movies");
    }

    /**
     * Displays the add to watchlist panel
     * Allows users to search and add movies to their watchlist
     */
    private void showAddToWatchlist() {
        AddToWatchlistPane pane = new AddToWatchlistPane(movieManager, userManager);
        rootLayout.setCenter(pane);
        updateStatus("Add movies to your watchlist");
    }

    /**
     * Displays the user's watchlist panel
     * Shows movies currently in user's watchlist with management options
     */
    private void showWatchlist() {
        if (watchlistPane == null) {
            watchlistPane = new WatchlistPane(movieManager, userManager);
        } else {
            watchlistPane.refresh();
        }
        rootLayout.setCenter(watchlistPane);
        updateStatus("Viewing your watchlist");
    }

    /**
     * Displays the mark as watched panel
     * Allows users to move movies from watchlist to history
     */
    private void showMarkAsWatched() {
        MarkAsWatchedPane pane = new MarkAsWatchedPane(movieManager, userManager);
        rootLayout.setCenter(pane);
        updateStatus("Mark movies as watched");
    }

    /**
     * Displays the watch history panel
     * Shows movies the user has previously watched
     */
    private void showHistory() {
        if (historyPane == null) {
            historyPane = new HistoryPane(movieManager, userManager);
        } else {
            historyPane.refresh();
        }
        rootLayout.setCenter(historyPane);
        updateStatus("Viewing your watch history");
    }

    /**
     * Displays the recommendations panel
     * Shows personalized movie recommendations based on user preferences
     */
    private void showRecommendations() {
        if (recommendationPane == null) {
            recommendationPane = new RecommendationPane(movieManager, userManager, recommendationEngine);
        } else {
            recommendationPane.refresh();
        }
        rootLayout.setCenter(recommendationPane);
        updateStatus("Getting movie recommendations");
    }

    /**
     * Displays the account settings panel
     * Allows users to manage their account preferences
     */
    private void showAccountSettings() {
        AccountSettingsPane pane = new AccountSettingsPane(userManager);
        rootLayout.setCenter(pane);
        updateStatus("Account settings");
    }

    /**
     * Updates the status bar with the specified message
     * @param message The status message to display
     */
    private void updateStatus(String message) {
        statusLabel.setText("Status: " + message);
    }

    /**
     * Displays an information alert dialog to the user
     * @param title The title of the alert dialog
     * @param message The message content to display
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * The main entry point of the application
     * Launches the JavaFX application
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
}