package clueGame;

import java.util.HashSet;
import java.util.Set;
import java.awt.Graphics;
import java.awt.Color;

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
    private char secretPassage = ' ';
    private Set<BoardCell> adjList;
    private boolean hasSecretPassage;

    public void setHasSecretPassage(boolean hasSecretPassage) {
        this.hasSecretPassage = hasSecretPassage;
    }

    public boolean getHasSecretPassage() {
        return hasSecretPassage;
    }
    
    private boolean isHighlighted = false;

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.isHighlighted = highlighted;
    }
    
    public BoardCell(int row, int col) {
        this.row = row;
        this.col = col;
        this.adjList = new HashSet<>();
    }
    
    // method to draw the cells to make sure right width, height, and color
    public void draw(Graphics g, int cellWidth, int cellHeight) {
        int x = col * cellWidth;
        int y = row * cellHeight;

        if (isHighlighted) {
            g.setColor(Color.CYAN);
            g.fillRect(x, y, cellWidth, cellHeight);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, cellWidth, cellHeight);
        } else if (isWalkway()) {
            g.setColor(Color.YELLOW);
            g.fillRect(x, y, cellWidth, cellHeight);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, cellWidth, cellHeight);
        } else if (isRoom()) {
            g.setColor(Color.GRAY);
            g.fillRect(x, y, cellWidth, cellHeight);
        }

        if (isDoorway()) {
            g.setColor(Color.BLUE);
            int doorWidth = cellWidth / 4;
            int doorHeight = cellHeight / 4;
            switch (doorDirection) {
                case UP:
                    g.fillRect(x, y, cellWidth, doorHeight);
                    break;
                case DOWN:
                    g.fillRect(x, y + cellHeight - doorHeight, cellWidth, doorHeight);
                    break;
                case LEFT:
                    g.fillRect(x, y, doorWidth, cellHeight);
                    break;
                case RIGHT:
                    g.fillRect(x + cellWidth - doorWidth, y, doorWidth, cellHeight);
                    break;
                default:
                    break;
            }
        }
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
    
    public void setOccupied(boolean occupied) {
        this.isOccupied = occupied;
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
