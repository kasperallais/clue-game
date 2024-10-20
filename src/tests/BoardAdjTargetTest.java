package tests;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTest {

    private static Board board;

    @BeforeAll
    public static void setUp() {
        board = Board.getInstance();
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        board.initialize();
    }

    // LIGHT ORANGE
    @Test
    public void testAdjacenciesRooms() {
        // Library of Secrets (Single Door, secret Passage)
        Set<BoardCell> testList = board.getAdjList(2, 2);  // Library of Secrets
        assertEquals(2, testList.size());
        assertTrue(testList.contains(board.getCell(5, 0)));  // Door to Walkway
        assertTrue(testList.contains(board.getCell(2, 21)));  // Secret passage to Shadow Study

        // Shadow Study Test (Single door, secret Passage)
        testList = board.getAdjList(2, 21);  // Gloomy Cellar
        assertEquals(2, testList.size());
        assertTrue(testList.contains(board.getCell(5, 20)));  // Door to Walkway
        assertTrue(testList.contains(board.getCell(2, 2)));  // Secret passage to Library of Secrets
    }

    // LIGHT ORANGE
    @Test
    public void testAdjacencyDoor() {
        Set<BoardCell> testList = board.getAdjList(20, 11); // Gloomy Cellar Door
        assertEquals(2, testList.size());
        assertTrue(testList.contains(board.getCell(21, 14)));  // Room adjacent
        assertTrue(testList.contains(board.getCell(19, 11)));  // Walkway adjacent

        
        testList = board.getAdjList(20, 5); // Crimson Parlor Entrance
        assertEquals(3, testList.size());
        assertTrue(testList.contains(board.getCell(21, 2)));  // Room adjacent
        assertTrue(testList.contains(board.getCell(19, 5)));  // Walkway adjacent
        assertTrue(testList.contains(board.getCell(21, 5)));  // Walkway adjacent
    }

    // DARK ORANGE
    @Test
    public void testAdjacencyWalkways() {
        // Bottom Edge of Board, just one adjacent walkway
        Set<BoardCell> testList = board.getAdjList(24, 6);
        assertEquals(1, testList.size());
        assertTrue(testList.contains(board.getCell(23, 6)));

        // Near a door, but not adjacent
        testList = board.getAdjList(22, 5);
        assertEquals(3, testList.size());
        assertTrue(testList.contains(board.getCell(23, 5)));
        assertTrue(testList.contains(board.getCell(21, 5)));
        assertTrue(testList.contains(board.getCell(22, 6)));

        // Adjacent to door and walkways
        testList = board.getAdjList(20, 6);
        assertEquals(3, testList.size());
        assertTrue(testList.contains(board.getCell(20, 5)));
        assertTrue(testList.contains(board.getCell(19, 6)));
        assertTrue(testList.contains(board.getCell(21, 6)));
    }

    // LIGHT BLUE
    @Test
    public void testTargetsInBallroomOfEchoes() {
        // Roll 1
        board.calcTargets(board.getCell(12, 21), 1);
        Set<BoardCell> targets = board.getTargets();
        assertEquals(1, targets.size());
        assertTrue(targets.contains(board.getCell(10, 17)));  // Walkway | Door

        // Roll 3
        board.calcTargets(board.getCell(12, 21), 3);
        targets = board.getTargets();
        assertEquals(4, targets.size());
        assertTrue(targets.contains(board.getCell(8, 17)));  // Walkway
        assertTrue(targets.contains(board.getCell(9, 16)));  // Walkway
    }

    @Test
    public void testTargetsInGloomyCellar() {
        // Roll 1
        board.calcTargets(board.getCell(21, 14), 1);
        Set<BoardCell> targets = board.getTargets();
        assertEquals(2, targets.size());
        assertTrue(targets.contains(board.getCell(20, 11)));  // Walkway | Door
        assertTrue(targets.contains(board.getCell(2, 11)));  // Secret Passage to Enchanted Garden

        // Roll 3
        board.calcTargets(board.getCell(21, 14), 3);
        targets = board.getTargets();
        assertEquals(6, targets.size());
        assertTrue(targets.contains(board.getCell(2, 6)));  // Walkway
        assertTrue(targets.contains(board.getCell(18, 11)));  // Walkway
    }

    @Test
    public void testTargetsAtDoor() {
        // Roll 1
        board.calcTargets(board.getCell(8, 17), 1);
        Set<BoardCell> targets = board.getTargets();
        assertEquals(4, targets.size());
        assertTrue(targets.contains(board.getCell(20, 9)));  // Room
        assertTrue(targets.contains(board.getCell(16, 8)));  // Walkway
        assertTrue(targets.contains(board.getCell(15, 9)));  // Walkway

        // Test a roll of 3 at door
        board.calcTargets(board.getCell(8, 17), 3);
        targets = board.getTargets();
        assertEquals(12, targets.size());
        assertTrue(targets.contains(board.getCell(12, 20)));  // Room
        assertTrue(targets.contains(board.getCell(3, 20)));  // Walkway
        assertTrue(targets.contains(board.getCell(7, 17)));  // Walkway
    }

    @Test
    public void testTargetsInWalkway() {
        // Test a roll of 1
        board.calcTargets(board.getCell(11, 2), 1);
        Set<BoardCell> targets = board.getTargets();
        assertEquals(2, targets.size());
        assertTrue(targets.contains(board.getCell(11, 1)));  // Adjacent walkway
        assertTrue(targets.contains(board.getCell(11, 3)));  // Adjacent walkway

        // Test a roll of 4
        board.calcTargets(board.getCell(13, 7), 4);
        targets = board.getTargets();
        assertEquals(15, targets.size());
        assertTrue(targets.contains(board.getCell(14, 2)));  // Walkway
        assertTrue(targets.contains(board.getCell(15, 9)));  // Walkway
    }

    // RED
    @Test
    public void testTargetsOccupied() {
        // Test a roll of 4, cell (15, 7) is occupied
        board.getCell(15, 7).setOccupied(true);
        board.calcTargets(board.getCell(13, 7), 4);
        board.getCell(15, 7).setOccupied(false);
        Set<BoardCell> targets = board.getTargets();
        assertEquals(13, targets.size());
        assertTrue(targets.contains(board.getCell(14, 2)));
        assertTrue(targets.contains(board.getCell(15, 9)));
        assertFalse(targets.contains(board.getCell(15, 7)));  // Occupied cell should not be a target
    }
}
