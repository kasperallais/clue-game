package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoard {
    private Set<TestBoardCell> targets;

    public TestBoard() {
        targets = new HashSet<>();
    }

    public void calcTargets(TestBoardCell startCell, int pathLength) {
        targets.clear();
    }

    public TestBoardCell getCell(int row, int col) {
        return new TestBoardCell(row, col);
    }

    public Set<TestBoardCell> getTargets() {
        return targets;
    }
}
