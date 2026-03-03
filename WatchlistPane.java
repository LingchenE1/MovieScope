import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * WatchlistPane class provides a GUI panel for managing user's movie watchlist
 * Allows users to view, remove, and mark movies as watched from their watchlist
 * Implements core watchlist functionality required by the specification
 */
public class WatchlistPane extends VBox {
    private final MovieManager movieManager;  // For accessing movie data
    private final UserManager userManager;    // For managing user's watchlist operations
    private TableView<Movie> tableView; // Table displaying watchlist movies
    private ObservableList<Movie> watchlistData; // Observable list for watchlist data

    /**
     * Constructor initializes the watchlist panel
     * @param movieManager For movie data access
     * @param userManager For user watchlist operations
     */
    public WatchlistPane(MovieManager movieManager, UserManager userManager) {
        this.movieManager = movieManager;
        this.userManager = userManager;
        initialize(); // Set up UI components
    }

    /**
     * Initializes the UI components, layout, and event handlers
     */
    private void initialize() {
        setSpacing(10); // Vertical spacing
        setPadding(new javafx.geometry.Insets(10)); // Internal padding

        Label titleLabel = new Label("My Watchlist");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Create table view for watchlist movies
        tableView = new TableView<>();
        setupTableColumns(); // Configure table columns

        // Button to remove selected movie from watchlist
        Button removeButton = new Button("Remove Selected Movie");
        removeButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;"); // Red color for remove action
        removeButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent e) {
                removeFromWatchlist();
            }
        });

        // Button to mark selected movie as watched (moves to history)
        Button markWatchedButton = new Button("Mark Selected as Watched");
        markWatchedButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;"); // Blue color
        markWatchedButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent e) {
                markAsWatched();
            }
        });

        // Horizontal container for action buttons
        HBox buttonBox = new HBox(10, removeButton, markWatchedButton);

        refresh(); // Load initial watchlist data

        // Add all components to the panel
        getChildren().addAll(titleLabel, tableView, buttonBox);
    }

    /**
     * Configures table columns for watchlist movie display
     * Similar to MovieBrowserPane but for watchlist-specific operations
     */
    private void setupTableColumns() {
        TableColumn<Movie, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(80);

        TableColumn<Movie, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(200);

        TableColumn<Movie, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreCol.setPrefWidth(120);

        TableColumn<Movie, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        yearCol.setPrefWidth(80);

        TableColumn<Movie, Double> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        ratingCol.setPrefWidth(80);

        tableView.getColumns().add(idCol);
        tableView.getColumns().add(titleCol);
        tableView.getColumns().add(genreCol);
        tableView.getColumns().add(yearCol);
        tableView.getColumns().add(ratingCol);
    }

    /**
     * Refreshes the watchlist data from current user
     * Converts movie IDs to full Movie objects for display
     */
    public void refresh() {
        // Get list of movie IDs from user's watchlist
        List<String> watchlistIds = userManager.getCurrentUser().getWatchlist();
        List<Movie> watchlistMovies = new ArrayList<>();

        // Convert each movie ID to Movie object
        for (String movieId : watchlistIds) {
            Movie movie = movieManager.getMovie(movieId);
            if (movie != null) {
                watchlistMovies.add(movie);
            }
        }

        // Update observable list and table
        watchlistData = FXCollections.observableArrayList(watchlistMovies);
        tableView.setItems(watchlistData);
    }

    /**
     * Removes the selected movie from user's watchlist
     * Performs validation and updates data file
     */
    private void removeFromWatchlist() {
        // Get currently selected movie from table
        Movie selectedMovie = tableView.getSelectionModel().getSelectedItem();
        if (selectedMovie == null) {
            showAlert("Error", "Please select a movie to remove");
            return;
        }

        // Remove movie from user's watchlist and save changes
        if (userManager.getCurrentUser().removeFromWatchlist(selectedMovie.getId())) {
            userManager.saveUsersToCSV(); // Persist changes to CSV file
            refresh(); // Update table display
            showAlert("Success", "Movie removed from watchlist: " + selectedMovie.getTitle());
        }
    }

    /**
     * Marks selected movie as watched - moves it from watchlist to history
     * Implements the "Mark movie as watched" requirement from specification
     */
    private void markAsWatched() {
        Movie selectedMovie = tableView.getSelectionModel().getSelectedItem();
        if (selectedMovie == null) {
            showAlert("Error", "Please select a movie to mark as watched");
            return;
        }

        // Add to history and automatically remove from watchlist (per specification requirement)
        userManager.getCurrentUser().addToHistory(selectedMovie.getId());
        userManager.saveUsersToCSV(); // Save changes to file
        refresh(); // Update UI
        showAlert("Success", "Movie marked as watched: " + selectedMovie.getTitle());
    }

    /**
     * Utility method to display information/error alerts to user
     * @param title Alert dialog title
     * @param message Message to display in alert
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null); // No header text
        alert.setContentText(message);
        alert.showAndWait(); // Display and wait for user acknowledgment
    }
}