package trial; 
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;
import java.util.Scanner;

/**
 * The ArticleHomePage class extends the JavaFX Application class and serves as the main interface
 * for managing articles in the system. It initializes the primary stage and manages navigation 
 * between various functionalities like creating, deleting, updating, and listing articles.
 */
public class ArticleHomePage extends Application {

    private CardPane cardPane; // A custom pane used to manage and display different panels
    private Database db = Driver.getDb(); // Accessing the shared Database instance from Driver
    private Admin admin; // Reference to the Admin user
    private Instructor instructor; // Reference to the Instructor user
    
    // Constructor that initializes the ArticleHomePage with admin and instructor parameters.
    // Passes null for admin or instructor if the user is not using that role in the current session.
    public ArticleHomePage(Admin admin, Instructor instructor) {
        this.admin = admin; // Assigning the admin instance
        this.instructor = instructor; // Assigning the instructor instance
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Articles"); // Setting the title for the primary stage

        cardPane = new CardPane(); // Initialize the custom card pane to manage different panels
        Scene scene = new Scene(cardPane, 400, 900); // Set the dimensions for the scene
        
        primaryStage.setScene(scene); // Assign the scene to the primary stage
        primaryStage.show(); // Display the primary stage
    }

    /**
     * Inner class representing the main pane that holds different panels and allows navigation 
     * between them.
     */
    class CardPane extends StackPane {
    	
        /**
         * Constructor for CardPane. Sets the initial panel displayed to the welcome panel.
         */
        public CardPane() {
            getChildren().add(createArticlePanel()); // Load the welcome panel initially
        }

        /**
         * Creates the welcome panel which includes buttons for creating, deleting, updating, 
         * listing, and searching for articles. This panel is the main interface for article management.
         * @return VBox containing the welcome panel UI
         */
        private VBox createArticlePanel() {
            VBox panel = new VBox(10); // Create a vertical box layout with 10 pixels of spacing
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;"); // Set padding and alignment for the panel

            // Create buttons for various article management functionalities
            Button createButton = new Button("Create Articles");
            Button deleteButton = new Button("Delete Articles");
            Button updateButton = new Button("Update Articles");
            Button listButton = new Button("List Articles");
            Button searchButton = new Button("Search Articles");
            Button returnButton = new Button("Exit");
            
            // Action for creating an article
            createButton.setOnAction(e -> {
            	getChildren().clear(); // Clear current children from the pane
            	getChildren().add(createArticle()); // Load the create article panel
            });

            // Action for deleting an article
            deleteButton.setOnAction(e -> {
            	getChildren().clear(); // Clear current children from the pane
            	getChildren().add(deleteArticle()); // Load the delete article panel
            });
            
            // Action for updating articles
            updateButton.setOnAction(e -> {
            	getChildren().clear(); // Clear current children from the pane
            	getChildren().add(updateArticles()); // Load the update articles panel
            });

            // Action for listing articles
            listButton.setOnAction(e -> {
            	getChildren().clear(); // Clear current children from the pane
            	getChildren().add(listArticles()); // Load the list articles panel
            });

            // Action for exiting the application or returning to the login page
            returnButton.setOnAction(e -> {
            	if(admin != null) { // Check if an admin is logged in
                    // Switch to the admin home page when logout is clicked
                    Stage stage = (Stage) returnButton.getScene().getWindow(); // Get the current stage (window)
                    AdminHomeScreen adminPage = new AdminHomeScreen(); // Create an instance of the admin home screen
                    try {
                        adminPage.start(stage); // Start the admin home page
                    } catch (Exception ex) {
                        ex.printStackTrace(); // Print any exception that occurs during page transition
                    }
            	} else { // If no admin, assume instructor is logged in
                    // Switch to the instructor home page when logout is clicked
                    Stage stage = (Stage) returnButton.getScene().getWindow(); // Get the current stage (window)
                    InstructorHomePage instructorPage = new InstructorHomePage(); // Create an instance of the instructor home screen
                    try {
                        instructorPage.start(stage); // Start the instructor home page
                    } catch (Exception ex) {
                        ex.printStackTrace(); // Print any exception that occurs during page transition
                    }
            	}
            });
            
            // Action for searching articles
            searchButton.setOnAction(e -> {
                // Switch to the search panel when search button is clicked
                Stage stage = (Stage) searchButton.getScene().getWindow(); // Get the current stage (window)
                Search searchScreen = new Search(admin, instructor, null); // Create an instance of the search screen
                try {
                	searchScreen.start(stage); // Start the search panel
                } catch (Exception ex) {
                    ex.printStackTrace(); // Print any exception that occurs during page transition
                }
            });
            
            // Add all created buttons to the panel
            panel.getChildren().addAll(createButton, deleteButton, updateButton, listButton, searchButton, returnButton);
            
            return panel; // Return the configured panel
        }

