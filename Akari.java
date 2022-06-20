/**
 * Akari represents a single puzzle of the game Akari.
 *
 * @author Lyndon While 
 * @version 2021
 * 
 * Khanh Hua Quang - 22928469
 */
import java.util.ArrayList;
import java.awt.event.*; 
import java.awt.*;

public class Akari
{
    private String filename; // the name of the puzzle file
    private int size;        // the board is size x size
    private Space[][] board; // the board is a square grid of spaces of various types

    /**
     * Constructor for objects of class Akari. 
     * Creates and initialises the fields with the contents of filename. 
     * The layout of a puzzle file is described on the LMS page; 
     * you may assume the layout is always correct. 
     */
    public Akari(String filename)
    {
        // TODO 3
        String[] subFile;
        FileIO file = new FileIO(filename);
        this.filename = file.getName();
        this.size = Integer.valueOf(file.getLines().get(0));
        this.board = new Space[size][size];
        int x, y;
        
        for (int line = 1; line <= 6; line++)
        {    
            subFile = file.getLines().get(line).split(" ");
            for (int i = 0; i < subFile.length; i++)
                for (int j = 1; j < subFile[i].length(); j++)
                {
                    x = Character.getNumericValue(subFile[i].charAt(0));
                    y = Character.getNumericValue(subFile[i].charAt(j));
                    if (line == 1) this.board[x][y] = Space.BLACK;
                    if (line == 2) this.board[x][y] = Space.ZERO;
                    if (line == 3) this.board[x][y] = Space.ONE;
                    if (line == 4) this.board[x][y] = Space.TWO;
                    if (line == 5) this.board[x][y] = Space.THREE;
                    if (line == 6) this.board[x][y] = Space.FOUR;
                }
        }
        
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (board[i][j] == null)
                    this.board[i][j] = Space.EMPTY;
    }
    
    /**
     * Uses the example file from the LMS page.
     */
    public Akari()
    {
        this("Puzzles/p7-e1.txt");
    }
    
    /**
     * Returns the name of the puzzle file.
     */
    public String getFilename()
    {
        // TODO 1a
        return filename;
    }
    
    /**
     * Returns the size of the puzzle.
     */
    public int getSize()
    {
        // TODO 1b
        return size;
    }
    
    /**
     * Returns true iff k is a legal index into the board. 
     */
    public boolean isLegal(int k)
    {
        // TODO 5
        return k >= 0 && k <= size - 1;
    }
    
    /**
     * Returns true iff r and c are both legal indices into the board. 
     */
    public boolean isLegal(int r, int c)
    {
        // TODO 6
        return isLegal(r) && isLegal(c); 
    }
    
    /**
     * Returns the contents of the square at r,c if the indices are legal, 
     * o/w throws an illegal argument exception. 
     */
    public Space getBoard(int r, int c)
    { 
        // TODO 7
        if (isLegal(r, c)) return board[r][c];
        throw new IllegalArgumentException("Indice are not legal");
    }
    
    /**
     * Returns the int equivalent of x. 
     * If x is a digit, returns 0 -> 0, 1 -> 1, etc; 
     * if x is an upper-case letter, returns A -> 10, B -> 11, etc; 
     * o/w throws an illegal argument exception. 
     */
    public static int parseIndex(char x)
    {
        // TODO 2
        if (Character.isDigit(x)) return x - 48;
        else if (Character.isUpperCase(x)) return x - 55;
        else throw new IllegalArgumentException("Please input number or uppercase letter");
    }
    
    /**
     * Performs a left click on the square at r,c if the indices are legal, o/w does nothing. 
     * If r,c is empty, a bulb is placed; if it has a bulb, that bulb is removed.
     */
    public void leftClick(int r, int c)
    {
        // TODO 8
        if (isLegal(r, c) && Space.isMutable(board[r][c]))
            if (board[r][c] == Space.EMPTY)
                board[r][c] = Space.BULB;
            else board[r][c] = Space.EMPTY;
    }
    
    /**
     * Sets all mutable squares on the board to empty.
     */
    public void clear()
    {
        // TODO 4
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (Space.isMutable(board[i][j]))
                    board[i][j] = Space.EMPTY;
    }
    
