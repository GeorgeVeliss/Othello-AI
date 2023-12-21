import java.util.Scanner;

import static java.lang.Thread.sleep;

/**
 * Othello - MiniMax
 *
 * An Othello game that uses the MiniMax algorithm with a-b pruning and allows for Human vs AI.
 *
 * @authors: P3200005, P3210255, P3200262
 * @info: Made for the course of Artificial Intelligence @ AUEB 2022-2023
 **/
public class Main
{
    public static void main(String[] args){
        intro();                         // Intro Message
        int gameMode = getGameMode();    // Get the desired mode of the game
        int depth = 0;
        Board board = new Board();
        PlayerAB black, white;
        
        if(gameMode == 1){
            depth = getDepth();          // Get the desired depth of the tree
            boolean playFirst = playFirst();
            black = new PlayerAB(depth, Board.B, !playFirst);
            white = new PlayerAB(depth, Board.W, playFirst);
        } else if (gameMode == 2){
            depth = getDepth();          // Get the desired depth of the tree
            black = new PlayerAB(depth, Board.B, true);
            white = new PlayerAB(depth, Board.W, true);
        } else {
            black = new PlayerAB(depth, Board.B, false);
            white = new PlayerAB(depth, Board.W, false);
        }
        
        boolean canPlayW, canPlayB;
        canPlayB = canPlayW = true;
        
        System.out.println("\nThe game has started! This is the starting board:\n");
        board.print();
        
        while(!board.isFull() && (canPlayW || canPlayB)) {
            switch (board.getLastPlayer()) {
                case Board.W -> {
                    if (canPlayB) {
                        System.out.println("\nBlack's Turn");

                        Move move = black.move(board);
    
                        try {
                            if(black.isAI() && depth <= 9)
                                sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        
                        System.out.println("Black's move: " + (move.getRow() + 1) + " " + (move.getCol() + 1));
                        board.placeDisk(move.getRow(), move.getCol(), Board.B);
                    } else {
                        System.out.println("\nBlack cannot play! Skipping turn...");
                    }
                    
                    canPlayW = board.canPlay(Board.W);
                    board.setLastPlayer(Board.B);
                }
                case Board.B -> {
                    if (canPlayW) {
                        System.out.println("\nWhite's Turn");

                        Move move = white.move(board);
                        
                        try {
                            if(white.isAI() && depth <= 9)
                                sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        
                        System.out.println("White's move: " + (move.getRow() + 1) + " " + (move.getCol() + 1));
                        board.placeDisk(move.getRow(), move.getCol(), Board.W);
                    } else {
                        System.out.println("\nWhite cannot play! Skipping turn...");
                    }
                    
                    board.setLastPlayer(Board.W);
                    canPlayB = board.canPlay(Board.B);
                }
            }
            board.print();
        }
        board.printWinner();
        board.printScore();

    }


    
    public static void intro() {
        System.out.println("Welcome to Othello!");
        System.out.println("The game is played on an 8x8 board. You will be matched against the computer.");
        System.out.println("The player with the black disks goes first and the player with the white disks goes second.");
    }
    
    public static int getGameMode() {
        Scanner scanner = new Scanner(System.in);
        int gameMode;
        
        System.out.println("\nChoose the game mode:");
        System.out.println("1. Human vs AI");
        System.out.println("2. AI vs AI");
        System.out.println("3. Human vs Human");
        
        do {
            System.out.println("Give game mode: ");
            try{
                gameMode = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                gameMode = 0;
                System.out.println("Invalid input! Please try again.");
            }
        } while (gameMode != 1 && gameMode != 2 && gameMode != 3);
        
        return gameMode;
    }
    
    public static boolean playFirst(){
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\nDo you want to play first with the black disks? (y/n)");
        String answer = scanner.nextLine();
        
        while(!answer.equals("y") && !answer.equals("n")) {
            System.out.println("Invalid answer. Please try again.");
            System.out.println("Do you want to play first with the black disks? (y/n)");
            answer = scanner.nextLine();
        }
        
        return answer.equals("y");
    }
    
    public static int getDepth() {
        Scanner scanner = new Scanner(System.in);
        int depth;
    
        do {
            System.out.println("\nPlease pick the maximum depth for the AI.\nRecommendations:\n(2) Easy\n(6) Medium\n(10) Hard");
            try{
                depth = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                depth = 0;
            }
        } while (depth < 1);
        System.out.println("\nYou have chosen a depth of " + depth + ". Best of luck!");
        
        return depth;
    }
}
