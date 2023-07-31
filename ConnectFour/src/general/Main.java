package general;

import java.util.Scanner;

public class Main {

    public static char[][] gameBoard = new char[6][7];
    
    public static char currentPlayer = 'n';
    public static int currentCol, currentRow;

    public static void main(String[] args) {
        
        Scanner myScan = new Scanner(System.in);
        resetBoard();
        drawBoard();

        char checkWin = checkWin();

        while(checkWin() == 'n'){
            System.out.print("Player X Move: ");
            playMove(myScan.nextInt() - 1, 'X');
            drawBoard();
            if(checkWin() != 'n'){
                break;
            }
            System.out.print("Player O Move: ");
            playMove(myScan.nextInt() - 1, 'O'); 
            drawBoard();
        }
        System.out.println("Player " + checkWin() + " wins!!!!");
    }
    
    public static void resetBoard(){
        for(int i=0; i < gameBoard.length; i++){
            for(int j=0; j < gameBoard[0].length; j++){
                gameBoard[i][j] = '-';
            }
        }
    }

    public static void drawBoard(){
        System.out.print("\n");
        for(int i=0; i < gameBoard.length + 1; i++){
            for(int j=0; j < gameBoard[0].length; j++){
                if(i == 0){
                    System.out.print((j+1) + " ");
                } else {
                    System.out.print(gameBoard[i-1][j] + " ");
                }
             }
            System.out.print("\n");
        }
    }
    
    public static void playMove(int col, char symbol){
      currentPlayer = symbol;
        for(int i = gameBoard.length - 1; i >= 0; i-=1){
            if(gameBoard[i][col] == '-'){
                gameBoard[i][col] = symbol;
                currentRow = i;
                currentCol = col;
                return;
            }
        }
    }
    
    public static char checkWin(){

        // Horizontal check
        int counter = 0;
        for(int i = 0; i < gameBoard[0].length; i++){
          if(gameBoard[currentRow][i] == currentPlayer){
            counter++;
            if(counter == 4){
              return currentPlayer;
            }
          } else {
            counter = 0;
          }
        }
        // Vertical check
        counter = 0;
        for(int i = 0; i < gameBoard.length; i++){
          if(gameBoard[i][currentCol] == currentPlayer){
            counter++;
            if(counter == 4){
              return currentPlayer;
            }
          } else {
            counter = 0;
          }
        }

        // Diagonal check
        
        if(diagonalCheck(1, -1)){
            return currentPlayer;
        } else if(diagonalCheck(1, 1)){
            return currentPlayer;
        } else if(diagonalCheck(-1, 1)){
            return currentPlayer;
        } else if(diagonalCheck(-1, -1)){
            return currentPlayer;
        } 

        return 'n';
    }
    
    public static boolean diagonalCheck(int rowMult, int colMult){    
        int counter = 0;
        int diagonalMult = 1;
        for(int i = 0; i < 4; i++){
          
           int checkRow = currentRow + i * rowMult;
           int checkCol = currentCol - i * colMult;
           if(checkRow < 0 || checkRow > 5 || checkCol < 0 || checkCol > 6){
               continue;
           }
            
            
          if(gameBoard[checkRow][checkCol] == currentPlayer){
            counter++;
            if(counter == 4){
              return true;
            }
          } else if(diagonalMult != -1){
              diagonalMult = -1;
          } else {
              break;
          }
        }
        return false;
    }
}
