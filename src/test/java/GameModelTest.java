import org.junit.jupiter.api.Test;
import org.progtech.Models.GameModel;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {

    @Test
    void testMakeMove() {
        GameModel model = new GameModel(6, 7);
        assertTrue(model.makeMove(0)); // Valid move
        assertTrue(model.makeMove(1)); // Another valid move
        assertFalse(model.makeMove(-1)); // Invalid move (out of bounds)
        assertFalse(model.makeMove(7)); // Invalid move (out of bounds)
    }

    @Test
    void testWinningMoveHorizontal() {
        GameModel model = new GameModel(6, 7);

        for (int i = 0; i < 4; i++) {
            model.makeMove(i); // Player 'Y'
            model.switchPlayer();
            model.makeMove(i); // Player 'R'
            model.switchPlayer();
        }

        assertTrue(model.isWinningMove(3));
    }

    @Test
    void testWinningMoveVertical() {
        GameModel model = new GameModel(6, 7);

        for (int i = 0; i < 4; i++) {
            model.makeMove(0);
            model.switchPlayer();
        }

        assertFalse(model.isWinningMove(0));
    }

    @Test
    void testWinningMoveDiagonal() {
        GameModel model = new GameModel(6, 7);

        // Simulate diagonal win
        model.makeMove(0); // Y
        model.switchPlayer();
        model.makeMove(1); // R
        model.switchPlayer();
        model.makeMove(1); // Y
        model.switchPlayer();
        model.makeMove(2); // R
        model.switchPlayer();
        model.makeMove(2); // Y
        model.switchPlayer();
        model.makeMove(2); // R
        model.switchPlayer();
        model.makeMove(3); // Y
        model.switchPlayer();
        model.makeMove(3); // R
        model.switchPlayer();
        model.makeMove(3); // Y
        model.switchPlayer();
        model.makeMove(3); // R
        model.switchPlayer();

        assertFalse(model.isWinningMove(3));
    }

    @Test
    void testBoardFull() {
        GameModel model = new GameModel(2, 2);

        model.makeMove(0);
        model.makeMove(1);
        model.makeMove(0);
        model.makeMove(1);

        assertTrue(model.isBoardFull());
    }
}
