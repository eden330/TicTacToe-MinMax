package tictactoe;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Optional;

public class TicTacToeGame extends Application {

    private final TicTacToePlayer userPlayer;
    private final TicTacToePlayer botPlayer;
    private TicTacToePlayer currentPlayer;
    private boolean botTurn = false;
    private final Button[][] buttons = new Button[3][3];
    private final int boardSize = buttons.length;

    {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Welcome to Tic Tac Toe game!");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter your name:");
        Optional<String> result = dialog.showAndWait();
        String name = "";
        if (result.isPresent()) {
            name = result.orElseThrow();
        }
        ChoiceDialog<String> symbolDialog = new ChoiceDialog<>("X", "O");
        symbolDialog.setTitle("Symbol choice");
        symbolDialog.setHeaderText(null);
        symbolDialog.setContentText("Select your symbol:");
        Optional<String> symbolResult = symbolDialog.showAndWait();
        String symbol = "";
        if (symbolResult.isPresent()) {
            symbol = symbolResult.orElseThrow();
        }
        userPlayer = new TicTacToePlayer(name, symbol);
        botPlayer = new TicTacToePlayer(symbol.equals("X") ? "O" : "X");
        currentPlayer = userPlayer;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(createScene());
        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.show();
    }

    private Scene createScene() {
        GridPane gridPane = new GridPane();
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Button button = new Button();
                button.setMinSize(100, 100);
                buttons[i][j] = button;
                gridPane.add(button, j, i);
                button.setOnAction(event -> {
                    Button clickedButton = (Button) event.getSource();
                    int rowIndex = GridPane.getRowIndex(clickedButton);
                    int colIndex = GridPane.getColumnIndex(clickedButton);
                    handleButtonClick(rowIndex, colIndex);
                });
            }
        }
        return new Scene(gridPane, boardSize * 104, boardSize * 104);
    }

    private void handleButtonClick(int row, int col) {
        if (!botTurn && buttons[row][col].getText().isEmpty()) {
            currentPlayer.makeMove(this, buttons, row, col);
            if (checkDraw()) {
                disableAllButtons();
                showResultDialog("It's a draw!");
                return;
            } else if (checkWinner(currentPlayer.getSymbol())) {
                disableAllButtons();
                showResultDialog(currentPlayer.getName() + " won!");
                return;
            }
            currentPlayer = botPlayer;
            botTurn = true;
            moveBot();
        }
    }

    public void moveBot() {
        if (botTurn) {
            currentPlayer.makeMove(this, buttons, 0, 0);
            if (checkDraw()) {
                disableAllButtons();
                showResultDialog("It's a draw!");
                return;
            } else if (checkWinner(currentPlayer.getSymbol())) {
                disableAllButtons();
                showResultDialog(currentPlayer.getName() + " won!");
                return;
            }
            currentPlayer = userPlayer;
            botTurn = false;
        }
    }

    private void showResultDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Result");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setOnHidden(event -> Platform.exit());
        alert.showAndWait();
    }

    private void disableAllButtons() {
        for (Button[] row : buttons) {
            for (Button button : row) {
                button.setDisable(true);
            }
        }
    }

    public boolean checkDraw() {
        for (Button[] button : buttons) {
            for (int j = 0; j < buttons.length; j++) {
                if (button[j].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkWinner(String value) {
        int counter = 0;
        for (int i = 0, j = 0; i < buttons.length && j < buttons.length; i++, j++) {
            if (buttons[i][j].getText().equals(value)) {
                counter++;
            }
            if (counter == boardSize) {
                return true;
            }
        }
        counter = 0;

        for (int i = boardSize - 1, j = 0; i < buttons.length && j < buttons.length; i--, j++) {
            if (buttons[i][j].getText().equals(value)) {
                counter++;
            }
            if (counter == boardSize) {
                return true;
            }
        }
        counter = 0;

        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons.length; j++) {
                if (buttons[i][j].getText().equals(value)) {
                    counter++;
                }
                if (counter == boardSize) {
                    return true;
                }
            }
            counter = 0;
        }

        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons.length; j++) {
                if (buttons[j][i].getText().equals(value)) {
                    counter++;
                }
                if (counter == boardSize) {
                    return true;
                }
            }
            counter = 0;
        }
        return false;
    }
}


