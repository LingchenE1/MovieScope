import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Mark the movie as watched on the panel and allow users to view it.
 */
public class MarkAsWatchedPane extends VBox {
    private final MovieManager movieManager;  //Class for managing movie data
    private final UserManager userManager;    //Class for managing users data
    private ComboBox<Movie> movieComboBox;  // Dropdown box for selecting movies from the available list

    /**
     * Constructor for MarkAsWatchedPane
     */
    public MarkAsWatchedPane(MovieManager movieManager, UserManager userManager) {
        this.movieManager = movieManager;       //Manager for movie data operations
        this.userManager = userManager;     //Manager for user data operations and user history
        initialize();
    }

    /**
     * Initializes the UI components
     * Sets up labels, movie selection dropdown and mark as watched button
     */
    private void initialize() {
        setSpacing(15);
        setPadding(new javafx.geometry.Insets(10));

        // Main title label
        Label titleLabel = new Label("Mark Movie as Watched");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // label content
        Label descLabel = new Label("Select a movie to mark as watched. If it's in your watchlist, it will be automatically removed.");
        descLabel.setWrapText(true);

        // Movie selection section
        Label selectLabel = new Label("Select a movie:");
        movieComboBox = new ComboBox<>();
        refreshMovieList();// Populate the combo box with available movies

        // Button to mark selected movie as watched
        Button markButton = new Button("Mark as Watched");
        markButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");
        markButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                markAsWatched();
            }
        });

        // Add all components to the main pane
        getChildren().addAll(titleLabel, descLabel, selectLabel, movieComboBox, markButton);
    }

    /**
     * Refresh movie list in the drop-down box
     * Retrieve all movies from the movie manager and update the dropdown box
     */
    private void refreshMovieList() {
        List<Movie> movies = movieManager.getAllMovies();
        movieComboBox.setItems(FXCollections.observableArrayList(movies));
    }

    /**
     * Mark the selected movie as watched and add this movie to the history.
     */
    private void markAsWatched() {
        Movie selectedMovie = movieComboBox.getValue();

        // Verify whether a movie has been selected
        if (selectedMovie == null) {
            showAlert("Error", "Please select a movie");
            return;
        }

        // Add movie to the history
        userManager.getCurrentUser().addToHistory(selectedMovie.getId());

        // Save changes
        userManager.saveUsersToCSV();

        // Show success message
        showAlert("Success", "Movie marked as watched: " + selectedMovie.getTitle());

        // Clear selection after successful operation
        movieComboBox.getSelectionModel().clearSelection();
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