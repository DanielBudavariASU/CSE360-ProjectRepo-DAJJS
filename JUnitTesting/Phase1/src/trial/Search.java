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
    private CardPane cardPane;
    private Database db = Driver.getDb(); 
    private Admin admin;
    private Instructor instructor;
    private User user;

    public Search(Admin admin, Instructor instructor, User user) {
        this.admin = admin;
        this.instructor = instructor;
        this.user = user;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Articles");

        cardPane = new CardPane();
        Scene scene = new Scene(cardPane, 400, 900);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    class CardPane extends StackPane {
        
        public CardPane() {
            getChildren().add(searchArticles());
        }

        private VBox searchArticles() {
            VBox panel = new VBox(10);
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");
            
            // Return button setup
            Button returnButton = new Button("Exit");
            returnButton.setOnAction(e -> {
                Stage stage = (Stage) returnButton.getScene().getWindow();
                if (admin != null) {
                    AdminHomeScreen adminPage = new AdminHomeScreen();
                    try {
                        adminPage.start(stage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    InstructorHomePage instructorPage = new InstructorHomePage();
                    try {
                        instructorPage.start(stage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            // Search fields for ID, title, and group
            Label idLabel = new Label("Search by Article ID:");
            TextField idField = new TextField();
            idField.setPromptText("Enter article ID");

            Label titleLabel = new Label("Search by Title:");
            TextField titleField = new TextField();
            titleField.setPromptText("Enter article title");

            Label groupLabel = new Label("Search by Group:");
            TextField groupField = new TextField();
            groupField.setPromptText("Enter group names, separated by commas");

            Button searchButton = new Button("Search Articles");

            // VBox for displaying search results
            VBox articleDisplay = new VBox(5);
            articleDisplay.setStyle("-fx-padding: 10; -fx-background-color: #f9f9f9; -fx-border-color: #ccc;");

            searchButton.setOnAction(e -> {
                String idInput = idField.getText().trim();
                long id = -1;  // Default to -1 to skip ID-based search if no ID is entered
                if (!idInput.isEmpty()) {
                    try {
                        id = Long.parseLong(idInput);
                    } catch (NumberFormatException ex) {
                        articleDisplay.getChildren().clear();
                        articleDisplay.getChildren().add(new Label("Invalid ID format. Please enter a numeric ID."));
                        return;  // Stop execution if ID format is invalid
                    }
                }

                String titleInput = titleField.getText().trim();
                String groupInput = groupField.getText().trim();
                List<String> groupNames = groupInput.isEmpty() ? null : List.of(groupInput.split(","));

                articleDisplay.getChildren().clear();

                // Fetch articles from the database using multiple search criteria
                List<HelpArticle> articles = db.searchHelpArticles(id, titleInput, groupNames);

                if (articles.isEmpty()) {
                    articleDisplay.getChildren().add(new Label("No articles found for the specified criteria."));
                } else {
                    for (HelpArticle article : articles) {
                        Label idResultLabel = new Label("ID: " + article.getArticleId());
                        Label titleResultLabel = new Label("Title: " + article.getTitle());
                        Label levelLabel = new Label("Level: " + article.getLevel());
                        Label descriptionLabel = new Label("Description: " + article.getShortDescription());
                        Label keywordsLabel = new Label("Key Words: " + article.getKeywords());
                        Label referencesLabel = new Label("References: " + article.getReferences());
                        Label groupsLabel = new Label("Groups: " + article.getGroups());
                        VBox articleBox = new VBox(3, idResultLabel, titleResultLabel, levelLabel, descriptionLabel, keywordsLabel, referencesLabel, groupsLabel);
                        articleBox.setStyle("-fx-padding: 5; -fx-background-color: #e6e6e6; -fx-border-color: #ccc;");
                        
                        articleDisplay.getChildren().add(articleBox);
                    }
                }
            });


            panel.getChildren().addAll(returnButton, idLabel, idField, titleLabel, titleField, groupLabel, groupField, searchButton, articleDisplay);
            return panel;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}