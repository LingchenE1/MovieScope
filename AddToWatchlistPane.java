import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.List;

/**
 * Panel for adding movies to user's watchlist
 * Allow users to select from all the movies and add them to the watchlist.
 */
public class AddToWatchlistPane extends VBox {
    private final MovieManager movieManager;  //Class for managing movie data
    private final UserManager userManager;    //Class for managing users data
    private ComboBox<Movie> movieComboBox;  //Dropdown box for selecting movies from the available list

    /**
     * Constructor for AddToWatchlistPane
     */
    public AddToWatchlistPane(MovieManager movieManager, UserManager userManager) {
        this.movieManager = movieManager;       //Manager for movie data operations
        this.userManager = userManager;     //Manager for user data operations
        initialize();
    }

    /**
     * Initializes the UI components
     * Set the labels, the dropdown box for movie selection and the add button
     */
    private void initialize() {
        setSpacing(15);
        setPadding(new Insets(10));

        // Main title label
        Label titleLabel = new Label("Add Movie to Watchlist");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Movie selection section
        Label selectLabel = new Label("Select a movie:");
        movieComboBox = new ComboBox<>();
        refreshMovieList();

        // Button for adding movies to the Watchlist
        Button addButton = new Button("Add to Watchlist");
        addButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        addButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                addToWatchlist();
            }
        });

        // Add all components to the main pane
        getChildren().addAll(titleLabel, selectLabel, movieComboBox, addButton);
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
     * Adds the selected movie to the current user's watchlist
     */
    private void addToWatchlist() {
        Movie selectedMovie = movieComboBox.getValue();

        // Verify whether a movie has been selected
        if (selectedMovie == null) {
            showAlert("Error", "Please select a movie");
            return;
        }

        // Attempt to add movie to watchlist
        if (userManager.getCurrentUser().addToWatchlist(selectedMovie.getId())) {
            // Save changes
            userManager.saveUsersToCSV();
            showAlert("Success", "Movie added to watchlist: " + selectedMovie.getTitle());

            // Clear selection after successful addition
            movieComboBox.getSelectionModel().clearSelection();
        } else {
            showAlert("Error", "Movie is already in your watchlist");
        }
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