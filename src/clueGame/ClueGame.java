package clueGame;

import javax.swing.JFrame;
import java.awt.BorderLayout;

public class ClueGame extends JFrame {
    private static final int WIDTH = 900;
    private static final int HEIGHT = 750;

    public ClueGame() {
        setTitle("Clue Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize the board
        Board board = Board.getInstance();
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt"); // set the files
        board.initialize();

        // Add the board to the center
        add(board, BorderLayout.CENTER);

        // Add the GameControlPanel to the south
        GameControlPanel controlPanel = new GameControlPanel();
        add(controlPanel, BorderLayout.SOUTH);

        // Add the KnownCardsPanel to the east
        GameCardPanel cardPanel = new GameCardPanel();
        add(cardPanel, BorderLayout.EAST);
    }

    public static void main(String[] args) {
        ClueGame clueGame = new ClueGame();
        clueGame.setVisible(true);
    }
}
