package org.progtech;

import org.progtech.Controllers.GameController;
import org.progtech.DB.DbConnection;
import org.progtech.Models.GameModel;

public class Main {
    public static void main(String[] args) {
        // Alapértelmezett táblaméret: 6 sor, 7 oszlop
        int rows = 6;
        int cols = 7;

        GameModel model = new GameModel(rows, cols);

        // Létrehoz egy DbConnection példányt
        DbConnection dbConnection = new DbConnection();

        // GameController példány létrehozása a modell és az adatbázis kapcsolat alapján
        GameController controller = new GameController(model, dbConnection);

        // Játék elindítása
        controller.mainMenu();
    }
}
