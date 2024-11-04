package tests;


import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Room;
import clueGame.Solution;;

class ComputerAITest {
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}
	
	@Test
	public void testCreateSuggestion() {
	    ComputerPlayer player = new ComputerPlayer("Test AI", "Blue", 5, 5);

	    // Set up the current room
	    Room currentRoom = new Room("Library");

	    // Unseen cards
	    Card unseenPerson = new Card("Professor Plum", CardType.PERSON);
	    Card unseenWeapon = new Card("Knife", CardType.WEAPON);

	    // Add seen cards to the player
	    player.addSeenCard(new Card("Colonel Mustard", CardType.PERSON)); // Different person
	    player.addSeenCard(new Card("Candlestick", CardType.WEAPON));     // Different weapon

	    Solution suggestion = player.createSuggestion(currentRoom);

	    // Verify room matches the current location
	    assertEquals("Library", suggestion.getRoom().getName(), "Room should match current location");

	    // Verify that person and weapon in suggestion are unseen
	    assertFalse(player.getSeenCards().contains(suggestion.getPerson()), "Person should be unseen");
	    assertFalse(player.getSeenCards().contains(suggestion.getWeapon()), "Weapon should be unseen");
	}

	@Test
	public void testSelectTargets() {
	    ComputerPlayer player = new ComputerPlayer("Test AI", "Blue", 5, 5);

	    // Create a set of possible targets
	    Set<BoardCell> targets = new HashSet<>();
	    BoardCell roomCell = new BoardCell(1, 1);
	    roomCell.setRoomName("Kitchen");
	    roomCell.setRoom(true);

	    BoardCell walkway1 = new BoardCell(2, 2);
	    BoardCell walkway2 = new BoardCell(3, 3);
	    targets.add(roomCell);
	    targets.add(walkway1);
	    targets.add(walkway2);

	    // Add seen cards to ensure the room is treated as unseen
	    player.addSeenCard(new Card("Dining Room", CardType.ROOM));  // Different room marked as seen

	    // Assume roomCell is the target
	    BoardCell selected = player.selectTargets(targets);

	    // Check if the selected target matches the expected room cell
	    assertEquals(roomCell.getRow(), selected.getRow(), "Selected row should match the room row");
	    assertEquals(roomCell.getCol(), selected.getCol(), "Selected column should match the room column");
	}
}