import org.junit.jupiter.api.Test;
import org.progtech.DB.DbConnection;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class DbConnectionTest {

    @Test
    void testGetConnection() {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.getConnection();
        assertNotNull(connection, "Connection should not be null if database is available");
    }
}
