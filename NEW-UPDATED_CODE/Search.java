package trial;

import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Search extends Application {
    private CardPane cardPane;  // Pane to hold the search interface
    private Database db = Driver.getDb();  // Database instance for querying articles
    private Admin admin;  // Admin user instance
    private Instructor instructor;  // Instructor user instance
    private User user;  // Regular user instance

    // Constructor to initialize user roles
    public Search(Admin admin, Instructor instructor, User user) {
        this.admin = admin;
        this.instructor = instructor;
        this.user = user;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Articles");  // Set the title of the window

        cardPane = new CardPane();  // Create the card pane for search interface
        Scene scene = new Scene(cardPane, 400, 900);  // Create scene with specified dimensions
        
        primaryStage.setScene(scene);  // Set the scene for the primary stage
        primaryStage.show();  // Show the primary stage
    }

    // Inner class representing the search card pane
    class CardPane extends StackPane {
        
        public CardPane() {
            getChildren().add(searchArticles());  // Add the search articles UI to the pane
        }

        // Method to create the search articles UI
        private VBox searchArticles() {
            VBox panel = new VBox(10);  // Create a vertical box for layout with spacing
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");  // Style the panel
            
            // Return button setup to exit search
            Button returnButton = new Button("Exit");
            returnButton.setOnAction(e -> {
                Stage stage = (Stage) returnButton.getScene().getWindow();  // Get current window
                if (admin != null) {  // Check if the current user is admin
                    AdminHomeScreen adminPage = new AdminHomeScreen();  // Load admin home screen
                    try {
                        adminPage.start(stage);  // Start admin page
                    } catch (Exception ex) {
                        ex.printStackTrace();  // Print stack trace on exception
                    }
                } else {  // Load instructor home screen if not admin
                    InstructorHomePage instructorPage = new InstructorHomePage();
                    try {
                        instructorPage.start(stage);  // Start instructor page
                    } catch (Exception ex) {
                        ex.printStackTrace();  // Print stack trace on exception
                    }
                }
            });

            // Search fields for Article ID, Title, and Group
            Label idLabel = new Label("Search by Article ID:");
            TextField idField = new TextField();
            idField.setPromptText("Enter article ID");  // Set placeholder for ID field

            Label titleLabel = new Label("Search by Title:");
            TextField titleField = new TextField();
            titleField.setPromptText("Enter article title");  // Set placeholder for title field

            Label groupLabel = new Label("Search by Group:");
            TextField groupField = new TextField();
            groupField.setPromptText("Enter group names, separated by commas");  // Set placeholder for group field

            Button searchButton = new Button("Search Articles");  // Button to initiate search

            // VBox for displaying search results
            VBox articleDisplay = new VBox(5);  // Create a vertical box for article results
            articleDisplay.setStyle("-fx-padding: 10; -fx-background-color: #f9f9f9; -fx-border-color: #ccc;");

            // Action for search button
            searchButton.setOnAction(e -> {
                String idInput = idField.getText().trim();  // Get input from ID field
                long id = -1;  // Default to -1 to skip ID-based search if no ID is entered
                if (!idInput.isEmpty()) {  // Validate ID input
                    try {
                        id = Long.parseLong(idInput);  // Parse ID input to long
                    } catch (NumberFormatException ex) {
                        articleDisplay.getChildren().clear();  // Clear previous results
                        articleDisplay.getChildren().add(new Label("Invalid ID format. Please enter a numeric ID."));  // Display error
                        return;  // Stop execution if ID format is invalid
                    }
                }

                String titleInput = titleField.getText().trim();  // Get title input
                String groupInput = groupField.getText().trim();  // Get group input
                List<String> groupNames = groupInput.isEmpty() ? null : List.of(groupInput.split(","));  // Split groups by comma

                articleDisplay.getChildren().clear();  // Clear previous results

                // Fetch articles from the database using multiple search criteria
                List<HelpArticle> articles = db.searchHelpArticles(id, titleInput, groupNames);

                if (articles.isEmpty()) {  // Check if no articles were found
                    articleDisplay.getChildren().add(new Label("No articles found for the specified criteria."));  // Display message
                } else {
                    // Loop through and display each article found
                    for (HelpArticle article : articles) {
                        Label idResultLabel = new Label("ID: " + article.getArticleId());  // Display article ID
                        Label titleResultLabel = new Label("Title: " + article.getTitle());  // Display article title
                        Label levelLabel = new Label("Level: " + article.getLevel());  // Display article level
                        Label descriptionLabel = new Label("Description: " + article.getShortDescription());  // Display article description
                        Label keywordsLabel = new Label("Key Words: " + article.getKeywords());  // Display article keywords
                        Label referencesLabel = new Label("References: " + article.getReferences());  // Display article references
                        Label groupsLabel = new Label("Groups: " + article.getGroups());  // Display article groups
                        VBox articleBox = new VBox(3, idResultLabel, titleResultLabel, levelLabel, descriptionLabel, keywordsLabel, referencesLabel, groupsLabel);  // Create a box for each article
                        articleBox.setStyle("-fx-padding: 5; -fx-background-color: #e6e6e6; -fx-border-color: #ccc;");  // Style article box
                        
                        articleDisplay.getChildren().add(articleBox);  // Add article box to display
                    }
                }
            });

            // Add all UI elements to the panel
            panel.getChildren().addAll(returnButton, idLabel, idField, titleLabel, titleField, groupLabel, groupField, searchButton, articleDisplay);
            return panel;  // Return the completed search panel
        }
    }

    public static void main(String[] args) {
        launch(args);  // Launch the JavaFX application
    }
}
