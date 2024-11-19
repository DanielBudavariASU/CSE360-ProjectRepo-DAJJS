package trial;

import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import trial.StudentMessageSystem.CardPane;
import java.util.ArrayList;
import java.util.List;
import java.util.Base64;
import Encryption.EncryptionHelper;
import Encryption.EncryptionUtils;

public class StudentSearchSystem extends Application {
    private CardPane cardPane;
    private Database db = Driver.getDb(); 
    private Student student;

    public StudentSearchSystem(Student student) {
        this.student = student;
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
            getChildren().add(MessagePanel()); // Initialize with the welcome panel
        }

        /**
         * Creates the welcome panel with options to create an account or login.
         * If no admin is present in the system, forces the creation of an admin account.
         * @return VBox containing the welcome panel UI
         */
        private VBox MessagePanel() {
            VBox panel = new VBox(10); // Vertical box layout
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            // Buttons
            Button searchButton = new Button("Search Articles");
            Button viewButton = new Button("View Articles");
            Button listButton = new Button("List search history");
            Button returnButton = new Button("Exit");

            searchButton.setOnAction(e -> {
            	getChildren().clear();
            	getChildren().add((searchArticles()));
            });
            
            viewButton.setOnAction(e -> {
            	getChildren().clear();
            	getChildren().add((viewArticles()));
            });

            // Reset Password Button Action
            listButton.setOnAction(e -> {
            	getChildren().clear();
            	getChildren().add(listSearchRequests());	
                
            });

            // Logout Button Action
            returnButton.setOnAction(e -> {

            	// Switch to the login page when logout is clicked
            	Stage stage = (Stage) returnButton.getScene().getWindow();  // Get the current stage (window)
            	UserHomePage userPage = new UserHomePage(student);  // Create an instance of the login page (Driver)

            	try {
            		userPage.start(stage);  // Start the login page
            	} catch (Exception ex) {
            		ex.printStackTrace();  // Handle any exception that occurs during page transition
            	}
            	
            });
            
        
            panel.getChildren().addAll(searchButton, listButton, viewButton, returnButton);
            
            return panel;
        }
        
        /**
         * Creates the login panel where users can enter their username and password.
         * @return VBox containing the login panel UI
         */
        private VBox searchArticles() {
            VBox panel = new VBox(10); // Vertical box layout
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");
            
            
            Label keywordLabel = new Label("Enter keywords");
            TextField keywordField = new TextField();
            Label groupLabel = new Label("Enter group name (or type 'all' to search all groups): ");
            TextField groupField = new TextField();
            Label levelLabel = new Label("Enter level (e.g., beginner, intermediate, advanced, expert, or 'all'): ");
            TextField levelField = new TextField();
            
            Button searchButton = new Button("Search");
            
            VBox resultsBox = new VBox(10); 
            resultsBox.setPadding(new Insets(10));
          
            Button cancelButton = new Button("Back"); // Button to login

            searchButton.setOnAction(e -> {
              	String keyword = keywordField.getText();
              	String level = levelField.getText();
              	String groupName = groupField.getText();
                if (groupName.equalsIgnoreCase("all")) {
                    groupName = null;  // Null to represent no group restriction
                }
                
              	List<HelpArticle> searchResults = student.searchHelpArticles(db, keyword, groupName, level);
              	
              	// Clear previous results
                resultsBox.getChildren().clear();

                // Display active group
                Label activeLabel = new Label("Active Group: " + (groupName == null ? "All Groups" : groupName));
                resultsBox.getChildren().add(activeLabel);

                if (searchResults.isEmpty()) {
                    resultsBox.getChildren().add(new Label("No articles found."));
                    // Display level counts
                    resultsBox.getChildren().add(new Label("Number of articles by level:"));
                    resultsBox.getChildren().add(new Label("Beginner: " + 0));
                    resultsBox.getChildren().add(new Label("Intermediate: " + 0));
                    resultsBox.getChildren().add(new Label("Advanced: " + 0));
                    resultsBox.getChildren().add(new Label("Expert: " + 0));
                    
                } else {
                    // Count articles by level
                    int beginnerCount = 0, intermediateCount = 0, advancedCount = 0, expertCount = 0;
                    for (HelpArticle article : searchResults) {
                        switch (article.getLevel().toLowerCase()) {
                            case "beginner": beginnerCount++; break;
                            case "intermediate": intermediateCount++; break;
                            case "advanced": advancedCount++; break;
                            case "expert": expertCount++; break;
                        }
                    }

                    // Display level counts
                    resultsBox.getChildren().add(new Label("Number of articles by level:"));
                    resultsBox.getChildren().add(new Label("Beginner: " + beginnerCount));
                    resultsBox.getChildren().add(new Label("Intermediate: " + intermediateCount));
                    resultsBox.getChildren().add(new Label("Advanced: " + advancedCount));
                    resultsBox.getChildren().add(new Label("Expert: " + expertCount));

                    // Display article list
                    resultsBox.getChildren().add(new Label("\n=== Matching Articles ==="));
                    for (HelpArticle article : searchResults) {
                        HBox articleBox = new HBox(10);
                        articleBox.getChildren().addAll(
                                new Label("ID: " + article.getArticleId()),
                                new Label("Title: " + article.getTitle()),
                                new Label("Author: " + article.getAuthor()),
                                new Label("Description: " + article.getShortDescription())
                        );
                        resultsBox.getChildren().add(articleBox);
                    }             
                }
           
            });

            cancelButton.setOnAction(e -> {
            	getChildren().clear();
            	getChildren().add(MessagePanel());
            });

            panel.getChildren().addAll(
            	keywordLabel, keywordField, 
            	groupLabel, groupField,
            	levelLabel, levelField,
                searchButton, resultsBox, cancelButton
                 
            );
 
            return panel;
        }


