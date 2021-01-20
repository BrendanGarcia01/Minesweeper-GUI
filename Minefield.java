import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;

public class Minefield extends BorderPane {

    private GridPane fieldPane;
    private Label[][] visibleField;
    private String[][] invisibleField;
    private String[][] checkField;
    private boolean[][] flagged;
    private Label timer;
    private Label mineCounter;
    private Button restartButton;

    private final int CELL_WIDTH = 36;
    private int rows;
    private int columns;
    private int mines;
    private int mineCount;
    private String time;
    private boolean win;

    //constructor of the minefield used in the Minesweeper class
    public Minefield() {

        rows = 10;
        columns = 10;
        mines = 10;
        mineCount = mines;
        win = false;

        visibleField = new Label[rows][columns];
        invisibleField = new String[rows][columns];
        checkField = new String[rows][columns];
        flagged = new boolean[rows][columns];
        Font fieldFont = new Font(30);
        for(int row = 0;row < rows;row++) {
            for(int column = 0;column < columns;column++) {
                visibleField[row][column] = new Label();
                visibleField[row][column].setText("?");
                visibleField[row][column].setFont(fieldFont);
                visibleField[row][column].setPrefSize(CELL_WIDTH,CELL_WIDTH);
                flagged[row][column] = false;
            }
        }



        fieldPane = new GridPane();
        fieldPane.setAlignment(Pos.CENTER);
        for(int row = 0;row < rows;row++) {
            for(int column = 0;column < columns;column++) {
                fieldPane.add(visibleField[row][column],column,row);
            }
        }

        //Top of the screen.  Includes reset, number of mines left, and timer
        HBox menu = new HBox();
        menu.setPrefWidth(400);
        menu.setPrefHeight(50);
        menu.setSpacing(85);
        mineCounter = new Label();
        mineCounter.setPrefSize(133.33,50);
        mineCounter.setText("Mines: " + mines);
        restartButton = new Button();
        restartButton.setPrefSize(133.33,50);
        restartButton.setText(":)");

        time = "Time: 0:00";
        timer = new Label();
        timer.setPrefSize(133.33,50);
        timer.setText(time);

        menu.getChildren().addAll(mineCounter, restartButton, timer);

        this.setTop(menu);
        this.setCenter(fieldPane);

        fieldPane.setOnMouseClicked(new MouseHandler());
        fieldPane.setOnMouseDragged(new MouseHandler());
        restartButton.setOnAction(new RestartHandler());


        int allCells = rows * columns;
        double ratio = (double) mines / (double) allCells;

        int minesLeft = mines;
        int cellsLeft = allCells;
        double random;

        //Set Mine Cells
        for(int i = 0;i < rows;i++) {
            for(int j = 0;j < columns;j++) {
                random = Math.random();
                if(minesLeft == cellsLeft) {
                    invisibleField[i][j] = "*";
                    minesLeft--;
                }
                else if(minesLeft == 0) invisibleField[i][j] = "0";
                else if(random >= ratio) invisibleField[i][j] = "0";
                else if(random < ratio) {
                    invisibleField[i][j] = "*";
                    minesLeft--;
                }
                cellsLeft--;
            }
        }

        //Set check field
        for(int i = 0;i < rows;i++) {
            for(int j = 0;j < columns;j++) {
                if(invisibleField[i][j].equals("*")) checkField[i][j] = "X";
                else checkField[i][j] = "O";
            }
        }


        //Number Assignment
        int count;

        for(int i = 0;i < rows;i++) {
            for (int j = 0; j < columns; j++) {

                count = 0;

                if(!(invisibleField[i][j].contains("*"))) {
                    try {
                        if (invisibleField[i + 1][j].contains("*")) count++;
                    } catch (IndexOutOfBoundsException ex) {
                    }
                    try {
                        if (invisibleField[i + 1][j + 1].contains("*")) count++;
                    } catch (IndexOutOfBoundsException ex) {
                    }
                    try {
                        if (invisibleField[i][j + 1].contains("*")) count++;
                    } catch (IndexOutOfBoundsException ex) {
                    }
                    try {
                        if (invisibleField[i - 1][j].contains("*")) count++;
                    } catch (IndexOutOfBoundsException ex) {
                    }
                    try {
                        if (invisibleField[i - 1][j - 1].contains("*")) count++;
                    } catch (IndexOutOfBoundsException ex) {
                    }
                    try {
                        if (invisibleField[i][j - 1].contains("*")) count++;
                    } catch (IndexOutOfBoundsException ex) {
                    }
                    try {
                        if (invisibleField[i + 1][j - 1].contains("*")) count++;
                    } catch (IndexOutOfBoundsException ex) {
                    }
                    try {
                        if (invisibleField[i - 1][j + 1].contains("*")) count++;
                    } catch (IndexOutOfBoundsException ex) {
                    }

                    invisibleField[i][j] = "" + count;
                }
            }
        }
        
    }

    //
    private void check(int x, int y) {
        if(checkField[x][y].contains("O")) {
            visibleField[x][y].setText(invisibleField[x][y]);
            if(invisibleField[x][y].contains("0")) {
                checkField[x][y] = "X";
                zeroCellCluster(x,y);
            }
            else checkField[x][y] = "X";
        }
    }

    private void zeroCellCluster(int x, int y) {

        try {
            check(x+1,y);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            check(x+1,y+1);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            check(x,y+1);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            check(x-1,y);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            check(x-1,y-1);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            check(x,y-1);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            check(x+1,y-1);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            check(x-1,y+1);
        } catch (IndexOutOfBoundsException ex) {}

    }

