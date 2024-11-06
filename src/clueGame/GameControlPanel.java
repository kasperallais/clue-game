package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


public class GameControlPanel extends JPanel {

	private JTextField guessText;
	private JTextField guessLabel;

	private JTextField diceRoll;
	private JTextField diceLabel;
    private JTextField responseText;
    private JTextField responseLabel;
    private JTextField nameLabel;
    private JTextField turnLabel;
    private JTextField bgColor =  new JTextField("white");
    private JPanel turnPanel; 
	

	JButton next, accuse;

	public GameControlPanel(){
		setLayout(new GridLayout(2,1));
		
		// make top half
		JPanel upperPanel = new JPanel();
		upperPanel.setLayout(new GridLayout(1,2));  
		JPanel turnPanel = createTurn();
		upperPanel.add(turnPanel);
		add(upperPanel);
		JPanel dicePanel = createDiceRoll();
		upperPanel.add(dicePanel);
		add(upperPanel);
		JPanel accusationPanel = createAccusationButton();
		upperPanel.add(accusationPanel);
		add(upperPanel);
		JPanel nextPanel = createNextButton();
		upperPanel.add(nextPanel);
		add(upperPanel);
		
		// make bottom half
		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new GridLayout(1,2));
		JPanel guessPanel = createGuess();
		lowerPanel.add(guessPanel);
		add(lowerPanel);
		JPanel responsePanel = createResponse();
		lowerPanel.add(responsePanel);
		add(lowerPanel);
	}
	
	public JPanel createTurn() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,2));
		nameLabel = new JTextField("");
		nameLabel.setBorder(new EtchedBorder());
		turnLabel = new JTextField("Whose Turn?");
		panel.add(turnLabel);
		panel.add(nameLabel);
		return panel;
	}
	
	public JPanel createDiceRoll() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,2));
		diceRoll = new JTextField("");
		diceRoll.setBorder(new EtchedBorder());
		diceLabel = new JTextField("Roll:");
		diceLabel.setBorder(new EtchedBorder());
		panel.add(diceLabel);
		panel.add(diceRoll);
		return panel;
	}
	
	public JPanel createAccusationButton() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,0));
		JButton button = new JButton("Make Accusation");
		panel.add(button);
		return panel;
	}
	
	public JPanel createNextButton() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,0));
		JButton button = new JButton("Next");
		panel.add(button);
		return panel;
	}
	
	public JPanel createGuess() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,0));
		guessLabel = new JTextField("Guess:");
		guessText = new JTextField("");
		guessLabel.setBorder(new EtchedBorder());
		guessText.setBorder(new EtchedBorder());
		panel.add(guessLabel);
		panel.add(guessText);
		return panel;
	}
	
	public JPanel createResponse() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,0));
		responseLabel = new JTextField("Response:");
		responseText = new JTextField("");
		responseLabel.setBorder(new EtchedBorder());
		responseText.setBorder(new EtchedBorder());
		panel.add(responseLabel);
		panel.add(responseText);
		return panel;
	}
	
	public static void main(String[] args) {
		GameControlPanel panel = new GameControlPanel();  // create the panel
		JFrame frame = new JFrame();  // create the frame 
		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(750, 180);  // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible
		
		// test filling in the data
		panel.setTurn(new ComputerPlayer( "Col. Mustard", "orange", 0, 0), 5);
		panel.setGuess( "I have no guess!");
		panel.setGuessResult( "So you have nothing?");
	}
	
		
	public void setGuess(String s) {
		this.guessText.setText(s);
	}
	
	public void setGuessResult(String s) {
		this.responseText.setText(s);
	}
	
	public void setTurn(Player player, int roll) {
		this.nameLabel.setText(player.getName());
		this.bgColor.setText(player.getColor());
		this.diceRoll.setText((Integer.toString(roll)));
		
		if (bgColor.getText().equals("orange")) {
            nameLabel.setBackground(Color.ORANGE);
        } else {
            nameLabel.setBackground(Color.WHITE);
        }
		
	}
}