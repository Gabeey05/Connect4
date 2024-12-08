package org.progtech.Models;

import java.util.Arrays;

public class GameModel {
    private final char[][] board;
    private final int rows;
    private final int cols;
    private char currentPlayer;

    public GameModel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.board = new char[rows][cols];
        this.currentPlayer = 'Y'; // Sárga kezd
        for (char[] row : board) {
            Arrays.fill(row, ' '); // Kezdő tábla üres
        }
    }

    public boolean makeMove(int col) {
        if (col < 0 || col >= cols || board[0][col] != ' ') {
            return false; // Érvénytelen lépés
        }
        for (int row = rows - 1; row >= 0; row--) {
            if (board[row][col] == ' ') {
                board[row][col] = currentPlayer;
                return true;
            }
        }
        return false;
    }

    public boolean isWinningMove(int col) {
        int row = findLastFilledRow(col);
        if (row == -1) {
            return false;
        }
        char player = board[row][col];
        return checkDirection(row, col, 1, 0, player) || // Vízszintes
                checkDirection(row, col, 0, 1, player) || // Függőleges
                checkDirection(row, col, 1, 1, player) || // Átlós \
                checkDirection(row, col, 1, -1, player);  // Átlós /
    }

    private int findLastFilledRow(int col) {
        for (int row = rows - 1; row >= 0; row--) {
            if (board[row][col] != ' ') {
                return row;
            }
        }
        return -1; // Ha nincs kitöltött cella
    }

    private boolean checkDirection(int row, int col, int rowDir, int colDir, char player) {
        int count = 0;
        for (int i = -3; i <= 3; i++) {
            int r = row + i * rowDir;
            int c = col + i * colDir;
            if (r >= 0 && r < rows && c >= 0 && c < cols && board[r][c] == player) {
                count++;
                if (count == 4) {
                    return true;
                }
            } else {
                count = 0;
            }
        }
        return false;
    }

    public boolean isBoardFull() {
        for (int j = 0; j < cols; j++) {
            if (board[0][j] == ' ') {
                return false;
            }
        }
        return true;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == 'Y') ? 'R' : 'Y';
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public char[][] getBoard() {
        return board;
    }
}
