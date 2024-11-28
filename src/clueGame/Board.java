package clueGame;

import java.util.Scanner;
import java.util.HashSet;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;


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
    private GameCardPanel cardPanel;
    private boolean humanMustFinish = false;
    private static JDialog d;  

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
            ComputerPlayer computerPlayer = (ComputerPlayer) currentPlayer;

            if (computerPlayer.isReadyToAccuse()) {
                // Computer makes an accusation
                Solution accusation = computerPlayer.getAccusation();
                boolean accusationResult = checkAccusation(accusation.getRoom(), accusation.getPerson(), accusation.getWeapon());

                // Display the accusation
                JOptionPane.showMessageDialog(null, computerPlayer.getName() + " accuses " +
                        accusation.getPerson().getName() + " in the " + accusation.getRoom().getName() +
                        " with the " + accusation.getWeapon().getName() + ".", "Accusation", JOptionPane.INFORMATION_MESSAGE);

                if (accusationResult) {
                    // Accusation correct, computer wins
                    JOptionPane.showMessageDialog(null, computerPlayer.getName() + " has won the game!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0); // End the game
                } else {
                    // Accusation incorrect, computer loses
                    JOptionPane.showMessageDialog(null, computerPlayer.getName() + " made a wrong accusation and is out of the game.", "Accusation Incorrect", JOptionPane.INFORMATION_MESSAGE);
                    computerPlayer.setIsOut(true);
                }
                return; // Move to next player
            }

            // Existing movement and suggestion code...
            // Calculate targets
            BoardCell startCell1 = getCell(currentPlayer.getRow(), currentPlayer.getCol());
            calcTargets(startCell1, diceRoll);

            // Computer selects a target
            BoardCell targetCell = computerPlayer.selectTargets(targets);

            // Before moving, set current cell to unoccupied
            startCell1.setOccupied(false);
            // Move the player to the selected target
            currentPlayer.setRow(targetCell.getRow());
            currentPlayer.setCol(targetCell.getCol());
            // After moving, set new cell to occupied
            targetCell.setOccupied(true);

            // Repaint the board
            repaint();

            // Check if target cell is a room
            if (targetCell.isRoom()) {
                // Create a suggestion
                Room currentRoom = getRoom(targetCell);
                Solution suggestion = computerPlayer.createSuggestion(currentRoom);

                // Display the suggestion
                controlPanel.setGuess(suggestion.getPerson(), suggestion.getRoom(), suggestion.getWeapon(), computerPlayer.getColor());

                // Move the suggested player to the room
                moveSuggestedPlayerToRoom(suggestion.getPerson().getName(), currentRoom.getCenterCell());

                // Handle the suggestion
                DisproveResult result = handleSuggestion(computerPlayer, suggestion.getRoom(), suggestion.getPerson(), suggestion.getWeapon());

                if (result != null) {
                    // Suggestion was disproved
                    controlPanel.setGuessResult("Suggestion disproved by " + result.getDisprovingPlayer().getName());
                    computerPlayer.setLastSuggestionDisproved(true);
                } else {
                    // No one could disprove
                    controlPanel.setGuessResult("No new clue");
                    computerPlayer.setLastSuggestionDisproved(false);
                    // Prepare for accusation
                    computerPlayer.setReadyToAccuse(true);
                    computerPlayer.setAccusation(suggestion);
                }
            }
        }
        else if (currentPlayer instanceof HumanPlayer) {
            // Human player's turn
            humanMustFinish = true;
            
            // Highlight possible targets
            for (BoardCell cell : targets) {
                cell.setHighlighted(true);
            }
            repaint();
        }
    }
    
    public void doAccusation() {
        // Create accusation panel
        SuggestionPanel accusationDialog = new SuggestionPanel(
            (JFrame) SwingUtilities.getWindowAncestor(Board.this), 
           "", true, "Make an Accusation"
        );

        accusationDialog.setVisible(true);

        if (!accusationDialog.pressCancel()) {
            // Get what the user selected in accusation panel
            String room = accusationDialog.getSelectedRoom();
            String person = accusationDialog.getSelectedPerson();
            String weapon = accusationDialog.getSelectedWeapon();
            Card roomCard = null;
            Card personCard = null;
            Card weaponCard = null;

            // Loop through deck and find the three cards from the strings
            for (Card c : fullDeck) {
                if (c.getName().equals(room)) {
                    roomCard = c;
                } else if (c.getName().equals(person)) {
                    personCard = c;
                } else if (c.getName().equals(weapon)) {
                    weaponCard = c;
                }
            }

            boolean accusationResult = checkAccusation(roomCard, personCard, weaponCard);

            String message = "Your accusation was ";

            if (accusationResult) {
                message += "correct! You have won the game!";
            } else {
                message += "incorrect. You have lost the game.";
            }

            JOptionPane.showMessageDialog(null, message, "Accusation Result", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0); // End the game
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
                
                
                if (clickedCell.getInitial() != 'W') {
                    // Show suggestion dialog
                    Room currentRoom = getRoom(clickedCell); // Retrieve the room object
                    SuggestionPanel suggestionDialog = new SuggestionPanel(
                        (JFrame) SwingUtilities.getWindowAncestor(Board.this), 
                        currentRoom.getName(), false, "Make a Suggestion"
                    );

                    suggestionDialog.setVisible(true); // Display the dialog

                    // Handle suggestion result
                    String person = suggestionDialog.getSelectedPerson();
                    String weapon = suggestionDialog.getSelectedWeapon();

                    if (person != null && weapon != null) {
                        // Process the suggestion
                        Card roomCard = new Card(currentRoom.getName(), CardType.ROOM);
                        Card personCard = new Card(person, CardType.PERSON);
                        Card weaponCard = new Card(weapon, CardType.WEAPON);

                        // Display the suggestion
                        controlPanel.setGuess(personCard, roomCard, weaponCard, humanPlayer.getColor());

                        // Move the suggested person to the room
                        moveSuggestedPlayerToRoom(personCard.getName(), currentRoom.getCenterCell());

                        // Handle the suggestion
                        DisproveResult result = handleSuggestion(humanPlayer, roomCard, personCard, weaponCard);

                        if (result != null) {
                            // Someone disproved the suggestion
                            controlPanel.setGuessResult(result.getDisprovingCard());
                            JOptionPane.showMessageDialog(null, result.getDisprovingPlayer().getName() + " has disproved your suggestion.", "Suggestion Result", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            // No one could disprove
                            controlPanel.setGuessResult("No new clue");
                            JOptionPane.showMessageDialog(null, "No one could disprove your suggestion.", "Suggestion Result", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }

                }

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

	private void addDoorwayAdjacencies(int row, int col, Set<BoardCell> adjacencies) {
	    // Add adjacent walkways
	    addAdjacentWalkways(row, col, adjacencies);

	    DoorDirection direction = getCell(row, col).getDoorDirection();
	    int targetRow = row, targetCol = col;

	    switch (direction) {
	        case DOWN -> targetRow++;
	        case UP -> targetRow--;
	        case RIGHT -> targetCol++;
	        case LEFT -> targetCol--;
	        default -> {}
	    }

	    if (isInBounds(targetRow, targetCol)) {
	        BoardCell roomCell = getCell(targetRow, targetCol);
	        if (roomCell.isRoom()) {
	            adjacencies.add(roomCell); // Add the actual room cell adjacent to the doorway
	        }
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

	    // Add secret passage if available
	    if (room.getHasSecretPassage()) {
	        adjacencies.add(room.getSecretPassageCell());
	    }

	    // Add door cells adjacent to the room
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
							HumanPlayer newHuman = new HumanPlayer(parts[0], convertColor(parts[1]), tempRow, tempCol);
							players.add(0, newHuman);
						} else {
							ComputerPlayer newComp = new ComputerPlayer(parts[0], convertColor(parts[1]), tempRow, tempCol);
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

	public Color convertColor(String color) {
		Color returnColor = Color.white;
		switch(color) {
		case "RED":
			returnColor = Color.red;
			break;
		case "GREEN":
			returnColor = Color.green;
			break;
		case "PINK":
			returnColor = Color.pink;
			break;
		case "BLUE":
			returnColor = Color.blue;
			break;
		case "ORANGE":
			returnColor = Color.orange;
			break;
		case "GRAY":
			returnColor = Color.gray;
			break;
		}
		return returnColor;
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
	                String cellCode = rowCells[col];
	                char cellInitial = cellCode.charAt(0);
	                BoardCell cell = new BoardCell(row, col);
	                cell.setInitial(cellInitial);

	                if (cellInitial == 'W') {
	                    // It's a walkway
	                    cell.setWalkway(true);
	                } else if (roomMap.containsKey(cellInitial)) {
	                    // It's a room
	                    cell.setRoom(true);
	                    cell.setRoomName(roomMap.get(cellInitial).getName());
	                } else if (cellInitial == 'X') {
	                    // Unused cell
	                    // You may want to handle unused cells if necessary
	                } else {
	                    throw new BadConfigFormatException("Invalid cell initial: " + cellInitial);
	                }

	                // Check for special characters for doorways, labels, centers, secret passages
	                if (cellCode.length() > 1) {
	                    for (int i = 1; i < cellCode.length(); i++) {
	                        char c = cellCode.charAt(i);
	                        if (c == '#') {
	                            cell.setLabel(true);
	                            roomMap.get(cellInitial).setLabelCell(cell);
	                        } else if (c == '*') {
	                            cell.setRoomCenter(true);
	                            roomMap.get(cellInitial).setCenterCell(cell);
	                        } else if (c == '^' || c == 'v' || c == '<' || c == '>') {
	                            setDoorDirection(cell, c);
	                        } else if (Character.isUpperCase(c)) {
	                            cell.setSecretPassage(c);
	                            cell.setHasSecretPassage(true);
	                        }
	                    }
	                }

	                grid[row][col] = cell;
	            }
	        }

	        // Set up doorways into rooms
	        setupDoorways();

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
    
	private void setDoorDirection(BoardCell cell, char directionChar) {
	    cell.setDoorway(true);
	    switch (directionChar) {
	        case '^':
	            cell.setDoorDirection(DoorDirection.UP);
	            break;
	        case 'v':
	            cell.setDoorDirection(DoorDirection.DOWN);
	            break;
	        case '<':
	            cell.setDoorDirection(DoorDirection.LEFT);
	            break;
	        case '>':
	            cell.setDoorDirection(DoorDirection.RIGHT);
	            break;
	        default:
	            cell.setDoorDirection(DoorDirection.NONE);
	    }
	}

	private void setupDoorways() {
	    for (int i = 0; i < numRows; i++) {
	        for (int j = 0; j < numColumns; j++) {
	            BoardCell doorCell = grid[i][j];
	            if (doorCell.isDoorway()) {
	                int roomRow = i;
	                int roomCol = j;
	                switch (doorCell.getDoorDirection()) {
	                    case UP:
	                        roomRow--;
	                        break;
	                    case DOWN:
	                        roomRow++;
	                        break;
	                    case LEFT:
	                        roomCol--;
	                        break;
	                    case RIGHT:
	                        roomCol++;
	                        break;
	                    default:
	                        break;
	                }
	                if (isInBounds(roomRow, roomCol)) {
	                    BoardCell roomCell = grid[roomRow][roomCol];
	                    if (roomCell.isRoom()) {
	                        Room room = roomMap.get(roomCell.getInitial());
	                        room.setDoorwayCell(doorCell);
	                    }
	                }
	            }
	        }
	    }
	}

	
	
	public void calcTargets(BoardCell startCell, int pathLength) {
	    visited = new HashSet<>();
	    targets = new HashSet<>();

	    findAllTargets(startCell, pathLength);

	    Player currentPlayer = players.get(currentPlayerIndex);

	    if (currentPlayer.isMovedBySuggestion() && startCell.isRoom()) {
	        // Allow staying in the room to make a suggestion
	        targets.add(startCell);
	        // Do not reset movedBySuggestion here; reset it after the player makes their move
	    }

	    // Debug: Print out the targets
	    System.out.println("Targets for player " + currentPlayer.getName() + " at (" + startCell.getRow() + ", " + startCell.getCol() + "):");
	    for (BoardCell cell : targets) {
	        System.out.println(" - (" + cell.getRow() + ", " + cell.getCol() + ") Initial: " + cell.getInitial());
	    }
	}


    
    // recursive function to find all nearby spots
	public void findAllTargets(BoardCell startCell, int pathLength) {
	    visited.add(startCell);

	    Set<BoardCell> adjacents = getAdjList(startCell.getRow(), startCell.getCol());

	    for (BoardCell adjCell : adjacents) {
	        if (visited.contains(adjCell) || adjCell.getOccupied()) {
	            continue;
	        }

	        visited.add(adjCell);

	        if (adjCell.isDoorway()) {
	            // If we enter a doorway, add the room beyond it to targets
	            if (pathLength == 1) {
	                targets.add(adjCell);
	            } else {
	                findAllTargets(adjCell, pathLength - 1);
	            }
	        } else if (adjCell.isRoom() && !startCell.isRoom()) {
	            // If moving into a room from a doorway, stop
	            targets.add(adjCell);
	        } else if (pathLength == 1) {
	            targets.add(adjCell);
	        } else {
	            findAllTargets(adjCell, pathLength - 1);
	        }

	        visited.remove(adjCell);
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
            card.setColor(players.get(playerIndex).getColor());
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
    
    public DisproveResult handleSuggestion(Player suggester, Card suggRoom, Card suggPerson, Card suggWeapon) {
        for (Player currentPlayer : players) {
            if (currentPlayer.equals(suggester)) {
                continue; // Skip the suggester
            }
            Card returned = currentPlayer.disproveSuggestion(suggRoom, suggPerson, suggWeapon);
            if (returned != null) {
                return new DisproveResult(returned, currentPlayer);
            }
        }
        return null; // No one can disprove
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
	
	public void setCardPanel(GameCardPanel cardPanel) {
		this.cardPanel = cardPanel;
		for (Player p : players) {
	        if (p instanceof HumanPlayer) {
	        	ArrayList<Card> hand = p.getHand();
	        	for (Card c : hand) {
	        		if (c.getCardType() == CardType.PERSON) {
	        			Color color = p.getColor();
	        			c.setColor(color);
	        			cardPanel.addPeopleHand(c);
	        		}
	        		if (c.getCardType() == CardType.WEAPON) {
	        			Color color = p.getColor();
	        			c.setColor(color);
	        			cardPanel.addWeaponHand(c);
	        		}
	        		if (c.getCardType() == CardType.ROOM) {
	        			Color color = p.getColor();
	        			c.setColor(color);
	        			cardPanel.addRoomHand(c);
	        		}
	        	}
	        }
	        else if (p instanceof ComputerPlayer) {
	        	ArrayList<Card> cHand = p.getHand();
	        	for (Card c: cHand) {
	        		Color color = p.getColor();
	        		c.setColor(color);
	        	}
	        }
		}
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
	
	public void moveSuggestedPlayerToRoom(String playerName, BoardCell roomCell) {
	    for (Player player : players) {
	        if (player.getName().equals(playerName)) {
	            // Update player's position
	            player.setRow(roomCell.getRow());
	            player.setCol(roomCell.getCol());
	            player.setMovedBySuggestion(true); // We'll use this later
	            repaint();
	            break;
	        }
	    }
	}
	
	public int getCurrentPlayerIndex() {
	    return currentPlayerIndex;
	}


}
