package trial;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

/**
 * The Driver class that extends the JavaFX Application class and serves as the entry point
 * for the Login System. This class initializes the primary stage, sets up the initial scene,
 * and provides navigation between different panels like login, account creation, etc.
 */
public class InstructorGroupPage extends Application {

    private CardPane cardPane; // Custom pane to hold different panels
    private Database db = Driver.getDb();
    private Instructor instructor;
    
    //pass admin or instructor as null if user is not using that role in that session
    public InstructorGroupPage(Instructor instructor) {
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
            getChildren().add(InstructorPanel()); // Initialize with the welcome panel
        }

        /**
         * Creates the welcome panel with options to create an account or login.
         * If no admin is present in the system, forces the creation of an admin account.
         * @return VBox containing the welcome panel UI
         */
        private VBox InstructorPanel() {
            VBox panel = new VBox(10); // Vertical box layout
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            // Buttons
            Button viewButton = new Button("View Member Access");
            Button createButton = new Button("Create Groups");
            Button deleteButton = new Button("Delete Group");
            Button returnButton = new Button("Exit");
            

            viewButton.setOnAction(e -> {
            	getChildren().clear();
            	getChildren().add(viewMemberAccess());
            });

            // Reset Password Button Action
            deleteButton.setOnAction(e -> {
            	getChildren().clear();
            	getChildren().add(deleteGroup());	
                
            });
            
            createButton.setOnAction(e -> {
            	getChildren().clear();
            	getChildren().add(createGroup());
                
            });


            // Logout Button Action
            returnButton.setOnAction(e -> {
            	if(instructor != null)
            	{
            		// Switch to the login page when logout is clicked
                    Stage stage = (Stage) returnButton.getScene().getWindow();  // Get the current stage (window)
                    InstructorHomePage instructorPage = new InstructorHomePage(instructor);  // Create an instance of the login page (Driver)
                    
                    try {
                        instructorPage.start(stage);  // Start the login page
                    } catch (Exception ex) {
                        ex.printStackTrace();  // Handle any exception that occurs during page transition
                    }
            	}
            });
            
        
            panel.getChildren().addAll(viewButton, createButton, deleteButton, returnButton);
            
            return panel;
        }
        
        /**
         * Creates the login panel where users can enter their username and password.
         * @return VBox containing the login panel UI
         */
        private VBox createGroup() {
            VBox panel = new VBox(10); // Vertical box layout
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");
            
            
            Label specialGroup = new Label("Enter special group name");
            TextField specialGroupField = new TextField();
            Button createSpecialGroupButton = new Button("Create Special Group");
            
            // Fields for adding students
            Label generalLabel = new Label("Enter general group name:");
            TextField generalField = new TextField();
            Button createGeneralGroupButton = new Button("Create General Group");
          
            Button cancelButton = new Button("Back"); // Button to login

            createSpecialGroupButton.setOnAction(e -> {
              	String specialgroup = specialGroupField.getText().trim();
            	if(db.findSpecialAccessGroupByName((specialgroup)) == null)
            	{
            		SpecialAccessGroup newGroup = new SpecialAccessGroup(specialgroup);
            		db.addSpecialAccessGroup(newGroup);
            		Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Special access group added!");
                    successAlert.showAndWait();
            	}
            	else
            	{
            		Alert failAlert = new Alert(Alert.AlertType.ERROR, "Special access group already exists!!");
            		failAlert.showAndWait();
            	}

                
            });
            
            createGeneralGroupButton.setOnAction(e -> {
            	String generalgroup = specialGroupField.getText().trim();
            	if(db.findGroupByName((generalgroup)) == null)
            	{
            		HelpGroup newHelpGroup = new HelpGroup(generalgroup);
            		db.addHelpGroup(newHelpGroup);
            		Alert failAlert = new Alert(Alert.AlertType.INFORMATION, "Help group added!");
            		failAlert.showAndWait();
            	}
            	else
            	{
            		Alert successAlert = new Alert(Alert.AlertType.ERROR, "Help group already exists!!");
                    successAlert.showAndWait();
            	}
                
            });
            
            cancelButton.setOnAction(e -> {
            	getChildren().clear();
            	getChildren().add(InstructorPanel());
            });

            panel.getChildren().addAll(
                 specialGroup, specialGroupField, createSpecialGroupButton,
                 generalLabel, generalField, createGeneralGroupButton,
                 cancelButton
                 
            );
 
            return panel;
        }


