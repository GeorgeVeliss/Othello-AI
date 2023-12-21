/**
 * Move Class
 *
 * A class that represents a move in the game.
 * Class taken and adjusted from the labs.
 **/
public class Move
{
    private int row;
    private int col;
    private int value;
    
    public Move() {
        this(-1, -1, 0);
    }
    
    public Move(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }
    
    public Move(int row, int col) {
        this(row, col, -1);
    }

    public Move(int value) {
        this(-1, -1, value);
    }
    
    int getRow() {
        return this.row;
    }

    int getCol() {
        return this.col;
    }

    int getValue() {
        return this.value;
    }

    void setRow(int row) {
        this.row = row;
    }

    void setCol(int col) {
        this.col = col;
    }

    void setValue(int value) {
        this.value = value;
    }
    
    void setMove(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }
}
