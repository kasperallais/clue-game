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

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Board extends JPanel{
	
	// create variables
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
    private int currentPlayerIndex = -1;
    private int diceRoll = 0;
    private GameControlPanel controlPanel;
    private boolean humanMustFinish = false;

    // Add the mouse listener in the constructor
    private Board() {
        addMouseListener(new BoardMouseListener());
    }
    
    // Move to next player
    public void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        Player currentPlayer = players.get(currentPlayerIndex);

        // Roll the dice
        Random rand = new Random();
        diceRoll = rand.nextInt(6) + 1; // Random number between 1 and 6

        // Calculate targets
        BoardCell startCell = getCell(currentPlayer.getRow(), currentPlayer.getCol());
        calcTargets(startCell, diceRoll);

        // Update the control panel
        controlPanel.setTurn(currentPlayer, diceRoll);

        if (currentPlayer instanceof ComputerPlayer) {
            // Computer player's turn
            ComputerPlayer computerPlayer = (ComputerPlayer) currentPlayer;
            // Computer selects a target
            BoardCell targetCell = computerPlayer.selectTargets(targets);

            // Before moving, set current cell to unoccupied
            startCell.setOccupied(false);
            // Move the player to the selected target
            currentPlayer.setRow(targetCell.getRow());
            currentPlayer.setCol(targetCell.getCol());
            // After moving, set new cell to occupied
            targetCell.setOccupied(true);

            // Repaint the board
            repaint();
        } else if (currentPlayer instanceof HumanPlayer) {
            // Human player's turn
            humanMustFinish = true;
            // Highlight possible targets
            for (BoardCell cell : targets) {
                cell.setHighlighted(true);
            }
            repaint();
        }
    }


    public boolean isHumanMustFinish() {
        return humanMustFinish;
    }

    // gets where the player clicked and to move the player to that spot
    private class BoardMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            // Only process clicks during human player's turn
            if (!(players.get(currentPlayerIndex) instanceof HumanPlayer)) {
                return;
            }

            if (!humanMustFinish) {
                return; // Ignore clicks if human has already moved
            }

            HumanPlayer humanPlayer = (HumanPlayer) players.get(currentPlayerIndex);

            int x = e.getX();
            int y = e.getY();
            int cellWidth = getWidth() / numColumns;
            int cellHeight = getHeight() / numRows;
            int col = x / cellWidth;
            int row = y / cellHeight;

            BoardCell clickedCell = getCell(row, col);

            // If the clicked cell is a valid target
            if (targets.contains(clickedCell)) {
                // Before moving, set current cell to unoccupied
                BoardCell currentCell = getCell(humanPlayer.getRow(), humanPlayer.getCol());
                currentCell.setOccupied(false);
                // Move the player
                humanPlayer.setRow(row);
                humanPlayer.setCol(col);
                // After moving, set new cell to occupied
                clickedCell.setOccupied(true);

                // Clear highlights
                for (BoardCell cell : targets) {
                    cell.setHighlighted(false);
                }
                repaint();

                // Finish the human player's turn
                humanMustFinish = false;

                // Clear the targets
                targets.clear();

            }
        }
    }
    
    // draws the cells and makes them correct colors
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

    // display the room names
    private void drawRoomNames(Graphics g, int cellWidth, int cellHeight) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 12));

        for (Room room : roomMap.values()) {
            BoardCell labelCell = room.getLabelCell();
            if (labelCell != null) {
                int x = labelCell.getCol() * cellWidth;
                int y = labelCell.getRow() * cellHeight;
                g.drawString(room.getName(), x-18, y);
            }
        }
    }
    
    // loop through and draw the players
    private void drawPlayers(Graphics g, int cellWidth, int cellHeight) {
        for (Player player : players) {
            player.draw(g, cellWidth, cellHeight);
        }
    }
	

	// init the board and if it fails catch an exception
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
	
	// finds adjcanent squares to the human and computer players
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

	// helper function for findAdj which makes sure spots near doors work
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
	
	// helper function for findAdj 
	private void addWalkwayAdjacencies(int row, int col, Set<BoardCell> adjacencies) {
	    addAdjacentWalkways(row, col, adjacencies);
	}

	// helper function for findAdj to make sure nearby walkways are in bounds
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

	// helper function for findAdj to make sure rooms nearby get added to adj list

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

	// check to see if spot is in the board
	private boolean isInBounds(int row, int col) {
	    return row >= 0 && row < numRows && col >= 0 && col < numColumns;
	}

	// find the wanted room and return the room
	public Room getRoom(BoardCell cell) {
		if (cell.isRoom()) { // Check if the cell is part of a room
            char roomInitial = cell.getInitial();
            return roomMap.get(roomInitial); // Return the corresponding Room
        }
        return null; // Return null if the cell is not a room
    }
	
	// go through the setup file and load in what data we need to make the players, rooms and cards
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
					// skip lines with // in front
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
						// if the length is 5 that means its a player
						int tempRow = Integer.parseInt(parts[3]);
						int tempCol = Integer.parseInt(parts[4]);
						if (parts[2].equals("Human")) {
							HumanPlayer newHuman = new HumanPlayer(parts[0], parts[1], tempRow, tempCol);
							players.add(0, newHuman);
						} else {
							ComputerPlayer newComp = new ComputerPlayer(parts[0], parts[1], tempRow, tempCol);
							players.add(newComp);
						}
						Card newCard = new Card(parts[0], CardType.PERSON);
						cardDeck.add(newCard);
						fullDeck.add(newCard);
						// if the length is one that means its a card
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

	// go through the layout file and load in the board into a 2d array
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
		
		// loops through grid to find the doorways into rooms and then setting a cell in the room class to that cell
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
    
    // start a serach for all targets within a certian amount of space depending on roll
    public void calcTargets(BoardCell startCell, int pathLength) {
    	visited = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
		findAllTargets(startCell, pathLength);
    }
    
    // recursive function to find all nearby spots
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
	
	// make the solution and deal the cards out to players
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
    
    // get a random card of a certain type
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
    
    // check to see if an accusation matches the solution
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
    
    // check to see if a suggestion can be disproven or not
    public Card handleSuggestion(Player suggester, Card suggRoom, Card suggPerson, Card SuggWeapon) {
    	for (Player currentPlayer: players) {
    		// looping through players and running the method to see if a player can disprove the suggestion
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
    
    // getter and setter methods
    // loop through players and find and return the human player
    public HumanPlayer getHumanPlayer() {
        for (Player player : players) {
            if (player instanceof HumanPlayer) {
                return (HumanPlayer) player;
            }
        }
        return null;
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
	
	public Solution getSolution() {
        return solution;
    }
    
    public ArrayList<Card> getFullDeck() {
    	return fullDeck;
    }
    
    public ArrayList<Player> getPlayers(){
		return players;
	}
	
	public ArrayList<Card> getDeck(){
		return cardDeck;
	}
	
	public Set<BoardCell> getTargets() {
        return targets;
    }
	
	public void setControlPanel(GameControlPanel controlPanel) {
        this.controlPanel = controlPanel;
    }
	
	// set config file names
	public void setConfigFiles(String layoutConfigFile, String setupConfigFile) {
		this.layoutConfigFile = layoutConfigFile;
		this.setupConfigFile = setupConfigFile;
	}
	
	// singleton
	public static Board getInstance() {
		return theInstance;
	}
}
