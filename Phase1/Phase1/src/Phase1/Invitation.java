package Phase1;

public class Invitation {
    private String invitationCode;
    private boolean isUsed = false;

    public Invitation(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public boolean isValid() {
        return !isUsed; // if the code is expired or used
    }

    public void markAsUsed() {
        isUsed = true;
    }
}