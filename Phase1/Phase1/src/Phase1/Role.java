package Phase1;
import 

public class Role {
	private String roleName;

    public Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
	
	public String removeRole() {
		this.roleName = "";
		Alert alert = new Alert(Alert.AlertType.showAndWait());
        alert.setTitle("Removed Role");
        alert.setHeaderText("The user's role has been revoked");
	}
}
