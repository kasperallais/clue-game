package clueGame;

public class Board {
    private static Board theInstance = new Board();
    private int numRows;
    private int numColumns;

    private Board() {
        super();
    }

    public static Board getInstance() {
        return theInstance;
    }

    public void setConfigFiles(String layoutFile, String setupFile) {
        // Placeholder method, needs implementation.
    }

    public void initialize() {
        // Placeholder method, needs implementation.
    }

    public void loadSetupConfig() throws BadConfigFormatException {
        // Placeholder method, needs implementation.
    }

    public void loadLayoutConfig() throws BadConfigFormatException {
        // Placeholder method, needs implementation.
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumColumns() {
        return numColumns;
    }

    public Room getRoom(char initial) {
        // Placeholder method, needs implementation.
        return null;
    }

    public Room getRoom(BoardCell cell) {
        // Placeholder method, needs implementation.
        return null;
    }

    public BoardCell getCell(int row, int col) {
        // Placeholder method, needs implementation.
        return null;
    }
}
