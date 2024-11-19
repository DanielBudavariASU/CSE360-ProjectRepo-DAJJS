package trial;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * The InstructorHomePage class is a JavaFX application that represents 
 * the main homepage for instructors. It provides a user interface with 
 * various buttons to navigate to different features, such as managing 
 * articles, groups, students, backing up, and restoring help articles.
 * 
 * Features:
 * - Displays a welcome message.
 * - Offers navigation to various pages (Articles, Groups, Students).
 * - Provides functionality for backing up and restoring help articles.
 * - Allows users to log out and return to the login page.
 * 
 * This application uses a custom CardPane class for its layout and integrates
 * various UI components such as buttons, labels, and input fields.
 */
public class InstructorHomePage extends Application {
    private CardPane cardPane;  // Pane to hold the welcome content
    private Database db = Driver.getDb();  // Database instance for backup/restore
    private Instructor instructor;  // Represents the current instructor

    /**
     * Constructor that accepts an Instructor object.
     * 
     * @param instructor The instructor object associated with this homepage.
     */
    public InstructorHomePage(Instructor instructor) {
        this.instructor = instructor;
    }
    
    /**
     * Default constructor that creates a placeholder instructor with default credentials.
     */
    public InstructorHomePage() {
        String tusername = "Instructor";
        Password pass = new Password();
        String temppass = " ";
        this.instructor = new Instructor(tusername, temppass);
    }

    /**
     * The start method initializes and sets up the stage for the instructor homepage.
     * It configures the layout, scene, and displays the main window.
     * 
     * @param primaryStage The main stage for the JavaFX application.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Instructor Homepage");

        cardPane = new CardPane();  // Initialize the custom CardPane layout
        Scene scene = new Scene(cardPane, 400, 900);  // Create a scene with a specified size

        primaryStage.setScene(scene);  // Set the scene for the stage
        primaryStage.show();  // Display the stage
    }

    /**
     * The CardPane class is a custom container that extends StackPane.
     * It manages the layout for the user interface and includes various panels.
     */
    class CardPane extends StackPane {

        /**
         * Constructor for CardPane that initializes the layout by adding 
         * the welcome panel to the pane.
         */
        public CardPane() {
            getChildren().add(createWelcomePanel());
        }

        /**
         * Creates the welcome panel UI. This panel contains:
         * - A welcome message for the instructor.
         * - Navigation buttons for different features.
         * - Backup and restore functionality for help articles.
         * 
         * @return VBox The panel containing UI elements.
         */
        private VBox createWelcomePanel() {
            VBox panel = new VBox(10);  // VBox with spacing of 10 between elements
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");  // Set padding and alignment

            // Create UI components
            Label welcomeLabel = new Label("Welcome to Instructor Homepage");
            Button logoutButton = new Button("Logout");
            Button articleButton = new Button("Articles");
            Button groupsButton = new Button("Groups");
            Button studentButton = new Button("Students");
            Button backupButton = new Button("Backup Articles");
            Button restoreButton = new Button("Restore Articles");

            // Set action for the logout button
            logoutButton.setOnAction(e -> {
                Stage stage = (Stage) logoutButton.getScene().getWindow();
                Driver loginPage = new Driver();  // Create an instance of the login page
                try {
                    loginPage.start(stage);  // Switch to the login page
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            // Set action for the Articles button
            articleButton.setOnAction(e -> {
                Stage stage = (Stage) articleButton.getScene().getWindow();
                ArticleHomePage articlePage = new ArticleHomePage(null, instructor);  // Navigate to the Articles page
                try {
                    articlePage.start(stage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            // Set action for the Groups button
            groupsButton.setOnAction(e -> {
                Stage stage = (Stage) articleButton.getScene().getWindow();
                InstructorGroupPage groupPage = new InstructorGroupPage(instructor);  // Navigate to the Groups page
                try {
                    groupPage.start(stage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            // Set action for the Students button
            studentButton.setOnAction(e -> {
                Stage stage = (Stage) articleButton.getScene().getWindow();
                InstructorStudentPage studentPage = new InstructorStudentPage(instructor);  // Navigate to the Students page
                try {
                    studentPage.start(stage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            // Set action for the Backup button
            backupButton.setOnAction(e -> {
                Stage backupStage = new Stage();
                backupStage.setTitle("Backup Help Articles");
                VBox backupPage = createBackupPage();  // Create the backup page UI
                Scene backupScene = new Scene(backupPage, 300, 300);
                backupStage.setScene(backupScene);
                backupStage.show();
            });

            // Set action for the Restore button
            restoreButton.setOnAction(e -> {
                Stage restoreStage = new Stage();
                restoreStage.setTitle("Restore Help Articles");
                VBox restorePage = createRestorePage();  // Create the restore page UI
                Scene restoreScene = new Scene(restorePage, 300, 300);
                restoreStage.setScene(restoreScene);
                restoreStage.show();
            });

            // Add the components to the panel
            panel.getChildren().addAll(welcomeLabel, logoutButton, articleButton, groupsButton, studentButton, backupButton, restoreButton);
            return panel;
        }

        /**
         * Creates the backup page UI. This page allows instructors to specify
         * group names and file name for backing up help articles.
         * 
         * @return VBox The panel containing backup UI elements.
         */
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
                        // Placeholder for backup implementation
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Backup successful!");
                        successAlert.showAndWait();
                    }
                });
            });

            panel.getChildren().addAll(instructions, groupNameField, fileLabel, fileField, submitButton);
            return panel;
        }

        /**
         * Creates the restore page UI. This page allows instructors to specify
         * the file name and choose between merge or overwrite options for restoring help articles.
         * 
         * @return VBox The panel containing restore UI elements.
         */
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
                            // Placeholder for restore implementation
                            boolean merge = option.equals("Merge");
                            db.restoreHelpArticles(fileName, merge, instructor);
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
     * The main method launches the JavaFX application by invoking the launch method.
     * 
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
