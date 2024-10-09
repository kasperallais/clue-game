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
        grid = new TestBoardCell[COLS][ROWS];
        for (int i = 0; i < COLS; i++) {
        	
        	for (int j = 0; j < ROWS; j++) {
        		
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
    		thisCell.addAdjacency(grid[col][row-1]);
    	}
    	if (row + 1 < 4) {
    		System.out.println("Check row");
    		thisCell.addAdjacency(grid[col][row+1]);
    	}
    	if (col - 1 >= 0) {
    		thisCell.addAdjacency(grid[col-1][row]);
    	}
    	if (col + 1 < 4) {
    		System.out.println("Check col");
    		thisCell.addAdjacency(grid[col+1][row]);
    	}
    }

    public void calcTargets(TestBoardCell startCell, int pathLength) {
        targets.clear();
    }

    public TestBoardCell getCell(int row, int col) {
        return grid[row][col];
    }

    public Set<TestBoardCell> getTargets() {
        return targets;
    }
}
