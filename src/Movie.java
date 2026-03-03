// Document 2:
// Represents a Movie with basic properties
public class Movie {
    private final String id;        // Unique identifier for the movie
    private final String title;     // Title of the movie
    private final String genre;     // Genre of the movie
    private final int year;         // Release year
    private final double rating;    // Rating score

    // Constructor to initialize all movie properties
    public Movie(String id, String title, String genre, int year, double rating) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.rating = rating;
    }

    // Getters
    public String getId() { return id; }           // Returns movie ID
    public String getTitle() { return title; }     // Returns movie title
    public String getGenre() { return genre; }     // Returns movie genre
    public int getYear() { return year; }          // Returns release year
    public double getRating() { return rating; }  // Returns rating

    // Returns formatted string representation of the movie
    @Override
    public String toString() {
        return String.format("%s | %s | %s | %d | %.1f", id, title, genre, year, rating);
    }
}