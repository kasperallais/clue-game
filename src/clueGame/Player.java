package clueGame;

import java.util.ArrayList;

public abstract class Player {
	private String name;
	private String color;
	private int row;
	private int col;
	private ArrayList<Card> hand;
	
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
}
