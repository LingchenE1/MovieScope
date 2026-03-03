// Document 1:
import java.io.*;
import java.util.*;

public class MovieManager {
    private final Map<String, Movie> movies; // Stores movies with their ID as key

    //Use HashMap for storing movies
    public MovieManager() {
        this.movies = new HashMap<>(); // Initialize the movies map
    }

    // Loads movie data from a CSV file and populates the movies map
    public void loadMoviesFromCSV(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) { // try-with-resources ensures reader is closed
            String line;
            boolean firstLine = true; // Flag to skip header row

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;  //Skip the CSV header row
                    continue;
                }

                String[] parts = line.split(","); // Split CSV line into columns
                if (parts.length >= 5) { // Ensure line has enough data fields
                    try {
                        String id = parts[0].trim();
                        String title = parts[1].trim();
                        String genre = parts[2].trim();
                        int year = Integer.parseInt(parts[3].trim()); // Parse year as integer
                        double rating = Double.parseDouble(parts[4].trim()); // Parse rating as double

                        movies.put(id, new Movie(id, title, genre, year, rating)); // Add new Movie to map

                        //exception handling
                    } catch (NumberFormatException e) {
                        System.out.println("Data format error: " + line); // Handle number parsing errors
                    }
                }
            }
            System.out.println("Successfully loaded " + movies.size() + " movies"); // Load completion message
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage()); // Handle file not found
        } catch (SecurityException e) {
            System.out.println("Security error: " + e.getMessage()); // Handle security restrictions
        } catch (IOException e) {
            System.out.println("IO error occurred while reading file: " + e.getMessage()); // Handle IO errors
        } catch (Exception e) {
            System.out.println("Unknown error occurred: " + e.getMessage()); // Handle any other exceptions
        }
    }

    // Returns all movies as a List
    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies.values()); // Convert map values to ArrayList
    }

    // Retrieves a specific movie by its ID
    public Movie getMovie(String id) {
        return movies.get(id); //Get directly from the HashMap by ID
    }
}