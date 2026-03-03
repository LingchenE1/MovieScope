import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * User Account Settings Panel
 * Provide the functions of password modification and account information display
 */
public class AccountSettingsPane extends VBox {
    // Manager class for handling user data and operations
    private final UserManager userManager;

    /**
     * Constructor for AccountSettingsPane
     */
    public AccountSettingsPane(UserManager userManager) {
        this.userManager = userManager;     //Manager for user data operations and authentication
        initialize();
    }

    /**
     * Initializes the UI components
     * Sets up password change section and account information display
     */
    private void initialize() {
        setSpacing(15);
        setPadding(new javafx.geometry.Insets(10));

        // Main title label
        Label titleLabel = new Label("Account Settings");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Password Modification Section
        Label passLabel = new Label("Change Password");
        passLabel.setStyle("-fx-font-weight: bold;");

        // Password input field
        PasswordField currentPassField = new PasswordField();
        currentPassField.setPromptText("Current Password");

        PasswordField newPassField = new PasswordField();
        newPassField.setPromptText("New Password");

        PasswordField confirmPassField = new PasswordField();
        confirmPassField.setPromptText("Confirm New Password");

        // Button for password modification
        Button changePassButton = new Button("Change Password");
        changePassButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                changePassword(
                        currentPassField.getText(),
                        newPassField.getText(),
                        confirmPassField.getText()
                );
            }
        });

        // Container for password modification component
        VBox passBox = new VBox(10, passLabel, currentPassField, newPassField, confirmPassField, changePassButton);
        passBox.setStyle("-fx-padding: 15; -fx-border-color: #ddd; -fx-border-radius: 5px;");

        // Account information display section
        Label infoLabel = new Label("Account Information");
        infoLabel.setStyle("-fx-font-weight: bold;");

        // Display the current user's statistics information
        Label userLabel = new Label("Username: " + userManager.getCurrentUser().getUsername());
        Label watchlistLabel = new Label("Movies in watchlist: " +
                userManager.getCurrentUser().getWatchlist().size());
        Label historyLabel = new Label("Movies watched: " +
                userManager.getCurrentUser().getHistory().size());

        // Container for account information
        VBox infoBox = new VBox(10, infoLabel, userLabel, watchlistLabel, historyLabel);
        infoBox.setStyle("-fx-padding: 15; -fx-border-color: #ddd; -fx-border-radius: 5px;");

        // Add all sections to the main pane
        getChildren().addAll(titleLabel, passBox, infoBox);
    }

    /**
     * Handle the password modification process and conduct verification
     * @param currentPass The user's current password for verification
     * @param newPass The new password to be set
     * @param confirmPass Confirmation of the new password
     */
    private void changePassword(String currentPass, String newPass, String confirmPass) {
        // Verify whether all fields have been filled in.
        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            showAlert("Error", "Please fill in all password fields");
            return;
        }

        // Verify that the new password and the confirmed password match each other
        if (!newPass.equals(confirmPass)) {
            showAlert("Error", "New passwords do not match");
            return;
        }

        // Verify whether the current password is correct
        if (!currentPass.equals(userManager.getCurrentUser().getPassword())) {
            showAlert("Error", "Current password is incorrect");
            return;
        }

        // Success message
        showAlert("Success", "Password changed successfully (Note: This is a demo - password change not fully implemented)");
    }

    /**
     * Display the information prompt dialog box
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
