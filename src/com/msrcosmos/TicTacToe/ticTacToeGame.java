package com.msrcosmos.TicTacToe;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
public class ticTacToeGame {
	
	
	
	/**
	 * This is the code for a console based TicTacToe game.
	 * You can configure the grid sze of the board, by changing the value of "ROWS", "COLS"
	 *  
	 */
	   // Name-constants to represent the seeds and cell contents
	   public static final int EMPTY = 0;
	   public static final int CROSS = 1;
	   public static final int NOUGHT = 2;
	 
	   // Name-constants to represent the various states of the game
	   public static final int PLAYING = 0;
	   public static final int DRAW = 1;
	   public static final int CROSS_WON = 2;
	   public static final int NOUGHT_WON = 3;
	 
	   // The game board and the game status
	   public static final int ROWS = 10;// To Configure the number of rows and columns; Rows and columns should be equal
	   public static final int MIN_WIN_CRITERIA = 3;
	   public static int winCriteria = 3; // Should not be greater than number of Rows, if greater will be reset to number of rows
	   public static int[][] board = new int[ROWS][ROWS]; // game board in 2D array
	                                                      //  containing (EMPTY, CROSS, NOUGHT)
	   public static int currentState;  // the current state of the game
	                                    // (PLAYING, DRAW, CROSS_WON, NOUGHT_WON)
	   public static int currentPlayer; // the current player (CROSS or NOUGHT)
	   public static int currntRow, currentCol; // current seed's row and column
	   // The name of the save game file to open. The file path should be same as saved game
       public static String savedGame = "Savegame.text";
	 
	   public static Scanner in = new Scanner(System.in); // the input Scanner
	 
	   /** The entry main method (the program starts here) */
	   public static void main(String[] args) {
		   
		   checkWinCriteria();
		   
		   System.out.println("Would you like to load the saved game Y/N");
		   String load = in.next();
		   if(load.equalsIgnoreCase("Y")){
			   loadGame();
			   printBoard();
		   }
		   else
		   {
	      // Initialize the game-board and current status
	      initGame();
		   }
	      // Play the game once
	      do {
	         playerMove(currentPlayer); // update currentRow and currentCol
	         updateGame(currentPlayer, currntRow, currentCol); // update currentState
	         printBoard();
	         saveGame();
	         // Print message if game-over
	         if (currentState == CROSS_WON) {
	            System.out.println("'X' won! Bye!");
	         } else if (currentState == NOUGHT_WON) {
	            System.out.println("'O' won! Bye!");
	         } else if (currentState == DRAW) {
	            System.out.println("It's a Draw! Bye!");
	         }
	         // Switch player
	         currentPlayer = (currentPlayer == CROSS) ? NOUGHT : CROSS;
	      } while (currentState == PLAYING); // repeat if not game-over
	   }
	   
	   /* method to check the winning criteria
	    * Minimun should be greater than MIN_WINNING_CRITERIA
	    * Maximum is equal to the board size*/
	   public static void checkWinCriteria(){
		   if (winCriteria > ROWS){
			   winCriteria = ROWS;
		   }
		   else if (winCriteria < MIN_WIN_CRITERIA) {
			winCriteria = MIN_WIN_CRITERIA;
		}
	   }
	 
	   /** Initialize the game-board contents and the current states */
	   public static void initGame() {
	      for (int row = 0; row < ROWS; ++row) {
	         for (int col = 0; col < ROWS; ++col) {
	            board[row][col] = EMPTY;  // all cells empty
	         }
	      }
	      currentState = PLAYING; // ready to play
	      currentPlayer = CROSS;  // cross plays first
	   }
	 
	   /** Player with the "theSeed" makes one move, with input validation.
	       Update global variables "currentRow" and "currentCol". */
	   public static void playerMove(int theSeed) {
	      boolean validInput = false;  // for input validation
	      do {
	         if (theSeed == CROSS) {
	            System.out.print("Player 'X', enter your move (row[1-"+ROWS+"] column[1-"+ROWS+"]): ");
	         } else {
	            System.out.print("Player 'O', enter your move (row[1-"+ROWS+"] column[1-"+ROWS+"]): ");
	         }
	         int row = in.nextInt() - 1;  // array index starts at 0 instead of 1
	         int col = in.nextInt() - 1;
	         if (row >= 0 && row < ROWS && col >= 0 && col < ROWS && board[row][col] == EMPTY) {
	            currntRow = row;
	            currentCol = col;
	            board[currntRow][currentCol] = theSeed;  // update game-board content
	            validInput = true;  // input okay, exit loop
	         } else {
	            System.out.println("This move at (" + (row + 1) + "," + (col + 1)
	                  + ") is not valid. Try again...");
	         }
	      } while (!validInput);  // repeat until input is valid
	   }
	 
	   /** Update the "currentState" after the player with "theSeed" has placed on
	       (currentRow, currentCol). */
	   public static void updateGame(int theSeed, int currentRow, int currentCol) {
	      if (hasWon(theSeed, currentRow, currentCol)) {  // check if winning move
	         currentState = (theSeed == CROSS) ? CROSS_WON : NOUGHT_WON;
	      } else if (isDraw()) {  // check for draw
	         currentState = DRAW;
	      }
	      // Otherwise, no change to currentState (still PLAYING).
	   }
	 
