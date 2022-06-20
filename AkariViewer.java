/**
 * AkariViewer represents an interface for a player of Akari.
 *
 * @author Lyndon While
 * @version 2021
 * 
 * Khanh Hua Quang - 22928469
 */
import java.awt.*;
import java.awt.event.*; 

public class AkariViewer implements MouseListener
{    
    private Akari puzzle;    // the internal representation of the puzzle
    private SimpleCanvas sc; // the display window
    int CELL_SIZE;
    int ADJ;
    
    boolean highlightLastChange;

    /**
     * Constructor for objects of class AkariViewer.
     * Sets all fields and displays the initial puzzle.
     */
    public AkariViewer(Akari puzzle)
    {
        // TODO 10
        this.puzzle = puzzle;
        this.sc = new SimpleCanvas("SimpleCanvas", 400 * puzzle.getSize() / 7, 400 * puzzle.getSize() / 7, Color.white);
        sc.addMouseListener(this);
        if (puzzle.getSize() >= 25)
        {
            this.CELL_SIZE = (255 * puzzle.getSize() / 7 - 50*2) / puzzle.getSize();
            this.ADJ = (255 * puzzle.getSize() / 7 - CELL_SIZE*puzzle.getSize())/2;
        }
        else
        {
            CELL_SIZE = (400 * puzzle.getSize() / 7 - 50*2) / puzzle.getSize();
            ADJ = (400 * puzzle.getSize() / 7 - CELL_SIZE*puzzle.getSize())/2;
        }
        this.highlightLastChange = false;
        sc.setFont(new Font("Times", 20, CELL_SIZE / 10 * 5));
        displayPuzzle();
    }
    
    /**
     * Selects from among the provided files in folder Puzzles. 
     * The number xyz selects pxy-ez.txt. 
     */
    public AkariViewer(int n)
    {
        this(new Akari("Puzzles/p" + n / 10 + "-e" + n % 10 + ".txt"));
    }
    
    /**
     * Uses the provided example file on the LMS page.
     */
    public AkariViewer()
    {
        this(101);
    }
    
    /**
     * Returns the internal state of the puzzle.
     */
    public Akari getPuzzle()
    {
        // TODO 9a
        return puzzle;
    }
    
    /**
     * Returns the canvas.
     */
    public SimpleCanvas getCanvas()
    {
        // TODO 9b
        return sc;
    }
    
    /**
     * Displays the initial puzzle; see the LMS page for a suggested layout. 
     */
    private void displayPuzzle()
    {
        // TODO 11
        sc.drawString("\u2610 Highlight last change?", CELL_SIZE/5*2 + ADJ - 1, -CELL_SIZE + CELL_SIZE/5*3 + ADJ*2/3 + 6, Color.BLACK);
        
        // Set up cell
        for (int i = 0; i < puzzle.getSize(); i++)
            for (int j = 0; j < puzzle.getSize(); j++)
            {
                sc.drawRectangle(i*CELL_SIZE + ADJ, j*CELL_SIZE + ADJ*2/3,
                    (i + 1)*CELL_SIZE + ADJ, (j + 1)*CELL_SIZE + ADJ*2/3, Color.WHITE);
                if (puzzle.getBoard(i, j) != Space.EMPTY)
                    sc.drawRectangle(i*CELL_SIZE + ADJ, j*CELL_SIZE + ADJ*2/3,
                        (i + 1)*CELL_SIZE + ADJ, (j + 1)*CELL_SIZE + ADJ*2/3, Color.BLACK);
                Space temp = Space.ZERO;
                for (int k = 0; k <= 4; k++){
                if (k == 0) temp = Space.ZERO;
                if (k == 1) temp = Space.ONE;
                if (k == 2) temp = Space.TWO;
                if (k == 3) temp = Space.THREE;
                if (k == 4) temp = Space.FOUR;
                
                if (puzzle.getBoard(i, j) == temp)
                    sc.drawString(k, i*CELL_SIZE + CELL_SIZE/5*2 + ADJ - 1, j*CELL_SIZE + CELL_SIZE/5*3 + ADJ*2/3 + 2, Color.WHITE);
                }
            }
            
        // Set up row and column
        for (int i = 0; i <= puzzle.getSize(); i++)
        {
            sc.drawLine(i*CELL_SIZE + ADJ, ADJ*2/3, i*CELL_SIZE + ADJ, puzzle.getSize()*CELL_SIZE + ADJ*2/3, Color.BLACK);
            sc.drawLine(ADJ, i*CELL_SIZE + ADJ*2/3, puzzle.getSize()*CELL_SIZE + ADJ, i*CELL_SIZE + ADJ*2/3, Color.BLACK);
        }
        
        // Display Solve? and Clear button
        sc.drawString("SOLVE?",ADJ, puzzle.getSize()*CELL_SIZE + CELL_SIZE/5*3 + ADJ*2/3, Color.BLACK);
        sc.drawString("CLEAR", (puzzle.getSize()-1)*CELL_SIZE + ADJ - 20,
            puzzle.getSize()*CELL_SIZE + CELL_SIZE/5*3 + ADJ*2/3, Color.BLACK);
    }
    
