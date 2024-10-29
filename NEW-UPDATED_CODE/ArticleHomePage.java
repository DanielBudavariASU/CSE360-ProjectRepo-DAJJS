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
 * The Driver class that extends the JavaFX Application class and serves as the entry point
 * for the Login System. This class initializes the primary stage, sets up the initial scene,
 * and provides navigation between different panels like login, account creation, etc.
 */
public class ArticleHomePage extends Application {

    private CardPane cardPane; // Custom pane to hold different panels
    private Database db = Driver.getDb();
    private Admin admin;
    private Instructor instructor;
    
    //pass admin or instructor as null if user is not using that role in that session
    public ArticleHomePage(Admin admin, Instructor instructor) {
        this.admin = admin;
        this.instructor = instructor;
        
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Articles");

        cardPane = new CardPane(); // Initialize the custom card pane
        Scene scene = new Scene(cardPane, 400, 900); // Set scene size
        
        primaryStage.setScene(scene);
        primaryStage.show(); // Display the stage
    }

    /**
     * Inner class representing the main pane that holds different panels and allows navigation between them.
     */
    class CardPane extends StackPane {
    	
        /**
         * Constructor for CardPane. Sets the initial welcome panel.
         */
        public CardPane() {
            getChildren().add(createArticlePanel()); // Initialize with the welcome panel
        }

        /**
         * Creates the welcome panel with options to create an account or login.
         * If no admin is present in the system, forces the creation of an admin account.
         * @return VBox containing the welcome panel UI
         */
        private VBox createArticlePanel() {
            VBox panel = new VBox(10); // Vertical box layout
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            // Buttons
            Button createButton = new Button("Create Articles");
            Button deleteButton = new Button("Delete Articles");
            Button updateButton = new Button("Update Articles");
            Button listButton = new Button("List Articles");
            Button searchButton = new Button("Search Articles");
            Button returnButton = new Button("Exit");
            

            createButton.setOnAction(e -> {
            	getChildren().clear();
            	getChildren().add(createArticle());
            });

            // Reset Password Button Action
            deleteButton.setOnAction(e -> {
            	getChildren().clear();
            	getChildren().add(deleteArticle());	
                
            });
            
            updateButton.setOnAction(e -> {
            	getChildren().clear();
            	getChildren().add(updateArticles());
                
            });

            // Delete User Button Action
            listButton.setOnAction(e -> {
            	getChildren().clear();
            	getChildren().add(listArticles());
            });

            // Logout Button Action
            returnButton.setOnAction(e -> {
            	if(admin != null)
            	{
            		// Switch to the login page when logout is clicked
                    Stage stage = (Stage) returnButton.getScene().getWindow();  // Get the current stage (window)
                    AdminHomeScreen adminPage = new AdminHomeScreen();  // Create an instance of the login page (Driver)
                    
                    try {
                        adminPage.start(stage);  // Start the login page
                    } catch (Exception ex) {
                        ex.printStackTrace();  // Handle any exception that occurs during page transition
                    }
            	}
            	else
            	{
            		// Switch to the login page when logout is clicked
                    Stage stage = (Stage) returnButton.getScene().getWindow();  // Get the current stage (window)
                    InstructorHomePage instructorPage = new InstructorHomePage();  // Create an instance of the login page (Driver)
                    
                    try {
                        instructorPage.start(stage);  // Start the login page
                    } catch (Exception ex) {
                        ex.printStackTrace();  // Handle any exception that occurs during page transition
                    }
            	}
            });
            
         // Set action for the logout button
            searchButton.setOnAction(e -> {
                // Switch to the login page when logout is clicked
                Stage stage = (Stage) searchButton.getScene().getWindow();  // Get the current stage (window)
                Search searchScreen = new Search(admin, instructor, null);  // Create an instance of the login page (Driver)
                
                try {
                	searchScreen.start(stage);  // Start the login page
                } catch (Exception ex) {
                    ex.printStackTrace();  // Handle any exception that occurs during page transition
                }
            });
            
            panel.getChildren().addAll(createButton, deleteButton, updateButton, listButton, searchButton, returnButton);
            
            return panel;
        }


