import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.event.KeyListener;

public class SudokuPanel extends JPanel implements KeyListener {
    private SudokuBoard board;
    private int selectedNumber; // Added selectedNumber variable
    private int selectedRow; // Added selectedRow variable
    private int selectedCol; // Added selectedCol variable

    public SudokuPanel(SudokuBoard board) {
        this.board = board;
        selectedNumber = 0; // Initialize the selectedNumber to 0 (or any default value)
        selectedRow = -1; // Initialize the selectedRow to an invalid value
        selectedCol = -1; // Initialize the selectedCol to an invalid value
        // Add a mouse listener to handle mouse clicks on the panel
      
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });

        // Add the panel itself as the key listener
       addKeyListener(this);

        // Set the panel focusable to receive keyboard input
        setFocusable(true);
    
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int boardSize = board.getSize();
    
        // Update the selected cell based on the arrow key pressed
        if (keyCode == KeyEvent.VK_UP && selectedRow > 0) {
            selectedRow--;
        } else if (keyCode == KeyEvent.VK_DOWN && selectedRow < boardSize - 1) {
            selectedRow++;
        } else if (keyCode == KeyEvent.VK_LEFT && selectedCol > 0) {
            selectedCol--;
        } else if (keyCode == KeyEvent.VK_RIGHT && selectedCol < boardSize - 1) {
            selectedCol++;
        } else {
           handleKeyPress(e.getKeyChar());
        }
    
        // Repaint the panel to update the UI
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Do nothing
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Do nothing
    }

  
    public void setBoard(SudokuBoard board) {
        this.board = board;
    }
  
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int boardSize = board.getSize();
        int blockSize = (int) Math.sqrt(boardSize);
        int cellSize = Math.min(getWidth(), getHeight()) / boardSize; // Calculate the cell size

        // Draw the Sudoku board grid
        g.setColor(Color.BLACK);
        for (int i = 0; i <= boardSize; i++) {
            if (i % blockSize == 0) {
                // Draw thicker lines for the block boundaries
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(2)); // Set the stroke width to 2
        
                // Draw the vertical line
                int x = i * cellSize;
                g2.drawLine(x, 0, x, boardSize * cellSize);
        
                // Draw the horizontal line
                int y = i * cellSize;
                g2.drawLine(0, y, boardSize * cellSize, y);
        
                // Reset the stroke back to the default
                g2.setStroke(new BasicStroke(1));
            } else {
                // Draw the regular lines
                g.drawLine(i * cellSize, 0, i * cellSize, boardSize * cellSize);
                g.drawLine(0, i * cellSize, boardSize * cellSize, i * cellSize);
            }
        }


        // Draw the numbers on the Sudoku board
        Font numberFont = new Font("Arial", Font.BOLD, cellSize / 2);
        g.setFont(numberFont);
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                int number = board.getNumber(row, col);
                if (number != 0) {
                    int x = col * cellSize + cellSize / 4;
                    int y = row * cellSize + cellSize / 2;
                    // Determine the color based on whether the number is original or user-inputted
                    Color numberColor = board.isEditable(row, col) ? Color.BLUE : Color.BLACK;
                    g.setColor(numberColor);
                    g.drawString(Integer.toString(number), x, y);
                }
            }
        }

        // Highlight the selected cell
        if (selectedRow >= 0 && selectedCol >= 0) {
            int x = selectedCol * cellSize;
            int y = selectedRow * cellSize;
    
            g.setColor(Color.RED);
            g.drawRect(x, y, cellSize - 1, cellSize - 1); // Draw a red outline
    
            // Repaint the panel to update the UI
            repaint();
        }
    }

    public SudokuBoard getBoard() {
        return board;
    }
  
    public boolean checkBoard() {
        int boardSize = board.getSize();

        // Check each row for duplicates
        for (int row = 0; row < boardSize; row++) {
            boolean[] used = new boolean[boardSize + 1];
            for (int col = 0; col < boardSize; col++) {
                int number = board.getNumber(row, col);
                if (number != 0) {
                    if (used[number]) {
                        return false; // Duplicate number found in the row
                    }
                    used[number] = true;
                }
            }
        }

        // Check each column for duplicates
        for (int col = 0; col < boardSize; col++) {
            boolean[] used = new boolean[boardSize + 1];
            for (int row = 0; row < boardSize; row++) {
                int number = board.getNumber(row, col);
                if (number != 0) {
                    if (used[number]) {
                        return false; // Duplicate number found in the column
                    }
                    used[number] = true;
                }
            }
        }

        // Check each block for duplicates
        int blockSize = (int) Math.sqrt(boardSize);
        for (int blockRow = 0; blockRow < blockSize; blockRow++) {
            for (int blockCol = 0; blockCol < blockSize; blockCol++) {
                boolean[] used = new boolean[boardSize + 1];
                for (int row = blockRow * blockSize; row < (blockRow + 1) * blockSize; row++) {
                    for (int col = blockCol * blockSize; col < (blockCol + 1) * blockSize; col++) {
                        int number = board.getNumber(row, col);
                        if (number != 0) {
                            if (used[number]) {
                                return false; // Duplicate number found in the block
                            }
                            used[number] = true;
                        }
                    }
                }
            }
        }

        return true; // Board is valid
    }

    public void handleMouseClick(int x, int y) {
        int boardSize = board.getSize();
        int cellSize = getWidth() / boardSize;
        
        // Determine the row and column based on the mouse click coordinates
        int row = y / cellSize;
        int col = x / cellSize;

        // Update the selected cell and number
        selectedRow = row;
        selectedCol = col;

        // Request focus to enable keyboard input
        requestFocus();
      
        // Repaint the panel to update the UI
        repaint();
    }

    public void handleKeyPress(char key) {
        // Check if a valid cell is selected
        if (selectedRow >= 0 && selectedCol >= 0) {
            if (Character.isDigit(key) && board.isEditable(selectedRow, selectedCol)) {
                int number = Character.getNumericValue(key);
                board.setNumber(selectedRow, selectedCol, number);
            }
            // Repaint the panel to update the UI
            repaint();
        }
    }

    public int getSelectedNumber() {
        return selectedNumber;
    }

    public void setNumber(int row, int col, int number) {
        if (board.isEditable(row, col)) {
          board.setNumber(row, col, number);
        }
    }
}
