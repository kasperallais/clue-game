package clueGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player{
	public ComputerPlayer(String name, String color, int row, int col) {
		super(name, color, row, col);
	}
	
	public Solution createSuggestion(Room currentRoom) {
	    Card roomCard = new Card(currentRoom.getName(), CardType.ROOM);

	    List<Card> unseenPeople = new ArrayList<>();
	    List<Card> unseenWeapons = new ArrayList<>();

	    for (Card card : Board.getInstance().getFullDeck()) {
	        if (!getSeenCards().contains(card) && card.getCardType() == CardType.PERSON) {
	            unseenPeople.add(card);
	        } else if (!getSeenCards().contains(card) && card.getCardType() == CardType.WEAPON) {
	            unseenWeapons.add(card);
	        }
	    }

	    Random random = new Random();
	    Card personCard = unseenPeople.get(random.nextInt(unseenPeople.size()));
	    Card weaponCard = unseenWeapons.get(random.nextInt(unseenWeapons.size()));

	    return new Solution(roomCard, weaponCard, personCard);
	}

	
	public BoardCell selectTargets(Set<BoardCell> targets) {
	    List<BoardCell> unseenRooms = new ArrayList<>();
	    List<BoardCell> otherTargets = new ArrayList<>();

	    for (BoardCell cell : targets) {
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
}
