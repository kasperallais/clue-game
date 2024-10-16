package clueGame;

import java.util.Scanner;
import java.util.HashSet;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.ResourceBundle.Control;
import java.util.HashMap;

//import experiment.TestBoardCell;

public class Board {

	private static Board theInstance = new Board();
	private BoardCell[][] grid;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	private int numRows;
	private int numColumns;
	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character, Room> roomMap;
	final static int COLS = 4;
	final static int ROWS = 4;
	private int numberRows;
	private int numberCols;

	private Board() {
		// Private constructor for Singleton pattern
	}

	public static Board getInstance() {
		return theInstance;
	}

	public void initialize() {
		try {
			loadSetupConfig();
			loadLayoutConfig();
		} catch (BadConfigFormatException e) {
			System.out.println("Unable to initialize the board");
		}
	}

	public void findAdj(BoardCell thisCell) {
		// check to see if there are adjacent cells without going out of bounds
		int row = thisCell.getRow();
		int col = thisCell.getCol();
		if (row - 1 >= 0) {
			thisCell.addAdj(grid[(row-1)][col]);
		}
		if (row + 1 < ROWS) {
			thisCell.addAdj(grid[(row+1)][col]);
		}
		if (col - 1 >= 0) {
			thisCell.addAdj(grid[row][(col-1)]);
		}
		if (col + 1 < COLS) {
			thisCell.addAdj(grid[row][(col+1)]);
		}
	}

	public void setConfigFiles(String layoutConfigFile, String setupConfigFile) {
		this.layoutConfigFile = layoutConfigFile;
		this.setupConfigFile = setupConfigFile;
	}


	public Room getRoom(BoardCell cell) {
		if (cell.isRoom()) { // Check if the cell is part of a room
            char roomInitial = cell.getInitial();
            return roomMap.get(roomInitial); // Return the corresponding Room
        }
        return null; // Return null if the cell is not a room
    }
	

	public Room getRoom(char initial) {
		return roomMap.get(initial);
	}

	public int getNumRows() {
		return numberRows; // Placeholder value
	}

	public int getNumColumns() {
		return numberCols; // Placeholder value
	}

	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}

	public void loadSetupConfig() throws BadConfigFormatException {
		try {
			File file = new File(setupConfigFile);
			Scanner reader = new Scanner(file);
			roomMap = new HashMap<>();
			while (reader.hasNextLine()) {
				String line = reader.nextLine().trim();
				if (line.startsWith("//") || line.isEmpty()) {
					continue;
				}
				String[] parts = line.split(",\\s*");

				if (parts.length == 3 && parts[0].equals("Room")) {
					String roomName = parts[1];
					char roomInitial = parts[2].charAt(0);
					Room room = new Room(roomName);
					room.setInitial(roomInitial);
					roomMap.put(roomInitial, room);
				} else if (parts.length == 3 && parts[0].equals("Space")) {
					String spaceName = parts[1];
					char spaceInitial = parts[2].charAt(0);
					Room space = new Room(spaceName);
					space.setInitial(spaceInitial);
					roomMap.put(spaceInitial, space);
				} else {
					throw new BadConfigFormatException("Invalid setup config format");
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			throw new BadConfigFormatException("Setup config file not found");
		}
	}

	public void loadLayoutConfig() throws BadConfigFormatException {
		try {
			File file = new File(layoutConfigFile);
			Scanner reader = new Scanner(file);
			List<String[]> lines = new ArrayList<>();
			while (reader.hasNextLine()) {
				String line = reader.nextLine().trim();
				String[] cells = line.split(",");
				lines.add(cells);
			}
			reader.close();
			// Set the dimensions of the board
			numRows = lines.size();
			this.numberRows = numRows;
			numColumns = lines.get(0).length;
			this.numberCols = numColumns;
			grid = new BoardCell[numRows][numColumns];

			// Loop through the layout lines and populate the grid
			for (int row = 0; row < numRows; row++) {
				String[] rowCells = lines.get(row);
				if (rowCells.length != numColumns) {
					throw new BadConfigFormatException("Row length mismatch in layout config");
				}
				for (int col = 0; col < numColumns; col++) {
					char cellInitial = rowCells[col].charAt(0);
					BoardCell cell = new BoardCell(row, col);
					cell.setInitial(cellInitial);

					if (roomMap.containsKey(cellInitial)) {
						Room room = roomMap.get(cellInitial);
						cell.setRoom(true);
						cell.setRoomName(room.getName());

						// Check for special characters for doorways, labels, centers
						if (rowCells[col].contains("#")) {
							cell.setLabel(true);
							room.setLabelCell(cell);
						}
						if (rowCells[col].contains("*")) {
							cell.setRoomCenter(true);
							room.setCenterCell(cell);
						}
						
						// Set doorway direction based on characters like ^, v, <, >
						if (rowCells[col].contains("^")) {
							System.out.println("Check Door");
							cell.setDoorway(true);
							cell.setDoorDirection(DoorDirection.UP);
							System.out.println("Check^");
						} else if (rowCells[col].contains("v")) {
							System.out.println("Check Door");
							cell.setDoorway(true);
							cell.setDoorDirection(DoorDirection.DOWN);
							
						} else if (rowCells[col].contains("<")) {
							System.out.println("Check Door");
							cell.setDoorway(true);
							cell.setDoorDirection(DoorDirection.LEFT);
						} else if (rowCells[col].contains(">")) {
							System.out.println("Check Door");
							cell.setDoorway(true);
							cell.setDoorDirection(DoorDirection.RIGHT);
						}
						
						if (rowCells[col].length() > 1) {
							if (Character.isUpperCase(rowCells[col].charAt(1))) {
							    cell.setSecretPassage(rowCells[col].charAt(1));
							} 
						}
						
					} else if (cellInitial == 'W') {
						cell.setWalkway(true);
					} 

					grid[row][col] = cell;
				}
			}

		} catch (FileNotFoundException e) {
			throw new BadConfigFormatException("Layout config file not found");
		}
	}
	
	
	
	

	public void calcTargets(BoardCell startCell, int pathLength) {
		// Find adjacent cells of paramter cell each time
		findAdj(startCell);
		// get the adjacent cell list
		Set<BoardCell> adjlist = startCell.getAdjList();
		// add the parameter cell to visited
		visited.add(startCell);
		for (BoardCell cell: adjlist) {
			if (visited.contains(cell)) {
				continue;
			}
			visited.add(cell);
			// make sure cell is not occupied
			if (pathLength == 1 && !(cell.getOccupied())) {
				targets.add(cell);
			}else if (!cell.isRoom() && !(cell.getOccupied())) {
				calcTargets(cell, (pathLength-1));
			}else if (cell.isRoom()) {
				targets.add(cell);
			}

			visited.remove(cell);
		}
	}
}
