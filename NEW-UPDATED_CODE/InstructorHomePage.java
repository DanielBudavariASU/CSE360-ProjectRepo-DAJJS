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
    private Database db = Driver.getDb();
    private Instructor instructor;
    
    public InstructorHomePage(Instructor instructor) {
        this.instructor = instructor;
    }
    
    /**
     * Default constructor that creates a placeholder admin user.
     */
    public InstructorHomePage() {
        String tusername = "Instructor";
        Password pass = new Password();
        String temppass = " ";
        this.instructor = new Instructor(tusername, temppass);
    }

    /**
     * The start method sets up the stage for the user home page, including
     * initializing the layout and scene.
     *
     * @param primaryStage The main stage for the JavaFX application.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Instructor Homepage");

        cardPane = new CardPane();
        Scene scene = new Scene(cardPane, 400, 900);  // Sets the size of the window

        primaryStage.setScene(scene);
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
            getChildren().add(createWelcomePanel());
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

            Label welcomeLabel = new Label("Welcome to Instructor Homepage");
            Button logoutButton = new Button("Logout");
            Button articleButton = new Button("Articles");
            Button backupButton = new Button("Backup Articles");
            Button restoreButton = new Button("Restore Articles");

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
            
         // Set action for the logout button
            articleButton.setOnAction(e -> {
                // Switch to the login page when logout is clicked
                Stage stage = (Stage) articleButton.getScene().getWindow();  // Get the current stage (window)
                ArticleHomePage articlePage = new ArticleHomePage(null, instructor);  // Create an instance of the login page (Driver)
                
                try {
                    articlePage.start(stage);  // Start the login page
                } catch (Exception ex) {
                    ex.printStackTrace();  // Handle any exception that occurs during page transition
                }
            });
            
            // Backup Button Action
            backupButton.setOnAction(e -> {
                Stage backupStage = new Stage();
                backupStage.setTitle("Backup Help Articles");
                VBox backupPage = createBackupPage();  // Use the backup page
                Scene backupScene = new Scene(backupPage, 300, 300);
                backupStage.setScene(backupScene);
                backupStage.show();
            });

            // Restore Button Action
            restoreButton.setOnAction(e -> {
                Stage restoreStage = new Stage();
                restoreStage.setTitle("Restore Help Articles");
                VBox restorePage = createRestorePage();  // Use the restore page
                Scene restoreScene = new Scene(restorePage, 300, 300);
                restoreStage.setScene(restoreScene);
                restoreStage.show();
            });
            
         
            // Add the label and button to the panel
            panel.getChildren().addAll(welcomeLabel, logoutButton, articleButton, backupButton, restoreButton);
            return panel;
        }
        
        // Backup page for AdminHomeScreen
        private VBox createBackupPage() {
            VBox panel = new VBox(10);
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");
            Label instructions = new Label("Enter group name(s) for backup (leave empty to backup all):");
            TextField groupNameField = new TextField();
            Label fileLabel = new Label("Enter the file name for backup:");
            TextField fileField = new TextField();
            Button submitButton = new Button("Submit");

            submitButton.setOnAction(e -> {
                String groupName = groupNameField.getText().trim();
                List<String> groupList = groupName.isEmpty() ? null : List.of(groupName.split(","));
                String file = fileField.getText().trim();
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to proceed with the backup?");
                confirmAlert.setHeaderText("Confirm Backup");
                confirmAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        db.backupHelpArticles(file, groupList);
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Backup successful!");
                        successAlert.showAndWait();
                    }
                });
            });

            panel.getChildren().addAll(instructions, groupNameField, fileLabel, fileField, submitButton);
            return panel;
        }

        // Restore page for AdminHomeScreen
        private VBox createRestorePage() {
            VBox panel = new VBox(10);
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");
            Label fileLabel = new Label("Enter the file name for backup:");
            TextField fileField = new TextField();
            Label instructions = new Label("Select restore option:");
            RadioButton mergeButton = new RadioButton("Merge");
            RadioButton overwriteButton = new RadioButton("Overwrite");
            ToggleGroup toggleGroup = new ToggleGroup();
            mergeButton.setToggleGroup(toggleGroup);
            overwriteButton.setToggleGroup(toggleGroup);
            Button submitButton = new Button("Submit");

            submitButton.setOnAction(e -> {
                RadioButton selectedOption = (RadioButton) toggleGroup.getSelectedToggle();
                String fileName = fileField.getText();
                if (selectedOption != null) {
                    String option = selectedOption.getText();
                    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to proceed with the restore?");
                    confirmAlert.setHeaderText("Confirm Restore");
                    
                    confirmAlert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                        	
                        	if(option.equals("Overwrite"))
                        	{
                        		db.restoreHelpArticles(fileName, false);
                                Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Restore successful!");
                                successAlert.showAndWait();
                        	}
                            
                        }
                        else //merge with existing files
                        {
                        	db.restoreHelpArticles(fileName, true);
                        	Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Restore successful!");
                            successAlert.showAndWait();
                        }
                    });
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a restore option.");
                    alert.showAndWait();
                }
            });

            panel.getChildren().addAll(fileLabel, fileField, instructions, mergeButton, overwriteButton, submitButton);
            return panel;
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
