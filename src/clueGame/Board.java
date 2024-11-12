package clueGame;

import java.util.Scanner;
import java.util.HashSet;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

public class Board extends JPanel{

	private static Board theInstance = new Board();
	private BoardCell[][] grid;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	private int numRows;
	private int numColumns;
	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character, Room> roomMap;
	private Map<BoardCell, Set<BoardCell>> adjList = new HashMap<>();
	private HashMap<Character, String> legendMap = new HashMap<>();
	private ArrayList<Player> players;
	private ArrayList<Card> cardDeck;
	private ArrayList<Card> fullDeck;
	private Solution solution;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Compute cell size
        int cellWidth = getWidth() / numColumns;
        int cellHeight = getHeight() / numRows;

        // Loop through all cells and draw them
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                BoardCell cell = grid[row][col];
                cell.draw(g, cellWidth, cellHeight);
            }
        }

        // Draw room names after all cells are drawn
        drawRoomNames(g, cellWidth, cellHeight);

        // Draw players
        drawPlayers(g, cellWidth, cellHeight);
    }

    private void drawRoomNames(Graphics g, int cellWidth, int cellHeight) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 12));

        for (Room room : roomMap.values()) {
            BoardCell labelCell = room.getLabelCell();
            if (labelCell != null) {
                int x = labelCell.getCol() * cellWidth;
                int y = labelCell.getRow() * cellHeight;
                g.drawString(room.getName(), x, y);
            }
        }
    }

    private void drawPlayers(Graphics g, int cellWidth, int cellHeight) {
        for (Player player : players) {
            player.draw(g, cellWidth, cellHeight);
        }
    }
	
	public static Board getInstance() {
		return theInstance;
	}

	public void initialize() {
		try {
			loadSetupConfig();
			loadLayoutConfig();
			findAdj();
			dealCards();
		} catch (BadConfigFormatException e) {
			System.out.println("Unable to initialize the board");
		}
	}
	
	public void findAdj() {
	    // Iterate through all cells to determine adjacency
	    for (int i = 0; i < numRows; i++) {
	        for (int j = 0; j < numColumns; j++) {
	            Set<BoardCell> tmp = new HashSet<>();
	            BoardCell currentCell = getCell(i, j);
	            
	            if (currentCell.isDoorway()) {
	                addDoorwayAdjacencies(i, j, tmp);
	            } else if (currentCell.isWalkway()) {
	                addWalkwayAdjacencies(i, j, tmp);
	            } else {
	                addRoomAdjacencies(i, j, tmp);
	            }
	            
	            adjList.put(currentCell, tmp);
	        }
	    }
	}

	private void addDoorwayAdjacencies(int row, int col, Set<BoardCell> adjacencies) {
	    addAdjacentWalkways(row, col, adjacencies);
	    
	    DoorDirection direction = getCell(row, col).getDoorDirection();
	    int targetRow = row, targetCol = col;

	    switch (direction) {
	        case DOWN -> targetRow++;
	        case UP -> targetRow--;
	        case RIGHT -> targetCol++;
	        case LEFT -> targetCol--;
	    }
	    
	    if (isInBounds(targetRow, targetCol) 
	        && getCell(targetRow, targetCol).getInitial() != 'X'
	        && getCell(targetRow, targetCol).getInitial() != 'W') {
	        char init = getCell(targetRow, targetCol).getInitial();
	        BoardCell roomCenter = roomMap.get(init).getCenterCell();
	        adjacencies.add(roomCenter);
	    }
	}

	private void addWalkwayAdjacencies(int row, int col, Set<BoardCell> adjacencies) {
	    addAdjacentWalkways(row, col, adjacencies);
	}

	private void addAdjacentWalkways(int row, int col, Set<BoardCell> adjacencies) {
	    if (isInBounds(row + 1, col) && getCell(row + 1, col).isWalkway()) {
	        adjacencies.add(getCell(row + 1, col));
	    }
	    if (isInBounds(row - 1, col) && getCell(row - 1, col).isWalkway()) {
	        adjacencies.add(getCell(row - 1, col));
	    }
	    if (isInBounds(row, col + 1) && getCell(row, col + 1).isWalkway()) {
	        adjacencies.add(getCell(row, col + 1));
	    }
	    if (isInBounds(row, col - 1) && getCell(row, col - 1).isWalkway()) {
	        adjacencies.add(getCell(row, col - 1));
	    }
	}

	private void addRoomAdjacencies(int row, int col, Set<BoardCell> adjacencies) {
	    BoardCell currentCell = getCell(row, col);
	    Room room = roomMap.get(currentCell.getInitial());

	    if (room.getHasSecretPassage()) {
	        adjacencies.add(room.getSecretPassageCell());
	    }
	    
	    for (BoardCell doorCell : room.getDoorCells()) {
	        adjacencies.add(doorCell);
	    }
	}

	private boolean isInBounds(int row, int col) {
	    return row >= 0 && row < numRows && col >= 0 && col < numColumns;
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
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}

    public Set<BoardCell> getAdjList(int row, int col) {
    	return adjList.get(getCell(row, col));
    	
    }
	
	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}

	public void loadSetupConfig() throws BadConfigFormatException {
		try {
			File file = new File(setupConfigFile);
			try (Scanner reader = new Scanner(file)) {
				roomMap = new HashMap<>();
				players = new ArrayList<Player>();
				cardDeck = new ArrayList<Card>();
				fullDeck = new ArrayList<Card>();
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
						legendMap.put(roomInitial, roomName);
						if (!roomMap.containsKey(room)) {
							Card newCard = new Card(roomName, CardType.ROOM);
							cardDeck.add(newCard);
							fullDeck.add(newCard);
						}
					} else if (parts.length == 3 && parts[0].equals("Space")) {
						String spaceName = parts[1];
						char spaceInitial = parts[2].charAt(0);
						Room space = new Room(spaceName);
						space.setInitial(spaceInitial);
						roomMap.put(spaceInitial, space);	
					} else if (parts.length == 5) {
						int tempRow = Integer.parseInt(parts[3]);
						int tempCol = Integer.parseInt(parts[4]);
						if (parts[2].equals("Human")) {
							HumanPlayer newHuman = new HumanPlayer(parts[0], parts[1], tempRow, tempCol);
							players.add(newHuman);
						} else {
							ComputerPlayer newComp = new ComputerPlayer(parts[0], parts[1], tempRow, tempCol);
							players.add(newComp);
						}
						Card newCard = new Card(parts[0], CardType.PERSON);
						cardDeck.add(newCard);
						fullDeck.add(newCard);
					} else if (parts.length == 1) {
						Card newCard = new Card(parts[0], CardType.WEAPON);
						cardDeck.add(newCard);
						fullDeck.add(newCard);
					}
					
					else {
						throw new BadConfigFormatException("Invalid setup config format");
					}
				}
				reader.close();
			}
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
			numColumns = lines.get(0).length;
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
						
						if (rowCells[col].charAt(0) == 'W') {
							cell.setWalkway(true);
						}
						
						// Set doorway direction based on characters like ^, v, <, >
						if (rowCells[col].contains("^")) {
							cell.setDoorway(true);
							cell.setDoorDirection(DoorDirection.UP);
						} else if (rowCells[col].contains("v")) {
							cell.setDoorway(true);
							cell.setDoorDirection(DoorDirection.DOWN);
						} else if (rowCells[col].contains("<")) {
							cell.setDoorway(true);
							cell.setDoorDirection(DoorDirection.LEFT);
						} else if (rowCells[col].contains(">")) {
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
					} else if (cellInitial == 'X') {
						continue;
					}
					else if(!(legendMap.containsKey(cellInitial))) {
						throw new BadConfigFormatException("Invalid Layout");
					}

					grid[row][col] = cell;
				}
			}

		} catch (FileNotFoundException e) {
			throw new BadConfigFormatException("Layout config file not found");
		}
		
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				BoardCell doorCell = grid[i][j];
				if (doorCell.isDoorway()) {
					if (doorCell.getDoorDirection().equals(DoorDirection.UP)) {
						Room myRoom = roomMap.get(grid[i-1][j].getInitial());
						myRoom.setDoorwayCell(doorCell);
					}
					if (doorCell.getDoorDirection().equals(DoorDirection.DOWN)) {
						Room myRoom = roomMap.get(grid[i+1][j].getInitial());
						myRoom.setDoorwayCell(doorCell);
					}
					if (doorCell.getDoorDirection().equals(DoorDirection.LEFT)) {
						Room myRoom = roomMap.get(grid[i][j-1].getInitial());
						myRoom.setDoorwayCell(doorCell);
					}
					if (doorCell.getDoorDirection().equals(DoorDirection.RIGHT)) {
						Room myRoom = roomMap.get(grid[i][j+1].getInitial());
						myRoom.setDoorwayCell(doorCell);
					}
				}
				if (doorCell.getSecretPassage() != ' ') {
					char passageRoom = grid[i][j].getSecretPassage();
					char ogRoom = grid[i][j].getInitial();
					Room theOGRoom = roomMap.get(ogRoom);
					Room tempRoom = roomMap.get(passageRoom);
					theOGRoom.setSecretPassageCell(tempRoom.getCenterCell());
					theOGRoom.setHasSecretPassage(true);
				}
			}
		}
	}
	
    public Set<BoardCell> getTargets() {
        return targets;
    }
    
    public void calcTargets(BoardCell startCell, int pathLength) {
    	visited = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
		findAllTargets(startCell, pathLength);
    }
    
	public void findAllTargets(BoardCell startCell, int pathLength) {
		// Find adjacent cells of parameter cell each time
		//findAdj();
		
		// get the adjacent cell list
		int row = startCell.getRow();
		int col = startCell.getCol();
		Set<BoardCell> adjlist = getAdjList(row,col);
		// add the parameter cell to visited
		visited.add(startCell);
		for (BoardCell cell: adjlist) {
			if (visited.contains(cell)) {
				continue;
			}
			visited.add(cell);
			if (pathLength == 1 && !(cell.getOccupied())) {
				targets.add(cell);
			}else if (cell.getInitial() == 'W' && !(cell.getOccupied())) {
				findAllTargets(cell, (pathLength-1));
			}else if (cell.getInitial() != 'W' && cell.getInitial() != 'X') {
				char init = cell.getInitial();
				BoardCell temp = roomMap.get(init).getCenterCell();
				if (!targets.contains(temp)) {
					targets.add(temp);
				}
			}

			visited.remove(cell);
		}
	}
	
	public ArrayList<Player> getPlayers(){
		return players;
	}
	
	public ArrayList<Card> getDeck(){
		return cardDeck;
	}
	
    public void dealCards() {
        // Randomly select one room, one person, and one weapon as the solution
        Card room = getRandomCardOfType(CardType.ROOM);
        Card person = getRandomCardOfType(CardType.PERSON);
        Card weapon = getRandomCardOfType(CardType.WEAPON);
        solution = new Solution(room, person, weapon);

        // Remove solution cards from the deck
        cardDeck.remove(room);
        cardDeck.remove(person);
        cardDeck.remove(weapon);

        // Shuffle and deal the remaining cards to players
        int playerIndex = 0;
        for (Card card : cardDeck) {
            players.get(playerIndex).updateHand(card);
            players.get(playerIndex).addSeenCard(card);
            playerIndex = (playerIndex + 1) % players.size();
        }
    }

    private Card getRandomCardOfType(CardType type) {
        ArrayList<Card> cardsOfType = new ArrayList<>();
        for (Card card : cardDeck) {
            if (card.getCardType() == type) {
                cardsOfType.add(card);
            }
        }
        if (cardsOfType.isEmpty()) {
            throw new IllegalArgumentException("No cards of type " + type + " found in cardDeck");
        }
        Random rand = new Random();
        return cardsOfType.get(rand.nextInt(cardsOfType.size()));
    }


    public Solution getSolution() {
        return solution;
    }
    
    public ArrayList<Card> getFullDeck() {
    	return fullDeck;
    }
    
    public boolean checkAccusation(Card room, Card person, Card weapon) {
    	if (room.getName().equals(solution.getRoom().getName())){
    		if (person.getName().equals(solution.getPerson().getName())) {
    			if (weapon.getName().equals(solution.getWeapon().getName())) {
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    public Card handleSuggestion(Player suggester, Card suggRoom, Card suggPerson, Card SuggWeapon) {
    	for (Player currentPlayer: players) {
    		Card returned = currentPlayer.disproveSuggestion(suggRoom, suggPerson, SuggWeapon);
    		if (returned == null) {
    			continue;
    		}
    		if (!returned.getName().equals(null)) {
    			if (currentPlayer.getName().equals(suggester.getName())) {
    				continue;
    			} else {
    				return returned;
    			}
    		}
    	}
    	
    	return null;
    }
    
    public void addTestingPlayers() {
    	players.clear();
    }
    
    public void addPlayerTest(Player addedPlayer) {
    	players.add(addedPlayer);
    }
}
