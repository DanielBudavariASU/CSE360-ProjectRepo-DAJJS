package trial;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The SpecialAccessGroup class represents a special group with restricted access permissions.
 * It includes lists of admins, instructors, and students with rights to view or manage help articles in this group.
 */
public class SpecialAccessGroup extends HelpGroup implements Serializable {
    private static final long serialVersionUID = 1L;

    // Lists of users with special access rights
    private List<String> adminAccess;              // Admins who have rights to manage access in this group
    private List<String> instructorAccess;    // Instructors who have view or manage rights to articles in this group
    private List<String> instructorAdmins;    // Instructors with admin rights for this group
    private List<String> studentAccess;          // Students who can view articles in this group

    /**
     * Constructor for SpecialAccessGroup.
     *
     * @param groupName The name of the special access group.
     */
    public SpecialAccessGroup(String groupName) {
        super(groupName);
        this.adminAccess = new ArrayList<>();
        this.instructorAccess = new ArrayList<>();
        this.instructorAdmins = new ArrayList<>();
        this.studentAccess = new ArrayList<>();
    }

    /**
     * Adds an admin with special rights to this group.
     *
     * @param admin The admin to add.
     */
    public void addAdmin(Admin admin) {
        if (!adminAccess.contains(admin.getUsername())) {
            adminAccess.add(admin.getUsername());
        }
    }

    /**
     * Adds an instructor with rights to view or manage articles in this group.
     * The first instructor added gets admin rights by default.
     *
     * @param instructor The instructor to add.
     */
    public void addInstructor(Instructor instructor) {
        if (!instructorAccess.contains(instructor.getUsername())) {
            instructorAccess.add(instructor.getUsername());
            if (instructorAccess.size() == 1) {
                // The first instructor added gets admin rights for this group
                addInstructorAdmin(instructor);
            }
        }
    }

    /**
     * Adds an instructor to the list of those with admin rights for this group.
     *
     * @param instructor The instructor to add as an admin.
     */
    public void addInstructorAdmin(Instructor instructor) {
        if (!instructorAdmins.contains(instructor.getUsername())) {
            instructorAdmins.add(instructor.getUsername());
        }
    }

    /**
     * Adds a student with viewing rights to this group.
     *
     * @param student The student to add.
     */
    public void addStudent(Student student) {
        if (!studentAccess.contains(student.getUsername())) {
            studentAccess.add(student.getUsername());
        }
    }

    /**
     * Removes an admin from the group.
     * There must always be at least one admin in the group.
     *
     * @param admin The admin to remove.
     */
    public void removeAdmin(Admin admin) {
        if (adminAccess.size() > 1) {
            adminAccess.remove(admin.getUsername());
        } else {
            System.out.println("Error: Cannot remove the last admin. There must always be at least one admin.");
        }
    }

    /**
     * Removes an instructor from the group.
     *
     * @param instructor The instructor to remove.
     */
    public void removeInstructor(Instructor instructor) {
        instructorAccess.remove(instructor);
        instructorAdmins.remove(instructor.getUsername()); // Remove if instructor has admin rights
    }

    /**
     * Removes a student from the group.
     *
     * @param student The student to remove.
     */
    public void removeStudent(Student student) {
        studentAccess.remove(student.getUsername());
    }

    /**
     * Checks if a user is allowed to view articles in this group.
     *
     * @param user The user to check.
     * @return true if the user has viewing rights, false otherwise.
     */
    public boolean hasAccess(User user) {
        if (user instanceof Admin) {
            return adminAccess.contains(user.getUsername());
        } else if (user instanceof Instructor) {
            return instructorAccess.contains(user.getUsername()) || instructorAdmins.contains(user.getUsername());
        } else if (user instanceof Student) {
            return studentAccess.contains(user.getUsername());
        }
        System.out.println("HERE-4");
        return false;
    }

    /**
     * Checks if an instructor has admin rights for this group.
     *
     * @param instructor The instructor to check.
     * @return true if the instructor has admin rights, false otherwise.
     */
    public boolean isInstructorAdmin(Instructor instructor) {
        return instructorAdmins.contains(instructor.getUsername());
    }

    /**
     * Checks if a user has admin rights for this group.
     *
     * @param user The user to check.
     * @return true if the user has admin rights, false otherwise.
     */
    public boolean isAdmin(User user) {
        return adminAccess.contains(user.getUsername()) || (user instanceof Instructor && instructorAdmins.contains(user.getUsername()));
    }

    /**
     * Lists all members with access to this group.
     *
     * @return A string listing all members with access.
     */
    public String listAccessMembers() {
        StringBuilder sb = new StringBuilder();
        sb.append("Admins: ");
        adminAccess.forEach(admin -> sb.append(admin).append(", "));
        sb.append("\nInstructor Admins: ");
        instructorAdmins.forEach(instructor -> sb.append(instructor).append(", "));
        sb.append("\nInstructors: ");
        instructorAccess.forEach(instructor -> {
            if (!instructorAdmins.contains(instructor)) {
                sb.append(instructor).append(", ");
            }
        });
        sb.append("\nStudents: ");
        studentAccess.forEach(student -> sb.append(student).append(", "));
        return sb.toString();
    }
    
   

    /**
     * Adds an instructor as an admin for this group.
     * This method can be used to manually promote an instructor to admin rights.
     *
     * @param instructor The instructor to promote.
     */
    public void promoteInstructorToAdmin(Instructor instructor) {
        if (instructorAccess.contains(instructor.getUsername()) && !instructorAdmins.contains(instructor.getUsername())) {
            addInstructorAdmin(instructor);
        }
    }

    /**
     * Demotes an instructor from admin rights for this group.
     * The instructor will still have viewing rights, but not admin rights.
     *
     * @param instructor The instructor to demote.
     */
    public void demoteInstructorFromAdmin(Instructor instructor) {
        instructorAdmins.remove(instructor.getUsername());
    }
    
    public List<String> getStudents(){
    	return studentAccess;
    }
    
    public List<String> getAdmins(){
    	return adminAccess;
    }
    
    public List<String> getInstructors(){
    	return instructorAdmins;
    }
}