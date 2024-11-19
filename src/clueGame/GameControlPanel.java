package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    private JButton nextButton;
    private JButton accusationButton;
    private Board board;

    public GameControlPanel() {
        setLayout(new GridLayout(2,1));

        // make top half
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new GridLayout(1,2)); 
        turnPanel = createTurn();
        upperPanel.add(turnPanel);
        JPanel dicePanel = createDiceRoll();
        upperPanel.add(dicePanel);
        JPanel accusationPanel = createAccusationButton();
        upperPanel.add(accusationPanel);
        JPanel nextPanel = createNextButton();
        upperPanel.add(nextPanel);
        add(upperPanel);

        // make bottom half
        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new GridLayout(1,2));
        JPanel guessPanel = createGuess();
        lowerPanel.add(guessPanel);
        JPanel responsePanel = createResponse();
        lowerPanel.add(responsePanel);
        add(lowerPanel);

        // Add action listeners to the buttons
        nextButton.addActionListener(new NextButtonListener());
        accusationButton.addActionListener(new AccusationButtonListener());
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public JPanel createTurn() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2,1));
        turnLabel = new JTextField("Whose Turn?");
        turnLabel.setEditable(false);
        turnLabel.setBackground(Color.LIGHT_GRAY);
        nameLabel = new JTextField("");
        nameLabel.setEditable(false);
        panel.add(turnLabel);
        panel.add(nameLabel);
        return panel;
    }

    public JPanel createDiceRoll() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2,1));
        diceLabel = new JTextField("Roll:");
        diceLabel.setEditable(false);
        diceLabel.setBackground(Color.LIGHT_GRAY);
        diceRoll = new JTextField("");
        diceRoll.setEditable(false);
        panel.add(diceLabel);
        panel.add(diceRoll);
        return panel;
    }

    public JPanel createAccusationButton() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,0));
        accusationButton = new JButton("Make Accusation");
        panel.add(accusationButton);
        return panel;
    }

    public JPanel createNextButton() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,0));
        nextButton = new JButton("Next");
        panel.add(nextButton);
        return panel;
    }

    public JPanel createGuess() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,0));
        guessLabel = new JTextField("Guess:");
        guessLabel.setEditable(false);
        guessLabel.setBackground(Color.LIGHT_GRAY);
        guessText = new JTextField("");
        guessText.setEditable(false);
        panel.add(guessLabel);
        panel.add(guessText);
        return panel;
    }

    public JPanel createResponse() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,0));
        responseLabel = new JTextField("Response:");
        responseLabel.setEditable(false);
        responseLabel.setBackground(Color.LIGHT_GRAY);
        responseText = new JTextField("");
        responseText.setEditable(false);
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
        panel.setTurn(new ComputerPlayer( "Col. Mustard", "orange", 0, 0),5);
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
        this.nameLabel.setBackground(player.getColor());
        this.diceRoll.setText(Integer.toString(roll));
    }

    private class NextButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (board.isHumanMustFinish()) {
                JOptionPane.showMessageDialog(null, "You must finish your move before proceeding.", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                board.nextPlayer();
            }
        }
    }

    private class AccusationButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        	
        }
    }
}
