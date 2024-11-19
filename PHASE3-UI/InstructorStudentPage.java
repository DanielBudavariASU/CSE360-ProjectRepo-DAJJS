package trial;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

/**
 * JavaFX application that provides an interface for instructors to manage students, 
 * including adding students to special access groups, removing them, and viewing group details.
 * The application is designed to be flexible, allowing both instructors and admins to manage access.
 */
public class InstructorStudentPage extends Application {

    private CardPane cardPane; // Custom pane to hold different panels
    private Database db = Driver.getDb(); // Database instance
    private Admin admin; // Admin instance (if used)
    private Instructor instructor; // Instructor instance (for the current session)

    /**
     * Constructor for InstructorStudentPage.
     * Initializes the application with the specified instructor.
     * 
     * @param instructor the instructor using the application
     */
    public InstructorStudentPage(Instructor instructor) {
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
         * Constructor for CardPane. Sets the initial instructor panel.
         */
        public CardPane() {
            getChildren().add(InstructorPanel()); // Initialize with the instructor panel
        }

        /**
         * Creates the instructor panel with options to view, remove, and add students.
         * Also provides a logout button for navigation.
         * 
         * @return VBox containing the instructor panel UI
         */
        private VBox InstructorPanel() {
            VBox panel = new VBox(10); // Vertical box layout
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            // Buttons for different actions
            Button viewButton = new Button("View Students");
            Button deleteButton = new Button("Remove Students");
            Button addButton = new Button("Add student to Special Access Group");
            Button returnButton = new Button("Exit");

            // Event handlers for buttons
            viewButton.setOnAction(e -> {
                getChildren().clear();
                getChildren().add(viewStudents());
            });

            deleteButton.setOnAction(e -> {
                getChildren().clear();
                getChildren().add(removeAccess());
            });

            addButton.setOnAction(e -> {
                getChildren().clear();
                getChildren().add(addStudents());
            });

            returnButton.setOnAction(e -> {
                // Logout logic based on the user's role
                if (instructor != null) {
                    Stage stage = (Stage) returnButton.getScene().getWindow();
                    InstructorHomePage instructorPage = new InstructorHomePage(instructor);

                    try {
                        instructorPage.start(stage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    Stage stage = (Stage) returnButton.getScene().getWindow();
                    AdminHomeScreen adminPage = new AdminHomeScreen(admin);

                    try {
                        adminPage.start(stage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            panel.getChildren().addAll(viewButton, deleteButton, addButton, returnButton);

            return panel;
        }

        /**
         * Creates a panel for removing students from special access groups or the system.
         * 
         * @return VBox containing the remove access UI
         */
        private VBox removeAccess() {
            VBox panel = new VBox(10);
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            Label specialGroup = new Label("Enter special group:");
            TextField specialGroupField = new TextField();
            Button verifyButton = new Button("Verify Access");

            Label studentsLabel = new Label("Enter student to remove from special group:");
            TextField studentsField = new TextField();
            Button removeStudentButton = new Button("Remove Student from special group");

            Button cancelButton = new Button("Back");

            // Event handlers for buttons
            verifyButton.setOnAction(e -> {
                String specialgroup = specialGroupField.getText().trim();

                if (db.hasAccessToGroups(instructor, List.of(specialgroup))) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Instructor has access to special group");
                    successAlert.showAndWait();
                } else {
                    Alert deniedAlert = new Alert(Alert.AlertType.ERROR, "Access Denied!");
                    deniedAlert.showAndWait();
                    getChildren().clear();
                    getChildren().add(InstructorPanel());
                }
            });

            removeStudentButton.setOnAction(e -> {
                String specialgroup = specialGroupField.getText().trim();
                String studentToRemove = studentsField.getText().trim();

                if (db.findStudentByUsername(studentToRemove) != null) {
                    instructor.removeStudentFromSpecialAccessGroup(db, specialgroup, db.findStudentByUsername(studentToRemove));
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Student removed from group");
                    successAlert.showAndWait();
                } else {
                    Alert notFoundAlert = new Alert(Alert.AlertType.ERROR, "The student was not found!");
                    notFoundAlert.showAndWait();
                }
            });

            cancelButton.setOnAction(e -> {
                getChildren().clear();
                getChildren().add(InstructorPanel());
            });

            panel.getChildren().addAll(specialGroup, specialGroupField, verifyButton, studentsLabel, studentsField, removeStudentButton, cancelButton);

            return panel;
        }

        /**
         * Creates a panel for viewing students in special groups or the general system.
         * 
         * @return VBox containing the view students UI
         */
        private VBox viewStudents() {
            VBox panel = new VBox(10);
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            Button returnButton = new Button("Back");
            TextField groupInputField = new TextField();
            groupInputField.setPromptText("E.g., Group1, Group2");
            Button listButton = new Button("List Students");

            VBox specialDisplay = new VBox(5);
            specialDisplay.setStyle("-fx-padding: 10; -fx-background-color: #f9f9f9; -fx-border-color: #ccc;");

            returnButton.setOnAction(e -> {
                getChildren().clear();
                getChildren().add(InstructorPanel());
            });

            listButton.setOnAction(e -> {
                String groupInput = groupInputField.getText();
                List<String> groupNames = groupInput.isEmpty() ? null : List.of(groupInput.split(","));
                specialDisplay.getChildren().clear();

                // Add logic to list students based on group names
            });

            panel.getChildren().addAll(returnButton, groupInputField, listButton, specialDisplay);
            return panel;
        }

        /**
         * Creates a panel for adding students to special access groups.
         * 
         * @return VBox containing the add students UI
         */
        private VBox addStudents() {
            VBox panel = new VBox(10);
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            Label specialGroup = new Label("Enter special group:");
            TextField specialGroupField = new TextField();
            Button verifyButton = new Button("Verify Access");

            Label studentsLabel = new Label("Enter students to add (comma-separated):");
            TextField studentsField = new TextField();
            Button addStudentButton = new Button("Add Student");

            Button cancelButton = new Button("Back");

            verifyButton.setOnAction(e -> {
                String specialgroup = specialGroupField.getText().trim();

                if (db.hasAccessToGroups(instructor, List.of(specialgroup))) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Instructor has access to special group");
                    successAlert.showAndWait();
                } else {
                    Alert deniedAlert = new Alert(Alert.AlertType.ERROR, "Access Denied!");
                    deniedAlert.showAndWait();
                }
            });

            addStudentButton.setOnAction(e -> {
                String specialgroup = specialGroupField.getText().trim();
                String studentToAdd = studentsField.getText().trim();

                if (db.findUserByUsername(studentToAdd) != null) {
                    User userToAdd = db.findUserByUsername(studentToAdd);
                    instructor.addStudentToSpecialAccessGroup(db, specialgroup, (Student) userToAdd);
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Student added to group");
                    successAlert.showAndWait();
                } else {
                    Alert notFoundAlert = new Alert(Alert.AlertType.ERROR, "The student was not found!");
                    notFoundAlert.showAndWait();
                }
            });

            cancelButton.setOnAction(e -> {
                getChildren().clear();
                getChildren().add(InstructorPanel());
            });

            panel.getChildren().addAll(specialGroup, specialGroupField, verifyButton, studentsLabel, studentsField, addStudentButton, cancelButton);

            return panel;
        }
    }

    /**
     * Main method to launch the JavaFX application.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}

