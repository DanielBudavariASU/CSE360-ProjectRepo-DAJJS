package trial;

import java.util.List;

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
public class InstructorHomePage extends Application {
    private CardPane cardPane;  // Pane to hold the welcome content
    private Database db = Driver.getDb();  // Database instance for operations
    private Instructor instructor;  // The instructor user currently logged in
    
    public InstructorHomePage(Instructor instructor) {
        this.instructor = instructor;  // Assign the instructor passed to the constructor
    }
    
    /**
     * Default constructor that creates a placeholder instructor user.
     */
    public InstructorHomePage() {
        String tusername = "Instructor";  // Placeholder username for default instructor
        Password pass = new Password();  // Password object for the instructor
        String temppass = " ";  // Placeholder for password
        this.instructor = new Instructor(tusername, temppass);  // Create a default instructor
    }

    /**
     * The start method sets up the stage for the instructor home page, including
     * initializing the layout and scene.
     *
     * @param primaryStage The main stage for the JavaFX application.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Instructor Homepage");  // Set the title of the window

        cardPane = new CardPane();  // Initialize the CardPane
        Scene scene = new Scene(cardPane, 400, 900);  // Sets the size of the window

        primaryStage.setScene(scene);  // Set the scene on the primary stage
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
            getChildren().add(createWelcomePanel());  // Add the welcome panel to the pane
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

            Label welcomeLabel = new Label("Welcome to Instructor Homepage");  // Welcome message
            Button logoutButton = new Button("Logout");  // Logout button
            Button articleButton = new Button("Articles");  // Button to navigate to articles
            Button backupButton = new Button("Backup Articles");  // Button to initiate backup
            Button restoreButton = new Button("Restore Articles");  // Button to initiate restore

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
            
            // Set action for the articles button
            articleButton.setOnAction(e -> {
                // Switch to the articles page when clicked
                Stage stage = (Stage) articleButton.getScene().getWindow();  // Get the current stage (window)
                ArticleHomePage articlePage = new ArticleHomePage(null, instructor);  // Create an instance of the articles page
                
                try {
                    articlePage.start(stage);  // Start the articles page
                } catch (Exception ex) {
                    ex.printStackTrace();  // Handle any exception that occurs during page transition
                }
            });
            
            // Backup Button Action
            backupButton.setOnAction(e -> {
                Stage backupStage = new Stage();  // Create a new stage for the backup operation
                backupStage.setTitle("Backup Help Articles");  // Set the title for the backup window
                VBox backupPage = createBackupPage();  // Create the backup page layout
                Scene backupScene = new Scene(backupPage, 300, 300);  // Set the scene for the backup stage
                backupStage.setScene(backupScene);  // Assign the scene to the stage
                backupStage.show();  // Display the backup window
            });

            // Restore Button Action
            restoreButton.setOnAction(e -> {
                Stage restoreStage = new Stage();  // Create a new stage for the restore operation
                restoreStage.setTitle("Restore Help Articles");  // Set the title for the restore window
                VBox restorePage = createRestorePage();  // Create the restore page layout
                Scene restoreScene = new Scene(restorePage, 300, 300);  // Set the scene for the restore stage
                restoreStage.setScene(restoreScene);  // Assign the scene to the stage
                restoreStage.show();  // Display the restore window
            });
            
            // Add the label and buttons to the panel
            panel.getChildren().addAll(welcomeLabel, logoutButton, articleButton, backupButton, restoreButton);
            return panel;  // Return the completed welcome panel
        }
        
        // Backup page for AdminHomeScreen
        private VBox createBackupPage() {
            VBox panel = new VBox(10);  // VBox for the backup page with spacing
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");  // Set padding and alignment
            Label instructions = new Label("Enter group name(s) for backup (leave empty to backup all):");  // Instructions for user
            TextField groupNameField = new TextField();  // Input field for group names
            Label fileLabel = new Label("Enter the file name for backup:");  // Label for backup file name
            TextField fileField = new TextField();  // Input field for backup file name
            Button submitButton = new Button("Submit");  // Button to submit backup request

            submitButton.setOnAction(e -> {
                String groupName = groupNameField.getText().trim();  // Get group name input
                List<String> groupList = groupName.isEmpty() ? null : List.of(groupName.split(","));  // Create list from input
                String file = fileField.getText().trim();  // Get file name input
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to proceed with the backup?");  // Confirmation alert
                confirmAlert.setHeaderText("Confirm Backup");  // Set header for confirmation alert
                confirmAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        db.backupHelpArticles(file, groupList);  // Call method to backup help articles
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Backup successful!");  // Success alert
                        successAlert.showAndWait();  // Display success alert
                    }
                });
            });

            panel.getChildren().addAll(instructions, groupNameField, fileLabel, fileField, submitButton);  // Add elements to the panel
            return panel;  // Return the completed backup panel
        }

        // Restore page for AdminHomeScreen
        private VBox createRestorePage() {
            VBox panel = new VBox(10);  // VBox for the restore page with spacing
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");  // Set padding and alignment
            Label fileLabel = new Label("Enter the file name for backup:");  // Label for file name input
            TextField fileField = new TextField();  // Input field for file name
            Label instructions = new Label("Select restore option:");  // Instructions for user to select option
            RadioButton mergeButton = new RadioButton("Merge");  // Option to merge with existing articles
            RadioButton overwriteButton = new RadioButton("Overwrite");  // Option to overwrite existing articles
            ToggleGroup toggleGroup = new ToggleGroup();  // Group for radio button selection
            mergeButton.setToggleGroup(toggleGroup);  // Assign merge button to toggle group
            overwriteButton.setToggleGroup(toggleGroup);  // Assign overwrite button to toggle group
            Button submitButton = new Button("Submit");  // Button to submit restore request

            submitButton.setOnAction(e -> {
                RadioButton selectedOption = (RadioButton) toggleGroup.getSelectedToggle();  // Get selected radio button
                String fileName = fileField.getText();  // Get file name input
                if (selectedOption != null) {  // Check if an option is selected
                    String option = selectedOption.getText();  // Get text of the selected option
                    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to proceed with the restore?");  // Confirmation alert
                    confirmAlert.setHeaderText("Confirm Restore");  // Set header for confirmation alert
                    confirmAlert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            db.restoreHelpArticles(fileName, option);  // Call method to restore help articles
                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Restore successful!");  // Success alert
                            successAlert.showAndWait();  // Display success alert
                        }
                    });
                }
            });

            panel.getChildren().addAll(fileLabel, fileField, instructions, mergeButton, overwriteButton, submitButton);  // Add elements to the panel
            return panel;  // Return the completed restore panel
        }
    }

    // Main method to launch the application
    public static void main(String[] args) {
        launch(args);  // Launch the JavaFX application
    }
}