    /**
     * Performs a left click on the square at r,c if the indices are legal, o/w does nothing. 
     * Updates both puzzle and the display. 
     */
    public void leftClick(int r, int c)
    {
        // TODO 12
        try
        {
            // Change cell color
            puzzle.leftClick(r, c);
            if (puzzle.getBoard(r, c) == Space.BULB)
                sc.drawRectangle(r*CELL_SIZE + ADJ + 1, c*CELL_SIZE + ADJ*2/3 + 1,
                    (r + 1)*CELL_SIZE + ADJ, (c + 1)*CELL_SIZE + ADJ*2/3, Color.YELLOW);
            else if (puzzle.getBoard(r, c) == Space.EMPTY)
                sc.drawRectangle(r*CELL_SIZE + ADJ + 1, c*CELL_SIZE + ADJ*2/3 + 1,
                    (r + 1)*CELL_SIZE + ADJ, (c + 1)*CELL_SIZE + ADJ*2/3, Color.WHITE);
                    
            // Light up cell and add bulb
            for (int i = 0; i < puzzle.getSize(); i++)
                for (int j = 0; j < puzzle.getSize(); j++)
                {
                    if (Space.isMutable(puzzle.getBoard(i, j)) && !puzzle.canSeeBulb(i, j))
                        sc.drawRectangle(i*CELL_SIZE + ADJ + 1, j*CELL_SIZE + ADJ*2/3 + 1,
                            (i + 1)*CELL_SIZE + ADJ, (j + 1)*CELL_SIZE + ADJ*2/3, Color.WHITE);
                    if (Space.isMutable(puzzle.getBoard(i, j)) && ( puzzle.getBoard(i, j) == Space.BULB || puzzle.canSeeBulb(i, j)))
                        sc.drawRectangle(i*CELL_SIZE + ADJ + 1, j*CELL_SIZE + ADJ*2/3 + 1,
                            (i + 1)*CELL_SIZE + ADJ, (j + 1)*CELL_SIZE + ADJ*2/3, Color.YELLOW);
                    if (puzzle.getBoard(i, j) == Space.BULB)
                            sc.drawString("💡", i*CELL_SIZE + CELL_SIZE/5*2 - 5 + ADJ,
                                j*CELL_SIZE + CELL_SIZE/5*3 + ADJ*2/3 + 2, Color.BLUE);
                    if (puzzle.getBoard(i, j) == Space.BULB && puzzle.canSeeBulb(i, j))
                        sc.drawString("💡", i*CELL_SIZE + CELL_SIZE/5*2 - 5 + ADJ,
                            j*CELL_SIZE + CELL_SIZE/5*3 + ADJ*2/3 + 2, Color.RED);
                }
        }
        catch (java.lang.IllegalArgumentException e) {}
    }
    
