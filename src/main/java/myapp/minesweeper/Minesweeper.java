package myapp.minesweeper;

import java.util.Random;
import java.util.Scanner;

public class Minesweeper {
    private String[][] minesweeperArr;
    private boolean[][] doesHaveMineArr;
    private String[][] noHiddenArr;
    private int roundsCompleted = 0;
    private int totalMines = 0;
    private boolean unHide = false;
    private static final int MAX_MINEFIELD = 10;
    private boolean showBomb = true;

    // Constructor that takes in rows and cols 
    public Minesweeper(int rows , int cols){
        
        Random xPosGen = new Random();
        Random yPosGen = new Random();

        // validate the rows and col before creation
        if(!(rows > 0 && rows <= MAX_MINEFIELD && cols > 0 && cols < MAX_MINEFIELD)){
            System.out.println();
            System.out.println("Cannot create mine field with that many rows and cols");
            System.exit(0);
        }else{
            // initialize all arrary
            doesHaveMineArr = new boolean[rows][cols];
            minesweeperArr = new String[rows][cols];
            noHiddenArr = new String[rows][cols];

            // initialize the whole 2d array
            for (int i =0; i < rows ; i++){
                for(int j =0 ; j < cols; j++){
                    doesHaveMineArr[i][j]  =false;
                    minesweeperArr[i][j] = " ";
                    noHiddenArr[i][j] = " ";
                }
            }

            this.totalMines = (int) Math.ceil(rows*cols*.10);
            System.out.printf("Total no. of mines generated > %s\n", this.totalMines);
            int oldRow= 0;
            int oldCol = 0;
            // populate the mines into the location of the grid
            for(int i =0; i < this.totalMines; i++){
                int row = xPosGen.nextInt(rows);
                int col = yPosGen.nextInt(cols);
                if(i > 0){
                    if(row == oldRow && col== oldCol){
                        i--;
                        continue;
                    }
                }
                oldRow = row;
                oldCol = col;
                if(showBomb)
                    System.out.printf("Row : %s  , Col : %s\n", row, col);
                doesHaveMineArr[row][col] = true;
            }

        }  
    }
    

    public void run(){
        displayWelcome();
        while(true){
            nextTurn();
            if(hasWon()){
                break;
            }
        }
        win();
    }


    public void displayWelcome(){
        System.out.println("----=== Welcome to my Minesweeper Copyright@2022 ===----");
    }

    public boolean digMine(int revealedRow, int revealedCol, boolean withGameOver){
        boolean didAccomplishReveal = false;
        

        if(isInBounds(revealedRow, revealedCol)) {
            didAccomplishReveal = true;
            roundsCompleted++;
            if(!doesHaveMineArr[revealedRow][revealedCol]) {
                minesweeperArr[revealedRow][revealedCol] = "" 
                    + getNeighbouringMines(revealedRow, revealedCol) + "";
                System.out.println();
            } else {
                if(withGameOver)
                    gameOver();
            }
        }
		return didAccomplishReveal;
    }

    public void win(){
        System.out.println("YOU HAVE WON!");
        System.out.printf("Your score : %s", score());
        
    }

    public int score(){
        return minesweeperArr.length*minesweeperArr[0].length - totalMines - roundsCompleted;
    }

    public void gameOver(){
        System.out.println("GAME OVER!");
        System.exit(0);
    }

    public void nextTurn(){
        printRoundsCompleted();
        if(unHide) {
            printNoFogGrid();
            unHide = false;
        } else {
            printMinesweeperGrid();
            
        }
        takeInput();
    }

    public void noFog(){
        roundsCompleted++;
        unHide = true;
        System.out.println();
    }

    public void printNoFogGrid(){
        for(int i  = 0; i < noHiddenArr.length; i++){
            System.out.print(i + " |");
            for(int j = 0; j < noHiddenArr[i].length; j++){
                if(doesHaveMineArr[i][j]){
                    System.out.print("<" + minesweeperArr[i][j].substring(1,2) + ">");
                }else{
                    System.out.print(minesweeperArr[i][j]);
                }
                if(j < noHiddenArr[i].length-1){
                    System.out.print("|");
                }
            }
            System.out.println("|");
        }
        System.out.print("   ");
        for(int i =0; i < minesweeperArr[0].length; i++){
            System.out.print(i + "   ");
        }
        System.out.println();
    }

    public void takeInput(){
        System.out.println("Enter your row and col ?  e.g 1 0");
        Scanner kb = new Scanner(System.in);
        int row = kb.nextInt();
        int col = kb.nextInt();
        if(!digMine(row, col, true)) {
            roundsCompleted++;
            printInvalidCommand();
        }else{
            /*
                top left: (row-1, col-1)
                top middle: (row-1, col)
                top right: (row-1, col+1)
                left: (row, col-1)
                right: (row, col+1)
                bottom left: (row+1, col-1)
                bottom middle: (row+1, col)
                bottom right: (row+1, col+1) */
            String[] revealMoreArr = {
                    "-1,-1","-1,0","-1,+1","0,-1","0,+1","+1,-1", "+1,0", "+1,+1"};
            for(String revealMoreval : revealMoreArr){
                String[] splitRevealMoreVal = revealMoreval.split(",");
                int rowReveal = Integer.parseInt(splitRevealMoreVal[0]);
                int colReveal = Integer.parseInt(splitRevealMoreVal[1]);
                digMine(row + rowReveal, col +colReveal, false);
            }
        }
        
    }

    private boolean isInBounds(int row , int col){
        return (row >= 0 && row < doesHaveMineArr.length && col >= 0 && col < doesHaveMineArr[0].length);
    }

    public void printMinesweeperGrid(){
        for(int i =0; i <minesweeperArr.length; i++){
            System.out.print(i + "  |");
            for(int j =0; j < minesweeperArr[i].length; j ++){
                System.out.print(minesweeperArr[i][j]);
                if(j < minesweeperArr[i].length -1){
                    System.out.print("|");
                }
            }
            System.out.println("|");
        }
        System.out.print("    ");
        for(int i =0; i < minesweeperArr[0].length; i++){
            System.out.print(i + " ");
        }
        System.out.println();
    }

    public boolean hasWon(){
        boolean allMinesRevelead = true;
        boolean allSquaresRevelead = true;

        for(int i=0; i < minesweeperArr.length; i ++){
            for(int j =0 ; j < minesweeperArr[0].length; j++){
                if(doesHaveMineArr[i][j]){
                    if(minesweeperArr[i][j] != " F ")
                        allMinesRevelead = false;
                        
                }else{
                    if(minesweeperArr[i][j] == " F " || minesweeperArr[i][j] == " ? "
                        || minesweeperArr[i][j] == "   ")
                        allSquaresRevelead = false;
                }
            }
        }
        
        return allMinesRevelead && allSquaresRevelead;
    }

    public void printInvalidCommand(){
        System.out.println("Invalid command");
    }

    private int getNeighbouringMines(int row , int col){
        int numAdjMines = 0;
        for(int i = row-1; i <= row+1; i++){
            if(!(i >=0 && i < minesweeperArr.length))
                continue;
            for(int j = col-1; j < col+1; j++){
                if((i == row && j ==col) || (!(j >=0 && j < minesweeperArr[0].length))){
                    continue;
                }else{
                    if(doesHaveMineArr[i][j]){
                        numAdjMines++;
                    }
                }
            }
            
        }
        return numAdjMines;
    }

    private void printRoundsCompleted(){
        System.out.printf("Rounds completed: %s\n", roundsCompleted );
    }
}
