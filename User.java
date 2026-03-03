import java.util.ArrayList;
import java.util.List;

//Represents a user in the system, containing username, password, watchlist, and viewing history
public class User {
    private final String username;
    private final String password;
    private final List<String> watchlist;
    private final List<String> history;

    //Creates a new user
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.watchlist = new ArrayList<>();     // Initialize the watchlist as an empty list
        this.history = new ArrayList<>();       // Initialize the history as an empty list
    }

    //Loads existing user data from strings.
    public User(String username, String password, String watchlistStr, String historyStr) {
        this(username, password);
        // Load the watchlist
        if (watchlistStr != null && !watchlistStr.isEmpty()) {
            String[] movies = watchlistStr.split(";");
            for (String movie : movies) {
                // Make sure that the imported movies are all non-empty strings.
                if (!movie.trim().isEmpty()) {
                    this.watchlist.add(movie.trim());
                }
            }
        }
        //Load the history
        if (historyStr != null && !historyStr.isEmpty()) {
            String[] records = historyStr.split(";");
            for (String record : records) {
                // Make sure that the imported histories are all non-empty strings.
                if (!record.trim().isEmpty()) {
                    this.history.add(record.trim());
                }
            }
        }
    }

    public String getUsername() { return username; }        // Get username
    public String getPassword() { return password; }        // Get password
    public List<String> getWatchlist() { return watchlist; }        // Get watchlist
    public List<String> getHistory() { return history; }        // Get history

    // Add movie to the watchlist
    public boolean addToWatchlist(String movieId) {
        // Successfully added returns true
        if (!watchlist.contains(movieId)) {
            watchlist.add(movieId);
            return true;
        }
        // If the movie is already in the watchlist, return false.
        return false;
    }

    // Remove the movie from the watchlist
    public boolean removeFromWatchlist(String movieId) {
        return watchlist.remove(movieId);
    }

    // Determine whether the movie is on the watchlist
    public boolean isInWatchlist(String movieId) {
        return watchlist.contains(movieId);
    }

    // Add records to the history and date
    public void addToHistory(String movieId) {
        String record = movieId + "@" + new java.util.Date();
        // Avoid adding the same record repeatedly
        if (!history.contains(record)) {
            history.add(record);
        }
        // If the movie is on the watchlist, it will be automatically removed.
        removeFromWatchlist(movieId);
    }

    // Convert user information into a CSV format string
    public String toCSVString() {
        String watchlistStr = String.join(";", watchlist);
        String historyStr = String.join(";", history);
        return String.format("%s,%s,%s,%s", username, password, watchlistStr, historyStr);
    }
}