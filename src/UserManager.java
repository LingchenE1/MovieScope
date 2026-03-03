import java.io.*;
import java.util.*;

//User management class responsible for user registration, login, data persistence
public class UserManager {
    private final Map<String, User> users;        //Map to store all users, with username as the key
    private User currentUser;       //Currently logged-in user
    private final String userDataFile;        //Path to the user data file

    //Initialize users, currentUser and userDataFile
    public UserManager(String userDataFile) {
        this.users = new HashMap<>();
        this.currentUser = null;
        this.userDataFile = userDataFile;
        loadUsersFromCSV();
    }

    //Load user data from the CSV file into the "users"
    //CSV file format: username, password, watchlist, history
    public void loadUsersFromCSV() {
        try (Scanner scanner = new Scanner(new File(userDataFile))) {
            boolean firstLine = true;
            // Loop through each line of the file until it is completely read
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                //Split CSV row by ",", max 4 parts
                String[] parts = line.split(",", 4);
                if (parts.length >= 4) {
                    String username = parts[0].trim();
                    String password = parts[1].trim();
                    String watchlist = parts[2].trim();
                    String history = parts[3].trim();

                    //Store the user data in the "users"
                    users.put(username, new User(username, password, watchlist, history));
                }
            }
            System.out.println("Successfully loaded " + users.size() + " users");
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    //Save all current user data to a CSV file
    public void saveUsersToCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(userDataFile))) {
            //Write CSV header line
            pw.println("username,password,watchlist,history");
            //Convert each user to the CSV string and write it to the file.
            for (User user : users.values()) {
                pw.println(user.toCSVString());
            }
        } catch (IOException e) {
            System.out.println("Unable to save user data: " + e.getMessage());
        }
    }

    //Users login
    public boolean login(String username, String password) {
        //Check whether the username exists in the "users"
        if (users.containsKey(username)) {
            User user = users.get(username);
            //Verify whether the passwords match
            if (user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    //User logout
    public void logout() {
        saveUsersToCSV();       // Save data before logging out
        currentUser = null;
    }

    //User registration
    public boolean register(String username, String password) {
        //Check whether the username has already been taken
        if (users.containsKey(username)) {
            return false;
        }
        //Create a new User object and store it in the users collection
        users.put(username, new User(username, password));
        saveUsersToCSV();
        return true;
    }

    // Getters
    public boolean isLoggedIn() { return currentUser != null; }
    public User getCurrentUser() { return currentUser; }
    public Map<String, User> getUsers() { return users; }
}