        /**
         * Creates the signup panel for creating a user or admin account based on the role provided.
         * @param role The role to be assigned to the new account (admin or user)
         * @return VBox containing the signup panel UI
         */
        private VBox createArticle() {
        	VBox panel = new VBox(10); // Vertical box layout
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            TextField titleField = new TextField();
            TextField levelField = new TextField();
            TextField descriptionField = new TextField();
            TextField keywordField = new TextField();
            TextField bodyField = new TextField();
            TextField referenceField = new TextField();
            TextField groupsField = new TextField();
            Button submitButton = new Button("Create Article");

            // Add input fields for account creation
            panel.getChildren().addAll(
                new Label("Title:"), titleField,
                new Label("Level:"), levelField,
                new Label("Description:"), descriptionField,
                new Label("Key words:"), keywordField,
                new Label("Body:"), bodyField,
                new Label("References:"), referenceField,
                new Label("Groups:"), groupsField,
                submitButton
            );

            submitButton.setOnAction(e -> {
                // Retrieve and process data from text fields
                String title = titleField.getText();
                String level = levelField.getText();
                String description = descriptionField.getText();
                String body = bodyField.getText();

                // Parse comma-separated fields into lists
                List<String> keywords = List.of(keywordField.getText().split("\\s*,\\s*"));
                List<String> references = List.of(referenceField.getText().split("\\s*,\\s*"));
                List<String> groups = List.of(groupsField.getText().split("\\s*,\\s*"));

                // Create a new HelpArticle object
                HelpArticle article = new HelpArticle(
                    title,
                    level,
                    description,
                    keywords,
                    body,
                    references,
                    groups
                );
                
                db.addHelpArticle(article);

                // Add code here to handle the newly created article (e.g., save it or display a success message)
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Article created! : "  + article.getTitle());
                alert.showAndWait();
                System.out.println(article.getArticleId());
                
                getChildren().clear();
                getChildren().add(createArticlePanel());
                
            });

            return panel;
        }

        /**
         * Creates the login panel where users can enter their username and password.
         * @return VBox containing the login panel UI
         */
        private VBox deleteArticle() {
            VBox panel = new VBox(10); // Vertical box layout
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");
            
            Button deleteButton = new Button("Delete Article"); // Button to reset password
            Button cancelButton = new Button("Cancel"); // Button to login
            TextField idField = new TextField(); // Username input
       
            
            deleteButton.setOnAction(e -> {
            	try {
                    // Convert the text field input to a long
                    long id = Long.parseLong(idField.getText());
                    
                    if(db.findHelpArticleById(id) == null)
                    {
                    	Alert alert = new Alert(Alert.AlertType.ERROR, "Article not found");
                        alert.showAndWait();
                        getChildren().clear();
                        getChildren().add(createArticlePanel());
   
                    }
                    else {
                    	// Your code for deleting the article with this ID goes here
                    	System.out.println("Article ID to delete: " + id);
                    	db.removeHelpArticle(id);

                    	if(db.findHelpArticleById(id) == null)
                    	{
                    		Alert alert = new Alert(Alert.AlertType.INFORMATION, "Article deleted successfully");
                    		alert.showAndWait();
                    		getChildren().clear();
                    		getChildren().add(createArticlePanel());

                    	}
                    	else {
                    		Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred");
                    		alert.showAndWait();
                    	}
                    }

                } catch (NumberFormatException ex) {
                    // Handle case where input is not a valid number
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Enter a valid id type.");
                    alert.showAndWait();
                } 
            });

            panel.getChildren().addAll(
                new Label("Enter the id of the article you want to delete:"), idField, deleteButton, cancelButton
            );
 
            return panel;
        }
        
