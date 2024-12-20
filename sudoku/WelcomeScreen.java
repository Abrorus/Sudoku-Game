/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #16
 * 1 - 5026231145 - Abrorus Shobah
 * 2 - 5026231076 - James Melvin Chandra
 */



package sudoku;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeScreen extends JFrame {
    private static final long serialVersionUID = 1L;

    public WelcomeScreen() {
        // Frame
        setTitle("Welcome to Sudoku");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Welcome to Sudoku!", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Desc
        JLabel description = new JLabel("<html><center>Attempt your best on this Sudoku Game!<br>"
                + "Click 'Start Game' to begin.</center></html>", JLabel.CENTER);
        description.setFont(new Font("Arial", Font.PLAIN, 16));
        add(description, BorderLayout.CENTER);

        // Create Start Game button
        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        add(startButton, BorderLayout.SOUTH);

        // Add action listener to the button
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Launch the Sudoku game
                new SudokuMain(); 
                dispose(); // Close the welcome screen
            }
        });

        setLocationRelativeTo(null); // Center the window
        setVisible(true); // Make the frame visible
    }

    public static void main(String[] args) {
        // Launch the welcome screen
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WelcomeScreen();
            }
        });
    }
}