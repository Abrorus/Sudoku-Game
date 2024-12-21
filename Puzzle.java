/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #16
 * 1 - 5026231145 - Abrorus Shobah
 * 2 - 5026231076 - James Melvin Chandra
 */


 package sudoku;

 import java.util.ArrayList;
 import java.util.List;
 import java.util.Random;
 
 public class Puzzle {
     int[][] numbers = new int[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE]; // Puzzle grid with missing cells
     int[][] answers = new int[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE]; // Complete solution grid
     boolean[][] isGiven = new boolean[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
     List<RemovedCell> removedCells = new ArrayList<>(); // Store removed cells (row, col, value)
 
     public Puzzle() {
         super();
     }
 
     public void newPuzzle(int cellsToGuess) {
         numbers = new int[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
         answers = new int[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
         isGiven = new boolean[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
         removedCells.clear(); // Clear previous removed cells
 
         // Generate a full solution grid
         generateFullGrid();
 
         // Save the complete solution
         for (int row = 0; row < SudokuConstants.GRID_SIZE; row++) {
             for (int col = 0; col < SudokuConstants.GRID_SIZE; col++) {
                 answers[row][col] = numbers[row][col];
             }
         }
 
         // Remove random cells to create the puzzle
         removeCells(cellsToGuess);
     }
 
     private void generateFullGrid() {
         Random rand = new Random();
 
         // Fill diagonal sub-grids
         for (int i = 0; i < SudokuConstants.GRID_SIZE; i += SudokuConstants.SUBGRID_SIZE) {
             fillSubGrid(i, i, rand);
         }
 
         // Solve the rest of the grid using backtracking
         if (!solveGrid(0, 0)) {
             throw new IllegalStateException("Failed to generate a valid Sudoku grid.");
         }
     }
 
     private void fillSubGrid(int rowStart, int colStart, Random rand) {
         boolean[] used = new boolean[SudokuConstants.GRID_SIZE + 1];
 
         for (int r = 0; r < SudokuConstants.SUBGRID_SIZE; r++) {
             for (int c = 0; c < SudokuConstants.SUBGRID_SIZE; c++) {
                 int num;
                 do {
                     num = rand.nextInt(SudokuConstants.GRID_SIZE) + 1;
                 } while (used[num]);
 
                 used[num] = true;
                 numbers[rowStart + r][colStart + c] = num;
             }
         }
     }
 
     private boolean solveGrid(int row, int col) {
         if (row == SudokuConstants.GRID_SIZE) {
             return true; // Solved all rows
         }
 
         int nextRow = col == SudokuConstants.GRID_SIZE - 1 ? row + 1 : row;
         int nextCol = (col + 1) % SudokuConstants.GRID_SIZE;
 
         if (numbers[row][col] != 0) {
             return solveGrid(nextRow, nextCol); // Skip pre-filled cells
         }
 
         for (int num = 1; num <= SudokuConstants.GRID_SIZE; num++) {
             if (isSafeToPlace(row, col, num)) {
                 numbers[row][col] = num;
 
                 if (solveGrid(nextRow, nextCol)) {
                     return true;
                 }
 
                 numbers[row][col] = 0; // Backtrack
             }
         }
 
         return false; // No valid number found, backtrack
     }
 
     private boolean isSafeToPlace(int row, int col, int num) {
         for (int i = 0; i < SudokuConstants.GRID_SIZE; i++) {
             if (numbers[row][i] == num || numbers[i][col] == num) {
                 return false; // Check row and column
             }
         }
 
         int boxRowStart = (row / SudokuConstants.SUBGRID_SIZE) * SudokuConstants.SUBGRID_SIZE;
         int boxColStart = (col / SudokuConstants.SUBGRID_SIZE) * SudokuConstants.SUBGRID_SIZE;
 
         for (int r = 0; r < SudokuConstants.SUBGRID_SIZE; r++) {
             for (int c = 0; c < SudokuConstants.SUBGRID_SIZE; c++) {
                 if (numbers[boxRowStart + r][boxColStart + c] == num) {
                     return false; // Check sub-grid
                 }
             }
         }
 
         return true; // Safe to place
     }
 
     private void removeCells(int cellsToGuess) {
         Random rand = new Random();
         int removed = 0;
 
         while (removed < cellsToGuess) {
             int row = rand.nextInt(SudokuConstants.GRID_SIZE);
             int col = rand.nextInt(SudokuConstants.GRID_SIZE);
 
             if (numbers[row][col] != 0) { // Remove only non-empty cells
                 removedCells.add(new RemovedCell(row, col, numbers[row][col])); // Store removed cell
                 numbers[row][col] = 0;
                 removed++;
             }
         }
 
         // Set remaining cells as "given"
         for (int row = 0; row < SudokuConstants.GRID_SIZE; row++) {
             for (int col = 0; col < SudokuConstants.GRID_SIZE; col++) {
                 isGiven[row][col] = numbers[row][col] != 0;
             }
         }
     }
 
     public boolean validateInput(int row, int col, int input) {
         // Check if the input matches the stored value for the removed cell
         for (RemovedCell cell : removedCells) {
             if (cell.row == row && cell.col == col) {
                 return cell.value == input;
             }
         }
         return false; // Input does not match
     }
 
     private static class RemovedCell {
         int row, col, value;
 
         RemovedCell(int row, int col, int value) {
             this.row = row;
             this.col = col;
             this.value = value;
         }
     }
 }
 