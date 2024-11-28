package clueGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.awt.Color;

public class ComputerPlayer extends Player{
	private boolean readyToAccuse = false;
	private Solution accusation;
	
	public ComputerPlayer(String name, Color color, int row, int col) {
		super(name, color, row, col);
	}
	
	// function so that the computer players can make their own suggestions
	public Solution createSuggestion(Room currentRoom) {
	    Card roomCard = new Card(currentRoom.getName(), CardType.ROOM);

	    List<Card> unseenPeople = new ArrayList<>();
	    List<Card> unseenWeapons = new ArrayList<>();

	    // Filter unseen cards for people and weapons
	    for (Card card : Board.getInstance().getFullDeck()) {
	        if (!getSeenCards().contains(card)) { // Ensure card is unseen
	            if (card.getCardType() == CardType.PERSON) {
	                unseenPeople.add(card);
	            } else if (card.getCardType() == CardType.WEAPON) {
	                unseenWeapons.add(card);
	            }
	        }
	    }

	    Random random = new Random();

	    // Select random unseen person and weapon
	    Card personCard = unseenPeople.get(random.nextInt(unseenPeople.size()));
	    Card weaponCard = unseenWeapons.get(random.nextInt(unseenWeapons.size()));

	    return new Solution(roomCard, weaponCard, personCard);
	}
	
	// method to help computer players make decsions on what square to move to
	public BoardCell selectTargets(Set<BoardCell> targets) {
	    List<BoardCell> unseenRooms = new ArrayList<>();
	    List<BoardCell> otherTargets = new ArrayList<>();

	    for (BoardCell cell : targets) {
	    	// loop through the target cells
	        if (cell.isRoom() && !getSeenCards().contains(new Card(cell.getName(), CardType.ROOM))) {
	            unseenRooms.add(cell);
	        } else {
	            otherTargets.add(cell);
	        }
	    }

	    Random random = new Random();
	    if (!unseenRooms.isEmpty()) {
	        return unseenRooms.get(random.nextInt(unseenRooms.size()));
	    } else {
	        return otherTargets.get(random.nextInt(otherTargets.size()));
	    }
	}

	public boolean isReadyToAccuse() {
	    return readyToAccuse;
	}

	public void setReadyToAccuse(boolean readyToAccuse) {
	    this.readyToAccuse = readyToAccuse;
	}

	public Solution getAccusation() {
	    return accusation;
	}

	public void setAccusation(Solution accusation) {
	    this.accusation = accusation;
	}

	public void setLastSuggestionDisproved(boolean wasDisproved) {
	    if (!wasDisproved && !getHand().contains(accusation.getRoom())) {
	        readyToAccuse = true;
	    }
	}
}
