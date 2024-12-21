/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #16
 * 1 - 5026231145 - Abrorus Shobah
 * 2 - 5026231076 - James Melvin Chandra
 */
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

public class SudokuMain extends JFrame {
    private static final long serialVersionUID = 1L;

    private final GameBoardPanel board = new GameBoardPanel();
    private final JLabel timerLabel = new JLabel("Time: 0s");
    private final JLabel scoreLabel = new JLabel("Score: 0");
    private final JLabel playerNameLabel; // Label to display player name
    private final JProgressBar progressBar = new JProgressBar(0, 100);
    private final JButton btnPauseResume = new JButton("Pause");
    private Timer timer;
    private int elapsedTime = 0;
    private int score = 0;
    private boolean isPaused = false;
    private String difficulty = "Medium"; // Default difficulty

    public SudokuMain(String playerName) {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        // Add board
        cp.add(board, BorderLayout.CENTER);

        // Add timer, score, player name, and progress bar
        JPanel statusPanel = new JPanel(new GridLayout(1, 4));
        playerNameLabel = new JLabel("Player: " + playerName, JLabel.CENTER); // Display player name
        statusPanel.add(playerNameLabel);
        statusPanel.add(timerLabel);
        statusPanel.add(scoreLabel);
        statusPanel.add(progressBar);
        cp.add(statusPanel, BorderLayout.NORTH);

        // Add buttons
        JPanel controlPanel = new JPanel();
        JButton btnNewGame = new JButton("New Game");
        JButton btnCheckAnswer = new JButton("Check Answer");

        controlPanel.add(btnNewGame);
        controlPanel.add(btnPauseResume);
        controlPanel.add(btnCheckAnswer);
        cp.add(controlPanel, BorderLayout.SOUTH);

        // Initialize timer
        timer = new Timer(1000, e -> updateTimer());
        timer.start();

        // Button actions
        btnNewGame.addActionListener(e -> startNewGame());
        btnPauseResume.addActionListener(e -> togglePauseResume());
        btnCheckAnswer.addActionListener(e -> board.checkAnswer());

        // Add menu bar for difficulty selection
        setJMenuBar(createMenuBar());

        // Initialize the game
        startNewGame();

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sudoku by T16 - Player: " + playerName); // Include player name in the title
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem newGameItem = new JMenuItem("New Game");
        JMenuItem resetGameItem = new JMenuItem("Reset Game");
        JMenuItem exitItem = new JMenuItem("Exit");

        newGameItem.addActionListener(e -> startNewGame());
        resetGameItem.addActionListener(e -> board.resetGame());
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(newGameItem);
        fileMenu.add(resetGameItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Difficulty menu
        JMenu difficultyMenu = new JMenu("Difficulty");
        JMenuItem easyItem = new JMenuItem("Easy");
        JMenuItem mediumItem = new JMenuItem("Medium");
        JMenuItem hardItem = new JMenuItem("Hard");

        easyItem.addActionListener(e -> difficulty = "Easy");
        mediumItem.addActionListener(e -> difficulty = "Medium");
        hardItem.addActionListener(e -> difficulty = "Hard");

        difficultyMenu.add(easyItem);
        difficultyMenu.add(mediumItem);
        difficultyMenu.add(hardItem);

        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem howToPlayItem = new JMenuItem("How to Play");
        JMenuItem aboutItem = new JMenuItem("About");

        howToPlayItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Sudoku Instructions:\n" +
                        "1. Fill the grid so that every row, column, and 3x3 sub-grid contains the numbers 1-9.\n" +
                        "2. Use logical reasoning to solve the puzzle.",
                "How to Play", JOptionPane.INFORMATION_MESSAGE));

        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Sudoku Game\nVersion 1.0\nDeveloped by Team 16:\n James Melvin Chandra 5026231076\n Abrorus Shobah 5026231145",
                "About", JOptionPane.INFORMATION_MESSAGE));

        helpMenu.add(howToPlayItem);
        helpMenu.add(aboutItem);

        // Add menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(difficultyMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private void startNewGame() {
        int cellsToGuess = getCellsToGuessBasedOnDifficulty();
        board.newGame(cellsToGuess);
        elapsedTime = 0;
        score = 0;
        timer.start();
        isPaused = false;
        btnPauseResume.setText("Pause");
        updateStatus();
    }

    private void togglePauseResume() {
        if (isPaused) {
            timer.start();
            btnPauseResume.setText("Pause");
        } else {
            timer.stop();
            btnPauseResume.setText("Resume");
        }
        isPaused = !isPaused;
    }

    private void updateTimer() {
        elapsedTime++;
        updateStatus();
    }

    private void updateStatus() {
        timerLabel.setText("Time: " + elapsedTime + "s");
        scoreLabel.setText("Score: " + score);
    }

    private int getCellsToGuessBasedOnDifficulty() {
        switch (difficulty) {
            case "Easy":
                return 20; // Few cells to guess
            case "Medium":
                return 40; // Moderate number of cells to guess
            case "Hard":
                return 60; // Many cells to guess
            default:
                return 40; // Default to medium
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SudokuMain("Player"));
    }
}
