import java.util.Random;

public class SudokuBoard {
  // Instance variables
  private int[][] board;
  private int[][] solution;
  private int size = 9;
  private int[][] originalBoard;

  // Constructor
  public SudokuBoard(int size) {
    this.size = size;
    board = new int[size][size];
    solution = new int[size][size];
    originalBoard = new int[size][size];
  }

  // Size instance variable getter
  public int getSize() {
    return size;
  }

  // Generate a new board based on the chosen difficulty
  public void generateBoard(Difficulty difficulty) {
    clearBoard();
    solve(); // Generate a complete solution

    // Adjust the number of cells to remove depending on the difficulty
    Random random = new Random();
    int numToRemove = 0;
    switch (difficulty) {
      case EASY:
        numToRemove = 40;
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

  // Clear the board
  private void clearBoard() {
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        board[i][j] = 0;
        solution[i][j] = 0;
      }
    }
  }

  // Check if the board is completely filled
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

  public boolean checkBoard() {
    // Check each row for duplicates
    for (int row = 0; row < size; row++) {
      boolean[] used = new boolean[size + 1];
      for (int col = 0; col < size; col++) {
        int number = getNumber(row, col);
        if (number != 0) {
          if (used[number]) {
            return false; // Duplicate number found in the row
          }
          used[number] = true;
        }
      }
    }
    // Check each column for duplicates
    for (int col = 0; col < size; col++) {
      boolean[] used = new boolean[size + 1];
      for (int row = 0; row < size; row++) {
        int number = getNumber(row, col);
        if (number != 0) {
          if (used[number]) {
            return false; // Duplicate number found in the column
          }
          used[number] = true;
        }
      }
    }
    // Check each block for duplicates
    int blockSize = (int) Math.sqrt(size);
    for (int blockRow = 0; blockRow < blockSize; blockRow++) {
      for (int blockCol = 0; blockCol < blockSize; blockCol++) {
        boolean[] used = new boolean[size + 1];
        for (int row = blockRow * blockSize; row < (blockRow + 1) * blockSize; row++) {
          for (int col = blockCol * blockSize; col < (blockCol + 1) * blockSize; col++) {
            int number = getNumber(row, col);
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
    return true; // The board is correct
  }

  // Solve the board
  public boolean solve() {
    return solveHelper(0, 0);
  }

  // Helper method for solve() using backtracking algorithm
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

  // Check if cell is editable and not part of original bard
  public boolean isEditable(int row, int col) {
    return originalBoard[row][col] == 0;
  }

  // Check if placing a number at a specific position is valid
  public boolean isValid(int row, int col, int num) {
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

  // Place number on the board if valid
  public void placeNumber(int row, int col, int num) {
    if (isValid(row, col, num)) {
      board[row][col] = num;
    }
  }

  // Place number on the board, even if not valid
  public void setNumber(int row, int col, int num) {
    board[row][col] = num;
  }

  // Get the number at a specific position on the board
  public int getNumber(int row, int col) {
    if (isValidPosition(row, col)) {
      return board[row][col];
    } else {
      throw new IllegalArgumentException("Invalid position: (" + row + ", " + col + ")");
    }
  }

  // Check if position is inside board
  private boolean isValidPosition(int row, int col) {
    return row >= 0 && row < size && col >= 0 && col < size;
  }
}
