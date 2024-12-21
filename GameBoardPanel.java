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
 import java.io.*;
 import javax.sound.sampled.*;
 import javax.swing.*;
 
 public class GameBoardPanel extends JPanel {
     private static final long serialVersionUID = 1L;
 
     public static final int CELL_SIZE = 60; // Cell width/height in pixels
     public static final int BOARD_WIDTH = CELL_SIZE * SudokuConstants.GRID_SIZE;
     public static final int BOARD_HEIGHT = CELL_SIZE * SudokuConstants.GRID_SIZE;
 
     private final Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
     private final Puzzle puzzle = new Puzzle();
     private final JTextField statusBar = new JTextField("Cells remaining: 0"); // Status bar
     private final JPanel sidePanel = new JPanel(); // Side panel for additional options
     private Clip backgroundClip;
     private boolean isMuted = false;
 
     public GameBoardPanel() {
         super.setLayout(new BorderLayout());
 
         JPanel boardPanel = new JPanel(new GridLayout(SudokuConstants.GRID_SIZE, SudokuConstants.GRID_SIZE));
         for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
             for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                 cells[row][col] = new Cell(row, col);
                 boardPanel.add(cells[row][col]);
             }
         }
 
         super.add(boardPanel, BorderLayout.CENTER);
         statusBar.setEditable(false);
         super.add(statusBar, BorderLayout.SOUTH);
 
         setupSidePanel();
         super.add(sidePanel, BorderLayout.EAST);
 
         super.setPreferredSize(new Dimension(BOARD_WIDTH + 200, BOARD_HEIGHT));
 
         playBackgroundSound();
     }
 
     private void setupSidePanel() {
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setPreferredSize(new Dimension(200, BOARD_HEIGHT));
        sidePanel.setBorder(BorderFactory.createTitledBorder("Options"));
    
        JButton btnHint = new JButton("Hint");
        btnHint.addActionListener(e -> provideHint());
    
        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(e -> resetGame());
    
        JButton btnMute = new JButton("Mute/Unmute");
        btnMute.addActionListener(e -> toggleBackgroundSound());
    
        JLabel lblStrategy = new JLabel("Strategy Tips:");
        JTextArea txtStrategy = new JTextArea(10, 15);
        txtStrategy.setText("1. Start with rows and columns with the most clues.\n2. Focus on 3x3 grids.\n3. Use process of elimination.");
        txtStrategy.setWrapStyleWord(true);
        txtStrategy.setLineWrap(true);
        txtStrategy.setEditable(false);
        JScrollPane strategyScroll = new JScrollPane(txtStrategy);
    
        // Load and add image
        JLabel imgLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon("chillguy.jpg"); 
            Image scaledImage = icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            imgLabel.setText("Image not found"); // Fallback if the image cannot be loaded
        }
    
        sidePanel.add(btnHint);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(btnReset);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(btnMute);
        sidePanel.add(Box.createVerticalStrut(20));
        sidePanel.add(lblStrategy);
        sidePanel.add(strategyScroll);
        sidePanel.add(Box.createVerticalStrut(20));
        sidePanel.add(imgLabel); // Add the image to the panel
    }
    
 
     private void playBackgroundSound() {
         try {
             File soundFile = new File("Elevator Music (Kevin MacLeod) - Background Music (HD).wav"); 
             AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
             backgroundClip = AudioSystem.getClip();
             backgroundClip.open(audioStream);
             backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
             backgroundClip.start();
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 
     private void toggleBackgroundSound() {
         if (backgroundClip != null) {
             if (isMuted) {
                 backgroundClip.start();
                 backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
             } else {
                 backgroundClip.stop();
             }
             isMuted = !isMuted;
         }
     }
 
     private void provideHint() {
         // Simple hint logic: Find the first empty cell and fill it with the correct answer
         for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
             for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                 if (!puzzle.isGiven[row][col] && cells[row][col].getText().isEmpty()) {
                     cells[row][col].setText(String.valueOf(puzzle.answers[row][col]));
                     updateStatusBar();
                     return;
                 }
             }
         }
 
         JOptionPane.showMessageDialog(this, "No hints available!", "Hint", JOptionPane.INFORMATION_MESSAGE);
     }
 
     public void newGame(int cellsToGuess) {
         puzzle.newPuzzle(cellsToGuess);
 
         for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
             for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                 cells[row][col].newGame(puzzle.numbers[row][col], puzzle.isGiven[row][col]);
             }
         }
 
         updateStatusBar();
     }
 
     public void resetGame() {
         for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
             for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                 if (!puzzle.isGiven[row][col]) {
                     cells[row][col].setText("");
                     cells[row][col].status = CellStatus.TO_GUESS;
                     cells[row][col].repaint();
                 }
             }
         }
 
         updateStatusBar();
     }
 
     public void checkAnswer() {
         boolean allCorrect = true;
 
         for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
             for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                 if (!puzzle.isGiven[row][col]) {
                     String userInput = cells[row][col].getText().trim();
                     try {
                         int playerInput = Integer.parseInt(userInput);
                         if (playerInput == puzzle.answers[row][col]) {
                             cells[row][col].status = CellStatus.CORRECT_GUESS;
                             playCorrectAnswerSound(); // Play sound for correct answer
                         } else {
                             cells[row][col].status = CellStatus.WRONG_GUESS;
                             allCorrect = false;
                         }
                     } catch (NumberFormatException ex) {
                         cells[row][col].status = CellStatus.WRONG_GUESS;
                         allCorrect = false;
                     }
                     cells[row][col].repaint(); // Trigger repaint for visual updates
                 }
             }
         }
 
         if (allCorrect) {
             JOptionPane.showMessageDialog(this, "Congratulations! All your answers are correct!");
         } else {
             JOptionPane.showMessageDialog(this, "Some answers are incorrect. Keep trying!");
         }
 
         updateStatusBar();
     }
 
     private void playCorrectAnswerSound() {
         try {
             File soundFile = new File("Correct Answer sound effect.wav"); // Replace with your file path
             AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
             Clip clip = AudioSystem.getClip();
             clip.open(audioStream);
             clip.start();
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 
     private void updateStatusBar() {
         int remainingCells = 0;
 
         for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
             for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                 if (!puzzle.isGiven[row][col] && cells[row][col].getText().isEmpty()) {
                     remainingCells++;
                 }
             }
         }
 
         statusBar.setText("Cells remaining: " + remainingCells);
     }
 
     public boolean isCellCorrect(int row, int col) {
         return cells[row][col].status == CellStatus.CORRECT_GUESS;
     }
 }
 