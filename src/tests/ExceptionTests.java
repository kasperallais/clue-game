package tests;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;
import clueGame.BadConfigFormatException;
import clueGame.Board;

public class ExceptionTests {
    @Test
    public void testBadColumns() throws BadConfigFormatException {
        assertThrows(BadConfigFormatException.class, () -> {
            Board board = Board.getInstance();
            board.setConfigFiles("BadLayout.csv", "ClueSetup.txt");
            board.loadSetupConfig();
            board.loadLayoutConfig();
        });
    }

    @Test
    public void testBadRoom() throws BadConfigFormatException {
        assertThrows(BadConfigFormatException.class, () -> {
            Board board = Board.getInstance();
            board.setConfigFiles("ClueLayout.csv", "BadSetup.txt");
            board.loadSetupConfig();
            board.loadLayoutConfig();
        });
    }
    
	@Test
	public void testBadRoomFormat() throws BadConfigFormatException, FileNotFoundException {
		assertThrows(BadConfigFormatException.class, () -> {
			Board board = Board.getInstance();
			board.setConfigFiles("ClueLayout.csv", "ClueSetupBadFormat.txt");
			board.loadSetupConfig();
			board.loadLayoutConfig();
		});
	}
}
