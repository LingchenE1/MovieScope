import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;

/**
 * HistoryPane class provides a GUI panel for displaying user's movie viewing history
 * Shows watched movies with additional information like watch date
 * Implements the viewing history feature required by the specification
 */
public class HistoryPane extends VBox {
    private final MovieManager movieManager;  // Handles movie data retrieval
    private final UserManager userManager;    // Manages user data and history operations
    private TableView<HistoryRecord> tableView; // Table to display history records
    private ObservableList<HistoryRecord> historyData; // Observable list for history data

    /**
     * Constructor initializes the history panel with required managers
     * @param movieManager For accessing movie details
     * @param userManager For accessing user's history data
     */
    public HistoryPane(MovieManager movieManager, UserManager userManager) {
        this.movieManager = movieManager;
        this.userManager = userManager;
        initialize(); // Set up the UI components
    }

    /**
     * Initializes the UI components and layout for history display
     */
    private void initialize() {
        setSpacing(10); // Vertical spacing between components
        setPadding(new javafx.geometry.Insets(10)); // Internal padding

        Label titleLabel = new Label("Viewing History");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Create table view for displaying history records
        tableView = new TableView<>();
        setupTableColumns(); // Configure table columns

        refresh(); // Load initial history data

        getChildren().addAll(titleLabel, tableView); // Add components to panel
    }

    /**
     * Configures the table columns for history records
     * Each column binds to a property of the HistoryRecord helper class
     */
    private void setupTableColumns() {
        // Create and configure table columns
        TableColumn<HistoryRecord, String> titleCol = createColumn("Title", "title", 200);
        TableColumn<HistoryRecord, String> genreCol = createColumn("Genre", "genre", 120);
        TableColumn<HistoryRecord, Integer> yearCol = createColumn("Year", "year", 80);
        TableColumn<HistoryRecord, Double> ratingCol = createColumn("Rating", "rating", 80);
        TableColumn<HistoryRecord, String> dateCol = createColumn("Watched Date", "date", 120);

        // Add columns individually to avoid varargs warning
        tableView.getColumns().add(titleCol);
        tableView.getColumns().add(genreCol);
        tableView.getColumns().add(yearCol);
        tableView.getColumns().add(ratingCol);
        tableView.getColumns().add(dateCol);
    }

    /**
     * Helper method to create configured table columns
     */
    private <T> TableColumn<HistoryRecord, T> createColumn(String header, String property, int width) {
        TableColumn<HistoryRecord, T> column = new TableColumn<>(header);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setPrefWidth(width);
        return column;
    }

    /**
     * Refreshes the history data from current user and updates the table
     * Parses history records and fetches corresponding movie details
     */
    public void refresh() {
        // Get raw history records from current user (format: "movieId@date")
        List<String> historyRecords = userManager.getCurrentUser().getHistory();
        List<HistoryRecord> historyList = new ArrayList<>();

        // Process each history record
        for (String record : historyRecords) {
            // Split record into movie ID and watch date (separated by @ symbol)
            String[] parts = record.split("@");
            String movieId = parts[0]; // First part is always the movie ID
            // If date exists, use it; otherwise show "Unknown date"
            String date = parts.length > 1 ? parts[1] : "Unknown date";

            // Fetch complete movie details using movie ID
            Movie movie = movieManager.getMovie(movieId);
            if (movie != null) {
                // Create HistoryRecord with movie details and watch date
                historyList.add(new HistoryRecord(movie, date));
            }
        }

        // Convert to observable list and update table
        historyData = FXCollections.observableArrayList(historyList);
        tableView.setItems(historyData);
    }

    /**
     * Helper class to represent a history record for table display
     * Combines Movie object with watch date information
     * This class is used exclusively for table data binding
     */
    public static class HistoryRecord {
        private final Movie movie;  // The movie that was watched
        private final String date;  // Date when the movie was watched

        /**
         * Constructor creates a new history record
         * @param movie The movie that was watched
         * @param date The date when it was watched
         */
        public HistoryRecord(Movie movie, String date) {
            this.movie = movie;
            this.date = date;
        }

        // The following getter methods are used by PropertyValueFactory for table binding

        public String getTitle() { return movie.getTitle(); }
        public String getGenre() { return movie.getGenre(); }
        public int getYear() { return movie.getYear(); }
        public double getRating() { return movie.getRating(); }
        public String getDate() { return date; }
    }
}