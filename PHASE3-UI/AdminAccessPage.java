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
public class AdminAccessPage extends Application {

    private CardPane cardPane; // Custom pane to hold different panels
    private Database db = Driver.getDb();
    private Admin admin;
    private Instructor instructor;
    
    //pass admin or instructor as null if user is not using that role in that session
    public AdminAccessPage(Admin admin, Instructor instructor) {
        this.admin = admin;
        this.instructor = instructor;
        
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("View, Add, Delete Users from General/Special Groups");

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
            getChildren().add(accessPanel()); // Initialize with the welcome panel
        }

        /**
         * Creates the welcome panel with options to create an account or login.
         * If no admin is present in the system, forces the creation of an admin account.
         * @return VBox containing the welcome panel UI
         */
        private VBox accessPanel() {
            VBox panel = new VBox(10); // Vertical box layout
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");

            // Buttons
            Button grantButton = new Button("Grant Access");
            Button removeButton = new Button("Remove Access");
            Button viewButton = new Button("View Access Members");
            Button returnButton = new Button("Exit");
            

            grantButton.setOnAction(e -> {
            	getChildren().clear();
            	getChildren().add(grantAccess());
            });

            // Reset Password Button Action
            removeButton.setOnAction(e -> {
            	getChildren().clear();
            	getChildren().add(removeAccess());
            	
                
            });
            
            viewButton.setOnAction(e -> {
            	getChildren().clear();
            	getChildren().add(listMembers());
                
            });

            // Logout Button Action
            returnButton.setOnAction(e -> {
            	if(admin != null)
            	{
            		// Switch to the login page when logout is clicked
                    Stage stage = (Stage) returnButton.getScene().getWindow();  // Get the current stage (window)
                    AdminHomeScreen adminPage = new AdminHomeScreen(admin);  // Create an instance of the login page (Driver)
                    
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
            
 
            panel.getChildren().addAll(grantButton, removeButton, viewButton, returnButton);
            
            return panel;
        }

        /**
         * Creates the login panel where users can enter their username and password.
         * @return VBox containing the login panel UI
         */
        private VBox grantAccess() {
            VBox panel = new VBox(10); // Vertical box layout
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");
            
            
            Label specialGroup = new Label("Enter special group to edit");
            TextField specialGroupField = new TextField();
            Button verifyButton = new Button("Verify Access");
            
            // Fields for adding students
            Label studentsLabel = new Label("Enter students to add (comma-separated):");
            TextField studentsField = new TextField();
            Button addStudentButton = new Button("Add Student");
            

            // Fields for adding instructors
            Label instructorsLabel = new Label("Enter instructors to add (comma-separated):");
            TextField instructorsField = new TextField();
            Button addInstructorButton = new Button("Add Instructor");

            // Fields for adding admins
            Label adminLabel = new Label("Enter admins to add (comma-separated):");
            TextField adminField = new TextField();
            Button addAdminButton = new Button("Add Admin");

            // Submit button
            Button submitButton = new Button("Submit");
            Button cancelButton = new Button("Back"); // Button to login
            
            verifyButton.setOnAction(e -> {
            	String specialgroup = specialGroupField.getText().trim();
            	List<String> specialGroupList = specialgroup.isEmpty() ? null : List.of(specialgroup.split(","));
            	
            	System.out.println(admin.getUsername());
            	if(db.hasAccessToGroups(admin, List.of(specialgroup)))
            	{
            		Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Admin has access to special group");
                    successAlert.showAndWait();
            	}
            	else
            	{
            		Alert deniedAlert = new Alert(Alert.AlertType.ERROR, "Access Denied!");
            		deniedAlert.showAndWait();
            		getChildren().clear();
            		getChildren().add(accessPanel());
            	}
            });
            
            addStudentButton.setOnAction(e -> {
            	String specialgroup = specialGroupField.getText().trim();
                String studentToAdd = studentsField.getText().trim();

                List<String> studentList = studentToAdd.isEmpty() ? null : List.of(studentToAdd.split(","));

                if(studentList != null)
                {
                	if(db.findUserByUsername(studentToAdd) != null)
                	{
                		User userToAdd = db.findUserByUsername(studentToAdd);
                		admin.addStudentToSpecialAccessGroup(db, specialgroup, (Student) userToAdd);	
                		Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Student added to group");
                        successAlert.showAndWait();
                	}
                	else
                	{
                		Alert notFoundAlert= new Alert(Alert.AlertType.ERROR, "The student was not found!");
                		notFoundAlert.showAndWait();
                	}
                }
                
            });
            
            addInstructorButton.setOnAction(e -> {
            	String specialgroup = specialGroupField.getText();
                String instructorToAdd = instructorsField.getText();
                List<String> instructorList = instructorToAdd.isEmpty() ? null : List.of(instructorToAdd.split(","));
                      
                if(instructorList != null)
                {
                	if(db.findUserByUsername(instructorToAdd) != null)
                	{
                		User userToAdd = db.findUserByUsername(instructorToAdd);
                		if (userToAdd instanceof Instructor) {
                            admin.addInstructorAdminToSpecialAccessGroup(db, specialgroup, (Instructor) userToAdd);
                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Instructor added to group");
                            successAlert.showAndWait();
                		}
                	}
                	else
                	{
                		Alert notFoundAlert= new Alert(Alert.AlertType.ERROR, "The instructor was not found!");
                		notFoundAlert.showAndWait();
                	}
                }
                
            });
            
            addAdminButton.setOnAction(e -> {
            	String specialgroup = specialGroupField.getText().trim();
                String adminToAdd = adminField.getText().trim();
                
                List<String> adminList = adminToAdd.isEmpty() ? null : List.of(adminToAdd.split(","));
                
                if(admin != null)
                {
                	if(db.findUserByUsername(adminToAdd) != null)
                	{
                		User userToAdd = db.findUserByUsername(adminToAdd);
                		if (userToAdd instanceof Admin) {
                            admin.addAdminToSpecialAccessGroup(db, specialgroup, (Admin) userToAdd);
                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Admin added to group");
                            successAlert.showAndWait();
                		}
                	}
                	else
                	{
                		Alert notFoundAlert= new Alert(Alert.AlertType.ERROR, "The admin was not found!");
                		notFoundAlert.showAndWait();
                	}
                }
                
            });
            
            cancelButton.setOnAction(e -> {
            	getChildren().clear();
            	getChildren().add(accessPanel());
            });

            panel.getChildren().addAll(
                 specialGroup, specialGroupField, verifyButton,
                 studentsLabel, studentsField, addStudentButton,
                 instructorsLabel, instructorsField, addInstructorButton,
                 adminLabel, adminField, addAdminButton,
                 cancelButton
                 
            );
 
            return panel;
        }
        
        /**
         * Creates the login panel where users can enter their username and password.
         * @return VBox containing the login panel UI
         */
        private VBox removeAccess() {
            VBox panel = new VBox(10); // Vertical box layout
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");
            
            
            Label specialGroup = new Label("Enter special group to edit");
            TextField specialGroupField = new TextField();
            Button verifyButton = new Button("Verify Access");
            
            // Fields for adding students
            Label studentsLabel = new Label("Enter students to remove:");
            TextField studentsField = new TextField();
            Button removeStudentButton = new Button("Remove Student");
            

            // Fields for adding instructors
            Label instructorsLabel = new Label("Enter instructors to remove:");
            TextField instructorsField = new TextField();
            Button removeInstructorButton = new Button("Remove Instructor");

            // Fields for adding admins
            Label adminLabel = new Label("Enter admins to remove:");
            TextField adminField = new TextField();
            Button removeAdminButton = new Button("Remove Admin");

            // Submit button
            Button submitButton = new Button("Submit");
            Button cancelButton = new Button("Back"); // Button to login
            
            verifyButton.setOnAction(e -> {
            	String specialgroup = specialGroupField.getText().trim();
            	List<String> specialGroupList = specialgroup.isEmpty() ? null : List.of(specialgroup.split(","));
            	
            	System.out.println(admin.getUsername());
            	if(db.hasAccessToGroups(admin, List.of(specialgroup)))
            	{
            		Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Admin has access to special group");
                    successAlert.showAndWait();
            	}
            	else
            	{
            		Alert deniedAlert = new Alert(Alert.AlertType.ERROR, "Access Denied!");
            		deniedAlert.showAndWait();
            		getChildren().clear();
            		getChildren().add(accessPanel());
            	}
            });
            
            removeStudentButton.setOnAction(e -> {
            	String specialgroup = specialGroupField.getText().trim();
                String studentToRemove = studentsField.getText().trim();

                List<String> studentList = studentToRemove.isEmpty() ? null : List.of(studentToRemove.split(","));

                if(studentList != null)
                {
                	if(db.findStudentByUsername(studentToRemove) != null)
                	{
                		admin.removeStudentFromSpecialAccessGroup(db, specialgroup, db.findStudentByUsername(studentToRemove));	
                		Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Student removed from group");
                        successAlert.showAndWait();
                	}
                	else
                	{
                		Alert notFoundAlert= new Alert(Alert.AlertType.ERROR, "The student was not found!");
                		notFoundAlert.showAndWait();
                	}
                }
                
            });
            
            removeInstructorButton.setOnAction(e -> {
            	String specialgroup = specialGroupField.getText();
                String instructorToRemove = instructorsField.getText();
                List<String> instructorList = instructorToRemove.isEmpty() ? null : List.of(instructorToRemove.split(","));
                      
                if(instructorList != null)
                {
                	if(db.findUserByUsername(instructorToRemove) != null)
                	{
                		User userToRemove = db.findUserByUsername(instructorToRemove);
                		if (userToRemove instanceof Instructor) {
                            admin.removeInstructorAdminFromSpecialAccessGroup(db, specialgroup, (Instructor) userToRemove);
                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Instructor removed from group");
                            successAlert.showAndWait();
                		}
                	}
                	else
                	{
                		Alert notFoundAlert= new Alert(Alert.AlertType.ERROR, "The instructor was not found!");
                		notFoundAlert.showAndWait();
                	}
                }
                
            });
            
            removeAdminButton.setOnAction(e -> {
            	String specialgroup = specialGroupField.getText().trim();
                String adminToRemove = adminField.getText().trim();
                
                List<String> adminList = adminToRemove.isEmpty() ? null : List.of(adminToRemove.split(","));
                List<SpecialAccessGroup> groups = new ArrayList<>();
                SpecialAccessGroup specialAccessGroup = db.findSpecialAccessGroupByName(specialgroup);
                
                if(specialAccessGroup.getAdmins().size() == 1)
                {
                	System.out.println("if(specialAccessGroup.getAdmins().size() == 1)");
                	Alert onlyOneAdmin = new Alert(Alert.AlertType.ERROR, "There must be one admin per special access group!");
                	onlyOneAdmin.showAndWait();

                }else if (admin != null)
                {
                	if(db.findUserByUsername(adminToRemove) != null)
                	{
                		User userToRemove = db.findUserByUsername(adminToRemove);
                		if (userToRemove instanceof Admin) {
                			admin.removeAdminFromSpecialAccessGroup(db, specialgroup, (Admin) userToRemove);
                			Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Admin removed from group");
                			successAlert.showAndWait();
                		}
                	}
                	else
                	{
                		Alert notFoundAlert= new Alert(Alert.AlertType.ERROR, "The admin was not found!");
                		notFoundAlert.showAndWait();
                	}
                }
                
            });
            
            cancelButton.setOnAction(e -> {
            	getChildren().clear();
            	getChildren().add(accessPanel());
            });

            panel.getChildren().addAll(
                 specialGroup, specialGroupField, verifyButton,
                 studentsLabel, studentsField, removeStudentButton,
                 instructorsLabel, instructorsField, removeInstructorButton,
                 adminLabel, adminField, removeAdminButton,
                 cancelButton
                 
            );
 
            return panel;
        }
        
        private VBox listMembers() {
            VBox panel = new VBox(10); // Vertical box layout with spacing
            panel.setStyle("-fx-padding: 10; -fx-alignment: center;");
            
            Button returnButton = new Button("Back");
            Label instructionLabel = new Label("Enter group names to view member access:");
            TextField groupInputField = new TextField();
            groupInputField.setPromptText("E.g., Group1, Group2");
            Button listButton = new Button("List Members");

            // VBox to display the list of articles
            VBox articleDisplay = new VBox(5);
            articleDisplay.setStyle("-fx-padding: 10; -fx-background-color: #f9f9f9; -fx-border-color: #ccc;");
            
            returnButton.setOnAction(e -> {
            	getChildren().clear();
            	getChildren().add(accessPanel());
            });
            
            listButton.setOnAction(e -> {
                String groupInput = groupInputField.getText();
                List<String> groupNames = groupInput.isEmpty() ? null : List.of(groupInput.split(","));
                
                // Clear previous article display
                articleDisplay.getChildren().clear();
                
                List<SpecialAccessGroup> groups = new ArrayList<>();
                
                if(groupNames == null)
                {
                	groupNames = db.getallGroupNames();
                }
                
                List<String> inaccessibleGroups = new ArrayList<>();
                
                for (String name : groupNames) {
                    name = name.trim(); // Remove extra spaces
               
                    if (db.findSpecialAccessGroupByName(name) != null) {
            	        if (db.hasAccessToGroups(admin, List.of(name))) {
            	            groups.add(db.findSpecialAccessGroupByName(name));
            	        } else {
            	            inaccessibleGroups.add(name);
            	        }
            	    }                
                }
                
                
                for (SpecialAccessGroup specialGroup : groups) {
                	// Display each article with title, level, and short description
                	Label groupLabel = new Label("Group Name: " + specialGroup.getGroupName());
                	Label idLabel = new Label("Admins: " + specialGroup.getAdmins());
                	Label titleLabel = new Label("Instructors: " + specialGroup.getInstructors());
                	Label levelLabel = new Label("Students: " + specialGroup.getStudents());
                	VBox articleBox = new VBox(3, groupLabel, idLabel, titleLabel, levelLabel);
                	articleBox.setStyle("-fx-padding: 5; -fx-background-color: #e6e6e6; -fx-border-color: #ccc;");

                	articleDisplay.getChildren().add(articleBox);
                }
                
                if(inaccessibleGroups != null)
                {
                	Label inaccessibleLabel = new Label("Admins does not have access to these groups: ");
                	for(String inaccessibleGroupNames : inaccessibleGroups)
                    {
                     	Label groupLabel = new Label(inaccessibleGroupNames);
                    	VBox inaccessibleBox = new VBox(3, inaccessibleLabel, groupLabel); 	
                    	inaccessibleBox.setStyle("-fx-padding: 5; -fx-background-color: #e6e6e6; -fx-border-color: #ccc;");

                    	articleDisplay.getChildren().add(inaccessibleBox);
                    }
                }
                
            });

            panel.getChildren().addAll(returnButton, instructionLabel, groupInputField, listButton, articleDisplay);
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