package clueGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class Player {
	private String name;
	private String color;
	private int row;
	private int col;
	private ArrayList<Card> hand;
	private Set<Card> seenCards;

	Player(String name, String color, int row, int col){
		this.name = name;
		this.color = color;
		this.row = row;
		this.col = col;
		this.hand = new ArrayList<>();
	}

	public String getName() {
		return this.name;
	}

	public int getCol() {
		return this.col;
	}

	public int getRow() {
		return this.row;
	}

	public ArrayList<Card> getHand() {
		return hand;
	}

	public void updateHand(Card card) {
		hand.add(card);
	}

	public Card disproveSuggestion(Card suggestionRoom, Card suggestionPerson, Card suggestionWeapon) {
		Random random = new Random();
		int rn = random.nextInt(2 - 0 + 0) + 0;

		seenCards = new HashSet<Card>();
		for (int i = 0; i < hand.size(); i++) {
			if (hand.get(i).getName().equals(suggestionRoom.getName())) {
				seenCards.add(hand.get(i));
			}
			else if (hand.get(i).getName().equals(suggestionPerson.getName())) {
				seenCards.add(hand.get(i));
			}
			else if (hand.get(i).getName().equals(suggestionWeapon.getName())) {
				seenCards.add(hand.get(i));
			}
		}

		if (seenCards.size() == 1){
			for (Card card: seenCards) {
				return card;
			}
		} else if (seenCards.size() > 1){
			
			int counter = 0;
			for (Card card: seenCards) {
				if (counter == rn) {
					return card;
				}
				counter ++;
			}
		}
		
		seenCards.clear();
		return null;

	}
}
