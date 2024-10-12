package clueGame;

public class Room {
    private String name;
    private BoardCell centerCell;
    private BoardCell labelCell;

    public Room(String name) {
        this.name = name;
    }

    public String getName() {
        return name; // Placeholder for getting the room name
    }

    public BoardCell getCenterCell() {
        return centerCell; // Placeholder for center cell
    }

    public BoardCell getLabelCell() {
        return labelCell; // Placeholder for label cell
    }
}
