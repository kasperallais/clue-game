package clueGame;

import java.util.HashSet;
import java.util.Set;

import experiment.TestBoardCell;

public class BoardCell {
	private String roomName;
    private int row;
    private int col;
    private char initial;
    private boolean isRoom;
    private boolean isDoorway;
    private boolean isWalkway;
    private boolean isOccupied;
    private DoorDirection doorDirection;
    private boolean roomLabel;
    private boolean roomCenter;
    private char secretPassage;
    private Set<BoardCell> adjList;

    public BoardCell(int row, int col) {
        this.row = row;
        this.col = col;
        this.adjList = new HashSet<>();
    }

    public DoorDirection getDoorDirection() {
        return this.doorDirection; // Placeholder for door direction
    }

    public boolean isLabel() {
        return roomLabel; // Placeholder for label status
    }

    public boolean isRoomCenter() {
        return roomCenter; // Placeholder for room center status
    }

    public char getSecretPassage() {
        return secretPassage; // Placeholder for secret passage
    }

    public void addAdj(BoardCell adj) {
        adjList.add(adj); // Placeholder for adding adjacent cells
    }
    
    public int getCol() {
    	return col;
    }
    
    public int getRow() {
    	return row;
    }
    
    public Set<BoardCell> getAdjList() {
        return adjList;
    }
    
    public boolean isRoom() {
        return isRoom;
    }
    
    public boolean getOccupied() {
        return isOccupied;
    }
    
    public void setRoomName(String name) {
    	this.roomName = name;
    }
    
    public String getName() {
    	return this.roomName;
    }
    
    public void setDoorway(boolean doorway) {
    	this.isDoorway = doorway;
    }
    
    public boolean isDoorway() {
    	return this.isDoorway;
    }
    
    public void setWalkway(boolean isWalkway) {
		this.isWalkway = isWalkway;
	}
    
    public boolean isWalkway() {
		return isWalkway;
	}
    
    public char getInitial() {
		return this.initial;
	}
    
    public void setDoorDirection(DoorDirection doorDirection) {
		this.doorDirection = doorDirection;
	}

	public void setInitial(char initial) {
		this.initial = initial;
	}
	
	public void setRoom(boolean isroom) {
		this.isRoom = isroom;
	}
	
	public void setRoomCenter(boolean center) {
		this.roomCenter = center;
	}
	
	public void setLabel(boolean label) {
		this.roomLabel = label;
	}
	
	public void setSecretPassage(char letter) {
		this.secretPassage = letter;
	}
	
}
