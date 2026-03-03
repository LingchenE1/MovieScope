import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Callback;
import java.util.List;

/**
 * Movie Recommendation Panel
 * Provides personalized recommendations based on history and watchlist
 */
public class RecommendationPane extends VBox {
    private final MovieManager movieManager;  // Class for managing movie data
    private final UserManager userManager;    // Class for managing user data
    private final RecommendationEngine recommendationEngine;  // Recommendation Engine
    private ListView<Movie> listView;   // Used to display the recommended UI components
    private Spinner<Integer> countSpinner;  // UI component used to display user input

    /**
     * Constructor for RecommendationPane
     */
    public RecommendationPane(MovieManager movieManager, UserManager userManager, RecommendationEngine recommendationEngine) {
        this.movieManager = movieManager;
        this.userManager = userManager;
        this.recommendationEngine = recommendationEngine;
        initialize();
    }

    /**
     * Initializes the UI components and layout
     * Sets up labels, controls, and the recommendation list view
     */
    private void initialize() {
        setSpacing(15);
        setPadding(new Insets(10));

        // Main title label
        Label titleLabel = new Label("Movie Recommendations");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Description label
        Label descLabel = new Label("Get personalized movie recommendations based on your watch history and preferences");
        descLabel.setWrapText(true);

        // Control panel for selecting number of recommendations
        HBox controlBox = new HBox(10);
        controlBox.setAlignment(Pos.CENTER_LEFT);

        Label countLabel = new Label("Number of recommendations:");
        countSpinner = new Spinner<>(1, 20, 5);
        countSpinner.setPrefWidth(80);

        // Button for recommendation generation
        Button generateButton = new Button("Generate Recommendations");
        generateButton.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white;");
        generateButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                generateRecommendations();
            }
        });

        controlBox.getChildren().addAll(countLabel, countSpinner, generateButton);

        // ListView for displaying recommended movies
        listView = new ListView<>();
        listView.setPrefHeight(400);

        // Display movie information
        listView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Movie> call(ListView<Movie> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Movie movie, boolean empty) {
                        super.updateItem(movie, empty);
                        if (empty || movie == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            // Create a custom layout for each movie item
                            VBox content = new VBox(5);
                            Label titleLabel = new Label(movie.getTitle());
                            titleLabel.setStyle("-fx-font-weight: bold;");

                            String detailsText = String.format("Genre: %s | Year: %d | Rating: %.1f",
                                    movie.getGenre(), movie.getYear(), movie.getRating());
                            Label detailsLabel = new Label(detailsText);
                            detailsLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 11px;");

                            content.getChildren().addAll(titleLabel, detailsLabel);
                            setGraphic(content);
                        }
                    }
                };
            }
        });

        // Add all components to the main pane
        getChildren().addAll(titleLabel, descLabel, controlBox, listView);
    }

    /**
     * Get the recommended movies and display them.
     */
    private void generateRecommendations() {
        int count = countSpinner.getValue();

        // Get recommendations
        List<Movie> recommendations = recommendationEngine.getRecommendations(
                userManager.getCurrentUser(), count);

        // Update the ListView
        listView.setItems(FXCollections.observableArrayList(recommendations));

        // If no recommendations are available, show message "No recommendations available. Try watching more movies first!"
        if (recommendations.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText(null);
            alert.setContentText("No recommendations available. Try watching more movies first!");
            alert.showAndWait();
        }
    }

    /**
     * Refresh the recommendation list
     */
    public void refresh() {
        generateRecommendations();
    }
}