        /**
         * This method creates a panel that allows instructors to view member access for specific groups. 
         * The panel includes:
         * - A text field to input group names.
         * - A list button to display the group member access details.
         * - A back button to return to the main instructor panel.
         * - Displays details for accessible and inaccessible groups.
         * 
         * @return VBox containing the view member access UI components.
         */
        private VBox viewMemberAccess() {
            VBox panel = new VBox(10); // Vertical box layout with spacing
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            Button returnButton = new Button("Back");
            Label instructionLabel = new Label("Enter group names to view member access:");
            TextField groupInputField = new TextField();
            groupInputField.setPromptText("E.g., Group1, Group2");
            Button listButton = new Button("List Members");

            // VBox to display the list of group members
            VBox articleDisplay = new VBox(5);
            articleDisplay.setStyle("-fx-padding: 10; -fx-background-color: #f9f9f9; -fx-border-color: #ccc;");

            returnButton.setOnAction(e -> {
                getChildren().clear();
                getChildren().add(InstructorPanel());
            });

            listButton.setOnAction(e -> {
                String groupInput = groupInputField.getText();
                List<String> groupNames = groupInput.isEmpty() ? null : List.of(groupInput.split(","));

                // Clear previous article display
                articleDisplay.getChildren().clear();

                List<SpecialAccessGroup> groups = new ArrayList<>();

                if (groupNames == null) {
                    groupNames = db.getallGroupNames();
                }

                List<String> inaccessibleGroups = new ArrayList<>();

                for (String name : groupNames) {
                    name = name.trim(); // Remove extra spaces
                    if (db.findSpecialAccessGroupByName(name) != null) {
                        if (db.hasAccessToGroups(instructor, List.of(name))) {
                            groups.add(db.findSpecialAccessGroupByName(name));
                        } else {
                            inaccessibleGroups.add(name);
                        }
                    }
                }

                for (SpecialAccessGroup specialGroup : groups) {
                    // Display each group's details
                    Label groupLabel = new Label("Group Name: " + specialGroup.getGroupName());
                    Label idLabel = new Label("Admins: " + specialGroup.getAdmins());
                    Label titleLabel = new Label("Instructors: " + specialGroup.getInstructors());
                    Label levelLabel = new Label("Students: " + specialGroup.getStudents());
                    VBox articleBox = new VBox(3, groupLabel, idLabel, titleLabel, levelLabel);
                    articleBox.setStyle("-fx-padding: 5; -fx-background-color: #e6e6e6; -fx-border-color: #ccc;");

                    articleDisplay.getChildren().add(articleBox);
                }

                if (inaccessibleGroups != null) {
                    Label inaccessibleLabel = new Label("Admins do not have access to these groups: ");
                    for (String inaccessibleGroupName : inaccessibleGroups) {
                        Label groupLabel = new Label(inaccessibleGroupName);
                        VBox inaccessibleBox = new VBox(3, inaccessibleLabel, groupLabel);
                        inaccessibleBox.setStyle("-fx-padding: 5; -fx-background-color: #e6e6e6; -fx-border-color: #ccc;");

                        articleDisplay.getChildren().add(inaccessibleBox);
                    }
                }
            });

            panel.getChildren().addAll(returnButton, instructionLabel, groupInputField, listButton, articleDisplay);
            return panel;
        }

        
        /**
         * This method creates a panel for instructors to delete specific groups, including:
         * - Special access groups.
         * - General groups.
         * It provides input fields for group names and buttons to perform deletion actions.
         * 
         * @return VBox containing the delete group UI components.
         */
        private VBox deleteGroup() {
            VBox panel = new VBox(10); // Vertical box layout
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            Label specialGroup = new Label("Enter special group name:");
            TextField specialGroupField = new TextField();
            Button deleteSpecialGroupButton = new Button("Delete Special Group");

            // Fields for deleting general groups
            Label generalLabel = new Label("Enter general group name:");
            TextField generalField = new TextField();
            Button deleteGeneralGroupButton = new Button("Delete General Group");

            // Cancel button to return to the instructor panel
            Button cancelButton = new Button("Back");

            deleteSpecialGroupButton.setOnAction(e -> {
                String specialgroup = specialGroupField.getText().trim();
                if (db.hasAccessToGroups(instructor, List.of(specialgroup))) {
                    if (db.findSpecialAccessGroupByName(specialgroup) != null) {
                        instructor.deleteGroup(db, specialgroup);
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Special access group deleted!");
                        successAlert.showAndWait();
                    } else {
                        Alert failAlert = new Alert(Alert.AlertType.INFORMATION, "Special access group not found!");
                        failAlert.showAndWait();
                    }
                } else {
                    Alert failAlert = new Alert(Alert.AlertType.ERROR, "Access Denied");
                    failAlert.showAndWait();
                }
            });

            deleteGeneralGroupButton.setOnAction(e -> {
                String generalGroup = generalField.getText().trim();
                if (db.findGroupByName(generalGroup) != null) {
                    instructor.deleteGroup(db, generalGroup);
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "General group deleted!");
                    successAlert.showAndWait();
                } else {
                    Alert failAlert = new Alert(Alert.AlertType.ERROR, "General group not found!");
                    failAlert.showAndWait();
                }
            });

            cancelButton.setOnAction(e -> {
                getChildren().clear();
                getChildren().add(InstructorPanel());
            });

            panel.getChildren().addAll(
                specialGroup, specialGroupField, deleteSpecialGroupButton,
                generalLabel, generalField, deleteGeneralGroupButton,
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

