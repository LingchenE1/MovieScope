import java.util.*;

public class RecommendationEngine {
    private final MovieManager movieManager;

    /**
     * Constructor initializes the recommendation engine with a movie manager
     * @param movieManager Reference to the movie management system for data access
     */
    public RecommendationEngine(MovieManager movieManager) {
        this.movieManager = movieManager;
    }

    /**
     * Generate movie recommendations for a specific user
     * @param user The user for whom to generate recommendations
     * @param n The number of recommendations to generate
     * @return List of recommended movies
     */
    public List<Movie> getRecommendations(User user, int n) {
        Map<String, Integer> genrePreferences = analyzeUserPreferences(user);
        List<Movie> allMovies = movieManager.getAllMovies();

        // Avoid recommending movies that have already been watched or are on the list
        List<Movie> candidateMovies = new ArrayList<>();
        for (Movie movie : allMovies) {
            if (!hasInteracted(user, movie.getId())) {
                candidateMovies.add(movie);
            }
        }

        if (candidateMovies.isEmpty()) {
            return getTopRatedMovies(Math.min(n, allMovies.size()));
        }

        // Calculate the recommendation score for each movie
        Map<Movie, Double> movieScores = new HashMap<>();
        for (Movie movie : candidateMovies) {
            double score = calculateRecommendationScore(movie, genrePreferences, user);
            movieScores.put(movie, score);
        }

        // Sort the candidate movies from the highest to the lowest based on the recommended scores
        candidateMovies.sort(new Comparator<>() {
            @Override
            public int compare(Movie m1, Movie m2) {
                return Double.compare(movieScores.get(m2), movieScores.get(m1));
            }
        });

        // Select the first N movies as the recommended results
        List<Movie> result = new ArrayList<>();
        for (int i = 0; i < Math.min(n, candidateMovies.size()); i++) {
            result.add(candidateMovies.get(i));
        }

        return result;
    }

    /**
     * Analyze user preferences based on viewing history and watchlist
     * @param user The user to analyze preferences for
     * @return Map containing genre preferences with weights
     */
    private Map<String, Integer> analyzeUserPreferences(User user) {
        Map<String, Integer> genreCount = new HashMap<>();

        // Analyzing the history
        List<String> history = user.getHistory();
        for (String record : history) {
            String movieId = record.split("@")[0];
            Movie movie = movieManager.getMovie(movieId);
            if (movie != null) {
                String genre = movie.getGenre();
                int count = genreCount.getOrDefault(genre, 0);
                genreCount.put(genre, count + 2); // The history has a relatively high weight
            }
        }

        // Analysis of the watchlist
        List<String> watchlist = user.getWatchlist();
        for (String movieId : watchlist) {
            Movie movie = movieManager.getMovie(movieId);
            if (movie != null) {
                String genre = movie.getGenre();
                int count = genreCount.getOrDefault(genre, 0);
                genreCount.put(genre, count + 1); // The watchlist has a relatively low weight
            }
        }

        return genreCount;
    }

    /**
     * Calculate the recommendation score for a single movie
     * @param movie The movie to calculate score for
     * @param genrePreferences User's genre preferences
     * @param user The user for whom recommendation is being made
     * @return Calculated recommendation score
     */
    private double calculateRecommendationScore(Movie movie, Map<String, Integer> genrePreferences,User user) {
        double score = movie.getRating(); // Base score: Movie rating (0 - 10 points)

        // Type preference bonus (0 - 5 points)
        String movieGenre = movie.getGenre();
        int genrePreference = genrePreferences.getOrDefault(movieGenre, 0);
        score += genrePreference * 0.5;

        // Year freshness bonus (0 - 1 point)
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        int yearsAgo = currentYear - movie.getYear();
        double recencyBonus = Math.max(0, 1.0 - yearsAgo / 20.0); // Movies released within the past 20 years will receive bonus points.
        score += recencyBonus;

        // Popular type rewards (to ensure diversity)
        if (genrePreferences.isEmpty()) {
            // New users: Give more points to less common types to encourage exploration
            if (isLessCommonGenre(movieGenre)) {
                score += 0.3;
            }
        }
        return score;
    }

    /**
     * Determine whether the movie type falls under the category of "rare types"
     * @param genre The genre to check
     * @return True if the genre is less common, false otherwise
     */
    private boolean isLessCommonGenre(String genre) {
        String[] commonGenres = {"Action", "Drama", "Comedy", "Thriller"};
        for (String commonGenre : commonGenres) {
            if (commonGenre.equals(genre)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determine whether the movie has been in the history or is on the watchlist
     * @param user The user to check interactions for
     * @param movieId The movie ID to check
     * @return True if user has interacted with the movie, false otherwise
     */
    private boolean hasInteracted(User user, String movieId) {
        // Check if it is in the history
        List<String> history = user.getHistory();
        for (String record : history) {
            if (record.startsWith(movieId + "@")) {
                return true;
            }
        }
        // Check if it is on the watchlist
        return user.isInWatchlist(movieId);
    }

    /**
     * Obtain the top-N highest-rated movies
     * @param n The number of top-rated movies to return
     * @return List of top-rated movies
     */
    private List<Movie> getTopRatedMovies(int n) {
        List<Movie> allMovies = movieManager.getAllMovies();

        // Sort by movie rating in descending order
        allMovies.sort(new Comparator<>() {
            @Override
            public int compare(Movie m1, Movie m2) {
                return Double.compare(m2.getRating(), m1.getRating());
            }
        });

        // Limit the number of returns
        List<Movie> result = new ArrayList<>();
        for (int i = 0; i < Math.min(n, allMovies.size()); i++) {
            result.add(allMovies.get(i));
        }

        return result;
    }
}