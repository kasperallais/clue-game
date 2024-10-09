package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoard {
	private TestBoardCell[][] grid;
    private Set<TestBoardCell> targets;
    private Set<TestBoardCell> visited;
    final static int COLS = 4;
    final static int ROWS = 4;
    
    public TestBoard() {
        targets = new HashSet<>();
        grid = new TestBoardCell[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
        	
        	for (int j = 0; j < COLS; j++) {
        		
        		grid[i][j] = new TestBoardCell(i,j);
        		
        	}
        }
    }
    
//    public void findAllTarget(TestBoardCell thisCell, int numSteps) {
//    	
//    }
    
    public void findAdj(TestBoardCell thisCell) {
    	int row = thisCell.getRow();
    	int col = thisCell.getCol();
    	if (row - 1 >= 0) {
    		thisCell.addAdjacency(grid[(row-1)][col]);
    	}
    	if (row + 1 < 4) {
    		thisCell.addAdjacency(grid[(row+1)][col]);
    	}
    	if (col - 1 >= 0) {
    		thisCell.addAdjacency(grid[row][(col-1)]);
    	}
    	if (col + 1 < 4) {
    		thisCell.addAdjacency(grid[row][(col+1)]);
    	}
    }

    public void calcTargets(TestBoardCell startCell, int pathLength) {
        targets.clear();
    }

    public TestBoardCell getCell(int row, int col) {
    	System.out.println(row + " " + col);
        return grid[row][col];
    }

    public Set<TestBoardCell> getTargets() {
        return targets;
    }
}