    /**
     * Highlight the last change in puzzle 
     * Tick the box to enable this feature
     */
    public void highlightLastChange(int r, int c)
    {
        if (puzzle.isLegal(r, c) && Space.isMutable(puzzle.getBoard(r, c)))
        {
            sc.drawLine(r*CELL_SIZE + ADJ + 5, c*CELL_SIZE + ADJ*2/3 + 5,
                r*CELL_SIZE + ADJ + 5, (c+1)*CELL_SIZE + ADJ*2/3 - 5, Color.BLUE);
            sc.drawLine(r*CELL_SIZE + ADJ + 5,c*CELL_SIZE + ADJ*2/3 + 5,
               (r+1)*CELL_SIZE + ADJ - 5, c*CELL_SIZE + ADJ*2/3 + 5, Color.BLUE);
            sc.drawLine((r+1)*CELL_SIZE + ADJ - 5, c*CELL_SIZE + ADJ*2/3 + 5,
                (r+1)*CELL_SIZE + ADJ - 5, (c+1)*CELL_SIZE + ADJ*2/3 - 5, Color.BLUE);
            sc.drawLine(r*CELL_SIZE + ADJ + 5, (c+1)*CELL_SIZE + ADJ*2/3 - 5,
               (r+1)*CELL_SIZE + ADJ - 5, (c+1)*CELL_SIZE + ADJ*2/3 - 5, Color.BLUE);
        }
    }
    
    // TODO 13
    public void mousePressed (MouseEvent e) {}
    public void mouseClicked (MouseEvent e) {}
    public void mouseReleased(MouseEvent e) 
    {
        int r, c;
        // Left Click
        if (puzzle.getSize() >= 25) 
        {
            r = e.getX()/CELL_SIZE - 2;
            c = e.getY()/CELL_SIZE - 1;
        }
        else
        {
            r = e.getX()/CELL_SIZE - 1;
            c = e.getY()/CELL_SIZE - 1;
        }
        leftClick(r, c);  
        
        // Clear Button
        if (( r== puzzle.getSize()-1 || r == puzzle.getSize()-2) && c == puzzle.getSize())
        {
            puzzle.clear();
            sc.drawRectangle(ADJ - 20, (puzzle.getSize()+1)*CELL_SIZE + ADJ*2/3 - 12,
                (puzzle.getSize()-3)*CELL_SIZE + ADJ + 10, (puzzle.getSize()+2)*CELL_SIZE + ADJ*2/3 - 25, Color.WHITE);
            displayPuzzle();
        }
        
        // Solve? button
        if ((r == 0 || r == 1) && c == puzzle.getSize())
        {
            sc.drawRectangle(ADJ - 20, (puzzle.getSize()+1)*CELL_SIZE + ADJ*2/3 - 12,
                (puzzle.getSize()-3)*CELL_SIZE + ADJ + 10, (puzzle.getSize()+2)*CELL_SIZE + ADJ*2/3 - 25, Color.WHITE);
            sc.drawString(puzzle.isSolution(),ADJ*2/3,
                (puzzle.getSize()+1)*CELL_SIZE + CELL_SIZE/5*3 + ADJ*2/3 - 20, Color.BLACK);
        }
        
        // Highlight last change
        if ((r == 0 || r == 1) && c == -1)
            if (highlightLastChange == false)
            {
                highlightLastChange = true;
                sc.drawString("\u2713", CELL_SIZE/5*2 + ADJ - 1, -CELL_SIZE + CELL_SIZE/5*3 + ADJ*2/3 + 6, Color.BLACK);
            }
            else 
            {
                highlightLastChange = false;
                sc.drawString("\u2713", CELL_SIZE/5*2 + ADJ - 1, -CELL_SIZE + CELL_SIZE/5*3 + ADJ*2/3 + 6, Color.WHITE);
                sc.drawString("\u2610", CELL_SIZE/5*2 + ADJ - 1, -CELL_SIZE + CELL_SIZE/5*3 + ADJ*2/3 + 6, Color.BLACK);
            }
        if (highlightLastChange) highlightLastChange(r, c);
    }
    public void mouseEntered (MouseEvent e) {}
    public void mouseExited  (MouseEvent e) {}
}
