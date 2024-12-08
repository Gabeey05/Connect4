import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.progtech.Controllers.GameController;
import org.progtech.DB.DbConnection;
import org.progtech.Models.GameModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.mockito.Mockito.*;

class GameControllerTest {
    private GameController controller;
    private DbConnection mockDbConnection;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private GameModel mockModel;

    @BeforeEach
    void setUp() throws Exception {
        mockDbConnection = mock(DbConnection.class);
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockDbConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        // Mock GameModel
        mockModel = mock(GameModel.class);
        when(mockModel.getBoard()).thenReturn(new char[6][7]);
        controller = new GameController(mockModel, mockDbConnection);
    }

    @Test
    void testSavePlayerWin() throws Exception {
        // Simulate player not existing in DB
        when(mockResultSet.next()).thenReturn(false);

        // Save player win
        controller.savePlayerWin("TestPlayer");

        // Verify insert query was executed
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    void testSavePlayerWinExistingPlayer() throws Exception {
        // Simulate player existing in DB
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("winNum")).thenReturn(5);

        // Save player win
        controller.savePlayerWin("TestPlayer");

        // Verify update query was executed
        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    void testShowWinnersRank() throws Exception {
        // Simulate database return values
        when(mockResultSet.next()).thenReturn(true).thenReturn(false); // One winner
        when(mockResultSet.getString("name")).thenReturn("TestPlayer");
        when(mockResultSet.getInt("winNum")).thenReturn(10);

        // Call showWinnersRank and verify output
        controller.showWinnersRank();

        // Verify query execution
        verify(mockStatement, times(1)).executeQuery();
    }

    // Test: Show winners' rank when database is empty
    @Test
    void testShowWinnersRankNoData() throws Exception {
        // Simulate database returning no results
        when(mockResultSet.next()).thenReturn(false);

        // Call showWinnersRank and verify output
        controller.showWinnersRank();

        // Verify query execution
        verify(mockStatement, times(1)).executeQuery();
    }
}
