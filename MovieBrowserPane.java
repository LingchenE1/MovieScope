import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * MovieBrowserPane class provides a GUI panel for browsing and searching movies
 * Displays all movies in a table view with search functionality
 * This is part of the advanced feature (GUI implementation) for the Movie Recommendation System
 */
public class MovieBrowserPane extends VBox {
    private final MovieManager movieManager;  // Handles movie data operations and storage
    private TableView<Movie> tableView; // JavaFX table component to display movies in tabular format
    private ObservableList<Movie> movieData; // Observable list for automatic UI updates when data changes

    /**
     * Constructor initializes the movie browser panel
     * @param movieManager Reference to the movie management system for data access
     */
    public MovieBrowserPane(MovieManager movieManager) {
        this.movieManager = movieManager;
        initialize(); // Set up the UI components and layout
    }

    /**
     * Initializes all UI components and sets up the layout
     * Called once during object construction
     */
    private void initialize() {
        setSpacing(10); // Vertical spacing between child components
        setPadding(new javafx.geometry.Insets(10)); // Padding around the panel edges

        Label titleLabel = new Label("All Movies");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Search box for filtering movies by title or genre
        TextField searchField = new TextField();
        searchField.setPromptText("Search movies by title or genre...");
        // Add listener to filter movies in real-time as user types
        searchField.textProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterMovies(newValue);
            }
        });

        // Create table view for displaying movie data
        tableView = new TableView<>();
        setupTableColumns(); // Configure table columns and data binding

        // Load initial movie data into the table
        refresh();

        // Add all components to the panel in vertical order
        getChildren().addAll(titleLabel, searchField, tableView);
    }

    /**
     * Configures the table columns and their data bindings
     * Each column is mapped to a property of the Movie class using PropertyValueFactory
     */
    private void setupTableColumns() {
        // ID column configuration
        TableColumn<Movie, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id")); // Binds to Movie.getId() method
        idCol.setPrefWidth(80); // Preferred column width in pixels

        // Title column configuration
        TableColumn<Movie, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title")); // Binds to Movie.getTitle()
        titleCol.setPrefWidth(200);

        // Genre column configuration
        TableColumn<Movie, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre")); // Binds to Movie.getGenre()
        genreCol.setPrefWidth(120);

        // Release year column configuration
        TableColumn<Movie, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year")); // Binds to Movie.getYear()
        yearCol.setPrefWidth(80);

        // Rating column configuration (0.0 to 10.0 scale)
        TableColumn<Movie, Double> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating")); // Binds to Movie.getRating()
        ratingCol.setPrefWidth(80);

        // Add all configured columns to the table view
        tableView.getColumns().addAll(idCol, titleCol, genreCol, yearCol, ratingCol);
    }

    /**
     * Filters the movie list based on search text
     * Performs case-insensitive search in movie titles and genres
     * @param searchText The text to search for in movie titles and genres
     */
    private void filterMovies(String searchText) {
        // If search text is empty or null, show all movies
        if (searchText == null || searchText.isEmpty()) {
            tableView.setItems(movieData);
            return;
        }

        // Create filtered list based on search criteria
        ObservableList<Movie> filteredList = FXCollections.observableArrayList();
        for (Movie movie : movieData) {
            // Check if title or genre contains search text (case-insensitive)
            if (movie.getTitle().toLowerCase().contains(searchText.toLowerCase()) ||
                    movie.getGenre().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(movie);
            }
        }
        tableView.setItems(filteredList); // Update table with filtered results
    }

    /**
     * Refreshes the movie data from MovieManager and updates the table view
     * Called initially and whenever data needs to be reloaded
     */
    public void refresh() {
        List<Movie> movies = movieManager.getAllMovies(); // Get current movie list
        movieData = FXCollections.observableArrayList(movies); // Convert to observable list for UI binding
        tableView.setItems(movieData); // Update table data
    }
}