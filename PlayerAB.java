import java.util.ArrayList;
import java.util.Scanner;

/**
 * PlayerAB Class
 *
 * Allows for the player or AI to make a move.
 * Takes input from the human player and checks its validity.
 * Calculates the best move for the AI using the minimax algorithm with a-b pruning.
 * Class taken and adjusted from the labs.
 **/
public class PlayerAB {
    private final int color;
    private final boolean isAI;
    protected final int maxDepth;
    
    public PlayerAB(int maxDepth, int color, boolean isAI) {
        this.maxDepth = maxDepth;
        this.color = color;
        this.isAI = isAI;
    }
    
    public boolean isAI() {
        return isAI;
    }
    
    public Move move(Board board) {
        if (isAI) {
            return MiniMax(board);
        } else {
            return pickMove(board);
        }
    }
    
    /**
     * MiniMax
     * @param board
     * @return The best move for the current AI player
     **/
    Move MiniMax(Board board){
        if(color == Board.B)
        {
            //If the AI player has the black disks, it wants to maximize the heuristic value
            return max(board, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
        else
        {
            //If the AI player has the white disks, it wants to minimize the heuristic value
            return min(board, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
    }
    
    /**
     * pickMove
     * @param board
     * @return The player's choice of move
     **/
    public Move pickMove(Board board) {
        Scanner scanner = new Scanner(System.in);
        int row = 0, col = 0;
        boolean invalid = true;
        
        // Check if the move is valid
        do {
            try{
                System.out.print("Give row: ");
                row = scanner.nextInt() - 1;
                System.out.print("Give col: ");
                col = scanner.nextInt() - 1;
                
                invalid = !board.isValidMove(row, col, color);
                if(invalid) System.out.println("Invalid move. Please try again.");
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
                scanner.nextLine();
            }
        }while(invalid);
        
        return new Move(row, col);
    }
    
    /**
     * Max
     * @param board, depth, color
     * @return The best move for the max player
     **/
    Move max(Board board, int depth, int alpha, int beta) {
        
        if(board.isFull() || (depth == this.maxDepth))
            return new Move(board.getLastMove().getRow(), board.getLastMove().getCol(), board.evaluate());
        
        if(!board.canPlay(Board.B))
            return min(board, depth + 1, alpha, beta);
        
        ArrayList<Board> children = board.getChildren(Board.B);
        Move bestMove = new Move(Integer.MIN_VALUE);
        for(Board child : children) {
            Move move = min(child, depth + 1, alpha, beta);
            
            if(move.getValue() > bestMove.getValue())
                bestMove.setMove(child.getLastMove().getRow(), child.getLastMove().getCol(), move.getValue());
            
            
            if(bestMove.getValue() >= beta) break;
            
            alpha = Math.max(alpha, bestMove.getValue());
        }
        return bestMove;
    }
    
    /**
     * Min
     * @param board, depth, color
     * @return The best move for the min player
     **/
    Move min(Board board, int depth, int alpha, int beta) {
        
        if(board.isFull() || (depth == this.maxDepth))
            return new Move(board.getLastMove().getRow(), board.getLastMove().getCol(), board.evaluate());
        
        if(!board.canPlay(Board.W))
            return max(board, depth + 1, alpha, beta);
        
        ArrayList<Board> children = board.getChildren(Board.W);
        Move bestMove = new Move(Integer.MAX_VALUE);
        for(Board child : children) {
            Move move = max(child, depth + 1, alpha, beta);
            
            if(move.getValue() < bestMove.getValue())
                bestMove.setMove(child.getLastMove().getRow(), child.getLastMove().getCol(), move.getValue());
            
            if(bestMove.getValue() <= alpha) break;
            
            beta = Math.min(beta, bestMove.getValue());
        }
        return bestMove;
    }
}