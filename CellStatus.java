/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #16
 * 1 - 5026231145 - Abrorus Shobah
 * 2 - 5026231076 - James Melvin Chandra
 */


 package sudoku;

 public enum CellStatus {
     GIVEN,           // Pre-filled cell
     TO_GUESS,        // Cell that the user needs to fill
     CORRECT_GUESS,   // Correctly filled cell
     WRONG_GUESS,     // Incorrectly filled cell
     CONFLICT         // Cell that causes a conflict
 }
 