package clueGame;

public class Room {
	private char initial;
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
    
    public void setInitial(char newinit) {
    	this.initial = newinit;
    }
    
    public void setCenterCell(BoardCell cell) {
    	this.centerCell = cell;
    }
    
    public void setLabelCell(BoardCell cell) {
    	this.labelCell = cell;
    }
}
