package org.progtech.Controllers;

import org.progtech.DB.DbConnection;
import org.progtech.Models.GameModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import java.util.Scanner;

public class GameController {
    private final GameModel model;
    private final Scanner scanner;
    private final Random random;
    private final DbConnection dbConnection;
    private String playerName;

    public GameController(GameModel model, DbConnection dbConnection) {
        this.model = model;
        this.dbConnection = dbConnection;
        this.scanner = new Scanner(System.in);
        this.random = new Random();
    }

    public void mainMenu() {
        while (true) {
            System.out.println("\nVálassz egy lehetőséget:");
            System.out.println("1. Győztesek rangsora");
            System.out.println("2. Játék indítása");
            System.out.print("Add meg a választásod (1 vagy 2): ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> showWinnersRank();
                case "2" -> {
                    startGame();
                    return; // Exit the menu after the game
                }
                default -> System.out.println("Hibás választás. Próbáld újra!");
            }
        }
    }

    public void startGame() {
        askPlayerName();
        System.out.println("Üdvözöllek a Connect 4 játékban, " + playerName + "!");
        displayBoard();

        while (true) {
            char currentPlayer = model.getCurrentPlayer();
            System.out.println("Aktuális játékos: " + (currentPlayer == 'Y' ? "Sárga (" + playerName + ")" : "Piros (gép)"));

            int move = (currentPlayer == 'Y') ? getPlayerMove() : getComputerMove();

            if (!model.makeMove(move)) {
                System.out.println("Érvénytelen lépés. Próbáld újra!");
                continue;
            }

            displayBoard();

            if (model.isWinningMove(move)) {
                System.out.println("Győzött: " + (currentPlayer == 'Y' ? "Sárga (" + playerName + ")" : "Piros (gép)"));
                if (currentPlayer == 'Y') savePlayerWin(playerName);
                break;
            }

            if (model.isBoardFull()) {
                System.out.println("Döntetlen! A tábla megtelt.");
                break;
            }

            model.switchPlayer();
        }
    }

    public void askPlayerName() {
        System.out.print("Kérlek, add meg a neved: ");
        playerName = scanner.nextLine().trim();
        if (playerName.isEmpty()) {
            playerName = "Játékos";
        }
    }

    public int getPlayerMove() {
        while (true) {
            System.out.print("Válassz oszlopot (a-" + (char) ('a' + model.getBoard()[0].length - 1) + "): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.length() == 1) {
                char column = input.charAt(0);
                int colIndex = column - 'a';
                if (colIndex >= 0 && colIndex < model.getBoard()[0].length) {
                    return colIndex;
                }
            }
            System.out.println("Hibás bemenet. Próbáld újra!");
        }
    }

    private int getComputerMove() {
        int col;
        do {
            col = random.nextInt(model.getBoard()[0].length);
        } while (model.getBoard()[0][col] != ' '); // Only select valid columns
        System.out.println("Gép választása: " + (char) ('a' + col));
        return col;
    }

    private void displayBoard() {
        char[][] board = model.getBoard();
        System.out.print("  ");
        for (int col = 0; col < board[0].length; col++) {
            System.out.print((char) ('a' + col) + " ");
        }
        System.out.println();

        for (int row = 0; row < board.length; row++) {
            System.out.print((board.length - row) + " ");
            for (int col = 0; col < board[0].length; col++) {
                System.out.print("|" + board[row][col]);
            }
            System.out.println("|");
        }
        System.out.print("  ");
        System.out.println("-".repeat(2 * board[0].length + 1));
    }

    public void savePlayerWin(String playerName) {
        Connection connection = dbConnection.getConnection();

        if (connection == null) {
            System.out.println("Nem sikerült kapcsolódni az adatbázishoz.");
            return;
        }

        try {
            String selectQuery = "SELECT winNum FROM playerswin WHERE name = ?";
            PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
            selectStmt.setString(1, playerName);
            ResultSet resultSet = selectStmt.executeQuery();

            if (resultSet.next()) {
                int currentWins = resultSet.getInt("winNum");
                String updateQuery = "UPDATE playerswin SET winNum = ? WHERE name = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                updateStmt.setInt(1, currentWins + 1);
                updateStmt.setString(2, playerName);
                updateStmt.executeUpdate();
                updateStmt.close();
                System.out.println("Frissítve a győzelmi szám.");
            } else {
                String insertQuery = "INSERT INTO playerswin (name, winNum) VALUES (?, ?)";
                PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                insertStmt.setString(1, playerName);
                insertStmt.setInt(2, 1);
                insertStmt.executeUpdate();
                insertStmt.close();
                System.out.println("Játékos hozzáadva az adatbázishoz.");
            }

            resultSet.close();
            selectStmt.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("Hiba történt az adatbázisművelet során: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void showWinnersRank() {
        Connection connection = dbConnection.getConnection();

        if (connection == null) {
            System.out.println("Nem sikerült kapcsolódni az adatbázishoz.");
            return;
        }

        try {
            String query = "SELECT name, winNum FROM playerswin ORDER BY winNum DESC";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("\nGyőztesek rangsora:");
            System.out.println("-------------------");
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int wins = resultSet.getInt("winNum");
                System.out.println(name + ": " + wins + " győzelem");
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("Hiba történt a rangsor lekérdezése során: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
