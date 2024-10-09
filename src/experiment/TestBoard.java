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
		// init sets and arrays
		targets = new HashSet<>();
		grid = new TestBoardCell[ROWS][COLS];
		visited = new HashSet<>();
		// fill grid with board cells
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				grid[i][j] = new TestBoardCell(i,j);
			}
		}
	}

	public void findAdj(TestBoardCell thisCell) {
		// check to see if there are adjacent cells without going out of bounds
		int row = thisCell.getRow();
		int col = thisCell.getCol();
		if (row - 1 >= 0) {
			thisCell.addAdjacency(grid[(row-1)][col]);
		}
		if (row + 1 < ROWS) {
			thisCell.addAdjacency(grid[(row+1)][col]);
		}
		if (col - 1 >= 0) {
			thisCell.addAdjacency(grid[row][(col-1)]);
		}
		if (col + 1 < COLS) {
			thisCell.addAdjacency(grid[row][(col+1)]);
		}
	}

	public void calcTargets(TestBoardCell startCell, int pathLength) {
		// Find adjacent cells of paramter cell each time
		findAdj(startCell);
		// get the adjacent cell list
		Set<TestBoardCell> adjlist = startCell.getAdjList();
		// add the parameter cell to visited
		visited.add(startCell);
		for (TestBoardCell cell: adjlist) {
			if (visited.contains(cell)) {
				continue;
			}
			visited.add(cell);
			// make sure cell is not occupied
			if (pathLength == 1 && !(cell.getOccupied())) {
				targets.add(cell);
			}else if (!cell.isRoom()) {
				calcTargets(cell, (pathLength-1));
			}
			visited.remove(cell);
		}
		
	}

	public TestBoardCell getCell(int row, int col) {
		return grid[row][col];
	}

	public Set<TestBoardCell> getTargets() {
		return targets;
	}
}
