package clueGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.Field;

public abstract class Player {
    private String name;
    private Color color;
    private int row;
    private int col;
    private ArrayList<Card> hand;
    private Set<Card> seenCards;
    private boolean isOut = false;
    private boolean movedBySuggestion = false;


    Player(String name, Color color, int row, int col){
        this.name = name;
        this.color = color;
        this.row = row;
        this.col = col;
        this.hand = new ArrayList<>();
        this.seenCards = new HashSet<Card>();
    }

    // convert to color for the player if needed
    private Color convertColor(String strColor) {
        Color color;
        try {
            Field field = Class.forName("java.awt.Color").getField(strColor.toLowerCase());
            color = (Color)field.get(null);
        } catch (Exception e) {
            color = Color.WHITE; // Default color if not found
        }
        return color;
    }

    // method to draw the players onto the board to make sure right size and color
    public void draw(Graphics g, int cellWidth, int cellHeight) {
        int x = this.col * cellWidth;
        int y = this.row * cellHeight;
        g.setColor(this.color);
        int playerSize = Math.min(cellWidth, cellHeight) / 2;
        g.fillOval(x + cellWidth / 2 - playerSize, y + cellHeight / 2 - playerSize, playerSize*2, playerSize*2);
        g.setColor(Color.BLACK);
        g.drawOval(x + cellWidth / 2 - playerSize, y + cellHeight / 2 - playerSize, playerSize*2, playerSize*2);
    }
	
	// method to check to see if a player has the cards in the suggestion and disprove it if so
	public Card disproveSuggestion(Card suggestionRoom, Card suggestionPerson, Card suggestionWeapon) {
		Random random = new Random();
		int rn = random.nextInt(2 - 0 + 0) + 0;
		
		Set<Card> extraCards = new HashSet<Card>();
		for (int i = 0; i < hand.size(); i++) {
			// loop through hand and see if the suggestion cards are in the hand of this player
			if (hand.get(i).getName().equals(suggestionRoom.getName())) {
				extraCards.add(hand.get(i));
			}
			else if (hand.get(i).getName().equals(suggestionPerson.getName())) {
				extraCards.add(hand.get(i));
			}
			else if (hand.get(i).getName().equals(suggestionWeapon.getName())) {
				extraCards.add(hand.get(i));
			}
		}
		
		// if only one card matches the suggestion return it
		if (extraCards.size() == 1){
			for (Card card: extraCards) {
				return card;
			}
		// if multiple cards match suggestion return them all
		} else if (extraCards.size() > 1){
			
			int counter = 0;
			for (Card card: extraCards) {
				if (counter == rn) {
					return card;
				}
				counter ++;
			}
		}
		
		extraCards.clear();
		return null;

	}
	
	// getters and setters methods
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
	
	public Set<Card> getSeenCards() {
	    return seenCards;
	}

	public void updateHand(Card card) {
		hand.add(card);
	}
	
	public void addSeenCard(Card card) {
		seenCards.add(card);
	}
	
	public Color getColor() {
        return this.color;
    }
	
	public void setRow(int row) {
		this.row = row;
	}

	public void setCol(int col) {
		this.col = col;
	}
	
	public boolean isOut() {
	    return isOut;
	}

	public void setIsOut(boolean isOut) {
	    this.isOut = isOut;
	}
	
	public boolean isMovedBySuggestion() {
	    return movedBySuggestion;
	}

	public void setMovedBySuggestion(boolean movedBySuggestion) {
	    this.movedBySuggestion = movedBySuggestion;
	}
}