    /**
     * Returns the number of bulbs adjacent to the square at r,c. 
     * Throws an illegal argument exception if r,c are illegal coordinates.
     */
    public int numberOfBulbs(int r, int c)
    {
        // TODO 14
        if (isLegal(r, c))
        {
            int count = 0;
            if (r != 0 && board[r - 1][c] == Space.BULB) count ++; 
            if (r != 6 && board[r + 1][c] == Space.BULB)  count ++;
            if (c != 0 && board[r][c - 1] == Space.BULB)  count ++;
            if (c != 6 && board[r][c + 1] == Space.BULB)  count ++;
            return count;
        }
        else throw new IllegalArgumentException("r,c are illegal coordinates");
    }
    
    /**
     * Returns true iff the square at r,c is lit by a bulb elsewhere. 
     * Throws an illegal argument exception if r,c are illegal coordinates.
     */
    public boolean canSeeBulb(int r, int c)
    {
        // TODO 15
        if (isLegal(r, c))
        {
            for (int i = r - 1; i >= 0; i--)
            {
                if (!Space.isMutable(board[i][c]))
                    break;
                if (board[i][c] == Space.BULB)
                    return true;
            }
            for (int i = r + 1; i < size; i++)
            {
                if (!Space.isMutable(board[i][c]))
                    break;
                if (board[i][c] == Space.BULB)
                    return true;
            }
            for (int j = c - 1; j >= 0; j--)
            {
                if (!Space.isMutable(board[r][j]))
                    break;
                if (board[r][j] == Space.BULB)
                    return true;
            }
            for (int j = c + 1; j < size; j++)
            {
                if (!Space.isMutable(board[r][j]))
                    break;
                if (board[r][j] == Space.BULB)
                    return true;
            }
            return false;
        }
        else throw new IllegalArgumentException("r, c are illegal coordinates");
    }
    
    /**
     * Returns an assessment of the state of the puzzle, either 
     * "Clashing bulb at r,c", 
     * "Unlit square at r,c", 
     * "Broken number at r,c", or
     * three ticks, as on the LMS page. 
     * r,c must be the coordinates of a square that has that kind of error. 
     * If there are multiple errors on the board, you may return any one of them. 
     */
    public String isSolution()
    {
        // TODO 16
        //Broken number
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
            {
                if (board[i][j] == Space.ZERO)
                {
                    if (i != 0 && board[i - 1][j] == Space.BULB) return "Broken number at " + i + "," + j;
                    if (i != 6 && board[i + 1][j] == Space.BULB)  return "Broken number at " + i + "," + j;
                    if (j != 0 && board[i][j - 1] == Space.BULB)  return "Broken number at " + i + "," + j;
                    if (j != 6 && board[i][j + 1] == Space.BULB)  return "Broken number at " + i + "," + j;
                }
                for (int k = 1; k <= 4; k++)
                {
                    int countBulb = 0;
                    Space temp = Space.ONE;
                    if (k == 1) temp = Space.ONE;
                    if (k == 2) temp = Space.TWO;
                    if (k == 3) temp = Space.THREE;
                    if (k == 4) temp = Space.FOUR;
                    if (board[i][j] == temp)
                    {
                        if (i != 0 && board[i - 1][j] == Space.BULB) countBulb ++;
                        if (i != 6 && board[i + 1][j] == Space.BULB)  countBulb ++;
                        if (j != 0 && board[i][j - 1] == Space.BULB)  countBulb ++;
                        if (j != 6 && board[i][j + 1] == Space.BULB)  countBulb ++;
                        if(countBulb != k) return "Broken number at " + i + "," + j;
                    }
                }
            }
        //Clashing bulb
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (board[i][j] == Space.BULB && canSeeBulb(i, j))
                    return "Clashing bulb at " + i + "," + j;
        //Unlit square
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (board[i][j] != Space.BULB && !canSeeBulb(i, j) && Space.isMutable(board[i][j]))
                    return "Unlit square at " + i + "," + j;   
                    
        // Solved
        return "\u2713\u2713\u2713";
    }
}