        private VBox listArticles() {
            VBox panel = new VBox(10); // Vertical box layout with spacing
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");
            
            Button returnButton = new Button("Exit");
            Label instructionLabel = new Label("Enter group names to list articles (comma-separated):");
            TextField groupInputField = new TextField();
            groupInputField.setPromptText("E.g., Group1, Group2");
            Button listButton = new Button("List Articles");

            // VBox to display the list of articles
            VBox articleDisplay = new VBox(5);
            articleDisplay.setStyle("-fx-padding: 10; -fx-background-color: #f9f9f9; -fx-border-color: #ccc;");
            
            returnButton.setOnAction(e -> {
            	getChildren().clear();
            	getChildren().add(createArticlePanel());
            });
            
            listButton.setOnAction(e -> {
                String groupInput = groupInputField.getText();
                List<String> groupNames = groupInput.isEmpty() ? null : List.of(groupInput.split(","));
                
                // Clear previous article display
                articleDisplay.getChildren().clear();

                // Fetch articles from the database
                List<HelpArticle> articles = db.listHelpArticles(groupNames);
                
                if (articles.isEmpty()) {
                    articleDisplay.getChildren().add(new Label("No articles found for the specified groups."));
                } else {
                    for (HelpArticle article : articles) {
                        // Display each article with title, level, and short description
                    	Label idLabel = new Label("ID: " + article.getArticleId());
                        Label titleLabel = new Label("Title: " + article.getTitle());
                        Label levelLabel = new Label("Level: " + article.getLevel());
                        Label descriptionLabel = new Label("Description: " + article.getShortDescription());
                        Label keywordsLabel = new Label("Key Words: " + article.getKeywords());
                        Label referencesLabel = new Label("References: " + article.getReferences());
                        Label groupsLabel = new Label("Groups: " + article.getGroups());
                        VBox articleBox = new VBox(3, idLabel, titleLabel, levelLabel, descriptionLabel, keywordsLabel, referencesLabel, groupsLabel);
                        articleBox.setStyle("-fx-padding: 5; -fx-background-color: #e6e6e6; -fx-border-color: #ccc;");
                        
                        articleDisplay.getChildren().add(articleBox);
                    }
                }
            });

            panel.getChildren().addAll(returnButton, instructionLabel, groupInputField, listButton, articleDisplay);
            return panel;
        }

        
        /**
         * Creates the user home page UI.
         * @return VBox containing the user home page UI
         */
        private VBox updateArticles() {
        	VBox panel = new VBox(10); // Vertical box layout
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            TextField titleField = new TextField();
            TextField levelField = new TextField();
            TextField descriptionField = new TextField();
            TextField keywordField = new TextField();
            TextField bodyField = new TextField();
            TextField referenceField = new TextField();
            TextField groupsField = new TextField();
            TextField idField = new TextField();
            Button returnButton = new Button("Return");
            Button verifyButton = new Button("Verify");
            Button submitButton = new Button("Update Article");
            

            // Add input fields for account creation
            panel.getChildren().addAll(
                returnButton,
            	new Label("Enter ID of article you want to update: "), idField, verifyButton,
                new Label("Title:"), titleField,
                new Label("Level:"), levelField,
                new Label("Description:"), descriptionField,
                new Label("Key words:"), keywordField,
                new Label("Body:"), bodyField,
                new Label("References:"), referenceField,
                new Label("Groups:"), groupsField,
                submitButton
            );
            
          
            returnButton.setOnAction(e -> {
            	getChildren().clear();
            	getChildren().add(createArticlePanel());
            });
            
            verifyButton.setOnAction(e -> {
                
            	long id = Long.parseLong(idField.getText());
            	if(db.findHelpArticleById(id) == null)
                {
                	Alert alert = new Alert(Alert.AlertType.ERROR, "Article not found");
                    alert.showAndWait();
                    getChildren().clear();
                    getChildren().add(createArticlePanel());

                }
            	else
            	{
            		Alert alert = new Alert(Alert.AlertType.INFORMATION, "Article found!");
                    alert.showAndWait();
            	}
                
            });

            submitButton.setOnAction(e -> {
                // Retrieve and process data from text fields
                String title = titleField.getText();
                String level = levelField.getText();
                String description = descriptionField.getText();
                String body = bodyField.getText();
                long id = Long.parseLong(idField.getText());

                // Parse comma-separated fields into lists
                List<String> keywords = List.of(keywordField.getText().split("\\s*,\\s*"));
                List<String> references = List.of(referenceField.getText().split("\\s*,\\s*"));
                List<String> groups = List.of(groupsField.getText().split("\\s*,\\s*"));

                // Create a new HelpArticle object
                HelpArticle article = new HelpArticle(
                    title,
                    level,
                    description,
                    keywords,
                    body,
                    references,
                    groups
                );
                
                article.setArticleId(id);
                db.updateHelpArticle(article);

                // Add code here to handle the newly created article (e.g., save it or display a success message)
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Article updated! : "  + article.getTitle());
                alert.showAndWait();
                System.out.println(article.getArticleId());
                
                getChildren().clear();
                getChildren().add(createArticlePanel());
                
            });

            return panel;
        }
        
        /**
         * Creates the admin home page UI.
         * @return VBox containing the admin home page UI
         */
        private VBox createInstructorHomePage() {
            VBox panel = new VBox(10); // Vertical box layout
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            Stage stage = new Stage();
            InstructorHomePage instructorHomePage = new InstructorHomePage(); // Create an instance of AdminHomeScreen
            
            try {
            	instructorHomePage.start(stage); // Start AdminHomeScreen stage (login page)
            } catch (Exception ex) {
                  ex.printStackTrace();
                }
            
            return panel;
        }
        
        /**
         * Creates the reset password page.
         * @return VBox containing the reset password page UI
         */
         private VBox createResetPasswordPage()
         {
        	 VBox panel = new VBox(10); // Vertical box layout
             panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

             Stage stage = new Stage();
             ResetPasswordPage resetScreen = new ResetPasswordPage(); 
             
             try {
             	resetScreen.start(stage); 
             } catch (Exception ex) {
                   ex.printStackTrace();
                 }
             
             return panel;
         }
         
         
    }

    /**
     * Main method that launches the JavaFX application.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args); // Launch JavaFX application
    }
}
