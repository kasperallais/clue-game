package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.Player;
import experiment.TestBoardCell;

class GameSetupTests {
	private static Board board;
	
	/*
	 * I changed cluesetup.txt and added in players and weapons
	 * The config functions loads them in
	 * I added two new arraylists at the top called players and cardDeck
	 * players holds all the players (6)
	 * cardDeck holds all room card, person cards, and weapon cards
	 * This is all done in loadSetupConfig
	 * I started deal cards but i mean you can restart it i havent really done much on that
	 */
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
    public void testPlayersSetup() {
		ArrayList<Player> myArray = board.getPlayers();
        assertEquals(6, myArray.size());
        assertEquals("Alex Honnold", myArray.get(4).getName());
        assertEquals(10, myArray.get(5).getCol());
    }

	@Test
    public void testCardSetup() {
		ArrayList<Card> myArray = board.getDeck();
        assertEquals(21, myArray.size());
        assertEquals(CardType.ROOM, myArray.get(0).getCardType());
        assertEquals(CardType.PERSON, myArray.get(12).getCardType());
        assertEquals(CardType.WEAPON, myArray.get(20).getCardType());
        assertEquals("Chair", myArray.get(17).getName());
    }

}
