import java.util.ArrayList;

/**
 * Board
 *
 * A class that represents the game board.
 * Handles the game logic and the board state (discs).
 * Class taken and adjusted from the labs.
 **/
public class Board {
    public static final int W = -1;
    public static final int B = 1;
    public static final int E = 0;
    
    private int whiteDiscs;
    private int blackDiscs;
    
    private int[][] gameBoard;
    private int lastPlayer;
    private Move lastMove;
    
    public Board() {
        this.gameBoard = new int[8][8];
        this.lastMove = new Move();
        this.lastPlayer = W;
        this.whiteDiscs = 2;
        this.blackDiscs = 2;
        
        this.gameBoard[3][3] = W;
        this.gameBoard[4][4] = W;
        this.gameBoard[3][4] = B;
        this.gameBoard[4][3] = B;
    }
    
    // Copy Constructor
    public Board(Board board) {
        this.gameBoard = new int[8][8];
        this.lastMove = new Move(board.getLastMove().getRow(), board.getLastMove().getCol());
        this.lastPlayer = board.lastPlayer;
        this.whiteDiscs = board.whiteDiscs;
        this.blackDiscs = board.blackDiscs;
        
        for (int i = 0; i < this.gameBoard.length; i++)
            System.arraycopy(board.gameBoard[i], 0, this.gameBoard[i], 0, this.gameBoard.length);
    }
    
    
    /* Move Making */
    
    /**
     * Place Disk
     * Places a disk of the given color on the board.
     * Move validity to be checked before calling this method.
     * @param row, col, color
     **/
    public void placeDisk(int row, int col, int color) {
        this.gameBoard[row][col] = color;
        this.lastMove = new Move(row, col);
        this.lastPlayer = color;
        
        if (color == B) this.blackDiscs++;
        else this.whiteDiscs++;
        
        flipDisks(row, col, color);
    }
    
