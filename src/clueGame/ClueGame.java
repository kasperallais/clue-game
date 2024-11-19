package clueGame;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;

public class ClueGame extends JFrame {
    private static final int WIDTH = 900;
    private static final int HEIGHT = 750;

    private Board board;
    private GameControlPanel controlPanel;

    public ClueGame() {
        setTitle("Clue Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize the board
        board = Board.getInstance();
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt"); // set the files
        board.initialize();

        // Add the board to the center
        add(board, BorderLayout.CENTER);

        // Add the GameControlPanel to the south
        controlPanel = new GameControlPanel();
        add(controlPanel, BorderLayout.SOUTH);

        // Add the KnownCardsPanel to the east
        GameCardPanel cardPanel = new GameCardPanel();
        add(cardPanel, BorderLayout.EAST);

        // Set references so Board and GameControlPanel can communicate
        board.setControlPanel(controlPanel);
        controlPanel.setBoard(board);
    }

    public static void main(String[] args) {
        ClueGame clueGame = new ClueGame();
        clueGame.setVisible(true);
        // Display the splash screen
        JOptionPane.showMessageDialog(clueGame, "Welcome to Clue! \nYou are playing as " + Board.getInstance().getHumanPlayer().getName() + ".\nPress OK to start the game.", "Welcome to Clue", JOptionPane.INFORMATION_MESSAGE);
        // Process the first player's turn
        clueGame.board.nextPlayer();
    }
}
