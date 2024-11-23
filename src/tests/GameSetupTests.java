package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.Player;
import clueGame.Solution;

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
        assertEquals(6, myArray.get(5).getCol());
    }

	@Test
    public void testCardSetup() {
		ArrayList<Card> myArray = board.getFullDeck();
        assertEquals(21, myArray.size());
        assertEquals(CardType.ROOM, myArray.get(0).getCardType());
        assertEquals(CardType.PERSON, myArray.get(12).getCardType());
        assertEquals(CardType.WEAPON, myArray.get(20).getCardType());
        assertEquals("Chair", myArray.get(17).getName());
    }
	
    @Test
    public void testSolutionSetup() {
        Solution solution = board.getSolution();
        
        // Ensure that the solution has one room, one person, and one weapon
        assertNotNull(solution.getRoom(), "Solution room card is null");
        assertNotNull(solution.getPerson(), "Solution person card is null");
        assertNotNull(solution.getWeapon(), "Solution weapon card is null");

        // Verify solution cards are removed from the deck
        ArrayList<Card> deck = board.getDeck();
        assertFalse(deck.contains(solution.getRoom()), "Solution room card should not be in deck");
        assertFalse(deck.contains(solution.getPerson()), "Solution person card should not be in deck");
        assertFalse(deck.contains(solution.getWeapon()), "Solution weapon card should not be in deck");
    }

    @Test
    public void testCardDealing() {
        ArrayList<Player> players = board.getPlayers();
        ArrayList<Card> deck = board.getDeck();

        // Ensure all remaining cards are dealt to players
        int totalCardsDealt = players.stream().mapToInt(player -> player.getHand().size()).sum();
        assertEquals(deck.size(), totalCardsDealt, "All cards should be dealt");

        // Verify each player has approximately the same number of cards
        int minCards = players.get(0).getHand().size();
        int maxCards = players.get(0).getHand().size();
        for (Player player : players) {
            int handSize = player.getHand().size();
            if (handSize < minCards) minCards = handSize;
            if (handSize > maxCards) maxCards = handSize;
        }
        assertTrue(maxCards - minCards <= 1, "Players should have roughly the same number of cards");

        // Ensure no card is dealt to more than one player
        Set<Card> allDealtCards = new HashSet<>();
        for (Player player : players) {
            for (Card card : player.getHand()) {
                assertTrue(allDealtCards.add(card), "Duplicate card found in players' hands");
            }
        }
    }
}
