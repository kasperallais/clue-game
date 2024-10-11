package clueGame;

public class BoardCell {
    private int row;
    private int col;
    private DoorDirection doorDirection;

    public boolean isDoorway() {
        // Placeholder method, needs implementation.
        return false;
    }

    public DoorDirection getDoorDirection() {
        return doorDirection;
    }

    public char getSecretPassage() {
        // Placeholder method, needs implementation.
        return ' ';
    }

    public boolean isRoomCenter() {
        return false;
    }

    public boolean isLabel() {
        return false;
    }
}
