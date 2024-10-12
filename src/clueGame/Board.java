package clueGame;

import java.util.Map;

public class Board {
    private static Board theInstance = new Board();
    private BoardCell[][] grid;
    private int numRows;
    private int numColumns;
    private String layoutConfigFile;
    private String setupConfigFile;
    private Map<Character, Room> roomMap;

    private Board() {
        // Private constructor for Singleton pattern
    }

    public static Board getInstance() {
        return theInstance;
    }

    public void initialize() {
        // Placeholder for initializing the board
    }

    public void setConfigFiles(String layoutConfigFile, String setupConfigFile) {
        this.layoutConfigFile = layoutConfigFile;
        this.setupConfigFile = setupConfigFile;
    }

    
    public Room getRoom(BoardCell cell) {
    	return new Room("");
    }
    
    public Room getRoom(char initial) {
        return new Room(""); // Placeholder for returning a room by its initial
    }

    public int getNumRows() {
        return numRows; // Placeholder value
    }

    public int getNumColumns() {
        return numColumns; // Placeholder value
    }

    public BoardCell getCell(int row, int col) {
        return new BoardCell(row, col); // Placeholder for returning a cell
    }

    public void loadSetupConfig() throws BadConfigFormatException {
        // Placeholder for loading setup config
    }

    public void loadLayoutConfig() throws BadConfigFormatException {
        // Placeholder for loading layout config
    }
}