    private boolean checkWin() {
        for(int i = 0;i < rows;i++) {
            for(int j = 0;j < columns;j++) {
                if(!(checkField[i][j].contains("X"))) return false;
            }
        }
        return true;
    }


    private class MouseHandler implements EventHandler<MouseEvent> {
        public void handle(MouseEvent event) {

            // Gets mouse location and converts it into column and row numbers
            double x = event.getX();
            double y = event.getY();
            int columnNum = (int)((x-10)/35);
            int rowNum = (int)((y-25)/45);

            if(x > 10 && x < columns*35+10 && y > 25 && y < rows*45+25) {

                //Checks if mouse is pressed or released
                if(event.getEventType() == MouseEvent.MOUSE_CLICKED && event.getButton() == MouseButton.PRIMARY) {

                    //First checks that the mouse is in the desired dimensions of grid and
                    //then sets primary and secondary color according to mouse location
                    //(Uses try-catch statements to prevent IndexOutOfBoundExceptions)
                    if(visibleField[rowNum][columnNum].getText().equals("?")) {

                        visibleField[rowNum][columnNum].setText(invisibleField[rowNum][columnNum]);
                        checkField[rowNum][columnNum] = "X";
                        if(invisibleField[rowNum][columnNum].equals("*")) {
                            for(int i = 0;i < rows;i++) {
                                for (int j = 0; j < columns; j++) {
                                    visibleField[i][j].setText(invisibleField[i][j]);
                                }
                            }
                            restartButton.setText("X(");
                            //stop timer
                        } else if(invisibleField[rowNum][columnNum].equals("0")) {
                            zeroCellCluster(rowNum, columnNum);
                        }
                        win = checkWin();
                        if (win == true) {
                            restartButton.setText("B)");
                            //stop timer
                        }
                    }
                } else if(event.getEventType() == MouseEvent.MOUSE_CLICKED && event.getButton() == MouseButton.SECONDARY) {

                    if(flagged[rowNum][columnNum] == false && mineCount > 0) {
                        visibleField[rowNum][columnNum].setText("F");
                        flagged[rowNum][columnNum] = true;
                        mineCount--;
                        mineCounter.setText("Mines: " + mineCount);
                        if(mineCount == 0) win = checkWin(); //Doesn't work because not all non-mines have been pressed
                        if (win == true) {
                            restartButton.setText("B)");
                            //stop timer
                        }
                    } else if(flagged[rowNum][columnNum] == true){
                        visibleField[rowNum][columnNum].setText("?");
                        flagged[rowNum][columnNum] = false;
                        mineCount++;
                        mineCounter.setText("Mines: " + mineCount);
                    }
                }

            }

        }//end handle()
    }//end MouseHandler

    private class RestartHandler implements EventHandler<ActionEvent> {
        //Overrides the abstact method handle()
        public void handle(ActionEvent event) {
            for(int row = 0;row < rows;row++) {
                for (int column = 0; column < columns; column++) {
                    visibleField[row][column].setText("?");
                    visibleField[row][column].setPrefSize(CELL_WIDTH, CELL_WIDTH);
                    flagged[row][column] = false;
                }
            }

            restartButton.setText(":)");
            mineCounter.setText("Mines: " + mines);
            mineCount = mines;
            int allCells = rows * columns;
            double ratio = (double) mines / (double) allCells;

            int minesLeft = mines;
            int cellsLeft = allCells;
            double random;

            //Set Mine Cells
            for(int i = 0;i < rows;i++) {
                for(int j = 0;j < columns;j++) {
                    random = Math.random();
                    if(minesLeft == cellsLeft) {
                        invisibleField[i][j] = "*";
                        minesLeft--;
                    }
                    else if(minesLeft == 0) invisibleField[i][j] = "0";
                    else if(random >= ratio) invisibleField[i][j] = "0";
                    else if(random < ratio) {
                        invisibleField[i][j] = "*";
                        minesLeft--;
                    }
                    cellsLeft--;
                }
            }

            //Set check field
            for(int i = 0;i < rows;i++) {
                for(int j = 0;j < columns;j++) {
                    if(invisibleField[i][j].equals("*")) checkField[i][j] = "X";
                    else checkField[i][j] = "O";
                }
            }


            //Number Assignment
            int count;

            for(int i = 0;i < rows;i++) {
                for (int j = 0; j < columns; j++) {

                    count = 0;

                    if(!(invisibleField[i][j].contains("*"))) {
                        try {
                            if (invisibleField[i + 1][j].contains("*")) count++;
                        } catch (IndexOutOfBoundsException ex) {}
                        try {
                            if (invisibleField[i + 1][j + 1].contains("*")) count++;
                        } catch (IndexOutOfBoundsException ex) {}
                        try {
                            if (invisibleField[i][j + 1].contains("*")) count++;
                        } catch (IndexOutOfBoundsException ex) {}
                        try {
                            if (invisibleField[i - 1][j].contains("*")) count++;
                        } catch (IndexOutOfBoundsException ex) {}
                        try {
                            if (invisibleField[i - 1][j - 1].contains("*")) count++;
                        } catch (IndexOutOfBoundsException ex) {}
                        try {
                            if (invisibleField[i][j - 1].contains("*")) count++;
                        } catch (IndexOutOfBoundsException ex) {}
                        try {
                            if (invisibleField[i + 1][j - 1].contains("*")) count++;
                        } catch (IndexOutOfBoundsException ex) {}
                        try {
                            if (invisibleField[i - 1][j + 1].contains("*")) count++;
                        } catch (IndexOutOfBoundsException ex) {}

                        invisibleField[i][j] = "" + count;
                    }
                }
            }
        }
    }
}