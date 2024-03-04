package tictactoe;

import javafx.scene.control.Button;

public class TicTacToePlayer {

    private final String name;
    private final String symbol;
    private final boolean isComputer;

    public TicTacToePlayer(String name, String symbol, boolean isComputer) {
        this.name = name;
        this.symbol = symbol;
        this.isComputer = isComputer;
    }

    public TicTacToePlayer(String name, String symbol) {
        this(name, symbol, false);
    }

    public TicTacToePlayer(String symbol) {
        this("AI", symbol, true);
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void makeMove(TicTacToeGame ticTacToeGame, Button[][] buttons, int row, int col) {
        if (isComputer) {
            computerMove(buttons, ticTacToeGame);
        } else {
            putSymbol(row, col, buttons);
        }

    }

    public void computerMove(Button[][] tab, TicTacToeGame ticTacToeGame) {
        boolean isCircle = symbol.equals("O");
        int bestScore = isCircle ? 1000 : -1000;
        int row = 0;
        int col = 0;
        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab[i].length; j++) {
                if (tab[i][j].getText().equals("")) {
                    tab[i][j].setText(symbol);
                    int score = minMax(tab, 0, isCircle, ticTacToeGame);
                    tab[i][j].setText("");
                    if (isCircle) {
                        if (score < bestScore) {
                            bestScore = score;
                            row = i;
                            col = j;
                        }
                    } else {
                        if (score > bestScore) {
                            bestScore = score;
                            row = i;
                            col = j;
                        }
                    }
                }
            }
        }
        putSymbol(row, col, tab);
    }

    public int minMax(Button[][] array, int depth, boolean isMaximizing, TicTacToeGame ticTacToeGame) {
        if (ticTacToeGame.checkWinner("X")) {
            return 100 - depth;
        } else if (ticTacToeGame.checkWinner("O")) {
            return depth - 100;
        } else if (ticTacToeGame.checkDraw()) {
            return 0;
        }

        int bestScore;
        if (isMaximizing) {
            bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < array[i].length; j++) {
                    if (array[i][j].getText().equals("")) {
                        array[i][j].setText("X");
                        int score = minMax(array, depth + 1, false, ticTacToeGame);
                        array[i][j].setText("");
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < array[i].length; j++) {
                    if (array[i][j].getText().equals("")) {
                        array[i][j].setText("O");
                        int score = minMax(array, depth + 1, true, ticTacToeGame);
                        array[i][j].setText("");
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
        }
        return bestScore;
    }

    public void putSymbol(int row, int col, Button[][] buttons) {
        String clickedButtonText = buttons[row][col].getText();
        if (clickedButtonText.equals("")) {
            buttons[row][col].setText(symbol);
            buttons[row][col].setDisable(true);
        }
    }
}
