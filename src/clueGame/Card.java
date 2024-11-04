package clueGame;

public class Card {
	String cardName;
	CardType cardType;
	
	public Card(String name, CardType type) {
		this.cardName = name;
		this.cardType = type;
	}
	
	@Override
	public boolean equals(Object obj) {
	    // Check if the object is the same instance
	    if (this == obj) {
	        return true;
	    }

	    // Check if the object is an instance of Card
	    if (obj instanceof Card) {
	        Card otherCard = (Card) obj;

	        // Compare both the card name and type
	        return this.cardName.equals(otherCard.cardName) && this.cardType == otherCard.cardType;
	    }

	    return false;
	}

	@Override
	public int hashCode() {
	    // Generate a hash code based on the card name and type
	    return cardName.hashCode() + cardType.hashCode();
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
