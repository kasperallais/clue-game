package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoard {
    private Set<TestBoardCell> targets;

    public TestBoard() {
        targets = new HashSet<>();
    }

    public void calcTargets(TestBoardCell startCell, int pathLength) {
        // Stub method - implementation will come later
        targets.clear();
    }

    public TestBoardCell getCell(int row, int col) {
        // Stub method - implementation will come later
        return new TestBoardCell(row, col); // Placeholder return
    }

    public Set<TestBoardCell> getTargets() {
        return targets; // Currently returns an empty set
    }
}
