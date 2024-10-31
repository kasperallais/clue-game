package clueGame;

public class Card {
	String cardName;
	CardType cardType;
	
	public Card(String name, CardType type) {
		this.cardName = name;
		this.cardType = type;
	}
	
	public boolean equals(Card otherCard){
		if (otherCard.equals(this)){
			return true;
		} else {
			return false;
		}
	}
	public CardType getCardType() {
		return cardType;
	}

	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}

	public String getName() {
		return cardName;
	}
	public void setName(String name) {
		this.cardName = name;
	}
}
