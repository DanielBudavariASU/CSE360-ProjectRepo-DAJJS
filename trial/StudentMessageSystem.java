package trial;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * The StudentMessageSystem class extends the JavaFX Application class and provides
 * a UI for students to send general feedback or specific requests. It serves as the 
 * entry point for the messaging system for students.
 */
public class StudentMessageSystem extends Application {

    private CardPane cardPane; // Custom pane to hold different panels
    private Database db = Driver.getDb(); // Database instance
    private Student student; // The current student using the application

    /**
     * Constructor for StudentMessageSystem.
     * Initializes the application with the specified student.
     * 
     * @param student the student using the application
     */
    public StudentMessageSystem(Student student) {
        this.student = student;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Message System");

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
         * Constructor for CardPane. Sets the initial message panel.
         */
        public CardPane() {
            getChildren().add(MessagePanel()); // Initialize with the message panel
        }

        /**
         * Creates the main message panel with options for general feedback, specific requests, and logout.
         * 
         * @return VBox containing the message panel UI
         */
        private VBox MessagePanel() {
            VBox panel = new VBox(10); // Vertical box layout
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            // Buttons for different actions
            Button generalButton = new Button("General Message");
            Button specificButton = new Button("Specific Message");
            Button returnButton = new Button("Exit");

            // Event handlers for buttons
            generalButton.setOnAction(e -> {
                getChildren().clear();
                getChildren().add(generalMessage());
            });

            specificButton.setOnAction(e -> {
                getChildren().clear();
                getChildren().add(specificMessage());
            });

            returnButton.setOnAction(e -> {
                // Return to the user home page when exiting
                Stage stage = (Stage) returnButton.getScene().getWindow();
                UserHomePage userPage = new UserHomePage(student);

                try {
                    userPage.start(stage);
                } catch (Exception ex) {
                    ex.printStackTrace(); // Handle page transition errors
                }
            });

            panel.getChildren().addAll(generalButton, specificButton, returnButton);

            return panel;
        }

        /**
         * Creates the panel for sending general feedback.
         * 
         * @return VBox containing the general feedback UI
         */
        private VBox generalMessage() {
            VBox panel = new VBox(10); // Vertical box layout
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            // Input fields and labels
            Label generalLabel = new Label("Enter a general feedback:");
            TextField generalField = new TextField();
            Button generalButton = new Button("Send Feedback");

            // Back button
            Button cancelButton = new Button("Back");

            // Event handlers
            generalButton.setOnAction(e -> {
                String feedback = generalField.getText().trim();
                student.sendGenericFeedback(feedback); // Send feedback
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Thank you for your feedback!");
                successAlert.showAndWait();
            });

            cancelButton.setOnAction(e -> {
                getChildren().clear();
                getChildren().add(MessagePanel());
            });

            panel.getChildren().addAll(generalLabel, generalField, generalButton, cancelButton);

            return panel;
        }

        /**
         * Creates the panel for sending specific requests.
         * 
         * @return VBox containing the specific request UI
         */
        private VBox specificMessage() {
            VBox panel = new VBox(10); // Vertical box layout
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            // Input fields and labels
            Label specificLabel = new Label("Enter a specific message request:");
            TextField specificField = new TextField();
            Button specificButton = new Button("Send Request");

            // Back button
            Button cancelButton = new Button("Back");

            // Event handlers
            specificButton.setOnAction(e -> {
                String feedback = specificField.getText().trim();
                student.sendSpecificRequest(feedback); // Send specific request
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Special request received!");
                successAlert.showAndWait();
            });

            cancelButton.setOnAction(e -> {
                getChildren().clear();
                getChildren().add(MessagePanel());
            });

            panel.getChildren().addAll(specificLabel, specificField, specificButton, cancelButton);

            return panel;
        }
    }

    /**
     * Main method that launches the JavaFX application.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}