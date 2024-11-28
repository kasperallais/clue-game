package clueGame;

public class DisproveResult {
    private Card disprovingCard;
    private Player disprovingPlayer;

    public DisproveResult(Card card, Player player) {
        this.disprovingCard = card;
        this.disprovingPlayer = player;
    }

    public Card getDisprovingCard() {
        return disprovingCard;
    }

    public Player getDisprovingPlayer() {
        return disprovingPlayer;
    }
}
