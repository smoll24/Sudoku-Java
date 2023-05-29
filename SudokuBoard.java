import java.util.Random;

public class SudokuBoard {
    private int[][] board;
    private int[][] solution;
    private int size = 9;
    private int[][] originalBoard;


    public SudokuBoard(int size) {
        this.size = size;
        board = new int[size][size];
        solution = new int[size][size];
        originalBoard = new int[size][size];
    }


    public int getSize() {
        return size;
    }

    public void generateBoard(Difficulty difficulty) {
        clearBoard();
        solve(); // Generate a complete solution

        Random random = new Random();
        int numToRemove = 0;
        switch (difficulty) {
            case EASY:
                numToRemove = 40; // Adjust the number of cells to remove for each difficulty
                break;
            case MEDIUM:
                numToRemove = 55;
                break;
            case HARD:
                numToRemove = 65;
                break;
        }

        // Randomly remove numbers from the solution to create the puzzle
        for (int i = 0; i < numToRemove; i++) {
            int row = random.nextInt(size);
            int col = random.nextInt(size);

            if (board[row][col] != 0) {
                board[row][col] = 0;
            } else {
                i--;
            }
        }

        // Copy the generated numbers to the original board
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                originalBoard[row][col] = board[row][col];
            }
        }
    }

    private void clearBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = 0;
                solution[i][j] = 0;
            }
        }
    }

    public boolean isFilled() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col] == 0) {
                    return false; // Found an empty cell
                }
            }
        }
        return true; // All cells are filled
    }

    public boolean isCorrect() {
        // Check rows
        for (int row = 0; row < size; row++) {
            if (!isValidGroup(row, 0, 0, 1)) {
                return false;
            }
        }
    
        // Check columns
        for (int col = 0; col < size; col++) {
            if (!isValidGroup(0, col, 1, 0)) {
                return false;
            }
        }
    
        // Check blocks
        int blockSize = (int) Math.sqrt(size);
        for (int blockRow = 0; blockRow < blockSize; blockRow++) {
            for (int blockCol = 0; blockCol < blockSize; blockCol++) {
                int row = blockRow * blockSize;
                int col = blockCol * blockSize;
                if (!isValidGroup(row, col, 1, 1)) {
                    return false;
                }
            }
        }
    
        return true; // The board is correct
    }
    
    private boolean isValidGroup(int startRow, int startCol, int rowIncrement, int colIncrement) {
        boolean[] seen = new boolean[size + 1]; // Array to keep track of seen numbers
    
        for (int i = 0; i < size; i++) {
            int num = board[startRow][startCol];
            if (num != 0) {
                if (seen[num]) {
                    return false; // Duplicate number found
                }
                seen[num] = true;
            }
            startRow += rowIncrement;
            startCol += colIncrement;
        }
    
        return true; // The group is valid
    }


    public boolean solve() {
        return solveHelper(0, 0);
    }

    private boolean solveHelper(int row, int col) {
        // Base case: if the entire board is filled, return true
        if (row == size) {
            return true;
        }

        // Calculate the next row and column indices
        int nextRow = col == size - 1 ? row + 1 : row;
        int nextCol = (col + 1) % size;

        // Skip cells that are already filled
        if (board[row][col] != 0) {
            return solveHelper(nextRow, nextCol);
        }

        // Try placing numbers from 1 to 9 and check if it is valid
        for (int num = 1; num <= 9; num++) {
            if (isValid(row, col, num)) {
                board[row][col] = num; // Place the number

                if (solveHelper(nextRow, nextCol)) {
                    return true; // Successfully solved the Sudoku
                }

                board[row][col] = 0; // Clear the number (backtrack)
            }
        }

        return false; // Unable to solve the Sudoku
    }

    public boolean isEditable(int row, int col) {
        return originalBoard[row][col] == 0;
    }


    public boolean isValid(int row, int col, int num) {
        // Check if placing a number at a specific position is valid

        // Check if the number already exists in the row
        for (int c = 0; c < size; c++) {
            if (board[row][c] == num) {
                return false;
            }
        }

        // Check if the number already exists in the column
        for (int r = 0; r < size; r++) {
            if (board[r][col] == num) {
                return false;
            }
        }

        // Check if the number already exists in the 3x3 block
        int blockStartRow = (row / 3) * 3;
        int blockStartCol = (col / 3) * 3;
        for (int r = blockStartRow; r < blockStartRow + 3; r++) {
            for (int c = blockStartCol; c < blockStartCol + 3; c++) {
                if (board[r][c] == num) {
                    return false;
                }
            }
        }

        return true; // The number can be placed at the given position
    }

     public void placeNumber(int row, int col, int num) {
        if (isValid(row, col, num)) {
            board[row][col] = num;
        }
    }

   public void setNumber(int row, int col, int num) {
        board[row][col] = num;
    }

    public void clearNumber(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IllegalArgumentException("Invalid row or column index");
        }

        if (board[row][col] != 0) {
            board[row][col] = 0;
        } else {
            throw new IllegalStateException("The specified cell is already empty");
        }
    }

    public int getNumber(int row, int col) {
        if (isValidPosition(row, col)) {
            return board[row][col];
        } else {
            throw new IllegalArgumentException("Invalid position: (" + row + ", " + col + ")");
        }
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }
}
