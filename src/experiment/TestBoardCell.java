package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoardCell {
    private int row;
    private int col;
    private boolean isRoom;
    private boolean isOccupied;
    private Set<TestBoardCell> adjList;

    public TestBoardCell(int row, int col) {
        this.row = row;
        this.col = col;
        this.adjList = new HashSet<>();
    }

    public void addAdjacency(TestBoardCell cell) {
        adjList.add(cell);
    }

    public Set<TestBoardCell> getAdjList() {
        return adjList;
    }

    public void setRoom(boolean isRoom) {
        this.isRoom = isRoom;
    }

    public boolean isRoom() {
        return isRoom;
    }

    public void setOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    public boolean getOccupied() {
        return isOccupied;
    }
    
    public int getCol() {
    	return col;
    }
    
    public int getRow() {
    	return row;
    }
}
