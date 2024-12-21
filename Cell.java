/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #16
 * 1 - 5026231145 - Abrorus Shobah
 * 2 - 5026231076 - James Melvin Chandra
 */

 package sudoku;

 import java.awt.*;
 import javax.swing.*;
 
 public class Cell extends JTextField {
     private static final long serialVersionUID = 1L;
 
     public int row, col;
     public CellStatus status;
 
     public Cell(int row, int col) {
         super();
         this.row = row;
         this.col = col;
         this.status = CellStatus.TO_GUESS;
         setHorizontalAlignment(JTextField.CENTER);
         setFont(new Font("Monospaced", Font.BOLD, 20));
     }
 
     /**
      * Initialize the cell for a new game.
      * @param number The correct number for this cell.
      * @param isGiven True if the cell is pre-filled, false if the user needs to guess.
      */
     public void newGame(int number, boolean isGiven) {
         if (isGiven) {
             setText(String.valueOf(number));
             setEditable(false);
             setBackground(Color.LIGHT_GRAY);
             status = CellStatus.GIVEN;
         } else {
             setText("");
             setEditable(true);
             setBackground(Color.WHITE);
             status = CellStatus.TO_GUESS;
         }
     }
 
     /**
      * Override the paintComponent method to customize cell appearance.
      * @param g The Graphics object to protect.
      */
     @Override
     protected void paintComponent(Graphics g) {
         if (status == CellStatus.CONFLICT) {
             setBackground(Color.RED); // Conflict (collision)
         } else if (status == CellStatus.CORRECT_GUESS) {
             setBackground(Color.GREEN); // Correct input
         } else if (status == CellStatus.WRONG_GUESS) {
             setBackground(Color.PINK); // Invalid input
         } else if (status == CellStatus.GIVEN) {
             setBackground(Color.LIGHT_GRAY); // Pre-filled cell
         } else {
             setBackground(Color.WHITE); // Default for empty cells
         }
         super.paintComponent(g); // Call the parent class's paintComponent method
     }
 }
 