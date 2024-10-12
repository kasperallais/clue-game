package clueGame;

import java.util.HashSet;
import java.util.Set;

public class BoardCell {
    private int row;
    private int col;
    private char initial;
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

    public boolean isDoorway() {
        return false; // Placeholder for determining if the cell is a doorway
    }

    public DoorDirection getDoorDirection() {
        return DoorDirection.NONE; // Placeholder for door direction
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
}
