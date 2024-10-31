package clueGame;

public class Solution {
	Card roomCard;
	Card weaponCard;
	Card personCard;
	
	public Solution(Card room, Card weapon, Card person) {
		this.roomCard = room;
		this.weaponCard = weapon;
		this.personCard = person;
	}
	
	public Card getPerson() {
		return personCard;
	}

	public void setPerson(Card person) {
		this.personCard = person;
	}

	public Card getRoom() {
		return roomCard;
	}

	public void setRoom(Card room) {
		this.roomCard = room;
	}

	public Card getWeapon() {
		return weaponCard;
	}

	public void setWeapon(Card weapon) {
		this.weaponCard = weapon;
	}
}
