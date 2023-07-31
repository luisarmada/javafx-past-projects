package game;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiceGame {

    static FileManager fm = new FileManager();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        displayMenu();
    }

    public static void registerNew() {
        clearOutput();
        System.out.print("Enter your desired username: ");
        String userName = scanner.nextLine();

        if (fm.readFile("Users.txt").contains("User: " + userName)) {
            System.out.println("That user already exists!");
            displayMenu();
        } else if (userName.length() <= 3) {
            waitingText(" ! Username is too short ( <4 ), try again", 700);
            registerNew();
        } else {
            fm.writeFile("Users.txt", "User: " + userName);
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            if (password.length() <= 3) {
                waitingText(" ! Password is too short ( <4 ), try again", 700);
                registerNew();
            }

            fm.writeFile("Users.txt", "Pass: " + password);
            waitingText("Registering", 600);
            clearOutput();
            System.out.println("Successfully registered!");
            waitingText("Returning to the menu", 800);
            displayMenu();
        }
    }

    public static String login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        if (fm.readFile("Users.txt").contains("User: " + username)) {
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            if (fm.readFile("Users.txt").contains("User: " + username + "\n" + "Pass: " + password)) {
                return username;
            } else {
                waitingText(" ! Unauthorised User, returning to menu", 600);
                displayMenu();
            }
        } else {
            waitingText(" ! Unauthorised User, returning to menu", 600);
            displayMenu();
        }
        return null;
    }

    public static void playGame() {
        clearOutput();
        System.out.println(" - Player 1: ");
        String playerOne = login();
        waitingText("Successfully logged in as " + playerOne + "!", 500);
        clearOutput();
        System.out.println(" - Player 2: ");
        String playerTwo = login();
        waitingText("Successfully logged in as " + playerTwo + "!", 500);
        clearOutput();
        waitingText("Starting Game", 500);

        int playerOneScore = 0;
        int playerTwoScore = 0;

        for (int i = 1; i <= 5; i++) {
            playerOneScore = playGameReturnScore(playerOne, playerTwo, playerOneScore, playerTwoScore, " * Player 2 Turn Loading", i);
            if(i < 5){
                playerTwoScore = playGameReturnScore(playerTwo, playerOne, playerTwoScore, playerOneScore, " * Starting Next Round", i);
            } else {
                playerTwoScore = playGameReturnScore(playerTwo, playerOne, playerTwoScore, playerOneScore, " * Calculating Scores", i);
            }
            
        }
        
        waitingText("The winner is", 700);
        if(playerOneScore > playerTwoScore){
            // PLAYER ONE WINS
            System.out.println(" ! " + playerOne + " ! with a score of " + playerOneScore + " to " + playerTwo + "'s score of " + playerTwoScore);
        } else if(playerTwoScore > playerTwoScore){
            // PLAYER TWO WINS
            System.out.println(" ! " + playerTwo + " ! with a score of " + playerTwoScore + " to " + playerOne + "'s score of " + playerOneScore);
        } else {
            // DRAW
            System.out.println(" ! Nobody, it's a draw with a score of " + playerOneScore + " for both players!");
            waitingText(" * Sudden Death starting", 600);
            
            
            
        }
        
        
       
    }

    public static int playGameReturnScore(String currentPlayer, String opponentPlayer, int currentScore, int opponentScore, String nextGameText, int roundNumber) {
        clearOutput();
        System.out.println(" - Round " + roundNumber + " - ");
        waitingText(currentPlayer + " is rolling", 500);
        int rollDice1 = rollDice();
        int rollDice2 = rollDice();
        int total = rollDice1 + rollDice2;
        System.out.println(" - Rolled a " + rollDice1 + " and a " + rollDice2 + "!");
        if (total % 2 == 0) {
            waitingText(" ! Total is even so 10 points are added", 500);
            total += 10;
        } else {
            waitingText(" ! Total is odd so 5 points are subtracted", 500);
            total -= 5;
            if (total < 0) {
                total = 0;
            }
        }
        if (rollDice1 == rollDice2) {
            waitingText(" ! Rolled a double so rerolling another die", 500);
            int rollDice3 = rollDice();
            System.out.println(" - Rolled a " + rollDice3);
            total += rollDice3;
            System.out.println(" ! Total is now " + total);
            if (total % 2 == 0) {
                waitingText(" ! Total is even so 10 points are added", 500);
                total += 10;
            } else {
                waitingText(" ! Total is odd so 5 points are subtracted", 500);
                total -= 5;
                if (total < 0) {
                    total = 0;
                }
            }
        }

        System.out.println("                  ");
        System.out.println(" ---------------- ");
        System.out.println(" TOTAL = " + total);
        currentScore += total;
        System.out.println(currentPlayer + "'s SCORE = " + currentScore);
        System.out.println(opponentPlayer + "'s SCORE = " + opponentScore);
        waitingText(nextGameText, 2000);
        clearOutput();

        return currentScore;
    }

    public static int rollDice() {
        return (int) (Math.random() * ((6 - 1) + 1)) + 1;
    }

    public static void showLeaderboard() {
        clearOutput();
        System.out.println(" - LEADERBOARD - ");
        for (int i = 0; i < 5; i++) {
            System.out.println(" " + (i + 1) + ". Player - 500pts ");
        }
        System.out.println(" Press enter to return to menu: ");
        scanner.nextLine();
        displayMenu();
    }

    public static void displayMenu() {
        clearOutput();
        System.out.println("----- DICE GAME -----");
        System.out.println("- 1. Play ");
        System.out.println("- 2. Register ");
        System.out.println("- 3. Leaderboard ");
        System.out.print(" > ");
        String input = scanner.nextLine();
        switch (input) {
            case "1":
                waitingText("Loading", 500);
                playGame();
                break;
            case "2":
                waitingText("Loading", 500);
                registerNew();
                break;
            case "3":
                showLeaderboard();
                break;
            default:
                waitingText(" ! Invalid Option - Try Again", 750);
                displayMenu();
                break;
        }
    }

    static void waitingText(String text, int delay) {
        System.out.print(text);
        waitDelay(delay);
        System.out.print('\r');
        System.out.print(text + ".");
        waitDelay(delay);

        System.out.print('\r');
        System.out.print(text + "..");
        waitDelay(delay);
        System.out.print('\r');
        System.out.print(text + "...");
        waitDelay(delay);
        System.out.println("");
    }

    static void waitDelay(int delay) {
        try {
            TimeUnit.MILLISECONDS.sleep(delay);
        } catch (InterruptedException ex) {
            Logger.getLogger(DiceGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void clearOutput() {
        for (int i = 0; i < 100; i++) {
            System.out.println("");
        }
    }

}