	   /** Return true if it is a draw (no more empty cell) */
	   // TODO: Shall declare draw if no player can "possibly" win
	   public static boolean isDraw() {
	      for (int row = 0; row < ROWS; ++row) {
	         for (int col = 0; col < ROWS; ++col) {
	            if (board[row][col] == EMPTY) {
	               return false;  // an empty cell found, not draw, exit
	            }
	         }
	      }
	      return true;  // no empty cell, it's a draw
	   }
	 
	   /** Return true if the player with "theSeed" has won after placing at
	       (currentRow, currentCol) */
	   public static boolean hasWon(int theSeed, int currentRow, int currentCol) {
		   
		   return columnWon(theSeed, currentRow, currentCol) || rowWon(theSeed, currentRow, currentCol) || diagonalWon(theSeed, currentRow, currentCol)||oppositeDiagonalWon(theSeed, currentRow, currentCol);
		   
		   
	   
	   }
	   
	   public static boolean columnWon(int theSeed, int currentRow, int currentCol){
		   int count =0;
		   boolean status = true;
		   for( int rowNum = 0; rowNum <ROWS; ++rowNum ){
			   status = (board[rowNum][currentCol] == theSeed) && status;
			   if(status){
				   ++count;
				   if(count == winCriteria){
					   return status;
				   }
			   }
			   else{
				   count =0;
			   }

		   }
		   
		   return status;
	   }
	   
	   public static boolean rowWon(int theSeed, int currentRow, int currentCol){
		   int count=0;
		   boolean status = true;
		   for( int colNum = 0; colNum <ROWS; ++colNum ){
			   status = (board[currentRow][colNum] == theSeed) && status;
			   if(status){
				   ++count;
				   if(count == winCriteria){
					   return status;
				   }
			   }
			   else{
				   count =0;
			   }

		   }
		   
		   return status;
	   }
	   
	   public static boolean diagonalWon(int theSeed, int currentRow, int currentCol){
		   int count =0;
		   boolean status = true;
		   for( int colNum = 0; colNum <ROWS; ++colNum ){
			   status = (board[colNum][colNum] == theSeed) && status;
			   if(status){
				   ++count;
				   if(count == winCriteria){
					   return status;
				   }
			   }
			   else{
				   count =0;
			   }

		   }
		   
		   return status;
	   }
	   
	   public static boolean oppositeDiagonalWon(int theSeed, int currentRow, int currentCol){
		   boolean status = true;
		   int count = 0;
		   for( int colNum = 0; colNum <ROWS; ++colNum ){
			   status = (board[colNum][ROWS-colNum-1] == theSeed) && status;
			   if(status){
				   ++count;
				   if(count == winCriteria){
					   return status;
				   }
			   }
			   else{
				   count =0;
			   }

		   }
		   
		   return status;
	   }
	 
	   /** Print the game board */
	   public static void printBoard() {
	      for (int row = 0; row < ROWS; ++row) {
	         for (int col = 0; col < ROWS; ++col) {
	            printCell(board[row][col]); // print each of the cells
	            if (col != ROWS - 1) {
	               System.out.print("|");   // print vertical partition
	            }
	         }
	         System.out.println();
	         if (row != ROWS - 1) {
	        	 for(int row1 = 0; row1 < ROWS; ++row1){
	            System.out.print("----"); // print horizontal partition
	        	 }
	        	 System.out.println();
	         }
	      }
	      System.out.println("The win criteria to win the game is "+winCriteria+" in a row");
	   }
	   
	   //Method to load saved game
	   public static void loadGame(){

	        // This will reference one cell at a time
	        String line = null;
	        ArrayList<String> ar = new ArrayList<String>();

	        try {
	            // FileReader reads saved game files in the default encoding.
	            FileReader fileReader = new FileReader(savedGame);

	            // Always wrap FileReader in BufferedReader.
	            BufferedReader bufferedReader = new BufferedReader(fileReader);

	            while((line = bufferedReader.readLine()) != null) {
	            	ar.add(line);
	            }   
	            // Always close files.
	            bufferedReader.close(); 
	            
	        }
	        catch(FileNotFoundException ex) {System.out.println("Sorry! you do not have any saved game");                
	        }
	        catch(IOException ex) {
	        	System.out.println("Error loading saved game");
	        }
	        
	        for (int row = 0; row < ROWS; ++row) {
		         for (int col = 0; col < ROWS; ++col) {
		            board[row][col] =Integer.parseInt( ar.get(0));
		            ar.remove(0); //remove the list at index 0 for FIFO
		         }
	        }
	        currentPlayer =Integer.parseInt(ar.get(0));
	        currentPlayer = (currentPlayer == CROSS) ? NOUGHT : CROSS; 
	    }
	   
	   /** Method to save the game**/
	   public static void saveGame(){
		   
			   try {
				   PrintStream ps = new PrintStream(new FileOutputStream(savedGame));
				   for(int row=0;row < board.length;row++){
				   for(int col=0; col < board[row].length;col++){
				   int s =  board[row][col];
				   ps.println(s);
				   }
				   }
				   ps.println(currentPlayer);
				   ps.close();
				   } catch (FileNotFoundException e) {
				   System.out.println(e.getMessage());
				   }
			   System.out.println("Game saved");
		}
		   
	   /** Print a cell with the specified "content" */
	   public static void printCell(int content) {
	      switch (content) {
	         case EMPTY:  System.out.print("   "); break;
	         case NOUGHT: System.out.print(" O "); break;
	         case CROSS:  System.out.print(" X "); break;
	      }
	   }
	

}


