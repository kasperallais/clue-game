package tests;


import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
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

class GameSolutionTest {
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
    public void testCheckAccusation() {
		Solution mysolution = board.getSolution();
		Card extraRoom = new Card("extraRoom", CardType.ROOM);
		Card extraPerson = new Card("extraperson", CardType.PERSON);
		Card extraWeapon = new Card("extraweapon", CardType.WEAPON);
		assertTrue(board.checkAccusation(mysolution.getRoom(), mysolution.getPerson(), mysolution.getWeapon()));
		
		// wrong room
		assertFalse(board.checkAccusation(extraRoom, mysolution.getPerson(), mysolution.getWeapon()));
		// wrong person
		assertFalse(board.checkAccusation(mysolution.getRoom(), extraPerson, mysolution.getWeapon()));
		// wrong weapon
		assertFalse(board.checkAccusation(mysolution.getRoom(), mysolution.getPerson(), extraWeapon));
    }
	
	@Test
	public void testDisproveSuggestion() {
		Player testPlayer = new HumanPlayer("TestPlayer", Color.CYAN, 5, 5);
		Card extraRoom = new Card("extraRoom", CardType.ROOM);
		Card extraPerson = new Card("extraperson", CardType.PERSON);
		Card extraWeapon = new Card("extraweapon", CardType.WEAPON);
		
		Card wrongRoom = new Card("wrongRoom", CardType.ROOM);
		Card wrongPerson = new Card("wrongPerson", CardType.PERSON);
		Card wrongWeapon = new Card("wrongWeapon", CardType.WEAPON);
		testPlayer.updateHand(extraRoom);
		testPlayer.updateHand(extraPerson);
		testPlayer.updateHand(extraWeapon);
		
		// test one card matches
		assertEquals(extraRoom, testPlayer.disproveSuggestion(extraRoom, wrongPerson, wrongWeapon));
		assertEquals(extraPerson, testPlayer.disproveSuggestion(wrongRoom, extraPerson, wrongWeapon));
		
		// test multiple match
		for (int i = 0; i < 10; i++) {
			Card returned = testPlayer.disproveSuggestion(extraRoom, extraPerson, extraWeapon);
			if (returned.getName().equals(extraRoom.getName())) {
				assertTrue(true);
			}
		}
		for (int i = 0; i < 10; i++) {
			Card returned = testPlayer.disproveSuggestion(extraRoom, extraPerson, extraWeapon);
			if (returned.getName().equals(extraWeapon.getName())) {
				assertTrue(true);
			}
		}
		
		// test no card matches
		assertEquals(null, testPlayer.disproveSuggestion(wrongRoom, wrongPerson, wrongWeapon));
	}
	
	@Test
	public void testHandleSuggestion() {
		Player testPlayer1 = new HumanPlayer("TestPlayer1", Color.CYAN, 5, 5);
		Player testPlayer2 = new HumanPlayer("TestPlayer2", Color.PINK, 6, 6);
		Player testPlayer3 = new HumanPlayer("TestPlayer3", Color.ORANGE, 7, 7);
		
		Card p1Room = new Card("p1Room", CardType.ROOM);
		Card p2Room = new Card("p2Room", CardType.ROOM);
		Card p3Room = new Card("p3Room", CardType.ROOM);
		
		Card p1Person = new Card("p1Person", CardType.PERSON);
		Card p2Person = new Card("p2Person", CardType.PERSON);
		Card p3Person = new Card("p3Person", CardType.PERSON);
		
		Card p1Weapon = new Card("p1Weapon", CardType.WEAPON);
		Card p2Weapon = new Card("p2Weapon", CardType.WEAPON);
		Card p3Weapon = new Card("p3Weapon", CardType.WEAPON);
		
		Card extraRoom = new Card("extraRoom", CardType.ROOM);
		Card extraPerson = new Card("extraperson", CardType.PERSON);
		Card extraWeapon = new Card("extraweapon", CardType.WEAPON);
		
		testPlayer1.updateHand(p1Room);
		testPlayer2.updateHand(p2Room);
		testPlayer3.updateHand(p3Room);
		
		testPlayer1.updateHand(p1Person);
		testPlayer2.updateHand(p2Person);
		testPlayer3.updateHand(p3Person);
		
		testPlayer1.updateHand(p1Weapon);
		testPlayer2.updateHand(p2Weapon);
		testPlayer3.updateHand(p3Weapon);
		
		board.addTestingPlayers();
		board.addPlayerTest(testPlayer1);
		board.addPlayerTest(testPlayer2);
		board.addPlayerTest(testPlayer3);
		
		// no players have the cards suggested
		assertEquals(null, board.handleSuggestion(testPlayer1, extraRoom, extraPerson, extraWeapon));
		assertEquals(null, board.handleSuggestion(testPlayer2, extraRoom, extraPerson, extraWeapon));
		assertEquals(null, board.handleSuggestion(testPlayer3, extraRoom, extraPerson, extraWeapon));
		
		
		// check to see if player 1 suggests cards that they have it returns null
		assertEquals(null, board.handleSuggestion(testPlayer1, p1Room, p1Person, p1Weapon));
		assertEquals(null, board.handleSuggestion(testPlayer1, p1Room, extraPerson, p1Weapon));
		assertEquals(null, board.handleSuggestion(testPlayer1, extraRoom, p1Person, extraWeapon));
		
		// player 1 accuser and both player 2 and 3 can disprove
		assertEquals(p2Person, board.handleSuggestion(testPlayer1, extraRoom, p2Person, p3Weapon));
		assertNotEquals(p3Weapon, board.handleSuggestion(testPlayer1, extraRoom, p2Person, p3Weapon));
		
		// player 1 accuser and player 3 can disprove
		assertEquals(p3Weapon, board.handleSuggestion(testPlayer1, extraRoom, extraPerson, p3Weapon));
		
		// player 1 accuser and player 2 can disprove
		assertEquals(p2Room, board.handleSuggestion(testPlayer1, p2Room, extraPerson, extraWeapon));
	}

}