    /**
     * flipDisks
     * Flips the disks in the given direction.
     *
     * @param srcRow, srcCol, color
     **/
    private void flipDisks(int srcRow, int srcCol, int color) {
        int opponentColor = color * -1;
        int rowOffset;
        int colOffset;
        int row;
        int col;
        int captured;
        
        // Check all 8 directions for opponent discs.
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                rowOffset = i;
                colOffset = j;
                row = srcRow + rowOffset;
                col = srcCol + colOffset;
                captured = 0;
                
                if(!inBounds(row, col))
                    continue;
                
                // Check if the next disc is an opponent disc.
                if (this.gameBoard[row][col] == opponentColor) {
                    row += rowOffset;
                    col += colOffset;
                    captured++;
                    
                    // While the next disc is an opponent disc, keep going.
                    while (inBounds(row, col) && this.gameBoard[row][col] == opponentColor) {
                        row += rowOffset;
                        col += colOffset;
                        captured++;
                    }
                    
                    // Check if the neighbour is an empty space.
                    if (inBounds(row, col) && this.gameBoard[row][col] == E)
                        continue;
                    
                    // When you reach a friendly disc, flip all the captured discs.
                    if (inBounds(row, col) && this.gameBoard[row][col] == color) {
                        while (captured > 0) {
                            row -= rowOffset;
                            col -= colOffset;
                            this.gameBoard[row][col] = color;
                            if (color == B) {
                                this.blackDiscs++;
                                this.whiteDiscs--;
                            } else {
                                this.whiteDiscs++;
                                this.blackDiscs--;
                            }
                            captured--;
                        }
                    }
                }
            }
        }
    }
    
    
    /* Control */
    
    /**
     * isValidMove
     * Checks if the given move is valid.
     *
     * @param row, col, color
     **/
    public boolean isValidMove(int row, int col, int color) {
        int column;
        int opponentColor = color * -1;
        
        if(!inBounds(row, col)) return false;
        
        // Check if a disk already exists at this point on the board
        if(this.gameBoard[row][col]!=0) return false;
        
        // Check if a neighbour disc is an opponent disc.
        for(int x = Math.max(0, row - 1); x <= Math.min(row + 1, 7); x++)
        {
            for(int y = Math.max(0, col - 1); y <= Math.min(col + 1, 7); y++)
            {
                if(x != row || y != col)
                {
                    if(this.gameBoard[x][y] == opponentColor)
                    {
                        // This neighbour disc (in row x and column y) is an opponent disc
                        // Run in the direction where the neighbour is
                        // If you reach an empty space, ignore this neighbour and look for the next one
                        // If you reach a friendly disk then the given Move is Valid so return true
                        
                        if(x < row)
                        {
                            if(y < col)
                            {
                                // Run diagonally up and left until you reach a friendly disk
                                column = col;
                                for(int i = row - 1; i >= 0; i--)
                                {
                                    column--;
                                    if(this.gameBoard[i][column] == E) break;
                                    if(this.gameBoard[i][column] == color) return true;
                                    if(column==0) break;
                                }
                            }
                            if(y > col)
                            {
                                // Run diagonally up and right until you reach a friendly disk
                                column = col;
                                for(int i = row - 1; i >= 0; i--)
                                {
                                    column++;
                                    if(this.gameBoard[i][column] == E) break;
                                    if(this.gameBoard[i][column] == color) return true;
                                    if(column==7) break;
                                }
                            }
                            if(y == col)
                            {
                                // Run up until you reach a friendly disk
                                for(int i = row - 1; i >= 0; i--)
                                {
                                    if(this.gameBoard[i][col] == E) break;
                                    if(this.gameBoard[i][col] == color) return true;
                                }
                            }
                        }
                        if(x == row)
                        {
                            if(y < col)
                            {
                                // Run to the left until you reach a friendly disk
                                for(int i= col - 1;i >= 0; i--)
                                {
                                    if(this.gameBoard[row][i] == E) break;
                                    if(this.gameBoard[row][i] == color) return true;
                                }
                            }
                            if(y > col)
                            {
                                // Run right to the right until you reach a friendly disk
                                for(int i =col + 1; i <= 7;i++)
                                {
                                    if(this.gameBoard[row][i] == E) break;
                                    if(this.gameBoard[row][i] == color) return true;
                                }
                            }
                        }
                        if(x > row)
                        {
                            if(y < col)
                            {
                                // Run diagonally down and left until you reach a friendly disk
                                column=col;
                                for(int i=row+1;i<=7;i++)
                                {
                                    column--;
                                    if(this.gameBoard[i][column]==E) break;
                                    if(this.gameBoard[i][column]==color) return true;
                                    if(column==0) break;
                                }
                            }
                            if(y == col)
                            {
                                // Run down until you reach a friendly disk
                                for(int i= row + 1; i <=7; i++)
                                {
                                    if(this.gameBoard[i][col]==E) break;
                                    if(this.gameBoard[i][col]==color) return true;
                                }
                            }
                            if(y > col)
                            {
                                // Run diagonally down and right until you reach a friendly disk
                                column = col;
                                for(int i = row + 1; i <= 7; i++)
                                {
                                    column++;
                                    if(this.gameBoard[i][column] == E) break;
                                    if(this.gameBoard[i][column] == color) return true;
                                    if(column==7) break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * canPlay
     * Iterates through the game board and checks if there's at least one valid move for the given color.
     * @return true if there's at least one valid move, false otherwise.
     **/
    public boolean canPlay(int color) {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (isValidMove(i, j, color))
                    return true;
        
        return false;
    }
    
    public boolean inBounds(int row, int col) {
        return row >= 0 && row < gameBoard.length && col >= 0 && col < gameBoard.length;
    }
    
    /**
     * Evaluate
     * Positive if black is winning, negative if white is winning, 0 when it's a draw.
     * @return int evaluation of the current board
     **/
    public int evaluate() {
        int score = 0;
    
        boolean blackCanPlay = canPlay(B);
        boolean whiteCanPlay = canPlay(W);
    
        if (!blackCanPlay && !whiteCanPlay)
            if (blackDiscs > whiteDiscs)
                return 10000;
            else if (whiteDiscs > blackDiscs)
                return -10000;
            else
                return 0;
    
        if (!whiteCanPlay) score += 1000;
        if (!blackCanPlay) score -= 1000;
    
        //checking corners
        score += 50*(this.gameBoard[0][0] + this.gameBoard[0][7] + this.gameBoard[7][0] + this.gameBoard[7][7]);
    
        //check x squares
        score -= 20*(this.gameBoard[0][1] + this.gameBoard[0][6] + this.gameBoard[1][0] + this.gameBoard[1][7] + this.gameBoard[6][0] + this.gameBoard[6][7] + this.gameBoard[7][1] + this.gameBoard[7][6]);
    
        //check c squares
        score -= 20*(this.gameBoard[0][2] + this.gameBoard[0][5] + this.gameBoard[2][0] + this.gameBoard[2][7] + this.gameBoard[5][0] + this.gameBoard[5][7] + this.gameBoard[7][2] + this.gameBoard[7][5]);
    
        //checking edges
        for(int i = 1; i < this.gameBoard.length - 1; i++)
            score += 3*(this.gameBoard[i][0] + this.gameBoard[i][7] + this.gameBoard[0][i] + this.gameBoard[7][i]);
        
        if(this.whiteDiscs > this.blackDiscs * 1.5)
            score -= 25;
    
        if(this.blackDiscs > this.whiteDiscs * 1.5)
            score += 25;
    
        return score;
    }
    
    public boolean isFull() {
        return this.blackDiscs + this.whiteDiscs >= (gameBoard.length * gameBoard.length);
    }
    
    
    /* Getters */
    
    /**
     * GetChildren
     * Calculates all possible moves for the given color.
     * @param color
     * @return ArrayList<Board> of all the children of the current board
     **/
    public ArrayList<Board> getChildren(int color) {
        ArrayList<Board> children = new ArrayList<>();
        for(int row = 0; row < this.gameBoard.length; row++)
        {
            for(int col = 0; col < this.gameBoard.length; col++)
            {
                if(this.isValidMove(row, col, color))
                {
                    Board child = new Board(this);
                    child.placeDisk(row, col, color);
                    children.add(child);
                }
            }
        }
        return children;
    }
    
    public Move getLastMove() {
        return this.lastMove;
    }
    
    public int getLastPlayer() {
        return this.lastPlayer;
    }
    
    public int[][] getGameBoard() {
        return this.gameBoard;
    }
    
    
    /* Setters */
    
    public void setLastMove(Move lastMove) {
        this.lastMove.setRow(lastMove.getRow());
        this.lastMove.setCol(lastMove.getCol());
        this.lastMove.setValue(lastMove.getValue());
    }
    
    public void setLastPlayer(int lastPlayer) {
        this.lastPlayer = lastPlayer;
    }
    
    
    /* Printers */
    
    /**
     * print
     * Prints the board to the console.
     * @return void
     **/
    public void print() {
        System.out.println("    1 2 3 4 5 6 7 8");
        System.out.println("    _ _ _ _ _ _ _ _");
        for (int i = 0; i < 8; i++) {
            System.out.print(i + 1 + " | ");
            for (int j = 0; j < 8; j++) {
                if (this.gameBoard[i][j] == 1)
                    System.out.print("B ");
                else if (this.gameBoard[i][j] == -1)
                    System.out.print("W ");
                else
                    System.out.print(". ");
            }
            System.out.println();
        }
    }
    
    public void printScore() {
        System.out.println("The score is:\nBlack: " + this.blackDiscs + "\nWhite: " + this.whiteDiscs);
    }
    
    public void printWinner() {
        if (this.blackDiscs > this.whiteDiscs)
            System.out.println("\nBlack wins!\n");
        else if (this.blackDiscs < this.whiteDiscs)
            System.out.println("\nWhite wins!\n");
        else
            System.out.println("\nDraw!\n");
    }
}