        private VBox viewArticles() {
            VBox panel = new VBox(10); // Vertical box layout
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");
            
            Label idLabel = new Label("Enter the Article ID to view in detail: ");
            TextField idField = new TextField();

            Button viewButton = new Button("View");

            Button cancelButton = new Button("Back"); // Button to login
            
            VBox articleDisplay = new VBox(5);
            articleDisplay.setStyle("-fx-padding: 10; -fx-background-color: #f9f9f9; -fx-border-color: #ccc;");
            
            viewButton.setOnAction(e -> {
            	Long id = Long.parseLong(idField.getText());
            	HelpArticle selectedArticle = db.findHelpArticleById( id );
            	
            	if (selectedArticle == null) {
            		Alert failAlert = new Alert(Alert.AlertType.ERROR, "Article not found!");
          			failAlert.showAndWait();
          			getChildren().clear();
                	getChildren().add(MessagePanel());
            	} else {
            		if (db.hasAccessToArticle(student, selectedArticle)){
            			//student.viewArticle(db, articleId);
            			HelpArticle articleToDisplay = db.returnArticletoDisplay(id);
            			Label idResultLabel = new Label("ID: " + articleToDisplay.getArticleId());
            			Label titleResultLabel = new Label("Title: " + articleToDisplay.getTitle());
            			Label descriptionLabel = new Label("Description: " + articleToDisplay.getShortDescription());
            			Label authorLabel = new Label("Key Words: " + articleToDisplay.getAuthor());  
            			
            			VBox articleBox = new VBox(3, idResultLabel, titleResultLabel, descriptionLabel, authorLabel);
            			articleBox.setStyle("-fx-padding: 5; -fx-background-color: #e6e6e6; -fx-border-color: #ccc;");

            			articleDisplay.getChildren().add(articleBox);

            		}
            		else {
            			Alert failAlert = new Alert(Alert.AlertType.ERROR, "Access Denied");
              			failAlert.showAndWait();
            			getChildren().clear();
                    	getChildren().add(MessagePanel());
            		}
            	}
           
            });

            cancelButton.setOnAction(e -> {
            	getChildren().clear();
            	getChildren().add(MessagePanel());
            });

            panel.getChildren().addAll(
            		idLabel, idField, 
            		viewButton, articleDisplay, 
            		cancelButton
                 
            );
 
            return panel;
        } 
        
       
        private VBox listSearchRequests() {
        	VBox panel = new VBox(10); // Vertical box layout
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");
            
            Button listButton = new Button("List search request");
            VBox articleDisplay = new VBox(5);
            articleDisplay.setStyle("-fx-padding: 10; -fx-background-color: #f9f9f9; -fx-border-color: #ccc;");

            Button cancelButton = new Button("Back"); // Button to login

            listButton.setOnAction(e -> {
            	List <String> searchItems = student.getSearchHistory();
            	
            	for(String searchItem: searchItems)
            	{
            		Label requestLabel = new Label(searchItem);
            		VBox articleBox = new VBox(3, requestLabel);
                    articleBox.setStyle("-fx-padding: 5; -fx-background-color: #e6e6e6; -fx-border-color: #ccc;");
                    articleDisplay.getChildren().add(articleBox);
            	}
    
            });

            cancelButton.setOnAction(e -> {
            	getChildren().clear();
            	getChildren().add(MessagePanel());
            });

            panel.getChildren().addAll(
            	listButton, articleDisplay,
                 cancelButton
                 
            );
 
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
