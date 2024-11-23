package tests;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;
import clueGame.Room;

public class FileInitTests {
	public static final int NUM_ROWS = 23;
	public static final int NUM_COLUMNS = 24;

    private static Board board;
	
    @BeforeAll
    public static void setUp() {
        board = Board.getInstance();
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        board.initialize();
    }

    @Test
    public void testRoomLabels() {
		assertEquals("Library of Secrets", board.getRoom('L').getName() );
		assertEquals("Enchanted Garden", board.getRoom('G').getName() );
		assertEquals("Shadow Study", board.getRoom('S').getName() );
		assertEquals("Ballroom of Echoes", board.getRoom('B').getName() );
		assertEquals("Walkway", board.getRoom('W').getName() );
    }

    @Test
    public void testBoardDimensions() {
        assertEquals(25, board.getNumRows());
        assertEquals(24, board.getNumColumns());
    }

    @Test
    public void testFourDoorDirections() {
    	// Doorway UP
		BoardCell cell = board.getCell(5, 0);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.UP, cell.getDoorDirection());
		// Doorway DOWN
		cell = board.getCell(3, 6);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());
		// Doorway LEFT
		cell = board.getCell(2,16);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.LEFT, cell.getDoorDirection());
		// Doorway RIGHT
		cell = board.getCell(6,2);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.DOWN, cell.getDoorDirection());
		// Walkways are not doors
		cell = board.getCell(6, 6);
		assertFalse(cell.isDoorway());
    }

	@Test
	public void testNumberOfDoorways() {
		int numDoors = 0;
		for (int row = 0; row < board.getNumRows(); row++)
			for (int col = 0; col < board.getNumColumns(); col++) {
				BoardCell cell = board.getCell(row, col);
				if (cell.isDoorway())
					numDoors++;
			}
		Assert.assertEquals(10, numDoors);
	}

	@Test
	public void testRooms() {
		// Standard Room Cell test
		BoardCell cell = board.getCell(2, 12);
		Room room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Enchanted Garden");
		assertFalse(cell.isLabel());
		assertFalse(cell.isRoomCenter());
		assertFalse(cell.isDoorway());
		
		// Label Room Cell test
		cell = board.getCell(2, 20);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Shadow Study" ) ;
		assertTrue( cell.isLabel() );
		assertTrue( room.getLabelCell() == cell );
		
		// Room Center Cell test
		cell = board.getCell(2, 2);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Library of Secrets" ) ;
		assertTrue( cell.isRoomCenter() );
		assertTrue( room.getCenterCell() == cell );
		
		// Walkway test
		cell = board.getCell(6, 17);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Walkway" ) ;
		assertFalse( cell.isRoomCenter() );
		assertFalse( cell.isLabel() );
		
	}
}
