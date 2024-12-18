package trial;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * The UserHomePage class represents the main homepage UI for users or instructors.
 * It displays a welcome message and provides a logout button, allowing users to switch
 * back to the login page.
 */
public class UserHomePage extends Application {
    private CardPane cardPane;  // Pane to hold the welcome content
    private User user;  // Instance variable for the User object
    
    // Constructor that initializes the User instance with provided details
    public UserHomePage(User user) {
        this.user = user;
    }

    /**
     * Default constructor that creates a placeholder admin user.
     * This constructor initializes a user with default values.
     */
    public UserHomePage() {
        String tusername = "User";  // Default username
        Password pass = new Password();  // Placeholder password instance
        String temppass = " ";  // Temporary password value
        String role = "User";  // Default role
        this.user = new User(tusername, temppass, role);  // Initialize User with default values
    }

    /**
     * The start method sets up the stage for the user home page, including
     * initializing the layout and scene.
     *
     * @param primaryStage The main stage for the JavaFX application.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("User Homepage");  // Set the title of the window

        cardPane = new CardPane();  // Initialize the CardPane for the UI
        Scene scene = new Scene(cardPane, 400, 900);  // Sets the size of the window

        primaryStage.setScene(scene);  // Set the scene for the primary stage
        primaryStage.show();  // Display the window
    }

    /**
     * The CardPane class is a custom container that extends StackPane.
     * It contains the welcome panel and manages the layout for the user interface.
     */
    class CardPane extends StackPane {

        /**
         * Constructor for CardPane that initializes the layout
         * by adding the welcome panel to the pane.
         */
        public CardPane() {
            getChildren().add(createWelcomePanel());  // Add the welcome panel to the CardPane
        }

        /**
         * Creates the welcome panel UI that includes a welcome message and a logout button.
         * The logout button allows users to return to the login page.
         *
         * @return VBox The panel containing UI elements.
         */
        private VBox createWelcomePanel() {
            VBox panel = new VBox(10);  // VBox with spacing of 10 between elements
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");  // Set padding and alignment

            Label welcomeLabel = new Label("Welcome to User Homepage");  // Welcome message
            Button logoutButton = new Button("Logout");  // Button for logging out
            Button searchButton = new Button("Search Articles");  // Button to initiate article search

            // Set action for the logout button
            logoutButton.setOnAction(e -> {
                // Switch to the login page when logout is clicked
                Stage stage = (Stage) logoutButton.getScene().getWindow();  // Get the current stage (window)
                Driver loginPage = new Driver();  // Create an instance of the login page (Driver)
                
                try {
                    loginPage.start(stage);  // Start the login page
                } catch (Exception ex) {
                    ex.printStackTrace();  // Handle any exception that occurs during page transition
                }
            });
            
            // Action for the search button
            searchButton.setOnAction(e -> {
                // Switch to the search articles page
                Stage stage = (Stage) searchButton.getScene().getWindow();  // Get the current stage (window)
                Search search = new Search(null, null, user);  // Create an instance of the Search page
                
                try {
                    search.start(stage);  // Start the search page
                } catch (Exception ex) {
                    ex.printStackTrace();  // Handle any exception that occurs during page transition
                }
            });

            // Add the label and buttons to the panel
            panel.getChildren().addAll(welcomeLabel, logoutButton, searchButton);  // Add elements to the VBox
            return panel;  // Return the panel containing UI elements
        }
    }

    /**
     * The main method launches the JavaFX application by calling the launch method.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        launch(args);  // Launch the JavaFX application
    }
}
