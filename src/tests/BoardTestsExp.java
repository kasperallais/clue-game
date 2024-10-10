package tests;

import experiment.TestBoard;
import experiment.TestBoardCell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTestsExp {
    private TestBoard board;

    @BeforeEach
    public void setUp() {
        board = new TestBoard();
    }

    @Test
    public void testAdjacencyTopLeftCorner() {
        TestBoardCell cell = board.getCell(0, 0);
        board.findAdj(cell);
        Set<TestBoardCell> adjList = cell.getAdjList();
        assertEquals(2, adjList.size());
        //System.out.println()
        assertTrue(adjList.contains(board.getCell(0, 1)));
        assertTrue(adjList.contains(board.getCell(1, 0)));
    }

    @Test
    public void testAdjacencyBottomRightCorner() {
        TestBoardCell cell = board.getCell(3, 3);
        board.findAdj(cell);
        Set<TestBoardCell> adjList = cell.getAdjList();
        assertEquals(2, adjList.size());
        assertTrue(adjList.contains(board.getCell(3, 2)));
        assertTrue(adjList.contains(board.getCell(2, 3)));
    }

    @Test
    public void testAdjacencyRightEdge() {
        TestBoardCell cell = board.getCell(1, 3);
        board.findAdj(cell);
        Set<TestBoardCell> adjList = cell.getAdjList();
        assertEquals(3, adjList.size());
        assertTrue(adjList.contains(board.getCell(0, 3)));
        assertTrue(adjList.contains(board.getCell(2, 3)));
        assertTrue(adjList.contains(board.getCell(1, 2)));
    }

    @Test
    public void testAdjacencyLeftEdge() {
        TestBoardCell cell = board.getCell(3, 0);
        board.findAdj(cell);
        Set<TestBoardCell> adjList = cell.getAdjList();
        assertEquals(2, adjList.size());
        assertTrue(adjList.contains(board.getCell(2, 0)));
        assertTrue(adjList.contains(board.getCell(3, 1)));
    }

    @Test
    public void testCalcTargetsNormal() {
        TestBoardCell cell = board.getCell(2, 2);
        board.calcTargets(cell, 2);
        Set<TestBoardCell> targets = board.getTargets();
        assertEquals(6, targets.size());
        assertTrue(targets.contains(board.getCell(0, 2)));
        assertTrue(targets.contains(board.getCell(3, 1)));
    }

    @Test
    public void testCalcTargetsWithOccupied() {
        TestBoardCell cell = board.getCell(0, 3);
        TestBoardCell occupiedCell = board.getCell(1, 3);
        occupiedCell.setOccupied(true);
        board.calcTargets(cell, 3);
        Set<TestBoardCell> targets = board.getTargets();
        assertFalse(targets.contains(occupiedCell));
        assertTrue(targets.contains(board.getCell(0, 0)));
        assertTrue(targets.contains(board.getCell(1, 1)));
        assertTrue(targets.contains(board.getCell(2, 2)));
    }

    @Test
    public void testCalcTargetsWithRoom() {
        TestBoardCell cell = board.getCell(2, 2);
        TestBoardCell roomCell = board.getCell(3, 3);
        roomCell.setRoom(true);
        board.calcTargets(cell, 2);
        Set<TestBoardCell> targets = board.getTargets();
        assertTrue(targets.contains(roomCell));
    }
    
    @Test
    public void testTargetRoom() {
    	board.getCell(1, 3).setRoom(true);
    	TestBoardCell cell = board.getCell(0, 3);
    	board.calcTargets(cell, 3);
    	Set<TestBoardCell> targets = board.getTargets();
    	assertEquals(4, targets.size());
    } 
    
    @Test
    public void testTargetMixed() {
    	board.getCell(1, 2).setRoom(true);
    	board.getCell(0, 2).setOccupied(true);
    	TestBoardCell cell = board.getCell(0, 3);
    	board.calcTargets(cell, 3);
    	Set<TestBoardCell> targets = board.getTargets();
    	assertEquals(3, targets.size());
    } 
}
