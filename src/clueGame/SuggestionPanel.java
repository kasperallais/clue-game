package clueGame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SuggestionPanel extends JDialog {
	private JComboBox<String> roomComboBox;
    private JComboBox<String> personComboBox;
    private JComboBox<String> weaponComboBox;
    private JButton submitButton;
    private JButton cancelButton;
    private boolean didPressCancel;

    public SuggestionPanel(JFrame parent, String currentRoom, boolean checkAccusation, String title) {
        super(parent, title, true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // take in paramter to see if program wants to build the suggestion panel or accusation
        if (!checkAccusation) {
	        // Current room label
	        gbc.gridx = 0;
	        gbc.gridy = 0;
	        add(new JLabel("Current room:"), gbc);
	
	        gbc.gridx = 1;
	        gbc.gridy = 0;
	        add(new JLabel(currentRoom), gbc);
        } else {
        	gbc.gridx = 0;
	        gbc.gridy = 0;
	        
	        add(new JLabel("Room:"), gbc);
	
	        gbc.gridx = 1;
	        gbc.gridy = 0;
	        roomComboBox = new JComboBox<>(new String[] {"Library of Secrets", "Enchanted Garden", "Shadow Study", "Ballroom of Echos", 
	        											 "The velvet Lounge", "The Gloomy Cellar", "The Hall of Mirrors", "The Crimson Parlor",
	        											  "The Observatory Tower"});
	        add(roomComboBox, gbc);
        }
        // Person label and combo box
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Person:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        personComboBox = new JComboBox<>(new String[]{"Big Man", "Toby Mack", "John Cena", "Max Verstappen", "Alex Honnold", "George Washington"});
        add(personComboBox, gbc);

        // Weapon label and combo box
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Weapon:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        weaponComboBox = new JComboBox<>(new String[]{"Candlestick", "Knife", "Revolver", "Chair", "Wrench", "Rope"});
        add(weaponComboBox, gbc);

        // Submit and Cancel buttons
        JPanel buttonPanel = new JPanel();
        submitButton = new JButton("Submit");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        // Add action listeners
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedPerson = (String) personComboBox.getSelectedItem();
                String selectedWeapon = (String) weaponComboBox.getSelectedItem();
                dispose(); // Close the dialog
            }
        });

        
        cancelButton.addActionListener(e -> {
            didPressCancel = true; // Set cancel flag
            dispose();
        });
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                didPressCancel = true; // Set cancel flag
            }
        });

        // Set dialog properties
        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            SuggestionPanel dialog = new SuggestionPanel(frame, "Lounge", false, "Make a Suggestion");
            dialog.setVisible(true);
        });
    }
    
    public String getSelectedPerson() {
        return (String) personComboBox.getSelectedItem();
    }

    public String getSelectedWeapon() {
        return (String) weaponComboBox.getSelectedItem();
    }
    
    public String getSelectedRoom() {
    	return (String) roomComboBox.getSelectedItem();
    }
    
    public boolean pressCancel() {
    	return this.didPressCancel;
    }

}