        /**
         * Creates the panel for creating a new article with input fields for the article's details.
         * @return VBox containing the article creation panel UI
         */
        private VBox createArticle() {
        	VBox panel = new VBox(10); // Vertical box layout with 10 pixels of spacing
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;"); // Set padding and alignment for the panel

            // Input fields for article details
            TextField titleField = new TextField(); // Title input field
            TextField levelField = new TextField(); // Level input field
            TextField descriptionField = new TextField(); // Description input field
            TextField keywordField = new TextField(); // Keywords input field
            TextField bodyField = new TextField(); // Body content input field
            TextField referenceField = new TextField(); // References input field
            TextField groupsField = new TextField(); // Groups input field
            Button submitButton = new Button("Create Article"); // Button to submit article creation

            // Add labels and input fields to the panel
            panel.getChildren().addAll(
                new Label("Title:"), titleField,
                new Label("Level:"), levelField,
                new Label("Description:"), descriptionField,
                new Label("Key words:"), keywordField,
                new Label("Body:"), bodyField,
                new Label("References:"), referenceField,
                new Label("Groups:"), groupsField,
                submitButton // Include the submit button at the end
            );

            // Action for the submit button
            submitButton.setOnAction(e -> {
                // Retrieve and process data from the text fields
                String title = titleField.getText(); // Get the title from the input field
                String level = levelField.getText(); // Get the level from the input field
                String description = descriptionField.getText(); // Get the description from the input field
                String body = bodyField.getText(); // Get the body content from the input field

                // Parse comma-separated fields into lists
                List<String> keywords = List.of(keywordField.getText().split("\\s*,\\s*")); // Split keywords
                List<String> references = List.of(referenceField.getText().split("\\s*,\\s*")); // Split references
                List<String> groups = List.of(groupsField.getText().split("\\s*,\\s*")); // Split groups

                // Create a new HelpArticle object with the collected data
                HelpArticle article = new HelpArticle(
                    title,
                    level,
                    description,
                    keywords,
                    body,
                    references,
                    groups
                );
                
                // Add the new article to the database
                db.addHelpArticle(article);

                // Show a success alert indicating that the article was created
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Article created! : "  + article.getTitle());
                alert.showAndWait(); // Wait for the user to acknowledge the alert
                System.out.println(article.getArticleId()); // Log the ID of the newly created article
                
                // Clear current pane and reload the article panel
                getChildren().clear();
                getChildren().add(createArticlePanel());
            });

            return panel; // Return the article creation panel
        }

        /**
         * Creates the panel for deleting an article based on its ID.
         * @return VBox containing the article deletion panel UI
         */
        private VBox deleteArticle() {
            VBox panel = new VBox(10); // Vertical box layout with 10 pixels of spacing
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;"); // Set padding and alignment for the panel
            
            // Buttons and fields for deleting an article
            Button deleteButton = new Button("Delete Article");
            TextField articleIdField = new TextField(); // Field to input the article ID for deletion

            // Action for the delete button
            deleteButton.setOnAction(e -> {
                // Get the article ID from the input field and attempt to delete the article
                int id = Integer.parseInt(articleIdField.getText()); // Parse the article ID
                boolean deleted = db.deleteHelpArticle(id); // Call the delete function in the database

                // Show an alert based on the success of the deletion
                Alert alert = deleted ? new Alert(Alert.AlertType.INFORMATION, "Article deleted!")
                                      : new Alert(Alert.AlertType.ERROR, "Article not found!"); 
                alert.showAndWait(); // Wait for the user to acknowledge the alert

                // Clear current pane and reload the article panel
                getChildren().clear();
                getChildren().add(createArticlePanel());
            });

            // Add input fields and buttons to the panel
            panel.getChildren().addAll(
                new Label("Article ID:"), articleIdField, 
                deleteButton // Include the delete button
            );

            return panel; // Return the article deletion panel
        }

