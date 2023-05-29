import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuGame {
  // Instance variables
  private JFrame frame;
  private SudokuPanel sudokuPanel;
  private JComboBox<Difficulty> difficultyComboBox;

  public SudokuGame() {
    // Constructor
    frame = new JFrame("Sudoku Game");
    SudokuBoard board = new SudokuBoard(9);
    sudokuPanel = new SudokuPanel(board);
    difficultyComboBox = new JComboBox<>(Difficulty.values()); // Instantiate the difficultyComboBox
  }

  public void initializeGame() {
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());
    frame.requestFocus();

    // Create buttons
    JButton checkButton = new JButton("Check");
    JButton solveButton = new JButton("Solve");
    JButton generateButton = new JButton("Generate New");

    // Add button listeners

    // Check button
    checkButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Check if current board is correct
        boolean isCorrect = sudokuPanel.getBoard().checkBoard();
        if (isCorrect) {
          JOptionPane.showMessageDialog(frame, "Congratulations! The Sudoku board is correct!", "Check Result",
              JOptionPane.INFORMATION_MESSAGE);
        } else {
          JOptionPane.showMessageDialog(frame, "Oops! The Sudoku board contains errors.", "Check Result",
              JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    // Solve button
    solveButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Solve the Sudoku board
        boolean solved = sudokuPanel.getBoard().solve();
        if (solved) {
          updateBoardView();
          JOptionPane.showMessageDialog(frame, "Sudoku solved!", "Solved", JOptionPane.INFORMATION_MESSAGE);
        } else {
          JOptionPane.showMessageDialog(frame, "Unable to solve the Sudoku!", "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    // Generate new board button
    generateButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Get chosen difficulty
        Difficulty selectedDifficulty = (Difficulty) difficultyComboBox.getSelectedItem();
        SudokuBoard board = new SudokuBoard(9);
        board.generateBoard(selectedDifficulty);
        sudokuPanel.setBoard(board);
        sudokuPanel.repaint();
      }
    });

    // Create a panel for buttons
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(checkButton);
    buttonPanel.add(solveButton);
    buttonPanel.add(generateButton);

    // Create a panel for difficulty selection
    JPanel difficultyPanel = new JPanel();
    difficultyPanel.add(new JLabel("Difficulty: "));
    difficultyPanel.add(difficultyComboBox);

    // Add the Sudoku panel, button panel, and difficulty panel to the frame
    frame.add(sudokuPanel, BorderLayout.CENTER);
    frame.add(buttonPanel, BorderLayout.SOUTH);
    frame.add(difficultyPanel, BorderLayout.NORTH);

    // Set the frame size, pack, and display
    frame.setSize(500, 500);
    frame.pack();
    frame.setVisible(true);

  }

  // Repaint the Sudoku panel to update the view
  private void updateBoardView() {
    sudokuPanel.repaint();
  }

}
