import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateAccount {
    
    private JFrame frame;
    private CardLayout cardLayout;
    
    public CreateAccount() {
        // Create the main frame
        frame = new JFrame("Login System");
        cardLayout = new CardLayout();
        frame.setLayout(cardLayout);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        
        // Create panels for each screen
        JPanel welcomePanel = createWelcomePanel();
        JPanel signupPanel = createSignupPanel();
        
        // Add panels to frame
        frame.add(welcomePanel, "Welcome!");
        frame.add(signupPanel, "Sign Up");
        
        frame.setVisible(true);
    }
    
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        
        JLabel welcomeLabel = new JLabel("Welcome to the Login System");
        JButton createAccountButton = new JButton("Create Account");
        
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(frame.getContentPane(), "Sign Up");
            }
        });
        
        panel.add(welcomeLabel);
        panel.add(createAccountButton);
        
        return panel;
    }
    
    private JPanel createSignupPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));
        
        JLabel firstNameLabel = new JLabel("First Name:");
        JTextField firstNameField = new JTextField();
        JLabel middleNameLabel = new JLabel("Middle Name:");
        JTextField middleNameField = new JTextField();
        JLabel lastNameLabel = new JLabel("Last Name:");
        JTextField lastNameField = new JTextField();
        JLabel preferredNameLabel = new JLabel("Preferred Name:");
        JTextField preferredNameField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        Password passwordObj = new Password();
        JButton passwordButton = new JButton("Set Password");
        
        passwordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passwordObj.setPassword();
                // Save the password securely here
                // Example: Save to the User object
                // User user = new User(firstNameField.getText(), lastNameField.getText(), emailField.getText(), usernameField.getText(), passwordObj.getPassword());
                // Show success message or proceed
                JOptionPane.showMessageDialog(panel, "Account created successfully!");
                // Optionally, go back to welcome screen
                cardLayout.show(frame.getContentPane(), "Welcome");
            }
        });
        
        panel.add(firstNameLabel);
        panel.add(firstNameField);
        panel.add(middleNameLabel);
        panel.add(middleNameField);
        panel.add(lastNameLabel);
        panel.add(lastNameField);
        panel.add(preferredNameLabel);
        panel.add(preferredNameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordButton);
        
        return panel;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CreateAccount());
    }
}