        /**
         * Creates the panel for updating an existing article.
         * @return VBox containing the article update panel UI
         */
        private VBox updateArticles() {
            VBox panel = new VBox(10); // Vertical box layout with 10 pixels of spacing
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;"); // Set padding and alignment for the panel
            
            // Input fields for article update details
            TextField articleIdField = new TextField(); // Field to input the article ID for update
            TextField titleField = new TextField(); // Field for new title
            TextField levelField = new TextField(); // Field for new level
            TextField descriptionField = new TextField(); // Field for new description
            Button updateButton = new Button("Update Article"); // Button to submit article updates

            // Action for the update button
            updateButton.setOnAction(e -> {
                // Get the article ID and new details from the input fields
                int id = Integer.parseInt(articleIdField.getText()); // Parse the article ID
                String title = titleField.getText(); // Get the new title
                String level = levelField.getText(); // Get the new level
                String description = descriptionField.getText(); // Get the new description

                // Attempt to update the article in the database
                boolean updated = db.updateHelpArticle(id, title, level, description); // Call the update function

                // Show an alert based on the success of the update
                Alert alert = updated ? new Alert(Alert.AlertType.INFORMATION, "Article updated!")
                                      : new Alert(Alert.AlertType.ERROR, "Article not found!"); 
                alert.showAndWait(); // Wait for the user to acknowledge the alert
                
                // Clear current pane and reload the article panel
                getChildren().clear();
                getChildren().add(createArticlePanel());
            });

            // Add input fields and button to the panel
            panel.getChildren().addAll(
                new Label("Article ID:"), articleIdField,
                new Label("New Title:"), titleField,
                new Label("New Level:"), levelField,
                new Label("New Description:"), descriptionField,
                updateButton // Include the update button
            );

            return panel; // Return the article update panel
        }

        /**
         * Creates the panel for listing all articles stored in the database.
         * @return VBox containing the article listing panel UI
         */
        private VBox listArticles() {
            VBox panel = new VBox(10); // Vertical box layout with 10 pixels of spacing
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;"); // Set padding and alignment for the panel
            
            // Button to refresh the list of articles
            Button refreshButton = new Button("Refresh Articles");
            TextArea textArea = new TextArea(); // Text area to display article details
            textArea.setEditable(false); // Make the text area read-only

            // Action for the refresh button
            refreshButton.setOnAction(e -> {
                // Retrieve the list of articles from the database and display them
                List<HelpArticle> articles = db.listHelpArticles(); // Call the list function from the database
                StringBuilder sb = new StringBuilder(); // StringBuilder to accumulate article details

                // Iterate over articles and append their details to the StringBuilder
                for (HelpArticle article : articles) {
                    sb.append("ID: ").append(article.getArticleId()).append("\n")
                      .append("Title: ").append(article.getTitle()).append("\n")
                      .append("Level: ").append(article.getLevel()).append("\n")
                      .append("Description: ").append(article.getDescription()).append("\n")
                      .append("Keywords: ").append(String.join(", ", article.getKeywords())).append("\n")
                      .append("References: ").append(String.join(", ", article.getReferences())).append("\n")
                      .append("Groups: ").append(String.join(", ", article.getGroups())).append("\n\n");
                }
                
                textArea.setText(sb.toString()); // Set the text area to display the accumulated details
            });

            // Add the refresh button and text area to the panel
            panel.getChildren().addAll(refreshButton, textArea); // Include the refresh button and text area

            return panel; // Return the article listing panel
        }
    }

    // Main method to launch the application
    public static void main(String[] args) {
        launch(args); // Start the JavaFX application
    }
}
