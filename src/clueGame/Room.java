package clueGame;
import java.util.ArrayList;

public class Room {
	private char initial;
    private String name;
    private BoardCell centerCell;
    private BoardCell labelCell;
    private ArrayList<BoardCell> doorCells;
    private BoardCell secretPassageCell;
    private boolean hasSecretPassage;

    public Room(String name) {
        this.name = name;
        this.doorCells = new ArrayList<BoardCell>();
    }

    public String getName() {
        return name;
    }

    public BoardCell getCenterCell() {
        return centerCell;
    }

    public BoardCell getLabelCell() {
        return labelCell;
    }
    
    public ArrayList<BoardCell> getDoorCells() {
    	return this.doorCells;
    }
    
    public BoardCell getSecretPassageCell() {
    	return this.secretPassageCell;
    }
    
    public boolean getHasSecretPassage() {
    	return this.hasSecretPassage;
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
    
    public void setDoorwayCell(BoardCell cell) {
    	this.doorCells.add(cell);
    }
    
    public void setSecretPassageCell(BoardCell cell) {
    	this.secretPassageCell = cell;
    }
    
    public void setHasSecretPassage(boolean isPassage) {
    	this.hasSecretPassage = isPassage;
    }
}
